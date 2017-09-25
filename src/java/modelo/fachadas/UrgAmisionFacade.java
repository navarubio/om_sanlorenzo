/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import beans.enumeradores.EstadoAdmisionPaciente;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import modelo.entidades.UrgAdmision;

/**
 *
 * @author Enderson
 */
@Stateless
public class UrgAmisionFacade extends AbstractFacade<UrgAdmision> {

    public UrgAmisionFacade() {
        super(UrgAdmision.class);
    }

    public List<UrgAdmision> findAllAdmisionesVigentes(EstadoAdmisionPaciente estadoPaciente) {
        EntityManager entityManager = this.getEntityManager();
        try {
            TypedQuery<UrgAdmision> query = entityManager.createNamedQuery("UrgAdmision.findAllAdmisionesVigentes", UrgAdmision.class);
            query.setParameter("estadoPaciente", estadoPaciente);
            return query.getResultList();
        } catch (PersistenceException ex) {
            Logger.getLogger(UrgAdmision.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public Long numeroAdmisionesDiarias(Date fecha) {
        EntityManager entityManager = this.getEntityManager();
        try {
            TypedQuery<Long> query = entityManager.createNamedQuery("UrgAdmision.numeroAdmisionesDiarias", Long.class);
            query.setParameter("fecha", fecha);
            return query.getSingleResult();
        } catch (PersistenceException e) {
            Logger.getLogger(Long.class.getName()).log(Level.SEVERE, null, e);
            throw e;
        }
    }
}
