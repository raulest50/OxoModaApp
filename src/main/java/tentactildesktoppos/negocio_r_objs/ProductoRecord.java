package tentactildesktoppos.negocio_r_objs;

import BD_tdpos_r.Hndl_Productos;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *Clase que representa un item de una factura de venta
 * @author esteban
 */
public class ProductoRecord {
    
    /**
     * identificacion autoincremental, llave foranea Factura coonsecutivo
     */
    public int id, Factura_consecutivo, cantidad, costo, pventa;
    
    
    /**
     * llave foranea codigo del producto
     */
    public String Producto_codigo;
    
    
    /**
     * constructor normal
     * @param id
     * @param Factura_consecutivo
     * @param cantidad
     * @param costo
     * @param pventa
     * @param Producto_codigo
     */
    public ProductoRecord(int id, int Factura_consecutivo, int cantidad,
            int costo, int pventa, String Producto_codigo){
        
        this.id = id;
        this.Factura_consecutivo = Factura_consecutivo;
        this.cantidad = cantidad;
        this.costo = costo;
        this.pventa = pventa;
        this.Producto_codigo = Producto_codigo;
        
    }
    
    /**
     * Constructor que se debe usar principalmente en el controlador
     * de ventas. tener en cuenta que no se inicializa el id de la factura ya 
     * que este  numero se genera a la hora de registrar.
     * 
     * @param p
     * @param cantidad 
     * @param tipoVenta 
     * @param c 
     */
    public ProductoRecord(Producto p, int cantidad, String tipoVenta, Cliente c){
        this.Producto_codigo = p.codigo;
        this.costo = p.costo;
        this.cantidad = cantidad;
        this.pventa = this.DeterminarPventa(tipoVenta, c.tipo, p);
    }
    
    /**
     * metodo que usa el String del codigo del producto para 
     * traer su descripcion de la tabla de productos 
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public String getDescripcionFromProducto() 
            throws SQLException, ClassNotFoundException{
        Hndl_Productos hndp = new Hndl_Productos();
        return hndp.buscarProducto_codigo(this.Producto_codigo).get(0).descripcion;
    }
    
    /**
     * metodo que usa el String del codigo del producto para 
     * traer su pprecio de venta de contado de la tabla de productos 
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public int getPvContadoFromProducto() 
            throws SQLException, ClassNotFoundException{
        Hndl_Productos hndp = new Hndl_Productos();
        return hndp.buscarProducto_codigo(this.Producto_codigo).get(0).pventa_contado;
    }
    
    /**
     * metodo que usa el String del codigo del producto para 
     * traer su precio de venta a credito de la tabla de productos 
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public int getPvCreditoFromProducto() 
            throws SQLException, ClassNotFoundException{
        Hndl_Productos hndp = new Hndl_Productos();
        return hndp.buscarProducto_codigo(this.Producto_codigo).get(0).pventa_credito;
    }
    
    /**
     * metodo que usa el String del codigo del producto para 
     * traer su descripcion de la tabla de productos 
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public int getPvMayorFromProducto() 
            throws SQLException, ClassNotFoundException{
        Hndl_Productos hndp = new Hndl_Productos();
        return hndp.buscarProducto_codigo(this.Producto_codigo).get(0).pventa_mayor;
    }
    
    
    /**
     * metodo que determina el precio de venta de un producto dependiendo de la
     * clasificacion del cliente y/o del tipo de venta que pupede ser de contado 
     * o de credito. este metodo retorna el precio de venta pero
     * tambien lo asigna a esta instancia.
     * @param tipoVenta
     * @param tipoCliente
     * @param p
     * @return 
     */
    public final int DeterminarPventa(String tipoVenta, String tipoCliente, Producto p){
        int pv;
        if(tipoCliente.equals(Cliente.TIPO_MAYOR)){ // si es un cliente de pormayor el precio de venta
            // es indenpendiente de si la compra es a credito o de contado.
            pv = p.pventa_mayor;
        } else{ // en caso de que el cliente sea de tipo contado o credito
            
            if(tipoVenta.equals(Factura.TIPOVENTA_CONTADO)){ // si la venta es de contado
                pv = p.pventa_contado;
            } else{ // si la venta es de credito
                 pv = p.pventa_credito;
            }
        }
        this.pventa = pv;
        return pv;
    }
    
    
    /**
     * para evitar redundancia de datos, no se guardan todos los datoss del producto x
     * en la tabla de producto record sino solo el codigo como llave foranea.
     * Esto lleva a la necesidad de usar metodoss que entreguen el producto
     * correspondiente a un producto record.
     * @return 
     */
    public Producto getProducto() 
            throws ClassNotFoundException, SQLException{
        ArrayList<Producto> lp = new ArrayList<>();
        Hndl_Productos hndp = new Hndl_Productos(); //si existe el producto record, como
        // consecuencia el producto debe tambien de existir ya que la BD esta configurada
        // para cuidar la integridad relacional.
        lp = hndp.buscarProducto_codigo(this.Producto_codigo);
        return lp.get(0);
    }
    
    
}
