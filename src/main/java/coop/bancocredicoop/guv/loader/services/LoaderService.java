package coop.bancocredicoop.guv.loader.services;

import coop.bancocredicoop.guv.loader.models.GUVConfig;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCheque;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.ConfigRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionImporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class LoaderService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);
    private Integer pageSize, percentageSize, actualTotal = 0;

    @Autowired
    ChequeRepository db;

    @Autowired
    CorreccionImporteRepository mongo;

    @Autowired
    ConfigRepository config;

    void load(){
        if(moreChequesAreNeeded()){
            log.info("Cargando nuevos cheques");
            doLoad();
        }
        else
            log.info("No se requieren más cheques.");
    }

    private void doLoad(){
        Flux<CorreccionCheque> chequeFlux = retrieveFromDatabase();
        storeInMongo(chequeFlux).subscribe();
    }

    //TODO: Revisar si se debe ir a la base por cada carga para cargar configuracion.
    private void loadSizing() {
        String sizeConfig = config.getValueOf(GUVConfig.LOADER_PAGE_SIZE);
        try {
            pageSize = Integer.parseInt(sizeConfig);
        } catch (NumberFormatException nfe) {
            log.error("Valor de tamaño inválido: " + sizeConfig, nfe);
        }

        String percentageConfig = config.getValueOf(GUVConfig.LOADER_MINIMUM_PERCENTAGE);
        try {
            percentageSize = Integer.parseInt(percentageConfig);
        } catch (NumberFormatException nfe) {
            log.error("Valor de porcentaje inválido: " + percentageConfig, nfe);
        }
    }

    /**
     *  Verifica si se necesita cargar mas cheques.
     *
     *  100% ------ pageSize
     *   X   ------ total
     *
     *   X <= percentageSize
     *
     * @return Boolean
     */
    private Boolean moreChequesAreNeeded(){
        loadSizing();
        return mongo.count()
                .map(total -> {
                        actualTotal = total.intValue();
                        return total == 0L || (100 * total / pageSize) <= percentageSize;
                        })
                .block();
    }

    private Pageable getPage() {
        //FIXME: ora-01795 - maximum number of expressions in a list is 1000
        return PageRequest.of(0, pageSize - actualTotal);
    }

    private Flux<CorreccionCheque> storeInMongo(Flux<CorreccionCheque> chequesFlux) {
        log.debug("Storing FLUX in MongoDB");
        return chequesFlux
                .flatMap(mongo::save)
                .doOnError(e -> log.error("Failed to store cheques", e));

    }

    private Flux<CorreccionCheque> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return mongo.findAll()
                .doOnError(e -> log.error("Failed to retrieve cheques from MongoDB", e))
                .retry(3)
                .map(CorreccionCheque::getId)
                .defaultIfEmpty(0L)
                .collectList()
                .map(idCheques ->
                        db.findByEstadoAndImporteCeroAndIdNotIn(
                                EstadoCheque.VALIDAR_CMC7,
                                idCheques,
                                getPage()))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
