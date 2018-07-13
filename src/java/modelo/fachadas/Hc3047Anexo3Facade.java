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
import modelo.entidades.Hc3047Anexo2;
import modelo.entidades.Hc3047Anexo3;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo3Facade extends AbstractFacade<Hc3047Anexo3> {

    public Hc3047Anexo3Facade() {
        super(Hc3047Anexo3.class);
    }
    
    public List<Hc3047Anexo3> buscar3047Anexos3() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo3 h ORDER BY h.id3047Anexo3";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo3 get3047Anexo3xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM Hc3047Anexo3 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo3> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo3 get3047Anexos3xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo3 h WHERE h.numerosolicitud=:numerosolicitud";
            List<Hc3047Anexo3> lista = getEntityManager().createQuery(hql).setParameter("numerosolicitud", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    

}
