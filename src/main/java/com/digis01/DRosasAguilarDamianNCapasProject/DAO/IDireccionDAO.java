
package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Direccion;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;


public interface IDireccionDAO {
    
    Result addToUsuario(int idUsuario, Direccion direccion );
    Result updateDireccion(Direccion direccion);
    Result delete(int idDireccion);
    Result getbyid(int idDireccion);
    
    
    
}
