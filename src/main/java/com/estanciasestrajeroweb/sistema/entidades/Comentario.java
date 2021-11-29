/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.estanciasestrajeroweb.sistema.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="comentario")
public class Comentario {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    private String id;
    
    private String descripcion;
    
    @ManyToOne
    private Casa casa;

    public Comentario() {
    }

    public Comentario(String id, String descripcion, Casa casa) {
        this.id = id;
        this.descripcion = descripcion;
        this.casa = casa;
    }

    public Casa getCasa() {
        return casa;
    }
    
    
    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
