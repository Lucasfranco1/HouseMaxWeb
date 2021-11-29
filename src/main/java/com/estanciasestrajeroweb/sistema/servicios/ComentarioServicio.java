/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import com.estanciasestrajeroweb.sistema.entidades.Comentario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.ComentarioRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioServicio {
    @Autowired
    private ComentarioRepositorio comR;
    
    @Autowired
    private CasaServicio cS;
    
    @Transactional
    public void crearComentario(String descripcion, String idCasa) throws ErrorService{
        validar(descripcion);
        Casa casa=cS.buscarCasaPorId(idCasa);
        
        Comentario comentario = new Comentario();
        comentario.setDescripcion(descripcion);
        comentario.setCasa(casa);
        
        comR.save(comentario);
    }

    @Transactional
    public void modificarComentario(String id, String descripcion, String idCasa) throws ErrorService{
        validar(descripcion);
        Casa casa=cS.buscarCasaPorId(idCasa);
        Optional<Comentario> resp=comR.findById(id);
        if(resp.isPresent()){
            Comentario comentario=resp.get();
            comentario.setDescripcion(descripcion);
            comentario.setCasa(casa);
            comR.save(comentario);
        }else{
            throw new ErrorService("Error al modificar");
        }
    }
    
    @Transactional
    public void eliminarComentario(String id) throws ErrorService{
        Optional<Comentario> respuesta=comR.findById(id);
        if(respuesta.isPresent()){
            comR.deleteById(id);
        }else{
            throw new ErrorService("Error al eliminar comentario.");
        }
    }
    private void validar(String descripcion) throws ErrorService {
       if(descripcion==null||descripcion.isEmpty()){
           throw new ErrorService("La descripción no puede ser nula, ni estar vacía");
       }
       
    }
}
