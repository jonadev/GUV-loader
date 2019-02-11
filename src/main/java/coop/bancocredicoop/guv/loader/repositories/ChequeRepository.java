package coop.bancocredicoop.guv.loader.repositories;

import coop.bancocredicoop.guv.loader.models.Cheque;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCMC7;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte;
import coop.bancocredicoop.guv.loader.models.mongo.CorreccionCUIT;
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
            "SELECT new coop.bancocredicoop.guv.loader.models.mongo.CorreccionImporte(c.id, c.importe, " +
                    "c.fechaDiferida, c.cuit, m.codMoneda) " +
            "FROM Cheque c " +
            "JOIN c.deposito d " +
            "JOIN c.moneda m " +
            "WHERE c.estado = :estado " +
            "AND (c.importe = 0 OR c.importe IS NULL) " +
            "AND c.id NOT IN (:ids) " +
            "ORDER BY d.prioridadForzada ASC")
    List<CorreccionImporte> findCorreccionImporte(@Param("estado") EstadoCheque estado,
                                                  @Param("ids") List<Long> idList,
                                                  Pageable request);

    @Query(value =
            "SELECT new coop.bancocredicoop.guv.loader.models.mongo.CorreccionCUIT(c.id, c.importe, " +
                    "c.fechaDiferida, c.cuit, m.codMoneda) " +
                    "FROM Cheque c " +
                    "JOIN c.deposito d " +
                    "JOIN c.moneda m " +
                    "WHERE c.estado = :estado " +
                    "AND (c.importe = 0 OR c.importe IS NULL) " +
                    "AND c.id NOT IN (:ids) " +
                    "ORDER BY d.prioridadForzada ASC")
    List<CorreccionCUIT> findCorreccionCUIT(@Param("estado") EstadoCheque estado,
                                               @Param("ids") List<Long> idList,
                                               Pageable request);

    @Query(value =
            "SELECT new coop.bancocredicoop.guv.loader.models.mongo.CorreccionCMC7(c.id, c.importe, " +
                    "c.fechaDiferida, c.cuit, m.codMoneda) " +
                    "FROM Cheque c " +
                    "JOIN c.deposito d " +
                    "JOIN c.moneda m " +
                    "WHERE c.estado = :estado " +
                    "AND (c.importe = 0 OR c.importe IS NULL) " +
                    "AND c.id NOT IN (:ids) " +
                    "ORDER BY d.prioridadForzada ASC")
    List<CorreccionCMC7> findCorreccionCMC7(@Param("estado") EstadoCheque estado,
                                            @Param("ids") List<Long> idList,
                                            Pageable request);

    @Query(value =
            "SELECT new coop.bancocredicoop.guv.loader.models.mongo.CorreccionFecha(c.id, c.importe, " +
                    "c.fechaDiferida, c.cuit, m.codMoneda) " +
                    "FROM Cheque c " +
                    "JOIN c.deposito d " +
                    "JOIN c.moneda m " +
                    "WHERE c.estado = :estado " +
                    "AND (c.importe = 0 OR c.importe IS NULL) " +
                    "AND c.id NOT IN (:ids) " +
                    "ORDER BY d.prioridadForzada ASC")
    List<CorreccionFecha> findCorreccionFecha(@Param("estado") EstadoCheque estado,
                                              @Param("ids") List<Long> idList,
                                              Pageable request);


}
