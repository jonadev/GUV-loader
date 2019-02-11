package coop.bancocredicoop.guv.loader.models.mongo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CorreccionFecha implements Serializable {

    private Long id;
    private BigDecimal importe;
    private Date fechaDiferida;
    private String cuit;
    private Integer codMoneda;

    public CorreccionFecha(Long id, BigDecimal importe, Date fechaDiferida, String cuit, Integer codMoneda) {
        this.id = id;
        this.importe = importe;
        this.fechaDiferida = fechaDiferida;
        this.cuit = cuit;
        this.codMoneda = codMoneda;
    }

    public Long getId() {
        return id;
    }
}
