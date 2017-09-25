/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.CfgDiagnosticoPrincipal;

/**
 *
 * @author santos
 */
@Stateless
public class CfgDiagnosticoPrincipalFacade extends AbstractFacade<CfgDiagnosticoPrincipal> {

    public CfgDiagnosticoPrincipalFacade() {
        super(CfgDiagnosticoPrincipal.class);
    }

    public List<String> autocompletarDiagnostico(String txt) {
        try {
            return getEntityManager().createNativeQuery("select codigo_diagnostico||' - '||nombre_diagnostico from cfg_diagnostico_principal where codigo_diagnostico||' - '||nombre_diagnostico ilike '%" + txt + "%' limit 10").getResultList();
        } catch (Exception e) {                          
            return null;
        }
    }
    
    public CfgDiagnosticoPrincipal buscarPorNombre(String nombreDiagnostico) {
        String hql = "SELECT c FROM CfgDiagnosticoPrincipal c WHERE c.nombreDiagnostico = :nombreDiagnostico";
        try {
            return (CfgDiagnosticoPrincipal) getEntityManager().createQuery(hql).setParameter("nombreDiagnostico", nombreDiagnostico).getSingleResult();
        } catch (Exception e) {
            System.out.println(e.toString() + "----------------------------------------------------");
            return null;
        }
    }
    

}
