package coop.bancocredicoop.guv.loader.repositories.mongo;

import coop.bancocredicoop.guv.loader.model.Cheque;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ChequeMongoRepository extends ReactiveMongoRepository<Cheque, Long> {

}
