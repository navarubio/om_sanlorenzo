/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgCamposArchivosPaciente;

/**
 *
 * @author Familia Ibañez Duran
 */
@Stateless
public class CfgCamposArchivosPacienteFacade extends AbstractFacade<CfgCamposArchivosPaciente> {

    public CfgCamposArchivosPacienteFacade() {
        super(CfgCamposArchivosPaciente.class);
    }
    
    public List<CfgCamposArchivosPaciente> getArchivosXAdministrado(int administradora){
        Query query;
        query = getEntityManager().createQuery("select c from CfgCamposArchivosPaciente c where c.idAdministradora.idAdministradora=?1 order by c.orden ");
        query.setParameter(1, administradora);
        return query.getResultList(); 
    }
    public int existeCampoAdministradora(int administradora,String campo,String nombreCampo) {
        try {
            
            Query q = getEntityManager().createQuery("SELECT COUNT(c) FROM CfgCamposArchivosPaciente c where c.idAdministradora.idAdministradora=?1 and c.orden=?2 and c.nombreCampo=?3");
            q.setParameter(1, administradora);
            q.setParameter(2, Integer.parseInt(campo));
            q.setParameter(3, nombreCampo);
            return Integer.parseInt(q.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
    public int existeNombreCampoAdministradora(int administradora,String nombreCampo) {
        try {
            
            Query q = getEntityManager().createQuery("SELECT COUNT(c) FROM CfgCamposArchivosPaciente c where c.idAdministradora.idAdministradora=?1 and c.nombreCampo=?2");
            q.setParameter(1, administradora);
            q.setParameter(2, nombreCampo);
            return Integer.parseInt(q.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }
}
