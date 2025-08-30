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
public interface IUsuarioJPADAO {

    Result GetAll();

    Result Add(com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuario);

    Result Update(com.digis01.DRosasAguilarDamianNCapasProject.ML.Usuario usuario);

    Result GetByIdUsuario(int IdUsuario);

    Result DireccionesByIdUsuario(int IdUsuario);

    Result Delete(int IdUsuario);

   Result SetActivo(int idUsuario, boolean activo, String usuarioBaja);

    // (azúcar opcional)
    default Result BajaLogica(int idUsuario, String usuarioBaja) {
        return SetActivo(idUsuario, false, usuarioBaja);
    }
    default Result Reactivar(int idUsuario) {
        return SetActivo(idUsuario, true, null);
    }
}

