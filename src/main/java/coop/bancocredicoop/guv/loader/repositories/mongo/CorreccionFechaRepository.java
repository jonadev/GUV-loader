package coop.bancocredicoop.guv.loader.repositories.mongo;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CorreccionFechaRepository extends ReactiveMongoRepository<CorreccionFecha, Long> {

    Flux<CorreccionFecha> findFirst20ByCreatedAtNull();
}
