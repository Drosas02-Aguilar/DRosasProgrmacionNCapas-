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
import jakarta.persistence.Table;

@Entity
@Table(name = "DIRECCION")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddireccion")
    private int IdDireccion;

    @Column(name = "calle", nullable = false)
    private String Calle;
    @Column(name = "numerointerior")
    private String NumeroInterior;
    
    @Column(name = "numeroexterior", nullable = false)
    private String NumeroExterior;
    
    @ManyToOne()
    @JoinColumn(name = "idcolonia", nullable = false)
    public Colonia Colonia;
    
    @ManyToOne()
    @JoinColumn(name = "idusuario", nullable = false) 
    public Usuario usuario;   

    public Colonia getColonia() {
        return Colonia;
    }

    public void setColonia(Colonia Colonia) {
        this.Colonia = Colonia;
    }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public Direccion() {
    }

    public Direccion(com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion dML) {
        this.IdDireccion = dML.getIdDireccion();
        this.Calle = dML.getCalle();
        this.NumeroInterior = dML.getNumeroInterior();
        this.NumeroExterior = dML.getNumeroExterior();

        if (dML.getColonia() != null) {
            this.Colonia = new Colonia(dML.getColonia());
        }
    }
    
    
    public Direccion(com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuarioML){
//        alumnoML.Direcciones.get(0) -> Direccion ML
        com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion direccionML = usuarioML.direcciones.get(0); 
        
        this.IdDireccion = direccionML.getIdDireccion();
        this.Calle = direccionML.getCalle();
        this.NumeroInterior = direccionML.getNumeroInterior();
        this.NumeroExterior = direccionML.getNumeroExterior();
        this.Colonia = new Colonia();
        this.Colonia.setIdColonia(direccionML.Colonia.getIdColonia());
        this.usuario = new Usuario();
        this.usuario.setIdUsuario(usuarioML.getIdUsuario());
    }
    
    public Direccion(int IdDireccion, String Calle, String NumeroExterior, String NumeroInterior) {
        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroExterior = NumeroExterior;
        this.NumeroInterior = NumeroInterior;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public void setUsuario(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Usuario uJPA) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
