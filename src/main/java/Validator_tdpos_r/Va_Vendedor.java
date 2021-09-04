package Validator_tdpos_r;

import BD_tdpos_r.Hndl_Vendedor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Vendedor;

/**
 * Clase que encapsula las variables y metodos necesarios para hacer validacion
 * de los datos en ingresados en la UI para codificar a un Vendedor
 * @author esteban
 */
public class Va_Vendedor extends Validador{
    
    /**
     * mensaje que indica que el id para el vendedor no es valido.
     */
    public String id_no_valido = "El favor rectifique el numero de cedula introducida";
    
    
    /**
     * mensaje que indica al usario que el cc que va a ingresar ya existe en la BD.
     */
    public String id_repetido = "Ya existe un vendedor con ese numero de cedula en la base de datos";
    
    /**
     * mensaje que indica que los telefonos para el vendedor no es valido.
     */
    public String telefonos_no_validos = "Porfavor rectifique los telefonos introducidos. telefono 1 es obligatorio"
            + " pero telefono 2 es opcional";
    
    /**
     * mensaje que indica que el email para el vendedor no es valido.
     */
    public String email_nol_valido = "Porfavor introducir un email valido";
    
    /**
     * mensaje que indica que el id para el vendedor no es valido.
     */
    public String nombre_no_valido = "Porfavor rectifique el nombre introducido, recuerde que incluye apellidos";
    
    /**
     * mensaje que indica que el id para el vendedor no es valido.
     */
    public String direccion_no_valido = "Porfavor rectifique la direccion introducida";
    
    /**
     * mensaje que se muestra al usuario cundo no introduce una fecha de nacimiento.
     */
    public String nacimiento_no_valido = "La fecha de nacimiento no puede quedar vacia";
    
    
    /** 
     * metodo que indica si los datos establecidos para codificar a un cliiente
     * son o no validos vr.result
     * true = valido
     * false = no valido
     * @param id
     * @param nombre
     * @param tel1
     * @param tel2
     * @param email
     * @param Direccion
     * @param ld
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ValidationResult VendedorEsValido(String id, String nombre, 
            String tel1, String tel2, String email, String Direccion, LocalDate ld) 
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult(); 
        boolean r = true; // variable auxiliar del resultado de la validacion, Se inicia asumiendo validez.
        
        ValidationResult vaux = new ValidationResult(); // variable auxiliar para el return de los submetodos
        
        // si entra en cualquiera de los if's entonces no pasa la validacion
        vaux = this.IdEsValido(id);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vaux.getMotivo());
        }
        
        
        vaux = this.nombreEsValido(nombre);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        vaux = this.TelefonosValidos(tel1, tel2);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        vaux = this.emailEsValido(email);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        vaux = this.DireccionEsValida(Direccion);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        // se agrego para no permitir fechas de nacimiento vacias para los vendedores
        // y al miesmo tiempo mantener el patron de diseño.
        if(ld.toString().equals(Validador.EMPTY_DATE_STR)){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + this.nacimiento_no_valido);
        }
        
        vr.setResult(r); // se estable el resultado de la validacion
        return vr;
    }
    
    
    
    
    /**
     * Hace validacion de la modificacion de un vendedor. hace lo mismo que la validcion de una insercion,
     * con la diferencia de que la existencia del id especificado solo se hace si new id es diferente de old id.
     * si son iguales entonces no se valida id.
     * @param id
     * @param nombre
     * @param tel1
     * @param tel2
     * @param email
     * @param Direccion
     * @param ld
     * @param old_id
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
        public ValidationResult EsValidoModificarVendedor(String id, String nombre, 
            String tel1, String tel2, String email, String Direccion, LocalDate ld, String old_id) 
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult(); 
        boolean r = true; // variable auxiliar del resultado de la validacion, Se inicia asumiendo validez.
        
        ValidationResult vaux = new ValidationResult(); // variable auxiliar para el return de los submetodos
        
        // si entra en cualquiera de los if's entonces no pasa la validacion
        
        // solo si se cambia el id entonces se hace validacion.
        // en caso contario no se valida este atributo.
        if(!id.equals(old_id)){
            vaux = this.IdEsValido(id);
            if(!vaux.result){
                r = false;
                vr.setMotivo(vaux.getMotivo());
            }
        }
        
        vaux = this.nombreEsValido(nombre);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        vaux = this.TelefonosValidos(tel1, tel2);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        vaux = this.emailEsValido(email);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        vaux = this.DireccionEsValida(Direccion);
        if(!vaux.result){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + vaux.getMotivo());
        }
        
        
        // se agrego para no permitir fechas de nacimiento vacias para los vendedores
        // y al miesmo tiempo mantener el patron de diseño.
        if(ld.toString().equals(Validador.EMPTY_DATE_STR)){
            r = false;
            vr.setMotivo(vr.motivo + "\n\n" + this.nacimiento_no_valido);
        }
        
        vr.setResult(r); // se estable el resultado de la validacion
        return vr;
    }
    
    
    
    /**
     * metodo que indica si el id empleado para codificar a un vendedor
     * es valido o no. en caso contrario establece en validationResult
     * el motivo de la invalidez
     * @param id
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ValidationResult IdEsValido(String id) 
            throws ClassNotFoundException, SQLException{
        ValidationResult vr = new ValidationResult();
        boolean r = true; 
        
        if(!super.isPositiveNum(id) || id.length()<5){ // si no es numero positivo o si es menor a 5 digitos.
            r = false; // falla la validacion
            vr.motivo = this.id_no_valido;
        } 
        
        Hndl_Vendedor hndv = new Hndl_Vendedor();
        ArrayList<Vendedor> lv = new ArrayList<>();
        lv = hndv.getVendedorId(id);
        if(!lv.isEmpty()){ // si no esta vacio entonces si existe un vendedor con este id
            r = false;
            vr.motivo =  vr.motivo + "\n\n" + this.id_repetido;
        }
        
        vr.setResult(r);
        return vr;
    }
    
    
    
    /**
     * metodo que indica si el nombre empleado es valido para el vendedor.
     * se revisa que no este vacio, que contenga solo espacios o menos de 3 
     * caracteres.
     * @param nombre
     * @return 
     */
    public ValidationResult nombreEsValido(String nombre){
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        if(super.EstaVacio(nombre) || nombre.length()<3){
            r = false; // no pasa la validacion
            vr.setMotivo(this.nombre_no_valido);
        }
        
        vr.setResult(r);
        return vr;
    }
    
    
    /**
     * metodo que indica en vr si los dos telefonos son o no validos
     * @param tel1
     * @param tel2
     * @return 
     */
    public ValidationResult TelefonosValidos(String tel1, String tel2){
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        
        // si esta vacio o no es telefono valido (validacion de un obligatorio)
        if(super.EstaVacio(tel1) || !super.isTel(tel1)) r = false; // no pasa validacion
        
        
        // si no esta vacio (validacion de opcional) o no es un numero valido
        if( !tel2.isEmpty() && !super.isTel(tel2)) r = false; // no pasa validacion
        
        if(!r) vr.motivo = this.telefonos_no_validos;
        
        vr.setResult(r);
        return vr;
    }
    
    
    
    /**
     * metodo para validar email como String
     * @param email
     * @return 
     */
    public ValidationResult emailEsValido(String email){
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        if(!email.isEmpty() && !super.regexCheckEmail(email)){
            r = false;
            vr.setMotivo(this.email_nol_valido);
        }
        
        vr.setResult(r);
        return vr;
    }
    
    
    /**
     * metodo validar la direccion. solo se revisa que no sea string vacio
     * o llenado solo con barra espaciadora.
     * @return 
     */
    public ValidationResult DireccionEsValida(String dir){
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        if(super.EstaVacio(dir)){ // solo se revisa que no este vacio o solo con backslash.
            r = false;  // no pasa la validacion.
            vr.motivo = this.direccion_no_valido;
        }
        
        vr.setResult(r);
        return vr;
    }
    
}
