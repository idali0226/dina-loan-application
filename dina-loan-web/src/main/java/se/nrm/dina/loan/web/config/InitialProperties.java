package se.nrm.dina.loan.web.config;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue; 
import se.nrm.dina.loan.web.util.Department;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialProperties implements Serializable {

    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";

    private String scientificPolicy;
    private String educationalPolicy;
    private String supportMail;
    private String supportPhone;
    private String sbdiUrl;
    private String solrUrl;
    private String solrUsername;
    private String solrPassword;

    private String loanFilePath;
    private String tempFilePath;

    private String esbUrlen;
    private String esbUrlse;
    private String botanyUrlen;
    private String botanyUrlse;
    private String mineralogyUrlen;
    private String mineralogyUrlse;
    private String palaebotanyUrlen;
    private String palaebotanyUrlse;
    
    private String host;
    private String admin;

    public InitialProperties() {
    }

    @Inject
    public InitialProperties(@ConfigurationValue("swarm.loan.policies.scientific") String scientificPolicy,
            @ConfigurationValue("swarm.loan.policies.educational") String educationalPolicy,
            @ConfigurationValue("swarm.sbdi") String sbdiUrl,
            @ConfigurationValue("swarm.solr.path") String solrUrl,
            @ConfigurationValue("swarm.solr.username") String solrUsername,
            @ConfigurationValue("swarm.solr.password") String solrPassword,
            @ConfigurationValue("swarm.support.mail") String supportMail,
            @ConfigurationValue("swarm.support.phone") String supportPhone,
            @ConfigurationValue("swarm.loan.file.loan") String loanFilePath,
            @ConfigurationValue("swarm.loan.file.temp") String tempFilePath,
            @ConfigurationValue("swarm.collectionUrl.esb.en") String esbUrlen,
            @ConfigurationValue("swarm.collectionUrl.esb.se") String esbUrlse,
            @ConfigurationValue("swarm.collectionUrl.botany.en") String botanyUrlen,
            @ConfigurationValue("swarm.collectionUrl.botany.se") String botanyUrlse,
            @ConfigurationValue("swarm.collectionUrl.mineralogyurl.en") String mineralogyUrlen,
            @ConfigurationValue("swarm.collectionUrl.mineralogyurl.se") String mineralogyUrlse,
            @ConfigurationValue("swarm.collectionUrl.palaebotanyurl.en") String palaebotanyUrlen,
            @ConfigurationValue("swarm.collectionUrl.palaebotanyurl.se") String palaebotanyUrlse,
            @ConfigurationValue("swarm.host") String host,
            @ConfigurationValue("swarm.admin") String admin) {
        this.scientificPolicy = scientificPolicy;
        this.educationalPolicy = educationalPolicy;
        this.sbdiUrl = sbdiUrl;
        this.solrUrl = solrUrl;
        this.solrUsername = solrUsername;
        this.solrPassword = solrPassword;
        this.supportMail = supportMail;
        this.supportPhone = supportPhone;
        this.loanFilePath = loanFilePath;
        this.tempFilePath = tempFilePath;
        this.esbUrlen = esbUrlen;
        this.esbUrlse = esbUrlse;
        this.botanyUrlen = botanyUrlen;
        this.botanyUrlse = botanyUrlse;
        this.mineralogyUrlen = mineralogyUrlen;
        this.mineralogyUrlse = mineralogyUrlse;
        this.palaebotanyUrlen = palaebotanyUrlen;
        this.palaebotanyUrlse = palaebotanyUrlse;
        this.host = host;
        this.admin = admin;
        
        log.info("InitialProperties : {}", solrUsername);
    }

    public String getScientificPolicyPath() {
        if (scientificPolicy == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return scientificPolicy;
    }

    public String getLoanFilePath() {
        if (loanFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return loanFilePath;
    }

    public String getTempFilePath() {
        if (tempFilePath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return tempFilePath;
    }

    public String getEducationalPolicyPath() {
        if (educationalPolicy == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return educationalPolicy;
    }

    public String getSbdiUrl() {
        if (sbdiUrl == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return sbdiUrl;
    }

    public String getSolrUrl() {
        if (solrUrl == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrUrl;
    }

    public String getSolrUsername() {
        if (solrUsername == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrUsername;
    }

    public String getSolrPassword() {
        if (solrPassword == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return solrPassword;
    }

    public String getSupportMail() {
        if (supportMail == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return supportMail;
    }

    public String getSupportPhone() {
        if (supportPhone == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return supportPhone;
    }
    
    public String getHost() {
        if (host == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return host;
    }
    
    public String getAdmin() {
        if (admin == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return admin;
    }

    public String getEsbUrlen() {
        if (esbUrlen == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return esbUrlen;
    }

    public String getEsbUrlse() {
        if (esbUrlse == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return esbUrlse;
    }

    public String getBotnayUrlen() {
        if (botanyUrlen == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return botanyUrlen;
    }

    public String getBotnayUrlse() {
        if (botanyUrlse == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return botanyUrlse;
    }

    public String getMineralogyUrlen() {
        if (mineralogyUrlen == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mineralogyUrlen;
    }

    public String getMineralogyUrlse() {
        if (mineralogyUrlse == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mineralogyUrlse;
    }

    public String getPalaeobotanyUrlen() {
        if (palaebotanyUrlen == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return palaebotanyUrlen;
    }

    public String getPalaeobotanyUrlse() {
        if (palaebotanyUrlse == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return palaebotanyUrlse;
    }
    
    
    
    public String getDepartmentUrl(String department, boolean isSwedish) { 
        
        if(Department.Botany.isSelectedDepartment(department)) { 
            return isSwedish ? botanyUrlse : botanyUrlen;
        } else if(Department.Geology.isSelectedDepartment(department)) {
            return isSwedish ? mineralogyUrlse : mineralogyUrlen;
        } else if(Department.Palaeobiology.isSelectedDepartment(department)) {
            return isSwedish ? palaebotanyUrlse : palaebotanyUrlen;
        } 
        return isSwedish ? getEsbUrlse() : getEsbUrlen();
    }
}
