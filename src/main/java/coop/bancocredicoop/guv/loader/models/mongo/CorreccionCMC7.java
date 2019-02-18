package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Embeddable
public class CorreccionCMC7 implements Serializable {

    private Long id;
    private BigDecimal importe;
    private Date fechaDiferida;
    private String cuit;
    private Integer codMoneda;
    private CMC7 cmc7;

    public CorreccionCMC7(Long id, BigDecimal importe, Date fechaDiferida, String cuit, Integer codMoneda, CMC7 cmc7) {
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
