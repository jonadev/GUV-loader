package coop.bancocredicoop.guv.loader.repositories;

import coop.bancocredicoop.guv.loader.model.Cheque;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChequeRepository extends CrudRepository<Cheque, Long> {

    List<Cheque> findFirst10ByActivo(Integer activo);
}
