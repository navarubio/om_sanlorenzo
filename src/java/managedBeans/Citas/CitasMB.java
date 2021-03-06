/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.utilidades.LazyAgendaModel;
import beans.utilidades.LazyPacienteDataModel;
import beans.utilidades.LazyPrestadorDataModel;
import modelo.entidades.CitAutorizaciones;
import modelo.entidades.CitCitas;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.FacAdministradora;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitTurnos;
import modelo.fachadas.CitAutorizacionesFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CitTurnosFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.context.RequestContext;
import beans.utilidades.MetodosGenerales;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgDiasNoLaborales;
import modelo.entidades.CitAutorizacionesServicios;
import modelo.entidades.CitAutorizacionesServiciosPK;
import modelo.entidades.FacManualTarifarioServicio;
import modelo.entidades.FacServicio;
import modelo.entidades.PyPProgramaItem;
import modelo.entidades.PyPprograma;
import modelo.fachadas.CfgDiasNoLaboralesFacade;
import modelo.fachadas.CfgPacientesFacade;
import modelo.fachadas.PyPprogramaItems;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitAutorizacionesServiciosFacade;
import modelo.fachadas.FacManualTarifarioFacade;
import modelo.fachadas.FacServicioFacade;
import modelo.fachadas.PyPProgramas;
import modelo.fachadas.PyPprogramAsoc;
import modelo.fachadas.PypProgramaCitas;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author mario
 */
@ManagedBean(name = "citasMB")
@SessionScoped
public class CitasMB extends MetodosGenerales implements Serializable {

    private long idCitas;

    //informacion paciente
    private String tipoIdentificacion;
    private String identificacion;
    private CfgPacientes pacienteSeleccionado;
    private String displayPaciente = "none";//muestra u oculta campos del paciente
    private boolean hayPacienteSeleccionado;
    private List<SelectItem> listaServicios;
    private LazyDataModel<CfgPacientes> listaPacientes;
    private String idTurno;

    //informacion prestador
    private CfgUsuarios prestadorSeleccionado;
    private String id_prestador;
    private String nombreCompleto;
    private String displayPrestador = "none";//muestra u oculta campos del prestador
    private List<SelectItem> listaEspecialidades;
    private boolean hayPrestadorSeleccionado;
    private LazyDataModel<CfgUsuarios> listaPrestadores;

    private CitTurnos turnoSeleccionado;
    private CitCitas citaSeleccionada;

    private boolean estado;
    private int motivoConsultas;
    private int motivoCancelacion;
    private String descripcionCancelacion;
    private int idPrograma;
    private int idServicio;
    private String nombreServicio;
    private String numAutorizacion;
    private int sesionesAutorizadas = 1;
    private CitAutorizaciones autorizacionSeleccionada;
    private CitAutorizacionesServicios autorizacionServicioSeleccionado;
    private boolean autorizacionrequerida = false;
    private boolean autorizacionvalidada = false;
    private boolean rendBtnAutorizacion = false;
//opcion escogida para cancelar citas

    //AGENDA
    private LazyAgendaModel evenModel;
    private ScheduleEvent event = new DefaultScheduleEvent();
    private boolean rend = false;
    private boolean rendBtnReservar = false;
    private boolean rendBtnElegir = false;
    private boolean renderizarbotones = false;//renderiza los botones de recordatorio, cancelar cita y facturar

    private List<CitCitas> listaCitas;
    // lista para reportes
    // private List<CitCitas> listaCitasDos;

    //lista de citas creadas
    private List<SelectItem> listaTipoCitas = null;
    private CitCitas citaSelecionada;
    private int sede;
    private List<SelectItem> listaMotivo;

    //Programas
    private PyPprograma programaSeleccionadoTabla;
    public List<PyPProgramaItem> listaMedicamentosPrograma;
    public List<PyPProgramaItem> listaServiciosPrograma;
    public PyPProgramaItem servicioSeleccionadoTabla;
    public PyPProgramaItem medicamentoSeleccionadoTabla;
    private String semaforo="../recursos/img/semaforo3.png";
//    private pypbean programas = new pypbean();

    @EJB
    PyPProgramas ProgramaFacade;

    @EJB
    PyPprogramAsoc ProgramaFacadeAsoc;

    @EJB
    PyPprogramaItems ProgramaFacadeItem;

    @EJB
    CitCitasFacade citasFacade;

    @EJB
    PypProgramaCitas citasProgramaCitas;

    @EJB
    CitTurnosFacade turnosFacade;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    CitAutorizacionesFacade autorizacionesFacade;

    @EJB
    CitAutorizacionesServiciosFacade autorizacionesServiciosFacade;

    @EJB
    FacServicioFacade facServicioFacade;

    @EJB
    CfgPacientesFacade pacientesFachada;

    @EJB
    CfgUsuariosFacade usuariosFachada;
    @EJB
    FacManualTarifarioFacade manualFacade;

    @EJB
    CfgDiasNoLaboralesFacade noLaborablesFacade;

    public CitasMB() {
    }

    @PostConstruct
    private void inicializar() {

        listaMedicamentosPrograma = new ArrayList<>();
        //-----------
        setListaPacientes(new LazyPacienteDataModel(pacientesFachada));
        listaServicios = new ArrayList();
        listaMotivo = new ArrayList();
        setListaPrestadores(new LazyPrestadorDataModel(usuariosFachada));
        //setListaPrestadores(prestadoresFachada.findAll());
        cargarEspecialidadesPrestadores();
        listaCitas = new ArrayList();
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        sede = loginMB.getCentroDeAtencionactual().getIdSede();
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

    //-----------------------------------------------------------------------------------
    //-----------------------METODOS DE CARGAR PACIENTE----------------------------------
    //-----------------------------------------------------------------------------------    
    public void findPaciente() {
        if (getIdentificacion() != null) {
            if (!identificacion.isEmpty()) {

                //setPacienteSeleccionado(null);
                pacienteSeleccionado = pacientesFachada.buscarPorIdentificacion(getIdentificacion());
                if (pacienteSeleccionado == null) {
                    imprimirMensaje("Error", "No se encontro el paciente", FacesMessage.SEVERITY_ERROR);
                    setHayPacienteSeleccionado(false);
                    setDisplayPaciente("none");
                } else {
                    pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
                    setDisplayPaciente("block");
                    setPacienteSeleccionado(pacienteSeleccionado);
                    setHayPacienteSeleccionado(true);
                    RequestContext.getCurrentInstance().update("cargar");
                    //buscarPrograma();
                    //descomentar cuando los servicios dependen del genero, edad, zona y administradora del paciente
                    //loadServicios(null);
                    try {
                        semaforo = ProgramaFacadeItem.validarSemaforo(calcularEdadInt(pacienteSeleccionado.getFechaNacimiento()), calcularEdadMes(pacienteSeleccionado.getFechaNacimiento()),pacienteSeleccionado.getGenero().getObservacion(),  pacienteSeleccionado.getIdAdministradora().getIdAdministradora(), pacienteSeleccionado.getIdPaciente());
                        CargarServicios();
                    } catch (Exception e) {
                    }
                    
                }
            } else {
                setPacienteSeleccionado(null);
                setDisplayPaciente("none");
                setHayPacienteSeleccionado(false);
            }
        } else {
            setPacienteSeleccionado(null);
            setDisplayPaciente("none");
            setHayPacienteSeleccionado(false);
        }
        limpiarServicioMotivoConsulta();
        //loadEvents();

    }

    public void crearPaciente() {
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Paciente','configuraciones/pacientes.xhtml','-')");
    }

    public void actualizarPaciente() {
        if (pacienteSeleccionado != null) {
            setHayPacienteSeleccionado(true);
            setIdentificacion(pacienteSeleccionado.getIdentificacion());
            if (pacienteSeleccionado.getFechaNacimiento() != null) {
                pacienteSeleccionado.setEdad(calcularEdad(pacienteSeleccionado.getFechaNacimiento()));
            }
            setDisplayPaciente("block");
            setTipoIdentificacion(String.valueOf(pacienteSeleccionado.getTipoIdentificacion().getId()));
            try {
                semaforo = ProgramaFacadeItem.validarSemaforo(calcularEdadInt(pacienteSeleccionado.getFechaNacimiento()), calcularEdadMes(pacienteSeleccionado.getFechaNacimiento()),pacienteSeleccionado.getGenero().getObservacion(),  pacienteSeleccionado.getIdAdministradora().getIdAdministradora(), pacienteSeleccionado.getIdPaciente());
            } catch (Exception e) {
            }
            
        }
        CargarServicios();
        limpiarServicioMotivoConsulta();
    }

    private void CargarServicios() {
        listaServicios.clear();
        listaServicios = new ArrayList();
        idServicio = 0;
        if (pacienteSeleccionado.getIdContrato() != null) {
            List<FacManualTarifarioServicio> servicios = pacienteSeleccionado.getIdContrato().getIdManualTarifario().getFacManualTarifarioServicioList();
            for (FacManualTarifarioServicio servicio : servicios) {
                if (servicio.getFacServicio().getIdServicio() == 5865) {
                    getListaServicios().add(new SelectItem(servicio.getFacServicio().getIdServicio(), servicio.getFacServicio().getCodigoServicio() + " - " + servicio.getFacServicio().getNombreServicio()));
                    idServicio = 5865;
                }
            }
            for (FacManualTarifarioServicio servicio : servicios) {
                if (servicio.getFacServicio().getIdServicio() != 5865) {
                    getListaServicios().add(new SelectItem(servicio.getFacServicio().getIdServicio(), servicio.getFacServicio().getCodigoServicio() + " - " + servicio.getFacServicio().getNombreServicio()));
                } else {
                    idServicio = 5865;
                }
            }
        } else {
            imprimirMensaje("Error", "El paciente no esta asosiado a un Contrato", FacesMessage.SEVERITY_ERROR);
        }
//        RequestContext.getCurrentInstance().execute("PF('selectipocita').selectValue("+idServicio+")");
    }

    //----------------------------------------------------------------------------------
    //-----------------------METODOS DE CARGAR PRESTADOR--------------------------------
    //----------------------------------------------------------------------------------
    private void cargarEspecialidadesPrestadores() {
        setListaEspecialidades((List<SelectItem>) new ArrayList());
        List<CfgClasificaciones> lista = usuariosFachada.findEspecialidades();
        for (CfgClasificaciones especialidad : lista) {
            if (especialidad != null) {
                getListaEspecialidades().add(new SelectItem(especialidad.getId(), especialidad.getDescripcion()));
            }
        }
    }

    public void functionDisplay() {
        if (getPrestadorSeleccionado() != null) {
            setDisplayPrestador("block");
        } else {
            setDisplayPrestador("none");
        }
    }

    public void findprestador() {

        if (id_prestador == null) {
            setEvenModel(null);
            return;
        }
        if (!id_prestador.isEmpty()) {
            try {
                setPrestadorSeleccionado(usuariosFachada.buscarPorIdentificacion(getId_prestador()));
                if (getPrestadorSeleccionado() == null) {

                    setHayPrestadorSeleccionado(false);
                    imprimirMensaje("Error", "No se encontro el prestador", FacesMessage.SEVERITY_ERROR);
                    setNombreCompleto(null);
                } else {
                    setPrestadorSeleccionado(getPrestadorSeleccionado());
                    setHayPrestadorSeleccionado(true);
                    setNombreCompleto(obtenerNombreCompleto());
                }
                functionDisplay();
                loadEvents();
            } catch (Exception e) {
                imprimirMensaje("Error", "Ingrese un codigo de prestador valido", FacesMessage.SEVERITY_ERROR);
                setHayPrestadorSeleccionado(false);
                setNombreCompleto(null);
                setDisplayPrestador("none");
            }
        } else {
            setPrestadorSeleccionado(null);
            functionDisplay();
            setHayPrestadorSeleccionado(false);
            setNombreCompleto(null);
            setEvenModel(null);
        }
    }

    private String obtenerNombreCompleto() {
        setNombreCompleto("");
        if (getPrestadorSeleccionado().getPrimerNombre() != null) {
            setNombreCompleto(getNombreCompleto() + getPrestadorSeleccionado().getPrimerNombre());
        }
        if (getPrestadorSeleccionado().getSegundoNombre() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoNombre());
        }
        if (getPrestadorSeleccionado().getPrimerApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getPrimerApellido());
        }
        if (getPrestadorSeleccionado().getSegundoApellido() != null) {
            setNombreCompleto(getNombreCompleto() + " " + getPrestadorSeleccionado().getSegundoApellido());
        }
        return getNombreCompleto();
    }

    public void actualizarPrestador(ActionEvent actionEvent) {
        if (getPrestadorSeleccionado() != null) {
            setId_prestador(getPrestadorSeleccionado().getIdentificacion());
            setHayPrestadorSeleccionado(true);
            obtenerNombreCompleto();
        }
        loadEvents();
        functionDisplay();
    }
    //-----------------------------------------------------------------------------------
    //---------------------------METODOS DE GESTION DE CITAS-----------------------------
    //-----------------------------------------------------------------------------------

    public void cargarCitas() {
        //setListaCitas(citasFacade.findCitasByPrestador(idPrestador));
        setListaCitas(citasFacade.findCitas());
    }

    public void guardarCita() throws ParseException {
        //System.out.println("Guardando cita ...");

        if (pacienteSeleccionado == null) {
            imprimirMensaje("Error", "Necesita ingresar el paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (pacienteSeleccionado.getIdAdministradora() == null) {
            imprimirMensaje("Error", "El paciente no tiene asignado una Admnistradora y tampoco esta especificado como Particular", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (getPrestadorSeleccionado() == null) {
            imprimirMensaje("Error", "Necesita ingresar el prestador", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (turnoSeleccionado == null) {
            imprimirMensaje("Error", "Necesita elegir el turno de la cita", FacesMessage.SEVERITY_ERROR);
            return;
        }
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        Date fechaActual = formatter.parse(formatter.format(fecha));
        if (turnoSeleccionado.getFecha().before(fechaActual)) {
            imprimirMensaje("Error", "El turno seleccionado esta programado para una fecha previa", FacesMessage.SEVERITY_ERROR);
            return;
        }

        CfgDiasNoLaborales noLabora = noLaborablesFacade.FindDiaNoLaboraleBySede(sede, turnoSeleccionado.getFecha());
        if (noLabora != null) {
            imprimirMensaje("Error", "El dia del turno seleccionado es no laborable", FacesMessage.SEVERITY_ERROR);
            return;
        }

        //variable que controla si el servicio o tipo de cita seleccionado requiere autorizacion
//        boolean ban = false;
        if (idServicio != 0) {
            FacServicio facServicio = facServicioFacade.find(idServicio);
//            if (facServicio.getAutorizacion()) {
            validarAutorizacion(0);
            if (!autorizacionvalidada && autorizacionrequerida) {
                imprimirMensaje("Error", "Para crear la cita se requiere autorizacion", FacesMessage.SEVERITY_ERROR);
                return;
            }
//                ban = true;
//            }

        } else {
            imprimirMensaje("Error", "Es necesario elegir el servicio", FacesMessage.SEVERITY_ERROR);
            return;

        }

//        if (motivoConsulta == 0) {
//            imprimirMensaje("Error", "Especifique el motivo de consulta", FacesMessage.SEVERITY_ERROR);
//            return;
//        }
        List<Integer> listaIdTurno = new ArrayList();
        listaIdTurno.add(turnoSeleccionado.getIdTurno());
        int totalTurnoDisponible = turnosFacade.totalTurnosDisponibles(listaIdTurno);
        if (totalTurnoDisponible != 1) {
            loadEvents();
            imprimirMensaje("Error", "El turno ya se encuentra asignado", FacesMessage.SEVERITY_ERROR);
            return;
        }

        if (pacienteSeleccionado != null) {
            //creando una cita nueva
            CitCitas nuevaCita = new CitCitas();
            nuevaCita.setIdPaciente(pacienteSeleccionado);
            nuevaCita.setIdPrestador(getPrestadorSeleccionado());
            nuevaCita.setIdTurno(turnoSeleccionado);
            nuevaCita.setIdPaquete(null);
            CfgClasificaciones clasificaciones;
            if (motivoConsultas != 0) {
                clasificaciones = clasificacionesFachada.find(motivoConsultas);
                nuevaCita.setTipoCita(clasificaciones);
            }
            nuevaCita.setFechaRegistro(new Date());
            nuevaCita.setIdServicio(new FacServicio(idServicio));
            nuevaCita.setFacturada(false);
            nuevaCita.setAtendida(false);
            nuevaCita.setCancelada(false);
            nuevaCita.setMultado(false);
            FacAdministradora facAdministradora = pacienteSeleccionado.getIdAdministradora();
            nuevaCita.setIdAdministradora(facAdministradora);
            //si el servicio requiere autorizacion se envia esta a la tabla de citas

            if (autorizacionrequerida) {
//                if (isAutorizacionvalidada()) {
                nuevaCita.setIdAutorizacion(getAutorizacionSeleccionada());
                //se modifica las sesiones pendientes del servicio de la autorizacion
                CitAutorizaciones autorizacion = getAutorizacionSeleccionada();
//                    una autorizacion posee varios servicios, entonces se busca el servicio de la cita en los servicios de la autorizacion para decrementar las sesiones pendientes
                CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacionDisponible(autorizacion.getIdAutorizacion(), idServicio);
                if (autorizacionServicio != null) {
                    autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() - 1);
                    autorizacionesServiciosFacade.edit(autorizacionServicio);
                }
//                } else {
//                    nuevaCita.setIdAutorizacion(null);
//                }
            } else {
                nuevaCita.setIdAutorizacion(null);
            }
            nuevaCita.setTieneRegAsociado(false);
            citasFacade.create(nuevaCita);
            if (listaMedicamentosPrograma.size() > 0) {
                int i = 0;
                while (i < listaMedicamentosPrograma.size()) {
                    modelo.entidades.PypProgramaCita c = new modelo.entidades.PypProgramaCita();
                    c.setIdProgramaItem(listaMedicamentosPrograma.get(i).getIdProgramaItems());
                    c.setIdCita(nuevaCita.getIdCita());
                    citasProgramaCitas.create(c);
                    i++;
                }
            }
            //modificando la tabla turnos: se incrementa el contador, dependiendo de la situacion el estado del turno puede llegar a ser false
            turnoSeleccionado.setContador(turnoSeleccionado.getContador() + 1);
            if (turnoSeleccionado.getContador() == turnoSeleccionado.getConcurrencia()) {
                //turno.setEstado(false);
                turnoSeleccionado.setEstado("asignado");
            }
            turnosFacade.edit(turnoSeleccionado);

            //liberando Selecciones previas
            setIdServicio(0);
            imprimirMensaje("Correto", "La cita ha sido creada.", FacesMessage.SEVERITY_INFO);
            loadEvents();
            
            //carga la lista de citas que sera usada porla tabla que muestra las citas creadas para el prestador elegido
            //cargarCitas();

            //listaCitas.add(citasFacade.findCitasByTurno(turnoSeleccionado.getIdTurno()));
            //setCitaSelecionada(citasFacade.findCitasByTurno(idTurno));
            //RequestContext.getCurrentInstance().update("result");
            //RequestContext context = RequestContext.getCurrentInstance();
            //context.execute("PF('dlgresult').show();");
        } else {
            imprimirMensaje("Error", "Es necesario seleccionar el paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        liberarCampos(1);
        limpiarServicioMotivoConsulta();

    }

    public void limpiarServicioMotivoConsulta() {
        setIdServicio(0);
        setMotivoConsultas(0);
        setAutorizacionSeleccionada(null);
        setNumAutorizacion(null);
        autorizacionvalidada = false;
        //setListaCitas(null);
        //listaCitas = new ArrayList();
    }

    public void liberarCampos(int ban) {
        if (ban == 1) {
            setPacienteSeleccionado(null);
            setPrestadorSeleccionado(null);
            setListaTipoCitas(null);
            motivoConsultas = 0;
            idServicio = 0;
        }
        listaMedicamentosPrograma = new ArrayList<>();
        //setIdPrestador(0);
        //idPaciente = 0;

        motivoCancelacion = 0;
        descripcionCancelacion = null;
        idPrograma = 0;
        //setIdServicio(0);
        autorizacionvalidada = false;
        setTurnoSeleccionado(null);
        setAutorizacionSeleccionada(null);
        setNumAutorizacion(null);

    }

    public void seleccionarCita(ActionEvent actionEvent) {
        int id_cita = (int) actionEvent.getComponent().getAttributes().get("id_cita");
        citaSelecionada = citasFacade.find(id_cita);
    }

    public void reservarCita(ActionEvent actionEvent) {
        try {
            turnoSeleccionado.setEstado("reservado");
            turnosFacade.edit(turnoSeleccionado);
        } catch (Exception e) {
            imprimirMensaje("Error", "No se logro reservar el turno " + turnoSeleccionado.getIdTurno() + " correctamente" + e.getMessage(), FacesMessage.SEVERITY_ERROR);
            return;
        }
        imprimirMensaje("Correto", "El turno " + turnoSeleccionado.getIdTurno() + " ha sido reservado", FacesMessage.SEVERITY_INFO);
        loadEvents();
        liberarCampos(0);
    }

    public void cancelarCita(ActionEvent actionEvent) {
        //System.out.println(motivoCancelacion + " - " + descripcionCancelacion);
        if (!citaSelecionada.getCancelada() && !citaSeleccionada.getAtendida() && !citaSeleccionada.getFacturada()) {
            citaSelecionada.setCancelada(true);
            CfgClasificaciones clasificaciones = clasificacionesFachada.find(motivoCancelacion);
            citaSelecionada.setDescCancelacion(descripcionCancelacion);
            citaSelecionada.setMotivoCancelacion(clasificaciones);
            Date aux = new Date();
            citaSelecionada.setFechaCancelacion(aux);
            citasFacade.edit(citaSelecionada);
            CitTurnos turno = citaSelecionada.getIdTurno();
            turno.setEstado("disponible");
            turno.setContador(turno.getContador() - 1);
            turnosFacade.edit(turno);
            if (citaSelecionada.getIdAutorizacion() != null) {
                CitAutorizaciones autorizacion = citaSelecionada.getIdAutorizacion();
                CitAutorizacionesServicios autorizacionServicio = autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), citaSeleccionada.getIdServicio().getIdServicio());
                if (autorizacionServicio != null) {
                    autorizacionServicio.setSesionesPendientes(autorizacionServicio.getSesionesPendientes() + 1);
                    autorizacionesServiciosFacade.edit(autorizacionServicio);
                }
            }
            imprimirMensaje("Correcto", "Cita " + citaSelecionada.getIdCita() + " cancelada", FacesMessage.SEVERITY_INFO);
            //actualiza los items de listaCitas
            listaCitas.remove(citaSelecionada);
            setListaCitas(listaCitas);
            //cargarCitas();
        } else {
            imprimirMensaje("Error", "Cita " + citaSelecionada.getIdCita() + " ya se encuentra cancelada, atendida o facturada.", FacesMessage.SEVERITY_ERROR);
        }
        descripcionCancelacion = null;

    }

    //ban == 1 muestra informaciona adcional cuando se valida una autorizacion
    public void validarAutorizacion(int ban) {
        setRendBtnAutorizacion(false);
        if (idServicio != 0) {
            FacServicio facServicio = facServicioFacade.find(idServicio);
            setNombreServicio(facServicio.getNombreServicio());
            if (facServicio != null) {
                if (facServicio.getAutorizacion()) {
                    if (pacienteSeleccionado != null) {
                        if (pacienteSeleccionado.getIdAdministradora() == null) {
                            idServicio = 0;
                            RequestContext.getCurrentInstance().update("formCita");
                            imprimirMensaje("Error", "Este paciente no tiene administradora, debe ingresar a pacientes y asignarle una administradora", FacesMessage.SEVERITY_ERROR);
                            return;
                        }
                        //paciente particular no necesita validar autorizaciones
                        if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                            autorizacionrequerida = false;
                            autorizacionvalidada = false;
                            setRendBtnAutorizacion(false);
                            setAutorizacionSeleccionada(null);
                            RequestContext context = RequestContext.getCurrentInstance();
                            context.update("formCita");
                            return;
                        }
                        autorizacionrequerida = true;                        
                        CitAutorizaciones autorizacion = autorizacionesFacade.findAutorizacion(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                        if (autorizacion != null) {
                            autorizacionvalidada = true;
                            setAutorizacionSeleccionada(autorizacion);
                            if (ban == 1) {
                                setAutorizacionServicioSeleccionado(autorizacionesServiciosFacade.buscarServicioPorAutorizacion(autorizacion.getIdAutorizacion(), idServicio));
                                RequestContext context = RequestContext.getCurrentInstance();
                                context.execute("PF('dlgresautorizacion').show();");
                            }
                        } else {
                            autorizacionvalidada = false;
                            autorizacion = autorizacionesFacade.findAutorizacionDos(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
                            setAutorizacionSeleccionada(null);
                            setAutorizacionServicioSeleccionado(null);
//                        RequestContext context = RequestContext.getCurrentInstance();
//                        context.update("autorizar");
                            if (ban == 1 && autorizacion == null) {
                                setRendBtnAutorizacion(true);
                                imprimirMensaje("Error", "El servicio requiere autorizacion y el paciente no posee una autorizacion vigente", FacesMessage.SEVERITY_ERROR);
                            }
                            if (ban == 1 && autorizacion != null) {
                                setAutorizacionSeleccionada(autorizacion);
                                imprimirMensaje("Error", "La autorizacion asociada al servicio no admite crear otra cita", FacesMessage.SEVERITY_ERROR);
                            }
                        }
                    }
                } else {
                    autorizacionrequerida = false;
                    autorizacionvalidada = false;
                    setNombreServicio(null);
                    setAutorizacionSeleccionada(null);
//                RequestContext context = RequestContext.getCurrentInstance();
//                context.update("autorizar");
                }
            }

        } else {
            autorizacionvalidada = false;
            autorizacionrequerida = false;
            setNombreServicio(null);
            setAutorizacionSeleccionada(null);
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.update("autorizar");
        }
        RequestContext.getCurrentInstance().update("formCita");
    }

    public void abrirTabAutorizaciones() {
        RequestContext.getCurrentInstance().execute("window.parent.cargarTab('Autorizaciones','citas/autorizaciones.xhtml','idPaciente;" + pacienteSeleccionado.getIdPaciente().toString() + ";idServicio;" + String.valueOf(idServicio) + "')");
    }

    public void crearAutorizacion() {
//        System.out.println("creando autorizacion");
        if (pacienteSeleccionado == null) {
            imprimirMensaje("Información", "Ingrese un documento valido del paciente", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (pacienteSeleccionado.getIdAdministradora() != null) {
            if (pacienteSeleccionado.getIdAdministradora().getCodigoAdministradora().equals("1")) {
                imprimirMensaje("Información", "El paciente esta registrado como particular, no necesita autorizaciones", FacesMessage.SEVERITY_ERROR);
                return;
            }
        } else {
            imprimirMensaje("Información", "El paciente no esta registrado como particular y no tiene una administradora asociada", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (idServicio == 0) {
            imprimirMensaje("Información", "No ha seleccionado un servicio", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (sesionesAutorizadas == 0) {
            imprimirMensaje("Información", "Ingrese el numero de sesiones autorizadas", FacesMessage.SEVERITY_ERROR);
            return;

        }
        if (numAutorizacion.isEmpty()) {
            imprimirMensaje("Información", "Ingrese el numero de autorizacion", FacesMessage.SEVERITY_ERROR);
            return;
        }
        if (autorizacionesFacade.findAutorizacionDisponible(pacienteSeleccionado.getIdPaciente(), idServicio, pacienteSeleccionado.getIdAdministradora().getIdAdministradora()) != null) {
            imprimirMensaje("Error", "El paciente ya cuenta con una autorizacion para este servicio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        CitAutorizaciones autorizacion = new CitAutorizaciones();
        autorizacion.setPaciente(pacienteSeleccionado);
        autorizacion.setAdministradora(pacienteSeleccionado.getIdAdministradora());
        autorizacion.setNumAutorizacion(numAutorizacion);
        LoginMB loginMB = FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        autorizacion.setIdUsuarioCreador(loginMB.getUsuarioActual());
        autorizacion.setCerrada(false);
        autorizacionesFacade.create(autorizacion);
        //ojo
        CitAutorizacionesServicios autorizacionServicio = new CitAutorizacionesServicios();
        autorizacionServicio.setFacServicio(facServicioFacade.find(idServicio));
        autorizacionServicio.setSesionesAutorizadas(sesionesAutorizadas);
        autorizacionServicio.setSesionesRealizadas(0);
        autorizacionServicio.setSesionesPendientes(sesionesAutorizadas);
        autorizacionServicio.setCitAutorizaciones(autorizacion);
        autorizacionServicio.setCitAutorizacionesServiciosPK(new CitAutorizacionesServiciosPK(autorizacion.getIdAutorizacion(), idServicio));
        autorizacionesServiciosFacade.create(autorizacionServicio);
        validarAutorizacion(1);
        imprimirMensaje("Información", "Autorizacion creada correctamente", FacesMessage.SEVERITY_INFO);
    }

    //----------------------------------------------------------------------------------
    //------------------------METHODS AGENDA CITA---------------------------------------
    //----------------------------------------------------------------------------------
    private void seleccionarCita(long id) {
        citaSeleccionada = citasFacade.findCitasByTurno(id);

        if (getCitaSeleccionada() != null) {
            setCitaSeleccionada(citaSeleccionada);
            setRend(true);
            setRenderizarbotones(true);
            setRendBtnElegir(false);
        } else {
            setCitaSeleccionada(null);
            seleccionarTurno(id);
            setRenderizarbotones(false);
            setRend(false);
            setRendBtnElegir(true);
        }
        CitTurnos turno = turnosFacade.findById(id);
        if (!turno.getEstado().equals("no_disponible")) {
            if (turno.getEstado().equals("reservado") || turno.getEstado().equals("asignado")) {
                setRendBtnReservar(false);
            } else {
                setRendBtnReservar(true);
            }
            RequestContext.getCurrentInstance().update("fstsch:eventDetails");
            RequestContext.getCurrentInstance().execute("PF('eventDialog').show()");

        }
    }

    private void seleccionarTurno(long id) {
        CitTurnos turno = turnosFacade.buscarPorId(id);
        setTurnoSeleccionado(turno);
        //turnoMB.setTurnoSeleccionado(turno);

    }

    public void loadEvents() {
        setEvenModel(new LazyAgendaModel(prestadorSeleccionado.getIdUsuario(), sede, turnosFacade, citasFacade, "citaUnica"));
    }

    public void onEventSelect(SelectEvent selectEvent) {
        setEvent((ScheduleEvent) selectEvent.getObject());
        if (event.getTitle() != null) {
            String[] vector = event.getTitle().split(" - ");
            idTurno = vector[0];
            seleccionarCita(Long.parseLong(idTurno));
        }
    }

    public void buscarPrograma() {
//        listaServiciosPrograma = ProgramaFacadeItem.buscar_programas();
        System.out.println(calcularEdadInt(pacienteSeleccionado.getFechaNacimiento()) + " " + pacienteSeleccionado.getGenero().getObservacion());
        listaServiciosPrograma = ProgramaFacadeItem.buscar_programas_val(calcularEdadInt(pacienteSeleccionado.getFechaNacimiento()), calcularEdadMes(pacienteSeleccionado.getFechaNacimiento()), pacienteSeleccionado.getGenero().getObservacion(), pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
        if (listaServiciosPrograma.isEmpty()) {
            listaServiciosPrograma = ProgramaFacadeItem.buscar_programas_val_(calcularEdadInt(pacienteSeleccionado.getFechaNacimiento()), calcularEdadMes(pacienteSeleccionado.getFechaNacimiento()), pacienteSeleccionado.getGenero().getObservacion(), pacienteSeleccionado.getIdAdministradora().getIdAdministradora());
        }
        RequestContext.getCurrentInstance().execute("PF('wvTablaServiciosManual').clearFilters(); PF('wvTablaServiciosManual').getPaginator().setPage(0);");
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').show()");
        RequestContext.getCurrentInstance().update("cargar");
    }

    public void cargarPrograma(ActionEvent actionEvent) {
        if (servicioSeleccionadoTabla == null) {
            imprimirMensaje("Error", "No se ha seleccionado ningún programa de promoción y prevención de la tabla", FacesMessage.SEVERITY_ERROR);
            return;
        }
        int i = 0, o = 0;
        if (listaMedicamentosPrograma.size() > 0) {
            while (i < listaMedicamentosPrograma.size()) {
                if (listaMedicamentosPrograma.get(i).getIdProgramaItems().equals(servicioSeleccionadoTabla.getIdProgramaItems())) {
                    imprimirMensaje("Error", "Programa ya cargado", FacesMessage.SEVERITY_ERROR);
                    o++;
                }
                i++;
            }
        }
        if (o == 0) {
            listaMedicamentosPrograma.add(servicioSeleccionadoTabla);
            RequestContext.getCurrentInstance().execute("PF('dialogoBuscarManualesTarifarios').hide()");
            RequestContext.getCurrentInstance().update("formCita");
        }
    }

    //-----------------------------------------------------------------
    //------------------------GETTERS AND SETTERS---------------------    
    //-----------------------------------------------------------------
    /**
     * @return the idCitas
     */
    public long getIdCitas() {
        return idCitas;
    }

    /**
     * @param idCitas the idCitas to set
     */
    public void setIdCitas(long idCitas) {
        this.idCitas = idCitas;
    }

    /**
     * @return the estado
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * @return the listaCitas
     */
    public List<CitCitas> getListaCitas() {
        return listaCitas;
    }

    /**
     * @param listaCitas the listaCitas to set
     */
    public void setListaCitas(List<CitCitas> listaCitas) {
        this.listaCitas = listaCitas;
    }

    /**
     * @return the citaSelecionada
     */
    public CitCitas getCitaSelecionada() {
        return citaSelecionada;
    }

    /**
     * @param citaSelecionada the citaSelecionada to set
     */
    public void setCitaSelecionada(CitCitas citaSelecionada) {
        this.citaSelecionada = citaSelecionada;
    }

    /**
     * @return the tipoConsulta
     */
    public int getMotivoConsultas() {
        return motivoConsultas;
    }

    /**
     * @param motivoConsulta the motivoConsulta to set
     */
    public void setMotivoConsultas(int motivoConsulta) {
        this.motivoConsultas = motivoConsulta;
    }

    /**
     * @return the motivoCancelacion
     */
    public int getMotivoCancelacion() {
        return motivoCancelacion;
    }

    /**
     * @param motivoCancelacion the motivoCancelacion to set
     */
    public void setMotivoCancelacion(int motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    /**
     * @return the descripcionCancelacion
     */
    public String getDescripcionCancelacion() {
        return descripcionCancelacion;
    }

    /**
     * @param descripcionCancelacion the descripcionCancelacion to set
     */
    public void setDescripcionCancelacion(String descripcionCancelacion) {
        this.descripcionCancelacion = descripcionCancelacion;
    }

    /**
     * @return the listaTipoCitas
     */
    public List<SelectItem> getListaTipoCitas() {
        return listaTipoCitas;
    }

    /**
     * @param listaTipoCitas the listaTipoCitas to set
     */
    public void setListaTipoCitas(List<SelectItem> listaTipoCitas) {
        this.listaTipoCitas = listaTipoCitas;
    }

    /**
     * @return the autorizacionrequerida
     */
    public boolean isAutorizacionrequerida() {
        return autorizacionrequerida;
    }

    /**
     * @param autorizacionrequerida the autorizacionrequerida to set
     */
    public void setAutorizacionrequerida(boolean autorizacionrequerida) {
        this.autorizacionrequerida = autorizacionrequerida;
    }

    /**
     * @return the idPrograma
     */
    public int getIdPrograma() {
        return idPrograma;
    }

    /**
     * @param idPrograma the idPrograma to set
     */
    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
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

    public LazyDataModel<CfgPacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(LazyDataModel<CfgPacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public CfgUsuarios getPrestadrSeleccionado() {
        return getPrestadorSeleccionado();
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }

    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
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

    public List<SelectItem> getListaServicios() {
        return listaServicios;
    }

    public void setListaServicios(List<SelectItem> listaServicios) {
        this.listaServicios = listaServicios;
    }

    public String getDisplayPaciente() {
        return displayPaciente;
    }

    public void setDisplayPaciente(String displayPaciente) {
        this.displayPaciente = displayPaciente;
    }

    public String getDisplayPrestador() {
        return displayPrestador;
    }

    public void setDisplayPrestador(String displayPrestador) {
        this.displayPrestador = displayPrestador;
    }

    public boolean isHayPrestadorSeleccionado() {
        return hayPrestadorSeleccionado;
    }

    public void setHayPrestadorSeleccionado(boolean hayPrestadorSeleccionado) {
        this.hayPrestadorSeleccionado = hayPrestadorSeleccionado;
    }

    public LazyDataModel<CfgUsuarios> getListaPrestadores() {
        return listaPrestadores;
    }

    public void setListaPrestadores(LazyDataModel<CfgUsuarios> listaPrestadores) {
        this.listaPrestadores = listaPrestadores;
    }

    public String getId_prestador() {
        return id_prestador;
    }

    public void setId_prestador(String id_prestador) {
        this.id_prestador = id_prestador;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public List<SelectItem> getListaEspecialidades() {
        return listaEspecialidades;
    }

    public void setListaEspecialidades(List<SelectItem> listaEspecialidades) {
        this.listaEspecialidades = listaEspecialidades;
    }

    public CfgUsuarios getPrestadorSeleccionado() {
        return prestadorSeleccionado;
    }

    public void setPrestadorSeleccionado(CfgUsuarios prestadorSeleccionado) {
        this.prestadorSeleccionado = prestadorSeleccionado;
    }

    public CitTurnos getTurnoSeleccionado() {
        return turnoSeleccionado;
    }

    public void setTurnoSeleccionado(CitTurnos turnoSeleccionado) {
        this.turnoSeleccionado = turnoSeleccionado;
    }

    public LazyAgendaModel getEvenModel() {
        return evenModel;
    }

    public void setEvenModel(LazyAgendaModel evenModel) {
        this.evenModel = evenModel;
    }

    public ScheduleEvent getEvent() {
        return event;
    }

    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }

    public boolean isRend() {
        return rend;
    }

    public void setRend(boolean rend) {
        this.rend = rend;
    }

    public boolean isRendBtnReservar() {
        return rendBtnReservar;
    }

    public void setRendBtnReservar(boolean rendBtnReservar) {
        this.rendBtnReservar = rendBtnReservar;
    }

    public boolean isRendBtnElegir() {
        return rendBtnElegir;
    }

    public void setRendBtnElegir(boolean rendBtnElegir) {
        this.rendBtnElegir = rendBtnElegir;
    }

    public CitCitas getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public void setCitaSeleccionada(CitCitas citaSeleccionada) {
        this.citaSeleccionada = citaSeleccionada;
    }

    public boolean isRenderizarbotones() {
        return renderizarbotones;
    }

    public void setRenderizarbotones(boolean renderizarbotones) {
        this.renderizarbotones = renderizarbotones;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getNumAutorizacion() {
        return numAutorizacion;
    }

    public void setNumAutorizacion(String numAutorizacion) {
        this.numAutorizacion = numAutorizacion;
    }

    public CitAutorizaciones getAutorizacionSeleccionada() {
        return autorizacionSeleccionada;
    }

    public void setAutorizacionSeleccionada(CitAutorizaciones autorizacionSeleccionada) {
        this.autorizacionSeleccionada = autorizacionSeleccionada;
    }

    public int getSesionesAutorizadas() {
        return sesionesAutorizadas;
    }

    public void setSesionesAutorizadas(int sesionesAutorizadas) {
        this.sesionesAutorizadas = sesionesAutorizadas;
    }

    public boolean isAutorizacionvalidada() {
        return autorizacionvalidada;
    }

    public void setAutorizacionvalidada(boolean autorizacionvalidada) {
        this.autorizacionvalidada = autorizacionvalidada;
    }

    public CitAutorizacionesServicios getAutorizacionServicioSeleccionado() {
        return autorizacionServicioSeleccionado;
    }

    public void setAutorizacionServicioSeleccionado(CitAutorizacionesServicios autorizacionServicioSeleccionado) {
        this.autorizacionServicioSeleccionado = autorizacionServicioSeleccionado;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public boolean isRendBtnAutorizacion() {
        return rendBtnAutorizacion;
    }

    public void setRendBtnAutorizacion(boolean rendBtnAutorizacion) {
        this.rendBtnAutorizacion = rendBtnAutorizacion;
    }

    public PyPprograma getProgramaSeleccionadoTabla() {
        return programaSeleccionadoTabla;
    }

    public void setProgramaSeleccionadoTabla(PyPprograma programaSeleccionadoTabla) {
        this.programaSeleccionadoTabla = programaSeleccionadoTabla;
    }

    public int getSede() {
        return sede;
    }

    public void setSede(int sede) {
        this.sede = sede;
    }

    public List<PyPProgramaItem> getListaMedicamentosPrograma() {
        return listaMedicamentosPrograma;
    }

    public void setListaMedicamentosPrograma(List<PyPProgramaItem> listaMedicamentosPrograma) {
        this.listaMedicamentosPrograma = listaMedicamentosPrograma;
    }

    public PyPProgramas getProgramaFacade() {
        return ProgramaFacade;
    }

    public void setProgramaFacade(PyPProgramas ProgramaFacade) {
        this.ProgramaFacade = ProgramaFacade;
    }

    public CitCitasFacade getCitasFacade() {
        return citasFacade;
    }

    public void setCitasFacade(CitCitasFacade citasFacade) {
        this.citasFacade = citasFacade;
    }

    public PypProgramaCitas getCitasProgramaCitas() {
        return citasProgramaCitas;
    }

    public void setCitasProgramaCitas(PypProgramaCitas citasProgramaCitas) {
        this.citasProgramaCitas = citasProgramaCitas;
    }

    public CitTurnosFacade getTurnosFacade() {
        return turnosFacade;
    }

    public void setTurnosFacade(CitTurnosFacade turnosFacade) {
        this.turnosFacade = turnosFacade;
    }

    public CfgClasificacionesFacade getClasificacionesFachada() {
        return clasificacionesFachada;
    }

    public void setClasificacionesFachada(CfgClasificacionesFacade clasificacionesFachada) {
        this.clasificacionesFachada = clasificacionesFachada;
    }

    public CitAutorizacionesFacade getAutorizacionesFacade() {
        return autorizacionesFacade;
    }

    public void setAutorizacionesFacade(CitAutorizacionesFacade autorizacionesFacade) {
        this.autorizacionesFacade = autorizacionesFacade;
    }

    public CitAutorizacionesServiciosFacade getAutorizacionesServiciosFacade() {
        return autorizacionesServiciosFacade;
    }

    public void setAutorizacionesServiciosFacade(CitAutorizacionesServiciosFacade autorizacionesServiciosFacade) {
        this.autorizacionesServiciosFacade = autorizacionesServiciosFacade;
    }

    public FacServicioFacade getFacServicioFacade() {
        return facServicioFacade;
    }

    public void setFacServicioFacade(FacServicioFacade facServicioFacade) {
        this.facServicioFacade = facServicioFacade;
    }

    public CfgPacientesFacade getPacientesFachada() {
        return pacientesFachada;
    }

    public void setPacientesFachada(CfgPacientesFacade pacientesFachada) {
        this.pacientesFachada = pacientesFachada;
    }

    public CfgUsuariosFacade getUsuariosFachada() {
        return usuariosFachada;
    }

    public void setUsuariosFachada(CfgUsuariosFacade usuariosFachada) {
        this.usuariosFachada = usuariosFachada;
    }

    public PyPprogramAsoc getProgramaFacadeAsoc() {
        return ProgramaFacadeAsoc;
    }

    public void setProgramaFacadeAsoc(PyPprogramAsoc ProgramaFacadeAsoc) {
        this.ProgramaFacadeAsoc = ProgramaFacadeAsoc;
    }

    public PyPprogramaItems getProgramaFacadeItem() {
        return ProgramaFacadeItem;
    }

    public void setProgramaFacadeItem(PyPprogramaItems ProgramaFacadeItem) {
        this.ProgramaFacadeItem = ProgramaFacadeItem;
    }

    public List<PyPProgramaItem> getListaServiciosPrograma() {
        return listaServiciosPrograma;
    }

    public void setListaServiciosPrograma(List<PyPProgramaItem> listaServiciosPrograma) {
        this.listaServiciosPrograma = listaServiciosPrograma;
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

    public List<SelectItem> getListaMotivo() {
        if (listaMotivo != null) {
            listaMotivo.clear();
        }
        List<SelectItem> listaRetorno = new ArrayList<>();
        List<CfgClasificaciones> listaClasificaciones = clasificacionesFachada.buscarPorMaestro("MotivoConsulta");
        for (CfgClasificaciones clasificacion : listaClasificaciones) {
            listaRetorno.add(new SelectItem(clasificacion.getId(), clasificacion.getCodigo() + " - " + clasificacion.getDescripcion()));
        }
        for (SelectItem servicio : listaRetorno) {
            if (servicio.getValue().toString().equals("475")) {
                listaMotivo.add(servicio);
            }
        }
        for (SelectItem servicio : listaRetorno) {
            if (!servicio.getValue().toString().equals("475")) {
                listaMotivo.add(servicio);
            } else {
                motivoConsultas = 475;
            }
        }
        return listaMotivo;
    }

    public void setListaMotivo(List<SelectItem> listaMotivoConsulta) {
        this.listaMotivo = listaMotivoConsulta;
    }

    public String getSemaforo() {
        return semaforo;
    }

    public void setSemaforo(String semaforo) {
        this.semaforo = semaforo;
    }

}
