/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgPacientes;
import modelo.entidades.Hc3047Anexo1;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo1Facade extends AbstractFacade<Hc3047Anexo1> {

    public Hc3047Anexo1Facade() {
        super(Hc3047Anexo1.class);
    }
    
    public List<Hc3047Anexo1> buscar3047Anexos1() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo1 h ORDER BY h.id3047Anexo1";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo1 get3047Anexo1xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM HcAnexos3047 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo1> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcAnexos3047 get3047Anexos1xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo1 h WHERE h.numeroinforme=:numeroinforme";
            List<HcAnexos3047> lista = getEntityManager().createQuery(hql).setParameter("numeroinforme", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    

}
