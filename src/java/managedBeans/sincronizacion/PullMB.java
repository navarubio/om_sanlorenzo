/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.sincronizacion;

import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.net.ssl.HttpsURLConnection;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgHorario;
import modelo.entidades.CfgItemsHorario;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.CitPaqDetalle;
import modelo.entidades.CitPaqMaestro;
import modelo.entidades.CitTurnos;
import modelo.entidades.FacCaja;
import modelo.entidades.FacPeriodo;
import modelo.entidades.sinc.SinStatus;
import modelo.entidades.HcArchivos;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcRepExamenes;
import modelo.fachadas.CfgHorarioFacade;
import modelo.fachadas.CfgItemsHorarioFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitPaqDetalleFacade;
import modelo.fachadas.CitPaqMaestroFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacCajaFacade;
import modelo.fachadas.FacPeriodoFacade;
import modelo.fachadas.HcArchivosFacade;
import modelo.fachadas.HcDetalleFacade;
import modelo.fachadas.HcItemsFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.HcRepExamenesFacade;
import modelo.fachadas.sinc.SinStatusFacade;
import modelo.fachadas.sinc.SinTablasFacade;
import modelo.fachadas.sinc.SincronizacionFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "pullMB")
@ViewScoped
public class PullMB extends MetodosGenerales implements Serializable {

    @EJB
    private SincronizacionFacade sincronizador;
    @EJB
    private SinStatusFacade sinFacade;
    @EJB
    private SinTablasFacade sinTablaFacade;
    @EJB
    private CfgPacientesFacade pacienteFacade;
    @EJB
    private CfgHorarioFacade horarioFacade;
    @EJB
    private CfgUsuariosFacade usuarioFacade;
    @EJB
    private CfgItemsHorarioFacade itHorarioFacade;
    @EJB
    private CitTurnosFacade turnosFacade;
    @EJB
    private CitCitasFacade citasFacade;
    @EJB
    private CitPaqMaestroFacade citPaqFacade;
    @EJB
    private CitPaqDetalleFacade citPaqDetFacade;
    @EJB
    private CitAutorizacionesFacade citAutFacade;
    @EJB
    private CitAutorizacionesServiciosFacade citAutServFacade;
    @EJB
    private HcArchivosFacade hcArchFacade;
    @EJB
    private HcItemsFacade hcItemsFacade;
    @EJB
    private HcRepExamenesFacade hcRepFacade;
    @EJB
    private HcDetalleFacade hcDetFacade;
    @EJB
    private FacCajaFacade cajaFacade;
    @EJB
    private HcRegistroFacade registroFacade;
    @EJB
    private FacPeriodoFacade facPerFacade;

    private Integer progress;
    private double dblProgreso;
    private String progreso = "-";
    private String pacientes = "-";
    private String historias = "-";
    private String facturas = "-";
    private String totalRegistros = "-";

    Connection conn;
    private boolean renderBoton;

    public PullMB() {
        renderBoton = false;
    }

    @PostConstruct
    public void init() {
        if (getConnectionStatus()) {
            renderBoton = true;

        } else {
            imprimirMensaje("Sin acceso", "No posee acceso a internet", FacesMessage.SEVERITY_WARN);
        }
    }

    public void pullDatos() {
        int pacientesSincronizados = 0;
        int historiasSincronizadas = 0;
        int facturasSincronizadas = 0;
        String tabla = "";
        int idTabla = 0, id = 0;
        double salto = 100;
        boolean result = false;
        int current = 0;
        progreso = "Verificar Status de conexion...";
        if (getConnectionStatus()) {
            try {
                LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
                int idNodo = loginMB.getCentroDeAtencionactual().getIdSede();
                //buscamos registros en el online
                progreso = "Consultar Registros pendientes por descargar";
                List<SinStatus> pendientes = sincronizador.getRegistros(idNodo);
                if (pendientes.size() > 0) {
                    salto = (double) 100.0 / pendientes.size();
                }
                progress = 0;
                progreso = "Cantidad de registros a procesar " + pendientes.size();
                for (SinStatus registro : pendientes) {//resgistros a sincronizar ordenados por tabla
                    tabla = registro.getSinTablas().getTabla();
                    idTabla = registro.getSinTablas().getIdTabla();
                    result = false;
                    current++;
                    progreso = "Procesando " + current + " de " + pendientes.size();
                    //buscar en local para ver si existe
                    SinStatus registroLocal = sinFacade.existeRegistro(idTabla, registro.getSinStatusPK().getIdLocal(), true);
                    progreso = "Procesando tabla " + tabla + " identificador " + registro.getSinStatusPK().getIdLocal();
                    SinStatus s = null;
                    SinStatus su = null;
                    switch (tabla) {
                        case "cfg_usuarios":
                            CfgUsuarios usuario = sincronizador.consultarUsuario(registro.getSinStatusPK().getIdLocal());
                            if (usuario != null) {
                                if (registroLocal == null) {// no existe se debe insertar
                                    usuario.setIdUsuario(null);
                                    usuarioFacade.create(usuario);
                                    registroLocal = sinFacade.existeRegistro(idTabla, usuario.getIdUsuario(), false);
                                } else {
                                    usuario.setIdUsuario(registroLocal.getSinStatusPK().getIdLocal());
                                    usuarioFacade.edit(usuario);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cfg_pacientes":
                            CfgPacientes p = sincronizador.consultarPaciente(registro.getSinStatusPK().getIdLocal());
                            if (p != null) {
                                if (registroLocal == null) {// no existe se debe insertar
                                    p.setIdPaciente(null);
                                    pacienteFacade.create(p);
                                    registroLocal = sinFacade.existeRegistro(idTabla, p.getIdPaciente(), false);
                                } else {
                                    p.setIdPaciente(registroLocal.getSinStatusPK().getIdLocal());
                                    pacienteFacade.edit(p);
                                }
                                updateSinStatus(registroLocal, registro);
                                pacientesSincronizados++;
                            }
                            break;
                        case "cfg_horario":
                            CfgHorario horario = sincronizador.consultarHorario(registro.getSinStatusPK().getIdLocal());
                            if (horario != null) {
                                if (registroLocal == null) {// no existe se debe insertar
                                    horario.setIdHorario(null);
                                    horarioFacade.create(horario);
                                    registroLocal = sinFacade.existeRegistro(idTabla, horario.getIdHorario(), false);
                                } else {
                                    horario.setIdHorario(registroLocal.getSinStatusPK().getIdLocal());
                                    horarioFacade.edit(horario);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cfg_items_horario":
                            CfgItemsHorario itHorario = sincronizador.consultarItemsHorario(registro.getSinStatusPK().getIdLocal());
                            if (itHorario != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_horario"), itHorario.getIdHorario().getIdHorario(), true);
                                itHorario.getIdHorario().setIdHorario(s.getSinStatusPK().getIdLocal());
                                if (registroLocal == null) {// no existe se debe insertar
                                    itHorario.setIdItemHorario(null);
                                    itHorarioFacade.create(itHorario);
                                    registroLocal = sinFacade.existeRegistro(idTabla, itHorario.getIdItemHorario(), false);
                                } else {
                                    itHorario.setIdItemHorario(registroLocal.getSinStatusPK().getIdLocal());
                                    itHorarioFacade.edit(itHorario);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cit_paq_maestro":
                            CitPaqMaestro citPaquete = sincronizador.consultarCitPaquete(registro.getSinStatusPK().getIdLocal());
                            if (citPaquete != null) {
                                if (registroLocal == null) {// no existe se debe insertar
                                    citPaquete.setIdPaqMaestro(null);
                                    citPaqFacade.create(citPaquete);
                                    registroLocal = sinFacade.existeRegistro(idTabla, citPaquete.getIdPaqMaestro(), false);
                                } else {
                                    citPaquete.setIdPaqMaestro(registroLocal.getSinStatusPK().getIdLocal());
                                    citPaqFacade.edit(citPaquete);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cit_paq_detalle":
                            CitPaqDetalle citPaqDet = sincronizador.consultarCitPaqDetalle(registro.getSinStatusPK().getIdLocal());
                            if (citPaqDet != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cit_paq_maestro"), citPaqDet.getIdPaqMaestro().getIdPaqMaestro(), true);
                                citPaqDet.getIdPaqMaestro().setIdPaqMaestro(s.getSinStatusPK().getIdLocal());
                                su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), citPaqDet.getIdPrestador().getIdUsuario(), true);
                                citPaqDet.getIdPrestador().setIdUsuario(su.getSinStatusPK().getIdLocal());
                                if (registroLocal == null) {// no existe se debe insertar
                                    citPaqDet.setIdPaqDetalle(null);
                                    citPaqDetFacade.create(citPaqDet);
                                    registroLocal = sinFacade.existeRegistro(idTabla, citPaqDet.getIdPaqDetalle(), false);
                                } else {
                                    citPaqDet.setIdPaqDetalle(registroLocal.getSinStatusPK().getIdLocal());
                                    citPaqDetFacade.edit(citPaqDet);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cit_autorizaciones":
                            CitAutorizaciones citAut = sincronizador.consultarCitAutorizaciones(registro.getSinStatusPK().getIdLocal());
                            if (citAut != null) {
                                if (!citAut.getCitAutorizacionesServiciosList().isEmpty()) {
                                    citAut.getCitAutorizacionesServiciosList().clear();
                                }
                                su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), citAut.getIdUsuarioCreador().getIdUsuario(), true);
                                citAut.getIdUsuarioCreador().setIdUsuario(su.getSinStatusPK().getIdLocal());

                                if (registroLocal == null) {// no existe se debe insertar
                                    citAut.setIdAutorizacion(null);
                                    citAutFacade.create(citAut);
                                    registroLocal = sinFacade.existeRegistro(idTabla, citAut.getIdAutorizacion(), false);
                                } else {
                                    citAut.setIdAutorizacion(registroLocal.getSinStatusPK().getIdLocal());
                                    citAutFacade.edit(citAut);
                                }
                                updateSinStatus(registroLocal, registro);
                            }

                            break;
                        case "cit_autorizaciones_servicios":
                            CitAutorizacionesServicios citAutServ = sincronizador.consultarCitAutServ(registro.getSinStatusPK().getIdLocal());
                            if (citAutServ != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cit_autorizaciones"), citAutServ.getCitAutorizacionesServiciosPK().getIdAutorizacion(), true);
                                citAutServ.getCitAutorizacionesServiciosPK().setIdAutorizacion(s.getSinStatusPK().getIdLocal());
                                citAutServ.setCitAutorizaciones(null);
                                if (registroLocal == null) {// no existe se debe insertar
                                    citAutServ.setIdSincronizador(null);
                                    citAutServFacade.create(citAutServ);
                                    citAutServ = citAutServFacade.find(citAutServ.getCitAutorizacionesServiciosPK());
                                    registroLocal = sinFacade.existeRegistro(idTabla, citAutServ.getIdSincronizador(), false);
                                } else {
                                    citAutServ.setIdSincronizador(registroLocal.getSinStatusPK().getIdLocal());
                                    citAutServFacade.edit(citAutServ);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cit_turnos":
                            CitTurnos turno = sincronizador.consultarTurno(registro.getSinStatusPK().getIdLocal());
                            if (turno != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_horario"), turno.getIdHorario().getIdHorario(), true);
                                turno.getIdHorario().setIdHorario(s.getSinStatusPK().getIdLocal());

                                su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), turno.getIdPrestador().getIdUsuario(), true);
                                turno.getIdPrestador().setIdUsuario(su.getSinStatusPK().getIdLocal());

                                if (registroLocal == null) {// no existe se debe insertar
                                    turno.setIdTurno(null);
                                    turnosFacade.create(turno);
                                    registroLocal = sinFacade.existeRegistro(idTabla, turno.getIdTurno(), false);
                                } else {
                                    turno.setIdTurno(registroLocal.getSinStatusPK().getIdLocal());
                                    turnosFacade.edit(turno);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "cit_citas":
                            CitCitas cita = sincronizador.consultarCita(registro.getSinStatusPK().getIdLocal());
                            if (cita != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_pacientes"), cita.getIdPaciente().getIdPaciente(), true);
                                cita.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cit_turnos"), cita.getIdTurno().getIdTurno(), true);
                                cita.getIdTurno().setIdTurno(s.getSinStatusPK().getIdLocal());

                                su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), cita.getIdPrestador().getIdUsuario(), true);
                                cita.getIdPrestador().setIdUsuario(su.getSinStatusPK().getIdLocal());

                                if (cita.getIdPaquete() != null) {
                                    s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cit_paq_maestro"), cita.getIdPaquete().getIdPaqMaestro(), true);
                                    cita.getIdPaquete().setIdPaqMaestro(s.getSinStatusPK().getIdLocal());
                                }
                                if (registroLocal == null) {// no existe se debe insertar
                                    cita.setIdCita(null);
                                    citasFacade.create(cita);
                                    registroLocal = sinFacade.existeRegistro(idTabla, cita.getIdCita(), false);
                                } else {
                                    cita.setIdCita(registroLocal.getSinStatusPK().getIdLocal());
                                    citasFacade.edit(cita);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "hc_archivos":
                            HcArchivos ha = sincronizador.consultarHistArch(registro.getSinStatusPK().getIdLocal());
                            if (ha != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_pacientes"), ha.getIdPaciente().getIdPaciente(), true);
                                ha.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
                                if (registroLocal == null) {// no existe se debe insertar
                                    ha.setIdArchivo(null);
                                    hcArchFacade.create(ha);
                                    registroLocal = sinFacade.existeRegistro(idTabla, ha.getIdArchivo().intValue(), false);
                                } else {
                                    ha.setIdArchivo((long) registroLocal.getSinStatusPK().getIdLocal());
                                    hcArchFacade.edit(ha);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "hc_items":
                            HcItems hi = sincronizador.consultarHistItem(registro.getSinStatusPK().getIdLocal());
                            if (hi != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("hc_registro"), hi.getIdRegistro().getIdRegistro(), true);
                                hi.getIdRegistro().setIdRegistro(s.getSinStatusPK().getIdLocal());
                                if (registroLocal == null) {// no existe se debe insertar
                                    hi.setIdItem(null);
                                    hcItemsFacade.create(hi);
                                    registroLocal = sinFacade.existeRegistro(idTabla, hi.getIdItem(), false);
                                } else {
                                    hi.setIdItem(registroLocal.getSinStatusPK().getIdLocal());
                                    hcItemsFacade.edit(hi);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "hc_rep_examenes":
                            HcRepExamenes hcrep = sincronizador.consultarHistRep(registro.getSinStatusPK().getIdLocal());
                            if (hcrep != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("hc_registro"), hcrep.getIdRegistro().getIdRegistro(), true);
                                hcrep.getIdRegistro().setIdRegistro(s.getSinStatusPK().getIdLocal());
                                if (registroLocal == null) {// no existe se debe insertar
                                    hcrep.setIdRepExamenes(null);
                                    hcRepFacade.create(hcrep);
                                    registroLocal = sinFacade.existeRegistro(idTabla, hcrep.getIdRepExamenes(), false);
                                } else {
                                    hcrep.setIdRepExamenes(registroLocal.getSinStatusPK().getIdLocal());
                                    hcRepFacade.edit(hcrep);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "hc_detalle":
                            HcDetalle hdet = sincronizador.consultarHistDet(registro.getSinStatusPK().getIdLocal());
                            if (hdet != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("hc_registro"), hdet.getHcDetallePK().getIdRegistro(), true);
                                hdet.getHcDetallePK().setIdRegistro(s.getSinStatusPK().getIdLocal());
                                hdet.setHcRegistro(null);
                                if (registroLocal == null) {// no existe se debe insertar
                                    hdet.setIdSincronizador(null);
                                    hcDetFacade.create(hdet);
                                    hdet = hcDetFacade.find(hdet.getHcDetallePK());
                                    registroLocal = sinFacade.existeRegistro(idTabla, hdet.getIdSincronizador(), false);
                                } else {
                                    hdet.setIdSincronizador(registroLocal.getSinStatusPK().getIdLocal());
                                    hcDetFacade.edit(hdet);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "hc_registro":
                            HcRegistro h = sincronizador.consultarHistoria(registro.getSinStatusPK().getIdLocal());
                            if (h != null) {
                                s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_pacientes"), h.getIdPaciente().getIdPaciente(), true);
                                h.getIdPaciente().setIdPaciente(s.getSinStatusPK().getIdLocal());
                                if (h.getIdMedico() != null) {
                                    su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), h.getIdMedico().getIdUsuario(), true);
                                    h.getIdMedico().setIdUsuario(su.getSinStatusPK().getIdLocal());
                                }
                                if (h.getIdCita() != null) {
                                    s = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cit_citas"), h.getIdCita().getIdCita(), true);
                                    h.getIdCita().setIdCita(s.getSinStatusPK().getIdLocal());
                                }
                                if (!h.getHcDetalleList().isEmpty()) {
                                    h.getHcDetalleList().clear();
                                }
                                if (!h.getHcItemsList().isEmpty()) {
                                    h.getHcItemsList().clear();
                                }
                                if (registroLocal == null) {// no existe se debe insertar
                                    h.setIdRegistro(null);
                                    registroFacade.create(h);
                                    registroLocal = sinFacade.existeRegistro(idTabla, h.getIdRegistro(), false);
                                } else {
                                    h.setIdRegistro(registroLocal.getSinStatusPK().getIdLocal());
                                    registroFacade.edit(h);
                                }
                                updateSinStatus(registroLocal, registro);
                                historiasSincronizadas++;
                            }
                            break;
                        case "fac_caja":
                            FacCaja f = sincronizador.consultarCaja(registro.getSinStatusPK().getIdLocal());
                            if (f != null) {
                                if (f.getIdUsuario() != null) {
                                    su = sinFacade.existeRegistro(sinTablaFacade.consultarIdTabla("cfg_usuarios"), f.getIdUsuario().getIdUsuario(), true);
                                    f.getIdUsuario().setIdUsuario(su.getSinStatusPK().getIdLocal());
                                }
                                if (registroLocal == null) {// no existe se debe insertar
                                    f.setIdCaja(null);
                                    cajaFacade.create(f);
                                    registroLocal = sinFacade.existeRegistro(idTabla, f.getIdCaja(), false);
                                } else {
                                    f.setIdCaja(registroLocal.getSinStatusPK().getIdLocal());
                                    cajaFacade.edit(f);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                        case "fac_periodo":
                            FacPeriodo facPer = sincronizador.consultarFacturaPer(registro.getSinStatusPK().getIdLocal());
                            if (facPer != null) {
                                if (registroLocal == null) {// no existe se debe insertar
                                    facPer.setIdPeriodo(null);
                                    facPerFacade.create(facPer);
                                    registroLocal = sinFacade.existeRegistro(idTabla, facPer.getIdPeriodo(), false);
                                } else {
                                    facPer.setIdPeriodo(registroLocal.getSinStatusPK().getIdLocal());
                                    facPerFacade.edit(facPer);
                                }
                                updateSinStatus(registroLocal, registro);
                            }
                            break;
                    }
                    dblProgreso = (double) (dblProgreso + salto);
                    progress = (int) dblProgreso;
                }
                progress = 100;
                progreso = "sincronización Finalizada";
                pacientes = "Total Pacientes sincronizados " + pacientesSincronizados;
                historias = "Total Historias sincronizadas " + historiasSincronizadas;
                facturas = "Total Facturas sincronizadas " + facturasSincronizadas;
                totalRegistros = "Total Registros sincronizados " + pendientes.size();

            } catch (Exception e) {
                System.out.println("" + e.getMessage());
            }
        } else {
            imprimirMensaje("Sin acceso", "No posee acceso a internet", FacesMessage.SEVERITY_WARN);
        }
    }

    public boolean updateSinStatus(SinStatus registroLocal, SinStatus registro) {
        boolean result = false;
        try {
            registroLocal.setStatus(Boolean.FALSE);
            registroLocal.setIdRemoto(registro.getSinStatusPK().getIdLocal());
            sinFacade.edit(registroLocal);
            registro.setIdRemoto(registroLocal.getSinStatusPK().getIdLocal());
            registro.setStatus(Boolean.FALSE);
            sincronizador.guardarRegistro(registro);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public Integer getProgress() {
        if (progress == null) {
            progress = 0;

        } else if (progress > 100) {
            progress = 100;
        }
        RequestContext.getCurrentInstance().update("frmSincronizacion:dialogo");
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void onComplete() {
        imprimirMensaje("Sincronizacion", "Sincronización Terminada", FacesMessage.SEVERITY_INFO);
    }

    private boolean getConnectionStatus() {
        try {
            URL u = new URL("https://www.google.es/");
            HttpsURLConnection huc = (HttpsURLConnection) u.openConnection();
            huc.connect();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public boolean isRenderBoton() {
        return renderBoton;
    }

    public void setRenderBoton(boolean renderBoton) {
        this.renderBoton = renderBoton;
    }

    public String getProgreso() {
        return progreso;
    }

    public void setProgreso(String progreso) {
        this.progreso = progreso;
    }

    public String getPacientes() {
        return pacientes;
    }

    public void setPacientes(String pacientes) {
        this.pacientes = pacientes;
    }

    public String getHistorias() {
        return historias;
    }

    public void setHistorias(String historias) {
        this.historias = historias;
    }

    public String getFacturas() {
        return facturas;
    }

    public void setFacturas(String facturas) {
        this.facturas = facturas;
    }

    public String getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(String totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

}
