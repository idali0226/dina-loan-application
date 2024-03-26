package se.nrm.dina.manager.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;  
import javax.ejb.Stateless;
import javax.persistence.EntityManager;  
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils; 
import se.nrm.dina.manager.entities.TblGroups;
import se.nrm.dina.manager.entities.TblUsers;
import se.nrm.dina.manager.exceptions.ManagerException;

/**
 *
 * @author idali
 */
@Stateless
@Slf4j
public class AccountDao implements Serializable {
    
    private final String validateUsernameNamedQuery = "TblUsers.validateUsername";
    private final String findByUsernameNamedQuery = "TblUsers.findByUsername";
    
    private final String findByEmailNamedQuery = "TblUsers.findByEmail";
    
    private final String jpql = "SELECT Count(u) FROM TblUsers u JOIN u.tblGroupsList g WHERE g.username = u.username AND g.groupname IN (:group_params) AND u.email = :email";
    
    private final String queryUsernameParamKey = "username";
    private final String queryGroupParamKey = "group_params";
    private final String queryEmailParamKey = "email";
    
    
    private final String usernameKey = "Username: ";
    private final String usernameNotFoundError = " not found.";
    
    
      
    List<String> excludeGroups = new ArrayList();
    List<String> loanGroup = new ArrayList();
    
    @PersistenceContext(unitName = "userPU")                  //  persistence unit connect to test database
    private EntityManager entityManager;
    
    public AccountDao() {
        excludeGroups = new ArrayList<>();
        excludeGroups.add("inventory");
        excludeGroups.add("superuser");
        excludeGroups.add("vegadare");
        
        loanGroup = new ArrayList();
        loanGroup.add("user");
        loanGroup.add("admin");
        loanGroup.add("manager"); 
    }
    
    public void delete(TblUsers user) {
        log.info("delete: {}", user);

        TblUsers u = findByUserName(user.getUsername()); 
        try {
            entityManager.remove(u); 
            entityManager.flush();                              // this is needed for throwing internal exception
        } catch (ConstraintViolationException e) { 
            log.error(e.getMessage());
        } catch (Exception e) { 
            log.error(e.getMessage());
        }
    }
     
    public TblUsers findByUserName(String username) {
        log.info("findByUserName");
        
        Query query = entityManager.createNamedQuery(findByUsernameNamedQuery); 
        query.setParameter(queryUsernameParamKey, username);
         
        try {
            return (TblUsers)query.getSingleResult();
        } catch (javax.persistence.NoResultException | javax.persistence.NonUniqueResultException ex) {
            throw new ManagerException(usernameKey + username + usernameNotFoundError); 
        } 
    }
    
    public boolean validateUserName(String username) {  
        Query query = entityManager.createNamedQuery(validateUsernameNamedQuery);

        query.setParameter(queryUsernameParamKey, username);
        Number number = (Number) query.getSingleResult();  
        return number.intValue() < 1; 
    }
      
    public boolean validateEmail(String email) {    
        Query query = entityManager.createQuery(jpql); 
        query.setParameter(queryGroupParamKey, loanGroup); 
        query.setParameter(queryEmailParamKey, email);
        Number number = (Number) query.getSingleResult(); 
        log.info("number : {}", number);
        return number.intValue() < 1;   
    }
    
    public TblUsers createAccount(TblUsers user) {
        log.info("createAccount: {}", user);
        TblUsers tmp = user;
        try {
            entityManager.persist(user);
            entityManager.flush();  
        } catch (ConstraintViolationException e) { 
            log.error(e.getMessage());
        } catch (javax.persistence.PersistenceException ex) { 
            log.error("PersistenceException : {}", ex.getMessage());
            if (ex.getCause() instanceof  org.hibernate.exception.ConstraintViolationException) {  
                org.hibernate.exception.ConstraintViolationException e = (org.hibernate.exception.ConstraintViolationException) ex.getCause();
                throw new ManagerException(handleHibernateConstraintViolation(e), 400); 
            }
        } catch (Exception e) { 
            log.error(e.getMessage());
        }
        return tmp;
    }
    
    public TblUsers findOneUserByEmail(String email) {
        log.info("findOneUserByEmail : {}", email);
        
        log.info("entityManager : {}", entityManager);
        
        Query query = entityManager.createNamedQuery(findByEmailNamedQuery);
        query.setParameter(queryEmailParamKey, email);
        List<TblUsers> users = query.getResultList();
        
        if(users != null && !users.isEmpty()) {
            return users.get(0);
        } 
        return null;
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public List<String> findAllCuratorsEmailList() {
        String strSql = "SELECT u.email FROM TblUsers u JOIN u.tblGroupsList g WHERE g.username = u.username AND g.groupname NOT IN (:group_params) ";
         
        Query query = entityManager.createQuery(strSql);
        query.setParameter("group_params", excludeGroups); 
        return query.getResultList();
    }
    


    private String handleHibernateConstraintViolation(org.hibernate.exception.ConstraintViolationException e) {
        return getRootCause(e).getMessage();
    }

    private Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.size() < 2 ? null : (Throwable) list.get(list.size() - 1);
    }

    private List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<>();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = ExceptionUtils.getCause(throwable);
        }
        return list;
    }


    public TblUsers mergeAccount(TblUsers user) {
        log.info("mergeAccount: {}", user);

        TblUsers tmp = user;
        try { 
            tmp = entityManager.merge(user); 
            entityManager.flush();     
        } catch (OptimisticLockException | ConstraintViolationException e) { 
            throw new ManagerException("Failed to update user account: " + e.getMessage()); 
        } catch (Exception e) {  
            throw new ManagerException("Failed to update user account: " + e.getMessage()); 
        }  
        return tmp;
    }
    

    public List<TblGroups> findAll() {
        log.info("findAll");
        
        Query query = entityManager.createNamedQuery("TblGroups.findAll");
         
        return query.getResultList();
    }
    
    public List<TblGroups> findGroupByNamedQuery(String namedQuery, List<String> groups) {
        log.info("findGroupByNamedQuery : {}", namedQuery);
        
        Query query = entityManager.createNamedQuery(namedQuery); 
        query.setParameter("groupnames", groups);
         
        return query.getResultList();
    }

    
    public List<TblUsers> findByEmail(String email) {
        log.info("findByEmail");
        
        Query query = entityManager.createNamedQuery("TblUsers.findByEmail");
        query.setParameter("email", email);
 
        return query.getResultList();
    }
    



//
//    @PreDestroy
//    public void destroyBean() {
//        logger.info("The bean is being destroyed now, be careful!!!");
//        entityManager.close();
//    }
}
