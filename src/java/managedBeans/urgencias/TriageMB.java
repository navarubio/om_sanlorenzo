/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.urgencias;

import beans.enumeradores.EstadoAdmisionPaciente;
import beans.enumeradores.NivelTriage;
import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import managedBeans.seguridad.LoginMB;
import modelo.entidades.CfgPacientes;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.UrgAdmision;
import modelo.entidades.UrgTriage;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.UrgAmisionFacade;
import modelo.fachadas.UrgTriageFacade;

/**
 *
 * @author Enderson
 */
@ManagedBean(name = "triageMB")
@SessionScoped
public class TriageMB extends MetodosGenerales implements Serializable {

    private List<UrgAdmision> listaAdmisionesPaciente;
    private CfgUsuarios usuarioLogueado;
    private Date fecha;
    private Date hora;
    private CfgPacientes pacienteSeleccionado;
    private UrgTriage urgTriage;
    private UrgAdmision urgAdmision;
    private java.util.Set<NivelTriage> nivelTriage;
    private String immpresionDiagnostica;
    private Double imc;
    private String urlFoto;

    @EJB
    CfgClasificacionesFacade clasificacionesFachada;

    @EJB
    UrgAmisionFacade urgAmisionFacade;

    @EJB
    UrgTriageFacade urgTriageFacade;

    LoginMB loginMB;

    @PostConstruct
    public void inicializar() {
        this.setFecha(new Date());
        this.setHora(new Date());
        this.getListaAdmisionesPaciente().clear();
        this.getListaAdmisionesPaciente().addAll(this.getUrgAmisionFacade().findAllAdmisionesVigentes(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_TRIAGE));
        this.setUrgTriage(new UrgTriage());
        // this.setUsuarioLogueado(this.loginMB.getUsuarioActual());
        //  System.out.println("managedBeans.urgencias.TriageMB.inicializar()" + this.loginMB.getUsuarioActual());
        loginMB = (LoginMB) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{loginMB}", LoginMB.class);
        this.nivelTriage = EnumSet.of(NivelTriage.NIVELTRIAGE_I, NivelTriage.NIVELTRIAGE_II, NivelTriage.NIVELTRIAGE_III,
                NivelTriage.NIVELTRIAGE_IV, NivelTriage.NIVELTRIAGE_V);

    }

    public String nroAdmision() {
        String nro = "";
        SimpleDateFormat format = new SimpleDateFormat("YYYYmmdd");
        Long nroAdmsionesDiarias;
//        nroAdmsionesDiarias = this.getUrgAmisionFacade().numeroAdmisionesDiarias(new Date());
        //  nro = cerosIzquierda(nroAdmsionesDiarias);
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

    public void iniciarTriage(UrgAdmision admision) {
        this.setPacienteSeleccionado(admision.getIdPaciente());
        this.setUrgAdmision(admision);
        String fotoPorDefecto = "../recursos/img/img_usuario.png";
        if (this.getPacienteSeleccionado().getFoto() != null) {
            urlFoto = "../imagenesOpenmedical/" + this.getPacienteSeleccionado().getFoto().getUrlImagen();
        } else {
            urlFoto = fotoPorDefecto;
        }
        imprimirMensaje("Información", "Se ha cargado la información del paciente seleccionado", FacesMessage.SEVERITY_INFO);
    }

    public void cancelarTriage() {
        this.setPacienteSeleccionado(null);
        imprimirMensaje("Advertencia", "Se ha cancelado el triage del paciente", FacesMessage.SEVERITY_WARN);

    }

    public void guardarTriage() {
        if (this.validarExamenFisico()) {
            this.getUrgTriage().setIdAdmision(this.getUrgAdmision());
            this.getUrgTriage().setFechaTriage(this.getFecha());
            this.getUrgTriage().setHoraTriage(this.getHora());
            this.getUrgTriage().setIdAdmision(this.getUrgAdmision());
            this.getUrgTriage().setIdPrestador(loginMB.getUsuarioActual());
            this.getUrgTriage().setImpresionDiagnostica(clasificacionesFachada.find(Integer.parseInt(this.getImmpresionDiagnostica())));
            this.getUrgTriageFacade().create(this.getUrgTriage());
            this.getUrgAdmision().setEstadoAdmisionPaciente(EstadoAdmisionPaciente.ADMISION_PACIENTE_EN_CONSULTA_DOCTOR);
            this.getUrgAmisionFacade().edit(this.getUrgAdmision());
            this.setPacienteSeleccionado(null);
            this.getListaAdmisionesPaciente().clear();
            this.getListaAdmisionesPaciente().addAll(this.getUrgAmisionFacade().findAllAdmisionesVigentes(EstadoAdmisionPaciente.ADMISION_PACIENTE_ENVIADO_TRIAGE));
            imprimirMensaje("Información", "Se ha guardado la información del triage y se le ha enviado al doctor", FacesMessage.SEVERITY_INFO);
        }
    }

    public void determinarimc() {
        Double imc = 0.0;
        Double tallaMetros = 0.0;
        if (this.getUrgTriage().getPeso() != null
                && this.getUrgTriage().getTalla() != null) {
            tallaMetros = (this.getUrgTriage().getTalla() / 100);
            imc = this.getUrgTriage().getPeso()
                    / ((tallaMetros * tallaMetros));

        }
        this.getUrgTriage().setMc(imc);
        this.setImc(imc);
    }

    public Boolean validarExamenFisico() {
        if (this.getUrgTriage().getMotivo().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar el contenido de la revisión del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getPeso() == null
                || this.getUrgTriage().getPeso() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar el peso del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getTalla() == null
                || this.getUrgTriage().getTalla() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la talla del paciente", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getFrecuenciaCardiaca() == null
                || this.getUrgTriage().getFrecuenciaCardiaca() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la frecuencia cardiaca del paciente valores validos entre 0 - 201", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getFrecuencia_respiratoria() == null
                || this.getUrgTriage().getFrecuencia_respiratoria() == 0.0) {
            imprimirMensaje("Error", "Debe ingresar la frecuencia respiratoria del paciente valores validos entre 0 - 81", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getPresionArteriaMinima() == null) {
            imprimirMensaje("Error", "Debe ingresar los valores de presión arterial minima ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getPresionArteriaMaxima() == null) {
            imprimirMensaje("Error", "Debe ingresar los valores de presión arterial maxima ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getTemperatura() == null) {
            imprimirMensaje("Error", "Debe ingresar la temperatura del paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getSaturacion() == null) {
            imprimirMensaje("Error", "Debe ingresar la saturación del paciente valores valido de 0 a 100 ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getHallazgosClinico().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar los hallazgos clinicos encontrados en el paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else if (this.getUrgTriage().getConducta().isEmpty()) {
            imprimirMensaje("Error", "Debe ingresar la conducta observada en el paciente ", FacesMessage.SEVERITY_INFO);
            return Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }

    public LoginMB getLoginMB() {
        return loginMB;
    }

    public CfgUsuarios getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public UrgTriage getUrgTriage() {
        return urgTriage;
    }

    public void setUrgTriage(UrgTriage urgTriage) {
        this.urgTriage = urgTriage;
    }

    public void setUsuarioLogueado(CfgUsuarios usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    public List<UrgAdmision> getListaAdmisionesPaciente() {
        if (listaAdmisionesPaciente == null) {
            listaAdmisionesPaciente = new ArrayList<>();
        }
        return listaAdmisionesPaciente;
    }

    public void setListaAdmisionesPaciente(List<UrgAdmision> listaAdmisionesPaciente) {
        this.listaAdmisionesPaciente = listaAdmisionesPaciente;
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

    public UrgAmisionFacade getUrgAmisionFacade() {
        return urgAmisionFacade;
    }

    public void setUrgAmisionFacade(UrgAmisionFacade urgAmisionFacade) {
        this.urgAmisionFacade = urgAmisionFacade;
    }

    public CfgPacientes getPacienteSeleccionado() {
        return pacienteSeleccionado;
    }

    public void setPacienteSeleccionado(CfgPacientes pacienteSeleccionado) {
        this.pacienteSeleccionado = pacienteSeleccionado;
    }

    public UrgAdmision getUrgAdmision() {
        return urgAdmision;
    }

    public void setUrgAdmision(UrgAdmision urgAdmision) {
        this.urgAdmision = urgAdmision;
    }

    public UrgTriageFacade getUrgTriageFacade() {
        return urgTriageFacade;
    }

    public void setUrgTriageFacade(UrgTriageFacade urgTriageFacade) {
        this.urgTriageFacade = urgTriageFacade;
    }

    public Set<NivelTriage> getNivelTriage() {
        return nivelTriage;
    }

    public void setNivelTriage(Set<NivelTriage> nivelTriage) {
        this.nivelTriage = nivelTriage;
    }

    public String getImmpresionDiagnostica() {
        return immpresionDiagnostica;
    }

    public void setImmpresionDiagnostica(String immpresionDiagnostica) {
        this.immpresionDiagnostica = immpresionDiagnostica;
    }

    public CfgClasificacionesFacade getClasificacionesFachada() {
        return clasificacionesFachada;
    }

    public void setClasificacionesFachada(CfgClasificacionesFacade clasificacionesFachada) {
        this.clasificacionesFachada = clasificacionesFachada;
    }

    public Double getImc() {
        return imc;
    }

    public void setImc(Double imc) {
        this.imc = imc;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

}
