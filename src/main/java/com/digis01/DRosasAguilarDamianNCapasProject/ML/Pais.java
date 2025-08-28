package com.digis01.DRosasAguilarDamianNCapasProject.ML;

public class Pais {

    private int IdPais;
    private String Nombre;

    public Pais() {}
    

    public Pais(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Pais pJPA) {
        if (pJPA == null) return;
        this.IdPais = pJPA.getIdPais();
        this.Nombre = pJPA.getNombre();
    }
    

  /*  public Pais(int IdPais, String Nombre) {
        this.IdPais = IdPais;
        this.Nombre = Nombre;
    }*/

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
