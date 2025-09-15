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
import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Basic;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;
import javax.persistence.ElementCollection;

import javax.persistence.Transient;

import java.time.ZoneId;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import javax.persistence.CascadeType;

import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.persistence.Column;
import javax.persistence.Lob;

@Entity
public class propuesta implements Serializable {
    @ManyToOne
    @JoinColumn(name="proponente_id")
    private proponente prop;
    @Id
    private String titulo;
    @Lob
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    @Basic
    private byte[] imagen; 
    private String tipoEspectaculo; //teatro, musica, literatura, cine, danza, standup
    private String lugar;
    @Temporal(TemporalType.DATE)
    private Date fechaRealizacion;
    @Basic
    private float precioEntrada;
    private float montoNecesario;
    private float montoRecaudado;
    @Temporal(TemporalType.DATE)
    private Date fechaPublicada;
    
    @Enumerated(EnumType.STRING)
    private estadoPropuesta estadoActual;
    
    @OneToMany(mappedBy="propuesta", cascade = CascadeType.ALL)
    private List<historialEstado> historial;
    
    @OneToMany(mappedBy = "propuesta")
    private List<colaboracion> colaboraciones;
    
    //prueba. esta prop tiene tipo d retorno q ofrecer.
    @Basic
    private String tipoRetorno; //entrada o porcentaje o ambas
    
    public propuesta(){
    
    }
    
    public propuesta(proponente proponente, String titulo, String descripcion, String tipoEspectaculo, String lugar, Date fechaRealizacion, float precioEntrada, float montoNecesario, String tipoRetorno){
    this.prop = proponente;
    this.titulo = titulo;
    this.descripcion = descripcion;
    this.tipoEspectaculo = tipoEspectaculo;
    this.lugar = lugar;
    this.fechaRealizacion = fechaRealizacion;
    this.precioEntrada = precioEntrada;
    this.montoNecesario = montoNecesario;
    this.montoRecaudado = 0;
    this.tipoRetorno = tipoRetorno;
    this.fechaPublicada = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()); //
    
    this.estadoActual = estadoPropuesta.INGRESADA;
    this.historial = new ArrayList<>();
    this.historial.add(new historialEstado(this, this.estadoActual));
    this.colaboraciones = new ArrayList<>();
    }
    
    public String getTitulo(){
        return titulo;
    }
    
    public estadoPropuesta getEstadoActual(){
    return estadoActual;
    }
    
    public void cambiarEstado(estadoPropuesta nuevo){
        this.estadoActual = nuevo;
        historial.add(new historialEstado(this, nuevo));
    }
    
    public void registrarCambioEstado(estadoPropuesta nuevo) {
        if (!historial.isEmpty()) {
            historialEstado ultimo = historial.get(historial.size() - 1);
            if (ultimo.getEstado() == nuevo) return; // evitar duplicar mismo estado
        }
        
        historialEstado h = new historialEstado();
        h.setPropuesta(this);  
        h.setEstado(nuevo);
        h.setFecha(new Date());
        historial.add(h);                  
        this.estadoActual = nuevo;           
        
    }
    
    public void agregarColaboracion(colaboracion colab){
    colaboraciones.add(colab);
    }
    
    public void mostrarColaboraciones(){
    System.out.println("Colaboraciones en " + titulo + ":");
    for(colaboracion c : colaboraciones){
        System.out.println(c.getColaborador().getNickname() + "aporto $" + c.getMontoAportado());
    }
    }
    
    public String getProponente(){
    return this.prop.getNickname();
    }
    
    public String getDescripcion(){
    return this.descripcion;
    }
    
    public String getLugar(){
    return this.lugar;
    }
    
    public String getTipo(){
    return this.tipoEspectaculo;
    }
    
    public Float getEntrada(){
    return this.precioEntrada;
    }
    
    public Date getFecha(){
    return this.fechaRealizacion;
    }
    
    public Float getMonto(){
    return this.montoNecesario;
    }
    
    public void setDescripcion(String desc){
    this.descripcion = desc;
    }
    
    public void setTipo(String nuevoTipo){
    this.tipoEspectaculo = nuevoTipo;
    }
    
    public void setLugar(String nuevoLugar){
    this.lugar = nuevoLugar;
    }
    
    public void setPrecio(float nuevoPrecio){
    this.precioEntrada = nuevoPrecio;
    }
    
    public void setMonto(float nuevoMonto){
    this.montoNecesario = nuevoMonto;
    }
    
    public void setFecha(Date nuevaFecha){
    this.fechaRealizacion = nuevaFecha;
    }
    
    public ImageIcon bytesAIcon(){
    if(imagen == null || imagen.length == 0) return null;
    try{
    BufferedImage src = ImageIO.read(new ByteArrayInputStream(imagen));
    if(src == null) return null;
    return new ImageIcon(src);
    }catch(Exception e){
    return null;
    }
    }
    
    public Float getRecaudado(){
    return this.montoRecaudado;
    }
    
    public void setMontoRecaudado(Float monto){
    this.montoRecaudado += monto;
    }
    
    public String getTipoRetorno(){
    return this.tipoRetorno;
    }
    
    public void setEstado(estadoPropuesta estado){
    this.estadoActual = estado;
    }
    
    public void setImagen(byte[] imagen){
    this.imagen = imagen;
    }
    
}