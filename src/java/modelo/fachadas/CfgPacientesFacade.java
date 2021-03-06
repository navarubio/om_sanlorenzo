/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.Date;
import java.util.List;
import modelo.entidades.CfgPacientes;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author santos
 */
@Stateless
public class CfgPacientesFacade extends AbstractFacade<CfgPacientes> {

    public List<CfgPacientes> consultaNativaPacientes(String sql) {
        List<CfgPacientes> listaPacientes = (List<CfgPacientes>) getEntityManager().createNativeQuery(sql, CfgPacientes.class).getResultList();
        return listaPacientes;
    }   
    
    public List<Object> exportacionPacientes() {
        try {
            return (List<Object>) getEntityManager().createNativeQuery(
                    "   SELECT   \n"
                    + "   identificacion,      \n"
                    + "   (select descripcion from cfg_clasificaciones where tipo_identificacion = id) as tipo_identificacion,\n"
                    + "   lugar_expedicion,\n"
                    + "   fecha_nacimiento::date,\n"
                    + "   replace(replace(replace(replace(\n"
                    + "   (select date_trunc('day',age(now(),fecha_nacimiento)))::text\n"
                    + "   ,'s',''),' year','A'),' mon','M'),' day','D') as edad,   \n"
                    + "   (select descripcion from cfg_clasificaciones where genero=id) as genero,   \n"
                    + "   (select descripcion from cfg_clasificaciones where grupo_sanguineo=id) as grupo_sanguineo,\n"
                    + "   primer_nombre,\n"
                    + "   segundo_nombre,\n"
                    + "   primer_apellido,\n"
                    + "   segundo_apellido,\n"
                    + "   (select descripcion from cfg_clasificaciones where ocupacion = id) as ocupacion,\n"
                    + "   (select descripcion from cfg_clasificaciones where estado_civil = id) as estado_civil,\n"
                    + "   telefono_residencia,\n"
                    + "   telefono_oficina,\n"
                    + "   celular,\n"
                    + "   (select descripcion from cfg_clasificaciones where departamento = id) as departamento,\n"
                    + "   (select descripcion from cfg_clasificaciones where municipio = id) as municipio, \n"
                    + "   (select descripcion from cfg_clasificaciones where zona = id) as zona,\n"
                    + "   direccion,\n"
                    + "   barrio, \n"
                    + "   email,\n"
                    + "   CASE \n"
                    + "      WHEN activo   = 't' THEN 'ACTIVO' \n"
                    + "      ELSE 'INACTIVO'    \n"
                    + "   END AS activo, \n"
                    + "   (select razon_social from fac_administradora where fac_administradora.id_administradora = cfg_pacientes.id_administradora) as administradora,\n"
                    + "   (select descripcion from cfg_clasificaciones where tipo_afiliado = id) as tipo_afiliado,\n"
                    + "   (select descripcion from cfg_clasificaciones where regimen = id) as regimen,\n"
                    + "   (select descripcion from cfg_clasificaciones where categoria_paciente = id) as categoria_paciente, \n"
                    + "   (select descripcion from cfg_clasificaciones where nivel = id) as nivel,\n"
                    + "   (select descripcion from cfg_clasificaciones where etnia = id) as etnia,\n"
                    + "   (select descripcion from cfg_clasificaciones where escolaridad  = id) as escolaridad,\n"
                    + "   responsable,\n"
                    + "   telefono_responsable,\n"
                    + "   (select descripcion from cfg_clasificaciones where parentesco = id) as parentesco,\n"
                    + "   acompanante,\n"
                    + "   telefono_acompanante,  \n"
                    + "   fecha_afiliacion,\n"
                    + "   fecha_sisben,\n"
                    + "   carnet,  \n"
                    + "   fecha_vence_carnet,\n"
                    + "   observaciones\n"
                    + " from cfg_pacientes"
            ).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<CfgPacientes> buscarOrdenado() {
        try {
            String hql = "SELECT c FROM CfgPacientes c";
            System.out.println(hql);
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<CfgPacientes> buscarOrdenado(int administradora, Date inicio, Date fin) {
        try {
            String hql = "SELECT c FROM CfgPacientes c "
                    + "where "
                    + "c.fechaNacimiento >= ?2 and c.fechaNacimiento <= ?3 "
                    + "and c.idAdministradora.idAdministradora = ?1 "
                    + "ORDER BY c.primerNombre ASC";
            if (inicio == null || fin == null) {
                hql = "SELECT c FROM CfgPacientes c "
                        + "where "
                        + "c.idAdministradora.idAdministradora = ?1 "
                        + "ORDER BY c.primerNombre ASC";
                return getEntityManager().createQuery(hql).setParameter(1, administradora).getResultList();
            } else {
                return getEntityManager().createQuery(hql).setParameter(1, administradora).setParameter(2, inicio).setParameter(3, fin).getResultList();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<CfgPacientes> buscarOrdenado(int idpaciente) {
        try {
            String hql = "SELECT c FROM CfgPacientes c where c.idPaciente = ?1 ";
            return getEntityManager().createQuery(hql).setParameter(1, idpaciente).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public CfgPacientes buscarPaciente (int idpaciente) {
        try {
            String hql = "SELECT c FROM CfgPacientes c where c.idPaciente = ?1 ";
            return (CfgPacientes) getEntityManager().createQuery(hql).setParameter(1, idpaciente).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public int numeroCedulas(String identificacion) {
        try {
            Query q = getEntityManager().createQuery("SELECT COUNT(p) FROM CfgPacientes  p where p.identificacion = ?1");
            q.setParameter(1, identificacion);
            return Integer.parseInt(q.getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public int numeroTotalRegistros() {
        try {            
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT COUNT(*) FROM cfg_pacientes").getSingleResult().toString());            
        } catch (Exception e) {
            return 0;
        }
    }

    public CfgPacientesFacade() {
        super(CfgPacientes.class);
    }

    public CfgPacientes buscarPorIdentificacion(String identificacion) {
        try {
            return getEntityManager().createNamedQuery("CfgPacientes.findByIdentificacion", CfgPacientes.class).setParameter("identificacion", identificacion).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public CfgPacientes findPacienteByTipIden(int id, String identificacion) {
        CfgPacientes paciente;
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgPacientes p WHERE p.identificacion=?1 AND p.tipoIdentificacion.id=?2");
            query.setParameter(1, identificacion);
            query.setParameter(2, id);
            paciente = (CfgPacientes) query.getSingleResult();
        } catch (Exception e) {
            paciente = null;
        }
        return paciente;
    }

    public List<CfgPacientes> findLazyPaciente(int limit, int offset) {
        CfgPacientes paciente;
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM CfgPacientes p WHERE p.identificacion=?1 AND p.tipoIdentificacion.id=?2");
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
    
    public EntityManager obtenerEntityManager() {
        return getEntityManager();
    }

     public List<CfgPacientes> buscarPacientesAtendidosAdministradora(int administradora, Date inicio, Date fin) {
        System.out.println("Administ " + administradora);
        System.out.println("Iniio " + inicio);
        System.out.println("Fin " + fin);
        StringBuilder cadPacientes = new StringBuilder("-1");
        try {
            String queryHistorias
                    = " SELECT distinct(h.idPaciente.idPaciente)"
                    + " FROM HcRegistro h "
                    + " where  h.fechaReg >= ?1 and h.fechaReg <= ?2 "
                    + " and h.idPaciente.idAdministradora.idAdministradora = ?3 ";

            if (inicio == null || fin == null) {
                throw new Exception("Debe Ingresar Fechas");
            } else {
                Query q = getEntityManager().createQuery(queryHistorias);
                q.setParameter(1, inicio);
                q.setParameter(2, fin);
                q.setParameter(3, administradora);
                List<Object> arrPacientes
                        = q.getResultList();
                if (arrPacientes != null && !arrPacientes.isEmpty()) {
                    for (Object pac : arrPacientes) {
                        cadPacientes.append(",").append(pac.toString());
                    }
                }
            }
            String hql = "SELECT c FROM CfgPacientes c "
                    + "where c.idPaciente IN("
                    + cadPacientes.toString()
                    + ")"
                    //+ "and c.idAdministradora.idAdministradora = ?1 "
                    + "ORDER BY c.primerNombre ASC";
            Query q2 = getEntityManager().createQuery(hql);
            //q2.setParameter(1, administradora);
            return q2.getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
