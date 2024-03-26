package se.nrm.dina.mongodb.jdbc;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

/**
 *
 * @author idali
 */
@ApplicationScoped
@Slf4j
public class InitialConfig implements Serializable {
    
    private final static String CONFIG_INITIALLISING_ERROR = "Property not initialized";
    
    private String mongoPath;
    
    public InitialConfig() {
    }
    
    @Inject
    public InitialConfig(@ConfigurationValue("swarm.mongo.host") String mongoPath) {
 
        log.info("InitialConfig : {}", mongoPath);
    }
    
    public String getMongoPath() {
        if (mongoPath == null) {
            throw new RuntimeException(CONFIG_INITIALLISING_ERROR);
        }
        return mongoPath;
    } 
}
