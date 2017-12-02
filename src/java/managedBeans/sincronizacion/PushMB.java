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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.naming.InitialContext;
import javax.net.ssl.HttpsURLConnection;
import javax.transaction.UserTransaction;
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
import modelo.entidades.sinc.SinStatus;
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
import modelo.fachadas.FacConsumoInsumoFacade;
import modelo.fachadas.FacConsumoMedicamentoFacade;
import modelo.fachadas.FacConsumoPaqueteFacade;
import modelo.fachadas.FacConsumoServicioFacade;
import modelo.fachadas.FacFacturaInsumoFacade;
import modelo.fachadas.FacFacturaMedicamentoFacade;
import modelo.fachadas.FacFacturaPacienteFacade;
import modelo.fachadas.FacFacturaPaqueteFacade;
import modelo.fachadas.FacFacturaServicioFacade;
import modelo.fachadas.FacPeriodoFacade;
import modelo.fachadas.HcArchivosFacade;
import modelo.fachadas.HcDetalleFacade;
import modelo.fachadas.HcItemsFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.HcRepExamenesFacade;
import modelo.fachadas.sinc.SinStatusFacade;
import modelo.fachadas.sinc.SincronizacionFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "pushMB")
@ViewScoped
public class PushMB extends MetodosGenerales implements Serializable {

    @EJB
    private CfgPacientesFacade pacientesFacade;
    @EJB
    private HcRegistroFacade registroFacade;
    @EJB
    private CfgHorarioFacade horarioFacade;
    @EJB
    private CfgUsuariosFacade usuarioFacade;
    @EJB
    private CfgItemsHorarioFacade itHorarioFacade;
    @EJB
    private SincronizacionFacade sincronizador;
    @EJB
    private SinStatusFacade sinStatus;
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
    private FacConsumoInsumoFacade conInsFacade;
    @EJB
    private FacConsumoMedicamentoFacade conMedFacade;
    @EJB
    private FacConsumoPaqueteFacade conPaqFacade;
    @EJB
    private FacConsumoServicioFacade conServFacade;
    @EJB
    private FacFacturaPacienteFacade facFacade;
    @EJB
    private FacFacturaInsumoFacade facInsFacade;
    @EJB
    private FacFacturaMedicamentoFacade facMedFacade;
    @EJB
    private FacFacturaPaqueteFacade facPaqFacade;
    @EJB
    private FacFacturaServicioFacade facSerFacade;
    @EJB
    private FacPeriodoFacade facPerFacade;
    Connection conn;

    private Integer progress;
    private double dblProgreso;
    private String progreso = "-";
    private String pacientes = "-";
    private String historias = "-";
    private String facturas = "-";
    private String totalRegistros = "-";

    private boolean renderBoton;

    public PushMB() {
    }

    @PostConstruct
    public void init() {
        if (getConnectionStatus()) {
            renderBoton = true;
        } else {
            imprimirMensaje("Sin acceso", "No posee acceso a internet", FacesMessage.SEVERITY_WARN);
        }
    }

    public void setDatos() {
        int pacientesSincronizados = 0;
        int historiasSincronizadas = 0;
        int facturasSincronizadas = 0;
        String tabla = "";
        int idTabla = 0, id = 0;
        double salto = 100;
        UserTransaction transaction = null;
        boolean result = false;
        int current = 0;
        progreso = "Verificar Status de conexion...";
        if (getConnectionStatus()) {
            try {
                LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
                int idNodo = loginMB.getCentroDeAtencionactual().getIdSede();
                transaction = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
                progreso = "Consultar registros a procesar";
                List<SinStatus> pendientes = sinStatus.buscarOrdenado(true);
                if (!pendientes.isEmpty()) {
                    salto = (double) 100.0 / pendientes.size();
                }
                progress = 0;
                progreso = "Cantidad de registros " + pendientes.size();
                for (SinStatus registro : pendientes) {//resgistros a sincronizar ordenados por tabla
                    tabla = registro.getSinTablas().getTabla();
                    idTabla = registro.getSinTablas().getIdTabla();
                    result = false;
                    current++;
                    progreso = "Sincronizando registro " + current + " de " + pendientes.size();
                    //buscar en el remoto para ver si existe
                    System.out.println("Consulta registro en remoto");
                    SinStatus registroRemoto = sincronizador.existeRegistro(idTabla, idNodo, registro.getSinStatusPK().getIdLocal(), true);
                    System.out.println("devolvio " + registroRemoto);
                    progreso = "Procesando Tabla " + tabla + " " + registro.getSinStatusPK().getIdLocal();
                    //transaction.begin();
                    System.out.println(progreso);
                    switch (tabla) {
                        case "cfg_usuarios":
                            CfgUsuarios usuario = usuarioFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (usuario != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    //buscamos el paciente por si otro nodo lo cargo
                                    System.out.println("Consulta usuario remoto por identificacion " + usuario.getIdentificacion());
                                    CfgUsuarios uRemoto = sincronizador.consultarUsuarioIdentificacion(usuario.getIdentificacion());
                                    //Consultar de nuevo pero por login
                                    if(uRemoto==null){
                                        uRemoto = sincronizador.consultarUsuarioLogin(usuario.getLoginUsuario());
                                    }
                                    System.out.println("Devolvio " + uRemoto);
                                    if (uRemoto == null) {
                                        usuario.setIdUsuario(null);
                                    } else {
                                        usuario.setIdUsuario(uRemoto.getIdUsuario());
                                        registroRemoto = sincronizador.existeRegistro(idTabla, idNodo, uRemoto.getIdUsuario(), false);
                                        registroRemoto.setStatus(Boolean.FALSE);
                                        registroRemoto.setIdRemoto(registro.getSinStatusPK().getIdLocal());
                                        sincronizador.guardarRegistro(registroRemoto);
                                    }
                                } else {
                                    usuario.setIdUsuario(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarUsuario(usuario, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cfg_pacientes":
                            System.out.println("Consulta paciente local");
                            CfgPacientes p = pacientesFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (p != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    //buscamos el paciente por si otro nodo lo cargo
                                    System.out.println("Consulta paciente remoto por identificacion " + p.getIdentificacion());
                                    CfgPacientes pRemoto = sincronizador.consultarPaciente(p.getIdentificacion());
                                    System.out.println("Devolvio " + pRemoto);
                                    if (pRemoto == null) {
                                        p.setIdPaciente(null);
                                    } else {
                                        p.setIdPaciente(pRemoto.getIdPaciente());
                                        registroRemoto = sincronizador.existeRegistro(idTabla, idNodo, pRemoto.getIdPaciente(), false);
                                        registroRemoto.setStatus(Boolean.FALSE);
                                        registroRemoto.setIdRemoto(registro.getSinStatusPK().getIdLocal());
                                        sincronizador.guardarRegistro(registroRemoto);
                                    }
                                } else {
                                    p.setIdPaciente(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarPaciente(p, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                                pacientesSincronizados++;
                            } else {
                                System.out.println("No se encontro paciente  se colocarástatus null por eliminacion");
                                registro.setStatus(false);
                                sinStatus.edit(registro);
                            }
                            break;
                        case "cfg_horario":
                            CfgHorario horario = horarioFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (horario != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    horario.setIdHorario(null);
                                } else {
                                    horario.setIdHorario(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHorario(horario, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }else{
                                 System.out.println("No se encontro paciente  se colocarástatus null por eliminacion");
                                registro.setStatus(false);
                                sinStatus.edit(registro);
                            }
                            break;
                        case "cfg_items_horario":
                            CfgItemsHorario itHorario = itHorarioFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (itHorario != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    itHorario.setIdItemHorario(null);
                                } else {
                                    itHorario.setIdItemHorario(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarItemsHorario(itHorario, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_paq_maestro":
                            CitPaqMaestro citPaquete = citPaqFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (citPaquete != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    citPaquete.setIdPaqMaestro(null);
                                } else {
                                    citPaquete.setIdPaqMaestro(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCitPaquete(citPaquete, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_paq_detalle":
                            CitPaqDetalle citPaqDet = citPaqDetFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (citPaqDet != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    citPaqDet.setIdPaqDetalle(null);
                                } else {
                                    citPaqDet.setIdPaqDetalle(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCitPaqDetalle(citPaqDet, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_autorizaciones":
                            CitAutorizaciones citAutorizaciones = citAutFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (citAutorizaciones != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    citAutorizaciones.setIdAutorizacion(null);
                                } else {
                                    citAutorizaciones.setIdAutorizacion(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCitAutorizaciones(citAutorizaciones, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_autorizaciones_servicios":
                            CitAutorizacionesServicios citAutServ = citAutServFacade.consultarCitAutServ(registro.getSinStatusPK().getIdLocal());
                            if (citAutServ != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    citAutServ.setIdSincronizador(null);
                                } else {
                                    citAutServ.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCitAutServ(citAutServ, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_turnos":
                            CitTurnos turno = turnosFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (turno != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    turno.setIdTurno(null);
                                } else {
                                    turno.setIdTurno(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarTurno(turno, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "cit_citas":
                            CitCitas cita = citasFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (cita != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    cita.setIdCita(null);
                                } else {
                                    cita.setIdCita(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCita(cita, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "hc_archivos":
                            HcArchivos ha = hcArchFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (ha != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    ha.setIdArchivo(null);
                                } else {
                                    ha.setIdArchivo((long) registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHistArch(ha, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "hc_items":
                            HcItems hi = hcItemsFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (hi != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    hi.setIdItem(null);
                                } else {
                                    hi.setIdItem(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHistItem(hi, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "hc_rep_examenes":
                            HcRepExamenes hcrep = hcRepFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (hcrep != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    hcrep.setIdRepExamenes(null);
                                } else {
                                    hcrep.setIdRepExamenes(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHistRep(hcrep, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "hc_detalle":
                            HcDetalle hdet = hcDetFacade.consultarHcDetalleSinc(registro.getSinStatusPK().getIdLocal());
                            if (hdet != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    hdet.setIdSincronizador(null);
                                } else {
                                    hdet.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHistDet(hdet, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "hc_registro":
                            HcRegistro h = registroFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (h != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    h.setIdRegistro(null);
                                } else {
                                    h.setIdRegistro(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarHistoria(h, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                                historiasSincronizadas++;
                            }
                            break;
                        case "fac_caja":
                            FacCaja f = cajaFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (f != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    f.setIdCaja(null);
                                } else {
                                    f.setIdCaja(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarCaja(f, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_consumo_insumo":
                            FacConsumoInsumo conIns = conInsFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (conIns != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    conIns.setIdConsumoInsumo(null);
                                } else {
                                    conIns.setIdConsumoInsumo(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarConIns(conIns, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_consumo_medicamento":
                            FacConsumoMedicamento conMed = conMedFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (conMed != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    conMed.setIdConsumoMedicamento(null);
                                } else {
                                    conMed.setIdConsumoMedicamento(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarConMed(conMed, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_consumo_paquete":
                            FacConsumoPaquete conPaq = conPaqFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (conPaq != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    conPaq.setIdConsumoPaquete(null);
                                } else {
                                    conPaq.setIdConsumoPaquete(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarConPaq(conPaq, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_consumo_servicio":
                            FacConsumoServicio conServ = conServFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (conServ != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    conServ.setIdConsumoServicio(null);
                                } else {
                                    conServ.setIdConsumoServicio(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarConSer(conServ, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_factura_paciente":
                            FacFacturaPaciente fac = facFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (fac != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    fac.setIdFacturaPaciente(null);
                                } else {
                                    fac.setIdFacturaPaciente(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFactura(fac, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                                facturasSincronizadas++;
                            }
                            break;
                        case "fac_factura_insumo":
                            FacFacturaInsumo facIns = facInsFacade.consultarFacFacturaInsumo(registro.getSinStatusPK().getIdLocal());
                            if (facIns != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    facIns.setIdSincronizador(null);
                                } else {
                                    facIns.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFacturaIns(facIns, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_factura_medicamento":
                            FacFacturaMedicamento facMed = facMedFacade.consultarFacFacturaMed(registro.getSinStatusPK().getIdLocal());
                            if (facMed != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    facMed.setIdSincronizador(null);
                                } else {
                                    facMed.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFacturaMed(facMed, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_factura_paquete":
                            FacFacturaPaquete facPaq = facPaqFacade.consultarFacFacturaPaq(registro.getSinStatusPK().getIdLocal());
                            if (facPaq != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    facPaq.setIdSincronizador(null);
                                } else {
                                    facPaq.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFacturaPaq(facPaq, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_factura_servicio":
                            FacFacturaServicio facSer = facSerFacade.consultarFacFacturaSer(registro.getSinStatusPK().getIdLocal());
                            if (facSer != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    facSer.setIdSincronizador(null);
                                } else {
                                    facSer.setIdSincronizador(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFacturaSer(facSer, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                        case "fac_periodo":
                            FacPeriodo facPer = facPerFacade.find(registro.getSinStatusPK().getIdLocal());
                            if (facPer != null) {
                                if (registroRemoto == null) {// no existe se debe insertar
                                    facPer.setIdPeriodo(null);
                                } else {
                                    facPer.setIdPeriodo(registroRemoto.getSinStatusPK().getIdLocal());
                                }
                                id = sincronizador.guardarFacturaPer(facPer, idTabla, idNodo, registro.getSinStatusPK().getIdLocal());
                                result = updateSinStatus(id, registro);
                            }
                            break;
                    }
                    /*if (result) {
                        transaction.commit();
                    } else {
                        transaction.rollback();
                    }*/
                    dblProgreso = (double) (dblProgreso + salto);
                    progress = (int) dblProgreso;
                }
                progress = 100;
                progreso = "sincronización Finalizada";
                pacientes = "Total Pacientes sincronizados " + pacientesSincronizados;
                historias = "Total Historias sincronizadas " + historiasSincronizadas;
                facturas = "Total Facturas sincronizadas " + facturasSincronizadas;
                totalRegistros = "Total Registros sincronizados " + pendientes.size();
            } catch (Exception ex) {
                try {
                    //transaction.rollback();
                    Logger.getLogger(PushMB.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex1) {
                    Logger.getLogger(PushMB.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        } else {
            imprimirMensaje("Sin acceso", "No posee acceso a internet", FacesMessage.SEVERITY_WARN);
        }
    }

    public boolean updateSinStatus(int idReg, SinStatus registro) {
        boolean result = false;
        try {
            if (idReg > 0) {
                result = true;
                registro.setStatus(false);
                registro.setIdRemoto(idReg);
                sinStatus.edit(registro);
            }
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

    public String getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(String totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
}
