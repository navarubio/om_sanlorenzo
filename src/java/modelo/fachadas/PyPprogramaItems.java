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
                    + "where "
                    + ":age between m.edad_ini and "
                    + " m.edad_fin and "
                    + "m.genero in( "+gen+","+"0)" )//and EXISTS (SELECT 1 FROM PyPprogramaAsoc p where p.idPrograma = m.idPrograma and p.idAdministradora = :administradora)")
                    //.setParameter("list", list)
                    .setParameter("age", edad_year);
                    //.setParameter("genero", gen)   ;
                    //.setParameter("administradora", administradora);   
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
            int gen=0;
            switch (genero) {
                case "F":
                    gen = 2;
                    break;
                case "M":
                    gen = 1;
                    break;
            }
            query = getEntityManager().createQuery("SELECT m FROM PyPProgramaItem m "
                    + "where "
                    + ":age between m.edad_ini and "
                    + " m.edad_fin and "
                    + "m.genero in( "+gen+","+"0)" )//and EXISTS (SELECT 1 FROM PyPprogramaAsoc p where p.idPrograma = m.idPrograma and p.idAdministradora = :administradora)")
                    //.setParameter("list", list)
                    .setParameter("age", edad_year);
                    
                    //.setParameter("administradora", administradora);   
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

    public String validarSemaforo(int edad_year,String edad_mes, String genero, int administradora, int idPaciente){
        
        String valor="../recursos/img/semaforo3.png";
        try {
            if(Integer.parseInt(edad_mes) < 12 && edad_year == 0){
                edad_year = Integer.parseInt(edad_mes) ;
            }
            //System.out.println(genero);
            int generoV= genero.equals("F")?2:1;
            Query query = getEntityManager().createNativeQuery("select c.id_Cita, c.atendida " +
                " from pyp_programa_item pp" +
                " left join pyp_programa_cita p on p.id_programa_items=pp.id_programa_items" +
                " left join cit_citas c  on p.id_cita = c.id_cita and c.id_paciente="+idPaciente  +
                " where "+ edad_year+"between pp.edad_inicial and pp.edad_final  "+
                " and pp.genero in("+generoV+","+0+") order by 1 ");
            /*System.out.println("select c.id_Cita, c.atendida " +
                " from pyp_programa_item pp" +
                " left join pyp_programa_cita p on p.id_programa_items=pp.id_programa_items" +
                " left join cit_citas c  on p.id_cita = c.id_cita and c.id_paciente="+idPaciente  +
                " where "+ edad_year+" between pp.edad_inicial and pp.edad_final  "+
                " and pp.genero in("+generoV+","+0+") order by 1 " );*/
            List<Object[]> lst = query.getResultList();
            int totalCitado=0;
            for(Object[] c: lst){
                if(c[0]==null){
                    valor="../recursos/img/semaforo1.png";
                    break;
                }else{
                    if(c[1]!=null){
                        System.out.println(c[1].toString());
                        if(c[1].toString().equals("false")){
                            valor="../recursos/img/semaforo2.png";
                            break;
                        }else if(c[1].toString().equals("true")){
                            totalCitado++;
                        }
                    }
                }
            }
            if(lst.isEmpty())valor="../recursos/img/semaforo1.png";
            if(totalCitado==lst.size())valor="../recursos/img/semaforo3.png";
            if(totalCitado>0)valor="../recursos/img/semaforo2.png";
        } catch (Exception e) {
            System.out.println(e);
        }
        return valor;
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
