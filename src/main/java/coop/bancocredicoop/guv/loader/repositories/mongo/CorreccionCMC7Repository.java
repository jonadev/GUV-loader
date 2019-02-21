package coop.bancocredicoop.guv.loader.repositories.mongo;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCMC7;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CorreccionCMC7Repository extends ReactiveMongoRepository<CorreccionCMC7, Long> {

    Flux<CorreccionCMC7> findFirst20ByCreatedAtNull();
}
