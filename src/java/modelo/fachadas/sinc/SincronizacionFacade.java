/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.fachadas.sinc;

import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgHorario;
import modelo.entidades.CfgItemsHorario;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitPaqDetalle;
import modelo.entidades.CitPaqMaestro;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacCaja;
import modelo.entidades.FacConsecutivo;
import modelo.entidades.FacConsumoInsumo;
import modelo.entidades.FacConsumoMedicamento;
import modelo.entidades.FacConsumoPaquete;
import modelo.entidades.FacConsumoServicio;
import modelo.entidades.FacFacturaInsumo;
import modelo.entidades.FacFacturaMedicamento;
import modelo.entidades.FacFacturaPaciente;
import modelo.entidades.FacFacturaPaquete;
import modelo.entidades.FacFacturaServicio;
import modelo.entidades.FacPeriodo;
import modelo.entidades.HcArchivos;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcRepExamenes;
import modelo.entidades.sinc.SinNodos;
import modelo.entidades.sinc.SinStatus;
import modelo.entidades.sinc.SinStatusPK;
import modelo.entidades.sinc.SinTablas;
/**
 *
 * @author santos
 */
@Stateless
public class SincronizacionFacade {

    
    //private Class<T> entityClass;
    @PersistenceContext(unitName = "OPENMEDICALPUREMOTO")
    private EntityManager em;

    @Inject
    private LoginMB loginMB;

    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Object> consultaNativaArreglo(String sql) {//consulta nativa que retorna una lista de listas
        try {
            return (List<Object>) getEntityManager().createNativeQuery(sql).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public int consultaNativaConteo(String sql) {
        try {
            return Integer.parseInt(getEntityManager().createNativeQuery(sql).getSingleResult().toString());
        } catch (Exception e) {
            return 0;
        }
    }

    /*Push*/
    public SinStatus existeRegistro(int idTabla, int idNodo, int idRegistro, boolean remoto) {
        try {
            String sql = "SELECT c FROM SinStatus c  WHERE c.sinStatusPK.idTabla = ?1 and c.sinStatusPK.idNodo = ?2 and ";
            if (remoto) {
                sql += "c.idRemoto = ?3";
            } else {
                sql += "c.sinStatusPK.idLocal = ?3";
            }
            Query query = em.createQuery(sql);
            query.setParameter(1, idTabla);
            query.setParameter(2, idNodo);
            query.setParameter(3, idRegistro);
            return (SinStatus) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void guardarRegistro(SinStatus r) {
        try {
            if (r.getSinStatusPK() == null) {
                em.persist(r);
            } else {
                em.merge(r);
            }

        } catch (Exception e) {
        }
    }

    public void guardarRegistro(int idTabla, int idNodo, int idLocal, int idRemoto) {
        try {
            SinStatus reg = new SinStatus();
            reg.setSinStatusPK(new SinStatusPK());
            reg.getSinStatusPK().setIdLocal(idLocal);
            reg.getSinStatusPK().setIdTabla(idTabla);
            reg.getSinStatusPK().setIdNodo(idNodo);
            reg.setIdRemoto(idRemoto);
            reg.setSinTablas(new SinTablas());
            reg.getSinTablas().setIdTabla(idTabla);
            reg.setSinNodos(new SinNodos());
            reg.getSinNodos().setIdNodo(idNodo);
            reg.setStatus(Boolean.FALSE);
            em.persist(reg);
        } catch (Exception e) {
        }
    }

    public int guardarPaciente(CfgPacientes p, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            if (p.getIdPaciente() == null) {
                em.persist(p);
                em.flush();
                guardarRegistro(idTabla, idNodo, p.getIdPaciente(), idLocal);
            } else {
                em.merge(p);
            }
            result = p.getIdPaciente();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CfgPacientes consultarPaciente(int id) {
        CfgPacientes obj = null;
        try {
            String sql = "SELECT c FROM CfgPacientes c WHERE c.idPaciente = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CfgPacientes) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public CfgPacientes consultarPaciente(String id) {
        CfgPacientes obj = null;
        try {
            String sql = "SELECT c FROM CfgPacientes c WHERE c.identificacion = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CfgPacientes) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHorario(CfgHorario h, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            if (h.getIdHorario() == null) {
                em.persist(h);
                em.flush();
                guardarRegistro(idTabla, idNodo, h.getIdHorario(), idLocal);
                /*String sql = "SELECT c.id_horario FROM cfg_horario c WHERE c.codigo = ?1";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, h.getCodigo());
                result = ((Integer) query.getSingleResult());*/
            } else {
                em.merge(h);
            }
            result = h.getIdHorario();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CfgHorario consultarHorario(int id) {
        CfgHorario obj = null;
        try {
            String sql = "SELECT c FROM CfgHorario c WHERE c.idHorario = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CfgHorario) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarItemsHorario(CfgItemsHorario h, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            //consultar id horario
            SinStatus s = existeRegistro(consultarTabla("cfg_horario"), idNodo, h.getIdHorario().getIdHorario(), true);
            h.getIdHorario().setIdHorario(s.getSinStatusPK().getIdLocal());
            if (h.getIdItemHorario() == null) {
                em.persist(h);
                em.flush();
                guardarRegistro(idTabla, idNodo, h.getIdItemHorario(), idLocal);
                /*String sql = "SELECT c.id_item_horario FROM cfg_items_horario c WHERE c.dia = ?1 and c.hora_inicio = ?2 and"
                        + " c.hora_final = ?3 and c.nombredia = ?4 ";
                Query query = em.createNativeQuery(sql);
                query.setParameter(1, h.getDia());
                query.setParameter(2, h.getHoraInicio());
                query.setParameter(3, h.getHoraFinal());
                query.setParameter(4, h.getNombredia());
                result = ((Integer) query.getSingleResult());*/
            } else {
                em.merge(h);
            }
            result = h.getIdItemHorario();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CfgItemsHorario consultarItemsHorario(int id) {
        CfgItemsHorario obj = null;
        try {
            String sql = "SELECT c FROM CfgItemsHorario c WHERE c.idItemHorario = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CfgItemsHorario) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarTurno(CitTurnos t, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            SinStatus s = existeRegistro(consultarTabla("cfg_horario"), idNodo, t.getIdHorario().getIdHorario(), true);
            t.getIdHorario().setIdHorario(s.getSinStatusPK().getIdLocal());
            if (t.getIdTurno() == null) {
                em.persist(t);
                em.flush();
                guardarRegistro(idTabla, idNodo, t.getIdTurno(), idLocal);
            } else {
                em.merge(t);
            }
            result = t.getIdTurno();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitTurnos consultarTurno(int id) {
        CitTurnos obj = null;
        try {
            String sql = "SELECT c FROM CitTurnos c WHERE c.idTurno = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitTurnos) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCitPaquete(CitPaqMaestro p, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            if (!p.getCitPaqDetalleList().isEmpty()) {
                p.getCitPaqDetalleList().clear();
            }
            if (p.getIdPaqMaestro() == null) {
                em.persist(p);
                em.flush();
                guardarRegistro(idTabla, idNodo, p.getIdPaqMaestro(), idLocal);
            } else {
                em.merge(p);
            }
            result = p.getIdPaqMaestro();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitPaqMaestro consultarCitPaquete(int id) {
        CitPaqMaestro obj = null;
        try {
            String sql = "SELECT c FROM CitPaqMaestro c WHERE c.idPaqMaestro = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitPaqMaestro) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCitPaqDetalle(CitPaqDetalle d, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            SinStatus s = existeRegistro(consultarTabla("cit_paq_maestro"), idNodo, d.getIdPaqMaestro().getIdPaqMaestro(), true);
            d.getIdPaqMaestro().setIdPaqMaestro(s.getSinStatusPK().getIdLocal());
            if (d.getIdPaqDetalle() == null) {
                em.persist(d);
                em.flush();
                guardarRegistro(idTabla, idNodo, d.getIdPaqDetalle(), idLocal);
            } else {
                em.merge(d);
            }
            result = d.getIdPaqDetalle();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitPaqDetalle consultarCitPaqDetalle(int id) {
        CitPaqDetalle obj = null;
        try {
            String sql = "SELECT c FROM CitPaqDetalle c WHERE c.idPaqDetalle = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitPaqDetalle) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCitAutorizaciones(CitAutorizaciones c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            if (!c.getCitAutorizacionesServiciosList().isEmpty()) {
                c.getCitAutorizacionesServiciosList().clear();
            }
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getPaciente().getIdPaciente(), true);
            c.getPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdAutorizacion() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdAutorizacion(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdAutorizacion();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitAutorizaciones consultarCitAutorizaciones(int id) {
        CitAutorizaciones obj = null;
        try {
            String sql = "SELECT c FROM CitAutorizaciones c WHERE c.idAutorizacion = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitAutorizaciones) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCitAutServ(CitAutorizacionesServicios c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //buscando autorizacion .....
            s = existeRegistro(consultarTabla("cit_autorizaciones"), idNodo, c.getCitAutorizacionesServiciosPK().getIdAutorizacion(), true);
            c.getCitAutorizacionesServiciosPK().setIdAutorizacion(s.getSinStatusPK().getIdLocal());
            c.setCitAutorizaciones(null);
            if (c.getIdSincronizador() == null) {
                em.persist(c);
                em.flush();
                c = em.find(CitAutorizacionesServicios.class, c.getCitAutorizacionesServiciosPK());
                guardarRegistro(idTabla, idNodo, c.getIdSincronizador(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitAutorizacionesServicios consultarCitAutServ(int id) {
        CitAutorizacionesServicios obj = null;
        try {
            String sql = "SELECT c FROM CitAutorizacionesServicios c WHERE c.idSincronizador = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitAutorizacionesServicios) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCita(CitCitas c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            s = existeRegistro(consultarTabla("cit_turnos"), idNodo, c.getIdTurno().getIdTurno(), true);
            c.getIdTurno().setIdTurno(s.getSinStatusPK().getIdLocal());
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getIdPaciente().getIdPaciente(), true);
            c.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdPaquete() != null) {
                s = existeRegistro(consultarTabla("cit_paq_maestro"), idNodo, c.getIdPaquete().getIdPaqMaestro(), true);
                c.getIdPaquete().setIdPaqMaestro(s.getSinStatusPK().getIdLocal());
            }

            if (c.getIdCita() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdCita(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdCita();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public CitCitas consultarCita(int id) {
        CitCitas obj = null;
        try {
            String sql = "SELECT c FROM CitCitas c WHERE c.idCita = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (CitCitas) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHistArch(HcArchivos h, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            SinStatus s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, h.getIdPaciente().getIdPaciente(), true);
            h.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (h.getIdArchivo() == null) {
                em.persist(h);
                em.flush();
                guardarRegistro(idTabla, idNodo, h.getIdArchivo().intValue(), idLocal);

            } else {
                em.merge(h);
            }
            result = h.getIdArchivo().intValue();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public HcArchivos consultarHistArch(int id) {
        HcArchivos obj = null;
        try {
            String sql = "SELECT c FROM HcArchivos c WHERE c.idArchivo = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (HcArchivos) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHistItem(HcItems hi, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            s = existeRegistro(consultarTabla("hc_registro"), idNodo, hi.getIdRegistro().getIdRegistro(), true);
            hi.getIdRegistro().setIdRegistro(s.getSinStatusPK().getIdLocal());
            if (hi.getIdItem() == null) {
                em.persist(hi);
                em.flush();
                guardarRegistro(idTabla, idNodo, hi.getIdItem(), idLocal);
            } else {
                em.merge(hi);
            }
            result = hi.getIdItem();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public HcItems consultarHistItem(int id) {
        HcItems obj = null;
        try {
            String sql = "SELECT c FROM HcItems c WHERE c.idItem = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (HcItems) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHistRep(HcRepExamenes hc, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            s = existeRegistro(consultarTabla("hc_registro"), idNodo, hc.getIdRegistro().getIdRegistro(), true);
            hc.getIdRegistro().setIdRegistro(s.getSinStatusPK().getIdLocal());
            if (hc.getIdRepExamenes() == null) {
                em.persist(hc);
                em.flush();
                guardarRegistro(idTabla, idNodo, hc.getIdRepExamenes(), idLocal);
            } else {
                em.merge(hc);
            }
            result = hc.getIdRepExamenes();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public HcRepExamenes consultarHistRep(int id) {
        HcRepExamenes obj = null;
        try {
            String sql = "SELECT c FROM HcRepExamenes c WHERE c.idRepExamenes = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (HcRepExamenes) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHistDet(HcDetalle hd, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            s = existeRegistro(consultarTabla("hc_registro"), idNodo, hd.getHcDetallePK().getIdRegistro(), true);
            hd.getHcDetallePK().setIdRegistro(s.getSinStatusPK().getIdLocal());
            if (hd.getIdSincronizador() == null) {
                em.persist(hd);
                em.flush();
                hd = em.find(HcDetalle.class, hd.getHcDetallePK());
                guardarRegistro(idTabla, idNodo, hd.getIdSincronizador(), idLocal);
            } else {
                em.merge(hd);
            }
            result = hd.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public HcDetalle consultarHistDet(int id) {
        HcDetalle obj = null;
        try {
            String sql = "SELECT c FROM HcDetalle c WHERE c.idSincronizador = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (HcDetalle) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarHistoria(HcRegistro h, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, h.getIdPaciente().getIdPaciente(), true);
            h.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            //consultar la cita asociada a la historia
            if (h.getIdCita() != null) {
                s = existeRegistro(consultarTabla("cit_citas"), idNodo, h.getIdCita().getIdCita(), true);
                h.getIdCita().setIdCita(s.getSinStatusPK().getIdLocal());
            }
            if (!h.getHcItemsList().isEmpty()) {
                h.getHcItemsList().clear();
            }
            if (!h.getHcDetalleList().isEmpty()) {
                h.getHcDetalleList().clear();
            }
            if (h.getIdRegistro() == null) {
                em.persist(h);
                em.flush();
                guardarRegistro(idTabla, idNodo, h.getIdRegistro(), idLocal);
            } else {
                em.merge(h);

            }
            result = h.getIdRegistro();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public HcRegistro consultarHistoria(int id) {
        HcRegistro obj = null;
        try {
            String sql = "SELECT c FROM HcRegistro c WHERE c.idRegistro = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (HcRegistro) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarCaja(FacCaja c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        try {
            if (c.getIdCaja() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdCaja(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdCaja();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public FacCaja consultarCaja(int id) {
        FacCaja obj = null;
        try {
            String sql = "SELECT c FROM FacCaja c WHERE c.idCaja = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (FacCaja) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int guardarConIns(FacConsumoInsumo c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getIdPaciente().getIdPaciente(), true);
            c.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdConsumoInsumo() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdConsumoInsumo(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdConsumoInsumo();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarConMed(FacConsumoMedicamento c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getIdPaciente().getIdPaciente(), true);
            c.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdConsumoMedicamento() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdConsumoMedicamento(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdConsumoMedicamento();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarConPaq(FacConsumoPaquete c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getIdPaciente().getIdPaciente(), true);
            c.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdConsumoPaquete() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdConsumoPaquete(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdConsumoPaquete();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarConSer(FacConsumoServicio c, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, c.getIdPaciente().getIdPaciente(), true);
            c.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (c.getIdConsumoServicio() == null) {
                em.persist(c);
                em.flush();
                guardarRegistro(idTabla, idNodo, c.getIdConsumoServicio(), idLocal);
            } else {
                em.merge(c);
            }
            result = c.getIdConsumoServicio();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public List<FacConsecutivo> buscarPorTipoDocumento(Integer tipo) {
        try {
            String hql = "SELECT c FROM FacConsecutivo c WHERE c.tipoDocumento.id = :tipo ORDER BY c.idConsecutivo";
            return getEntityManager().createQuery(hql).setParameter("tipo", tipo).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public FacConsecutivo findFacConsecutivo(Object id) {
        return getEntityManager().find(FacConsecutivo.class, id);
    }

    public String cerosIzquierda(int valor, int numCifras) {
        //aumenta ceros a la izquierda segun numCifas
        String v = String.valueOf(valor);
        int ceros = numCifras - v.length();
        if (v.length() < numCifras) {
            for (int i = 0; i < ceros; i++) {
                v = "0" + v;
            }
        }
        return v;
    }

    public boolean validarNoVacio(String valor) {
        //validas si una cadena no es vacia //true= no vacio //false=vacio
        if (valor == null) {
            return false;
        }
        return valor.trim().length() != 0;
    }

    public int guardarFactura(FacFacturaPaciente f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("cfg_pacientes"), idNodo, f.getIdPaciente().getIdPaciente(), true);
            f.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
            if (f.getIdCita() != null) {
                s = existeRegistro(consultarTabla("cit_citas"), idNodo, f.getIdCita().getIdCita(), true);
                f.getIdCita().setIdCita(s.getSinStatusPK().getIdLocal());
            }
            if (f.getIdCaja() != null) {
                s = existeRegistro(consultarTabla("fac_caja"), idNodo, f.getIdCaja().getIdCaja(), true);
                f.getIdCaja().setIdCaja(s.getSinStatusPK().getIdLocal());
            }
            if (f.getIdPeriodo() != null) {
                s = existeRegistro(consultarTabla("fac_periodo"), idNodo, f.getIdPeriodo().getIdPeriodo(), true);
                f.getIdPeriodo().setIdPeriodo(s.getSinStatusPK().getIdLocal());
            }
            if (!f.getFacFacturaServicioList().isEmpty()) {
                f.getFacFacturaServicioList().clear();
            }
            if (!f.getFacFacturaMedicamentoList().isEmpty()) {
                f.getFacFacturaMedicamentoList().clear();
            }
            if (!f.getFacFacturaPaqueteList().isEmpty()) {
                f.getFacFacturaPaqueteList().clear();
            }
            if (!f.getFacFacturaInsumoList().isEmpty()) {
                f.getFacFacturaInsumoList().clear();
            }
            if (!f.getFacFacturaAdmiList().isEmpty()) {
                f.getFacFacturaAdmiList().clear();
            }

            if (f.getIdFacturaPaciente() == null) {
                String numDocStr;
                int numDocInt;
                List<FacConsecutivo> listaConsecutivos = this.buscarPorTipoDocumento(f.getTipoDocumento().getId());
                FacConsecutivo consecutivoSeleccionado = null;
                if (listaConsecutivos != null && !listaConsecutivos.isEmpty()) {
                    for (FacConsecutivo c : listaConsecutivos) {
                        if (!Objects.equals(c.getActual(), c.getFin())) {
                            consecutivoSeleccionado = c;
                        }
                    }
                }
                if (consecutivoSeleccionado == null) {
                    throw new Exception("Consecutivo invalido");
                }

                consecutivoSeleccionado = this.findFacConsecutivo(consecutivoSeleccionado.getIdConsecutivo());
                if (Objects.equals(consecutivoSeleccionado.getActual(), consecutivoSeleccionado.getFin())) {//se vuelve a comprobar el consecutivo
                    throw new Exception("Otro usuario utiliza el ultimo consecutivo para el tipo de documento seleccionado");

                } else {
                    numDocInt = consecutivoSeleccionado.getActual() + 1;
                    numDocStr = cerosIzquierda(numDocInt, 5);//el documento se completa con ceros a 5 cifras
                    if (validarNoVacio(consecutivoSeleccionado.getPrefijo())) {
                        numDocStr = consecutivoSeleccionado.getPrefijo() + numDocStr;
                    }
                    consecutivoSeleccionado.setActual(consecutivoSeleccionado.getActual() + 1);
                    getEntityManager().merge(consecutivoSeleccionado);
                }
                f.setNumeroDocumento(numDocInt);
                f.setCodigoDocumento(numDocStr);
                em.persist(f);
                em.flush();
                guardarRegistro(idTabla, idNodo, f.getIdFacturaPaciente(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdFacturaPaciente();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarFacturaIns(FacFacturaInsumo f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("fac_factura_paciente"), idNodo, f.getFacFacturaInsumoPK().getIdFactura(), true);
            f.getFacFacturaInsumoPK().setIdFactura(s.getSinStatusPK().getIdLocal());
            f.setFacFacturaPaciente(null);

            if (f.getIdSincronizador() == null) {
                em.persist(f);
                em.flush();
                f = em.find(FacFacturaInsumo.class, f.getFacFacturaInsumoPK());
                guardarRegistro(idTabla, idNodo, f.getIdSincronizador(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarFacturaMed(FacFacturaMedicamento f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("fac_factura_paciente"), idNodo, f.getFacFacturaMedicamentoPK().getIdFactura(), true);
            f.getFacFacturaMedicamentoPK().setIdFactura(s.getSinStatusPK().getIdLocal());
            f.setFacFacturaPaciente(null);
            if (f.getIdSincronizador() == null) {
                em.persist(f);
                em.flush();
                f = em.find(FacFacturaMedicamento.class, f.getFacFacturaMedicamentoPK());
                guardarRegistro(idTabla, idNodo, f.getIdSincronizador(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarFacturaPaq(FacFacturaPaquete f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("fac_factura_paciente"), idNodo, f.getFacFacturaPaquetePK().getIdFactura(), true);
            f.getFacFacturaPaquetePK().setIdFactura(s.getSinStatusPK().getIdLocal());
            f.setFacFacturaPaciente(null);
            if (f.getIdSincronizador() == null) {
                em.persist(f);
                em.flush();
                f = em.find(FacFacturaPaquete.class, f.getFacFacturaPaquetePK());
                guardarRegistro(idTabla, idNodo, f.getIdSincronizador(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarFacturaSer(FacFacturaServicio f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            //consultar id paciente
            s = existeRegistro(consultarTabla("fac_factura_paciente"), idNodo, f.getFacFacturaServicioPK().getIdFactura(), true);
            f.getFacFacturaServicioPK().setIdFactura(s.getSinStatusPK().getIdLocal());
            f.setFacFacturaPaciente(null);
            if (f.getIdSincronizador() == null) {
                em.persist(f);
                em.flush();
                f = em.find(FacFacturaServicio.class, f.getFacFacturaServicioPK());
                guardarRegistro(idTabla, idNodo, f.getIdSincronizador(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdSincronizador();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public int guardarFacturaPer(FacPeriodo f, int idTabla, int idNodo, int idLocal) {
        int result = 0;
        SinStatus s = null;
        try {
            if (f.getIdPeriodo() == null) {
                em.persist(f);
                em.flush();
                guardarRegistro(idTabla, idNodo, f.getIdPeriodo(), idLocal);
            } else {
                em.merge(f);
            }
            result = f.getIdPeriodo();
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public FacPeriodo consultarFacturaPer(int id) {
        FacPeriodo obj = null;
        try {
            String sql = "SELECT c FROM FacPeriodo c WHERE c.idPeriodo = ?1 ";
            Query query = em.createQuery(sql);
            query.setParameter(1, id);
            obj = (FacPeriodo) query.getSingleResult();
        } catch (Exception e) {
        }
        return obj;
    }

    public int consultarTabla(String tabla) {
        int id = 0;
        try {
            String sql = "select id_tabla from sin_tablas where tabla = ?1 ";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, tabla);
            id = ((Integer) query.getSingleResult());
        } catch (Exception e) {
        }
        return id;
    }

    /*Pull*/
    public List<SinStatus> getRegistros(int idNodo) {
        String hql = "SELECT s FROM SinStatus s where s.status = ?1 and s.sinNodos.idNodo = ?2 Order by s.sinTablas.orden ";
        return getEntityManager().createQuery(hql).setParameter(1, true).setParameter(2, idNodo).getResultList();
    }

}