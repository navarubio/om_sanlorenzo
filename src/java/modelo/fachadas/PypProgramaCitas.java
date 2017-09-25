/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import modelo.entidades.PypProgramaCita;
/**
 *
 * @author cores
 */
@Stateless
public class PypProgramaCitas extends AbstractFacade<PypProgramaCita>  {

    public PypProgramaCitas() {
        super(PypProgramaCita.class);
    }
    
}
