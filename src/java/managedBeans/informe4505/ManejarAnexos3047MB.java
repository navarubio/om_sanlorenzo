/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.informe4505;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import managedBeans.historias.DatosFormularioHistoria;
import modelo.entidades.CfgClasificaciones;
import modelo.entidades.Hc3047Anexo1;
import modelo.fachadas.CfgClasificacionesFacade;
import modelo.fachadas.CfgDiagnosticoPrincipalFacade;
import modelo.fachadas.Hc3047Anexo1Facade;

/**
 *
 * @author sismacontab
 */
@ManagedBean(name = "manejarAnexos3047MB")
@SessionScoped
public class ManejarAnexos3047MB extends MetodosGenerales implements Serializable {

    /**
     * Manejar Informacion de Formularios Anexos 3047
     */
        
    @EJB
    CfgClasificacionesFacade clasificacionesFacade;
    @EJB
    CfgDiagnosticoPrincipalFacade diagnosticoFacade;
    @EJB
    Hc3047Anexo1Facade hc3047Anexo1Facade;
    
    private String pacienteremitido=""; 
    private int tipomovimiento=0;
    private boolean prestadorremitente=false;
    private List<SelectItem> listaMunicipios;
    private String departamento = "";
    private String tiposerviciosolicita="";
    private String prioridadservicio="";
    private String ubicacionpaciente="";
    private boolean coutam=false;
    private boolean copago=false;
    private boolean coutar=false;
    private boolean coutaotro=false;
    private Date fechaReg;
    private  Hc3047Anexo1 nuevoAnexo1 = new Hc3047Anexo1();
    
    
    private DatosFormularioHistoria datosFormulario = new DatosFormularioHistoria();//valores de cada uno de los campos de cualquier registro clinico
    
    public ManejarAnexos3047MB() {
    }

    public void selecciontipomovimiento() {
        if (pacienteremitido.equals("SI")) {
            tipomovimiento = 1;
            prestadorremitente=true;
        } else if (pacienteremitido.equals("NO")) {
            tipomovimiento = 0;
            prestadorremitente=false;
            datosFormulario.setDato1(null);
            listaMunicipios=null;
        } else {
            tipomovimiento = 0;
            prestadorremitente=false;
        }
        

    }
    
    public void cargarMunicipios() {
        listaMunicipios = new ArrayList<>();
        try {
            if (datosFormulario != null) {
                departamento = datosFormulario.getDato1().toString();
                if (departamento != null && departamento.length() != 0 && esNumero(departamento)) {
                    List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
                    for (CfgClasificaciones mun : listaM) {
                        listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
                    }
                } else {
                    List<CfgClasificaciones> listaM = clasificacionesFacade.buscarMunicipioPorDepartamento(clasificacionesFacade.find(Integer.parseInt(departamento)).getCodigo());
                    for (CfgClasificaciones mun : listaM) {
                        listaMunicipios.add(new SelectItem(mun.getId(), mun.getDescripcion()));
                    }
                }
            }
        } catch (Exception e) {
        }
    }
    
    public void guardarAnexo1() {//guardar un nuevo registro clinico        
        int idSede = 1;
        System.out.println(datosFormulario.getDato0());

        System.out.println("Iniciando el guardado del registro");
        
        
        hc3047Anexo1Facade.create(nuevoAnexo1);
        

    }
    
    public String getPacienteremitido() {
        return pacienteremitido;
    }

    public void setPacienteremitido(String pacienteremitido) {
        this.pacienteremitido = pacienteremitido;
    }

    public int getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(int tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public boolean isPrestadorremitente() {
        return prestadorremitente;
    }

    public void setPrestadorremitente(boolean prestadorremitente) {
        this.prestadorremitente = prestadorremitente;
    }

    public DatosFormularioHistoria getDatosFormulario() {
        return datosFormulario;
    }

    public void setDatosFormulario(DatosFormularioHistoria datosFormulario) {
        this.datosFormulario = datosFormulario;
    }

    public List<SelectItem> getListaMunicipios() {
        return listaMunicipios;
    }

    public void setListaMunicipios(List<SelectItem> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
    }

    public String getTiposerviciosolicita() {
        return tiposerviciosolicita;
    }

    public void setTiposerviciosolicita(String tiposerviciosolicita) {
        this.tiposerviciosolicita = tiposerviciosolicita;
    }

    public String getPrioridadservicio() {
        return prioridadservicio;
    }

    public void setPrioridadservicio(String prioridadservicio) {
        this.prioridadservicio = prioridadservicio;
    }

    public String getUbicacionpaciente() {
        return ubicacionpaciente;
    }

    public void setUbicacionpaciente(String ubicacionpaciente) {
        this.ubicacionpaciente = ubicacionpaciente;
    }

    public boolean isCoutam() {
        return coutam;
    }

    public void setCoutam(boolean coutam) {
        this.coutam = coutam;
    }

    public boolean isCopago() {
        return copago;
    }

    public void setCopago(boolean copago) {
        this.copago = copago;
    }

    public boolean isCoutar() {
        return coutar;
    }

    public void setCoutar(boolean coutar) {
        this.coutar = coutar;
    }

    public boolean isCoutaotro() {
        return coutaotro;
    }

    public void setCoutaotro(boolean coutaotro) {
        this.coutaotro = coutaotro;
    }

    public Date getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(Date fechaReg) {
        this.fechaReg = fechaReg;
    }

    public Hc3047Anexo1 getNuevoAnexo1() {
        return nuevoAnexo1;
    }

    public void setNuevoAnexo1(Hc3047Anexo1 nuevoAnexo1) {
        this.nuevoAnexo1 = nuevoAnexo1;
    }
    
    
    
}
