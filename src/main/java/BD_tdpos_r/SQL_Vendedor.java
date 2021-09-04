package BD_tdpos_r;

/**
 * clase cuyo unico proposito en encapsular todos los SQL
 * queries relacionado con la tabla de vendedores.
 * todo en variables String. Tabmien se guardan los nombres de las 
 * columnas
 * @author esteban
 */
public class SQL_Vendedor {
    
    /**
     * llave primaria de la tabla
     */
    public static final String COL_ID = "id";
    
    /**
     * String arbitrario
     */
    public static final String COL_NOMBRE = "nombre";
    
    
    public static final String COL_TEL1 = "tel1";
    
    public static final String COL_TEL2 = "tel2";
    
    /**
     * string arbitrario
     */
    public static final String COL_DIR = "direccion";
    
    public static final String COL_EMAIL = "email";
    
    /**
     * string arbitrario
     */
    public static final String COL_DESCRI = "descripcion";
    
    
    /**
     * columna tipo sql.Date -  fecha de nacimiento
     */
    public static final String COL_NACIMIENTO = "nacimiento";
    
    
    /**
     * columna tipo sql.Date - fecha de registro, se debe generar con curdate() de MySQL
     */
    public static final String COL_FREGIS = "fregistro";
    
    /**
     * columna tipo INT.
     */
    public static final String COL_ESTADO = "estado";
    
    
    
    /*****************
     * queries de SQL
     */
    
    /**
     * para insertar un vendedor
     */
    public static final String INSERT = "INSERT INTO `negocio_r`.`vendedor`\n" +
            "(`id`, `nombre`,\n" + // 1, 2
            "`tel1`, `tel2`,\n" + // 3, 4
            "`direccion`, `email`,\n" + // 5, 6
            "`descripcion`, `nacimiento`,\n" + // 7, 8
            "`fregistro`, `estado`)\n" + // x, 9
            "VALUES\n" +
            "(?, ?,\n" +
            "?, ?,\n" +
            "?, ?,\n" +
            "?, ?,\n" +
            "curdate(), ?);";
    
    
    /**
     * para traer vendedores de la tabla de de datos usando la llave primaria
     */
    public static final String SELECT_ID = "SELECT `vendedor`.`id`,\n" +
            "    `vendedor`.`nombre`,\n" +
            "    `vendedor`.`tel1`,\n" +
            "    `vendedor`.`tel2`,\n" +
            "    `vendedor`.`direccion`,\n" +
            "    `vendedor`.`email`,\n" +
            "    `vendedor`.`descripcion`,\n" +
            "    `vendedor`.`nacimiento`,\n" +
            "    `vendedor`.`fregistro`,\n" +
            "    `vendedor`.`estado`\n" +
            "FROM `negocio_r`.`vendedor` where id = ?;";
    
    
    /**
     * para traer todos los vendedores "AACTIVOS" de la tabla de vendedores
     */
    public static final String SELECT_ACTIVOS = "SELECT `vendedor`.`id`,\n" +
            "    `vendedor`.`nombre`,\n" +
            "    `vendedor`.`tel1`,\n" +
            "    `vendedor`.`tel2`,\n" +
            "    `vendedor`.`direccion`,\n" +
            "    `vendedor`.`email`,\n" +
            "    `vendedor`.`descripcion`,\n" +
            "    `vendedor`.`nacimiento`,\n" +
            "    `vendedor`.`fregistro`,\n" +
            "    `vendedor`.`estado`\n" +
            "FROM `negocio_r`.`vendedor` where estado = 1;";
    
    
    /**
     * Querie base para agregar comandos como LIKE de SQL. el metodo que lo
     * use debe completar el querie.
     */
    public static final String BASE_SELECT = "SELECT `vendedor`.`id`,\n" +
            "    `vendedor`.`nombre`,\n" +
            "    `vendedor`.`tel1`,\n" +
            "    `vendedor`.`tel2`,\n" +
            "    `vendedor`.`direccion`,\n" +
            "    `vendedor`.`email`,\n" +
            "    `vendedor`.`descripcion`,\n" +
            "    `vendedor`.`nacimiento`,\n" +
            "    `vendedor`.`fregistro`,\n" +
            "    `vendedor`.`estado`\n" +
            "FROM `negocio_r`.`vendedor` where ";
    
    
    /***
     * para borrar vendedores de la BD
     */
    public static final String DELETE = "DELETE FROM `negocio_r`.`vendedor`\n" +
            "WHERE id=?;";
    
    
    /**
     * para actualizar vendedores en la BD
     */
    public static final String UPDATE = "UPDATE `negocio_r`.`vendedor`\n" +
            "SET\n" +
            "`id` = ?, `nombre` = ?,\n" + // 1, 2
            "`tel1` = ?, `tel2` = ?,\n" + // 3, 4
            "`direccion` = ?, `email` = ?,\n" + // 5, 6 
            "`descripcion` = ?, `nacimiento` = ?,\n" + // 7, 8
            "`fregistro` = curdate(), `estado` = ?\n" + // x, 9 
            "WHERE `id` = ?;"; // 10 
    
    
}
