/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacFacturaServicio;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaServicioFacade extends AbstractFacade<FacFacturaServicio> {

    public FacFacturaServicioFacade() {
        super(FacFacturaServicio.class);
    }
    
       public FacFacturaServicio consultarFacFacturaSer(int id) {
        FacFacturaServicio obj = null;
        try {
            String sql = "SELECT c FROM FacFacturaServicio c WHERE c.idSincronizador = ?1 ";
            Query query = getEntityManager().createQuery(sql);
            query.setParameter(1, id);
            obj = (FacFacturaServicio) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }
       
}
