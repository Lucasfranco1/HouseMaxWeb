/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.repositorios;

import com.estanciasestrajeroweb.sistema.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository 
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{
    
    @Query("SELECT m FROM Cliente m WHERE m.usuario.id= :id")
    public List<Cliente> buscarClientePorUsuario(@Param("id")String id);
   
}
