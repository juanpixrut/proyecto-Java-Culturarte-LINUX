/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Juanpi
 */

import persistencia.ControladoraPersistencia;

public class ctrl extends ictrl{
       
    public static ctrl getInstancia(){
        if(instancia == null){
        instancia = new ctrl();
        }
        return instancia;
    }
    
    private static ctrl instancia = null;
    
    private ctrl(){
    
    }
}
