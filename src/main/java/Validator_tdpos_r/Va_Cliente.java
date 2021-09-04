package Validator_tdpos_r;

import BD_tdpos_r.Hndl_Clientes;
import java.sql.SQLException;
import tentactildesktoppos.negocio_r_objs.Cliente;

/**
 *
 * Clase que encapsula metodos para hacer validacion de los datos
 * ingresados para registrar un cliente.
 * @author esteban
 */
public class Va_Cliente extends Validador{
    
    
    /**
     * mensaje que explica que la fecha establecida por el usuario no es valida
     */
    public String fecha_no_valida = "Porfavor verifique la fecha seleccionada";
    
    
    /**
     * mensaje de error para el numero de identificaicion.
     */
    public String id_no_valido = "El numero de identificion no es valido.";
    
    
    /**
     * mensaje de erro que indicca que el id ya esta registrado en la base de datos.
     */
    public String id_repetido = "Ya existe un cliente con esta identificacion en la base de datos";
    
    
    /**
     * mensaje de error para los numeros telefonicos.
     */
    public String tels_no_valido = "Alguno de los numeros telefonicos introducidos no es valido.";
    
    /**
     * mensaje que indica error por falta de campos obligatorios.
     * cuando se codifica un cliente de mayoreo credito, el telefono 1 es obligatorio.
     */
    public String obl_no_valido_tel1 = "Para los clientes de credito o pormayor el telefono 1 "
            + "es un campo obligatorio. rectifique este campo porfavor";
    
    
    /**
     * mensaje que indica error por falta de campos obligatorios.
     * cuando se codifica un cliente de mayoreo credito, la direccion son obligatorios.
     */
    public String obl_no_valido_dir = "Para los clientes de credito o pormayor la direccion "
            + "es un campo obligatorio. rectifique este campo porfavor";
    
    
    /**
     * mensaje de error para el email.
     */
    public String email_no_valido = "El email introducido no es valido";
    
    
    public String nombre_no_valido = "El nombre introducido no es valido";
    
    
    
    /**
     * metodo que hace validacion de un objeto tipo cliente
     * para saber si se puede o no codificar.
     * @param id
     * @param nombre
     * @param tel1
     * @param ubicacion
     * @param tel3
     * @param tel2
     * @param email
     * @param direccion
     * @param tipo_cliente
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ValidationResult clienteValido(String id, String nombre, String tel1,
            String tel2, String tel3, String direccion, String email,
            String ubicacion, String tipo_cliente) 
            throws ClassNotFoundException, SQLException{
        
        ValidationResult vr = new ValidationResult(); // objeto que encapsula el resultado final d ela validacion.
        boolean r = true;
        
        ValidationResult vaux = new ValidationResult(); // objeto auxiliar para leer sib validaciones
        
        //*****************************************
        // se hace validacion de la identificacion
        vaux = this.idEstructuraValida(id);
        if(!vaux.result){ // si el id (cc o nit) no tiene una estructura valida
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vaux.motivo; // se agrega la explicacion
        } 
        
        vaux = this.existeCliente(id);
        if(vaux.result){ // si el codigo ya existe en la BD
            r = false; // entonces los datos nos on validos.
            vr.motivo = vr.motivo + "\n\n" + vaux.motivo; // se agrega la explicacion
        }
        
        
        //******************************************
        // se hacce validacooin del nombre del cliente
        if(super.EstaVacio(nombre) || nombre.length()<3){ // si el nombre esta vacio
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vr.motivo + "\n\n" + this.nombre_no_valido;
        }
        
        //****************
        // si es un cliente es de contado las demas casillas importantes se validan como opcionales
        if(tipo_cliente.equals(Cliente.TIPO_CONTADO)){
            
            vaux = this.telsValidos(tel1, tel2, tel3);
            if(!vaux.result){ // si los telefonos no son validos y no estan vacios (validado como dato opcional)
                r = false;
                vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
            }
        }
        
        //****************************************************************
        // si el cliente es de mayoreo las casillas immportantes se validan como obligatorias
        if(tipo_cliente.equals(Cliente.TIPO_CREDITO) || tipo_cliente.equals(Cliente.TIPO_MAYOR)){
            
            // se valida telefono1
            if(!super.isTel(tel1)){ // el telefono 1 ya es obligatorio.
                r = false;
                vr.motivo = vr.motivo + "\n\n" + this.obl_no_valido_tel1;
            }
            // se validan como opcionales tel2 y 3, no se usa tel1 para reusar el metodo
            vaux = this.telsValidos("777 777 7", tel2, tel3);
            if(!vaux.result){ // si los telefonos no son validos y no estan vacios (validado como dato opcional)
                r = false;
                vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
            }
            
            if(super.EstaVacio(direccion) || direccion.length()<3){
                r = false;
                vr.motivo = vr.motivo + "\n\n" + this.obl_no_valido_dir;
            }        
        }
        
        vaux = this.emailValido(email); // el email en cualquiera de los caso se valida como dato opcional
        if(!vaux.result && !email.isEmpty()){ // si el email no es valido y no esta vacio
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
        }
        
        vr.setResult(r); // si cualquiera de las validaciones falla entonces el resultado es tambienn false
        return vr;
    }
    
    
    
    public ValidationResult MODclienteValido(String id, String nombre, String tel1,
            String tel2, String tel3, String direccion, String email,
            String ubicacion, String tipo_cliente, String old_id) 
            throws ClassNotFoundException, SQLException{
        
        ValidationResult vr = new ValidationResult(); // objeto que encapsula el resultado final d ela validacion.
        boolean r = true;
        
        ValidationResult vaux = new ValidationResult(); // objeto auxiliar para leer sib validaciones
        
        //*****************************************
        // se hace validacion de la identificacion
        vaux = this.idEstructuraValida(id);
        if(!vaux.result){ // si el id (cc o nit) no tiene una estructura valida
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vaux.motivo; // se agrega la explicacion
        } 
        
        // esto es lo que hace diferente la validacion para insertar un cliente nuevo
        // y modificar un cliente ya existente
        if(!id.equals(old_id)){ // solo si se hace modificacion de la llave primaria
            // del cliente entonces se hace una revision si el id esta disponible
            // o si ya es usado por otro cliente.
            vaux = this.existeCliente(id);
            if(vaux.result){ // si el codigo ya existe en la BD
                r = false; // entonces los datos nos on validos.
                vr.motivo = vr.motivo + "\n\n" + vaux.motivo; // se agrega la explicacion
            }
        }
        
        
        //******************************************
        // se hacce validacooin del nombre del cliente
        if(super.EstaVacio(nombre) || nombre.length()<3){ // si el nombre esta vacio
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vr.motivo + "\n\n" + this.nombre_no_valido;
        }
        
        //****************
        // si es un cliente es de contado las demas casillas importantes se validan como opcionales
        if(tipo_cliente.equals(Cliente.TIPO_CONTADO)){
            
            vaux = this.telsValidos(tel1, tel2, tel3);
            if(!vaux.result){ // si los telefonos no son validos y no estan vacios (validado como dato opcional)
                r = false;
                vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
            }
        }
        
        //****************************************************************
        // si el cliente es de mayoreo las casillas immportantes se validan como obligatorias
        if(tipo_cliente.equals(Cliente.TIPO_CREDITO) || tipo_cliente.equals(Cliente.TIPO_MAYOR)){
            
            // se valida telefono1
            if(!super.isTel(tel1)){ // el telefono 1 ya es obligatorio.
                r = false;
                vr.motivo = vr.motivo + "\n\n" + this.obl_no_valido_tel1;
            }
            // se validan como opcionales tel2 y 3, no se usa tel1 para reusar el metodo
            vaux = this.telsValidos("777 777 7", tel2, tel3);
            if(!vaux.result){ // si los telefonos no son validos y no estan vacios (validado como dato opcional)
                r = false;
                vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
            }
            
            if(super.EstaVacio(direccion) || direccion.length()<3){
                r = false;
                vr.motivo = vr.motivo + "\n\n" + this.obl_no_valido_dir;
            }    
        }
        
        vaux = this.emailValido(email); // el email en cualquiera de los caso se valida como dato opcional
        if(!vaux.result && !email.isEmpty()){ // si el email no es valido y no esta vacio
            r = false; // entonces el obj cliente no es valido
            vr.motivo = vr.motivo + "\n\n" + vaux.motivo;
        }
        
        vr.setResult(r); // si cualquiera de las validaciones falla entonces el resultado es tambienn false
        return vr;
    }
    
    
    
    /**
     * metodo que retorna true si ya existe en la  base de datos un cliente
     * con la cedula o nit especificado por el String id.
     * @param id
     * @return 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public ValidationResult existeCliente(String id)
            throws SQLException, ClassNotFoundException{
        
        ValidationResult vr = new ValidationResult();
        Hndl_Clientes hnc = new Hndl_Clientes(); // obejto para hacer operaciones sobre la BD
        
        if (!hnc.buscarCliente_id(id).isEmpty()){ // si no esta vacio, si existe ya este id
            vr.setResult(true);
            vr.setMotivo(this.id_repetido);
        }
        else{ // si no existe este id
            vr.setResult(false);
        }
        return vr;
    }
    
    
    /**
     * metodo que hace validacion de cc o nit del cliente.
     * @param id
     * @return 
     */
    public ValidationResult idEstructuraValida(String id){
        ValidationResult vr = new ValidationResult();
        
        if(super.isPositiveNum(id) && id.length()>=Validador.CC_MIN_DIGITS){
            vr.setResult(true);// si el id es un numero entero valido
        } 
        else{
            vr.setResult(false); // se indica que el id no tiene una estructura valida
            vr.setMotivo(this.id_no_valido);
        }
        return vr;
    }
    
    
    /**
     * metodo que hace validacion de los numero telefonicos de un cliente.
     * el numero telefonico solo puede  ser un String vacio o un numeroo valido.
     * tambien se aceptan espacios.
     * @param tel1
     * @param tel2
     * @param tel3
     * @return 
     */
    public ValidationResult telsValidos(String tel1, String tel2, String tel3){
        
        ValidationResult vr = new ValidationResult();
        boolean r = true;
        
        if(!super.isTel(tel1) && !tel1.equals("")) r = false;
        if(!super.isTel(tel2) && !tel2.equals("")) r = false;
        if(!super.isTel(tel3) && !tel3.equals("")) r = false;
        
        vr.setResult(r);
        if(!r) vr.setMotivo(this.tels_no_valido);
        
        return vr;
        
    }
    
    /**
     * metodo que hace validacion de un email.
     * si el email es valido el metodo retorna true.
     * SOLO se acepta un email vacio "" o con un String valido de
     * acuerdo al regex usado.
     * 
     * @param email
     * @return 
     */
    public ValidationResult emailValido(String email){
        ValidationResult vr = new ValidationResult();
        
        if(email.equals("")){ // si no tiene caracteres
            vr.setResult(true);
        } else{ 
            if(super.regexCheckEmail(email)){ // si tienen caracteres y el string es valido como correo
                vr.setResult(true);
            }
            else{ // si tiene caracteres y el string no es valido
                vr.setResult(false);
                vr.setMotivo(this.email_no_valido);
            }
        }
        return vr;
    }
    
}
