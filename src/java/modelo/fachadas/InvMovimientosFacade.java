/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.entidades.InvMovimientos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvMovimientosFacade extends AbstractFacade<InvMovimientos> {

    public InvMovimientosFacade() {
        super(InvMovimientos.class);
    }
    
    public List<InvMovimientos> getMovimientoTipoEmpresa(int idEmpresa, String tipo){
        try {
            String hql = "SELECT i FROM InvMovimientos i where i.idEmpresa.codEmpresa= :idEmpresa and i.tipoMovimiento=:tipo";
            Query query = getEntityManager().createQuery(hql).setParameter("idEmpresa", idEmpresa).setParameter("tipo", tipo);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public InvMovimientos getMovimientoNumero(String numero, String tipo, int idBodegaDestino){
        try {
            String hql = "SELECT i FROM InvMovimientos i where i.numeroDocumento= :numeroDocumento and i.tipoMovimiento=:tipo and i.idBodegaDestino.idBodega=:idBodegaDestino";
            Query query = getEntityManager().createQuery(hql).setParameter("numeroDocumento", numero).setParameter("tipo", tipo).setParameter("idBodegaDestino", idBodegaDestino);
            return (InvMovimientos)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    public InvMovimientos getMovimientoNumeroBodegaOrigen(String numero, String tipo, int idBodegaOrigen){
        try {
            String hql = "SELECT i FROM InvMovimientos i where i.numeroDocumento= :numeroDocumento and i.tipoMovimiento=:tipo and i.idBodegaOrigen.idBodega=:idBodegaOrigen";
            Query query = getEntityManager().createQuery(hql).setParameter("numeroDocumento", numero).setParameter("tipo", tipo).setParameter("idBodegaOrigen", idBodegaOrigen);
            return (InvMovimientos)query.getSingleResult();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public InvMovimientos getMovimientoNumeroBodegaDestinoNroSalida(String numero, String tipo, int idBodegaOrigen){
        try {
            String hql = "SELECT i FROM InvMovimientos i where i.numeroDocumento= :numeroDocumento and i.tipoMovimiento=:tipo and i.idBodegaOrigen.idBodega=:idBodegaOrigen";
            Query query = getEntityManager().createQuery(hql).setParameter("numeroDocumento", numero).setParameter("tipo", tipo).setParameter("idBodegaOrigen", idBodegaOrigen);
            return (InvMovimientos)query.getSingleResult();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public InvMovimientos getMovimientoOrden(int nroOrden){
        try {
            String hql = "SELECT i FROM InvMovimientos i where i.idOrdenCompra.idOrdenCompra=:orden";
            Query query = getEntityManager().createQuery(hql).setParameter("orden", nroOrden);
            return (InvMovimientos)query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
