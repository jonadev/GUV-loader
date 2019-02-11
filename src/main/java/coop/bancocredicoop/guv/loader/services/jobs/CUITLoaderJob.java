package coop.bancocredicoop.guv.loader.services.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CUITLoaderJob {

    private static Logger log = LoggerFactory.getLogger(CUITLoaderJob.class);

    @Autowired
    private LoaderService service;

    @Scheduled(cron="30 * * * * *")
    public void reportCurrentTime() {
        String taskId = UUID.randomUUID().toString();
        log.info("Running loader CUIT task: {}", taskId);
        service.loadCUIT();
        log.info("Finish loader CUIT task: {}", taskId);
    }
}
