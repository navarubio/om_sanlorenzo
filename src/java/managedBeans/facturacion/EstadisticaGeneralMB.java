/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.EstadisticaGeneral;
import beans.utilidades.MetodosGenerales;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgConfiguraciones;
import modelo.entidades.CfgEmpresa;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgSede;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgEmpresaFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.CfgSedeFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang3.SystemUtils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "estadisticaGeneralMB")
@ViewScoped
public class EstadisticaGeneralMB extends MetodosGenerales implements java.io.Serializable {

    @EJB
    private FacAdministradoraFacade administradoraFacade;
    @EJB
    CfgSedeFacade sedesFacade;
    @EJB
    private CfgClasificacionesFacade clasificacionFacade;
    @EJB
    private CfgUsuariosFacade usuarioFacade;
    @EJB
    private FacContratoFacade contratoFacade;
    @EJB
    private CfgPacientesFacade pacienteFacade;
    @EJB
    CfgEmpresaFacade empresaFacade;

    private List<SelectItem> listaFiltro;
    private List<FacAdministradora> listaAdministradora;
    private List<CfgClasificaciones> listaTipoIdentificacion;
    private List<CfgClasificaciones> listaEstrato;
    private List<CfgUsuarios> listaUsuarios;
    private List<EstadisticaGeneral> listaConsulta;

    private FacAdministradora administradora;
    private CfgPacientes paciente;
    private String idAdministradora;
    private String idAdministradoraSeleccionada;
    private CfgClasificaciones estrato;
    private CfgUsuarios usuario;
    private Date fechaInicial;
    private Date fechaFinal;
    private String filtroReporte;
    private String estadoFactura;
    private boolean renderFiltro1;
    private boolean renderFiltro2;
    private boolean renderFiltro3;
    private boolean renderFiltro4;
    private boolean renderFiltro5;
    private boolean renderFiltro6;
    private boolean renderFiltro7;
    private boolean renderBuscar;
    private boolean renderResultado;
    private boolean renderDetalladas;
    private boolean renderConsolidadas;
    private int id_contrato;
    private String centroDeAtencion;//identificador del centro de atencion
    private List<FacContrato> listaContratos;
    private FacAdministradora administradoraActual;
    private List<SelectItem> listaCentrosDeAtencion = new ArrayList<>();

    public EstadisticaGeneralMB() {
        listaTipoIdentificacion = new ArrayList();
        listaAdministradora = new ArrayList();
        listaEstrato = new ArrayList();
        listaFiltro = new ArrayList<>();
        listaUsuarios = new ArrayList<>();
//        listaFiltro.add(new SelectItem("1", "Administradora"));
//        listaFiltro.add(new SelectItem("2", "Paciente"));
//        listaFiltro.add(new SelectItem("3", "Fecha Facturación"));
//        listaFiltro.add(new SelectItem("4", "Estado"));
        //listaFiltro.add(new SelectItem("5", "Estrato"));
//        listaFiltro.add(new SelectItem("6", "Médico"));
        listaFiltro.add(new SelectItem("7", "Todos"));
        listaFiltro.add(new SelectItem("8", "Consolidadas"));
        listaFiltro.add(new SelectItem("9", "Detalladas"));
        paciente = new CfgPacientes();
        CfgClasificaciones tipoIdentificacion = new CfgClasificaciones();
        tipoIdentificacion.setId(0);
        paciente.setTipoIdentificacion(tipoIdentificacion);
        estrato = new CfgClasificaciones();
        usuario = new CfgUsuarios();
        administradora = new FacAdministradora();
        administradora.setIdAdministradora(0);
        idAdministradora = "0";
        renderBuscar = false;
        listaConsulta = new ArrayList();
        idAdministradoraSeleccionada = "0";
        renderDetalladas = false;
        renderConsolidadas = false;
    }

    @PostConstruct
    public void init() {
        listaAdministradora = administradoraFacade.findAll();
        cargarSedes();
    }
    
    public void cargarSedes() {
        listaCentrosDeAtencion = new ArrayList<>();
        List<CfgSede> lista = sedesFacade.buscarOrdenado();
        for (CfgSede sede : lista) {
            listaCentrosDeAtencion.add(new SelectItem(sede.getIdSede(), sede.getNombreSede()));
        }
    }

    public void cambiaAdministradora_programa() {
        listaContratos = new ArrayList<>();
        if (administradora != null) {
            administradoraActual = administradoraFacade.find(administradora.getIdAdministradora());
            listaContratos = administradoraActual.getFacContratoList();
        }
    }
    
    public void consultar() {
        String filtro = "";
        listaConsulta.clear();
        renderResultado = false;
        boolean consultar = true;
        int tipoIdentificacion = 0;
        String numeroIdentificacion = "";
        switch (filtroReporte) {
            case "1":
                if (!administradora.getIdAdministradora().equals(0)) {
                    filtro = " and fp.id_administradora =" + administradora.getIdAdministradora();
                    idAdministradoraSeleccionada = administradora.getIdAdministradora().toString();
                    listaAdministradora = listaAdministradora.isEmpty() ? administradoraFacade.findAll() : listaAdministradora;
                }
                break;
            case "2":
                if (paciente.getTipoIdentificacion().getId() != null) {
                    if (!paciente.getTipoIdentificacion().getId().equals(0)) {
                        if (paciente.getIdentificacion() != null) {
                            if (!paciente.getIdentificacion().equals("")) {
                                tipoIdentificacion = paciente.getTipoIdentificacion().getId();
                                numeroIdentificacion = paciente.getIdentificacion();
                                paciente = pacienteFacade.findPacienteByTipIden(paciente.getTipoIdentificacion().getId(), paciente.getIdentificacion());
                                filtro = " and fp.id_paciente =" + (paciente != null ? paciente.getIdPaciente() : "0");
                            } else {
                                imprimirMensaje("Error al consulta", "Digite número de identificación", FacesMessage.SEVERITY_INFO);
                                consultar = false;
                            }

                        } else {
                            consultar = false;
                            imprimirMensaje("Error al consulta", "Digite número de identificación", FacesMessage.SEVERITY_INFO);
                        }
                    } else {
                        imprimirMensaje("Error al consulta", "Seleccione un tipo de identificación", FacesMessage.SEVERITY_INFO);
                        consultar = false;
                        CfgClasificaciones c = new CfgClasificaciones();
                        c.setId(0);
                        paciente.setTipoIdentificacion(c);

                    }
                } else {
                    imprimirMensaje("Error al consulta", "Seleccione un tipo de identificación", FacesMessage.SEVERITY_INFO);
                    consultar = false;
                    CfgClasificaciones c = new CfgClasificaciones();
                    c.setId(0);
                    paciente.setTipoIdentificacion(c);
                }
                break;
            case "3":
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                filtro = " and  to_char(fp.fecha_sistema, 'yyyy-MM-dd') between '" + formato.format(fechaInicial) + "' and '" + formato.format(fechaFinal) + "' ";
                break;
            case "4":
                switch (estadoFactura) {
                    case "1":
                        filtro = " and fp.facturada_en_admi =TRUE ";
                        break;
                    case "2":
                        filtro = " and fp.facturada_en_admi =FALSE ";
                        break;
                    case "3":
                        filtro = " and fp.anulada = TRUE ";
                        break;
                    default:
                        break;
                }
                break;
            case "5":
                filtro = " and id =?1";
                break;
            case "6":
                filtro = "and ff.id_medico =" + (usuario.getIdUsuario() != null ? usuario.getIdUsuario() : "0");
                break;
            case "7":
                if (paciente.getTipoIdentificacion().getId() != null) {
                    if (!paciente.getTipoIdentificacion().getId().equals(0)) {
                        if (paciente.getIdentificacion() != null) {
                            if (!paciente.getIdentificacion().equals("")) {
                                tipoIdentificacion = paciente.getTipoIdentificacion().getId();
                                numeroIdentificacion = paciente.getIdentificacion();
                                paciente = pacienteFacade.findPacienteByTipIden(paciente.getTipoIdentificacion().getId(), paciente.getIdentificacion());
                                filtro += " and fp.id_paciente =" + (paciente != null ? paciente.getIdPaciente() : "0");
                            }
                        }
                    }
                }
                switch (estadoFactura) {
                    case "1":
                        filtro += " and fp.facturada_en_admi =TRUE ";
                        break;
                    case "2":
                        filtro += " and fp.facturada_en_admi =FALSE ";
                        break;
                    case "3":
                        filtro += " and fp.anulada = TRUE ";
                        break;
                    default:
                        break;
                }
                if(!centroDeAtencion.equals("0")){
                    filtro += " and sede.id_sede = "+centroDeAtencion;
                }
                if (!administradora.getIdAdministradora().equals(0)) {
                    filtro += " and fp.id_administradora =" + administradora.getIdAdministradora();
                    idAdministradoraSeleccionada = administradora.getIdAdministradora().toString();
                    listaAdministradora = listaAdministradora.isEmpty() ? administradoraFacade.findAll() : listaAdministradora;
                }
                if (id_contrato > 0) {
                    filtro += " and fp.id_contrato =" + id_contrato;
                } 
                formato = new SimpleDateFormat("yyyy-MM-dd");
                if (fechaInicial != null && fechaFinal != null) {
                    filtro += " and  to_char(fp.fecha_sistema, 'yyyy-MM-dd') between '" + formato.format(fechaInicial) + "' and '" + formato.format(fechaFinal) + "' ";
                }
                if (usuario.getIdUsuario() != null && usuario.getIdUsuario() != 0) {
                    filtro += " and ff.id_medico =" + (usuario.getIdUsuario() != null ? usuario.getIdUsuario() : "0");
                }
                break;            
            case "8":
                if (paciente.getTipoIdentificacion().getId() != null) {
                    if (!paciente.getTipoIdentificacion().getId().equals(0)) {
                        if (paciente.getIdentificacion() != null) {
                            if (!paciente.getIdentificacion().equals("")) {
                                tipoIdentificacion = paciente.getTipoIdentificacion().getId();
                                numeroIdentificacion = paciente.getIdentificacion();
                                paciente = pacienteFacade.findPacienteByTipIden(paciente.getTipoIdentificacion().getId(), paciente.getIdentificacion());
                                filtro += " and fp.id_paciente =" + (paciente != null ? paciente.getIdPaciente() : "0");
                            }
                        }
                    }
                }
                if(!centroDeAtencion.equals("0")){
                    filtro += " and sede.id_sede = "+centroDeAtencion;
                }
                if (!administradora.getIdAdministradora().equals(0)) {
                    filtro += " and fp.id_administradora =" + administradora.getIdAdministradora();
                    idAdministradoraSeleccionada = administradora.getIdAdministradora().toString();
                    listaAdministradora = listaAdministradora.isEmpty() ? administradoraFacade.findAll() : listaAdministradora;
                }
                if (id_contrato > 0) {
                    filtro += " and fp.id_contrato =" + id_contrato;
                } 
                formato = new SimpleDateFormat("yyyy-MM-dd");
                if (fechaInicial != null && fechaFinal != null) {
                    filtro += " and  to_char(fp.fecha_sistema, 'yyyy-MM-dd') between '" + formato.format(fechaInicial) + "' and '" + formato.format(fechaFinal) + "' ";
                }
                if (usuario.getIdUsuario() != null && usuario.getIdUsuario() != 0) {
                    filtro += " and ff.id_medico =" + (usuario.getIdUsuario() != null ? usuario.getIdUsuario() : "0");
                }
                break;
                case "9":
                if (paciente.getTipoIdentificacion().getId() != null) {
                    if (!paciente.getTipoIdentificacion().getId().equals(0)) {
                        if (paciente.getIdentificacion() != null) {
                            if (!paciente.getIdentificacion().equals("")) {
                                tipoIdentificacion = paciente.getTipoIdentificacion().getId();
                                numeroIdentificacion = paciente.getIdentificacion();
                                paciente = pacienteFacade.findPacienteByTipIden(paciente.getTipoIdentificacion().getId(), paciente.getIdentificacion());
                                filtro += " and fp.id_paciente =" + (paciente != null ? paciente.getIdPaciente() : "0");
                            }
                        }
                    }
                }
                if(!centroDeAtencion.equals("0")){
                    filtro += " and sede.id_sede = "+centroDeAtencion;
                }
                if (!administradora.getIdAdministradora().equals(0)) {
                    filtro += " and fp.id_administradora =" + administradora.getIdAdministradora();
                    idAdministradoraSeleccionada = administradora.getIdAdministradora().toString();
                    listaAdministradora = listaAdministradora.isEmpty() ? administradoraFacade.findAll() : listaAdministradora;
                }else{
                    imprimirMensaje("Faltan datos", "Seleccione una Administradora", FacesMessage.SEVERITY_INFO);
                    consultar = false;
                }
                if (id_contrato > 0) {
                    filtro += " and fp.id_contrato =" + id_contrato;
                } 
                formato = new SimpleDateFormat("yyyy-MM-dd");
                if (fechaInicial != null && fechaFinal != null) {
                    filtro += " and  to_char(fp.fecha_sistema, 'yyyy-MM-dd') between '" + formato.format(fechaInicial) + "' and '" + formato.format(fechaFinal) + "' ";
                }
                if (usuario.getIdUsuario() != null && usuario.getIdUsuario() != 0) {
                    filtro += " and ff.id_medico =" + (usuario.getIdUsuario() != null ? usuario.getIdUsuario() : "0");
                }
                break;
        }
        if (consultar) {
            if("8".equals(filtroReporte)||"9".equals(filtroReporte)){
                listaConsulta = contratoFacade.getEstadisticaGeneralConsolidada(filtro);
                if("8".equals(filtroReporte)){
                    renderDetalladas = false;
                    renderConsolidadas = true;
                }else{
                    renderDetalladas = true;
                    renderConsolidadas = true;
                }
            }else{
                listaConsulta = contratoFacade.getEstadisticaGeneral(filtro);
            }
            if (listaConsulta.size() > 0) {
                renderResultado = true;
            } else {
                imprimirMensaje("No hay registros", "No se encontró registros", FacesMessage.SEVERITY_INFO);
                if (filtroReporte.equals("2")) {
                    paciente = new CfgPacientes();
                    CfgClasificaciones c = new CfgClasificaciones();
                    c.setId(tipoIdentificacion);
                    paciente.setTipoIdentificacion(c);
                    paciente.setIdentificacion(numeroIdentificacion);
                }
            }
        }

    }

    public void validarFiltro() {
        limpiar();
        switch (filtroReporte) {
            case "1":
                renderFiltro1 = true;
                renderBuscar = true;
                break;
            case "2":
                renderFiltro2 = true;
                listaTipoIdentificacion = listaTipoIdentificacion.isEmpty() ? (clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString())) : listaTipoIdentificacion;
                paciente = new CfgPacientes();
                CfgClasificaciones tipoIdentificacion = new CfgClasificaciones();
                tipoIdentificacion.setId(0);
                paciente.setTipoIdentificacion(tipoIdentificacion);
                renderBuscar = true;
                break;
            case "3":
                fechaInicial = null;
                fechaFinal = null;
                renderFiltro3 = true;
                renderBuscar = true;
                break;
            case "4":
                renderFiltro4 = true;
                renderBuscar = true;
                break;
            case "5":
                listaEstrato = listaEstrato.isEmpty() ? (clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.Estrato.toString())) : listaEstrato;
                renderFiltro5 = true;
                renderBuscar = true;
                break;
            case "6":
                renderFiltro6 = true;
                renderBuscar = true;
                listaUsuarios = listaUsuarios.isEmpty() ? (usuarioFacade.findPrestadores()) : listaUsuarios;
                break;
            case "7":
                renderFiltro1 = true;
                renderFiltro2 = true;
                listaTipoIdentificacion = listaTipoIdentificacion.isEmpty() ? (clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString())) : listaTipoIdentificacion;
                paciente = new CfgPacientes();
                tipoIdentificacion = new CfgClasificaciones();
                tipoIdentificacion.setId(0);
                paciente.setTipoIdentificacion(tipoIdentificacion);
                fechaInicial = null;
                fechaFinal = null;
                renderFiltro3 = true;
                renderFiltro4 = true;
                renderFiltro6 = true;
                renderFiltro7 = true;
                renderBuscar = true;
                listaUsuarios = listaUsuarios.isEmpty() ? (usuarioFacade.findPrestadores()) : listaUsuarios;
                break;
            case "8":
                renderFiltro1 = true;
                renderFiltro2 = true;
                listaTipoIdentificacion = listaTipoIdentificacion.isEmpty() ? (clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString())) : listaTipoIdentificacion;
                paciente = new CfgPacientes();
                tipoIdentificacion = new CfgClasificaciones();
                tipoIdentificacion.setId(0);
                paciente.setTipoIdentificacion(tipoIdentificacion);
                fechaInicial = null;
                fechaFinal = null;
                renderFiltro3 = true;
                renderFiltro7 = true;
                renderBuscar = true;
                listaUsuarios = listaUsuarios.isEmpty() ? (usuarioFacade.findPrestadores()) : listaUsuarios;
                break;
            case "9":
                renderFiltro1 = true;
                renderFiltro2 = true;
                listaTipoIdentificacion = listaTipoIdentificacion.isEmpty() ? (clasificacionFacade.buscarPorMaestro(ClasificacionesEnum.TipoIdentificacion.toString())) : listaTipoIdentificacion;
                paciente = new CfgPacientes();
                tipoIdentificacion = new CfgClasificaciones();
                tipoIdentificacion.setId(0);
                paciente.setTipoIdentificacion(tipoIdentificacion);
                fechaInicial = null;
                fechaFinal = null;
                renderFiltro3 = true;
                renderFiltro7 = true;
                renderBuscar = true;
                listaUsuarios = listaUsuarios.isEmpty() ? (usuarioFacade.findPrestadores()) : listaUsuarios;
                break;
        }
    }
    
    //SUBIR AL TERMINAR EL CODIGO
    private List<EstructuraDetalladas> listaRegistrosFactura;
    private List<EstructuraConsolidadas> listaRegistrosFacturaConsolidadas;
    private LoginMB loginMB = new LoginMB();
    private CfgEmpresa empresaActual;
    public void generarDetalladasPDF(ActionEvent actionEvent){
        loginMB.inicializar();
        if (SystemUtils.IS_OS_WINDOWS) {
            loginMB.setRutaCarpetaImagenes("C:/imagenesOpenmedical/");
        } else {
            loginMB.setRutaCarpetaImagenes("/home/imagenesOpenmedical/");
        }
        empresaActual = empresaFacade.find(1);
        List<EstructuraItemsDetalladas> listaItemsFactura = new ArrayList<>();
        listaRegistrosFactura = new ArrayList<>();
        EstructuraDetalladas nuevaFactura = new EstructuraDetalladas();
        nuevaFactura.setEps("EPS: "+administradoraFacade.find(administradora.getIdAdministradora()).getRazonSocial());
        if(id_contrato == 0){
            nuevaFactura.setContrato("TODOS LOS CONTRATOS");
        }else{
            nuevaFactura.setContrato("CONTRATO: "+contratoFacade.find(id_contrato).getDescripcion());
        }
        nuevaFactura.setLogoEmpresa(
                loginMB.getRutaCarpetaImagenes() 
                        + empresaActual.getLogo().getUrlImagen().substring(0, empresaActual.getLogo().getUrlImagen().length()-8)+"-fac.png");
        nuevaFactura.setRegimenEmpresa(empresaActual.getRegimen());
        nuevaFactura.setNitEmpresa("NIT. " + empresaActual.getNumIdentificacion());
        nuevaFactura.setTitulo(empresaActual.getRazonSocial());
        nuevaFactura.setSubtitulo(empresaActual.getDireccion() + " - Tel1: " + empresaActual.getTelefono1() + " - Tel2: " + empresaActual.getTelefono2() + " - " + empresaActual.getWebsite());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat format = NumberFormat.getCurrencyInstance();
        int i = 1;
        double total_facturado = 0.0;
        for(EstadisticaGeneral est : listaConsulta){
            EstructuraItemsDetalladas eid = new EstructuraItemsDetalladas();
            eid.setN(i+"");
            eid.setFactura(est.getFactura());
            eid.setFecha(formatter.format(est.getFechaFactura()));
            eid.setDocumento(est.getTipoIdentificacion()+"-"+est.getIdentificacion());
            eid.setNombres(est.getNombre1()+" "+est.getNombre2()+" "+est.getApellido1()+" "+est.getApellido2());
            eid.setValor(format.format(est.getValorTotal()));
            listaItemsFactura.add(eid);
            total_facturado += est.getValorTotal();
            i++;
        }
        nuevaFactura.setTotal_facturas("Total de Facturas: "+(i-1));
        nuevaFactura.setTotal_general("Total Facturado: "+format.format(total_facturado));
        nuevaFactura.setListaItemsFactura(listaItemsFactura);
        listaRegistrosFactura.add(nuevaFactura);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosFactura);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/detallado.jasper"), new HashMap(), beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException | JRException ex) {
            Logger.getLogger(EstadisticaGeneralMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void generarConsolidadasPDF(ActionEvent actionEvent){
        loginMB.inicializar();
        if (SystemUtils.IS_OS_WINDOWS) {
            loginMB.setRutaCarpetaImagenes("C:/imagenesOpenmedical/");
        } else {
            loginMB.setRutaCarpetaImagenes("/home/imagenesOpenmedical/");
        }
        empresaActual = empresaFacade.find(1);
        List<EstructuraItemsConsolidadas> listaItemsFactura = new ArrayList<>();
        listaRegistrosFacturaConsolidadas = new ArrayList<>();
        EstructuraConsolidadas nuevaFactura = new EstructuraConsolidadas();
        nuevaFactura.setLogoEmpresa(loginMB.getRutaCarpetaImagenes() 
                        + empresaActual.getLogo().getUrlImagen().substring(0, empresaActual.getLogo().getUrlImagen().length()-8)+"-fac.png");
        nuevaFactura.setRegimenEmpresa(empresaActual.getRegimen());
        nuevaFactura.setNitEmpresa("NIT. " + empresaActual.getNumIdentificacion());
        nuevaFactura.setTitulo(empresaActual.getRazonSocial());
        nuevaFactura.setSubtitulo(empresaActual.getDireccion() + " - Tel1: " + empresaActual.getTelefono1() + " - Tel2: " + empresaActual.getTelefono2() + " - " + empresaActual.getWebsite());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        NumberFormat format = NumberFormat.getCurrencyInstance();
        int i = 1, j = 0, k = 0,c = 0;
        double total_facturado = 0.0, total = 0.0,total_contratos = 0.0;
        String contrat = "",administrador = "";
        for(EstadisticaGeneral est : listaConsulta){
            total_facturado+=est.getValorTotal();
            if(i == 1){
                administrador = est.getEmpresa();
                contrat = est.getContrato();
                EstructuraItemsConsolidadas eid = new EstructuraItemsConsolidadas();
                eid.setEps(administrador);
                listaItemsFactura.add(eid);
                total+=est.getValorTotal();
                j++;
                c++;
                System.out.println(i+"-"+administrador+" "+c);
            }else{  
                if(est.getContrato().equals(contrat)){
                    j++;
                    total+=est.getValorTotal(); 
                }else{
                    c++;
                    EstructuraItemsConsolidadas eid = new EstructuraItemsConsolidadas();
                    eid.setFacturas(j+"");
                    eid.setValor_total(format.format(total));
                    eid.setEps(contrat);
                    listaItemsFactura.add(eid);
                    total_facturado += est.getValorTotal();
                    if(est.getEmpresa().equals(administrador)){
                        total_contratos += total;
                        k+= j;
                    }else{
                        eid = new EstructuraItemsConsolidadas();
                        eid.setEps("TOTAL");
                        eid.setFacturas(k+"");
                        eid.setValor_total(format.format(total_contratos));
                        System.out.println(i+"-"+k+" "+total_contratos);
                        total_contratos = 0.0;
                        k = 0;
                    }
                    j = 0;
                    total = 0.0;
                    administrador = est.getEmpresa();
                    contrat = est.getContrato();
                    eid = new EstructuraItemsConsolidadas();
                    eid.setEps(administrador);
                    listaItemsFactura.add(eid);
                }
                if(c == 1 && listaConsulta.size() == i){
                    EstructuraItemsConsolidadas eid = new EstructuraItemsConsolidadas();
                    eid.setFacturas(j+"");
                    eid.setValor_total(format.format(total));
                    eid.setEps(contrat);
                    listaItemsFactura.add(eid);
                }
            }
            i++;
        } 
        nuevaFactura.setTotal_facturas("Total de Facturas: "+(i-1));
        nuevaFactura.setTotal_general("Total Facturado: "+format.format(total_facturado));
        nuevaFactura.setListaItemsFactura(listaItemsFactura);
        listaRegistrosFacturaConsolidadas.add(nuevaFactura);
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listaRegistrosFacturaConsolidadas);
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            httpServletResponse.setContentType("application/pdf");
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            JasperPrint jasperPrint = JasperFillManager.fillReport(servletContext.getRealPath("facturacion/reportes/consolidado.jasper"), new HashMap(), beanCollectionDataSource);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (IOException | JRException ex) {
            Logger.getLogger(EstadisticaGeneralMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //RECORDAR SUBIR LOS ATRIBUTOS
    
    public void exportarCSV() {
        FacesContext context = FacesContext.getCurrentInstance();
        String parametro1 = "";
        String parametro2 = "";
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            switch (filtroReporte) {
                case "1":
                    parametro1 = idAdministradoraSeleccionada;
                    break;
                case "2":
                    parametro1 = paciente.getIdPaciente().toString();
                    break;
                case "3":
                    parametro1 = formato.format(fechaInicial);
                    parametro2 = formato.format(fechaFinal);
                    break;
                case "4":
                    parametro1 = estadoFactura;
                    break;
                case "6":
                    parametro1 = usuario.getIdUsuario().toString();
                    break;
            }
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = "sumedica.jpasolutions.co";//PRODUCCION
//            String baseURL = context.getExternalContext().getRequestContextPath();
            String url = baseURL + "/EstadisticasGeneralesServlet?filtro=" + filtroReporte + "&param1=" + parametro1 + "&param2=" + parametro2;
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
            context.getExternalContext().redirect(encodeURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limpiar() {
        renderFiltro1 = false;
        renderFiltro2 = false;
        renderFiltro3 = false;
        renderFiltro4 = false;
        renderFiltro5 = false;
        renderFiltro6 = false;
        renderFiltro7 = false;
        renderBuscar = false;
        renderResultado = false;
        renderDetalladas = false;
        renderConsolidadas = false;
        paciente = new CfgPacientes();
        CfgClasificaciones tipoIdentificacion = new CfgClasificaciones();
        tipoIdentificacion.setId(0);
        paciente.setTipoIdentificacion(tipoIdentificacion);
        estrato = new CfgClasificaciones();
        usuario = new CfgUsuarios();
        idAdministradora = "0";
        idAdministradoraSeleccionada = "0";
        administradora = new FacAdministradora();
        administradora.setIdAdministradora(0);
    }

    public List<SelectItem> getListaFiltro() {
        return listaFiltro;
    }

    public void setListaFiltro(List<SelectItem> listaFiltro) {
        this.listaFiltro = listaFiltro;
    }

    public String getFiltroReporte() {
        return filtroReporte;
    }

    public void setFiltroReporte(String filtroReporte) {
        this.filtroReporte = filtroReporte;
    }

    public boolean isRenderFiltro1() {
        return renderFiltro1;
    }

    public void setRenderFiltro1(boolean renderFiltro1) {
        this.renderFiltro1 = renderFiltro1;
    }

    public boolean isRenderFiltro2() {
        return renderFiltro2;
    }

    public void setRenderFiltro2(boolean renderFiltro2) {
        this.renderFiltro2 = renderFiltro2;
    }

    public boolean isRenderFiltro3() {
        return renderFiltro3;
    }

    public void setRenderFiltro3(boolean renderFiltro3) {
        this.renderFiltro3 = renderFiltro3;
    }

    public boolean isRenderFiltro4() {
        return renderFiltro4;
    }

    public void setRenderFiltro4(boolean renderFiltro4) {
        this.renderFiltro4 = renderFiltro4;
    }

    public boolean isRenderFiltro5() {
        return renderFiltro5;
    }

    public void setRenderFiltro5(boolean renderFiltro5) {
        this.renderFiltro5 = renderFiltro5;
    }

    public boolean isRenderFiltro6() {
        return renderFiltro6;
    }

    public void setRenderFiltro6(boolean renderFiltro6) {
        this.renderFiltro6 = renderFiltro6;
    }

    public List<FacAdministradora> getListaAdministradora() {
        return listaAdministradora;
    }

    public void setListaAdministradora(List<FacAdministradora> listaAdministradora) {
        this.listaAdministradora = listaAdministradora;
    }

    public boolean isRenderBuscar() {
        return renderBuscar;
    }

    public void setRenderBuscar(boolean renderBuscar) {
        this.renderBuscar = renderBuscar;
    }

    public List<CfgClasificaciones> getListaTipoIdentificacion() {
        return listaTipoIdentificacion;
    }

    public void setListaTipoIdentificacion(List<CfgClasificaciones> listaTipoIdentificacion) {
        this.listaTipoIdentificacion = listaTipoIdentificacion;
    }

    public String getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(String idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public CfgPacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(CfgPacientes paciente) {
        this.paciente = paciente;
    }

    public List<CfgClasificaciones> getListaEstrato() {
        return listaEstrato;
    }

    public void setListaEstrato(List<CfgClasificaciones> listaEstrato) {
        this.listaEstrato = listaEstrato;
    }

    public List<CfgUsuarios> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<CfgUsuarios> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public CfgClasificaciones getEstrato() {
        return estrato;
    }

    public void setEstrato(CfgClasificaciones estrato) {
        this.estrato = estrato;
    }

    public CfgUsuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(CfgUsuarios usuario) {
        this.usuario = usuario;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public List<EstadisticaGeneral> getListaConsulta() {
        return listaConsulta;
    }

    public void setListaConsulta(List<EstadisticaGeneral> listaConsulta) {
        this.listaConsulta = listaConsulta;
    }

    public boolean isRenderResultado() {
        return renderResultado;
    }

    public void setRenderResultado(boolean renderResultado) {
        this.renderResultado = renderResultado;
    }

    public FacAdministradora getAdministradora() {
        return administradora;
    }

    public void setAdministradora(FacAdministradora administradora) {
        this.administradora = administradora;
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }

    public FacAdministradora getAdministradoraActual() {
        return administradoraActual;
    }

    public void setAdministradoraActual(FacAdministradora administradoraActual) {
        this.administradoraActual = administradoraActual;
    }

    public boolean isRenderFiltro7() {
        return renderFiltro7;
    }

    public void setRenderFiltro7(boolean renderFiltro7) {
        this.renderFiltro7 = renderFiltro7;
    }

    public String getIdAdministradoraSeleccionada() {
        return idAdministradoraSeleccionada;
    }

    public void setIdAdministradoraSeleccionada(String idAdministradoraSeleccionada) {
        this.idAdministradoraSeleccionada = idAdministradoraSeleccionada;
    }

    public String getCentroDeAtencion() {
        return centroDeAtencion;
    }

    public void setCentroDeAtencion(String centroDeAtencion) {
        this.centroDeAtencion = centroDeAtencion;
    }

    public List<SelectItem> getListaCentrosDeAtencion() {
        return listaCentrosDeAtencion;
    }

    public void setListaCentrosDeAtencion(List<SelectItem> listaCentrosDeAtencion) {
        this.listaCentrosDeAtencion = listaCentrosDeAtencion;
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public void setLoginMB(LoginMB loginMB) {
        this.loginMB = loginMB;
    }

    public boolean isRenderDetalladas() {
        return renderDetalladas;
    }

    public void setRenderDetalladas(boolean renderDetalladas) {
        this.renderDetalladas = renderDetalladas;
    }

    public boolean isRenderConsolidadas() {
        return renderConsolidadas;
    }

    public void setRenderConsolidadas(boolean renderConsolidadas) {
        this.renderConsolidadas = renderConsolidadas;
    }
    
    

}
