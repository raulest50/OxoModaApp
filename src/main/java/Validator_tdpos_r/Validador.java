package Validator_tdpos_r;


/**
 * La clase de validacion encapsula metodos genrles usados en trabajos 
 * de validacion. La idea es que las clases hijas de validacion herende de
 * esta clase para que hagan uso de metodos de validacion genericos que
 * a su vez permiten hacer validaciones mas complejas.
 * @author esteban
 */
public class Validador {
    
    
    /**
     * el uso de valores nulos siempre conlleva a implementaciones menos robustas,
     * mas complejas y menos flexibles.
     * nada mas implementando un unico valor nulo para la fecha experimente esto.
     * ademas programadores de mucho nivel aconsejan nunca usar valores nulos ya que 
     * generan mas problemas que soluciones. por este motivo decidi definir un valor de 
     * fecha vacia similar a String = "".
     */
    public static final String EMPTY_DATE_STR = "0001-01-01";
    
    
    
    /**
     * numero minimo de digitos que el software acepta para la cedula o el nit.
     */
    public static final int CC_MIN_DIGITS = 4;
    
    
    /**
     * regex para validar si un string solo contiene numeros y letras.
     * false si contiene espacio o cualquier caracter especial. tambien
     * es false si es vacio
     * 
     */
    public final String RGX_ALP_NUM = "[a-zA-Z0-9]+";
    
    /**
     * Regex para verificar que un String es un numero positivo.
     * 0 a inf
     */
    public final String RGX_POS_NUM = "[0-9]+";
    
    
    /**
     * exprecion regular para verifiacar si un String introducido es o no un 
     * porcentaje valido
     */
    public final String RGX_PORCENTAJE = "[0-9][0-9]%";
    
    
    /**
     * regex para hacer validacion de email.
     * no es perfecto pero done is better than perfect
     */
    public final String RGX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    /**
     * regex que permite identificar cuando un campo de texto lo dejan vacio
     * o solo con espacios
     */
   public final String RGX_VACIO = " *";  
   
   
   /**
    * titulo para ventana modal el cual indica que un string ingresado no es un
    * numero entero positivo valido.
    */
   public static final String NUM_NO_POS_TITLE = "Hay un inconveniente con el valor introducido";
   
   
   /**
    * mensaje para ventana modal el cual indica que un string ingresado no es un
    * numero entero positivo valido.
    */
   public static final String NUM_NO_POS_MESS = "Solo se permite ingresar numeros enteros positivos.";
    
   
   /**
     * titu para ventana modal cuando se desea pedir un numero entero al usuario
     */
    public static final String INPUT_QUANT_TITLE = "Ingrese un numero positivo porfavor";
    
    
    /**
     * mensaje para ventana modal cuando se desea pedir un entero positivo a un usuario
     */
    public static final String INPUT_QUANT_MESS = "Ingrese por favor la cantidad de prendas que desea registrar";
    
    
    /**
     * metodo que verifica que un string solo contiene letras y numeros.
     * no espacios ni caracteres especiales. Tambien genera false si es un 
     * String vacio ("").
     * @param s
     * @return 
     */
    public boolean Only_alp_num(String s){
        return s.matches(this.RGX_ALP_NUM);
    }
   
    
    /**
     * metodo que verifica que un string es un numero positivo y no esta vacio
     * @param s
     * @return 
     */
    public boolean isPositiveNum(String s){
        return s.matches(this.RGX_POS_NUM);
    }
    
    /**
     * metodo que verifica que un string es un numero positivo y es mayor a 0.
     * @param s
     * @return 
     */
    public boolean isPositiveNumGreZero(String s){
        boolean r = isPositiveNum(s);
        if(r){
            try{
                int nume = Integer.parseInt(s);
                if(nume <= 0) r = false;
            } catch(NumberFormatException ex){
                r = false;
            }
        }
        return r;
    }
    
    
    /**
     * metodo que solo valida si un String es un porcentaje valido.
     * se verifica que el porcentaje introducido este entre 0% y 99%
     * ya que uso esta pensado para acciones de descuento y es improbable
     * la generacion de descuentos de mas del 99%.
     * ------------------------
     * true si es valido.
     * false en caso contrario.
     * ------------------------
     * @param prc
     * @return 
     */
    public boolean EsPorcentajeValido(String prc){
        return prc.matches(this.RGX_PORCENTAJE);
    }
    
    /**
     * metodo que valida si un string es un numero telefonico valido
     * @param s
     * @return 
     */
    public boolean isTel(String s){
        String sm = s.replace(" ", "");
        if(sm.length()<6) return false;
        else return sm.matches(RGX_POS_NUM);
    }
    
    
    /**
     * metodo que hace validacion de un String e-mail.
     * no es perfecto pero funciona para la mayoria de los casos.
     * 
     * revisar RFC-822 los casos como "abc..@//"@gmail.com no son cubiertos.
     * el uso de las comillas posibilita el uso de casi cualquier caracter
     * de acuerdo al estandar. sin embargo muchas paginas no soportan 
     * el estandar completo e imponen restricciones que se salen del RFC822
     * @param s
     * @return 
     */
    public boolean regexCheckEmail(String s){
        return s.matches(this.RGX_EMAIL);
    }
    
    
    /**
     * metodo que entrega true cuando el String s esta vacio 
     * o solo contiene espacios vacios (backslash).
     * @param s
     * @return 
     */
    public boolean EstaVacio(String s){
        return s.matches(this.RGX_VACIO);
    }
    
}
