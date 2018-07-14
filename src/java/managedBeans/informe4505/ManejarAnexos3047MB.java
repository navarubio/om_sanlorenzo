/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informe4505;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import managedBeans.historias.DatosFormularioHistoria;
import managedBeans.historias.HistoriasMB;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgDiagnostico;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.Hc3047Anexo1;
import modelo.entidades.Hc3047Anexo2;
import modelo.entidades.Hc3047Anexo3;
import modelo.entidades.Hc3047Anexo31;
import modelo.entidades.Hc3047Anexo4;
import modelo.entidades.Hc3047Anexo41;
import modelo.entidades.Hc3047Anexo5;
import modelo.entidades.Hc3047Anexo6;
import modelo.entidades.HcAnexos3047;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoFacade;
import modelo.fachadas.CfgDiagnosticoPrincipalFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.Hc3047Anexo1Facade;
import modelo.fachadas.Hc3047Anexo2Facade;
import modelo.fachadas.Hc3047Anexo3Facade;
import modelo.fachadas.Hc3047Anexo3_1Facade;
import modelo.fachadas.Hc3047Anexo4Facade;
import modelo.fachadas.Hc3047Anexo4_1Facade;
import modelo.fachadas.Hc3047Anexo5Facade;
import modelo.fachadas.Hc3047Anexo6Facade;
import modelo.fachadas.HcAnexos3047Facade;

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
    Hc3047Anexo3_1Facade  hc3047Anexo3_1Facade;
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

    private String pacienteremitido = "";
    private int tipomovimiento = 0;
    private int consecutivo = 0;
    private boolean prestadorremitente = false;
    private List<SelectItem> listaMunicipios;
    private List<CfgClasificaciones> listaEspecialidades;
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
    private boolean haypacienteseleccionado = false;
    private CfgPacientes pacienteseleccionado;
    private Date fechaReg = new Date();
    private Hc3047Anexo1 nuevoAnexo1 = new Hc3047Anexo1();
    private Hc3047Anexo2 nuevoAnexo2 = new Hc3047Anexo2();
    private Hc3047Anexo3 nuevoAnexo3 = new Hc3047Anexo3();
    private Hc3047Anexo31 nuevoAnexo31 = new Hc3047Anexo31();
    private Hc3047Anexo4 nuevoAnexo4 = new Hc3047Anexo4();
    private Hc3047Anexo41 nuevoAnexo41 = new Hc3047Anexo41();
    private Hc3047Anexo5 nuevoAnexo5 = new Hc3047Anexo5();
    private Hc3047Anexo6 nuevoAnexo6 = new Hc3047Anexo6();
    
    
    private HcAnexos3047 tipoanexoActual = new HcAnexos3047();
    private List<CfgClasificaciones> listaInconsistencias = null;
    private List<CfgClasificaciones> listaTipoidentificacion = null;
    private List<CfgUsuarios> listaUsuarios = null;


//    private HistoriasMB historiasMB;
    private String cei100 = null;
    private String cei101 = null;
    private String cei102 = null;
    private String cei103 = null;

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
        listaEspecialidades=cfgClasificacionesFacade.buscarPorMaestro("Especialidad");

        nuevoAnexo1.setFechadocumento(fechaReg);
        nuevoAnexo2.setFechadocumento(fechaReg);
        nuevoAnexo2.setFechaingreso(fechaReg);
        nuevoAnexo3.setFechadocumento(fechaReg);
        nuevoAnexo4.setFechadocumento(fechaReg);
        nuevoAnexo5.setFechadocumento(fechaReg);
        nuevoAnexo6.setFechadocumento(fechaReg);
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
        generarNumeroInforme();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro " + numeroInforme);

        try {
            nuevoAnexo1.setNumeroinforme(numeroInforme);

//            nuevoAnexo1.setIdPaciente(pacienteseleccionado);
            hc3047Anexo1Facade.create(nuevoAnexo1);
            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo fue almacenado con el Nro " + numeroInforme));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo"));
        }

    }

    public void guardarAnexo2() {//guardar un nuevo registro clinico        
        int idSede = 1;
        generarNumeroAtencion();
        System.out.println(fechaReg);
        System.out.println("Iniciando el guardado del registro Anexo2 " + numeroAtencion);
        String codigo0 = "";
        String codigo1 = "";
        String codigo2 = "";
        String codigo3 = "";

        try {
            nuevoAnexo2.setNumeroatencion(numeroAtencion);
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
            if (prestadorremitente = true) {
                nuevoAnexo2.setIddepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)));
                municipio = datosFormulario.getDato2().toString();
                nuevoAnexo2.setIdmunicipio(clasificacionesFacade.find(Integer.parseInt(municipio)));
            }
            nuevoAnexo2.setNumeroatencion(numeroAtencion);
            hc3047Anexo2Facade.create(nuevoAnexo2);
            anexoActual.setConsecutivo(consecutivo);
            hcAnexos3047Facade.edit(anexoActual);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Su Anexo2 fue almacenado con el Nro " + numeroAtencion));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Aviso", "Error al Grabar Anexo2"));
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

        try {
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

        try {
            nuevoAnexo4.setNumeroautorizacion(numeroAutorizacion);
            hc3047Anexo4Facade.create(nuevoAnexo4);
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

        try {
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

        try {
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

    
    
}
