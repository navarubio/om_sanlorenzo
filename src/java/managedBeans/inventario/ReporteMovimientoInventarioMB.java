/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.utilidades.Fechas;
import beans.utilidades.MetodosGenerales;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import modelo.entidades.InvBodegas;
import modelo.entidades.InvCategorias;
import modelo.entidades.InvLotes;
import modelo.entidades.InvProductos;
import modelo.fachadas.DBConnector;
import modelo.fachadas.InvBodegasFacade;
import modelo.fachadas.InvCategoriasFacade;
import modelo.fachadas.InvLotesFacade;
import modelo.fachadas.InvProductosFacade;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;

/**
 *
 * @author casc 20171116
 */
@Named(value = "reporteMovimientoInventarioMB")
@Stateless
public class ReporteMovimientoInventarioMB extends MetodosGenerales implements java.io.Serializable {

    /*Fachadas*/
    @EJB
    private InvBodegasFacade bodegaFachada;
    @EJB
    private InvProductosFacade productoFachada;
    @EJB
    private InvCategoriasFacade categoriaFachada;
    @EJB
    private InvLotesFacade loteFachada;
    /* Variables de uso en beans*/
    private List<InvBodegas> listaBodegas;
    private List<InvCategorias> listaCategorias;
    private List<InvProductos> listaProductos;
    private List<InvProductos> listaProductosSeleccionados;
    private List<SelectItem> listaProcesos;
    private List<InvLotes> listaLote;
    private String tipoMovimiento;
    private String tipoProceso;
    private Date fechaDesde;
    private Date fechaHasta;
    private InvCategorias categoria;
    private InvProductos producto;
    private InvBodegas bodega;
    private int idLote;
    private String operador;
    private double cantidadOperador;

    @PostConstruct()
    public void init() {
        if (listaCategorias == null) {
            listaCategorias = categoriaFachada.getActivos();
        }
        if (listaBodegas == null) {
            listaBodegas = bodegaFachada.findAll();
        }
        listaProcesos = new ArrayList<>();
        listaProductos = productoFachada.getProductosActivos();
        listaLote = loteFachada.getLotesSinVencer(1);
        this.categoria = new InvCategorias();
        this.categoria.setIdCategoria(0);
        this.bodega = new InvBodegas();
        this.bodega.setIdBodega(0);
        this.idLote = 0;
        tipoMovimiento = "0";
        tipoProceso = "0";
        fechaDesde = new Date();
        fechaHasta = new Date();
    }

    public void validarTipoMovimiento() {
        if (tipoMovimiento.equals("E")) {
            listaProcesos.clear();
            listaProcesos.add(new SelectItem(1, "Traslados Bodegas"));
            listaProcesos.add(new SelectItem(2, "Saldo Inicial"));
            listaProcesos.add(new SelectItem(3, "Ajuste Positivo"));
            //listaProcesos.add(new SelectItem(4,"Devolución por cliente"));
        } else {
            listaProcesos.clear();
            listaProcesos.add(new SelectItem(1, "Traslados Bodegas"));
            listaProcesos.add(new SelectItem(6, "Ajuste Negativo"));
            listaProcesos.add(new SelectItem(7, "Devoluciones por compra"));
        }
    }

    public void generarReporte(ActionEvent actionEvent) {
        String logoEmpresa = (String) actionEvent.getComponent().getAttributes().get("logo_empresa");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse httpServletResponse = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        int filtroBodega = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroBodega = bodega.getIdBodega();
        }
        Fechas fec = new Fechas();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            StringBuilder query = new StringBuilder("select case when tipo_movimiento='E' then 'ENTRADA' ELSE 'SALIDA' END tipo, ")
                    .append(" to_char(fecha_movimiento,'dd/MM/yyyy') fecha,")
                    .append(" login_usuario, numero_documento, pr.codigo || ' - ' ||  pr.nombre as producto,  ")
                    .append(" dm.cantidad_recibida, pr.costo, pr.precio_venta,")
                    .append(" fcn_existencia_total(").append(filtroBodega)
                    .append(", pr.id_producto) existencia")
                    .append(" from inv_movimientos cm, inv_movimiento_productos dm, inv_productos pr, cfg_usuarios usu")
                    .append(" where cm.id_movimiento=dm.id_movimiento and dm.id_producto=pr.id_producto")
                    .append(" and cm.usuario_aprueba=usu.id_usuario");
            query.append(" AND DATE(cm.fecha_movimiento) BETWEEN '")
                    .append(fec.fechaToString(fechaDesde, "yyyy-MM-dd"))
                    .append("' AND '")
                    .append(fec.fechaToString(fechaHasta, "yyyy-MM-dd"))
                    .append("'");
            if (filtroBodega > 0) {
                query.append(" AND (cm.id_bodega_origen=").append(filtroBodega)
                        .append(" OR id_bodega_destino=").append(filtroBodega)
                        .append(")");
            }
            if (categoria != null && categoria.getIdCategoria() > 0) {
                query.append(" AND pr.id_categoria='").append(categoria.getIdCategoria())
                        .append("'");
            }
            if (tipoMovimiento != null && !tipoMovimiento.equals("0")) {
                query.append(" AND cm.tipo_movimiento='").append(tipoMovimiento)
                        .append("'");
            }
            if (tipoProceso != null && !tipoProceso.equals("0")) {
                query.append(" AND cm.tipo_proceso='").append(tipoProceso)
                        .append("'");

            }
            if (idLote > 0) {
                //TODO
            }
            if (listaProductosSeleccionados != null && !listaProductosSeleccionados.isEmpty()) {
                String cadProductos = "0";
                for (InvProductos prod : listaProductosSeleccionados) {
                    cadProductos += "," + prod.getIdProducto();

                }
                if (!cadProductos.equals("0")) {
                    query.append(" AND dm.id_producto in(")
                            .append(cadProductos)
                            .append(")");
                }
            }
            if (operador != null && !operador.equals("0")) {
                switch (operador) {
                    case "1":
                        query.append(" and dm.cantidad_recibida=").append(cantidadOperador);
                        break;
                    case "2":
                        query.append(" and dm.cantidad_recibida>").append(cantidadOperador);
                        break;
                    case "3":
                        query.append(" and dm.cantidad_recibida<").append(cantidadOperador);
                        break;
                    case "4":
                        query.append(" and dm.cantidad_recibida>=").append(cantidadOperador);
                        break;
                    case "5":
                        query.append(" and dm.cantidad_recibida<=").append(cantidadOperador);
                        break;
                }

            }
            System.out.println("Query " + query.toString());
            HashMap<String, Object> params = new HashMap<>();
            params.put("query", query.toString());
            params.put("bodega", (bodega != null && bodega.getIdBodega() > 0) ? bodega.getCodigoBodega() + "-" + bodega.getNombre() : "TODAS");
            params.put("fechaDesde", fec.fechaToString(fechaDesde, "dd/MM/yyyy"));
            params.put("fechaHasta", fec.fechaToString(fechaHasta, "dd/MM/yyyy"));
            params.put("logoEmpresa", logoEmpresa);

            ServletContext servletContext = (ServletContext) facesContext.getExternalContext().getContext();
            String ruta = servletContext.getRealPath("inventario/reportes/r_movimiento_inventario.jasper");
            Connection con = DBConnector.getInstance().getConnection();
            JasperPrint jasperPrint = JasperFillManager.fillReport(ruta, params, con);
            JasperExportManager.exportReportToPdfStream(jasperPrint, servletOutputStream);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception e) {
            System.out.println("Error generando reporte");
            imprimirMensaje("Error al guardar", "Digite código del producto", FacesMessage.SEVERITY_ERROR);
        }

    }

    public List<InvBodegas> getListaBodegas() {
        return listaBodegas;
    }

    public void setListaBodegas(List<InvBodegas> listaBodegas) {
        this.listaBodegas = listaBodegas;
    }

    public List<InvCategorias> getListaCategorias() {
        return listaCategorias;
    }

    public void setListaCategorias(List<InvCategorias> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }

    public List<InvProductos> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<InvProductos> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public List<InvProductos> getListaProductosSeleccionados() {
        return listaProductosSeleccionados;
    }

    public void setListaProductosSeleccionados(List<InvProductos> listaProductosSeleccionados) {
        this.listaProductosSeleccionados = listaProductosSeleccionados;
    }

    public List<SelectItem> getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(List<SelectItem> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
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

    public InvCategorias getCategoria() {
        return categoria;
    }

    public void setCategoria(InvCategorias categoria) {
        this.categoria = categoria;
    }

    public InvProductos getProducto() {
        return producto;
    }

    public void setProducto(InvProductos producto) {
        this.producto = producto;
    }

    public InvBodegas getBodega() {
        return bodega;
    }

    public void setBodega(InvBodegas bodega) {
        this.bodega = bodega;
    }

    public List<InvLotes> getListaLote() {
        return listaLote;
    }

    public void setListaLote(List<InvLotes> listaLote) {
        this.listaLote = listaLote;
    }

    public int getIdLote() {
        return idLote;
    }

    public void setIdLote(int idLote) {
        this.idLote = idLote;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public double getCantidadOperador() {
        return cantidadOperador;
    }

    public void setCantidadOperador(double cantidadOperador) {
        this.cantidadOperador = cantidadOperador;
    }

}
