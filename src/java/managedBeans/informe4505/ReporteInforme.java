/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informe4505;

import beans.utilidades.AutorizacionReport;
import beans.utilidades.CitaU;
import beans.utilidades.Informe4505;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyPrestadorDataModel;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.Oportunidad;
import java.io.BufferedInputStream;
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
import managedBeans.historias.DatosFormularioHistoria;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcTipoReg;
import modelo.entidades.PyPProgramaItem;
import modelo.entidades.PyPprograma;
import modelo.entidades.PyPprogramaAsoc;
import modelo.entidades.PypProgramaCita;
import modelo.fachadas.CfgClasificacionesFacade;
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
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Mario
 */
@ManagedBean(name = "reporteInforme")
@ViewScoped
public class ReporteInforme extends MetodosGenerales implements Serializable {

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
    private List<CfgClasificaciones> getDes;
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

    @EJB
    CfgClasificacionesFacade clasificaciones;

    public ReporteInforme() {
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

    public String getDescripcion(int maestro, String valor) {
        String ma = "";
        try {
            switch (maestro) {
                case 1:
                    ma = "Etnia";
                    break;
                case 2:
                    ma = "Ocupacion";
                    break;
                case 3:
                    ma = "Escolaridad";
                    break;
                case 4:
                    ma = "Gestacion";
                    break;
            }
            List<CfgClasificaciones> des = clasificaciones.buscarPorMaestroDescripcionCodigo(ma, valor);
            return des.get(0).getDescripcion();
        } catch (Exception e) {
            return "";
        }
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
        List<PyPprogramaAsoc> lista_asoc = new ArrayList<>();
        List<Object[]> progra;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        /*
       CASC 20180407
        Antes consultaba fecha inicio y fecha fin para filtrar pacientes por 
        fecha de nacimiento. Ahora se va a cambiar para que consulte los que 
        tengan atenciones hc_registro entre esas fechas
        user = pacientesFachada.buscarOrdenado(idAdministradora, fechaInicial, fechaFinal);
         */
        try {
            post_filtrar = new ArrayList<>();
            user = pacientesFachada.buscarPacientesAtendidosAdministradora(idAdministradora, fechaInicial, fechaFinal);
            if (user == null || user.isEmpty()) {
                imprimirMensaje("Error", "No se Consiguieron pacientes", FacesMessage.SEVERITY_ERROR);
                return;
            }
            carga_4505(user);
            RequestContext.getCurrentInstance().update("form4505");
            setRenBuscar(true);
        } catch (Exception ex) {
            imprimirMensaje("Error", ex.getMessage(), FacesMessage.SEVERITY_ERROR);
            return;
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

    public void generarInforme() throws IOException, IllegalArgumentException, IllegalAccessException {
        SimpleDateFormat file_ = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String csvFile = "SGD280RPED" + file_.format(fechaFinal) + "NIT000052054575S01.TXT";
        FileWriter writer = new FileWriter(csvFile);
        String valor = "";
        
        // Generar registro tipo 1. Primer registro del archivo
        valor += "1";//tipo de registro
        valor += "|";
        valor += "";//código de la EPS o de la Dirección Territorial de Salud 
        valor += "|";
        valor += sdf.format(fechaInicial);//fecha inicial del período de la información reportada 
        valor += "|";
        valor += sdf.format(fechaFinal);//fecha final del período de la información reportada 
        valor += "|";
        valor += post_filtrar.size();//cantidad de registros tipo 2
        
        writer.append(valor);
        writer.append("\n");
        
        // Generar registros tipo 2.
        for (Informe4505 r : post_filtrar) {
            valor = "2" + "|";//tipo de registro
            valor += r.getConsecutivo_registro()  + "|";

            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_primaria()))
                valor += r.getCodigo_habilitacion_ips_primaria() + "|";
            else
                valor += 99 + "|";
            
            valor += r.getTipo_identificacion_usuario().substring(0, Math.min(r.getTipo_identificacion_usuario().length(), 2)) + "|";
            
            valor += r.getNumero_identificacion_usuario().substring(0, Math.min(r.getNumero_identificacion_usuario().length(), 18)) + "|";
            
            valor += r.getPrimer_apellido_usuario().substring(0, Math.min(r.getPrimer_apellido_usuario().length(), 30)) + "|";
            
            if (StringUtils.isAnyBlank(r.getSegundo_apellido_usuario()))
                valor += "NONE" + "|";
            else
                valor += r.getSegundo_apellido_usuario().substring(0, Math.min(r.getSegundo_apellido_usuario().length(), 30)) + "|";
            
            valor += r.getPrimer_nombre_usuario().substring(0, Math.min(r.getPrimer_nombre_usuario().length(), 30)) + "|";
            
            if (StringUtils.isAnyBlank(r.getSegundo_nombre_usuario()))
                valor += "NONE" + "|";
            else
                valor += r.getSegundo_nombre_usuario().substring(0, Math.min(r.getSegundo_nombre_usuario().length(), 30)) + "|";
            
            valor += sdf.format(r.getFecha_nacimiento()) + "|";//campo 9 (fecha nacimiento)
            
            valor += r.getSexo().substring(0, Math.min(r.getSexo().length(), 1)) + "|";//campo 10 (sexo)
            
            if (StringUtils.isNumeric(r.getCodigo_pertenencia_etnica()))
                valor += r.getCodigo_pertenencia_etnica() + "|";
            else
                valor += 6 + "|";
            
            if (StringUtils.isNumeric(r.getCodigo_ocupacion()))
                valor += r.getCodigo_ocupacion() + "|";
            else
                valor += "9999" + "|";
            
            if (StringUtils.isNumeric(r.getCodigo_nivel_educativo()))
                valor += r.getCodigo_nivel_educativo() + "|";
            else
                valor += "1" + "|";//No Definido
            
            if (StringUtils.isNumeric(r.getGestacion()))
                valor += r.getGestacion() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getSifilis_gestacional_congenita()))
                valor += r.getSifilis_gestacional_congenita() + "|";
            else
                valor += "4" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getHipertension_gestacional_congenita()))
                valor += r.getHipertension_gestacional_congenita() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getHipotiroidismo_congenito()))
                valor += r.getHipotiroidismo_congenito() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getSintomatico_respiratorio()))
                valor += r.getSintomatico_respiratorio() + "|";
            else
                valor += "2" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getTuberculosis_multidrogoresistente()))
                valor += r.getTuberculosis_multidrogoresistente() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getLepra()))
                valor += r.getLepra() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            //campo 20 (Lepra)
            if (StringUtils.isNumeric(r.getLepra()))
                valor += r.getLepra() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getObesidad_desutricion_proteico_calorica()))
                valor += r.getObesidad_desutricion_proteico_calorica() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getMujer_victima_maltrato()))
                valor += r.getMujer_victima_maltrato() + "|";
            else
                valor += "4" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getVictima_violencia_sexual()))
                valor += r.getVictima_violencia_sexual() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getInfecciones_transmision_sexual()))
                valor += r.getInfecciones_transmision_sexual() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getEnfermedad_salud_mental()))
                valor += r.getEnfermedad_salud_mental() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getCancer_cervix()))
                valor += r.getCancer_cervix() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getCancer_seno()))
                valor += r.getCancer_seno() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (StringUtils.isNumeric(r.getFluorosis_dental()))
                valor += r.getFluorosis_dental() + "|";
            else
                valor += "3" + "|";//Riesgo no evaluado
            
            if (null != r.getFecha_peso())
                valor += sdf.format(r.getFecha_peso()) + "|";//campo 29 (fecha del peso)
            else
                valor += "1800-01-01" + "|";
            
            //campo 30 (Peso en Kilogramos)
            if (StringUtils.isNumeric(r.getPeso_kilogramos()))
                valor += r.getPeso_kilogramos() + "|";
            else
                valor += "999" + "|";
            
            if (null != r.getFecha_talla())
                valor += sdf.format(r.getFecha_talla()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getTalla_metros()))
                valor += r.getTalla_metros() + "|";
            else
                valor += "999" + "|";
            
            if (null != r.getFecha_probable_parto())
                valor += sdf.format(r.getFecha_probable_parto()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getEdad_gestacional_nacer()))
                valor += r.getEdad_gestacional_nacer() + "|";
            else
                valor += "99" + "|";
            
            if (StringUtils.isNumeric(r.getEdad_gestacional_nacer()))
                valor += r.getEdad_gestacional_nacer() + "|";
            else
                valor += "99" + "|";
            
            //campo 35 (BCG)
            if (StringUtils.isNumeric(r.getBcg()))
                valor += r.getBcg() + "|";
            else
                valor += "2" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getHepatitis_b_menores_1_año()))
                valor += r.getHepatitis_b_menores_1_año() + "|";
            else
                valor += "4" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getPentavalente()))
                valor += r.getPentavalente() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getPolio()))
                valor += r.getPolio() + "|";
            else
                valor += "5" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getDpt_menores_5_años()))
                valor += r.getDpt_menores_5_años() + "|";
            else
                valor += "5" + "|";//Sin dato
            
            //campo 40 (Rotavirus)
            if (StringUtils.isNumeric(r.getRotavirus()))
                valor += r.getRotavirus() + "|";
            else
                valor += "2" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getNeumococo()))
                valor += r.getNeumococo() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getInfluenza_niños()))
                valor += r.getInfluenza_niños() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getFiebre_amarilla_niños_1_año()))
                valor += r.getFiebre_amarilla_niños_1_año() + "|";
            else
                valor += "1" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getHepatitis_a()))
                valor += r.getHepatitis_a() + "|";
            else
                valor += "1" + "|";//Sin dato
            
            //campo 45 (Triple Viral Niños)
            if (StringUtils.isNumeric(r.getTriple_viral_niños()))
                valor += r.getTriple_viral_niños() + "|";
            else
                valor += "2" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getVirus_papiloma_humano()))
                valor += r.getVirus_papiloma_humano() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getTd_tt_mujeres_edad_fertil_15_49_años()))
                valor += r.getTd_tt_mujeres_edad_fertil_15_49_años() + "|";
            else
                valor += "5" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getControl_placa_bacteriana()))
                valor += r.getControl_placa_bacteriana() + "|";
            else
                valor += "7" + "|";//Sin dato
            
            //campo 49 (Fecha atención parto o cesárea)
            if (null != r.getFecha_atencion_parto_cesarea())
                valor += sdf.format(r.getFecha_atencion_parto_cesarea()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            //campo 50 (Fecha salida de la atención del parto o cesárea)
            if (null != r.getFecha_salida_atencion_parto_cesarea())
                valor += sdf.format(r.getFecha_salida_atencion_parto_cesarea()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_consejeria_lactancia_materna())
                valor += sdf.format(r.getFecha_consejeria_lactancia_materna()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getControl_recien_nacido())
                valor += sdf.format(r.getControl_recien_nacido()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getPlanificacion_familiar_primera_vez())
                valor += sdf.format(r.getPlanificacion_familiar_primera_vez()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getSuministro_metodo_anticonceptivo())
                valor += sdf.format(r.getSuministro_metodo_anticonceptivo()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_suministro_medico_anticonceptivo())
                valor += sdf.format(r.getFecha_suministro_medico_anticonceptivo()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            //campo 55 (Fecha Suministro de Método Anticonceptivo)
            if (null != r.getFecha_suministro_medico_anticonceptivo())
                valor += sdf.format(r.getFecha_suministro_medico_anticonceptivo()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getControl_prenatal_primera_vez())
                valor += sdf.format(r.getControl_prenatal_primera_vez()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getControl_prenatal()))
                valor += r.getControl_prenatal() + "|";
            else
                valor += "999" + "|";//Sin dato
            
            if (null != r.getUltimo_control_prenatal())
                valor += sdf.format(r.getUltimo_control_prenatal()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getSuministro_acido_folico_ultimo_control_prenatal()))
                valor += r.getSuministro_acido_folico_ultimo_control_prenatal() + "|";
            else
                valor += "5" + "|";//Registro no Evaluado
            
            //campo 60 (Suministro de Sulfato Ferroso en el Último Control Prenatal)
            if (StringUtils.isNumeric(r.getSuministro_sulfato_ferroso_ultimo_control_prenatal()))
                valor += r.getSuministro_sulfato_ferroso_ultimo_control_prenatal() + "|";
            else
                valor += "5" + "|";//Registro no Evaluado
            
            if (StringUtils.isNumeric(r.getSuministro_carbonato_calcio_ultimo_control_prenatal()))
                valor += r.getSuministro_carbonato_calcio_ultimo_control_prenatal() + "|";
            else
                valor += "5" + "|";//Registro no Evaluado
            
            if (null != r.getValoracion_agudeza_visual())
                valor += sdf.format(r.getValoracion_agudeza_visual()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_oftalmologia())
                valor += sdf.format(r.getConsulta_oftalmologia()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_diagnostico_desnutricion_proteico_calorica())
                valor += sdf.format(r.getFecha_diagnostico_desnutricion_proteico_calorica()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_mujero_menor_maltratado())
                valor += sdf.format(r.getConsulta_mujero_menor_maltratado()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_victimas_violencia_sexual())
                valor += sdf.format(r.getConsulta_victimas_violencia_sexual()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_nutricion())
                valor += sdf.format(r.getConsulta_nutricion()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_psicologica())
                valor += sdf.format(r.getConsulta_psicologica()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_crecimiento_desarrollo_primera_vez())
                valor += sdf.format(r.getConsulta_crecimiento_desarrollo_primera_vez()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            //campo 70 (Suministro de Sulfato Ferroso en la Última Consulta del Menor de 10 años)
            if (StringUtils.isNumeric(r.getSuministro_sulfato_ferroso_ultima_consulta_menor_10_años()))
                valor += r.getSuministro_sulfato_ferroso_ultima_consulta_menor_10_años() + "|";
            else
                valor += "5" + "|";//Registro no Evaluado
            
            if (StringUtils.isNumeric(r.getSuministro_vitamina_a_ultima_consulta_menor_10_años()))
                valor += r.getSuministro_vitamina_a_ultima_consulta_menor_10_años() + "|";
            else
                valor += "5" + "|";//Registro no Evaluado
            
            if (null != r.getConsulta_joven_primera_vez())
                valor += sdf.format(r.getConsulta_joven_primera_vez()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getConsulta_adulto_primera_vez())
                valor += sdf.format(r.getConsulta_adulto_primera_vez()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getPreservativos_entregados_pacientes_con_its()))
                valor += r.getPreservativos_entregados_pacientes_con_its() + "|";
            else
                valor += "999" + "|";//Sin dato
            
            //campo 75 (Asesoría Pre test Elisa para VIH)
            if (null != r.getAsesoria_pre_test_elisa_vih())
                valor += sdf.format(r.getAsesoria_pre_test_elisa_vih()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getAsesoria_pos_test_elisa_vih())
                valor += sdf.format(r.getAsesoria_pos_test_elisa_vih()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getPaciente_diagnostico_salud_mental_recibio_atencion_ultimos_6_meses_equipo_interdisciplinario_completo()))
                valor += r.getPaciente_diagnostico_salud_mental_recibio_atencion_ultimos_6_meses_equipo_interdisciplinario_completo() + "|";
            else
                valor += "6" + "|";//Sin dato
            
            if (null != r.getFecha_antigeno_superficie_hepatitis_b_gestantes())
                valor += sdf.format(r.getFecha_antigeno_superficie_hepatitis_b_gestantes()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getResultado_antigeno_superficie_hepatitis_b_gestantes()))
                valor += r.getResultado_antigeno_superficie_hepatitis_b_gestantes() + "|";
            else
                valor += "2" + "|";//Sin dato
            
            //campo 80 (Fecha Serología para Sífilis)
            if (null != r.getFecha_serologia_sifilis())
                valor += sdf.format(r.getFecha_serologia_sifilis()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getResultado_serologia_sifilis()))
                valor += r.getResultado_serologia_sifilis() + "|";
            else
                valor += "2" + "|";//Sin dato
            
            if (null != r.getFecha_toma_elisa_vih())
                valor += sdf.format(r.getFecha_toma_elisa_vih()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getResultado_elisa_vih()))
                valor += r.getResultado_elisa_vih() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (null != r.getFecha_tsh_neonatal())
                valor += sdf.format(r.getFecha_tsh_neonatal()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            //campo 85 (Resultado de TSH Neonatal)
            if (StringUtils.isNumeric(r.getResultado_tsh_neonatal()))
                valor += r.getResultado_tsh_neonatal() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getTamizaje_cancer_cuello_uterino()))
                valor += r.getTamizaje_cancer_cuello_uterino() + "|";
            else
                valor += "8" + "|";//Sin dato
            
            if (null != r.getCitologia_cervico_uterina())
                valor += sdf.format(r.getCitologia_cervico_uterina()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getCitologia_cervico_uterina_resultados_bethesda()))
                valor += r.getCitologia_cervico_uterina_resultados_bethesda() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getCalidad_muestra_citologia_cervicouterina()))
                valor += r.getCalidad_muestra_citologia_cervicouterina() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            //campo 90 (Código de habilitación IPS donde se toma Citología Cervicouterina)
            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_donde_toma_citologia_cervicouterina()))
                valor += r.getCodigo_habilitacion_ips_donde_toma_citologia_cervicouterina() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (null != r.getFecha_colposcopia())
                valor += sdf.format(r.getFecha_colposcopia()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_donde_toma_colposcopia()))
                valor += r.getCodigo_habilitacion_ips_donde_toma_colposcopia() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (null != r.getFecha_biopsia_cervical())
                valor += sdf.format(r.getFecha_biopsia_cervical()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getResultado_biopsia_cervical()))
                valor += r.getResultado_biopsia_cervical() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            //campo 95 (Código de habilitación IPS donde se toma Biopsia Cervical)
            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_donde_toma_biopsia_cervical()))
                valor += r.getCodigo_habilitacion_ips_donde_toma_biopsia_cervical() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (null != r.getFecha_mamografia())
                valor += sdf.format(r.getFecha_mamografia()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getResultado_mamografia()))
                valor += r.getResultado_mamografia() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_donde_toma_mamografia()))
                valor += r.getCodigo_habilitacion_ips_donde_toma_mamografia() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (null != r.getFecha_toma_biopsia_seno_bacaf())
                valor += sdf.format(r.getFecha_toma_biopsia_seno_bacaf()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            //campo 100 (Fecha Resultado Biopsia Seno por BACAF)
            if (null != r.getFecha_resultado_biopsia_seno_bacaf())
                valor += sdf.format(r.getFecha_resultado_biopsia_seno_bacaf()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getBiopsia_seno_bacaf()))
                valor += r.getBiopsia_seno_bacaf() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getCodigo_habilitacion_ips_donde_toma_biopsia_seno_bacaf()))
                valor += r.getCodigo_habilitacion_ips_donde_toma_biopsia_seno_bacaf() + "|";
            else
                valor += "99" + "|";//Sin dato
            
            if (null != r.getFecha_toma_hemoglobina())
                valor += sdf.format(r.getFecha_toma_hemoglobina()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getHemoglobina()))
                valor += r.getHemoglobina() + "|";
            else
                valor += "9998" + "|";//No aplica
            
            //campo 105 (Fecha de la Toma de Glicemia Basal)
            if (null != r.getFecha_toma_glicemia_basal())
                valor += sdf.format(r.getFecha_toma_glicemia_basal()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_creatinina())
                valor += sdf.format(r.getFecha_creatinina()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getCreatinina()))
                valor += r.getCreatinina() + "|";
            else
                valor += "999" + "|";//Sin dato
            
            if (null != r.getFecha_hemoglobina_glicosilada())
                valor += sdf.format(r.getFecha_hemoglobina_glicosilada()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getHemoglobina_glicosilada()))
                valor += r.getHemoglobina_glicosilada() + "|";
            else
                valor += "999" + "|";//Sin dato
            
            //campo 110 (Fecha Toma de Microalbuminuria)
            if (null != r.getFecha_toma_microalbuminuria())
                valor += sdf.format(r.getFecha_toma_microalbuminuria()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_toma_hdl())
                valor += sdf.format(r.getFecha_toma_hdl()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (null != r.getFecha_toma_baciloscopia_diagnostico())
                valor += sdf.format(r.getFecha_toma_baciloscopia_diagnostico()) + "|";
            else
                valor += "1800-01-01" + "|";
            
            if (StringUtils.isNumeric(r.getBaciloscopia_diagnostico()))
                valor += r.getBaciloscopia_diagnostico() + "|";
            else
                valor += "3" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getTratamiento_hipotiroidismo_congenito()))
                valor += r.getTratamiento_hipotiroidismo_congenito() + "|";
            else
                valor += "6" + "|";//Sin dato
            
            //campo 115
            if (StringUtils.isNumeric(r.getTratamiento_sifilis_gestacional()))
                valor += r.getTratamiento_sifilis_gestacional() + "|";
            else
                valor += "6" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getTratamiento_sifilis_congenita()))
                valor += r.getTratamiento_sifilis_congenita() + "|";
            else
                valor += "6" + "|";//Sin dato
            
            if (StringUtils.isNumeric(r.getTratamiento_terminado_lepra()))
                valor += r.getTratamiento_terminado_lepra() + "|";
            else
                valor += "7" + "|";//Sin dato
            
            //campo 118 
            if (null != r.getFecha_terminacion_tratamiento_leishmaniasis())
                valor += sdf.format(r.getFecha_terminacion_tratamiento_leishmaniasis());
            else
                valor += "1800-01-01";
            
            
            writer.append(valor);
            writer.append("\n");
        }
        writer.flush();
        writer.close();
        InputStream stream = new BufferedInputStream(new FileInputStream(csvFile));
        file = new DefaultStreamedContent(stream, "application/csv", csvFile);
        imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);
    }

    public void carga_4505(List<CfgPacientes> user) {
        System.out.println("Iniciando cargar 4505 , nro de pacientes " + user.size());
        SimpleDateFormat usuario_nacimiento = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        HcRegistro registroEncontrado = null;
        HcTipoReg tipoRegistroClinicoActual;
        DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();
        int i = 1;
        System.out.println(new Date());
        for (CfgPacientes r : user) {
            System.out.println("Procesando " + i + " de " + user.size());
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
                Logger.getLogger(ReporteInforme.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("fin");

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
            ByteArrayOutputStream output = new ByteArrayOutputStream();
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
            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);
        } catch (JRException e) {
            System.out.println(e);
        }
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

            imprimirMensaje("Información", "Reporte Generado", FacesMessage.SEVERITY_INFO);
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