package coop.bancocredicoop.guv.loader.services.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CMC7LoaderJob {

    private static Logger log = LoggerFactory.getLogger(CMC7LoaderJob.class);

    @Autowired
    private LoaderService service;

    @Scheduled(cron="15 * * * * *")
    public void reportCurrentTime() {
        String taskId = UUID.randomUUID().toString();
        log.info("Running loader CMC7 task: {}", taskId);
        service.loadCMC7();
        log.info("Finish loader CMC7 task: {}", taskId);
    }
}
