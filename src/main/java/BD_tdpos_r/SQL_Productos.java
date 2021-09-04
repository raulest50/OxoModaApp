package BD_tdpos_r;

/**
 *
 * Encapsulamiento de los strings para hacer queries
 * a la tabla de datos de productos
 * 
 * @author esteban
 * 
 */
public class SQL_Productos {

    /**
     * insertar un producto
     */
    public final static  String INSERT = "INSERT INTO `negocio_r`.`producto`\n" +
            "(`codigo`, `descripcion`, " // 1, 2
            + "`costo`, `pventa_contado`, " // 3, 4
            + "`pventa_credito`, \n" + // 5
            "`pventa_mayor`, `fingreso`, " // 6, x
            + "`stock`, `iva`)\n" + // 7, 8
            "VALUES (?, ?, ?, ?, ?, ?, curdate(), ?, ?);";
    
    
    /**
     * query para eliminar un producto usando la llave primaria id
     */
    public static final String DELETE = "DELETE FROM `negocio_r`.`producto` WHERE codigo = ?;";
    
    
    /**
     * query de update para un producto usando la llave primaria id.
     */
    public static final String UPDATE = "UPDATE `negocio_r`.`producto`\n" +
            "SET\n" +
            "`codigo` = ?, `descripcion` = ?, \n" + // 1, 2
            "`costo` = ?, `pventa_contado` = ?,\n" + // 3, 4
            "`pventa_credito` = ?, `pventa_mayor` = ?,\n" + // 5, 6
            "`fingreso` = curdate(), `stock` = ?,\n" + // x, 7
            "`iva` = ? WHERE `codigo` = ?;"; // 8, 9.
    
    
    /**
     * seleccionar un producto basado en el codigo
     */
    public final static String SELECT_CODIGO = "SELECT " +
            "    `producto`.`codigo`,\n" +
            "    `producto`.`descripcion`,\n" +
            "    `producto`.`costo`,\n" +
            "    `producto`.`pventa_contado`,\n" +
            "    `producto`.`pventa_credito`,\n" +
            "    `producto`.`pventa_mayor`,\n" +
            "    `producto`.`fingreso`,\n" +
            "    `producto`.`stock`,\n" +
            "    `producto`.`iva`\n" +
            "FROM `negocio_r`.`producto` where `producto`.`codigo` = ?;";
    
    
    
    /**
     * query que se usa de base para armar una busqueda por palabras clave
     * concatenando String de la forma where atributte like %% or like %% ...
     */
    public static final String  BASE_SEARCH_DESCRI = "SELECT " +
            "    `producto`.`codigo`,\n" +
            "    `producto`.`descripcion`,\n" +
            "    `producto`.`costo`,\n" +
            "    `producto`.`pventa_contado`,\n" +
            "    `producto`.`pventa_credito`,\n" +
            "    `producto`.`pventa_mayor`,\n" +
            "    `producto`.`fingreso`,\n" +
            "    `producto`.`stock`,\n" +
            "    `producto`.`iva`\n" +
            "FROM `negocio_r`.`producto` where ";
    
    
    /**
     * query para modificar el stock de un producto 
     * de la manera: stock = stock + n. si se desea restar a stock entonces
     * n se usa neegativo.
     */
    public static final String SUMAR_A_STOCK = 
            "UPDATE `negocio_r`.`producto` SET `stock` = `stock` + ? WHERE `codigo` = ?;";
    
    
    /**
     * query para contar el numero de productos en la tabla de productos.
     * nota: no hay que hacer ningun pst.set  .
     */
    public static final String SELECT_COUNT_NUM_PRODUCTS = "select count(*) as "+ SQL_Productos.COL_ASQRY_COUNT +" from producto;";
    
    
    /**
     * Query para hacer un conteo del numero de productos codificados en los ultimos 7 dias.
     * retorna 2 columnas, fecha de ingreso, que se tratara de 7 dates consecutivos incluyendo el dia de hoy.
     * y otra columna con la cuenta de records introducidos en cada fecha.
     * 
     * el query siempre retornara 7 records. No se debe hacer ningun set
     */
    public static final String SELECT_COUNT_WITHIN7 = "select fingreso, count(*) as cuenta\n" +
            "from producto \n" +
            "where fingreso >= curdate() - interval 7 day\n" +
            "group by fingreso;";
    

    
    /**
     * nombre de la columna de codigo de producto (primary key)
     */
    public static final String COL_CODIGO = "codigo";
    
    /**
     * nombre de la columna de descripcion (string arbitrario)
     */
    public static final String COL_DESCRIPCION = "descripcion";
    
    /**
     * nombre de la columna de costo (int)
     */
    public static final String COL_COSTO = "costo";
    
    /**
     * nombre de la columna de precio de venta contado (int)
     */
    public static final String COL_PVCONTADO = "pventa_contado";
    
    
    /**
     * nombre de la columna de precio de venta credito (int)
     */
    public static final String COL_PVCREDITO = "pventa_credito";
    
    
    /**
     * nombre de la columna de precio de venta pormayor (int)
     */
    public static final String COL_PVMAYOR = "pventa_mayor";
    
    
    /**
     * nombre de la columna de fecha  de ingreso (string - text)
     */
    public static final String COL_FINGRESO = "fingreso";
    
    
    /**
     * nombre de la columna de stock (int)
     */
    public static final String COL_STOCK = "stock";
    
    
    /**
     * nombre de la columna de iva (int)
     */
    public static final String COL_IVA = "iva";
    
    /**
     * ASQRY = AS QUERY.
     * nombre de la columna en la que se retorna el esultado del query de cuenta de productos
     * 
     * se hace un cast con la sql key word "as"
     */
    public static final String COL_ASQRY_COUNT = "cuenta";
    
    
    
    // a continuacion los strings que representan los tipos de busqueda
    
    /**
     * string que representa un tipo de busqueda de producto de acuerdo al
     * nombre
     */
    public static final String TIPOB_NOMBRE = "busqueda_por_nombre";
    
    
    /**
     * string que representa un tipo de busqueda por codigo exacto en la tabla de 
     * productos
     */
    public static final String TIPOB_CODIGO = "busqueda_por_nombre";
    
}
