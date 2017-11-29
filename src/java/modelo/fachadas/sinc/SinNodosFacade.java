/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas.sinc;

import modelo.fachadas.*;
import javax.ejb.Stateless;
import modelo.entidades.sinc.SinNodos;

/**
 *
 * @author santos
 */
@Stateless
public class SinNodosFacade extends AbstractFacade<SinNodos> {

    public SinNodosFacade() {
        super(SinNodos.class);
    }


}
