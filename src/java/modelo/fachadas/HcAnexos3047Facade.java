/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcTipoReg;

/**
 *
 * @author sismacontab
 */
@Stateless
public class HcAnexos3047Facade extends AbstractFacade<HcAnexos3047> {

    public HcAnexos3047Facade() {
        super(HcAnexos3047.class);
    }
    
    public List<HcAnexos3047> buscarAnexos3047Activos() {
        try {
            String hql = "SELECT h FROM HcAnexos3047 h WHERE h.activo = TRUE ORDER BY h.idAnexos3047";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcAnexos3047 getAnexos3047UrlPagin(String urlPagina){
        try {
            String hql ="SELECT h FROM HcAnexos3047 h WHERE h.urlPagina=:urlPagina ";
            List<HcAnexos3047> lista = getEntityManager().createQuery(hql).setParameter("urlPagina", urlPagina).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcAnexos3047 getAnexos3047Nombre(String nombre){
        try {
            String hql ="SELECT h FROM HcAnexos3047 h WHERE h.nombre=:nombre";
            List<HcAnexos3047> lista = getEntityManager().createQuery(hql).setParameter("nombre", nombre).getResultList();
            return lista.size() > 0 ? lista.get(0) : null;
        } catch (Exception e) {
            return null;
        }
    }
    

}
