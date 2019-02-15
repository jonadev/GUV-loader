package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.Deposito;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCUIT;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionCUITRepository;
import coop.bancocredicoop.guv.loader.utils.LoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CUITService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ChequeRepository db;

    @Autowired
    private LoaderUtils utils;

    @Autowired
    private CorreccionCUITRepository cuitRepository;

    public Mono<Long> getSize(Boolean run){
        return run ? cuitRepository.count() : Mono.just(-1L);
    }

    public Mono<List<CorreccionCUIT>> doLoad(){
        Flux<CorreccionCUIT> chequeFlux = retrieveFromDatabase();
        return storeInMongo(chequeFlux).collectList();
    }

    private Flux<CorreccionCUIT> storeInMongo(Flux<CorreccionCUIT> chequesFlux) {
        log.debug("Storing FLUX in MongoDB");
        return chequesFlux
                .flatMap(cuitRepository::save)
                .doOnError(e -> log.error("Failed to store cheques", e));

    }

    private Flux<CorreccionCUIT> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return cuitRepository.findAll()
                .doOnError(e -> log.error("Failed to retrieve cheques from MongoDB", e))
                .retry(3)
                .map(CorreccionCUIT::getId)
                .defaultIfEmpty(0L)
                .collectList()
                .map(idCheques ->
                        db.findCorreccionCUIT(
                                EstadoCheque.VALIDAR_CMC7,
                                Deposito.TipoOperatoria.getDiferidos(Deposito.TipoOperatoria.VAL_NEG),
                                idCheques,
                                utils.getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
