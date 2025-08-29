package com.digis01.DRosasAguilarDamianNCapasProject.ML;
public class Rol {

 private  int IdRol;
 private String Nombre;

 
 public Rol(){}
 
  public Rol(com.digis01.DRosasAguilarDamianNCapasProject.JPA.Rol rolJPA) {
        if (rolJPA != null) {
            this.IdRol = rolJPA.getIdRol();
            this.Nombre = rolJPA.getNombre();
        }
    }
 
 
  public Rol(int IdRol, String Nombre) {
        this.IdRol = IdRol;
        this.Nombre = Nombre;
    }
    public int getIdRol() {
        return IdRol;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
 
 
 
 
 
 
 
 
 
}
