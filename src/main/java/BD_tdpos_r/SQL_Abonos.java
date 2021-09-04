package BD_tdpos_r;

/**
 *Clase que encapsula todos los queries relacionados directamente con la tabla
 * de Abonos.
 * Inicialmente se penso esta tabla solo para insertar records relacionados
 * con abonos a facturass de credito pero actualmente la veo mas como una tabla
 * de ingreso de efectivo. es decir que si se registra una venta de contado
 * se genera la factura e inmediatamente un abono record que representara su 
 * pago total.
 * @author esteban
 */
public class SQL_Abonos {
    
    /**
     * llave primaria autoincremental de la tabla.
     * tipo int.
     */
    public static final String COL_ID= "id";
    
    /**
     * valor en pesos del abono.
     * tipo int.
     */
    public static final String COL_VALOR= "valor";
    
    /**
     * fecha en la que se ingreso el abono.
     * tipo LocalDate.
     */
    public static final String COL_FECHA= "fecha";
    
    /**
     * hora, minutos y segundos del sistema en quee se registro el abono.
     * tipo LocalTime.
     */
    public static final String COL_HORA= "hora";
    
    /**
     * cc o nit, llave foranea, del cliente que realizo el abono.
     * tipo String.
     */
    public static final String COL_CLIENTE_ID= "Cliente_id";
    
    /**
     * llave foranea de la tabla de facturas. 
     * tipo int.
     */
    public static final String COL_FACTURA_CONSEC= "Factura_consecutivo";
    
    /**
     * llave foranea a la tabla de vendedores.
     * tipo String.
     */
    public static final String COL_VENDEDOR_ID= "Vendedor_id";
    
    /**
     * nombre de la comlumna en el result set del query de seleeccion del abono mas reciente del cliente
     * especificado.
     */
    public static final String COL_AS_MAX = "max";
    
    /**
     * query para seleccionar el ultimo abono del cliente especificado por pst.set
     */
    public static final String SELECT_LAST_ABONO_BY_CLIENT = "select max(fecha) as max from abonos where Cliente_id = ?;";
    
    /**
     * querie para insertar un abono en la BD.
     * notese que la fecha y la hora la inserta el motor de MySQL
     */
    public static final String INSERT_ABONO = "INSERT INTO `negocio_r`.`abonos`\n" +
            "(`id`, `valor`,\n" + // 1, 2
            "`fecha`, `hora`,\n" + // x, x
            "`Cliente_id`, `Factura_consecutivo`,\n" + // 3, 4
            "`Vendedor_id`)\n" + // 5
            "VALUES\n" +
            "(?, ?,\n" +
            "curdate(), curtime(),\n" +
            "?, ?,\n" +
            "?);";
    
    
    /**
     * 
     * querie para seleccionar todos lo abonos que tenga una factura.
     * 
     */
    public static final String SELECT_BY_FACTURA = "SELECT `abonos`.`id`,\n" +
            "    `abonos`.`valor`,\n" +
            "    `abonos`.`fecha`,\n" +
            "    `abonos`.`hora`,\n" +
            "    `abonos`.`Cliente_id`,\n" +
            "    `abonos`.`Factura_consecutivo`,\n" +
            "    `abonos`.`Vendedor_id`\n" +
            "FROM `negocio_r`.`abonos` where `abonos`.`Factura_consecutivo` = ?;";
    
    
    /**
     * querie para seleccionar todos los abonos de una fecha.
     * 
     * -*-*-    (falta ensyarlo)   -*-*-
     */
    public static final String SELECT_BY_DATE = "SELECT `abonos`.`id`,\n" +
            "    `abonos`.`valor`,\n" +
            "    `abonos`.`fecha`,\n" +
            "    `abonos`.`hora`,\n" +
            "    `abonos`.`Cliente_id`,\n" +
            "    `abonos`.`Factura_consecutivo`,\n" +
            "    `abonos`.`Vendedor_id`\n" +
            "FROM `negocio_r`.`abonos` where `abonos`.`fecha` = ?;";
    
    
    /**
     * querie para seleccionar todos los abonos de un cliente.
     */
    public static final String SELECT_BY_CLIENTE = "SELECT `abonos`.`id`,\n" +
            "    `abonos`.`valor`,\n" +
            "    `abonos`.`fecha`,\n" +
            "    `abonos`.`hora`,\n" +
            "    `abonos`.`Cliente_id`,\n" +
            "    `abonos`.`Factura_consecutivo`,\n" +
            "    `abonos`.`Vendedor_id`\n" +
            "FROM `negocio_r`.`abonos` where `abonos`.`Cliente_id` = ?;";
    
    
    
    /**
     * Query para seleccionar el ultimo abono de una factura dada.
     * se usa principalmente pra contruir un objeto tipo SptFactura.
     * 
     * En este query se selecciona el maximo de la columna fecha de la tabla abdf, que es el resutlado
     * de un select con todos los abonos cuya factura = ?.
     */
    public static final String SELECT_LAST_ABONO_FROM_FACTURA =
            "select max(abdf.fecha) as maxi from (\n" +
            "select abonos.fecha as fecha\n" +
            "from abonos \n" +
            "where abonos.Factura_consecutivo = ? \n" +
            ") as abdf;";
    
    
    
}
