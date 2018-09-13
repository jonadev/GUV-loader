package coop.bancocredicoop.guv.loader.model;

import javax.persistence.Id;

import javax.persistence.Entity;
import java.io.Serializable;

@Entity
public class Cheque implements Serializable {

    @Id
    private Long id;
    private Integer activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }
}
