/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.entidades;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Arcosoft-PC2
 */
@Entity
@Table(name = "cfg_historia_campos_predefinidos", schema = "public")
@XmlRootElement
public class CfgHistoriaCamposPredefinidos implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @JoinColumn(name = "id_campo", referencedColumnName = "id_campo")
    @ManyToOne
    private HcCamposReg idCampo;
    @Column(name = "valor", length = 2147483647)
    private String valor;

    public CfgHistoriaCamposPredefinidos(){
        idCampo = new HcCamposReg(0);
        
    }
    public CfgHistoriaCamposPredefinidos(Integer id, HcCamposReg idCampo, String valor) {
        this.id = id;
        this.idCampo = idCampo;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HcCamposReg getIdCampo() {
        return idCampo;
    }

    public void setIdCampo(HcCamposReg idCampo) {
        this.idCampo = idCampo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HcCamposReg)) {
            return false;
        }
        CfgHistoriaCamposPredefinidos other = (CfgHistoriaCamposPredefinidos) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entidades2.CfgHistoriaCamposPredefinidos[ id=" + id + " ]";
    }
    
}
