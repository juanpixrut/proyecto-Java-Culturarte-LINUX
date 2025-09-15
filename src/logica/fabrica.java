/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

/**
 *
 * @author Juanpi
 */
public class fabrica {
    
    public fabrica(){
    
    }
    
    public ictrl getIctrl(){
    return ctrl.getInstancia();
    }
    
}
