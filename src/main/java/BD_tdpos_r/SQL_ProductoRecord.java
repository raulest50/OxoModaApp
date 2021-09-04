package BD_tdpos_r;

/**
 *Queries y datos para el conector a la  tabla de Productos records
 * contiene queries para hacer CRUD operations y los nombres de las columnas
 * de la  tabla.
 * @author esteban
 */
public class SQL_ProductoRecord {
    
    
    
    public static final String TABLE_NAME = "productoRecord";
    
    /**
     * llave primaria de la tabla
     * (tipo entero)
     */
    public static final String COL_ID = "id";
    
    /**
     * llave foranea que relaciona varios records con un elemento de la tabla
     * de facturas. (tipo entero)
     */
    public static final String COL_FAC_CONSEC = "Factura_consecutivo";
    
    
    /**
     * cantidad del producto x
     * (tipo entero)
     */
    public static final String COL_CANTIDAD = "cantidad";
    
    /**
     * precio del costo del producto al momento de realizar la venta
     * (tipo entero)
     */
    public static final String COL_COSTO = "costo";
    
    
    /**
     * precio de venta del producto
     * (tipo entero)
     */
    public static final String COL_PVENTA = "pventa";
    
    
    /**
     * relaciona varios records con un elemento de la tabla de productos
     * (tipo String 100 caracteres)
     */
    public static final String COL_PRODUCTO_COD = "Producto_codigo";
    
    
    
    /**
     * SQL query para traer todos los records de productos de una factura desde 
     * la tabla de productos_records
     */
    public static final String SELECT_BY_FACTURA = "SELECT `productoRecord`.`id`,\n" +
            "    `productoRecord`.`Factura_consecutivo`,\n" +
            "    `productoRecord`.`cantidad`,\n" +
            "    `productoRecord`.`costo`,\n" +
            "    `productoRecord`.`pventa`,\n" +
            "    `productoRecord`.`Producto_codigo`\n" +
            "FROM `negocio_r`.`productoRecord` where `productoRecord`.`Factura_consecutivo` = ?;";
    
    
    
    /**
     * SQL query para insertar un producto en la tabla de Produdto records
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`productoRecord`\n" +
            "(`id`, `Factura_consecutivo`,\n" +
            "`cantidad`, `costo`,\n" +
            "`pventa`, `Producto_codigo`)\n" +
            "VALUES\n" +
            "(?, ?,\n" +
            "?, ?,\n" +
            "?, ?);";
    
    
    /**
     * 
     */
    public static final String DELETE_BY_FACTURA_ID = "";
    
    
}
