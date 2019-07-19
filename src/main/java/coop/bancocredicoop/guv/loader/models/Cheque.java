package coop.bancocredicoop.guv.loader.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    protected LocalDate fechaDiferida;

    @Column(name = "cuit")
    protected String cuit;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Deposito deposito;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Moneda moneda;

    @Column(name = "fechaingreso1")
    protected LocalDate fechaIngreso1;

    @Column(name = "fechaingreso2")
    protected LocalDate fechaIngreso2;

    @Column(name = "numero")
    protected BigInteger numero;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Set<Observacion> observaciones;

    @Embedded
    protected CMC7 cmc7;

    @JsonIgnore
    @Field
    @Indexed(name = "expire_at_120_seconds", expireAfterSeconds = 120)
    //TODO: Pasar a variable.
    // En caso de necesitar extender el tiempo de expiracion de la clave se debe BORRAR la coleccion completa
    // de la MongoDB y reinicia la app para que se cree nuevamente
    protected LocalDateTime createdAt;

    public Cheque(Long id, BigDecimal importe, LocalDate fechaDiferida, String cuit, CMC7 cmc7) {
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

    public LocalDate getFechaDiferida() {
        return fechaDiferida;
    }

    public void setFechaDiferida(LocalDate fechaDiferida) {
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

    public LocalDate getFechaIngreso1() {
        return fechaIngreso1;
    }

    public void setFechaIngreso1(LocalDate fechaIngreso1) {
        this.fechaIngreso1 = fechaIngreso1;
    }

    public LocalDate getFechaIngreso2() {
        return fechaIngreso2;
    }

    public void setFechaIngreso2(LocalDate fechaIngreso2) {
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
