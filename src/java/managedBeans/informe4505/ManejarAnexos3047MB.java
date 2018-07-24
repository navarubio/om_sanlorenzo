/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informe4505;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import managedBeans.historias.DatosFormularioHistoria;
import managedBeans.historias.HistoriasMB;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgDiagnostico;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgHistoriaCamposPredefinidos;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.FacServicio;
import modelo.entidades.Hc3047Anexo1;
import modelo.entidades.Hc3047Anexo2;
import modelo.entidades.Hc3047Anexo3;
import modelo.entidades.Hc3047Anexo31;
import modelo.entidades.Hc3047Anexo4;
import modelo.entidades.Hc3047Anexo41;
import modelo.entidades.Hc3047Anexo5;
import modelo.entidades.Hc3047Anexo6;
import modelo.entidades.HcAnexos3047;
import modelo.entidades.HcCamposReg;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgDiagnosticoPrincipalFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.Hc3047Anexo1Facade;
import modelo.fachadas.Hc3047Anexo2Facade;
import modelo.fachadas.Hc3047Anexo3Facade;
import modelo.fachadas.Hc3047Anexo3_1Facade;
import modelo.fachadas.Hc3047Anexo4Facade;
import modelo.fachadas.Hc3047Anexo4_1Facade;
import modelo.fachadas.Hc3047Anexo5Facade;
import modelo.fachadas.Hc3047Anexo6Facade;
import modelo.fachadas.HcAnexos3047Facade;
import net.sf.jasperreports.data.cache.NullableValues;
import org.primefaces.context.RequestContext;

/**
 *
 * @author sismacontab
 */
@ManagedBean(name = "manejarAnexos3047MB")
@SessionScoped
public class ManejarAnexos3047MB extends MetodosGenerales implements Serializable {

    /**
     * Manejar Informacion de Formularios Anexos 3047
     */
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgDiagnosticoPrincipalFacade diagnosticoFacade;
    @EJB
    Hc3047Anexo1Facade hc3047Anexo1Facade;
    @EJB
    Hc3047Anexo2Facade hc3047Anexo2Facade;
    @EJB
    Hc3047Anexo3Facade hc3047Anexo3Facade;
    @EJB
    Hc3047Anexo3_1Facade hc3047Anexo3_1Facade;
    @EJB
    Hc3047Anexo4Facade hc3047Anexo4Facade;
    @EJB
    Hc3047Anexo4_1Facade hc3047Anexo41Facade;
    @EJB
    Hc3047Anexo5Facade hc3047Anexo5Facade;
    @EJB
    Hc3047Anexo6Facade hc3047Anexo6Facade;
    @EJB
    CfgUsuariosFacade cfgUsuariosFacade;
    @EJB
    CfgPacientesFacade cfgPacientesFacade;
    @EJB
    CfgDiagnosticoFacade cfgDiagnosticoFacade;
    @EJB
    HcAnexos3047Facade hcAnexos3047Facade;
    @EJB
    CfgClasificacionesFacade cfgClasificacionesFacade;
    @EJB
    CfgEmpresaFacade cfgEmpresaFacade;
    @EJB
    FacServicioFacade facServicioFacade;

    private String pacienteremitido = "";
    private int tipomovimiento = 0;
    private int consecutivo = 0;
    private boolean prestadorremitente = false;
    private List<SelectItem> listaMunicipios;
    private List<CfgClasificaciones> listaEspecialidades;
    private List<FacServicio> listaCUPS;
    private List<Hc3047Anexo31> listarequerimiento = new ArrayList();
    private List<Hc3047Anexo41> listarequerimiento1 = new ArrayList();
    private String departamento = "";
    private String municipio = "";
    private String tiposerviciosolicita = "";
    private String prioridadservicio = "";
    private String ubicacionpaciente = "";
    private String numeroInforme = "";
    private String numeroAtencion = "";
    private String numeroSolicitud = "";
    private String numeroAutorizacion = "";
    private String numeroRemision = "";
    private String numeroContrarremision = "";

    private boolean coutam = false;
    private boolean copago = false;
    private boolean coutar = false;
    private boolean coutaotro = false;

    boolean primerapellido = false;
    boolean segundoapellido = false;
    boolean primernombre = false;
    boolean segundonombre = false;
    boolean tipodocumentoidentifica = false;
    boolean numerodocumento = false;
    boolean fechanacimiento = false;
    boolean accidentetransito = false;
    int actualizarregistro = 0;
    int nuevoregistro = 1;
    boolean imprimirregistro = false;
    boolean lupanexo1 = false;
    boolean lupanexo2 = false;
    boolean lupanexo3 = false;
    boolean lupanexo4 = false;
    boolean lupanexo5 = false;
    boolean lupanexo6 = false;
    
    int visualizar = 0;

    private boolean haypacienteseleccionado = false;
    private CfgPacientes pacienteseleccionado;
    private Date fechaReg = new Date();
    private Hc3047Anexo1 nuevoAnexo1 = new Hc3047Anexo1();
    private Hc3047Anexo2 nuevoAnexo2 = new Hc3047Anexo2();
    private Hc3047Anexo3 nuevoAnexo3 = new Hc3047Anexo3();
    private Hc3047Anexo3 codAnexo3 = new Hc3047Anexo3();
    private Hc3047Anexo4 codAnexo4 = new Hc3047Anexo4();

    private Hc3047Anexo31 anexo3Grabar = new Hc3047Anexo31();
    private Hc3047Anexo41 anexo4Grabar = new Hc3047Anexo41();

    private Hc3047Anexo31 nuevoAnexo31 = new Hc3047Anexo31();
    private Hc3047Anexo4 nuevoAnexo4 = new Hc3047Anexo4();
    private Hc3047Anexo41 nuevoAnexo41 = new Hc3047Anexo41();
    private Hc3047Anexo5 nuevoAnexo5 = new Hc3047Anexo5();
    private Hc3047Anexo6 nuevoAnexo6 = new Hc3047Anexo6();
    private Hc3047Anexo1 Anexo1Seleccionado;
    private Hc3047Anexo2 Anexo2Seleccionado;
    private Hc3047Anexo3 Anexo3Seleccionado;
    private Hc3047Anexo4 Anexo4Seleccionado;
    private Hc3047Anexo5 Anexo5Seleccionado;
    private Hc3047Anexo6 Anexo6Seleccionado;

    private FacServicio facServ;
    private CfgEmpresa empresa;
    private int id = 0;
    private int id1 = 0;

    private double cantidad = 0.0;

    private HcAnexos3047 tipoanexoActual = new HcAnexos3047();
    private List<CfgClasificaciones> listaInconsistencias = null;
    private List<CfgClasificaciones> listaTipoidentificacion = null;
    private List<CfgUsuarios> listaUsuarios = null;
    private List<Hc3047Anexo1> listaAnexo1Modif = null;
    private List<Hc3047Anexo2> listaAnexo1Modif2 = null;
    private List<Hc3047Anexo3> listaAnexo1Modif3 = null;
    private List<Hc3047Anexo4> listaAnexo1Modif4 = null;
    private List<Hc3047Anexo5> listaAnexo1Modif5 = null;
    private List<Hc3047Anexo6> listaAnexo1Modif6 = null;

//    private HistoriasMB historiasMB;
    private String cei100 = null;
    private String cei101 = null;
    private String cei102 = null;
    private String cei103 = null;
    private String cups1 = null;

    private CfgDiagnostico diagnosticoppal;
    private CfgDiagnostico diagnosticorelacion1;
    private CfgDiagnostico diagnosticorelacion2;
    private CfgDiagnostico diagnosticorelacion3;
    private CfgClasificaciones origenatencion;

    private HcAnexos3047 anexoActual;

    private DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico

    public ManejarAnexos3047MB() {
        //      historiasMB=new HistoriasMB();
    }

    @PostConstruct
    public void init() {
        listaInconsistencias = clasificacionesFacade.buscarPorMaestro("Inconsistencia");
        listaTipoidentificacion = clasificacionesFacade.buscarPorMaestro("TipoIdentificacion");
        listaUsuarios = cfgUsuariosFacade.buscarOrdenado();
        listaEspecialidades = cfgClasificacionesFacade.buscarPorMaestro("Especialidad");
        listaCUPS = facServicioFacade.buscarTodosOrdenado();
        nuevoAnexo1.setFechadocumento(fechaReg);
        nuevoAnexo2.setFechadocumento(fechaReg);
        nuevoAnexo2.setFechaingreso(fechaReg);
        nuevoAnexo3.setFechadocumento(fechaReg);
        nuevoAnexo4.setFechadocumento(fechaReg);
        nuevoAnexo5.setFechadocumento(fechaReg);
        nuevoAnexo6.setFechadocumento(fechaReg);

        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        empresa = cfgEmpresaFacade.findAll().get(0);
    }

    public void selecciontipomovimiento() {
        if (pacienteremitido.equals("SI")) {
            tipomovimiento = 1;
            prestadorremitente = true;
        } else if (pacienteremitido.equals("NO")) {
            tipomovimiento = 0;
            prestadorremitente = false;
            datosFormulario.setDato1(null);
            listaMunicipios = null;
        } else {
            tipomovimiento = 0;
            prestadorremitente = false;
        }
        nuevoAnexo2.setReferido(prestadorremitente);
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

    public void guardarAnexo1() {//guardar un nuevo registro clinico        
        int idSede = 1;
        System.out.println(fechaReg);
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);

        if (nuevoregistro == 1) {
            System.out.println("Iniciando el Almacenamiento del registro " + numeroInforme);
            try {
                generarNumeroInforme();
                nuevoAnexo1.setIdPaciente(pacienteseleccionado);
                nuevoAnexo1.setNumeroinforme(numeroInforme);
                hc3047Anexo1Facade.create(nuevoAnexo1);
                anexoActual.setConsecutivo(consecutivo);
                hcAnexos3047Facade.edit(anexoActual);
                imprimirregistro = true;
                nuevoregistro = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo fue almacenado con el Nro " + numeroInforme));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo"));
            }
        }
        if (actualizarregistro == 1) {
            System.out.println("Iniciando actualizacion del registro " + nuevoAnexo1.getNumeroinforme());
            try {
                hc3047Anexo1Facade.edit(nuevoAnexo1);
                imprimirregistro = true;
                actualizarregistro = 0;
                nuevoregistro = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo fue actualziado con exito"));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Actualizar el Anexo"));
            }

        }

    }

    public void guardarAnexo2() {//guardar un nuevo registro clinico        
        int idSede = 1;
        System.out.println(fechaReg);
        String codigo0 = "";
        String codigo1 = "";
        String codigo2 = "";
        String codigo3 = "";
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        if (nuevoregistro == 1) {
            try {
                System.out.println("Iniciando el guardado del registro Anexo2 " + numeroAtencion);
                generarNumeroAtencion();
                nuevoAnexo2.setIdPaciente(pacienteseleccionado);
                if (cei100 != null) {
                    codigo0 = cei100.substring(0, 4);
                    diagnosticoppal = cfgDiagnosticoFacade.find(codigo0);
                    nuevoAnexo2.setCei100(diagnosticoppal);
                }
                if (cei101 != null) {
                    codigo1 = cei101.substring(0, 4);
                    diagnosticorelacion1 = cfgDiagnosticoFacade.find(codigo1);
                    nuevoAnexo2.setCei101(diagnosticorelacion1);
                }
                if (cei102 != null) {
                    codigo2 = cei102.substring(0, 4);
                    diagnosticorelacion2 = cfgDiagnosticoFacade.find(codigo2);
                    nuevoAnexo2.setCei102(diagnosticorelacion2);
                }
                if (cei103 != null) {
                    codigo3 = cei103.substring(0, 4);
                    diagnosticorelacion3 = cfgDiagnosticoFacade.find(codigo3);
                    nuevoAnexo2.setCei103(diagnosticorelacion3);
                }
                if (prestadorremitente) {
                    nuevoAnexo2.setIddepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
                    municipio = datosFormulario.getDato2().toString();
                    nuevoAnexo2.setIdmunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
                }
                nuevoAnexo2.setNumeroatencion(numeroAtencion);
                hc3047Anexo2Facade.create(nuevoAnexo2);
                anexoActual.setConsecutivo(consecutivo);
                hcAnexos3047Facade.edit(anexoActual);
                imprimirregistro = true;
                nuevoregistro = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo2 fue almacenado con el Nro " + numeroAtencion));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo2"));
            }
        }
        if (actualizarregistro == 1) {
            System.out.println("Iniciando actualizacion del registro " + nuevoAnexo2.getNumeroatencion());
            try {
                if (cei100 != null) {
                    codigo0 = cei100.substring(0, 4);
                    diagnosticoppal = cfgDiagnosticoFacade.find(codigo0);
                    nuevoAnexo2.setCei100(diagnosticoppal);
                }
                if (cei101 != null) {
                    codigo1 = cei101.substring(0, 4);
                    diagnosticorelacion1 = cfgDiagnosticoFacade.find(codigo1);
                    nuevoAnexo2.setCei101(diagnosticorelacion1);
                }
                if (cei102 != null) {
                    codigo2 = cei102.substring(0, 4);
                    diagnosticorelacion2 = cfgDiagnosticoFacade.find(codigo2);
                    nuevoAnexo2.setCei102(diagnosticorelacion2);
                }
                if (cei103 != null) {
                    codigo3 = cei103.substring(0, 4);
                    diagnosticorelacion3 = cfgDiagnosticoFacade.find(codigo3);
                    nuevoAnexo2.setCei103(diagnosticorelacion3);
                }
                if (prestadorremitente) {
                    nuevoAnexo2.setIddepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
                    municipio = datosFormulario.getDato2().toString();
                    nuevoAnexo2.setIdmunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
                }

                hc3047Anexo2Facade.edit(nuevoAnexo2);
                imprimirregistro = true;
                actualizarregistro = 0;
                nuevoregistro = 0;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo fue actualziado con exito"));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Actualizar el Anexo"));
            }

        }

    }

    public void guardarAnexo3() {//guardar un nuevo registro clinico        
        int idSede = 1;
        generarNumeroSolicitud();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro Anexo3 " + numeroSolicitud);
        String codigo0 = "";
        String codigo1 = "";
        String codigo2 = "";
        String codigo3 = "";
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        try {
            nuevoAnexo3.setIdPaciente(pacienteseleccionado);
            nuevoAnexo3.setNumerosolicitud(numeroSolicitud);
            if (cei100 != null) {
                codigo0 = cei100.substring(0, 4);
                diagnosticoppal = cfgDiagnosticoFacade.find(codigo0);
                nuevoAnexo3.setCei100(diagnosticoppal);
            }
            if (cei101 != null) {
                codigo1 = cei101.substring(0, 4);
                diagnosticorelacion1 = cfgDiagnosticoFacade.find(codigo1);
                nuevoAnexo3.setCei101(diagnosticorelacion1);
            }
            if (cei102 != null) {
                codigo2 = cei102.substring(0, 4);
                diagnosticorelacion2 = cfgDiagnosticoFacade.find(codigo2);
                nuevoAnexo3.setCei102(diagnosticorelacion2);
            }
            hc3047Anexo3Facade.create(nuevoAnexo3);
            codAnexo3 = hc3047Anexo3Facade.ultimoInsertado();

            for (Hc3047Anexo31 rq : listarequerimiento) {
                anexo3Grabar.setId3047anexo3(codAnexo3);
                anexo3Grabar.setIdServicio(rq.getIdServicio());
                anexo3Grabar.setCantidad(rq.getCantidad());
                hc3047Anexo3_1Facade.create(anexo3Grabar);
            }
//            listarequerimiento.clear();

            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo3 fue almacenado con el Nro " + numeroSolicitud));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo3"));
        }
    }

    public void guardarAnexo4() {//guardar un nuevo registro clinico        
        int idSede = 1;
        generarNumeroAutorizacion();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro Anexo4 " + numeroAutorizacion);
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        try {
            nuevoAnexo4.setIdPaciente(pacienteseleccionado);
            nuevoAnexo4.setNumeroautorizacion(numeroAutorizacion);
            hc3047Anexo4Facade.create(nuevoAnexo4);

            codAnexo4 = hc3047Anexo4Facade.ultimoInsertado();

            for (Hc3047Anexo41 rq : listarequerimiento1) {
                anexo4Grabar.setId3047anexo4(codAnexo4);
                anexo4Grabar.setIdServicio(rq.getIdServicio());
                anexo4Grabar.setCantidad(rq.getCantidad());
                hc3047Anexo41Facade.create(anexo4Grabar);
            }
            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo4 fue almacenado con el Nro " + numeroAutorizacion));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo4"));
        }
    }

    public void guardarAnexo5() {//guardar un nuevo registro clinico        
        int idSede = 1;
        generarNumeroRemision();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro Anexo5 " + numeroRemision);
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        try {
            nuevoAnexo5.setIdPaciente(pacienteseleccionado);
            nuevoAnexo5.setNumeroremision(numeroRemision);
            nuevoAnexo5.setIdDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
            municipio = datosFormulario.getDato2().toString();
            nuevoAnexo5.setIdMunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
            hc3047Anexo5Facade.create(nuevoAnexo5);
            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo5 fue almacenado con el Nro " + numeroRemision));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo5"));
        }

    }

    public void guardarAnexo6() {//guardar un nuevo registro clinico        
        int idSede = 1;
        generarNumeroContrarremision();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro Anexo6 " + numeroContrarremision);
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        try {
            nuevoAnexo6.setIdPaciente(pacienteseleccionado);
            nuevoAnexo6.setNumerocontrarremision(numeroContrarremision);
            nuevoAnexo6.setIdDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
            municipio = datosFormulario.getDato2().toString();
            nuevoAnexo6.setIdMunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
            hc3047Anexo6Facade.create(nuevoAnexo6);
            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo6 fue almacenado con el Nro " + numeroContrarremision));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo6"));
        }

    }

    public void generarNumeroInforme() {
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(1);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroInforme = yearsmall + month + consecutivo;

    }

    public void generarNumeroAtencion() {
        //fechacomprobante=comprobanteivaef.getFecha();
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(2);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroAtencion = yearsmall + month + consecutivo;
    }

    public void generarNumeroSolicitud() {
        //fechacomprobante=comprobanteivaef.getFecha();
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(3);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroSolicitud = yearsmall + month + consecutivo;
    }

    public void generarNumeroAutorizacion() {
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(4);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroAutorizacion = yearsmall + month + consecutivo;
    }

    public void generarNumeroRemision() {
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(5);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroRemision = yearsmall + month + consecutivo;

    }

    public void generarNumeroContrarremision() {
        int mes = 0;
        int anio = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaReg);
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH) + 1;
        String year = Integer.toString(anio);
        String yearsmall = year.substring(2, 4);
        String month = String.format("%02d", mes);
        anexoActual = hcAnexos3047Facade.find(6);
        consecutivo = anexoActual.getConsecutivo() + 1;
        numeroContrarremision = yearsmall + month + consecutivo;

    }

    public void verReporte() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reporteAnexos
        ReporteAnexos rAnexo = new ReporteAnexos();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/articulos.jasper");

        rAnexo.getReporte(ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo1() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();

        String admin = null;
        String codadmin = null;
        String dptopaciente = null;
        String mcpiopaciente = null;
        String dptoempresa = null;
        String mcpioempresa = null;

        if (nuevoAnexo1.getIdPaciente()!= null) {
            admin = nuevoAnexo1.getIdPaciente().getIdAdministradora().getRazonSocial();
            codadmin = nuevoAnexo1.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
            dptopaciente = nuevoAnexo1.getIdPaciente().getDepartamento().getDescripcion();
            mcpiopaciente = nuevoAnexo1.getIdPaciente().getMunicipio().getDescripcion();
        }
        if (empresa.getCodDepartamento().getDescripcion() != null) {
            dptoempresa = empresa.getCodDepartamento().getDescripcion();
            mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        }
        String numinform = numeroInforme;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexoone.jasper");

        rAnexo.getAnexo1(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, numinform, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo2() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();
        String admin = null;
        String codadmin = null;
        String dptopaciente = null;
        String mcpiopaciente = null;
        String dptoempresa = null;
        String mcpioempresa = null;
        String dptoremite = null;
        String mcpioremite = null;
        String numinform = numeroAtencion;
        String diagppal = null;
        String diagrel1 = null;
        String diagrel2 = null;
        String diagrel3 = null;
        
        if (nuevoAnexo2.getIdPaciente() != null) {
            admin = nuevoAnexo2.getIdPaciente().getIdAdministradora().getRazonSocial();
            codadmin = nuevoAnexo2.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
            dptopaciente = nuevoAnexo2.getIdPaciente().getDepartamento().getDescripcion();
            mcpiopaciente = nuevoAnexo2.getIdPaciente().getMunicipio().getDescripcion();
        }
        if (empresa.getCodDepartamento()!= null) {
            dptoempresa = empresa.getCodDepartamento().getDescripcion();
            mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        }
        if (nuevoAnexo2.getReferido()){
            if (nuevoAnexo2.getIddepartamento()!=null) {
                dptoremite = nuevoAnexo2.getIddepartamento().getDescripcion();
            }
            if (nuevoAnexo2.getIdmunicipio().getDescripcion() != null) {
                mcpioremite = nuevoAnexo2.getIdmunicipio().getDescripcion();
            }
        }
       
        if (nuevoAnexo2.getCei100()!= null) {
            diagppal = nuevoAnexo2.getCei100().getNombreDiagnostico();
        }
        if (nuevoAnexo2.getCei101()!= null) {
            diagrel1 = nuevoAnexo2.getCei101().getNombreDiagnostico();
        }
        
        if (nuevoAnexo2.getCei102()!= null) {
            diagrel2 = nuevoAnexo2.getCei102().getNombreDiagnostico();
        }
        if (nuevoAnexo2.getCei103()!= null) {
            diagrel3 = nuevoAnexo2.getCei103().getNombreDiagnostico();
        }
        numinform = numeroAtencion;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexotwo.jasper");

        rAnexo.getAnexo2(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, dptoremite, mcpioremite, diagppal, diagrel1, diagrel2, diagrel3, numinform, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo3() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();
        List<Hc3047Anexo31> listaCupsDetalles = hc3047Anexo3_1Facade.buscarAnexos31xAnexo3(codAnexo3.getId3047anexo3());

        String admin = nuevoAnexo3.getIdPaciente().getIdAdministradora().getRazonSocial();
        String codadmin = nuevoAnexo3.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
        String dptopaciente = nuevoAnexo3.getIdPaciente().getDepartamento().getDescripcion();
        String mcpiopaciente = nuevoAnexo3.getIdPaciente().getMunicipio().getDescripcion();
        String dptoempresa = empresa.getCodDepartamento().getDescripcion();
        String mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        String numinform = numeroSolicitud;
        String diagppal = null;
        String diagrel1 = null;
        String diagrel2 = null;
        String services = null;
        if (nuevoAnexo3.getCei100()!= null) {
            diagppal = nuevoAnexo3.getCei100().getNombreDiagnostico();
        }
        if (nuevoAnexo3.getCei101()!= null) {
            diagrel1 = nuevoAnexo3.getCei101().getNombreDiagnostico();
        }
        if (nuevoAnexo3.getCei102()!= null) {
            diagrel2 = nuevoAnexo3.getCei102().getNombreDiagnostico();
        }
        if (nuevoAnexo3.getIdservicio() != null) {
            services = nuevoAnexo3.getIdservicio().getDescripcion();
        }
        int anexo3 = codAnexo3.getId3047anexo3();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexotree.jasper");

        rAnexo.getAnexo3(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, diagppal, diagrel1, diagrel2, services, numinform, anexo3, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo4() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();

        String admin = nuevoAnexo4.getIdPaciente().getIdAdministradora().getRazonSocial();
        String codadmin = nuevoAnexo4.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
        String dptopaciente = nuevoAnexo4.getIdPaciente().getDepartamento().getDescripcion();
        String mcpiopaciente = nuevoAnexo4.getIdPaciente().getMunicipio().getDescripcion();
        String dptoempresa = empresa.getCodDepartamento().getDescripcion();
        String mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        String numinform = numeroAutorizacion;
        String services = null;
        if (nuevoAnexo4.getIdservicio().getDescripcion() != null) {
            services = nuevoAnexo4.getIdservicio().getDescripcion();
        }
        int anexo4 = codAnexo4.getId3047anexo4();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexofour.jasper");

        rAnexo.getAnexo4(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, services, numinform, anexo4, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo5() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();

        String admin = nuevoAnexo5.getIdPaciente().getIdAdministradora().getRazonSocial();
        String codadmin = nuevoAnexo5.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
        String dptopaciente = nuevoAnexo5.getIdPaciente().getDepartamento().getDescripcion();
        String mcpiopaciente = nuevoAnexo5.getIdPaciente().getMunicipio().getDescripcion();
        String dptoempresa = empresa.getCodDepartamento().getDescripcion();
        String mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        String dptoresponsable = nuevoAnexo5.getIdDepartamento().getDescripcion();
        String mcpioresponsable = nuevoAnexo5.getIdMunicipio().getDescripcion();
        String numinform = numeroRemision;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexofive.jasper");

        rAnexo.getAnexo5(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, dptoresponsable, mcpioresponsable, numinform, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void verAnexo6() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        //Instancia hacia la clase reportes Anexos
        ReporteAnexos rAnexo = new ReporteAnexos();

        String admin = nuevoAnexo6.getIdPaciente().getIdAdministradora().getRazonSocial();
        String codadmin = nuevoAnexo6.getIdPaciente().getIdAdministradora().getCodigoAdministradora();
        String dptopaciente = nuevoAnexo6.getIdPaciente().getDepartamento().getDescripcion();
        String mcpiopaciente = nuevoAnexo6.getIdPaciente().getMunicipio().getDescripcion();
        String dptoempresa = empresa.getCodDepartamento().getDescripcion();
        String mcpioempresa = empresa.getCodMunicipio().getDescripcion();
        String dptoresponsable = nuevoAnexo6.getIdDepartamento().getDescripcion();
        String mcpioresponsable = nuevoAnexo6.getIdMunicipio().getDescripcion();
        String numinform = numeroContrarremision;

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
        String ruta = servletContext.getRealPath("/anexos3047/reportes/anexosix.jasper");

        rAnexo.getAnexo5(admin, codadmin, mcpiopaciente, dptopaciente, dptoempresa, mcpioempresa, dptoresponsable, mcpioresponsable, numinform, ruta);
        FacesContext.getCurrentInstance().responseComplete();
    }

    public List<String> autocompletarServicio(String txt) {//retorna una lista con los diagnosticos que contengan el parametro txt
        if (txt != null && txt.length() > 2) {
            return facServicioFacade.autocompletarFacServicio(txt);
        } else {
            return null;
        }
    }

    public void anexarCups() {
        String codigocups = "";
        FacServicio nuevofacServicio = new FacServicio();
        Hc3047Anexo31 reque1 = new Hc3047Anexo31();

        if (cantidad != 0) {
            int index = cups1.indexOf(".");
            codigocups = cups1.substring(0, index - 1);
            int codServicio = Integer.parseInt(codigocups);
            nuevofacServicio = facServicioFacade.find(codServicio);
            nuevoAnexo31.setIdServicio(nuevofacServicio);
            nuevoAnexo31.setCantidad(cantidad);
            reque1.setIdServicio(nuevoAnexo31.getIdServicio());
            reque1.setCantidad(nuevoAnexo31.getCantidad());
            reque1.setId3047anexo31(id);
            this.listarequerimiento.add(reque1);
            id++;
//            nuevoAnexo31 = null;
            cantidad = 0.0;
            cups1 = "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "No puede dejar el campo Cantidad en 0.0"));
        }

    }

    public void anexarCups4() {
        String codigocups = "";
        FacServicio nuevofacServicio = new FacServicio();
        Hc3047Anexo41 reque1 = new Hc3047Anexo41();

        if (cantidad != 0) {
            int index = cups1.indexOf(".");
            codigocups = cups1.substring(0, index - 1);
            int codServicio = Integer.parseInt(codigocups);
            nuevofacServicio = facServicioFacade.find(codServicio);
            nuevoAnexo41.setIdServicio(nuevofacServicio);
            nuevoAnexo41.setCantidad(cantidad);
            reque1.setIdServicio(nuevoAnexo41.getIdServicio());
            reque1.setCantidad(nuevoAnexo41.getCantidad());
            reque1.setId3047anexo41(id1);
            this.listarequerimiento1.add(reque1);
            id1++;
//            nuevoAnexo31 = null;
            cantidad = 0.0;
            cups1 = "";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "No puede dejar el campo Cantidad en 0.0"));
        }

    }

    public void eliminar(Hc3047Anexo31 requerim) {
        listarequerimiento.remove(requerim.hashCode());
        int indice = 0;
        for (Hc3047Anexo31 requeri : listarequerimiento) {
            requeri.setId3047anexo31(indice);
            indice++;
            id = indice;
        }
        if (requerim.hashCode() == 0) {
            id = 0;
        }
    }

    public void eliminar1(Hc3047Anexo41 requerim) {
        listarequerimiento1.remove(requerim.hashCode());
        int indice = 0;
        for (Hc3047Anexo41 requeri : listarequerimiento1) {
            requeri.setId3047anexo41(indice);
            indice++;
            id1 = indice;
        }
        if (requerim.hashCode() == 0) {
            id1 = 0;
        }
    }

    public void actualizaparaModif() {
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        listaAnexo1Modif = hc3047Anexo1Facade.Anexos1xPaciente(pacienteseleccionado.getIdPaciente());
    }
    
    public void actualizaparaModif2() {
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        listaAnexo1Modif2 = hc3047Anexo2Facade.Anexos2xPaciente(pacienteseleccionado.getIdPaciente());
    }

    public void actualizaparaModif5() {
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        listaAnexo1Modif5 = hc3047Anexo5Facade.Anexos5xPaciente(pacienteseleccionado.getIdPaciente());
    }
    
    public void actualizaparaModif6() {
        pacienteseleccionado = cfgPacientesFacade.find(HistoriasMB.codPaciente);
        listaAnexo1Modif6 = hc3047Anexo6Facade.Anexos6xPaciente(pacienteseleccionado.getIdPaciente());
    }

    public void cargarAnexo1Modif() {
        nuevoregistro = 0;
        actualizarregistro = 1;
        imprimirregistro=true;
        this.nuevoAnexo1 = Anexo1Seleccionado;
        this.numeroInforme = nuevoAnexo1.getNumeroinforme();
        if (nuevoAnexo1.getPrimerapellido() != null) {
            primerapellido = true;
        }
        if (nuevoAnexo1.getSegundoapellido() != null) {
            segundoapellido = true;
        }
        if (nuevoAnexo1.getPrimernombre() != null) {
            primernombre = true;
        }
        if (nuevoAnexo1.getSegundonombre() != null) {
            segundonombre = true;
        }

        if (nuevoAnexo1.getTipoIdentificacion() != null) {
            tipodocumentoidentifica = true;
        }
        if (nuevoAnexo1.getNumeroidentificacion() != null) {
            numerodocumento = true;
        }
        if (nuevoAnexo1.getFechanacimiento() != null) {
            fechanacimiento = true;
        }
        RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarAnexo').hide();");

    }
    public void cargarAnexo2Modif() {
        nuevoregistro = 0;
        actualizarregistro = 1;
        imprimirregistro=true;
        this.nuevoAnexo2 = Anexo2Seleccionado;
        this.prestadorremitente = nuevoAnexo2.getReferido();
        if (prestadorremitente){
            pacienteremitido="SI";
        }else{
            pacienteremitido="NO";
        }
        if (nuevoAnexo2.getCei100()!=null){
            cei100=nuevoAnexo2.getCei100().getCodigoDiagnostico()+" - " + nuevoAnexo2.getCei100().getNombreDiagnostico();
        }
        if (nuevoAnexo2.getCei101()!=null){
            cei101=nuevoAnexo2.getCei101().getCodigoDiagnostico()+" - " + nuevoAnexo2.getCei101().getNombreDiagnostico();
        }
        if (nuevoAnexo2.getCei102()!=null){    
            cei102=nuevoAnexo2.getCei102().getCodigoDiagnostico()+" - " + nuevoAnexo2.getCei102().getNombreDiagnostico();
        }
        if (nuevoAnexo2.getCei103()!=null){
            cei103=nuevoAnexo2.getCei103().getCodigoDiagnostico()+" - " + nuevoAnexo2.getCei103().getNombreDiagnostico();
        }    
        this.numeroAtencion = nuevoAnexo2.getNumeroatencion();

        RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarAnexo2').hide();");
    }
    
    public void cargarAnexo5Modif() {
        nuevoregistro = 0;
        actualizarregistro = 1;
        imprimirregistro=true;
        this.nuevoAnexo5 = Anexo5Seleccionado;
        this.numeroRemision = nuevoAnexo5.getNumeroremision();

        RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarAnexo5').hide();");
    }
    
    public void cargarAnexo6Modif() {
        nuevoregistro = 0;
        actualizarregistro = 1;
        imprimirregistro=true;
        this.nuevoAnexo6 = Anexo6Seleccionado;
        this.numeroContrarremision = nuevoAnexo6.getNumerocontrarremision();

        RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarAnexo6').hide();");
    }
    
    public void cambiaTipoAnexo() {
        if (tipoanexoActual.getIdAnexos3047() == 1) {
            lupanexo1 = true;
            lupanexo2 = false;
            lupanexo3 = false;
            lupanexo4 = false;
            lupanexo5 = false;
            lupanexo6 = false;
        }
        if (tipoanexoActual.getIdAnexos3047() == 2) {
            lupanexo2 = true;
            lupanexo1 = false;
            lupanexo3 = false;
            lupanexo4 = false;
            lupanexo5 = false;
            lupanexo6 = false;
            nuevoregistro=1;
            actualizarregistro=0;  
        }
        if (tipoanexoActual.getIdAnexos3047() == 5) {
            lupanexo5 = true;
            lupanexo1 = false;
            lupanexo2 = false;
            lupanexo3 = false;
            lupanexo4 = false;
            lupanexo6 = false;
            nuevoregistro=1;
            actualizarregistro=0;  
        }
        if (tipoanexoActual.getIdAnexos3047() == 6) {
            lupanexo6 = true;
            lupanexo1 = false;
            lupanexo2 = false;
            lupanexo3 = false;
            lupanexo4 = false;
            lupanexo5 = false;
            nuevoregistro=1;
            actualizarregistro=0;  
        }

    }

    public void btnLimpiarFormulario() {
        if (tipoanexoActual.getIdAnexos3047() == 1) {
            limpiarFormulario1();
            
        }else if (tipoanexoActual.getIdAnexos3047() == 2) {
            limpiarFormulario2();
        }
    }

    public void limpiarFormulario1() {//reinicia todos los controles de un registro clinico
        imprimirregistro = false;
        if (actualizarregistro == 1) {
            nuevoregistro = 0;
        } else {
            nuevoregistro = 1;
        }
        nuevoAnexo1 = null;
        nuevoAnexo1 = new Hc3047Anexo1();
        primerapellido = false;
        segundoapellido = false;
        primernombre = false;
        segundonombre = false;
        tipodocumentoidentifica = false;
        numerodocumento = false;
        fechanacimiento = false;
        listaInconsistencias.clear();
        listaInconsistencias = clasificacionesFacade.buscarPorMaestro("Inconsistencia");
        listaTipoidentificacion.clear();
        listaTipoidentificacion = clasificacionesFacade.buscarPorMaestro("TipoIdentificacion");
        nuevoAnexo1.setFechadocumento(fechaReg);
    }
    
        public void limpiarFormulario2() {//reinicia todos los controles de un registro clinico
        imprimirregistro = false;
        if (actualizarregistro == 1) {
            nuevoregistro = 0;
        } else {
            nuevoregistro = 1;
        }
        nuevoAnexo2 = null;
        nuevoAnexo2 = new Hc3047Anexo2();
        cei100="";
        cei101="";
        cei102="";
        cei103="";
        pacienteremitido="";
        prestadorremitente = false;
        datosFormulario.setDato1(null);
        datosFormulario.setDato2(null);
        listaUsuarios.clear();
        listaUsuarios = cfgUsuariosFacade.buscarOrdenado();
        nuevoAnexo2.setFechadocumento(fechaReg);
    }


    public String getPacienteremitido() {
        return pacienteremitido;
    }

    public void setPacienteremitido(String pacienteremitido) {
        this.pacienteremitido = pacienteremitido;
    }

    public int getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(int tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public boolean isPrestadorremitente() {
        return prestadorremitente;
    }

    public void setPrestadorremitente(boolean prestadorremitente) {
        this.prestadorremitente = prestadorremitente;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public String getTiposerviciosolicita() {
        return tiposerviciosolicita;
    }

    public void setTiposerviciosolicita(String tiposerviciosolicita) {
        this.tiposerviciosolicita = tiposerviciosolicita;
    }

    public String getPrioridadservicio() {
        return prioridadservicio;
    }

    public void setPrioridadservicio(String prioridadservicio) {
        this.prioridadservicio = prioridadservicio;
    }

    public String getUbicacionpaciente() {
        return ubicacionpaciente;
    }

    public void setUbicacionpaciente(String ubicacionpaciente) {
        this.ubicacionpaciente = ubicacionpaciente;
    }

    public boolean isCoutam() {
        return coutam;
    }

    public void setCoutam(boolean coutam) {
        this.coutam = coutam;
    }

    public boolean isCopago() {
        return copago;
    }

    public void setCopago(boolean copago) {
        this.copago = copago;
    }

    public boolean isCoutar() {
        return coutar;
    }

    public void setCoutar(boolean coutar) {
        this.coutar = coutar;
    }

    public boolean isCoutaotro() {
        return coutaotro;
    }

    public void setCoutaotro(boolean coutaotro) {
        this.coutaotro = coutaotro;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Hc3047Anexo1 getNuevoAnexo1() {
        return nuevoAnexo1;
    }

    public void setNuevoAnexo1(Hc3047Anexo1 nuevoAnexo1) {
        this.nuevoAnexo1 = nuevoAnexo1;
    }

    public List<CfgClasificaciones> getListaInconsistencias() {
        return listaInconsistencias;
    }

    public void setListaInconsistencias(List<CfgClasificaciones> listaInconsistencias) {
        this.listaInconsistencias = listaInconsistencias;
    }

    public List<CfgClasificaciones> getListaTipoidentificacion() {
        return listaTipoidentificacion;
    }

    public void setListaTipoidentificacion(List<CfgClasificaciones> listaTipoidentificacion) {
        this.listaTipoidentificacion = listaTipoidentificacion;
    }

    public String getNumeroInforme() {
        return numeroInforme;
    }

    public void setNumeroInforme(String numeroInforme) {
        this.numeroInforme = numeroInforme;
    }

    public HcAnexos3047 getTipoanexoActual() {
        return tipoanexoActual;
    }

    public void setTipoanexoActual(HcAnexos3047 tipoanexoActual) {
        this.tipoanexoActual = tipoanexoActual;
    }

    public List<CfgUsuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<CfgUsuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public CfgDiagnostico getDiagnosticoppal() {
        return diagnosticoppal;
    }

    public void setDiagnosticoppal(CfgDiagnostico diagnosticoppal) {
        this.diagnosticoppal = diagnosticoppal;
    }

    public CfgDiagnostico getDiagnosticorelacion1() {
        return diagnosticorelacion1;
    }

    public void setDiagnosticorelacion1(CfgDiagnostico diagnosticorelacion1) {
        this.diagnosticorelacion1 = diagnosticorelacion1;
    }

    public CfgDiagnostico getDiagnosticorelacion2() {
        return diagnosticorelacion2;
    }

    public void setDiagnosticorelacion2(CfgDiagnostico diagnosticorelacion2) {
        this.diagnosticorelacion2 = diagnosticorelacion2;
    }

    public CfgDiagnostico getDiagnosticorelacion3() {
        return diagnosticorelacion3;
    }

    public void setDiagnosticorelacion3(CfgDiagnostico diagnosticorelacion3) {
        this.diagnosticorelacion3 = diagnosticorelacion3;
    }

    public Hc3047Anexo2 getNuevoAnexo2() {
        return nuevoAnexo2;
    }

    public void setNuevoAnexo2(Hc3047Anexo2 nuevoAnexo2) {
        this.nuevoAnexo2 = nuevoAnexo2;
    }

    public CfgClasificaciones getOrigenatencion() {
        return origenatencion;
    }

    public void setOrigenatencion(CfgClasificaciones origenatencion) {
        this.origenatencion = origenatencion;
    }

    public String getCei100() {
        return cei100;
    }

    public void setCei100(String cei100) {
        this.cei100 = cei100;
    }

    public String getCei101() {
        return cei101;
    }

    public void setCei101(String cei101) {
        this.cei101 = cei101;
    }

    public String getCei102() {
        return cei102;
    }

    public void setCei102(String cei102) {
        this.cei102 = cei102;
    }

    public String getCei103() {
        return cei103;
    }

    public void setCei103(String cei103) {
        this.cei103 = cei103;
    }

    public Hc3047Anexo3 getNuevoAnexo3() {
        return nuevoAnexo3;
    }

    public void setNuevoAnexo3(Hc3047Anexo3 nuevoAnexo3) {
        this.nuevoAnexo3 = nuevoAnexo3;
    }

    public List<CfgClasificaciones> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<CfgClasificaciones> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public Hc3047Anexo4 getNuevoAnexo4() {
        return nuevoAnexo4;
    }

    public void setNuevoAnexo4(Hc3047Anexo4 nuevoAnexo4) {
        this.nuevoAnexo4 = nuevoAnexo4;
    }

    public Hc3047Anexo41 getNuevoAnexo41() {
        return nuevoAnexo41;
    }

    public void setNuevoAnexo41(Hc3047Anexo41 nuevoAnexo41) {
        this.nuevoAnexo41 = nuevoAnexo41;
    }

    public Hc3047Anexo31 getNuevoAnexo31() {
        return nuevoAnexo31;
    }

    public void setNuevoAnexo31(Hc3047Anexo31 nuevoAnexo31) {
        this.nuevoAnexo31 = nuevoAnexo31;
    }

    public Hc3047Anexo5 getNuevoAnexo5() {
        return nuevoAnexo5;
    }

    public void setNuevoAnexo5(Hc3047Anexo5 nuevoAnexo5) {
        this.nuevoAnexo5 = nuevoAnexo5;
    }

    public Hc3047Anexo6 getNuevoAnexo6() {
        return nuevoAnexo6;
    }

    public void setNuevoAnexo6(Hc3047Anexo6 nuevoAnexo6) {
        this.nuevoAnexo6 = nuevoAnexo6;
    }

    public List<FacServicio> getListaCUPS() {
        return listaCUPS;
    }

    public void setListaCUPS(List<FacServicio> listaCUPS) {
        this.listaCUPS = listaCUPS;
    }

    public List<Hc3047Anexo31> getListarequerimiento() {
        return listarequerimiento;
    }

    public void setListarequerimiento(List<Hc3047Anexo31> listarequerimiento) {
        this.listarequerimiento = listarequerimiento;
    }

    public FacServicio getFacServ() {
        return facServ;
    }

    public void setFacServ(FacServicio facServ) {
        this.facServ = facServ;
    }

    public String getCups1() {
        return cups1;
    }

    public void setCups1(String cups1) {
        this.cups1 = cups1;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public List<Hc3047Anexo41> getListarequerimiento1() {
        return listarequerimiento1;
    }

    public void setListarequerimiento1(List<Hc3047Anexo41> listarequerimiento1) {
        this.listarequerimiento1 = listarequerimiento1;
    }

    public List<Hc3047Anexo1> getListaAnexo1Modif() {
        return listaAnexo1Modif;
    }

    public void setListaAnexo1Modif(List<Hc3047Anexo1> listaAnexo1Modif) {
        this.listaAnexo1Modif = listaAnexo1Modif;
    }

    public Hc3047Anexo1 getAnexo1Seleccionado() {
        return Anexo1Seleccionado;
    }

    public void setAnexo1Seleccionado(Hc3047Anexo1 Anexo1Seleccionado) {
        this.Anexo1Seleccionado = Anexo1Seleccionado;
    }

    public boolean isPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(boolean primerapellido) {
        this.primerapellido = primerapellido;
    }

    public boolean isSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(boolean segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public boolean isPrimernombre() {
        return primernombre;
    }

    public void setPrimernombre(boolean primernombre) {
        this.primernombre = primernombre;
    }

    public boolean isSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(boolean segundonombre) {
        this.segundonombre = segundonombre;
    }

    public boolean isTipodocumentoidentifica() {
        return tipodocumentoidentifica;
    }

    public void setTipodocumentoidentifica(boolean tipodocumentoidentifica) {
        this.tipodocumentoidentifica = tipodocumentoidentifica;
    }

    public boolean isNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(boolean numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public boolean isFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(boolean fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public int getActualizarregistro() {
        return actualizarregistro;
    }

    public void setActualizarregistro(int actualizarregistro) {
        this.actualizarregistro = actualizarregistro;
    }

    public boolean isInprimirregistro() {
        return imprimirregistro;
    }

    public void setInprimirregistro(boolean inprimirregistro) {
        this.imprimirregistro = inprimirregistro;
    }

    public boolean isLupanexo1() {
        return lupanexo1;
    }

    public void setLupanexo1(boolean lupanexo1) {
        this.lupanexo1 = lupanexo1;
    }

    public int getNuevoregistro() {
        return nuevoregistro;
    }

    public void setNuevoregistro(int nuevoregistro) {
        this.nuevoregistro = nuevoregistro;
    }

    public int getVisualizar() {
        return visualizar;
    }

    public void setVisualizar(int visualizar) {
        this.visualizar = visualizar;
    }

    public boolean isLupanexo2() {
        return lupanexo2;
    }

    public void setLupanexo2(boolean lupanexo2) {
        this.lupanexo2 = lupanexo2;
    }

    public boolean isLupanexo3() {
        return lupanexo3;
    }

    public void setLupanexo3(boolean lupanexo3) {
        this.lupanexo3 = lupanexo3;
    }

    public boolean isLupanexo4() {
        return lupanexo4;
    }

    public void setLupanexo4(boolean lupanexo4) {
        this.lupanexo4 = lupanexo4;
    }

    public boolean isLupanexo6() {
        return lupanexo6;
    }

    public void setLupanexo6(boolean lupanexo6) {
        this.lupanexo6 = lupanexo6;
    }

    public boolean isLupanexo5() {
        return lupanexo5;
    }

    public void setLupanexo5(boolean lupanexo5) {
        this.lupanexo5 = lupanexo5;
    }

    public List<Hc3047Anexo2> getListaAnexo1Modif2() {
        return listaAnexo1Modif2;
    }

    public void setListaAnexo1Modif2(List<Hc3047Anexo2> listaAnexo1Modif2) {
        this.listaAnexo1Modif2 = listaAnexo1Modif2;
    }

    public List<Hc3047Anexo3> getListaAnexo1Modif3() {
        return listaAnexo1Modif3;
    }

    public void setListaAnexo1Modif3(List<Hc3047Anexo3> listaAnexo1Modif3) {
        this.listaAnexo1Modif3 = listaAnexo1Modif3;
    }

    public List<Hc3047Anexo4> getListaAnexo1Modif4() {
        return listaAnexo1Modif4;
    }

    public void setListaAnexo1Modif4(List<Hc3047Anexo4> listaAnexo1Modif4) {
        this.listaAnexo1Modif4 = listaAnexo1Modif4;
    }

    public List<Hc3047Anexo5> getListaAnexo1Modif5() {
        return listaAnexo1Modif5;
    }

    public void setListaAnexo1Modif5(List<Hc3047Anexo5> listaAnexo1Modif5) {
        this.listaAnexo1Modif5 = listaAnexo1Modif5;
    }

    public List<Hc3047Anexo6> getListaAnexo1Modif6() {
        return listaAnexo1Modif6;
    }

    public void setListaAnexo1Modif6(List<Hc3047Anexo6> listaAnexo1Modif6) {
        this.listaAnexo1Modif6 = listaAnexo1Modif6;
    }

    public Hc3047Anexo2 getAnexo2Seleccionado() {
        return Anexo2Seleccionado;
    }

    public void setAnexo2Seleccionado(Hc3047Anexo2 Anexo2Seleccionado) {
        this.Anexo2Seleccionado = Anexo2Seleccionado;
    }

    public Hc3047Anexo3 getAnexo3Seleccionado() {
        return Anexo3Seleccionado;
    }

    public void setAnexo3Seleccionado(Hc3047Anexo3 Anexo3Seleccionado) {
        this.Anexo3Seleccionado = Anexo3Seleccionado;
    }

    public Hc3047Anexo4 getAnexo4Seleccionado() {
        return Anexo4Seleccionado;
    }

    public void setAnexo4Seleccionado(Hc3047Anexo4 Anexo4Seleccionado) {
        this.Anexo4Seleccionado = Anexo4Seleccionado;
    }

    public Hc3047Anexo5 getAnexo5Seleccionado() {
        return Anexo5Seleccionado;
    }

    public void setAnexo5Seleccionado(Hc3047Anexo5 Anexo5Seleccionado) {
        this.Anexo5Seleccionado = Anexo5Seleccionado;
    }

    public Hc3047Anexo6 getAnexo6Seleccionado() {
        return Anexo6Seleccionado;
    }

    public void setAnexo6Seleccionado(Hc3047Anexo6 Anexo6Seleccionado) {
        this.Anexo6Seleccionado = Anexo6Seleccionado;
    }

    
}
