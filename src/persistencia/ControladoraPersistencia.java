/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

/**
 *
 * @author Juanpi
 */

import logica.usuario;
import logica.proponente;
import logica.colaborador;
import logica.categoria;
import logica.propuesta;
import logica.colaboracion;

import java.util.List;

import persistencia.exceptions.PreexistingEntityException; //para manejar mensajes de errores con try catch creo

//investigar sobre el uso de try catch y los exception.

public class ControladoraPersistencia {
    private usuarioJpaController usuJpa = new usuarioJpaController();
    private proponenteJpaController prop = new proponenteJpaController();
    private colaboradorJpaController colab = new colaboradorJpaController();
    private propuestaJpaController propue = new propuestaJpaController();
    private colaboracionJpaController c = new colaboracionJpaController();
    private categoriaJpaController categoria = new categoriaJpaController();
    private historialEstadoJpaController historial = new historialEstadoJpaController();
    
    public void crearUsuarioProponente(proponente usuarioProponente){
        try{
        prop.create(usuarioProponente);
        }catch(PreexistingEntityException e){
        //PK/Nickname ya existe
        throw new IllegalArgumentException("El nickname ya esta en uso", e);
        }catch(Exception e){
        throw new RuntimeException("No se pudo crear el usuario.", e);
        }
    }
    
    public List<proponente> listarProponentes(){
        return prop.findproponenteEntities();
}
    
    public void crearUsuarioColaborador(colaborador usuarioColaborador){
        try{
        colab.create(usuarioColaborador);
        }catch(PreexistingEntityException e){
        //PK/Nickname ya existe
        throw new IllegalArgumentException("El nickname ya esta en uso", e);
        }catch(Exception e){
        throw new RuntimeException("No se pudo crear el usuario.", e);
        }
    }
    
    public List<colaborador> listarColaboradores(){
    return colab.findcolaboradorEntities();
    }
    
    public void crearCategoria(categoria cat){
        try{
        categoria.create(cat);
        }catch(Exception e){
        throw new RuntimeException("No se pudo agregar.", e);
        }
    }
    
    public List<categoria> listarCategoria(){
    return categoria.findcategoriaEntities();
    }
    
    public categoria GetCategoria(String nombre){
        for(categoria c : categoria.findcategoriaEntities()){
           if(c.getNombre() != null && c.getNombre().equalsIgnoreCase(nombre)){
           return c;
           }
        }
        return null;
    }
    
    public void crearPropuesta(propuesta prop){
        try{
        propue.create(prop);
        }catch(Exception e){
        throw new RuntimeException("No se pudo agregar", e);
        }
    }
    
    public List<propuesta> listarPropuestas(){
    return propue.findpropuestaEntities();
    }
    
    public void modificoPropuesta(propuesta prop){
    try{
    propue.edit(prop);
    }catch(Exception e){
    throw new RuntimeException("No se pudo agregar.", e);
    }
    }
    
    public void crearColaboracion(colaboracion colab){
       try{
           c.create(colab);
       }catch(Exception e){
       throw new RuntimeException("No se pudo crear", e);
       }
    }
    
    public List<colaboracion> listarColaboraciones(){
    return c.findcolaboracionEntities();
    }
    
    public void eliminoColaboracion(int id){
        try{
        c.destroy(id);
        }catch(Exception e){
        throw new RuntimeException("No se pudo agregar.", e);
        }
    
    }
    
    public List<usuario> listarUsuarios(){
    return usuJpa.findusuarioEntities();
    }
    
    public void seguirUsuario(String seguidorNick, String seguidoNick){
    
        //
       usuario seguidor = usuJpa.findusuario(seguidorNick);
       usuario seguido = usuJpa.findusuario(seguidoNick);
       
       if (seguidor == null || seguido == null){
        throw new IllegalArgumentException("Usuario inexistente");
       }
       
       boolean agregado = seguidor.getSeguidos().add(seguido);
       seguido.getSeguidores().add(seguidor);
     
       if (!agregado) return; // ya lo seguia, nada q hacer
       
       try {
        usuJpa.edit(seguidor);
    } catch (persistencia.exceptions.NonexistentEntityException e) {
        throw new IllegalArgumentException("El seguidor ya no existe", e);
    } catch (Exception e) {
        throw new RuntimeException("No se pudo seguir al usuario", e);
    }
       
    }
    
    public void dejarDeSeguir(String seguidorNick, String seguidoNick){
        
        //
    usuario seguidor = usuJpa.findusuario(seguidorNick);
    usuario seguido  = usuJpa.findusuario(seguidoNick);
    
    if (seguidor == null || seguido == null){
        throw new IllegalArgumentException("Usuario inexistente");
       }

    seguidor.getSeguidos().remove(seguido);
    
    seguido.getSeguidores().remove(seguidor); // 


    try {
        usuJpa.edit(seguidor); // dueño
    } catch (Exception e) {
        throw new RuntimeException("No se pudo dejar de seguir", e);
    }
}
    
    
}




