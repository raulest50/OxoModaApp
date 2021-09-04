package BD_tdpos_r;

/**
 *Clase que encapsula nombres de columnas y queries para hacer operaciones con
 * la tabla de devoluciones.
 * @author esteban
 */
public class SQL_Devoluciones {
    
    /**
     * llave primaria de la tabla de devoluciones, autoincremento.
     * tipo int.
     */
    public static final String COL_ID = "consecutivo";
    
    /**
     * llave foranea a la tabla de facturas.
     * tipo int.
     */
    public static final String COL_FACTU = "Factura_consecutivo";
    
    /**
     * precio de venta registrado en la factura para el producto que se desea 
     * devolver.
     * tipo int.
     */
    public static final String COL_PVENTA = "pventa";
    
    /**
     * cantidad de prendas que se devuelven, en la referencia especificada por
     * la columna Producto_codigo.
     * tipo int.
     */
    public static final String COL_CANTIDAD = "cantidad";
    
    /**
     * fecha en la que se realiza el registro de la devolucion.
     * tipo LocalDate
     */
    public static final String COL_FECHA = "fecha";
    
    /**
     * hora, minutos y segundos en el que se realizo el registro de la
     * devolucion.
     * tipo LocalTime.
     */
    public static final String COL_HORA = "hora";
    
    /**
     * llave foranea a la table dde vendedores.
     * tipo String.
     */
    public static final String COL_VENDEDOR = "Vendedor_id";
    
    /**
     * llave foranea a la tabla de productos.
     * tipo String
     */
    public static final String COL_PRODUCTO_COD = "Producto_codigo";
            
    
    /**
     * querie para seleccionar todas las devoluciones correspondientes a
     * a una factura determinadamente.
     */
    public static final String SELECT_BY_FACTURA = "SELECT " + 
            "    `devoluciones`.`consecutivo`,\n" +
            "    `devoluciones`.`Factura_consecutivo`,\n" +
            "    `devoluciones`.`pventa`,\n" +
            "    `devoluciones`.`cantidad`,\n" +
            "    `devoluciones`.`fecha`,\n" +
            "    `devoluciones`.`hora`,\n" +
            "    `devoluciones`.`Vendedor_id`,\n" +
            "    `devoluciones`.`Producto_codigo`\n" +
            "FROM `negocio_r`.`devoluciones` WHERE `devoluciones`.`Factura_consecutivo` = ? ;";
    
    
    /**
     * sql query para insertar una devolucion.
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`devoluciones`\n" +
            "(`consecutivo`, `Factura_consecutivo`,\n" + // 1, 2
            "`pventa`, `cantidad`,\n" + // 3, 4
            "`fecha`, `hora`,\n" + // x, x
            "`Vendedor_id`, `Producto_codigo`)\n" + // 7, 8
            "VALUES\n" +
            "(?, ?,\n" +
            "?, ?,\n" +
            "curdate(), curtime(),\n" +
            "?, ?);";
    
    
}
