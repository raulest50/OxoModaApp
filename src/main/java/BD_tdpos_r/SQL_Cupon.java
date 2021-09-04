package BD_tdpos_r;

/**
 *Clase que encapsula los nombre de las columnas y queries necesarios para
 * hacer operaciones de conexion local con la tabla de Cupones
 * @author esteban
 */
public class SQL_Cupon {
    
    
    
    /**
     * nombre de la tabla de cupones en la base de datos
     */
    public static final String TABLE_NAME = "Cupones";
    
    
    /**
     * llave primaria de la tabla, int
     */
    public static final String COL_ID = "id";
    
    /**
     * cliente al cual pertenece el cupon, String, llave foranea
     */
    public static final String COL_CLIENTE = "Cliente_id";
    
    
    /**
     * valor en pesos del cupon, int
     */
    public static final String COL_VALOR = "valor";
    
    
    /**
     * fecha en la que se emitio el cupon
     */
    public static final String COL_FECHA = "fecha";
    
    
    /**
     * estado del cupon, int 0 gastado, int 1 aun activo.
     */
    public static final String COL_ESTADO = "estado";
    
    
    /**
     * clave aleatoria de 4 digitos que se genera para el cupon
     */
    public static final String COL_PASS = "pass";
    
    
    /**
     * representa el consecutivo de la factura en la cual se geenra el cupon
     */
    public static final String COL_FACTURA_GEN = "Factura_consecutivo_gen";
    
    /**
     * representa el consecutivo de la factura en la cual se gasta el cupon.
     * cuando el cupon aun no se ha gastado, esta columna se le asigna 0,
     * nuero reservado para factura nula.
     */
    public static final String COL_FACTURA_SPEND = "Factura_consecutivo_spend";
    
    
    /**
     * Columna que representa el vendedor que genero el cupon
     */
    public static final String COL_VENDEDOR = "Vendedor_id";
    
    
    /**
     * nombre de la columna cuando se hace un querie para obtener el maximo
     * id de todos los records de la tabla.
     */
    public static final String COL_MAX_ID = "colmax";
    
    
    
    /**
     * para insertar cupon o bono por devolucion en factura ya cancelada
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`cupones`\n" +
            "(`id`, `Cliente_id`,\n" + // 1, 2
            "`valor`, `fecha`,\n" + // 3, x
            "`estado`, `pass`,\n" + // 4, 5
            "`Factura_consecutivo_gen`, `Vendedor_id` , " + // 6, 7
            "`Factura_consecutivo_spend`)\n" + // 8
            " VALUES\n" +
            "(?, ?,\n" +
            "?, curdate(),\n" + // con curdate() el motor de MySQL genera la fecha automaticamente.
            "?, ?,\n" +
            "?, ?," +
            "? );";
    
    
    /**
     * Trae el cupon identificado por ID, la llave primaria.
     */
    public static final String SELECT_ID = 
            "SELECT `cupones`.`id`,\n" +
            "    `cupones`.`Cliente_id`,\n" +
            "    `cupones`.`valor`,\n" +
            "    `cupones`.`fecha`,\n" +
            "    `cupones`.`estado`,\n" +
            "    `cupones`.`pass`,\n" +
            "    `cupones`.`Factura_consecutivo_gen`,\n" +
            "    `cupones`.`Vendedor_id`,\n" +
            "    `cupones`.`Factura_consecutivo_spend` \n" +
            "FROM `negocio_r`.`cupones` where id = ?;";
    
    
    
    /**
     * Trae todos los cupones que se hayan gastado en una factura dada.
     */
    public static final String SELECT_BY_FACTURA_SPEND = 
            "SELECT `cupones`.`id`,\n" +
            "    `cupones`.`Cliente_id`,\n" +
            "    `cupones`.`valor`,\n" +
            "    `cupones`.`fecha`,\n" +
            "    `cupones`.`estado`,\n" +
            "    `cupones`.`pass`,\n" +
            "    `cupones`.`Factura_consecutivo_gen`,\n" +
            "    `cupones`.`Vendedor_id`, \n" +
            "    `cupones`.`Factura_consecutivo_spend` " +
            "FROM `negocio_r`.`cupones` where `cupones`.`Factura_consecutivo_spend` = ?;";
    
    
    /**
     * Trae todos los cupones que se hayan generado en una factura dada.
     */
    public static final String SELECT_BY_FACTURA_GEN = 
            "SELECT `cupones`.`id`,\n" +
            "    `cupones`.`Cliente_id`,\n" +
            "    `cupones`.`valor`,\n" +
            "    `cupones`.`fecha`,\n" +
            "    `cupones`.`estado`,\n" +
            "    `cupones`.`pass`,\n" +
            "    `cupones`.`Factura_consecutivo_gen`,\n" +
            "    `cupones`.`Vendedor_id`, \n" +
            "    `cupones`.`Factura_consecutivo_spend` " +
            "FROM `negocio_r`.`cupones` where `cupones`.`Factura_consecutivo_gen` = ?;";
    
    
    
    /**
     * querie para seleccionar un record tipo abono deacuerdo a el cliente
     * y a una clave aleatoria de 4 digitos. tambien de acuerdo a la llave 
     * primaria id, ya que existe la remota posiblidad de que un cliente
     * tenga 2 cuppones con la misma clave aleatoria, mientras que usando
     *
     */
    public static final String SELECT_ID_CLIENTE_PASS = 
            "SELECT `cupones`.`id`,\n" +
            "    `cupones`.`Cliente_id`,\n" +
            "    `cupones`.`valor`,\n" +
            "    `cupones`.`fecha`,\n" +
            "    `cupones`.`estado`,\n" +
            "    `cupones`.`pass`,\n" +
            "    `cupones`.`Factura_consecutivo_gen`,\n" +
            "    `cupones`.`Vendedor_id`, \n" +
            "    `cupones`.`Factura_consecutivo_spend` " +
            "FROM `negocio_r`.`cupones` where id = ? and pass = ? and Cliente_id = ?";
    
    
    /**
     * modificar valores de un record de cupones
     */
    public static final String UPDATE = "UPDATE `negocio_r`.`cupones`\n" +
            "SET\n" +
            "`id` = ?,\n" +
            "`Cliente_id` = ?,\n" +
            "`valor` = ?,\n" +
            "`fecha` = ?,\n" +
            "`estado` = ?,\n" +
            "`pass` = ?,\n" +
            "`Factura_consecutivo_gen` = ?,\n" +
            "`Vendedor_id` = ?,\n" +
            "`Factura_consecutivo_spend` = ?\n" +
            "WHERE `id` = ?;";
    
    
    /**
     * borrar un cupon.
     */
    public static final String DELETE = "";
    
    
    /**
     * querie para seleccionar el maximo id. se usa principalmente para hallar
     * cual debe ser el siguiente numero de cupon.
     */
    public static final String SELECT_MAX_ID = "select max(id) as colmax from cupones";
    
    
    /**
     * query para inhabilitar un cupon. se usa cuanto este se redime en la
     * pestaña de ventas
     */
    public static final String DISABLE_BY_ID =
            "UPDATE `negocio_r`.`cupones` \n" +
            "SET cupones.estado = ?, cupones.factura_consecutivo_spend = ? \n" +
            "WHERE `id` = ?;";
    
    
    /**
     * Querie para seleccionar los cupones que le pertenezcan a un cliente dado y ademas esten activos.
     */
    public static final String SELECT_BY_ACTIVE_CLIENTS = "SELECT `cupones`.`id`,\n" +
            "    `cupones`.`Cliente_id`,\n" +
            "    `cupones`.`valor`,\n" +
            "    `cupones`.`fecha`,\n" +
            "    `cupones`.`estado`,\n" +
            "    `cupones`.`pass`,\n" +
            "    `cupones`.`Factura_consecutivo_gen`,\n" +
            "    `cupones`.`Vendedor_id`,\n" +
            "    `cupones`.`factura_consecutivo_spend`\n" +
            "FROM `negocio_r`.`cupones` \n" +
            "where `cupones`.`Cliente_id` = ? \n" +
            "and `cupones`.`estado` = 1;";
    
    
    
    
}
