/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedBeans.inventario;

import beans.utilidades.Fechas;
import beans.utilidades.MetodosGenerales;
import beans.utilidades.reportescsv.inventario.RepInventarioCsv;
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
    private List<RepInventarioCsv> listaReporteCsv;//
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
        Fechas fec = new Fechas();
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            String query = this.generarQueryMovimientos(false);
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

    public void vistaPrevia() {
        listaReporteCsv = new ArrayList<>();
        List<Object> arr
                = productoFachada.dataReporteMovimientoInventario(generarQueryMovimientos(true));
        RepInventarioCsv fila;
        for (Object object : arr) {
            Object[] campos = (Object[]) object;
            fila = new RepInventarioCsv();
            fila.setTipoMovimiento(campos[0].toString());
            fila.setFecha(campos[1].toString());
            fila.setUsuario(campos[2].toString());
            fila.setNumeroDocumento(campos[3].toString());
            fila.setCodigo(campos[4].toString());
            fila.setProducto(campos[5].toString());
            fila.setCantidad((Double) campos[6]);
            fila.setCosto((Double) campos[7]);
            fila.setCosto_total(fila.getCantidad() * fila.getCosto());
            fila.setPrecio((Double) campos[8]);
            fila.setExistencia((Double) campos[9]);
            fila.setValor(fila.getPrecio() * fila.getExistencia());
            listaReporteCsv.add(fila);
        }

    }

    /**
     * Crear cadena con query segun filtros seleccionados para el movimiento de
     * inventario
     *
     * @param _separaProducto
     * @return
     */
    public String generarQueryMovimientos(boolean _separaProducto) {
        Fechas fec = new Fechas();
        int filtroBodega = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroBodega = bodega.getIdBodega();
        }
        StringBuilder query = new StringBuilder("select case when tipo_movimiento='E' then 'ENTRADA' ELSE 'SALIDA' END tipo, ")
                .append(" to_char(fecha_movimiento,'dd/MM/yyyy') fecha,")
                .append(" login_usuario, numero_documento, ");
        if (_separaProducto) {
            query.append(" pr.codigo, pr.nombre, ");
        } else {
            query.append(" pr.codigo || ' - ' ||  pr.nombre as producto, ");
        }
        query.append(" dm.cantidad_recibida, pr.costo, pr.precio_venta,")
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
        return query.toString();
    }

    /**
     * Crear cadena de query para el kardex segun filtros seleccionadas
     *
     * @param _separaProducto
     * @return
     */
    public String generarQueryKardex(boolean _separaProducto) {
        Fechas fec = new Fechas();
        int filtroBodega = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroBodega = bodega.getIdBodega();
        }
        String orden = " group by 1,2,3,4,5,6,7,cm.tipo_movimiento,id_bodega_destino,id_bodega_origen, pr.id_producto";

        StringBuilder query = new StringBuilder("SELECT ")
                .append(" case when cm.tipo_movimiento='E' then  fcn_obtener_nombre_bodega(id_bodega_destino) else fcn_obtener_nombre_bodega(id_bodega_origen) END bodega, ");

        if (_separaProducto) {
            query.append(" cat.codigo, cat.nombre categoria, ");
            query.append(" pr.codigo, pr.nombre producto, ");
        } else {
            query.append(" cat.codigo || '-' || cat.nombre categoria, ");
            query.append(" pr.codigo || ' - ' ||  pr.nombre as producto, ");
        }
        query.append(" pr.stock_minimo, pr.stock_maximo, pr.costo, pr.precio_venta,")
                .append(" fcn_existencia_total(case when cm.tipo_movimiento='E' then id_bodega_destino else id_bodega_origen end, pr.id_producto)  existencia,")
                .append(" sum(case when cm.tipo_movimiento='E' then cantidad_recibida else 0 end) entradas,")
                .append(" sum(case when cm.tipo_movimiento='S' then cantidad_solicitada else 0 end) salidas")
                .append(" from inv_movimientos cm, inv_movimiento_productos dm, inv_productos pr, inv_categorias cat")
                .append(" where cm.id_movimiento=dm.id_movimiento and dm.id_producto=pr.id_producto")
                .append(" and pr.id_categoria=cat.id_categoria");
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

        return query.toString();
    }

    private String queryVencimientoLote(boolean _separarDescripciones) {
        Fechas fec = new Fechas();
        int filtroBodega = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroBodega = bodega.getIdBodega();
        }
        StringBuilder query = new StringBuilder("SELECT ");
        if (_separarDescripciones) {
            query.append(" mb.codigo_bodega, mb.nombre nombre_bodega,")
                    .append(" cat.codigo codigo_categoria, cat.nombre nombre_categoria,")
                    .append(" pr.codigo || '-' || pr.nombre producto,");
        } else {
            query.append(" mb.codigo_bodega || '-' || mb.nombre bodega,")
                    .append(" cat.codigo || '_' || cat.nombre categoria,")
                    .append(" pr.codigo || '-' || pr.nombre producto,");
        }
        query.append(" lot.codigo lote,  to_char(lot.fecha_vencimiento,'dd/MM/yyyy') fecha_vencimiento,")
                .append(" pr.stock_minimo, pr.stock_maximo, db.existencia, costo, precio_venta")
                .append(" from inv_bodegas mb, inv_bodega_productos db, inv_productos pr, inv_lotes lot, inv_categorias cat")
                .append(" where mb.id_bodega=db.id_bodega")
                .append(" and db.id_producto=pr.id_producto and pr.id_categoria=cat.id_categoria")
                .append(" and db.id_lote=lot.id_lote");
        query.append(" AND DATE(lot.fecha_vencimiento) BETWEEN '")
                .append(fec.fechaToString(fechaDesde, "yyyy-MM-dd"))
                .append("' AND '")
                .append(fec.fechaToString(fechaHasta, "yyyy-MM-dd"))
                .append("'");
        if (filtroBodega > 0) {
            query.append(" AND mb=").append(filtroBodega);
        }
        if (categoria != null && categoria.getIdCategoria() > 0) {
            query.append(" AND pr.id_categoria='").append(categoria.getIdCategoria())
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
        return query.toString();
    }

    /**
     *
     * @param _separarDescripciones
     * @return
     */
    private String queryConsumoPacientes(boolean _separarDescripciones) {
        Fechas fec = new Fechas();
        int filtroPaciente = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroPaciente = bodega.getIdBodega();
        }
        StringBuilder query = new StringBuilder("SELECT ")
                .append(" to_char(fecha_entrega,'dd/MM/yyy') fecha, numero_entrega,  login_usuario,")
                .append(" coalesce(mp.primer_nombre,'') || ' ' || coalesce(mp.segundo_nombre,'') || ' ' || coalesce(mp.primer_apellido,'') || ' ' || coalesce(mp.segundo_apellido,'') paciente,");
        if (_separarDescripciones) {
            query.append(" pr.codigo || ' ' || pr.nombre producto,");
        } else {
            query.append(" pr.codigo, pr.nombre,");
        }
        query.append(" cantidad_recibida, pr.costo, pr.precio_venta")
                .append(" from inv_entrega_medicamentos me, inv_entrega_medicamentos_detalle  de, cfg_pacientes mp, inv_productos pr , cfg_usuarios usu")
                .append(" where me.id_entrega=de.id_entrega")
                .append(" and me.id_paciente=mp.id_paciente ")
                .append(" and de.id_producto=pr.id_producto")
                .append(" and me.usuario_elabora=usu.id_usuario");

        query.append(" AND DATE(me.fecha_entrega) BETWEEN '")
                .append(fec.fechaToString(fechaDesde, "yyyy-MM-dd"))
                .append("' AND '")
                .append(fec.fechaToString(fechaHasta, "yyyy-MM-dd"))
                .append("'");

        if (filtroPaciente > 0) {
            query.append(" AND me.id_paciente=").append(filtroPaciente);
        }
        if (categoria != null && categoria.getIdCategoria() > 0) {
            query.append(" AND pr.id_categoria='").append(categoria.getIdCategoria())
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

        return query.toString();
    }

    private String queryInformeCompras(boolean _separarDescripciones) {
        Fechas fec = new Fechas();
        int filtroProveedor = 0;
        if (bodega != null && bodega.getIdBodega() > 0) {
            filtroProveedor = bodega.getIdBodega();
        }

        StringBuilder query = new StringBuilder("SELECT ")
                .append("nro_documento, to_char(mo.fecha,'dd/MM/yyyy') fecha, mp.razon_social,")
                .append(" ss");

        if (_separarDescripciones) {
            query.append(" cat.codigo || '-' || cat.nombre categoria,")
                    .append(" pr.codigo || ' ' || pr.nombre producto,");

        } else {

            query.append(" cat.codigo, cat.nombre categoria,")
                    .append(" pr.codigo, pr.nombre,");
        }
        query.append(" det.cantidad as cantidad_socilitada, det.cantidad_entregada, det.precio_unidad")
                .append(" from inv_orden_compra mo, inv_orden_compra_productos det, inv_proveedores mp, inv_productos pr, inv_categorias cat")
                .append(" where mo.id_orden_compra=det.id_orden_compra")
                .append(" and mo.id_proveedor=mp.id_proveedor and det.id_producto=pr.id_producto")
                .append(" and pr.id_categoria=cat.id_categoria");

        if (filtroProveedor > 0) {
            query.append(" AND me.id_paciente=").append(filtroProveedor);
        }
        if (categoria != null && categoria.getIdCategoria() > 0) {
            query.append(" AND pr.id_categoria='").append(categoria.getIdCategoria())
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
        return query.toString();

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

    public List<RepInventarioCsv> getListaReporteCsv() {
        return listaReporteCsv;
    }

    public void setListaReporteCsv(List<RepInventarioCsv> listaReporteCsv) {
        this.listaReporteCsv = listaReporteCsv;
    }

}
