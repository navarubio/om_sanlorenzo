/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.facturacion;

import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.FacAdministradora;
import modelo.fachadas.FacAdministradoraFacade;
import modelo.fachadas.FacFacturaPacienteFacade;

/**
 *
 * @author miguel
 */
@Named(value = "informeMasivoMB")
@ViewScoped
public class InformeMasivoMB extends MetodosGenerales implements java.io.Serializable{

  private List<FacAdministradora> listaAdministradora;
    
    @EJB
    private FacAdministradoraFacade administradoraFacade;
    @EJB
    private FacFacturaPacienteFacade facturaPacienteFacade;
    
    
    private List<Object[]> listaResultados;
    private int idAdministradora;
    private boolean renderTabla;
    public InformeMasivoMB() {
        listaAdministradora = new ArrayList<>();
    }
    
    @PostConstruct
    public void init() {
       if(listaAdministradora.isEmpty())listaAdministradora = administradoraFacade.findAll();
        listaResultados = new ArrayList<>();
        renderTabla = false;
    }

    public void buscar(){
        listaResultados = facturaPacienteFacade.getInformeMasivo(idAdministradora);
        if(listaResultados.size()>0)renderTabla=true;
        else{
            imprimirMensaje("No hay registros","no se encontró registros", FacesMessage.SEVERITY_INFO);
            renderTabla = false;
        }
    }
    public List<FacAdministradora> getListaAdministradora() {
        return listaAdministradora;
    }

    public void setListaAdministradora(List<FacAdministradora> listaAdministradora) {
        this.listaAdministradora = listaAdministradora;
    }

    public int getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(int idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public List<Object[]> getListaResultados() {
        return listaResultados;
    }

    public void setListaResultados(List<Object[]> listaResultados) {
        this.listaResultados = listaResultados;
    }

    public boolean isRenderTabla() {
        return renderTabla;
    }

    public void setRenderTabla(boolean renderTabla) {
        this.renderTabla = renderTabla;
    }
    
}
