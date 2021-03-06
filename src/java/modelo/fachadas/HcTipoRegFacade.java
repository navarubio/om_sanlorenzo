/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author santos
 */
@Stateless
public class HcTipoRegFacade extends AbstractFacade<HcTipoReg> {

    public HcTipoRegFacade() {
        super(HcTipoReg.class);
    }
    
    public List<HcTipoReg> buscarTiposRegstroActivos() {
        try {
            String hql = "SELECT h FROM HcTipoReg h WHERE h.activo = TRUE ORDER BY h.idTipoReg";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcTipoReg getTipoRegUrlPagin(String urlPagina){
        try {
            String hql ="SELECT h FROM HcTipoReg h WHERE h.urlPagina=:urlPagina ";
            List<HcTipoReg> lista = getEntityManager().createQuery(hql).setParameter("urlPagina", urlPagina).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }

}
