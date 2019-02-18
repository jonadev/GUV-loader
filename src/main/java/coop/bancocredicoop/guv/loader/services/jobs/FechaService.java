package coop.bancocredicoop.guv.loader.services.jobs;

import coop.bancocredicoop.guv.loader.models.Cheque;
import coop.bancocredicoop.guv.loader.models.Deposito;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionFechaRepository;
import coop.bancocredicoop.guv.loader.utils.LoaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FechaService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    private ChequeRepository db;

    @Autowired
    private LoaderUtils utils;

    @Autowired
    private CorreccionFechaRepository fechaRepository;

    public Mono<Long> getSize(Boolean run){
        return run ? fechaRepository.count() : Mono.just(-1L);
    }

    public Mono<List<CorreccionFecha>> doLoad(){
        Flux<CorreccionFecha> chequeFlux = retrieveFromDatabase();
        return storeInMongo(chequeFlux).collectList();
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
                                Deposito.TipoOperatoria.getDiferidos(Deposito.TipoOperatoria.VAL_NEG),
                                idCheques,
                                Deposito.Estado.VALIDAR_CMC7,
                                Cheque.Observacion.FECHA,
                                utils.getPage(idCheques.size())))
                .doOnError(e -> log.error("Failed to retrieve cheques from DB", e))
                .flatMapMany(Flux::fromIterable);
    }
}
