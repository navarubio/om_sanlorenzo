/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgHistoriaCamposPredefinidos;

/**
 *
 * @author Arcosoft-PC2
 */
@Stateless
public class CfgHistoriaCamposPredefinidosFacade  extends AbstractFacade<CfgHistoriaCamposPredefinidos>{

    public CfgHistoriaCamposPredefinidosFacade() {
        super(CfgHistoriaCamposPredefinidos.class);
    }

    public List<CfgHistoriaCamposPredefinidos> getCamposDefinidosXHistoriaClinica(int idHistoriaClinica){
        String hql = "SELECT c FROM CfgHistoriaCamposPredefinidos c WHERE c.idCampo.idTipoReg.idTipoReg=:idTipoReg";
        Query query = getEntityManager().createQuery(hql).setParameter("idTipoReg", idHistoriaClinica);
        return query.getResultList();
    }
    public List<CfgHistoriaCamposPredefinidos> getCamposDefinidosXHistoriaClinicaXCampo(int idHistoriaClinica, int idCampo){
        String hql = "SELECT c FROM CfgHistoriaCamposPredefinidos c WHERE c.idCampo.idTipoReg.idTipoReg=:idTipoReg and c.idCampo.idCampo=:idCampo";
        Query query = getEntityManager().createQuery(hql).setParameter("idTipoReg", idHistoriaClinica).setParameter("idCampo",idCampo);
        return query.getResultList();
    }
    
    public List<CfgHistoriaCamposPredefinidos> getCamposDefinidosXCampo(int idCampo){
        String hql = "SELECT c FROM CfgHistoriaCamposPredefinidos c WHERE c.idCampo.idCampo=:idCampo";
        Query query = getEntityManager().createQuery(hql).setParameter("idCampo",idCampo);
        return query.getResultList();
    }
    
    public void setNoneDefault(int idCampo){
        try {
            String query  ="UPDATE CfgHistoriaCamposPredefinidos set defaultValor=:valor WHERE c.idCampo.idCampo=:idCampo";
            Query q = getEntityManager().createQuery(query).setParameter("valor",false).setParameter("idCampo", idCampo);
            q.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
