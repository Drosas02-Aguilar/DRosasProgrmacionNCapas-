package com.digis01.DRosasAguilarDamianNCapasProject.JPA;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
 
public class Pais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    @Column(name = "IdPais")
    private int IdPais;

    @Column(name = "Nombre", nullable = false, length = 120, unique = true)
    private String Nombre;

    public Pais() {
    }

    public Pais(int IdPais, String Nombre) {
        this.IdPais = IdPais;
        this.Nombre = Nombre;
    }

    public int getIdPais() {
        return IdPais;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
