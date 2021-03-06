/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.pyp_programas;

import beans.utilidades.AutorizacionReport;
import beans.utilidades.CSVUtils;
import beans.utilidades.CitaU;
import beans.utilidades.Informe4505;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.Oportunidad;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import managedBeans.historias.DatosFormularioHistoria;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacFacturaPaciente;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcTipoReg;
import modelo.entidades.PyPProgramaItem;
import modelo.entidades.PyPprograma;
import modelo.entidades.PyPprogramaAsoc;
import modelo.entidades.PypProgramaCita;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.HcCamposRegFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.HcTipoRegFacade;
import modelo.fachadas.PyPProgramas;
import modelo.fachadas.PyPprogramAsoc;
import modelo.fachadas.PyPprogramaItems;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "reporteProgramasMB")
@ViewScoped
public class ReporteProgramasMB extends MetodosGenerales implements Serializable {

    /**
     * Creates a new instance of RecordatorioMB
     */
    private String tipoReporte;
    private List<SelectItem> listaprograma;
    private String identificacionPaciente;
    private Date fechaInicial;
    private Date fechaFinal;
    private CfgPacientes pacienteSeleccionado;
    private Informe4505 pacienteSeleccionado4505;
    private CfgPacientes pacienteHC;
    private CfgUsuarios prestadorSeleccionado;
    private FacAdministradora administradoraSeleccionada;
    private List<CfgUsuarios> listaPrestadores;//para filtrar
    private List<CfgPacientes> listaPacientes;//para filtrar
    List<CfgPacientes> user;
    List<Informe4505> post_filtrar;
    Informe4505 u = new Informe4505();
    private List<SelectItem> listaAdministradoras;
    private List<FacContrato> listaContratos;
    private List<SelectItem> listaReportes;
    private List<SelectItem> listaEstadoAutorizacion;
    private LazyDataModel<CfgUsuarios> prestadores;
    private LazyDataModel<CfgPacientes> pacientes;
    private boolean renBtnFiltrar;//renderiza el boton de filtrado
    private boolean renProgramas;//renderiza el boton de filtrado
    private boolean renBtnAdministradora;//renderiza el boton de filtrado
    private boolean renBtcontrato;//renderiza el boton de filtrado
    private boolean renBtnPaciente;//renderiza el boton de filtrado
    private boolean renBtnReporte;//renderiza el boton de generear reporte
    private boolean renFechas;
    private boolean renModificar;
    private boolean renBuscar;
    private String displayBusquedaPaciente = "none";
    private LazyDataModel<CfgPacientes> listaPacientesBusqueda;
    private int estadoAutorizacion;// 0 -> todas, 1->cerradas, 2->no cerradas
    private int idAdministradora;
    private int id_contrato;
    private String numAutorizacion;
    private int id_programa;
    StreamedContent file;
    private int sede;

    @EJB
    FacServicioFacade servicioFacade;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    CfgUsuariosFacade usuariosFachada;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    FacAdministradoraFacade administradoraFacade;

    @EJB
    PyPProgramas PyPprogramasFacade;

    public ReporteProgramasMB() {
    }

    @PostConstruct
    public void init() {
        listaAdministradoras = new ArrayList();
        listaPacientes = new ArrayList();
        listaPrestadores = new ArrayList();
        listaprograma = new ArrayList();
        post_filtrar = new ArrayList();
        crearlistaProgramas();
        setListaPacientesBusqueda(new LazyPacienteDataModel(pacientesFachada));
        listaReportes = new ArrayList();
        listaEstadoAutorizacion = new ArrayList();
        listaEstadoAutorizacion.add(new SelectItem(0, "Todas"));
        listaEstadoAutorizacion.add(new SelectItem(1, "Cerradas"));
        listaEstadoAutorizacion.add(new SelectItem(2, "Abiertas"));
        setRenBtnFiltrar(false);
        setRenBtnReporte(true);
        crearlistaAdministradoras();
        setRenBtnAdministradora(false);
        setRenBtnPaciente(false);
        setRenFechas(true);
        setRenProgramas(true);
        setRenBtcontrato(false);
        setRenModificar(false);
        setRenBuscar(false);
        crearMenuReportes();
        setPacientes(new LazyPacienteDataModel(pacientesFachada));
        setPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
    }

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    PyPProgramas programasFacade;

    @EJB
    FacContratoFacade contratoFacade;

    @EJB
    PyPprogramAsoc programasAsocFacade;

    @EJB
    CfgEmpresaFacade cfgEmpresaFacade;

    @EJB
    PyPprogramaItems pypItemFacade;

    //--------------------------------------------------------------------------------
    //--------------------------------METHODS-----------------------------------------
    //--------------------------------------------------------------------------------
    private FacAdministradora administradoraActual;

    private void crearMenuReportes() {

        listaReportes.add(new SelectItem(1, "Programas"));
        listaReportes.add(new SelectItem(2, "Programas Ejecutados"));
        listaReportes.add(new SelectItem(3, "Pacientes"));
        listaReportes.add(new SelectItem(4, "Administradoras"));
//        listaReportes.add(new SelectItem(5, "Resolución 4505"));
    }

    public void onCellEdit(CellEditEvent event) {
        try {
            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void cambiaAdministradora_programa() {
//        listaContratos = new ArrayList<>();
//        if (!validarNoVacio(idAdministradora+"")) {
//            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
//            RequestContext.getCurrentInstance().execute("remoteCommand();");
//            return;
//        }
//        administradoraActual = administradoraFacade.find(Integer.parseInt(idAdministradora+""));
//        listaContratos = administradoraActual.getFacContratoList();
//        RequestContext.getCurrentInstance().update("formReporte");
//        RequestContext.getCurrentInstance().execute("remoteCommand();");
    }

    public void crearlistaAdministradoras() {
        List<FacAdministradora> progra = administradoraFacade.buscarOrdenado();
        for (FacAdministradora programa : progra) {
            getListaAdministradoras().add(new SelectItem(programa.getIdAdministradora(), programa.getCodigoAdministradora() + " - " + programa.getRazonSocial()));
        }
    }

    public void habilitarFuncionalidadReporte() {
        setIdentificacionPaciente(null);
        setPacienteHC(null);
        setRenBtnFiltrar(false);
        setRenBtnAdministradora(false);
        setRenBtnPaciente(false);
        setRenBtnReporte(false);
        setRenFechas(true);
        setRenProgramas(false);
        setRenBtcontrato(false);
        switch (tipoReporte) {
            case "1":
                setRenBtnReporte(true);
                setRenProgramas(true);
                break;
            case "2":
                setRenBtnReporte(true);
                setRenProgramas(true);
                break;
            case "3":
                setRenBtnReporte(true);
                setRenBtnPaciente(true);
                break;
            case "4":
                setRenBtnReporte(true);
                setRenBtnAdministradora(true);
                break;
            case "5":
                setRenBtnReporte(true);
                setRenBtnAdministradora(true);
                setRenBtcontrato(false);
                break;
            default:
                setRenBtnReporte(true);
                break;
        }
    }

    public void crearlistaProgramas() {
        List<PyPprograma> progra = PyPprogramasFacade.buscar_programas();
        for (PyPprograma programa : progra) {
            getListaprograma().add(new SelectItem(programa.getIdPrograma(), programa.getCodigoPrograma() + " - " + programa.getNombrePrograma()));
        }
    }

    public void findPaciente() {
        if (!identificacionPaciente.isEmpty()) {
            pacienteHC = pacientesFachada.buscarPorIdentificacion(getIdentificacionPaciente());
            if (pacienteHC == null) {
                setRenBtnReporte(false);
                imprimirMensaje("Error", "No se encontro el paciente", FacesMessage.SEVERITY_ERROR);
            } else {
                setRenBtnReporte(true);
            }
        } else {
            setPacienteSeleccionado(null);
            setRenBtnReporte(false);
        }
    }

    public void actualizarPaciente() {
        if (pacienteHC != null) {
            setIdentificacionPaciente(pacienteHC.getIdentificacion());
            setRenBtnReporte(true);
        }
    }

    private List<Oportunidad> generarListaOportunidad(List<Object[]> rows) {
        List<Oportunidad> listaoportunidad = new ArrayList();
        for (Object[] row : rows) {
            String servicio = (String) row[0];
            long totalcitas = row[1] == null ? 0 : (long) row[1];
            long totaldias = row[2] == null ? 0 : (long) row[2];
            listaoportunidad.add(new Oportunidad(servicio, totalcitas, totaldias));
        }
        return listaoportunidad;
    }

    private List<AutorizacionReport> generarListaAutorizaciones(List<CitAutorizaciones> autorizaciones) {
        List<AutorizacionReport> listaAutorizacionReport = new ArrayList();
        if (autorizaciones != null) {
            if (!autorizaciones.isEmpty()) {
                for (CitAutorizaciones autorizacion : autorizaciones) {
                    for (CitAutorizacionesServicios autorizacionServicio : autorizacion.getCitAutorizacionesServiciosList()) {
                        AutorizacionReport autorizacionReport = new AutorizacionReport(autorizacion.getIdAutorizacion(), autorizacion.getAdministradora().getRazonSocial(), autorizacion.getPaciente().nombreCompleto(), autorizacion.getNumAutorizacion(), autorizacion.getCerrada(), autorizacionServicio.getFacServicio().getNombreServicio(), autorizacionServicio.getSesionesAutorizadas(), autorizacionServicio.getSesionesRealizadas(), autorizacionServicio.getSesionesPendientes());
                        listaAutorizacionReport.add(autorizacionReport);
                    }
                }
            }
        }
        return listaAutorizacionReport;
    }

    private List<CitaU> generarCitasAuxiliar(List<CitCitas> citas, List<PypProgramaCita> programas) {
        List<CitaU> listaCitasU = new ArrayList();
        if (citas != null) {
            int i = 0;
            for (CitCitas cita : citas) {
                CitaU citaU = new CitaU();
                List<CfgEmpresa> listaempresa = cfgEmpresaFacade.findAll();
                List<PyPProgramaItem> lista_item = pypItemFacade.buscar_programas_idItem(programas.get(i).getIdProgramaItem());
                List<PyPprograma> lista_programa = programasFacade.buscar_programas_id(lista_item.get(0).getIdPrograma());
                citaU.setActividad(lista_item.get(0).getNombreActividad());
                citaU.setPrograma(lista_programa.get(0).getCodigoPrograma() + " - " + lista_programa.get(0).getNombrePrograma());
                //solo existira una empresa => intranet
                citaU.setEmpresa(listaempresa.get(0).getRazonSocial());
                citaU.setEmpresaDireccion(listaempresa.get(0).getDireccion());
                citaU.setEmpresaTelefono(listaempresa.get(0).getTelefono1());
                citaU.setPaciente(citas.get(i).getIdPaciente().getPrimerNombre() + " " + citas.get(i).getIdPaciente().getSegundoNombre() + " " + citas.get(i).getIdPaciente().getPrimerApellido() + " " + citas.get(0).getIdPaciente().getSegundoApellido());
                citaU.setObservaciones(listaempresa.get(0).getObservaciones());
                citaU.setSede(cita.getIdTurno().getIdConsultorio().getIdSede().getNombreSede());
                citaU.setSedeDir(cita.getIdTurno().getIdConsultorio().getIdSede().getDireccion());
                citaU.setSedeTel(cita.getIdTurno().getIdConsultorio().getIdSede().getTelefono1());
                citaU.setIdCita(cita.getIdCita());
                citaU.setFecha(cita.getIdTurno().getFecha());
                citaU.setHora(cita.getIdTurno().getHoraIni());
                citaU.setConsultorio(cita.getIdTurno().getIdConsultorio().getNomConsultorio());
                citaU.setIdPaciente(cita.getIdPaciente().getIdPaciente());
                citaU.setPrestadorPN(cita.getIdPrestador().nombreCompleto());
                citaU.setPrestadorEspecialidad(cita.getIdPrestador().getEspecialidad().getDescripcion());
                citaU.setIdPrestador(cita.getIdPrestador().getIdUsuario());
                citaU.setPacientePN(cita.getIdPaciente().nombreCompleto());
                System.out.println(cita.getIdPrestador().nombreCompleto());
                citaU.setCodAdministradora(String.valueOf(cita.getIdAdministradora().getIdAdministradora()));
                citaU.setAdministradora(cita.getIdAdministradora().getRazonSocial());
                citaU.setFechaRegistro(cita.getFechaRegistro());
                citaU.setPacienteTipoDoc(cita.getIdPaciente().getTipoIdentificacion().getDescripcion());
                citaU.setPacienteNumDoc(cita.getIdPaciente().getIdentificacion());
                if (cita.getTipoCita() != null) {
                    citaU.setMotivoConsulta(cita.getTipoCita().getDescripcion());
                }
                int idServicio = cita.getIdServicio().getIdServicio();
                citaU.setServicio(servicioFacade.find(idServicio).getNombreServicio());
                if (cita.getCancelada()) {
                    citaU.setCancelada(true);
                    citaU.setFechaCancelacion(cita.getFechaCancelacion());
                    citaU.setMotivoCancelacion(cita.getMotivoCancelacion().getDescripcion());
                } else {
                    citaU.setCancelada(false);
                    citaU.setFechaCancelacion(null);
                    citaU.setMotivoCancelacion("");
                }
                citaU.setAtendida(cita.getAtendida());
                listaCitasU.add(citaU);
                i++;
            }
        }
        return listaCitasU;
    }

    private List<CitaU> generarCitasAuxiliar(List<CitCitas> citas, List<PypProgramaCita> programas, List<FacFacturaPaciente> paciente) {
        List<CitaU> listaCitasU = new ArrayList();
        if (citas != null) {
            int i = 0;
            for (CitCitas cita : citas) {
                CitaU citaU = new CitaU();
                List<CfgEmpresa> listaempresa = cfgEmpresaFacade.findAll();
                List<PyPProgramaItem> lista_item = pypItemFacade.buscar_programas_idItem(programas.get(i).getIdProgramaItem());
                List<PyPprograma> lista_programa = programasFacade.buscar_programas_id(lista_item.get(0).getIdPrograma());
                citaU.setActividad(lista_item.get(0).getNombreActividad());
                citaU.setPrograma(lista_programa.get(0).getCodigoPrograma() + " - " + lista_programa.get(0).getNombrePrograma());
                //solo existira una empresa => intranet
                citaU.setEmpresa(listaempresa.get(0).getRazonSocial());
                citaU.setEmpresaDireccion(listaempresa.get(0).getDireccion());
                citaU.setEmpresaTelefono(listaempresa.get(0).getTelefono1());
                citaU.setPaciente(citas.get(i).getIdPaciente().getPrimerNombre() + " " + citas.get(i).getIdPaciente().getSegundoNombre() + " " + citas.get(i).getIdPaciente().getPrimerApellido() + " " + citas.get(0).getIdPaciente().getSegundoApellido());
                citaU.setObservaciones(listaempresa.get(0).getObservaciones());
                citaU.setSede(cita.getIdTurno().getIdConsultorio().getIdSede().getNombreSede());
                citaU.setSedeDir(cita.getIdTurno().getIdConsultorio().getIdSede().getDireccion());
                citaU.setSedeTel(cita.getIdTurno().getIdConsultorio().getIdSede().getTelefono1());
                citaU.setIdCita(cita.getIdCita());
                citaU.setFecha(cita.getIdTurno().getFecha());
                citaU.setHora(cita.getIdTurno().getHoraIni());
                citaU.setConsultorio(cita.getIdTurno().getIdConsultorio().getNomConsultorio());
                citaU.setIdPaciente(cita.getIdPaciente().getIdPaciente());
                citaU.setPrestadorPN(cita.getIdPrestador().nombreCompleto());
                citaU.setPrestadorEspecialidad(cita.getIdPrestador().getEspecialidad().getDescripcion());
                citaU.setIdPrestador(cita.getIdPrestador().getIdUsuario());
                citaU.setPacientePN(cita.getIdPaciente().nombreCompleto());
                System.out.println(cita.getIdPrestador().nombreCompleto());
                citaU.setCodAdministradora(String.valueOf(cita.getIdAdministradora().getIdAdministradora()));
                citaU.setAdministradora(cita.getIdAdministradora().getRazonSocial());
                citaU.setFechaRegistro(cita.getFechaRegistro());
                citaU.setPacienteTipoDoc(cita.getIdPaciente().getTipoIdentificacion().getDescripcion());
                citaU.setPacienteNumDoc(cita.getIdPaciente().getIdentificacion());
                if (cita.getTipoCita() != null) {
                    citaU.setMotivoConsulta(cita.getTipoCita().getDescripcion());
                }
                int idServicio = cita.getIdServicio().getIdServicio();
                citaU.setServicio(servicioFacade.find(idServicio).getNombreServicio());
                if (cita.getCancelada()) {
                    citaU.setCancelada(true);
                    citaU.setFechaCancelacion(cita.getFechaCancelacion());
                    citaU.setMotivoCancelacion(cita.getMotivoCancelacion().getDescripcion());
                } else {
                    citaU.setCancelada(false);
                    citaU.setFechaCancelacion(null);
                    citaU.setMotivoCancelacion("");
                }

                DecimalFormat dec = new DecimalFormat("###,###,###.00");
                citaU.setValEmpresa(dec.format(paciente.get(i).getValorEmpresa()));
                citaU.setValTotal(dec.format(paciente.get(i).getValorTotal()));
                citaU.setCopago(dec.format(paciente.get(i).getCopago()));
                citaU.setFactura(paciente.get(i).getCodigoDocumento());
                citaU.setAtendida(cita.getAtendida());
                listaCitasU.add(citaU);
                i++;
            }
        }
        return listaCitasU;
    }

    public void generarReporte(ActionEvent actionEvent) throws IOException, JRException {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        //System.out.println(tipoReporte + " - " + fechaInicial + " - " + fechaFinal);
        Map<String, Object> parametros = new HashMap<>();
//        NO FUNCIONA LA RUTA DEL LOGO
        parametros.put("logoEmpresa", logoEmpresa);
        List<CitCitas> list = new ArrayList<>();
        List<String> list_item = new ArrayList<>();
        List<String> list_value = new ArrayList<>();
        List<PypProgramaCita> lista_programas = new ArrayList<>();
        List<FacFacturaPaciente> lista_facturas = new ArrayList<>();
        List<PyPprogramaAsoc> lista_asoc = new ArrayList<>();
        List<Object[]> progra;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        switch (tipoReporte) {
            case "1":
                String periodo;
                if (fechaInicial == null && fechaFinal == null) {
                    periodo = "TODAS";
                } else if (fechaInicial != null && fechaFinal == null) {
                    periodo = "Desde " + dateFormat.format(fechaInicial);
                } else if (fechaInicial == null && fechaFinal != null) {
                    periodo = "Hasta " + dateFormat.format(fechaFinal);
                } else {
                    periodo = dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                }

                parametros.put("title", "PROGRAMAS");
                progra = programasFacade.informe_programas(fechaInicial, fechaFinal, getId_programa());
                if (getId_programa() != 0) {
                    parametros.put("periodo", periodo + ", " + programasFacade.find(getId_programa()).getCodigoPrograma() + " - " + programasFacade.find(getId_programa()).getNombrePrograma());
                } else {
                    parametros.put("periodo", periodo);
                }
                for (Object[] result : progra) {
                    list_item.add(result[0] + "");
                    list_value.add(result[1] + "");
                }
                if (!progra.isEmpty()) {
                    programasActividades(list_item, list_value, parametros, "/programas/reportes/actividades.jasper");
                } else {
                    imprimirMensaje("Error", "Ningun registro encontrado", FacesMessage.SEVERITY_WARN);
                }
                break;
            case "2":
                if (fechaInicial == null && fechaFinal == null) {
                    periodo = "TODAS";
                } else if (fechaInicial != null && fechaFinal == null) {
                    periodo = "Desde " + dateFormat.format(fechaInicial);
                } else if (fechaInicial == null && fechaFinal != null) {
                    periodo = "Hasta " + dateFormat.format(fechaFinal);
                } else {
                    periodo = dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                }
                parametros.put("title", "INFORME PYP PROMOCIÓN Y PREVENCIÓN - PROGRAMAS EJECUTADOS");
                progra = programasFacade.informe_pacientes_programas_agrupado(fechaInicial, fechaFinal, getId_programa());
                if (getId_programa() != 0) {
                    parametros.put("periodo", periodo + ", " + programasFacade.find(getId_programa()).getCodigoPrograma() + " - " + programasFacade.find(getId_programa()).getNombrePrograma());
                } else {
                    parametros.put("periodo", periodo);
                }
                for (Object[] result : progra) {
                    list.add((CitCitas) result[0]);
                    lista_programas.add((PypProgramaCita) result[1]);
                    lista_facturas.add((FacFacturaPaciente) result[2]);
                }
                if (!progra.isEmpty()) {
                    pacientesReport(lista_facturas, list, lista_programas, parametros, "/programas/reportes/citasProgramasEjecutados.jasper");
                } else {
                    imprimirMensaje("Error", "Ningun registro encontrado", FacesMessage.SEVERITY_WARN);
                }

                break;
            case "3":
                if (fechaInicial == null && fechaFinal == null) {
                    periodo = "TODAS";
                } else if (fechaInicial != null && fechaFinal == null) {
                    periodo = "Desde " + dateFormat.format(fechaInicial);
                } else if (fechaInicial == null && fechaFinal != null) {
                    periodo = "Hasta " + dateFormat.format(fechaFinal);
                } else {
                    periodo = dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                }
                parametros.put("periodo", periodo);
                parametros.put("title", "INFORME PYP PROMOCIÓN Y PREVENCIÓN - PACIENTE");
//                if (identificacionPaciente != null) {
                progra = programasFacade.informe_pacientes_programas(fechaInicial, fechaFinal, pacienteHC);
                int i = 0;
                for (Object[] result : progra) {
                    list.add((CitCitas) result[0]);
                    lista_programas.add((PypProgramaCita) result[1]);
                    lista_facturas.add((FacFacturaPaciente) result[2]);
                    i++;
                }
                if (!progra.isEmpty()) {
                    String paciente = list.get(0).getIdPaciente().getPrimerNombre() + " " + list.get(0).getIdPaciente().getSegundoNombre() + " " + list.get(0).getIdPaciente().getPrimerApellido() + " " + list.get(0).getIdPaciente().getSegundoApellido();
                    parametros.put("paciente", paciente);
                    pacientesReport(lista_facturas, list, lista_programas, parametros, "/programas/reportes/citasPacientes.jasper");
                } else {
                    imprimirMensaje("Error", "Ningun registro encontrado", FacesMessage.SEVERITY_WARN);
                }
                break;
            case "4":
                if (fechaInicial == null && fechaFinal == null) {
                    periodo = "TODAS";
                } else if (fechaInicial != null && fechaFinal == null) {
                    periodo = "Desde " + dateFormat.format(fechaInicial);
                } else if (fechaInicial == null && fechaFinal != null) {
                    periodo = "Hasta " + dateFormat.format(fechaFinal);
                } else {
                    periodo = dateFormat.format(fechaInicial) + " - " + dateFormat.format(fechaFinal);
                }
                parametros.put("periodo", periodo);
                parametros.put("title", "INFORME PYP PROMOCIÓN Y PREVENCIÓN - ADMINISTRADORA");
                if (idAdministradora != 0) {
                    progra = programasFacade.informe_pacientes_programas_administradora(fechaInicial, fechaFinal, getIdAdministradora());
                    String v = "";
                    int a = 0;
                    for (Object[] result : progra) {
                        list.add((CitCitas) result[0]);
                        if (a == 0) {
                            v = list.get(a).getIdCita().toString();
                            lista_programas.add((PypProgramaCita) result[1]);
                            lista_asoc.add((PyPprogramaAsoc) result[3]);
                            lista_facturas.add((FacFacturaPaciente) result[4]);
                            a++;
                        } else {
                            if (list.get(a).getIdCita().toString().equals(v)) {
                                list.remove(a);
                            } else {
                                lista_programas.add((PypProgramaCita) result[1]);
                                lista_asoc.add((PyPprogramaAsoc) result[3]);
                                lista_facturas.add((FacFacturaPaciente) result[4]);
                                a++;
                            }
                        }

                    }
                    if (!progra.isEmpty()) {
                        parametros.put("administradora", list.get(0).getIdAdministradora().getCodigoAdministradora() + " - " + list.get(0).getIdAdministradora().getRazonSocial() + ",Contrato: " + contratoFacade.find(lista_asoc.get(0).getIdContrato()).getDescripcion());
                        pacientesReport(lista_facturas, list, lista_programas, parametros, "/programas/reportes/citasAdministradoras.jasper");
                    } else {
                        imprimirMensaje("Error", "Ningun registro encontrado", FacesMessage.SEVERITY_WARN);
                    }
                } else {
                    progra = null;
                }
                break;
            case "5":
                user = pacientesFachada.buscarOrdenado(idAdministradora, fechaInicial, fechaFinal);
                carga_4505(user);
                RequestContext.getCurrentInstance().update("form4505");
                setRenBuscar(true);
//                RequestContext.getCurrentInstance().execute("PF('dlg4505').show();");
                break;
            case "6":
                break;
        }

    }

    public void eventoChange() {
//        imprimirMensaje("Informacion", String.valueOf(estadoAutorizacion), FacesMessage.SEVERITY_INFO);
        setEstadoAutorizacion(estadoAutorizacion);
    }

    public void modificar_datos(ActionEvent actionEvent) {
        if (pacienteSeleccionado4505 != null) {
            u = pacienteSeleccionado4505;
            setRenModificar(true);
            setRenBuscar(false);
            System.out.println(2);
            RequestContext.getCurrentInstance().update("@form");
        } else {
            imprimirMensaje("Información", "Debe seleccionar un elemento de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardar_datos_dataset(ActionEvent actionEvent) throws IOException, JRException {
        post_filtrar.set(pacienteSeleccionado4505.getConsecutivo_registro() - 1, u);
        setRenModificar(false);
        setRenBuscar(true);
    }

    public DatosFormularioHistoria datosForm(HcRegistro registroEncontrado, SimpleDateFormat sdfDateHour, DatosFormularioHistoria datosFormulario) {
        //cargar los datos adicionales a estructuraCampos
        List<HcDetalle> listaDetalles = registroEncontrado.getHcDetalleList();
        for (HcDetalle detalle : listaDetalles) {
            HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
            if (campo != null) {
                if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                    switch (campo.getTabla()) {
                        case "date":
                            try {
                                Date f = sdfDateHour.parse(detalle.getValor());
                                datosFormulario.setValor(campo.getPosicion(), f);
                            } catch (ParseException ex) {
                                datosFormulario.setValor(campo.getPosicion(), "");
                            }
                            break;
                        default://es una categoria
                            datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                            break;
                    }
                } else {//simplemente es texto
                    datosFormulario.setValor(campo.getPosicion(), detalle.getValor());
                }//System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
            } else {
                System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
            }
        }
        return datosFormulario;
    }

    public static JasperDesign jasperDesign;
    public static JasperPrint jasperPrint;
    public static JasperReport jasperReport;
    public static String reportTemplateUrl = "person-template.jrxml";
    @EJB
    HcRegistroFacade registroFacade;
    @EJB
    HcTipoRegFacade tipoRegCliFacade;
    @EJB
    HcCamposRegFacade camposRegFacade;

    public void generarInforme() {
        try {
            FileWriter writer = null;
            SimpleDateFormat file = new SimpleDateFormat("yyyyMMdd");
            String csvFile = "c:/temp/SGD280RPED" + file.format(new Date()) + "NIT000052054575S01.TXT";
            writer = new FileWriter(csvFile);
            for (Informe4505 r : post_filtrar) {
                List<String> lista_csv = new ArrayList<>();
                int i = 0;
                for (Field field : r.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    String name = field.getName();
                    Object value = field.get(r);
                    try {
                        if (value.toString().length() > 9) {
                            value = new SimpleDateFormat("yyyy-MM-dd").format(field.get(r));
                        }
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        value = field.get(r);
                    }
                    lista_csv.add(value.toString().toUpperCase().trim());
                    i++;
                }
                CSVUtils.writeLine(writer, lista_csv, '|');
            }
            post_filtrar.clear();
            setRenBuscar(false);
            writer.flush();
            writer.close();
            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);
        } catch (IOException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ReporteProgramasMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void carga_4505(List<CfgPacientes> user) {

        SimpleDateFormat usuario_nacimiento = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        HcRegistro registroEncontrado = null;
        HcTipoReg tipoRegistroClinicoActual;
        DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();
        int i = 1;
        System.out.println(new Date());
        for (CfgPacientes r : user) {
            try {
                Informe4505 u = new Informe4505();
                u.setTipo_registro("2");
                u.setConsecutivo_registro(i);
                u.setCodigo_habilitacion_ips_primaria("99");
                u.setTipo_identificacion_usuario(r.getTipoIdentificacion().getDescripcion());
                u.setNumero_identificacion_usuario(r.getIdentificacion());
                u.setPrimer_apellido_usuario(r.getPrimerApellido());
                u.setSegundo_apellido_usuario(r.getSegundoApellido());
                u.setPrimer_nombre_usuario(r.getPrimerNombre());
                u.setSegundo_nombre_usuario(r.getSegundoNombre());
                String fecha = r.getFechaNacimiento() + "";
                u.setFecha_nacimiento(usuario_nacimiento.parse(fecha));
                if (r.getGenero() == null) {
                    u.setSexo("M");
                } else {
                    if (r.getGenero().getDescripcion().equals("MASCULINO")) {
                        u.setSexo("M");
                    } else {
                        u.setSexo("F");
                    }
                }

                if (r.getEtnia() == null) {
                    u.setCodigo_pertenencia_etnica("6");
                } else {
                    u.setCodigo_pertenencia_etnica(r.getEtnia().getCodigo());
                }

                if (r.getOcupacion() == null) {
                    u.setCodigo_ocupacion("9999");
                } else {
                    u.setCodigo_ocupacion(r.getOcupacion().getCodigo());
                }
                if (r.getNivel() == null) {
                    u.setCodigo_nivel_educativo("1");
                } else {
                    u.setCodigo_nivel_educativo(r.getNivel().getCodigo());
                }

                if (r.getGestacion() == null) {
                    u.setGestacion("2");
                } else {
                    u.setGestacion(r.getGestacion().getCodigo());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(45);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato53().toString().isEmpty()) {
                    u.setSifilis_gestacional_congenita("4");
                } else {
                    u.setSifilis_gestacional_congenita(datosFormulario.getDato53().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(52);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato76().toString().isEmpty()) {
                    u.setHipertension_gestacional_congenita("3");
                } else {
                    u.setHipertension_gestacional_congenita(datosFormulario.getDato76().toString());
                }
                u.setHipotiroidismo_congenito("3");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(52);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato1().toString().isEmpty()) {
                    u.setSintomatico_respiratorio("2");
                } else {
                    u.setSintomatico_respiratorio(datosFormulario.getDato1().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(52);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato94().toString().isEmpty()) {
                    u.setTuberculosis_multidrogoresistente("3");
                } else {
                    u.setTuberculosis_multidrogoresistente(datosFormulario.getDato94().toString());
                }
                u.setLepra("3");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(54);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato176().toString().isEmpty()) {
                    u.setObesidad_desutricion_proteico_calorica("3");
                } else {
                    u.setObesidad_desutricion_proteico_calorica(datosFormulario.getDato176().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(37);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato15().toString().isEmpty()) {
                    u.setMujer_victima_maltrato("4");
                } else {
                    u.setMujer_victima_maltrato(datosFormulario.getDato15().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(47);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato52().toString().isEmpty()) {
                    u.setVictima_violencia_sexual("3");
                } else {
                    u.setVictima_violencia_sexual(datosFormulario.getDato52().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(37);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato108().toString().isEmpty()) {
                    u.setInfecciones_transmision_sexual("3");
                } else {
                    u.setInfecciones_transmision_sexual(datosFormulario.getDato108().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(49);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato26().toString().isEmpty()) {
                    u.setEnfermedad_salud_mental("7");
                } else {
                    u.setEnfermedad_salud_mental(datosFormulario.getDato26().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(54);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato37().toString().isEmpty()) {
                    u.setCancer_cervix("3");
                } else {
                    u.setCancer_cervix(datosFormulario.getDato37().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(73);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato3().toString().isEmpty()) {
                    u.setCancer_seno("3");
                } else {
                    u.setCancer_seno(datosFormulario.getDato3().toString());
                }
                u.setFluorosis_dental("3");
                u.setFecha_peso(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(11);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato28().toString().isEmpty()) {
                    u.setPeso_kilogramos("999");
                } else {
                    u.setPeso_kilogramos(datosFormulario.getDato28().toString());
                }
                u.setFecha_talla(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(11);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato29().toString().isEmpty()) {
                    u.setTalla_metros("999");
                } else {
                    u.setTalla_metros(datosFormulario.getDato29().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(45);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato8().toString().isEmpty()) {
                    u.setFecha_probable_parto(sdf.parse("1800-01-01"));
                } else {
                    u.setFecha_probable_parto(new Date(usuario_nacimiento.parse(datosFormulario.getDato8().toString()).toString()));
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(45);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato9().toString().isEmpty()) {
                    u.setEdad_gestacional_nacer("99");
                } else {
                    u.setEdad_gestacional_nacer(datosFormulario.getDato9().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato172().toString().isEmpty()) {
                    u.setBcg("2");
                } else {
                    u.setBcg(datosFormulario.getDato172().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato173().toString().isEmpty()) {
                    u.setHepatitis_b_menores_1_año("4");
                } else {
                    u.setHepatitis_b_menores_1_año(datosFormulario.getDato173().toString());
                    if (!datosFormulario.getDato174().toString().isEmpty()) {
                        u.setHepatitis_b_menores_1_año(datosFormulario.getDato174().toString());
                        if (!datosFormulario.getDato175().toString().isEmpty()) {
                            u.setHepatitis_b_menores_1_año(datosFormulario.getDato175().toString());
                            if (!datosFormulario.getDato176().toString().isEmpty()) {
                                u.setHepatitis_b_menores_1_año(datosFormulario.getDato176().toString());
                            }
                        }
                    }
                }
                u.setPentavalente("3");
                u.setPolio("5");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato177().toString().isEmpty()) {
                    u.setDpt_menores_5_años("5");
                } else {
                    u.setDpt_menores_5_años(datosFormulario.getDato177().toString());
                    if (!datosFormulario.getDato178().toString().isEmpty()) {
                        u.setDpt_menores_5_años(datosFormulario.getDato178().toString());
                        if (!datosFormulario.getDato179().toString().isEmpty()) {
                            u.setDpt_menores_5_años(datosFormulario.getDato179().toString());
                            if (!datosFormulario.getDato180().toString().isEmpty()) {
                                u.setDpt_menores_5_años(datosFormulario.getDato180().toString());
                                if (!datosFormulario.getDato181().toString().isEmpty()) {
                                    u.setDpt_menores_5_años(datosFormulario.getDato181().toString());
                                }
                            }
                        }
                    }
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato190().toString().isEmpty()) {
                    u.setDpt_menores_5_años("2");
                } else {
                    u.setDpt_menores_5_años(datosFormulario.getDato190().toString());
                    if (!datosFormulario.getDato191().toString().isEmpty()) {
                        u.setDpt_menores_5_años(datosFormulario.getDato191().toString());
                    }
                }
                u.setRotavirus("2");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato192().toString().isEmpty()) {
                    u.setNeumococo("3");
                } else {
                    u.setNeumococo(datosFormulario.getDato192().toString());
                    if (!datosFormulario.getDato193().toString().isEmpty()) {
                        u.setNeumococo(datosFormulario.getDato193().toString());
                        if (!datosFormulario.getDato194().toString().isEmpty()) {
                            u.setNeumococo(datosFormulario.getDato194().toString());
                        }
                    }
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato187().toString().isEmpty()) {
                    u.setInfluenza_niños("3");
                } else {
                    u.setInfluenza_niños(datosFormulario.getDato197().toString());
                    if (!datosFormulario.getDato188().toString().isEmpty()) {
                        u.setInfluenza_niños(datosFormulario.getDato188().toString());
                        if (!datosFormulario.getDato189().toString().isEmpty()) {
                            u.setInfluenza_niños(datosFormulario.getDato189().toString());
                        }
                    }
                }
                u.setFiebre_amarilla_niños_1_año("1");
                u.setHepatitis_a("1");
                u.setTriple_viral_niños("2");
                u.setVirus_papiloma_humano("3");
                u.setTd_tt_mujeres_edad_fertil_15_49_años("5");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(35);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato106().toString().isEmpty()) {
                    u.setControl_placa_bacteriana("7");
                } else {
                    u.setControl_placa_bacteriana(datosFormulario.getDato106().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(45);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato19().toString().isEmpty()) {
                    u.setFecha_atencion_parto_cesarea(sdf.parse("1800-01-01"));
                } else {
                    u.setFecha_atencion_parto_cesarea(new Date(usuario_nacimiento.parse(datosFormulario.getDato19().toString()).toString()));
                }
                u.setFecha_salida_atencion_parto_cesarea(sdf.parse("1800-01-01"));
                u.setFecha_consejeria_lactancia_materna(sdf.parse("1800-01-01"));
                u.setControl_recien_nacido(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(41);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato0().toString().isEmpty()) {
                    u.setPlanificacion_familiar_primera_vez(sdf.parse("1800-01-01"));
                } else {
                    u.setPlanificacion_familiar_primera_vez(new Date(usuario_nacimiento.parse(datosFormulario.getDato0().toString()).toString()));
                }
                u.setSuministro_metodo_anticonceptivo("15");
                u.setFecha_suministro_medico_anticonceptivo(sdf.parse("1800-01-01"));
                u.setControl_prenatal_primera_vez(sdf.parse("1800-01-01"));
                u.setControl_prenatal("999");
                u.setUltimo_control_prenatal(sdf.parse("1800-01-01"));
                u.setSuministro_acido_folico_ultimo_control_prenatal("5");
                u.setSuministro_sulfato_ferroso_ultimo_control_prenatal("5");
                u.setSuministro_carbonato_calcio_ultimo_control_prenatal("5");
                u.setValoracion_agudeza_visual(sdf.parse("1800-01-01"));
                u.setConsulta_oftalmologia(sdf.parse("1800-01-01"));
                u.setFecha_diagnostico_desnutricion_proteico_calorica(sdf.parse("1800-01-01"));
                u.setConsulta_mujero_menor_maltratado(sdf.parse("1800-01-01"));
                u.setConsulta_victimas_violencia_sexual(sdf.parse("1800-01-01"));
                u.setConsulta_nutricion(sdf.parse("1800-01-01"));
                u.setConsulta_psicologica(sdf.parse("1800-01-01"));
                u.setConsulta_crecimiento_desarrollo_primera_vez(sdf.parse("1800-01-01"));
                u.setSuministro_sulfato_ferroso_ultima_consulta_menor_10_años("5");
                u.setSuministro_vitamina_a_ultima_consulta_menor_10_años("5");
                u.setConsulta_joven_primera_vez(sdf.parse("1800-01-01"));
                u.setConsulta_adulto_primera_vez(sdf.parse("1800-01-01"));
                u.setPreservativos_entregados_pacientes_con_its("999");
                u.setAsesoria_pre_test_elisa_vih(sdf.parse("1800-01-01"));
                u.setAsesoria_pos_test_elisa_vih(sdf.parse("1800-01-01"));
                u.setPaciente_diagnostico_salud_mental_recibio_atencion_ultimos_6_meses_equipo_interdisciplinario_completo("6");
                u.setFecha_antigeno_superficie_hepatitis_b_gestantes(sdf.parse("1800-01-01"));
                u.setResultado_antigeno_superficie_hepatitis_b_gestantes("2");
                u.setFecha_serologia_sifilis(sdf.parse("1800-01-01"));
                u.setResultado_serologia_sifilis("2");
                u.setFecha_toma_elisa_vih(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(52);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato172().toString().isEmpty()) {
                    u.setResultado_elisa_vih("3");
                } else {
                    u.setResultado_elisa_vih(datosFormulario.getDato172().toString());
                }
                u.setFecha_tsh_neonatal(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(51);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato51().toString().isEmpty()) {
                    u.setResultado_tsh_neonatal("2");
                } else {
                    u.setResultado_tsh_neonatal(datosFormulario.getDato51().toString());
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(59);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato18().toString().isEmpty()) {
                    u.setTamizaje_cancer_cuello_uterino("8");
                } else {
                    u.setTamizaje_cancer_cuello_uterino(datosFormulario.getDato18().toString());
                }
                u.setCitologia_cervico_uterina(sdf.parse("1800-01-01"));
                u.setCitologia_cervico_uterina_resultados_bethesda("99");
                u.setCalidad_muestra_citologia_cervicouterina("99");
                u.setCodigo_habilitacion_ips_donde_toma_citologia_cervicouterina("99");
                u.setFecha_colposcopia(sdf.parse("1800-01-01"));
                u.setCodigo_habilitacion_ips_donde_toma_colposcopia("99");
                u.setFecha_biopsia_cervical(sdf.parse("1800-01-01"));
                u.setResultado_biopsia_cervical("99");
                u.setCodigo_habilitacion_ips_donde_toma_biopsia_cervical("99");
                u.setFecha_mamografia(sdf.parse("1800-01-01"));
                tipoRegistroClinicoActual = tipoRegCliFacade.find(59);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato102().toString().isEmpty()) {
                    u.setResultado_mamografia("99");
                } else {
                    u.setResultado_mamografia(datosFormulario.getDato102().toString());
                }
                u.setCodigo_habilitacion_ips_donde_toma_mamografia("99");
                u.setFecha_toma_biopsia_seno_bacaf(sdf.parse("1800-01-01"));
                u.setFecha_resultado_biopsia_seno_bacaf(sdf.parse("1800-01-01"));
                u.setBiopsia_seno_bacaf("99");
                u.setCodigo_habilitacion_ips_donde_toma_biopsia_seno_bacaf("99");
                u.setFecha_toma_hemoglobina(sdf.parse("1800-01-01"));
                u.setHemoglobina("9998");
                u.setFecha_toma_glicemia_basal(sdf.parse("1800-01-01"));
                u.setFecha_creatinina(sdf.parse("1800-01-01"));
                u.setCreatinina("999");
                u.setFecha_hemoglobina_glicosilada(sdf.parse("1800-01-01"));
                u.setHemoglobina_glicosilada("999");
                tipoRegistroClinicoActual = tipoRegCliFacade.find(54);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato211().toString().isEmpty()) {
                    u.setFecha_toma_microalbuminuria(sdf.parse("1800-01-01"));
                } else {
                    u.setFecha_toma_microalbuminuria(new Date(usuario_nacimiento.parse(datosFormulario.getDato211().toString()).toString()));
                }
                tipoRegistroClinicoActual = tipoRegCliFacade.find(54);
                registroEncontrado = registroFacade.buscarUltimo(r.getIdentificacion(), tipoRegistroClinicoActual.getIdTipoReg());
                if (registroEncontrado != null) {
                    datosFormulario = datosForm(registroEncontrado, sdfDateHour, datosFormulario);
                }
                if (datosFormulario.getDato199().toString().isEmpty()) {
                    u.setFecha_toma_hdl(sdf.parse("1800-01-01"));
                } else {
                    u.setFecha_toma_hdl(new Date(usuario_nacimiento.parse(datosFormulario.getDato199().toString()).toString()));
                }
                u.setFecha_toma_baciloscopia_diagnostico(sdf.parse("1800-01-01"));
                u.setBaciloscopia_diagnostico("3");
                u.setTratamiento_hipotiroidismo_congenito("6");
                u.setTratamiento_sifilis_gestacional("6");
                u.setTratamiento_sifilis_congenita("6");
                u.setTratamiento_terminado_lepra("7");
                u.setFecha_terminacion_tratamiento_leishmaniasis(sdf.parse("1800-01-01"));
                post_filtrar.add(u);
                i++;
            } catch (ParseException | IllegalArgumentException ex) {
                Logger.getLogger(ReporteProgramasMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(new Date());

    }

    public void export(List<String> valores, Map<String, Object> parametros, String path) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath(path);
            JRCsvExporter exporter = new JRCsvExporter();

            jasperPrint = JasperFillManager.fillReport(ruta, parametros,
                    new JRBeanCollectionDataSource(
                            valores));
            String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss'.csv'").format(new Date());
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File("c:/temp/" + fileName));
            exporter.exportReport();
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    private void citasReport(List<CitCitas> citas, List<PypProgramaCita> programas, Map<String, Object> parametros, String path) throws IOException, JRException {

        List<CitaU> citasU = generarCitasAuxiliar(citas, programas);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath(path);
        try {
            String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss'.xlsx'").format(new Date());
            if (!new File("c:/temp/").exists()) {
                new File("c:/temp/").mkdir();
            }

            OutputStream outputfile = new FileOutputStream(new File("c:/temp/" + fileName));
            jasperPrint = JasperFillManager.fillReport(ruta, parametros,
                    new JRBeanCollectionDataSource(
                            citasU));
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "xxx.xlsx");
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            xlsxExporter.exportReport();
            outputfile.write(os.toByteArray());
            outputfile.flush();
            outputfile.close();

            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);

            download("c:/temp/" + fileName, fileName);
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    private void pacientesReport(List<FacFacturaPaciente> paciente, List<CitCitas> citas, List<PypProgramaCita> programas, Map<String, Object> parametros, String path) throws IOException, JRException {

        List<CitaU> citasU = generarCitasAuxiliar(citas, programas, paciente);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath(path);
        try {
            String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss'.xlsx'").format(new Date());
            if (!new File("c:/temp/").exists()) {
                new File("c:/temp/").mkdir();
            }

            OutputStream outputfile = new FileOutputStream(new File("c:/temp/" + fileName));
            jasperPrint = JasperFillManager.fillReport(ruta, parametros,
                    new JRBeanCollectionDataSource(
                            citasU));
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "xxx.xlsx");
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            xlsxExporter.exportReport();
            outputfile.write(os.toByteArray());
            outputfile.flush();
            outputfile.close();

            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);

            download("c:/temp/" + fileName, fileName);
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    public void download(String ruta, String filename) throws IOException {

        File file = new File(ruta);

        FacesContext facesContext = FacesContext.getCurrentInstance();

        HttpServletResponse response
                = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        response.reset();
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);

        OutputStream responseOutputStream = response.getOutputStream();

        InputStream fileInputStream = new FileInputStream(file);

        byte[] bytesBuffer = new byte[2048];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(bytesBuffer)) > 0) {
            responseOutputStream.write(bytesBuffer, 0, bytesRead);
        }

        responseOutputStream.flush();

        fileInputStream.close();
        responseOutputStream.close();

        facesContext.responseComplete();

    }

    private void programasActividades(List<String> citas, List<String> value, Map<String, Object> parametros, String path) throws IOException, JRException {
        List<CitaU> listaCitasU = new ArrayList();
        int i = 0;
        for (String cit : citas) {
            CitaU citaU = new CitaU();
            citaU.setValue(value.get(i));
            citaU.setActividad(cit);
            listaCitasU.add(citaU);
            i++;
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath(path);
        try {
            String fileName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss'.xlsx'").format(new Date());
            if (!new File("c:/temp/").exists()) {
                new File("c:/temp/").mkdir();
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            OutputStream outputfile = new FileOutputStream(new File("c:/temp/" + fileName));
            jasperPrint = JasperFillManager.fillReport(ruta, parametros,
                    new JRBeanCollectionDataSource(
                            listaCitasU));
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            xlsxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "xxx.xlsx");
            xlsxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
            xlsxExporter.exportReport();
            outputfile.write(os.toByteArray());
            outputfile.flush();
            outputfile.close();

            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);

            download("c:/temp/" + fileName, fileName);
        } catch (JRException e) {
            System.out.println(e);
        }
    }

    public void addPrestadorFiltro() {
        listaPrestadores.add(prestadorSeleccionado);
    }

    public void addPacienteFiltro() {
        listaPacientes.add(pacienteSeleccionado);
        if (tipoReporte.equals("6")) {
            RequestContext.getCurrentInstance().update("formfiltradoAutorizaciones");
        } else {
            RequestContext.getCurrentInstance().update("formfiltrado");
        }
    }

    public void addAdministradoraFiltro() {
        if (tipoReporte.equals("6")) {
            RequestContext.getCurrentInstance().update("formfiltradoAutorizaciones");
        } else {
            RequestContext.getCurrentInstance().update("formfiltrado");
        }
    }

    public void limpiarFiltros() {
        listaAdministradoras.clear();
        listaPacientes.clear();
        listaPrestadores.clear();
    }

    public void limpiarFiltrosDos() {
        listaAdministradoras.clear();
        listaPacientes.clear();
        setNumAutorizacion(null);
        setEstadoAutorizacion(0);
    }

//---------------------------------------------------------------------
//------------------------GETTERS AND SETTERS -------------------------
//---------------------------------------------------------------------
    /**
     * @return the tipoReporte
     */
    public String getTipoReporte() {
        return tipoReporte;
    }

    /**
     * @param tipoReporte the tipoReporte to set
     */
    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * @return the fechaInicial
     */
    public Date getFechaInicial() {
        return fechaInicial;
    }

    /**
     * @param fechaInicial the fechaInicial to set
     */
    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public List<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public List<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<SelectItem> getListaAdministradoras() {
        return listaAdministradoras;
    }

    public void setListaAdministradoras(List<SelectItem> listaAdministradoras) {
        this.listaAdministradoras = listaAdministradoras;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public FacAdministradora getAdministradoraSeleccionada() {
        return administradoraSeleccionada;
    }

    public void setAdministradoraSeleccionada(FacAdministradora administradoraSeleccionada) {
        this.administradoraSeleccionada = administradoraSeleccionada;
    }

    public LazyDataModel<CfgUsuarios> getPrestadores() {
        return prestadores;
    }

    public void setPrestadores(LazyDataModel<CfgUsuarios> prestadores) {
        this.prestadores = prestadores;
    }

    public LazyDataModel<CfgPacientes> getPacientes() {
        return pacientes;
    }

    public void setPacientes(LazyDataModel<CfgPacientes> pacientes) {
        this.pacientes = pacientes;
    }

    public List<SelectItem> getListaReportes() {
        return listaReportes;
    }

    public void setListaReportes(List<SelectItem> listaReportes) {
        this.listaReportes = listaReportes;
    }

    public boolean isRenBtnFiltrar() {
        return renBtnFiltrar;
    }

    public void setRenBtnFiltrar(boolean renBtnFiltrar) {
        this.renBtnFiltrar = renBtnFiltrar;
    }

    public String getDisplayBusquedaPaciente() {
        return displayBusquedaPaciente;
    }

    public void setDisplayBusquedaPaciente(String displayBusquedaPaciente) {
        this.displayBusquedaPaciente = displayBusquedaPaciente;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public boolean isRenBtnReporte() {
        return renBtnReporte;
    }

    public void setRenBtnReporte(boolean renBtnReporte) {
        this.renBtnReporte = renBtnReporte;
    }

    public CfgPacientes getPacienteHC() {
        return pacienteHC;
    }

    public void setPacienteHC(CfgPacientes pacienteHC) {
        this.pacienteHC = pacienteHC;
    }

    public LazyDataModel<CfgPacientes> getListaPacientesBusqueda() {
        return listaPacientesBusqueda;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public void setListaPacientesBusqueda(LazyDataModel<CfgPacientes> listaPacientesBusqueda) {
        this.listaPacientesBusqueda = listaPacientesBusqueda;
    }

    public int getEstadoAutorizacion() {
        return estadoAutorizacion;
    }

    public void setEstadoAutorizacion(int estadoAutorizacion) {
        this.estadoAutorizacion = estadoAutorizacion;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public List<SelectItem> getListaEstadoAutorizacion() {
        return listaEstadoAutorizacion;
    }

    public void setListaEstadoAutorizacion(List<SelectItem> listaEstadoAutorizacion) {
        this.listaEstadoAutorizacion = listaEstadoAutorizacion;
    }

    public boolean isRenBtnAdministradora() {
        return renBtnAdministradora;
    }

    public void setRenBtnAdministradora(boolean renBtnAdministradora) {
        this.renBtnAdministradora = renBtnAdministradora;
    }

    public int getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(int idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public boolean isRenBtnPaciente() {
        return renBtnPaciente;
    }

    public void setRenBtnPaciente(boolean renBtnPaciente) {
        this.renBtnPaciente = renBtnPaciente;
    }

    public boolean isRenFechas() {
        return renFechas;
    }

    public void setRenFechas(boolean renFechas) {
        this.renFechas = renFechas;
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public List<SelectItem> getListaprograma() {
        return listaprograma;
    }

    public void setListaprograma(List<SelectItem> listaprograma) {
        this.listaprograma = listaprograma;
    }

    public int getId_programa() {
        return id_programa;
    }

    public void setId_programa(int id_programa) {
        this.id_programa = id_programa;
    }

    public boolean isRenProgramas() {
        return renProgramas;
    }

    public void setRenProgramas(boolean renProgramas) {
        this.renProgramas = renProgramas;
    }

    public boolean isRenBtcontrato() {
        return renBtcontrato;
    }

    public void setRenBtcontrato(boolean renBtcontrato) {
        this.renBtcontrato = renBtcontrato;
    }

    public List<CfgPacientes> getUser() {
        return user;
    }

    public void setUser(List<CfgPacientes> user) {
        this.user = user;
    }

    public Informe4505 getPacienteSeleccionado4505() {
        return pacienteSeleccionado4505;
    }

    public void setPacienteSeleccionado4505(Informe4505 pacienteSeleccionado4505) {
        this.pacienteSeleccionado4505 = pacienteSeleccionado4505;
    }

    public List<Informe4505> getPost_filtrar() {
        return post_filtrar;
    }

    public void setPost_filtrar(List<Informe4505> post_filtrar) {
        this.post_filtrar = post_filtrar;
    }

    public boolean isRenModificar() {
        return renModificar;
    }

    public void setRenModificar(boolean renModificar) {
        this.renModificar = renModificar;
    }

    public Informe4505 getU() {
        return u;
    }

    public void setU(Informe4505 u) {
        this.u = u;
    }

    public boolean isRenBuscar() {
        return renBuscar;
    }

    public void setRenBuscar(boolean renBuscar) {
        this.renBuscar = renBuscar;
    }

    public StreamedContent getFile() {
        return file;
    }
}
