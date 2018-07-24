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
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo5Facade extends AbstractFacade<Hc3047Anexo5> {

    public Hc3047Anexo5Facade() {
        super(Hc3047Anexo5.class);
    }
    
    public List<Hc3047Anexo5> buscar3047Anexos5() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo5 h ORDER BY h.id3047Anexo5";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo5 get3047Anexo5xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM Hc3047Anexo5 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo5> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo5 get3047Anexos5xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo5 h WHERE h.numeroremision=:numeroremision";
            List<Hc3047Anexo5> lista = getEntityManager().createQuery(hql).setParameter("numeroremision", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Hc3047Anexo5> Anexos5xPaciente(int paciente){
        String consulta;
        List<Hc3047Anexo5> lista = null;
            try {
                consulta = "From Hc3047Anexo5 h where h.idPaciente.idPaciente= ?1";
                Query query = getEntityManager().createQuery(consulta);
                query.setParameter(1, paciente);
                lista = query.getResultList();
            } catch (Exception e) {
                throw e;
            }
        
        return lista;
    }

}
