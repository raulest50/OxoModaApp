package tentactildesktoppos.negocio_r_objs;


import java.time.LocalDate;


/**
 * OO representacion de un vendedor
 * @author esteban
 */
public class Vendedor {
    
    /**
     * la cedula o el nit del vendedor.
     */
    public String id;
    
    
    /**
     * nombre comleto del vendedor conn apellidos
     */
    public String nombre;
    
    /**
     * los telefonos de los vendedores, pueden contener espacios.
     */
    public String tel1, tel2;
    
    
    public String direccion;
    public String email;
    public String descripcion;
    
    
    /**
     * fecha de nacimiento y de registro del vendedor.
     */
    public LocalDate nacimiento, fregistro;
    
    
    /**
     * entero que indica el estado del vendedor.
     * 1 = esta trabajando en el almaccen
     * 0 = ya no se encuentra trabajando en el almacen
     */
    public int estado;

    
    /**
     * constructor vacio.
     * todos los atributos en
     */
    public Vendedor(){
        
    }
    
    
    /**
     * el constructor estandar.
     * @param id
     * @param nombre
     * @param tel1
     * @param tel2
     * @param direccion
     * @param email
     * @param descripcion
     * @param nacimiento
     * @param fregistro
     * @param estado 
     */
    public Vendedor(String id, String nombre, String tel1, String tel2, 
            String direccion, String email, String descripcion, 
            LocalDate nacimiento, LocalDate fregistro, int estado) {
        
        this.id = id;
        this.nombre = nombre;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.direccion = direccion;
        this.email = email;
        this.descripcion = descripcion;
        this.nacimiento = nacimiento;
        this.fregistro = fregistro;
        this.estado = estado;
    }
    
    
    /**
     * retorna un representaicon String del cliente.
     * @return 
     */
    public String Vendedor2str(){
        String r = "";
        r += "cedula: " + this.id + "\n";
        r += "Nombre Completo: " + this.nombre + "\n";
        r += "Telefono 1: " + this.tel1 + "\n";
        r += "Telefono 2: " + this.tel2 + "\n";
        r += "Direccion: " + this.direccion + "\n";
        r += "E-mail: " + this.email + "\n";
        r += "cedula: " + this.id + "\n";
        r += "Fecha de Nacimiento: " + this.nacimiento.toString() + "\n";
        return r;
    }
    
}