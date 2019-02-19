package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;

import java.math.BigDecimal;
import java.util.Date;

public class CorreccionImporte extends Correccion {

    public CorreccionImporte(Long id, BigDecimal importe, Date fechaDiferida, String cuit, Integer codMoneda, CMC7 cmc7) {
        super(id, importe, fechaDiferida, cuit, codMoneda, cmc7);
    }

}
