package coop.bancocredicoop.guv.loader.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
public abstract class Cheque implements Serializable {

    @Id
    protected Long id;

    @Column(name = "activo")
    protected boolean activo = true;

    @Column(name = "importe")
    protected BigDecimal importe;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    protected EstadoCheque estado;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_AR", timezone = "America/Argentina/Buenos_Aires")
    @Column(name = "fechadiferida")
    protected Date fechaDiferida;

    @Column(name = "cuit")
    protected String cuit;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Deposito deposito;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Moneda moneda;

    @Column(name = "fechaingreso1")
    protected Date fechaIngreso1;

    @Column(name = "fechaingreso2")
    protected Date fechaIngreso2;

    @Column(name = "numero")
    protected BigInteger numero;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<Observacion> observaciones;

    @Embedded
    protected CMC7 cmc7;

    @JsonIgnore
    @Indexed(name = "expire_at_120_seconds", expireAfterSeconds = 120)
    protected LocalDateTime createdAt;

    public Cheque(Long id, BigDecimal importe, Date fechaDiferida, String cuit, CMC7 cmc7) {
        this.id = id;
        this.importe = importe;
        this.fechaDiferida = fechaDiferida;
        this.cuit = cuit;
        this.cmc7 = cmc7;
        this.createdAt = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public EstadoCheque getEstado() {
        return estado;
    }

    public void setEstado(EstadoCheque estado) {
        this.estado = estado;
    }

    public Date getFechaDiferida() {
        return fechaDiferida;
    }

    public void setFechaDiferida(Date fechaDiferida) {
        this.fechaDiferida = fechaDiferida;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Date getFechaIngreso1() {
        return fechaIngreso1;
    }

    public void setFechaIngreso1(Date fechaIngreso1) {
        this.fechaIngreso1 = fechaIngreso1;
    }

    public Date getFechaIngreso2() {
        return fechaIngreso2;
    }

    public void setFechaIngreso2(Date fechaIngreso2) {
        this.fechaIngreso2 = fechaIngreso2;
    }

    public BigInteger getNumero() {
        return numero;
    }

    public void setNumero(BigInteger numero) {
        this.numero = numero;
    }

    public Set<Observacion> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(Set<Observacion> observaciones) {
        this.observaciones = observaciones;
    }

    public enum Observacion {
        CMC7,
        IMPORTE,
        FECHA,
        CUIT
    }

    public CMC7 getCmc7() {
        return cmc7;
    }

    public void setCmc7(CMC7 cmc7) {
        this.cmc7 = cmc7;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
