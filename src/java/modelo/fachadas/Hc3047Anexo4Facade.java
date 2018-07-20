/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgPacientes;
import modelo.entidades.Hc3047Anexo1;
import modelo.entidades.Hc3047Anexo2;
import modelo.entidades.Hc3047Anexo3;
import modelo.entidades.Hc3047Anexo4;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo4Facade extends AbstractFacade<Hc3047Anexo4> {

    public Hc3047Anexo4Facade() {
        super(Hc3047Anexo4.class);
    }
    
    public List<Hc3047Anexo4> buscar3047Anexos4() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo4 h ORDER BY h.id3047Anexo4";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo4 get3047Anexo4xPaciente(CfgPacientes paciente){
        try {
            String hql ="SELECT h FROM Hc3047Anexo4 h WHERE h.id_paciente=:id_paciente";
            List<Hc3047Anexo4> lista = getEntityManager().createQuery(hql).setParameter("id_paciente", paciente.getIdPaciente()).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo4 get3047Anexos4xNumero (String numero){
        try {
            String hql ="SELECT h FROM Hc3047Anexo3 h WHERE h.numeroautorizacion=:numeroautorizacion";
            List<Hc3047Anexo4> lista = getEntityManager().createQuery(hql).setParameter("numeroautorizacion", numero).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo4 ultimoInsertado() {
        String consulta = null;
        Hc3047Anexo4 ultimo = new Hc3047Anexo4();
        try {
            String hql = "SELECT h FROM Hc3047Anexo4 h ";
            List<Hc3047Anexo4> lista = getEntityManager().createQuery(hql).getResultList();
            if (!lista.isEmpty()) {
                Collections.reverse(lista);
                ultimo = lista.get(0);
            }
        } catch (Exception e) {
            throw e;
        }
        return ultimo;
    }


}
