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
import modelo.entidades.Hc3047Anexo41;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class Hc3047Anexo4_1Facade extends AbstractFacade<Hc3047Anexo41> {

    public Hc3047Anexo4_1Facade() {
        super(Hc3047Anexo41.class);
    }
    
    public List<Hc3047Anexo41> buscar3047Anexos41() {
        try {
            String hql = "SELECT h FROM Hc3047Anexo41 h ORDER BY h.id3047Anexo41";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Hc3047Anexo41 get3047Anexos41xAnexo4 (int idanexo4){
        try {
            String hql ="SELECT h FROM Hc3047Anexo41 h WHERE h.id3047anexo41 = :id3047anexo41";
            List<Hc3047Anexo41> lista = getEntityManager().createQuery(hql).setParameter("id3047anexo41", idanexo4).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Hc3047Anexo41> buscarAnexos41xAnexo4(int idanexo4) {
        try {
            String hql ="SELECT h FROM Hc3047Anexo41 h WHERE h.id3047anexo4.id3047anexo4 = ?1";
            return getEntityManager().createQuery(hql).setParameter(1, idanexo4).getResultList();
        } catch (Exception e) {
            return null;
        }
        
    }
    

}
