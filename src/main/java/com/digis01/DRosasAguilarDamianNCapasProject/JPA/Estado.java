package com.digis01.DRosasAguilarDamianNCapasProject.JPA;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(
        name = "ESTADO_SEQ_GEN",
        sequenceName = "MUNICIPIO_SEQ",
        allocationSize = 1
)

public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESTADO_SEQ_GEN")
    @Column(name = "IdEstado")
    private int IdEstado;

    @Column(name = "Nombre", nullable = false)
    private String Nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdPais", nullable = false)
    public Pais Pais;

    public Pais getPais() {

        return Pais;
    }

    public Estado() {
    }

    public Estado(int IdEstado, String Nombre) {
        this.IdEstado = IdEstado;
        this.Nombre = Nombre;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;

    }

}
