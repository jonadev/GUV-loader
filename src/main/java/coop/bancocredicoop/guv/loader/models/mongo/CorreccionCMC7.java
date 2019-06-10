package coop.bancocredicoop.guv.loader.models.mongo;

import coop.bancocredicoop.guv.loader.models.CMC7;
import coop.bancocredicoop.guv.loader.models.Cheque;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document
public class CorreccionCMC7 extends Cheque {

    public CorreccionCMC7(Long id, BigDecimal importe, LocalDate fechaDiferida, String cuit, CMC7 cmc7) {
        super(id, importe, fechaDiferida, cuit, cmc7);
    }

}
