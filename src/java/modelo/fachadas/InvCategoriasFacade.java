/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.entidades.InvCategorias;

/**
 *
 * @author miguel
 */
@Stateless
public class InvCategoriasFacade extends AbstractFacade<InvCategorias> {

    public InvCategoriasFacade() {
        super(InvCategorias.class);
    }

    public List<InvCategorias> getActivos(){
        try {
            String hql = "SELECT i FROM InvCategorias i where i.activo=1";
            Query q = getEntityManager().createQuery(hql);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger("Error clase "+InvCategoriasFacade.class,e.toString());
        }
        return new ArrayList<>();
    }
}
