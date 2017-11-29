/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas.sinc;

import modelo.fachadas.*;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.sinc.SinTablas;

/**
 *
 * @author santos
 */
@Stateless
public class SinTablasFacade extends AbstractFacade<SinTablas> {

    public SinTablasFacade() {
        super(SinTablas.class);
    }

    public SinTablas consultarTablaXNombre(String tabla) {
        try {
            String hql = "SELECT s FROM SinTablas s where s.tabla = ?1  ";
            return (SinTablas) getEntityManager().createQuery(hql).setParameter(1, tabla).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public int consultarIdTabla(String tabla) {
        int id = 0;
        try {
            String sql = "select id_tabla from sin_tablas where tabla = ?1 ";
            Query query = getEntityManager().createNativeQuery(sql);
            query.setParameter(1, tabla);
            id = ((Integer) query.getSingleResult());
        } catch (Exception e) {
        }
        return id;
    }

}
