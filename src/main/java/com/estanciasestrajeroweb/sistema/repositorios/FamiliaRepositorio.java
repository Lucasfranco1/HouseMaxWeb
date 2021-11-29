/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.repositorios;

import com.estanciasestrajeroweb.sistema.entidades.Familia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FamiliaRepositorio extends JpaRepository<Familia, String> {
    
    @Query("SELECT m FROM Familia m WHERE m.usuario.id= :id")
    public List<Familia> buscarFamiliaPorUsuario(@Param("id")String id);
   
    @Query("SELECT m FROM Familia m WHERE m.usuario.id= :id")
    public Familia buscarFamiliaUsuario(@Param("id")String id);
}
