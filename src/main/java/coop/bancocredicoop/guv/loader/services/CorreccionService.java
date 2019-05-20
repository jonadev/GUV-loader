package coop.bancocredicoop.guv.loader.services;

import coop.bancocredicoop.guv.loader.models.Cheque;
import coop.bancocredicoop.guv.loader.models.Proceso;
import coop.bancocredicoop.guv.loader.models.mongo.*;
import coop.bancocredicoop.guv.loader.repositories.mongo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public class CorreccionService {

    @Autowired
    private CorreccionImporteRepository importeRepository;

    @Autowired
    private CorreccionCUITRepository cuitRepository;

    @Autowired
    private CorreccionCMC7Repository cmc7Repository;

    @Autowired
    private CorreccionFechaRepository fechaRepository;

    private static Logger log = LoggerFactory.getLogger(CorreccionService.class);

    public Flux<Cheque> findAll(String type) {
        Proceso proceso;
        try{
            proceso = Proceso.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException iae){
            String message = "Invalid process name: " + type;
            log.warn(message, iae);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message, iae);
        }

        return setTTLAndRetrieve(proceso);

    }

    private Flux<Cheque> setTTLAndRetrieve(Proceso proceso){
        switch(proceso) {
            case IMPORTE:
                return importeRepository
                        .findFirst20ByCreatedAtNull()
                        .map(this::setTTL)
                        .map(CorreccionImporte.class::cast)
                        .flatMap(importeRepository::save);

            case CMC7:
                return cmc7Repository
                        .findFirst20ByCreatedAtNull()
                        .map(this::setTTL)
                        .map(CorreccionCMC7.class::cast)
                        .flatMap(cmc7Repository::save);
            case FECHA:
                return fechaRepository
                        .findFirst20ByCreatedAtNull()
                        .map(this::setTTL)
                        .map(CorreccionFecha.class::cast)
                        .flatMap(fechaRepository::save);
            case CUIT:
                return cuitRepository
                        .findFirst20ByCreatedAtNull()
                        .map(this::setTTL)
                        .map(CorreccionCUIT.class::cast)
                        .flatMap(cuitRepository::save);
            default:
                return Flux.empty();
        }
    }

    private Cheque setTTL(Cheque correccion){
        correccion.setCreatedAt(LocalDateTime.now());
        return correccion;
    }
}