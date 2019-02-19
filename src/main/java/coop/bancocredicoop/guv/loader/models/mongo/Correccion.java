package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public abstract class Correccion implements Serializable {

    protected Long id;
    protected BigDecimal importe;
    protected Date fechaDiferida;
    protected String cuit;
    protected Integer codMoneda;
    protected CMC7 cmc7;

    public Correccion() {}

    public Correccion(Long id, BigDecimal importe, Date fechaDiferida, String cuit, Integer codMoneda, CMC7 cmc7) {
        this.id = id;
        this.importe = importe;
        this.fechaDiferida = fechaDiferida;
        this.cuit = cuit;
        this.codMoneda = codMoneda;
        this.cmc7 = cmc7;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public Date getFechaDiferida() {
        return fechaDiferida;
    }

    public String getCuit() {
        return cuit;
    }

    public Integer getCodMoneda() {
        return codMoneda;
    }

    public CMC7 getCmc7() {
        return cmc7;
    }

}
