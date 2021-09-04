package Validator_tdpos_r;

import java.sql.SQLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.Factura;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;

/**
 *Clase para hacer validacion de las operaciones de hechas en la seccion de
 * abonos y devoluciones.
 * @author esteban
 */
public class Va_Abo_Dev extends Validador{
    
    /**
     * Indica que noo es valido hacer una devolucion porque no
     * hay factura seleccionada. Para uso en ValidationResult object
     */
    public final String NO_VAL_FACTURA_EMPTY = "No hay factura seleccionada";
    
    /**
     * Indica que el productoo seleccionado para hacer devolucion no esta registrado
     * en la factura seleccionada y por tanto no es valido agregarlo a la tabla de operaciones.
     * para uso en ValidationResult.motivo.
     */
    public final String NO_VAL_PRODUCTO_DEV = "La referencia de producto que desea devolver no \n"
            + "esta registrada en esta factura";
    
    /**
     * mesaje de explicacion si el usuario ingresa cero o un numero negativo
     * como cantidad de prendas a devolver.
     */
    public final String CANT_ZERO_MENOS = "El numero de prensas a devolver no puede ser cero\n"
            + " ni numeros negativos";
    
    /**
     * cuando hay error por produto repetetido en tabla de operaciones.
     */
    public final String NO_VAL_OPE_TAB_REPE = "Ya se ha ingresado este producto para \n "
            + "devolucion.";
    
    
    /**
     * cuando ya hay en la tabla de operaciones un abono record.
     */
    public final String NO_VAL_ABONO_REPE = "Ya hay un registro de abono en la tabla de operaciones";
    
    
    /**
     * mensaje de error cuando el valor a abonar supera el saldo de la factura seleccionada.
     */
    public final String NO_VAL_SALDO = "El valor a abonar no puede exceder el saldo de la factura.";
    
    
    /**
     * Indica que  el numero de unidades que se desea devolver no es valido
     * exede las [ registradas ] o [ registradas ] - [ previamente devueltas ].
     * para uso con validation result.
     */
    public final String NO_VAL_CANT = "La cantidad de unidades que desea devolver no es valida \n"
            + " ya que exede el numero de unidades pendientes en la factura.";
    
    /**
     * indica si es valido hacer una devolucion de un producto determinado.
     * en caso de no ser valido entonces se entrega el motivo en un String,
     * en el segundo lugar del Array List de ValidationResult.
     * Este metodo se debe usar antes de el dialogo modal.
     * @param fac
     * @param op
     * @param codigo_dev
     * @param qnt
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public ValidationResult PreDevolucionValida (Factura fac, ObservableList<SptFacturaRecord> op, String codigo_dev)
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        try{
            
            int a = fac.consecutivo; // solo para generar NullPointer si no hay
            // factura seleccionada. int a nunca se usa.
            
            //No contiene el producto
            if(!fac.ContieneProducto(codigo_dev)){
                r = false;
                vr.motivo += this.NO_VAL_PRODUCTO_DEV;
            }
            
            // si la factura si contiene el producto
            if(fac.ContieneProducto(codigo_dev)){
                for(SptFacturaRecord fr : op){ // se revisa que el producto ingresado no este en la tabla de operaciones
                    if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                        if(fr.spt_codigo.get().equals(codigo_dev)){
                            r=false; // se indica que no se permite record de devolucion repetida.
                            vr.motivo = this.NO_VAL_OPE_TAB_REPE;
                        }
                    }
                } // para mejor consistencia no se permite codigo repetido en tabla de operaciones
            }
            
        } catch(NullPointerException ex){
            r = false;
            vr.motivo = this.NO_VAL_FACTURA_EMPTY;
        }
        
        vr.setResult(r);
        return vr;
    }
    
    
    /**
     * metodo que revisa si la cantidad ingresada a deolver es valida.
     * no es tribial ya que no solo hay que tener en cuenta los records
     * iniciales de la factura sino tambien las devoluciones que se hayan 
     * agregado a la misma
     * @param fac
     * @param quant
     * @param cod
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ValidationResult DevQuantValido(Factura fac, int quant, String cod) 
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult();
        boolean r = true; // se parte de que es valido hacer la d
        
        if(quant <= 0){ // validando que no se ingrese 0 o negativo
            r = false;
            vr.motivo = this.CANT_ZERO_MENOS;
        }
        
        ArrayList<SptFacturaRecord> lfr = fac.getFacturaRecords();
        
        // lista a parte para separar el producto record
        // y sus devoluciones para unificar cantidad y obtener un int 
        // de maxima ccantidad de unidades que se pueden devolver.
        ArrayList<SptFacturaRecord> lfr_sep = new ArrayList<>();
        
        // se juntan todos los records
        for(SptFacturaRecord fr : lfr){ // para record se compara el codigo
            if(fr.getSptCodigo().get().equals(cod)){
                lfr_sep.add(fr); // si coincide entonces se agrega a la lista
            }
        } //a este punto se dispone de una lista con minimo el record
        
        int max_dev_num = 0; // numero maximo que se puede devolver.
        for(SptFacturaRecord fr : lfr_sep){
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD))
                max_dev_num += fr.spt_cantidad.get();
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD))
                max_dev_num -= fr.spt_cantidad.get();
        }
        
        // si el numero de unidades que se desea devolver es mayor al
        // numero de unidades restantes de la factura entonces no se valido
        // y se retorna r=false
        if(max_dev_num < quant){
            r = false;
            vr.motivo = this.NO_VAL_CANT;
        }
        
        vr.result = r;
        return vr;
    }
    
    
    
    /**
     * metodo que permite revisar si el abono que se desea ingresar es valido.
     * Validacion previa al promt de cantidad a abonar.
     * @param fac
     * @param abono
     * @param op
     * @return 
     */
    public ValidationResult PreAbonoValido(Factura fac, ObservableList op){
        boolean r = true;
        ValidationResult vr = new ValidationResult();
        
        try{
            // para detectar si no hay factura seleccionada mediante NullPointerException
            int a = fac.consecutivo; // a no se usa realmente.
            
            if(this.ContieneAbono(op)){
                r = false; // si ya hhay un abono record en la tabla de operaciones
                vr.motivo = this.NO_VAL_ABONO_REPE;
            }
            
        } catch(NullPointerException ex){ // en caso de que no haya factura seleccionada
            r = false; // se agrega al mensaje dee explicacion.
            vr.motivo = this.NO_VAL_FACTURA_EMPTY;
        }
        vr.result = r;
        return vr;
    }
    
    
    /**
     * retorna true si el valor especifica  por abono no supera el 
     * saldo de la factura.
     * @param fac
     * @param abono
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public ValidationResult NumAbonoCheck(Factura fac, int abono) 
            throws SQLException, ClassNotFoundException{
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        if(abono>fac.ReconstruirSaldo()){
            r = false;
            vr.motivo = this.NO_VAL_SALDO;
        }
        
        vr.result = r;
        return vr;
    }
    
    
    /**
     * metodo que revisa si en la tabla de operaciones ya hay un abono
     * record. True si ya hay un abono.
     * @param op
     * @return 
     */
    public boolean ContieneAbono(ObservableList<SptFacturaRecord> op){
        boolean r = false; // se parte asumiendo no hay ningun abono
        for(SptFacturaRecord fr : op){ // con un solo abono que haya se retorna true
            if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)) r = true;
        }
        return r;
    }
}