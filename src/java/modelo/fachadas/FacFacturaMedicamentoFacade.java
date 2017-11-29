/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacFacturaMedicamento;

/**
 *
 * @author santos
 */
@Stateless
public class FacFacturaMedicamentoFacade extends AbstractFacade<FacFacturaMedicamento> {
    
    public FacFacturaMedicamentoFacade() {
        super(FacFacturaMedicamento.class);
    }
    
     public FacFacturaMedicamento consultarFacFacturaMed(int id) {
        FacFacturaMedicamento obj = null;
        try {
            String sql = "SELECT c FROM FacFacturaMedicamento c WHERE c.idSincronizador = ?1 ";
            Query query = getEntityManager().createQuery(sql);
            query.setParameter(1, id);
            obj = (FacFacturaMedicamento) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }
    
}
