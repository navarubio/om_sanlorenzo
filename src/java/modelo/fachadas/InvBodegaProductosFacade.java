/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.InvBodegaProductos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvBodegaProductosFacade extends AbstractFacade<InvBodegaProductos> {

    
    public InvBodegaProductosFacade() {
        super(InvBodegaProductos.class);
    }
    
    public InvBodegaProductos getBodegaProductoLote(int idBodega, int idProducto,int idLote){
        try {
            String hql = "SELECT i FROM InvBodegaProductos i where i.idBodega.idBodega=:idBodega and i.idProducto.idProducto=:idProducto and i.idLote.idLote=:idLote";
            Query query = getEntityManager().createQuery(hql).setParameter("idBodega", idBodega).setParameter("idProducto", idProducto).setParameter("idLote",idLote);
            return (InvBodegaProductos)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    public InvBodegaProductos getBodegaProducto(int idBodega, int idProducto){
        try {
            String hql = "SELECT i FROM InvBodegaProductos i where i.idBodega.idBodega=:idBodega and i.idProducto.idProducto=:idProducto";
            Query query = getEntityManager().createQuery(hql).setParameter("idBodega", idBodega).setParameter("idProducto", idProducto);
            return (InvBodegaProductos)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    public List<InvBodegaProductos> getProductosBodegasExistencia(int idBodega){
        try {
            String hql ="SELECT i FROM InvBodegaProductos i where i.idBodega.idBodega=:idBodega and i.existencia>0";
            Query query = getEntityManager().createQuery(hql).setParameter("idBodega", idBodega);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
    
    public List<InvBodegaProductos> getProductosBodegasExistenciaXlote(int idBodega,int idLote){
        try {
            String hql ="SELECT i FROM InvBodegaProductos i where i.idBodega.idBodega=:idBodega and i.existencia>0 and i.idLote.idLote=:idLote";
            Query query = getEntityManager().createQuery(hql).setParameter("idBodega", idBodega).setParameter("idLote",idLote);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
    public List<InvBodegaProductos> getProductosBodegas(int idBodega){
        try {
            String hql ="SELECT i FROM InvBodegaProductos i ";
            if(idBodega!=0)hql +="where i.idBodega.idBodega=:idBodega";
            hql +=" ORDER BY i.idBodega.idBodega ";
            Query query = getEntityManager().createQuery(hql);
            if(idBodega!=0)
                query.setParameter("idBodega", idBodega);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
   
    }
    public List<InvBodegaProductos> getProductosBodegasXLote(int idBodega, int idLote){
        try {
            String hql ="SELECT i FROM InvBodegaProductos i where i.idBodega.idBodega=:idBodega and i.idLote.idLote=:idLote";
            Query query = getEntityManager().createQuery(hql).setParameter("idBodega", idBodega).setParameter("idLote", idLote);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
   
    }
    
    
    
        /**
     * Método para consultar medicamentos según el texto enviado por parámetro.
     *
     * @param txt parámetro por el que se hace la búsqueda.
     * @return Listado de mediacamentos encontrados.
     */
    public List<String> autocompletarMedicamentos(String txt) {
        try {

             String hql = "SELECT CONCAT('(',t.idProducto.idProducto,')',t.idProducto.codigo,' - ', t.idProducto.nombre ,' - ', t.existencia,' - Lote:',t.idLote.codigo) FROM InvBodegaProductos t "
                    + "WHERE t.idProducto.idCategoria.idCategoria=1 and t.idProducto.productoPos=true and t.idProducto.activo=true and "
                    + "UPPER(CONCAT(t.idProducto.codigo,' - ', t.idProducto.nombre ,' - ', t.existencia)) LIKE '%" + txt + "%'";
            
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
            return null;
        }
    }

}
