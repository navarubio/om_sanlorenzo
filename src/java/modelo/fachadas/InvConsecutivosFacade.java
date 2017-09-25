/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.entidades.InvConsecutivos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvConsecutivosFacade extends AbstractFacade<InvConsecutivos> {

    public InvConsecutivosFacade() {
        super(InvConsecutivos.class);
    }
    
    public InvConsecutivos getConsecutivoTipo(String tipo){
        try {
            String hql  ="SELECT i FROM InvConsecutivos i where i.tipo=:tipo";
            Query query = getEntityManager().createQuery(hql).setParameter("tipo", tipo);
            return (InvConsecutivos)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
