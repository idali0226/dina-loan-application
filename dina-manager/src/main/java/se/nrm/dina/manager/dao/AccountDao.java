/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.nrm.dina.manager.entities.TblGroups;
import se.nrm.dina.manager.entities.TblUsers;
import se.nrm.dina.manager.exceptions.ManagerException;

/**
 *
 * @author idali
 */
@Stateless
public class AccountDao implements Serializable {
    
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    List<String> excludeGroups = new ArrayList();
    
    @PersistenceContext(unitName = "userPU")                  //  persistence unit connect to test database
    private EntityManager entityManager;
    
    public AccountDao() {
        excludeGroups = new ArrayList<>();
        excludeGroups.add("inventory");
        excludeGroups.add("superuser");
        excludeGroups.add("vegadare");
    }
    
    public List<String> findAllCuratorsEmailList() {
        String strSql = "SELECT u.email FROM TblUsers u JOIN u.tblGroupsList g WHERE g.username = u.username AND g.groupname NOT IN (:group_params) ";
         
        Query query = entityManager.createQuery(strSql);
        query.setParameter("group_params", excludeGroups); 
        return query.getResultList();
    }
    
    public TblUsers createAccount(TblUsers user) {
        logger.info("createAccount: {}", user);
        TblUsers tmp = user;
        try {
            entityManager.persist(user);
            entityManager.flush();  
        } catch (ConstraintViolationException e) { 
            logger.error(e.getMessage());
        } catch (javax.persistence.PersistenceException ex) { 
            logger.error("PersistenceException : {}", ex.getMessage());
            if (ex.getCause() instanceof  org.hibernate.exception.ConstraintViolationException) {  
                org.hibernate.exception.ConstraintViolationException e = (org.hibernate.exception.ConstraintViolationException) ex.getCause();
                throw new ManagerException(handleHibernateConstraintViolation(e), 400); 
            }
        } catch (Exception e) { 
            logger.error(e.getMessage());
        }
        return tmp;
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
        logger.info("mergeAccount: {}", user);

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
    
    public void delete(TblUsers user) {
        logger.info("delete: {}", user);

        TblUsers u = findByUserName(user.getUsername()); 
        try {
            entityManager.remove(u); 
            entityManager.flush();                              // this is needed for throwing internal exception
             
        } catch (ConstraintViolationException e) { 
            logger.error(e.getMessage());
        } catch (Exception e) { 
            logger.error(e.getMessage());
        }
    }
    
    public List<TblGroups> findAll() {
        logger.info("findAll");
        
        Query query = entityManager.createNamedQuery("TblGroups.findAll");
         
        return query.getResultList();
    }
    
    public List<TblGroups> findGroupByNamedQuery(String namedQuery, List<String> groups) {
        logger.info("findGroupByNamedQuery : {}", namedQuery);
        
        Query query = entityManager.createNamedQuery(namedQuery); 
        query.setParameter("groupnames", groups);
         
        return query.getResultList();
    }
     
    public TblUsers findByUserName(String username) {
        logger.info("findByUserName");
        
        Query query = entityManager.createNamedQuery("TblUsers.findByUsername"); 
        query.setParameter("username", username);
         
        try {
            return (TblUsers)query.getSingleResult();
        } catch (javax.persistence.NoResultException | javax.persistence.NonUniqueResultException ex) {
            throw new ManagerException("Username: " + username + " not found."); 
        } 
    }
    
    public List<TblUsers> findByEmail(String email) {
        logger.info("findByEmail");
        
        Query query = entityManager.createNamedQuery("TblUsers.findByEmail");
        query.setParameter("email", email);
 
        return query.getResultList();
    }
    
    public TblUsers findOneUserByEmail(String email) {
        logger.info("findOneUserByEmail : {}", email);
        
        Query query = entityManager.createNamedQuery("TblUsers.findByEmail");
        query.setParameter("email", email);
        List<TblUsers> users = query.getResultList();
        
        if(users != null && !users.isEmpty()) {
            return users.get(0);
        } 
        return null;
    }
    
    public boolean validateEmail(String email) { 
        Query query = entityManager.createNamedQuery("TblUsers.validateEmail");
        
        query.setParameter("email", email);
        Number number = (Number) query.getSingleResult(); 
        return number.intValue() < 1;  
    }

    public boolean validateUserName(String username) {  
        Query query = entityManager.createNamedQuery("TblUsers.validateUsername");

        query.setParameter("username", username);
        Number number = (Number) query.getSingleResult();  
        return number.intValue() < 1; 
    }
//
//    @PreDestroy
//    public void destroyBean() {
//        logger.info("The bean is being destroyed now, be careful!!!");
//        entityManager.close();
//    }
}
