package Validator_tdpos_r;

import BD_tdpos_r.Hndl_Productos;
import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Producto;

/**
 * claase que encapsula todos los metodos necesarios para hacer la 
 * validacion de un producto.
 * @author esteban
 */
public class Va_Producto extends Validador{
    
    /**
     * mensaje error codigo de un producto
     */
    String codigo_no_valido_struc = "El codigo de un producto solo puede contener letras y numeros sin espacios";
    
    /**
     * mensaje de error para codigo de producto repetido.
     */
    String codigo_no_valido_rept = "Ya exite un producto con este codigo";
    
    /**
     * mensaje de error para precios no validos
     */
    String precios_no_validos = "el costo no es valido y los precios de venta deben ser mayor al costo";
    
    /**
     * mensaje de error para iva no valido
     */
    String iva_no_valido = "el iva debe ser estar entre 0% y 100%";
    
    
    /**
     * mensaje de error cuando el stock no es un numero valido.
     */
    String stock_no_valido = "el stock debe ser mayor a 0";
    
    
    public final String DESCRIPCION_NO_VALIDA = "La descripcion introducida no es valida."
            + "debe tener mas de 4 caracteres.";
    
    
    /**
     * metodo que valida todos los campos de texto para la codificacion de 
     * un producto.
     * @param codigo
     * @param descripcion
     * @param costo
     * @param pv_mayor
     * @param pv_contado
     * @param pv_credito
     * @param stock
     * @param iva
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ValidationResult ProductoEsValido(String codigo, String descripcion,
            String costo, String pv_contado, String pv_credito,
            String pv_mayor, String stock, String iva) 
            throws ClassNotFoundException, SQLException{
        
        ValidationResult vr = new ValidationResult(); // respuesta final
        boolean r = true;
        
        ValidationResult vraux = new ValidationResult(); // variable auxiliar.
        
        vraux = this.codigoEsValido(codigo); // validacion de codigo
        if(!vraux.result){ // si codigo no es valido
            r = false;
            vr.motivo = vraux.motivo; // se sabe que empieza vacio el atributo motivo.
        }
        
        
        vraux = this.DescripcionValida(descripcion);// validacion de la descripcion
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        
        // validacion de precios
        vraux = this.preciosValidos(costo, pv_contado, pv_credito, pv_mayor);
        if(!vraux.result){ // si precios no son validos
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        
        
        vraux = this.stockValido(stock);// validacion del stock
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        
        
        vraux = this.ivaValido(iva); // validacion del iva.
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        vr.setResult(r);
        
        return vr;
    }
    
    
    /**
     * metodo que indica si un codigo para un producto es valido
     * @param codigo
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ValidationResult codigoEsValido(String codigo) 
            throws ClassNotFoundException, SQLException{
        
        boolean r = true;
        ValidationResult vr = new ValidationResult();
        
        if(super.Only_alp_num(codigo)){ // se revisa si contiene solo numeros y letras  sin espacios
            if(this.existeProducto(codigo)){ // se revisa si el producto aun no esta codificado
                r = false;
                vr.setMotivo(this.codigo_no_valido_rept); // se indica que el codigo esta repetido
            }
        } 
        else{
            r = false;
            vr.setMotivo(this.codigo_no_valido_struc); // se indica que la composicion del codigo no es valida
        }
        
        vr.setResult(r);
        return vr;
    }
    
    /**
     * metodo que valida si existe o no un producto
     * @param codigo
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public boolean existeProducto(String codigo) throws ClassNotFoundException, SQLException{
        boolean r = true;
        Hndl_Productos hnp = new Hndl_Productos();
        ArrayList<Producto> lp = hnp.buscarProducto_codigo(codigo);
        if ( lp.isEmpty()) r = false;
        return r;
    }
    
    
    /**
     * metodo que determina si los precios establecidos para un prodcuto
     * son validos o no en caso contrario
     * @param costo
     * @param contado
     * @param credito
     * @param mayor
     * @return 
     */
    public ValidationResult preciosValidos(String costo, String contado, String credito, String mayor){
        boolean r = true;
        
        int pcosto, pcontado, pcredito, pmayor;
        
        try{
            // se revisa sin son numeros enteros
            pcosto = Integer.parseInt(costo);
            pcontado = Integer.parseInt(contado);
            pcredito = Integer.parseInt(credito);
            pmayor = Integer.parseInt(mayor);
            
            if(pcosto <= 0) r = false; // el costo no puede ser negativo
            
            // los precios de venta no pueden ser menores al costo.
            if(pcontado <= pcosto) r = false;
            if(pcredito <= pcosto) r = false;
            if(pmayor <= pcosto) r = false;
            
        } catch(NumberFormatException nfe){
            r = false;
        }
        ValidationResult vr;
        
        if(r) vr = new ValidationResult(r, "");
        else vr = new ValidationResult(r, this.precios_no_validos);
        
        return vr;
    }
    
    /**
     * meto que verifica si el iva establecido para un producto es valido.
     * @param iva_s
     * @return 
     */
    public ValidationResult ivaValido(String iva_s){
        boolean r = true;
        
        int iva;
        try{
            iva = Integer.parseInt(iva_s);
            if(iva < 0) r = false;
            if(iva > 100) r = false;
            
        } catch(NumberFormatException nfe){
            r=false;
        }
        
        ValidationResult vr;
        
        if(r) vr = new ValidationResult(r, "");
        else vr = new ValidationResult(r, this.iva_no_valido);
        
        return vr;
    }
    
    
    /**
     * metodo que indica si el stock introducido es valido.
     * @param stock_s
     * @return 
     */
    public ValidationResult stockValido(String stock_s){
        boolean r = true;
        int stock;
        try{
            stock = Integer.parseInt(stock_s);
            if (stock < 0) r = false;
        } catch(NumberFormatException nfe){
            r=false;
        }
        
        ValidationResult vr;
        
        if(r) vr = new ValidationResult(r, "");
        else vr = new ValidationResult(r, this.stock_no_valido);
        
        return vr;
    }

    public ValidationResult EsValidoModificar(String codigo, String descripcion,
            String costo,String pv_contado, String pv_credito, String pv_mayor, String stock,
            String iva, String old_cod) throws ClassNotFoundException, SQLException {
        
        ValidationResult vr = new ValidationResult(); // respuesta final
        boolean r = true;
        
        ValidationResult vraux = new ValidationResult(); // variable auxiliar.
        
        if(!codigo.equals(old_cod)){ // solo si el codigo nuevo es diferente del codigo
            // anterior, se hace validacion de que no este repretido.
            vraux = this.codigoEsValido(codigo); // validacion de codigo
            if(!vraux.result){ // si codigo no es valido
                r = false;
                vr.motivo = vraux.motivo; // se sabe que empieza vacio el atributo motivo.
            }
        }
        
        // validacion de precios
        vraux = this.preciosValidos(costo, pv_contado, pv_credito, pv_mayor);
        if(!vraux.result){ // si precios no son validos
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        
        
        vraux = this.stockValido(stock);// validacion del stock
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }   
        
        vraux = this.DescripcionValida(descripcion);// validacion de la descripcion
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        
        vraux = this.ivaValido(iva); // validacion del iva.
        if(!vraux.result){
            r = false;
            vr.motivo = vr.motivo + "\n" + vraux.motivo;
        }
        vr.setResult(r);
        
        return vr;
    }
    
    
    /**
     * metodo que indica si la descripcion introducida es valida.
     * @param descripcion 
     * @return 
     */
    public ValidationResult DescripcionValida(String descripcion){
        boolean r = true;
        ValidationResult vr;
        
        if( !(descripcion.length()<4 || super.EstaVacio(descripcion)) ){
            vr = new ValidationResult(r, this.DESCRIPCION_NO_VALIDA);
        }
        else{ // si la descripcion tiene mas de 4 caracteres y esto no son white spaces
            // entonces la descripcion si es valida
            vr = new ValidationResult(r, "");
        }
        return vr;
    }
    
}
