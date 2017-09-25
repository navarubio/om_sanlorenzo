
package modelo.entidades;

import java.io.Serializable;

/**
 *
 * @author luis
 */
public class WrapperHcItems extends HcItems implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String codigo;
    private String descripcion;
    private String presentacion;
    private String concentracion;
    private String viaAdmin;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getViaAdmin() {
        return viaAdmin;
    }

    public void setViaAdmin(String viaAdmin) {
        this.viaAdmin = viaAdmin;
    }

    
    
}
