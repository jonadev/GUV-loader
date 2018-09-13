package coop.bancocredicoop.guv.loader.services;

import coop.bancocredicoop.guv.loader.model.Cheque;
import coop.bancocredicoop.guv.loader.repositories.ChequeRepository;
import coop.bancocredicoop.guv.loader.repositories.mongo.ChequeMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class LoaderService {

    private static Logger log = LoggerFactory.getLogger(LoggerFactory.class);

    @Autowired
    ChequeRepository chequeRepository;

    @Autowired
    ChequeMongoRepository mongo;

    public void load(){
        Flux<Cheque> chequeFlux = retrieveFromDatabase();
        storeInMongo(chequeFlux);
    }

    private void storeInMongo(Flux<Cheque> chequeFlux) {
        log.debug("Storing FLUX in MongoDB");
        chequeFlux
                .flatMap(mongo::save)
                .subscribe();
    }

    private Flux<Cheque> retrieveFromDatabase() {
        log.debug("Retrieve cheques from DB");
        return Flux.fromIterable(chequeRepository.findFirst10ByActivo(1));
    }
}
