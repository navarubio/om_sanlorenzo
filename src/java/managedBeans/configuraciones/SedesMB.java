package managedBeans.configuraciones;

import beans.enumeradores.ClasificacionesEnum;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.CfgCama;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgHabitacion;
import modelo.entidades.CfgSede;
import modelo.fachadas.AbstractFacade;
import modelo.fachadas.CfgCamaFacade;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgHabitacionFacade;
import modelo.fachadas.CfgSedeFacade;
import org.primefaces.context.RequestContext;

@ManagedBean(name = "sedesMB")
@SessionScoped
public class SedesMB extends MetodosGenerales implements Serializable {

    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------    
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;

    @EJB
    CfgSedeFacade sedeFacade;

    @EJB
    CfgHabitacionFacade cfgHabitacionFacade;

    @EJB
    CfgCamaFacade cfgCamaFacade;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private List<CfgSede> listaSedes;
    private CfgSede sedeSeleccionada;
    private CfgSede sedeSeleccionadaTabla;
    private List<CfgHabitacion> listaHabitaciones;
    private CfgHabitacion habitacion;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private List<SelectItem> listaMunicipiosSede;
    private String tituloTabSede = "Datos Nueva Sede";
    private String nombreSede = "";
    private String codigoSede = "";
    private String departamentoSede = "";
    private String municipioSede = "";
    private String encargadoSede = "";
    private String direccionSede = "";
    private String telefono1Sede = "";
    private String telefono2Sede = "";
    private AplicacionGeneralMB aplicacionGeneralMB;
    private String numeroHabitacion;
    private String observacion;
    private String numeroCama;
    private String observacionCama;
    private Boolean isUrgencia;

    //---------------------------------------------------
    //------------- FUNCIONES INICIALES -----------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
        listaSedes = sedeFacade.buscarOrdenado();
    }

    public SedesMB() {
    }

    //---------------------------------------------------
    //-----------------FUNCIONES SEDES ------------------
    //---------------------------------------------------    
    public void limpiarSede() {
        listaSedes = sedeFacade.buscarOrdenado();
        tituloTabSede = "Datos Nueva Sede";
        sedeSeleccionada = null;
        listaMunicipiosSede = new ArrayList<>();
        codigoSede = "";
        nombreSede = "";
        departamentoSede = "";
        municipioSede = "";
        encargadoSede = "";
        direccionSede = "";
        telefono1Sede = "";
        telefono2Sede = "";
        this.getListaHabitaciones().clear();
        this.setIsUrgencia(false);
    }

    public void buscarSede() {
        listaSedes = sedeFacade.buscarOrdenado();
        RequestContext.getCurrentInstance().execute("PF('wvTablaSedes').clearFilters(); PF('wvTablaSedes').getPaginator().setPage(0); PF('dialogoBuscarSede').show();");
    }

    public void cargarSede() {
        if (sedeSeleccionadaTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar una sede de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarSede();
        sedeSeleccionada = sedeFacade.find(sedeSeleccionadaTabla.getIdSede());
        codigoSede = sedeSeleccionada.getCodigoSede();
        nombreSede = sedeSeleccionada.getNombreSede();
        tituloTabSede = "Datos Sede: " + nombreSede;
        if (sedeSeleccionada.getDepartamento() != null) {
            departamentoSede = sedeSeleccionada.getDepartamento().getId().toString();
            cargarMunicipiosSede();
        }
        if (sedeSeleccionada.getMunicipio() != null) {
            municipioSede = sedeSeleccionada.getMunicipio().getId().toString();
        }
        this.getListaHabitaciones().addAll(sedeSeleccionada.getCfgHabitacionList());
        encargadoSede = sedeSeleccionada.getEncargado();
        direccionSede = sedeSeleccionada.getDireccion();
        telefono1Sede = sedeSeleccionada.getTelefono1();
        telefono2Sede = sedeSeleccionada.getTelefono2();
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarSede').hide(); PF('wvTablaSedes').clearFilters(); PF('wvTablaSedes').getPaginator().setPage(0);");
    }

    public void eliminarSede() {
        if (sedeSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna sede", FacesMessage.SEVERITY_ERROR);
            return;
        }
        RequestContext.getCurrentInstance().execute("PF('dialogoEliminarSede').show();");

    }

    public void confirmarEliminarSede() {
        if (sedeSeleccionada == null) {
            imprimirMensaje("Error", "No se ha cargado ninguna sede", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            sedeFacade.remove(sedeSeleccionada);
            limpiarSede();
            RequestContext.getCurrentInstance().update("IdFormSedes");
            aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
            imprimirMensaje("Correcto", "La sede ha sido eliminada", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "la sede que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminada.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public void guardarSede() {
        if (validacionCampoVacio(codigoSede, "Código sede")) {
            return;
        }
        if (validacionCampoVacio(nombreSede, "Nombre sede")) {
            return;
        }
        if (departamentoSede == null || departamentoSede.isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar un departamento", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (municipioSede == null || municipioSede.isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar un municipio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (direccionSede == null || direccionSede.isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una dirección", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (sedeSeleccionada == null) {
            guardarNuevaSede();
        } else {
            if (this.getListaHabitaciones().isEmpty()) {
                imprimirMensaje("Error", "Debe asociar al menos una  habitación.", FacesMessage.SEVERITY_ERROR);
                return;
            }
            Boolean habitacionSinCama = Boolean.FALSE;
            Iterator<CfgHabitacion> listaHabitacionesConsultorio = this.getListaHabitaciones().iterator();
            while (listaHabitacionesConsultorio.hasNext()) {
                CfgHabitacion habitacionSeleccionada = listaHabitacionesConsultorio.next();
                if (habitacionSeleccionada.getCfgCamaList() == null
                        || habitacionSeleccionada.getCfgCamaList().isEmpty()) {
                    habitacionSinCama = Boolean.TRUE;
                    break;
                }
            }
            if (habitacionSinCama.equals(Boolean.TRUE)) {
                imprimirMensaje("Error", "Debe asociar al menos una cama a la habitación .", FacesMessage.SEVERITY_ERROR);
                return;
            }
            actualizarSedeExistente();
        }
    }

    private void guardarNuevaSede() {
        CfgSede nuevaSede = new CfgSede();
        nuevaSede.setDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)));
        nuevaSede.setMunicipio(clasificacionesFacade.find(Integer.parseInt(municipioSede)));
        nuevaSede.setCodigoSede(codigoSede);
        nuevaSede.setNombreSede(nombreSede);
        nuevaSede.setEncargado(encargadoSede);
        nuevaSede.setDireccion(direccionSede);
        nuevaSede.setTelefono1(telefono1Sede);
        nuevaSede.setTelefono2(telefono2Sede);
        sedeFacade.create(nuevaSede);
        limpiarSede();
        CfgSede sede = sedeFacade.getSede(nuevaSede.getCodigoSede(), nuevaSede.getNombreSede());
        System.out.println("managedBeans" + sede.getCodigoSede());
        sedeSeleccionada = sede;
        codigoSede = sede.getCodigoSede();
        nombreSede = sede.getNombreSede();
        encargadoSede = sede.getEncargado();
        direccionSede = sede.getDireccion();
        telefono1Sede = sede.getTelefono1();
        telefono2Sede = sede.getTelefono2();
        tituloTabSede = "Datos Sede: " + nombreSede;
        if (sede.getDepartamento() != null) {
            departamentoSede = sede.getDepartamento().getId().toString();
            cargarMunicipiosSede();
        }
        if (sede.getMunicipio() != null) {
            municipioSede = sede.getMunicipio().getId().toString();
        }
        sedeSeleccionada.setCfgHabitacionList(new ArrayList<CfgHabitacion>());
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
        RequestContext.getCurrentInstance().update("IdFormSedes");
        imprimirMensaje("Correcto", "La sede se ha creado, proceda a asociarle las habitaciones y camas", FacesMessage.SEVERITY_INFO);
    }

    private void actualizarSedeExistente() {
        sedeSeleccionada.setCodigoSede(codigoSede);
        sedeSeleccionada.setNombreSede(nombreSede);
        sedeSeleccionada.setDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)));
        sedeSeleccionada.setMunicipio(clasificacionesFacade.find(Integer.parseInt(municipioSede)));

        for (CfgHabitacion habitacion : this.getListaHabitaciones()) {
            sedeSeleccionada.getCfgHabitacionList().add(habitacion);
            System.out.println("kk");
        }
        sedeSeleccionada.setEncargado(encargadoSede);
        sedeSeleccionada.setDireccion(direccionSede);
        sedeSeleccionada.setTelefono1(telefono1Sede);
        sedeSeleccionada.setTelefono2(telefono2Sede);
        sedeFacade.edit(sedeSeleccionada);
        limpiarSede();
        aplicacionGeneralMB.cargarClasificacion(ClasificacionesEnum.Sedes);
        RequestContext.getCurrentInstance().update("IdFormSedes");
        imprimirMensaje("Correcto", "La sede ha sido actualizada.", FacesMessage.SEVERITY_INFO);
    }

    public void cargarMunicipiosSede() {
        listaMunicipiosSede = new ArrayList<>();
        if (departamentoSede != null && departamentoSede.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamentoSede)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipiosSede.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
    }

    public void borrarCama(CfgCama cama) {
        Iterator<CfgHabitacion> listaHabitacionesConsultorio = this.getListaHabitaciones().iterator();
        while (listaHabitacionesConsultorio.hasNext()) {
            CfgHabitacion habitacionSeleccionada = listaHabitacionesConsultorio.next();
            if (habitacionSeleccionada.getNumeroHabitacion().equals(cama.getCfgHabitacion().getNumeroHabitacion())) {
                Iterator<CfgCama> listaCamas = habitacionSeleccionada.getCfgCamaList().iterator();
                while (listaCamas.hasNext()) {
                    CfgCama camaSeleccionada = listaCamas.next();
                    if (camaSeleccionada.getNumeroCama().equals(cama.getNumeroCama())) {
                        listaCamas.remove();
                        this.getCfgCamaFacade().remove(camaSeleccionada);
                        break;
                    }
                }
                break;
            }
        }
        imprimirMensaje("Información", "Cama borrada de la habitación", FacesMessage.SEVERITY_INFO);
    }

    public void borrarHabitacion(CfgHabitacion habitacionEliminar) {
        Boolean encontrado = Boolean.FALSE;
        if (habitacionEliminar.getCfgCamaList() != null) {
            Iterator<CfgCama> listaCama = habitacionEliminar.getCfgCamaList().iterator();
            while (listaCama.hasNext()) {
                CfgCama cfgCama = listaCama.next();
                if (cfgCama.getOcupado().equals(Boolean.TRUE)) {
                    encontrado = Boolean.TRUE;
                    break;
                }
            }
        }
        if (encontrado.equals(Boolean.FALSE)) {
            this.getListaHabitaciones().remove(habitacionEliminar);
            this.getCfgHabitacionFacade().remove(habitacionEliminar);
            imprimirMensaje("Información", "Habitación borrada", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Información", "La Habitación no puede ser borrada tiene camas ocupadas disculpe.", FacesMessage.SEVERITY_INFO);
        }
    }

    public void cargarDialogoAgregarHabitacion() {
        this.setNumeroHabitacion("");
        this.setObservacion("");
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarHabitaciones').show()");
    }

    public void cargarDialogoAgregarCama(CfgHabitacion habitacionSeleccionada) {
        this.setNumeroCama("");
        this.setObservacionCama("");
        this.setHabitacion(new CfgHabitacion());
        this.setHabitacion(habitacionSeleccionada);
        RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarCama').show()");

    }

    public void agregarHabitacion() {
        if (this.getNumeroHabitacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar el número de la habitación del consultorio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getObservacion().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una observación de la habitación del consultorio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarHabitacion().equals(Boolean.FALSE)) {
            CfgHabitacion habita = new CfgHabitacion();
            habita.setCfgSedes(this.getSedeSeleccionada());
            habita.setNumeroHabitacion(this.getNumeroHabitacion());
            habita.setIsHabitacionUrgencia(this.getIsUrgencia());
            habita.setObservacion(this.getObservacion());
            this.getListaHabitaciones().add(habita);
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarHabitaciones').hide()");
            imprimirMensaje("Correcto", "Se ha agregado a la lista la habitación.", FacesMessage.SEVERITY_INFO);
        } else {
            imprimirMensaje("Error", "El número de habitación ingresado esta repetido, verifique e intente de nuevo.", FacesMessage.SEVERITY_ERROR);
        }
    }

    public Boolean validarHabitacion() {
        Boolean encontrado = Boolean.FALSE;
        Iterator<CfgHabitacion> listaHabitacionesConsultorio = this.getListaHabitaciones().iterator();
        while (listaHabitacionesConsultorio.hasNext()) {
            CfgHabitacion habitacionSeleccionada = listaHabitacionesConsultorio.next();
            if (habitacionSeleccionada.getNumeroHabitacion().equals(this.getNumeroHabitacion())) {
                encontrado = Boolean.TRUE;
                break;
            }
        }
        return encontrado;
    }

    public Boolean validarCama() {
        Boolean encontrado = Boolean.FALSE;
        if (this.getHabitacion().getCfgCamaList() == null) {
            return encontrado;
        }
        Iterator<CfgCama> listaCama = this.getHabitacion().getCfgCamaList().iterator();
        while (listaCama.hasNext()) {
            CfgCama cfgCama = listaCama.next();
            if (cfgCama.getNumeroCama().equals(this.getNumeroCama())) {
                encontrado = Boolean.TRUE;
                break;
            }
        }
        return encontrado;
    }

    public void agregarCama() {
        if (this.getNumeroCama().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar el número de la cama asociada a la habitación del consultorio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (this.getObservacionCama().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar una observación de la cama asociada a la habitación del consultorio.", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarCama().equals(Boolean.FALSE)) {
            CfgCama cama = new CfgCama();
            cama.setCfgHabitacion(this.getHabitacion());
            cama.setNumeroCama(this.getNumeroCama());
            cama.setObservacion(this.getObservacionCama());
            cama.setOcupado(Boolean.FALSE);
            Iterator<CfgHabitacion> listaHabitacionesConsultorio = this.getListaHabitaciones().iterator();
            while (listaHabitacionesConsultorio.hasNext()) {
                CfgHabitacion habitacionSeleccionada = listaHabitacionesConsultorio.next();
                if (habitacionSeleccionada.getNumeroHabitacion().equals(this.getHabitacion().getNumeroHabitacion())) {
                    if (habitacionSeleccionada.getCfgCamaList() == null) {
                        habitacionSeleccionada.setCfgCamaList(new ArrayList<CfgCama>());
                    }
                    habitacionSeleccionada.getCfgCamaList().add(cama);
                    break;
                }
            }
            RequestContext.getCurrentInstance().execute("PF('IdDialogoAgregarCama').hide()");
            imprimirMensaje("Correcto", "Se ha asociado la cama a la habitación seleccionada.", FacesMessage.SEVERITY_INFO);

        } else {
            imprimirMensaje("Error", "El número de cama ingresado esta repetido, verifique e intente de nuevo.", FacesMessage.SEVERITY_ERROR);
        }
    }

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------    
    public CfgSede getSedeSeleccionada() {
        return sedeSeleccionada;
    }

    public void setSedeSeleccionada(CfgSede sedeSeleccionada) {
        this.sedeSeleccionada = sedeSeleccionada;
    }

    public List<CfgSede> getListaSedes() {
        return listaSedes;
    }

    public void setListaSedes(List<CfgSede> listaSedes) {
        this.listaSedes = listaSedes;
    }

    public String getNombreSede() {
        return nombreSede;
    }

    public void setNombreSede(String nombreSede) {
        this.nombreSede = nombreSede;
    }

    public String getMunicipioSede() {
        return municipioSede;
    }

    public void setMunicipioSede(String municipioSede) {
        this.municipioSede = municipioSede;
    }

    public String getEncargadoSede() {
        return encargadoSede;
    }

    public void setEncargadoSede(String encargadoSede) {
        this.encargadoSede = encargadoSede;
    }

    public String getDireccionSede() {
        return direccionSede;
    }

    public void setDireccionSede(String direccionSede) {
        this.direccionSede = direccionSede;
    }

    public String getTelefono1Sede() {
        return telefono1Sede;
    }

    public void setTelefono1Sede(String telefono1Sede) {
        this.telefono1Sede = telefono1Sede;
    }

    public String getTelefono2Sede() {
        return telefono2Sede;
    }

    public void setTelefono2Sede(String telefono2Sede) {
        this.telefono2Sede = telefono2Sede;
    }

    public String getDepartamentoSede() {
        return departamentoSede;
    }

    public void setDepartamentoSede(String departamentoSede) {
        this.departamentoSede = departamentoSede;
    }

    public List<SelectItem> getListaMunicipiosSede() {
        return listaMunicipiosSede;
    }

    public void setListaMunicipiosSede(List<SelectItem> listaMunicipiosSede) {
        this.listaMunicipiosSede = listaMunicipiosSede;
    }

    public String getTituloTabSede() {
        return tituloTabSede;
    }

    public void setTituloTabSede(String tituloTabSede) {
        this.tituloTabSede = tituloTabSede;
    }

    public CfgSede getSedeSeleccionadaTabla() {
        return sedeSeleccionadaTabla;
    }

    public void setSedeSeleccionadaTabla(CfgSede sedeSeleccionadaTabla) {
        this.sedeSeleccionadaTabla = sedeSeleccionadaTabla;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public List<CfgHabitacion> getListaHabitaciones() {
        if (listaHabitaciones == null) {
            listaHabitaciones = new ArrayList<>();
        }
        return listaHabitaciones;
    }

    public void setListaHabitaciones(List<CfgHabitacion> listaHabitaciones) {
        this.listaHabitaciones = listaHabitaciones;
    }

    public CfgHabitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(CfgHabitacion habitacion) {
        this.habitacion = habitacion;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getNumeroCama() {
        return numeroCama;
    }

    public void setNumeroCama(String numeroCama) {
        this.numeroCama = numeroCama;
    }

    public String getObservacionCama() {
        return observacionCama;
    }

    public void setObservacionCama(String observacionCama) {
        this.observacionCama = observacionCama;
    }

    public Boolean getIsUrgencia() {
        return isUrgencia;
    }

    public void setIsUrgencia(Boolean isUrgencia) {
        this.isUrgencia = isUrgencia;
    }

    public CfgClasificacionesFacade getClasificacionesFacade() {
        return clasificacionesFacade;
    }

    public void setClasificacionesFacade(CfgClasificacionesFacade clasificacionesFacade) {
        this.clasificacionesFacade = clasificacionesFacade;
    }

    public CfgSedeFacade getSedeFacade() {
        return sedeFacade;
    }

    public void setSedeFacade(CfgSedeFacade sedeFacade) {
        this.sedeFacade = sedeFacade;
    }

    public CfgHabitacionFacade getCfgHabitacionFacade() {
        return cfgHabitacionFacade;
    }

    public void setCfgHabitacionFacade(CfgHabitacionFacade cfgHabitacionFacade) {
        this.cfgHabitacionFacade = cfgHabitacionFacade;
    }

    public CfgCamaFacade getCfgCamaFacade() {
        return cfgCamaFacade;
    }

    public void setCfgCamaFacade(CfgCamaFacade cfgCamaFacade) {
        this.cfgCamaFacade = cfgCamaFacade;
    }

    public AplicacionGeneralMB getAplicacionGeneralMB() {
        return aplicacionGeneralMB;
    }

    public void setAplicacionGeneralMB(AplicacionGeneralMB aplicacionGeneralMB) {
        this.aplicacionGeneralMB = aplicacionGeneralMB;
    }

}
