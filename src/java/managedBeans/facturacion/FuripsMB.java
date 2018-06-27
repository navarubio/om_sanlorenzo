/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import managedBeans.historias.DatosFormularioHistoria;
import modelo.entidades.CfgPacientes;
import modelo.fachadas.CfgPacientesFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author migel.arango.bu
 */
@Named(value = "furipsMB")
@ViewScoped
public class FuripsMB extends MetodosGenerales implements Serializable{

    @EJB
    CfgPacientesFacade pacientesFacade;
    
    private CfgPacientes pacienteSeleccionadoTabla;
    private CfgPacientes pacienteSeleccionado;
    private CfgPacientes pacienteTmp;
    
    private String urlFoto = "../recursos/img/img_usuario.png";
    private boolean hayPacienteSeleccionado = false;
    private String identificacionPaciente = "";
    private String tipoIdentificacion = "";
    private String nombrePaciente = "Paciente";
    private String generoPaciente = "";
    private String edadPaciente = "";
    private String administradoraPaciente = "";
    private DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();
    public FuripsMB() {
    }
    
    public void validarIdentificacion() {//verifica si existe la identificacion de lo contrario abre un dialogo para seleccionar el paciente de una tabla
        pacienteTmp = pacientesFacade.buscarPorIdentificacion(identificacionPaciente);

        if (pacienteTmp != null) {
             pacienteSeleccionadoTabla = pacienteTmp;
            hayPacienteSeleccionado = true;//se pueden mostrar opciones
            cargarPaciente();

        } else {
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').show();");
        }
    }
    
    public void cargarPaciente() {//cargar un paciente desde del dialogo de buscar paciente o al digitar una identificacion valida(esta en pacientes)        
        if (pacienteSeleccionadoTabla != null) {
            pacienteSeleccionado = pacientesFacade.find(pacienteSeleccionadoTabla.getIdPaciente());
            identificacionPaciente = "";
            nombrePaciente = "Paciente";
            hayPacienteSeleccionado = true;
            identificacionPaciente = pacienteSeleccionado.getIdentificacion();
            if (pacienteSeleccionado.getTipoIdentificacion() != null) {
                tipoIdentificacion = pacienteSeleccionado.getTipoIdentificacion().getDescripcion();
            } else {
                tipoIdentificacion = "";
            }
            nombrePaciente = pacienteSeleccionado.nombreCompleto();
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
            hayPacienteSeleccionado = true;
            RequestContext.getCurrentInstance().execute("PF('dlgSeleccionarPaciente').hide();");
        } else {
            hayPacienteSeleccionado = false;
            imprimirMensaje("Error", "Se debe seleccionar un paciente de la tabla", FacesMessage.SEVERITY_ERROR);
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

    public CfgPacientes getPacienteSeleccionadoTabla() {
        return pacienteSeleccionadoTabla;
    }

    public void setPacienteSeleccionadoTabla(CfgPacientes pacienteSeleccionadoTabla) {
        this.pacienteSeleccionadoTabla = pacienteSeleccionadoTabla;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public CfgPacientes getPacienteTmp() {
        return pacienteTmp;
    }

    public void setPacienteTmp(CfgPacientes pacienteTmp) {
        this.pacienteTmp = pacienteTmp;
    }

    public boolean isHayPacienteSeleccionado() {
        return hayPacienteSeleccionado;
    }

    public void setHayPacienteSeleccionado(boolean hayPacienteSeleccionado) {
        this.hayPacienteSeleccionado = hayPacienteSeleccionado;
    }

    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }

    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
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

    public void setAdministradoraPaciente(String administradoraPaciente) {
        this.administradoraPaciente = administradoraPaciente;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }
    
    
}
