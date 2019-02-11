package coop.bancocredicoop.guv.loader.services.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FechaLoaderJob {

    private static Logger log = LoggerFactory.getLogger(FechaLoaderJob.class);

    @Autowired
    private LoaderService service;

    @Scheduled(cron="45 * * * * *")
    public void reportCurrentTime() {
        String taskId = UUID.randomUUID().toString();
        log.info("Running loader FECHA task: {}", taskId);
        service.loadFecha();
        log.info("Finish loader FECHA task: {}", taskId);
    }
}
