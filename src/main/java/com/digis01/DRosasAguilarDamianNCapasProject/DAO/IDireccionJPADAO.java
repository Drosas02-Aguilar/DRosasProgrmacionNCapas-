/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;

/**
 *
 * @author digis
 */
public interface IDireccionJPADAO {
    
    Result AddDireccion(int IdUsuario, com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion direccionML);
    
   Result GetByIdDireccion(int IdDireccion);
    
    Result Update(com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion direccionML);
    
    Result Delete(int IdDireccion);
    
    
    
    
}
