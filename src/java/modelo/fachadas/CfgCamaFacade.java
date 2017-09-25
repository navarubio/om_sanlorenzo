/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.CfgCama;

/**
 *
 * @author santos
 */
@Stateless
public class CfgCamaFacade extends AbstractFacade<CfgCama> {

    public CfgCamaFacade() {
        super(CfgCama.class);
    }

    public void eliminarHabitacion(CfgCama cfgCama) {
        remove(cfgCama);
    }
}
