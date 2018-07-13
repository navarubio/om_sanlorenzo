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
import modelo.entidades.Hc3047Anexo31;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo3_1Facade extends AbstractFacade<Hc3047Anexo31> {

    public Hc3047Anexo3_1Facade() {
        super(Hc3047Anexo31.class);
    }
    
    public List<Hc3047Anexo31> buscar3047Anexos31() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo31 h ORDER BY h.id3047Anexo31";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo31 get3047Anexos31xAnexo3 (int idanexo3){
        try {
            String hql ="SELECT h FROM Hc3047Anexo31 h WHERE h.id3047anexo31 = :id3047anexo31";
            List<Hc3047Anexo31> lista = getEntityManager().createQuery(hql).setParameter("id3047anexo31", idanexo3).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    

}
