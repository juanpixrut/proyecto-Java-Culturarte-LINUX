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
import java.time.LocalDate;
import java.time.ZoneId;

import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
        
@Entity
public class colaboracion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    @ManyToOne
    @JoinColumn(name = "colaborador_id")
    private colaborador colaborador;
    @ManyToOne
    @JoinColumn(name="propuesta_titulo")
    private propuesta propuesta;
    @Basic
    private float montoAportado;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic
    private String tipoRetorno; //entrada o porcentaje o ambas
    
    public colaboracion(){
    
    }
    
    public colaboracion(colaborador colab, propuesta prop, float montoAportado, String tipoRetorno){
    this.colaborador = colab;
    this.propuesta = prop;
    this.montoAportado = montoAportado;
    this.tipoRetorno = tipoRetorno;
    this.fecha = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()); //
    }
    
    public int getId(){
    return this.id;
    }
    
    public propuesta getPropuesta(){
    return propuesta;
    }
    
    public colaborador getColaborador(){
    return colaborador;
    }
    
    public Float getMontoAportado(){
    return montoAportado;
    }
    
    public String getTipoRetorno(){
    return tipoRetorno;
    }
    
    public Date getFecha(){
    return fecha;
    }
    
}
