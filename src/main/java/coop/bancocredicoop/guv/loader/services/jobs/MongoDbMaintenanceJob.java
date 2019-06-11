package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.repositories.mongo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Componente que se encarga de limpiar las colecciones de mongo para correccion.
 */
@Component
public class MongoDbMaintenanceJob {

    private static Logger LOGGER = LoggerFactory.getLogger(ImporteLoaderJob.class);

    @Autowired
    private CorreccionCMC7Repository correccionCMC7Repository;

    @Autowired
    private CorreccionCUITRepository correccionCUITRepository;

    @Autowired
    private CorreccionFechaRepository correccionFechaRepository;

    @Autowired
    private CorreccionImporteRepository correccionImporteRepository;

    @Scheduled(cron="0 0 0 * * *")
    public void cleanCollections() {
        LOGGER.info("Initializing collections");
        this.correccionCMC7Repository.deleteAll();
        this.correccionCUITRepository.deleteAll();
        this.correccionFechaRepository.deleteAll();
        this.correccionImporteRepository.deleteAll();
        LOGGER.info("Finishing collection initialization");
    }

}
