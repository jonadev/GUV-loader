package coop.bancocredicoop.guv.loader.repositories;

import coop.bancocredicoop.guv.loader.models.Cheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCheque;
import coop.bancocredicoop.guv.loader.models.EstadoCheque;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChequeRepository extends CrudRepository<Cheque, Long> {

    @Query(value =
            "SELECT new coop.bancocredicoop.guv.loader.models.mongo.CorreccionCheque(c.id, c.importe, " +
                    "c.fechaDiferida, c.cuit, m.codMoneda) " +
            "FROM Cheque c " +
            "JOIN c.deposito d " +
            "JOIN c.moneda m " +
            "WHERE c.estado = :estado " +
            "AND (c.importe = 0 OR c.importe IS NULL) " +
            "AND c.id NOT IN (:ids) " +
            "ORDER BY d.prioridadForzada ASC")
    List<CorreccionCheque> findByEstadoAndImporteCeroAndIdNotIn(@Param("estado") EstadoCheque estado,
                                                                @Param("ids") List<Long> idList,
                                                                Pageable request);
}
