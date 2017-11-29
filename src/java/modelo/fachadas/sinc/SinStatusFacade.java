/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas.sinc;

import java.util.List;
import modelo.fachadas.*;
import javax.ejb.Stateless;
import modelo.entidades.sinc.SinStatus;

/**
 *
 * @author santos
 */
@Stateless
public class SinStatusFacade extends AbstractFacade<SinStatus> {

    public SinStatusFacade() {
        super(SinStatus.class);
    }

    public List<SinStatus> buscarOrdenado(boolean status) {
        try {
            String hql = "SELECT s FROM SinStatus s where s.status=?1 ORDER BY s.sinTablas.orden";
            return getEntityManager().createQuery(hql).setParameter(1, status).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public SinStatus existeRegistro(int idTabla, int idRegistro, boolean remoto) {
        try {
            String hql = "SELECT s FROM SinStatus s where s.sinTablas.idTabla = ?1 and  ";
            if (remoto) {
                hql += "s.idRemoto = ?2";
            } else {
                hql += "s.sinStatusPK.idLocal = ?2";
            }
            return (SinStatus) getEntityManager().createQuery(hql).setParameter(1, idTabla)
                    .setParameter(2, idRegistro).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
