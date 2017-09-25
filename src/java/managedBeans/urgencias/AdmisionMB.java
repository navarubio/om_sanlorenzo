/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.urgencias;

import beans.enumeradores.EstadoAdmisionPaciente;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.MetodosGenerales;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.stream.FileImageOutputStream;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgImagenes;
import modelo.entidades.CfgPacientes;
import modelo.entidades.UrgAdmision;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgImagenesFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.UrgAmisionFacade;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Enderson
 */
@ManagedBean(name = "admisionMB")
@SessionScoped
public class AdmisionMB extends MetodosGenerales implements Serializable {

    private List<UrgAdmision> listaAdmisionesPaciente;
    private LazyDataModel<CfgPacientes> listaPacientes;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes nuevoPaciente;
    private UploadedFile archivoFirma;
    private UploadedFile archivoFoto;
    private final LoginMB loginMB;
    private final String firmaPorDefecto = "../recursos/img/firma.png";
    private final String fotoPorDefecto = "../recursos/img/img_usuario.png";
    private String urlFirma = firmaPorDefecto;
    private String urlFoto = fotoPorDefecto;
    private List<SelectItem> listaMunicipios;
    private List<SelectItem> listaServicios;
    private boolean fotoTomadaWebCam = false;//saber si la foto se tomo de la webcam
    private boolean nuevoRegistro = true;
    private String tabActivaPacientes = "0";//cual tab esa activa(0=datos presonales 1=datos adicionales)
    private String tituloTabPacientes = "Datos nuevo paciente";
    private String tipoIdentificacion = "";
    private String identificacion = "";
    private String lugarExpedicion = "";
    private Date fechaNacimiento = null;
    private String fechNacimiConvetEdad = "-";//fecha de nacimiento convertida en edad
    private String genero = "";
    private String grupoSanguineo = "";
    private String primerNombre = "";
    private String segundoNombre = "";
    private String primerApellido = "";
    private String segundoApellido = "";
    private String estadoCivil = "";
    private String ocupacion = "";
    private String telefonoResidencia = "";
    private String telefonoOficina = "";
    private String celular = "";
    private String departamento = "";
    private String municipio = "";
    private String zona = "";
    private String barrio = "";
    private String direccion = "";
    private String email = "";
    private Boolean activo = true;
    private String administradora = "";
    private String tipoAfiliado = "";
    private String regimen = "";
    private String categoriaPaciente;
    private String estrato = "";
    private String etnia = "";
    private String escolaridad = "";
    private String numeroAutorizacion = "";
    private String responsable;
    private String telefonoResponsable;
    private String parentesco;
    private String acompanante;
    private String telefonoAcompanante;
    private Date fechaAfiliacion = null;
    private Date fechaSisben = null;
    private String carnet = "";
    private Date fechaVenceCarnet = null;
    private String observaciones = "";
    private String discapacidad;
    private String gestacion;
    private String religion;
    private Boolean victimaConflicto;
    private Boolean poblacionLBGT;
    private Boolean desplazado;
    private Boolean victimaMaltrato;
    private Boolean nuevoRegistroPaciente;
    private Date fecha;
    private Date hora;
    @EJB
    CfgPacientesFacade pacientesFachada;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    CfgImagenesFacade imagenesFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    UrgAmisionFacade urgAmisionFacade;

    @PostConstruct
    public void inicializar() {
        nuevoRegistro = true;
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        limpiarFormulario();
        this.getListaAdmisionesPaciente().clear();
        this.getListaAdmisionesPaciente().addAll(this.getUrgAmisionFacade().findAllAdmisionesVigentes(EstadoAdmisionPaciente.ADMITIDO));
    }

    public void cambiaFechaNacimiento() {
        if (fechaNacimiento != null) {
            fechNacimiConvetEdad = calcularEdad(fechaNacimiento);
        } else {
            fechNacimiConvetEdad = "-";
        }
    }

    public AdmisionMB() {
        loginMB = (LoginMB) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
    }

    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        if (departamento != null && departamento.length() != 0) {
            List<CfgClasificaciones> listaM = clasificacionesFachada.buscarMunicipioPorDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)).getCodigo());
            for (CfgClasificaciones mun : listaM) {
                listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
            }
        }
    }

    public void limpiarFormulario() {
        tituloTabPacientes = "Datos nuevo paciente";
        pacienteSeleccionado = null;
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;
        urlFirma = firmaPorDefecto;
        urlFoto = fotoPorDefecto;
        tipoIdentificacion = "";
        lugarExpedicion = "";
        fechaNacimiento = null;
        fechNacimiConvetEdad = "-";
        genero = "";
        grupoSanguineo = "";
        primerNombre = "";
        segundoNombre = "";
        primerApellido = "";
        segundoApellido = "";
        estadoCivil = "";
        ocupacion = "";
        telefonoResidencia = "";
        telefonoOficina = "";
        celular = "";
        departamento = "";
        municipio = "";
        zona = "";
        barrio = "";
        direccion = "";
        email = "";
        activo = true;
        administradora = "";
        tipoAfiliado = "";
        regimen = "";
        categoriaPaciente = "";
        estrato = "";
        etnia = "";
        escolaridad = "";
        numeroAutorizacion = "";
        responsable = "";
        telefonoResponsable = "";
        parentesco = "";
        acompanante = "";
        telefonoAcompanante = "";
        fechaAfiliacion = null;
        fechaSisben = null;
        carnet = "";
        fechaVenceCarnet = null;
        observaciones = "";
        religion = "";
        gestacion = "";
        discapacidad = "";
        victimaConflicto = false;
        victimaMaltrato = false;
        desplazado = false;
        poblacionLBGT = false;
        cargarMunicipios();
        this.setNuevoRegistroPaciente(Boolean.FALSE);
        this.setPacienteSeleccionado(null);
        this.setFecha(new Date());
        this.setHora(new Date());
    }

    public void cargarPacienteDesdeHistorias(String id) {//se llama a esta funcion desde historias para que cargue un paciente
        pacienteSeleccionadoTabla = pacientesFachada.find(Integer.parseInt(id));
        cargarPaciente();

    }

    public void cargarPaciente() {
        if (pacienteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormulario();
        this.setPacienteSeleccionado(new CfgPacientes());
        pacienteSeleccionado = pacientesFachada.find(pacienteSeleccionadoTabla.getIdPaciente());
        tituloTabPacientes = "Datos paciente: " + pacienteSeleccionado.nombreCompleto();
        archivoFirma = null;
        archivoFoto = null;
        fotoTomadaWebCam = false;

        if (pacienteSeleccionado.getFirma() != null) {
            urlFirma = "../imagenesOpenmedical/" + pacienteSeleccionado.getFirma().getUrlImagen();
        } else {
            urlFirma = firmaPorDefecto;
        }
        if (pacienteSeleccionado.getFoto() != null) {
            urlFoto = "../imagenesOpenmedical/" + pacienteSeleccionado.getFoto().getUrlImagen();
        } else {
            urlFoto = fotoPorDefecto;
        }
        this.setIdentificacion(pacienteSeleccionado.getIdentificacion());
        if (pacienteSeleccionado.getTipoIdentificacion() != null) {
            tipoIdentificacion = pacienteSeleccionado.getTipoIdentificacion().getId().toString();
        } else {
            tipoIdentificacion = "";
        }
        lugarExpedicion = pacienteSeleccionado.getLugarExpedicion();
        if (pacienteSeleccionado.getFechaNacimiento() != null) {
            fechaNacimiento = pacienteSeleccionado.getFechaNacimiento();
            fechNacimiConvetEdad = calcularEdad(fechaNacimiento);
        } else {
            fechaNacimiento = null;
            fechNacimiConvetEdad = "-";
        }
        if (pacienteSeleccionado.getGenero() != null) {
            genero = pacienteSeleccionado.getGenero().getId().toString();
        } else {
            genero = "";
        }
        if (pacienteSeleccionado.getGrupoSanguineo() != null) {
            grupoSanguineo = pacienteSeleccionado.getGrupoSanguineo().getId().toString();
        } else {
            grupoSanguineo = "";
        }
        primerNombre = pacienteSeleccionado.getPrimerNombre();
        segundoNombre = pacienteSeleccionado.getSegundoNombre();
        primerApellido = pacienteSeleccionado.getPrimerApellido();
        segundoApellido = pacienteSeleccionado.getSegundoApellido();
        if (pacienteSeleccionado.getEstadoCivil() != null) {
            estadoCivil = pacienteSeleccionado.getEstadoCivil().getId().toString();
        } else {
            estadoCivil = "";
        }
        if (pacienteSeleccionado.getOcupacion() != null) {
            ocupacion = pacienteSeleccionado.getOcupacion().getId().toString();
        } else {
            ocupacion = "";
        }
        telefonoResidencia = pacienteSeleccionado.getTelefonoResidencia();
        telefonoOficina = pacienteSeleccionado.getTelefonoOficina();
        celular = pacienteSeleccionado.getCelular();
        if (pacienteSeleccionado.getDepartamento() != null) {
            departamento = pacienteSeleccionado.getDepartamento().getId().toString();
            cargarMunicipios();
            municipio = pacienteSeleccionado.getMunicipio().getId().toString();
        } else {
            departamento = "";
            municipio = "";
        }
        if (pacienteSeleccionado.getZona() != null) {
            zona = pacienteSeleccionado.getZona().getId().toString();
        } else {
            zona = "";
        }
        barrio = pacienteSeleccionado.getBarrio();
        direccion = pacienteSeleccionado.getDireccion();
        email = pacienteSeleccionado.getEmail();
        activo = pacienteSeleccionado.getActivo();
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            administradora = pacienteSeleccionado.getIdAdministradora().getIdAdministradora().toString();
        } else {
            administradora = "";
        }
        if (pacienteSeleccionado.getTipoAfiliado() != null) {
            tipoAfiliado = pacienteSeleccionado.getTipoAfiliado().getId().toString();
        } else {
            tipoAfiliado = "";
        }
        if (pacienteSeleccionado.getRegimen() != null) {
            regimen = pacienteSeleccionado.getRegimen().getId().toString();
        } else {
            regimen = "";
        }
        if (pacienteSeleccionado.getCategoriaPaciente() != null) {
            categoriaPaciente = pacienteSeleccionado.getCategoriaPaciente().getId().toString();
        } else {
            categoriaPaciente = "";
        }
        if (pacienteSeleccionado.getNivel() != null) {
            estrato = pacienteSeleccionado.getNivel().getId().toString();
        } else {
            estrato = "";
        }
        if (pacienteSeleccionado.getEtnia() != null) {
            etnia = pacienteSeleccionado.getEtnia().getId().toString();
        } else {
            etnia = "";
        }
        if (pacienteSeleccionado.getEscolaridad() != null) {
            escolaridad = pacienteSeleccionado.getEscolaridad().getId().toString();
        } else {
            escolaridad = "";
        }

        if (pacienteSeleccionado.getReligion() != null) {
            religion = pacienteSeleccionado.getReligion().getId().toString();
        } else {
            religion = "";
        }
        if (pacienteSeleccionado.getDiscapacidad() != null) {
            discapacidad = pacienteSeleccionado.getDiscapacidad().getId().toString();
        } else {
            discapacidad = "";
        }
        if (pacienteSeleccionado.getGestacion() != null) {
            gestacion = pacienteSeleccionado.getGestacion().getId().toString();
        } else {
            gestacion = "";
        }
        if (pacienteSeleccionado.getDesplazado() != null) {
            desplazado = pacienteSeleccionado.getDesplazado();
        } else {
            desplazado = false;
        }
        if (pacienteSeleccionado.getPoblacionLBGT() != null) {
            poblacionLBGT = pacienteSeleccionado.getPoblacionLBGT();
        } else {
            poblacionLBGT = false;
        }
        if (pacienteSeleccionado.getVictimaMaltrato() != null) {
            victimaMaltrato = pacienteSeleccionado.getVictimaMaltrato();
        } else {
            victimaMaltrato = false;
        }
        if (pacienteSeleccionado.getVictimaConflicto() != null) {
            victimaConflicto = pacienteSeleccionado.getVictimaConflicto();
        } else {
            victimaConflicto = false;
        }

        numeroAutorizacion = pacienteSeleccionado.getNumeroAutorizacion();
        responsable = pacienteSeleccionado.getResponsable();
        telefonoResponsable = pacienteSeleccionado.getTelefonoResponsable();
        if (pacienteSeleccionado.getParentesco() != null) {
            parentesco = pacienteSeleccionado.getParentesco().getId().toString();
        } else {
            parentesco = null;
        }
        acompanante = pacienteSeleccionado.getAcompanante();
        telefonoAcompanante = pacienteSeleccionado.getTelefonoAcompanante();

        if (pacienteSeleccionado.getFechaAfiliacion() != null) {
            fechaAfiliacion = pacienteSeleccionado.getFechaAfiliacion();
        } else {
            fechaAfiliacion = null;
        }
        if (pacienteSeleccionado.getFechaSisben() != null) {
            fechaSisben = pacienteSeleccionado.getFechaSisben();
        } else {
            fechaSisben = null;
        }
        carnet = pacienteSeleccionado.getCarnet();
        if (pacienteSeleccionado.getFechaVenceCarnet() != null) {
            fechaVenceCarnet = pacienteSeleccionado.getFechaVenceCarnet();
        } else {
            fechaVenceCarnet = null;
        }
        observaciones = pacienteSeleccionado.getObservaciones();
        tabActivaPacientes = "0";
        if (this.getPacienteSeleccionado().getIdAdministradora() == null) {
            this.setNuevoRegistroPaciente(Boolean.TRUE);
            imprimirMensaje("Información", "Debe completar los datos de la administradora ", FacesMessage.SEVERITY_INFO);
        } else {
            this.setNuevoRegistroPaciente(Boolean.FALSE);
        }
    }

    public void findPaciente() {

        if (this.getIdentificacion().trim().equals("") || this.getIdentificacion().isEmpty() || this.getIdentificacion() == null) {

            imprimirMensaje("Información", "Debe ingresar un numero de identificación", FacesMessage.SEVERITY_INFO);
            setPacienteSeleccionado(null);
            return;
        }
        pacienteSeleccionado = pacientesFachada.buscarPorIdentificacion(getIdentificacion());
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "No se encontro el paciente proceda a registrarlo", FacesMessage.SEVERITY_ERROR);
            this.limpiarFormulario();
            this.setNuevoRegistroPaciente(Boolean.TRUE);
        } else {
            setPacienteSeleccionado(pacienteSeleccionado);
            if (this.getPacienteSeleccionado().getIdAdministradora() == null) {
                this.setNuevoRegistroPaciente(Boolean.TRUE);
                imprimirMensaje("Información", "Paciente encontrado debe completar los datos de la administradora ", FacesMessage.SEVERITY_INFO);
            } else {
                this.setNuevoRegistroPaciente(Boolean.FALSE);
            }
            pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
        }

    }

    /**
     * Metodo para persistir el registro del paciente en la admision.
     *
     */
    public void guardarAdmisionPaciente() {
        UrgAdmision admision = new UrgAdmision();
        admision.setAtendida(false);
        admision.setFechaAdmision(new Date());
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH:mm:ss");
        String hora = sdfHour.format(new Date());
        admision.setNroAdmision(this.nroAdmision());
        try {
            admision.setHoraAdmision(sdfHour.parse(hora));
        } catch (ParseException ex) {
            Logger.getLogger(AdmisionMB.class.getName()).log(Level.SEVERE, null, ex);
        }
        admision.setIdPrestador(loginMB.getUsuarioActual());
        admision.setEstadoAdmisionPaciente(EstadoAdmisionPaciente.ADMITIDO);
        admision.setIdPaciente(this.getPacienteSeleccionado());
        urgAmisionFacade.create(admision);
        this.getListaAdmisionesPaciente().clear();
        this.getListaAdmisionesPaciente().addAll(this.getUrgAmisionFacade().findAllAdmisionesVigentes(EstadoAdmisionPaciente.ADMITIDO));
        this.cancelar();
        imprimirMensaje("Información", "Se ha agregado el paciente a la lista de espera exitosamente", FacesMessage.SEVERITY_INFO);
    }

    public void guardarPaciente() {

        CfgPacientes pacienteTmp;
        //VALIDACION DE DATOS OBLIGATORIOS

        if (validacionCampoVacio(identificacion, "Identificación")) {
            return;
        }
        if (validacionCampoVacio(tipoIdentificacion, "Tipo de identificación")) {
            return;
        }
        if (fechaNacimiento == null) {
            imprimirMensaje("Error", "La fecha de nacimiento es obligatoria", FacesMessage.SEVERITY_ERROR);
            return;
        }
        //VALIDACION DE VALORES UNICOS
        if (pacienteSeleccionado == null) {//guardando nuevo paciente                        
            if (pacientesFachada.buscarPorIdentificacion(identificacion) != null) {
                imprimirMensaje("Error", "Ya existe un paciente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoPaciente();

        } else {//modificando paciente existente            
            pacienteTmp = pacientesFachada.buscarPorIdentificacion(identificacion);
            if (pacienteTmp != null && !pacienteSeleccionado.getIdentificacion().equals(pacienteTmp.getIdentificacion())) {
                imprimirMensaje("Error", "Existe un paciente diferente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        tabActivaPacientes = "0";
        //   this.setNuevoRegistroPaciente(false);

    }

    private void guardarNuevoPaciente() {
        nuevoPaciente = new CfgPacientes();
        String nombreImagenEnTmp;
        String extension;
        String nombreImagenReal;
        if (archivoFirma != null) {//se cargo firma         
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            CfgImagenes nuevaImagen = new CfgImagenes();
            imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
            nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoPaciente.setFirma(nuevaImagen);
        }
        if (archivoFoto != null || fotoTomadaWebCam) {//se cargo foto
            if (fotoTomadaWebCam) {//es por webCam
                nombreImagenReal = "fotoWebCam.png";
                extension = ".png";
            } else {//es por carga de archivo
                nombreImagenReal = archivoFoto.getFileName();
                extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            }

            CfgImagenes nuevaImagen = new CfgImagenes();
            imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
            nombreImagenEnTmp = "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
            nuevaImagen.setNombre(nombreImagenReal);
            nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
            nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
            imagenesFacade.edit(nuevaImagen);
            nuevoPaciente.setFoto(nuevaImagen);
        }
        nuevoPaciente.setIdentificacion(identificacion);
        if (validarNoVacio(tipoIdentificacion)) {
            nuevoPaciente.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        }
        nuevoPaciente.setLugarExpedicion(lugarExpedicion);
        if (fechaNacimiento != null) {
            nuevoPaciente.setFechaNacimiento(fechaNacimiento);
        }
        this.getNuevoPaciente().setPrimerNombre(primerNombre);
        if (this.nuevoPaciente.getPrimerNombre().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar el primer nombre", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.getNuevoPaciente().setSegundoNombre(segundoNombre);
        if (this.getNuevoPaciente().getSegundoNombre().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar el segundo nombre", FacesMessage.SEVERITY_ERROR);
            return;
        }
        this.getNuevoPaciente().setPrimerApellido(primerApellido);
        if (this.getNuevoPaciente().getPrimerApellido().isEmpty()) {
            imprimirMensaje("Error", "Debe seleccionar el primer apellido", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(grupoSanguineo)) {
            nuevoPaciente.setGrupoSanguineo(clasificacionesFachada.find(Integer.parseInt(grupoSanguineo)));
        }
        nuevoPaciente.setPrimerNombre(primerNombre);
        nuevoPaciente.setSegundoNombre(segundoNombre);
        nuevoPaciente.setPrimerApellido(primerApellido);
        nuevoPaciente.setSegundoApellido(segundoApellido);
        if (validarNoVacio(estadoCivil)) {
            nuevoPaciente.setEstadoCivil(clasificacionesFachada.find(Integer.parseInt(estadoCivil)));
        }
        if (ocupacion != null) {
            nuevoPaciente.setOcupacion(clasificacionesFachada.find(Integer.parseInt(ocupacion)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar una ocupación", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(telefonoResidencia)) {
            nuevoPaciente.setTelefonoResidencia(telefonoResidencia);
        } else {
            imprimirMensaje("Error", "Debe ingresar un teléfono", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(departamento)) {
            nuevoPaciente.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
        } else {
            imprimirMensaje("Error", "Debe selecionar un departamento y un municipio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(religion)) {
            nuevoPaciente.setReligion(clasificacionesFachada.find(Integer.parseInt(religion)));
        }

        if (validarNoVacio(discapacidad)) {
            nuevoPaciente.setDiscapacidad(clasificacionesFachada.find(Integer.parseInt(discapacidad)));
        }

        nuevoPaciente.setTelefonoResidencia(telefonoResidencia);
        nuevoPaciente.setTelefonoOficina(telefonoOficina);
        nuevoPaciente.setCelular(celular);
        if (departamento != null && departamento.length() != 0) {
            nuevoPaciente.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            nuevoPaciente.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(zona)) {
            nuevoPaciente.setZona(clasificacionesFachada.find(Integer.parseInt(zona)));
        }
        nuevoPaciente.setBarrio(barrio);
        nuevoPaciente.setDireccion(direccion);
        nuevoPaciente.setEmail(email);
        nuevoPaciente.setActivo(activo);
        if (validarNoVacio(administradora)) {
            nuevoPaciente.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar una administradora en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(tipoAfiliado)) {
            nuevoPaciente.setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar un tipo de afiliado en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(regimen)) {
            nuevoPaciente.setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar un regimen en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(categoriaPaciente)) {
            nuevoPaciente.setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar una categoria en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(estrato)) {
            nuevoPaciente.setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar un estrato en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(responsable)) {
            nuevoPaciente.setResponsable(responsable);
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe ingresar un responsable en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(telefonoResponsable)) {
            nuevoPaciente.setTelefonoResponsable(telefonoResponsable);
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe ingresar un telefono en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(parentesco)) {
            nuevoPaciente.setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        } else {
            this.setTabActivaPacientes("1");
            imprimirMensaje("Error", "Debe seleccionar un parentezco en datos adicionales", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(etnia)) {
            nuevoPaciente.setEtnia(clasificacionesFachada.find(Integer.parseInt(etnia)));
        }
        if (validarNoVacio(escolaridad)) {
            nuevoPaciente.setEscolaridad(clasificacionesFachada.find(Integer.parseInt(escolaridad)));
        }
        nuevoPaciente.setNumeroAutorizacion(numeroAutorizacion);
        nuevoPaciente.setResponsable(responsable);
        nuevoPaciente.setTelefonoResponsable(telefonoResponsable);
        if (validarNoVacio(parentesco)) {
            nuevoPaciente.setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        }
        nuevoPaciente.setAcompanante(acompanante);
        nuevoPaciente.setTelefonoAcompanante(telefonoAcompanante);

        if (fechaAfiliacion != null) {
            nuevoPaciente.setFechaAfiliacion(fechaAfiliacion);
        }
        if (fechaSisben != null) {
            nuevoPaciente.setFechaSisben(fechaSisben);
        }
        nuevoPaciente.setCarnet(carnet);
        if (fechaVenceCarnet != null) {
            nuevoPaciente.setFechaVenceCarnet(fechaVenceCarnet);
        }
        nuevoPaciente.setObservaciones(observaciones);

        nuevoPaciente.setVictimaConflicto(victimaConflicto);
        nuevoPaciente.setVictimaMaltrato(victimaMaltrato);
        nuevoPaciente.setPoblacionLBGT(poblacionLBGT);
        nuevoPaciente.setDesplazado(desplazado);
        pacientesFachada.create(nuevoPaciente);
        imprimirMensaje("Correcto", "Nuevo paciente creado correctamente,proceda a registrar la admisión", FacesMessage.SEVERITY_INFO);
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        // limpiarFormulario();//limpiar formulario
        this.findPaciente();

    }

    private UploadedFile file;
    private String newFileName = "";

    private void copyFile(String fileName, InputStream in) {
        try {
            try (OutputStream out = new FileOutputStream(new File(fileName))) {
                int read;
                byte[] bytes = new byte[1024];
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
                in.close();
                out.flush();
            }
            System.out.println("El nuevo fichero fue creado con éxito!");
        } catch (IOException e) {
            System.out.println("Error 4 en " + this.getClass().getName() + ":" + e.toString());
        }
    }

    public void uploadFirma(FileUploadEvent event) {
        try {
            archivoFirma = event.getFile();
            String nombreImg = "firmaPaciente" //es firma de usuario
                    + loginMB.getUsuarioActual().getIdUsuario() //diferenciar el usuario actual
                    + archivoFirma.getFileName().substring(archivoFirma.getFileName().lastIndexOf("."), archivoFirma.getFileName().length());//colocar extension
            if (uploadArchivo(archivoFirma, loginMB.getUrltmp() + nombreImg)) {
                urlFirma = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/" + nombreImg;
            } else {
                urlFirma = firmaPorDefecto;
                archivoFirma = null;
            }
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
        }
    }

    public void uploadFoto(FileUploadEvent event) {
        try {
            archivoFoto = event.getFile();
            String nombreImg = "fotoPaciente" //es foto de usuario
                    + loginMB.getUsuarioActual().getIdUsuario() //diferenciar el usuario actual
                    + archivoFoto.getFileName().substring(archivoFoto.getFileName().lastIndexOf("."), archivoFoto.getFileName().length());//colocar extension
            if (uploadArchivo(archivoFoto, loginMB.getUrltmp() + nombreImg)) {
                urlFoto = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/" + nombreImg;
                fotoTomadaWebCam = false;
            } else {
                urlFoto = fotoPorDefecto;
                archivoFoto = null;
                fotoTomadaWebCam = false;
            }
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
        }
    }

    public void tomarFoto(CaptureEvent captureEvent) {
        byte[] data = captureEvent.getData();
        FileImageOutputStream imageOutput;
        archivoFoto = null;
        try {
            File imagen = new File(loginMB.getUrltmp() + "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            if (imagen.exists()) {
                imagen.delete();
                imagen = new File(loginMB.getUrltmp() + "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png");
            }
            imageOutput = new FileImageOutputStream(imagen);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            fotoTomadaWebCam = true;
            urlFoto = "../imagenesOpenmedical/" + loginMB.getBaseDeDatosActual() + "/tmp/fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + ".png";
        } catch (IOException e) {
            urlFoto = fotoPorDefecto;
            fotoTomadaWebCam = false;
            System.out.println("Error 02: " + e.getMessage());//imprimirMensaje("Error 02", e.getMessage(), FacesMessage.SEVERITY_ERROR);            
        }
    }

    public void cancelar() {
        this.limpiarFormulario();
        this.setIdentificacion("");
    }

    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public CfgPacientes getNuevoPaciente() {
        return nuevoPaciente;
    }

    public void setNuevoPaciente(CfgPacientes nuevoPaciente) {
        this.nuevoPaciente = nuevoPaciente;
    }

    public CfgPacientesFacade getPacientesFachada() {
        return pacientesFachada;
    }

    public void setPacientesFachada(CfgPacientesFacade pacientesFachada) {
        this.pacientesFachada = pacientesFachada;
    }

    public CfgClasificacionesFacade getClasificacionesFachada() {
        return clasificacionesFachada;
    }

    public void setClasificacionesFachada(CfgClasificacionesFacade clasificacionesFachada) {
        this.clasificacionesFachada = clasificacionesFachada;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getRegimen() {
        return regimen;
    }

    public void setRegimen(String regimen) {
        this.regimen = regimen;
    }

    public String getEstrato() {
        return estrato;
    }

    public void setEstrato(String estrato) {
        this.estrato = estrato;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getEtnia() {
        return etnia;
    }

    public void setEtnia(String etnia) {
        this.etnia = etnia;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefonoResidencia() {
        return telefonoResidencia;
    }

    public void setTelefonoResidencia(String telefonoResidencia) {
        this.telefonoResidencia = telefonoResidencia;
    }

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTipoAfiliado() {
        return tipoAfiliado;
    }

    public void setTipoAfiliado(String tipoAfiliado) {
        this.tipoAfiliado = tipoAfiliado;
    }

    public Date getFechaAfiliacion() {
        return fechaAfiliacion;
    }

    public void setFechaAfiliacion(Date fechaAfiliacion) {
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public Date getFechaSisben() {
        return fechaSisben;
    }

    public void setFechaSisben(Date fechaSisben) {
        this.fechaSisben = fechaSisben;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getUrlFirma() {
        return urlFirma;
    }

    public void setUrlFirma(String urlFirma) {
        this.urlFirma = urlFirma;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getAdministradora() {
        return administradora;
    }

    public void setAdministradora(String administradora) {
        this.administradora = administradora;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getTelefonoResponsable() {
        return telefonoResponsable;
    }

    public void setTelefonoResponsable(String telefonoResponsable) {
        this.telefonoResponsable = telefonoResponsable;
    }

    public String getAcompanante() {
        return acompanante;
    }

    public void setAcompanante(String acompanante) {
        this.acompanante = acompanante;
    }

    public String getTelefonoAcompanante() {
        return telefonoAcompanante;
    }

    public void setTelefonoAcompanante(String telefonoAcompanante) {
        this.telefonoAcompanante = telefonoAcompanante;
    }

    public String getCategoriaPaciente() {
        return categoriaPaciente;
    }

    public void setCategoriaPaciente(String categoriaPaciente) {
        this.categoriaPaciente = categoriaPaciente;
    }

    public UploadedFile getArchivoFoto() {
        return archivoFoto;
    }

    public void setArchivoFoto(UploadedFile archivoFoto) {
        this.archivoFoto = archivoFoto;
    }

    public String getFechNacimiConvetEdad() {
        return fechNacimiConvetEdad;
    }

    public void setFechNacimiConvetEdad(String fechNacimiConvetEdad) {
        this.fechNacimiConvetEdad = fechNacimiConvetEdad;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public Date getFechaVenceCarnet() {
        return fechaVenceCarnet;
    }

    public void setFechaVenceCarnet(Date fechaVenceCarnet) {
        this.fechaVenceCarnet = fechaVenceCarnet;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getTabActivaPacientes() {
        return tabActivaPacientes;
    }

    public void setTabActivaPacientes(String tabActivaPacientes) {
        this.tabActivaPacientes = tabActivaPacientes;
    }

    public String getTituloTabPacientes() {
        return tituloTabPacientes;
    }

    public void setTituloTabPacientes(String tituloTabPacientes) {
        this.tituloTabPacientes = tituloTabPacientes;
    }

    public String getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(String discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getGestacion() {
        return gestacion;
    }

    public void setGestacion(String gestacion) {
        this.gestacion = gestacion;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Boolean getVictimaConflicto() {
        return victimaConflicto;
    }

    public void setVictimaConflicto(Boolean victimaConflicto) {
        this.victimaConflicto = victimaConflicto;
    }

    public Boolean getPoblacionLBGT() {
        return poblacionLBGT;
    }

    public void setPoblacionLBGT(Boolean poblacionLBGT) {
        this.poblacionLBGT = poblacionLBGT;
    }

    public Boolean getDesplazado() {
        return desplazado;
    }

    public void setDesplazado(Boolean desplazado) {
        this.desplazado = desplazado;
    }

    public Boolean getVictimaMaltrato() {
        return victimaMaltrato;
    }

    public Boolean getNuevoRegistroPaciente() {
        return nuevoRegistroPaciente;
    }

    public List<UrgAdmision> getListaAdmisionesPaciente() {
        if (listaAdmisionesPaciente == null) {
            listaAdmisionesPaciente = new ArrayList<UrgAdmision>();
        }
        return listaAdmisionesPaciente;
    }

    public void setListaAdmisionesPaciente(List<UrgAdmision> listaAdmisionesPaciente) {
        this.listaAdmisionesPaciente = listaAdmisionesPaciente;
    }

    public UploadedFile getArchivoFirma() {
        return archivoFirma;
    }

    public void setArchivoFirma(UploadedFile archivoFirma) {
        this.archivoFirma = archivoFirma;
    }

    public boolean isFotoTomadaWebCam() {
        return fotoTomadaWebCam;
    }

    public void setFotoTomadaWebCam(boolean fotoTomadaWebCam) {
        this.fotoTomadaWebCam = fotoTomadaWebCam;
    }

    public boolean isNuevoRegistro() {
        return nuevoRegistro;
    }

    public void setNuevoRegistro(boolean nuevoRegistro) {
        this.nuevoRegistro = nuevoRegistro;
    }

    public CfgImagenesFacade getImagenesFacade() {
        return imagenesFacade;
    }

    public void setImagenesFacade(CfgImagenesFacade imagenesFacade) {
        this.imagenesFacade = imagenesFacade;
    }

    public FacAdministradoraFacade getAdministradoraFacade() {
        return administradoraFacade;
    }

    public void setAdministradoraFacade(FacAdministradoraFacade administradoraFacade) {
        this.administradoraFacade = administradoraFacade;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public void setNuevoRegistroPaciente(Boolean nuevoRegistroPaciente) {
        this.nuevoRegistroPaciente = nuevoRegistroPaciente;
    }

    public void setVictimaMaltrato(Boolean victimaMaltrato) {
        this.victimaMaltrato = victimaMaltrato;
    }

    public UrgAmisionFacade getUrgAmisionFacade() {
        return urgAmisionFacade;
    }

    public void setUrgAmisionFacade(UrgAmisionFacade urgAmisionFacade) {
        this.urgAmisionFacade = urgAmisionFacade;
    }

    public String nroAdmision() {
        String nro = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Long nroAdmsionesDiarias;
        nroAdmsionesDiarias = this.getUrgAmisionFacade().numeroAdmisionesDiarias(new Date());
        nro = cerosIzquierda(nroAdmsionesDiarias);
        nro = format.format(new Date()) + nro;
        System.out.println("ddd" + nro);

        return nro;
    }

    public String cerosIzquierda(Long valor) {
        //aumenta ceros a la izquierda segun numCifas
        String v = String.valueOf(valor);
        int ceros = 4 - v.length();
        if (ceros < 4) {
            for (int i = 0; i < ceros; i++) {
                v = "0" + v;
            }
        }
        return v;
    }

    public void ingresarPacienteTriage(UrgAdmision pacienteAdmitido) {
        this.getListaAdmisionesPaciente().remove(pacienteAdmitido);
        pacienteAdmitido.setAtendida(true);
        pacienteAdmitido.setEstadoAdmisionPaciente(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_TRIAGE);
        this.getUrgAmisionFacade().edit(pacienteAdmitido);
        imprimirMensaje("Información", "Se ha enviado exitosamente el paciente a Triage", FacesMessage.SEVERITY_INFO);
    }

    public void actualizarPaciente() {
        if (validarNoVacio(religion)) {
            this.getPacienteSeleccionado().setReligion(clasificacionesFachada.find(Integer.parseInt(religion)));
        }
        if (validarNoVacio(discapacidad)) {
            this.getPacienteSeleccionado().setDiscapacidad(clasificacionesFachada.find(Integer.parseInt(discapacidad)));
        }
        if (validarNoVacio(administradora)) {
            this.getPacienteSeleccionado().setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar una administradora", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(tipoAfiliado)) {
            this.getPacienteSeleccionado().setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un tipo de afiliado", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(regimen)) {
            this.getPacienteSeleccionado().setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un regimen "
                    + "", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(categoriaPaciente)) {
            this.getPacienteSeleccionado().setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar una categoria", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(estrato)) {
            this.getPacienteSeleccionado().setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un estrato", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(responsable)) {
            getPacienteSeleccionado().setResponsable(responsable);
        } else {
            imprimirMensaje("Error", "Debe ingresar el nombre de un responsable", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(telefonoResponsable)) {
            getPacienteSeleccionado().setTelefonoResponsable(telefonoResponsable);
        } else {
            imprimirMensaje("Error", "Debe ingresar un teléfono", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(parentesco)) {
            getPacienteSeleccionado().setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        } else {
            imprimirMensaje("Error", "Debe seleccionar un parentesco", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (validarNoVacio(etnia)) {
            getPacienteSeleccionado().setEtnia(clasificacionesFachada.find(Integer.parseInt(etnia)));
        }
        if (validarNoVacio(escolaridad)) {
            getPacienteSeleccionado().setEscolaridad(clasificacionesFachada.find(Integer.parseInt(escolaridad)));
        }
        getPacienteSeleccionado().setNumeroAutorizacion(numeroAutorizacion);
        getPacienteSeleccionado().setTelefonoResponsable(telefonoResponsable);
        if (validarNoVacio(parentesco)) {
            getPacienteSeleccionado().setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        }
        getPacienteSeleccionado().setAcompanante(acompanante);
        getPacienteSeleccionado().setTelefonoAcompanante(telefonoAcompanante);

        if (fechaAfiliacion != null) {
            getPacienteSeleccionado().setFechaAfiliacion(fechaAfiliacion);
        }
        if (fechaSisben != null) {
            getPacienteSeleccionado().setFechaSisben(fechaSisben);
        }
        getPacienteSeleccionado().setCarnet(carnet);
        if (fechaVenceCarnet != null) {
            getPacienteSeleccionado().setFechaVenceCarnet(fechaVenceCarnet);
        }
        getPacienteSeleccionado().setObservaciones(observaciones);
        getPacienteSeleccionado().setVictimaConflicto(victimaConflicto);
        getPacienteSeleccionado().setVictimaMaltrato(victimaMaltrato);
        getPacienteSeleccionado().setPoblacionLBGT(poblacionLBGT);
        getPacienteSeleccionado().setDesplazado(desplazado);
        pacientesFachada.edit(getPacienteSeleccionado());
        imprimirMensaje("Correcto", "Se ha actualizado los datos del paciente correctamente,proceda a registrar la admisión", FacesMessage.SEVERITY_INFO);
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        this.findPaciente();
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

}
