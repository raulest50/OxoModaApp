package tentactildesktoppos.negocio_r_objs.spt_objects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tentactildesktoppos.negocio_r_objs.Producto;

/**
 *Clase que hereda de producto. su unica funcion es encapsular las variables
 * y estructura necesaria para ser usado con la tableView.
 * 
 * la creacion de este objeto es necesaria para poder hacer uso del TableView de
 * java fx. sin embargo esta clase no  es  util fuera de java FX. por tanto
 * se hace que esta clase herede de producto asi el objeto producto
 * es portable y al mismo timepo se extiende su comportamiento para poder 
 * mostrarlo en un table view de javafx.
 * @author esteban
 */
public class SptProducto extends Producto{
    
    public SimpleStringProperty spt_codigo, spt_descripcion;
    
    public SimpleIntegerProperty spt_costo, spt_pv_contado, spt_pv_credito, 
            spt_pv_mayor, spt_iva, spt_stock;
    
    /**
     * fecha de ingreso del producto.
     * la clase padre implementa la fecha con LocalDate pero
     * para mostrarlo en la tableView este SPT lo implementa como
     * String
     */
    public SimpleStringProperty spt_fingreso; 
    
    
    /**
     * constructor que no solo instancia este objeto sino tambien
     * los atributos de clase padre.
     * 
     * @param p 
     */
    public SptProducto(Producto p){
        
        // inicializacion de las variables de la super clase
        super.codigo = p.codigo;
        super.descripcion = p.descripcion;
        super.costo = p.costo;
        super.pventa_contado = p.pventa_contado;
        super.pventa_credito = p.pventa_credito;
        super.pventa_mayor = p.pventa_mayor;
        super.fingreso = p.fingreso;
        super.stock = p.stock;
        
        super.iva = p.iva;
        
        
        // extended
        this.spt_codigo = new SimpleStringProperty(super.codigo);
        this.spt_descripcion = new SimpleStringProperty(super.descripcion);
        
        this.spt_costo = new SimpleIntegerProperty(super.costo);
        this.spt_pv_contado = new SimpleIntegerProperty(super.pventa_contado);
        this.spt_pv_credito = new SimpleIntegerProperty(super.pventa_credito);
        this.spt_pv_mayor = new SimpleIntegerProperty(super.pventa_mayor);
        this.spt_iva = new SimpleIntegerProperty(super.iva);
        this.spt_stock = new SimpleIntegerProperty(super.stock);
        this.spt_fingreso = new SimpleStringProperty(super.fingreso.toString());
    }

    public String getSpt_codigo() {
        return spt_codigo.get();
    }

    public String getSpt_descripcion() {
        return spt_descripcion.get();
    }

    public Integer getSpt_costo() {
        return spt_costo.get();
    }

    public Integer getSpt_pv_contado() {
        return spt_pv_contado.get();
    }

    public Integer getSpt_pv_credito() {
        return spt_pv_credito.get();
    }

    public Integer getSpt_pv_mayor() {
        return spt_pv_mayor.get();
    }

    public Integer getSpt_iva() {
        return spt_iva.get();
    }

    public Integer getSpt_stock() {
        return spt_stock.get();
    }

    public String getSpt_fingreso() {
        return spt_fingreso.get();
    }
    
}
