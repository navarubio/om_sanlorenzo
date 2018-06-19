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

import modelo.entidades.InvBodegas;

/**
 *
 * @author miguel
 */
@Stateless
public class InvBodegasFacade extends AbstractFacade<InvBodegas> {

    public InvBodegasFacade() {
        super(InvBodegas.class);
    }
   
    public InvBodegas bodegaPrincipal(int idEmpresa, int tipo){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.idEmpresa.codEmpresa=:idEmpresa and i.principal=1 and i.tipo=:tipo";
            Query query = getEntityManager().createQuery(hql).setParameter("idEmpresa", idEmpresa).setParameter("tipo", tipo);
            return (InvBodegas)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
    public List<InvBodegas> bodegaEmpresas(int idEmpresa){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.idEmpresa.codEmpresa=:idEmpresa";
            Query query = getEntityManager().createQuery(hql).setParameter("idEmpresa", idEmpresa);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public List<InvBodegas> bodegaActivasEmpresas(int idEmpresa){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.idEmpresa.codEmpresa=:idEmpresa and i.activo=1";
            Query query = getEntityManager().createQuery(hql).setParameter("idEmpresa", idEmpresa);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<InvBodegas> bodegaActivasEmpresasDiferenteOrigen(int idEmpresa,int bodegaOrigen){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.idEmpresa.codEmpresa=:idEmpresa and i.activo=1 and i.idBodega!=:bodegaOrigen";
            Query query = getEntityManager().createQuery(hql).setParameter("idEmpresa", idEmpresa).setParameter("bodegaOrigen", bodegaOrigen);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public InvBodegas bodegaUsuarioReponsable(int idUsuario){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.responsable.idUsuario=:idUsuario";
            Query query = getEntityManager().createQuery(hql).setParameter("idUsuario", idUsuario);
            return (InvBodegas)query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Responsable de Bodega "+idUsuario+" invalido");
            return null;
        }
    }
	
	public InvBodegas bodegaPorSede(int idSede){
        try {
            String hql = "SELECT i FROM InvBodegas i WHERE i.idSede.idSede=:idSede";
            Query query = getEntityManager().createQuery(hql).setParameter("idSede", idSede);
            return (InvBodegas)query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Bodega "+idSede+" invalido");
            return null;
        }
    }
    
    /**
     * Método para consultar medicamentos según el texto enviado por parámetro.
     *
     * @param txt parámetro por el que se hace la búsqueda.
     * @return Listado de mediacamentos encontrados.
     */
    public List<String> autocompletarMedicamentos(String txt) {
        try {

             String hql = "SELECT CONCAT('(',t.idProducto.idProducto,')',t.idProducto.codigo,' - ', t.idProducto.nombre ,' - ', t.existencia) FROM InvBodegaProductos t "
                    + "WHERE t.idProducto.idCategoria.idCategoria=1 and t.idProducto.productoPos=true and t.idProducto.activo=true and "
                    + "UPPER(CONCAT(t.idProducto.codigo,' - ', t.idProducto.nombre ,' - ', t.existencia)) LIKE '%" + txt + "%'";
            
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            System.out.println("error: "+e.getMessage());
            return null;
        }
    }
    
}
