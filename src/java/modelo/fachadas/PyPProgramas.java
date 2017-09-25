/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import modelo.entidades.CfgPacientes;
import modelo.entidades.PyPprograma;

/**
 *
 * @author cores
 */
@Stateless
public class PyPProgramas extends AbstractFacade<PyPprograma> {

    public PyPProgramas() {
        super(PyPprograma.class);
    }

    public List<PyPprograma> buscar_programas() {
        try {
            String hql = "SELECT f FROM PyPprograma f";
            return getEntityManager().createQuery(hql).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPprograma> buscar_programas_codigo(String codigo) {
        try {
            String hql = "SELECT f FROM PyPprograma f where f.codigoPrograma = :codigo";

            return getEntityManager().createQuery(hql).setParameter("codigo", codigo).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<PyPprograma> buscar_programas_id(int codigo) {
        try {
            String hql = "SELECT f FROM PyPprograma f where f.idPrograma = :codigo";

            return getEntityManager().createQuery(hql).setParameter("codigo", codigo).getResultList();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Object[]> informe_pacientes_programas(Date fecha_ini, Date fecha_fin, CfgPacientes pacienteHC) {
        try {
            Query query;
            String sql = "and p.idTurno.fecha >= ?2 AND p.idTurno.fecha <= ?3 ";
            if (fecha_ini == null || fecha_fin == null) {
                sql = "";
            }
            if (pacienteHC != null) {
                sql += "and p.idPaciente = ?1 ";
            }
            query = getEntityManager().createQuery(""
                    + "SELECT p,c,fp "
                    + "FROM "
                    + "PypProgramaCita c, "
                    + "CitCitas p,FacFacturaPaciente fp "
                    + "WHERE c.idCita = p.idCita and fp.idCita.idCita = c.idCita  " + sql
                    + "ORDER BY p.idTurno.fecha, p.idTurno.horaIni");
            if (pacienteHC != null) {
                query.setParameter(1, pacienteHC);
            }
            if (fecha_ini != null || fecha_fin != null) {
                query.setParameter(2, fecha_ini);
                query.setParameter(3, fecha_fin);
            }
            List<Object[]> progra = query.getResultList();
            return progra;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Object[]> informe_pacientes_programas_agrupado(Date fecha_ini, Date fecha_fin, int id) {
        try {
            Query query;
            String sql = "p.idTurno.fecha >= ?2 AND p.idTurno.fecha <= ?3 and ";
            if (fecha_ini == null || fecha_fin == null) {
                sql = "";
            }
            query = getEntityManager().createQuery(""
                    + "SELECT p,c,fp "
                    + "FROM "
                    + "PypProgramaCita c, "
                    + "CitCitas p,FacFacturaPaciente fp, "
                    + "PyPprograma pyp, "
                    + "PyPProgramaItem pyp_item "
                    + "WHERE " + sql
                    + "pyp.idPrograma = ?1 and c.idCita = p.idCita and fp.idCita.idCita = c.idCita and pyp_item.idProgramaItems = c.idProgramaItem and pyp_item.idPrograma = pyp.idPrograma "
                    + "ORDER BY p.idTurno.fecha, p.idTurno.horaIni,pyp.idPrograma");
            if (fecha_ini != null || fecha_fin != null) {
                query.setParameter(2, fecha_ini);
                query.setParameter(3, fecha_fin);
            }
            query.setParameter(1, id);
            List<Object[]> progra = query.getResultList();
            return progra;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Object[]> informe_programas(Date fecha_ini, Date fecha_fin, int id) {
        try {
            Query query;
            String sql = "p.idTurno.fecha >= ?2 AND p.idTurno.fecha <= ?3 and ";
            if (fecha_ini == null || fecha_fin == null) {
                sql = "";
            }
            query = getEntityManager().createQuery(""
                    + "SELECT pyp_item.nombreActividad as act, COUNT(pyp_item.nombreActividad) as total "
                    + "FROM "
                    + "PypProgramaCita c, "
                    + "CitCitas p, "
                    + "PyPprograma pyp, "
                    + "PyPProgramaItem pyp_item "
                    + "WHERE " + sql
                    + "pyp.idPrograma = ?1 and c.idCita = p.idCita and pyp_item.idProgramaItems = c.idProgramaItem and pyp_item.idPrograma = pyp.idPrograma "
                    + "group by pyp_item.nombreActividad order by pyp_item.nombreActividad");
            if (fecha_ini != null || fecha_fin != null) {
                query.setParameter(2, fecha_ini);
                query.setParameter(3, fecha_fin);
            }
            query.setParameter(1, id);
            List<Object[]> progra = query.getResultList();
            System.out.println(query);
            return progra;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Object[]> informe_pacientes_programas_administradora(Date fecha_ini, Date fecha_fin, int ida) {
        try {
            Query query;
            String sql = "p.idTurno.fecha >= ?2 AND p.idTurno.fecha <= ?3 and ";
            if (fecha_ini == null || fecha_fin == null) {
                sql = "";
            }
            query = getEntityManager().createQuery(""
                    + "SELECT p,c,pyp_item,pyp_asoc,fp "
                    + "FROM "
                    + "PypProgramaCita c, "
                    + "CitCitas p, "
                    + "PyPprograma pyp,FacFacturaPaciente fp,  "
                    + "PyPProgramaItem pyp_item, "
                    + "PyPprogramaAsoc pyp_asoc "
                    + "WHERE " + sql
                    + "c.idCita = p.idCita and fp.idCita.idCita = c.idCita and p.idAdministradora.idAdministradora = ?1 and pyp_asoc.idAdministradora = p.idAdministradora.idAdministradora and "
                    + "pyp_item.idProgramaItems = c.idProgramaItem and "
                    + "pyp_item.idPrograma = pyp.idPrograma "
                    + "ORDER BY p.idTurno.fecha, p.idTurno.horaIni,pyp.idPrograma");
            System.out.println(query);
            if (fecha_ini != null || fecha_fin != null) {
                query.setParameter(2, fecha_ini);
                query.setParameter(3, fecha_fin);
            }
            query.setParameter(1, ida);
            List<Object[]> progra = query.getResultList();
            return progra;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public List<Object[]> informe_pacientes_programas_administradora_contrato(Date fecha_ini, Date fecha_fin, int ida, int ico) {
        try {
            Query query;
            String sql = "p.idTurno.fecha >= ?3 AND p.idTurno.fecha <= ?4 and ";
            if (fecha_ini == null || fecha_fin == null) {
                sql = "";
            }
            query = getEntityManager().createQuery(""
                    + "SELECT p,c,pyp_item "
                    + "FROM "
                    + "PypProgramaCita c, "
                    + "CitCitas p, "
                    + "PyPprograma pyp, "
                    + "PyPProgramaItem pyp_item, "
                    + "PyPprogramaAsoc pyp_asoc "
                    + "WHERE " + sql
                    + "c.idCita = p.idCita and p.idAdministradora.idAdministradora = ?1 and pyp_asoc.idAdministradora = p.idAdministradora.idAdministradora and "
                    + "pyp_item.idProgramaItems = c.idProgramaItem and "
                    + "pyp_item.idPrograma = pyp.idPrograma "
                    + "ORDER BY p.idTurno.fecha, p.idTurno.horaIni,pyp.idPrograma");
            if (fecha_ini != null || fecha_fin != null) {
                query.setParameter(3, fecha_ini);
                query.setParameter(4, fecha_fin);
            }
            query.setParameter(1, ida);
            query.setParameter(2, ico);
            List<Object[]> progra = query.getResultList();
            return progra;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
