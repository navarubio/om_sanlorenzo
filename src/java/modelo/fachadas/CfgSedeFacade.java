/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.entidades.CfgSede;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

/**
 *
 * @author santos
 */
@Stateless
public class CfgSedeFacade extends AbstractFacade<CfgSede> {

    public CfgSedeFacade() {
        super(CfgSede.class);
    }

    public List<CfgSede> buscarOrdenado() {
        try {
            String hql = "SELECT a FROM CfgSede a ORDER BY a.idSede ASC";
            return getEntityManager().createQuery(hql).getResultList();
            
        } catch (Exception e) {
            return null;
        }
    }
    

    public CfgSede getSede(String codigo, String nombre) {
        EntityManager emT = this.getEntityManager();
        try {
            TypedQuery<CfgSede> query = emT.createNamedQuery("CfgSede.findSedeByCodigoAndNombre", CfgSede.class);
            query.setParameter("codigo", codigo);
            query.setParameter("nombre", nombre);
            query.setMaxResults(1);
            List result = query.getResultList();
            if (result.isEmpty()) {
                return new CfgSede();
            } else {
                return query.getSingleResult();
            }
        } catch (PersistenceException ex) {
            Logger.getLogger(CfgSede.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
}

