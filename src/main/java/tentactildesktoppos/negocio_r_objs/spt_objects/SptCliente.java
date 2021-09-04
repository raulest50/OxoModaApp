package tentactildesktoppos.negocio_r_objs.spt_objects;

import BD_tdpos_r.Hndl_Abonos;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tentactildesktoppos.negocio_r_objs.Cliente;

/**
 *Representacion de un cliente para un TableView.
 * @author esteban
 */
public class SptCliente extends Cliente{
    
    /**
     * para las columnas de cc, nombre fecha del ultimo abono.
     */
    SimpleStringProperty spt_id, spt_nombre, spt_last_abono, spt_tipo_cobro;
    
    /**
     * propiedad que muestra en la tabla de clientes el consolidado de cada cliente.
     */
    SimpleIntegerProperty spt_consolidado;
    
    
    // constructores
    public SptCliente(String id, String nombre, String tel1, String tel2, 
            String tel3, String direccion, String email, String descripcion, 
            LocalDate cumple, String tipo, String ubicacion) 
            throws SQLException, ClassNotFoundException {
        
        super(id, nombre, tel1, tel2, tel3, direccion, email, descripcion, cumple, tipo, ubicacion);
        
        this.spt_id = new SimpleStringProperty(super.id);
        this.spt_nombre = new SimpleStringProperty(super.nombre);
        this.spt_consolidado = new SimpleIntegerProperty(super.getConsolidado());
        
        Hndl_Abonos hndab = new Hndl_Abonos();
        // se pone la fecha del ultimo abono del cliente seleccionado.
        this.spt_last_abono = new SimpleStringProperty(hndab.getLastAbonoFromCliente(super.id));
        if(super.direccion.equals(Cliente.HACER_COBRO)) this.spt_tipo_cobro = new SimpleStringProperty("c");
        else this.spt_tipo_cobro = new SimpleStringProperty("v");
        
    }

    
    public SptCliente(Cliente c) 
            throws SQLException, ClassNotFoundException {
        super(c.id, c.nombre, c.tel1, c.tel2, c.tel3, c.direccion, c.email, 
                c.descripcion, c.cumple, c.tipo, c.ubicacion);
        
        this.spt_id = new SimpleStringProperty(super.id);
        this.spt_nombre = new SimpleStringProperty(super.nombre);
        this.spt_consolidado = new SimpleIntegerProperty(super.getConsolidado());
        
        Hndl_Abonos hndab = new Hndl_Abonos();
        // se pone la fecha del ultimo abono del cliente seleccionado.
        this.spt_last_abono = new SimpleStringProperty(hndab.getLastAbonoFromCliente(super.id));
        
        if(super.descripcion.equals(Cliente.HACER_COBRO)) this.spt_tipo_cobro = new SimpleStringProperty("c");
        else this.spt_tipo_cobro = new SimpleStringProperty("v");
    }

    
    // metodos get para uso de la api de javafx, en el table view de clientes.
    public String getSpt_id() {
        return spt_id.get();
    }

    public String getSpt_nombre() {
        return spt_nombre.get();
    }

    public String getSpt_last_abono() {
        return spt_last_abono.get();
    }

    public Integer getSpt_consolidado() {
        return spt_consolidado.get();
    }

    public String getSpt_tipo_cobro() {
        return spt_tipo_cobro.get();
    }
    
    
    /**
     * metodo que construye en un solo String los demas datos que no
     * se pueden mostrar en la tabla de busqueda de clientes.
     * 
     * @return 
     */
    public String retrieveRemainingData(){
        String r = "";
        r = r.concat("Telefono 1: " + this.tel1);
        r = r.concat("\n" + "Telefono 2: " + this.tel2);
        r = r.concat("\n" +  "Telefono 3: " + this.tel3);
        
        r = r.concat("\n" + "Direccion: " + this.direccion);
        r = r.concat("\n" + "Email: " + this.email);
        r = r.concat("\n" + "Cummpleaños: " + this.cumple.toString());
        r = r.concat("\n" + "Ubicacion: " + this.ubicacion);
        r = r.concat("\n" + "Tipo de Cliente: " + this.tipo);
        r = r.concat("\n" + "Descripcion: " + this.descripcion);
        return r;
    }

    /**
     * retorna true si este es el cliente generico
     * @return 
     */
    public boolean isGeneric() {
        return this.id.equals("123");
    }
    
}
