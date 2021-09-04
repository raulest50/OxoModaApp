package tentactildesktoppos.negocio_r_objs;


import BD_tdpos_r.Hndl_Abonos;
import BD_tdpos_r.Hndl_Factura;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;

/**
 *
 * @author cliente
 */
public class Cliente {
    
    
    /**
     * precio de venta normal.
     * uno de los tipos de clientes validos del programa.
     */
    public static final String TIPO_CONTADO = "Contado";
    
    
    /**
     * cliente de pago a credito - usualmente corresonde a los precios de venta
     * mas caro
     * uno de los tipos de clientes validos del programa.
     */
    public static final String TIPO_CREDITO = "Credito";
    
    
    /**
     * por mayor -  usualmente corresonde a los precioss mas baratos
     * uno de los tipos de clientes validos del programa.
     */
    public static final String TIPO_MAYOR = "Mayorista";
    
    
    
    /**
     * String que se usa para el combo box de forma de pago.
     * indica que es un cliente al que se debe mandar a cobrar.
     */
    public static final String HACER_COBRO = "Se Manda a Cobrar";
    
    
    /**
     * String que se usa para el combo box de forma de pago.
     * indica que al cliente no se le debe mandar a cobrar sino quee por el 
     * contrario el cliente viene a pagar al local.
     */
    public static final String VIENE_PAGAR = "Viene a pagar";
    
    /**
     * cedula o nit del cliente.
     */
    public String id;
    
    
    /**
     * nombre completo con apellidos del cliente
     */
    public String nombre;
    
    
    /**
     * telefono 1
     */
    public String tel1;
    public String tel2;
    public String tel3;
    public String direccion;
    public String email;
    
    
    /**
     * inicialmente se penso como descripcion opcional y arbitraria 
     * del cliente para cualquier proposito. pero se decidio elminar esta 
     * caracteristica y en su lugar agregar otro atributo para el cliente que 
     * permita identificar cuando este realiza los abonos en el local
     * o hay que ir a cobrarle. se reusa entoncces el campo descripcion
     * para esta finalidad y asi minimizar la cantidad de cambios a codigo fuente.
     */
    public String descripcion;
    
    
    /**
     * cumpleaños del cliente
     */
    public LocalDate cumple;
    
    
    /**
     * tipo de cliente. determina que tipos de compra puede hacer un cliente.
     * un cliente de contado solo puede hacer compras de contado.
     * pero un cliente de credito o por mayor puede hacer commpras no solo de 
     * contado sino tambien de credito.
     */
    public String tipo;
    
    
    /**
     * string arbitrario para decribir la ubicacion de un cliente.
     */
    public String ubicacion;
    
    
    public Cliente(String id, String nombre, String tel1, String tel2, String tel3, 
            String direccion, String email, String descripcion, LocalDate cumple, String tipo, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.tel3 = tel3;
        this.direccion = direccion;
        this.email = email;
        this.descripcion = descripcion; // tipo de cliente.
        this.cumple = cumple; // puede ser null
        this.tipo = tipo;
        this.ubicacion = ubicacion;
    }
    
    

    /**
     * constructor vacio.
     */
    public Cliente() {
    }
    
    
    /**
     * metodo que construye en un solo String los demas datos que no
     * se pueden mostrar en la tabla de busqueda de clientes.
     * 
     * @return 
     */
    public String InfoClienteStr(){
        String r = "";
        
        r = r.concat("Nombre: " + this.nombre);
        r = r.concat("\n" + "Telefono 1: " + this.tel1);
        r = r.concat("\n" + "Telefono 2: " + this.tel2);
        r = r.concat("\n" + "Telefono 3: " + this.tel3);
        
        r = r.concat("\n" + "Direccion: " + this.direccion);
        r = r.concat("\n" + "Email: " + this.email);
        r = r.concat("\n" + "Cumpleaños: " + this.cumple.toString());
        r = r.concat("\n" + "Ubicacion: " + this.ubicacion);
        r = r.concat("\n" + "Tipo: " + this.tipo);
        r = r.concat("\n" + "Descripcion Forma Pago: " + this.descripcion);
        return r;
    }
    
    
    /**
     * trae de la base de datos todas las facturas con saldo mayor que cero 
     * y suma sus saldos para calcular el total de la deuda del cliente seleccionado.
     * @return 
     */
    public int getConsolidado() 
            throws SQLException, ClassNotFoundException{
        
        int sum = 0; // acumulacion de los saldos para el consolidado
        Hndl_Factura hndfac = new Hndl_Factura();
        ObservableList<SptFactura> lfs = hndfac.getFacturasByClienteID_saldo_no_zero(this.id);
        
        // se hace la acumulacion de los saldos de cada factura.
        for(SptFactura f : lfs){
            sum += f.factura.saldo;
        }
       return sum;
    }
    
}
