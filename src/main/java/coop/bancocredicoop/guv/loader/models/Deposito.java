package coop.bancocredicoop.guv.loader.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Deposito {

    @Id
    private Long id;

    @Column(name = "prioridadforzada")
    private Integer prioridadForzada;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "deposito")
    private Set<Cheque> cheques;

}
