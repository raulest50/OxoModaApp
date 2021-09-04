package tentactildesktoppos.negocio_r_objs.spt_objects;

import BD_tdpos_r.Hndl_Productos;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tentactildesktoppos.negocio_r_objs.Abono;
import tentactildesktoppos.negocio_r_objs.Cupon;
import tentactildesktoppos.negocio_r_objs.Devolucion;
import tentactildesktoppos.negocio_r_objs.Producto;
import tentactildesktoppos.negocio_r_objs.ProductoRecord;

/**
 *
 * @author esteban
 */
public class SptFacturaRecord {
    
    
    // atributos correspondientes a la ui.
    public SimpleStringProperty spt_codigo, spt_descripcion;
    public SimpleIntegerProperty spt_precio_venta, spt_cantidad, spt_subtotal;
    
    /**
     * se usa principalmennte para records  tipo abono y devolucion.
     * 
     * solo se creo para ser usado en el constructor que usa un abono
     * y en el constructor que usa una devolucion. luego su respectivo
     * metodo get permite su uso con el table view de la seccion de abonos
     * y devluciones.
     */
    public SimpleStringProperty spt_fecha = new SimpleStringProperty("");
    
    // atributos correspondientes a la capa de logica
    public ProductoRecord producto_record;
    
    public Cupon cupon;
    
    public Devolucion devolucion;
    
    public Abono abono;
    
    /**
     * String que indica quee tipo de factura record es:
     * Abono, ProductoRecord, Cupon, Devolucion.
     * 
     * java no soporta multiple heredacion.
     * analizando las relaciones entre todos los entes que se relacionan con
     * el software determine que lo mas apropiado es la creacion de un objeto
     * tipo Factura record el cual permita visualizar en un mismo TableView
     * 4 objetos distintoss, ProductoRecord, Abono, Devolucion, Cupon.
     * para esto la clase factura record encapsula los cuatro objetos
     * y este String behave el cual indica de cual de estos  objetos debe
     * "heredar comportamiento". recuerdese que no se heredacion en el sentido 
     * estricto de la palabra ssino mas bien una heredacion artificial que
     * se maneja programaticamente con if's, contructores y metodos de esta y
     * demas clases del programa. 
     */
    public String behave;
    
    
    
    /**
     * identificador para un producto record
     */
    public static final String TIPO_PRODUCTO_RECORD = "PRODUCTO_RECORD";
    
    /**
     * identificador para un record tipo de volucion
     */
    public static final String TIPO_DEVOLUCION_RECORD = "DEVOLUCION_RECORD";
    
    /**
     * identificador para un record tipo abono
     */
    public static final String TIPO_ABONO_RECORD = "ABONO_RECORD";
    
    /**
     * identificador para un record tipo cupon
     */
    public static final String TIPO_CUPON_RECORD = "CUPON_RECORD";
    
    
    /**
     * String para poner como descripcion de un record tipo abono - ingreso de efectivoo
     */
    public static final String DESCRI_ABONO = "Ingreso de Efectivo";
    
    /**
     * String para poner como descripcion de un record tipo cupo ya redimido
     */
    public static final String DESCRI_CUPON = "Cupon";
    
    
    /**
     * String para concatenar con descripcion de un producto para indicar que 
     * este es una devolucion y no un record de compra en la tabla de 
     * records del acordeon de abonos y devoluciones.
     */
    public final String DEVOLUCION_STR = "(Devolucion) ";
    
    /**
     * contruye una factura record a partir de un producto record
     * @param pr 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public SptFacturaRecord(ProductoRecord pr) 
            throws SQLException, ClassNotFoundException{
        
        this.behave = SptFacturaRecord.TIPO_PRODUCTO_RECORD; // se indica el tipo de record

        this.producto_record = pr;
        
        this.spt_codigo = new SimpleStringProperty(pr.Producto_codigo);
        this.spt_descripcion = new SimpleStringProperty(pr.getDescripcionFromProducto());
        
        this.spt_precio_venta = new SimpleIntegerProperty(pr.pventa);
        this.spt_cantidad = new SimpleIntegerProperty(pr.cantidad);
        this.spt_subtotal = new SimpleIntegerProperty(this.getSpt_precio_venta()*this.getSpt_cantidad());
        
    }
    
    
    /**
     * construye a partir de un objeto devolucion
     * @param dev 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public SptFacturaRecord(Devolucion dev) 
            throws ClassNotFoundException, SQLException{
        this.behave = SptFacturaRecord.TIPO_DEVOLUCION_RECORD;
        this.devolucion = dev;
        this.spt_codigo = new SimpleStringProperty(this.devolucion.Producto_codigo);
        Hndl_Productos hndp = new Hndl_Productos();
        Producto p = hndp.buscarProducto_codigo(this.spt_codigo.get()).get(0);        
        //se agrega a la descripcion para indicar que se trata de un record tipo devolucion.
        this.spt_descripcion = new SimpleStringProperty(this.DEVOLUCION_STR + p.descripcion);
        this.spt_codigo = new SimpleStringProperty(this.devolucion.Producto_codigo);
        this.spt_cantidad = new SimpleIntegerProperty(dev.cantidad);
        this.spt_precio_venta = new SimpleIntegerProperty(dev.pventa);
        this.spt_subtotal = new SimpleIntegerProperty(dev.cantidad*dev.pventa);
        this.spt_fecha = new SimpleStringProperty(dev.fecha.toString()+ " " + dev.hora.toString());
    }
    
    /**
     * Contruye a partir de un cupon.
     * los campos spt se inicializan deacuerdo al tipo de comportamiento
     * Behave.
     * @param cup 
     */
    public SptFacturaRecord(Cupon cup){
        this.behave = SptFacturaRecord.TIPO_CUPON_RECORD;
        this.cupon = cup;
        this.spt_codigo = new SimpleStringProperty("");
        this.spt_descripcion = new SimpleStringProperty(SptFacturaRecord.DESCRI_CUPON);
        this.spt_cantidad = new SimpleIntegerProperty(1);
        this.spt_precio_venta = new SimpleIntegerProperty(this.cupon.valor);
        this.spt_subtotal = new SimpleIntegerProperty(this.cupon.valor);
    }
    
    
    /**
     * construye a partir de un onjeto tipo abono
     * @param abo 
     */
    public SptFacturaRecord(Abono abo){
        this.behave = SptFacturaRecord.TIPO_ABONO_RECORD;
        this.abono = abo;
        this.spt_codigo = new SimpleStringProperty("");
        this.spt_descripcion = new SimpleStringProperty(SptFacturaRecord.DESCRI_ABONO);
        this.spt_cantidad = new SimpleIntegerProperty(1);
        this.spt_precio_venta = new SimpleIntegerProperty(this.abono.valor);
        this.spt_subtotal = new SimpleIntegerProperty(this.abono.valor);
        this.spt_fecha = new SimpleStringProperty(abo.fecha.toString() + " " + abo.hora.toString());
    }

    /**
     * empty constructor
     */
    public SptFacturaRecord() {}
    
    /**
     * este metodo agrega +1 a la cantidad de un producto record.
     * Este metodo solo se debe incovar si esta Factura record se comporta como
     * un ProdcutoRecord
     */
    public void OnePlusProducto(){
        // solo si hay un producto record
        if(!(this.producto_record == null)){
            this.setSpt_cantidad(this.getSpt_cantidad()+1);// se usa el metodo set
            // ya que actualiza el subtotal.
            this.producto_record.cantidad = this.getSpt_cantidad();
        }
    }
    
    
    /**
     * Equivalente a oneplus producto para el caso de un record tipo devolucion
     */
    public void OnePlusDevolucion(){
        // solo si hay un producto record
        if(!(this.devolucion == null)){
            this.setSpt_cantidad(this.getSpt_cantidad()+1);// se usa el metodo set
            // ya que actualiza el subtotal.
            this.devolucion.cantidad = this.getSpt_cantidad();
        }
    }
    
    
    /**
     * este metodo reduce -1 a la cantidad de un producto record.
     * Este metodo solo se debe incovar si esta Factura record se comporta como
     * un ProdcutoRecord
     */
    public void OneMinusProducto(){
        // solo si hay un producto record
        if(!(this.producto_record == null)){
            this.setSpt_cantidad(this.getSpt_cantidad()-1); // se usa el metodo
            // set que actualiza el subtotal.
            this.producto_record.cantidad = this.getSpt_cantidad();
        }
    }
    
    
    /**
     * equivalente al -1 para records tiop devolucion.
     * solo usar para devoluciones en la tabla de operaciones.
     */
    public void OneMinusDevolucion(){
        // solo si hay un producto record
        if(!(this.devolucion == null)){
            this.setSpt_cantidad(this.getSpt_cantidad()-1); // se usa el metodo
            // set que actualiza el subtotal.
            this.devolucion.cantidad = this.getSpt_cantidad();
        }
    }
    
    
    // los getters de esta clase se ponen principalmente para que sean usados por la 
    // API de FX en el rendering de los table view que involucren este objeto
    
    /**
     * este metodo esta hecho principalmente para ser pasado como argumento
     * a la api de java collections. se trata de un metodo usado en la invocacion
     * de .stream().collect(groupingBy(Object :: getSpt_codigo()).
     * se usa unicamente para producto records y devoluciones.
     * @return 
     */
    public String getSpt_codigo() {
        String r="";
        
        if(this.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)) r = this.producto_record.Producto_codigo;
        
        if(this.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)) r = this.devolucion.Producto_codigo;
            
        return r;
    }
    
    public SimpleStringProperty getSptCodigo() {
        return spt_codigo;
    }

    public String getSpt_descripcion() {
        return spt_descripcion.get();
    }
    
    public final Integer getSpt_precio_venta() {
        return spt_precio_venta.get();
    }
    
    public final Integer getSpt_cantidad() {
        return spt_cantidad.get();
    }
    
    public Integer getSpt_subtotal() {
        return spt_subtotal.get();
    }
    
    public String getSpt_fecha() {
        return spt_fecha.get();
    }
    
    
    /**
     * metodo que solo se debe usar para el caso de un record tipo productoRecord.
     * Calcula el minimo porcentaje de rebaja admisible. lo retorna como int 
     * en la primera posicion del arreglo. en la segunda posicion se 
     * ingresa este mismo valor pero String.
     * 
     * @return 
     */
    public ArrayList MinPercent()
            throws ClassNotFoundException, SQLException{
        ArrayList min = new ArrayList();
        
        // se crean como double para evitar posible perdida de robustez por truncamiento.
        double pv = this.spt_precio_venta.get();
        double pvmayor = this.producto_record.getProducto().pventa_mayor;
        
        // se hace el calculo del minimo porcentage usando double y redondeo hacia abajo
        // para evitar perdida de dinero por truncamiento.
        double aux = (100*(pv-pvmayor)/pv);
        int min_percent = (int) Math.ceil(aux);
        
        min.add(min_percent); // se agrega porcentaje minimo como int
        min.add(Integer.toString(min_percent)); // como String
        
        return min;
    }
    
    /**
     * metodo que hace una reduccion del precio de venta por el porcentaje
     * especificado por percent. Este metodo no valida que el porcentaje de
     * reduccion no cambie precio por debajo del precio de costo por lo que esta 
     * validacion se debe hacer previamente.
     * @param percent 
     */
    public void SetDescuento(int percent){
        double pv = this.getSpt_precio_venta(); // se copia el precio de venta
        double prc = percent; // se toma el porcentaje como double
        prc = 1 - prc/100; // se pasa de porcentaje a fraccion double
        
        // se hace la multiplicacion para hacer el descuento
        // y se establece en la variable de pprecio de costo
        this.setSpt_precio_venta( (int) Math.floor(prc*pv)); 
    }

    
    
    // setter methods.
    
    public void setSpt_codigo(String spt_codigo) {
        this.spt_codigo = new SimpleStringProperty(spt_codigo);
    }

    public void setSpt_descripcion(String spt_descripcion) {
        this.spt_descripcion = new SimpleStringProperty(spt_descripcion);
    }

    /**
     * El subtotal debe ser siempre congruente con la cantidad y el precio de venta
     * por lo que en los 2 setter methods (set pv y set cantidad) se agrega una 
     * linea adicional para recalcular el subtotal.
     * @param spt_precio_venta 
     */
    public void setSpt_precio_venta(int spt_precio_venta) {
        this.setSpt_subtotal(spt_precio_venta * this.getSpt_cantidad());
        this.spt_precio_venta = new SimpleIntegerProperty(spt_precio_venta);
        
        // arreglando un bug.
        /**
         * cuando se establecia un precio arbitrario para un item de la tabla de ventas, se modificaba el precio de
         * venta de la clase encapsuladora pero no del producto record interno. esto daba lugar a saldos erroneos
         * entre otros por ovbias razones.
         * 
         * en caso de que este metodo sea usado en casos diferentes entonces se usa este if
         * para que solo afecte el caso de un producto record.
         */
        if(behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
            producto_record.pventa = spt_precio_venta;
        }
    }
    
    
    public void setSpt_cantidad(int spt_cantidad) {
        this.spt_cantidad = new SimpleIntegerProperty(spt_cantidad);
        this.setSpt_subtotal(spt_cantidad * this.getSpt_precio_venta());
    }

    public void setSpt_subtotal(int spt_subtotal) {
        this.spt_subtotal = new SimpleIntegerProperty(spt_subtotal);
    }
    
    
    /**
     * Metodo que se invoca cuando se cambia el tipo de venta en la pestaña de
     * ventas.cuando en el combo box se pasa por ejempo de venta credito
 a contado, se debe cambair a todos los productos de la lista un precio
 de venta a contado, que suele ser mas barato.
     * @param tipoVenta
     * @param TipoCliente 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public void ChangeSellPrice(String tipoVenta, String TipoCliente) 
            throws ClassNotFoundException, SQLException{
        // este metodo solo se debe usar si esta factura record se comporta como
        // producto record.
        
        this.producto_record.DeterminarPventa(tipoVenta, TipoCliente, this.producto_record.getProducto());
        
        //se debe cambiar precio de venta del record y su respectivo subtotal.
        this.setSpt_precio_venta(this.producto_record.pventa);
    }
    
}
