package tentactildesktoppos.negocio_r_objs;

import BD_tdpos_r.Hndl_Clientes;
import BD_tdpos_r.Hndl_Factura;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * clase que encapsula y representa el ente cupon.
 * se trata de un herramienta
 * @author esteban
 */
public class Cupon {
    
    
    public int id;
    
    /**
     * Identificacion del cliente al que se entrega el cupon
     */
    public String Cliente_id;
    
    /**
     * valor del cupon en pesos.
     */
    public int valor;
    
    /**
     * fecha en la que creo el cupon
     */
    public LocalDate fecha;
    
    /**
     * estado del cupon.
     * 1 => el cupon esta activo y puede usarse en una compra
     */
    public int estado;
    
    /**
     * numero aleatorio que se asigna a un cupon para labores de validacion
     */
    public int pass;
    
    /**
     * consecutivo de la factura de la factura en que se genera el cupon.
     * los cuponnes solo se pueden generar sobre facturas con ingresos de 
     * efectivo y saldo 0. el valor de cupon nunca puede superar el ingreso de 
     * efectivo de la factura.
     */
    public int Factura_consecutivo_gen;
    
    /**
     * consecutivo de la factura en que se gasta  el bono o cupon
     */
    public int Factura_consecutivo_spend;
    
    /**
     * llave foranea de la tabla de Vendedores.
     * es la cedula del vendodor que hizo consumo o generacion del bono
     */
    public String Vendedor_id;
    
    /**
     * constructor vacio
     */
    public Cupon(){}

    
    /**
     * copnstructor con todos los atributos, ideal para leer de la base de datos.
     * @param id
     * @param Cliente_id
     * @param valor
     * @param fecha
     * @param estado
     * @param pass
     * @param Factura_consecutivo_gen
     * @param Vendedor_id 
     * @param Factura_consecutivo_spend 
     */
    public Cupon(int id, String Cliente_id, int valor, LocalDate fecha, 
            int estado, int pass, int Factura_consecutivo_gen, String Vendedor_id,
            int Factura_consecutivo_spend) {
        this.id = id;
        this.Cliente_id = Cliente_id;
        this.valor = valor;
        this.fecha = fecha;
        this.estado = estado;
        this.pass = pass;
        this.Factura_consecutivo_gen = Factura_consecutivo_gen;
        this.Factura_consecutivo_spend = Factura_consecutivo_spend;
        this.Vendedor_id = Vendedor_id;
    }
    
    
    /**
     * metodo que usa la llave primaria guardada en esta clase y se emplea para
     * construir una instancia del cliente asociado
     * @return 
     */
    public Cliente getCliente() 
            throws ClassNotFoundException, SQLException{
        Hndl_Clientes hndcli = new Hndl_Clientes();
        return hndcli.buscarCliente_id(this.Cliente_id).get(0);
    }

    
    /**
     * metodo que usa la llave primaria guardada en esta clase y se emplea para
     * construir una instancia de la factura de generacion asociada. asociado
     * @return 
     */
    public Factura getFacturaGen() 
            throws ClassNotFoundException, SQLException{
        Hndl_Factura hndfac = new Hndl_Factura();
        return hndfac.getFacturaBy_ID(this.Factura_consecutivo_gen).get(0);
    }
    
    
}
