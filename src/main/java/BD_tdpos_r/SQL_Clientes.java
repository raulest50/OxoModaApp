package BD_tdpos_r;

/**
 * Encapsulamiento de los strings para hacer queries
 * a la tabla de datos de clientes
 * @author esteban
 */
public class SQL_Clientes {
    
    /**
     * columna de llave primaria
     */
    public static final String COL_ID = "id";
    
    /**
     * columna de nombre completo y con apellidos
     */
    public static final String COL_NOMBRE = "nombre";
    
    /**
     * columna telefono1
     */
    public static final String COL_TEL1 = "tel1";
    public static final String COL_TEL2 = "tel2";
    public static final String COL_TEL3 = "tel3";
    
    public static final String COL_DIRECCION = "direccion";
    public static final String COL_EMAIL = "email";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_CUMPLE = "cumple";
    public static final String COL_TIPO = "tipo";
    public static final String COL_UBICACION = "Ubicacion";
    
    /**
     * cuando se usan los query de count se usa la palabra clave de 
     * sql para renombrar la columna y hacer mas legible en los rs.get
     */
    public static final String COL_AS_COUNT = "cuenta";
    
    
    
    /**
     * SQL query para buscar un cliente por su cc o nit.
     * recordadr que se trta e una llave primaria.
     */
    public static final String SELECT_ID = "SELECT " +
            "    `cliente`.`id`,\n" +
            "    `cliente`.`nombre`,\n" +
            "    `cliente`.`tel1`,\n" +
            "    `cliente`.`tel2`,\n" +
            "    `cliente`.`tel3`,\n" +
            "    `cliente`.`direccion`,\n" +
            "    `cliente`.`email`,\n" +
            "    `cliente`.`descripcion`,\n" +
            "    `cliente`.`cumple`,\n" +
            "    `cliente`.`tipo`,\n" +
            "    `cliente`.`Ubicacion`\n" +
            "FROM `negocio_r`.`cliente` where id = ?";
    
    
    
    /**
     * SQL query para insertar un cliente en la base de datos
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`cliente`\n" +
            "(`id`,\n" +
            "`nombre`,\n" +
            "`tel1`,\n" +
            "`tel2`,\n" +
            "`tel3`,\n" +
            "`direccion`,\n" +
            "`email`,\n" +
            "`descripcion`,\n" +
            "`cumple`,\n" +
            "`tipo`,\n" +
            "`Ubicacion`)\n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    
    
    /**
     * SQL query para eliminar un cliente de la base de datos
     */
    public static final String DELETE = "DELETE FROM `negocio_r`.`cliente`\n" +
            "WHERE id = ?;";
    
    
    
    /**
     * SQL query para actualizar un cliente de l base de datos.
     */
    public static final String UPDATE = "UPDATE `negocio_r`.`cliente`\n" +
            "SET\n" +
            "`id` = ?,\n" +
            "`nombre` = ?,\n" +
            "`tel1` = ?,\n" +
            "`tel2` = ?,\n" +
            "`tel3` = ?,\n" +
            "`direccion` = ?,\n" +
            "`email` = ?,\n" +
            "`descripcion` = ?,\n" +
            "`cumple` = ?,\n" +
            "`tipo` = ?,\n" +
            "`Ubicacion` = ?\n" +
            "WHERE `id` = ?;";
    
    
    
    public static final String BASE_SEARCH_NOMBRE = "SELECT " +
            "    `cliente`.`id`,\n" +
            "    `cliente`.`nombre`,\n" +
            "    `cliente`.`tel1`,\n" +
            "    `cliente`.`tel2`,\n" +
            "    `cliente`.`tel3`,\n" +
            "    `cliente`.`direccion`,\n" +
            "    `cliente`.`email`,\n" +
            "    `cliente`.`descripcion`,\n" +
            "    `cliente`.`cumple`,\n" +
            "    `cliente`.`tipo`,\n" +
            "    `cliente`.`Ubicacion`\n" +
            "FROM `negocio_r`.`cliente` where ";
    
    
    /**
     * Select para hacer un count de los clientes tales que tipo = ?.
     * se debe hacer un set en que se establezca que tipo se desea contar, tipo mayorista
     * o tipo credito o tipo contado.
     */
    public static final String SELECT_COUNT_TIPO = "select count(*) "+ COL_AS_COUNT +" from cliente where cliente.tipo = ?;";
    
    
    /**
     * select para contar todos los records de la tabla de clientes.
     */
    public static final String SELECT_COUNT_ALL = "select count(*) "+ COL_AS_COUNT +" from cliente;";
    
    
    /**
     * Select para contar los clientes de la tabla tales que concuerden con la descripcion.
     * recordar que para evitar cambiar la estructura de la base de datos se dejo la columna de 
     * descripcion para establecer vantidad de clientes a los que se manda a cobrar o vienen a pagar.
     * se debe hacer un set para determinar cual de los dos tipos de cobros se van a contar.
     */
    public static final String SELECT_COUNT_DESCRI = "select count(*) as "+ COL_AS_COUNT +" from cliente where cliente.descripcion = ?;";
    
    
    
}
