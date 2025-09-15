/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Juanpi
 */

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.JoinColumn;
import javax.persistence.ForeignKey;

import javax.persistence.CascadeType;

@Entity
public class categoria implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Basic
    private String nombre;
    
    // LADO DUEÑO (guarda la FK):
    @ManyToOne
    @JoinColumn(name = "padre_id", referencedColumnName = "id")
    private categoria padre; //null = raiz
    
    @OneToMany(mappedBy="padre", cascade = CascadeType.ALL)
    private List<categoria> hijos = new ArrayList<>();
    
    public categoria(){
    
    }
    
    public categoria(String nombre){
    this.nombre = nombre;
    this.padre = null;
    }
    
    public int getId(){
    return id;
    }
    
    public String getNombre(){
    return nombre;
    }
    
    public void setPadre(categoria padre){
    this.padre = padre;
    }
    
    public categoria getPadre(){
    return this.padre;
    }
    
    public String toString() {
        return nombre; // asi el JTree muestra el nombre
    }
    
}
