package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class ImporteService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private Environment env;

    @Autowired
    private ChequeRepository db;

    @Autowired
    private CorreccionImporteRepository importeRepository;

    public Mono<Long> count(){
        return importeRepository.count();
    }

    public Mono<List<CorreccionImporte>> doLoad(){
        Flux<CorreccionImporte> chequeFlux = retrieveFromDatabase();
        return storeInMongo(chequeFlux).collectList();
    }

    private Pageable getPage(int actualTotal) {
        //FIXME: ora-01795 - maximum number of expressions in a list is 1000
        return PageRequest.of(
                0,
                Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size"))) - actualTotal);
    }

    private Flux<CorreccionImporte> storeInMongo(Flux<CorreccionImporte> chequesFlux) {
        log.debug("Storing FLUX in MongoDB");
        return chequesFlux
                .flatMap(importeRepository::save)
                .doOnError(e -> log.error("Failed to store cheques", e));

    }

    private Flux<CorreccionImporte> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return importeRepository.findAll()
                .doOnError(e -> log.error("Failed to retrieve cheques from MongoDB", e))
                .retry(3)
                .map(CorreccionImporte::getId)
                .defaultIfEmpty(0L)
                .collectList()
                .map(idCheques ->
                        db.findCorreccionImporte(
                                EstadoCheque.VALIDAR_CMC7,
                                idCheques,
                                getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
