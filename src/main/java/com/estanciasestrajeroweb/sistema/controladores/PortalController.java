/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.controladores;

import com.estanciasestrajeroweb.sistema.entidades.Usuario;
import com.estanciasestrajeroweb.sistema.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PortalController {
    @Autowired
    private UsuarioServicio uS;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USUARIO', 'ROLE_FAMILIA', 'ROLE_CLIENTE')")
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio.html";
    }
    @GetMapping("/familia")
    public String irFamilia(){
        return "familia.html";
    } 
    
    @GetMapping("/registro")
    public String irARegistrar(ModelMap modelo){
        try {
        Usuario usuario=new Usuario();
        modelo.addAttribute("usuario", usuario);
        return "registro.html";
        } catch (Exception e) {
            return "index.html";
        }
        
    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Usuario o clave incorrectos");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente.");
        }
        return "login.html";
    }
    @PostMapping("/registro")
    public String registrar(ModelMap modelo,MultipartFile archivo, @RequestParam(required=false)String alias, @RequestParam(required=false)String email,
            @RequestParam(required=false)String clave){
        try {
            uS.registrarUsuario(archivo, alias, email, clave);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("archivo", archivo);
            modelo.put("alias", alias);
            modelo.put("email", email);
            modelo.put("clave", clave);
            return "registro.html";
            
            
        }
        modelo.put("exito", "Bienvenido a HouseMax");
        modelo.put("mensaje", "Inicia Sesión por favor");
        return "aviso.html";
        
    }
}
