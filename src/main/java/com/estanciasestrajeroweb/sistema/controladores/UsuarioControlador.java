/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.controladores;

import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.enumeraciones.TipoUsuario;
import com.estanciasestrajeroweb.sistema.excepciones.ErrorService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    
    @Autowired
    private UsuarioServicio uS;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/crearAltaFamilia")
    public String crearFamilia(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.buscarUsuarioPorId(id);
            usuario.setTipoUsuario(TipoUsuario.FAMILIA);
            model.addAttribute("usuario", usuario);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/familia/registrar";
                
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA', 'ROLE_CLIENTE')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, @RequestParam String id, ModelMap model, @RequestParam(required = false) String action) {
        if(action==null){
            action="";
        }

        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }        

        try {
            Usuario usuario = uS.buscarUsuarioPorId(id);
            model.addAttribute("perfil", usuario);
        } catch (ErrorService e) {
            model.addAttribute("error", e.getMessage());
        }
        model.put("action", action);
        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA', 'ROLE_CLIENTE')")
    @PostMapping("/actualizar-perfil")
    public String registrar(ModelMap modelo, HttpSession session, MultipartFile archivo, @RequestParam String id, @RequestParam String alias, @RequestParam String email, @RequestParam String clave) {

        Usuario usuario = null;
        try {

            Usuario login = (Usuario) session.getAttribute("usuariosession");
            if (login == null || !login.getId().equals(id)) {
                return "redirect:/inicio";
            }

            usuario = uS.buscarUsuarioPorId(id);
            uS.modificarUsuario(id, archivo, alias, email, clave);
            session.setAttribute("usuariosession", usuario);
            modelo.put("action","Actualizar");
            return "redirect:/inicio";
        } catch (ErrorService ex) {            
            modelo.put("error", ex.getMessage());
            modelo.put("perfil", usuario);

            return "perfil.html";
        }

    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA', 'ROLE_CLIENTE')")
    @GetMapping("/listar-usuarios")
    public String ListarUsuarios(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.buscarUsuarioPorId(id);
            List<Usuario>todos=uS.listarUsuarios();
            model.addAttribute("usuarios", todos);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "carga_usuarios";
                
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA', 'ROLE_CLIENTE')")
    @GetMapping("/ajustes-perfil")
    public String ajustesdeUsuarios(HttpSession session, @RequestParam String id, ModelMap model){
    Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }
        return "opcion.html";
        
}
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/baja/{id}")
    public String bajaUsuarios(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.buscarUsuarioPorId(id);          
            model.addAttribute("usuario", usuario);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "perfil_baja.html";
                
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/baja{id}")
    public String bajadefinitivaUsuarios(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.deshabilitarUsuario(id);          
            model.addAttribute("usuario", usuario);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/inicio";
                
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @GetMapping("/alta{id}")
    public String altaDeUsuario(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.habilitarUsuario(id);          
            model.addAttribute("usuario", usuario);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/inicio";
                
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/usuariofamilia")
    public String irAFamilia(HttpSession session, @RequestParam String id, ModelMap model) {
        
        Usuario login = (Usuario) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/inicio";
        }

        try {
            Usuario usuario = uS.habilitarUsuario(id);          
            model.addAttribute("usuario", usuario);
            
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/familia/registrar";
                
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @DeleteMapping("/baja-usuarios")
    public String eliminar(HttpSession session, @RequestParam String id){
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            uS.deshabilitarUsuario(login.getId());
            
        } catch (ErrorService ex) {
            Logger.getLogger(FamiliaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/usuario/listar-usuarios";
    }
    @PreAuthorize("hasAnyRole('ROLE_USUARIO','ROLE_FAMILIA')")
    @PostMapping("/alta-perfil")
    public String alta(HttpSession session){
        try {
            Usuario login = (Usuario) session.getAttribute("usuariosession");
            
            uS.habilitarUsuario(login.getId());
            
        } catch (ErrorService ex) {
            Logger.getLogger(FamiliaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/inicio";
    }
}
