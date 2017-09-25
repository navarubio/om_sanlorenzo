/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.Citas;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.CfgUsuarios;
import modelo.entidades.CitCitas;
import modelo.entidades.FacServicio;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgUsuariosFacade;
import modelo.fachadas.CitCitasFacade;
import modelo.fachadas.FacServicioFacade;

/**
 *
 * @author miguel
 */
@Named(value = "informeCitasMB")
@ViewScoped
public class InformeCitasMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private CfgUsuariosFacade medicoFacade;
    @EJB
    private CitCitasFacade citaFacade;
    @EJB
    private CfgClasificacionesFacade clasificacionesFacade;
    
    private String medico;
    private Date fechaDesde;
    private Date fechaHasta;
    private String administradora;
    private String motivoConsulta;
    private String oportunidad;
    private String estado;
    private String estadoPaciente;
    private boolean renderBuscar;
    private List<CfgUsuarios> listaMedicos;
    private List<CitCitas> listaCitas;
    private List<CfgClasificaciones> listaMotivoConsulta;
    public InformeCitasMB() {
        medico = "";
        administradora  ="";
        motivoConsulta = "";
        oportunidad  ="";
        estado = "";
        
    }

    @PostConstruct
    public void init(){
        listaMedicos = medicoFacade.findPrestadores();
        listaCitas = new ArrayList<>();
        listaMotivoConsulta = clasificacionesFacade.buscarPorMaestro(ClasificacionesEnum.MotivoConsulta.toString());
    }
    
    public void limpiar(){
        listaCitas.clear();
        renderBuscar = false;
    }
    
    public void consultar(){
        listaCitas.clear();
        if(fechaDesde!=null && fechaHasta==null){
            imprimirMensaje("Error al consultar", "Seleccione fecha hasta", FacesMessage.SEVERITY_ERROR);
        }else if(fechaDesde==null && fechaHasta!=null){
            imprimirMensaje("Error al consultar", "Seleccione fecha desde", FacesMessage.SEVERITY_ERROR);
        }else{
            String filtro="";
            if (medico != null) {
                if (!medico.equals("")) {
                    filtro += " AND c.idPrestador.idUsuario =" + medico;
                }
            }
            if (fechaDesde != null && fechaHasta != null) {
                filtro += " AND c.fechaRegistro BETWEEN :param1 and :param2 ";
            }
            if(administradora!=null){
                if(!administradora.equals("")){
                    filtro += " AND c.idAdministradora.idAdministradora = "+administradora;
                }
            }
            if(motivoConsulta!=null){
                if(!motivoConsulta.equals("")){
                    filtro += " AND c.tipoCita.id = "+motivoConsulta+"";
                }
            }
            if(oportunidad!=null){
                if(!oportunidad.equals("")){
                    filtro +=" AND c.idTurno.estado='"+oportunidad+"'";
                }
            }
            if(estado!=null){
                if(!estado.equals("")){
                    switch(estado){
                        case "1"://Asignada
                            filtro  += " AND c.atendida=false and c.cancelada=false and c.facturada=false";
                            break;
                        case "2"://Atendida
                            filtro  += " AND c.atendida=true and c.facturada=false ";
                            break;        
                        case "3"://Cancelada
                            filtro  += " AND c.cancelada=true";
                            break;    
                        case "4"://Facturada
                            filtro  += " AND c.facturada=true";
                            break;    
                    }
                }
            }
            if(estadoPaciente!=null){
                if(!estadoPaciente.equals("")){
                    filtro = "AND c.idPaciente.activo = ";
                    if(estadoPaciente.equals("1")){
                        filtro +="true" ;
                    }else{
                        filtro +="false" ;
                    }
                }
            }
            listaCitas = citaFacade.getInformeFiltro(filtro,fechaDesde, fechaHasta);
            if (!listaCitas.isEmpty()) {
                renderBuscar = true;
            } else {
                imprimirMensaje("No hay registros", "No se encontró registros", FacesMessage.SEVERITY_INFO);
                renderBuscar = false;
            }
        }
    }
    public void exportarCSV(){
        FacesContext context = FacesContext.getCurrentInstance();
        try{
            FacesContext.getCurrentInstance().responseComplete();
            String baseURL = context.getExternalContext().getRequestContextPath();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
            String url =  baseURL +"/InformeCitasServlet?medico="+medico+"&fechaDesde="+(fechaDesde!=null?formato.format(fechaDesde):"")+"&fechaHasta="+(fechaHasta!=null?formato.format(fechaHasta):"")+
                    "&motivo="+motivoConsulta+"&oportunidad="+oportunidad+"&estado="+estado+"&administradora="+administradora+"&estadoPaciente="+estadoPaciente;
            String encodeURL = context.getExternalContext().encodeResourceURL(url);
        context.getExternalContext().redirect(encodeURL);
        }  catch(Exception e)    {
            e.printStackTrace();
            }
    }
    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public String getAdministradora() {
        return administradora;
    }

    public void setAdministradora(String administradora) {
        this.administradora = administradora;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getOportunidad() {
        return oportunidad;
    }

    public void setOportunidad(String oportunidad) {
        this.oportunidad = oportunidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<CfgUsuarios> getListaMedicos() {
        return listaMedicos;
    }

    public void setListaMedicos(List<CfgUsuarios> listaMedicos) {
        this.listaMedicos = listaMedicos;
    }

    public boolean isRenderBuscar() {
        return renderBuscar;
    }

    public void setRenderBuscar(boolean renderBuscar) {
        this.renderBuscar = renderBuscar;
    }

    public List<CitCitas> getListaCitas() {
        return listaCitas;
    }

    public void setListaCitas(List<CitCitas> listaCitas) {
        this.listaCitas = listaCitas;
    }

    public List<CfgClasificaciones> getListaMotivoConsulta() {
        return listaMotivoConsulta;
    }

    public void setListaMotivoConsulta(List<CfgClasificaciones> listaMotivoConsulta) {
        this.listaMotivoConsulta = listaMotivoConsulta;
    }

    public String getEstadoPaciente() {
        return estadoPaciente;
    }

    public void setEstadoPaciente(String estadoPaciente) {
        this.estadoPaciente = estadoPaciente;
    }

    
    
    
}
