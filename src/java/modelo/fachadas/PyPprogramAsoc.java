/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;
import java.util.List;
import javax.ejb.Stateless;
import modelo.entidades.PyPprogramaAsoc;
/**
 *
 * @author cores
 */
@Stateless
public class PyPprogramAsoc  extends AbstractFacade<PyPprogramaAsoc> {

    public PyPprogramAsoc() {
        super(PyPprogramaAsoc.class);
    }

    public List<PyPprogramaAsoc> buscar_programas_codigo(int codigo, int idadministradora) {
        try {
            String hql = "SELECT f FROM PyPprogramaAsoc f where f.idPrograma = :codigo and idAdministradora = :idadministradora";
            return getEntityManager().createQuery(hql).setParameter("codigo", codigo).setParameter("idadministradora", idadministradora).getResultList();
        } catch (Exception e) {
            return null;
        }
    } 

    public List<PyPprogramaAsoc> buscar_programas_administradora(int idadministradora) {
        try {
            String hql = "SELECT f FROM PyPprogramaAsoc f where idAdministradora = :idadministradora";
            return getEntityManager().createQuery(hql).setParameter("idadministradora", idadministradora).getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
}
