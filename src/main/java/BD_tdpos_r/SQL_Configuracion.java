package BD_tdpos_r;

/**
 * Esta clase encapsula todos los Strings necesarios para leer y esccribir
 * informacion en la tabla de configuracion.
 * a diferencia de las otras tablas, configuracion esta diseñada para contener 
 * unos valores particulares y por tanto solo se soporta operacion select
 * y update por la capa de logica java de esta aplicacion.
 * 
 * ademas esta clase no solo contiene los queries y los nombres de las columnas
 * sino tambien el nombre id (idconf) de cada record.
 * @author esteban
 */
public class SQL_Configuracion {
    
    
    //********* VALORES DE LA CONFIGURACION.
    
    /**
     * valor de iva por defecto configurado en las pestaña de configuracion
     */
    public static final String PRT_IVA = "iva_defecto";
    
    
    /**
     * nombre de la impresora de stickers en el servicio de impresion del sistema 
     * operativo
     */
    public static final String PRT_IMPR_FACTURA = "impresora_facturas";
    
    
    /**
     * fila de la tabla de configuracion que indica si el sistema esta
     * o no bloqueado.
     */
    public static final String PRT_LOCKED = "LOCKED";
    
    
    /**
     * en la fila de prt locked se guarda este String para indicar que
     * se pueden hacer cambios.
     */
    public static final String NOT_LOCKED = "no";
            
    
    /**
     * en la fila de prt locked se guarda este String para indicar
     * que no se pueden hacer modificaciones del los productos.
     */
    public static final String LOCKED = "yes";
    
    
    /**
     * nombre de la impresora de stickers en el servicio de impresion del sistema 
     * operativo
     */
    public static final String PRT_IMPR_STICKER = "impresora_sticker";
    
    
    /**
     * fila para guardar configuracion de tipo de impresion.
     * 
     * por defecto, cuando se ejecuta el script de sql se comienza esta propiedad en 
     * impreision tipo sintesis.
     */
    public static final String PRT_PRINT_TYPE = "Tipo Impresion recibos";
    
    
    /**
     * constant para poner en el combo box de tipo de impresion.
     * se relaciona con el atributo PRT_PRINT_TYPE
     */
    public static final String PRINT_OPTION_SINTESIS = "Impresion tipo Sistesis";
    
    
    /**
     * constant para poner en el combo box de tipo de impresion.
     * se relaciona con el atributo PRT_PRINT_TYPE
     */
    public static final String PRINT_OPTION_HISTORIA = "Impricion tipo Historia";
    
    
    /**
     * row name in configuration table that stores the max characters that fits in a single line
     */
    public static final String PRT_FACT_MAX_LINES = "factu_char_line";
    
    
    /**
     * Atributo de configuracion donde se guarda el ultimo codigo usado, para mostrar en la inerfaz
     * o para realizar el calculo de el siguiente codigo.
     */
   public static final String  PRT_LAST_CODE_PROD = "last_producto_code";
   
   /***
    * Propiedad que representa el email de google que se usa para enviar
    */
   public static final String PRT_SENDER_MAIL = "sender_mail";
   
   /**
    * contraseña de la cuenta de google que se usa para enviar correo
    */
   public static final String PRT_SENDER_PASS = "sender_pass";
   
   /**
    * correo del destinatario
    */
   public static final String PRT_DESTINATION_MAIL = "destination_mail";
   
   /**
    * contraseña dinamica que se genera cada que se va hacer una operacion critica.
    * estrictamente no es necesario guardarla pero se hace con el fin
    * de poder recuperar el sistema en caso de extraviar la contraseña
    */
   public static final String PRT_DYN_PASS = "dyn_pass";
   
   
   
   
    //****** NOMBRE DE LA COLUMNAS
    
   
    /**
     * nombre de la columna de la tabla de configuracion donde se guarda el valor
     * de la configuracion
     */
    public static final String COL_VALOR = "valor";
    
    
    /**
     * nombre de la columna de configuracion donde se guarda el string identificador
     * de cada configuracion.
     */
    public static final String COL_IDCONF = "idconf";
    
    
    
    //********* QUERIES DE SELECCION Y MODIFICACION
    
    /**
     * Cuando se instala el software por primera vez se ejecuta un script de sql
     * que asegura cada record de configuracion ya existe, por lo que no es necesario
     * agregar un querie de insecion en esta clase.
     */
    
    
    /**
     * SQL-q para leer una configuracion particular de la tabla
     */
    public static final String SELECT = "SELECT `configuracion`.`valor`\n" +
            "FROM `negocio_r`.`configuracion` where idconf = ?;";
    
    
    /**
     * SQL-q para actualizar una configuracion particular de la tabla
     */
    public static final String UPDATE = "UPDATE `negocio_r`.`configuracion`\n" +
            "SET `valor` = ? WHERE `idconf` = ?;";
    
   
}
