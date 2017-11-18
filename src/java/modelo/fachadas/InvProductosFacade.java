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
import modelo.entidades.InvProductos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvProductosFacade extends AbstractFacade<InvProductos> {

    public InvProductosFacade() {
        super(InvProductos.class);
    }
    public List<InvProductos> getProductoCategorias(int categoria){
        try {
            String hql = "SELECT i from InvProductos i where i.idCategoria.idCategoria=:categoria";
            Query query = getEntityManager().createQuery(hql).setParameter("categoria", categoria);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger("Error clase "+InvProductosFacade.class,e.toString());
        }
        return new ArrayList<>();
    }
    public List<InvProductos> getProductosLotesActivos(){
        try {
            String hql = "SELECT i from InvProductos i where i.activo=1 and i.lote=1";
            Query query = getEntityManager().createQuery(hql);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger("Error clase "+InvProductosFacade.class,e.toString());
        }
        return new ArrayList<>();
    }
    public List<InvProductos> getProductosActivos(){
        try {
            String hql = "SELECT i from InvProductos i where i.activo=1";
            Query query = getEntityManager().createQuery(hql);
            return query.getResultList();
        } catch (Exception e) {
            Logger.getLogger("Error clase "+InvProductosFacade.class,e.toString());
        }
        return new ArrayList<>();
    }
    
    public List<Object> dataReporteMovimientoInventario(String _query){
         Query query = getEntityManager().createNativeQuery(_query);
         return query.getResultList();
        
    }
}
