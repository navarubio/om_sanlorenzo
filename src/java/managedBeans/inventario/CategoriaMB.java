/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.utilidades.MetodosGenerales;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import managedBeans.seguridad.AplicacionGeneralMB;
import modelo.entidades.InvCategorias;
import modelo.fachadas.InvCategoriasFacade;
import org.primefaces.context.RequestContext;

/**
 *
 * @author miguel
 */
@Named(value = "categoriaMB")
@ViewScoped
public class CategoriaMB extends MetodosGenerales implements Serializable{

    @EJB
    private InvCategoriasFacade clasificacionesFachada;
    
    private List<InvCategorias> listaCategorias;
    private InvCategorias categoria;
    private AplicacionGeneralMB aplicacionGeneralMB;
    public CategoriaMB() {
        aplicacionGeneralMB=FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{aplicacionGeneralMB}", AplicacionGeneralMB.class);
    }
    
    @PostConstruct
    public void init(){
        categoria = new InvCategorias();
        categoria.setActivo(true);
        listaCategorias = clasificacionesFachada.getActivos();
    }
    
    public void nuevo(){
        categoria = new InvCategorias();
        categoria.setActivo(true);
        listaCategorias = clasificacionesFachada.getActivos();
    }
    public void buscar(){
        RequestContext.getCurrentInstance().execute("PF('dialogoBuscarCategoria').hide(); PF('wvTablaClasificaciones').clearFilters(); PF('wvTablaClasificaciones').getPaginator().setPage(0);");
    }
    public void eliminar(){
        try {
            categoria.setActivo(false);
            clasificacionesFachada.edit(categoria);
            imprimirMensaje("Eliminado", "Eliminado correctamente", FacesMessage.SEVERITY_INFO);
        } catch (Exception e) {
            imprimirMensaje("Error al eliminar", "La categoría esta asociada a un producto", FacesMessage.SEVERITY_ERROR);
        }
    }
    public void guardar(){
        if(categoria.getNombre()!=null){
            if(!categoria.getNombre().equals("")){
                try {
                    if(categoria.getIdCategoria()==null){
                        clasificacionesFachada.create(categoria);
                    }else {
                        clasificacionesFachada.edit(categoria);
                    }
                    imprimirMensaje("Guardado", "Guardado Correctamente", FacesMessage.SEVERITY_INFO);
                    RequestContext.getCurrentInstance().update("IdTablaClasificaciones");
                    nuevo();
                } catch (Exception e) {
                    imprimirMensaje("Error al guardar", e.getMessage(), FacesMessage.SEVERITY_ERROR);
                }
                
                
            }else{
                imprimirMensaje("Error al guardar", "Digite nombre categoría", FacesMessage.SEVERITY_ERROR);
            }
        }else{
            imprimirMensaje("Error al guardar", "Digite nombre categoría", FacesMessage.SEVERITY_ERROR);
        }
    }

    public List<InvCategorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<InvCategorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public InvCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(InvCategorias categoria) {
        this.categoria = categoria;
    }

    public AplicacionGeneralMB getAplicacionGeneralMB() {
        return aplicacionGeneralMB;
    }

    public void setAplicacionGeneralMB(AplicacionGeneralMB aplicacionGeneralMB) {
        this.aplicacionGeneralMB = aplicacionGeneralMB;
    }
}
