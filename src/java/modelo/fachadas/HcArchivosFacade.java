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
import modelo.entidades.HcArchivos;

/**
 *
 * @author Cristhian
 */
@Stateless
public class HcArchivosFacade extends AbstractFacade<HcArchivos> {

    public HcArchivosFacade() {
        super(HcArchivos.class);
    }

    public List<HcArchivos> getHcArchivosByPaciente(CfgPacientes paciente) {
        Query query = getEntityManager().createQuery("SELECT a FROM HcArchivos a WHERE a.idPaciente = ?1 ORDER BY a.fechaUpload DESC");
        query.setParameter(1, paciente);
        return query.getResultList();
    }

}
