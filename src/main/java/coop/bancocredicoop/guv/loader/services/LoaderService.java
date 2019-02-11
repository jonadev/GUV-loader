package coop.bancocredicoop.guv.loader.services;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCheque;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionImporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
public class LoaderService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ChequeRepository db;

    @Autowired
    private CorreccionImporteRepository mongo;

    @Autowired
    private Environment env;

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

    private Boolean evalSize(Long total){
        int pageSize,percentageSize;
        try{
            pageSize = Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size")));
            percentageSize = Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.mongo.percentage")));
        }catch (Exception e) {
            pageSize = 200;
            percentageSize = 50;
        }
        return total == 0L || (100 * total / pageSize) <= percentageSize;
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
        return mongo.count()
                .map(this::evalSize)
                .block();
    }

    private Pageable getPage(int actualTotal) {
        //FIXME: ora-01795 - maximum number of expressions in a list is 1000
        return PageRequest.of(
                0,
                 Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size"))) - actualTotal);
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
                                getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
