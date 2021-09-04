package tentactildesktoppos.negocio_r_objs.spt_objects;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tentactildesktoppos.negocio_r_objs.Factura;

/**
 *Clase que se usa como requerimiento de FX para mostrar representaciones
 * de las facturas en el TableView de busque da de facturas.
 * @author esteban
 */
public class SptFactura {
    
    /**
     * Instancia a para acceder a los atributos originales de la factura
     */
    public Factura factura;
    
    /**
     * consecutivo de la factura en un formato apropiado para un table view
     */
    public SimpleIntegerProperty spt_consecutivo, spt_saldo;
    
    /**
     * cc y nombre del cliente asociado a la factura para mostrar en un table view
     */
    public SimpleStringProperty spt_Cliente_id, spt_Cliente_nombre;
    
    /**
     * fecha de emision de la factura y del ultimo abono.
     */
    public SimpleStringProperty spt_fecha_registro, spt_fecha_last_abono;

    
    
   /**
    * constructor por defecto. Es el que se debe usar para fines operacionales.
    * @param factura
    */
    public SptFactura(Factura factura)
            throws ClassNotFoundException, SQLException {
        this.factura = factura;
        this.spt_consecutivo = new SimpleIntegerProperty(factura.consecutivo);
        this.spt_saldo = new SimpleIntegerProperty(factura.saldo);
        this.spt_Cliente_id = new SimpleStringProperty(factura.Cliente_id);
        this.spt_Cliente_nombre = new SimpleStringProperty(factura.getCliente().nombre);
        this.spt_fecha_registro = new SimpleStringProperty(factura.fecha.toString());
        this.spt_fecha_last_abono = new SimpleStringProperty(factura.getLastAbonoFecha().toString());
    }
    
    
    
    /**
     * constructor de test
     * @param spt_consecutivo
     * @param spt_saldo
     * @param spt_Cliente_id
     * @param spt_Cliente_nombre
     * @param spt_fecha_registro
     * @param spt_fecha_last_abono 
     */
    public SptFactura(SimpleIntegerProperty spt_consecutivo, 
            SimpleIntegerProperty spt_saldo, 
            SimpleStringProperty spt_Cliente_id, 
            SimpleStringProperty spt_Cliente_nombre, 
            SimpleStringProperty spt_fecha_registro, 
            SimpleStringProperty spt_fecha_last_abono) {
        
        this.spt_consecutivo = spt_consecutivo;
        this.spt_saldo = spt_saldo;
        this.spt_Cliente_id = spt_Cliente_id;
        this.spt_Cliente_nombre = spt_Cliente_nombre;
        this.spt_fecha_registro = spt_fecha_registro;
        this.spt_fecha_last_abono = spt_fecha_last_abono;
        
    }
    
    
    
    public Factura getFactura() {
        return factura;
    }

    public Integer getSpt_consecutivo() {
        return spt_consecutivo.get();
    }

    public String getSpt_Cliente_id() {
        return spt_Cliente_id.get();
    }

    public String getSpt_Cliente_nombre() {
        return spt_Cliente_nombre.get();
    }

    public String getSpt_fecha_registro() {
        return spt_fecha_registro.get();
    }

    public String getSpt_fecha_last_abono() {
        return spt_fecha_last_abono.get();
    }

    
    public Integer getSpt_saldo() {
        return spt_saldo.get();
    }
    
    
    
    /**
     * metodo que genera una representacion String de esta factura,
     * con sus datos y records. Este metodo se usa en la seccion de busqueda de
     * facturas para mostrar la informacion de la factura que este seleccionada
     * en la tabla al haccer doble click.
     * 
     * AUN ANALIZANDO SI USO O NO EL METODO.
     * 
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public String str_rep() 
            throws ClassNotFoundException, SQLException{
        String r = "";
        ArrayList<SptFacturaRecord> lr = this.factura.getFacturaRecords();
        
        // se agrega la informacion de la factura
        r += "Consecutivo: " + Integer.toString(this.factura.consecutivo) + "\n";
        r += "Nombre Cliente: " + this.spt_Cliente_nombre.get() + "\n";
        r += "ID Cliente: " + this.spt_Cliente_id.get() + "\n";
        
        for(SptFacturaRecord fr : lr){
            // se agregan los records de la factura
            switch(fr.behave){
                
                case SptFacturaRecord.TIPO_PRODUCTO_RECORD:
                    break;
                    
                case SptFacturaRecord.TIPO_CUPON_RECORD:
                    break;
                    
                case SptFacturaRecord.TIPO_DEVOLUCION_RECORD:
                    break;
                    
                case SptFacturaRecord.TIPO_ABONO_RECORD:
                    break;
            }
        }
        return r;
    }
    
}
