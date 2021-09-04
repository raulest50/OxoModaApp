package tentactildesktoppos.negocio_r_objs;

import BD_tdpos_r.Hndl_Factura;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;


/**
 *
 * @author esteban
 */
public class Devolucion {
    
    
    /**
     * llave primaria. no plano usarla para nada en especial. solo se cumple con
     * el deber de una llave primaria para identificacion unica.
     * es entero autoincremental.
     */
    public int consecutivo;
    
    
    /**
     * Consecutivo de la factura
     */
    public int Factura_consecutivo;
    
    /**
     * precio de venta para hacer la cuenta de la devolucion
     */
    public int pventa;
    
    /**
     * cantidad de unidades que  se estan devolviendo.
     */
    public int cantidad;
    
    /**
     * fecha en que se hizo la devolucion.
     */
    public LocalDate fecha;
    
    /**
     * hora en que realizo la devolucion 
     */
    public LocalTime hora;
    
    /**
     * identificacion del vendedor 
     */
    public String Vendedor_id;
    
    /**
     * codigo del producto quue se desa devolver. llave foranea a la tabla de
     * productos.
     */
    public String Producto_codigo;
    
    /**
     * constructor vacio
     */
    public Devolucion(){
        
    }
    
    
    
    /**
     * constructor parametrizado
     * @param consecutivo
     * @param Factura_consecutivo
     * @param pventa
     * @param cantidad
     * @param fecha
     * @param hora
     * @param Vendedor_id
     * @param Producto_codigo 
     */
    public Devolucion(int consecutivo, int Factura_consecutivo, int pventa,
            int cantidad, LocalDate fecha, LocalTime hora, String Vendedor_id,
            String Producto_codigo){
        
        this.consecutivo = consecutivo;
        this.Factura_consecutivo = Factura_consecutivo;
        this.pventa = pventa;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.hora = hora;
        this.Vendedor_id = Vendedor_id;
        this.Producto_codigo = Producto_codigo;
    }
    
    
    /**
     * construye  un objeto devolucion para ser agregado a la tabla de 
     * operaciones.la fecha y hora son establecidas por las funciones
 curdate() y curtime() por lo que ponen valores sin sentido para no
 dejar nulo y lograr construir el objeto.
     * @param pr
     * @param qnt 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public Devolucion(ProductoRecord pr, int qnt) 
            throws ClassNotFoundException, SQLException{
        this.consecutivo = 0; // se deja en cero porque es autoincremental
        this.Factura_consecutivo = pr.Factura_consecutivo;
        this.pventa = pr.pventa;
        this.cantidad = qnt;
        this.fecha = LocalDate.EPOCH;
        this.hora = LocalTime.MIDNIGHT;
        Hndl_Factura hndf = new Hndl_Factura();
        this.Vendedor_id = hndf.getFacturaBy_ID(this.Factura_consecutivo).get(0).Vendedor_id;
        this.Producto_codigo = pr.Producto_codigo;
    }
}
