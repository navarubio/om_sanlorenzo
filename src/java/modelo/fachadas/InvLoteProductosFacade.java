/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelo.entidades.InvLoteProductos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvLoteProductosFacade extends AbstractFacade<InvLoteProductos> {

    public InvLoteProductosFacade() {
        super(InvLoteProductos.class);
    }
    
}
