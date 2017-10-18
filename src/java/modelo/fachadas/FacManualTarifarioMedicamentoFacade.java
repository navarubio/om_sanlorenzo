/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.FacManualTarifarioMedicamento;

/**
 *
 * @author santos
 */
@Stateless
public class FacManualTarifarioMedicamentoFacade extends AbstractFacade<FacManualTarifarioMedicamento> {

    public FacManualTarifarioMedicamentoFacade() {
        super(FacManualTarifarioMedicamento.class);
    }

    public List<FacManualTarifarioMedicamento> buscarPorManualTarifario(Integer idManual) {
        Query query;
        try {
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioMedicamento m WHERE m.facManualTarifario.idManualTarifario = :idManual ORDER BY m.facManualTarifarioMedicamentoPK.idMedicamento").setParameter("idManual", idManual);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public FacManualTarifarioMedicamento buscarManualContratoMedicamento(int idManual,int idMedicamento){
        Query query;
        try {
            query = getEntityManager().createQuery("SELECT m FROM FacManualTarifarioMedicamento m WHERE m.facManualTarifario.idManualTarifario = :idManual and m.cfgMedicamento.idMedicamento=:idMedicamento ").setParameter("idManual", idManual).setParameter("idMedicamento", idMedicamento);
            List<FacManualTarifarioMedicamento> listaMedicamento = query.getResultList();
            return listaMedicamento.size() > 0 ? listaMedicamento.get(0) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
