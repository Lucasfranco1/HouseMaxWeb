/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.controladores;

import com.estanciasestrajeroweb.sistema.entidades.Familia;
import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
import com.estanciasestrajeroweb.sistema.servicios.FamiliaServicio;
import com.estanciasestrajeroweb.sistema.servicios.UsuarioServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
@Controller
@RequestMapping("/familia")
public class FamiliaControlador {

    @Autowired
    private FamiliaServicio famS;
    @Autowired
    private UsuarioServicio uS;

    @GetMapping("/editar-perfil")
    public String irARegistrarFamilia(HttpSession session, @RequestParam(required = false) String id, @RequestParam(required = false) String action, ModelMap modelo) {
       
        if(action==null){
            action="Crear";
        }
        
        Usuario login=(Usuario)session.getAttribute("usuariosession");
        if(login==null){
            return "redirect:/inicio";
        }
        Familia familia = new Familia();
        if (id != null && !id.isEmpty()) {
            
           try {
               familia = famS.buscarFamiliaPorId(id);
               
           } catch (ErrorService ex) {
               Logger.getLogger(FamiliaControlador.class.getName()).log(Level.SEVERE, null, ex);
           }            
        }
        modelo.put("perfil", familia);
        modelo.put("action", action);
//        modelo.put("nombre", familia.getNombre());
        return "formulario.html";

    }

    @PostMapping("/actualizar-perfil")
    public String cargarFamilia(ModelMap modelo, HttpSession session, MultipartFile archivo,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(required = false) Integer numHijos,
            @RequestParam(required = false) String email,
            @RequestParam String id
    ) {

        Usuario login = (Usuario) session.getAttribute("usuariosession");
         if(login==null){
            return "redirect:/inicio";
        }
        try {
            if (id == null || id.isEmpty()) {

                famS.crearFamilia(archivo,nombre, edadMin, edadMax, numHijos, email, login.getId());
                modelo.put("titulo", "Familia cargada");
            modelo.put("mensaje", "¡Tu familia se cargó correctamente!");
            } else {
                famS.modificarFamilia(id, archivo, nombre, edadMin, edadMax, numHijos, email, login.getId());
                modelo.put("titulo", "Familia modificada");
            modelo.put("mensaje", "¡Tu familia se modificó correctamente!");
            modelo.put("action", "Actualizar");
            }

            
            return "aviso.html";

        } catch (Exception e) {
            Familia familia = new Familia();
            familia.setNombre(nombre);
            familia.setEdadMin(edadMin);
            familia.setEdadMax(edadMax);
            familia.setNumHijos(numHijos);
            familia.setEmail(email);
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("edadMin", edadMin);
            modelo.put("edadMax", edadMax);
            modelo.put("numHijos", numHijos);
            modelo.put("email", email);
            modelo.put("perfil", familia);
            return "formulario.html";
        }

    }
    @GetMapping("/mi-familia")
    public String ListarFamilias(HttpSession session, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
         if(login==null){
            return "redirect:/inicio";
        }

        List<Familia>todos=famS.listarFamiliasPorUsuario(login.getId());
        model.addAttribute("familias", todos);
        
        return "carga_familia.html";
        
                
    }
    @PostMapping("/eliminar-perfil")
    public String eliminar(HttpSession session, @RequestParam String id){
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            famS.eliminar(login.getId(),id);
            
        } catch (ErrorService ex) {
            Logger.getLogger(FamiliaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/familia/mi-familia";
    }
    

}
