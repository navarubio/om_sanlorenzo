/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.enumeradores.ClasificacionesEnum;
import beans.utilidades.MetodosGenerales;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modelo.entidades.CfgPacientes;
import modelo.entidades.InvBodegaProductos;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvCategorias;
import modelo.entidades.InvProveedores;
import modelo.fachadas.InvBodegaProductosFacade;
import modelo.fachadas.InvBodegasFacade;

/**
 *
 * @author miguel
 */
@Named(value = "informeInventarioMB")
@ViewScoped
public class InformeInventarioMB extends MetodosGenerales implements java.io.Serializable{

    @EJB
    private InvBodegasFacade bodegaFachada;
    @EJB
    private InvBodegaProductosFacade bodegaProductosFachada;
    
    private List<InvBodegas> listaBodegas;
    private List<InvBodegaProductos> listaBodegasProductos;
    private int idBodega = 0;
    public InformeInventarioMB() {
    }
    
    @PostConstruct()
    public void init() {
        if (listaBodegas == null) {
            listaBodegas = bodegaFachada.findAll();
        }
    }
    
    public void consultar(){
        try {
            listaBodegasProductos = bodegaProductosFachada.getProductosBodegas(idBodega);
        } catch (Exception e) {
        }
    }

    public List<InvBodegas> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<InvBodegas> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public List<InvBodegaProductos> getListaBodegasProductos() {
        return listaBodegasProductos;
    }

    public void setListaBodegasProductos(List<InvBodegaProductos> listaBodegasProductos) {
        this.listaBodegasProductos = listaBodegasProductos;
    }

    public int getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(int idBodega) {
        this.idBodega = idBodega;
    }
    
    
    
    public void exportar(){
        
    }
    
}
