package BD_tdpos_r;

/**
 *Clase para encapsular todos los atributos y queries para la manipulacion de 
 * la tabla de facturas
 * @author esteban
 */
public class SQL_Factura {
    
    
    
    // columnas de la tabla de facturas
    
    /**
     * columna de llave primaria, int
     */
    public static final String COL_CONSEC = "consecutivo";
    
    
    /**
     * columna de saldo de la factura, es int
     */
    public static final String COL_SALDO = "saldo";
    
    
    /**
     * columna de identificacion del cliente, String
     */
    public static final String COL_CLIENTE = "Cliente_id";
    
    
    /**
     * columna de fecha de generacion de la factura, LocalDate.
     */
    public static final String COL_FECHA = "fecha";
    
    
    /**
     * columna de HORA, LocalTime
     */
    public static final String COL_HORA = "hora";
    
    
    /**
     * columna de identificacion del vendedor, String
     */
    public static final String COL_VENDEDOR = "Vendedor_id";
    
    
    /**
     * columna de forma de pago, credito o contado, String
     */
    public static final String COL_FORMA_PAGO = "forma_pago";
    
    
    /**
     * columna de intervalo de pago, int
     */
    public static final String COL_INTERV_PAGO = "intervalo_pago";
    
    
    /**
     * Columna que aparece cuando se hace el query que calcula la
     * cartera. se hace uso de la palabra clave de sql as para renombrar
     * la columna como suma.
     */
    public static final String COL_SUMA = "suma";
    
    
    /**
     * comando de sql que suma todos los saldos de aquellas facturas cuyo
     * saldo es mayor que cero. es decir que calcula el total que todos los
     * clientes le deben al negocio.
     */
    public static final String SELECT_CARTERA = "select sum(saldo) as suma from factura where saldo > 0;";
    
    
    /**
     * query que suma el valor de todos los abonos hechos entre las fechas
     * ?:1 y ?:2, es decir, fecha inicia y fecha final.
     */
    public static final String SELECT_PAGOS_IN_DT = 
            "select sum(valor) as suma from abonos where abonos.fecha>=? and abonos.fecha<=?;";
    
    
    /**
     * query que calcula la sumatoria del producto del stock con el costo
     * para obtener el equivalente en dinero de la mercancia del negocio.
     */
    public static final String SELECT_STOCK2MONEY = 
            "select sum(stock * costo) as suma from producto where stock > 0;";
    
    
    /**
     * para seleccionar una factura de la tabla basado en la llave primaria
     */
    public static final String SELECT_ID = "SELECT `factura`.`consecutivo`,\n" +
            "    `factura`.`saldo`,\n" +
            "    `factura`.`Cliente_id`,\n" +
            "    `factura`.`fecha`,\n" +
            "    `factura`.`hora`,\n" +
            "    `factura`.`Vendedor_id`,\n" +
            "    `factura`.`forma_pago`,\n" +
            "    `factura`.`intervalo_pago`\n" +
            "FROM `negocio_r`.`factura` where `factura`.`consecutivo` = ?;";
    
    
    /**
     * para insertar una factura en la base de datos.
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`factura`\n" +
            "(`consecutivo`, `saldo`,\n" + // 1, 2
            "`Cliente_id`, `fecha`,\n" + // 3, x
            "`hora`, `Vendedor_id`,\n" + // x, 4
            "`forma_pago`, `intervalo_pago`)\n" + // 5, 6
            "VALUES\n" +
            "(?, ?,\n" +
            "?, curdate(),\n" +
            "curtime(), ?,\n" +
            "?, ?);";
    
    
    /**
     * para seleccionar todas las facturas asociadas a un cliente
     */
    public static final String SELECT_CLIENTE = "SELECT `factura`.`consecutivo`,\n" +
            "    `factura`.`saldo`,\n" +
            "    `factura`.`Cliente_id`,\n" +
            "    `factura`.`fecha`,\n" +
            "    `factura`.`hora`,\n" +
            "    `factura`.`Vendedor_id`,\n" +
            "    `factura`.`forma_pago`,\n" +
            "    `factura`.`intervalo_pago`\n" +
            "FROM `negocio_r`.`factura` where `factura`.`Cliente_id` = ?;";
    
    
    /**
     * Este query tiene la misma funcion que el query para traer todas las facturas
     * de la bd correspondientes a un cliente, con la diferencia que se agrega una condicion adicional
     * para filtrar las facturasc on saldo diferente de zero desde el motor de mysql en lugar de hacer lo java.
     * se espera que esto mejore notablemente el desempeño a largo plazo
     */
    public static final String SELECT_CLIENTE_SALDO_NO_ZERO = "SELECT `factura`.`consecutivo`,\n" +
            "    `factura`.`saldo`,\n" +
            "    `factura`.`Cliente_id`,\n" +
            "    `factura`.`fecha`,\n" +
            "    `factura`.`hora`,\n" +
            "    `factura`.`Vendedor_id`,\n" +
            "    `factura`.`forma_pago`,\n" +
            "    `factura`.`intervalo_pago`\n" +
            "FROM `negocio_r`.`factura` where `factura`.`Cliente_id` = ? and `factura`.`saldo`> 0;";
    
    
    /**
     * para seleccionar todas las facturas ACTIVAS asociadas a un cliente.
     */
    public static final String SELECT_ACTIVE_CLIENTE = "SELECT `factura`.`consecutivo`,\n" +
            "    `factura`.`saldo`,\n" +
            "    `factura`.`Cliente_id`,\n" +
            "    `factura`.`fecha`,\n" +
            "    `factura`.`hora`,\n" +
            "    `factura`.`Vendedor_id`,\n" +
            "    `factura`.`forma_pago`,\n" +
            "    `factura`.`intervalo_pago`\n" +
            "FROM `negocio_r`.`factura` where `factura`.`Cliente` = ?;";
    
    
    /**
     * para seleccionar el consecutivo de mayor valor, es decir
     * averiguar cual debe ser el connsecutivo a utilizar.
     */
    public static final String MAX_CONSEC = "select max(consecutivo) from factura;";
    
    
    /**
     * nombre de la columna de resultado para el query MAX_CONSEC.
     * este valor es usado por unn resultSet.
     */
    public static final String COL_MAX = "max(consecutivo)";
    
    
    /**
     * querie para actualizaar un record de la tabla de 
     */
    public static final String UPDATE = "";
    
    
    /**
     * query para modificar el saldo de una factura.
     */
    public static final String MOD_SALDO_FACTURA = 
            "UPDATE `negocio_r`.`factura` SET `saldo` = `saldo` + ? WHERE `consecutivo` = ?;";
    
    
    /**
     * QUERY de mysql para seleccionar todos los records de la tabla de facturas tales que el saldo 
     * sea mayor que un int especificado mediante un pst.setint...
     */
    public static final String SELECT_SALDO_MAYOR = "select * from factura where saldo > ?;";
    
    /**
     * QUERY de mysql para seleccionar todos los records de la tabla de facturas tales que el saldo 
     * sea menor que un int especificado mediante un pst.setint...
     */
    public static final String SELECT_SALDO_MENOR = "select * from factura where saldo > ?;";
    
}
