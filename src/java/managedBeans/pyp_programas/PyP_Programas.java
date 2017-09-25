/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.pyp_programas;

import beans.utilidades.FilaDataTable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import beans.utilidades.MetodosGenerales;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgInsumo;
import modelo.entidades.CfgMedicamento;
import modelo.entidades.FacAdministradora;
import modelo.entidades.FacContrato;
import modelo.entidades.FacServicio; 
import modelo.entidades.PyPProgramaItem;
import modelo.entidades.PyPprograma;
import modelo.entidades.PyPprogramaAsoc;
import modelo.fachadas.CfgInsumoFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.CfgMedicamentoFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.PyPProgramas;
import modelo.fachadas.PyPprogramAsoc;
import modelo.fachadas.PyPprogramaItems;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Cores
 */
@ManagedBean(name = "pyp_programas")
@SessionScoped 
public class PyP_Programas extends MetodosGenerales implements Serializable {

    //informacion programa
    private int id_programa;
    private int codigo_programa;
    private String descripcion;
    public String actividad;
    private String finalidad;
    private int genero;
    private int edad_ini;
    private int edad_fin;
    private String dosis;
    private String frecuencia;
    private List<SelectItem> listaprograma;
    private int id_administradora;
    private int id_contrato;
    private double meta;
    
    //VARIABLE
    private List<programas> programas;
    private FilaDataTable programa_seleccionado = null;
    private programas pro = new programas();
    
    //servicios
    private List<SelectItem> listaServicios;
    private int idServicio;
    private String des_servicio;
    
    //medicamento
    private int idMedicamento;
    private List<SelectItem> listaMedicamentos;
    
    //insumo
    private int idInsumo;
    private List<SelectItem> listaInsumo;
    //sede
    private int sede;
    
    //Administradoras
    private List<SelectItem> listaAdministradoras;
    private FacAdministradora administradoraActual;
    //Contratos
    private List<FacContrato> listaContratos;
    private String idContrato;
    private FacContrato contratoTodos;
    
    @EJB
    FacAdministradoraFacade administradoraFacade;
    
    @EJB
    PyPProgramas PyPprogramasFacade;
    
    @EJB
    PyPprogramAsoc PyPprogramaAsocFacade;
     
    @EJB
    PyPprogramaItems PyPprogramasItemFacade;
    
    @EJB
    FacServicioFacade facServicioFacade;
    
    @EJB
    CfgMedicamentoFacade MedicamentoFacade;
    
    @EJB
    CfgInsumoFacade InsumoFacade;

    public PyP_Programas() {
    }

    @PostConstruct
    private void inicializar() {
        
        programas = new ArrayList<programas>();
        listaAdministradoras = new ArrayList<>();
        listaServicios = new ArrayList();
        listaMedicamentos = new ArrayList();
        listaInsumo = new ArrayList();
        listaprograma = new ArrayList();
        crearlistaProgramas();
        crearlistaServicios();
        crearlistaMedicamentos();
        crearlistaInsumo();
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
    }
    
    public void cambiaAdministradora_programa() {
        listaContratos = new ArrayList<>();
        if (!validarNoVacio(id_administradora+"")) {
            imprimirMensaje("Error", "Se debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
            RequestContext.getCurrentInstance().execute("remoteCommand();");
            return;
        }
        administradoraActual = administradoraFacade.find(Integer.parseInt(id_administradora+""));
        listaContratos = administradoraActual.getFacContratoList();
        RequestContext.getCurrentInstance().update("formPrograma_asoc");
        RequestContext.getCurrentInstance().execute("remoteCommand();");
    }

    public void crearlistaAdministradoras() {
        List<FacAdministradora> progra = administradoraFacade.buscarOrdenado();
        for (FacAdministradora programa : progra) {
            getListaAdministradoras().add(new SelectItem(programa.getIdAdministradora(), programa.getCodigoAdministradora() + " - " + programa.getRazonSocial()));
        }
    }

    public void crearlistaProgramas() { 
        List<PyPprograma> progra = PyPprogramasFacade.buscar_programas();
        for (PyPprograma programa : progra) {
            getListaprograma().add(new SelectItem(programa.getIdPrograma(), programa.getCodigoPrograma() + " - " + programa.getNombrePrograma()));
        }
    }

    public void crearlistaServicios() { 
        List<FacServicio> servicios = facServicioFacade.buscarActivos();
        for (FacServicio servicio : servicios) {
            getListaServicios().add(new SelectItem(servicio.getIdServicio(), servicio.getCodigoServicio() + " - " + servicio.getNombreServicio()));
        }
    }
    
    public void crearlistaMedicamentos() { 
        List<CfgMedicamento> medicamentos = MedicamentoFacade.buscarOrdenado();
        for (CfgMedicamento medicamento : medicamentos) {
            getListaMedicamentos().add(new SelectItem(medicamento.getIdMedicamento(), medicamento.getCodigoMedicamento() + " - " + medicamento.getNombreMedicamento()));
        }
    }
    
    public void crearlistaInsumo() { 
        List<CfgInsumo> insumos = InsumoFacade.buscarOrdenado();
        for (CfgInsumo insumo : insumos) {
            getListaInsumo().add(new SelectItem(insumo.getIdInsumo(), insumo.getCodigoInsumo() + " - " + insumo.getNombreInsumo()));
        }
    }
    
    public void agregar_fila() {
        if (validacionCampoVacio(idServicio+"", "Servicio")) {
            return;
        }
        if (validacionCampoVacio(actividad, "Actividad")) {
            return;
        }
        if (validacionCampoVacio(finalidad, "Finalidad")) {
            return;
        }
        if (validacionCampoVacio(edad_ini+"", "Edad")) {
            return;
        }
        if (validacionCampoVacio(edad_fin+"", "Edad Final")) {
            return;
        }
        if (validacionCampoVacio(dosis, "Dosis")) {
            return;
        }
        if (validacionCampoVacio(frecuencia, "Frecuencia")) {
            return;
        } 
        pro.setTipo("Servicio");
        pro.setId(idServicio);
        pro.setActividad(actividad);
        pro.setCups("");
        pro.setDosis(dosis);
        pro.setEdad_fin(edad_fin);
        pro.setEdad_fin(edad_fin);
        pro.setFinalidad(finalidad);
        pro.setFrecuencia(frecuencia);
        pro.setGenero(genero);
        pro.setRips("");
        this.programas.add(pro);
        pro = new programas();
    }
    
    public void agregar_fila_medicamento() {
        if (validacionCampoVacio(idMedicamento+"", "Medicamento")) {
            return;
        }
        if (validacionCampoVacio(actividad, "Actividad")) {
            return;
        }
        if (validacionCampoVacio(finalidad, "Finalidad")) {
            return;
        }
        if (validacionCampoVacio(edad_ini+"", "Edad")) {
            return;
        }
        if (validacionCampoVacio(edad_fin+"", "Edad Final")) {
            return;
        }
        if (validacionCampoVacio(dosis, "Dosis")) {
            return;
        }
        if (validacionCampoVacio(frecuencia, "Frecuencia")) {
            return;
        } 
        pro.setTipo("Medicamento");
        pro.setId(idMedicamento);
        pro.setActividad(actividad);
        pro.setCups("");
        pro.setDosis(dosis);
        pro.setEdad_fin(edad_fin);
        pro.setEdad_fin(edad_fin);
        pro.setFinalidad(finalidad);
        pro.setFrecuencia(frecuencia);
        pro.setGenero(genero);
        pro.setRips("");
        this.programas.add(pro);
        pro = new programas();
    }
    
    public void agregar_fila_insumo() {
        if (validacionCampoVacio(idInsumo+"", "Insumo")) {
            return;
        }
        if (validacionCampoVacio(actividad, "Actividad")) {
            return;
        }
        if (validacionCampoVacio(finalidad, "Finalidad")) {
            return;
        }
        if (validacionCampoVacio(edad_ini+"", "Edad")) {
            return;
        }
        if (validacionCampoVacio(edad_fin+"", "Edad Final")) {
            return;
        }
        if (validacionCampoVacio(dosis, "Dosis")) {
            return;
        }
        if (validacionCampoVacio(frecuencia, "Frecuencia")) {
            return;
        } 
        pro.setTipo("Insumo");
        pro.setId(idInsumo);
        pro.setActividad(actividad);
        pro.setCups("");
        pro.setDosis(dosis);
        pro.setEdad_fin(edad_fin);
        pro.setEdad_fin(edad_fin);
        pro.setFinalidad(finalidad);
        pro.setFrecuencia(frecuencia);
        pro.setGenero(genero);
        pro.setRips("");
        this.programas.add(pro);
        pro = new programas();
    }
    
    public void crear_programa(){
        if (validacionCampoVacio(codigo_programa+"", "Programa")) {
            return;
        }else 
        if (validacionCampoVacio(descripcion+"", "Nombre del Programa")) {
            return;
        }else{
            PyPprograma pro = new PyPprograma();
            pro.setCodigoPrograma(codigo_programa+"");
            pro.setIdPrograma(null);
            pro.setNombrePrograma(descripcion);
            pro.setEstado(true);
            PyPprogramasFacade.create(pro);
            for (int i = 0; i < programas.size(); i++) {
                PyPProgramaItem proi = new PyPProgramaItem();
                proi.setDosis(programas.get(i).getDosis());
                proi.setEdad_fin(programas.get(i).getEdad_fin());
                proi.setEdad_ini(programas.get(i).getEdad_ini());
                proi.setFinalidad(programas.get(i).getFinalidad());
                proi.setFrecuencia(programas.get(i).getFrecuencia());
                proi.setGenero(programas.get(i).getGenero());
                switch (programas.get(i).getTipo()) {
                    case "Servicio":
                        proi.setIdInsumo(null);
                        proi.setIdMedicamento(null);
                        proi.setIdServicio(programas.get(i).getId());
                        break;
                    case "Medicamento":
                        proi.setIdInsumo(null);
                        proi.setIdMedicamento(programas.get(i).getId());
                        proi.setIdServicio(null);
                        break;
                    default:
                        proi.setIdInsumo(programas.get(i).getId());
                        proi.setIdMedicamento(null);
                        proi.setIdServicio(null);
                        break;
                }
                proi.setIdProgramaItems(null);
                proi.setNombreActividad(actividad);
                proi.setIdPrograma(pro.getIdPrograma());
                PyPprogramasItemFacade.create(proi);
            }
            this.codigo_programa = 0;
            this.descripcion = "";
            this.programas = new ArrayList<programas>();
            imprimirMensaje("Completado", "Programa Registrado", FacesMessage.SEVERITY_INFO);
        }
    }
    
    public void asociar_programa(){
        if (validacionCampoVacio(id_programa+"", "Programa")) {
            return;
        }else 
        if (validacionCampoVacio(id_administradora+"", "Administradora")) {
            return;
        }else 
        if (validacionCampoVacio(id_contrato+"", "Contrato")) {
            return;
        }else 
        if (validacionCampoVacio(meta+"", "Meta")) {
            return;
        }else{
            PyPprogramaAsoc pro = new PyPprogramaAsoc();
            pro.setIdAdministradora(id_administradora);
            pro.setIdContrato(id_contrato);
            pro.setIdPrograma(id_programa);
            pro.setIdProgramaAsoc(null);
            pro.setMeta(meta);
//            List<PyPprogramaAsoc> buscado =  PyPprogramaAsocFacade.buscar_programas_codigo(id_programa, id_administradora);
//            System.out.println(buscado);
//            System.out.println(buscado.size());
//            if(buscado.size() > 0){
//                imprimirMensaje("Error", "Este programa ya esta asosiado a la administradora seleccionada.", FacesMessage.SEVERITY_ERROR);
//                return;
//            }   
            PyPprogramaAsocFacade.create(pro); 
            this.meta = 0; 
            imprimirMensaje("Completado", "Programa Asociado", FacesMessage.SEVERITY_INFO);
        }
    }

    /**
     * @return the idServicio
     */
    public int getIdServicio() {
        return idServicio;
    }

    /**
     * @param idServicio the idServicio to set
     */
    public void setIdServicio(int idServicio) {
        this.idServicio = idServicio;
    }

    public List<programas> getProgramas() {
        return programas;
    }

    public void setProgramas(List<programas> programas) {
        this.programas = programas;
    }
    
    public List<SelectItem> getListaInsumo() {
        return listaInsumo;
    }

    public void setListaInsumo(List<SelectItem> listaInsumo) {
        this.listaInsumo = listaInsumo;
    }

    public CfgInsumoFacade getInsumoFacade() {
        return InsumoFacade;
    }

    public void setInsumoFacade(CfgInsumoFacade InsumoFacade) {
        this.InsumoFacade = InsumoFacade;
    }
    
    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFinalidad() {
        return finalidad;
    }

    public void setFinalidad(String finalidad) {
        this.finalidad = finalidad;
    }

    public int getGenero() {
        return genero;
    }

    public void setGenero(int genero) {
        this.genero = genero;
    }

    public int getEdad_ini() {
        return edad_ini;
    }

    public void setEdad_ini(int edad_ini) {
        this.edad_ini = edad_ini;
    }

    public int getEdad_fin() {
        return edad_fin;
    }

    public void setEdad_fin(int edad_fin) {
        this.edad_fin = edad_fin;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDes_servicio() {
        return des_servicio;
    }

    public void setDes_servicio(String des_servicio) {
        this.des_servicio = des_servicio;
    }

    public int getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(int idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public List<SelectItem> getListaMedicamentos() {
        return listaMedicamentos;
    }

    public void setListaMedicamentos(List<SelectItem> listaMedicamentos) {
        this.listaMedicamentos = listaMedicamentos;
    }

    public int getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    public CfgMedicamentoFacade getMedicamentoFacade() {
        return MedicamentoFacade;
    }

    public void setMedicamentoFacade(CfgMedicamentoFacade MedicamentoFacade) {
        this.MedicamentoFacade = MedicamentoFacade;
    }

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public int getCodigo_programa() {
        return codigo_programa;
    }

    public void setCodigo_programa(int codigo_programa) {
        this.codigo_programa = codigo_programa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getServicio() {
        return des_servicio;
    }

    public void setServicio(String servicio) {
        this.des_servicio = servicio;
    }

    public FacServicioFacade getFacServicioFacade() {
        return facServicioFacade;
    }

    public void setFacServicioFacade(FacServicioFacade facServicioFacade) {
        this.facServicioFacade = facServicioFacade;
    }

    public int getSede() {
        return sede;
    }

    public void setSede(int sede) {
        this.sede = sede;
    }

    public FilaDataTable getPrograma_seleccionado() {
        return programa_seleccionado;
    }

    public void setPrograma_seleccionado(FilaDataTable programa_seleccionado) {
        this.programa_seleccionado = programa_seleccionado;
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

    public int getId_administradora() {
        return id_administradora;
    }

    public void setId_administradora(int id_administradora) {
        this.id_administradora = id_administradora;
    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public List<SelectItem> getListaAdministradoras() {
        return listaAdministradoras;
    }

    public void setListaAdministradoras(List<SelectItem> listaAdministradoras) {
        this.listaAdministradoras = listaAdministradoras;
    }

    public List<FacContrato> getListaContratos() {
        return listaContratos;
    }

    public void setListaContratos(List<FacContrato> listaContratos) {
        this.listaContratos = listaContratos;
    }
    
    

}
