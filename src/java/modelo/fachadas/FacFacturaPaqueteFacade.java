/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacFacturaPaquete;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaPaqueteFacade extends AbstractFacade<FacFacturaPaquete> {

    public FacFacturaPaqueteFacade() {
        super(FacFacturaPaquete.class);
    }

    
     public FacFacturaPaquete consultarFacFacturaPaq(int id) {
        FacFacturaPaquete obj = null;
        try {
            String sql = "SELECT c FROM FacFacturaPaquete c WHERE c.idSincronizador = ?1 ";
            Query query = getEntityManager().createQuery(sql);
            query.setParameter(1, id);
            obj = (FacFacturaPaquete) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }
}
