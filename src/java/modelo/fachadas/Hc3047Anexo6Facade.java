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
import modelo.entidades.Hc3047Anexo3;
import modelo.entidades.Hc3047Anexo4;
import modelo.entidades.Hc3047Anexo5;
import modelo.entidades.Hc3047Anexo6;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo6Facade extends AbstractFacade<Hc3047Anexo6> {

    public Hc3047Anexo6Facade() {
        super(Hc3047Anexo6.class);
    }
    
    public List<Hc3047Anexo6> buscar3047Anexos6() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo6 h ORDER BY h.id3047Anexo6";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo6 get3047Anexo6xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM Hc3047Anexo6 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo6> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo6 get3047Anexos6xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo6 h WHERE h.numerocontrarremision=:numerocontrarremision";
            List<Hc3047Anexo6> lista = getEntityManager().createQuery(hql).setParameter("numerocontrarremision", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
        
    public List<Hc3047Anexo6> Anexos6xPaciente(int paciente){
        String consulta;
        List<Hc3047Anexo6> lista = null;
            try {
                consulta = "From Hc3047Anexo6 h where h.idPaciente.idPaciente= ?1";
                Query query = getEntityManager().createQuery(consulta);
                query.setParameter(1, paciente);
                lista = query.getResultList();
            } catch (Exception e) {
                throw e;
            }
        
        return lista;
    }
    

}
