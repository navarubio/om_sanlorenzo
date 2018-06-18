/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelo.entidades.InvLotes;

/**
 *
 * @author miguel
 */
@Stateless
public class InvLotesFacade extends AbstractFacade<InvLotes> {

    public InvLotesFacade() {
        super(InvLotes.class);
    }

    public List<InvLotes> getLotesSinVencer(int idEmpresa){
        List<InvLotes> lista = new ArrayList<>();
        String hql = "SELECT i FROM InvLotes i WHERE i.fechaVencimiento>=:fechaVencimiento "
                //+ "and i.idEmpresa.codEmpresa=:idEmpresa"
                ;
        Query query = getEntityManager().createQuery(hql).setParameter("fechaVencimiento", new Date())
                //.setParameter("idEmpresa",idEmpresa)
                ;
        
        return query.getResultList();
    }
    
    public InvLotes getLoteXCodigo(String codigo){
        String hql = "SELECT i FROM InvLotes i WHERE i.codigo=:codigo";
        Query query = getEntityManager().createQuery(hql).setParameter("codigo",codigo);
        List<InvLotes> lista =query.getResultList();
        
        return lista.size() > 0 ? lista.get(0) : null;
    }

    public int idLote(int idProducto){
        int idLote = 0;
        try {
            String sql  ="select min(i.id_lote) from inv_bodega_productos  b " +
                        "inner join inv_lotes i on i.id_lote =b.id_lote " +
                        "where id_producto  =? and fecha_vencimiento>=CURRENT_DATE";
            Query query  = getEntityManager().createNativeQuery(sql).setParameter(1, idProducto);
            idLote = Integer.parseInt(query.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idLote;
    }
}
