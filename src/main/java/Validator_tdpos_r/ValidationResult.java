package Validator_tdpos_r;

/**
 * Metodo que encapsula un boolean que indica si una operacion es valida o no,
 * exitosa o no y ademas un string indicando el porque en caso del fracaso de 
 * la operacion.
 * @author esteban
 */
public class ValidationResult {
    
    /**
     * indica si la operacion realizda es o no exitosa.
     */
    public boolean result;
    
    
    /**
     * String que explica el motivo de la falla de la operacion realizada en 
     * caso de falla.
     * si la operacion es exitosa entonces puede ser un string vacio no nulo
     * o un mensaje de operacion exitosa.
     */
    public String motivo;
    
    
    /**
     * cuando se hace durante el proceso de validacion la contruccion de un objeto
     * que es requerido si la validacion retorna true. En este caso, ya que la contruccion
     * puede ser dispendiosa por necesitar de multiples metodos entonces se ofrece este atributo
     * para poder no solo empaquetar en la respuesta de la validacion el motivo y el resultado
     * sino tambien un objeto construido a partir de ella. Este es el caso de la validacion del ingreso
     * de un cupon en la tabla de ventas. ver Va_Cupon para mas informacion.
     */
    public Object obj_carry;
    
    

    public ValidationResult(boolean result, String motivo) {
        this.result = result;
        this.motivo = motivo;
    }
    
    
    /**
     * constructor vacio.
     */
    public ValidationResult(){
        this.motivo = "";
    }

    public boolean isResult() {
        return result;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
}
