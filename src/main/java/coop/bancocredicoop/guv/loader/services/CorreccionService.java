package coop.bancocredicoop.guv.loader.services;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCheque;
import coop.bancocredicoop.guv.loader.repositories.mongo.CorreccionImporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CorreccionService {

    @Autowired
    private CorreccionImporteRepository correccionImporteRepository;

    public Flux<CorreccionCheque> findAll() {
        return this.correccionImporteRepository.findAll();
    }

}
