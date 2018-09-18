package coop.bancocredicoop.guv.loader.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Cheque implements Serializable {

    @Id
    private Long id;

    @Column(name = "importe")
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCheque estado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_AR", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "fechadiferida")
    private Date fechaDiferida;

    @Column(name = "cuit")
    private String cuit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Deposito deposito;

    @ManyToOne(fetch = FetchType.LAZY)
    private Moneda moneda;

}
