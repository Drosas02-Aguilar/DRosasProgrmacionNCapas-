package com.digis01.DRosasAguilarDamianNCapasProject.ML;

public class Estado {

    private int IdEstado;
    private String Nombre;
    public Pais Pais;


    public Pais getPais() {

        return Pais;
    }
    
     public Estado() {}

    public Estado(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Estado eJPA) {
        if (eJPA == null) return;
        this.IdEstado = eJPA.getIdEstado();
        this.Nombre = eJPA.getNombre();

        if (eJPA.getPais() != null) {
            this.Pais = new Pais(eJPA.getPais());
        }
    }
 

    // public Estado() {}
    /*  public Estado(int IdEstado, String Nombre) {
    this.IdEstado = IdEstado;
    this.Nombre = Nombre;
    }*/
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
