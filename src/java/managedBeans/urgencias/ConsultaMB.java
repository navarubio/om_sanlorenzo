/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.urgencias;

import beans.ViewModels.ReporteFormulaMedicaViewModel;
import beans.ViewModels.ReporteNotaEnfermerasViewModel;
import beans.ViewModels.ReporteRipsViewModel;
import beans.ViewModels.ReporteTriageViewModel;
import beans.enumeradores.EstadoAdmisionPaciente;
import beans.enumeradores.EstadoFisicoPaciente;
import beans.enumeradores.IndicacionesMedicamentos;
import beans.enumeradores.NivelTriage;
import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgCama;
import modelo.entidades.CfgHabitacion;
import modelo.entidades.CfgInsumo;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.FacConsumoInsumo;
import modelo.entidades.FacConsumoMedicamento;
import modelo.entidades.FacConsumoPaquete;
import modelo.entidades.FacConsumoServicio;
import modelo.entidades.FacContrato;
import modelo.entidades.FacManualTarifario;
import modelo.entidades.FacManualTarifarioInsumo;
import modelo.entidades.FacManualTarifarioMedicamento;
import modelo.entidades.FacManualTarifarioPaquete;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.entidades.FacPaquete;
import modelo.entidades.FacServicio;
import modelo.entidades.UrgConsultaPacienteUrgencia;
import modelo.entidades.UrgControlPrescripcionMedicamento;
import modelo.entidades.UrgDetalleConsulta;
import modelo.entidades.UrgNotasEnfermerias;
import modelo.entidades.UrgNotasMedicas;
import modelo.entidades.UrgOrdenCobro;
import modelo.entidades.UrgPlanManejoPaciente;
import modelo.entidades.UrgPrescripcionMedicamento;
import modelo.entidades.UrgTriage;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.UrgAmisionFacade;
import modelo.fachadas.UrgConsultaFacade;
import modelo.fachadas.UrgControlySuministroMedicamentosFacade;
import modelo.fachadas.UrgDetalleConsultaFacade;
import modelo.fachadas.UrgPlanyManejoFacade;
import modelo.fachadas.UrgPrescripcionMedicamentoFacade;
import modelo.fachadas.UrgTriageFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Enderson
 */
@ManagedBean(name = "consultaMB")
@SessionScoped
public class ConsultaMB extends MetodosGenerales implements Serializable {

    private Integer activeTabIndex;
    private List<UrgTriage> listaUrgTriagePaciente;
    private List<UrgTriage> listaUrgTriagePacienteDadoAlta;
    private CfgUsuarios usuarioLogueado;
    private Date fecha;
    private Date hora;
    private CfgPacientes pacienteSeleccionado;
    private UrgTriage urgTriage;
    private java.util.Set<NivelTriage> nivelTriage;
    private String immpresionDiagnostica;
    private List<FacServicio> listaServicios;
    private java.util.Set<EstadoFisicoPaciente> estadoFisicoPaciente;
    private String diagnostico;
    private String destinoPaciente;
    private String idMedicamentoManual;
    private List<FacManualTarifarioMedicamento> listaMedicamentosManual;
    private UrgPrescripcionMedicamento urgPrescripcionMedicamento;
    private java.util.Set<IndicacionesMedicamentos> indicacionesMedicamentos;
    private FacConsumoMedicamento facConsumoMedicamento;
    private List<UrgPrescripcionMedicamento> listaUrgPrescripcionMedicamento;
    private FacManualTarifario manualTarifarioPaciente;
    private List<CfgMedicamento> listaCfgMedicamento;
    private FacConsumoServicio facConsumoServicio;
    private List<FacServicio> listaServiciosOrden;
    private UrgOrdenCobro urgOrdenCobro;
    private FacConsumoInsumo facConsumoInsumo;
    private List<CfgInsumo> listaInsumosOrden;
    private FacConsumoPaquete facConsumoPaquete;
    private List<FacPaquete> listaFacPaqueteOrden;
    private UrgControlPrescripcionMedicamento urgControlPrescripcionMedicamento;
    private UrgNotasMedicas urgNotasMedicas;
    private UrgNotasEnfermerias urgNotasEnfermerias;
    private String destinoFinalPaciente;
    private Double imc;
    private Integer cantidad;
    private CfgCama camaDisponible;
    private Boolean camaNoDisponible;
    private String urlFoto;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    UrgAmisionFacade urgAmisionFacade;

    @EJB
    UrgTriageFacade urgTriageFacade;

    @EJB
    UrgConsultaFacade urgConsultaFacade;

    @EJB
    FacServicioFacade facServicioFacade;

    @EJB
    UrgDetalleConsultaFacade urgDetalleConsultaFacade;

    @EJB
    CfgDiagnosticoFacade cfgDiagnosticoFacade;

    @EJB
    CfgMedicamentoFacade cfgMedicamentoFacade;

    @EJB
    UrgControlySuministroMedicamentosFacade urgControlySuministroMedicamentosFacade;

    @EJB
    UrgPrescripcionMedicamentoFacade urgPrescripcionMedicamentoFacade;

    @EJB
    UrgPlanyManejoFacade urgPlanyManejoFacade;

    LoginMB loginMB;

    @PostConstruct
    public void inicializar() {
        this.inicializarFormulario();
    }

    public void inicializarFormulario() {
        this.setFecha(new Date());
        this.setHora(new Date());
        this.setActiveTabIndex(0);
        this.getListaUrgTriagePaciente().clear();
        this.getListaUrgTriagePaciente().addAll(this.getUrgTriageFacade().findAllPacientesByTriage(EstadoAdmisionPaciente.ADMISION_PACIENTE_EN_CONSULTA_DOCTOR));
        this.getListaUrgTriagePacienteDadoAlta().clear();
        this.getListaUrgTriagePacienteDadoAlta().addAll(this.getUrgTriageFacade().findAllHistorialUrgencias());
        loginMB = (LoginMB) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        this.estadoFisicoPaciente = EnumSet.of(EstadoFisicoPaciente.NORMAL, EstadoFisicoPaciente.ANORMAL, EstadoFisicoPaciente.NO_SE_EXPLORA);
        this.getListaServicios().addAll(this.getFacServicioFacade().buscarActivos());
        this.indicacionesMedicamentos = EnumSet.of(IndicacionesMedicamentos.AHORA, IndicacionesMedicamentos.CADA_HORAI, IndicacionesMedicamentos.CADA_HORAII, IndicacionesMedicamentos.CADA_HORAIII,
                IndicacionesMedicamentos.CADA_HORAIV, IndicacionesMedicamentos.CADA_HORAV, IndicacionesMedicamentos.CADA_HORAVI, IndicacionesMedicamentos.CADA_HORAVII,
                IndicacionesMedicamentos.CADA_HORAVIII, IndicacionesMedicamentos.CADA_HORAIX, IndicacionesMedicamentos.CADA_HORAX, IndicacionesMedicamentos.CADA_HORAXI,
                IndicacionesMedicamentos.CADA_HORAXII, IndicacionesMedicamentos.CADA_HORAVIII, IndicacionesMedicamentos.CADA_HORAXIV, IndicacionesMedicamentos.CADA_HORAXV,
                IndicacionesMedicamentos.CADA_HORAXVI, IndicacionesMedicamentos.CADA_HORAXXII, IndicacionesMedicamentos.CADA_HORAXIII, IndicacionesMedicamentos.CADA_HORAXIX,
                IndicacionesMedicamentos.CADA_HORAXX, IndicacionesMedicamentos.CADA_HORAXXI, IndicacionesMedicamentos.CADA_HORAXXII, IndicacionesMedicamentos.CADA_HORAXXIII, IndicacionesMedicamentos.CADA_HORAXXIV);
        this.setCamaDisponible(new CfgCama());
    }

    public List<String> autocompletarDiagnostico(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return cfgDiagnosticoFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    public void guardarPrescripciones() {
        Boolean nuevaPrescripcion = Boolean.FALSE;
        for (UrgPrescripcionMedicamento prescripcion : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist()) {
            if (prescripcion.getIdPrescripcion() == null) {
                nuevaPrescripcion = Boolean.TRUE;
                break;
            }
        }
        if (nuevaPrescripcion.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "Debe hacer clic en el boton NUEVA y completar los datos de la prescripción antes de hacer clic en guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getFecha() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado la prescripción del paciente", FacesMessage.SEVERITY_INFO);

    }

    public void guardarPrescripcionesAmbulatorio() {
        Boolean nuevaPrescripcion = Boolean.FALSE;
        Double valorInicial = 0.0;
        Double valorFinal = 0.0;
        for (UrgPrescripcionMedicamento prescripcion : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist()) {
            if (prescripcion.getIdPrescripcion() == null) {
                nuevaPrescripcion = Boolean.TRUE;
                break;
            }
        }
        if (nuevaPrescripcion.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "Debe hacer clic en el boton NUEVA y completar los datos de la prescripción antes de hacer clic en guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgOrdenCobro(new UrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFecha(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setHora(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getFacConsumoServicio().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoMedicamentolist(new ArrayList<FacConsumoMedicamento>());
        } else {
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoMedicamentolist() == null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoMedicamentolist(new ArrayList<FacConsumoMedicamento>());
            }
        }
        Iterator<UrgPrescripcionMedicamento> listaPrescripcionesAmbulatoria = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist().iterator();
        while (listaPrescripcionesAmbulatoria.hasNext()) {
            UrgPrescripcionMedicamento prescripcion = listaPrescripcionesAmbulatoria.next();
            System.out.println("managedBeans.urgencias.ConsultaMB.guardarPrescripcionesAmbulatorio()" + prescripcion.getIdPrescripcion());
            if (prescripcion.getIdPrescripcion() == null) {
                this.setFacConsumoMedicamento(new FacConsumoMedicamento());
                this.getFacConsumoMedicamento().setCantidad(prescripcion.getCantidad());
                this.getFacConsumoMedicamento().setFecha(new Date());
                this.getFacConsumoMedicamento().setIdPrestador(loginMB.getUsuarioActual());
                this.getFacConsumoMedicamento().setIdPaciente(this.getPacienteSeleccionado());
                this.getFacConsumoMedicamento().setIdMedicamento(prescripcion.getIdMedicamento());
                this.getFacConsumoMedicamento().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
                valorInicial = 0.0;
                valorFinal = 0.0;
                for (FacManualTarifarioMedicamento medicamento : this.getManualTarifarioPaciente().getFacManualTarifarioMedicamentoList()) {
                    if (prescripcion.getIdMedicamento().getIdMedicamento().equals(medicamento.getCfgMedicamento().getIdMedicamento())) {
                        valorInicial = medicamento.getValorInicial();
                        break;
                    }
                }
                valorFinal = valorInicial * prescripcion.getCantidad();
                this.getFacConsumoMedicamento().setValorUnitario(valorInicial);
                this.getFacConsumoMedicamento().setValorFinal(valorFinal);
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoMedicamentolist().add(this.getFacConsumoMedicamento());
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
                this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
                this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
            }
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }

        imprimirMensaje("Información", "Se ha guardado la prescripción del paciente", FacesMessage.SEVERITY_INFO);

    }

    public void cerrarHistoriaPacienteDestinoAmbulatorio() {
        this.getUrgTriage().getIdAdmision().setEstado(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_DADO_ALTA);
        this.getUrgTriage().getIdAdmision().setHoraSalidaUrgencia(new Date());
        this.getUrgTriage().getIdAdmision().setFechaSalidaUrgencia(new Date());
        this.getUrgAmisionFacade().edit(this.getUrgTriage().getIdAdmision());
        this.setPacienteSeleccionado(new CfgPacientes());
        this.getListaUrgTriagePaciente().clear();
        this.getListaUrgTriagePaciente().addAll(this.getUrgTriageFacade().findAllPacientesByTriage(EstadoAdmisionPaciente.ADMISION_PACIENTE_EN_CONSULTA_DOCTOR));
        this.getListaUrgTriagePacienteDadoAlta().clear();
        this.getListaUrgTriagePacienteDadoAlta().addAll(this.getUrgTriageFacade().findAllHistorialUrgencias());
        imprimirMensaje("Información", "Se ha cerrado la historia clinica del paciente y concluye el proceso de urgencia", FacesMessage.SEVERITY_INFO);
        RequestContext.getCurrentInstance().execute("PF('dialogoRips').show()");

    }

    public void cerrarHistoriaPaciente() {
        Double valor = 0.0;
        Double valorFinal = 0.0;
        this.getUrgTriage().getIdAdmision().setHoraSalidaUrgencia(new Date());
        this.getUrgTriage().getIdAdmision().setFechaSalidaUrgencia(new Date());
        this.getUrgTriage().getIdAdmision().setEstado(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_DADO_ALTA);
        this.getUrgAmisionFacade().edit(this.getUrgTriage().getIdAdmision());
        Iterator<UrgPrescripcionMedicamento> listaPrescripcionesAmbulatoria = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist().iterator();
        while (listaPrescripcionesAmbulatoria.hasNext()) {
            UrgPrescripcionMedicamento prescripcion = listaPrescripcionesAmbulatoria.next();
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getCfgCama() != null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getCfgCama().setOcupado(false);
            }
            if (prescripcion.getCantidadSuministrada() > 0) {
                this.setFacConsumoMedicamento(new FacConsumoMedicamento());
                this.getFacConsumoMedicamento().setCantidad(prescripcion.getCantidadSuministrada());
                this.getFacConsumoMedicamento().setFecha(new Date());
                this.getFacConsumoMedicamento().setIdPrestador(prescripcion.getIdPrestador());
                this.getFacConsumoMedicamento().setIdPaciente(this.getPacienteSeleccionado());
                this.getFacConsumoMedicamento().setIdMedicamento(prescripcion.getIdMedicamento());
                this.getFacConsumoMedicamento().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
                valor = 0.0;
                valorFinal = 0.0;
                for (FacManualTarifarioMedicamento medicamento : this.getManualTarifarioPaciente().getFacManualTarifarioMedicamentoList()) {
                    if (prescripcion.getIdMedicamento().getIdMedicamento().equals(medicamento.getCfgMedicamento().getIdMedicamento())) {
                        valor = medicamento.getValorInicial();
                        break;
                    }
                }
                valorFinal = valor * prescripcion.getCantidadSuministrada();
                this.getFacConsumoMedicamento().setValorUnitario(valor);
                this.getFacConsumoMedicamento().setValorFinal(valorFinal);
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoMedicamentolist().add(this.getFacConsumoMedicamento());
            }
            this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());

        }
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        this.setPacienteSeleccionado(new CfgPacientes());
        this.getListaUrgTriagePaciente().clear();
        this.getListaUrgTriagePaciente().addAll(this.getUrgTriageFacade().findAllPacientesByTriage(EstadoAdmisionPaciente.ADMISION_PACIENTE_EN_CONSULTA_DOCTOR));
        this.getListaUrgTriagePacienteDadoAlta().clear();
        this.getListaUrgTriagePacienteDadoAlta().addAll(this.getUrgTriageFacade().findAllHistorialUrgencias());
        imprimirMensaje("Información", "Se ha cerrado la historia clinica del paciente y concluye el proceso de urgencia", FacesMessage.SEVERITY_INFO);
        RequestContext.getCurrentInstance().execute("PF('dialogoRips').show()");

    }

    public void guardarServicios() {
        Boolean nuevoServicio = Boolean.FALSE;
        for (FacConsumoServicio servicio : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist()) {
            if (servicio.getIdConsumoServicio() == null) {
                nuevoServicio = Boolean.TRUE;
                break;
            }
        }
        if (nuevoServicio.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "Debe hacer clic en el boton NUEVO y completar los datos del servicio solicitado antes de hacer clic en guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getFacConsumoServicio().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado los servicios asociados al paciente", FacesMessage.SEVERITY_INFO);

    }

    public void guardarInsumos() {
        Boolean nuevoInsumo = Boolean.FALSE;
        for (FacConsumoInsumo insumo : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoInsumolist()) {
            if (insumo.getIdConsumoInsumo() == null) {
                nuevoInsumo = Boolean.TRUE;
                break;
            }
        }
        if (nuevoInsumo.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "Debe hacer clic en el boton NUEVO y completar los datos del insumo solicitado antes de hacer clic en guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getFacConsumoInsumo().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado los insumos asociados al paciente", FacesMessage.SEVERITY_INFO);

    }

    public void guardarPaquetes() {
        Boolean nuevoPaquete = Boolean.FALSE;
        for (FacConsumoPaquete paquete : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoPaquetelist()) {
            if (paquete.getIdConsumoPaquete() == null) {
                nuevoPaquete = Boolean.TRUE;
                break;
            }
        }
        if (nuevoPaquete.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "Debe hacer clic en el boton NUEVO y completar los datos del paquete solicitado antes de hacer clic en guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getFacConsumoPaquete().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado los paquetes asociados al paciente", FacesMessage.SEVERITY_INFO);

    }

    public void cargarDialogoMedicamentos() {
        this.setUrgPrescripcionMedicamento(new UrgPrescripcionMedicamento());
        this.getUrgPrescripcionMedicamento().setCantidad(1);
        this.getUrgPrescripcionMedicamento().setCantidadSuministrada(0);
        this.getUrgPrescripcionMedicamento().setDosis(1);
        this.getUrgPrescripcionMedicamento().setFecha(new Date());
        this.getUrgPrescripcionMedicamento().setHora(new Date());
        this.getUrgPrescripcionMedicamento().setIdPrestador(loginMB.getUsuarioActual());
        this.getUrgPrescripcionMedicamento().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.getUrgPrescripcionMedicamento().setSuministrado(false);
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente().getId() == 1831) {
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarPrescripcionMedicamento').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarMedicamento').show()");
        }
    }
//falta cargar servicios los valores en cconsumo e insumos

    public void cargarDialogoServicios() {
        this.setFacConsumoServicio(new FacConsumoServicio());
        this.getFacConsumoServicio().setCantidad(1);
        this.getFacConsumoServicio().setFecha(new Date());
        this.getFacConsumoServicio().setEstado(false);
        this.getFacConsumoServicio().setIdPrestador(loginMB.getUsuarioActual());
        this.getFacConsumoServicio().setIdPaciente(this.getPacienteSeleccionado());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgOrdenCobro(new UrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFecha(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setHora(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getFacConsumoServicio().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoServiciolist(new ArrayList<FacConsumoServicio>());
        } else {
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist() == null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoServiciolist(new ArrayList<FacConsumoServicio>());
            }
        }
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarServicios').show()");

    }

    public void cargarDialogoInsumos() {
        this.setFacConsumoInsumo(new FacConsumoInsumo());
        this.getFacConsumoInsumo().setCantidad(1);
        this.getFacConsumoInsumo().setFecha(new Date());
        this.getFacConsumoInsumo().setIdPrestador(loginMB.getUsuarioActual());
        this.getFacConsumoInsumo().setIdPaciente(this.getPacienteSeleccionado());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgOrdenCobro(new UrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFecha(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setHora(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getFacConsumoInsumo().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoInsumolist(new ArrayList<FacConsumoInsumo>());
        } else {
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoInsumolist() == null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoInsumolist(new ArrayList<FacConsumoInsumo>());
            }
        }
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarInsumos').show()");
    }

    public void cargarDialogoPaquetes() {
        this.setFacConsumoPaquete(new FacConsumoPaquete());
        this.getFacConsumoPaquete().setCantidad(1);
        this.getFacConsumoPaquete().setFecha(new Date());
        this.getFacConsumoPaquete().setIdPrestador(loginMB.getUsuarioActual());
        this.getFacConsumoPaquete().setIdPaciente(this.getPacienteSeleccionado());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgOrdenCobro(new UrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFecha(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setHora(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoPaquetelist(new ArrayList<FacConsumoPaquete>());
            this.getFacConsumoPaquete().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
        } else {
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoPaquetelist() == null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoPaquetelist(new ArrayList<FacConsumoPaquete>());
            }
        }
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarPaquetes').show()");
    }

    public void capturarDiagnostico(SelectEvent event) {
        System.out.println("managedBeans.urgencias.cxcxc.capturarDiagnostico()" + this.getDiagnostico());
    }

    public void iniciarConsulta(UrgTriage admision) {
        String fotoPorDefecto = "../recursos/img/img_usuario.png";
        this.setPacienteSeleccionado(admision.getIdAdmision().getIdPaciente());
        this.setUrgTriage(admision);
        if (this.getPacienteSeleccionado().getFoto() != null) {
            urlFoto = "../imagenesOpenmedical/" + this.getPacienteSeleccionado().getFoto().getUrlImagen();
        } else {
            urlFoto = fotoPorDefecto;
        }

        this.immpresionDiagnostica = admision.getImpresionDiagnostica().getDescripcion();
        if (this.getPacienteSeleccionado().getIdAdministradora() != null) {
            if (!this.getPacienteSeleccionado().getIdAdministradora().getFacContratoList().isEmpty()) {
                this.getListaCfgMedicamento().clear();
                this.getListaServiciosOrden().clear();
                this.getListaInsumosOrden().clear();
                FacContrato contrato = this.getPacienteSeleccionado().getIdAdministradora().getFacContratoList().get(0);
                this.setManualTarifarioPaciente(contrato.getIdManualTarifario());
                this.getListaMedicamentosManual().addAll(this.getManualTarifarioPaciente().getFacManualTarifarioMedicamentoList());
                for (FacManualTarifarioMedicamento medicamento : this.getListaMedicamentosManual()) {
                    this.getListaCfgMedicamento().add(medicamento.getCfgMedicamento());
                }
                for (FacManualTarifarioServicio servicio : this.getManualTarifarioPaciente().getFacManualTarifarioServicioList()) {
                    this.getListaServiciosOrden().add(servicio.getFacServicio());
                }
                for (FacManualTarifarioInsumo insumo : this.getManualTarifarioPaciente().getFacManualTarifarioInsumoList()) {
                    this.getListaInsumosOrden().add(insumo.getCfgInsumo());
                }
                for (FacManualTarifarioPaquete paquete : this.getManualTarifarioPaciente().getFacManualTarifarioPaqueteList()) {
                    this.getListaFacPaqueteOrden().add(paquete.getFacPaquete());
                }
            }
        }
        this.setUrgNotasEnfermerias(new UrgNotasEnfermerias());
        this.setUrgNotasMedicas(new UrgNotasMedicas());
        this.setFacConsumoServicio(new FacConsumoServicio());
        this.setFacConsumoInsumo(new FacConsumoInsumo());
        this.setFacConsumoPaquete(new FacConsumoPaquete());
        this.setUrgControlPrescripcionMedicamento(new UrgControlPrescripcionMedicamento());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia() == null) {
            System.out.println("xx1");
            this.getUrgTriage().setIdUrgConsultaPacienteUrgencia(new UrgConsultaPacienteUrgencia());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setAtendida(false);
            this.setUrgPrescripcionMedicamento(new UrgPrescripcionMedicamento());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgDetalleConsulta(new UrgDetalleConsulta());
        } else {
            System.out.println("xx2");
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta() == null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgDetalleConsulta(new UrgDetalleConsulta());
                this.setUrgPrescripcionMedicamento(new UrgPrescripcionMedicamento());
            } else {
                if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
                    this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
                }
                this.setUrgPrescripcionMedicamento(new UrgPrescripcionMedicamento());
                this.setDiagnostico(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getIdCfgDiagnostico());
                if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente() == null) {
                } else {
                    this.setDestinoPaciente(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente().getId().toString());
                }
                if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
                    this.setDestinoFinalPaciente("");
                } else {
                    this.setDestinoFinalPaciente(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getAmbitoDestinoPaciente().getId().toString());
                }
            }
        }
        imprimirMensaje("Información", "Se ha cargado la información del paciente seleccionado", FacesMessage.SEVERITY_INFO);
    }

    public void cancelarAnamnesis() {
        this.getUrgTriage().setIdUrgConsultaPacienteUrgencia(new UrgConsultaPacienteUrgencia());
    }

    public void guardarConsulta() {
        if (this.validarAnamnesis().equals(Boolean.FALSE)) {
            return;
        } else {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgDetalleConsulta(null);
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdPaciente(this.getPacienteSeleccionado());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setFechaIniConsulta(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setHoraIniConsulta(this.getHora());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgTriage(this.getUrgTriage());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdPrestador(loginMB.getUsuarioActual());
            this.getUrgTriageFacade().edit(this.getUrgTriage());
            this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
            this.setFacConsumoServicio(new FacConsumoServicio());
            this.getFacConsumoServicio().setCantidad(1);
            this.getFacConsumoServicio().setFecha(new Date());
            this.getFacConsumoServicio().setEstado(false);
            this.getFacConsumoServicio().setDiagnosticoPrincipal(this.getUrgTriage().getHallazgosClinico());
            this.getFacConsumoServicio().setIdPrestador(loginMB.getUsuarioActual());
            this.getFacConsumoServicio().setIdPaciente(this.getPacienteSeleccionado());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgOrdenCobro(new UrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFecha(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setHora(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getFacConsumoServicio().setUrgOrdenCobro(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoServiciolist(new ArrayList<FacConsumoServicio>());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().setFacConsumoServiciolist(new ArrayList<FacConsumoServicio>());
            this.getFacConsumoServicio().setIdServicio(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdServicio());
            String tarifa = "";
            Double valor = 0.0;
            for (FacManualTarifarioServicio servicio : this.getManualTarifarioPaciente().getFacManualTarifarioServicioList()) {
                if (servicio.getFacServicio().getIdServicio().equals(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdServicio().getIdServicio())) {
                    tarifa = servicio.getTipoTarifa();
                    valor = servicio.getValorInicial();
                    break;
                }
            }
            this.getFacConsumoServicio().setTipoTarifa(tarifa);
            this.getFacConsumoServicio().setValorUnitario(valor);
            this.getFacConsumoServicio().setValorFinal(valor);
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist().add(this.getFacConsumoServicio());
            this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgDetalleConsulta(new UrgDetalleConsulta());
            imprimirMensaje("Información", "Se ha guardado la ananmesis de la consulta del paciente ", FacesMessage.SEVERITY_INFO);

        }
    }

    public void determinarimc() {
        Double imc = 0.0;
        Double tallaMetros = 0.0;
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPeso() != null
                && this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getTalla() != null) {
            tallaMetros = (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getTalla() / 100);
            imc = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPeso()
                    / ((tallaMetros * tallaMetros));

        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setMc(imc);
        this.setImc(imc);
    }

    public void borrarPrescripcion(UrgPrescripcionMedicamento prescripcion) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist().remove(prescripcion);
        imprimirMensaje("Información", "Prescripción borrada con exito", FacesMessage.SEVERITY_INFO);
    }

    public void borrarServicio(FacConsumoServicio servicio) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist().remove(servicio);
        imprimirMensaje("Información", "Servicio de consumo borrado con exito", FacesMessage.SEVERITY_INFO);
    }

    public void borrarInsumo(FacConsumoInsumo insumo) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoInsumolist().remove(insumo);
        imprimirMensaje("Información", "Insumo de consumo borrado con exito", FacesMessage.SEVERITY_INFO);
    }

    public void borrarPaquete(FacConsumoPaquete paquete) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoPaquetelist().remove(paquete);
        imprimirMensaje("Información", "Paquete de consumo borrado con exito", FacesMessage.SEVERITY_INFO);
    }

    public Boolean validarExamenFisico() {
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getRevision().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar el contenido de la revisión del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPeso() == null
                || this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPeso() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar el peso del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getTalla() == null
                || this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getTalla() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la talla del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getFrecuenciaCardiaca() == null
                || this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getFrecuenciaCardiaca() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la frecuencia cardiaca del paciente valores validos entre 0 - 201", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getFrecuencia_respiratoria() == null
                || this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getFrecuencia_respiratoria() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la frecuencia respiratoria del paciente valores validos entre 0 - 81", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPresionArteriaMinima() == null) {
            imprimirMensaje("Error", "Debe ingresar los valores de presión arterial minima ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getPresionArteriaMaxima() == null) {
            imprimirMensaje("Error", "Debe ingresar los valores de presión arterial maxima ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getTemperatura() == null) {
            imprimirMensaje("Error", "Debe ingresar la temperatura del paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getSaturacion() == null) {
            imprimirMensaje("Error", "Debe ingresar la saturación del paciente valores valido de 0 a 100 ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getHallazgosClinico().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar los hallazgos clinicos encontrados en el paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean validarDiagnostico() {
        if (this.getDiagnostico() == null) {
            imprimirMensaje("Error", "Debe seleccionar el diagnostico determinado en el paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getObservacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una observación asociada al diagnostico del paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public Boolean validarPlanyManejoConsultaPaciente() {
        if (this.getDestinoPaciente() == null || this.getDestinoPaciente().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar el destino del paciente finalizado la fase de chequeo y consulta", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getRecomendacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una recomendación asociada al diagnostico del paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public void volver() {
        this.inicializar();
        this.setPacienteSeleccionado(new CfgPacientes());
    }

    public void cancelarExamenFisico() {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setIdUrgDetalleConsulta(new UrgDetalleConsulta());
    }

    public void guardarExamenFisico() {
        if (this.validarExamenFisico()) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setIdPrestador(loginMB.getUsuarioActual());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
            imprimirMensaje("Información", "Se ha guardado la información del examen fisico del paciente", FacesMessage.SEVERITY_INFO);
        }
    }

    public void guardarDiagnostico() {
        if (this.validarDiagnostico()) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setIdCfgDiagnostico(this.getDiagnostico());
            this.getUrgDetalleConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta());
            imprimirMensaje("Información", "Se ha guardado el diagnostico del paciente", FacesMessage.SEVERITY_INFO);
        }
    }

    public void cancelarDiagnostico() {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setObservacion("");
    }

    public void cancelarPlanyManejo() {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setRecomendacion("");
    }

    public void guardarPlanYManejo() {
        if (this.validarPlanyManejoConsultaPaciente()) {

            if (this.getDestinoPaciente() != null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setAmbitoDestinoPaciente(this.getClasificacionesFachada().find(Integer.parseInt(this.getDestinoPaciente())));
            }
            if (this.getDestinoPaciente().equals("1833")) {
                this.asignarCama();
                if (this.getCamaNoDisponible().equals(Boolean.TRUE)) {
                    RequestContext.getCurrentInstance().execute("PF('dialogoAsignarCama').show()");
                    this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setCfgCama(this.getCamaDisponible());
                    this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getCfgCama().setOcupado(true);
                    this.getUrgTriage().getIdAdmision().setEstadoAdmisionPaciente(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_OBSERVACION);
                } else {
                    RequestContext.getCurrentInstance().execute("PF('dialogoNoDisponible').show()");
                    return;
                }
            }
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setIdRegistroAuditoriaProfesional(loginMB.getUsuarioActual());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setAtendida(Boolean.TRUE);
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setHoraFinConsulta(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setFechaFinConsulta(new Date());
            this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getUrgDetalleConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta());
            imprimirMensaje("Información", "Se ha guardado el plan de manejo y recomendaciones del paciente y se ha concluido la consulta", FacesMessage.SEVERITY_INFO);
            this.urgAmisionFacade.edit(this.getUrgTriage().getIdAdmision());
            this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
    }

    public void asignarCama() {
        this.setCamaDisponible(new CfgCama());
        this.setCamaNoDisponible(false);
        Iterator<CfgHabitacion> listaHabitacion = loginMB.getCentroDeAtencionactual().getCfgHabitacionList().iterator();
        while (listaHabitacion.hasNext()) {
            CfgHabitacion habitacion = listaHabitacion.next();
            if (habitacion.getIsHabitacionUrgencia().equals(Boolean.TRUE)) {
                Iterator<CfgCama> listaCama = habitacion.getCfgCamaList().iterator();
                while (listaCama.hasNext()) {
                    CfgCama cama = listaCama.next();
                    if (cama.getOcupado().equals(Boolean.FALSE)) {
                        this.setCamaDisponible(cama);
                        this.setCamaNoDisponible(true);
                        return;
                    }
                }
            }
        }
    }

    public void agregarMedicamento() {
        if (this.getUrgPrescripcionMedicamento().getObservacion().isEmpty()) {
            imprimirMensaje("Información", "Debe ingresar una observación", FacesMessage.SEVERITY_INFO);
            return;
        }
        this.getUrgPrescripcionMedicamento().setFecha(new Date());
        this.getUrgPrescripcionMedicamento().setHora(new Date());
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist().add(this.getUrgPrescripcionMedicamento());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente().getId() == 1831) {
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarPrescripcionMedicamento').hide()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarMedicamento').hide()");
        }
        imprimirMensaje("Información", "Se ha agregado a la lista la prescripción del medicamento", FacesMessage.SEVERITY_INFO);
    }

    public Boolean validarAnamnesis() {
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdServicio() == null) {
            imprimirMensaje("Error", "Debe seleccionar un servicio", FacesMessage.SEVERITY_ERROR);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getMotivo().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar un motivo de la consulta del paciente", FacesMessage.SEVERITY_ERROR);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getEnfermedadActual().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar la enfermedad actual de paciente", FacesMessage.SEVERITY_ERROR);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public void agregarServicio() {
        if (this.getFacConsumoServicio().getDiagnosticoPrincipal().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar el diagnostico principal", FacesMessage.SEVERITY_ERROR);
            return;
        }
        for (FacManualTarifarioServicio servicio : this.getManualTarifarioPaciente().getFacManualTarifarioServicioList()) {
            if (servicio.getFacServicio().getIdServicio().equals(this.getFacConsumoServicio().getIdServicio().getIdServicio())) {
                this.getFacConsumoServicio().setTipoTarifa(servicio.getTipoTarifa());
                this.getFacConsumoServicio().setValorUnitario(servicio.getValorInicial());
                this.getFacConsumoServicio().setValorFinal(this.getFacConsumoServicio().getCantidad() * servicio.getValorInicial());
                break;
            }
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist().add(this.getFacConsumoServicio());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarServicios').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista el servicio", FacesMessage.SEVERITY_INFO);

    }

    public void guardarPlanManejoSinCamaDisponible() {
        if (this.validarPlanyManejoConsultaPaciente()) {

            if (this.getDestinoPaciente() != null) {
                this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setAmbitoDestinoPaciente(this.getClasificacionesFachada().find(Integer.parseInt(this.getDestinoPaciente())));
            }
            this.getUrgTriage().getIdAdmision().setEstadoAdmisionPaciente(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_OBSERVACION);
            RequestContext.getCurrentInstance().execute("PF('dialogoNoDisponible').hide()");
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().setIdRegistroAuditoriaProfesional(loginMB.getUsuarioActual());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setAtendida(Boolean.TRUE);
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setHoraFinConsulta(new Date());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setFechaFinConsulta(new Date());
            this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
            this.getUrgDetalleConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta());
            imprimirMensaje("Información", "Se ha guardado el plan de manejo y recomendaciones del paciente y se ha concluido la consulta", FacesMessage.SEVERITY_INFO);
            this.urgAmisionFacade.edit(this.getUrgTriage().getIdAdmision());
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
    }

    public void agregarInsumo() {
        if (this.getFacConsumoInsumo().getCantidad() == 0) {
            imprimirMensaje("Error", "Debe ingresar la cantidad solicitada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        for (FacManualTarifarioInsumo insumo : this.getManualTarifarioPaciente().getFacManualTarifarioInsumoList()) {
            if (insumo.getCfgInsumo().getIdInsumo().equals(this.getFacConsumoInsumo().getIdInsumo().getIdInsumo())) {
                this.getFacConsumoInsumo().setValorUnitario(insumo.getValorInicial());
                this.getFacConsumoInsumo().setValorFinal(this.getFacConsumoInsumo().getCantidad() * insumo.getValorInicial());
                break;
            }
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoInsumolist().add(this.getFacConsumoInsumo());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarInsumos').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista el insumo", FacesMessage.SEVERITY_INFO);

    }

    public void agregarPaquete() {
        if (this.getFacConsumoPaquete().getCantidad() == 0) {
            imprimirMensaje("Error", "Debe ingresar la cantidad solicitada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        for (FacManualTarifarioPaquete paquete : this.getManualTarifarioPaciente().getFacManualTarifarioPaqueteList()) {
            if (paquete.getFacPaquete().getIdPaquete().equals(this.getFacConsumoPaquete().getIdPaquete().getIdPaquete())) {
                this.getFacConsumoPaquete().setValorUnitario(paquete.getValorInicial());
                this.getFacConsumoPaquete().setValorFinal(this.getFacConsumoPaquete().getCantidad() * paquete.getValorInicial());
                break;
            }
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoPaquetelist().add(this.getFacConsumoPaquete());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarPaquetes').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista el paquete", FacesMessage.SEVERITY_INFO);

    }

    public void cargarDialogoSuministroMedicamento(UrgPrescripcionMedicamento prescripcion) {
        //this.setUrgPrescripcionMedicamento(new UrgPrescripcionMedicamento());
        this.setUrgControlPrescripcionMedicamento(new UrgControlPrescripcionMedicamento());
        this.getUrgControlPrescripcionMedicamento().setFecha(new Date());
        this.getUrgControlPrescripcionMedicamento().setHora(new Date());
        this.getUrgControlPrescripcionMedicamento().setCantidadSuministrada(1);
        this.getUrgControlPrescripcionMedicamento().setIdPrestador(loginMB.getUsuarioActual());
        this.getUrgControlPrescripcionMedicamento().setUrgPrescripcionMedicamento(prescripcion);
        RequestContext.getCurrentInstance().execute("PF('IdDialogoSuministroMedicamento').show();");

    }

    public void agregarSuministroMedicamentoPaciente() {
        if (this.getUrgControlPrescripcionMedicamento().getCantidadSuministrada() == 0) {
            imprimirMensaje("Error", "Debe ingresar la cantidad suministrada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgControlPrescripcionMedicamento().getObservacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una observación", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.getUrgControlPrescripcionMedicamento().getUrgPrescripcionMedicamento().getUrgControlPrescripcionMedicamentoList().add(this.getUrgControlPrescripcionMedicamento());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoSuministroMedicamento').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista el suministro aplicado por el profesional", FacesMessage.SEVERITY_INFO);

    }

    public void borrarNotaEnFermeria(UrgNotasEnfermerias nota) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasEnfermeriaList().remove(nota);
        imprimirMensaje("Información", "Nota de enfermeria borrada con exito", FacesMessage.SEVERITY_INFO);
    }

    public void borrarNotaMedica(UrgNotasMedicas nota) {
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasMedicasList().remove(nota);
        imprimirMensaje("Información", "Nota medica borrada con exito", FacesMessage.SEVERITY_INFO);
    }

    public void guardarSuministroMedicamentoPaciente() {
        Boolean suministro = Boolean.FALSE;
        for (UrgPrescripcionMedicamento prescripcion : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist()) {
            Iterator<UrgControlPrescripcionMedicamento> listaControl = prescripcion.getUrgControlPrescripcionMedicamentoList().iterator();
            while (listaControl.hasNext()) {
                UrgControlPrescripcionMedicamento control = listaControl.next();
                if (control.getIdControlPrescripcion() == null) {
                    suministro = Boolean.TRUE;
                    break;
                }
            }
            if (suministro.equals(Boolean.TRUE)) {
                break;
            }
        }
        if (suministro.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "No existen nuevos registros de suministros para guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        int cantidadSuministrada = 0;
        //agregar el medicamento al fac consumo
        RequestContext.getCurrentInstance().execute("PF('IdDialogoSuministroMedicamento').hide();");
        cantidadSuministrada = this.getUrgControlPrescripcionMedicamento().getCantidadSuministrada() + this.getUrgControlPrescripcionMedicamento().getUrgPrescripcionMedicamento().getCantidadSuministrada();
        this.getUrgControlPrescripcionMedicamento().getUrgPrescripcionMedicamento().setCantidadSuministrada(cantidadSuministrada);
        this.getUrgControlySuministroMedicamentosFacade().create(this.getUrgControlPrescripcionMedicamento());
        this.getUrgPrescripcionMedicamentoFacade().edit(this.getUrgControlPrescripcionMedicamento().getUrgPrescripcionMedicamento());
        imprimirMensaje("Información", "Se ha guardado los suministro aplicado al paciente por el profesional", FacesMessage.SEVERITY_INFO);
    }

    public void cargarDialogoAgregarNotaMedicas() {
        this.setUrgNotasMedicas(new UrgNotasMedicas());
        this.getUrgNotasMedicas().setFecha(new Date());
        this.getUrgNotasMedicas().setHora(new Date());
        this.getUrgNotasMedicas().setIdPrestador(loginMB.getUsuarioActual());
        this.getUrgNotasMedicas().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoNotasMedicas').show();");
    }

    public void agregarNotaMedicas() {
        if (this.getUrgNotasMedicas().getNota().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una nota", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasMedicasList().add(this.getUrgNotasMedicas());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoNotasMedicas').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista la nota", FacesMessage.SEVERITY_INFO);
    }

    public void guardarNotaMedicas() {
        Boolean nota = Boolean.FALSE;
        Iterator<UrgNotasMedicas> listaNota = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasMedicasList().iterator();
        while (listaNota.hasNext()) {
            UrgNotasMedicas notaMedica = listaNota.next();
            if (notaMedica.getIdNota() == null) {
                nota = Boolean.TRUE;
                break;
            }
        }
        if (nota.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "No existen nuevos registros de nota para guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado la(s) nota del medico", FacesMessage.SEVERITY_INFO);
    }

    public void cargarDialogoAgregarNotaEnfermeria() {
        this.setUrgNotasEnfermerias(new UrgNotasEnfermerias());
        this.getUrgNotasEnfermerias().setFecha(new Date());
        this.getUrgNotasEnfermerias().setHora(new Date());
        this.getUrgNotasEnfermerias().setIdPrestador(loginMB.getUsuarioActual());
        this.getUrgNotasEnfermerias().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoNotasEnfermeria').show();");
    }

    public void agregarNotaEnfermeria() {
        if (this.getUrgNotasEnfermerias().getNota().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una nota", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasEnfermeriaList().add(this.getUrgNotasEnfermerias());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoNotasEnfermeria').hide();");
        imprimirMensaje("Información", "Se ha agregado a la lista la nota", FacesMessage.SEVERITY_INFO);
    }

    public void guardarNotaEnfermeria() {
        Boolean nota = Boolean.FALSE;
        Iterator<UrgNotasEnfermerias> listaNota = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasEnfermeriaList().iterator();
        while (listaNota.hasNext()) {
            UrgNotasEnfermerias notaEnfermeria = listaNota.next();
            if (notaEnfermeria.getIdNota() == null) {
                nota = Boolean.TRUE;
                break;
            }
        }
        if (nota.equals(Boolean.FALSE)) {
            imprimirMensaje("Error", "No existen nuevos registros de nota para guardar", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        imprimirMensaje("Información", "Se ha guardado la(s) nota de la enfermera", FacesMessage.SEVERITY_INFO);
    }

    public void guardarSalidaUrgenciaPaciente() {
        if (this.getDestinoPaciente() != null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setAmbitoDestinoPaciente(this.getClasificacionesFachada().find(Integer.parseInt(this.getDestinoPaciente())));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un destino para el paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getObservacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una observación", FacesMessage.SEVERITY_ERROR);
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setObservacion(null);
            return;
        }
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setFecha(new Date());
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setHora(new Date());
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setIdPrestador(loginMB.getUsuarioActual());
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().setIdUrgConsultaPacienteUrgencia(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.getUrgPlanyManejoFacade().create(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente());
        imprimirMensaje("Información", "Se ha guardado el plan de manejo y recomendaciones del paciente y se ha concluido el proceso de urgencias", FacesMessage.SEVERITY_INFO);
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));
    }

    public void crearNuevaRecomendacion() {
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
    }

    public void cargarDialogoCancelarServicio(FacConsumoServicio fac) {
        this.setCantidad(0);
        this.setCantidad(fac.getCantidad());
        this.setFacConsumoServicio(fac);
        System.out.println("cantidad" + this.getCantidad());
        RequestContext.getCurrentInstance().execute("PF('IdDialogoGestionarServicios').show()");
    }

    public void guardarServiciosCancelados() {
        if (this.getFacConsumoServicio().getObservacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar un motivo de cancelación", FacesMessage.SEVERITY_INFO);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('IdDialogoGestionarServicios').hide()");
        System.out.println("cantidad dos" + this.getCantidad());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getIdDestino() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(null);
        }
        this.getFacConsumoServicio().setEstado(true);
        this.getFacConsumoServicio().setProfesionaApruebaServicio(this.loginMB.getUsuarioActual());
        this.getFacConsumoServicio().setEstado(true);
        this.getFacConsumoServicio().setObservacion(this.getFacConsumoServicio().getObservacion());
        this.getFacConsumoServicio().setFechaCancelacion(new Date());
        this.getFacConsumoServicio().setValorUnitario(0.0);
        this.getFacConsumoServicio().setValorFinal(0.0);
        this.getFacConsumoServicio().setCantidad(this.getCantidad());
        this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgOrdenCobro().getFacConsumoServiciolist().add(this.getFacConsumoServicio());
        this.getUrgConsultaFacade().edit(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia());
        this.setUrgTriage(this.getUrgTriageFacade().find(this.getUrgTriage().getIdTriage()));

        imprimirMensaje("Información", "Se ha cancelado el servicio asociado al paciente", FacesMessage.SEVERITY_INFO);
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() == null) {
            this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().setUrgDestinoPaciente(new UrgPlanManejoPaciente());
        }
        RequestContext.getCurrentInstance().update("formConsulta:IdTabView:IdTablaGestionarServicios");
    }

    public int daysBetween(Date d1, Date d2) {
        int nro = 0;
        nro = (int) ((d2.getTime() - d1.getTime()));
        if (nro <= 0) {
            nro = 1;
        }
        return nro;
    }

    public void generarNotaEnfermeria(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        List<ReporteNotaEnfermerasViewModel> notasEnfermeria = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");

        Iterator<UrgNotasEnfermerias> listaNota = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasEnfermeriaList().iterator();
        while (listaNota.hasNext()) {
            UrgNotasEnfermerias notaEnfermeria = listaNota.next();
            ReporteNotaEnfermerasViewModel notas = new ReporteNotaEnfermerasViewModel();
            notas.setPacienteCama(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getCfgCama().getNumeroCama());
            notas.setPacienteHabitacion(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getCfgCama().getCfgHabitacion().getNumeroHabitacion());
            notas.setFecha(formatFecha.format(notaEnfermeria.getFecha()));
            notas.setHora(formatHora.format(notaEnfermeria.getHora()));
            notas.setNota(notaEnfermeria.getNota());
            notas.setEstado(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdUrgTriage().getIdAdmision().getEstadoAdmisionPaciente().getDescription());
            notas.setUser(loginMB.getUsuarioActual().getPrimerNombre() + " " + loginMB.getUsuarioActual().getPrimerApellido());
            notas.setPacienteNumDoc(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getIdentificacion());
            notas.setPacientePA(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerApellido());
            notas.setPacienteSA(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoApellido());
            notas.setPacientePN(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerNombre());
            notas.setPacienteSN(notaEnfermeria.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoNombre());
            notas.setPrestadorPA(notaEnfermeria.getIdPrestador().getPrimerApellido());
            notas.setPrestadorPN(notaEnfermeria.getIdPrestador().getSegundoNombre());
            notas.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
            notas.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
            notas.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
            notasEnfermeria.add(notas);
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(notasEnfermeria);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/notaenfermeras.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarNotaAdministracionMedicamentos(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        List<ReporteNotaEnfermerasViewModel> notasAdministracionMedicamentos = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");

        Iterator<UrgPrescripcionMedicamento> listaPrescripcion = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist().iterator();
        while (listaPrescripcion.hasNext()) {
            UrgPrescripcionMedicamento prescripcion = listaPrescripcion.next();
            Iterator<UrgControlPrescripcionMedicamento> listaControlPrescripcion = prescripcion.getUrgControlPrescripcionMedicamentoList().iterator();
            while (listaControlPrescripcion.hasNext()) {
                UrgControlPrescripcionMedicamento control = listaControlPrescripcion.next();

                ReporteNotaEnfermerasViewModel notas = new ReporteNotaEnfermerasViewModel();
                notas.setPacienteCama(prescripcion.getIdUrgConsultaPacienteUrgencia().getCfgCama().getNumeroCama());
                notas.setPacienteHabitacion(prescripcion.getIdUrgConsultaPacienteUrgencia().getCfgCama().getCfgHabitacion().getNumeroHabitacion());
                notas.setFecha(formatFecha.format(control.getFecha()));
                notas.setHora(formatHora.format(control.getHora()));
                notas.setNota(control.getObservacion());
                notas.setEstado(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdUrgTriage().getIdAdmision().getEstadoAdmisionPaciente().getDescription());
                notas.setUser(loginMB.getUsuarioActual().getPrimerNombre() + " " + loginMB.getUsuarioActual().getPrimerApellido());
                notas.setPacienteNumDoc(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getIdentificacion());
                notas.setPacientePA(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerApellido());
                notas.setPacienteSA(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoApellido());
                notas.setPacientePN(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerNombre());
                notas.setPacienteSN(prescripcion.getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoNombre());
                notas.setPrestadorPA(control.getIdPrestador().getPrimerApellido());
                notas.setPrestadorPN(control.getIdPrestador().getPrimerNombre());
                notas.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
                notas.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
                notas.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
                notasAdministracionMedicamentos.add(notas);
            }
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(notasAdministracionMedicamentos);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/notaadministracionmedicamentos.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarTriage(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        List<ReporteTriageViewModel> triages = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");

        ReporteTriageViewModel triageView = new ReporteTriageViewModel();
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia() != null) {

        }
        //triageView.setPacienteCama(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getCfgCama().getNumeroCama());
        //  triageView.setPacienteHabitacion(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getCfgCama().getCfgHabitacion().getNumeroHabitacion());
        triageView.setFecha(formatFecha.format(this.getUrgTriage().getFechaTriage()));
        triageView.setHora(formatHora.format(this.getUrgTriage().getHoraTriage()));
        triageView.setEstado(this.getUrgTriage().getIdAdmision().getEstadoAdmisionPaciente().getDescription());
        triageView.setUser(loginMB.getUsuarioActual().getPrimerNombre() + " " + loginMB.getUsuarioActual().getPrimerApellido());
        triageView.setPacienteNumDoc(this.getUrgTriage().getIdAdmision().getIdPaciente().getCarnet());
        triageView.setPacientePA(this.getUrgTriage().getIdAdmision().getIdPaciente().getPrimerApellido());
        triageView.setPacienteSA(this.getUrgTriage().getIdAdmision().getIdPaciente().getSegundoApellido());
        triageView.setPacientePN(this.getUrgTriage().getIdAdmision().getIdPaciente().getPrimerNombre());
        triageView.setPacienteSN(this.getUrgTriage().getIdAdmision().getIdPaciente().getSegundoNombre());
        triageView.setPrestadorPA(this.getUrgTriage().getIdPrestador().getPrimerApellido());
        triageView.setPrestadorPN(this.getUrgTriage().getIdPrestador().getPrimerNombre());
        triageView.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
        triageView.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
        triageView.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
        triageView.setConducta(this.getUrgTriage().getConducta());
        triageView.setDocumentoIdentidad(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdentificacion());
        triageView.setFrecuenciaCardiaca(this.getUrgTriage().getFrecuenciaCardiaca().toString());
        triageView.setFrecuenciaRespiratoria(this.getUrgTriage().getFrecuencia_respiratoria().toString());
        triageView.setHallazgosClinico(this.getUrgTriage().getHallazgosClinico());
        triageView.setImpresionDiagnostica(this.getUrgTriage().getImpresionDiagnostica().getDescripcion());
        triageView.setMc((this.getUrgTriage().getMc().toString()));
        triageView.setMotivo(this.getUrgTriage().getMotivo());
        triageView.setNivelTriage(this.getUrgTriage().getNivelTriage().getDescription());
        triageView.setNumeroRegistroProfesional(this.getUrgTriage().getIdPrestador().getRegistroProfesional());
        triageView.setPacienteDireccion(this.getUrgTriage().getIdAdmision().getIdPaciente().getDireccion());
        triageView.setPacienteEdad(String.valueOf(calcularEdadInt(this.getUrgTriage().getIdAdmision().getIdPaciente().getFechaNacimiento())));
        triageView.setPacienteFechaNacimiento(formatFecha.format(this.getUrgTriage().getIdAdmision().getIdPaciente().getFechaNacimiento()));
        triageView.setPacienteSexo(this.getUrgTriage().getIdAdmision().getIdPaciente().getGenero().getDescripcion());
        triageView.setPacienteTelefono(this.getUrgTriage().getIdAdmision().getIdPaciente().getTelefonoResidencia());
        triageView.setPacienteTipoDocumento(this.getUrgTriage().getIdAdmision().getIdPaciente().getTipoIdentificacion().getObservacion());
        triageView.setPeso(this.getUrgTriage().getPeso().toString());
        triageView.setPresionArteriaMaxima(this.getUrgTriage().getPresionArteriaMaxima().toString());
        triageView.setPresionArteriaMinima(this.getUrgTriage().getPresionArteriaMinima().toString());
        triageView.setTalla(this.getUrgTriage().getTalla().toString());
        triageView.setTemperatura(this.getUrgTriage().getTemperatura().toString());
        triageView.setSaturacion(this.getUrgTriage().getSaturacion().toString());
        triages.add(triageView);

        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(triages);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/triage.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarEvolucion(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        List<ReporteTriageViewModel> notasEvolucion = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        Iterator<UrgNotasMedicas> listaNota = this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgNotasMedicasList().iterator();
        while (listaNota.hasNext()) {
            UrgNotasMedicas notaEvolucion = listaNota.next();

            ReporteTriageViewModel evolucion = new ReporteTriageViewModel();
            evolucion.setFecha(formatFecha.format(this.getUrgTriage().getFechaTriage()));
            evolucion.setHora(formatHora.format(this.getUrgTriage().getHoraTriage()));
            evolucion.setUser(loginMB.getUsuarioActual().getPrimerNombre() + " " + loginMB.getUsuarioActual().getPrimerApellido());
            evolucion.setPacientePA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerApellido());
            evolucion.setPacienteSA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoApellido());
            evolucion.setPacientePN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerNombre());
            evolucion.setPacienteSN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoNombre());
            evolucion.setPrestadorPA(notaEvolucion.getIdPrestador().getPrimerApellido());
            evolucion.setPrestadorPN(notaEvolucion.getIdPrestador().getPrimerNombre());
            evolucion.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
            evolucion.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
            evolucion.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
            evolucion.setDocumentoIdentidad(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdentificacion());
            evolucion.setPacienteDireccion(this.getUrgTriage().getIdAdmision().getIdPaciente().getDireccion());
            evolucion.setPacienteTipoDocumento(this.getUrgTriage().getIdAdmision().getIdPaciente().getTipoIdentificacion().getObservacion());
            evolucion.setNota(notaEvolucion.getNota());
            notasEvolucion.add(evolucion);
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(notasEvolucion);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/evolucion.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarRisp(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm:ss");
        List<ReporteRipsViewModel> rispUrgencia = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        ReporteRipsViewModel risp = new ReporteRipsViewModel();
        risp.setFecha(formatFecha.format(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getFechaIniConsulta()));
        risp.setHora(formatHora.format(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getFechaFinConsulta()));
        risp.setUser(loginMB.getUsuarioActual().getPrimerNombre() + " " + loginMB.getUsuarioActual().getPrimerApellido());
        risp.setPacientePA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerApellido());
        risp.setPacienteSA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoApellido());
        risp.setPacientePN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerNombre());
        risp.setPacienteSN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoNombre());
        risp.setPrestadorPA(this.getUrgTriage().getIdPrestador().getPrimerApellido());
        risp.setPrestadorPN(this.getUrgTriage().getIdPrestador().getPrimerNombre());
        risp.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
        risp.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
        risp.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
        risp.setDocumentoIdentidad(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdentificacion());
        risp.setPacienteDireccion(this.getUrgTriage().getIdAdmision().getIdPaciente().getDireccion());
        risp.setTipoIdentificacion(this.getUrgTriage().getIdAdmision().getIdPaciente().getTipoIdentificacion().getObservacion());
        risp.setNumeroAdmision(this.getUrgTriage().getIdAdmision().getNroAdmision());
        risp.setDocumentoIdentidad(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdentificacion());
        risp.setPacienteSexo(this.getUrgTriage().getIdAdmision().getIdPaciente().getGenero().getDescripcion());
        risp.setCarnet(this.getUrgTriage().getIdAdmision().getIdPaciente().getCarnet());
        risp.setPacienteEdad(String.valueOf(calcularEdadInt(this.getUrgTriage().getIdAdmision().getIdPaciente().getFechaNacimiento())));
        risp.setLocalidad((this.getUrgTriage().getIdAdmision().getIdPaciente().getMunicipio().getDescripcion()));
        risp.setAdministradora(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdAdministradora().getNombreRepresentante());
        risp.setCodigoConsulta((this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdServicio().getCodigoServicio()));
        risp.setPacienteFechaNacimiento(formatFecha.format(this.getUrgTriage().getIdAdmision().getIdPaciente().getFechaNacimiento()));
        risp.setMotivo(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getMotivo());
        risp.setConsulta((this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdServicio().getNombreServicio()));
        risp.setFechaSalida(formatFecha.format(this.getUrgTriage().getIdAdmision().getFechaSalidaUrgencia()));
        risp.setHoraSalida(formatFecha.format(this.getUrgTriage().getIdAdmision().getHoraSalidaUrgencia()));
        risp.setImpresionDiagnostica(this.getUrgTriage().getImpresionDiagnostica().getDescripcion());
        risp.setCodigoDiagnostico(this.getUrgTriage().getImpresionDiagnostica().getCodigo());
        risp.setZona(this.getUrgTriage().getIdAdmision().getIdPaciente().getZona().getDescripcion());
        if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente() != null) {
            if (this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getAmbitoDestinoPaciente() != null) {
                risp.setDestino(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getAmbitoDestinoPaciente().getDescripcion());
                risp.setNota(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgDestinoPaciente().getObservacion());

            } else {
                risp.setDestino(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente().getDescripcion());
                risp.setNota(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getRecomendacion());
            }
        } else {
            risp.setDestino(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getAmbitoDestinoPaciente().getDescripcion());
            risp.setNota(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getRecomendacion());

        }
        risp.setDiagnosticoIngreso(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getIdCfgDiagnostico());
        risp.setObservaciones(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdUrgDetalleConsulta().getObservacion());
        risp.setResponsable(this.getUrgTriage().getIdAdmision().getIdPaciente().getResponsable());
        rispUrgencia.add(risp);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(rispUrgencia);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/ripsdeurgencias.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void generarFormulaMedica(ActionEvent actionEvent) throws JRException, IOException, SQLException, ClassNotFoundException {

        List<ReporteFormulaMedicaViewModel> formulaMedica = new ArrayList<>();
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        for (UrgPrescripcionMedicamento medicamento : this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getUrgPrescripcionMedicamentolist()) {

            ReporteFormulaMedicaViewModel formula = new ReporteFormulaMedicaViewModel();
            formula.setPacientePA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerApellido());
            formula.setPacienteSA(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoApellido());
            formula.setPacientePN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getPrimerNombre());
            formula.setPacienteSN(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPaciente().getSegundoNombre());
            formula.setPrestadorPA(this.getUrgTriage().getIdPrestador().getPrimerApellido());
            formula.setPrestadorPN(this.getUrgTriage().getIdPrestador().getPrimerNombre());
            formula.setSede(loginMB.getCentroDeAtencionactual().getNombreSede());
            formula.setSedeDir(loginMB.getCentroDeAtencionactual().getDireccion());
            formula.setSedeTel(loginMB.getCentroDeAtencionactual().getTelefono1());
            formula.setIdentificacion(this.getUrgTriage().getIdAdmision().getIdPaciente().getIdentificacion());
            formula.setDir(this.getUrgTriage().getIdAdmision().getIdPaciente().getDireccion());
            formula.setCarnet(this.getUrgTriage().getIdAdmision().getIdPaciente().getCarnet());
            formula.setNumeroRegistroProfesional(this.getUrgTriage().getIdUrgConsultaPacienteUrgencia().getIdPrestador().getRegistroProfesional());
            formula.setPacienteTelefono(this.getUrgTriage().getIdAdmision().getIdPaciente().getCelular());
            formula.setCantidad(medicamento.getCantidad().toString());
            formula.setNota("Suministrar el medicamento" + " " + medicamento.getIdMedicamento().getNombreMedicamento() + " una dosis de " + medicamento.getDosis() + "cada"
                    + medicamento.getIndicacionesMedicamentos().getDescription() + "; " + medicamento.getObservacion());
            formulaMedica.add(formula);
        }
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(formulaMedica);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("urgencias/reportes/formulamedica.jasper");
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("logoEmpresa", logoEmpresa);
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, parametros, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public CfgUsuarios getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public UrgTriage getUrgTriage() {
        return urgTriage;
    }

    public void setUrgTriage(UrgTriage urgTriage) {
        this.urgTriage = urgTriage;
    }

    public void setUsuarioLogueado(CfgUsuarios usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public List<UrgTriage> getListaUrgTriagePaciente() {
        if (listaUrgTriagePaciente == null) {
            listaUrgTriagePaciente = new ArrayList<>();
        }
        return listaUrgTriagePaciente;
    }

    public void setListaUrgTriagePaciente(List<UrgTriage> listaUrgTriagePaciente) {
        this.listaUrgTriagePaciente = listaUrgTriagePaciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public UrgAmisionFacade getUrgAmisionFacade() {
        return urgAmisionFacade;
    }

    public void setUrgAmisionFacade(UrgAmisionFacade urgAmisionFacade) {
        this.urgAmisionFacade = urgAmisionFacade;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public UrgTriageFacade getUrgTriageFacade() {
        return urgTriageFacade;
    }

    public void setUrgTriageFacade(UrgTriageFacade urgTriageFacade) {
        this.urgTriageFacade = urgTriageFacade;
    }

    public Set<NivelTriage> getNivelTriage() {
        return nivelTriage;
    }

    public void setNivelTriage(Set<NivelTriage> nivelTriage) {
        this.nivelTriage = nivelTriage;
    }

    public String getImmpresionDiagnostica() {
        return immpresionDiagnostica;
    }

    public void setImmpresionDiagnostica(String immpresionDiagnostica) {
        this.immpresionDiagnostica = immpresionDiagnostica;
    }

    public CfgClasificacionesFacade getClasificacionesFachada() {
        return clasificacionesFachada;
    }

    public void setClasificacionesFachada(CfgClasificacionesFacade clasificacionesFachada) {
        this.clasificacionesFachada = clasificacionesFachada;
    }

    public Integer getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
    }

    public UrgConsultaFacade getUrgConsultaFacade() {
        return urgConsultaFacade;
    }

    public void setUrgConsultaFacade(UrgConsultaFacade urgConsultaFacade) {
        this.urgConsultaFacade = urgConsultaFacade;
    }

    public List<FacServicio> getListaServicios() {
        if (this.listaServicios == null) {
            listaServicios = new ArrayList<>();
        }
        return listaServicios;
    }

    public void setListaServicios(List<FacServicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public FacServicioFacade getFacServicioFacade() {
        return facServicioFacade;
    }

    public void setFacServicioFacade(FacServicioFacade facServicioFacade) {
        this.facServicioFacade = facServicioFacade;
    }

    public Set<EstadoFisicoPaciente> getEstadoFisicoPaciente() {
        return estadoFisicoPaciente;
    }

    public void setEstadoFisicoPaciente(Set<EstadoFisicoPaciente> estadoFisicoPaciente) {
        this.estadoFisicoPaciente = estadoFisicoPaciente;
    }

    public UrgDetalleConsultaFacade getUrgDetalleConsultaFacade() {
        return urgDetalleConsultaFacade;
    }

    public void setUrgDetalleConsultaFacade(UrgDetalleConsultaFacade urgDetalleConsultaFacade) {
        this.urgDetalleConsultaFacade = urgDetalleConsultaFacade;
    }

    public CfgDiagnosticoFacade getCfgDiagnosticoFacade() {
        return cfgDiagnosticoFacade;
    }

    public void setCfgDiagnosticoFacade(CfgDiagnosticoFacade cfgDiagnosticoFacade) {
        this.cfgDiagnosticoFacade = cfgDiagnosticoFacade;
    }

    public String getDestinoPaciente() {
        return destinoPaciente;
    }

    public void setDestinoPaciente(String destinoPaciente) {
        this.destinoPaciente = destinoPaciente;
    }

    public String getIdMedicamentoManual() {
        return idMedicamentoManual;
    }

    public void setIdMedicamentoManual(String idMedicamentoManual) {
        this.idMedicamentoManual = idMedicamentoManual;
    }

    public List<FacManualTarifarioMedicamento> getListaMedicamentosManual() {
        if (this.listaMedicamentosManual == null) {
            listaMedicamentosManual = new ArrayList<>();
        }
        return listaMedicamentosManual;
    }

    public void setListaMedicamentosManual(List<FacManualTarifarioMedicamento> listaMedicamentosManual) {
        this.listaMedicamentosManual = listaMedicamentosManual;
    }

    public List<CfgMedicamento> getListaCfgMedicamento() {
        if (this.listaCfgMedicamento == null) {
            listaCfgMedicamento = new ArrayList<>();
        }
        return listaCfgMedicamento;
    }

    public void setListaCfgMedicamento(List<CfgMedicamento> listaCfgMedicamento) {
        this.listaCfgMedicamento = listaCfgMedicamento;
    }

    public List<UrgPrescripcionMedicamento> getListaUrgPrescripcionMedicamento() {
        if (this.listaUrgPrescripcionMedicamento == null) {
            listaUrgPrescripcionMedicamento = new ArrayList<>();
        }
        return listaUrgPrescripcionMedicamento;
    }

    public void setListaUrgPrescripcionMedicamento(List<UrgPrescripcionMedicamento> listaUrgPrescripcionMedicamento) {
        this.listaUrgPrescripcionMedicamento = listaUrgPrescripcionMedicamento;
    }

    public List<FacServicio> getListaServiciosOrden() {
        if (this.listaServiciosOrden == null) {
            listaServiciosOrden = new ArrayList<>();
        }
        return listaServiciosOrden;
    }

    public void setListaServiciosOrden(List<FacServicio> listaServiciosOrden) {
        this.listaServiciosOrden = listaServiciosOrden;
    }

    public UrgPrescripcionMedicamento getUrgPrescripcionMedicamento() {
        return urgPrescripcionMedicamento;
    }

    public void setUrgPrescripcionMedicamento(UrgPrescripcionMedicamento urgPrescripcionMedicamento) {
        this.urgPrescripcionMedicamento = urgPrescripcionMedicamento;
    }

    public Set<IndicacionesMedicamentos> getIndicacionesMedicamentos() {
        return indicacionesMedicamentos;
    }

    public void setIndicacionesMedicamentos(Set<IndicacionesMedicamentos> indicacionesMedicamentos) {
        this.indicacionesMedicamentos = indicacionesMedicamentos;
    }

    public FacConsumoMedicamento getFacConsumoMedicamento() {
        return facConsumoMedicamento;
    }

    public void setFacConsumoMedicamento(FacConsumoMedicamento facConsumoMedicamento) {
        this.facConsumoMedicamento = facConsumoMedicamento;
    }

    public CfgMedicamentoFacade getCfgMedicamentoFacade() {
        return cfgMedicamentoFacade;
    }

    public void setCfgMedicamentoFacade(CfgMedicamentoFacade cfgMedicamentoFacade) {
        this.cfgMedicamentoFacade = cfgMedicamentoFacade;
    }

    public FacManualTarifario getManualTarifarioPaciente() {
        return manualTarifarioPaciente;
    }

    public void setManualTarifarioPaciente(FacManualTarifario manualTarifarioPaciente) {
        this.manualTarifarioPaciente = manualTarifarioPaciente;
    }

    public FacConsumoServicio getFacConsumoServicio() {
        return facConsumoServicio;
    }

    public void setFacConsumoServicio(FacConsumoServicio facConsumoServicio) {
        this.facConsumoServicio = facConsumoServicio;
    }

    public UrgOrdenCobro getUrgOrdenCobro() {
        return urgOrdenCobro;
    }

    public void setUrgOrdenCobro(UrgOrdenCobro urgOrdenCobro) {
        this.urgOrdenCobro = urgOrdenCobro;
    }

    public FacConsumoInsumo getFacConsumoInsumo() {
        return facConsumoInsumo;
    }

    public void setFacConsumoInsumo(FacConsumoInsumo facConsumoInsumo) {
        this.facConsumoInsumo = facConsumoInsumo;
    }

    public List<CfgInsumo> getListaInsumosOrden() {
        if (this.listaInsumosOrden == null) {
            listaInsumosOrden = new ArrayList<>();
        }
        return listaInsumosOrden;
    }

    public void setListaInsumosOrden(List<CfgInsumo> listaInsumosOrden) {
        this.listaInsumosOrden = listaInsumosOrden;
    }

    public FacConsumoPaquete getFacConsumoPaquete() {
        return facConsumoPaquete;
    }

    public void setFacConsumoPaquete(FacConsumoPaquete facConsumoPaquete) {
        this.facConsumoPaquete = facConsumoPaquete;
    }

    public List<FacPaquete> getListaFacPaqueteOrden() {
        if (this.listaFacPaqueteOrden == null) {
            listaFacPaqueteOrden = new ArrayList<>();
        }
        return listaFacPaqueteOrden;
    }

    public void setListaFacPaqueteOrden(List<FacPaquete> listaFacPaqueteOrden) {
        this.listaFacPaqueteOrden = listaFacPaqueteOrden;
    }

    public List<UrgTriage> getListaUrgTriagePacienteDadoAlta() {
        if (this.listaUrgTriagePacienteDadoAlta == null) {
            listaUrgTriagePacienteDadoAlta = new ArrayList<>();
        }
        return listaUrgTriagePacienteDadoAlta;
    }

    public void setListaUrgTriagePacienteDadoAlta(List<UrgTriage> listaUrgTriagePacienteDadoAlta) {
        this.listaUrgTriagePacienteDadoAlta = listaUrgTriagePacienteDadoAlta;
    }

    public UrgControlPrescripcionMedicamento getUrgControlPrescripcionMedicamento() {
        return urgControlPrescripcionMedicamento;
    }

    public void setUrgControlPrescripcionMedicamento(UrgControlPrescripcionMedicamento urgControlPrescripcionMedicamento) {
        this.urgControlPrescripcionMedicamento = urgControlPrescripcionMedicamento;
    }

    public UrgControlySuministroMedicamentosFacade getUrgControlySuministroMedicamentosFacade() {
        return urgControlySuministroMedicamentosFacade;
    }

    public void setUrgControlySuministroMedicamentosFacade(UrgControlySuministroMedicamentosFacade urgControlySuministroMedicamentosFacade) {
        this.urgControlySuministroMedicamentosFacade = urgControlySuministroMedicamentosFacade;
    }

    public UrgPrescripcionMedicamentoFacade getUrgPrescripcionMedicamentoFacade() {
        return urgPrescripcionMedicamentoFacade;
    }

    public void setUrgPrescripcionMedicamentoFacade(UrgPrescripcionMedicamentoFacade urgPrescripcionMedicamentoFacade) {
        this.urgPrescripcionMedicamentoFacade = urgPrescripcionMedicamentoFacade;
    }

    public UrgNotasMedicas getUrgNotasMedicas() {
        return urgNotasMedicas;
    }

    public void setUrgNotasMedicas(UrgNotasMedicas urgNotasMedicas) {
        this.urgNotasMedicas = urgNotasMedicas;
    }

    public UrgNotasEnfermerias getUrgNotasEnfermerias() {
        return urgNotasEnfermerias;
    }

    public void setUrgNotasEnfermerias(UrgNotasEnfermerias urgNotasEnfermerias) {
        this.urgNotasEnfermerias = urgNotasEnfermerias;
    }

    public String getDestinoFinalPaciente() {
        return destinoFinalPaciente;
    }

    public void setDestinoFinalPaciente(String destinoFinalPaciente) {
        this.destinoFinalPaciente = destinoFinalPaciente;
    }

    public UrgPlanyManejoFacade getUrgPlanyManejoFacade() {
        return urgPlanyManejoFacade;
    }

    public void setUrgPlanyManejoFacade(UrgPlanyManejoFacade urgPlanyManejoFacade) {
        this.urgPlanyManejoFacade = urgPlanyManejoFacade;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public CfgCama getCamaDisponible() {
        return camaDisponible;
    }

    public void setCamaDisponible(CfgCama camaDisponible) {
        this.camaDisponible = camaDisponible;
    }

    public Boolean getCamaNoDisponible() {
        return camaNoDisponible;
    }

    public void setCamaNoDisponible(Boolean camaNoDisponible) {
        this.camaNoDisponible = camaNoDisponible;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

}
