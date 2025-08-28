package com.digis01.DRosasAguilarDamianNCapasProject.DAO;

import com.digis01.DRosasAguilarDamianNCapasProject.ML.Result;
import com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario;
public interface IUsuarioDAO {
    
Result GetAll(Usuario usuario);
Result Add(Usuario usuario); 
Result DireccionesByIdUsuario(int idUsuario);
Result update(Usuario usuario);
Result deleteById(int IdUsuario);
Result GetByiDUsuario(int IdUsuario);

}
