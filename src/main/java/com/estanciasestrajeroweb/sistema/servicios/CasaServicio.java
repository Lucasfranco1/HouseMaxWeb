/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.servicios;

import com.estanciasestrajeroweb.sistema.entidades.Casa;
import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Foto;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.enumeraciones.TipoUsuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.repositorios.CasaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CasaServicio {
    Integer count=0;
    
    @Autowired
    private CasaRepositorio cR;
    
    @Autowired
    private FotoServicio fS;
    
    @Autowired
    private FamiliaServicio famS;
    
    @Transactional
    public void guardarCasa(String calle, Integer numero, String codPostal, String ciudad, String pais,
           Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda,
            MultipartFile archivo, String idFamilia) throws ErrorService{
        validar(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        Casa casa = new Casa();
        casa.setCalle(calle);
        casa.setNumero(numero);
        casa.setCodPostal(codPostal);
        casa.setCiudad(ciudad);
        casa.setPais(pais);
        casa.setFechaDesde(fechaDesde);
        casa.setFechaHasta(fechaHasta);
        casa.setMinDias(minDias);
        casa.setMaxDias(maxDias);
        casa.setPrecio(precio);
        casa.setTipoVivienda(tipoVivienda);
        
        Foto foto = fS.guardar(archivo);
        
        casa.setFoto(foto);
        
        Familia familia = famS.buscarFamiliaPorId(idFamilia);
        if (familia != null) {           
            count++;
            casa.setFamilia(familia);
            if(count==2){
                throw new ErrorService("Sólo puedes crear dos veces una casa");
            }
        } else {
            throw new ErrorService("Esa familia no existe");
        }
        
        
       
        
        cR.save(casa);
        
    }
    @Transactional
    public void actualizarCasa(String idCasa, String calle, Integer numero, String codPostal, String ciudad, String pais,
           Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda,
            MultipartFile archivo) throws ErrorService{
        validar(calle, numero, codPostal, ciudad, pais, fechaDesde, fechaHasta, minDias, maxDias, precio, tipoVivienda);
        Optional<Casa> resp= cR.findById(idCasa);
        if(resp.isPresent()){
            Casa casa=resp.get();
            casa.setCalle(calle);
            casa.setNumero(numero);
            casa.setCodPostal(codPostal);
            casa.setPais(pais);
            casa.setFechaDesde(fechaDesde);
            casa.setFechaHasta(fechaHasta);
            casa.setMinDias(minDias);
            casa.setMaxDias(maxDias);
            casa.setPrecio(precio);
            casa.setTipoVivienda(tipoVivienda);
                       
            
            String idFoto=null;
            if(casa.getFoto()!=null){
                idFoto=casa.getFoto().getId();
            }
            Foto foto=fS.actualizar(idFoto, archivo);
            
            casa.setFoto(foto);
            
            cR.save(casa);
            
        }
    }

    private void validar(String calle, Integer numero, String codPostal, String ciudad, String pais, Date fechaDesde, Date fechaHasta, Integer minDias, Integer maxDias, Double precio, String tipoVivienda) throws ErrorService {
       if(calle==null|| calle.isEmpty()){
           throw new ErrorService("La calle no puede ser nula, ni vacía.");
           
       }
       if(numero==null){
           throw new ErrorService("El número no puede ser nulo");
       }
       if(codPostal==null|| codPostal.isEmpty()){
           throw new ErrorService("El código postal no puede ser nulo ni estar vacío.");
           
       }
       if(ciudad==null||ciudad.isEmpty()){
           throw new ErrorService("La ciudad no puede ser nula ni estar vacía");
       }
       if(pais==null||pais.isEmpty()){
           throw new ErrorService("El país no puede ser nulo ni estar vacío");
       }
       if(fechaDesde.compareTo(fechaHasta)>=0){
           throw new ErrorService("La fecha de receso no puede ser anterior ni igual que la de inicio");
       }
       if(minDias==null||minDias>maxDias||minDias==0){
           throw new ErrorService("Los mínimos de días no pueden ser nulos, y no pueden ser mayores que el máximo de días, ni ser 0");
       }
       if(maxDias==null|| maxDias<minDias||maxDias==0){
           throw new ErrorService("Los máximos de días no pueden ser nulos, y no pueden ser menores que el mínimo de días, ni ser 0");
       }
       if(precio==null||precio==0){
           throw new ErrorService("El precio no puede ser cero, ni nulo");
       }
       if(tipoVivienda==null||tipoVivienda.isEmpty()){
           throw new ErrorService("El tipo de vivienda no puede ser nulo, ni estar vacío");
       }
    }
    @Transactional
    public void eliminarCasa(String idCasa) throws ErrorService{
         Optional<Casa> entidad = cR.findById(idCasa);
         if(entidad.isPresent()){
         cR.deleteById(idCasa);
        }else{
             throw new ErrorService("Error al eliminar casa.");
         }
    }
    
    public Casa buscarCasaPorId(String id) throws ErrorService{
        Optional<Casa> casa=cR.findById(id);
        if(casa.isPresent()){
            return cR.findById(id).get();
        }else{
            throw new ErrorService("No se encontró ese id.");
        }
    }
    
    public void buscarFamiliaPorId(String id) throws ErrorService{
        famS.buscarFamiliaPorId(id);
    }
    @Transactional
    public List<Casa> listarCasaPorUsuario(String id){
        return cR.buscarCasaPorUsuario(id);
    }
    @Transactional
    public List<Casa> listarCasasPorImagenes(){
        return cR.findAll();
    }
    
   
 }
