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
import modelo.entidades.InvProveedores;

/**
 *
 * @author miguel
 */
@Stateless
public class InvProveedoresFacade extends AbstractFacade<InvProveedores> {

    public InvProveedoresFacade() {
        super(InvProveedores.class);
    }

    public InvProveedores getProveedorXCodigo(String codigo){
        try {
            String hql ="SELECT i FROM InvProveedores i where i.codigoProveedor=:codigo";
            Query query = getEntityManager().createQuery(hql).setParameter("codigo", codigo);
            return (InvProveedores)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }   
    
    public List<InvProveedores> getActivos(){
        try {
            String hql = "SELECT i FROM InvProveedores i where i.activo=1";
            Query query = getEntityManager().createQuery(hql);
            return query.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
}
