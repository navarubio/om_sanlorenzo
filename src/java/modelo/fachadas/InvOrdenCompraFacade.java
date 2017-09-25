/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.InvOrdenCompra;

/**
 *
 * @author miguel
 */
@Stateless
public class InvOrdenCompraFacade extends AbstractFacade<InvOrdenCompra> {

    public InvOrdenCompraFacade() {
        super(InvOrdenCompra.class);
    }
  
    public List<InvOrdenCompra> getComprasXEstado(String estado){
        try {
            String hql = "SELECT i FROM InvOrdenCompra i where i.estado=:estado";
            Query query = getEntityManager().createQuery(hql).setParameter("estado", estado);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger(InvOrdenCompraFacade.class.getName()).log(Level.SEVERE, null, e);
        }
        return new ArrayList<>();
    }
    public InvOrdenCompra getCompraXDocumento(String nroDocumento){
        try {
            String hql = "SELECT i FROM InvOrdenCompra i where i.nroDocumento=:nroDocumento";
            Query query = getEntityManager().createQuery(hql).setParameter("nroDocumento", nroDocumento);
            return (InvOrdenCompra) query.getSingleResult();
        } catch (Exception e) {
            
        }
        return null;
    }
}
