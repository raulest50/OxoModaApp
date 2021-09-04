package Validator_tdpos_r;

import BD_tdpos_r.Hndl_Cupon;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.Cupon;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;

/**
 *
 * @author Esteban
 * 
 * para hacer validacion de el ingreso de un cupon en la tabla de ventas.
 * 
 */
public class Va_Cupon {
    
    
    /**
     * Explicacion cuando el cupon esta repetido
     */
    public final String CUPON_REPE = "Este cupon ya se ingreso en la tabla de ventas.";
    
    /**
     * 
     */
    public final String NO_EXISTE = "No existe un cupon con los datos ingresados";
    
    /**
     * 
     */
    public final String NO_PASS = "El password del cupon esta errado";
    
    /**
     * 
     */
    public final String CUPON_GASTADO = "El cupon especificado ya fue gastado en otra compra.";
    
    /**
     * mensaje de error si el identificador de cupon no tiene la estructura:
     * [numero]-[numero.]
     */
    public final String BAD_STRUCTURE = "Hay un error con ls caracteres ingresados.\n"
            + " Por favor inetente de nuevo";
    
    /**
     * mensaje si se intenta registrar un cupon sin tener un cliente seleccionado.
     */
    public final String NO_CLI_SELECTED = "No hay cliente seleccionado";
    
    /**
     * True si es valido ingresar un cupon en la tabla de ventas. false en caso contrario
     * y en motivo se explica el motivo.
     * @param user_input
     * @return 
     */
    public ValidationResult EsValidoIngresarCupon(String user_input, ObservableList<SptFacturaRecord> lfr, Cliente cliente)
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult();
        
        boolean r = true; // validez de la operacion
        
        String motivo = ""; // motivo de error si es el caso
        
        Cupon the_cupon = null;// instacion del cupon seleccioado
        String[] cup_str = user_input.split("-");
        
        // se revisa la estructura del String de entrada.
        if( !(cup_str.length == 2) ){ // error de mala estructura
            r=false;
            motivo = this.BAD_STRUCTURE;
        }
        
        int cupon_id=0;// se asignan numeros reservados para 
        int pass=0; // si se entra al siguiente if y no se asignan valores reales a estas
        // 2 variables entonces estos valores no se usan ya que se requiere nuevamente en el
        // siguiente if el valor de r=true.
                
        try{ // se revisa que no hayan letras.
            if(r){
                cupon_id = Integer.parseInt(cup_str[0]); // se obtiene el id del cupon del String del usuario
                pass = Integer.parseInt(cup_str[1]); // se obtiene el numero aleatorio del cupon del String del usuario
            }
        } catch(NumberFormatException ex){
            r = false;
            motivo = this.BAD_STRUCTURE;
        }
        
        if(r){ // se revisa la existencia del cupon.
            Hndl_Cupon hndcup = new Hndl_Cupon();
            ArrayList<Cupon> lscup = hndcup.getCupnBy_Id(cupon_id);
            if(lscup.isEmpty()){
                r=false;
                motivo = this.NO_EXISTE;
            }
            else the_cupon = lscup.get(0);
        }
   
        // hasta este punto si r=true entonces, el cupon existe.
        
        if(r){ // se revisa que coincidadn
            if( !(pass==the_cupon.pass) ){
                r=false;
                motivo = this.NO_PASS;
            }
        }
        
        if(r){ // se revisa que el cupon no haya sido gastado en una compra anterior
            if(the_cupon.estado==0){
                r=false;
                motivo=this.CUPON_GASTADO;
            }
        }
        
        
        if(r){ // se revisa que el cupon solo se ingrese una unica vez al table view de ventas.
            if(this.ContieneCupon(lfr, cupon_id)){
                r=false;
                motivo = this.CUPON_REPE;
            }
        }
        
        if(r){ // se verifica que haya cliente seleccioado.
            if(cliente.id.equals("")){
                r=false;
                motivo = this.NO_CLI_SELECTED;
            }
        }
        
        // se asignan los valores correspondientes al Validations Result.
        vr.setResult(r);
        if(!r) vr.motivo=motivo;
        
        /**
         * la construccion del cuopon involucra el uso del conetor correspondiente a la BD y ademas
         * el uso de un split. por esta coplejidad, se manda el cupon dentro de la validacion para
         * ahorrar en el doc controller repetir estos pasos en caso de que la validacion sea true.
         */
        if(r) vr.obj_carry = the_cupon;
        return vr;
    }
    
    
    /**
     * Valida la inhabilitacion de un cupon.
     * @param user_input
     * @return 
     */
    public ValidationResult EsValidoInhabilitarCupon(String user_input)
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult();
        
        boolean r = true; // validez de la operacion
        
        String motivo = ""; // motivo de error si es el caso
        
        Cupon the_cupon = null;// instancia del cupon seleccioado
        String[] cup_str = user_input.split("-");
        
        // se revisa la estructura del String de entrada.
        if( !(cup_str.length == 2) ){ // error de mala estructura
            r=false;
            motivo = this.BAD_STRUCTURE;
        }
        
        int cupon_id=0;// se asignan numeros reservados para 
        int pass=0; // si se entra al siguiente if y no se asignan valores reales a estas
        // 2 variables entonces estos valores no se usan ya que se requiere nuevamente en el
        // siguiente if el valor de r=true.
                
        try{ // se revisa que no hayan letras.
            if(r){
                cupon_id = Integer.parseInt(cup_str[0]); // se obtiene el id del cupon del String del usuario
                pass = Integer.parseInt(cup_str[1]); // se obtiene el numero aleatorio del cupon del String del usuario
            }
        } catch(NumberFormatException ex){
            r = false;
            motivo = this.BAD_STRUCTURE;
        }
        
        if(r){ // se revisa la existencia del cupon.
            Hndl_Cupon hndcup = new Hndl_Cupon();
            ArrayList<Cupon> lscup = hndcup.getCupnBy_Id(cupon_id);
            if(lscup.isEmpty()){
                r=false;
                motivo = this.NO_EXISTE;
            }
            else the_cupon = lscup.get(0);
        }
   
        // hasta este punto si r=true entonces, el cupon existe.
        
        if(r){ // se revisa que coincidadn
            if( !(pass==the_cupon.pass) ){
                r=false;
                motivo = this.NO_PASS;
            }
        }
        
        if(r){ // se revisa que el cupon no haya sido gastado en una compra anterior
            if(the_cupon.estado==0){
                r=false;
                motivo=this.CUPON_GASTADO;
            }
        }
        
        // se asignan los valores correspondientes al Validations Result.
        vr.setResult(r);
        if(!r) vr.motivo=motivo;
        
        /**
         * la construccion del cuopon involucra el uso del conetor correspondiente a la BD y ademas
         * el uso de un split. por esta coplejidad, se manda el cupon dentro de la validacion para
         * ahorrar en el doc controller repetir estos pasos en caso de que la validacion sea true.
         */
        if(r) vr.obj_carry = the_cupon;
        return vr;
    }
    
    
    /**
     * metodo que revisa en la lista observable del table view de venta si el cupon indicado ya existe
     * en dicha tabla. Se hace la validacion para evitar que se use varias veces el cupon en la misma venta.
     * @param lsRec
     * @return 
     */
    public boolean ContieneCupon(ObservableList<SptFacturaRecord> lsRec, int cupon_id){
        boolean r=false;
        for(SptFacturaRecord sptfr : lsRec){
            if(sptfr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
                if(sptfr.cupon.id==cupon_id) r = true; // se pone true si se encuentra u record tipo abono con el id especificado.
            }
        }
        return r;
    }

}
