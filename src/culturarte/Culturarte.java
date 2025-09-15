/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package culturarte;

/**
 *
 * @author Juanpi
 */

import logica.fabrica;
import logica.ictrl;

import persistencia.ControladoraPersistencia;

import presentacion.Pantalla;

public class Culturarte {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Pantalla panta = new Pantalla();
        panta.setVisible(true); //no se la diferencia con .show que use en QT c++, investigar.
        panta.setLocationRelativeTo(null); //ponela relativa a nada. ponerla en el medio y listo.
        
    }
    
}
