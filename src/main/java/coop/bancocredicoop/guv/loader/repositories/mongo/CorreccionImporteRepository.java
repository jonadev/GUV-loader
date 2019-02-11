package coop.bancocredicoop.guv.loader.repositories.mongo;

import coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CorreccionImporteRepository extends ReactiveMongoRepository<CorreccionImporte, Long> {

}
