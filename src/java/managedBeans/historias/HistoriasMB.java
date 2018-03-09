package managedBeans.historias;

import beans.utilidades.FilaDataTable;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.NodoArbolHistorial;
import beans.utilidades.TipoNodoEnum;
import co.com.kno.svg.SVGHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import managedBeans.seguridad.AplicacionGeneralMB;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgDiagnostico;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgFamiliar;
import modelo.entidades.CfgMaestrosTxtPredefinidos;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgTxtPredefinidos;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacConsumoMedicamento;
import modelo.entidades.FacServicio;
import modelo.entidades.HcArchivos;
import modelo.entidades.HcCamposReg;
import modelo.entidades.HcDetalle;
import modelo.entidades.HcItems;
import modelo.entidades.HcRegistro;
import modelo.entidades.HcRepExamenes;
import modelo.entidades.HcTipoReg;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgDiagnosticoPrincipalFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgFamiliarFacade;
import modelo.fachadas.CfgMaestrosTxtPredefinidosFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgTxtPredefinidosFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CitTurnosFacade;
import modelo.fachadas.FacConsumoMedicamentoFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.HcArchivosFacade;
import modelo.fachadas.HcCamposRegFacade;
import modelo.fachadas.HcDetalleFacade;
import modelo.fachadas.HcItemsFacade;
import modelo.fachadas.HcRegistroFacade;
import modelo.fachadas.HcRepExamenesFacade;
import modelo.fachadas.HcTipoRegFacade;
import modelo.fachadas.InvBodegaProductosFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.apache.batik.transcoder.TranscoderException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.LinearAxis;

@ManagedBean(name = "historiasMB")
@SessionScoped
public class HistoriasMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    HcRegistroFacade registroFacade;
    @EJB
    CitTurnosFacade turnosFacade;
    @EJB
    CitCitasFacade citasFacade;
    @EJB
    HcTipoRegFacade tipoRegCliFacade;
    @EJB
    HcCamposRegFacade camposRegFacade;
    @EJB
    CfgPacientesFacade pacientesFacade;
    @EJB
    CfgMaestrosTxtPredefinidosFacade maestrosTxtPredefinidosFacade;
    @EJB
    CfgClasificacionesFacade maestrosClasificacionesFacade;
    @EJB
    CfgTxtPredefinidosFacade txtPredefinidosFacade;
    @EJB
    CfgDiagnosticoFacade diagnosticoFacade;
    @EJB
    CfgDiagnosticoPrincipalFacade diagnosticoPrincipalFacade;
    @EJB
    CfgUsuariosFacade usuariosFacade;
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;
    @EJB
    CfgFamiliarFacade familiarFacade;
    @EJB
    HcItemsFacade itemsFacade;
    @EJB
    CfgMedicamentoFacade cfgMedicamento;
    @EJB
    HcArchivosFacade hcArchivosFacade;
    @EJB
    HcRepExamenesFacade hcRepFacade;
    @EJB
    HcDetalleFacade detalleFacade;
    @EJB
    InvBodegaProductosFacade invBodegaProductosFacade;
    @EJB
    FacConsumoMedicamentoFacade facConsumoMedicamentoFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    private HcTipoReg tipoRegistroClinicoActual;
    private List<CfgPacientes> listaPacientes;
    //private List<CfgPacientes> listaPacientes;
    private List<CfgPacientes> listaPacientesFiltro;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteTmp;
    private List<CfgMaestrosTxtPredefinidos> listaMaestrosTxtPredefinidos;
    private List<CfgTxtPredefinidos> listaTxtPredefinidos;
    private List<CfgUsuarios> listaPrestadores;
    private List<CfgUsuarios> listaOdontologos;
    private CfgTxtPredefinidos txtPredefinidoActual = null;
    private CfgClasificaciones clasificacionBuscada;
    private CfgDiagnostico diagnosticoBuscado;
    private FacServicio servicioBuscado;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------        

    private String filtroAutorizacion = "";
    private Date filtroFechaInicial = null;//new Date();
    private Date filtroFechaFinal = null;//new Date();

    public UploadedFile archivos;
    private String urlFile = "";

    private String detalleTextoPredef = "";
    private String descriparchivo = "";
    private String idTextoPredef = "";
    private String idMaestroTextoPredef = "";
    private String nombreTextoPredefinido = "";
    private String urlFoto = "../recursos/img/img_usuario.png";
    private String tipoRegistroClinico = "";//tipo de registro clinico usado para cargar el fomulario correspondiente
    private String[] regCliSelHistorial;//registros clinicos seleccionados para el historial
    private String urlPagina = "";
    private String nombreTipoRegistroClinico = "";
    private String identificacionPaciente = "";
    private String tipoIdentificacion = "";
    private String nombrePaciente = "Paciente";
    private String generoPaciente = "";
    private String edadPaciente = "";
    private String administradoraPaciente = "";
    private String idEditorSeleccionado = "";//determinar cual de los editores de texto se les asignara un texto predefinido
    private int posListaTxtPredef = 0;//determinar la posicion en la estructura 'estructuraCampos' al usar un texto predefinido
    private String idPrestador = "";
    private String especialidadPrestador = "";
    private DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico
    private DatosFormularioHistoria datosFormulario_nutricion = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico
    private DatosFormularioHistoria datosFormulario_formmedicamentos = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico
    private Date fechaReg;
    private Date fechaSis;
    private TreeNode treeNodeRaiz;//nodos raiz del historico
    //private TreeNode treeNodeSeleccionado;//nodo seleccionado del historico
    private TreeNode[] treeNodesSeleccionados;
    private List<HcTipoReg> listaTipoRegistroClinico;
    private boolean hayPacienteSeleccionado = false;//se ha seleccionado un paciente
    private boolean modificandoRegCli = false;//se esta modificando un registro clinco existente
    private boolean btnHistorialDisabled = true;
    private boolean btnPdfAgrupadoDisabled = true;
    private HcRegistro registroEncontrado = null;//variable que almacena el registro clinico cargado desde el historial
    private List<DatosFormularioHistoria> listaRegistrosParaPdf;// maneja como lista por si fueran varias historias al tiempo    

    //private final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
    private CfgEmpresa empresa;
    private LoginMB loginMB;
    private boolean btnEditarRendered = true;

    private String turnoCita = "";//identificador del turno de la cita
    private boolean cargandoDesdeTab = false;
    private CitCitas citaActual = null;

    //private String comoBolean = "TRUE";
    private List<SelectItem> listaMunicipios;
    private String departamento = "";
    private String municipio = "";

    private final SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm");
    private final SimpleDateFormat sdfDateHour = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private final SimpleDateFormat sdfFechaString = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);//Thu Apr 30 19:00:00 COT 2015
    private final DecimalFormat formateadorDecimal = new DecimalFormat("#.##");

    private String etnia = "";
    private String ocupacion = "";
    private String escolaridad = "";
    private String religion = "";
    private String gestacion = "";
    private String discapacidad = "";

    private Boolean victimaMaltrato = false; // Un booleano y un Str, el str es para mostrar nada, o SI o NO
    private String victimaMaltratoStr = "";
    private Boolean victimaConflicto = false;
    private String victimaConflictoStr = "";
    private Boolean desplazado = false;
    private String desplazadoStr = "";
    private Boolean poblacionLBGT = false;
    private String poblacionLBGTStr = "";

    private String imc = "";

    private List<FilaDataTable> listaEstructuraFamiliar = new ArrayList<>();
    private List<FilaDataTable> listaEstructuraFamiliarFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentos = new ArrayList<>();
    private List<FilaDataTable> listaServicios = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosFiltro = new ArrayList<>();
    private List<FilaDataTable> listaServiciosFiltro = new ArrayList<>();
    private FilaDataTable familiarSeleccionado = new FilaDataTable();
    private FilaDataTable medicamentoSeleccionado = new FilaDataTable();
    private FilaDataTable servicioSeleccionado = new FilaDataTable();
    private String nombreFamiliar = "";
    private String edadFamiliar = "";
    private String parentescoFamiliar = "";
    private String ocupacionFamiliar = "";

    private String idServicio;
    private String idMedicamento;
    private String codigo;
    private String descripcion;
    private Integer cantidad;
    private String dosis;
    private String presentacion;
    private String concentracion;
    private String viaAdmin;
    private String posologia;
    private String observacion;

    private String numeroDiente = "";
    private int variableInicial;
    private String urlZonasActivasOleary = "";
    private String detDiente1 = "";
    private String detDiente2 = "";
    private String detDiente3 = "";
    private String detDiente4 = "";
    private String detDiente5 = "";
    private String detDiente6 = "";
    private String detDiente7 = "";
    private String detOleary1 = "";
    private String detOleary2 = "";
    private String detOleary3 = "";
    private String detOleary4 = "";

    //prueba subida y descarga de archvio
    private List<HcArchivos> listaArchivo;
    private HcArchivos archivoSeleccionado;
    private StreamedContent fileDownload;
    private Boolean falla_atencion = false;
    private String jsonOdontograma;
    private String jsonEvolucion;
    private String jsonOleary1;
    private String jsonOleary2;
    private int edadAnios;
    FilaDataTable reporte_seleccionado;
    HcRepExamenes agregar_fila_reporte = new HcRepExamenes();
    String examen = "";
    int posicion = 0;
    int tipo = 0;
    Integer pas_d = null;
    Integer pad_d = null;
    Integer pas_i = null;
    Integer pad_i = null;
    Integer pas_c = null;
    Integer pad_c = null;
    Integer pas_ce = null;
    Integer pad_ce = null;
    //signos vitales
    Integer fc = null;
    Integer fr = null;
    Double t = 0.0;
    int v = 0;
    int pas = 0;
    int pad = 0;
    int horas = 0;
    int minutos = 0;
    String clasificacion_f = "Sin Dato...";
    Integer pas_de = 0;
    Integer pad_de = 0;
    String clasificacion_pa = "Sin Dato...";

    String clasificacion_peso = "Sin Dato...";
    Double peso = 0.0;
    Double talla = 0.0;
    Double peso_to = 0.0;

    String clasificacion_perimetro = "Sin Dato...";
    Double perimetro = 0.0;

    Date fecha;
    List<FilaDataTable> lista_reportes = new ArrayList<>();
    boolean ver_ginecostetricos = false;
    private AplicacionGeneralMB aplicacionGeneralMB;

    private List<FacServicio> listaServiciosOrdenMedica = new ArrayList<FacServicio>();

    //Variables para valores por defecto
    private String valorDefecto1;
    private String valorDefecto2;
    private String valorDefecto3;
    private String valorDefecto4;
    private String valorDefecto5;
    private String valorDefecto6;
    private String valorDefecto7;
    private String valorDefecto8;
    private String valorDefecto9;
    private String valorDefecto10;
    private String valorDefecto11;
    private String valorDefecto12;
    private String valorDefecto13;
    private String valorDefecto14;
    private String valorDefecto15;
    private String valorDefecto16;
    private String valorDefecto17;
    private String valorDefecto18;
    private String valorDefecto19;
    private String valorDefecto20;
    private List<SelectItem> listaValoresDefecto;
    private List<SelectItem> listaValoresDefecto2;
    
    
     private LineChartModel lineModel1 = new LineChartModel();
     private LineChartModel lineModel2 = new LineChartModel();
     private LineChartModel lineModel3 = new LineChartModel();
     private LineChartModel lineModel4 = new LineChartModel();
    
    //---------------------------------------------------
    //----------------- FUNCIONES INICIALES -----------------------
    //---------------------------------------------------

    public HistoriasMB() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
        listaValoresDefecto = new ArrayList();
        listaValoresDefecto.add(new SelectItem("No Refiere", "No Refiere"));
        listaValoresDefecto.add(new SelectItem("Normal", "Normal"));
        
        listaValoresDefecto2 = new ArrayList();
        listaValoresDefecto2.add(new SelectItem("Normal", "Normal"));
        listaValoresDefecto2.add(new SelectItem("Anormal", "Anormal"));
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        try {
            if (datosFormulario != null) {
                departamento = datosFormulario.getDato1().toString();
                if (departamento != null && departamento.length() != 0 && esNumero(departamento)) {
                    List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
                    for (CfgClasificaciones mun : listaM) {
                        listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
                    }
                } else {
                    List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
                    for (CfgClasificaciones mun : listaM) {
                        listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Se cargan todos los municipios ya que no se cuenta con el departamento
     */
    public void cargarMunicipiosDefault() {
        try {
            List<CfgClasificaciones> lstMun = clasificacionesFacade.buscarMunicipio();
            for (CfgClasificaciones mun : lstMun) {
                listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        } catch (Exception e) {

        }

    }

    /**
     * Se utiliza desde ventana para que a travez de un boton se cambien los
     * vaores por defecto de una seccion especifica
     *
     * @param tipo
     */
    public void cambiarValorDefecto(int tipo) {
        if (tipoRegistroClinicoActual == null) {
            return;
        }
        switch (tipo) {
            case 1:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato8(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato3(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                    datosFormulario.setDato3(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato3(valorDefecto1);
                    datosFormulario.setDato4(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato3(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato2(valorDefecto1);
                    datosFormulario.setDato3(valorDefecto1);
                    datosFormulario.setDato4(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 11) {
                    datosFormulario.setDato0(valorDefecto1);
                    datosFormulario.setDato1(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato3(valorDefecto1);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato4(valorDefecto1);
                    datosFormulario.setDato5(valorDefecto1);
                }

                break;
            case 2:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato9(valorDefecto2);
                    datosFormulario.setDato10(valorDefecto2);
                    datosFormulario.setDato11(valorDefecto2);
                    datosFormulario.setDato12(valorDefecto2);
                    datosFormulario.setDato13(valorDefecto2);
                    datosFormulario.setDato14(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato4(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                    datosFormulario.setDato13(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato5(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato4(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato29(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 11) {
                    datosFormulario.setDato2(valorDefecto2);
                    datosFormulario.setDato3(valorDefecto2);
                    datosFormulario.setDato4(valorDefecto2);
                    datosFormulario.setDato5(valorDefecto2);
                    datosFormulario.setDato6(valorDefecto2);
                    datosFormulario.setDato7(valorDefecto2);
                    datosFormulario.setDato8(valorDefecto2);
                    datosFormulario.setDato9(valorDefecto2);
                    datosFormulario.setDato10(valorDefecto2);
                    datosFormulario.setDato11(valorDefecto2);
                    datosFormulario.setDato12(valorDefecto2);
                    datosFormulario.setDato13(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato4(valorDefecto2);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato6(valorDefecto2);
                    datosFormulario.setDato7(valorDefecto2);
                    datosFormulario.setDato8(valorDefecto2);
                    datosFormulario.setDato9(valorDefecto2);
                    datosFormulario.setDato10(valorDefecto2);
                    datosFormulario.setDato11(valorDefecto2);
                    datosFormulario.setDato12(valorDefecto2);
                    datosFormulario.setDato13(valorDefecto2);
                    datosFormulario.setDato14(valorDefecto2);
                    datosFormulario.setDato15(valorDefecto2);
                    datosFormulario.setDato16(valorDefecto2);
                    datosFormulario.setDato17(valorDefecto2);
                    datosFormulario.setDato18(valorDefecto2);
                    datosFormulario.setDato19(valorDefecto2);
                    datosFormulario.setDato20(valorDefecto2);
                    datosFormulario.setDato21(valorDefecto2);
                }

                break;
            case 3:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato15(valorDefecto3);
                    datosFormulario.setDato16(valorDefecto3);
                    //datosFormulario.setDato17(valorDefecto3); Es fecha
                    datosFormulario.setDato18(valorDefecto3);
                    datosFormulario.setDato19(valorDefecto3);
                    datosFormulario.setDato20(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato5(valorDefecto3);
                    datosFormulario.setDato6(valorDefecto3);
                    datosFormulario.setDato7(valorDefecto3);
                    datosFormulario.setDato8(valorDefecto3);
                    datosFormulario.setDato9(valorDefecto3);
                    datosFormulario.setDato10(valorDefecto3);
                    datosFormulario.setDato11(valorDefecto3);
                    datosFormulario.setDato12(valorDefecto3);
                    datosFormulario.setDato13(valorDefecto3);
                    datosFormulario.setDato14(valorDefecto3);
                    datosFormulario.setDato15(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                    datosFormulario.setDato14(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato6(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato5(valorDefecto3);
                    datosFormulario.setDato6(valorDefecto3);
                    datosFormulario.setDato7(valorDefecto3);
                    datosFormulario.setDato8(valorDefecto3);
                    datosFormulario.setDato9(valorDefecto3);
                    datosFormulario.setDato10(valorDefecto3);
                    datosFormulario.setDato11(valorDefecto3);
                    datosFormulario.setDato12(valorDefecto3);
                    datosFormulario.setDato13(valorDefecto3);
                    datosFormulario.setDato14(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato46(valorDefecto3);
                    datosFormulario.setDato47(valorDefecto3);
                    datosFormulario.setDato52(valorDefecto3);
                    datosFormulario.setDato53(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 11) {
                    datosFormulario.setDato14(valorDefecto3);
                    datosFormulario.setDato15(valorDefecto3);
                    datosFormulario.setDato16(valorDefecto3);
                    datosFormulario.setDato17(valorDefecto3);
                    datosFormulario.setDato18(valorDefecto3);
                    datosFormulario.setDato19(valorDefecto3);
                    datosFormulario.setDato20(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato5(valorDefecto3);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato26(valorDefecto3);
                    datosFormulario.setDato29(valorDefecto3);
                    datosFormulario.setDato30(valorDefecto3);
                    datosFormulario.setDato31(valorDefecto3);
                    datosFormulario.setDato33(valorDefecto3);
                    datosFormulario.setDato35(valorDefecto3);
                    datosFormulario.setDato36(valorDefecto3);
                    datosFormulario.setDato38(valorDefecto3);
                    datosFormulario.setDato40(valorDefecto3);
                    datosFormulario.setDato43(valorDefecto3);
                    datosFormulario.setDato51(valorDefecto3);
                    datosFormulario.setDato53(valorDefecto3);
                    datosFormulario.setDato54(valorDefecto3);
                    datosFormulario.setDato57(valorDefecto3);
                }
                break;
            case 4:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato21(valorDefecto4);
                    datosFormulario.setDato22(valorDefecto4);
                    datosFormulario.setDato23(valorDefecto4);
                    datosFormulario.setDato24(valorDefecto4);
                    datosFormulario.setDato25(valorDefecto4);
                    datosFormulario.setDato26(valorDefecto4);
                    datosFormulario.setDato27(valorDefecto4);
                    datosFormulario.setDato28(valorDefecto4);
                    datosFormulario.setDato29(valorDefecto4);
                    datosFormulario.setDato30(valorDefecto4);
                    datosFormulario.setDato31(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato16(valorDefecto4);
                    datosFormulario.setDato17(valorDefecto4);
                    datosFormulario.setDato18(valorDefecto4);
                    datosFormulario.setDato19(valorDefecto4);
                    datosFormulario.setDato20(valorDefecto4);
                    datosFormulario.setDato21(valorDefecto4);
                    datosFormulario.setDato22(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                    datosFormulario.setDato15(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato7(valorDefecto4);
                    datosFormulario.setDato8(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato15(valorDefecto4);
                    datosFormulario.setDato16(valorDefecto4);
                    datosFormulario.setDato17(valorDefecto4);
                    datosFormulario.setDato18(valorDefecto4);
                    datosFormulario.setDato19(valorDefecto4);
                    datosFormulario.setDato20(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato56(valorDefecto4);
                    datosFormulario.setDato57(valorDefecto4);
                    datosFormulario.setDato59(valorDefecto4);
                    datosFormulario.setDato60(valorDefecto4);
                    datosFormulario.setDato62(valorDefecto4);
                    datosFormulario.setDato64(valorDefecto4);
                    datosFormulario.setDato65(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 11) {
                    datosFormulario.setDato21(valorDefecto4);
                    datosFormulario.setDato22(valorDefecto4);
                    datosFormulario.setDato23(valorDefecto4);
                    datosFormulario.setDato24(valorDefecto4);
                    datosFormulario.setDato25(valorDefecto4);
                    datosFormulario.setDato26(valorDefecto4);
                    datosFormulario.setDato27(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato6(valorDefecto4);
                    datosFormulario.setDato7(valorDefecto4);
                    datosFormulario.setDato8(valorDefecto4);
                    datosFormulario.setDato9(valorDefecto4);
                    datosFormulario.setDato10(valorDefecto4);
                    datosFormulario.setDato11(valorDefecto4);
                    datosFormulario.setDato12(valorDefecto4);
                    datosFormulario.setDato13(valorDefecto4);
                    datosFormulario.setDato14(valorDefecto4);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato75(valorDefecto4);
                    datosFormulario.setDato81(valorDefecto4);
                    datosFormulario.setDato83(valorDefecto4);
                    datosFormulario.setDato89(valorDefecto4);
                }
                break;
            case 5:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato32(valorDefecto5);
                    datosFormulario.setDato33(valorDefecto5);
                    datosFormulario.setDato34(valorDefecto5);
                    datosFormulario.setDato35(valorDefecto5);
                    datosFormulario.setDato36(valorDefecto5);
                    datosFormulario.setDato37(valorDefecto5);
                    datosFormulario.setDato38(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato23(valorDefecto5);
                    datosFormulario.setDato24(valorDefecto5);
                    datosFormulario.setDato25(valorDefecto5);
                    datosFormulario.setDato26(valorDefecto5);
                    datosFormulario.setDato27(valorDefecto5);
                    datosFormulario.setDato28(valorDefecto5);
                    datosFormulario.setDato29(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                    datosFormulario.setDato16(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato9(valorDefecto5);
                    datosFormulario.setDato10(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato21(valorDefecto5);
                    datosFormulario.setDato22(valorDefecto5);
                    datosFormulario.setDato23(valorDefecto5);
                    datosFormulario.setDato24(valorDefecto5);
                    datosFormulario.setDato25(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato67(valorDefecto5);
                    datosFormulario.setDato68(valorDefecto5);
                    datosFormulario.setDato73(valorDefecto5);
                    datosFormulario.setDato74(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 11) {
                    datosFormulario.setDato28(valorDefecto5);
                    datosFormulario.setDato29(valorDefecto5);
                    datosFormulario.setDato30(valorDefecto5);
                    datosFormulario.setDato31(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato15(valorDefecto5);
                    datosFormulario.setDato16(valorDefecto5);
                    datosFormulario.setDato17(valorDefecto5);
                    datosFormulario.setDato18(valorDefecto5);
                    datosFormulario.setDato19(valorDefecto5);
                    datosFormulario.setDato20(valorDefecto5);
                    datosFormulario.setDato21(valorDefecto5);
                    datosFormulario.setDato22(valorDefecto5);
                    datosFormulario.setDato23(valorDefecto5);
                    datosFormulario.setDato24(valorDefecto5);
                    datosFormulario.setDato25(valorDefecto5);
                    datosFormulario.setDato26(valorDefecto5);
                    datosFormulario.setDato27(valorDefecto5);
                    datosFormulario.setDato28(valorDefecto5);
                    datosFormulario.setDato29(valorDefecto5);
                    datosFormulario.setDato30(valorDefecto5);
                    datosFormulario.setDato31(valorDefecto5);
                    datosFormulario.setDato32(valorDefecto5);
                    datosFormulario.setDato33(valorDefecto5);
                    datosFormulario.setDato34(valorDefecto5);
                    datosFormulario.setDato35(valorDefecto5);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato105(valorDefecto5);
                    datosFormulario.setDato115(valorDefecto5);
                    datosFormulario.setDato116(valorDefecto5);
                    datosFormulario.setDato117(valorDefecto5);
                }
                break;
            case 6:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) {
                    datosFormulario.setDato39(valorDefecto6);
                    datosFormulario.setDato40(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato30(valorDefecto6);
                    datosFormulario.setDato31(valorDefecto6);
                    datosFormulario.setDato32(valorDefecto6);
                    datosFormulario.setDato33(valorDefecto6);
                    datosFormulario.setDato34(valorDefecto6);
                    datosFormulario.setDato35(valorDefecto6);
                    datosFormulario.setDato36(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato11(valorDefecto6);
                    datosFormulario.setDato12(valorDefecto6);
                    datosFormulario.setDato13(valorDefecto6);
                    datosFormulario.setDato14(valorDefecto6);
                    datosFormulario.setDato15(valorDefecto6);
                    datosFormulario.setDato16(valorDefecto6);
                    datosFormulario.setDato17(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato27(valorDefecto6);
                    datosFormulario.setDato28(valorDefecto6);
                    datosFormulario.setDato29(valorDefecto6);
                    datosFormulario.setDato30(valorDefecto6);
                    datosFormulario.setDato31(valorDefecto6);
                    datosFormulario.setDato32(valorDefecto6);
                    datosFormulario.setDato33(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato79(valorDefecto6);
                    datosFormulario.setDato80(valorDefecto6);
                    datosFormulario.setDato82(valorDefecto6);
                    datosFormulario.setDato83(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato36(valorDefecto6);
                    datosFormulario.setDato37(valorDefecto6);
                    datosFormulario.setDato39(valorDefecto6);
                    datosFormulario.setDato40(valorDefecto6);
                    datosFormulario.setDato41(valorDefecto6);
                    datosFormulario.setDato42(valorDefecto6);
                    datosFormulario.setDato43(valorDefecto6);
                    datosFormulario.setDato44(valorDefecto6);
                    datosFormulario.setDato45(valorDefecto6);
                    datosFormulario.setDato46(valorDefecto6);
                    datosFormulario.setDato47(valorDefecto6);
                    datosFormulario.setDato49(valorDefecto6);
                    datosFormulario.setDato50(valorDefecto6);
                    datosFormulario.setDato51(valorDefecto6);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato119(valorDefecto6);
                    datosFormulario.setDato120(valorDefecto6);
                    datosFormulario.setDato121(valorDefecto6);
                    datosFormulario.setDato122(valorDefecto6);
                    datosFormulario.setDato123(valorDefecto6);
                    datosFormulario.setDato125(valorDefecto6);
                    datosFormulario.setDato126(valorDefecto6);
                    datosFormulario.setDato128(valorDefecto6);
                    datosFormulario.setDato129(valorDefecto6);
                    datosFormulario.setDato131(valorDefecto6);
                    datosFormulario.setDato132(valorDefecto6);
                    datosFormulario.setDato135(valorDefecto6);
                }

                break;
            case 7:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato37(valorDefecto7);
                    datosFormulario.setDato38(valorDefecto7);
                    datosFormulario.setDato39(valorDefecto7);
                    datosFormulario.setDato40(valorDefecto7);
                    datosFormulario.setDato41(valorDefecto7);
                    datosFormulario.setDato42(valorDefecto7);
                    datosFormulario.setDato43(valorDefecto7);
                    datosFormulario.setDato44(valorDefecto7);
                    datosFormulario.setDato45(valorDefecto7);
                    datosFormulario.setDato46(valorDefecto7);
                    datosFormulario.setDato47(valorDefecto7);
                    datosFormulario.setDato48(valorDefecto7);
                    datosFormulario.setDato49(valorDefecto7);
                    datosFormulario.setDato50(valorDefecto7);
                    datosFormulario.setDato51(valorDefecto7);
                    datosFormulario.setDato52(valorDefecto7);
                    datosFormulario.setDato53(valorDefecto7);
                    datosFormulario.setDato54(valorDefecto7);
                    datosFormulario.setDato55(valorDefecto7);
                    datosFormulario.setDato56(valorDefecto7);
                    datosFormulario.setDato57(valorDefecto7);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato18(valorDefecto7);
                    datosFormulario.setDato19(valorDefecto7);
                    datosFormulario.setDato20(valorDefecto7);
                    datosFormulario.setDato21(valorDefecto7);
                    datosFormulario.setDato22(valorDefecto7);
                    datosFormulario.setDato23(valorDefecto7);
                    datosFormulario.setDato24(valorDefecto7);
                    datosFormulario.setDato25(valorDefecto7);
                    datosFormulario.setDato26(valorDefecto7);
                    datosFormulario.setDato27(valorDefecto7);
                    datosFormulario.setDato28(valorDefecto7);
                    datosFormulario.setDato29(valorDefecto7);
                    datosFormulario.setDato30(valorDefecto7);
                    datosFormulario.setDato31(valorDefecto7);
                    datosFormulario.setDato32(valorDefecto7);
                    datosFormulario.setDato33(valorDefecto7);
                    datosFormulario.setDato34(valorDefecto7);
                    datosFormulario.setDato35(valorDefecto7);
                    datosFormulario.setDato36(valorDefecto7);
                    datosFormulario.setDato37(valorDefecto7);
                    datosFormulario.setDato38(valorDefecto7);
                    datosFormulario.setDato39(valorDefecto7);
                    datosFormulario.setDato40(valorDefecto7);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato34(valorDefecto7);
                    datosFormulario.setDato35(valorDefecto7);
                    datosFormulario.setDato36(valorDefecto7);
                    datosFormulario.setDato37(valorDefecto7);
                    datosFormulario.setDato38(valorDefecto7);
                    datosFormulario.setDato39(valorDefecto7);
                    datosFormulario.setDato40(valorDefecto7);
                    datosFormulario.setDato41(valorDefecto7);
                    datosFormulario.setDato42(valorDefecto7);
                    datosFormulario.setDato43(valorDefecto7);
                    datosFormulario.setDato44(valorDefecto7);
                    datosFormulario.setDato45(valorDefecto7);
                    datosFormulario.setDato46(valorDefecto7);
                    datosFormulario.setDato47(valorDefecto7);
                    datosFormulario.setDato48(valorDefecto7);
                    datosFormulario.setDato49(valorDefecto7);
                    datosFormulario.setDato50(valorDefecto7);
                    datosFormulario.setDato51(valorDefecto7);
                    datosFormulario.setDato52(valorDefecto7);
                    datosFormulario.setDato53(valorDefecto7);
                    datosFormulario.setDato54(valorDefecto7);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato85(valorDefecto7);
                    datosFormulario.setDato87(valorDefecto7);
                    datosFormulario.setDato88(valorDefecto7);
                    datosFormulario.setDato89(valorDefecto7);
                    datosFormulario.setDato90(valorDefecto7);
                    datosFormulario.setDato91(valorDefecto7);
                    datosFormulario.setDato92(valorDefecto7);
                    datosFormulario.setDato94(valorDefecto7);
                    datosFormulario.setDato96(valorDefecto7);
                    datosFormulario.setDato97(valorDefecto7);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato52(valorDefecto7);
                    datosFormulario.setDato53(valorDefecto7);
                    datosFormulario.setDato54(valorDefecto7);
                    datosFormulario.setDato55(valorDefecto7);
                    datosFormulario.setDato56(valorDefecto7);
                    datosFormulario.setDato57(valorDefecto7);
                    datosFormulario.setDato58(valorDefecto7);
                    datosFormulario.setDato59(valorDefecto7);
                    datosFormulario.setDato60(valorDefecto7);
                    datosFormulario.setDato61(valorDefecto7);
                    datosFormulario.setDato62(valorDefecto7);
                    datosFormulario.setDato63(valorDefecto7);
                    datosFormulario.setDato64(valorDefecto7);
                    datosFormulario.setDato65(valorDefecto7);
                    datosFormulario.setDato66(valorDefecto7);
                    datosFormulario.setDato67(valorDefecto7);
                    datosFormulario.setDato68(valorDefecto7);
                    datosFormulario.setDato69(valorDefecto7);
                    datosFormulario.setDato70(valorDefecto7);
                    datosFormulario.setDato71(valorDefecto7);
                    datosFormulario.setDato72(valorDefecto7);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato138(valorDefecto7);
                    datosFormulario.setDato139(valorDefecto7);
                }
                break;
            case 8:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato58(valorDefecto8);
                    datosFormulario.setDato59(valorDefecto8);
                    datosFormulario.setDato60(valorDefecto8);
                    datosFormulario.setDato61(valorDefecto8);
                    datosFormulario.setDato62(valorDefecto8);
                    datosFormulario.setDato63(valorDefecto8);
                    datosFormulario.setDato64(valorDefecto8);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato41(valorDefecto8);
                    datosFormulario.setDato42(valorDefecto8);
                    datosFormulario.setDato43(valorDefecto8);
                    datosFormulario.setDato44(valorDefecto8);
                    datosFormulario.setDato45(valorDefecto8);
                    datosFormulario.setDato46(valorDefecto8);
                    datosFormulario.setDato47(valorDefecto8);
                    datosFormulario.setDato48(valorDefecto8);
                    datosFormulario.setDato49(valorDefecto8);
                    datosFormulario.setDato50(valorDefecto8);
                    datosFormulario.setDato51(valorDefecto8);
                    datosFormulario.setDato52(valorDefecto8);
                    datosFormulario.setDato53(valorDefecto8);
                    datosFormulario.setDato54(valorDefecto8);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato55(valorDefecto8);
                    datosFormulario.setDato56(valorDefecto8);
                    datosFormulario.setDato57(valorDefecto8);
                    datosFormulario.setDato58(valorDefecto8);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato98(valorDefecto8);
                    datosFormulario.setDato99(valorDefecto8);
                    datosFormulario.setDato109(valorDefecto8);
                    datosFormulario.setDato110(valorDefecto8);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato73(valorDefecto8);
                    datosFormulario.setDato74(valorDefecto8);
                    datosFormulario.setDato75(valorDefecto8);
                    datosFormulario.setDato76(valorDefecto8);
                    datosFormulario.setDato77(valorDefecto8);
                    datosFormulario.setDato78(valorDefecto8);
                    datosFormulario.setDato79(valorDefecto8);
                    datosFormulario.setDato80(valorDefecto8);
                    datosFormulario.setDato81(valorDefecto8);
                    datosFormulario.setDato82(valorDefecto8);
                    datosFormulario.setDato83(valorDefecto8);
                    datosFormulario.setDato84(valorDefecto8);
                    datosFormulario.setDato85(valorDefecto8);
                    datosFormulario.setDato86(valorDefecto8);
                    datosFormulario.setDato87(valorDefecto8);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato143(valorDefecto8);
                    datosFormulario.setDato144(valorDefecto8);
                    datosFormulario.setDato145(valorDefecto8);
                    datosFormulario.setDato146(valorDefecto8);
                    datosFormulario.setDato147(valorDefecto8);
                    datosFormulario.setDato162(valorDefecto8);
                }
                break;
            case 9:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato65(valorDefecto9);
                    datosFormulario.setDato66(valorDefecto9);
                    datosFormulario.setDato68(valorDefecto9);
                    datosFormulario.setDato69(valorDefecto9);
                    datosFormulario.setDato70(valorDefecto9);
                    datosFormulario.setDato71(valorDefecto9);
                    datosFormulario.setDato72(valorDefecto9);
                    datosFormulario.setDato73(valorDefecto9);
                    datosFormulario.setDato74(valorDefecto9);
                    datosFormulario.setDato75(valorDefecto9);
                    datosFormulario.setDato76(valorDefecto9);
                    datosFormulario.setDato77(valorDefecto9);
                    datosFormulario.setDato78(valorDefecto9);
                    datosFormulario.setDato79(valorDefecto9);
                    datosFormulario.setDato80(valorDefecto9);
                    datosFormulario.setDato81(valorDefecto9);
                    datosFormulario.setDato82(valorDefecto9);
                    datosFormulario.setDato83(valorDefecto9);
                    datosFormulario.setDato84(valorDefecto9);
                    datosFormulario.setDato85(valorDefecto9);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                    datosFormulario.setDato51(valorDefecto9);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato59(valorDefecto9);
                    datosFormulario.setDato60(valorDefecto9);
                    datosFormulario.setDato61(valorDefecto9);
                    datosFormulario.setDato62(valorDefecto9);
                    datosFormulario.setDato63(valorDefecto9);
                    datosFormulario.setDato64(valorDefecto9);
                    datosFormulario.setDato65(valorDefecto9);
                    datosFormulario.setDato66(valorDefecto9);
                    datosFormulario.setDato67(valorDefecto9);
                    datosFormulario.setDato68(valorDefecto9);
                    datosFormulario.setDato69(valorDefecto9);
                    datosFormulario.setDato70(valorDefecto9);
                    datosFormulario.setDato71(valorDefecto9);
                    datosFormulario.setDato72(valorDefecto9);
                    datosFormulario.setDato73(valorDefecto9);
                    datosFormulario.setDato74(valorDefecto9);
                    datosFormulario.setDato75(valorDefecto9);
                    datosFormulario.setDato76(valorDefecto9);
                    datosFormulario.setDato77(valorDefecto9);
                    datosFormulario.setDato78(valorDefecto9);
                    datosFormulario.setDato79(valorDefecto9);
                    datosFormulario.setDato80(valorDefecto9);
                    datosFormulario.setDato81(valorDefecto9);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato114(valorDefecto9);
                    datosFormulario.setDato119(valorDefecto9);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato88(valorDefecto9);
                    datosFormulario.setDato89(valorDefecto9);
                    datosFormulario.setDato90(valorDefecto9);
                    datosFormulario.setDato91(valorDefecto9);
                    datosFormulario.setDato92(valorDefecto9);
                    datosFormulario.setDato93(valorDefecto9);
                    datosFormulario.setDato94(valorDefecto9);
                    datosFormulario.setDato95(valorDefecto9);
                    datosFormulario.setDato96(valorDefecto9);
                    datosFormulario.setDato97(valorDefecto9);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato164(valorDefecto9);
                    datosFormulario.setDato165(valorDefecto9);
                    datosFormulario.setDato167(valorDefecto9);
                    datosFormulario.setDato168(valorDefecto9);
                    datosFormulario.setDato169(valorDefecto9);
                    datosFormulario.setDato170(valorDefecto9);
                }
                break;
            case 10:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato86(valorDefecto10);
                    datosFormulario.setDato87(valorDefecto10);
                    datosFormulario.setDato88(valorDefecto10);
                    datosFormulario.setDato89(valorDefecto10);
                    datosFormulario.setDato90(valorDefecto10);
                    datosFormulario.setDato91(valorDefecto10);
                    datosFormulario.setDato92(valorDefecto10);
                    datosFormulario.setDato93(valorDefecto10);
                    datosFormulario.setDato94(valorDefecto10);
                    datosFormulario.setDato95(valorDefecto10);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato82(valorDefecto10);
                    datosFormulario.setDato81(valorDefecto10);
                    datosFormulario.setDato82(valorDefecto10);
                    datosFormulario.setDato83(valorDefecto10);
                    datosFormulario.setDato84(valorDefecto10);
                    datosFormulario.setDato85(valorDefecto10);
                    datosFormulario.setDato86(valorDefecto10);
                    datosFormulario.setDato87(valorDefecto10);
                    datosFormulario.setDato88(valorDefecto10);
                    datosFormulario.setDato89(valorDefecto10);
                    datosFormulario.setDato90(valorDefecto10);
                    datosFormulario.setDato91(valorDefecto10);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato124(valorDefecto10);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato98(valorDefecto10);
                    datosFormulario.setDato99(valorDefecto10);
                    datosFormulario.setDato100(valorDefecto10);
                    datosFormulario.setDato101(valorDefecto10);
                    datosFormulario.setDato102(valorDefecto10);
                    datosFormulario.setDato103(valorDefecto10);
                    datosFormulario.setDato104(valorDefecto10);
                    datosFormulario.setDato105(valorDefecto10);
                    datosFormulario.setDato106(valorDefecto10);
                    datosFormulario.setDato107(valorDefecto10);
                    datosFormulario.setDato108(valorDefecto10);
                    datosFormulario.setDato109(valorDefecto10);
                    datosFormulario.setDato110(valorDefecto10);
                    datosFormulario.setDato111(valorDefecto10);
                    datosFormulario.setDato112(valorDefecto10);
                    datosFormulario.setDato113(valorDefecto10);
                    datosFormulario.setDato114(valorDefecto10);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato197(valorDefecto10);
                    datosFormulario.setDato198(valorDefecto10);
                    datosFormulario.setDato199(valorDefecto10);
                    datosFormulario.setDato200(valorDefecto10);
                }
                break;
            case 11:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato96(valorDefecto11);
                    datosFormulario.setDato97(valorDefecto11);
                    datosFormulario.setDato98(valorDefecto11);
                    datosFormulario.setDato99(valorDefecto11);
                    datosFormulario.setDato100(valorDefecto11);
                    datosFormulario.setDato101(valorDefecto11);
                    datosFormulario.setDato102(valorDefecto11);
                    datosFormulario.setDato103(valorDefecto11);
                    datosFormulario.setDato104(valorDefecto11);
                    datosFormulario.setDato105(valorDefecto11);
                    datosFormulario.setDato106(valorDefecto11);
                    datosFormulario.setDato107(valorDefecto11);
                    datosFormulario.setDato108(valorDefecto11);
                    datosFormulario.setDato109(valorDefecto11);
                    datosFormulario.setDato110(valorDefecto11);
                    datosFormulario.setDato111(valorDefecto11);
                    datosFormulario.setDato112(valorDefecto11);
                    datosFormulario.setDato113(valorDefecto11);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato92(valorDefecto11);
                    datosFormulario.setDato93(valorDefecto11);
                    datosFormulario.setDato94(valorDefecto11);
                    datosFormulario.setDato95(valorDefecto11);
                    datosFormulario.setDato96(valorDefecto11);
                    datosFormulario.setDato97(valorDefecto11);
                    datosFormulario.setDato98(valorDefecto11);
                    datosFormulario.setDato99(valorDefecto11);
                    datosFormulario.setDato100(valorDefecto11);
                    datosFormulario.setDato101(valorDefecto11);
                    datosFormulario.setDato102(valorDefecto11);
                    datosFormulario.setDato103(valorDefecto11);
                    datosFormulario.setDato104(valorDefecto11);
                    datosFormulario.setDato105(valorDefecto11);
                    datosFormulario.setDato106(valorDefecto11);
                    datosFormulario.setDato107(valorDefecto11);
                    datosFormulario.setDato108(valorDefecto11);
                    datosFormulario.setDato109(valorDefecto11);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato125(valorDefecto11);
                    datosFormulario.setDato126(valorDefecto11);
                    datosFormulario.setDato127(valorDefecto11);
                    datosFormulario.setDato128(valorDefecto11);
                    datosFormulario.setDato129(valorDefecto11);
                    datosFormulario.setDato130(valorDefecto11);
                    datosFormulario.setDato131(valorDefecto11);
                    datosFormulario.setDato147(valorDefecto11);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato115(valorDefecto11);
                    datosFormulario.setDato116(valorDefecto11);
                    datosFormulario.setDato117(valorDefecto11);
                    datosFormulario.setDato118(valorDefecto11);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato202(valorDefecto11);
                    datosFormulario.setDato205(valorDefecto11);
                    datosFormulario.setDato206(valorDefecto11);
                    datosFormulario.setDato207(valorDefecto11);
                    datosFormulario.setDato208(valorDefecto11);
                    datosFormulario.setDato209(valorDefecto11);
                    datosFormulario.setDato210(valorDefecto11);
                    datosFormulario.setDato211(valorDefecto11);
                    datosFormulario.setDato212(valorDefecto11);
                    datosFormulario.setDato215(valorDefecto11);
                    datosFormulario.setDato216(valorDefecto11);
                    datosFormulario.setDato217(valorDefecto11);
                }
                break;
            case 12:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato114(valorDefecto12);
                    datosFormulario.setDato115(valorDefecto12);
                    datosFormulario.setDato116(valorDefecto12);
                    datosFormulario.setDato117(valorDefecto12);
                    datosFormulario.setDato118(valorDefecto12);
                    datosFormulario.setDato119(valorDefecto12);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato110(valorDefecto12);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato148(valorDefecto12);
                    datosFormulario.setDato149(valorDefecto12);
                    datosFormulario.setDato150(valorDefecto12);
                    datosFormulario.setDato151(valorDefecto12);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato119(valorDefecto12);
                    datosFormulario.setDato120(valorDefecto12);
                    datosFormulario.setDato121(valorDefecto12);
                    datosFormulario.setDato122(valorDefecto12);
                    datosFormulario.setDato123(valorDefecto12);
                    datosFormulario.setDato124(valorDefecto12);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato218(valorDefecto12);
                    datosFormulario.setDato220(valorDefecto12);
                    datosFormulario.setDato221(valorDefecto12);
                    datosFormulario.setDato222(valorDefecto12);
                    datosFormulario.setDato223(valorDefecto12);
                    datosFormulario.setDato229(valorDefecto12);
                }
                break;
            case 13:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato123(valorDefecto13);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato111(valorDefecto13);
                    datosFormulario.setDato112(valorDefecto13);
                    datosFormulario.setDato113(valorDefecto13);
                    datosFormulario.setDato114(valorDefecto13);
                    datosFormulario.setDato115(valorDefecto13);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                    datosFormulario.setDato152(valorDefecto13);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato125(valorDefecto13);
                    datosFormulario.setDato126(valorDefecto13);
                    datosFormulario.setDato127(valorDefecto13);
                    datosFormulario.setDato128(valorDefecto13);
                    datosFormulario.setDato129(valorDefecto13);
                    datosFormulario.setDato130(valorDefecto13);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 35) {
                    datosFormulario.setDato230(valorDefecto13);
                    datosFormulario.setDato231(valorDefecto13);
                    datosFormulario.setDato232(valorDefecto13);
                    datosFormulario.setDato233(valorDefecto13);
                    datosFormulario.setDato234(valorDefecto13);
                    datosFormulario.setDato235(valorDefecto13);
                }
                break;
            case 14:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                    datosFormulario.setDato120(valorDefecto14);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato116(valorDefecto14);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato131(valorDefecto14);
                    datosFormulario.setDato132(valorDefecto14);
                    datosFormulario.setDato133(valorDefecto14);
                    datosFormulario.setDato134(valorDefecto14);
                    datosFormulario.setDato135(valorDefecto14);
                    datosFormulario.setDato136(valorDefecto14);
                    datosFormulario.setDato137(valorDefecto14);
                    datosFormulario.setDato138(valorDefecto14);
                    datosFormulario.setDato139(valorDefecto14);
                    datosFormulario.setDato140(valorDefecto14);
                    datosFormulario.setDato141(valorDefecto14);
                    datosFormulario.setDato142(valorDefecto14);
                    datosFormulario.setDato143(valorDefecto14);
                    datosFormulario.setDato144(valorDefecto14);
                    datosFormulario.setDato145(valorDefecto14);
                    datosFormulario.setDato146(valorDefecto14);
                    datosFormulario.setDato147(valorDefecto14);
                    datosFormulario.setDato148(valorDefecto14);
                    datosFormulario.setDato149(valorDefecto14);
                    datosFormulario.setDato150(valorDefecto14);
                    datosFormulario.setDato151(valorDefecto14);
                    datosFormulario.setDato152(valorDefecto14);
                    datosFormulario.setDato153(valorDefecto14);

                }
                break;
            case 15:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                    datosFormulario.setDato117(valorDefecto15);
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato154(valorDefecto15);
                    datosFormulario.setDato155(valorDefecto15);
                    datosFormulario.setDato156(valorDefecto15);
                    datosFormulario.setDato157(valorDefecto15);
                    datosFormulario.setDato158(valorDefecto15);
                    datosFormulario.setDato159(valorDefecto15);
                    datosFormulario.setDato160(valorDefecto15);
                    datosFormulario.setDato161(valorDefecto15);
                    datosFormulario.setDato162(valorDefecto15);
                    datosFormulario.setDato163(valorDefecto15);
                }
                break;
            case 16:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato164(valorDefecto16);
                }
                break;
            case 17:
                if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                    datosFormulario.setDato165(valorDefecto17);
                }
                break;
        }
    }

    public void agregar_fila_reporte_examenes() {
        if (tipo != 0 && fecha != null && examen.length() > 0) {
            FilaDataTable nuevaFila = new FilaDataTable();
            nuevaFila.setColumna1((lista_reportes.size() + 1) + "");
            nuevaFila.setColumna2(tipo + "");
            nuevaFila.setColumna3(sdfDate.format(fecha));
            nuevaFila.setColumna4(examen);
            lista_reportes.add(nuevaFila);
            tipo = 0;
            examen = "";
        } else {
            imprimirMensaje("Error", "Faltan datos", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void quitar_fila_reporte_examenes() {
        if (reporte_seleccionado == null) {
            imprimirMensaje("Error", "Debe seleccionar una fila", FacesMessage.SEVERITY_ERROR);
        } else {

            for (int i = 0; i < lista_reportes.size(); i++) {
                if (lista_reportes.get(i).getColumna1().compareTo(reporte_seleccionado.getColumna1()) == 0) {
                    lista_reportes.remove(i);
                    break;
                }
            }
            int j = 1;
            for (int i = 0; i < lista_reportes.size(); i++) {
                lista_reportes.get(i).setColumna1(j + "");
                j++;
            }
            reporte_seleccionado = null;
        }

    }

    public String tipo_reporte(String tipo_) {
        int tipo = Integer.parseInt(tipo_);
        if (tipo == 1) {
            return "HEMOGRAMA";
        } else if (tipo == 2) {
            return "GLICEMIA BASAL";
        } else if (tipo == 3) {
            return "HEMOGLOBINA GLICOSILADA";
        } else if (tipo == 4) {
            return "COLESTEROL TOTAL";
        } else if (tipo == 5) {
            return "TRIGLICERIDOS";
        } else if (tipo == 6) {
            return "HDL";
        } else if (tipo == 7) {
            return "LDL";
        } else if (tipo == 8) {
            return "PARCIAL DE ORINA";
        } else if (tipo == 9) {
            return "CREATININA SERICA";
        } else if (tipo == 10) {
            return "MICROALBULIMINURIA";
        } else if (tipo == 11) {
            return "ELECTROCARDIOGRAMA";
        }
        return null;
    }

    public void cargarEstructuraFamiliar() {
        listaEstructuraFamiliar = new ArrayList<>();
        List<CfgFamiliar> listaM = null;
        if (datosFormulario != null) {
            listaM = familiarFacade.findFamiliaresByPaciente(pacienteSeleccionado);
            System.out.println("buscando familiares del paciente ... " + pacienteSeleccionado.getIdPaciente());
        }

        if (listaM != null) {

            System.out.println("Fam encontrados " + listaM.size());

            for (CfgFamiliar cfgFamiliar : listaM) {
                FilaDataTable r = new FilaDataTable();
                r.setColumna1(cfgFamiliar.getNombre());
                r.setColumna2(cfgFamiliar.getEdad().toString());
                r.setColumna3(cfgFamiliar.getIdParentesco().getId().toString());
                r.setColumna4(cfgFamiliar.getIdOcupacion().getId().toString());
                r.setColumna5(cfgFamiliar.getIdParentesco().getDescripcion());
                r.setColumna6(cfgFamiliar.getIdOcupacion().getDescripcion());
                r.setColumna7(cfgFamiliar.getIdEstructuraFamiliar().toString());
                r.setColumna8(cfgFamiliar.getIdPaciente().getIdPaciente().toString());
                listaEstructuraFamiliar.add(r);
            }

            System.out.println("listaEstructuraFamiliar.size = " + listaEstructuraFamiliar.size());
        } else {

            System.out.println("Fam no encontrados ");
        }

    }

    public List<FilaDataTable> cargarListaMedicamentos(HcRegistro registro) {
        listaMedicamentos = new ArrayList<>();
        List<HcItems> listaM = null;
        if (datosFormulario != null) {
            listaM = itemsFacade.findByIdRegistro(registro);
            System.out.println("buscando medicamentos  de ... " + pacienteSeleccionado.getIdPaciente());
        }

        List<FilaDataTable> listaItems = new ArrayList<>();

        if (listaM != null) {
            System.out.println("reg encontrados " + listaM.size());

            for (HcItems itemMedicamento : listaM) {

                if (itemMedicamento.getIdTabla() != null) {
                    CfgMedicamento medicamentoEncontrado = cfgMedicamento.find(Integer.parseInt(itemMedicamento.getIdTabla()));

                    FilaDataTable r = new FilaDataTable();
                    r.setColumna1(medicamentoEncontrado.getCodigoMedicamento());
                    r.setColumna2(medicamentoEncontrado.getNombreMedicamento());
                    r.setColumna3(itemMedicamento.getCantidad().toString());
                    r.setColumna4(itemMedicamento.getDosis());
                    r.setColumna5(medicamentoEncontrado.getFormaMedicamento());
                    r.setColumna6(medicamentoEncontrado.getConcentracion());
                    r.setColumna7(medicamentoEncontrado.getModAdmin());
                    r.setColumna8(itemMedicamento.getPosologia());
                    r.setColumna9(itemMedicamento.getObservacion());
                    listaItems.add(r);
                }
            }

            System.out.println("medicamentos.size = " + listaItems.size());
        } else {

            System.out.println("medicamentos no encontrados ");
        }
        return listaItems;

    }

    public List<FilaDataTable> cargarListaServicios(HcRegistro registro) {
        listaServicios = new ArrayList<>();
        List<HcItems> listaM = null;
        if (datosFormulario != null) {
            listaM = itemsFacade.findByIdRegistro(registro);
            System.out.println("buscando servicios  de ... " + pacienteSeleccionado.getIdPaciente());
        }

        List<FilaDataTable> listaItems = new ArrayList<>();

        if (listaM != null) {
            System.out.println("reg encontrados " + listaM.size());

            for (HcItems itemS : listaM) {

                FacServicio servicioEncontrado = servicioFacade.buscarPorIdServicio(Integer.parseInt(itemS.getIdTabla()));

                FilaDataTable r = new FilaDataTable();
                r.setColumna1(servicioEncontrado.getCodigoServicio());
                r.setColumna2(servicioEncontrado.getNombreServicio());
                r.setColumna3(itemS.getCantidad().toString());
                r.setColumna4(itemS.getObservacion());
                listaItems.add(r);
            }

            System.out.println("servicios.size = " + listaItems.size());
        } else {

            System.out.println("servicios no encontrados ");
        }
        return listaItems;

    }

    @PostConstruct
    public void inicializar() {
        recargarMaestrosTxtPredefinidos();
//        listaPacientes = pacientesFacade.buscarOrdenado();
        //listaPacientes = pacientesFacade.buscarOrdenado();
        //listaPacientesFiltro = new ArrayList<>();
        //listaPacientesFiltro.addAll(listaPacientes);
        listaTipoRegistroClinico = tipoRegCliFacade.buscarTiposRegstroActivos();
        listaPrestadores = usuariosFacade.buscarUsuariosParaHistorias();
        listaOdontologos = new ArrayList<>();
        for (CfgUsuarios usuario : listaPrestadores) {
            if (usuario.getEspecialidad() != null) {
                if (Pattern.matches("460|461|462|463", usuario.getEspecialidad().getCodigo())) {
                    listaOdontologos.add(usuario);
                }
            }
        }
        seleccionaTodosRegCliHis();
        empresa = empresaFacade.findAll().get(0);

        listaArchivo = new ArrayList();

        medicamentoSeleccionado = new FilaDataTable();
        servicioSeleccionado = new FilaDataTable();
        codigo = "";
        descripcion = "";
        cantidad = 0;
        dosis = "";
        presentacion = "";
        concentracion = "";
        viaAdmin = "";
        posologia = "";
        observacion = "";
        loadServiciosOrden();

    }

    private void valoresPorDefecto() {
        if (tipoRegistroClinicoActual != null) {//validacion particular para asignar valores por defecto             
            if (tipoRegistroClinicoActual.getIdTipoReg() == 71) {
               loadGraphic();
            }else if(tipoRegistroClinicoActual.getIdTipoReg()==70 || tipoRegistroClinicoActual.getIdTipoReg()==69){
             loadGraphicValoracion0_18();   
            }else if (tipoRegistroClinicoActual.getIdTipoReg() == 8) {//SOLICITUD DE AUTORIZACION DE SERVICIOS
                datosFormulario.setDato4("Prioritaria");//prioridad de la atencion                
                datosFormulario.setDato0(pacienteSeleccionado.getIdAdministradora().getRazonSocial());//pagador 
                datosFormulario.setDato1(pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora());//codigo
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 19) {//FOrmulación Médica
                //Cargamos los diágnostico principales y 
                String diagPrincipal = detalleFacade.diagnosticoPrincipal(pacienteSeleccionado.getIdPaciente());
                datosFormulario.setDato0(diagPrincipal);
                String diagRel1 = detalleFacade.diagnosticoRel1(pacienteSeleccionado.getIdPaciente());
                String diagRel2 = detalleFacade.diagnosticoRel2(pacienteSeleccionado.getIdPaciente());
                datosFormulario.setDato1(diagRel1);
                datosFormulario.setDato2(diagRel2);
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 12) {
                //citologiaDefaultParams("No Refiere"); Citologia
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 20) {
                //urgenciasDefaultParams("No Refiere"); Historia clinica Urgencias
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 80) {
                resultadoCitologiaDefaultParams("No Refiere");
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 81) {
                suministroPlanificacionFamiliarDefaultParams("No Refiere");
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 82) {
                //evolucionHistoriaGlandulaMamariaDefaultParams("No Refiere"); Glandula mamaria
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 23) {
                //historiaClinicaDefaultParams("No Refiere"); Historiaclinica
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 37) {
                //clapOpsOmsDefaultParams("No Refiere"); CLAP
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 29) {
                epicrisisDefaultParams("No Refiere");
                /* 
                } else if (tipoRegistroClinicoActual.getIdTipoReg() == 78 || tipoRegistroClinicoActual.getIdTipoReg() == 79) { //Cancer cervix y seno
                cancerCervixSenoDefaultParams("No Refiere");*/
            } else if (tipoRegistroClinicoActual.getIdTipoReg() == 21) {
                // refContrRefDefaultParams("No Refiere"); Referencia y contrareferencia
                cargarMunicipiosDefault();
            }
        }
        //Para que no reemplace los cambpos de historias con valores por defecto
        if (tipoRegistroClinicoActual != null
                && tipoRegistroClinicoActual.getIdTipoReg() != 12
                && tipoRegistroClinicoActual.getIdTipoReg() != 20
                && tipoRegistroClinicoActual.getIdTipoReg() != 21
                && tipoRegistroClinicoActual.getIdTipoReg() != 80
                && tipoRegistroClinicoActual.getIdTipoReg() != 81
                && tipoRegistroClinicoActual.getIdTipoReg() != 82
                && tipoRegistroClinicoActual.getIdTipoReg() != 23
                && tipoRegistroClinicoActual.getIdTipoReg() != 29
                && tipoRegistroClinicoActual.getIdTipoReg() != 78
                && tipoRegistroClinicoActual.getIdTipoReg() != 79) {
            calculo_imc();
            calculo_imc_perimetro();
            calculo_imc_peso();
            clasificacion_fisica();
        }
    }
    
    private void loadGraphicValoracion0_18() {
        try {
            List<Long> listaPeso = new ArrayList();
            List<Long> listaTalla = new ArrayList();
            LineChartModel model = new LineChartModel();
            LineChartSeries seriesLineal = new LineChartSeries();
            seriesLineal.setLabel("Talla (cm)");
            List<HcDetalle> lista = detalleFacade.getValoresGraficasAnnio(pacienteSeleccionado.getIdPaciente(), 3, tipoRegistroClinicoActual.getIdTipoReg());
            for (HcDetalle hc : lista) {
                seriesLineal.set(hc.getValorX(), Long.valueOf(hc.getValor()));
                listaTalla.add(Long.valueOf(hc.getValor()));
            }

            model.addSeries(seriesLineal);
            lineModel1 = model;
            if(tipoRegistroClinicoActual.getIdTipoReg()==70){
                lineModel1.setTitle("Talla para la edad Niños de 5 a 18 años");
            }else{
                lineModel1.setTitle("Talla para la edad Niñas de 5 a 18 años");
            }
            lineModel1.setLegendPosition("e");
            lineModel1.setShowPointLabels(true);

            lineModel1.setLegendPlacement(LegendPlacement.OUTSIDE);
            Axis xAxis = lineModel1.getAxis(AxisType.X);
            xAxis.setMin(5);
            xAxis.setMax(18);
            xAxis.setTickInterval("1");
            xAxis.setLabel("Edad en Años");

            Axis yAxis = lineModel1.getAxis(AxisType.Y);
            yAxis.setLabel("Talla (cm)");
            yAxis.setMin(90);
            yAxis.setMax(200);
            yAxis.setTickInterval("10");
            //#4F4B4A

            lineModel1.setSeriesColors("D9300B");

            //cargamos peso
            lista = detalleFacade.getValoresGraficasAnnio(pacienteSeleccionado.getIdPaciente(), 2, tipoRegistroClinicoActual.getIdTipoReg());
            for (HcDetalle hc : lista) {
                listaPeso.add(Long.valueOf(hc.getValor()));
            }

            //IMC
            model = new LineChartModel();
            seriesLineal = new LineChartSeries();
            seriesLineal.setLabel("IMC (kg/m2)");
            for (int i = 0; i < lista.size(); i++) {
                float pesoC = listaPeso.get(i).floatValue();
                float tallaC = listaTalla.get(i).floatValue();
                float metros = tallaC / 100;
                float imcCalculado = pesoC / (metros * metros);
                seriesLineal.set(lista.get(i).getValorX(), imcCalculado);
            }

            model.addSeries(seriesLineal);
            lineModel2 = model;
            if(tipoRegistroClinicoActual.getIdTipoReg()==70){
                lineModel2.setTitle("IMC para la edad Niños de 5 a 18 años");
            }else{
                lineModel2.setTitle("IMC para la edad Niñas de 5 a 18 años");
            }
                    
            
            lineModel2.setLegendPosition("e");
            lineModel2.setShowPointLabels(true);
            //lineModel3.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
            lineModel2.setLegendPlacement(LegendPlacement.OUTSIDE);

            xAxis = lineModel2.getAxis(AxisType.X);
            xAxis.setMin(5);
            xAxis.setMax(18);
            xAxis.setTickInterval("1");
            xAxis.setLabel("Edad en Años");

            yAxis = lineModel2.getAxis(AxisType.Y);
            yAxis.setLabel("IMC (kg/m2)");
            yAxis.setTickInterval("2");
            yAxis.setMin(11);
            yAxis.setMax(36);

            lineModel2.setSeriesColors("D9300B");
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    private void loadGraphic(){
         try{
                    List<Long> listaPeso = new ArrayList();
                    List<Long> listaTalla = new ArrayList();
                    //Cargamos peso
                    LineChartModel model = new LineChartModel();
                    LineChartSeries series1 = new LineChartSeries();
                    
                    series1.setLabel("Peso (kg)");
                    List<HcDetalle> lista = detalleFacade.getValoresGraficas(pacienteSeleccionado.getIdPaciente(), 0, tipoRegistroClinicoActual.getIdTipoReg());
                    for(HcDetalle hc:lista){
                        series1.set(hc.getValorX(), Long.valueOf(hc.getValor()));
                        listaPeso.add(Long.valueOf(hc.getValor()));
                    }
                    
                    model.addSeries(series1);
                    lineModel1 = model;
                    lineModel1.setTitle("Peso para la edad Niñas de 0 a 2 años");
                    lineModel1.setLegendPosition("e");
                    lineModel1.setShowPointLabels(true);
                    
                     lineModel1.setLegendPlacement(LegendPlacement.OUTSIDE);
                     Axis xAxis = lineModel1.getAxis(AxisType.X);
                     xAxis.setMin(0);
                     xAxis.setMax(24);
                     xAxis.setTickInterval("1");
                     xAxis.setLabel("Meses");
                     
                     Axis yAxis = lineModel1.getAxis(AxisType.Y);
                     yAxis.setLabel("Peso (kg)");
                     yAxis.setMin(0);
                     yAxis.setMax(18);
                     yAxis.setTickInterval("1");
                    //#4F4B4A
                    
                    
                    lineModel1.setSeriesColors("D9300B");
                    
                    /////FIN PESO
                    //Cargamos talla
                    model = new LineChartModel();
                    series1 = new LineChartSeries();
                    series1.setLabel("Talla (cm)");
                    lista = detalleFacade.getValoresGraficas(pacienteSeleccionado.getIdPaciente(), 1, tipoRegistroClinicoActual.getIdTipoReg());
                    for(HcDetalle hc:lista){
                        series1.set(hc.getValorX(), Long.valueOf(hc.getValor()));
                        listaTalla.add(Long.valueOf(hc.getValor()));
                    }
                    
                    model.addSeries(series1);
                    lineModel2 = model;
                    lineModel2.setTitle("Talla para la edad Niñas de 0 a 2 años");
                    lineModel2.setLegendPosition("e");
                    lineModel2.setLegendPlacement(LegendPlacement.OUTSIDE);
                    lineModel2.setShowPointLabels(true);
                    //lineModel2.getAxes().put(AxisType.X);
                    
                     xAxis = lineModel2.getAxis(AxisType.X);
                     xAxis.setMin(0);
                     xAxis.setMax(24);
                     xAxis.setTickInterval("1");
                     xAxis.setLabel("Meses");
                     
                    /*,,,,,4F4B4A,*/
                    yAxis = lineModel2.getAxis(AxisType.Y);
                    yAxis.setTickInterval("5");
                    yAxis.setLabel("Talla (cm)");
                    yAxis.setMin(40);
                    yAxis.setMax(95);
                    
                    lineModel2.setSeriesColors("D9300B");

                    //Cargamos imc
                    
                    //valores defecto
                     
                    
                    model = new LineChartModel();
                    series1 = new LineChartSeries();
                    series1.setLabel("IMC (kg/m2)");
                    for(int i=0;i<lista.size();i++){
                        float pesoC= listaPeso.get(i).floatValue();
                        float tallaC= listaTalla.get(i).floatValue();
                        float metros = tallaC / 100;
                        float imcCalculado = pesoC / (metros * metros);
                        series1.set(lista.get(i).getValorX(), imcCalculado);
                    }

                    model.addSeries(series1);
                    lineModel3 = model;
                    lineModel3.setTitle("IMC para la edad Niñas de 0 a 2 años");
                    lineModel3.setLegendPosition("e");
                    lineModel3.setShowPointLabels(true);
                    //lineModel3.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
                    lineModel3.setLegendPlacement(LegendPlacement.OUTSIDE);
                    
                     xAxis = lineModel3.getAxis(AxisType.X);
                     xAxis.setMin(0);
                     xAxis.setMax(24);
                     xAxis.setTickInterval("1");
                     xAxis.setLabel("Meses");
                    
                    yAxis = lineModel3.getAxis(AxisType.Y);
                    yAxis.setLabel("IMC (kg/m2)");
                    yAxis.setTickInterval("1");
                    yAxis.setMin(9);
                    yAxis.setMax(22);

                    
                    lineModel3.setSeriesColors("D9300B");
                    //lineModel3.getAxes().put(AxisType.Y2, y2Axis);
                    //Cargamos perimetro cefalico
                    
                    
                    model = new LineChartModel();
                    series1 = new LineChartSeries();
                    series1.setLabel("Perímetro Cefálico (cm)");
                    lista = detalleFacade.getValoresGraficas(pacienteSeleccionado.getIdPaciente(), 2, tipoRegistroClinicoActual.getIdTipoReg());
                    for(HcDetalle hc:lista){
                        series1.set(hc.getValorX(), Long.valueOf(hc.getValor()));
                    }
                    
                    model.addSeries(series1);
                    lineModel4 = model;
                    lineModel4.setTitle("Perímetro Cefálico para la edad Niñas de 0 a 2 años");
                    lineModel4.setLegendPosition("e");
                    lineModel4.setShowPointLabels(true);
                    lineModel4.setLegendPlacement(LegendPlacement.OUTSIDE);
                    //lineModel4.getAxes().put(AxisType.X, new CategoryAxis("Meses"));
                    
                    xAxis = lineModel4.getAxis(AxisType.X);
                     xAxis.setMin(0);
                     xAxis.setMax(24);
                     xAxis.setTickInterval("1");
                     xAxis.setLabel("Meses");
                    
                    yAxis = lineModel4.getAxis(AxisType.Y);
                    yAxis.setLabel("Perímetro Cefálico (cm)");
                    yAxis.setTickInterval("2");
                    yAxis.setMin(30);
                    yAxis.setMax(56);
                    
                    
                    //lineModel4.getAxes().put(AxisType.Y2, y2Axis);
                    lineModel4.setSeriesColors("D9300B");
                    
                }catch(Exception ex){
                    
                }
    }
    public void cargarDialogoAgregarFamiliar() {

        nombreFamiliar = "";
        edadFamiliar = "";
        parentescoFamiliar = "";
        ocupacionFamiliar = "";

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarFamiliar");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarFamiliar').show();");
    }

    public void cargarDialogoAgregarItemMedicamento() {
        System.out.println("From cargarDialogoAgregarItemMedicamento()");

        medicamentoSeleccionado = new FilaDataTable();
        codigo = "";
        descripcion = "";
        cantidad = 0;
        dosis = "";
        presentacion = "";
        concentracion = "";
        viaAdmin = "";
        posologia = "";
        observacion = "";

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarItemMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarItemMedicamento').show();");
    }

    public void cargarDialogoAgregarItemServicio() {
        System.out.println("From cargarDialogoAgregarItemOrdenMedica()");

        servicioSeleccionado = new FilaDataTable();
        codigo = "";
        descripcion = "";
        cantidad = 0;
        observacion = "";

        RequestContext.getCurrentInstance().update("IdFormDialogs:IdPanelAgregarItemServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarItemServicio').show();");
    }

    public void quitarFamiliar() {

        System.out.println("From quitarFamiliar()" + listaEstructuraFamiliar.size());

        CfgFamiliar familiar = new CfgFamiliar();
        familiar.setIdEstructuraFamiliar(Integer.parseInt(familiarSeleccionado.getColumna7()));
        familiar.setNombre(familiarSeleccionado.getColumna1());
        familiar.setEdad(Integer.parseInt(familiarSeleccionado.getColumna2()));
        familiar.setIdParentesco(new CfgClasificaciones(Integer.parseInt(familiarSeleccionado.getColumna3())));
        familiar.setIdOcupacion(new CfgClasificaciones(Integer.parseInt(familiarSeleccionado.getColumna4())));
        familiar.setIdPaciente(new CfgPacientes(Integer.parseInt(familiarSeleccionado.getColumna8())));

        familiarFacade.remove(familiar); //quitar de base de datos

        listaEstructuraFamiliar.remove(familiarSeleccionado); //quitar de lista
        System.out.println("/From quitarFamiliar()" + listaEstructuraFamiliar.size());

        RequestContext.getCurrentInstance().update("tablaEstructuraFamiliar");
        RequestContext.getCurrentInstance().execute("PF('wvtablaEstructuraFamiliar').clearFilters(); PF('wvtablaEstructuraFamiliar').getPaginator().setPage(0);");
    }

    public void quitarItemMedicamento() {

        System.out.println("From quitarItemMedicamento()" + listaMedicamentos.size());

        listaMedicamentos.remove(medicamentoSeleccionado); //quitar de lista
        System.out.println("/From quitarItemMedicamento()" + listaMedicamentos.size());
        RequestContext.getCurrentInstance().execute("PF('wvtablaMedicamentos').clearFilters(); PF('wvtablaMedicamentos').getPaginator().setPage(0);");

        RequestContext.getCurrentInstance().update("tablaMedicamentos");
        RequestContext.getCurrentInstance().update("IdPanelAgregarItemMedicamento");
    }

    public void quitarItemServicio() {

        System.out.println("From quitarItemServicio()" + listaServicios.size());

        listaServicios.remove(servicioSeleccionado); //quitar de lista
        System.out.println("/From quitarItemServicio()" + listaServicios.size());

        RequestContext.getCurrentInstance().execute("PF('wvtablaServicios').clearFilters(); PF('wvtablaServicios').getPaginator().setPage(0);");

        RequestContext.getCurrentInstance().update("tablaAgregarItemServicio");
        RequestContext.getCurrentInstance().update("IdPanelAgregarItemServicio");
    }

    public void cerrar() {
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarFamiliar').hide(); PF('wvtablaEstructuraFamiliar').clearFilters(); PF('wvtablaEstructuraFamiliar').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("tablaEstructuraFamiliar");
    }

    public void agregarFamiliar() {
        System.out.println("From agregarFamiliar()");

        if (validacionCampoVacio(nombreFamiliar, "Nombre")) {
            return;
        }
        if (validacionCampoVacio(edadFamiliar, "Edad")) {
            return;
        }
        if (validacionCampoVacio(parentescoFamiliar, "Parentesco")) {
            return;
        }

        familiarSeleccionado = new FilaDataTable();
        familiarSeleccionado.setColumna1(nombreFamiliar);
        familiarSeleccionado.setColumna2(edadFamiliar);
        familiarSeleccionado.setColumna3(parentescoFamiliar);
        familiarSeleccionado.setColumna4(ocupacionFamiliar);

        CfgClasificaciones clasificacion;
        if (parentescoFamiliar != null) {
            clasificacion = clasificacionesFacade.find(Integer.parseInt(parentescoFamiliar));
            if (clasificacion != null) {
                familiarSeleccionado.setColumna5(clasificacion.getDescripcion());
            }
        } else {
            familiarSeleccionado.setColumna5("");

        }
        System.out.println("ocupacionFamiliar " + ocupacionFamiliar);
        if (ocupacionFamiliar != null) {
            clasificacion = clasificacionesFacade.find(Integer.parseInt(ocupacionFamiliar));
            if (clasificacion != null) {
                familiarSeleccionado.setColumna6(clasificacion.getDescripcion());
            }
        } else {
            familiarSeleccionado.setColumna6("");
        }

        listaEstructuraFamiliar.add(familiarSeleccionado);
        RequestContext.getCurrentInstance().execute("PF('wvtablaEstructuraFamiliar').clearFilters(); PF('wvtablaEstructuraFamiliar').getPaginator().setPage(0);");

        nombreFamiliar = "";
        edadFamiliar = "";
        parentescoFamiliar = "";
        ocupacionFamiliar = "";
        RequestContext.getCurrentInstance().update("tablaEstructuraFamiliar");

        System.out.println("/From agregarFamiliar()");

    }

    public void agregarMedicamento() {
        System.out.println("From agregarMedicamento()");
        if (validacionCampoVacio(codigo, "Codigo")) {
            return;
        }
        if (validacionCampoVacio(descripcion, "Descripcion")) {
            return;
        }
        if (validacionCampoVacio(dosis, "Dosis")) {
            return;
        }
        if (validacionCampoMayorCero(cantidad, "Cantidad")) {
            return;
        }
        if (validacionCampoVacio(presentacion, "Presentación")) {
            return;
        }
        if (validacionCampoVacio(concentracion, "Contentración")) {
            return;
        }
        if (validacionCampoVacio(viaAdmin, "Vía de administración")) {
            return;
        }
        if (validacionCampoVacio(posologia, "Posología")) {
            return;
        }

        medicamentoSeleccionado = new FilaDataTable();

        System.out.println("Id Medicamento " + idMedicamento);

        CfgMedicamento medicamento = cfgMedicamento.find(Integer.parseInt(idMedicamento));

        System.out.println("Medicamento " + medicamento.getNombreMedicamento());

        medicamentoSeleccionado.setColumna1(idMedicamento);
        medicamentoSeleccionado.setColumna2(codigo);
        medicamentoSeleccionado.setColumna3(descripcion);
        medicamentoSeleccionado.setColumna4(cantidad.toString());
        medicamentoSeleccionado.setColumna5(dosis);
        medicamentoSeleccionado.setColumna6(presentacion);
        medicamentoSeleccionado.setColumna7(concentracion);
        medicamentoSeleccionado.setColumna8(viaAdmin);
        medicamentoSeleccionado.setColumna9(posologia);
        medicamentoSeleccionado.setColumna10(observacion);
        medicamentoSeleccionado.setColumna11("cfg_medicamento");

        idMedicamento = "";
        codigo = "";
        descripcion = "";
        cantidad = 0;
        dosis = "";
        presentacion = "";
        concentracion = "";
        viaAdmin = "";
        posologia = "";
        observacion = "";

        listaMedicamentos.add(medicamentoSeleccionado);
        RequestContext.getCurrentInstance().execute("PF('wvtablaMedicamentos').clearFilters(); PF('wvtablaMedicamentos').getPaginator().setPage(0);");

        RequestContext.getCurrentInstance().update("tablaMedicamentos");
        RequestContext.getCurrentInstance().update("IdPanelAgregarItemMedicamento");

        System.out.println("/From agregarMedicamento()");

    }

    public void agregarServicio() {
        System.out.println("From agregarServicio()");

        if (validacionCampoVacio(codigo, "Código")) {
            return;
        }
        if (validacionCampoVacio(idServicio, "Servicio")) {
            return;
        }
        if (validacionCampoVacio(cantidad.toString(), "Cantidad")) {
            return;
        }

        servicioSeleccionado = new FilaDataTable();

        System.out.println("Id servicio " + idServicio);

        servicioSeleccionado.setColumna1(idServicio);
        servicioSeleccionado.setColumna2(codigo);
        servicioSeleccionado.setColumna3(descripcion);
        servicioSeleccionado.setColumna4(cantidad.toString());
        servicioSeleccionado.setColumna5(observacion);
        servicioSeleccionado.setColumna6("fac_servicio");

        idServicio = "";
        codigo = "";
        descripcion = "";
        cantidad = 0;
        dosis = "";
        presentacion = "";
        concentracion = "";
        viaAdmin = "";
        posologia = "";
        observacion = "";

        listaServicios.add(servicioSeleccionado);
        RequestContext.getCurrentInstance().execute("PF('wvtablaServicios').clearFilters(); PF('wvtablaServicios').getPaginator().setPage(0);");

        RequestContext.getCurrentInstance().update("tablaAgregarItemServicio");
        RequestContext.getCurrentInstance().update("IdPanelAgregarItemServicio");

        System.out.println("/From agregarServicio()");

    }

    public void cerrarFormMedicamentos() {
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarItemMedicamento').hide(); PF('wvtablaMedicamentos').clearFilters(); PF('wvtablaMedicamentos').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("tablaAgregarItemMedicamento");
    }

    public void cerrarFormServicios() {
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarItemServicio').hide(); PF('wvtablaServicios').clearFilters(); PF('wvtablaServicios').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("tablaAgregarItemServicio");
    }

    public void cambiarMedicamento() {
        System.out.println("Id Medicamento " + idMedicamento);
        CfgMedicamento medicamento = cfgMedicamento.find(Integer.parseInt(idMedicamento));
        codigo = medicamento.getCodigoMedicamento();
        descripcion = medicamento.getCodigoMedicamento();

        presentacion = medicamento.getFormaMedicamento();
        concentracion = medicamento.getConcentracion();
        viaAdmin = medicamento.getModAdmin();

        System.out.println("Medicamento " + medicamento.getNombreMedicamento());
    }

    public void cambiarServicio() {
        System.out.println("Id Servicio " + idServicio);
        FacServicio servicio = servicioFacade.buscarPorIdServicio(Integer.parseInt(idServicio));
        codigo = servicio.getCodigoServicio();
        descripcion = servicio.getNombreServicio();

        System.out.println("Servicio " + servicio.getNombreServicio());
    }

    private String calcularIMC(float peso, float talla) {

        //Infrapeso	IMC menos de 18.5
        //Peso Normal	IMC de 18.5 a 24.9
        //Sobrepeso	IMC de 25 a 29.9
        //Obesidad	IMC 30 o mayor
        //Obesidad Mórbida	IMC 40 o mayor
        //        
        float metros = talla / 100;
        float imcCalculado = peso / (metros * metros);

        String textoImc = "";
        if (imcCalculado < 18.5) {
            textoImc = "Infrapeso";
        } else if (imcCalculado >= 18.5 && imcCalculado < 25) {
            textoImc = "Peso Normal";
        } else if (imcCalculado >= 25 && imcCalculado < 30) {
            textoImc = "Sobrepeso";
        } else if (imcCalculado >= 30 && imcCalculado < 40) {
            textoImc = "Obesidad";
        } else if (imcCalculado >= 40) {
            textoImc = "Obesidad Mórbida";
        }
        double imcCalculadoD = (double) ((int) (imcCalculado * 100) / 100.0);
        return String.valueOf(imcCalculadoD) + ", " + textoImc;
    }

    public void calcularIMCHisCliTriage() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato28().toString());
            float talla = Float.parseFloat(datosFormulario.getDato29().toString());
            datosFormulario.setDato30(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCMaterna() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato148().toString());
            float talla = Float.parseFloat(datosFormulario.getDato149().toString());
            datosFormulario.setDato150(calcularIMC(peso, talla));

        } catch (NumberFormatException e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCHisCliUrgencias() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato65().toString());
            float talla = Float.parseFloat(datosFormulario.getDato66().toString());
            datosFormulario.setDato67(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCHisCliVisual() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato20().toString());
            float talla = Float.parseFloat(datosFormulario.getDato21().toString());
            datosFormulario.setDato22(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCCitologia() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato132().toString());
            float talla = Float.parseFloat(datosFormulario.getDato133().toString());
            datosFormulario.setDato134(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCHisRefContraRef() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato18().toString());
            float talla = Float.parseFloat(datosFormulario.getDato19().toString());
            datosFormulario.setDato20(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void calcularIMCHisClinica() {
        try {
            float peso = Float.parseFloat(datosFormulario.getDato60().toString());
            float talla = Float.parseFloat(datosFormulario.getDato61().toString());
            datosFormulario.setDato62(calcularIMC(peso, talla));

        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
    }

    public void cargarDesdeTab(String id) {//cargar paciente y desde Tab agenda prestador                
        cargandoDesdeTab = true;
        turnoCita = "";
        citaActual = null;
        limpiarFormulario();
        String[] splitId = id.split(";");
        if (splitId[0].compareTo("idCita") == 0) {
            citaActual = citasFacade.find(Integer.parseInt(splitId[1]));
            //BUSCAR PACIENTE
            pacienteTmp = pacientesFacade.find(citaActual.getIdPaciente().getIdPaciente());
            if (pacienteTmp == null) {
                imprimirMensaje("Error", "No se encontró el paciente correspondiente a la cita", FacesMessage.SEVERITY_ERROR);
                cargandoDesdeTab = false;
                RequestContext.getCurrentInstance().update("IdFormFacturacion");
                RequestContext.getCurrentInstance().update("IdMensajeFacturacion");
                return;
            }
            pacienteSeleccionadoTabla = pacienteTmp;
            cargarPaciente();
            turnoCita = citaActual.getIdTurno().getIdTurno().toString();
        }
        RequestContext.getCurrentInstance().update("IdMensajeFacturacion");
        cargandoDesdeTab = false;
    }

    //---------------------------------------------------
    //----------------- HISTORICO -----------------------
    //---------------------------------------------------
    private void seleccionaTodosRegCliHis() {//seleccionar todos los tipos de registros clinicos para el historial
        regCliSelHistorial = new String[listaTipoRegistroClinico.size()];
        for (int i = 0; i < listaTipoRegistroClinico.size(); i++) {
            regCliSelHistorial[i] = listaTipoRegistroClinico.get(i).getIdTipoReg().toString();
        }
    }

    public void cargarHistorialCompleto() {
        btnHistorialDisabled = true;
        btnPdfAgrupadoDisabled = true;
        seleccionaTodosRegCliHis();//selecciona todos los tipos de registros clinicos
        cargarHistorial();//realiza la creacion del arbol historial
    }

    public void cargarHistorial() {
        //genera un arbol con los registros clinicos de un paciente(depende de los seleccionados en 'IdComboTipReg')

        treeNodeRaiz = new DefaultTreeNode(new NodoArbolHistorial(TipoNodoEnum.RAIZ_HISTORIAL, null, -1, -1, null, null), null);
        NodoArbolHistorial nodoSeleccionTodoNada = new NodoArbolHistorial(TipoNodoEnum.NOVALUE, null, -1, -1, null, "Selección Todo/Ninguno");

        TreeNode nodoInicial = new DefaultTreeNode(nodoSeleccionTodoNada, treeNodeRaiz);
        nodoInicial.setExpanded(true);

        treeNodesSeleccionados = null;
        btnPdfAgrupadoDisabled = true;
        btnHistorialDisabled = true;
        boolean exitenRegistros = false;
        boolean usarFiltroFechas = false;
        boolean usarFiltroAutorizacion = false;

        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "No hay un paciente seleccionado", FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
            return;
        }
        if ((filtroFechaInicial == null && filtroFechaFinal != null) || (filtroFechaInicial != null && filtroFechaFinal == null)) {
            imprimirMensaje("Error", "Debe especificar fecha inicial y fecha final al tiempo ", FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
            return;
        }
        if (filtroFechaInicial != null && filtroFechaFinal != null) {
            usarFiltroFechas = true;
        }

        if (validarNoVacio(filtroAutorizacion)) {
            usarFiltroAutorizacion = true;
        }
        List<HcRegistro> listaRegistrosPaciente;
        if (usarFiltroFechas && usarFiltroAutorizacion) {//usar dos filtros
            listaRegistrosPaciente = registroFacade.buscarFiltradoPorNumeroAutorizacionYFecha(pacienteSeleccionado.getIdPaciente(), filtroFechaInicial, filtroFechaFinal, filtroAutorizacion);
        } else if (usarFiltroFechas) {// usar filtro de fecha
            listaRegistrosPaciente = registroFacade.buscarOrdenado(pacienteSeleccionado.getIdPaciente(), filtroFechaInicial, filtroFechaFinal);
        } else if (usarFiltroAutorizacion) {//usar filtro de autorizacion
            listaRegistrosPaciente = registroFacade.buscarFiltradoPorNumeroAutorizacion(pacienteSeleccionado.getIdPaciente(), filtroAutorizacion);
        } else {//no usar filtro, se busca todos
            listaRegistrosPaciente = registroFacade.buscarOrdenadoPorFecha(pacienteSeleccionado.getIdPaciente());// pacienteSeleccionado.getHcRegistroList();//buscar por orden de fecha decendente            
        }

        NodoArbolHistorial nodHisNuevo;//esta dentro de un TreeNode
        NodoArbolHistorial nodHisFecha;//
        NodoArbolHistorial nodHisComparar;//
        TreeNode treeNodeFecha = null;//son realmente nodos del arbol(contiene a un NodoArbolIstoria)
        TreeNode treeNodeCreado;
        for (HcRegistro registro : listaRegistrosPaciente) {
            if (estaEnListaTipRegHis(registro.getIdTipoReg().getIdTipoReg().toString())) {//verificar que este en la lista de tipos de registro que se desea listar
                exitenRegistros = true;
                nodHisNuevo = new NodoArbolHistorial(
                        TipoNodoEnum.REGISTRO_HISTORIAL,
                        registro.getFechaReg(),
                        registro.getIdRegistro(),
                        registro.getIdTipoReg().getIdTipoReg(),
                        registro.getIdTipoReg().getNombre(),
                        registro.getIdMedico().nombreCompleto());
                if (treeNodeFecha == null) {//nose han agregado nodos
                    nodHisFecha = new NodoArbolHistorial(TipoNodoEnum.FECHA_HISTORIAL, registro.getFechaReg(), -1, -1, null, null);
                    treeNodeFecha = new DefaultTreeNode(nodHisFecha, nodoInicial);
                    treeNodeFecha.setExpanded(true);
                    treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                } else {//ya existe un nodo de tipo fecha creado con anterioridad                    
                    nodHisComparar = (NodoArbolHistorial) treeNodeFecha.getData();
                    if (nodHisNuevo.getStrFecha().compareTo(nodHisComparar.getStrFecha()) == 0) {//la es la misma se crea un nodo REGISTRO_HISTORIAL al ultimo nodo fecha
                        treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                    } else {//la fecha ha cambiado, se crea un nodo 'FECHA_HISTORIAL' y se le agrega el nodo REGISTRO_HISTORIAL
                        nodHisFecha = new NodoArbolHistorial(TipoNodoEnum.FECHA_HISTORIAL, registro.getFechaReg(), -1, -1, null, null);
                        treeNodeFecha = new DefaultTreeNode(nodHisFecha, nodoInicial);
                        treeNodeFecha.setExpanded(true);
                        treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeFecha);
                    }
                }
            }
        }
        if (!exitenRegistros) {//no se encontraron registros
            nodHisNuevo = new NodoArbolHistorial(TipoNodoEnum.NOVALUE, null, -1, -1, "", "NO SE ENCONTRARON REGISTROS");
            treeNodeCreado = new DefaultTreeNode(nodHisNuevo, treeNodeRaiz);
        }

        //RequestContext.getCurrentInstance().update("IdFormHistorias:IdPanelHistorico");
    }

    private boolean estaEnListaTipRegHis(String idBuscado) {
        //buscar si un el tipo de registro se debe o no listar el el arbol del historial
        for (String idTipRegCliSelHis : regCliSelHistorial) {
            if (idBuscado.compareTo(idTipRegCliSelHis) == 0) {
                return true;
            }
        }
        return false;
    }

    public void seleccionaNodoArbol() {//determinar si se activan los botenes de 'editar' y 'PDF' del historial
        btnHistorialDisabled = true;
        btnPdfAgrupadoDisabled = true;
        boolean igualTipoRegistro = true;//determinar si los registros seleccionados son del mismo tipo para activar o no btnPdfAgrupadoDisabled
        int tipoRegistroEncontrado = -1;

        if (treeNodesSeleccionados != null) {
            int contadorRegistrosSeleccionadosHistorial = 0;
            for (TreeNode nodo : treeNodesSeleccionados) {
                NodoArbolHistorial nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
                if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                    if (tipoRegistroEncontrado == -1) {
                        tipoRegistroEncontrado = nodArbolHisSeleccionado.getIdTipoRegistro();
                    } else if (tipoRegistroEncontrado != nodArbolHisSeleccionado.getIdTipoRegistro()) {
                        igualTipoRegistro = false;
                    }
                    contadorRegistrosSeleccionadosHistorial++;
                }
            }
            if (contadorRegistrosSeleccionadosHistorial == 0) {//no hay ninguno
                btnHistorialDisabled = true;
                btnPdfAgrupadoDisabled = true;
            } else if (contadorRegistrosSeleccionadosHistorial == 1) {//solo es uno
                btnHistorialDisabled = false;
                btnPdfAgrupadoDisabled = true;
            } else//hay mas de uno
            {
                if (igualTipoRegistro) {//son del mismo tipo
                    btnHistorialDisabled = true;
                    btnPdfAgrupadoDisabled = false;
                } else {//no son del mismo tipo
                    btnHistorialDisabled = true;
                    btnPdfAgrupadoDisabled = true;
                }
            }
        }
    }

    public void cargarRegistroClinico() {//cargar un registro clinico seleccionado en el arbol de historial de paciente
        //System.out.println("Se INgreso a cargar registro");
        if (treeNodesSeleccionados == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        NodoArbolHistorial nodArbolHisSeleccionado = null;
        for (TreeNode nodo : treeNodesSeleccionados) {
            nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
            if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                break;
            }
        }
        if (nodArbolHisSeleccionado == null) {
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }

        tipoRegistroClinico = "";
        tipoRegistroClinico = String.valueOf(nodArbolHisSeleccionado.getIdTipoRegistro());//se posicione el combo
        limpiarFormulario();
        //cargo los datos principales a estructuraCampos
        registroEncontrado = registroFacade.find(nodArbolHisSeleccionado.getIdRegistro());
        if (registroEncontrado.getIdMedico() != null) {
            idPrestador = registroEncontrado.getIdMedico().getIdUsuario().toString();
            especialidadPrestador = registroEncontrado.getIdMedico().getEspecialidad().getDescripcion();
        } else {
            idPrestador = "";
            especialidadPrestador = "";
        }
        fechaReg = registroEncontrado.getFechaReg();
        fechaSis = registroEncontrado.getFechaSis();

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

            cargarMunicipios();//intento cargar municipios sea o no necesario

        }
        modificandoRegCli = true;
        mostrarFormularioRegistroClinico();
        RequestContext.getCurrentInstance().execute("PF('dlgHistorico').hide();");
        RequestContext.getCurrentInstance().update("IdFormHistorias");
    }

    public String tipo_paciente() {
        if (pacienteSeleccionado != null) {
            if (pacienteSeleccionado.getParentesco_a() != null) {
                return maestrosClasificacionesFacade.buscarPorId(pacienteSeleccionado.getParentesco_a().getId() + "").getDescripcion();
            }
        }
        return null;
    }

    //---------------------------------------------
    //---------   ------------
    //---------------------------------------------  
    private void cargarFuenteDatos(HcRegistro regEncontrado) {
        listaRegistrosParaPdf = new ArrayList<>();
        DatosFormularioHistoria datosReporte = new DatosFormularioHistoria();
        List<HcDetalle> listaCamposDeRegistroEncontrado = regEncontrado.getHcDetalleList();
        //----------------------------------------------------------------------
        //CARGO TITULOS TODOS LOS CAMPOS (hc_campos_registro)-------------------
        //----------------------------------------------------------------------
        List<HcCamposReg> listaCamposPorTipoDeRegistro = camposRegFacade.buscarPorTipoRegistro(regEncontrado.getIdTipoReg().getIdTipoReg());
        for (HcCamposReg campoPorTipoRegistro : listaCamposPorTipoDeRegistro) {
            datosReporte.setValor(campoPorTipoRegistro.getPosicion(), "<b>" + campoPorTipoRegistro.getNombrePdf() + " </b>");
        }
        //nota!!!!!! algunos de los siguientes campos tambien incluirlos en la tabla hc_campos_reg(para que se coloquen titulos en el ciclo anterior)        
        //----------------------------------------------------------------------
        //CARGO DATOS DESDE (hc_registro)(cfg_empresa) -------------------------
        //----------------------------------------------------------------------
        datosReporte.setValor(44, "<b>PRIMER NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerNombre()));//
        datosReporte.setValor(45, "<b>SEGUNDO NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoNombre()));//
        datosReporte.setValor(46, "<b>PRIMER APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerApellido()));//
        datosReporte.setValor(47, "<b>SEGUNDO APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        datosReporte.setValor(48, "<b>CELULAR: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        datosReporte.setValor(49, "<b>CORREO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getEmail()));//
        datosReporte.setValor(50, "<b>FOLIO: </b>" + regEncontrado.getFolio());//folio
        datosReporte.setValor(51, "<b>HISTORIA No: </b>" + regEncontrado.getIdPaciente().getIdentificacion());//numero de historia
        datosReporte.setValor(53, "<b>NOMBRE: </b>" + pacienteSeleccionado.nombreCompleto());//NOMBRES PACIENTE        

        datosReporte.setValor(54, "<b>FECHA: </b> " + sdfDateHour.format(regEncontrado.getFechaReg()));
        datosReporte.setValor(88, "<b>FECHA: </b> " + sdfDate.format(regEncontrado.getFechaReg()) + "<b> HORA: </b> " + sdfHour.format(regEncontrado.getFechaReg()));
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            datosReporte.setValor(55, "<b>ENTIDAD: </b> " + pacienteSeleccionado.getIdAdministradora().getRazonSocial());
        } else {
            datosReporte.setValor(55, "<b>ENTIDAD: </b> ");
        }
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(56, "<b>EDAD: </b> " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            datosReporte.setValor(43, "<b>FECHA NACIMIENTO: </b>" + sdfDate.format(pacienteSeleccionado.getFechaNacimiento()));
        } else {
            datosReporte.setValor(56, "<b>EDAD: </b> ");
            datosReporte.setValor(43, "<b>FECHA NACIMIENTO: </b>");
        }
        if (pacienteSeleccionado.getGenero() != null) {
            datosReporte.setValor(57, "<b>SEXO: </b> " + pacienteSeleccionado.getGenero().getDescripcion());
        } else {
            datosReporte.setValor(57, "<b>SEXO: </b> ");
        }
        if (pacienteSeleccionado.getOcupacion() != null) {
            datosReporte.setValor(58, "<b>OCUPACION: </b> " + pacienteSeleccionado.getOcupacion().getDescripcion());
        } else {
            datosReporte.setValor(58, "<b>OCUPACION: </b> ");
        }
        datosReporte.setValor(59, "<b>DIRECCION: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getDireccion()));
        datosReporte.setValor(60, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResidencia()));

        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(69, pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(61, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
        } else {
            datosReporte.setValor(69, pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(61, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getIdentificacion());
        }
        if (pacienteSeleccionado.getRegimen() != null) {
            datosReporte.setValor(62, "<b>TIPO AFILIACION: </b> " + pacienteSeleccionado.getRegimen().getDescripcion());
            datosReporte.setValor(42, "<b>COBERTURA EN SALUD: </b>" + pacienteSeleccionado.getRegimen().getDescripcion());
        } else {
            datosReporte.setValor(62, "<b>TIPO AFILIACION: </b> ");
            datosReporte.setValor(42, "<b>COBERTURA EN SALUD: </b>");
        }
        datosReporte.setValor(63, "<b>RESPONSABLE: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getResponsable()));
        datosReporte.setValor(64, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResponsable()));
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            datosReporte.setValor(65, "<b>ESTADO CIVIL: </b> " + pacienteSeleccionado.getEstadoCivil().getDescripcion());
        } else {
            datosReporte.setValor(65, "<b>ESTADO CIVIL: </b> ");
        }
        if (pacienteSeleccionado.getDepartamento() != null) {
            datosReporte.setValor(66, "<b>DEPARTAMENTO: </b>" + pacienteSeleccionado.getDepartamento().getDescripcion() + " " + pacienteSeleccionado.getDepartamento().getCodigo());
        } else {
            datosReporte.setValor(66, "<b>DEPARTAMENTO: </b>");
        }
        if (pacienteSeleccionado.getMunicipio() != null) {
            datosReporte.setValor(67, "<b>MUNICIPIO: </b>" + pacienteSeleccionado.getMunicipio().getDescripcion() + " " + pacienteSeleccionado.getMunicipio().getCodigo());//TELEFONO EMPRESA                
        } else {
            datosReporte.setValor(67, "<b>MUNICIPIO: </b>");//TELEFONO EMPRESA                
        }
        if (pacienteSeleccionado.getFirma() != null) {//firma paciente
            datosReporte.setValor(68, loginMB.getRutaCarpetaImagenes() + pacienteSeleccionado.getFirma().getUrlImagen());//FIRMA MEDICO
        } else {
            datosReporte.setValor(68, null);
        }
        datosReporte.setValor(69, pacienteSeleccionado.nombreCompleto() + "<br/>" + datosReporte.getValor(69));//NOMBRE EN FIRMA PACIENTE        

        //empresa
        if (loginMB.getEmpresaActual().getLogo() != null) {
            datosReporte.setValor(70, loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());//IMAGEN LOGO
        } else {
            datosReporte.setValor(70, null);//IMAGEN LOGO
        }
//        System.out.println("Imagen logo empres -> " + loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());
        System.out.println("Datos encontrados ... " + listaCamposDeRegistroEncontrado.size());

        if (regEncontrado.getIdMedico() != null) {
            datosReporte.setValor(71, regEncontrado.getIdMedico().nombreCompleto());//NOMBRE MEDICO
            datosReporte.setValor(86, regEncontrado.getIdMedico().getTelefonoResidencia());//TELEFONO MEDICO
            datosReporte.setValor(87, regEncontrado.getIdMedico().getTelefonoOficina());//CELULAR MEDICO
            datosReporte.setValor(84, regEncontrado.getIdMedico().nombreCompleto());//PARA FIRMA NOMBRE MEDICO
            if (regEncontrado.getIdMedico().getEspecialidad() != null) {
                datosReporte.setValor(72, regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//ESPECIALIDAD MEDICO
                datosReporte.setValor(84, datosReporte.getValor(84) + " <br/> " + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//PARA FIRMA  NOMBRE MEDICO
            }
            datosReporte.setValor(73, obtenerCadenaNoNula(regEncontrado.getIdMedico().getRegistroProfesional()));//REGISTRO PROFESIONAL MEDICO
            datosReporte.setValor(84, datosReporte.getValor(84) + " <br/> Reg. prof. " + regEncontrado.getIdMedico().getRegistroProfesional());//NOMBRE MEDICO

            if (regEncontrado.getIdMedico().getFirma() != null) {
                datosReporte.setValor(74, loginMB.getRutaCarpetaImagenes() + regEncontrado.getIdMedico().getFirma().getUrlImagen());//FIRMA MEDICO            
            } else {
                datosReporte.setValor(74, null);//FIRMA MEDICO
            }
        }
        datosReporte.setValor(75, "<b>Dirección: </b> " + empresa.getDireccion() + "      " + empresa.getWebsite() + "      <b>Teléfono: </b> " + empresa.getTelefono1());//DIR TEL EMPRESA        
        datosReporte.setValor(76, "<b>NOMBRE: </b>" + empresa.getRazonSocial());//NOMBRE EMPRESA                
        datosReporte.setValor(77, "<b>CODIGO: </b>" + empresa.getCodigoEmpresa());//CODIGO EMPRESA                
        datosReporte.setValor(78, "<b>DIRECCION: </b>" + empresa.getDireccion());//DIRECCION EMPRESA                
        datosReporte.setValor(79, "<b>TELEFONO: </b>" + empresa.getTelefono1());//TELEFONO EMPRESA                
        datosReporte.setValor(80, "<b>DEPARTAMENTO: </b>" + empresa.getCodDepartamento().getCodigo() + " " + empresa.getCodDepartamento().getDescripcion());//TELEFONO EMPRESA                
        datosReporte.setValor(81, "<b>MUNICIPIO: </b>" + empresa.getCodMunicipio().getCodigo() + " " + empresa.getCodMunicipio().getDescripcion());//TELEFONO EMPRESA                
        datosReporte.setValor(82, "<b>" + empresa.getTipoDoc().getDescripcion() + ": </b>  " + empresa.getNumIdentificacion());//NIT        
        datosReporte.setValor(83, empresa.getWebsite());//sitio web       

        datosReporte.setValor(97, empresa.getNomRepLegal());//CONSTANSA PORTILLA BENAVIDES
        datosReporte.setValor(98, empresa.getTipoDoc().getDescripcion() + ":" + empresa.getNumIdentificacion() + " " + empresa.getObservaciones());//OPTOMETRA U.L SALLE-BOGOTA        
        datosReporte.setValor(100, empresa.getRazonSocial());//
        datosReporte.setValor(99, "CONSULTORIO " + empresa.getDireccion() + " " + empresa.getCodMunicipio().getDescripcion() + "  TELEFONO: " + empresa.getTelefono1());//CONSULTRIO
        //datosReporte.setValor(85, "<b>ASEGURADORA RESPONSABLE DE LA ATENCION, NUMERO DE POLIZA SI ES SOAT Y VIGENCIA: </b> ");

        //datos fijos ... datos acudiente 
        //El siguiente dato getAcompañantee() en realidad trae el dato Nombre del acudiente
        datosReporte.setValor(101, "<b>NOMBRE :</b>" + pacienteSeleccionado.getAcompanante()); // NOMBRE DEL ACUDIENTE, si es correcto; del acudiente
        datosReporte.setValor(102, "<b>DIRECCION :</b>" + pacienteSeleccionado.getDireccion()); //DIRECCION DEL PACIENTE

        //y enfoque diferencial        
        datosReporte.setValor(103, "<b>NIVEL EDUCATIVO :</b>" + escolaridad);
        datosReporte.setValor(104, "<b>DISCAPACIDAD :</b>" + discapacidad);
        datosReporte.setValor(105, "<b>GESTACIÓN :</b>" + gestacion);
        datosReporte.setValor(106, "<b>OCUPACIÓN :</b>" + ocupacion);
        datosReporte.setValor(107, "<b>RELIGIÓN :</b>" + religion);
        datosReporte.setValor(108, "<b>ETNIA :</b>" + etnia);
        datosReporte.setValor(109, "<b>VIC. DE CONFLICTO ARMADO :</b>" + victimaConflictoStr);
        datosReporte.setValor(110, "<b>POBLACIÓN LBGT :</b>" + poblacionLBGTStr);
        datosReporte.setValor(111, "<b>DESPLAZADO :</b>" + desplazadoStr);
        datosReporte.setValor(112, "<b>VIC. DE MALTRATO :</b>" + victimaMaltratoStr);

        //Paciente
        datosReporte.setValor(113, "<b>CARNET: </b>" + pacienteSeleccionado.getCarnet());

//        <td><p:outputLabel value="Nivel educativo" /></td>
//        <td><p:outputLabel value="Discapacidad" /></td>
//        <td><p:outputLabel value="Gestación" /></td>
//        <td><p:outputLabel value="Ocupación" /></td>
//        <td><p:outputLabel value="Religión" /></td>
//        <td><p:outputLabel value="Etnia" /></td>
//        <p:outputLabel value="Víctima de conflicto armado" />
//        <p:outputLabel value="Población LBGT" />
//        <p:outputLabel value="Desplazado" />
//        <p:outputLabel value="Víctima de maltrato" />
        //----------------------------------------------------------------------
        //CARGO DATOS QUE SE LLENARON EN EL REGISTRO (hc_detalle)---------------
        //----------------------------------------------------------------------
        for (HcDetalle campoDeRegistroEncontrado : listaCamposDeRegistroEncontrado) {
            if (campoDeRegistroEncontrado.getHcCamposReg().getTabla() != null && campoDeRegistroEncontrado.getHcCamposReg().getTabla().length() >= 5) {//ES CATEGORIA (realizar busqueda), y considerar que la tabla minimo debe tener 5 caracts...
                switch (campoDeRegistroEncontrado.getHcCamposReg().getTabla()) {
                    case "cfg_clasificaciones":
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "cfg_clasificaciones_2"://el mismo anterior pero imprimiendo tambien el codigo de la clasificacion
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getCodigo() + " - " + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "fac_servicio":
                        servicioBuscado = servicioFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (servicioBuscado != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + servicioBuscado.getNombreServicio());
                        }
                        break;
                    case "boolean":
                        if (campoDeRegistroEncontrado.getValor().compareTo("true") == 0) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> SI");
                        } else {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> NO");
                        }
                        break;
                    case "date":
                    case "date2": //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                    case "html":
                        datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
                        break;
                }
            } else {//NO ES CATEGORIA (sacar valor)
                datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
            }
        }
        //datosReporte.setListaDatosAdicionales(listadoDatosAdicionales);//CUANDO SE USE SUBRREPORTES USAR ESTA LINEA
        listaRegistrosParaPdf.add(datosReporte);
    }

    /**
     * Para reportes con mas de 100 campos
     *
     * @param regEncontrado
     */
    private void cargarFuenteDatosReportesGrandes(HcRegistro regEncontrado) {

        System.out.println("iniciando Desde cargarFuenteDatosReportesGrandes ... ");

        listaRegistrosParaPdf = new ArrayList<>();
        DatosFormularioHistoria datosReporte = new DatosFormularioHistoria();
        List<HcDetalle> listaCamposDeRegistroEncontrado = regEncontrado.getHcDetalleList();//busca todos los datos
        //----------------------------------------------------------------------
        //CARGO TITULOS TODOS LOS CAMPOS (hc_campos_registro)-------------------
        //----------------------------------------------------------------------
        List<HcCamposReg> listaCamposPorTipoDeRegistro = camposRegFacade.buscarPorTipoRegistro(regEncontrado.getIdTipoReg().getIdTipoReg());
        for (HcCamposReg campoPorTipoRegistro : listaCamposPorTipoDeRegistro) {
            datosReporte.setValor(campoPorTipoRegistro.getPosicion(), "<b>" + campoPorTipoRegistro.getNombrePdf() + " </b>");
        }
        //nota!!!!!! algunos de los siguientes campos tambien incluirlos en la tabla hc_campos_reg(para que se coloquen titulos en el ciclo anterior)        
        //----------------------------------------------------------------------
        //CARGO DATOS DESDE (hc_registro)(cfg_empresa) -------------------------
        //----------------------------------------------------------------------
        //44
        datosReporte.setValor(200, "<b>PRIMER NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerNombre()));//
        //45
        datosReporte.setValor(201, "<b>SEGUNDO NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoNombre()));//
        //45
        datosReporte.setValor(202, "<b>PRIMER APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerApellido()));//
        //47
        datosReporte.setValor(203, "<b>SEGUNDO APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        //48
        datosReporte.setValor(204, "<b>CELULAR: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//
        //49
        if (pacienteSeleccionado.getEmail() != null) {
            datosReporte.setValor(205, "<b>CORREO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getEmail()));//
        }
        //50
        datosReporte.setValor(206, "<b>FOLIO: </b>" + regEncontrado.getFolio());//folio
        //51
        datosReporte.setValor(207, "<b>HISTORIA No: </b>" + regEncontrado.getIdPaciente().getIdentificacion());//numero de historia
        //53
        datosReporte.setValor(208, "<b>NOMBRE: </b>" + pacienteSeleccionado.nombreCompleto());//NOMBRES PACIENTE        

        //54
        datosReporte.setValor(209, "<b>FECHA: </b> " + sdfDateHour.format(regEncontrado.getFechaReg()));
        //88
        datosReporte.setValor(210, "<b>FECHA: </b> " + sdfDate.format(regEncontrado.getFechaReg()) + "<b> HORA: </b> " + sdfHour.format(regEncontrado.getFechaReg()));

        //55
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            datosReporte.setValor(211, "<b>ENTIDAD: </b> " + pacienteSeleccionado.getIdAdministradora().getRazonSocial());
        } else {
            datosReporte.setValor(211, "<b>ENTIDAD: </b> ");
        }

        //56,43
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(211, "<b>EDAD: </b> " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            datosReporte.setValor(212, "<b>FECHA NACIMIENTO: </b>" + sdfDate.format(pacienteSeleccionado.getFechaNacimiento()));
        } else {
            datosReporte.setValor(211, "<b>EDAD: </b> ");
            datosReporte.setValor(212, "<b>FECHA NACIMIENTO: </b>");
        }
        //57
        if (pacienteSeleccionado.getGenero() != null) {
            datosReporte.setValor(213, "<b>SEXO: </b> " + pacienteSeleccionado.getGenero().getDescripcion());
        } else {
            datosReporte.setValor(214, "<b>SEXO: </b> ");
        }
        //58
        if (pacienteSeleccionado.getOcupacion() != null) {
            datosReporte.setValor(215, "<b>OCUPACION: </b> " + pacienteSeleccionado.getOcupacion().getDescripcion());
        } else {
            datosReporte.setValor(215, "<b>OCUPACION: </b> ");
        }
        //59
        if (pacienteSeleccionado.getDireccion() != null) {
            datosReporte.setValor(265, "<b>DIRECCION: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getDireccion()));
        }
        //60
        datosReporte.setValor(216, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResidencia()));

        //69,61
        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(217, pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(218, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
        } else {
            datosReporte.setValor(217, pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(218, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getIdentificacion());
        }
        //62,42
        if (pacienteSeleccionado.getRegimen() != null) {
            datosReporte.setValor(219, "<b>TIPO AFILIACION: </b> " + pacienteSeleccionado.getRegimen().getDescripcion());
            datosReporte.setValor(220, "<b>COBERTURA EN SALUD: </b>" + pacienteSeleccionado.getRegimen().getDescripcion());
        } else {
            datosReporte.setValor(219, "<b>TIPO AFILIACION: </b> ");
            datosReporte.setValor(220, "<b>COBERTURA EN SALUD: </b>");
        }
        //63
        if (pacienteSeleccionado.getResponsable() != null) {
            datosReporte.setValor(221, "<b>RESPONSABLE: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getResponsable()));
            //64
            datosReporte.setValor(222, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResponsable()));
        }

        //65
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            datosReporte.setValor(223, "<b>ESTADO CIVIL: </b> " + pacienteSeleccionado.getEstadoCivil().getDescripcion());
        } else {
            datosReporte.setValor(223, "<b>ESTADO CIVIL: </b> ");
        }

        //66
        if (pacienteSeleccionado.getDepartamento() != null) {
            datosReporte.setValor(224, "<b>DEPARTAMENTO: </b>" + pacienteSeleccionado.getDepartamento().getDescripcion() + " " + pacienteSeleccionado.getDepartamento().getCodigo());
        } else {
            datosReporte.setValor(224, "<b>DEPARTAMENTO: </b>");
        }

        //67
        if (pacienteSeleccionado.getMunicipio() != null) {
            datosReporte.setValor(225, "<b>MUNICIPIO: </b>" + pacienteSeleccionado.getMunicipio().getDescripcion() + " " + pacienteSeleccionado.getMunicipio().getCodigo());//TELEFONO EMPRESA                
        } else {
            datosReporte.setValor(225, "<b>MUNICIPIO: </b>");//TELEFONO EMPRESA                
        }

        //68
        if (pacienteSeleccionado.getFirma() != null) {//firma paciente
            datosReporte.setValor(226, loginMB.getRutaCarpetaImagenes() + pacienteSeleccionado.getFirma().getUrlImagen());//FIRMA MEDICO
        } else {
            datosReporte.setValor(226, null);
        }

        //69
        datosReporte.setValor(227, pacienteSeleccionado.nombreCompleto() + "<br/>" + datosReporte.getValor(217));//NOMBRE EN FIRMA PACIENTE        

        //70
        //empresa
        if (loginMB.getEmpresaActual().getLogo() != null) {
            datosReporte.setValor(228, loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());//IMAGEN LOGO
        } else {
            datosReporte.setValor(228, null);//IMAGEN LOGO
        }

        if (regEncontrado.getIdMedico() != null) {

            //71
            datosReporte.setValor(229, regEncontrado.getIdMedico().nombreCompleto());//NOMBRE MEDICO
            //86
            datosReporte.setValor(230, regEncontrado.getIdMedico().getTelefonoResidencia());//TELEFONO MEDICO
            //87
            datosReporte.setValor(231, regEncontrado.getIdMedico().getTelefonoOficina());//CELULAR MEDICO
            //84
            datosReporte.setValor(232, regEncontrado.getIdMedico().nombreCompleto());//PARA FIRMA NOMBRE MEDICO

            if (regEncontrado.getIdMedico().getEspecialidad() != null) {
                //72
                datosReporte.setValor(233, regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//ESPECIALIDAD MEDICO
                //84
                datosReporte.setValor(234, datosReporte.getValor(229) + " <br/> " + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//PARA FIRMA  NOMBRE MEDICO
            }
            //73
            datosReporte.setValor(235, obtenerCadenaNoNula(regEncontrado.getIdMedico().getRegistroProfesional()));//REGISTRO PROFESIONAL MEDICO
            //84
            datosReporte.setValor(235, datosReporte.getValor(234) + " <br/> Reg. prof. " + regEncontrado.getIdMedico().getRegistroProfesional());//NOMBRE MEDICO

            //74
            if (regEncontrado.getIdMedico().getFirma() != null) {
                datosReporte.setValor(236, loginMB.getRutaCarpetaImagenes() + regEncontrado.getIdMedico().getFirma().getUrlImagen());//FIRMA MEDICO    
            } else {
                datosReporte.setValor(236, null);//FIRMA MEDICO
            }
        }

        //75
        datosReporte.setValor(237, "<b>Dirección: </b> " + empresa.getDireccion() + "      " + empresa.getWebsite() + "      <b>Teléfono: </b> " + empresa.getTelefono1());//DIR TEL EMPRESA        
        //76
        datosReporte.setValor(238, "<b>NOMBRE: </b>" + empresa.getRazonSocial());//NOMBRE EMPRESA                
        //77
        datosReporte.setValor(239, "<b>CODIGO: </b>" + empresa.getCodigoEmpresa());//CODIGO EMPRESA                
        //78
        datosReporte.setValor(240, "<b>DIRECCION: </b>" + empresa.getDireccion());//DIRECCION EMPRESA                
        //79
        datosReporte.setValor(241, "<b>TELEFONO: </b>" + empresa.getTelefono1());//TELEFONO EMPRESA                
        //80
        datosReporte.setValor(242, "<b>DEPARTAMENTO: </b>" + empresa.getCodDepartamento().getCodigo() + " " + empresa.getCodDepartamento().getDescripcion());//TELEFONO EMPRESA                
        //81
        datosReporte.setValor(243, "<b>MUNICIPIO: </b>" + empresa.getCodMunicipio().getCodigo() + " " + empresa.getCodMunicipio().getDescripcion());//TELEFONO EMPRESA                
        //82
        datosReporte.setValor(244, "<b>" + empresa.getTipoDoc().getDescripcion() + ": </b>  " + empresa.getNumIdentificacion());//NIT        
        //83
        datosReporte.setValor(245, empresa.getWebsite());//sitio web       

        //97
        datosReporte.setValor(246, empresa.getNomRepLegal());//CONSTANSA PORTILLA BENAVIDES
        //98
        datosReporte.setValor(247, empresa.getTipoDoc().getDescripcion() + ":" + empresa.getNumIdentificacion() + " " + empresa.getObservaciones());//OPTOMETRA U.L SALLE-BOGOTA        
        //100
        datosReporte.setValor(248, empresa.getRazonSocial());//
        //99
        datosReporte.setValor(249, "CONSULTORIO " + empresa.getDireccion() + " " + empresa.getCodMunicipio().getDescripcion() + "  TELEFONO: " + empresa.getTelefono1());//CONSULTRIO
        //datosReporte.setValor(85, "<b>ASEGURADORA RESPONSABLE DE LA ATENCION, NUMERO DE POLIZA SI ES SOAT Y VIGENCIA: </b> ");

//         datosReporte.setValor(101, "<b>NOMBRE :</b>" + pacienteSeleccionado.getAcompanante()); // NOMBRE DEL ACUDIENTE, si es correcto; del acudiente
//        datosReporte.setValor(102, "<b>DIRECCION :</b>" + pacienteSeleccionado.getDireccion()); //DIRECCION DEL PACIENTE
//
//        //y enfoque diferencial        
//        datosReporte.setValor(103, "<b>NIVEL EDUCATIVO :</b>" + escolaridad);
//        datosReporte.setValor(104, "<b>DISCAPACIDAD :</b>" + discapacidad);
//        datosReporte.setValor(105, "<b>GESTACIÓN :</b>" + gestacion);
//        datosReporte.setValor(106, "<b>OCUPACIÓN :</b>" + ocupacion);
//        datosReporte.setValor(107, "<b>RELIGIÓN :</b>" + religion);
//        datosReporte.setValor(108, "<b>ETNIA :</b>" + etnia);
//        datosReporte.setValor(109, "<b>VIC. DE CONFLICTO ARMADO :</b>" + victimaConflictoStr);
//        datosReporte.setValor(110, "<b>POBLACIÓN LBGT :</b>" + poblacionLBGTStr);
//        datosReporte.setValor(111, "<b>DESPLAZADO :</b>" + desplazadoStr);
//        datosReporte.setValor(112, "<b>VIC. DE MALTRATO :</b>" + victimaMaltratoStr);
//        
        //datos acudiente
        if (pacienteSeleccionado.getAcompanante() != null) {
            datosReporte.setValor(250, "<b>NOMBRE :</b>" + pacienteSeleccionado.getAcompanante()); // NOMBRE DEL ACUDIENTE, si es correcto; del acudiente
        }
        if (pacienteSeleccionado.getDireccion() != null) {
            datosReporte.setValor(251, "<b>DIRECCION :</b>" + pacienteSeleccionado.getDireccion()); //DIRECCION DEL PACIENTE
        }
        //y enfoque diferencial        
        datosReporte.setValor(252, "<b>NIVEL EDUCATIVO :</b>" + escolaridad);
        datosReporte.setValor(253, "<b>DISCAPACIDAD :</b>" + discapacidad);
        datosReporte.setValor(254, "<b>GESTACIÓN :</b>" + gestacion);
        datosReporte.setValor(255, "<b>OCUPACIÓN :</b>" + ocupacion);
        datosReporte.setValor(256, "<b>RELIGIÓN :</b>" + religion);
        datosReporte.setValor(257, "<b>ETNIA :</b>" + etnia);
        datosReporte.setValor(258, "<b>VIC. DE CONFLICTO ARMADO :</b>" + victimaConflictoStr);
        datosReporte.setValor(259, "<b>POBLACIÓN LBGT :</b>" + poblacionLBGTStr);
        datosReporte.setValor(260, "<b>DESPLAZADO :</b>" + desplazadoStr);
        datosReporte.setValor(261, "<b>VIC. DE MALTRATO :</b>" + victimaMaltratoStr);

        //datos informacion general de referencia-contrareferencia
        datosReporte.setValor(190, "<b>INSTITUCION :</b>" + loginMB.getEmpresaActual().getRazonSocial());
        datosReporte.setValor(191, "<b>E.S.E.IO :</b>" + loginMB.getEmpresaActual().getCodMunicipio().getDescripcion());
        datosReporte.setValor(192, "<b>ESPECIALIDAD :</b>" + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());
        datosReporte.setValor(193, "<b>NIVEL :</b>" + loginMB.getEmpresaActual().getNivel());

        datosReporte.setValor(262, "<b>EDAD EN MESES :</b>" + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));

        //Paciente
        if (pacienteSeleccionado.getCarnet() != null) {
            datosReporte.setValor(263, "<b>CARNET: </b>" + pacienteSeleccionado.getCarnet());
        }
        //imagenes de los reportes
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 19) {
            datosReporte.setValor(264, regEncontrado.getIdMedico().getPrimerNombre() + " " + regEncontrado.getIdMedico().getSegundoNombre() + " " + regEncontrado.getIdMedico().getPrimerApellido() + " " + regEncontrado.getIdMedico().getSegundoApellido()); //Flujograma
        } else {
            datosReporte.setValor(264, loginMB.getRutaCarpetaImagenes() + "Reportes/1.png"); //Flujograma
        }
        datosReporte.setValor(266, loginMB.getRutaCarpetaImagenes() + "Reportes/2.png"); //Altura uterina
        datosReporte.setValor(267, loginMB.getRutaCarpetaImagenes() + "Reportes/3.png"); //Peso durante la gestación
        datosReporte.setValor(268, loginMB.getRutaCarpetaImagenes() + "Reportes/4.png"); //Presión Arterial
        datosReporte.setValor(269, loginMB.getRutaCarpetaImagenes() + "Reportes/5.png"); //Relación Peso - Talla

        //----------------------------------------------------------------------
        //CARGO DATOS QUE SE LLENARON EN EL REGISTRO (hc_detalle)---------------
        //----------------------------------------------------------------------
        for (HcDetalle campoDeRegistroEncontrado : listaCamposDeRegistroEncontrado) { //recorre la lista de datos encontrados
            if (campoDeRegistroEncontrado.getHcCamposReg().getTabla() != null && campoDeRegistroEncontrado.getHcCamposReg().getTabla().length() >= 5) {//ES CATEGORIA (realizar busqueda)
                switch (campoDeRegistroEncontrado.getHcCamposReg().getTabla()) {
                    case "cfg_clasificaciones":
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "cfg_clasificaciones_2"://el mismo anterior pero imprimiendo tambien el codigo de la clasificacion
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getCodigo() + " - " + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "fac_servicio":
                        servicioBuscado = servicioFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (servicioBuscado != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + servicioBuscado.getNombreServicio());
                        }
                        break;
                    case "boolean":
                        if (campoDeRegistroEncontrado.getValor().compareTo("true") == 0) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> SI");
                        } else {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> NO");
                        }
                        break;
                    case "date":
                    case "date2": //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                    case "html":
                        datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
                        break;
                }
            } else {//NO ES CATEGORIA (sacar valor)
                datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
            }
        }

        List<DatosSubReporteHistoria> listaDatosAdicionales = new ArrayList<>();
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 17) { //psicologia
//            cargarEstructuraFamiliar();

            for (FilaDataTable item : listaEstructuraFamiliar) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna5());
                datosS.setDato3(item.getColumna6());
                listaDatosAdicionales.add(datosS);

                System.out.println("Familiar " + item.getColumna1());
            }
        }

        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 19) { //formulacion medicamentos
            List<FilaDataTable> listaMedicamentosParaReporte = cargarListaMedicamentos(regEncontrado);

            for (FilaDataTable item : listaMedicamentosParaReporte) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna3());
                datosS.setDato3(item.getColumna4());
                datosS.setDato4(item.getColumna5());
                datosS.setDato5(item.getColumna6());
                datosS.setDato6(item.getColumna7());
                datosS.setDato7(item.getColumna8());
                datosS.setDato8(item.getColumna9());
                listaDatosAdicionales.add(datosS);
                System.out.println("Medicamento  " + item.getColumna1());
            }

        }

        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 25) { //ordenes medicas
            List<FilaDataTable> listaServiciosParaReporte = cargarListaServicios(regEncontrado);

            for (FilaDataTable item : listaServiciosParaReporte) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna3());
                datosS.setDato3(item.getColumna4());
                listaDatosAdicionales.add(datosS);
                System.out.println("Servicio  " + item.getColumna1());
            }

        }

        System.out.println("listaDatosAdicionales size " + listaDatosAdicionales.size());

        datosReporte.setListaDatosAdicionales(listaDatosAdicionales);//CUANDO SE USE SUBRREPORTES USAR ESTA LINEA
        listaRegistrosParaPdf.add(datosReporte);
    }

    /**
     * Específico para reportes odontológico, que poseen más de 700 variables en
     * un caso (PyP) y +1400 en otro (HC).
     *
     * @param regEncontrado
     */
    private void cargarFuenteDatosReportesXL(HcRegistro regEncontrado) {

        System.out.println("iniciando Desde cargarFuenteDatosReportesXL ... ");

        listaRegistrosParaPdf = new ArrayList<>();
        DatosFormularioHistoria datosReporte = new DatosFormularioHistoria();
        List<HcDetalle> listaCamposDeRegistroEncontrado = regEncontrado.getHcDetalleList();//busca todos los datos
        /**
         * Se cargan todos los títulos que se mostraran en el reporte de
         * "hc_campos_reg"
         */
        List<HcCamposReg> listaCamposPorTipoDeRegistro = camposRegFacade.buscarPorTipoRegistro(regEncontrado.getIdTipoReg().getIdTipoReg());
        for (HcCamposReg campoPorTipoRegistro : listaCamposPorTipoDeRegistro) {
            datosReporte.setValor(campoPorTipoRegistro.getPosicion(), "<b>" + campoPorTipoRegistro.getNombrePdf() + " </b>");
        }

        /**
         * Se cargan datos de la historia clinica, fecha, folio etc, disponibles
         * del 600 al 619.
         */
        datosReporte.setValor(1700, "<b>HISTORIA No: </b>" + regEncontrado.getIdPaciente().getIdentificacion());//número de historia
        datosReporte.setValor(1701, "<b>FOLIO: </b>" + regEncontrado.getFolio());//folio
        datosReporte.setValor(1702, "<b>FECHA: </b> " + sdfDateHour.format(regEncontrado.getFechaReg()));//fecha del registro

        datosReporte.setValor(1703, "<b>NOMBRE: </b>" + pacienteSeleccionado.nombreCompleto());//NOMBRE COMPLETO 

        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(1704, "<b>EDAD: </b> " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));//EDAD
        } else {
            datosReporte.setValor(1704, "<b>EDAD: </b> ");
        }

        if (pacienteSeleccionado.getGenero() != null) {
            datosReporte.setValor(1705, "<b>SEXO: </b> " + pacienteSeleccionado.getGenero().getDescripcion());//SEXO
        } else {
            datosReporte.setValor(1705, "<b>SEXO: </b> ");
        }

        if (pacienteSeleccionado.getRegimen() != null) {
            datosReporte.setValor(1706, "<b>COBERTURA EN SALUD: </b>" + pacienteSeleccionado.getRegimen().getDescripcion());//COBERTURA EN SALUD
        } else {
            datosReporte.setValor(1706, "<b>COBERTURA EN SALUD: </b>");
        }

        //Paciente
        datosReporte.setValor(1707, "<b>CARNET: </b>" + pacienteSeleccionado.getCarnet());

        /**
         * Se cargan datos Del 680 al 729 se encuentran datos pertenecientes a
         * médico y empresa.
         */
        if (loginMB.getEmpresaActual().getLogo() != null) {
            datosReporte.setValor(1708, loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());//IMAGEN LOGO
        } else {
            datosReporte.setValor(1708, null);//IMAGEN LOGO
        }

        datosReporte.setValor(1709, empresa.getNomRepLegal());//NOMBRE REP. LEGAL
        datosReporte.setValor(1710, empresa.getTipoDoc().getDescripcion() + ":" + empresa.getNumIdentificacion() + " " + empresa.getObservaciones());//OPTOMETRA U.L SALLE-BOGOTA        
        datosReporte.setValor(1711, empresa.getRazonSocial());//
        datosReporte.setValor(1712, "CONSULTORIO " + empresa.getDireccion() + " " + empresa.getCodMunicipio().getDescripcion() + "  TELEFONO: " + empresa.getTelefono1());//CONSULTRIO

        //ENFOQUE DIFERENCIAL  
        datosReporte.setValor(1713, "<b>NIVEL EDUCATIVO :</b>" + escolaridad);
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(1714, "<b>FECHA NACIMIENTO: </b>" + sdfDate.format(pacienteSeleccionado.getFechaNacimiento()));//FECHA DE NACIMIENTO
        } else {
            datosReporte.setValor(1714, "<b>FECHA NACIMIENTO: </b>");
        }
        datosReporte.setValor(1715, "<b>DIRECCION: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getDireccion()));//DIRECCION
        datosReporte.setValor(1716, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResidencia()));//TELEFONO
        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(1717, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
        } else {
            datosReporte.setValor(1717, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getIdentificacion());
        }
        if (pacienteSeleccionado.getMunicipio() != null) {
            datosReporte.setValor(1718, "<b>MUNICIPIO: </b>" + pacienteSeleccionado.getMunicipio().getDescripcion() + " " + pacienteSeleccionado.getMunicipio().getCodigo());//MUNICIPIO            
        } else {
            datosReporte.setValor(1718, "<b>MUNICIPIO: </b>");
        }
        if (pacienteSeleccionado.getDepartamento() != null) {
            datosReporte.setValor(1719, "<b>DEPARTAMENTO: </b>" + pacienteSeleccionado.getDepartamento().getDescripcion() + " " + pacienteSeleccionado.getDepartamento().getCodigo());//DEPARTAMENTO
        } else {
            datosReporte.setValor(1719, "<b>DEPARTAMENTO: </b>");
        }
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            datosReporte.setValor(1720, "<b>ESTADO CIVIL: </b> " + pacienteSeleccionado.getEstadoCivil().getDescripcion());//ESTADO CIVIL
        } else {
            datosReporte.setValor(1720, "<b>ESTADO CIVIL: </b> ");
        }

        datosReporte.setValor(1721, "<b>OCUPACIÓN :</b>" + ocupacion);
        if (regEncontrado.getIdMedico() != null) {
            datosReporte.setValor(1722, regEncontrado.getIdMedico().nombreCompleto());//NOMBRE MEDICO
        }

        if (regEncontrado.getIdMedico().getFirma() != null) {
            datosReporte.setValor(1723, loginMB.getRutaCarpetaImagenes() + regEncontrado.getIdMedico().getFirma().getUrlImagen());//IMAGEN DE FIRMA DE MEDICO    
        } else {
            datosReporte.setValor(1723, null);
        }

        if (regEncontrado.getIdMedico().getEspecialidad() != null) {
            datosReporte.setValor(1724, datosReporte.getValor(1722) + " <br/> " + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//PARA FIRMA  NOMBRE MEDICO
        }

        if (pacienteSeleccionado.getFirma() != null) {
            datosReporte.setValor(1725, loginMB.getRutaCarpetaImagenes() + pacienteSeleccionado.getFirma().getUrlImagen());//firma paciente
        } else {
            datosReporte.setValor(1725, null);
        }

        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(1726, pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());//IDENTIFICACION
        } else {
            datosReporte.setValor(1726, pacienteSeleccionado.getIdentificacion());
        }
        datosReporte.setValor(1727, pacienteSeleccionado.nombreCompleto() + "<br/>" + datosReporte.getValor(1726));//NOMBRE EN FIRMA PACIENTE  

        /**
         * Se cargan los datos que fueron llenados en el regsitro "hc_detalle",
         * de ser un tipo "especial" según valor en el campo "tabla" de
         * "hc_campos_reg", se realiza una acción especial según sea el caso de
         * no poseer valor en dicho campo, simplemente se realiza etiqueta +
         * valor del campo.
         */
        for (HcDetalle campoDeRegistroEncontrado : listaCamposDeRegistroEncontrado) { //recorre la lista de datos encontrados
            if (campoDeRegistroEncontrado.getHcCamposReg().getTabla() != null && campoDeRegistroEncontrado.getHcCamposReg().getTabla().length() >= 5) {//ES CATEGORIA (realizar busqueda)
                switch (campoDeRegistroEncontrado.getHcCamposReg().getTabla()) {
                    case "cfg_clasificaciones":
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "cfg_clasificaciones_2"://el mismo anterior pero imprimiendo tambien el codigo de la clasificacion
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getCodigo() + " - " + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "fac_servicio":
                        servicioBuscado = servicioFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (servicioBuscado != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + servicioBuscado.getNombreServicio());
                        }
                        break;
                    case "boolean":
                        if (campoDeRegistroEncontrado.getValor().compareTo("true") == 0) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> SI");
                        } else {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> NO");
                        }
                        break;
                    case "date":
                    case "date2": //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                    case "html":
                        datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
                        break;
                }
            } else {//NO ES CATEGORIA (sacar valor)
                datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
            }
        }

        List<DatosSubReporteHistoria> listaDatosAdicionales = new ArrayList<>();

        System.out.println("listaDatosAdicionales size " + listaDatosAdicionales.size());

        datosReporte.setListaDatosAdicionales(listaDatosAdicionales);//CUANDO SE USE SUBRREPORTES USAR ESTA LINEA
        listaRegistrosParaPdf.add(datosReporte);
    }

    /**
     * Para reportes entre 200 y 599 registros.
     *
     * @param regEncontrado
     */
    private void cargarFuenteDatosReportesExtraGrandes(HcRegistro regEncontrado) {

        System.out.println("iniciando Desde cargarFuenteDatosReportesExtraGrandes ... ");

        listaRegistrosParaPdf = new ArrayList<>();
        DatosFormularioHistoria datosReporte = new DatosFormularioHistoria();
        DatosFormularioHistoria datosReporte2 = new DatosFormularioHistoria();
        List<HcDetalle> listaCamposDeRegistroEncontrado = regEncontrado.getHcDetalleList();//busca todos los datos
        List<Long> listaPeso = new ArrayList();
        List<Long> listaTalla = new ArrayList();
        if(regEncontrado.getIdTipoReg().getIdTipoReg()==71){
            //peso
            List<HcDetalle> lista1 =  detalleFacade.getValoresGraficas(pacienteSeleccionado.getIdPaciente(),0,regEncontrado.getIdTipoReg().getIdTipoReg());
            List<DatosGrafica> listaGrafica = new ArrayList();
            for(HcDetalle hc:lista1){
                listaPeso.add(Long.valueOf(hc.getValor()));
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(hc.getValor()));
                dg.setValorX(hc.getValorX().doubleValue());
                listaGrafica.add(dg);
            }
            datosReporte.setListaDatosPesos(listaGrafica);
            //talla
            lista1 =  detalleFacade.getValoresGraficas(pacienteSeleccionado.getIdPaciente(),1,regEncontrado.getIdTipoReg().getIdTipoReg());
            listaGrafica = new ArrayList();
            for(HcDetalle hc:lista1){
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(hc.getValor()));
                dg.setValorX(hc.getValorX().doubleValue());
                listaGrafica.add(dg);
                listaTalla.add(Long.valueOf(hc.getValor()));
            }
            datosReporte.setListaDatosTalla(listaGrafica);
            
            //IMC
            listaGrafica = new ArrayList();
            for (int i = 0; i < lista1.size(); i++) {
                float pesoC = listaPeso.get(i).floatValue();
                float tallaC = listaTalla.get(i).floatValue();
                float metros = tallaC / 100;
                float imcCalculado = pesoC / (metros * metros);
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(imcCalculado));
                dg.setValorX(lista1.get(i).getValorX().doubleValue());
                listaGrafica.add(dg);
            }	
            
            datosReporte.setListaDatosIMC(listaGrafica);
            
            //pc
            lista1 =  detalleFacade.getValoresGraficasAnnio(pacienteSeleccionado.getIdPaciente(),2,regEncontrado.getIdTipoReg().getIdTipoReg());
            listaGrafica = new ArrayList();
            for(HcDetalle hc:lista1){
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(hc.getValor()));
                dg.setValorX(hc.getValorX().doubleValue());
                listaGrafica.add(dg);
            }
            datosReporte.setListaDatosPC(listaGrafica);
            
            
        }else if(regEncontrado.getIdTipoReg().getIdTipoReg()==70 || regEncontrado.getIdTipoReg().getIdTipoReg()==69){
            //peso
            List<HcDetalle> lista1 =  detalleFacade.getValoresGraficasAnnio(pacienteSeleccionado.getIdPaciente(),2,regEncontrado.getIdTipoReg().getIdTipoReg());
            List<DatosGrafica> listaGrafica = new ArrayList();
            for(HcDetalle hc:lista1){
                listaPeso.add(Long.valueOf(hc.getValor()));
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(hc.getValor()));
                dg.setValorX(hc.getValorX().doubleValue());
                listaGrafica.add(dg);
            }
            datosReporte.setListaDatosPesos(listaGrafica);
            //talla
            lista1 =  detalleFacade.getValoresGraficasAnnio(pacienteSeleccionado.getIdPaciente(),3,regEncontrado.getIdTipoReg().getIdTipoReg());
            listaGrafica = new ArrayList();
            for(HcDetalle hc:lista1){
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(hc.getValor()));
                dg.setValorX(hc.getValorX().doubleValue());
                listaGrafica.add(dg);
                listaTalla.add(Long.valueOf(hc.getValor()));
            }
            datosReporte.setListaDatosTalla(listaGrafica);
            
            //IMC
            listaGrafica = new ArrayList();
            for (int i = 0; i < lista1.size(); i++) {
                float pesoC = listaPeso.get(i).floatValue();
                float tallaC = listaTalla.get(i).floatValue();
                float metros = tallaC / 100;
                float imcCalculado = pesoC / (metros * metros);
                DatosGrafica dg = new DatosGrafica();
                dg.setValor(Double.valueOf(imcCalculado));
                dg.setValorX(lista1.get(i).getValorX().doubleValue());
                listaGrafica.add(dg);
            }	
            
            datosReporte.setListaDatosIMC(listaGrafica);
        }
        
        /**
         * Se cargan todos los títulos que se mostraran en el reporte de
         * "hc_campos_reg"
         */
        List<HcCamposReg> listaCamposPorTipoDeRegistro = camposRegFacade.buscarPorTipoRegistro(regEncontrado.getIdTipoReg().getIdTipoReg());
        for (HcCamposReg campoPorTipoRegistro : listaCamposPorTipoDeRegistro) {
            datosReporte.setValor(campoPorTipoRegistro.getPosicion(), "<b>" + campoPorTipoRegistro.getNombrePdf() + " </b>");
        }
        for (HcDetalle c : regEncontrado.getHcDetalleList()) {
            datosReporte2.setValor(c.getHcCamposReg().getPosicion(), c.getValor());
        }

        /**
         * Se cargan datos de la historia clinica, fecha, folio etc, disponibles
         * del 600 al 619.
         */
        datosReporte.setValor(600, "<b>HISTORIA No: </b>" + regEncontrado.getIdPaciente().getIdentificacion());//número de historia
        datosReporte.setValor(601, "<b>FOLIO: </b>" + regEncontrado.getFolio());//folio
        datosReporte.setValor(602, "<b>FECHA: </b> " + sdfDateHour.format(regEncontrado.getFechaReg()));//fecha del registro
        datosReporte.setValor(603, "<b>FECHA: </b> " + sdfDate.format(regEncontrado.getFechaReg()) + "<b> HORA: </b> " + sdfHour.format(regEncontrado.getFechaReg()));//fecha y hora del registro

        /**
         * Se cargan datos de Del 620 al 679 datos pertenecientes al paciente.
         */
        datosReporte.setValor(620, "<b>PRIMER NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerNombre()));//PRIMER NOMBRE
        datosReporte.setValor(621, "<b>SEGUNDO NOMBRE: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoNombre()));//SEGUNDO NOMBRE
        datosReporte.setValor(622, "<b>PRIMER APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getPrimerApellido()));//PRIMER APELLIDO
        datosReporte.setValor(623, "<b>SEGUNDO APELLIDO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getSegundoApellido()));//SEGUNDO APELLIDO
        datosReporte.setValor(624, "<b>CELULAR: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getCelular()));//CELULAR
        if (pacienteSeleccionado.getEmail() != null) {
            datosReporte.setValor(625, "<b>CORREO: </b>" + obtenerCadenaNoNula(pacienteSeleccionado.getEmail()));//CORREO
        }
        datosReporte.setValor(626, "<b>NOMBRE: </b>" + pacienteSeleccionado.nombreCompleto());//NOMBRE COMPLETO 

        if (pacienteSeleccionado.getIdAdministradora() != null) {
            datosReporte.setValor(627, "<b>ENTIDAD: </b> " + pacienteSeleccionado.getIdAdministradora().getRazonSocial());//ENTIDAD
        } else {
            datosReporte.setValor(627, "<b>ENTIDAD: </b> ");
        }

        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            datosReporte.setValor(628, "<b>EDAD: </b> " + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));//EDAD
            datosReporte.setValor(629, "<b>FECHA NACIMIENTO: </b>" + sdfDate.format(pacienteSeleccionado.getFechaNacimiento()));//FECHA DE NACIMIENTO
        } else {
            datosReporte.setValor(628, "<b>EDAD: </b> ");
            datosReporte.setValor(629, "<b>FECHA NACIMIENTO: </b>");
        }

        if (pacienteSeleccionado.getGenero() != null) {
            datosReporte.setValor(630, "<b>SEXO: </b> " + pacienteSeleccionado.getGenero().getDescripcion());//SEXO
        } else {
            datosReporte.setValor(630, "<b>SEXO: </b> ");
        }

        if (pacienteSeleccionado.getOcupacion() != null) {
            datosReporte.setValor(631, "<b>OCUPACION: </b> " + pacienteSeleccionado.getOcupacion().getDescripcion());//OCUPACION
        } else {
            datosReporte.setValor(631, "<b>OCUPACION: </b> ");
        }

        datosReporte.setValor(632, "<b>DIRECCION: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getDireccion()));//DIRECCION
        datosReporte.setValor(633, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResidencia()));//TELEFONO

        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            datosReporte.setValor(634, pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());//IDENTIFICACION
            datosReporte.setValor(635, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getTipoIdentificacion().getDescripcion() + " " + pacienteSeleccionado.getIdentificacion());
        } else {
            datosReporte.setValor(634, pacienteSeleccionado.getIdentificacion());
            datosReporte.setValor(635, "<b>IDENTIFICACION: </b> " + pacienteSeleccionado.getIdentificacion());
        }

        if (pacienteSeleccionado.getRegimen() != null) {
            datosReporte.setValor(636, "<b>TIPO AFILIACION: </b> " + pacienteSeleccionado.getRegimen().getDescripcion());//TIPO AFILIACION
            datosReporte.setValor(637, "<b>COBERTURA EN SALUD: </b>" + pacienteSeleccionado.getRegimen().getDescripcion());//COBERTURA EN SALUD
        } else {
            datosReporte.setValor(636, "<b>TIPO AFILIACION: </b> ");
            datosReporte.setValor(637, "<b>COBERTURA EN SALUD: </b>");
        }
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
            String parentezco_a = "";
            if (pacienteSeleccionado.getParentesco_a() != null) {
                parentezco_a = ", PARENTEZCO: " + pacienteSeleccionado.getParentesco_a().getDescripcion();
            }
            if (pacienteSeleccionado.getResponsable() != null) {
                datosReporte.setValor(638, "<b>ACUDIENTE: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getResponsable()) + parentezco_a);//RESPONSABLE
                datosReporte.setValor(639, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResponsable()));//TELEFONO DEL RESPONSABLE
            }
        } else {
            if (pacienteSeleccionado.getResponsable() != null) {
                datosReporte.setValor(638, "<b>RESPONSABLE: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getResponsable()));//RESPONSABLE
                datosReporte.setValor(639, "<b>TELEFONO: </b> " + obtenerCadenaNoNula(pacienteSeleccionado.getTelefonoResponsable()));//TELEFONO DEL RESPONSABLE
            }
        }
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            datosReporte.setValor(640, "<b>ESTADO CIVIL: </b> " + pacienteSeleccionado.getEstadoCivil().getDescripcion());//ESTADO CIVIL
        } else {
            datosReporte.setValor(640, "<b>ESTADO CIVIL: </b> ");
        }

        if (pacienteSeleccionado.getDepartamento() != null) {
            datosReporte.setValor(641, "<b>DEPARTAMENTO: </b>" + pacienteSeleccionado.getDepartamento().getDescripcion() + " " + pacienteSeleccionado.getDepartamento().getCodigo());//DEPARTAMENTO
        } else {
            datosReporte.setValor(641, "<b>DEPARTAMENTO: </b>");
        }

        if (pacienteSeleccionado.getMunicipio() != null) {
            datosReporte.setValor(642, "<b>MUNICIPIO: </b>" + pacienteSeleccionado.getMunicipio().getDescripcion() + " " + pacienteSeleccionado.getMunicipio().getCodigo());//MUNICIPIO            
        } else {
            datosReporte.setValor(642, "<b>MUNICIPIO: </b>");
        }

        if (pacienteSeleccionado.getFirma() != null) {
            datosReporte.setValor(643, loginMB.getRutaCarpetaImagenes() + pacienteSeleccionado.getFirma().getUrlImagen());//firma paciente
        } else {
            datosReporte.setValor(643, null);
        }

        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54 || regEncontrado.getIdTipoReg().getIdTipoReg() == 77) {
            List<RepExamenes> RepExamenes2 = new ArrayList<>();
            List<HcRepExamenes> lista = hcRepFacade.buscarRepUltimo(regEncontrado.getIdPaciente().getIdPaciente());
            for (HcRepExamenes rep : lista) {
                RepExamenes2.add(new RepExamenes(rep.getPosicion().toString(), tipo_reporte(rep.getNombreParaclinico().toString()), sdfDate.format(rep.getFecha()), rep.getResultado()));
            }
            List<RepExamenes> RepExamenes1 = new ArrayList<>();
            for (RepExamenes e : RepExamenes2) {
                int i = 0;
                for (RepExamenes ee : RepExamenes1) {
                    if (ee.getN().equals(e.getN())) {
                        i++;
                    }
                }
                if (i == 0) {
                    RepExamenes1.add(e);
                }
            }
            datosReporte.setListaRepExamenes(RepExamenes1);
        }

        datosReporte.setValor(644, pacienteSeleccionado.nombreCompleto() + "<br/>" + datosReporte.getValor(634));//NOMBRE EN FIRMA PACIENTE   

        //DATOS DEL ACUDIENTE (ACOMPAÑANTE).
        if (pacienteSeleccionado.getAcompanante() != null) {
            datosReporte.setValor(645, "<b>NOMBRE :</b>" + pacienteSeleccionado.getAcompanante()); // NOMBRE DEL ACUDIENTE (ACOMPAÑANTE).
        }
        //ENFOQUE DIFERENCIAL  
        datosReporte.setValor(646, "<b>NIVEL EDUCATIVO :</b>" + escolaridad);
        datosReporte.setValor(647, "<b>DISCAPACIDAD :</b>" + discapacidad);
        datosReporte.setValor(648, "<b>GESTACIÓN :</b>" + gestacion);
        datosReporte.setValor(649, "<b>OCUPACIÓN :</b>" + ocupacion);
        datosReporte.setValor(650, "<b>RELIGIÓN :</b>" + religion);
        datosReporte.setValor(651, "<b>ETNIA :</b>" + etnia);
        datosReporte.setValor(652, "<b>VIC. DE CONFLICTO ARMADO :</b>" + victimaConflictoStr);
        datosReporte.setValor(653, "<b>POBLACIÓN LBGT :</b>" + poblacionLBGTStr);
        datosReporte.setValor(654, "<b>DESPLAZADO :</b>" + desplazadoStr);
        datosReporte.setValor(655, "<b>VIC. DE MALTRATO :</b>" + victimaMaltratoStr);
        datosReporte.setValor(656, "<b>EDAD EN MESES :</b>" + calcularEdad(pacienteSeleccionado.getFechaNacimiento()));

        //Paciente
        if (pacienteSeleccionado.getCarnet() != null) {
            datosReporte.setValor(657, "<b>CARNET: </b>" + pacienteSeleccionado.getCarnet());
        }
        /**
         * Se cargan datos Del 680 al 729 se encuentran datos pertenecientes a
         * médico y empresa.
         */
        if (loginMB.getEmpresaActual().getLogo() != null) {
            datosReporte.setValor(680, loginMB.getRutaCarpetaImagenes() + loginMB.getEmpresaActual().getLogo().getUrlImagen());//IMAGEN LOGO
        } else {
            datosReporte.setValor(680, null);//IMAGEN LOGO
        }

        if (regEncontrado.getIdMedico() != null) {
            datosReporte.setValor(681, regEncontrado.getIdMedico().nombreCompleto());//NOMBRE MEDICO
            datosReporte.setValor(682, regEncontrado.getIdMedico().getTelefonoResidencia());//TELEFONO MEDICO
            datosReporte.setValor(683, regEncontrado.getIdMedico().getTelefonoOficina());//CELULAR MEDICO
            datosReporte.setValor(684, regEncontrado.getIdMedico().nombreCompleto());//PARA FIRMA NOMBRE MEDICO

            if (regEncontrado.getIdMedico().getEspecialidad() != null) {
                datosReporte.setValor(685, regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//ESPECIALIDAD MEDICO
                datosReporte.setValor(686, datosReporte.getValor(681) + " <br/> " + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());//PARA FIRMA  NOMBRE MEDICO
            }
            datosReporte.setValor(687, obtenerCadenaNoNula(regEncontrado.getIdMedico().getRegistroProfesional()));//REGISTRO PROFESIONAL MEDICO
            datosReporte.setValor(688, datosReporte.getValor(686) + " <br/> Reg. prof. " + regEncontrado.getIdMedico().getRegistroProfesional());//NOMBRE MEDICO

            if (regEncontrado.getIdMedico().getFirma() != null) {
                datosReporte.setValor(689, loginMB.getRutaCarpetaImagenes() + regEncontrado.getIdMedico().getFirma().getUrlImagen());//IMAGEN DE FIRMA DE MEDICO    
            } else {
                datosReporte.setValor(689, null);
            }
        }

        datosReporte.setValor(690, "<b>Dirección: </b> " + empresa.getDireccion() + "      " + empresa.getWebsite() + "      <b>Teléfono: </b> " + empresa.getTelefono1());//DIR TEL EMPRESA        
        datosReporte.setValor(691, "<b>NOMBRE: </b>" + empresa.getRazonSocial());//NOMBRE EMPRESA                
        datosReporte.setValor(692, "<b>CODIGO: </b>" + empresa.getCodigoEmpresa());//CODIGO EMPRESA                
        datosReporte.setValor(693, "<b>DIRECCION: </b>" + empresa.getDireccion());//DIRECCION EMPRESA                
        datosReporte.setValor(694, "<b>TELEFONO: </b>" + empresa.getTelefono1());//TELEFONO EMPRESA                
        datosReporte.setValor(695, "<b>DEPARTAMENTO: </b>" + empresa.getCodDepartamento().getCodigo() + " " + empresa.getCodDepartamento().getDescripcion());//DEPARTAMENTO EMPRESA                
        datosReporte.setValor(696, "<b>MUNICIPIO: </b>" + empresa.getCodMunicipio().getCodigo() + " " + empresa.getCodMunicipio().getDescripcion());//MUNICIPIO EMPRESA                
        datosReporte.setValor(697, "<b>" + empresa.getTipoDoc().getDescripcion() + ": </b>  " + empresa.getNumIdentificacion());//NIT        
        datosReporte.setValor(698, empresa.getWebsite());//sitio web       

        datosReporte.setValor(699, empresa.getNomRepLegal());//NOMBRE REP. LEGAL
        datosReporte.setValor(700, empresa.getTipoDoc().getDescripcion() + ":" + empresa.getNumIdentificacion() + " " + empresa.getObservaciones());//OPTOMETRA U.L SALLE-BOGOTA        
        datosReporte.setValor(701, empresa.getRazonSocial());//
        datosReporte.setValor(702, "CONSULTORIO " + empresa.getDireccion() + " " + empresa.getCodMunicipio().getDescripcion() + "  TELEFONO: " + empresa.getTelefono1());//CONSULTRIO

        //datos informacion general de referencia-contrareferencia
        datosReporte.setValor(703, "<b>INSTITUCION :</b>" + loginMB.getEmpresaActual().getRazonSocial());
        datosReporte.setValor(704, "<b>E.S.E.IO :</b>" + loginMB.getEmpresaActual().getCodMunicipio().getDescripcion());
        datosReporte.setValor(705, "<b>ESPECIALIDAD :</b>" + regEncontrado.getIdMedico().getEspecialidad().getDescripcion());
        datosReporte.setValor(706, "<b>NIVEL :</b>" + loginMB.getEmpresaActual().getNivel());
        
        
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 71) {//Datos para valoració 0 a 5 anios
            datosReporte.setValor(99, regEncontrado.getIdRegistro());
        }
        
        //datos de clasificaciones historia RCV y nutricion 707 - 729
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54 || regEncontrado.getIdTipoReg().getIdTipoReg() == 77) {
            tipoRegistroClinicoActual = regEncontrado.getIdTipoReg();
            v = 0;
            pas = 0;
            pad = 0;
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                if (!datosReporte2.getDato128().equals("")) {
                    pas_d = Integer.parseInt(datosReporte2.getDato128().toString());
                }
                if (!datosReporte2.getDato129().equals("")) {
                    pad_d = Integer.parseInt(datosReporte2.getDato129().toString());
                }
                if (!datosReporte2.getDato248().equals("")) {
                    pas_i = Integer.parseInt(datosReporte2.getDato248().toString());
                }
                if (!datosReporte2.getDato249().equals("")) {
                    pad_i = Integer.parseInt(datosReporte2.getDato249().toString());
                }
                if (!datosReporte2.getDato251().equals("")) {
                    pas_c = Integer.parseInt(datosReporte2.getDato251().toString());
                }
                if (!datosReporte2.getDato252().equals("")) {
                    pad_c = Integer.parseInt(datosReporte2.getDato252().toString());
                }
                if (!datosReporte2.getDato254().equals("")) {
                    pas_ce = Integer.parseInt(datosReporte2.getDato254().toString());
                }
                if (!datosReporte2.getDato255().equals("")) {
                    pad_ce = Integer.parseInt(datosReporte2.getDato255().toString());
                }
                calculo_imc();
                datosReporte.setValor(707, pas_de + "");
                datosReporte.setValor(708, pad_de + "");
                datosReporte.setValor(709, clasificacion_pa);
            }
            v = 0;
            peso_to = 0.0;
            clasificacion_peso = "Sin Dato...";
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                if (!datosReporte2.getDato131().equals("")) {
                    peso = Double.parseDouble(datosReporte2.getDato131().toString());
                }
                if (!datosReporte2.getDato132().equals("")) {
                    talla = Double.parseDouble(datosReporte2.getDato132().toString());
                }
            }
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 77) {
                if (!datosReporte2.getDato21().equals("")) {
                    peso = Double.parseDouble(datosReporte2.getDato21().toString());
                }
                if (!datosReporte2.getDato22().equals("")) {
                    talla = Double.parseDouble(datosReporte2.getDato22().toString());
                }
            }
            calculo_imc_peso();

            datosReporte.setValor(710, formateadorDecimal.format(peso_to) + "");
            datosReporte.setValor(711, clasificacion_peso);

            v = 0;
            clasificacion_perimetro = "Sin Dato...";
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                perimetro = 0.0;
                if (!datosReporte2.getDato137().equals("")) {
                    perimetro = Double.parseDouble(datosReporte2.getDato137().toString());
                }
            }
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 77) {
                perimetro = 0.0;
                if (!datosReporte2.getDato23().equals("")) {
                    perimetro = Double.parseDouble(datosReporte2.getDato23().toString());
                }
            }
            calculo_imc_perimetro();
            datosReporte.setValor(712, clasificacion_perimetro);
            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 77) {
                datosFormulario.setDato15(horas);
                datosFormulario.setDato258(minutos);
                if (!datosReporte2.getDato15().equals("")) {
                    horas = Integer.parseInt(datosReporte2.getDato15().toString());
                }
                if (!datosReporte2.getDato258().equals("")) {
                    minutos = Integer.parseInt(datosReporte2.getDato258().toString());
                }
                if (horas > 0) {
                    clasificacion_f = "-";
                } else if (minutos >= 30) {
                    clasificacion_f = "-";
                } else {
                    clasificacion_f = "SEDENTARISMO";
                }
                datosReporte.setValor(713, clasificacion_f);
            }
        }

        /**
         * Se cargan datos Del 730 en adelante se encuentran datos
         * pertenecientes a las imagenes de los reportes.
         */
        datosReporte.setValor(730, loginMB.getRutaCarpetaImagenes() + "Reportes/1.png"); //Flujograma
        datosReporte.setValor(731, loginMB.getRutaCarpetaImagenes() + "Reportes/2.png"); //Altura uterina
        datosReporte.setValor(732, loginMB.getRutaCarpetaImagenes() + "Reportes/3.png"); //Peso durante la gestación
        datosReporte.setValor(733, loginMB.getRutaCarpetaImagenes() + "Reportes/4.png"); //Presión Arterial
        datosReporte.setValor(734, loginMB.getRutaCarpetaImagenes() + "Reportes/5.png"); //Relación Peso - Talla
        datosReporte.setValor(735, loginMB.getRutaCarpetaImagenes() + "Reportes/riesgobiopsicosocial1.png"); //riesgobiopsicosocial - PRIMERA IMAGEN
        datosReporte.setValor(736, loginMB.getRutaCarpetaImagenes() + "Reportes/riesgobiopsicosocial2.png"); //riesgobiopsicosocial - SEGUNDA IMAGEN
        datosReporte.setValor(737, loginMB.getRutaCarpetaImagenes() + "Reportes/tallanina5a18.png"); //tallanina5a18 - VALORACIÓN DE CRECIMIENTO PARA NIÑAS DE 5 A 18 AÑOS
        datosReporte.setValor(738, loginMB.getRutaCarpetaImagenes() + "Reportes/masacorporalnina5a18.png"); //masacorporalnina5a18 - VALORACIÓN DE CRECIMIENTO PARA NIÑAS DE 5 A 18 AÑOS
        datosReporte.setValor(739, loginMB.getRutaCarpetaImagenes() + "Reportes/tallanino5a18.png"); //tallanino5a18 - VALORACIÓN DE CRECIMIENTO PARA NIÑOS DE 5 A 18 AÑOS
        datosReporte.setValor(740, loginMB.getRutaCarpetaImagenes() + "Reportes/masacorporalnino5a18.png"); //masacorporalnino5a18 - VALORACIÓN DE CRECIMIENTO PARA NIÑOS DE 5 A 18 AÑOS
        datosReporte.setValor(741, loginMB.getRutaCarpetaImagenes() + "Reportes/pesotallanina0a2.png"); //pesotallanina0a2 - VALORACIÓN DE CRECIMIENTO PARA NIÑAS DE 0 A 5 AÑOS
        datosReporte.setValor(742, loginMB.getRutaCarpetaImagenes() + "Reportes/pesomasacorporal0a2.png"); //pesomasacorporal0a2 - VALORACIÓN DE CRECIMIENTO PARA NIÑAS DE 0 A 5 AÑOS
        datosReporte.setValor(743, loginMB.getRutaCarpetaImagenes() + "Reportes/riesgobiopsicosocial1.png"); //riesgobiopsicosocial1 - RIESGO BIOPSICOSOCIAL
        datosReporte.setValor(744, loginMB.getRutaCarpetaImagenes() + "Reportes/riesgobiopsicosocial2.png"); //riesgobiopsicosocial2 - RIESGO BIOPSICOSOCIAL
        datosReporte.setValor(745, loginMB.getRutaCarpetaImagenes() + "Reportes/signosvitales.png"); //signosvitales - SIGNOS VITALES

        /**
         * Se cargan los datos que fueron llenados en el regsitro "hc_detalle",
         * de ser un tipo "especial" según valor en el campo "tabla" de
         * "hc_campos_reg", se realiza una acción especial según sea el caso de
         * no poseer valor en dicho campo, simplemente se realiza etiqueta +
         * valor del campo.
         */
        for (HcDetalle campoDeRegistroEncontrado : listaCamposDeRegistroEncontrado) { //recorre la lista de datos encontrados
            if (campoDeRegistroEncontrado.getHcCamposReg().getTabla() != null && campoDeRegistroEncontrado.getHcCamposReg().getTabla().length() >= 5) {//ES CATEGORIA (realizar busqueda)
                switch (campoDeRegistroEncontrado.getHcCamposReg().getTabla()) {
                    case "cfg_clasificaciones":
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "cfg_clasificaciones_2"://el mismo anterior pero imprimiendo tambien el codigo de la clasificacion
                        clasificacionBuscada = clasificacionesFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (clasificacionBuscada != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + clasificacionBuscada.getCodigo() + " - " + clasificacionBuscada.getDescripcion());
                        }
                        break;
                    case "fac_servicio":
                        servicioBuscado = servicioFacade.find(Integer.parseInt(campoDeRegistroEncontrado.getValor()));
                        if (servicioBuscado != null) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + servicioBuscado.getNombreServicio());
                        }
                        break;
                    case "boolean":
                        if (campoDeRegistroEncontrado.getValor().compareTo("true") == 0) {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> SI");
                        } else {
                            datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b> NO");
                        }
                        break;
                    case "date":
                    case "date2": //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                    case "html":
                        datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
                        break;
                }
            } else {//NO ES CATEGORIA (sacar valor)
                datosReporte.setValor(campoDeRegistroEncontrado.getHcCamposReg().getPosicion(), "<b>" + campoDeRegistroEncontrado.getHcCamposReg().getNombrePdf() + " </b>" + campoDeRegistroEncontrado.getValor());
            }
        }

        List<DatosSubReporteHistoria> listaDatosAdicionales = new ArrayList<>();
        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 17) { //psicologia
            cargarEstructuraFamiliar();

            for (FilaDataTable item : listaEstructuraFamiliar) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna5());
                datosS.setDato3(item.getColumna6());
                listaDatosAdicionales.add(datosS);

                System.out.println("Familiar " + item.getColumna1());
            }
        }

        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 19) { //formulacion medicamentos
            List<FilaDataTable> listaMedicamentosParaReporte = cargarListaMedicamentos(regEncontrado);

            for (FilaDataTable item : listaMedicamentosParaReporte) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna3());
                datosS.setDato3(item.getColumna4());
                datosS.setDato4(item.getColumna5());
                datosS.setDato5(item.getColumna6());
                datosS.setDato6(item.getColumna7());
                datosS.setDato7(item.getColumna8());
                datosS.setDato8(item.getColumna9());
                listaDatosAdicionales.add(datosS);
                System.out.println("Medicamento  " + item.getColumna1());
            }

        }

        if (regEncontrado.getIdTipoReg().getIdTipoReg() == 25) { //ordenes medicas
            List<FilaDataTable> listaServiciosParaReporte = cargarListaServicios(regEncontrado);

            for (FilaDataTable item : listaServiciosParaReporte) {
                DatosSubReporteHistoria datosS = new DatosSubReporteHistoria();
                datosS.setDato0(item.getColumna1());
                datosS.setDato1(item.getColumna2());
                datosS.setDato2(item.getColumna3());
                datosS.setDato3(item.getColumna4());
                listaDatosAdicionales.add(datosS);
                System.out.println("Servicio  " + item.getColumna1());
            }

        }

        System.out.println("listaDatosAdicionales size " + listaDatosAdicionales.size());

        datosReporte.setListaDatosAdicionales(listaDatosAdicionales);//CUANDO SE USE SUBRREPORTES USAR ESTA LINEA
        listaRegistrosParaPdf.add(datosReporte);
    }

    public void generarPdf() throws JRException, IOException {//genera un pdf de una historia seleccionada en el historial        
        //----------------------------------------------------------------------
        //VALIDACIONES NECESARIAS-----------------------------------------------
        //----------------------------------------------------------------------
        if (treeNodesSeleccionados == null) {//hay nodos seleccionados en el arbol
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        NodoArbolHistorial nodArbolHisSeleccionado = null;
        for (TreeNode nodo : treeNodesSeleccionados) {
            nodArbolHisSeleccionado = (NodoArbolHistorial) nodo.getData();
            if (nodArbolHisSeleccionado.getTipoDeNodo() == TipoNodoEnum.REGISTRO_HISTORIAL) {
                break;
            }
        }
        if (nodArbolHisSeleccionado == null) {//de los nodos seleccionados ninguno es regitro clinico
            imprimirMensaje("Error", "Se debe seleccionar un registro del histórico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        HcRegistro regEncontrado = registroFacade.find(nodArbolHisSeleccionado.getIdRegistro());
        System.out.println("...regEncontrado " + regEncontrado.getHcDetalleList().size());

        //----------------------------------------------------------------------
        //SE CARGA LA FUENTE DE DATOS PARA EL PDF ------------------------------
        //----------------------------------------------------------------------
        System.out.println("Tipo registro  " + regEncontrado.getIdTipoReg().getUrlPagina());
        System.out.println(" regEncontrado.getIdTipoReg()..getUrlPagina().startsWith(g_)  = " + regEncontrado.getIdTipoReg().getNombre().startsWith("g_"));

        /**
         * La validación se hace por un reporte con 257 campos, y
         * "cargarFuenteDatosReportesGrandes" permite hasta 200, para evitar ir
         * subiendo de 100 en 100 y que luego aparezca un reporte de por decir
         * algo, 320 campos, se hará un salto más grande, uno de 600, entonces
         * luego del valor 600 se encontraran siempre los datos del paciente,
         * del médico, etc, y del valor 0 al 599 se encontraran los datos de la
         * consulta.
         *
         * A la hora de crear los reportes, tomar las previsiones, importar los
         * "datos600" en adelante, los que se necesiten, y del 0 al 599 de igual
         * manera.
         *
         *
         */
        if (regEncontrado.getIdTipoReg().getUrlPagina().startsWith("xl_")) { //especificamente para reportes de odotología que son muy extensos.
            cargarFuenteDatosReportesXL(regEncontrado);
        } else if (regEncontrado.getIdTipoReg().getUrlPagina().startsWith("s_") || regEncontrado.getIdTipoReg().getUrlPagina().startsWith("h_")) { //si es de los nuevos reportes con mas de 200 campos y máximo 599
            cargarFuenteDatosReportesExtraGrandes(regEncontrado);
        } else if (regEncontrado.getIdTipoReg().getUrlPagina().startsWith("g_")) { //si es de los nuevos reportes con mas de 40 campos
            cargarFuenteDatosReportesGrandes(regEncontrado);
        } else {
            cargarFuenteDatos(regEncontrado);
        }
        System.out.println("...Num datos encontrados " + listaRegistrosParaPdf.size());

        //----------------------------------------------------------------------
        //CREACION DEL PDF -----------------------------------------------------
        //----------------------------------------------------------------------
        JRBeanCollectionDataSource beanCollectionDataSource;
        HttpServletResponse httpServletResponse;
        String rutaReporte = "";
        rutaReporte = rutaReporte + "historias/reportes/";
        rutaReporte = rutaReporte + regEncontrado.getIdTipoReg().getUrlPagina();
        rutaReporte = rutaReporte.substring(0, rutaReporte.length() - 6);//quito .xhtml
        rutaReporte = rutaReporte + ".jasper";

        String fecha_registro = sdfDateHour.format(regEncontrado.getFechaReg());

        System.out.println("Reporte --> " + rutaReporte);
        System.out.println("....................................");

        List<JasperPrint> list = new ArrayList<JasperPrint>();
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String urlImagenes = request.getRequestURL().toString();
        urlImagenes = urlImagenes.substring(0, urlImagenes.indexOf("historias.xhtml")) + "img/";
        HashMap mapParams = new HashMap();
        mapParams.put("urlBaseImagenes", urlImagenes);
        beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
        httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath(rutaReporte), mapParams, beanCollectionDataSource);
            list.add(jasperPrint);

            if (regEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                rutaReporte = "historias/reportes/s_paraclinicossolicitados.jasper";
                beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                httpServletResponse.setContentType("application/pdf");
                servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath(rutaReporte), new HashMap(), beanCollectionDataSource);
                list.add(jasperPrint);
                rutaReporte = "historias/reportes/s_valoracionespecialista.jasper";
                beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
                httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                httpServletResponse.setContentType("application/pdf");
                servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath(rutaReporte), new HashMap(), beanCollectionDataSource);
                list.add(jasperPrint);

            }

            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, list);
            exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
            exporter.exportReport();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {
            System.out.println("Error generando reporte " + ex.getMessage());
        }
    }

//    public void generarPdfAgrupado() throws JRException, IOException {//genera un pdf de un serie de historias seleccionadas en el historial        
//        if (cargarFuenteDeDatosAgrupada()) {//si se puede cargar datos continuo
//            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosParaPdf);
//            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
//                httpServletResponse.setContentType("application/pdf");
//                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//                JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("historias/reportes/reporteRegCliAgrupado.jasper"), new HashMap(), beanCollectionDataSource);
//                JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
//                FacesContext.getCurrentInstance().responseComplete();
//            }
//        }
//    }
    //---------------------------------------------------
    //--------- FUNCIONES REGISTROS CLINICOS ------------
    //---------------------------------------------------
    public void limpiarFormulario() {//reinicia todos los controles de un registro clinico
        falla_atencion = false;
        fc = 0;
        fr = 0;
        t = 0.0;
        pas = 0;
        pad = 0;
        pas_c = 0;
        pas_ce = 0;
        pas_d = 0;
        pas_de = 0;
        pas_i = 0;
        pad_c = 0;
        pad_ce = 0;
        pad_d = 0;
        pad_de = 0;
        pad_i = 0;
        peso = 0.0;
        peso_to = 0.0;
        horas = 0;
        minutos = 0;
        talla = 0.0;
        perimetro = 0.0;
        modificandoRegCli = false;
        registroEncontrado = null;
        datosFormulario.limpiar();
        lista_reportes.clear();
        fecha = null;
        examen = "";
        tipo = 0;
        listaMunicipios = new ArrayList<>();
        idPrestador = null;
        especialidadPrestador = null;
        fechaReg = new Date();
        fechaSis = new Date();
        //seleccionar medico de ser posible
        if (loginMB.getUsuarioActual().getTipoUsuario().getCodigo().equals("2")) {//es un prestador
            for (CfgUsuarios prestador : listaPrestadores) {
                if (Objects.equals(prestador.getIdUsuario(), loginMB.getUsuarioActual().getIdUsuario())) {
                    idPrestador = loginMB.getUsuarioActual().getIdUsuario().toString();
                    if (prestador.getEspecialidad() != null) {
                        especialidadPrestador = prestador.getEspecialidad().getDescripcion();
                    }
                    break;
                }
            }
        }

        datosFormulario = new DatosFormularioHistoria();
        listaEstructuraFamiliar.clear();
        listaEstructuraFamiliarFiltro.clear();
        listaMedicamentos.clear();
        listaMedicamentosFiltro.clear();
        listaServicios.clear();
        listaServiciosFiltro.clear();
        listaServiciosOrdenMedica.clear();

    }

    public void btnLimpiarFormulario() {//no se carga ultimo registro
        limpiarFormulario();
        valoresPorDefecto();
        modificandoRegCli = false;
    }

    public void cambiaTipoRegistroClinico() {//cambia el combo 'tipo de registro clinico'
        modificandoRegCli = false;
        limpiarFormulario();
        cargarUltimoRegistro();
        valoresPorDefecto();
    }

    public void manageFile(FileUploadEvent event) {
        if (!"".equals(descriparchivo)) {

            try {

                UploadedFile file = event.getFile();
                SimpleDateFormat dt = new SimpleDateFormat("yyyyymmddhhmmss");
                String nomarch = "HC_" + pacienteSeleccionado.getIdPaciente() + "_" + dt.format(new Date())//diferenciar el usuario actual
                        + file.getFileName().substring(file.getFileName().lastIndexOf("."), file.getFileName().length());//colocar extension
                String path = loginMB.getUrlFile() + nomarch;
                if (uploadArchivo(file, path)) {
                    //guardar en base de datos
                    try {
                        HcArchivos a = new HcArchivos();
                        a.setDescripcion(descriparchivo);
                        a.setUrlFile(path);
                        a.setFechaUpload(new Date());
                        a.setIdPaciente(pacienteSeleccionado);
                        hcArchivosFacade.create(a);
                        //
                        listaArchivo = hcArchivosFacade.getHcArchivosByPaciente(pacienteSeleccionado);
                        imprimirMensaje("Información", "El archivo se guardo", FacesMessage.SEVERITY_INFO);
                        descriparchivo = "";

                    } catch (Exception e) {
                        imprimirMensaje("Error", "El archivo no se guardo", FacesMessage.SEVERITY_WARN);

                    }

                }

            } catch (Exception ex) {
                System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
            }

        } else {
            imprimirMensaje("Error", "Agregar una descripcion porfavor y seleccionar de nuevo el archivo", FacesMessage.SEVERITY_WARN);

        }

    }

    public void downloadArchivo() throws FileNotFoundException, MagicParseException, MagicMatchNotFoundException, MagicException {
        if (archivoSeleccionado != null) {
            File f = new File(archivoSeleccionado.getUrlFile());
            InputStream stream = new FileInputStream(f);
            //String mimeType = Magic.getMagicMatch(f, true).getMimeType();
            fileDownload = new DefaultStreamedContent(stream, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", f.getName());
        }
    }

    public void guardarOdontograma() {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> valores = new Gson().fromJson(this.jsonOdontograma, listType);
        for (String valor : valores) {
            try {
                String[] pares = valor.split("=");
                String nombreMetodo = String.format("setDato%1$s", pares[0]);
                Method metodo = datosFormulario.getClass().getMethod(nombreMetodo, Object.class);
                metodo.invoke(datosFormulario, pares[1]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void guardarEvolucion() {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> valores = new Gson().fromJson(this.jsonEvolucion, listType);
        for (String valor : valores) {
            try {
                String[] pares = valor.split("=");
                String nombreMetodo = String.format("setDato%1$s", pares[0]);
                Method metodo = datosFormulario.getClass().getMethod(nombreMetodo, Object.class);
                metodo.invoke(datosFormulario, pares[1]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void guardarOleary1() {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> valores = new Gson().fromJson(this.jsonOleary1, listType);
        for (String valor : valores) {
            try {
                String[] pares = valor.split("=");
                String nombreMetodo = String.format("setDato%1$s", pares[0]);
                Method metodo = datosFormulario.getClass().getMethod(nombreMetodo, Object.class);
                metodo.invoke(datosFormulario, pares[1]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void guardarOleary2() {
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> valores = new Gson().fromJson(this.jsonOleary2, listType);
        for (String valor : valores) {
            try {
                String[] pares = valor.split("=");
                String nombreMetodo = String.format("setDato%1$s", pares[0]);
                Method metodo = datosFormulario.getClass().getMethod(nombreMetodo, Object.class);
                metodo.invoke(datosFormulario, pares[1]);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void cargarUltimoRegistro() {
        mostrarFormularioRegistroClinico();
        if (tipoRegistroClinicoActual != null) {
            registroEncontrado = registroFacade.buscarUltimo(pacienteSeleccionado.getIdPaciente(), tipoRegistroClinicoActual.getIdTipoReg());
            List<HcRepExamenes> lista = hcRepFacade.buscarRepUltimo(pacienteSeleccionado.getIdPaciente());
            lista_reportes.clear();
            if (lista != null) {
                for (HcRepExamenes rep : lista) {
                    int i = 0;
                    for (FilaDataTable ee : lista_reportes) {
                        if (ee.getColumna1().equals(rep.getPosicion().toString())) {
                            i++;
                        }
                    }
                    if (i == 0) {
                        FilaDataTable f = new FilaDataTable();
                        f.setColumna1(rep.getPosicion() + "");
                        f.setColumna2(rep.getNombreParaclinico() + "");
                        f.setColumna3(sdfDate.format(rep.getFecha()));
                        f.setColumna4(rep.getResultado());
                        lista_reportes.add(f);
                    }
                }
            }
            if (registroEncontrado != null) {
                Date d = new Date();
                long secs = (d.getTime() - registroEncontrado.getFechaSis().getTime()) / 1000;
                int hours = (int) (secs / 3600);
                secs = secs % 3600;
                int mins = (int) (secs / 60);
                secs = secs % 60;
                System.out.println(hours + " " + mins + " " + registroEncontrado.getFechaSis());
                if (registroEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                    if (hours <= 23 && mins <= 59) {
                        modificandoRegCli = true;
                        imprimirMensaje("Informacion", "Le restan " + (23 - hours) + " Hora(s) y " + (59 - mins) + " Minuto(s) para agregar alguna observación o hallazgo.", FacesMessage.SEVERITY_INFO);
                    } else {
                        modificandoRegCli = false;
                    }
                }
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
                    cargarMunicipios();//intento cargar municipios sea o no necesario
                }
                if (registroEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                    //signos vitales
                    if (!datosFormulario.getDato125().equals("")) {
                        fc = Integer.parseInt(datosFormulario.getDato125().toString());
                    }
                    if (!datosFormulario.getDato126().equals("")) {
                        fr = Integer.parseInt(datosFormulario.getDato126().toString());
                    }
                    if (!datosFormulario.getDato127().equals("")) {
                        t = Double.parseDouble(datosFormulario.getDato127().toString());
                    }
                    //MEDIDAS ANTOPOMÉTRICAS
                    if (!datosFormulario.getDato131().equals("")) {
                        peso = Double.parseDouble(datosFormulario.getDato131().toString());
                    }
                    if (!datosFormulario.getDato132().equals("")) {
                        talla = Double.parseDouble(datosFormulario.getDato132().toString());
                    }
                    if (!datosFormulario.getDato137().equals("")) {
                        perimetro = Double.parseDouble(datosFormulario.getDato137().toString());
                    }
                    //condicion normal/especial
                    if (!datosFormulario.getDato128().equals("")) {
                        pas_d = Integer.parseInt(datosFormulario.getDato128().toString());
                    }
                    if (!datosFormulario.getDato129().equals("")) {
                        pad_d = Integer.parseInt(datosFormulario.getDato129().toString());
                    }
                    if (!datosFormulario.getDato248().equals("")) {
                        pas_i = Integer.parseInt(datosFormulario.getDato248().toString());
                    }
                    if (!datosFormulario.getDato249().equals("")) {
                        pad_i = Integer.parseInt(datosFormulario.getDato249().toString());
                    }
                    if (!datosFormulario.getDato251().equals("")) {
                        pas_c = Integer.parseInt(datosFormulario.getDato251().toString());
                    }
                    if (!datosFormulario.getDato252().equals("")) {
                        pad_c = Integer.parseInt(datosFormulario.getDato252().toString());
                    }
                    if (!datosFormulario.getDato254().equals("")) {
                        pas_ce = Integer.parseInt(datosFormulario.getDato254().toString());
                    }
                    if (!datosFormulario.getDato255().equals("")) {
                        pad_ce = Integer.parseInt(datosFormulario.getDato255().toString());
                    }
                }
                System.out.println("Tipo registro " + registroEncontrado.getIdTipoReg().getIdTipoReg());
                if (registroEncontrado.getIdTipoReg().getIdTipoReg() == 54) {
                    if (hours > 23) {
                        imprimirMensaje("Informacion", "Para su facilidad se cargo los datos de la última historia de este tipo de registro", FacesMessage.SEVERITY_INFO);
                    }
                } else {
                    imprimirMensaje("Informacion", "Para su facilidad se cargo los datos de la última historia de este tipo de registro", FacesMessage.SEVERITY_INFO);
                }
            } else {
                valoresPorDefecto();
                imprimirMensaje("Informacion", "El paciente no tiene registros anteriores de este tipo", FacesMessage.SEVERITY_INFO);
            }
            //ESPECIAL PARA NUTRICION HC 77
            if (tipoRegistroClinicoActual.getIdTipoReg() == 77) {
                datosFormulario_nutricion.limpiar();
                registroEncontrado = registroFacade.buscarUltimo(pacienteSeleccionado.getIdPaciente(), 54);
                if (registroEncontrado != null) {
                    List<HcDetalle> listaDetalles = registroEncontrado.getHcDetalleList();
                    for (HcDetalle detalle : listaDetalles) {
                        HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
                        if (campo != null) {
                            if (campo.getTabla() != null && campo.getTabla().length() != 0) {
                                switch (campo.getTabla()) {
                                    case "date":
                                        try {
                                            Date f = sdfDateHour.parse(detalle.getValor());
                                            datosFormulario_nutricion.setValor(campo.getPosicion(), f);
                                        } catch (ParseException ex) {
                                            datosFormulario_nutricion.setValor(campo.getPosicion(), "");
                                        }
                                        break;
                                    default://es una categoria
                                        datosFormulario_nutricion.setValor(campo.getPosicion(), detalle.getValor());
                                        break;
                                }
                            } else {
                                datosFormulario_nutricion.setValor(campo.getPosicion(), detalle.getValor());
                            }
                        }
                    }
                    registroEncontrado = registroFacade.buscarUltimo(pacienteSeleccionado.getIdPaciente(), 77);
                    datosFormulario.limpiar();
                    if (registroEncontrado != null) {
                        listaDetalles = registroEncontrado.getHcDetalleList();
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
                            cargarMunicipios();//intento cargar municipios sea o no necesario
                        }
                    }
                    //MEDIDAS ANTROPOMETRICAS
                    if (datosFormulario.getDato21().equals("")) {//PESO
                        if (!datosFormulario_nutricion.getDato131().equals("")) {//PESO 
                            peso = Double.parseDouble(datosFormulario_nutricion.getDato131().toString());
                        } else {
                            peso = 0.0;
                        }
                    } else {
                        peso = Double.parseDouble(datosFormulario.getDato21().toString());
                    }
                    if (datosFormulario.getDato22().equals("")) {//TALLA
                        if (!datosFormulario_nutricion.getDato132().equals("")) {//TALLA 
                            talla = Double.parseDouble(datosFormulario_nutricion.getDato132().toString());
                        } else {
                            talla = 0.0;
                        }
                    } else {
                        talla = Double.parseDouble(datosFormulario.getDato22().toString());
                    }
                    if (datosFormulario.getDato23().equals("")) {//PERIMETRO ABDOMINAL
                        if (!datosFormulario_nutricion.getDato137().equals("")) {//PERIMETRO ABDOMINAL 
                            perimetro = Double.parseDouble(datosFormulario_nutricion.getDato137().toString());
                        } else {
                            perimetro = 0.0;
                        }
                    } else {
                        perimetro = Double.parseDouble(datosFormulario.getDato23().toString());
                    }

                    //ANTECEDENTES
                    //--PERSONALES
                    if (datosFormulario.getDato47().equals("")) {//HTA
                        if (!datosFormulario_nutricion.getDato20().equals("")) {
                            datosFormulario.setDato47(datosFormulario_nutricion.getDato20());
                        }
                    }
                    if (datosFormulario.getDato48().equals("")) {
                        if (!datosFormulario_nutricion.getDato291().equals("")) {//OBSERVACION
                            datosFormulario.setDato48(datosFormulario_nutricion.getDato291());
                        }
                    }

                    if (datosFormulario.getDato49().equals("")) {//ENF. ENDOCRINA
                        if (!datosFormulario_nutricion.getDato21().equals("")) {
                            datosFormulario.setDato49(datosFormulario_nutricion.getDato21());
                        }
                    }
                    if (datosFormulario.getDato50().equals("")) {
                        if (!datosFormulario_nutricion.getDato292().equals("")) {//OBSERVACION
                            datosFormulario.setDato50(datosFormulario_nutricion.getDato292());
                        }
                    }

                    if (datosFormulario.getDato51().equals("")) {//ACV
                        if (!datosFormulario_nutricion.getDato22().equals("")) {
                            datosFormulario.setDato51(datosFormulario_nutricion.getDato22());
                        }
                    }
                    if (datosFormulario.getDato52().equals("")) {
                        if (!datosFormulario_nutricion.getDato293().equals("")) {//OBSERVACION
                            datosFormulario.setDato52(datosFormulario_nutricion.getDato293());
                        }
                    }

                    if (datosFormulario.getDato53().equals("")) {//GENITOURINARIO
                        if (!datosFormulario_nutricion.getDato23().equals("")) {
                            datosFormulario.setDato53(datosFormulario_nutricion.getDato23());
                        }
                    }
                    if (datosFormulario.getDato54().equals("")) {
                        if (!datosFormulario_nutricion.getDato294().equals("")) {//OBSERVACION
                            datosFormulario.setDato54(datosFormulario_nutricion.getDato294());
                        }
                    }

                    if (datosFormulario.getDato55().equals("")) {//DIABETES
                        if (!datosFormulario_nutricion.getDato24().equals("")) {
                            datosFormulario.setDato55(datosFormulario_nutricion.getDato24());
                        }
                    }
                    if (datosFormulario.getDato56().equals("")) {
                        if (!datosFormulario_nutricion.getDato295().equals("")) {//OBSERVACION
                            datosFormulario.setDato56(datosFormulario_nutricion.getDato295());
                        }
                    }

                    if (datosFormulario.getDato57().equals("")) {//ENF. RENAL
                        if (!datosFormulario_nutricion.getDato25().equals("")) {
                            datosFormulario.setDato57(datosFormulario_nutricion.getDato25());
                        }
                    }
                    if (datosFormulario.getDato58().equals("")) {
                        if (!datosFormulario_nutricion.getDato296().equals("")) {//OBSERVACION
                            datosFormulario.setDato58(datosFormulario_nutricion.getDato296());
                        }
                    }

                    if (datosFormulario.getDato59().equals("")) {//EPOC
                        if (!datosFormulario_nutricion.getDato26().equals("")) {
                            datosFormulario.setDato59(datosFormulario_nutricion.getDato26());
                        }
                    }
                    if (datosFormulario.getDato60().equals("")) {
                        if (!datosFormulario_nutricion.getDato297().equals("")) {//OBSERVACION
                            datosFormulario.setDato60(datosFormulario_nutricion.getDato297());
                        }
                    }

                    if (datosFormulario.getDato61().equals("")) {//CA. GÁSTRICO
                        if (!datosFormulario_nutricion.getDato27().equals("")) {
                            datosFormulario.setDato61(datosFormulario_nutricion.getDato27());
                        }
                    }
                    if (datosFormulario.getDato62().equals("")) {
                        if (!datosFormulario_nutricion.getDato298().equals("")) {//OBSERVACION
                            datosFormulario.setDato62(datosFormulario_nutricion.getDato298());
                        }
                    }

                    if (datosFormulario.getDato63().equals("")) {//DISLIPIDEMIA
                        if (!datosFormulario_nutricion.getDato28().equals("")) {
                            datosFormulario.setDato63(datosFormulario_nutricion.getDato28());
                        }
                    }
                    if (datosFormulario.getDato64().equals("")) {
                        if (!datosFormulario_nutricion.getDato299().equals("")) {//OBSERVACION
                            datosFormulario.setDato64(datosFormulario_nutricion.getDato299());
                        }
                    }

                    if (datosFormulario.getDato65().equals("")) {//OBESIDAD
                        if (!datosFormulario_nutricion.getDato29().equals("")) {
                            datosFormulario.setDato65(datosFormulario_nutricion.getDato29());
                        }
                    }
                    if (datosFormulario.getDato66().equals("")) {
                        if (!datosFormulario_nutricion.getDato300().equals("")) {//OBSERVACION
                            datosFormulario.setDato66(datosFormulario_nutricion.getDato300());
                        }
                    }

                    if (datosFormulario.getDato67().equals("")) {//ASMA
                        if (!datosFormulario_nutricion.getDato30().equals("")) {
                            datosFormulario.setDato67(datosFormulario_nutricion.getDato30());
                        }
                    }
                    if (datosFormulario.getDato68().equals("")) {
                        if (!datosFormulario_nutricion.getDato301().equals("")) {//OBSERVACION
                            datosFormulario.setDato68(datosFormulario_nutricion.getDato301());
                        }
                    }

                    if (datosFormulario.getDato69().equals("")) {//CA. MAMA
                        if (!datosFormulario_nutricion.getDato31().equals("")) {
                            datosFormulario.setDato69(datosFormulario_nutricion.getDato31());
                        }
                    }
                    if (datosFormulario.getDato70().equals("")) {
                        if (!datosFormulario_nutricion.getDato302().equals("")) {//OBSERVACION
                            datosFormulario.setDato70(datosFormulario_nutricion.getDato302());
                        }
                    }

                    if (datosFormulario.getDato71().equals("")) {//ENF. CORONARIA
                        if (!datosFormulario_nutricion.getDato32().equals("")) {
                            datosFormulario.setDato71(datosFormulario_nutricion.getDato32());
                        }
                    }
                    if (datosFormulario.getDato72().equals("")) {
                        if (!datosFormulario_nutricion.getDato303().equals("")) {//OBSERVACION
                            datosFormulario.setDato72(datosFormulario_nutricion.getDato303());
                        }
                    }

                    if (datosFormulario.getDato73().equals("")) {//ENF. VASCULAR
                        if (!datosFormulario_nutricion.getDato33().equals("")) {
                            datosFormulario.setDato73(datosFormulario_nutricion.getDato33());
                        }
                    }
                    if (datosFormulario.getDato74().equals("")) {
                        if (!datosFormulario_nutricion.getDato304().equals("")) {//OBSERVACION
                            datosFormulario.setDato74(datosFormulario_nutricion.getDato304());
                        }
                    }

                    if (datosFormulario.getDato75().equals("")) {//QUIRÚRGICOS
                        if (!datosFormulario_nutricion.getDato35().equals("")) {
                            datosFormulario.setDato75(datosFormulario_nutricion.getDato35());
                        }
                    }
                    if (datosFormulario.getDato76().equals("")) {
                        if (!datosFormulario_nutricion.getDato305().equals("")) {//OBSERVACION
                            datosFormulario.setDato76(datosFormulario_nutricion.getDato305());
                        }
                    }

                    if (datosFormulario.getDato77().equals("")) {//EPIMIOLOGICOS
                        if (!datosFormulario_nutricion.getDato36().equals("")) {
                            datosFormulario.setDato77(datosFormulario_nutricion.getDato36());
                        }
                    }
                    if (datosFormulario.getDato78().equals("")) {
                        if (!datosFormulario_nutricion.getDato306().equals("")) {//OBSERVACION
                            datosFormulario.setDato78(datosFormulario_nutricion.getDato306());
                        }
                    }

                    if (datosFormulario.getDato79().equals("")) {//TRAUMAS
                        if (!datosFormulario_nutricion.getDato38().equals("")) {
                            datosFormulario.setDato79(datosFormulario_nutricion.getDato38());
                        }
                    }
                    if (datosFormulario.getDato80().equals("")) {
                        if (!datosFormulario_nutricion.getDato308().equals("")) {//OBSERVACION
                            datosFormulario.setDato80(datosFormulario_nutricion.getDato308());
                        }
                    }

                    if (datosFormulario.getDato81().equals("")) {//CA. CÉRVIX
                        if (!datosFormulario_nutricion.getDato37().equals("")) {
                            datosFormulario.setDato81(datosFormulario_nutricion.getDato37());
                        }
                    }
                    if (datosFormulario.getDato82().equals("")) {
                        if (!datosFormulario_nutricion.getDato307().equals("")) {//OBSERVACION
                            datosFormulario.setDato82(datosFormulario_nutricion.getDato307());
                        }
                    }

                    if (datosFormulario.getDato83().equals("")) {//ALÉRGICOS
                        if (!datosFormulario_nutricion.getDato39().equals("")) {
                            datosFormulario.setDato83(datosFormulario_nutricion.getDato39());
                        }
                    }
                    if (datosFormulario.getDato84().equals("")) {
                        if (!datosFormulario_nutricion.getDato309().equals("")) {//OBSERVACION
                            datosFormulario.setDato84(datosFormulario_nutricion.getDato309());
                        }
                    }

                    if (datosFormulario.getDato85().equals("")) {//FIEBRE REUMÁTICA
                        if (!datosFormulario_nutricion.getDato319().equals("")) {
                            datosFormulario.setDato85(datosFormulario_nutricion.getDato319());
                        }
                    }
                    if (datosFormulario.getDato86().equals("")) {
                        if (!datosFormulario_nutricion.getDato320().equals("")) {//OBSERVACION
                            datosFormulario.setDato86(datosFormulario_nutricion.getDato320());
                        }
                    }

                    if (datosFormulario.getDato87().equals("")) {//ENFERMEDADES DE TRANSMISIÓN SEXUAL
                        if (!datosFormulario_nutricion.getDato321().equals("")) {
                            datosFormulario.setDato87(datosFormulario_nutricion.getDato321());
                        }
                    }
                    if (datosFormulario.getDato88().equals("")) {
                        if (!datosFormulario_nutricion.getDato322().equals("")) {//OBSERVACION
                            datosFormulario.setDato88(datosFormulario_nutricion.getDato322());
                        }
                    }

                    if (datosFormulario.getDato89().equals("")) {//LEISHMANIASIS
                        if (!datosFormulario_nutricion.getDato323().equals("")) {
                            datosFormulario.setDato89(datosFormulario_nutricion.getDato323());
                        }
                    }
                    if (datosFormulario.getDato90().equals("")) {
                        if (!datosFormulario_nutricion.getDato324().equals("")) {//OBSERVACION
                            datosFormulario.setDato90(datosFormulario_nutricion.getDato324());
                        }
                    }

                    if (datosFormulario.getDato91().equals("")) {//LEPRA
                        if (!datosFormulario_nutricion.getDato325().equals("")) {
                            datosFormulario.setDato91(datosFormulario_nutricion.getDato325());
                        }
                    }
                    if (datosFormulario.getDato92().equals("")) {
                        if (!datosFormulario_nutricion.getDato326().equals("")) {//OBSERVACION
                            datosFormulario.setDato92(datosFormulario_nutricion.getDato326());
                        }
                    }

                    if (datosFormulario.getDato93().equals("")) {//MALARIA
                        if (!datosFormulario_nutricion.getDato327().equals("")) {
                            datosFormulario.setDato93(datosFormulario_nutricion.getDato327());
                        }
                    }
                    if (datosFormulario.getDato94().equals("")) {
                        if (!datosFormulario_nutricion.getDato328().equals("")) {//OBSERVACION
                            datosFormulario.setDato94(datosFormulario_nutricion.getDato328());
                        }
                    }

                    if (datosFormulario.getDato95().toString().equals("")) {//DENGUE
                        if (!datosFormulario_nutricion.getDato329().equals("")) {
                            datosFormulario.setDato95(datosFormulario_nutricion.getDato329());
                        }
                    }
                    if (datosFormulario.getDato96().equals("")) {
                        if (!datosFormulario_nutricion.getDato330().equals("")) {//OBSERVACION
                            datosFormulario.setDato96(datosFormulario_nutricion.getDato330());
                        }
                    }

                    if (datosFormulario.getDato97().equals("")) {//FIEBRE AMARILLA
                        if (!datosFormulario_nutricion.getDato331().equals("")) {
                            datosFormulario.setDato97(datosFormulario_nutricion.getDato331());
                        }
                    }
                    if (datosFormulario.getDato98().equals("")) {
                        if (!datosFormulario_nutricion.getDato332().equals("")) {//OBSERVACION
                            datosFormulario.setDato98(datosFormulario_nutricion.getDato332());
                        }
                    }
                    if (datosFormulario.getDato99().equals("")) {//TUBERCULOSIS PULMONAR Y EXTRAPULMONAR
                        if (!datosFormulario_nutricion.getDato333().equals("")) {
                            datosFormulario.setDato99(datosFormulario_nutricion.getDato333());
                        }
                    }
                    if (datosFormulario.getDato100().equals("")) {
                        if (!datosFormulario_nutricion.getDato334().equals("")) {//OBSERVACION
                            datosFormulario.setDato100(datosFormulario_nutricion.getDato334());
                        }
                    }
                    //--FARMACOLOGICOS
                    if (datosFormulario.getDato109().equals("")) {
                        if (!datosFormulario_nutricion.getDato41().equals("")) {//MEDICACIÓN PREVIA
                            datosFormulario.setDato109(datosFormulario_nutricion.getDato41());
                        }
                    }
                    if (datosFormulario.getDato110().equals("")) {
                        if (!datosFormulario_nutricion.getDato42().equals("")) {//PROFESIONAL QUE PRESCRIBE EL TRATAMIENTO
                            datosFormulario.setDato110(datosFormulario_nutricion.getDato42());
                        }
                    }
                    if (datosFormulario.getDato111().equals("")) {
                        if (!datosFormulario_nutricion.getDato43().equals("")) {//ADHERENCIA AL TRATAMIENTO
                            datosFormulario.setDato111(datosFormulario_nutricion.getDato43());
                        }
                    }
                    if (datosFormulario.getDato112().equals("")) {
                        if (!datosFormulario_nutricion.getDato44().equals("")) {//ALÉRGICO A MEDICAMENTOS
                            datosFormulario.setDato112(datosFormulario_nutricion.getDato44());
                        }
                    }
                    if (datosFormulario.getDato113().equals("")) {
                        if (!datosFormulario_nutricion.getDato45().equals("")) {//CUALES
                            datosFormulario.setDato113(datosFormulario_nutricion.getDato45());
                        }
                    }
                    //--GINECOSTETRICOS
                    if (datosFormulario.getDato149().equals("")) {
                        if (!datosFormulario_nutricion.getDato50().equals("")) {//MENARQUIA
                            datosFormulario.setDato149(datosFormulario_nutricion.getDato50());
                        }
                    }
                    if (datosFormulario.getDato150().equals("")) {
                        if (!datosFormulario_nutricion.getDato51().equals("")) {//FUM
                            datosFormulario.setDato150(datosFormulario_nutricion.getDato51());
                        }
                    }
                    if (datosFormulario.getDato151().equals("")) {
                        if (!datosFormulario_nutricion.getDato52().equals("")) {//MENOPAUSIA
                            datosFormulario.setDato151(datosFormulario_nutricion.getDato52());
                        }
                    }
                    if (datosFormulario.getDato152().equals("")) {
                        if (!datosFormulario_nutricion.getDato53().equals("")) {//G
                            datosFormulario.setDato152(datosFormulario_nutricion.getDato53());
                        }
                    }
                    if (datosFormulario.getDato153().equals("")) {
                        if (!datosFormulario_nutricion.getDato54().equals("")) {//P
                            datosFormulario.setDato153(datosFormulario_nutricion.getDato54());
                        }
                    }
                    if (datosFormulario.getDato154().equals("")) {
                        if (!datosFormulario_nutricion.getDato55().equals("")) {//A
                            datosFormulario.setDato154(datosFormulario_nutricion.getDato55());
                        }
                    }
                    if (datosFormulario.getDato155().equals("")) {
                        if (!datosFormulario_nutricion.getDato56().equals("")) {//C
                            datosFormulario.setDato155(datosFormulario_nutricion.getDato56());
                        }
                    }
                    if (datosFormulario.getDato156().equals("")) {
                        if (!datosFormulario_nutricion.getDato57().equals("")) {//PLANIFICACIÓN FAMILIAR
                            datosFormulario.setDato156(datosFormulario_nutricion.getDato57());
                        }
                    }
                    if (datosFormulario.getDato157().equals("")) {
                        if (!datosFormulario_nutricion.getDato58().equals("")) {//MÉTODO
                            datosFormulario.setDato157(datosFormulario_nutricion.getDato58());
                        }
                    }
                    if (datosFormulario.getDato158().equals("")) {
                        if (!datosFormulario_nutricion.getDato59().equals("")) {//TIEMPO
                            datosFormulario.setDato158(datosFormulario_nutricion.getDato59());
                        }
                    }
                    if (datosFormulario.getDato159().equals("")) {
                        if (!datosFormulario_nutricion.getDato60().equals("")) {//CITOLOGIA FECHA
                            datosFormulario.setDato159(datosFormulario_nutricion.getDato60());
                        }
                    }
                    //--FAMILIARES
                    if (datosFormulario.getDato160().equals("")) {
                        if (!datosFormulario_nutricion.getDato5().equals("")) {//HTA
                            datosFormulario.setDato160(datosFormulario_nutricion.getDato5());
                        }
                    }
                    if (datosFormulario.getDato161().equals("")) {
                        if (!datosFormulario_nutricion.getDato278().equals("")) {//OBSERVACION
                            datosFormulario.setDato161(datosFormulario_nutricion.getDato278());
                        }
                    }
                    if (datosFormulario.getDato162().equals("")) {
                        if (!datosFormulario_nutricion.getDato6().equals("")) {//ENF. ENDOCRINA
                            datosFormulario.setDato162(datosFormulario_nutricion.getDato6());
                        }
                    }
                    if (datosFormulario.getDato163().equals("")) {
                        if (!datosFormulario_nutricion.getDato279().equals("")) {//OBSERVACION
                            datosFormulario.setDato163(datosFormulario_nutricion.getDato279());
                        }
                    }
                    if (datosFormulario.getDato164().equals("")) {
                        if (!datosFormulario_nutricion.getDato7().equals("")) {//CA. COLON
                            datosFormulario.setDato164(datosFormulario_nutricion.getDato7());
                        }
                    }
                    if (datosFormulario.getDato165().equals("")) {
                        if (!datosFormulario_nutricion.getDato280().equals("")) {//OBSERVACION
                            datosFormulario.setDato165(datosFormulario_nutricion.getDato280());
                        }
                    }
                    if (datosFormulario.getDato166().equals("")) {
                        if (!datosFormulario_nutricion.getDato8().equals("")) {//DIABETES
                            datosFormulario.setDato166(datosFormulario_nutricion.getDato8());
                        }
                    }
                    if (datosFormulario.getDato167().equals("")) {
                        if (!datosFormulario_nutricion.getDato281().equals("")) {//OBSERVACION
                            datosFormulario.setDato167(datosFormulario_nutricion.getDato281());
                        }
                    }
                    if (datosFormulario.getDato168().equals("")) {
                        if (!datosFormulario_nutricion.getDato9().equals("")) {//ENF. RENAL
                            datosFormulario.setDato168(datosFormulario_nutricion.getDato9());
                        }
                    }
                    if (datosFormulario.getDato169().equals("")) {
                        if (!datosFormulario_nutricion.getDato282().equals("")) {//OBSERVACION
                            datosFormulario.setDato169(datosFormulario_nutricion.getDato282());
                        }
                    }
                    if (datosFormulario.getDato170().equals("")) {
                        if (!datosFormulario_nutricion.getDato10().equals("")) {//GU y PROSTATA
                            datosFormulario.setDato170(datosFormulario_nutricion.getDato10());
                        }
                    }
                    if (datosFormulario.getDato171().equals("")) {
                        if (!datosFormulario_nutricion.getDato283().equals("")) {//OBSERVACION
                            datosFormulario.setDato171(datosFormulario_nutricion.getDato283());
                        }
                    }
                    if (datosFormulario.getDato172().equals("")) {
                        if (!datosFormulario_nutricion.getDato11().equals("")) {//DISLIPIDEMIA
                            datosFormulario.setDato172(datosFormulario_nutricion.getDato11());
                        }
                    }
                    if (datosFormulario.getDato173().equals("")) {
                        if (!datosFormulario_nutricion.getDato284().equals("")) {//OBSERVACION
                            datosFormulario.setDato173(datosFormulario_nutricion.getDato284());
                        }
                    }
                    if (datosFormulario.getDato174().equals("")) {
                        if (!datosFormulario_nutricion.getDato12().equals("")) {//OBESIDAD
                            datosFormulario.setDato174(datosFormulario_nutricion.getDato12());
                        }
                    }
                    if (datosFormulario.getDato175().equals("")) {
                        if (!datosFormulario_nutricion.getDato285().equals("")) {//OBSERVACION
                            datosFormulario.setDato175(datosFormulario_nutricion.getDato285());
                        }
                    }
                    if (datosFormulario.getDato176().equals("")) {
                        if (!datosFormulario_nutricion.getDato13().equals("")) {//CA. GÁSTRICO
                            datosFormulario.setDato176(datosFormulario_nutricion.getDato13());
                        }
                    }
                    if (datosFormulario.getDato177().equals("")) {
                        if (!datosFormulario_nutricion.getDato286().equals("")) {//OBSERVACION
                            datosFormulario.setDato177(datosFormulario_nutricion.getDato286());
                        }
                    }
                    if (datosFormulario.getDato178().equals("")) {
                        if (!datosFormulario_nutricion.getDato14().equals("")) {//ENF. CORONARIA
                            datosFormulario.setDato178(datosFormulario_nutricion.getDato14());
                        }
                    }
                    if (datosFormulario.getDato179().equals("")) {
                        if (!datosFormulario_nutricion.getDato287().equals("")) {//OBSERVACION
                            datosFormulario.setDato179(datosFormulario_nutricion.getDato287());
                        }
                    }
                    if (datosFormulario.getDato180().equals("")) {
                        if (!datosFormulario_nutricion.getDato15().equals("")) {//ENF. VASCULAR
                            datosFormulario.setDato180(datosFormulario_nutricion.getDato15());
                        }
                    }
                    if (datosFormulario.getDato181().equals("")) {
                        if (!datosFormulario_nutricion.getDato288().equals("")) {//OBSERVACION
                            datosFormulario.setDato181(datosFormulario_nutricion.getDato288());
                        }
                    }
                    if (datosFormulario.getDato182().equals("")) {
                        if (!datosFormulario_nutricion.getDato16().equals("")) {//CA. MAMA
                            datosFormulario.setDato182(datosFormulario_nutricion.getDato16());
                        }
                    }
                    if (datosFormulario.getDato183().equals("")) {
                        if (!datosFormulario_nutricion.getDato289().equals("")) {//OBSERVACION
                            datosFormulario.setDato183(datosFormulario_nutricion.getDato289());
                        }
                    }
                    if (datosFormulario.getDato184().equals("")) {
                        if (!datosFormulario_nutricion.getDato18().equals("")) {//GO y CERVIX
                            datosFormulario.setDato184(datosFormulario_nutricion.getDato18());
                        }
                    }
                    if (datosFormulario.getDato185().equals("")) {
                        if (!datosFormulario_nutricion.getDato290().equals("")) {//OBSERVACION
                            datosFormulario.setDato185(datosFormulario_nutricion.getDato290());
                        }
                    }
                    //FIN ANTECEDENTES
                    //DIAGNOSTICOS
                    if (datosFormulario.getDato191().equals("")) {
                        if (!datosFormulario_nutricion.getDato166().equals("")) {//PRINCIPAL
                            datosFormulario.setDato191(datosFormulario_nutricion.getDato166());
                        }
                    }
                    if (datosFormulario.getDato192().equals("")) {
                        if (!datosFormulario_nutricion.getDato167().equals("")) {//2
                            datosFormulario.setDato192(datosFormulario_nutricion.getDato167());
                        }
                    }
                    if (datosFormulario.getDato193().equals("")) {
                        if (!datosFormulario_nutricion.getDato168().equals("")) {//3
                            datosFormulario.setDato193(datosFormulario_nutricion.getDato168());
                        }
                    }
                    if (datosFormulario.getDato194().equals("")) {
                        if (!datosFormulario_nutricion.getDato169().equals("")) {//4
                            datosFormulario.setDato194(datosFormulario_nutricion.getDato169());
                        }
                    }
                    if (datosFormulario.getDato195().equals("")) {
                        if (!datosFormulario_nutricion.getDato268().equals("")) {//5
                            datosFormulario.setDato195(datosFormulario_nutricion.getDato268());
                        }
                    }
                    //REVISION POR SISTEMAS
                    if (datosFormulario.getDato205().equals("")) {
                        if (!datosFormulario_nutricion.getDato89().equals("")) {//ORL
                            datosFormulario.setDato205(datosFormulario_nutricion.getDato89());
                        }
                    }
                    if (datosFormulario.getDato206().equals("")) {
                        if (!datosFormulario_nutricion.getDato310().equals("")) {//OBSERVACION
                            datosFormulario.setDato206(datosFormulario_nutricion.getDato310());
                        }
                    }
                    if (datosFormulario.getDato207().equals("")) {
                        if (!datosFormulario_nutricion.getDato90().equals("")) {//SISTEMA RESPIRATORIO
                            datosFormulario.setDato207(datosFormulario_nutricion.getDato90());
                        }
                    }
                    if (datosFormulario.getDato208().equals("")) {
                        if (!datosFormulario_nutricion.getDato311().equals("")) {//OBSERVACION
                            datosFormulario.setDato208(datosFormulario_nutricion.getDato311());
                        }
                    }
                    if (datosFormulario.getDato209().equals("")) {
                        if (!datosFormulario_nutricion.getDato91().equals("")) {//SISTEMA CARDIOVASCULAR
                            datosFormulario.setDato209(datosFormulario_nutricion.getDato91());
                        }
                    }
                    if (datosFormulario.getDato210().equals("")) {
                        if (!datosFormulario_nutricion.getDato312().equals("")) {//OBSERVACION
                            datosFormulario.setDato210(datosFormulario_nutricion.getDato312());
                        }
                    }
                    if (datosFormulario.getDato211().equals("")) {
                        if (!datosFormulario_nutricion.getDato92().equals("")) {//SISTEMA GASTROINTESTINAL
                            datosFormulario.setDato211(datosFormulario_nutricion.getDato92());
                        }
                    }
                    if (datosFormulario.getDato212().equals("")) {
                        if (!datosFormulario_nutricion.getDato313().equals("")) {//OBSERVACION
                            datosFormulario.setDato212(datosFormulario_nutricion.getDato313());
                        }
                    }
                    if (datosFormulario.getDato213().equals("")) {
                        if (!datosFormulario_nutricion.getDato93().equals("")) {//SISTEMA GENITOURINARIO
                            datosFormulario.setDato213(datosFormulario_nutricion.getDato93());
                        }
                    }
                    if (datosFormulario.getDato214().equals("")) {
                        if (!datosFormulario_nutricion.getDato314().equals("")) {//OBSERVACION
                            datosFormulario.setDato214(datosFormulario_nutricion.getDato314());
                        }
                    }
                    if (datosFormulario.getDato215().equals("")) {
                        if (!datosFormulario_nutricion.getDato94().equals("")) {//SISTEMA OSTEOMUSCULAR
                            datosFormulario.setDato215(datosFormulario_nutricion.getDato94());
                        }
                    }
                    if (datosFormulario.getDato216().equals("")) {
                        if (!datosFormulario_nutricion.getDato315().equals("")) {//OBSERVACION
                            datosFormulario.setDato216(datosFormulario_nutricion.getDato315());
                        }
                    }
                    if (datosFormulario.getDato217().equals("")) {
                        if (!datosFormulario_nutricion.getDato95().equals("")) {//SISTEMA NERVIOSO CENTRAL
                            datosFormulario.setDato217(datosFormulario_nutricion.getDato95());
                        }
                    }
                    if (datosFormulario.getDato218().equals("")) {
                        if (!datosFormulario_nutricion.getDato316().equals("")) {//OBSERVACION
                            datosFormulario.setDato218(datosFormulario_nutricion.getDato316());
                        }
                    }
                    if (datosFormulario.getDato219().equals("")) {
                        if (!datosFormulario_nutricion.getDato96().equals("")) {//SISTEMA ENDOCRINOLOGICO
                            datosFormulario.setDato219(datosFormulario_nutricion.getDato96());
                        }
                    }
                    if (datosFormulario.getDato220().equals("")) {
                        if (!datosFormulario_nutricion.getDato317().equals("")) {//OBSERVACION
                            datosFormulario.setDato220(datosFormulario_nutricion.getDato317());
                        }
                    }
                    if (datosFormulario.getDato221().equals("")) {
                        if (!datosFormulario_nutricion.getDato97().equals("")) {//SISTEMA ENDOCRINOLOGICO
                            datosFormulario.setDato221(datosFormulario_nutricion.getDato97());
                        }
                    }
                    if (datosFormulario.getDato222().equals("")) {
                        if (!datosFormulario_nutricion.getDato318().equals("")) {//OBSERVACION
                            datosFormulario.setDato222(datosFormulario_nutricion.getDato318());
                        }
                    }
                    //EXAMEN FISICO
                    if (datosFormulario.getDato223().equals("")) {
                        if (!datosFormulario_nutricion.getDato261().equals("")) {//ESTADO GENERAL
                            datosFormulario.setDato223(datosFormulario_nutricion.getDato261());
                        }
                    }
                    if (datosFormulario.getDato224().equals("")) {
                        if (!datosFormulario_nutricion.getDato262().equals("")) {//OBSERVACION
                            datosFormulario.setDato224(datosFormulario_nutricion.getDato262());
                        }
                    }
                    if (datosFormulario.getDato225().equals("")) {
                        if (!datosFormulario_nutricion.getDato263().equals("")) {//CABEZA
                            datosFormulario.setDato225(datosFormulario_nutricion.getDato263());
                        }
                    }
                    if (datosFormulario.getDato226().equals("")) {
                        if (!datosFormulario_nutricion.getDato141().equals("")) {//OBSERVACION
                            datosFormulario.setDato226(datosFormulario_nutricion.getDato141());
                        }
                    }
                    if (datosFormulario.getDato227().equals("")) {
                        if (!datosFormulario_nutricion.getDato264().equals("")) {//OJOS
                            datosFormulario.setDato227(datosFormulario_nutricion.getDato264());
                        }
                    }
                    if (datosFormulario.getDato228().equals("")) {
                        if (!datosFormulario_nutricion.getDato265().equals("")) {//OBSERVACION
                            datosFormulario.setDato228(datosFormulario_nutricion.getDato265());
                        }
                    }
                    if (datosFormulario.getDato229().equals("")) {
                        if (!datosFormulario_nutricion.getDato266().equals("")) {//BOCA
                            datosFormulario.setDato229(datosFormulario_nutricion.getDato266());
                        }
                    }
                    if (datosFormulario.getDato230().equals("")) {
                        if (!datosFormulario_nutricion.getDato267().equals("")) {//OBSERVACION
                            datosFormulario.setDato230(datosFormulario_nutricion.getDato267());
                        }
                    }
                    if (datosFormulario.getDato231().equals("")) {
                        if (!datosFormulario_nutricion.getDato146().equals("")) {//CUELLO
                            datosFormulario.setDato231(datosFormulario_nutricion.getDato146());
                        }
                    }
                    if (datosFormulario.getDato232().equals("")) {
                        if (!datosFormulario_nutricion.getDato147().equals("")) {//OBSERVACION
                            datosFormulario.setDato232(datosFormulario_nutricion.getDato147());
                        }
                    }
                    if (datosFormulario.getDato233().equals("")) {
                        if (!datosFormulario_nutricion.getDato148().equals("")) {//TORAX
                            datosFormulario.setDato233(datosFormulario_nutricion.getDato148());
                        }
                    }
                    if (datosFormulario.getDato234().equals("")) {
                        if (!datosFormulario_nutricion.getDato149().equals("")) {//OBSERVACION
                            datosFormulario.setDato234(datosFormulario_nutricion.getDato149());
                        }
                    }
                    if (datosFormulario.getDato235().equals("")) {
                        if (!datosFormulario_nutricion.getDato150().equals("")) {//CORAZON
                            datosFormulario.setDato235(datosFormulario_nutricion.getDato150());
                        }
                    }
                    if (datosFormulario.getDato236().equals("")) {
                        if (!datosFormulario_nutricion.getDato151().equals("")) {//OBSERVACION
                            datosFormulario.setDato236(datosFormulario_nutricion.getDato151());
                        }
                    }
                    if (datosFormulario.getDato237().equals("")) {
                        if (!datosFormulario_nutricion.getDato152().equals("")) {//PULMONES
                            datosFormulario.setDato237(datosFormulario_nutricion.getDato152());
                        }
                    }
                    if (datosFormulario.getDato238().equals("")) {
                        if (!datosFormulario_nutricion.getDato153().equals("")) {//OBSERVACION
                            datosFormulario.setDato238(datosFormulario_nutricion.getDato153());
                        }
                    }
                    if (datosFormulario.getDato239().equals("")) {
                        if (!datosFormulario_nutricion.getDato154().equals("")) {//ABDOMEN
                            datosFormulario.setDato239(datosFormulario_nutricion.getDato154());
                        }
                    }
                    if (datosFormulario.getDato240().equals("")) {
                        if (!datosFormulario_nutricion.getDato155().equals("")) {//OBSERVACION
                            datosFormulario.setDato240(datosFormulario_nutricion.getDato155());
                        }
                    }
                    if (datosFormulario.getDato241().equals("")) {
                        if (!datosFormulario_nutricion.getDato156().equals("")) {//GENITOURINARIO
                            datosFormulario.setDato241(datosFormulario_nutricion.getDato156());
                        }
                    }
                    if (datosFormulario.getDato242().equals("")) {
                        if (!datosFormulario_nutricion.getDato157().equals("")) {//OBSERVACION
                            datosFormulario.setDato242(datosFormulario_nutricion.getDato157());
                        }
                    }
                    if (datosFormulario.getDato243().equals("")) {
                        if (!datosFormulario_nutricion.getDato158().equals("")) {//EXTREMIDADES
                            datosFormulario.setDato243(datosFormulario_nutricion.getDato158());
                        }
                    }
                    if (datosFormulario.getDato244().equals("")) {
                        if (!datosFormulario_nutricion.getDato159().equals("")) {//OBSERVACION
                            datosFormulario.setDato244(datosFormulario_nutricion.getDato159());
                        }
                    }
                    if (datosFormulario.getDato245().equals("")) {
                        if (!datosFormulario_nutricion.getDato160().equals("")) {//NEUROLOGICO
                            datosFormulario.setDato245(datosFormulario_nutricion.getDato160());
                        }
                    }
                    if (datosFormulario.getDato246().equals("")) {
                        if (!datosFormulario_nutricion.getDato161().equals("")) {//OBSERVACION
                            datosFormulario.setDato246(datosFormulario_nutricion.getDato161());
                        }
                    }
                    if (datosFormulario.getDato247().equals("")) {
                        if (!datosFormulario_nutricion.getDato162().equals("")) {//MUSCULOESQUELETICO
                            datosFormulario.setDato247(datosFormulario_nutricion.getDato162());
                        }
                    }
                    if (datosFormulario.getDato248().equals("")) {
                        if (!datosFormulario_nutricion.getDato163().equals("")) {//OBSERVACION
                            datosFormulario.setDato248(datosFormulario_nutricion.getDato163());
                        }
                    }
                    if (datosFormulario.getDato249().equals("")) {
                        if (!datosFormulario_nutricion.getDato164().equals("")) {//PIEL
                            datosFormulario.setDato249(datosFormulario_nutricion.getDato164());
                        }
                    }
                    if (datosFormulario.getDato250().equals("")) {
                        if (!datosFormulario_nutricion.getDato165().equals("")) {//OBSERVACION
                            datosFormulario.setDato250(datosFormulario_nutricion.getDato165());
                        }
                    }
                }
            }
            clasificacion_fisica();
            calculo_imc_peso();
            calculo_imc_perimetro();
            calculo_imc();
            if (tipoRegistroClinicoActual.getIdTipoReg() == 19) {
                registroEncontrado = registroFacade.buscarUltimo(pacienteSeleccionado.getIdPaciente(), 54);
                if (registroEncontrado != null) {
                    List<HcDetalle> listaDetalles = registroEncontrado.getHcDetalleList();
                    for (HcDetalle detalle : listaDetalles) {
                        HcCamposReg campo = camposRegFacade.find(detalle.getHcDetallePK().getIdCampo());
                        if (campo != null) {
                            if (campo.getTabla() != null && campo.getTabla().length() != 0) {//tiene tabala o tipo de dato
                                switch (campo.getTabla()) {
                                    case "date":
                                        try {
                                            Date f = sdfDateHour.parse(detalle.getValor());
                                            datosFormulario_formmedicamentos.setValor(campo.getPosicion(), f);
                                        } catch (ParseException ex) {
                                            datosFormulario_formmedicamentos.setValor(campo.getPosicion(), "");
                                        }
                                        break;
                                    default://es una categoria
                                        datosFormulario_formmedicamentos.setValor(campo.getPosicion(), detalle.getValor());
                                        break;
                                }
                            } else {//simplemente es texto
                                datosFormulario_formmedicamentos.setValor(campo.getPosicion(), detalle.getValor());
                            }//System.out.println("Se coloco en datosFormulario." + campo.getPosicion() + " el valor de " + detalle.getValor());
                        } else {
                            System.out.println("Error: no se encontro hc_campos_reg.id_campo= " + detalle.getHcDetallePK().getIdCampo());
                        }
                    }
                    //DIAGNOSTICOS
                    if (!datosFormulario_formmedicamentos.getDato166().equals("")) {//PRINCIPAL
                        datosFormulario.setDato0(datosFormulario_formmedicamentos.getDato166());
                    }
                    if (!datosFormulario_formmedicamentos.getDato167().equals("")) {//2
                        datosFormulario.setDato1(datosFormulario_formmedicamentos.getDato167());
                    }
                    if (!datosFormulario_formmedicamentos.getDato168().equals("")) {//3
                        datosFormulario.setDato2(datosFormulario_formmedicamentos.getDato168());
                    }
                }
            }
        }
        //RequestContext.getCurrentInstance().update("IdFormHistorias");

    }

    private void mostrarFormularioRegistroClinico() {//permite visualizar un formulario dependiendo de la seleccion en el combo 'tipo de registro clinico'
        if (tipoRegistroClinico != null && tipoRegistroClinico.length() != 0) {
            tipoRegistroClinicoActual = tipoRegCliFacade.find(Integer.parseInt(tipoRegistroClinico));
            urlPagina = tipoRegistroClinicoActual.getUrlPagina();
            nombreTipoRegistroClinico = tipoRegistroClinicoActual.getNombre();
        } else {
            tipoRegistroClinicoActual = null;
            urlPagina = "";
            nombreTipoRegistroClinico = "";
        }
    }

    public void cambiaMedico() {//cambia el combo 'medico'(actualiza el campo especialidad)
        if (idPrestador != null && idPrestador.length() != 0) {
            especialidadPrestador = usuariosFacade.find(Integer.parseInt(idPrestador)).getEspecialidad().getDescripcion();
        } else {
            especialidadPrestador = "";
        }
    }

    public void cambiaFalla() {
        falla_atencion = false;
        if (datosFormulario.getDato248().equals("SI")) {
            falla_atencion = true;
        } else {
            datosFormulario.setDato249(null);
            datosFormulario.setDato250(null);
        }
//        RequestContext.getCurrentInstance().update("IdFormRegistroClinico");
    }

    public void actualizarRegistro() {//actualizacion de un registro clinico existente
        List<HcDetalle> listaDetalle = new ArrayList<>();
        HcDetalle nuevoDetalle;
        HcCamposReg campoResgistro;

        guardarOdontograma();
        guardarEvolucion();
        guardarOleary1();
        guardarOleary2();
        if (!modificandoRegCli) {
            imprimirMensaje("Error", "No se ha cargado un registro para poder modificarlo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        //nuevoRegistro=
        if (idPrestador != null && idPrestador.length() != 0) {//validacion de campos obligatorios
//            registroEncontrado.setIdMedico(usuariosFacade.find(Integer.parseInt(idPrestador)));
        } else {
//            imprimirMensaje("Error", "Debe seleccionar un médico", FacesMessage.SEVERITY_ERROR);
//            return;
        }
        if (tipoRegistroClinicoActual != null) {
            registroEncontrado.setIdTipoReg(tipoRegistroClinicoActual);
        } else {
            imprimirMensaje("Error", "No se puede determinar el tipo de registro clinico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaReg == null) {
            fechaReg = fechaSis;
        }
//        registroEncontrado.setFechaReg(fechaReg);
//        registroEncontrado.setFechaSis(fechaSis);
        registroEncontrado.setIdPaciente(pacienteSeleccionado);
//        registroFacade.edit(registroEncontrado);

        /**
         * new arcarrero, se encontró un registro con 248 campos, hubo que hacer
         * algunas modificaciones al código, por ende se comenta la línea donde
         * se coloca el valor 200 de valor arbitraria, y se tomará en cuenta el
         * valor real del tamaño del reporte, indicada en la columna
         * "cant_campos" de la tabla "hc_tipo_reg", donde debe estar el valor
         * real del tamaño del reporte, de esta manera no se hacen ciclos extra
         * y además serviría para reportes de todos los tamaños.
         */
//        for (int i = 0; i < 200; i++) { //maximo 200 campos,  por ahora el maximo tiene 177 ...
//        for (int i = 0; i < tipoRegistroClinicoActual.getCantCampos(); i++) {
        int i = 338;
        if (datosFormulario.getValor(i) != null && datosFormulario.getValor(i).toString().length() != 0) {
            campoResgistro = camposRegFacade.buscarPorTipoRegistroYPosicion(tipoRegistroClinicoActual.getIdTipoReg(), i);
            if (campoResgistro != null) {
//                    if ((datosFormulario.getValor(i).toString().length() != 0 && campoResgistro.getTabla().contains("date")) || !campoResgistro.getTabla().contains("date")) {
                nuevoDetalle = new HcDetalle(registroEncontrado.getIdRegistro(), campoResgistro.getIdCampo());
                if (campoResgistro.getTabla() == null || campoResgistro.getTabla().length() == 0) {
                    nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                } else {
                    switch (campoResgistro.getTabla()) {
                        case "html":
                            nuevoDetalle.setValor(corregirHtml(datosFormulario.getValor(i).toString()));
                            break;
                        case "date":
                            try {
                                Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                nuevoDetalle.setValor(sdfDateHour.format(f));
                            } catch (ParseException ex) {
                                nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                            }
                            break;
                        //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                        case "date2":
                            try {
                                Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                nuevoDetalle.setValor(sdfDate.format(f));
                            } catch (ParseException ex) {
                                nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                            }
                            break;
                        default://casos: cfg_clasificaciones,boolean, double, u otros
                            nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                            break;
                    }
                }
                listaDetalle.add(nuevoDetalle);
                detalleFacade.edit(nuevoDetalle);
//                }
            } else {
                System.out.println("No encontro en tabla hc_campos_registro el valor: id_tipo_reg = " + tipoRegistroClinicoActual.getIdTipoReg());
            }
//            }
        }

//        registroEncontrado.setHcDetalleList(listaDetalle);
//        registroFacade.edit(registroEncontrado);
        imprimirMensaje("Correcto", "Registro actualizado.", FacesMessage.SEVERITY_INFO);
        limpiarFormulario();
        valoresPorDefecto();
        RequestContext.getCurrentInstance().update("IdFormRegistroClinico");
    }

    /**
     * Método para guardar el detalle del diente que se está gestionando
     *
     * @param _variableInicial variable inicial del diente (se manejan 8
     * variables por cada uno, 1 para un estatus general y 7 más para el detalle
     * por cada zona
     */
    public void guardarDetalleDiente(int _variableInicial) {

        datosFormulario.setValor(_variableInicial, loginMB.getRutaCarpetaImagenes() + "Reportes/dienteLimpiar.png");//Se pone "dienteLimpiar" ya que el estado general no se combina con los detallados.	
        datosFormulario.setValor(_variableInicial + 1, detDiente1);
        datosFormulario.setValor(_variableInicial + 2, detDiente2);
        datosFormulario.setValor(_variableInicial + 3, detDiente3);
        datosFormulario.setValor(_variableInicial + 4, detDiente4);
        datosFormulario.setValor(_variableInicial + 5, detDiente5);
        datosFormulario.setValor(_variableInicial + 6, detDiente6);
        datosFormulario.setValor(_variableInicial + 7, detDiente7);

        RequestContext.getCurrentInstance().execute("PF('dlgDetalleDiente').hide();");
    }

    /**
     * Método para cargar el detalle del diente que se está gestionando y abrir
     * la ventana emergente
     *
     * @param _variableInicial variable inicial del diente (se manejan 8
     * variables por cada uno, 1 para un estatus general y 7 más para el detalle
     * por cada zona
     * @param _diente Número de diente que se está gestionando
     */
    public void cargarVentanaDiente(int _variableInicial, String _diente) {

        //Se hace inicio+1 ya que no se toma en cuenta el estado general del diente para cargar el detalle.
        detDiente1 = (String) datosFormulario.getValor(_variableInicial + 1);
        detDiente2 = (String) datosFormulario.getValor(_variableInicial + 2);
        detDiente3 = (String) datosFormulario.getValor(_variableInicial + 3);
        detDiente4 = (String) datosFormulario.getValor(_variableInicial + 4);
        detDiente5 = (String) datosFormulario.getValor(_variableInicial + 5);
        detDiente6 = (String) datosFormulario.getValor(_variableInicial + 6);
        detDiente7 = (String) datosFormulario.getValor(_variableInicial + 7);

        numeroDiente = _diente;
        variableInicial = _variableInicial;
        RequestContext.getCurrentInstance().execute("PF('dlgDetalleDiente').show();");
    }

    /**
     * Método para limpiar el detalle de un diente, motivado a que el médico
     * decidió aplicar un estatus GENERAL al mismo..
     *
     * @param _variableInicial variable inicial del diente (se manejan 8
     * variables por cada uno, 1 para un estatus general y 7 más para el detalle
     * por cada zona
     */
    public void limpiarDetalleDiente(int _variableInicial) {

        //Se hace inicio+1 ya que no se desea limpiar el estado general del diente, solo sus detalles.
        datosFormulario.setValor(_variableInicial + 1, "");
        datosFormulario.setValor(_variableInicial + 2, "");
        datosFormulario.setValor(_variableInicial + 3, "");
        datosFormulario.setValor(_variableInicial + 4, "");
        datosFormulario.setValor(_variableInicial + 5, "");
        datosFormulario.setValor(_variableInicial + 6, "");
        datosFormulario.setValor(_variableInicial + 7, "");

    }

    /**
     * Limpia el detalle del diente al médico seleccionar un estatus general
     * oleary
     *
     * @param _variableInicial variable inicial del diente (se manejan 5
     * variables por cada uno, 1 para un estatus general y 4 más para el detalle
     * por cada zona teñida
     */
    public void limpiarDetalleDienteOleary(int _variableInicial) {
        /**
         * Se quitan los posibles detalles que hayan sido agregados ya que se
         * seleccinó un estado general, se hace _variableInicial+1 ya que solo
         * se considera el detalle del diente y _variableInicial es el estado
         * general.
         */
        datosFormulario.setValor(_variableInicial + 1, "");
        datosFormulario.setValor(_variableInicial + 2, "");
        datosFormulario.setValor(_variableInicial + 3, "");
        datosFormulario.setValor(_variableInicial + 4, "");
    }

    /**
     * Carga la ventana (los datos) del detalle del diente en o' Leary
     *
     * @param _variableInicial variable inicial del diente (se manejan 5
     * variables por cada uno, 1 para un estatus general y 4 más para el detalle
     * por cada zona teñida
     * @param _diente Número de diente que se está gestionando
     */
    public void cargarVentanaOleary(int _variableInicial, String _diente) {
        variableInicial = _variableInicial;
        numeroDiente = _diente;

        //Se cargan los datos del detalle del diente.
        detOleary1 = (String) datosFormulario.getValor(_variableInicial + 1);
        detOleary2 = (String) datosFormulario.getValor(_variableInicial + 2);
        detOleary3 = (String) datosFormulario.getValor(_variableInicial + 3);
        detOleary4 = (String) datosFormulario.getValor(_variableInicial + 4);

        RequestContext.getCurrentInstance().execute("PF('dlgDetalleDienteOleary').show();");
    }

    /**
     * Guarda el detalle del diente según zona o' Leary
     *
     * @param _variableInicial variable inicial del diente (se manejan 5
     * variables por cada uno, 1 para un estatus general y 4 más para el detalle
     * por cada zona teñida
     */
    public void guardarDetalleDienteOleary(int _variableInicial) {
        datosFormulario.setValor(_variableInicial, loginMB.getRutaCarpetaImagenes() + "Reportes/oLeary.png");//Se pone "oLeary" ya que el estado general no se combina con los detallados.
        datosFormulario.setValor(_variableInicial + 1, detOleary1);
        datosFormulario.setValor(_variableInicial + 2, detOleary2);
        datosFormulario.setValor(_variableInicial + 3, detOleary3);
        datosFormulario.setValor(_variableInicial + 4, detOleary4);

        RequestContext.getCurrentInstance().execute("PF('dlgDetalleDienteOleary').hide();");
    }

    public void guardarRegistro() {//guardar un nuevo registro clinico        

        System.out.println("Iniciando el guardado del registro");

        HcRegistro nuevoRegistro = new HcRegistro();
        List<HcDetalle> listaDetalle = new ArrayList<>();
        HcDetalle nuevoDetalle;
        HcCamposReg campoResgistro;
        if (idPrestador != null && idPrestador.length() != 0) {//validacion de campos obligatorios
            nuevoRegistro.setIdMedico(usuariosFacade.find(Integer.parseInt(idPrestador)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un médico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (tipoRegistroClinicoActual != null) {
            nuevoRegistro.setIdTipoReg(tipoRegistroClinicoActual);
        } else {
            imprimirMensaje("Error", "No se puede determinar el tipo de registro clinico", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (fechaReg == null) {
            fechaReg = fechaSis;
        }
        if (tipoRegistroClinicoActual.getIdTipoReg() == 54) {
            if (fc == 0 || fr == 0 || t == 0) {
                imprimirMensaje("Error", "Faltan diligenciar signos vitales", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (pas_de == 0 || pad_de == 0) {
                imprimirMensaje("Error", "Faltan diligenciar la hipertensión", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (peso == 0 || talla == 0) {
                imprimirMensaje("Error", "Faltan diligenciar el peso y la talla", FacesMessage.SEVERITY_ERROR);
                return;
            }
            if (perimetro == 0) {
                imprimirMensaje("Error", "Faltan diligenciar el perímetro abdominal", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        //Nutricion 77
        if (tipoRegistroClinicoActual.getIdTipoReg() == 77) {
            if (datosFormulario.getDato191() == null || datosFormulario.getDato191().equals("")) {
                imprimirMensaje("Error", "Debe Ingresar Diagnostico Principal", FacesMessage.SEVERITY_ERROR);
                return;
            }

        }
        nuevoRegistro.setFechaReg(fechaReg);
        nuevoRegistro.setFechaSis(fechaSis);
        nuevoRegistro.setIdPaciente(pacienteSeleccionado);

        if (validarNoVacio(turnoCita)) {
            List<CitCitas> listaCitas = turnosFacade.find(Integer.parseInt(turnoCita)).getCitCitasList();
            CitCitas citaAtendida = null;
            for (CitCitas cita : listaCitas) {
                if (cita.getCancelada() == false) {
                    nuevoRegistro.setIdCita(cita);
                    citaAtendida = cita;
                    break;
                }
            }
            if (citaAtendida != null) {
                citaAtendida.setAtendida(true);
                citasFacade.edit(citaAtendida);
            }
        }
        registroFacade.create(nuevoRegistro);

        /**
         * new arcarrero, se encontró un registro con 248 campos, hubo que hacer
         * algunas modificaciones al código, por ende se comenta la línea donde
         * se coloca el valor 200 de valor arbitraria, y se tomará en cuenta el
         * valor real del tamaño del reporte, indicada en la columna
         * "cant_campos" de la tabla "hc_tipo_reg", donde debe estar el valor
         * real del tamaño del reporte, de esta manera no se hacen ciclos extra
         * y además serviría para reportes de todos los tamaños.
         */
        //        for (int i = 0; i < 200; i++) { //maximo 200 campos,  por ahora el maximo tiene 177 ...
        System.out.println(tipoRegistroClinicoActual.getCantCampos());
        for (int i = 0; i < tipoRegistroClinicoActual.getCantCampos(); i++) {
            if (datosFormulario.getValor(i) != null && datosFormulario.getValor(i).toString().length() != 0) {
                campoResgistro = camposRegFacade.buscarPorTipoRegistroYPosicion(tipoRegistroClinicoActual.getIdTipoReg(), i);
                if (campoResgistro != null) {
                    nuevoDetalle = new HcDetalle(nuevoRegistro.getIdRegistro(), campoResgistro.getIdCampo());
                    if (campoResgistro.getTabla() == null || campoResgistro.getTabla().length() == 0) {
                        nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                    } else {
                        switch (campoResgistro.getTabla()) {
                            case "html":
                                nuevoDetalle.setValor(corregirHtml(datosFormulario.getValor(i).toString()));
                                break;
                            case "date":
                                try {
                                    Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                    nuevoDetalle.setValor(sdfDateHour.format(f));
                                } catch (ParseException ex) {
                                    nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                                }
                                break;
                            //new arcarrero, para permitir guardar fechas de solo días (sin horas).
                            case "date2":
                                try {
                                    Date f = sdfFechaString.parse(datosFormulario.getValor(i).toString());
                                    nuevoDetalle.setValor(sdfDate.format(f));
                                } catch (ParseException ex) {
                                    nuevoDetalle.setValor("Error: " + datosFormulario.getValor(i).toString());
                                }
                                break;
                            default://casos: cfg_clasificaciones,boolean, double, u otros
                                nuevoDetalle.setValor(datosFormulario.getValor(i).toString());
                                break;
                        }
                    }
                    listaDetalle.add(nuevoDetalle);
                } else {
                    System.out.println("No encontro en tabla hc_campos_registro el valor: id_tipo_reg=" + tipoRegistroClinicoActual.getIdTipoReg() + " posicion " + i);
                }
            }
        }

        if (lista_reportes.size() > 0) {
            for (FilaDataTable rep : lista_reportes) {
                System.out.println(new Date(rep.getColumna3()) + " ");
                HcRepExamenes r = new HcRepExamenes();
                r.setIdRegistro(nuevoRegistro);
                r.setFecha(new Date(rep.getColumna3()));
                r.setNombreParaclinico(Integer.parseInt(rep.getColumna2()));
                r.setPosicion(Integer.parseInt(rep.getColumna1()));
                r.setResultado(rep.getColumna4());
                hcRepFacade.create(r);
            }
        }

        //medicamentos asociados 
        if (listaMedicamentos != null) {
            for (FilaDataTable item : listaMedicamentos) {
                HcItems nuevoMedicamento = new HcItems();
                nuevoMedicamento.setIdRegistro(nuevoRegistro);
                nuevoMedicamento.setIdTabla(item.getColumna1());
                nuevoMedicamento.setTabla("cfg_medicamento");
//                nuevoMedicamento.setDescripcion(item.getDescripcion());
//                nuevoMedicamento.setConcentracion(item.getConcentracion());
                nuevoMedicamento.setObservacion(item.getColumna10());
                nuevoMedicamento.setCantidad(Integer.parseInt(item.getColumna4()));
                nuevoMedicamento.setDosis(item.getColumna5());
                nuevoMedicamento.setPosologia(item.getColumna9());
//                nuevoMedicamento.setViaAdmin(item.getViaAdmin());
                itemsFacade.create(nuevoMedicamento);
                System.out.println("GUARDADO ITEM");
                System.out.println(item.getColumna2());
                ///creamos la lista de insumos para formulacion de medicaentos
                FacConsumoMedicamento facMedicamento = new FacConsumoMedicamento();
                CfgMedicamento medicamento = cfgMedicamento.medicamentoXCodigo(item.getColumna2());
                if (medicamento != null) {
                    facMedicamento.setCantidad(Integer.parseInt(item.getColumna4()));
                    facMedicamento.setIdPaciente(pacienteSeleccionado);
                    facMedicamento.setIdMedicamento(medicamento);
                    if (idPrestador != null && idPrestador.length() != 0) {//validacion de campos obligatorios
                        facMedicamento.setIdPrestador(usuariosFacade.find(Integer.parseInt(idPrestador)));
                    }

                    facMedicamento.setFecha(new Date());
                    facMedicamento.setValorUnitario(medicamento.getValor());
                    facMedicamento.setValorFinal(medicamento.getValor() * facMedicamento.getCantidad());
                    facConsumoMedicamentoFacade.create(facMedicamento);
                }
            }
        }

        if (listaEstructuraFamiliar != null) {
            //guardar estructura familiar si es que existe
            for (FilaDataTable familiar : listaEstructuraFamiliar) {
                CfgFamiliar fam = new CfgFamiliar();
                fam.setIdPaciente(pacienteSeleccionado);
                fam.setNombre(familiar.getColumna1());
                fam.setEdad(Integer.parseInt(familiar.getColumna2()));
                CfgClasificaciones parentesco1 = new CfgClasificaciones(Integer.parseInt(familiar.getColumna3()));
                CfgClasificaciones ocupacion1 = new CfgClasificaciones(Integer.parseInt(familiar.getColumna4()));

                fam.setIdParentesco(parentesco1);
                fam.setIdOcupacion(ocupacion1);

                familiarFacade.create(fam);
            }
        }

        if (tipoRegistroClinicoActual.getIdTipoReg() == 8) {//SOLICITUD DE AUTORIZACION DE SERVICIOS            
            tipoRegistroClinicoActual.setConsecutivo(tipoRegistroClinicoActual.getConsecutivo() + 1);
            tipoRegCliFacade.edit(tipoRegistroClinicoActual);//incrementar consecutivo a tipo de registro clinico actual           
            for (int i = 0; i < listaDetalle.size(); i++) {//se quita el valor por se autocalculara
                if (listaDetalle.get(i).getHcDetallePK().getIdCampo() == 182) {
                    listaDetalle.remove(i);
                    break;
                }
            }
            nuevoDetalle = new HcDetalle(nuevoRegistro.getIdRegistro(), 182);//numero de solicitud
            nuevoDetalle.setValor(String.valueOf(tipoRegistroClinicoActual.getConsecutivo()));
            listaDetalle.add(nuevoDetalle);
        }

        nuevoRegistro.setHcDetalleList(listaDetalle);
        nuevoRegistro.setFolio(registroFacade.buscarMaximoFolio(nuevoRegistro.getIdPaciente().getIdPaciente()) + 1);
        registroFacade.edit(nuevoRegistro);

        //servicios o examenes medicos asociados
        if (listaServicios != null) {
            for (FilaDataTable item : listaServicios) {
                HcItems nuevoServicio = new HcItems();
                nuevoServicio.setIdRegistro(nuevoRegistro);
                nuevoServicio.setIdTabla(item.getColumna1());
                nuevoServicio.setTabla("fac_servicio");
//                nuevoMedicamento.setDescripcion(item.getDescripcion());
//                nuevoMedicamento.setConcentracion(item.getConcentracion());
                nuevoServicio.setObservacion(item.getColumna5());
                nuevoServicio.setCantidad(Integer.parseInt(item.getColumna4()));
//                nuevoServicio.setDosis(item.getColumna5());
//                nuevoServicio.setPosologia(item.getColumna9());
//                nuevoMedicamento.setViaAdmin(item.getViaAdmin());

                itemsFacade.create(nuevoServicio);

            }
        }
        imprimirMensaje("Correcto", "Nuevo registro almacenado.", FacesMessage.SEVERITY_INFO);
        limpiarFormulario();
        valoresPorDefecto();
        turnoCita = "";
        tipoRegistroClinico = "";
        //Cargamos grafica
        if(tipoRegistroClinicoActual.getIdTipoReg()==70 || tipoRegistroClinicoActual.getIdTipoReg()==69){
            loadGraphicValoracion0_18();
        }
        RequestContext.getCurrentInstance().update("IdFormRegistroClinico");
        RequestContext.getCurrentInstance().update("IdFormHistorias");
    }

    public List<String> autocompletarDiagnostico(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return diagnosticoFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    public List<String> autocompletarDiagnosticoPrincipal(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        System.out.println(txt);
        if (txt != null && txt.length() > 2) {
            return diagnosticoPrincipalFacade.autocompletarDiagnostico(txt);
        } else {
            return null;
        }
    }

    //---------------------------------------------------
    //------ FUNCIONES CARGAR/BUSCAR PACIENTES ----------
    //---------------------------------------------------
    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el paciente de una tabla
        pacienteTmp = pacientesFacade.buscarPorIdentificacion(identificacionPaciente);

        if (pacienteTmp != null) {
            tipoRegistroClinico = "";
            urlPagina = "";
            nombreTipoRegistroClinico = "";
            pacienteSeleccionadoTabla = pacienteTmp;
            hayPacienteSeleccionado = true;//se pueden mostrar opciones
            cargarPaciente();

        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').show();");
        }
    }

    public void editarPaciente() {//se invoca funcion javaScript(cargarPaciente -> home.xhtml) que carga la pestaña de pacientes y los datos del paciende seleccionado
        if (pacienteSeleccionado != null) {
            RequestContext.getCurrentInstance().execute("window.parent.cargarPaciente('Pacientes','configuraciones/pacientes.xhtml','" + pacienteSeleccionado.getIdPaciente() + "')");
        } else {
            hayPacienteSeleccionado = false;
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void calculo_imc_perimetro() {
        v = 0;
        clasificacion_perimetro = "Sin Dato...";
        if (tipoRegistroClinicoActual != null) {
            if (tipoRegistroClinicoActual.getIdTipoReg() == 54) {
                datosFormulario.setDato137(perimetro);
            }
            if (tipoRegistroClinicoActual.getIdTipoReg() == 77) {
                datosFormulario.setDato23(perimetro);
            }
            if (!pacienteSeleccionado.getGenero().getDescripcion().equals("MASCULINO")) {
                if (perimetro < 80.0) {
                    clasificacion_perimetro = "NORMAL";
                }
                if (perimetro >= 80.0) {
                    clasificacion_perimetro = "ELEVADO";
                }
            } else {
                if (perimetro < 90.0) {
                    clasificacion_perimetro = "NORMAL";
                }
                if (perimetro >= 90.0) {
                    clasificacion_perimetro = "ELEVADO";
                }
            }
        }
    }

    public void calculo_imc_peso() {
        if (tipoRegistroClinicoActual != null) {
            v = 0;
            peso_to = 0.0;
            clasificacion_peso = "Sin Dato...";
            if (talla > 0.0 && peso > 0.0) {
                peso_to = peso / (talla * talla);
                if (peso_to < 18) {
                    clasificacion_peso = "BAJO PESO";
                }
                if (peso_to >= 18 && peso_to <= 24.9) {
                    clasificacion_peso = "NORMAL";
                }
                if (peso_to >= 25.0 && peso_to <= 29.9) {
                    clasificacion_peso = "SOBREPESO";
                }
                if (peso_to >= 30.0 && peso_to <= 34.9) {
                    clasificacion_peso = "OBESIDAD I";
                }
                if (peso_to >= 35.0 && peso_to <= 39.9) {
                    clasificacion_peso = "OBESIDAD II";
                }
                if (peso_to >= 40.0) {
                    clasificacion_peso = "OBESIDAD II";
                }
            }
            if (tipoRegistroClinicoActual.getIdTipoReg() == 54) {
                datosFormulario.setDato131(peso);
                datosFormulario.setDato132(talla);
            }
            if (tipoRegistroClinicoActual.getIdTipoReg() == 77) {
                datosFormulario.setDato21(peso);
                datosFormulario.setDato22(talla);
            }
        }

    }

    public void clasificacion_fisica() {
        datosFormulario.setDato15(horas);
        datosFormulario.setDato258(minutos);
        if (horas > 0) {
            clasificacion_f = "-";
        } else if (minutos >= 30) {
            clasificacion_f = "-";
        } else {
            clasificacion_f = "SEDENTARISMO";
        }
    }

    public void signos_vitales_update() {
        if (fc != null) {
            datosFormulario.setDato125(fc);
        }
        if (fr != null) {
            datosFormulario.setDato126(fr);
        }
        if (t > 0.0) {
            datosFormulario.setDato127(t);
        }
    }

    public void calculo_imc() {
        if (tipoRegistroClinicoActual != null) {
            v = 0;
            pas = 0;
            pad = 0;
            if (pas_d != null && pad_d != null && pas_d > 0 && pad_d > 0) {
                v++;
                datosFormulario.setDato128(pas_d);
                datosFormulario.setDato129(pad_d);
                pas += pas_d;
                pad += pad_d;
                System.out.println(v + " " + pas_d + " " + pad_d + " " + pas + " " + pad);
            }
            if (pas_i != null && pad_i != null && pas_i > 0 && pad_i > 0) {
                v++;
                datosFormulario.setDato248(pas_i);
                datosFormulario.setDato249(pad_i);
                pas += pas_i;
                pad += pad_i;
                System.out.println(v + " " + pas_i + " " + pad_i + " " + pas + " " + pad);
            }
            if (pas_c != null && pad_c != null && pas_c > 0 && pad_c > 0) {
                v++;
                datosFormulario.setDato251(pas_c);
                datosFormulario.setDato252(pad_c);
                pas += pas_c;
                pad += pad_c;
                System.out.println(v + " " + pas_c + " " + pad_c + " " + pas + " " + pad);
            }
            if (pas_ce != null && pad_ce != null && pad_ce > 0 && pas_ce > 0) {
                v = 4;
                pas = 0;
                pad = 0;
                datosFormulario.setDato254(pas_ce);
                datosFormulario.setDato255(pad_ce);
                pas += pas_ce;
                pad += pad_ce;
                System.out.println(v + " " + pas_ce + " " + pad_ce + " " + pas + " " + pad);
            }
            if (v >= 2 && v <= 4) {
                if (v == 4) {
                    v = 1;
                }
                pas = pas / v;
                pad = pad / v;
                if (pas < 90 && pad <= 60) {
                    clasificacion_pa = "ANORMALMENTE BAJA O HIPOTENSIÓN";
                }
                if ((pas >= 90 && pas < 120) && (pad >= 60 && pad < 80)) {
                    clasificacion_pa = "OPTIMA";
                }
                if ((pas >= 120 && pas <= 129) && (pad >= 80 && pad <= 84)) {
                    clasificacion_pa = "NORMAL";
                }
                if ((pas >= 130 && pas <= 139) && (pad >= 85 && pad <= 89)) {
                    clasificacion_pa = "NORMAL ALTA";
                }
                if ((pas >= 140 && pas <= 159) && (pad >= 90 && pad <= 99)) {
                    clasificacion_pa = "HIPERTENSIÓN GRADO I";
                }
                if ((pas >= 160 && pas <= 179) && (pad >= 100 && pad <= 109)) {
                    clasificacion_pa = "HIPERTENSIÓN GRADO II";
                }
                if (pas >= 180 && pad >= 110) {
                    clasificacion_pa = "HIPERTENSIÓN GRADO II";
                }
                if (pas >= 140 && pad < 90) {
                    clasificacion_pa = "HIPERTENSIÓN SISTOLICA AISLADA";
                }
                System.out.println(v + " " + clasificacion_pa + " " + pas + " " + pad);
            } else {
                pas_de = 0;
                pad_de = 0;
                clasificacion_pa = "Sin Dato...";
            }
            pas_de = pas;
            pad_de = pad;
        }
    }

    public void cargarPaciente() {//cargar un paciente desde del dialogo de buscar paciente o al digitar una identificacion valida(esta en pacientes)        
        if (pacienteSeleccionadoTabla != null) {

            turnoCita = "";
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionadoTabla.getIdPaciente());
            listaArchivo = hcArchivosFacade.getHcArchivosByPaciente(pacienteSeleccionado);
            urlPagina = "";
            identificacionPaciente = "";
            nombrePaciente = "Paciente";
            nombreTipoRegistroClinico = "";
            hayPacienteSeleccionado = true;
            identificacionPaciente = pacienteSeleccionado.getIdentificacion();
            if (pacienteSeleccionado.getTipoIdentificacion() != null) {
                tipoIdentificacion = pacienteSeleccionado.getTipoIdentificacion().getDescripcion();
            } else {
                tipoIdentificacion = "";
            }
            nombrePaciente = pacienteSeleccionado.nombreCompleto();
            System.out.println("Paciente " + pacienteSeleccionado.getIdPaciente() + " ...  " + nombrePaciente);
            if (pacienteSeleccionado.getGenero() != null) {
                generoPaciente = pacienteSeleccionado.getGenero().getObservacion();
            } else {
                generoPaciente = "";
            }
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                edadPaciente = calcularEdad(pacienteSeleccionado.getFechaNacimiento());
            } else {
                edadPaciente = "";
            }
            if (pacienteSeleccionado.getIdAdministradora() != null) {
                administradoraPaciente = pacienteSeleccionado.getIdAdministradora().getRazonSocial();
            } else {
                administradoraPaciente = "";
            }

            //
            if (pacienteSeleccionado.getOcupacion() != null) {
                ocupacion = pacienteSeleccionado.getOcupacion().getDescripcion();
            } else {
                ocupacion = "";
            }
            if (pacienteSeleccionado.getEscolaridad() != null) {
                escolaridad = pacienteSeleccionado.getEscolaridad().getDescripcion();
            } else {
                escolaridad = "";
            }
            if (pacienteSeleccionado.getReligion() != null) {
                religion = pacienteSeleccionado.getReligion().getDescripcion();
            } else {
                religion = "";
            }
            if (pacienteSeleccionado.getDiscapacidad() != null) {
                discapacidad = pacienteSeleccionado.getDiscapacidad().getDescripcion();
            } else {
                discapacidad = "";
            }
            if (pacienteSeleccionado.getGestacion() != null) {
                gestacion = pacienteSeleccionado.getGestacion().getDescripcion();
            } else {
                gestacion = "";
            }
            if (pacienteSeleccionado.getDesplazado() != null) {
                desplazado = pacienteSeleccionado.getDesplazado();
                desplazadoStr = (desplazado) ? "SI" : "NO";
            } else {
                desplazado = false;
                desplazadoStr = "";
            }
            if (pacienteSeleccionado.getPoblacionLBGT() != null) {
                poblacionLBGT = pacienteSeleccionado.getPoblacionLBGT();
                poblacionLBGTStr = (poblacionLBGT) ? "SI" : "NO";
            } else {
                poblacionLBGT = false;
                poblacionLBGTStr = "";
            }
            if (pacienteSeleccionado.getVictimaMaltrato() != null) {
                victimaMaltrato = pacienteSeleccionado.getVictimaMaltrato();
                victimaMaltratoStr = (victimaMaltrato) ? "SI" : "NO";
            } else {
                victimaMaltrato = false;
                victimaMaltratoStr = "";
            }
            if (pacienteSeleccionado.getVictimaConflicto() != null) {
                victimaConflicto = pacienteSeleccionado.getVictimaConflicto();
                victimaConflictoStr = (victimaConflicto) ? "SI" : "NO";
            } else {
                victimaConflicto = false;
                victimaConflictoStr = "";
            }

            //
            tipoRegistroClinico = "";
            cambiaTipoRegistroClinico();
            if (!cargandoDesdeTab) {
                RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
            }
        } else {
            hayPacienteSeleccionado = false;
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void cancerCervixSenoDefaultParams(String _val) {
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);

        datosFormulario.setDato32(_val);
        datosFormulario.setDato33(_val);
        datosFormulario.setDato34(_val);
        datosFormulario.setDato35(_val);
        datosFormulario.setDato36(_val);
        datosFormulario.setDato37(_val);
        datosFormulario.setDato38(_val);

        datosFormulario.setDato40(_val);

    }

    public void clapOpsOmsDefaultParams(String _val) {
        datosFormulario.setDato0(_val);
        datosFormulario.setDato2(_val);
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato29(_val);
        datosFormulario.setDato53(_val);
        datosFormulario.setDato65(_val);
        datosFormulario.setDato73(_val);
        datosFormulario.setDato74(_val);
        datosFormulario.setDato83(_val);
        datosFormulario.setDato97(_val);
        datosFormulario.setDato110(_val);
        datosFormulario.setDato119(_val);
        datosFormulario.setDato124(_val);
        datosFormulario.setDato147(_val);
        datosFormulario.setDato152(_val);

    }

    public void epicrisisDefaultParams(String _val) {
        datosFormulario.setDato2(_val);
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
    }

    public void evolucionHistoriaGlandulaMamariaDefaultParams(String _val) {
        datosFormulario.setDato0(_val);
        datosFormulario.setDato2(_val);
        datosFormulario.setDato3(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);

    }

    public void historiaClinicaDefaultParams(String _val) {
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
        datosFormulario.setDato17(_val);
        datosFormulario.setDato18(_val);
        datosFormulario.setDato19(_val);
        datosFormulario.setDato20(_val);
        datosFormulario.setDato21(_val);
        datosFormulario.setDato22(_val);
        datosFormulario.setDato23(_val);
        datosFormulario.setDato24(_val);
        datosFormulario.setDato25(_val);
        datosFormulario.setDato26(_val);
        datosFormulario.setDato27(_val);
        datosFormulario.setDato28(_val);
        datosFormulario.setDato29(_val);
        datosFormulario.setDato30(_val);
        datosFormulario.setDato31(_val);
        datosFormulario.setDato32(_val);
        datosFormulario.setDato33(_val);
        datosFormulario.setDato34(_val);
        datosFormulario.setDato35(_val);
        datosFormulario.setDato36(_val);
        datosFormulario.setDato37(_val);
        datosFormulario.setDato38(_val);
        datosFormulario.setDato39(_val);
        datosFormulario.setDato40(_val);
        datosFormulario.setDato41(_val);
        datosFormulario.setDato42(_val);
        datosFormulario.setDato43(_val);
        datosFormulario.setDato44(_val);
        datosFormulario.setDato45(_val);
        datosFormulario.setDato46(_val);
        datosFormulario.setDato47(_val);
        datosFormulario.setDato48(_val);
        datosFormulario.setDato49(_val);
        datosFormulario.setDato50(_val);
        datosFormulario.setDato51(_val);
        datosFormulario.setDato52(_val);
        datosFormulario.setDato53(_val);
        datosFormulario.setDato54(_val);
        datosFormulario.setDato55(_val);
        datosFormulario.setDato56(_val);
        datosFormulario.setDato57(_val);
        datosFormulario.setDato58(_val);
        datosFormulario.setDato59(_val);
        datosFormulario.setDato60(_val);
        datosFormulario.setDato61(_val);
        datosFormulario.setDato62(_val);
        datosFormulario.setDato63(_val);
        datosFormulario.setDato64(_val);
        datosFormulario.setDato65(_val);
        datosFormulario.setDato66(_val);
        datosFormulario.setDato67(_val);
        datosFormulario.setDato68(_val);
        datosFormulario.setDato69(_val);
        datosFormulario.setDato70(_val);
        datosFormulario.setDato71(_val);
        datosFormulario.setDato72(_val);
        datosFormulario.setDato73(_val);
        datosFormulario.setDato74(_val);
        datosFormulario.setDato75(_val);
        datosFormulario.setDato76(_val);
        datosFormulario.setDato77(_val);
        datosFormulario.setDato78(_val);
        datosFormulario.setDato79(_val);
        datosFormulario.setDato80(_val);
        datosFormulario.setDato81(_val);
        datosFormulario.setDato82(_val);
        datosFormulario.setDato83(_val);
        datosFormulario.setDato84(_val);
        datosFormulario.setDato85(_val);
        datosFormulario.setDato86(_val);
        datosFormulario.setDato87(_val);
        datosFormulario.setDato88(_val);
        datosFormulario.setDato89(_val);
        datosFormulario.setDato90(_val);
        datosFormulario.setDato91(_val);
        datosFormulario.setDato92(_val);
        datosFormulario.setDato93(_val);
        datosFormulario.setDato94(_val);
        datosFormulario.setDato95(_val);
        datosFormulario.setDato96(_val);
        datosFormulario.setDato97(_val);
        datosFormulario.setDato98(_val);
        datosFormulario.setDato99(_val);
        datosFormulario.setDato100(_val);
        datosFormulario.setDato101(_val);
        datosFormulario.setDato102(_val);
        datosFormulario.setDato103(_val);
        datosFormulario.setDato104(_val);
        datosFormulario.setDato105(_val);
        datosFormulario.setDato106(_val);
        datosFormulario.setDato107(_val);
        datosFormulario.setDato108(_val);
        datosFormulario.setDato109(_val);
        datosFormulario.setDato110(_val);
        //
        datosFormulario.setDato115(_val);
        datosFormulario.setDato116(_val);
        datosFormulario.setDato117(_val);

    }

    public void suministroPlanificacionFamiliarDefaultParams(String _val) {
        datosFormulario.setDato0(_val);
        datosFormulario.setDato2(_val);
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
    }

    public void resultadoCitologiaDefaultParams(String _val) {
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
    }

    public void citologiaDefaultParams(String _val) {
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
        datosFormulario.setDato17(_val);
        datosFormulario.setDato18(_val);
        datosFormulario.setDato19(_val);
        datosFormulario.setDato20(_val);
        datosFormulario.setDato21(_val);
        datosFormulario.setDato22(_val);
        datosFormulario.setDato23(_val);
        datosFormulario.setDato24(_val);
        datosFormulario.setDato25(_val);
        datosFormulario.setDato26(_val);
        datosFormulario.setDato27(_val);
        datosFormulario.setDato28(_val);
        datosFormulario.setDato29(_val);
        datosFormulario.setDato30(_val);
        datosFormulario.setDato31(_val);
        datosFormulario.setDato32(_val);
        datosFormulario.setDato33(_val);
        datosFormulario.setDato34(_val);
        datosFormulario.setDato35(_val);
        datosFormulario.setDato36(_val);
        datosFormulario.setDato37(_val);
        datosFormulario.setDato38(_val);
        datosFormulario.setDato39(_val);
        datosFormulario.setDato40(_val);
        datosFormulario.setDato41(_val);
        datosFormulario.setDato42(_val);
        datosFormulario.setDato43(_val);
        datosFormulario.setDato44(_val);
        datosFormulario.setDato45(_val);
        datosFormulario.setDato46(_val);
        datosFormulario.setDato47(_val);
        datosFormulario.setDato48(_val);
        datosFormulario.setDato49(_val);
        datosFormulario.setDato50(_val);
        datosFormulario.setDato51(_val);
        datosFormulario.setDato52(_val);
        datosFormulario.setDato53(_val);
        datosFormulario.setDato54(_val);
        datosFormulario.setDato55(_val);
        datosFormulario.setDato56(_val);
        datosFormulario.setDato57(_val);
        datosFormulario.setDato58(_val);
        datosFormulario.setDato59(_val);
        datosFormulario.setDato60(_val);
        datosFormulario.setDato61(_val);
        datosFormulario.setDato62(_val);
        datosFormulario.setDato63(_val);
        datosFormulario.setDato64(_val);
        datosFormulario.setDato65(_val);
        datosFormulario.setDato66(_val);
        datosFormulario.setDato67(_val);
        datosFormulario.setDato68(_val);
        datosFormulario.setDato69(_val);
        datosFormulario.setDato70(_val);
        datosFormulario.setDato71(_val);
        datosFormulario.setDato72(_val);
        datosFormulario.setDato73(_val);
        datosFormulario.setDato74(_val);
        datosFormulario.setDato75(_val);
        datosFormulario.setDato76(_val);
        datosFormulario.setDato77(_val);
        datosFormulario.setDato78(_val);
        datosFormulario.setDato79(_val);
        datosFormulario.setDato80(_val);
        datosFormulario.setDato81(_val);
        datosFormulario.setDato82(_val);
        datosFormulario.setDato83(_val);
        datosFormulario.setDato84(_val);
        datosFormulario.setDato85(_val);
        datosFormulario.setDato86(_val);
        datosFormulario.setDato87(_val);
        datosFormulario.setDato88(_val);
        datosFormulario.setDato89(_val);
        datosFormulario.setDato90(_val);
        datosFormulario.setDato91(_val);
        datosFormulario.setDato92(_val);
        datosFormulario.setDato93(_val);
        datosFormulario.setDato94(_val);
        datosFormulario.setDato95(_val);
        datosFormulario.setDato96(_val);
        datosFormulario.setDato97(_val);
        datosFormulario.setDato98(_val);
        datosFormulario.setDato99(_val);
        datosFormulario.setDato100(_val);
        datosFormulario.setDato101(_val);
        datosFormulario.setDato102(_val);
        datosFormulario.setDato103(_val);
        datosFormulario.setDato104(_val);
        datosFormulario.setDato105(_val);
        datosFormulario.setDato106(_val);
        datosFormulario.setDato107(_val);
        datosFormulario.setDato108(_val);
        datosFormulario.setDato109(_val);
        datosFormulario.setDato110(_val);
        datosFormulario.setDato111(_val);
        datosFormulario.setDato112(_val);
        datosFormulario.setDato113(_val);
        datosFormulario.setDato114(_val);
        datosFormulario.setDato115(_val);
        datosFormulario.setDato116(_val);
        datosFormulario.setDato117(_val);
        datosFormulario.setDato118(_val);
        datosFormulario.setDato119(_val);
        datosFormulario.setDato120(_val);
        datosFormulario.setDato121(_val);
        datosFormulario.setDato122(_val);
        datosFormulario.setDato123(_val);
        datosFormulario.setDato124(_val);
        datosFormulario.setDato125(_val);
        datosFormulario.setDato126(_val);
        datosFormulario.setDato127(_val);
        datosFormulario.setDato128(_val);
        datosFormulario.setDato129(_val);
        datosFormulario.setDato130(_val);
        datosFormulario.setDato131(_val);
        datosFormulario.setDato132(_val);
        datosFormulario.setDato133(_val);
        datosFormulario.setDato134(_val);
        datosFormulario.setDato135(_val);
        datosFormulario.setDato136(_val);
        datosFormulario.setDato137(_val);
        datosFormulario.setDato138(_val);
        datosFormulario.setDato139(_val);
        datosFormulario.setDato140(_val);
        datosFormulario.setDato141(_val);
        datosFormulario.setDato142(_val);
        datosFormulario.setDato143(_val);
        datosFormulario.setDato144(_val);
        datosFormulario.setDato145(_val);
        datosFormulario.setDato146(_val);
        datosFormulario.setDato147(_val);
        datosFormulario.setDato148(_val);
        datosFormulario.setDato149(_val);
        datosFormulario.setDato150(_val);
        datosFormulario.setDato151(_val);
        datosFormulario.setDato152(_val);
        datosFormulario.setDato153(_val);
        datosFormulario.setDato154(_val);
        datosFormulario.setDato155(_val);
        datosFormulario.setDato156(_val);
        datosFormulario.setDato157(_val);
        datosFormulario.setDato158(_val);
        datosFormulario.setDato159(_val);
        datosFormulario.setDato160(_val);
        datosFormulario.setDato161(_val);
        datosFormulario.setDato162(_val);
        datosFormulario.setDato163(_val);
        datosFormulario.setDato165(_val);
    }

    public void urgenciasDefaultParams(String _val) {
        //Motivo
        datosFormulario.setDato3(_val);
        datosFormulario.setDato4(_val);
        //Disagnosticos
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
        datosFormulario.setDato17(_val);
        datosFormulario.setDato18(_val);
        datosFormulario.setDato19(_val);
        datosFormulario.setDato20(_val);
        datosFormulario.setDato21(_val);
        datosFormulario.setDato22(_val);
        datosFormulario.setDato23(_val);
        datosFormulario.setDato24(_val);
        datosFormulario.setDato25(_val);
        datosFormulario.setDato26(_val);
        datosFormulario.setDato27(_val);
        datosFormulario.setDato28(_val);
        datosFormulario.setDato29(_val);
        datosFormulario.setDato30(_val);
        datosFormulario.setDato31(_val);
        datosFormulario.setDato32(_val);
        datosFormulario.setDato33(_val);
        datosFormulario.setDato34(_val);
        datosFormulario.setDato35(_val);
        datosFormulario.setDato36(_val);
        //Revision por sistema
        datosFormulario.setDato37(_val);
        datosFormulario.setDato38(_val);
        datosFormulario.setDato39(_val);
        datosFormulario.setDato40(_val);
        datosFormulario.setDato41(_val);
        datosFormulario.setDato42(_val);
        datosFormulario.setDato43(_val);
        datosFormulario.setDato44(_val);
        datosFormulario.setDato45(_val);
        datosFormulario.setDato46(_val);
        datosFormulario.setDato47(_val);
        datosFormulario.setDato48(_val);
        datosFormulario.setDato49(_val);
        datosFormulario.setDato50(_val);
        datosFormulario.setDato51(_val);
        datosFormulario.setDato52(_val);
        datosFormulario.setDato53(_val);
        datosFormulario.setDato54(_val);
        datosFormulario.setDato55(_val);
        datosFormulario.setDato56(_val);
        datosFormulario.setDato57(_val);
        //Signos Vitales
        datosFormulario.setDato58(_val);
        datosFormulario.setDato59(_val);
        datosFormulario.setDato60(_val);
        datosFormulario.setDato61(_val);
        datosFormulario.setDato62(_val);
        datosFormulario.setDato63(_val);
        datosFormulario.setDato64(_val);
        //Examen Fisico
        datosFormulario.setDato65(_val);
        datosFormulario.setDato66(_val);
        datosFormulario.setDato67(_val);
        datosFormulario.setDato68(_val);
        datosFormulario.setDato69(_val);
        datosFormulario.setDato70(_val);
        datosFormulario.setDato71(_val);
        datosFormulario.setDato72(_val);
        datosFormulario.setDato73(_val);
        datosFormulario.setDato74(_val);
        datosFormulario.setDato75(_val);
        datosFormulario.setDato76(_val);
        datosFormulario.setDato77(_val);
        datosFormulario.setDato78(_val);
        datosFormulario.setDato79(_val);
        datosFormulario.setDato80(_val);
        datosFormulario.setDato81(_val);
        datosFormulario.setDato82(_val);
        datosFormulario.setDato83(_val);
        datosFormulario.setDato84(_val);
        datosFormulario.setDato85(_val);
        //Factores
        datosFormulario.setDato86(_val);
        datosFormulario.setDato87(_val);
        datosFormulario.setDato88(_val);
        datosFormulario.setDato89(_val);
        datosFormulario.setDato90(_val);
        datosFormulario.setDato91(_val);
        datosFormulario.setDato92(_val);
        datosFormulario.setDato93(_val);
        datosFormulario.setDato94(_val);
        datosFormulario.setDato95(_val);
        //Examen Mental
        datosFormulario.setDato96(_val);
        datosFormulario.setDato97(_val);
        datosFormulario.setDato98(_val);
        datosFormulario.setDato99(_val);
        datosFormulario.setDato100(_val);
        datosFormulario.setDato101(_val);
        datosFormulario.setDato102(_val);
        datosFormulario.setDato103(_val);
        datosFormulario.setDato104(_val);
        datosFormulario.setDato105(_val);
        datosFormulario.setDato106(_val);
        datosFormulario.setDato107(_val);
        datosFormulario.setDato108(_val);
        datosFormulario.setDato109(_val);
        datosFormulario.setDato110(_val);
        datosFormulario.setDato111(_val);
        datosFormulario.setDato112(_val);
        datosFormulario.setDato113(_val);
        //
        datosFormulario.setDato118(_val);
        datosFormulario.setDato119(_val);
        datosFormulario.setDato120(_val);

    }

    public void refContrRefDefaultParams(String _val) {
        datosFormulario.setDato5(_val);
        datosFormulario.setDato6(_val);
        datosFormulario.setDato7(_val);
        datosFormulario.setDato8(_val);
        datosFormulario.setDato9(_val);
        datosFormulario.setDato10(_val);
        datosFormulario.setDato11(_val);
        datosFormulario.setDato12(_val);
        datosFormulario.setDato13(_val);
        datosFormulario.setDato14(_val);
        datosFormulario.setDato15(_val);
        datosFormulario.setDato16(_val);
        datosFormulario.setDato17(_val);

        datosFormulario.setDato20(_val);
        datosFormulario.setDato21(_val);
        datosFormulario.setDato22(_val);
        datosFormulario.setDato23(_val);
        datosFormulario.setDato24(_val);
        datosFormulario.setDato25(_val);
        datosFormulario.setDato26(_val);
        datosFormulario.setDato27(_val);
        datosFormulario.setDato28(_val);
        datosFormulario.setDato29(_val);
        datosFormulario.setDato30(_val);
        datosFormulario.setDato31(_val);
        datosFormulario.setDato32(_val);
        datosFormulario.setDato33(_val);
        datosFormulario.setDato34(_val);
        datosFormulario.setDato35(_val);
        datosFormulario.setDato36(_val);
        datosFormulario.setDato37(_val);
        datosFormulario.setDato38(_val);
        datosFormulario.setDato39(_val);
        datosFormulario.setDato40(_val);

        datosFormulario.setDato41(_val);
        datosFormulario.setDato42(_val);
        datosFormulario.setDato43(_val);
        datosFormulario.setDato44(_val);
        datosFormulario.setDato45(_val);
        datosFormulario.setDato46(_val);
        datosFormulario.setDato47(_val);
        datosFormulario.setDato48(_val);
        datosFormulario.setDato49(_val);
        datosFormulario.setDato50(_val);
        datosFormulario.setDato51(_val);

    }

    public void loadServiciosOrden() {
        listaServiciosOrdenMedica = servicioFacade.buscarActivos();
    }

    //---------------------------------------------------
    //-------- FUNCIONES TEXTOS PREDEFINIDOS ------------
    //---------------------------------------------------
    public void seleccionEditor(String ed, String posVec) {//determinar cual 'p:editor' y la posicion en 'estructuraCampos' donde se usara el texto predefinido
        idEditorSeleccionado = ed;
        posListaTxtPredef = Integer.parseInt(posVec);//determinar la posicion en el vector 'estructuraCampos' al usar un texto predefinido

    }

    public void usarTextoPredefinido() {//asignar el texto predefinido a un 'p:editor'
        if (detalleTextoPredef != null && detalleTextoPredef.length() != 0) {
            datosFormulario.setValor(posListaTxtPredef, detalleTextoPredef);
            RequestContext.getCurrentInstance().update("IdFormRegistroClinico:" + idEditorSeleccionado);//se actualiza el editor
            RequestContext.getCurrentInstance().execute("PF('dlgTextosPredefinidos').hide()");//se cierra el dialogo            
        } else {
            imprimirMensaje("Error", "El texto predefinido está vacio", FacesMessage.SEVERITY_ERROR);
        }
    }

    private void recargarMaestrosTxtPredefinidos() {//carga la lista de textos predefinidos (se usa en cualquier p:editor)
        listaMaestrosTxtPredefinidos = maestrosTxtPredefinidosFacade.findAll();
        idMaestroTextoPredef = "";
        nombreTextoPredefinido = "";
        cambiaMaestro();
    }

    public void cambiaMaestro() {//cambia la clasificacion cuando se esta en el dialogo de textos predefinidos
        idTextoPredef = "";
        if (idMaestroTextoPredef != null && idMaestroTextoPredef.length() != 0) {
            listaTxtPredefinidos = maestrosTxtPredefinidosFacade.find(Integer.parseInt(idMaestroTextoPredef)).getCfgTxtPredefinidosList();
            if (listaTxtPredefinidos != null && !listaTxtPredefinidos.isEmpty()) {
                idTextoPredef = listaTxtPredefinidos.get(0).getIdTxtPredefinido().toString();
            }
        } else {
            listaTxtPredefinidos = null;
        }
        cambiaTextoPredefinido();
    }

    public void cambiaTextoPredefinido() {//usado en dialogo 'textosPredefinidos' cuando cambia el combo de 'textos predefinidos', permite cargar el texto predefinido en el p:ditor 
        if (idTextoPredef != null && idTextoPredef.length() != 0) {
            txtPredefinidoActual = txtPredefinidosFacade.find(Integer.parseInt(idTextoPredef));
            detalleTextoPredef = txtPredefinidoActual.getDetalle();
            nombreTextoPredefinido = txtPredefinidoActual.getNombre();
        } else {
            txtPredefinidoActual = null;
            detalleTextoPredef = "";
            nombreTextoPredefinido = "";
        }
    }

    public void guardarTextoPredefinido() {//almacenar en la base de datos el texto predefinido
        if (txtPredefinidoActual != null) {//se cargo uno
            if (txtPredefinidoActual.getNombre().compareTo(nombreTextoPredefinido) == 0) {//son los mismos se actualiza
                txtPredefinidoActual.setDetalle(detalleTextoPredef);
                txtPredefinidosFacade.edit(txtPredefinidoActual);
                imprimirMensaje("Correcto", "El texto predefinido se ha actualizado", FacesMessage.SEVERITY_INFO);
            } else if (nombreTextoPredefinido.trim().length() == 0) {
                imprimirMensaje("Error", "Debe escribir un nombre para el texto predefinido", FacesMessage.SEVERITY_ERROR);
            } else//es nuevo se debe crear
            {
                if (txtPredefinidosFacade.buscarPorNombre(nombreTextoPredefinido) != null) {
                    imprimirMensaje("Error", "Ya existe un texto predefinido con este nombre", FacesMessage.SEVERITY_ERROR);
                } else {
                    CfgTxtPredefinidos nuevoPredefinido = new CfgTxtPredefinidos();
                    nuevoPredefinido.setNombre(nombreTextoPredefinido);
                    nuevoPredefinido.setIdMaestro(txtPredefinidoActual.getIdMaestro());
                    nuevoPredefinido.setDetalle(detalleTextoPredef);
                    txtPredefinidosFacade.create(nuevoPredefinido);
                    recargarMaestrosTxtPredefinidos();
                    RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
                    imprimirMensaje("Correcto", "El nuevo texto predefinido ha sido creado", FacesMessage.SEVERITY_INFO);
                }
            }
        } else//se debe agregar a la categoria
        {
            if (idMaestroTextoPredef != null && idMaestroTextoPredef.length() != 0) {
                if (nombreTextoPredefinido.trim().length() == 0) {
                    imprimirMensaje("Error", "Debe escribir un nombre para el texto predefinido", FacesMessage.SEVERITY_ERROR);
                } else//es nuevo se debe crear
                {
                    if (txtPredefinidosFacade.buscarPorNombre(nombreTextoPredefinido) != null) {
                        imprimirMensaje("Error", "Ya existe un texto predefinido con este nombre", FacesMessage.SEVERITY_ERROR);
                    } else {
                        CfgTxtPredefinidos nuevoPredefinido = new CfgTxtPredefinidos();
                        nuevoPredefinido.setNombre(nombreTextoPredefinido);
                        nuevoPredefinido.setIdMaestro(maestrosTxtPredefinidosFacade.find(Integer.parseInt(idMaestroTextoPredef)));
                        nuevoPredefinido.setDetalle(detalleTextoPredef);
                        txtPredefinidosFacade.create(nuevoPredefinido);
                        recargarMaestrosTxtPredefinidos();
                        RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
                        imprimirMensaje("Correcto", "El nuevo texto predefinido ha sido creado", FacesMessage.SEVERITY_INFO);
                    }
                }
            } else {
                imprimirMensaje("Error", "No se ha seleccionado ninguna categoría", FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void eliminarPredefinido() {//eliminar un texto predefinido de una categoria seleccionada
        if (idTextoPredef != null && idTextoPredef.length() != 0) {
            txtPredefinidosFacade.remove(txtPredefinidosFacade.find(Integer.parseInt(idTextoPredef)));
            recargarMaestrosTxtPredefinidos();
            RequestContext.getCurrentInstance().update("IdFormRegistroClinico:IdPanelTextosPredefinidos");//se actualiza el editor
            imprimirMensaje("Correcto", "El texto predefinido se ha eliminado", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "No se ha seleccionado ningún texto predefinido", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET SET ----------------
    //---------------------------------------------------
    //public LazyDataModel<CfgPacientes> getListaPacientes() {
    //  return listaPacientes;
    // }
//
    //  public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
    //    this.listaPacientes = listaPacientes;
    //}
    public List<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(List<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public List<CfgPacientes> getListaPacientesFiltro() {
        return listaPacientesFiltro;
    }

    public void setListaPacientesFiltro(List<CfgPacientes> listaPacientesFiltro) {
        this.listaPacientesFiltro = listaPacientesFiltro;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }

    public String getUrlPagina() {
        return urlPagina;
    }

    public void setUrlPagina(String urlPagina) {
        this.urlPagina = urlPagina;
    }

    public String getTipoRegistroClinico() {
        return tipoRegistroClinico;
    }

    public void setTipoRegistroClinico(String tipoRegistroClinico) {
        this.tipoRegistroClinico = tipoRegistroClinico;
    }

    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }

    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    public String getGeneroPaciente() {
        return generoPaciente;
    }

    public void setGeneroPaciente(String generoPaciente) {
        this.generoPaciente = generoPaciente;
    }

    public String getEdadPaciente() {
        return edadPaciente;
    }

    public void setEdadPaciente(String edadPaciente) {
        this.edadPaciente = edadPaciente;
    }

    public String getAdministradoraPaciente() {
        return administradoraPaciente;
    }

    public String geturlFile() {
        return urlFile;
    }

    public void seturlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public void setAdministradoraPaciente(String administradoraPaciente) {
        this.administradoraPaciente = administradoraPaciente;
    }

    public String getNombreTipoRegistroClinico() {
        return nombreTipoRegistroClinico;
    }

    public void setNombreTipoRegistroClinico(String nombreTipoRegistroClinico) {
        this.nombreTipoRegistroClinico = nombreTipoRegistroClinico;
    }

    public List<CfgMaestrosTxtPredefinidos> getListaMaestrosTxtPredefinidos() {
        return listaMaestrosTxtPredefinidos;
    }

    public void setListaMaestrosTxtPredefinidos(List<CfgMaestrosTxtPredefinidos> listaMaestrosTxtPredefinidos) {
        this.listaMaestrosTxtPredefinidos = listaMaestrosTxtPredefinidos;
    }

    public List<CfgTxtPredefinidos> getListaTxtPredefinidos() {
        return listaTxtPredefinidos;
    }

    public void setListaTxtPredefinidos(List<CfgTxtPredefinidos> listaTxtPredefinidos) {
        this.listaTxtPredefinidos = listaTxtPredefinidos;
    }

    public String getDetalleTextoPredef() {
        return detalleTextoPredef;
    }

    public void setDetalleTextoPredef(String detalleTextoPredef) {
        this.detalleTextoPredef = detalleTextoPredef;
    }

    public String getIdTextoPredef() {
        return idTextoPredef;
    }

    public void setIdTextoPredef(String idTextoPredef) {
        this.idTextoPredef = idTextoPredef;
    }

    public String getIdMaestroTextoPredef() {
        return idMaestroTextoPredef;
    }

    public void setIdMaestroTextoPredef(String idMaestroTextoPredef) {
        this.idMaestroTextoPredef = idMaestroTextoPredef;
    }

    public String getNombreTextoPredefinido() {
        return nombreTextoPredefinido;
    }

    public void setNombreTextoPredefinido(String nombreTextoPredefinido) {
        this.nombreTextoPredefinido = nombreTextoPredefinido;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public boolean ValidarDato(Object _dato, String _dato2) {
        return _dato.toString().compareTo(_dato2) == 0;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    public String getIdEditorSeleccionado() {
        return idEditorSeleccionado;
    }

    public void setIdEditorSeleccionado(String idEditorSeleccionado) {
        this.idEditorSeleccionado = idEditorSeleccionado;
    }

    public String getIdPrestador() {
        return idPrestador;
    }

    public void setIdPrestador(String idPrestador) {
        this.idPrestador = idPrestador;
    }

    public String getEspecialidadPrestador() {
        return especialidadPrestador;
    }

    public void setEspecialidadPrestador(String especialidadPrestador) {
        this.especialidadPrestador = especialidadPrestador;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Date getFechaSis() {
        return fechaSis;
    }

    public void setFechaSis(Date fechaSis) {
        this.fechaSis = fechaSis;
    }

    public String[] getRegCliSelHistorial() {
        return regCliSelHistorial;
    }

    public void setRegCliSelHistorial(String[] regCliSelHistorial) {
        this.regCliSelHistorial = regCliSelHistorial;
    }

    public TreeNode getTreeNodeRaiz() {
        return treeNodeRaiz;
    }

    public void setTreeNodeRaiz(TreeNode treeNodeRaiz) {
        this.treeNodeRaiz = treeNodeRaiz;
    }

    public TreeNode[] getTreeNodesSeleccionados() {
        return treeNodesSeleccionados;
    }

    public void setTreeNodesSeleccionados(TreeNode[] treeNodesSeleccionados) {
        this.treeNodesSeleccionados = treeNodesSeleccionados;
    }

    public boolean isModificandoRegCli() {
        return modificandoRegCli;
    }

    public void setModificandoRegCli(boolean modificandoRegCli) {
        this.modificandoRegCli = modificandoRegCli;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public boolean isBtnHistorialDisabled() {
        return btnHistorialDisabled;
    }

    public void setBtnHistorialDisabled(boolean btnHistorialDisabled) {
        this.btnHistorialDisabled = btnHistorialDisabled;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public boolean isBtnPdfAgrupadoDisabled() {
        return btnPdfAgrupadoDisabled;
    }

    public void setBtnPdfAgrupadoDisabled(boolean btnPdfAgrupadoDisabled) {
        this.btnPdfAgrupadoDisabled = btnPdfAgrupadoDisabled;
    }

    public String getFiltroAutorizacion() {
        return filtroAutorizacion;
    }

    public void setFiltroAutorizacion(String filtroAutorizacion) {
        this.filtroAutorizacion = filtroAutorizacion;
    }

    public Date getFiltroFechaInicial() {
        return filtroFechaInicial;
    }

    public void setFiltroFechaInicial(Date filtroFechaInicial) {
        this.filtroFechaInicial = filtroFechaInicial;
    }

    public Date getFiltroFechaFinal() {
        return filtroFechaFinal;
    }

    public void setFiltroFechaFinal(Date filtroFechaFinal) {
        this.filtroFechaFinal = filtroFechaFinal;
    }

    public boolean isBtnEditarRendered() {
        return btnEditarRendered;
    }

    public void setBtnEditarRendered(boolean btnEditarRendered) {
        this.btnEditarRendered = btnEditarRendered;
    }

    public List<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(List<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public String getTurnoCita() {
        return turnoCita;
    }

    public void setTurnoCita(String turnoCita) {
        this.turnoCita = turnoCita;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getGestacion() {
        return gestacion;
    }

    public void setGestacion(String gestacion) {
        this.gestacion = gestacion;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public Boolean getVictimaMaltrato() {
        return victimaMaltrato;
    }

    public void setVictimaMaltrato(Boolean victimaMaltrato) {
        this.victimaMaltrato = victimaMaltrato;
    }

    public Boolean getVictimaConflicto() {
        return victimaConflicto;
    }

    public void setVictimaConflicto(Boolean victimaConflicto) {
        this.victimaConflicto = victimaConflicto;
    }

    public Boolean getDesplazado() {
        return desplazado;
    }

    public void setDesplazado(Boolean desplazado) {
        this.desplazado = desplazado;
    }

    public Boolean getPoblacionLBGT() {
        return poblacionLBGT;
    }

    public void setPoblacionLBGT(Boolean poblacionLBGT) {
        this.poblacionLBGT = poblacionLBGT;
    }

    public String getImc() {
        return imc;
    }

    public void setImc(String imc) {
        this.imc = imc;
    }

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getEtnia() {
        return etnia;
    }

    public void setEtnia(String etnia) {
        this.etnia = etnia;
    }

    public String getVictimaMaltratoStr() {
        return victimaMaltratoStr;
    }

    public void setVictimaMaltratoStr(String victimaMaltratoStr) {
        this.victimaMaltratoStr = victimaMaltratoStr;
    }

    public String getVictimaConflictoStr() {
        return victimaConflictoStr;
    }

    public void setVictimaConflictoStr(String victimaConflictoStr) {
        this.victimaConflictoStr = victimaConflictoStr;
    }

    public String getDesplazadoStr() {
        return desplazadoStr;
    }

    public void setDesplazadoStr(String desplazadoStr) {
        this.desplazadoStr = desplazadoStr;
    }

    public String getPoblacionLBGTStr() {
        return poblacionLBGTStr;
    }

    public void setPoblacionLBGTStr(String poblacionLBGTStr) {
        this.poblacionLBGTStr = poblacionLBGTStr;
    }

    public List<FilaDataTable> getListaEstructuraFamiliar() {
        return listaEstructuraFamiliar;
    }

    public void setListaEstructuraFamiliar(List<FilaDataTable> listaEstructuraFamiliar) {
        this.listaEstructuraFamiliar = listaEstructuraFamiliar;
    }

    public List<FilaDataTable> getListaEstructuraFamiliarFiltro() {
        return listaEstructuraFamiliarFiltro;
    }

    public void setListaEstructuraFamiliarFiltro(List<FilaDataTable> listaEstructuraFamiliarFiltro) {
        this.listaEstructuraFamiliarFiltro = listaEstructuraFamiliarFiltro;
    }

    public FilaDataTable getFamiliarSeleccionado() {
        return familiarSeleccionado;
    }

    public void setFamiliarSeleccionado(FilaDataTable familiarSeleccionado) {
        this.familiarSeleccionado = familiarSeleccionado;
    }

    public String getNombreFamiliar() {
        return nombreFamiliar;
    }

    public void setNombreFamiliar(String nombreFamiliar) {
        this.nombreFamiliar = nombreFamiliar;
    }

    public String getEdadFamiliar() {
        return edadFamiliar;
    }

    public void setEdadFamiliar(String edadFamiliar) {
        this.edadFamiliar = edadFamiliar;
    }

    public String getParentescoFamiliar() {
        return parentescoFamiliar;
    }

    public void setParentescoFamiliar(String parentescoFamiliar) {
        this.parentescoFamiliar = parentescoFamiliar;
    }

    public String getOcupacionFamiliar() {
        return ocupacionFamiliar;
    }

    public void setOcupacionFamiliar(String ocupacionFamiliar) {
        this.ocupacionFamiliar = ocupacionFamiliar;
    }

    public List<FilaDataTable> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<FilaDataTable> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public List<FilaDataTable> getListaMedicamentosFiltro() {
        return listaMedicamentosFiltro;
    }

    public void setListaMedicamentosFiltro(List<FilaDataTable> listaMedicamentosFiltro) {
        this.listaMedicamentosFiltro = listaMedicamentosFiltro;
    }

    public FilaDataTable getMedicamentoSeleccionado() {
        System.out.println(" ... getMedicamentoSeleccionado() ... ");
        return medicamentoSeleccionado;
    }

    public void setMedicamentoSeleccionado(FilaDataTable medicamentoSeleccionado) {
        this.medicamentoSeleccionado = medicamentoSeleccionado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getViaAdmin() {
        return viaAdmin;
    }

    public void setViaAdmin(String viaAdmin) {
        this.viaAdmin = viaAdmin;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public List<FilaDataTable> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<FilaDataTable> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public List<FilaDataTable> getListaServiciosFiltro() {
        return listaServiciosFiltro;
    }

    public void setListaServiciosFiltro(List<FilaDataTable> listaServiciosFiltro) {
        this.listaServiciosFiltro = listaServiciosFiltro;
    }

    public FilaDataTable getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    public void setServicioSeleccionado(FilaDataTable servicioSeleccionado) {
        this.servicioSeleccionado = servicioSeleccionado;
    }

    public String getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(String idServicio) {
        this.idServicio = idServicio;
    }

    public String getDetDiente1() {
        return detDiente1;
    }

    public void setDetDiente1(String detDiente1) {
        this.detDiente1 = detDiente1;
    }

    public String getDetDiente2() {
        return detDiente2;
    }

    public void setDetDiente2(String detDiente2) {
        this.detDiente2 = detDiente2;
    }

    public String getDetDiente3() {
        return detDiente3;
    }

    public void setDetDiente3(String detDiente3) {
        this.detDiente3 = detDiente3;
    }

    public String getDetDiente4() {
        return detDiente4;
    }

    public void setDetDiente4(String detDiente4) {
        this.detDiente4 = detDiente4;
    }

    public String getDetDiente5() {
        return detDiente5;
    }

    public void setDetDiente5(String detDiente5) {
        this.detDiente5 = detDiente5;
    }

    public String getDetDiente6() {
        return detDiente6;
    }

    public void setDetDiente6(String detDiente6) {
        this.detDiente6 = detDiente6;
    }

    public String getDetDiente7() {
        return detDiente7;
    }

    public void setDetDiente7(String detDiente7) {
        this.detDiente7 = detDiente7;
    }

    public String getNumeroDiente() {
        return numeroDiente;
    }

    public void setNumeroDiente(String numeroDiente) {
        this.numeroDiente = numeroDiente;
    }

    public String getDetOleary1() {
        return detOleary1;
    }

    public void setDetOleary1(String detOleary1) {
        this.detOleary1 = detOleary1;
    }

    public String getDetOleary2() {
        return detOleary2;
    }

    public void setDetOleary2(String detOleary2) {
        this.detOleary2 = detOleary2;
    }

    public String getDetOleary3() {
        return detOleary3;
    }

    public void setDetOleary3(String detOleary3) {
        this.detOleary3 = detOleary3;
    }

    public String getDetOleary4() {
        return detOleary4;
    }

    public void setDetOleary4(String detOleary4) {
        this.detOleary4 = detOleary4;
    }

    public int getVariableInicial() {
        return variableInicial;
    }

    public void setVariableInicial(int variableInicial) {
        this.variableInicial = variableInicial;
    }

    public List<HcArchivos> getListaArchivo() {
        return listaArchivo;
    }

    public HcArchivos getArchivoSeleccionado() {
        return archivoSeleccionado;
    }

    public void setArchivoSeleccionado(HcArchivos archivoSeleccionado) {
        this.archivoSeleccionado = archivoSeleccionado;
    }

    public StreamedContent getFileDownload() {
        return fileDownload;
    }

    public String getDescriparchivo() {
        return descriparchivo;
    }

    public void setDescriparchivo(String descriparchivo) {
        this.descriparchivo = descriparchivo;
    }

    public List<FilaDataTable> getLista_reportes() {
        return lista_reportes;
    }

    public void setLista_reportes(List<FilaDataTable> lista_reportes) {
        this.lista_reportes = lista_reportes;
    }

    public FilaDataTable getReporte_seleccionado() {
        return reporte_seleccionado;
    }

    public void setReporte_seleccionado(FilaDataTable reporte_seleccionado) {
        this.reporte_seleccionado = reporte_seleccionado;
    }

    public HcRepExamenes getAgregar_fila_reporte() {
        return agregar_fila_reporte;
    }

    public void setAgregar_fila_reporte(HcRepExamenes agregar_fila_reporte) {
        this.agregar_fila_reporte = agregar_fila_reporte;
    }

    public String getExamen() {
        return examen;
    }

    public void setExamen(String examen) {
        this.examen = examen;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isVer_ginecostetricos() {
        return ver_ginecostetricos;
    }

    public void setVer_ginecostetricos(boolean ver_ginecostetricos) {
        this.ver_ginecostetricos = ver_ginecostetricos;
    }

    public Integer getPas_d() {
        return pas_d;
    }

    public void setPas_d(Integer pas_d) {
        this.pas_d = pas_d;
    }

    public Integer getPad_d() {
        return pad_d;
    }

    public void setPad_d(Integer pad_d) {
        this.pad_d = pad_d;
    }

    public Integer getPas_i() {
        return pas_i;
    }

    public void setPas_i(Integer pas_i) {
        this.pas_i = pas_i;
    }

    public Integer getPad_i() {
        return pad_i;
    }

    public void setPad_i(Integer pad_i) {
        this.pad_i = pad_i;
    }

    public Integer getPas_c() {
        return pas_c;
    }

    public void setPas_c(Integer pas_c) {
        this.pas_c = pas_c;
    }

    public Integer getPad_c() {
        return pad_c;
    }

    public void setPad_c(Integer pad_c) {
        this.pad_c = pad_c;
    }

    public Integer getPas_ce() {
        return pas_ce;
    }

    public void setPas_ce(Integer pas_ce) {
        this.pas_ce = pas_ce;
    }

    public Integer getPad_ce() {
        return pad_ce;
    }

    public void setPad_ce(Integer pad_ce) {
        this.pad_ce = pad_ce;
    }

    public Integer getPas_de() {
        return pas_de;
    }

    public void setPas_de(Integer pas_de) {
        this.pas_de = pas_de;
    }

    public Integer getPad_de() {
        return pad_de;
    }

    public void setPad_de(Integer pad_de) {
        this.pad_de = pad_de;
    }

    public String getClasificacion_pa() {
        return clasificacion_pa;
    }

    public void setClasificacion_pa(String clasificacion_pa) {
        this.clasificacion_pa = clasificacion_pa;
    }

    public int getPas() {
        return pas;
    }

    public void setPas(int pas) {
        this.pas = pas;
    }

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getClasificacion_peso() {
        return clasificacion_peso;
    }

    public void setClasificacion_peso(String clasificacion_peso) {
        this.clasificacion_peso = clasificacion_peso;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getTalla() {
        return talla;
    }

    public void setTalla(Double talla) {
        this.talla = talla;
    }

    public Double getPeso_to() {
        return peso_to;
    }

    public void setPeso_to(Double peso_to) {
        this.peso_to = peso_to;
    }

    public String getClasificacion_perimetro() {
        return clasificacion_perimetro;
    }

    public void setClasificacion_perimetro(String clasificacion_perimetro) {
        this.clasificacion_perimetro = clasificacion_perimetro;
    }

    public Double getPerimetro() {
        return perimetro;
    }

    public void setPerimetro(Double perimetro) {
        this.perimetro = perimetro;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public String getClasificacion_f() {
        return clasificacion_f;
    }

    public void setClasificacion_f(String clasificacion_f) {
        this.clasificacion_f = clasificacion_f;
    }

    public HcRegistroFacade getRegistroFacade() {
        return registroFacade;
    }

    public void setRegistroFacade(HcRegistroFacade registroFacade) {
        this.registroFacade = registroFacade;
    }

    public CitTurnosFacade getTurnosFacade() {
        return turnosFacade;
    }

    public void setTurnosFacade(CitTurnosFacade turnosFacade) {
        this.turnosFacade = turnosFacade;
    }

    public CitCitasFacade getCitasFacade() {
        return citasFacade;
    }

    public void setCitasFacade(CitCitasFacade citasFacade) {
        this.citasFacade = citasFacade;
    }

    public HcTipoRegFacade getTipoRegCliFacade() {
        return tipoRegCliFacade;
    }

    public void setTipoRegCliFacade(HcTipoRegFacade tipoRegCliFacade) {
        this.tipoRegCliFacade = tipoRegCliFacade;
    }

    public HcCamposRegFacade getCamposRegFacade() {
        return camposRegFacade;
    }

    public void setCamposRegFacade(HcCamposRegFacade camposRegFacade) {
        this.camposRegFacade = camposRegFacade;
    }

    public CfgPacientesFacade getPacientesFacade() {
        return pacientesFacade;
    }

    public void setPacientesFacade(CfgPacientesFacade pacientesFacade) {
        this.pacientesFacade = pacientesFacade;
    }

    public CfgMaestrosTxtPredefinidosFacade getMaestrosTxtPredefinidosFacade() {
        return maestrosTxtPredefinidosFacade;
    }

    public void setMaestrosTxtPredefinidosFacade(CfgMaestrosTxtPredefinidosFacade maestrosTxtPredefinidosFacade) {
        this.maestrosTxtPredefinidosFacade = maestrosTxtPredefinidosFacade;
    }

    public CfgClasificacionesFacade getMaestrosClasificacionesFacade() {
        return maestrosClasificacionesFacade;
    }

    public void setMaestrosClasificacionesFacade(CfgClasificacionesFacade maestrosClasificacionesFacade) {
        this.maestrosClasificacionesFacade = maestrosClasificacionesFacade;
    }

    public CfgTxtPredefinidosFacade getTxtPredefinidosFacade() {
        return txtPredefinidosFacade;
    }

    public void setTxtPredefinidosFacade(CfgTxtPredefinidosFacade txtPredefinidosFacade) {
        this.txtPredefinidosFacade = txtPredefinidosFacade;
    }

    public CfgDiagnosticoFacade getDiagnosticoFacade() {
        return diagnosticoFacade;
    }

    public void setDiagnosticoFacade(CfgDiagnosticoFacade diagnosticoFacade) {
        this.diagnosticoFacade = diagnosticoFacade;
    }

    public CfgDiagnosticoPrincipalFacade getDiagnosticoPrincipalFacade() {
        return diagnosticoPrincipalFacade;
    }

    public void setDiagnosticoPrincipalFacade(CfgDiagnosticoPrincipalFacade diagnosticoPrincipalFacade) {
        this.diagnosticoPrincipalFacade = diagnosticoPrincipalFacade;
    }

    public CfgUsuariosFacade getUsuariosFacade() {
        return usuariosFacade;
    }

    public void setUsuariosFacade(CfgUsuariosFacade usuariosFacade) {
        this.usuariosFacade = usuariosFacade;
    }

    public CfgClasificacionesFacade getClasificacionesFacade() {
        return clasificacionesFacade;
    }

    public void setClasificacionesFacade(CfgClasificacionesFacade clasificacionesFacade) {
        this.clasificacionesFacade = clasificacionesFacade;
    }

    public FacServicioFacade getServicioFacade() {
        return servicioFacade;
    }

    public void setServicioFacade(FacServicioFacade servicioFacade) {
        this.servicioFacade = servicioFacade;
    }

    public CfgEmpresaFacade getEmpresaFacade() {
        return empresaFacade;
    }

    public void setEmpresaFacade(CfgEmpresaFacade empresaFacade) {
        this.empresaFacade = empresaFacade;
    }

    public CfgFamiliarFacade getFamiliarFacade() {
        return familiarFacade;
    }

    public void setFamiliarFacade(CfgFamiliarFacade familiarFacade) {
        this.familiarFacade = familiarFacade;
    }

    public HcItemsFacade getItemsFacade() {
        return itemsFacade;
    }

    public void setItemsFacade(HcItemsFacade itemsFacade) {
        this.itemsFacade = itemsFacade;
    }

    public CfgMedicamentoFacade getCfgMedicamento() {
        return cfgMedicamento;
    }

    public void setCfgMedicamento(CfgMedicamentoFacade cfgMedicamento) {
        this.cfgMedicamento = cfgMedicamento;
    }

    public HcArchivosFacade getHcArchivosFacade() {
        return hcArchivosFacade;
    }

    public void setHcArchivosFacade(HcArchivosFacade hcArchivosFacade) {
        this.hcArchivosFacade = hcArchivosFacade;
    }

    public HcRepExamenesFacade getHcRepFacade() {
        return hcRepFacade;
    }

    public void setHcRepFacade(HcRepExamenesFacade hcRepFacade) {
        this.hcRepFacade = hcRepFacade;
    }

    public HcTipoReg getTipoRegistroClinicoActual() {
        return tipoRegistroClinicoActual;
    }

    public void setTipoRegistroClinicoActual(HcTipoReg tipoRegistroClinicoActual) {
        this.tipoRegistroClinicoActual = tipoRegistroClinicoActual;
    }

    public CfgPacientes getPacienteTmp() {
        return pacienteTmp;
    }

    public void setPacienteTmp(CfgPacientes pacienteTmp) {
        this.pacienteTmp = pacienteTmp;
    }

    public CfgTxtPredefinidos getTxtPredefinidoActual() {
        return txtPredefinidoActual;
    }

    public void setTxtPredefinidoActual(CfgTxtPredefinidos txtPredefinidoActual) {
        this.txtPredefinidoActual = txtPredefinidoActual;
    }

    public CfgClasificaciones getClasificacionBuscada() {
        return clasificacionBuscada;
    }

    public void setClasificacionBuscada(CfgClasificaciones clasificacionBuscada) {
        this.clasificacionBuscada = clasificacionBuscada;
    }

    public CfgDiagnostico getDiagnosticoBuscado() {
        return diagnosticoBuscado;
    }

    public void setDiagnosticoBuscado(CfgDiagnostico diagnosticoBuscado) {
        this.diagnosticoBuscado = diagnosticoBuscado;
    }

    public FacServicio getServicioBuscado() {
        return servicioBuscado;
    }

    public void setServicioBuscado(FacServicio servicioBuscado) {
        this.servicioBuscado = servicioBuscado;
    }

    public UploadedFile getArchivos() {
        return archivos;
    }

    public void setArchivos(UploadedFile archivos) {
        this.archivos = archivos;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public int getPosListaTxtPredef() {
        return posListaTxtPredef;
    }

    public void setPosListaTxtPredef(int posListaTxtPredef) {
        this.posListaTxtPredef = posListaTxtPredef;
    }

    public DatosFormularioHistoria getDatosFormulario_nutricion() {
        return datosFormulario_nutricion;
    }

    public void setDatosFormulario_nutricion(DatosFormularioHistoria datosFormulario_nutricion) {
        this.datosFormulario_nutricion = datosFormulario_nutricion;
    }

    public List<HcTipoReg> getListaTipoRegistroClinico() {
        return listaTipoRegistroClinico;
    }

    public void setListaTipoRegistroClinico(List<HcTipoReg> listaTipoRegistroClinico) {
        this.listaTipoRegistroClinico = listaTipoRegistroClinico;
    }

    public HcRegistro getRegistroEncontrado() {
        return registroEncontrado;
    }

    public void setRegistroEncontrado(HcRegistro registroEncontrado) {
        this.registroEncontrado = registroEncontrado;
    }

    public List<DatosFormularioHistoria> getListaRegistrosParaPdf() {
        return listaRegistrosParaPdf;
    }

    public void setListaRegistrosParaPdf(List<DatosFormularioHistoria> listaRegistrosParaPdf) {
        this.listaRegistrosParaPdf = listaRegistrosParaPdf;
    }

    public CfgEmpresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(CfgEmpresa empresa) {
        this.empresa = empresa;
    }

    public boolean isCargandoDesdeTab() {
        return cargandoDesdeTab;
    }

    public void setCargandoDesdeTab(boolean cargandoDesdeTab) {
        this.cargandoDesdeTab = cargandoDesdeTab;
    }

    public CitCitas getCitaActual() {
        return citaActual;
    }

    public void setCitaActual(CitCitas citaActual) {
        this.citaActual = citaActual;
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }

    public Integer getFr() {
        return fr;
    }

    public void setFr(Integer fr) {
        this.fr = fr;
    }

    public Double getT() {
        return t;
    }

    public void setT(Double t) {
        this.t = t;
    }

    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    public void setLineModel1(LineChartModel lineModel1) {
        this.lineModel1 = lineModel1;
    }

    public LineChartModel getLineModel2() {
        return lineModel2;
    }

    public void setLineModel2(LineChartModel lineModel2) {
        this.lineModel2 = lineModel2;
    }

    public LineChartModel getLineModel3() {
        return lineModel3;
    }

    public void setLineModel3(LineChartModel lineModel3) {
        this.lineModel3 = lineModel3;
    }

    public LineChartModel getLineModel4() {
        return lineModel4;
    }

    public void setLineModel4(LineChartModel lineModel4) {
        this.lineModel4 = lineModel4;
    }

    public DatosFormularioHistoria getDatosFormulario_formmedicamentos() {
        return datosFormulario_formmedicamentos;
    }

    public void setDatosFormulario_formmedicamentos(DatosFormularioHistoria datosFormulario_formmedicamentos) {
        this.datosFormulario_formmedicamentos = datosFormulario_formmedicamentos;
    }

    /**
     * Método para buscar un medicamento bien sea por su código o nombre.
     *
     * @param _txt cadena por la que se buscará el medicamento.
     * @return
     */
    public List<String> autocompletarMedicamento(String _txt) { //retorna una lista con los medicamento que contengan el parametro txt

        if (_txt != null && _txt.length() > 2) {
            return invBodegaProductosFacade.autocompletarMedicamentos(_txt.toUpperCase());
        } else {
            return null;
        }
    }

    public Boolean getFalla_atencion() {
        return falla_atencion;
    }

    public void setFalla_atencion(Boolean falla_atencion) {
        this.falla_atencion = falla_atencion;
    }

    /**
     * @return the urlZonasActivasOleary
     */
    public String getUrlZonasActivasOleary() {
        return urlZonasActivasOleary;
    }

    /**
     * @param urlZonasActivasOleary the urlZonasActivasOleary to set
     */
    public void setUrlZonasActivasOleary(String urlZonasActivasOleary) {
        this.urlZonasActivasOleary = urlZonasActivasOleary;
    }

    /**
     * Obtiene el json correspondente al odontograma
     *
     * @return the El json con los datos correspondientes al odontograma
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public String getJsonOdontograma() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> valores = new ArrayList<>();
        Class<DatosFormularioHistoria> clase = (Class<DatosFormularioHistoria>) datosFormulario.getClass();
        for (int i = 90; i <= 505; i++) {
            Method method = clase.getMethod(String.format("getDato%1$d", i));
            Object valor = method.invoke(datosFormulario);
            valores.add(String.format("%1$d=%2$s", i, "".equals(valor) ? loginMB.getRutaCarpetaImagenes() + "Reportes/dienteLimpiar.png" : valor));
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        this.jsonOdontograma = new Gson().toJson(valores, listType);
        return jsonOdontograma;
    }

    /**
     * Obtiene el json correspondente al odontograma de la evolucion
     *
     * @return the El json con los datos correspondientes al odontograma de
     * evolucion
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public String getJsonEvolucion() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> valores = new ArrayList<>();
        Class<DatosFormularioHistoria> clase = (Class<DatosFormularioHistoria>) datosFormulario.getClass();
        for (int i = 878; i <= 1293; i++) {
            Method method = clase.getMethod(String.format("getDato%1$d", i));
            Object valor = method.invoke(datosFormulario);
            valores.add(String.format("%1$d=%2$s", i, "".equals(valor) ? loginMB.getRutaCarpetaImagenes() + "Reportes/dienteLimpiar.png" : valor));
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        this.jsonEvolucion = new Gson().toJson(valores, listType);
        return jsonEvolucion;
    }

    public String getJsonOleary1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> valores = new ArrayList<>();
        Class<DatosFormularioHistoria> clase = (Class<DatosFormularioHistoria>) datosFormulario.getClass();
        for (int i = 523; i <= 682; i++) {
            Method method = clase.getMethod(String.format("getDato%1$d", i));
            Object valor = method.invoke(datosFormulario);
            valores.add(String.format("%1$d=%2$s", i, "".equals(valor) ? loginMB.getRutaCarpetaImagenes() + "Reportes/dienteLimpiar.png" : valor));
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        this.jsonOleary1 = new Gson().toJson(valores, listType);
        return jsonOleary1;
    }

    public String getJsonOleary2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<String> valores = new ArrayList<>();
        Class<DatosFormularioHistoria> clase = (Class<DatosFormularioHistoria>) datosFormulario.getClass();
        for (int i = 687; i <= 846; i++) {
            Method method = clase.getMethod(String.format("getDato%1$d", i));
            Object valor = method.invoke(datosFormulario);
            valores.add(String.format("%1$d=%2$s", i, "".equals(valor) ? loginMB.getRutaCarpetaImagenes() + "Reportes/olearyLimpio.png" : valor));
        }
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        this.jsonOleary1 = new Gson().toJson(valores, listType);
        return jsonOleary1;
    }

    /**
     * @param jsonOdontograma the jsonOdontograma to set
     */
    public void setJsonOdontograma(String jsonOdontograma) {
        this.jsonOdontograma = jsonOdontograma;
    }

    /**
     * @return the edadAnios
     */
    public int getEdadAnios() {
        edadAnios = this.calcularEdadInt(this.getPacienteSeleccionado().getFechaNacimiento());
        return edadAnios;
    }

    /**
     * @param edadAnios the edadAnios to set
     */
    public void setEdadAnios(int edadAnios) {
        this.edadAnios = edadAnios;
    }

    /**
     * @param jsonEvolucion the jsonEvolucion to set
     */
    public void setJsonEvolucion(String jsonEvolucion) {
        this.jsonEvolucion = jsonEvolucion;
    }

    /**
     * @param jsonOleary1 the jsonOleary1 to set
     */
    public void setJsonOleary1(String jsonOleary1) {
        this.jsonOleary1 = jsonOleary1;
    }

    /**
     * @param jsonOleary2 the jsonOleary2 to set
     */
    public void setJsonOleary2(String jsonOleary2) {
        this.jsonOleary2 = jsonOleary2;
    }

    private boolean esCampoGraficaOdontologia(DatosFormularioHistoria datosReporte, List<HcDetalle> detalles, int contador) {
        int posicion = detalles.get(contador).getHcCamposReg().getPosicion();
        boolean retorno = false;
        String basePath = this.loginMB.getRutaCarpetaImagenes();
        if ((posicion >= 90 && posicion <= 505)//Odontograma
                || (posicion >= 878 && posicion <= 1293)) {//Odontograma- evolucion tratamiento
            int sustractor = (posicion >= 90 && posicion <= 505) ? 90 : 878;
            if ((posicion - sustractor) % 8 == 0) {
                List<String> valores = new ArrayList<>();
                for (int i = contador; i < contador + 8; i++) {
                    HcDetalle detalle = detalles.get(i);
                    valores.add(detalle.getValor());
                }
                try {
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    String url = request.getRequestURL().toString().replaceAll("(?i)/OM.*$", "/om/recursos/img/dienteNormal.svg");
                    SVGHelper.buildToothJpegFile(String.format("%1$sdato%2$d.svg", basePath, posicion), url, valores.toArray(new String[8]));
                    datosReporte.setValor(detalles.get(contador).getHcCamposReg().getPosicion(), String.format("%1$sdato%2$d.jpg", basePath, posicion));
                } catch (TranscoderException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    throw new RuntimeException("Error construyendo imagen SVG-odontograma para campo: dato" + contador, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Error construyendo imagen SVG-odontograma para campo: dato" + contador, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Error construyendo imagen SVG-odontograma para campo: dato" + contador, ex);
                }
            }
            retorno = true;
        }
        if ((posicion >= 523 && posicion <= 682)//Oleary1
                || (posicion >= 687 && posicion <= 846)) {//Oleary2
            int sustractor = (posicion >= 523 && posicion <= 682) ? 523 : 687;
            if ((posicion - sustractor) % 5 == 0) {
                List<String> valores = new ArrayList<>();
                for (int i = contador; i < contador + 5; i++) {
                    HcDetalle detalle = detalles.get(i);
                    valores.add(detalle.getValor());
                }
                try {
                    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                    String url = request.getRequestURL().toString().replaceAll("(?i)/OM.*$", "/om/recursos/img/oleary.svg");
                    SVGHelper.buildOlearyJpegFile(String.format("%1$sdato%2$d.svg", basePath, posicion), url, valores.toArray(new String[5]));
                    datosReporte.setValor(detalles.get(contador).getHcCamposReg().getPosicion(), String.format("%1$sdato%2$d.jpg", basePath, posicion));
                } catch (TranscoderException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                    throw new RuntimeException("Error construyendo imagen SVG-oleary para campo: dato" + contador, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Error construyendo imagen SVG-oleary para campo: dato" + contador, ex);
                } catch (TransformerException ex) {
                    Logger.getLogger(HistoriasMB.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Error construyendo imagen SVG-oleary para campo: dato" + contador, ex);
                }
            }
            retorno = true;
        }
        return retorno;
    }

    /**
     * @return the listaOdontologos
     */
    public List<CfgUsuarios> getListaOdontologos() {
        return listaOdontologos;
    }

    /**
     * @param listaOdontologos the listaOdontologos to set
     */
    public void setListaOdontologos(List<CfgUsuarios> listaOdontologos) {
        this.listaOdontologos = listaOdontologos;
    }

    public List<FacServicio> getListaServiciosOrdenMedica() {
        return listaServiciosOrdenMedica;
    }

    public void setListaServiciosOrdenMedica(List<FacServicio> listaServiciosOrdenMedica) {
        this.listaServiciosOrdenMedica = listaServiciosOrdenMedica;
    }

    public String getValorDefecto1() {
        return valorDefecto1;
    }

    public void setValorDefecto1(String valorDefecto1) {
        this.valorDefecto1 = valorDefecto1;
    }

    public String getValorDefecto2() {
        return valorDefecto2;
    }

    public void setValorDefecto2(String valorDefecto2) {
        this.valorDefecto2 = valorDefecto2;
    }

    public String getValorDefecto3() {
        return valorDefecto3;
    }

    public void setValorDefecto3(String valorDefecto3) {
        this.valorDefecto3 = valorDefecto3;
    }

    public String getValorDefecto4() {
        return valorDefecto4;
    }

    public void setValorDefecto4(String valorDefecto4) {
        this.valorDefecto4 = valorDefecto4;
    }

    public String getValorDefecto5() {
        return valorDefecto5;
    }

    public void setValorDefecto5(String valorDefecto5) {
        this.valorDefecto5 = valorDefecto5;
    }

    public String getValorDefecto6() {
        return valorDefecto6;
    }

    public void setValorDefecto6(String valorDefecto6) {
        this.valorDefecto6 = valorDefecto6;
    }

    public String getValorDefecto7() {
        return valorDefecto7;
    }

    public void setValorDefecto7(String valorDefecto7) {
        this.valorDefecto7 = valorDefecto7;
    }

    public String getValorDefecto8() {
        return valorDefecto8;
    }

    public void setValorDefecto8(String valorDefecto8) {
        this.valorDefecto8 = valorDefecto8;
    }

    public String getValorDefecto9() {
        return valorDefecto9;
    }

    public void setValorDefecto9(String valorDefecto9) {
        this.valorDefecto9 = valorDefecto9;
    }

    public String getValorDefecto10() {
        return valorDefecto10;
    }

    public void setValorDefecto10(String valorDefecto10) {
        this.valorDefecto10 = valorDefecto10;
    }

    public String getValorDefecto11() {
        return valorDefecto11;
    }

    public void setValorDefecto11(String valorDefecto11) {
        this.valorDefecto11 = valorDefecto11;
    }

    public String getValorDefecto12() {
        return valorDefecto12;
    }

    public void setValorDefecto12(String valorDefecto12) {
        this.valorDefecto12 = valorDefecto12;
    }

    public String getValorDefecto13() {
        return valorDefecto13;
    }

    public void setValorDefecto13(String valorDefecto13) {
        this.valorDefecto13 = valorDefecto13;
    }

    public String getValorDefecto14() {
        return valorDefecto14;
    }

    public void setValorDefecto14(String valorDefecto14) {
        this.valorDefecto14 = valorDefecto14;
    }

    public String getValorDefecto15() {
        return valorDefecto15;
    }

    public void setValorDefecto15(String valorDefecto15) {
        this.valorDefecto15 = valorDefecto15;
    }

    public String getValorDefecto16() {
        return valorDefecto16;
    }

    public void setValorDefecto16(String valorDefecto16) {
        this.valorDefecto16 = valorDefecto16;
    }

    public String getValorDefecto17() {
        return valorDefecto17;
    }

    public void setValorDefecto17(String valorDefecto17) {
        this.valorDefecto17 = valorDefecto17;
    }

    public String getValorDefecto18() {
        return valorDefecto18;
    }

    public void setValorDefecto18(String valorDefecto18) {
        this.valorDefecto18 = valorDefecto18;
    }

    public String getValorDefecto19() {
        return valorDefecto19;
    }

    public void setValorDefecto19(String valorDefecto19) {
        this.valorDefecto19 = valorDefecto19;
    }

    public String getValorDefecto20() {
        return valorDefecto20;
    }

    public void setValorDefecto20(String valorDefecto20) {
        this.valorDefecto20 = valorDefecto20;
    }

    public List<SelectItem> getListaValoresDefecto() {
        return listaValoresDefecto;
    }

    public void setListaValoresDefecto(List<SelectItem> listaValoresDefecto) {
        this.listaValoresDefecto = listaValoresDefecto;
    }

    public List<SelectItem> getListaValoresDefecto2() {
        return listaValoresDefecto2;
    }

    public void setListaValoresDefecto2(List<SelectItem> listaValoresDefecto2) {
        this.listaValoresDefecto2 = listaValoresDefecto2;
    }
    
    

}
