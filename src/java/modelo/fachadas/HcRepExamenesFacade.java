/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcRepExamenes;

/**
 *
 * @author cores
 */
@Stateless
public class HcRepExamenesFacade extends AbstractFacade<HcRepExamenes> {

    public HcRepExamenesFacade() {
        super(HcRepExamenes.class);
    }
    
    public List<HcRepExamenes> buscarRepIdregistro(int id_registro) {
        Query query = getEntityManager().createQuery("SELECT a FROM HcRepExamenes a WHERE a.idRegistro.idRegistro = ?1");
        query.setParameter(1, id_registro);
        return query.getResultList();
    }
    
    public List<HcRepExamenes> buscarRepUltimo(int id_paciente) {
        Query query = getEntityManager().createQuery("SELECT a FROM HcRepExamenes a WHERE a.idRegistro.idPaciente.idPaciente = ?1");
        query.setParameter(1, id_paciente);
        System.out.println(query);
        return query.getResultList();
    }

}
