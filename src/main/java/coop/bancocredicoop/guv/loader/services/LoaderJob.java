package coop.bancocredicoop.guv.loader.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LoaderJob {

    private static Logger log = LoggerFactory.getLogger(LoaderJob.class);

    @Autowired
    private LoaderService service;

    @Scheduled(cron="0 * * * * *")
    public void reportCurrentTime() {
        String taskId = UUID.randomUUID().toString();
        log.info("Running loader task: {}", taskId);
        service.load();
        log.info("Finish loader task: {}", taskId);
    }
}
