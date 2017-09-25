/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgHabitacion;

/**
 *
 * @author santos
 */
@Stateless
public class CfgHabitacionFacade extends AbstractFacade<CfgHabitacion> {
    
    public CfgHabitacionFacade() {
        super(CfgHabitacion.class);
    }
    
    public void eliminarHabitacion(CfgHabitacion cfgHabitacion) {
        remove(cfgHabitacion);
    }
}
