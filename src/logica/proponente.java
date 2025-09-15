/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Juanpi
 */

import java.util.ArrayList;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.PrimaryKeyJoinColumn; //este para herencia
import javax.persistence.OneToMany; //este para los arrayList
import javax.persistence.Transient;

import javax.persistence.DiscriminatorValue; //supuestamente para la herencia JPA probemos ...

import javax.persistence.Column;
import javax.persistence.Lob;

@Entity
@DiscriminatorValue(value = "proponente")
//@PrimaryKeyJoinColumn(name = "nickname") //misma PK que usuario
public class proponente extends usuario{
    @Basic
    private String direccion;
    @Lob
    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;
    @Basic
    private String linkSitio;
    
    //@OneToMany
    @Transient //sirve para no implementar todavia en la bd creo
    private ArrayList<propuesta> propuestas;
    
    public proponente(){
    
    }
    
    public proponente(String nickname, String nombre, String apellido, String correo, byte[] imagenBytes, String direccion, String biografia, String linkSitio){super(nickname, nombre, apellido, correo, imagenBytes);
    this.direccion = direccion;
    this.biografia = biografia;
    this.linkSitio = linkSitio;
    this.propuestas = new ArrayList<>();
    }
    
    public void agregarPropuesta(propuesta prop){
        //propuestas.add(prop);
    }
    public void mostrarPropuestas(){
    System.out.println("Propuestas de " + nombre + ":");
    for(propuesta p : propuestas){
        System.out.println("- " + p.getTitulo() + " | Estado: " + p.getEstadoActual());
    }
    }
    
    public String getDireccion(){
    return this.direccion;
    }
    
    public String getBiografia(){
    return this.biografia;
    }
    
    public String getLink(){
    return this.linkSitio;
    }
    
}
