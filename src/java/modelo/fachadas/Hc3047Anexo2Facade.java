/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgPacientes;
import modelo.entidades.Hc3047Anexo1;
import modelo.entidades.Hc3047Anexo2;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo2Facade extends AbstractFacade<Hc3047Anexo2> {

    public Hc3047Anexo2Facade() {
        super(Hc3047Anexo2.class);
    }
    
    public List<Hc3047Anexo2> buscar3047Anexos1() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo2 h ORDER BY h.id3047Anexo2";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo2 get3047Anexo2xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM Hc3047Anexo2 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo2> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcAnexos3047 get3047Anexos2xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo2 h WHERE h.numeroinforme=:numeroinforme";
            List<HcAnexos3047> lista = getEntityManager().createQuery(hql).setParameter("numeroinforme", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Hc3047Anexo2> Anexos2xPaciente(int paciente){
        String consulta;
        List<Hc3047Anexo2> lista = null;
            try {
                consulta = "From Hc3047Anexo2 h where h.idPaciente.idPaciente= ?1";
                Query query = getEntityManager().createQuery(consulta);
                query.setParameter(1, paciente);
                lista = query.getResultList();
            } catch (Exception e) {
                throw e;
            }
        
        return lista;
    }
    

}
