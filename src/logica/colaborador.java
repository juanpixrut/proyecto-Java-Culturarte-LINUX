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
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;//este para los arrayList
import javax.persistence.Transient;

import javax.persistence.DiscriminatorValue; //supuestamente para la herencia JPA probemos ...

import javax.persistence.ElementCollection;

@Entity
@DiscriminatorValue(value = "colaborador")
public class colaborador extends usuario{
    //@OneToMany
    private List<colaboracion> colaboraciones;
    
    public colaborador(){
    
    }
    
    public colaborador(String nickname, String nombre, String apellido, String correo, byte[] imagenBytes){super(nickname, nombre, apellido, correo, imagenBytes);
    this.colaboraciones = new ArrayList<>();
    }
    
    public void colaborar(propuesta prop, float monto, String tipoRetorno){
        //evitar que colabore mas de una vez en la misma propuesta
        for(colaboracion c : colaboraciones){
        if(c.getPropuesta() == prop){
        System.out.println("Ya colaboraste con esta propuesta.");
        return;
           }
        }
        colaboracion c = new colaboracion(this, prop, monto, tipoRetorno);
        colaboraciones.add(c);
        prop.agregarColaboracion(c);
        System.out.println(getNickname() + " colaboro con $" + monto + " en " + prop.getTitulo());
    }
    
    public void mostrarColaboraciones(){
    System.out.println("Colaboraciones de " + nombre + ":");
    for(colaboracion colab : colaboraciones){
    System.out.println("- " + colab.getPropuesta().getTitulo() + ": $" + colab.getMontoAportado() + " | Retorno: " + colab.getTipoRetorno());
       }
    }
    
    public String toString(){
    return getNickname();
    }
    
}
