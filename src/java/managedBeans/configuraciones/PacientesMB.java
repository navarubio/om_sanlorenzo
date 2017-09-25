package managedBeans.configuraciones;

import beans.utilidades.LazyPacienteDataModel;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgImagenes;
import modelo.entidades.CfgPacientes;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgImagenesFacade;
import modelo.fachadas.CfgPacientesFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.imageio.stream.FileImageOutputStream;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import beans.utilidades.MetodosGenerales;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.FacContrato;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacContratoFacade;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

@ManagedBean(name = "pacientesMB")
@SessionScoped
public class PacientesMB extends MetodosGenerales implements Serializable {
    //---------------------------------------------------
    //-----------------FACHADAS -------------------------
    //---------------------------------------------------

    @EJB
    CfgPacientesFacade pacientesFachada;
    @EJB
    CfgClasificacionesFacade clasificacionesFachada;
    @EJB
    CfgImagenesFacade imagenesFacade;
    @EJB
    FacAdministradoraFacade administradoraFacade;
    @EJB
    FacContratoFacade contratoFacade;
    
    private AplicacionGeneralMB aplicacionGeneralMB;
    //---------------------------------------------------
    //-----------------ENTIDADES -------------------------
    //---------------------------------------------------
    private LazyDataModel<CfgPacientes> listaPacientes;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes nuevoPaciente;
    private List<FacContrato> listaContrato;
    //---------------------------------------------------
    //-----------------VARIABLES ------------------------
    //---------------------------------------------------
    private UploadedFile archivoFirma;
    private UploadedFile archivoFoto;
    private final LoginMB loginMB;
    private final String firmaPorDefecto = "../recursos/img/firma.png";
    private final String fotoPorDefecto = "../recursos/img/img_usuario.png";
    private String urlFirma = firmaPorDefecto;
    private String urlFoto = fotoPorDefecto;
    private List<SelectItem> listaMunicipios;
    private List<SelectItem> listaServicios;
    //private List<CfgClasificaciones> listaCompletaOcupaciones;
    private boolean fotoTomadaWebCam = false;//saber si la foto se tomo de la webcam
    private boolean nuevoRegistro = true;
    private String tabActivaPacientes = "0";//cual tab esa activa(0=datos presonales 1=datos adicionales)
    //DATOS PRINCIPALES  
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
    private String otra_ocupacion = "";
    private String telefonoResidencia = "";
    private String telefonoOficina = "";
    private String celular = "";
    private String departamento = "";
    private String municipio = "";
    private String zona = "";
    private String barrio = "";
    private String desBarrio = "COMUNA Y BARRIO";
    private String direccion = "";
    private String email = "";
    private Boolean activo = true;
    private String administradora = "";
    private String contrato="0";
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
    private String parentesco_a;
    private String acompanante;
    private String telefonoAcompanante;
    private Date fechaAfiliacion = null;
    private Date fechaSisben = null;
    private String carnet = "";
    private Date fechaVenceCarnet = null;
    private String observaciones = "";

    //nuevos Luis CD
    private String discapacidad;
    private String gestacion;
    private String religion;
    private Boolean victimaConflicto;
    private Boolean poblacionLBGT;
    private Boolean desplazado;
    private Boolean victimaMaltrato;
    private Boolean ver_otra_ocupacion = false;
    private Boolean ver_ocupacion = false;
    private Boolean ver_estado_civil = false;
    private int años = 1;
    //    
    
    private final static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private final static Pattern EMAIL_COMPILED_PATTERN = Pattern.compile(EMAIL_PATTERN);
     

    //---------------------------------------------------
    //----------------- FUNCIONES -------------------------
    //---------------------------------------------------      
    @PostConstruct
    public void inicializar() {
        nuevoRegistro = true;
        listaPacientes = new LazyPacienteDataModel(pacientesFachada);
        listaContrato = new ArrayList();
        //listaCompletaOcupaciones = clasificacionesFachada.buscarPorMaestro("ocupaciones");
    }

    public void cambiaFechaNacimiento(SelectEvent event) {
        System.out.println(celular);
//        ocupacion = "";
//        otra_ocupacion = "";
//        estadoCivil = "";
        if (fechaNacimiento != null) {
            fechNacimiConvetEdad = calcularEdad(fechaNacimiento);
            años = calcularEdadInt(fechaNacimiento);
//            ver_ocupacion = true;
//            ver_estado_civil = true;
//            if(años <= 13){
//                estadoCivil = "1881";
//                ocupacion = "1654";
//            }
        } else {
            años = 1;
//            ver_ocupacion = false;
//            ver_estado_civil = false;
            fechNacimiConvetEdad = "-";
        }
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
    }
    
    public Date getMaxDate(){
        return new Date();
    }
    
    public void camZona(){
        if(zona.equals("1775")){
            desBarrio = "COMUNA Y BARRIO";
        }else{
            desBarrio = "VEREDA Y CORREGIMIENTO";
        }
    }

    public PacientesMB() {
        aplicacionGeneralMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
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
    
    public void validar_ocupacion(){
        if(ocupacion.equals("1603")){ 
            ver_otra_ocupacion = true;
        }else{
            if(años <= 13){
                ocupacion = "1654";
            }
            ver_otra_ocupacion = false;
        }
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
    }
    
    public void validar_edad(){
        System.out.println(años);
        System.out.println(estadoCivil);
        System.out.println(ocupacion);
        if(años <= 13){
            estadoCivil = "1881";
            ocupacion = "1654";
            RequestContext.getCurrentInstance().update("IdFormPrincipal");
        }else{
            if(estadoCivil.equals("1881")){
                estadoCivil = "246";
                RequestContext.getCurrentInstance().update("IdFormPrincipal");
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
        identificacion = null;
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
        cambiaFechaNacimiento(null);
        cargarMunicipios();
    }

    public void cargarPacienteDesdeHistorias(String id) {//se llama a esta funcion desde historias para que cargue un paciente
        pacienteSeleccionadoTabla = pacientesFachada.find(Integer.parseInt(id));
        setIdentificacion(id);
        cargarPaciente();
    }
    /***
     * Método que carga los contratos a partir de la administración
     */
    public void validarContrato(){
        try {
            if(!administradora.equals(""))
            listaContrato = contratoFacade.buscarPorAdministrador(Integer.parseInt(administradora));
        } catch (Exception e) {
        }
    }

    public void cargarPaciente() {
        if (pacienteSeleccionadoTabla == null) {
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        limpiarFormulario();
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
        identificacion = pacienteSeleccionado.getIdentificacion();
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
        cambiaFechaNacimiento(null);
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
        if(pacienteSeleccionado.getDireccion() != null){
            direccion = pacienteSeleccionado.getDireccion();
        }
        email = pacienteSeleccionado.getEmail();
        activo = pacienteSeleccionado.getActivo();
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            administradora = pacienteSeleccionado.getIdAdministradora().getIdAdministradora().toString();
            validarContrato();
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

        if(pacienteSeleccionado.getIdContrato()!=null){
            contrato = pacienteSeleccionado.getIdContrato().getIdContrato().toString();
        }else{
            contrato = "";
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
        System.out.println("Paciente ---> Id     :  " + pacienteSeleccionado.getIdPaciente());
        System.out.println("Paciente ---> Nombre :  " + pacienteSeleccionado.getNombreCompleto());
        RequestContext.getCurrentInstance().update("IdFormPrincipal");
    }

    public void guardarPaciente() {
        CfgPacientes pacienteTmp;
        //VALIDACION DE DATOS OBLIGATORIOS
        if (validacionCampoVacio(identificacion+"", "Identificación")) {
            return;
        }
        if (validacionCampoVacio(tipoIdentificacion, "Tipo de identificación")) {
            return;
        }
        if (fechaNacimiento == null) {
            imprimirMensaje("Error", "La fecha de nacimiento es obligatoria", FacesMessage.SEVERITY_ERROR);
            return;
        }
        
        if (validacionCampoVacio(primerNombre, "Primer Nombre")) {
            return;
        }
        if (validacionCampoVacio(primerApellido, "Primer Apellido")) {
            return;
        }
        if (validacionCampoVacio(celular, "Celular")) {
            return;
        }
        if (validacionCampoVacio(departamento, "Departamento")) {
            return;
        }
        if (validacionCampoVacio(barrio, desBarrio)) {
            return;
        }
        if (validacionCampoVacio(direccion, "Dirección")) {
            return;
        }
        if (!email.equals("")) {
            // The email matcher
            Matcher matcher = EMAIL_COMPILED_PATTERN.matcher(email);

            if (!matcher.matches()) {   // Email doesn't match
                imprimirMensaje("Error", "Formato de Email Invalido", FacesMessage.SEVERITY_ERROR);
                return;
            }
        }
        //VALIDACION DE VALORES UNICOS
        if (pacienteSeleccionado == null) {//guardando nuevo paciente                 
            if (pacientesFachada.buscarPorIdentificacion(identificacion+"") != null) {
                imprimirMensaje("Error", "Ya existe un paciente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            guardarNuevoPaciente();
        } else {//modificando paciente existente            
            pacienteTmp = pacientesFachada.buscarPorIdentificacion(identificacion+"");
            if (pacienteTmp != null && !pacienteSeleccionado.getIdentificacion().equals(pacienteTmp.getIdentificacion())) {
                imprimirMensaje("Error", "Existe un paciente diferente con esta identificación", FacesMessage.SEVERITY_ERROR);
                return;
            }
            actualizarPacienteExistente();
        }
        tabActivaPacientes = "0";
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
        nuevoPaciente.setIdentificacion(identificacion+"");
        if (validarNoVacio(tipoIdentificacion)) {
            nuevoPaciente.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        }
        nuevoPaciente.setLugarExpedicion(lugarExpedicion);
        if (fechaNacimiento != null) {
            nuevoPaciente.setFechaNacimiento(fechaNacimiento);
        }
        if (validarNoVacio(genero)) {
            nuevoPaciente.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        }
        if (validarNoVacio(grupoSanguineo)) {
            nuevoPaciente.setGrupoSanguineo(clasificacionesFachada.find(Integer.parseInt(grupoSanguineo)));
        }
        nuevoPaciente.setPrimerNombre(primerNombre.toUpperCase());
        nuevoPaciente.setSegundoNombre(segundoNombre.toUpperCase());
        nuevoPaciente.setPrimerApellido(primerApellido.toUpperCase());
        nuevoPaciente.setSegundoApellido(segundoApellido.toUpperCase());
        if (validarNoVacio(estadoCivil)) {
            nuevoPaciente.setEstadoCivil(clasificacionesFachada.find(Integer.parseInt(estadoCivil)));
        }
        if (validarNoVacio(ocupacion)) {
            nuevoPaciente.setOcupacion(clasificacionesFachada.find(Integer.parseInt(ocupacion)));
        }

        if (validarNoVacio(religion)) {
            nuevoPaciente.setReligion(clasificacionesFachada.find(Integer.parseInt(religion)));
        }

        if (validarNoVacio(gestacion)) {
            nuevoPaciente.setGestacion(clasificacionesFachada.find(Integer.parseInt(gestacion)));
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
        }
        if (validarNoVacio(tipoAfiliado)) {
            nuevoPaciente.setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        }
        if (validarNoVacio(regimen)) {
            nuevoPaciente.setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
        }
        if (validarNoVacio(categoriaPaciente)) {
            nuevoPaciente.setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        }
        if (validarNoVacio(estrato)) {
            nuevoPaciente.setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
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
        if (validarNoVacio(parentesco_a)) {
            nuevoPaciente.setParentesco_a(clasificacionesFachada.find(Integer.parseInt(parentesco_a)));
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
        
        if(!contrato.equals("0")){
            nuevoPaciente.setIdContrato(contratoFacade.find(Integer.parseInt(contrato)));
            if(regimen.equals("1741")){
                tipoAfiliado = "";
            }
            pacientesFachada.create(nuevoPaciente);
            imprimirMensaje("Correcto", "Nuevo paciente creado correctamente", FacesMessage.SEVERITY_INFO);
            listaPacientes = new LazyPacienteDataModel(pacientesFachada);
            limpiarFormulario();//limpiar formulario
        }else{
            imprimirMensaje("Error", "Debe seleccionar un contrato", FacesMessage.SEVERITY_ERROR);
        }
    } 
    
    public void validarIdentificacion() {
        System.out.println(tipoIdentificacion+""+identificacion);
        int cedula = pacientesFachada.numeroCedulas(identificacion+"");
        if (cedula > 0) {
            imprimirMensaje("Informacion", "El paciente ya se encuentra registrado", FacesMessage.SEVERITY_ERROR);
            nuevoRegistro = true;
            listaPacientes = new LazyPacienteDataModel(pacientesFachada);
            limpiarFormulario();
        }
    }

    private void actualizarPacienteExistente() {
        String nombreImagenEnTmp;
        String extension;
        String nombreImagenReal;
        if (archivoFirma != null) {//se cargo imagen
            nombreImagenReal = archivoFirma.getFileName();
            extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (pacienteSeleccionado.getFirma() != null) {//existe firma
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + pacienteSeleccionado.getFirma().getId() + extension);
            } else {//no existe firma
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "firmaPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFirmas() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + extension);
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/firmas/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                pacienteSeleccionado.setFirma(nuevaImagen);
            }
        }
        if (archivoFoto != null || fotoTomadaWebCam) {//se cargo foto
            if (fotoTomadaWebCam) {//es por webCam
                nombreImagenReal = "fotoWebCam.png";
                extension = ".png";
            } else {//es por carga de archivo
                nombreImagenReal = archivoFoto.getFileName();
                extension = nombreImagenReal.substring(nombreImagenReal.lastIndexOf("."), nombreImagenReal.length());
            }
            nombreImagenEnTmp = "fotoPaciente" + loginMB.getUsuarioActual().getIdUsuario() + extension;
            if (pacienteSeleccionado.getFoto() != null) {//existe foto
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + pacienteSeleccionado.getFoto().getId() + extension);
                pacienteSeleccionado.getFoto().setNombreEnServidor(pacienteSeleccionado.getFoto().getId() + extension);
                pacienteSeleccionado.getFoto().setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + pacienteSeleccionado.getFoto().getId() + extension);
                imagenesFacade.edit(pacienteSeleccionado.getFoto());
            } else {//no existe foto
                CfgImagenes nuevaImagen = new CfgImagenes();
                imagenesFacade.create(nuevaImagen);//crearlo para que me autogenere el ID            
                nombreImagenEnTmp = "fotoPaciente"
                        + loginMB.getUsuarioActual().getIdUsuario() + extension;
                moverArchivo(loginMB.getUrltmp() + nombreImagenEnTmp, loginMB.getUrlFotos() + nuevaImagen.getId().toString() + extension);
                nuevaImagen.setNombre(nombreImagenReal);
                nuevaImagen.setNombreEnServidor(nuevaImagen.getId().toString() + ".png");
                nuevaImagen.setUrlImagen(loginMB.getBaseDeDatosActual() + "/fotos/" + nuevaImagen.getId().toString() + extension);
                imagenesFacade.edit(nuevaImagen);
                pacienteSeleccionado.setFoto(nuevaImagen);
            }
        }

        pacienteSeleccionado.setIdentificacion(identificacion+"");
        if (validarNoVacio(tipoIdentificacion)) {
            pacienteSeleccionado.setTipoIdentificacion(clasificacionesFachada.find(Integer.parseInt(tipoIdentificacion)));
        }
        pacienteSeleccionado.setLugarExpedicion(lugarExpedicion);
        pacienteSeleccionado.setFechaNacimiento(fechaNacimiento);
        if (validarNoVacio(genero)) {
            pacienteSeleccionado.setGenero(clasificacionesFachada.find(Integer.parseInt(genero)));
        }
        if (validarNoVacio(grupoSanguineo)) {
            pacienteSeleccionado.setGrupoSanguineo(clasificacionesFachada.find(Integer.parseInt(grupoSanguineo)));
        }
        pacienteSeleccionado.setPrimerNombre(primerNombre.toUpperCase());
        pacienteSeleccionado.setSegundoNombre(segundoNombre.toUpperCase());
        pacienteSeleccionado.setPrimerApellido(primerApellido.toUpperCase());
        pacienteSeleccionado.setSegundoApellido(segundoApellido.toUpperCase());
        if (validarNoVacio(estadoCivil)) {
            pacienteSeleccionado.setEstadoCivil(clasificacionesFachada.find(Integer.parseInt(estadoCivil)));
        }
        if (validarNoVacio(ocupacion)) {
            pacienteSeleccionado.setOcupacion(clasificacionesFachada.find(Integer.parseInt(ocupacion)));
        }
        pacienteSeleccionado.setTelefonoResidencia(telefonoResidencia);
        pacienteSeleccionado.setTelefonoOficina(telefonoOficina);
        pacienteSeleccionado.setCelular(celular);
        if (departamento != null && departamento.length() != 0) {
            pacienteSeleccionado.setDepartamento(clasificacionesFachada.find(Integer.parseInt(departamento)));
            pacienteSeleccionado.setMunicipio(clasificacionesFachada.find(Integer.parseInt(municipio)));
        }
        if (validarNoVacio(zona)) {
            pacienteSeleccionado.setZona(clasificacionesFachada.find(Integer.parseInt(zona)));
        }
        pacienteSeleccionado.setBarrio(barrio);
        pacienteSeleccionado.setDireccion(direccion);
        pacienteSeleccionado.setEmail(email);
        pacienteSeleccionado.setActivo(activo);
        if (validarNoVacio(administradora)) {
            pacienteSeleccionado.setIdAdministradora(administradoraFacade.find(Integer.parseInt(administradora)));
        }
        if (validarNoVacio(regimen)) {
            pacienteSeleccionado.setRegimen(clasificacionesFachada.find(Integer.parseInt(regimen)));
            if(regimen.equals("1741")){
                tipoAfiliado = "";
            }
        }
        if (validarNoVacio(tipoAfiliado)) {
            pacienteSeleccionado.setTipoAfiliado(clasificacionesFachada.find(Integer.parseInt(tipoAfiliado)));
        }
        if (validarNoVacio(categoriaPaciente)) {
            pacienteSeleccionado.setCategoriaPaciente(clasificacionesFachada.find(Integer.parseInt(categoriaPaciente)));
        }
        if (validarNoVacio(estrato)) {
            pacienteSeleccionado.setNivel(clasificacionesFachada.find(Integer.parseInt(estrato)));
        }
        if (validarNoVacio(etnia)) {
            pacienteSeleccionado.setEtnia(clasificacionesFachada.find(Integer.parseInt(etnia)));
        }
        if (validarNoVacio(escolaridad)) {
            pacienteSeleccionado.setEscolaridad(clasificacionesFachada.find(Integer.parseInt(escolaridad)));
        }
        pacienteSeleccionado.setNumeroAutorizacion(numeroAutorizacion);
        pacienteSeleccionado.setResponsable(responsable);
        pacienteSeleccionado.setTelefonoResponsable(telefonoResponsable);
        if (validarNoVacio(parentesco)) {
            pacienteSeleccionado.setParentesco(clasificacionesFachada.find(Integer.parseInt(parentesco)));
        }
        if (validarNoVacio(parentesco_a)) {
            pacienteSeleccionado.setParentesco_a(clasificacionesFachada.find(Integer.parseInt(parentesco_a)));
        }
        pacienteSeleccionado.setAcompanante(acompanante);
        pacienteSeleccionado.setTelefonoAcompanante(telefonoAcompanante);

        if (fechaAfiliacion != null) {
            pacienteSeleccionado.setFechaAfiliacion(fechaAfiliacion);
        }
        if (fechaSisben != null) {
            pacienteSeleccionado.setFechaSisben(fechaSisben);
        }
        pacienteSeleccionado.setCarnet(carnet);
        if (fechaVenceCarnet != null) {
            pacienteSeleccionado.setFechaVenceCarnet(fechaVenceCarnet);
        }
        pacienteSeleccionado.setObservaciones(observaciones);

        if (validarNoVacio(religion)) {
            pacienteSeleccionado.setReligion(clasificacionesFachada.find(Integer.parseInt(religion)));
        }

        if (validarNoVacio(gestacion)) {
            pacienteSeleccionado.setGestacion(clasificacionesFachada.find(Integer.parseInt(gestacion)));
        }

        if (validarNoVacio(discapacidad)) {
            pacienteSeleccionado.setDiscapacidad(clasificacionesFachada.find(Integer.parseInt(discapacidad)));
        }

        pacienteSeleccionado.setVictimaConflicto(victimaConflicto);
        pacienteSeleccionado.setVictimaMaltrato(victimaMaltrato);
        pacienteSeleccionado.setPoblacionLBGT(poblacionLBGT);
        pacienteSeleccionado.setDesplazado(desplazado);
        
        if(!contrato.equals("0")){
            pacienteSeleccionado.setIdContrato(contratoFacade.find(Integer.parseInt(contrato)));
            pacientesFachada.edit(pacienteSeleccionado);
            imprimirMensaje("Correcto", "Paciente actualizado correctamente", FacesMessage.SEVERITY_INFO);
            listaPacientes = new LazyPacienteDataModel(pacientesFachada);
            limpiarFormulario();//limpiar formulario
        }else{
            imprimirMensaje("Error", "Debe seleccionar un contrato", FacesMessage.SEVERITY_ERROR);
        }
    }

    private UploadedFile file;
    private String newFileName = "";

    public void importarPacientes(FileUploadEvent event) {
        try {
            file = event.getFile();
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            newFileName = file.getFileName();
        } catch (Exception ex) {
            System.out.println("Error 20 en " + this.getClass().getName() + ":" + ex.toString());
            FacesMessage msg = new FacesMessage("Error:", "error al realizar la carga del archivo" + ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

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

    public void eliminarPaciente() {
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "No se ha cargado ningún paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        try {
            pacientesFachada.remove(pacienteSeleccionado);
            pacienteSeleccionado = null;
            //listaPacientes=new LazyPacienteDataModel(pacientesFachada.numeroTotalRegistros(),pacientesFachada.consultaNativaPacientes("SELECT * FROM cfg_pacientes LIMIT 10"),pacientesFachada);
            listaPacientes = new LazyPacienteDataModel(pacientesFachada);
            limpiarFormulario();//limpiar formulario
            imprimirMensaje("Correcto", "El registro fue eliminado", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error", "El paciente que se intenta eliminar tiene actividades dentro del sistema; por lo cual no puede ser eliminado.", FacesMessage.SEVERITY_ERROR);
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

    //---------------------------------------------------
    //-----------------FUNCIONES GET Y SET --------------
    //---------------------------------------------------
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

    public void setVictimaMaltrato(Boolean victimaMaltrato) {
        this.victimaMaltrato = victimaMaltrato;
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

    public List<FacContrato> getListaContrato() {
        return listaContrato;
    }

    public void setListaContrato(List<FacContrato> listaContrato) {
        this.listaContrato = listaContrato;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public FacContratoFacade getContratoFacade() {
        return contratoFacade;
    }

    public void setContratoFacade(FacContratoFacade contratoFacade) {
        this.contratoFacade = contratoFacade;
    }

    public String getOtra_ocupacion() {
        return otra_ocupacion;
    }

    public void setOtra_ocupacion(String otra_ocupacion) {
        this.otra_ocupacion = otra_ocupacion;
    }

    public Boolean getVer_otra_ocupacion() {
        return ver_otra_ocupacion;
    }

    public void setVer_otra_ocupacion(Boolean ver_otra_ocupacion) {
        this.ver_otra_ocupacion = ver_otra_ocupacion;
    }

    public Boolean getVer_ocupacion() {
        return ver_ocupacion;
    }

    public void setVer_ocupacion(Boolean ver_ocupacion) {
        this.ver_ocupacion = ver_ocupacion;
    }

    public Boolean getVer_estado_civil() {
        return ver_estado_civil;
    }

    public void setVer_estado_civil(Boolean ver_estado_civil) {
        this.ver_estado_civil = ver_estado_civil;
    }

    public String getDesBarrio() {
        return desBarrio;
    }

    public void setDesBarrio(String desBarrio) {
        this.desBarrio = desBarrio;
    }

    public String getParentesco_a() {
        return parentesco_a;
    }

    public void setParentesco_a(String parentesco_a) {
        this.parentesco_a = parentesco_a;
    }

    public int getAños() {
        return años;
    }

    public void setAños(int años) {
        this.años = años;
    }
}