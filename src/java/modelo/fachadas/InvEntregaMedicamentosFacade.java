/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.InvEntregaMedicamentos;

/**
 *
 * @author miguel
 */
@Stateless
public class InvEntregaMedicamentosFacade extends AbstractFacade<InvEntregaMedicamentos> {

    public InvEntregaMedicamentosFacade() {
        super(InvEntregaMedicamentos.class);
    }
    
    public InvEntregaMedicamentos entregaMedicamentoXRegistro(int registros){
        try {
            String hql  ="SELECT i FROM InvEntregaMedicamentos i WHERE i.idHistoriaClinica.idRegistro=:idRegistro and i.estado='P'";
            Query query = getEntityManager().createQuery(hql).setParameter("idRegistro", registros);
            return (InvEntregaMedicamentos)query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
