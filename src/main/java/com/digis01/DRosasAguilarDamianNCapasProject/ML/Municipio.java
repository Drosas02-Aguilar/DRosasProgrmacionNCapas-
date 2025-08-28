package com.digis01.DRosasAguilarDamianNCapasProject.ML;
public class Municipio {

    private int IdMunicipio;
    public String Nombre;
    
    
    public Municipio(){}
    public Estado Estado;
    
    
    public Municipio(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Municipio mJPA) {
        if (mJPA == null) return;
        this.IdMunicipio = mJPA.getIdMunicipio();
        this.Nombre = mJPA.getNombre();

        if (mJPA.getEstado() != null) {
            this.Estado = new Estado(mJPA.getEstado());
        }
    }
    

    public Municipio(int IdMunicipio, String Nombre) {
        this.IdMunicipio = IdMunicipio;
        this.Nombre = Nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.IdMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
    
    

    
}
