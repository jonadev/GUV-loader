package coop.bancocredicoop.guv.loader.services.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImporteLoaderJob {

    private static Logger log = LoggerFactory.getLogger(ImporteLoaderJob.class);

    @Autowired
    private LoaderService service;

    @Scheduled(cron="0 * * * * *")
    public void reportCurrentTime() {
        String taskId = UUID.randomUUID().toString();
        log.info("Running IMPORTE loader task: {}", taskId);
        service.loadImporte();
        log.info("Finish IMPORTE loader task: {}", taskId);
    }
}
