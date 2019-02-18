package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.Cheque;
import coop.bancocredicoop.guv.loader.models.Deposito;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCMC7;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionCMC7Repository;
import coop.bancocredicoop.guv.loader.utils.LoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CMC7Service {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ChequeRepository db;

    @Autowired
    private LoaderUtils utils;

    @Autowired
    private CorreccionCMC7Repository cmc7Repository;

    public Mono<Long> getSize(Boolean run){
        return run ? cmc7Repository.count() : Mono.just(-1L);
    }

    public Mono<List<CorreccionCMC7>> doLoad(){
        Flux<CorreccionCMC7> chequeFlux = retrieveFromDatabase();
        return storeInMongo(chequeFlux).collectList();
    }

    private Flux<CorreccionCMC7> storeInMongo(Flux<CorreccionCMC7> chequesFlux) {
        log.debug("Storing FLUX in MongoDB");
        return chequesFlux
                .flatMap(cmc7Repository::save)
                .doOnError(e -> log.error("Failed to store cheques", e));

    }

    private Flux<CorreccionCMC7> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return cmc7Repository.findAll()
                .doOnError(e -> log.error("Failed to retrieve cheques from MongoDB", e))
                .retry(3)
                .map(CorreccionCMC7::getId)
                .defaultIfEmpty(0L)
                .collectList()
                .map(idCheques ->
                        db.findCorreccionCMC7(
                                EstadoCheque.VALIDAR_CMC7,
                                idCheques,
                                Deposito.Estado.VALIDAR_CMC7,
                                Cheque.Observacion.CMC7,
                                utils.getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
