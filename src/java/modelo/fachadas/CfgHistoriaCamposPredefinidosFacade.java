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
}
