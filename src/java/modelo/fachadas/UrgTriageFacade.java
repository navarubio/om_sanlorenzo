/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import beans.enumeradores.EstadoAdmisionPaciente;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import modelo.entidades.UrgAdmision;
import modelo.entidades.UrgTriage;

/**
 *
 * @author Enderson
 */
@Stateless
public class UrgTriageFacade extends AbstractFacade<UrgTriage> {

    public UrgTriageFacade() {
        super(UrgTriage.class);
    }

    public List<UrgTriage> findAllPacientesByTriage(EstadoAdmisionPaciente estado) {
        EntityManager entityManager = this.getEntityManager();
        try {
            TypedQuery<UrgTriage> query = entityManager.createNamedQuery("UrgTriage.findAllPacientesByTriage", UrgTriage.class);
            query.setParameter("estado", estado);
            query.setParameter("estadoObservacion", EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_OBSERVACION);
            return query.getResultList();
        } catch (PersistenceException ex) {
            Logger.getLogger(UrgAdmision.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
   public List<UrgTriage> findAllHistorialUrgencias() {
        EntityManager entityManager = this.getEntityManager();
        try {
            TypedQuery<UrgTriage> query = entityManager.createNamedQuery("UrgTriage.findAllHistorialUrgencias", UrgTriage.class);
            query.setParameter("estadoObservacion", EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_DADO_ALTA);
            return query.getResultList();
        } catch (PersistenceException ex) {
            Logger.getLogger(UrgAdmision.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
}
