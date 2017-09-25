/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*; 
import modelo.entidades.PyPProgramaItem;
/**
 *
 * @author cores
 */
@Stateless
public class PyPprogramaItems extends AbstractFacade<PyPProgramaItem>  {
    
    @EJB
    PyPprogramAsoc ProgramaFacadeAsoc;

    public PyPprogramaItems() {
        super(PyPProgramaItem.class);
    } 

    public List<PyPProgramaItem> buscar_programas() {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m");     
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<PyPProgramaItem> buscar_programas_val(int edad_year,String edad_mes, String genero, int administradora) {
        Query query;
        try { 
            int list = 0,gen = 0;
            if(Integer.parseInt(edad_mes) < 12 && edad_year == 0){
                edad_year = Integer.parseInt(edad_mes) ;
                list++;
            }
            switch (genero) {
                case "F":
                    gen = 2;
                    break;
                case "M":
                    gen = 1;
                    break;
            }
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m "
                    + "where m.edad_ini_list = :list and "
                    + ":age >= m.edad_ini and "
                    + ":age <= m.edad_fin and "
                    + "m.genero = :genero and EXISTS (SELECT 1 FROM PyPprogramaAsoc p where p.idPrograma = m.idPrograma and p.idAdministradora = :administradora)")
                    .setParameter("list", list)
                    .setParameter("age", edad_year)
                    .setParameter("genero", gen)   
                    .setParameter("administradora", administradora);   
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_val_(int edad_year,String edad_mes, String genero, int administradora) {
        Query query;
        try { 
            int list = 0;
            if(Integer.parseInt(edad_mes) < 12 && edad_year == 0){
                edad_year = Integer.parseInt(edad_mes) ;
                list++;
            }
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m "
                    + "where m.edad_ini_list = :list and "
                    + ":age >= m.edad_ini and "
                    + ":age <= m.edad_fin and "
                    + "m.genero = :genero and EXISTS (SELECT 1 FROM PyPprogramaAsoc p where p.idPrograma = m.idPrograma and p.idAdministradora = :administradora)")
                    .setParameter("list", list)
                    .setParameter("age", edad_year)
                    .setParameter("genero", 0)   
                    .setParameter("administradora", administradora);   
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_servicios(Integer programas) {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idServicio is not null and m.idPrograma = :programa")
                    .setParameter("programa", programas);     
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_idItem(Integer programas) {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idServicio is not null and m.idProgramaItems = :programa")
                    .setParameter("programa", programas);     
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_idservicios(Integer programas,Integer idservicio) {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idServicio = :idservicio and m.idPrograma = :programa")
                    .setParameter("programa", programas)
                    .setParameter("idservicio", idservicio);     
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_idmedicamentos(Integer programas,Integer idmedicamentos) {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idMedicamento = :idmedicamentos and m.idPrograma = :programa")
                    .setParameter("programa", programas)
                    .setParameter("idmedicamentos", idmedicamentos);     
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_idinsumos(Integer programas,Integer idinsumo) {
        Query query;
        try { 
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idInsumo = :idinsumo and m.idPrograma = :programa")
                    .setParameter("programa", programas)
                    .setParameter("idinsumo", idinsumo);     
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_medicamentos(Integer programas) {
        Query query;
        try {
            
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idMedicamento is not null and m.idPrograma = :programa")
                    .setParameter("programa", programas);   
            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPProgramaItem> buscar_programas_insumos(Integer programas) {
        Query query;
        try {
            
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idInsumo is not null and m.idPrograma = :programa")
                    .setParameter("programa", programas);    
            return query.getResultList();
            
//            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m WHERE m.idInsumo != ?2 and m.idPrograma IN ?1");
//            query.setParameter(1, programas);
//            query.setParameter(2, null);
//            return query.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
}
