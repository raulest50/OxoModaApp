package support_tdpos;

/**
 *
 * @author esteban
 * clase que encapsula strings comunmente usados en los mensajes 
 * de soporte tecnico.
 */
public class SuppMess {
    
    /**
     * java exception
     */
    public static final String EXCEPTION = "java Exception";
    
    
    /**
     * llamar a soporte tecnico.
     */
    public static final String CALL_SUPPORT = "Porfavor llame a soporte tecnico \n"
            + "www.tentactil.com - raulesteban@tentactil.com";
    
    
    /**
     * mensaje que indica al usuario que es lo que debe informar a soporte.
     */
    public static final String INFORM_THIS = "Porfvor indique el siguiente mensaje a soporte "
            + "para poder reconocer el error y la seccion del programa donde ocurrio: \n";
    
    
    /**
     *  Titulo generico cuando los datos ingredaso no son validos
     */
    public static final String DATA_PROBLEM_TITLE = "Datos no Validos";
    
    
    /**
     *  SubTitulo generico cuando los datos ingredaso no son validos
     */
    public static final String DATA_PROBLEM_SUBTITLE = "Revise por favor los datos introducidos. \n"
            + "los incovenientes se listan a continuacion.";
    
    
    public static final String DATA_FORMAT_PROBLEM_NONUM = "El valor ingresado no es numero valido";
    
    /**
     * Mensaje generico para mostrar cuando se ingresa texto en lugar de numeros.
     */
    public static final String DATA_INT_NO_VALID_MESS = "El valor ingresado no corresponde a"
            + " una cantidad numerica validad porfavor revise los datos.";
    
    /**
     * titulo para ventana modal de warning que se genera cuando se
     * intenta lanzar un popup de modificacion y no hay item seleccionado
     */
    public static final String NO_SEL_ITEMTAB_TITLE = "No hay Seleccion";
    
    /**
     * SUBtitulo para ventana modal de warning que se genera cuando se
     * intenta lanzar un popup de modificacion y no hay item seleccionado
     */
    public static final String NO_SEL_ITEMTAB_SUB = "No hay item seleccionado";
    
    /**
     * mensaje para ventana modal de warning que se genera cuando se
     * intenta lanzar un popup de modificacion y no hay item seleccionado
     */
    public static final String NO_SEL_ITEMTAB_MESS = "Para Realizar esta operacion debe seleccionar \n"
            + "un item de la tabla. (se mostrara resaltado en azul)";
    
}
