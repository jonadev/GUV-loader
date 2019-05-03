package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;
import coop.bancocredicoop.guv.loader.models.Cheque;

import java.math.BigDecimal;
import java.util.Date;

public class CorreccionCUIT extends Cheque {

    public CorreccionCUIT(Long id, BigDecimal importe, Date fechaDiferida, String cuit, CMC7 cmc7) {
        super(id, importe, fechaDiferida, cuit, cmc7);
    }

}