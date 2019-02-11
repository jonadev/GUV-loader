package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionFechaRepository;
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
public class FechaService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private Environment env;

    @Autowired
    private ChequeRepository db;

    @Autowired
    private CorreccionFechaRepository fechaRepository;

    public Mono<Long> count(){
        return fechaRepository.count();
    }

    public Mono<List<CorreccionFecha>> doLoad(){
        Flux<CorreccionFecha> chequeFlux = retrieveFromDatabase();
        return storeInMongo(chequeFlux).collectList();
    }

    private Pageable getPage(int actualTotal) {
        //FIXME: ora-01795 - maximum number of expressions in a list is 1000
        return PageRequest.of(
                0,
                Integer.parseInt(Objects.requireNonNull(env.getProperty("loader.page.size"))) - actualTotal);
    }

    private Flux<CorreccionFecha> storeInMongo(Flux<CorreccionFecha> chequesFlux) {
        log.debug("Storing FLUX in MongoDB");
        return chequesFlux
                .flatMap(fechaRepository::save)
                .doOnError(e -> log.error("Failed to store cheques", e));

    }

    private Flux<CorreccionFecha> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return fechaRepository.findAll()
                .doOnError(e -> log.error("Failed to retrieve cheques from MongoDB", e))
                .retry(3)
                .map(CorreccionFecha::getId)
                .defaultIfEmpty(0L)
                .collectList()
                .map(idCheques ->
                        db.findCorreccionFecha(
                                EstadoCheque.VALIDAR_CMC7,
                                idCheques,
                                getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
