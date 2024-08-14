package se.nrm.dina.loan.web;
 
import lombok.extern.slf4j.Slf4j; 
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import se.nrm.dina.loan.web.logic.InitialData;

/**
 *
 * @author idali
 */
@Slf4j
@ApplicationScoped
public class Startup {
      
    @Inject
    private InitialData initData;
    
    
    public Startup() {

    }
 
    void init(@Observes @Initialized(ApplicationScoped.class) Object event) {
        log.info("StartupBean Application - INITIALIZATION");
        
        initData.process();

//        CompletableFuture.runAsync(() -> {
////            process.run();
//            log.info("app processing....");
//
//        });
    }
    
}
