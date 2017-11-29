/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacFacturaInsumo;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaInsumoFacade extends AbstractFacade<FacFacturaInsumo> {

    public FacFacturaInsumoFacade() {
        super(FacFacturaInsumo.class);
    }

    public FacFacturaInsumo consultarFacFacturaInsumo(int id) {
        FacFacturaInsumo obj = null;
        try {
            String sql = "SELECT c FROM FacFacturaInsumo c WHERE c.idSincronizador = ?1 ";
            Query query = getEntityManager().createQuery(sql);
            query.setParameter(1, id);
            obj = (FacFacturaInsumo) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }
}
