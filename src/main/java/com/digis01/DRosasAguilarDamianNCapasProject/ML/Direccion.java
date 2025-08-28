package com.digis01.DRosasAguilarDamianNCapasProject.ML;

public class Direccion {

    private int IdDireccion;
    private String Calle;
    private String NumeroInterior;
    private String NumeroExterior;
    public Colonia Colonia;

    public Colonia getColonia() {
        return Colonia;
    }

    public void setColonia(Colonia Colonia) {
        this.Colonia = Colonia;
    }

   

    public int getIdDireccion() {   
        return IdDireccion;
    }

     public Direccion() {}

 public Direccion(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Direccion dJPA) {
        if (dJPA == null) return;
        this.IdDireccion = dJPA.getIdDireccion();
        this.Calle = dJPA.getCalle();
        this.NumeroInterior = dJPA.getNumeroInterior();
        this.NumeroExterior = dJPA.getNumeroExterior();

        if (dJPA.getColonia() != null) {
            this.Colonia = new Colonia(dJPA.getColonia());
        }
 }
     
     
    // public Direccion(int IdDireccion, String Calle, String NumeroExterior, String NumeroInterior) {
    //  this.IdDireccion = IdDireccion;
    //   this.Calle = Calle;
    //    this.NumeroExterior = NumeroExterior;
    //     this.NumeroInterior = NumeroInterior;
    //}
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

}
