/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.ejb.Stateless;
import javax.persistence.Query; 
import javax.persistence.TemporalType;
import modelo.entidades.CitCitas;
import modelo.entidades.FacFacturaServicio;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;

/**
 *
 * @author santos
 */
@Stateless
public class HcRegistroFacade extends AbstractFacade<HcRegistro> {

    public HcRegistroFacade() {
        super(HcRegistro.class);
    }

    public List<HcRegistro> buscarOrdenadoPorFecha(int idPaciente) {//buscar todos los registros clinicos de un paciente
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente ORDER BY u.fechaReg DESC";
            return getEntityManager().createQuery(hql).setParameter("idPaciente", idPaciente).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarOrdenado(int idPaciente, Date fechaInicial, Date fechaFinal) {//buscar registros clinicos filtrando por fecha inicial y final
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 AND u.fechaReg >= ?2 AND u.fechaReg <= ?3 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarFiltradoPorNumeroAutorizacion(int idPaciente, String numAutorizacion) {//buscar registros clinicos filtrando por Numero Autorizacion
        try {
            List<HcRegistro> listaResultado;
            List<HcRegistro> listaResultadoAux;
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            listaResultadoAux = query.getResultList();
            listaResultado = new ArrayList<>();
            boolean agregar;
            for (HcRegistro resultado : listaResultadoAux) {
                List<HcDetalle> hcDetalleList = resultado.getHcDetalleList();
                agregar = false;
                for (HcDetalle detalle : hcDetalleList) {
                    if (detalle.getHcCamposReg().getNombre().contains("autorizacion")) {
                        if (detalle.getValor().compareTo(numAutorizacion) == 0) {
                            agregar = true;
                            break;
                        }
                    }
                }
                if (agregar) {
                    listaResultado.add(resultado);
                }
            }
            return listaResultado;
        } catch (Exception e) {
            return null;
        }
    }

    public List<HcRegistro> buscarFiltradoPorNumeroAutorizacionYFecha(int idPaciente, Date fechaInicial, Date fechaFinal, String numAutorizacion) {//buscar registros clinicos filtrando por fecha inicial, fecha final y Numero Autorizacion
        try {
            List<HcRegistro> listaResultado;
            List<HcRegistro> listaResultadoAux;
            Query query = getEntityManager().createQuery("SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = ?1 AND u.fechaReg >= ?2 AND u.fechaReg <= ?3 ORDER BY u.fechaReg DESC");
            query.setParameter(1, idPaciente);
            query.setParameter(2, fechaInicial);
            query.setParameter(3, fechaFinal);
            listaResultadoAux = query.getResultList();
            listaResultado = new ArrayList<>();
            boolean agregar;
            for (HcRegistro resultado : listaResultadoAux) {
                List<HcDetalle> hcDetalleList = resultado.getHcDetalleList();
                agregar = false;
                for (HcDetalle detalle : hcDetalleList) {
                    if (detalle.getHcCamposReg().getNombre().contains("autorizacion")) {
                        if (detalle.getValor().compareTo(numAutorizacion) == 0) {
                            agregar = true;
                            break;
                        }
                    }
                }
                if (agregar) {
                    listaResultado.add(resultado);
                }
            }
            return listaResultado;
        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarUltimo(String idPaciente, String idTipoReg) {//usado para cargar la ultima historia ingresada, de un tipo de registro especifico
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente AND u.idTipoReg.idTipoReg = :idTipoReg ORDER BY u.fechaReg DESC";
            List<HcRegistro> listaResultado = getEntityManager().createQuery(hql).setMaxResults(1).setParameter("idPaciente", idPaciente).setParameter("idTipoReg", idTipoReg).getResultList();
            if (listaResultado.isEmpty()) {
                return null;
            } else {
                return listaResultado.get(0);
            }

        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarUltimo(String idPaciente, int idTipoReg) {//usado para cargar la ultima historia ingresada, de un tipo de registro especifico
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente AND u.idTipoReg.idTipoReg = :idTipoReg ORDER BY u.fechaReg DESC";
            List<HcRegistro> listaResultado = getEntityManager().createQuery(hql).setMaxResults(1).setParameter("idPaciente", idPaciente).setParameter("idTipoReg", idTipoReg).getResultList();
            if (listaResultado.isEmpty()) {
                return null;
            } else {
                return listaResultado.get(0);
            }

        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarUltimo(int idPaciente, int idTipoReg) {//usado para cargar la ultima historia ingresada, de un tipo de registro especifico
        try {
            String hql = "SELECT u FROM HcRegistro u WHERE u.idPaciente.idPaciente = :idPaciente AND u.idTipoReg.idTipoReg = :idTipoReg ORDER BY u.fechaReg DESC";
            List<HcRegistro> listaResultado = getEntityManager().createQuery(hql).setMaxResults(1).setParameter("idPaciente", idPaciente).setParameter("idTipoReg", idTipoReg).getResultList();
            if (listaResultado.isEmpty()) {
                return null;
            } else {
                return listaResultado.get(0);
            }

        } catch (Exception e) {
            return null;
        }
    }

    public HcRegistro buscarRegistroConDiagnosticoSegunCita(String idCita) {
        try {
            return (HcRegistro) getEntityManager().createNativeQuery(""
                    + " select * \n"
                    + " from hc_registro  \n"
                    + " where \n"
                    + " id_cita = " + idCita + " AND \n"
                    + " id_tipo_reg in (1,2,5,7,8,54,77)  \n"
                    + " ORDER BY fecha_reg DESC\n"
                    + " LIMIT 1", HcRegistro.class).getSingleResult(); 
        } catch (Exception e) {
            return null;
        }
    }
    
    public HcRegistro buscarRegistroConDiagnosticoSegunPaciente(FacFacturaServicio facservicio) {
        try {
            String hql = "SELECT h "
                    + "FROM HcRegistro h "
                    + "where h.idPaciente.idPaciente=:idPaciente "
                    + "and h.idCita is null "
                    + "and h.idTipoReg.idTipoReg in (1,2,5,7,8,54,77) "
                    + "ORDER BY h.fechaReg DESC";
            Query q = getEntityManager().createQuery(hql);
            q.setParameter("idPaciente", facservicio.getFacFacturaPaciente().getIdPaciente().getIdPaciente());
            List<HcRegistro> listaResultadoAux = q.getResultList();
            Date s = null;
            HcRegistro definitivo = null;
            for(HcRegistro h: listaResultadoAux){
                if(h.getFechaReg().getYear() == facservicio.getFechaServicio().getYear()){
                    if(h.getFechaReg().getMonth()== facservicio.getFechaServicio().getMonth()){
                        if(h.getFechaReg().getDay()== facservicio.getFechaServicio().getDay()){
                            if(s == null){
                                s = h.getFechaReg();
                                definitivo = h;
                            }else{
                                if(h.getFechaReg().getTime() > s.getTime()){
                                    s = h.getFechaReg();
                                    definitivo = h;
                                }
                            }
                        }
                    }
                }
            }
            return definitivo;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    public int buscarMaximoFolio(Integer idPaciente) {
        try {            
            return Integer.parseInt(getEntityManager().createNativeQuery("SELECT MAX(folio) FROM hc_registro WHERE id_paciente = " + idPaciente).getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public HcRegistro historiaXFormulaMedicaPaciente(int idPaciente,int tipo){
        HcRegistro registro = null;
        try {
            String hql = "SELECT h FROM HcRegistro h where h.idPaciente.idPaciente=:idPaciente and h.idTipoReg.idTipoReg=:idTipo and not EXISTS (SELECT i FROM InvEntregaMedicamentos i WHERE i.idHistoriaClinica = h) ORDER BY h.idRegistro";
            Query q = getEntityManager().createQuery(hql).setParameter("idPaciente", idPaciente).setParameter("idTipo", tipo);
            List<HcRegistro> listaResultado = q.getResultList();
            for(HcRegistro h:listaResultado){
                //obtenemos el primer registro pendiente
                registro =h;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }
    
    public HcRegistro historiaXFormulaMedicaPacientePendiente(int idPaciente,int tipo){
        HcRegistro registro = null;
        try {
            String hql = "SELECT h FROM HcRegistro h where h.idPaciente.idPaciente=:idPaciente and h.idTipoReg.idTipoReg=:idTipo and EXISTS (SELECT i FROM InvEntregaMedicamentos i WHERE i.idHistoriaClinica = h and i.estado='P') ORDER BY h.idRegistro";
            Query q = getEntityManager().createQuery(hql).setParameter("idPaciente", idPaciente).setParameter("idTipo", tipo);
            List<HcRegistro> listaResultado = q.getResultList();
            for(HcRegistro h:listaResultado){
                //obtenemos el primer registro pendiente
                registro =h;
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }
}
