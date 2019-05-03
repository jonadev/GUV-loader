package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;
import coop.bancocredicoop.guv.loader.models.Cheque;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Document
public class CorreccionFecha extends Cheque {

    public CorreccionFecha(Long id, BigDecimal importe, Date fechaDiferida, String cuit, CMC7 cmc7) {
        super(id, importe, fechaDiferida, cuit, cmc7);
    }
}
