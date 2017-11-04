/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.pyp_programas;

import beans.utilidades.FilaDataTable;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import modelo.entidades.CfgInsumo;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.FacServicio;
import modelo.entidades.PyPProgramaItem;
import modelo.entidades.PyPprograma;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.FacPaqueteFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.PyPProgramas;
import modelo.fachadas.PyPprogramaItems;
import org.primefaces.context.RequestContext;

/**
 *
 * @author cores
 */
@ManagedBean(name = "PyP_Programas")
@SessionScoped
public class pypbean extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------
    @EJB
    PyPProgramas ProgramaFacade;
    @EJB
    PyPprogramaItems ProgramaItems;
    @EJB
    FacServicioFacade servicioFacade;
    @EJB
    CfgMedicamentoFacade medicamentoFacade;
    @EJB
    CfgInsumoFacade insumoFacade;
    @EJB
    FacPaqueteFacade paqueteFacade;

    //---------------------------------------------------
    //-----------------ENTIDADES ------------------------
    //---------------------------------------------------
    public PyPProgramaItem programaSeleccionadoAsoc;
    public PyPprograma programaSeleccionado;
    public PyPprograma programaSeleccionadoTabla = new PyPprograma();
    public PyPProgramaItem servicioSeleccionadoTabla;
    public PyPProgramaItem medicamentoSeleccionadoTabla;
    public PyPProgramaItem insumoSeleccionadoTabla;

    FacServicio servicioSeleccionadoTablaBusqueda;
    public List<PyPProgramaItem> listaProgramas_asoc;
    public List<PyPprograma> listaProgramas;
    public List<PyPprograma> listaProgramas_2;
    public List<PyPProgramaItem> listaServiciosPrograma;
    public List<PyPProgramaItem> listaMedicamentosPrograma;
    public List<PyPProgramaItem> listaInsumosPrograma;

    public List<FacServicio> listaServicios;
    public List<CfgInsumo> listaInsumos;
    public List<CfgMedicamento> listaMedicamentos;

    public boolean mostrarTabView = false;

    public boolean mostrarSIM = false;

    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private List<FilaDataTable> listaPaquetesManual = new ArrayList<>();
    private List<FilaDataTable> listaServiciosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaInsumosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaMedicamentosManualFiltro = new ArrayList<>();
    private List<FilaDataTable> listaPaquetesManualFiltro = new ArrayList<>();
    private FilaDataTable servicioManualSeleccionado;
    private FilaDataTable insumoManualSeleccionado;
    private FilaDataTable medicamentoManualSeleccionado;
    private FilaDataTable paqueteManualSeleccionado;

    private String tituloTabManual = "Nuevo Programa";
    private String tituloTabPaquetes = "Paquetes (0)";
    private String tituloTabInsumos = "Insumos (0)";
    private String tituloTabMedicamentos = "Medicamentos (0)";
    private String tituloTabServicios = "Serivicios (0)";
    //datos paquete
    private String codigoPrograma = "";
    private String nombreManual = "";
    //nuevo servicio a adicionar
    public PyPProgramaItem item_servicios = new PyPProgramaItem();
    public PyPProgramaItem item_servicios_editar = new PyPProgramaItem();
    public PyPProgramaItem item_medicamentos = new PyPProgramaItem();
    public PyPProgramaItem item_medicamentos_editar = new PyPProgramaItem();
    public PyPProgramaItem item_insumos = new PyPProgramaItem();
    public PyPProgramaItem item_insumos_editar = new PyPProgramaItem();

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES  ----------------
    //---------------------------------------------------
    public pypbean() {
    }

    //---------------------------------------------------
    //-------------FUNCIONES MANUALES TARIFARIOS --------
    //---------------------------------------------------
    public void cargarProgramaDesdeTab(String idAdministradora) {//cargar contrato desde el tab externo de Administradoras
    }

    public void buscarServicio() {
        servicioSeleccionadoTablaBusqueda = null;
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide();");
        RequestContext.getCurrentInstance().execute("PF('wvTablaServicios').clearFilters(); PF('wvTablaServicios').getPaginator().setPage(0); PF('dialogoBuscarServicios').show();");
    }

    public void buscarServicioEditar() {
        servicioSeleccionadoTablaBusqueda = null;
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').hide();");
        RequestContext.getCurrentInstance().execute("PF('wvTablaServiciosEditar').clearFilters(); PF('wvTablaServiciosEditar').getPaginator().setPage(0); PF('dialogoBuscarServiciosEditar').show();");
    }

    public void cargarServicio() {
        if (servicioSeleccionadoTablaBusqueda == null) {
            imprimirMensaje("Error", "Se debe seleccionar un servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        item_servicios.setNombreActividad(servicioSeleccionadoTablaBusqueda.getIdServicio() + "-" + servicioSeleccionadoTablaBusqueda.getCodigoServicio() + " - " + servicioSeleccionadoTablaBusqueda.getNombreServicio());
        item_servicios.setIdServicio(servicioSeleccionadoTablaBusqueda.getIdServicio());
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarServicios').hide(); PF('wvTablaServicios').clearFilters(); PF('wvTablaServicios').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormDialogs5");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");
    }

    public void cargarServicioEditar() {
        if (servicioSeleccionadoTablaBusqueda == null) {
            imprimirMensaje("Error", "Se debe seleccionar un servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        item_servicios_editar.setNombreActividad(servicioSeleccionadoTablaBusqueda.getIdServicio() + "-" + servicioSeleccionadoTablaBusqueda.getCodigoServicio() + " - " + servicioSeleccionadoTablaBusqueda.getNombreServicio());
        item_servicios_editar.setIdServicio(servicioSeleccionadoTablaBusqueda.getIdServicio());
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarServiciosEditar').hide(); PF('wvTablaServiciosEditar').clearFilters(); PF('wvTablaServiciosEditar').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().update("IdFormDialogs1");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').show();");
    }

    public void llenarlistas() {
        /*
        CASC 20171103
        Esto es lo que hace que se retrase la carga de modulo
        listaServicios = servicioFacade.buscarTodosOrdenado();
        listaInsumos = insumoFacade.buscarOrdenado();
        listaMedicamentos = medicamentoFacade.buscarOrdenado();
        */
    }

    public void clickBtnNuevoPrograma() {
        limpiarFormularioProgramas();
        mostrarTabView = true;
        mostrarSIM = false;
    }

    public String genero_to_string(int genero) {
        switch (genero) {
            case 1:
                return " MASCULINO";
            case 2:
                return " FEMENINO";
            case 0:
                return " AMBOS";
            default:
                return "";
        }
    }

    public String edad_to_string(int edad_list) {
        switch (edad_list) {
            case 0:
                return " AÑO";
            case 1:
                return " MES";
            case 2:
                return " DIA";
            default:
                return "";
        }
    }

    public void limpiarFormularioProgramas() {
        programaSeleccionado = null;
        if (listaInsumosPrograma != null) {
            listaInsumosPrograma.clear();
            listaMedicamentosPrograma.clear();
            listaServiciosPrograma.clear();
        }
        tituloTabManual = "Nuevo Programa";
        tituloTabPaquetes = "Paquetes (0)";
        tituloTabInsumos = "Insumos (0)";
        tituloTabMedicamentos = "Medicamentos (0)";
        tituloTabServicios = "Serivicios (0)";
        codigoPrograma = "";
        nombreManual = "";
    }

    public void limpiar() {
        mostrarTabView = false;
        mostrarSIM = false;
    }

    public void buscarPrograma() {
        listaProgramas = ProgramaFacade.buscar_programas();
        RequestContext.getCurrentInstance().execute("PF('wvTablaManualesTarifarios').clearFilters(); PF('wvTablaManualesTarifarios').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').show();");
    }

    public void cargarPrograma(ActionEvent actionEvent) {
        if (programaSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormularioProgramas();
        programaSeleccionado = ProgramaFacade.find(programaSeleccionadoTabla.getIdPrograma());
        codigoPrograma = programaSeleccionado.getCodigoPrograma();
        nombreManual = programaSeleccionado.getNombrePrograma();
        tituloTabManual = "Datos Programa: " + nombreManual;
        mostrarTabView = true;
        mostrarSIM = true;
        recargarFilasTablas();
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').hide();");
        RequestContext.getCurrentInstance().update("IdFormManuales");
    }

    public void eliminarPrograma() {
        if (programaSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarManualTarifario').show();");
    }

    public void confirmarEliminarPrograma() {
        if (programaSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa", FacesMessage.SEVERITY_ERROR);
            return;
        } else {
            try {
                ProgramaFacade.remove(programaSeleccionadoTabla);
                listaProgramas = ProgramaFacade.buscar_programas();
                limpiarFormularioProgramas();
                mostrarTabView = false;
                RequestContext.getCurrentInstance().update("IdFormManuales");
                imprimirMensaje("Correcto", "El Programa ha sido eliminado", FacesMessage.SEVERITY_INFO);
            } catch (Exception e) {
                imprimirMensaje("Error", "El Programa que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
            }
        }
    }

    public void guardarPrograma() {
        if (validacionCampoVacio(codigoPrograma, "Código Programa")) {
            return;
        }
        if (validacionCampoVacio(nombreManual, "Nombre Programa")) {
            return;
        }
        if (programaSeleccionado == null) {
            guardarNuevoPrograma();
        } else {
            actualizarProgramaExistente();
        }
        mostrarTabView = false;
    }

    private void guardarNuevoPrograma() {
        List<PyPprograma> buscado = ProgramaFacade.buscar_programas_codigo(codigoPrograma);
        if (buscado.size() > 0) {
            RequestContext.getCurrentInstance().update("IdFormManuales");
            imprimirMensaje("Error", "Este programa de promoción y prevención ya existe, utilice otro codígo.", FacesMessage.SEVERITY_ERROR);
        } else {
            PyPprograma nuevoManual = new PyPprograma();
            nuevoManual.setCodigoPrograma(codigoPrograma);
            nuevoManual.setNombrePrograma(nombreManual);
            nuevoManual.setEstado(true);
            ProgramaFacade.create(nuevoManual);
            limpiarFormularioProgramas();
            RequestContext.getCurrentInstance().update("IdFormManuales");
            imprimirMensaje("Correcto", "El programa de promoción y prevención ha sido creado.", FacesMessage.SEVERITY_INFO);
        }
    }

    private void actualizarProgramaExistente() {//realiza la actualizacion del consultorio
        programaSeleccionado.setCodigoPrograma(codigoPrograma);
        programaSeleccionado.setNombrePrograma(nombreManual);
        ProgramaFacade.edit(programaSeleccionado);
        listaProgramas = ProgramaFacade.buscar_programas();
        RequestContext.getCurrentInstance().update("IdFormManuales");
        imprimirMensaje("Correcto", "Programa actualizado.", FacesMessage.SEVERITY_INFO);
    }

    public void recargarFilasTablas() {
        servicioManualSeleccionado = null;
        insumoManualSeleccionado = null;
        medicamentoManualSeleccionado = null;
        paqueteManualSeleccionado = null;
        listaPaquetesManual = new ArrayList<>();
        listaServiciosManualFiltro = new ArrayList<>();
        listaPaquetesManualFiltro = new ArrayList<>();
        listaInsumosManualFiltro = new ArrayList<>();
        listaMedicamentosManualFiltro = new ArrayList<>();
        int p = 0, m = 0, i = 0;
        if (programaSeleccionado != null) {
            listaServiciosPrograma = ProgramaItems.buscar_programas_servicios(programaSeleccionado.getIdPrograma());
            listaMedicamentosPrograma = ProgramaItems.buscar_programas_medicamentos(programaSeleccionado.getIdPrograma());
            listaInsumosPrograma = ProgramaItems.buscar_programas_insumos(programaSeleccionado.getIdPrograma());
            p = listaServiciosPrograma.size();
            m = listaMedicamentosPrograma.size();
            i = listaInsumosPrograma.size();
        }
        tituloTabInsumos = "Insumos (" + i + ")";
        tituloTabMedicamentos = "Medicamentos (" + m + ")";
        tituloTabServicios = "Serivicios (" + p + ")";
    }

    public void cargarDialogoAgregarServicio() {
        /*
        CASC 20171103
        Comentado no puede buscar todos los servicios porque son muchos
        if (listaServicios != null && !listaServicios.isEmpty()) {
            item_servicios.setIdServicio(listaServicios.get(0).getIdServicio());
            item_servicios.setNombreActividad(listaServicios.get(0).getCodigoCup() + " - " + listaServicios.get(0).getNombreServicio());
        } else {
            listaServicios = servicioFacade.buscarTodosOrdenado();
        } */
        RequestContext.getCurrentInstance().update("IdFormDialogs5:IdPanelAgregarServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').show();");
        //Mostrar y luego cargar la ventana
        System.out.println("Va a Cargar los servicios");
        if(listaServicios==null || listaServicios.isEmpty()){
            listaServicios = servicioFacade.buscarTodosOrdenado();
        }
        System.out.println("Termino");
    }

    public void cargarDialogoEditarServicio() {
        if (servicioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        item_servicios_editar.setNombreActividad(servicioSeleccionadoTabla.getNombreActividad());
        item_servicios_editar.setIdServicio(servicioSeleccionadoTabla.getIdServicio());
        RequestContext.getCurrentInstance().update("IdFormDialogs1:IdPanelEditandoServicio");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').show();");
    }

    public void agregarServicio() {
        try {
            if (validacionCampoVacio(item_servicios.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_servicios.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_servicios.getEdad_ini() > item_servicios.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    List<PyPProgramaItem> buscado = ProgramaItems.buscar_programas_idservicios(programaSeleccionadoTabla.getIdPrograma(), item_servicios.getIdServicio());
                    if (buscado.size() > 0) {
                        imprimirMensaje("Error", "Ya tiene agregado este servicio.", FacesMessage.SEVERITY_ERROR);
                    } else {
                        item_servicios.setIdPrograma(programaSeleccionadoTabla.getIdPrograma());
                        item_servicios.setIdInsumo(null);
                        item_servicios.setIdMedicamento(null);
                        ProgramaItems.create(item_servicios);
                        programaSeleccionadoTabla = ProgramaFacade.find(programaSeleccionadoTabla.getIdPrograma());
                        recargarFilasTablas();
                        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
                        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                        imprimirMensaje("Correcto", "Servicio agregado al programa.", FacesMessage.SEVERITY_INFO);
                    }
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void actualizarServicio() {
        try {
            if (validacionCampoVacio(item_servicios_editar.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_servicios_editar.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_servicios_editar.getEdad_ini() > item_servicios_editar.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    ProgramaItems.remove(servicioSeleccionadoTabla);
                    item_servicios_editar.setIdPrograma(servicioSeleccionadoTabla.getIdPrograma());
                    item_servicios_editar.setIdInsumo(null);
                    item_servicios_editar.setIdMedicamento(null);
                    ProgramaItems.create(item_servicios_editar);
                    programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
                    recargarFilasTablas();
                    RequestContext.getCurrentInstance().execute("PF('dialogoEditandoServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
                    RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                    imprimirMensaje("Correcto", "El servicio ha sido actualizado.", FacesMessage.SEVERITY_INFO);
                    item_servicios_editar = new PyPProgramaItem();
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void quitarServicio() {
        if (servicioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').show();");
    }

    public void confirmarQuitarServicio() {
        if (servicioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            servicioSeleccionadoTabla.setIdInsumo(null);
            servicioSeleccionadoTabla.setIdMedicamento(null);
            ProgramaItems.remove(servicioSeleccionadoTabla);
            programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarServicio').hide(); PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
            imprimirMensaje("Correcto", "El servicio ha sido eliminado del programa.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El programa que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-------------FUNCIONES INSUMOS --------------------
    //---------------------------------------------------
    public void cambiaInsumo() {
        if (item_insumos.getIdInsumo() != null) {
            for (CfgInsumo s : listaInsumos) {
                if (s.getIdInsumo().equals(item_insumos.getIdInsumo())) {
                    item_insumos.setNombreActividad(s.getIdInsumo() + "-" + s.getCodigoInsumo() + " - " + s.getNombreInsumo());
                }
            }
        }
        if (item_insumos_editar.getIdInsumo() != null) {
            for (CfgInsumo s : listaInsumos) {
                if (s.getIdInsumo().equals(item_insumos_editar.getIdInsumo())) {
                    item_insumos_editar.setNombreActividad(s.getIdInsumo() + "-" + s.getCodigoInsumo() + " - " + s.getNombreInsumo());
                }
            }
        }
    }

    public void calcularValoresInsumo() {
        if (validarNoVacio(item_insumos.getIdInsumo().toString())) {
            CfgInsumo p = insumoFacade.find(item_insumos.getIdInsumo());
        }
    }

    public void cargarDialogoAgregarInsumo() {
        /*listaInsumos = insumoFacade.buscarOrdenado();
        if (listaInsumos != null && !listaInsumos.isEmpty()) {
            item_insumos.setIdInsumo(listaInsumos.get(0).getIdInsumo());
            item_insumos.setNombreActividad(listaInsumos.get(0).getCodigoInsumo() + " - " + listaInsumos.get(0).getNombreInsumo());
        }
        */
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormDialogs5:IdPanelAgregarInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').show();");
        //Mostrar y luego cargar la ventana
        System.out.println("Va a Cargar los Insumos");
        if(listaInsumos==null || listaInsumos.isEmpty()){
            listaInsumos = insumoFacade.buscarOrdenado();
        }
        System.out.println("Termino");
    }

    public void cargarDialogoEditarInsumo() {
        listaInsumos = insumoFacade.buscarOrdenado();
        item_insumos_editar.setNombreActividad(listaInsumos.get(0).getCodigoInsumo() + " - " + listaInsumos.get(0).getNombreInsumo());
        item_insumos_editar.setIdInsumo(listaInsumos.get(0).getIdInsumo());
        if (insumoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        cambiaInsumo();
        RequestContext.getCurrentInstance().update("IdFormDialogs1:IdPanelEditandoInsumo");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoInsumo').show();");
    }

    public void agregarInsumo() {
        try {
            if (validacionCampoVacio(item_insumos.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_insumos.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_insumos.getEdad_ini() > item_insumos.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    List<PyPProgramaItem> buscado = ProgramaItems.buscar_programas_idinsumos(programaSeleccionadoTabla.getIdPrograma(), item_medicamentos.getIdInsumo());
                    if (buscado.size() > 0) {
                        imprimirMensaje("Error", "Ya tiene agregado este insumo.", FacesMessage.SEVERITY_ERROR);
                    } else {
                        item_insumos.setIdPrograma(programaSeleccionadoTabla.getIdPrograma());
                        ProgramaItems.create(item_insumos);
                        programaSeleccionadoTabla = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
                        recargarFilasTablas();
                        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarInsumo').hide(); PF('wvTablaInsumos').clearFilters(); PF('wvTablaInsumos').getPaginator().setPage(0);");
                        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                    }
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void actualizarInsumo() {
        try {
            if (validacionCampoVacio(item_insumos_editar.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_insumos_editar.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_insumos_editar.getEdad_ini() > item_insumos_editar.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    ProgramaItems.remove(insumoSeleccionadoTabla);
                    item_insumos_editar.setIdPrograma(insumoSeleccionadoTabla.getIdPrograma());
                    ProgramaItems.create(item_insumos_editar);
                    programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
                    recargarFilasTablas();
                    RequestContext.getCurrentInstance().execute("PF('dialogoEditandoInsumo').hide(); PF('wvTablaInsumos').clearFilters(); PF('wvTablaInsumos').getPaginator().setPage(0);");
                    RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                    imprimirMensaje("Correcto", "El insumo ha sido actualizado.", FacesMessage.SEVERITY_INFO);
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void quitarInsumo() {
        if (insumoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').show();");
    }

    public void confirmarQuitarInsumo() {
        if (insumoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún insumo", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            ProgramaItems.remove(insumoSeleccionadoTabla);
            programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarInsumo').hide(); PF('wvTablaInsumos').clearFilters(); PF('wvTablaInsumos').getPaginator().setPage(0);");
            imprimirMensaje("Correcto", "El insumo ha sido eliminado del programa.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El insumo que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }

    }

     public void cambiaMedicamento() {
        if (item_medicamentos.getIdMedicamento() != null) {
            for (CfgMedicamento s : listaMedicamentos) {
                if (s.getIdMedicamento().equals(item_medicamentos.getIdMedicamento())) {
                    item_medicamentos.setNombreActividad(s.getIdMedicamento() + "-" + s.getCodigoMedicamento() + " - " + s.getNombreMedicamento());
                }
            }
        }
        if (item_medicamentos_editar.getIdMedicamento() != null) {
            for (CfgMedicamento s : listaMedicamentos) {
                if (s.getIdMedicamento().equals(item_medicamentos_editar.getIdMedicamento())) {
                    item_medicamentos_editar.setNombreActividad(s.getIdMedicamento() + "-" + s.getCodigoMedicamento() + " - " + s.getNombreMedicamento());
                }
            }
        }
    }

    public void cargarDialogoAgregarMedicamento() {
        /*
        listaMedicamentos = medicamentoFacade.buscarOrdenado();
        if (listaMedicamentos != null && !listaMedicamentos.isEmpty()) {
            item_medicamentos.setIdMedicamento(listaMedicamentos.get(0).getIdMedicamento());
            item_medicamentos.setNombreActividad(listaMedicamentos.get(0).getCodigoMedicamento() + " - " + listaMedicamentos.get(0).getNombreMedicamento());
        }*/
        cambiaMedicamento();
        
        RequestContext.getCurrentInstance().update("IdFormDialogs5:IdPanelAgregarMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').show();");
        System.out.println("A cargar medicamentos");
        if(listaMedicamentos==null || listaMedicamentos.isEmpty()){
        listaMedicamentos = medicamentoFacade.buscarOrdenado();
        }
        System.out.println("termino");
    }

    public void cargarDialogoEditarMedicamento() {
        listaMedicamentos = medicamentoFacade.buscarOrdenado();
        item_medicamentos_editar.setNombreActividad(listaMedicamentos.get(0).getCodigoMedicamento() + " - " + listaMedicamentos.get(0).getNombreMedicamento());
        item_medicamentos_editar.setIdMedicamento(listaMedicamentos.get(0).getIdMedicamento());
        if (medicamentoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        cambiaMedicamento();
        RequestContext.getCurrentInstance().update("IdFormDialogs1:IdPanelEditandoMedicamento");
        RequestContext.getCurrentInstance().execute("PF('dialogoEditandoMedicamento').show();");
    }

    public void agregarMedicamento() {
        try {
            if (validacionCampoVacio(item_medicamentos.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_medicamentos.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_medicamentos.getEdad_ini() > item_medicamentos.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    List<PyPProgramaItem> buscado = ProgramaItems.buscar_programas_idmedicamentos(programaSeleccionadoTabla.getIdPrograma(), item_medicamentos.getIdMedicamento());
                    if (buscado.size() > 0) {
                        imprimirMensaje("Error", "Ya tiene agregado este medicamento.", FacesMessage.SEVERITY_ERROR);
                    } else {
                        item_medicamentos.setIdPrograma(programaSeleccionadoTabla.getIdPrograma());
                        ProgramaItems.create(item_medicamentos);
                        programaSeleccionadoTabla = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
                        recargarFilasTablas();
                        RequestContext.getCurrentInstance().execute("PF('dialogoAgregarMedicamento').hide(); PF('wvTablaMedicamentos').clearFilters(); PF('wvTablaMedicamentos').getPaginator().setPage(0);");
                        RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                        imprimirMensaje("Correcto", "Medicamento agregado al programa.", FacesMessage.SEVERITY_INFO);
                    }
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void actualizarMedicamento() {
        try {
            if (validacionCampoVacio(item_medicamentos_editar.getEdad_ini().toString(), "Edad Inicial") || validacionCampoVacio(item_medicamentos_editar.getEdad_fin().toString(), "Edad Final")) {

            } else {
                if (item_medicamentos_editar.getEdad_ini() > item_medicamentos_editar.getEdad_fin()) {
                    imprimirMensaje("Error", "La edad inicial no debe ser mayor a la final.", FacesMessage.SEVERITY_WARN);
                } else {
                    ProgramaItems.remove(medicamentoSeleccionadoTabla);
                    item_medicamentos_editar.setIdPrograma(medicamentoSeleccionadoTabla.getIdPrograma());
                    ProgramaItems.create(item_medicamentos_editar);
                    programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
                    recargarFilasTablas();
                    RequestContext.getCurrentInstance().execute("PF('dialogoEditandoMedicamento').hide(); PF('wvTablaMedicamentos').clearFilters(); PF('wvTablaMedicamentos').getPaginator().setPage(0);");
                    RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
                    imprimirMensaje("Correcto", "El medicamento ha sido actualizado.", FacesMessage.SEVERITY_INFO);
                }
            }
        } catch (Exception e) {
            imprimirMensaje("Faltan datos", "Debe difinir la edad inicial y la final.", FacesMessage.SEVERITY_WARN);
        }
    }

    public void quitarMedicamento() {
        if (medicamentoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').show();");
    }

    public void confirmarQuitarMedicamento() {
        if (medicamentoSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún medicamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            ProgramaItems.remove(medicamentoSeleccionadoTabla);
            programaSeleccionado = ProgramaFacade.find(programaSeleccionado.getIdPrograma());
            recargarFilasTablas();
            RequestContext.getCurrentInstance().update("IdFormManuales:IdTabView");
            RequestContext.getCurrentInstance().execute("PF('dialogoQuitarMedicamento').hide(); PF('wvTablaMedicamentos').clearFilters(); PF('wvTablaMedicamentos').getPaginator().setPage(0);");
            imprimirMensaje("Correcto", "El medicamento ha sido eliminado del programa.", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El medicamento que intenta eliminar esta siendo usado por el sistema", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
    public PyPprograma getProgramaSeleccionado() {
        return programaSeleccionado;
    }

    public void setProgramaSeleccionado(PyPprograma programaSeleccionado) {
        this.programaSeleccionado = programaSeleccionado;
    }

    public PyPprograma getProgramaSeleccionadoTabla() {
        return programaSeleccionadoTabla;
    }

    public void setProgramaSeleccionadoTabla(PyPprograma programaSeleccionadoTabla) {
        this.programaSeleccionadoTabla = programaSeleccionadoTabla;
    }

    public List<PyPprograma> getListaProgramas() {
        return listaProgramas;
    }

    public void setListaProgramas(List<PyPprograma> listaProgramas) {
        this.listaProgramas = listaProgramas;
    }

    public String getTituloTabManual() {
        return tituloTabManual;
    }

    public void setTituloTabManual(String tituloTabManual) {
        this.tituloTabManual = tituloTabManual;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }

    public String getNombreManual() {
        return nombreManual;
    }

    public void setNombreManual(String nombreManual) {
        this.nombreManual = nombreManual;
    }

    public List<PyPProgramaItem> getListaServiciosPrograma() {
        return listaServiciosPrograma;
    }

    public void setListaServiciosPrograma(List<PyPProgramaItem> listaServiciosPrograma) {
        this.listaServiciosPrograma = listaServiciosPrograma;
    }

    public List<PyPProgramaItem> getListaInsumosPrograma() {
        return listaInsumosPrograma;
    }

    public void setListaInsumosPrograma(List<PyPProgramaItem> listaInsumosPrograma) {
        this.listaInsumosPrograma = listaInsumosPrograma;
    }

    public List<PyPProgramaItem> getListaMedicamentosPrograma() {
        return listaMedicamentosPrograma;
    }

    public void setListaMedicamentosPrograma(List<PyPProgramaItem> listaMedicamentosPrograma) {
        this.listaMedicamentosPrograma = listaMedicamentosPrograma;
    }

    public List<FilaDataTable> getListaPaquetesManual() {
        return listaPaquetesManual;
    }

    public void setListaPaquetesManual(List<FilaDataTable> listaPaquetesManual) {
        this.listaPaquetesManual = listaPaquetesManual;
    }

    public FilaDataTable getServicioManualSeleccionado() {
        return servicioManualSeleccionado;
    }

    public void setServicioManualSeleccionado(FilaDataTable servicioManualSeleccionado) {
        this.servicioManualSeleccionado = servicioManualSeleccionado;
    }

    public FilaDataTable getInsumoManualSeleccionado() {
        return insumoManualSeleccionado;
    }

    public void setInsumoManualSeleccionado(FilaDataTable insumoManualSeleccionado) {
        this.insumoManualSeleccionado = insumoManualSeleccionado;
    }

    public FilaDataTable getMedicamentoManualSeleccionado() {
        return medicamentoManualSeleccionado;
    }

    public void setMedicamentoManualSeleccionado(FilaDataTable medicamentoManualSeleccionado) {
        this.medicamentoManualSeleccionado = medicamentoManualSeleccionado;
    }

    public FilaDataTable getPaqueteManualSeleccionado() {
        return paqueteManualSeleccionado;
    }

    public void setPaqueteManualSeleccionado(FilaDataTable paqueteManualSeleccionado) {
        this.paqueteManualSeleccionado = paqueteManualSeleccionado;
    }

    public String getTituloTabPaquetes() {
        return tituloTabPaquetes;
    }

    public void setTituloTabPaquetes(String tituloTabPaquetes) {
        this.tituloTabPaquetes = tituloTabPaquetes;
    }

    public String getTituloTabInsumos() {
        return tituloTabInsumos;
    }

    public void setTituloTabInsumos(String tituloTabInsumos) {
        this.tituloTabInsumos = tituloTabInsumos;
    }

    public String getTituloTabMedicamentos() {
        return tituloTabMedicamentos;
    }

    public void setTituloTabMedicamentos(String tituloTabMedicamentos) {
        this.tituloTabMedicamentos = tituloTabMedicamentos;
    }

    public String getTituloTabServicios() {
        return tituloTabServicios;
    }

    public void setTituloTabServicios(String tituloTabServicios) {
        this.tituloTabServicios = tituloTabServicios;
    }

    public List<FilaDataTable> getListaServiciosManualFiltro() {
        return listaServiciosManualFiltro;
    }

    public void setListaServiciosManualFiltro(List<FilaDataTable> listaServiciosManualFiltro) {
        this.listaServiciosManualFiltro = listaServiciosManualFiltro;
    }

    public List<FilaDataTable> getListaInsumosManualFiltro() {
        return listaInsumosManualFiltro;
    }

    public void setListaInsumosManualFiltro(List<FilaDataTable> listaInsumosManualFiltro) {
        this.listaInsumosManualFiltro = listaInsumosManualFiltro;
    }

    public List<FilaDataTable> getListaMedicamentosManualFiltro() {
        return listaMedicamentosManualFiltro;
    }

    public void setListaMedicamentosManualFiltro(List<FilaDataTable> listaMedicamentosManualFiltro) {
        this.listaMedicamentosManualFiltro = listaMedicamentosManualFiltro;
    }

    public List<FilaDataTable> getListaPaquetesManualFiltro() {
        return listaPaquetesManualFiltro;
    }

    public void setListaPaquetesManualFiltro(List<FilaDataTable> listaPaquetesManualFiltro) {
        this.listaPaquetesManualFiltro = listaPaquetesManualFiltro;
    }

    public List<FacServicio> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<FacServicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public List<CfgInsumo> getListaInsumos() {
        return listaInsumos;
    }

    public void setListaInsumos(List<CfgInsumo> listaInsumos) {
        this.listaInsumos = listaInsumos;
    }

    public List<CfgMedicamento> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<CfgMedicamento> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public boolean isMostrarTabView() {
        return mostrarTabView;
    }

    public void setMostrarTabView(boolean mostrarTabView) {
        this.mostrarTabView = mostrarTabView;
    }

    public boolean isMostrarSIM() {
        return mostrarSIM;
    }

    public void setMostrarSIM(boolean mostrarSIM) {
        this.mostrarSIM = mostrarSIM;
    }

    public PyPProgramas getProgramaFacade() {
        return ProgramaFacade;
    }

    public void setProgramaFacade(PyPProgramas ProgramaFacade) {
        this.ProgramaFacade = ProgramaFacade;
    }

    public PyPprogramaItems getProgramaItems() {
        return ProgramaItems;
    }

    public void setProgramaItems(PyPprogramaItems ProgramaItems) {
        this.ProgramaItems = ProgramaItems;
    }

    public FacServicioFacade getServicioFacade() {
        return servicioFacade;
    }

    public void setServicioFacade(FacServicioFacade servicioFacade) {
        this.servicioFacade = servicioFacade;
    }

    public CfgMedicamentoFacade getMedicamentoFacade() {
        return medicamentoFacade;
    }

    public void setMedicamentoFacade(CfgMedicamentoFacade medicamentoFacade) {
        this.medicamentoFacade = medicamentoFacade;
    }

    public CfgInsumoFacade getInsumoFacade() {
        return insumoFacade;
    }

    public void setInsumoFacade(CfgInsumoFacade insumoFacade) {
        this.insumoFacade = insumoFacade;
    }

    public FacPaqueteFacade getPaqueteFacade() {
        return paqueteFacade;
    }

    public void setPaqueteFacade(FacPaqueteFacade paqueteFacade) {
        this.paqueteFacade = paqueteFacade;
    }

    public PyPProgramaItem getServicioSeleccionadoTabla() {
        return servicioSeleccionadoTabla;
    }

    public void setServicioSeleccionadoTabla(PyPProgramaItem servicioSeleccionadoTabla) {
        this.servicioSeleccionadoTabla = servicioSeleccionadoTabla;
    }

    public PyPProgramaItem getMedicamentoSeleccionadoTabla() {
        return medicamentoSeleccionadoTabla;
    }

    public void setMedicamentoSeleccionadoTabla(PyPProgramaItem medicamentoSeleccionadoTabla) {
        this.medicamentoSeleccionadoTabla = medicamentoSeleccionadoTabla;
    }

    public PyPProgramaItem getInsumoSeleccionadoTabla() {
        return insumoSeleccionadoTabla;
    }

    public void setInsumoSeleccionadoTabla(PyPProgramaItem insumoSeleccionadoTabla) {
        this.insumoSeleccionadoTabla = insumoSeleccionadoTabla;
    }

    public PyPProgramaItem getItem_servicios() {
        return item_servicios;
    }

    public void setItem_servicios(PyPProgramaItem item_servicios) {
        this.item_servicios = item_servicios;
    }

    public PyPProgramaItem getItem_medicamentos() {
        return item_medicamentos;
    }

    public void setItem_medicamentos(PyPProgramaItem item_medicamentos) {
        this.item_medicamentos = item_medicamentos;
    }

    public PyPProgramaItem getItem_insumos() {
        return item_insumos;
    }

    public void setItem_insumos(PyPProgramaItem item_insumos) {
        this.item_insumos = item_insumos;
    }

    public PyPProgramaItem getItem_servicios_editar() {
        return item_servicios_editar;
    }

    public void setItem_servicios_editar(PyPProgramaItem item_servicios_editar) {
        this.item_servicios_editar = item_servicios_editar;
    }

    public PyPProgramaItem getItem_medicamentos_editar() {
        return item_medicamentos_editar;
    }

    public void setItem_medicamentos_editar(PyPProgramaItem item_medicamentos_editar) {
        this.item_medicamentos_editar = item_medicamentos_editar;
    }

    public PyPProgramaItem getItem_insumos_editar() {
        return item_insumos_editar;
    }

    public void setItem_insumos_editar(PyPProgramaItem item_insumos_editar) {
        this.item_insumos_editar = item_insumos_editar;
    }

    public List<PyPprograma> getListaProgramas_2() {
        return listaProgramas_2;
    }

    public void setListaProgramas_2(List<PyPprograma> listaProgramas_2) {
        this.listaProgramas_2 = listaProgramas_2;
    }

    public PyPProgramaItem getProgramaSeleccionadoAsoc() {
        return programaSeleccionadoAsoc;
    }

    public void setProgramaSeleccionadoAsoc(PyPProgramaItem programaSeleccionadoAsoc) {
        this.programaSeleccionadoAsoc = programaSeleccionadoAsoc;
    }

    public List<PyPProgramaItem> getListaProgramas_asoc() {
        return listaProgramas_asoc;
    }

    public void setListaProgramas_asoc(List<PyPProgramaItem> listaProgramas_asoc) {
        this.listaProgramas_asoc = listaProgramas_asoc;
    }

    public FacServicio getServicioSeleccionadoTablaBusqueda() {
        return servicioSeleccionadoTablaBusqueda;
    }

    public void setServicioSeleccionadoTablaBusqueda(FacServicio servicioSeleccionadoTablaBusqueda) {
        this.servicioSeleccionadoTablaBusqueda = servicioSeleccionadoTablaBusqueda;
    }

}
