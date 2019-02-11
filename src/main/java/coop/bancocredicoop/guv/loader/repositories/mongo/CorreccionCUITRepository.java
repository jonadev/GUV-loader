package coop.bancocredicoop.guv.loader.repositories.mongo;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCUIT;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CorreccionCUITRepository extends ReactiveMongoRepository<CorreccionCUIT, Long> {

}
