
package BD_tdpos_r;

import Validator_tdpos_r.Validador;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Producto;

/**
 *
 * @author esteban
 */
public class BDHandler {
    
    // los metodos de esta superclase solo seran usadas por las clases hijas
    // solamente para obtener resultsets
    // en caso de hacer Update Queries se debe obtener el
    // Prepared statement en su respectivo BDH hijo.
    
    /**
     * a partir de mysql 8 se debe agregar ?useSSL=false a la direccion de 
     * conexion, ya que el metodo de autenticacion se cambio a uno mas robusto
     * y al inhabilitar ssl se mantiene compatibilidad con el codigo existente de
     * oxo moda.
     *  en mysql 8 cfname cambia a com.mysql.cj.jdbc.Driver
     */
    
    //private final String direccionGranero="jdbc:mysql://127.0.0.1:3306/negocio_r";
    
    /**
     * para evitar un error relacionado con la zona horaria se debe agregar
     * useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC 
     * 
     */
    private final String direccionGranero="jdbc:mysql://127.0.0.1:3306/negocio_r?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    //jdbc:mysql://localhost/db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    private final String user="root";
    private final String pass="000012";
    //private final String cfname="com.mysql.jdbc.Driver";
    private final String cfname="com.mysql.cj.jdbc.Driver";
    private final String jlangc = "class java.lang.";
    
    public Connection link;
    public ResultSet rs;
    public PreparedStatement pst;
    
    public BDHandler(){
        
    }
    
    
    /**
     * Metodo que entrega un objeto tipo conexion. Esta conexion se hace
     * a la base de datos negocio_r.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Connection GetConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.getCfname());
        Connection lk = DriverManager.getConnection(getDireccion(), getUser(), getPass());
        return lk;
    }
    
    
    /**
     * metodo que instanncia un objeto conexion en esta claase padre 
     * Objeto => (Connection link). 
     * la idea es que las clases hijas hagan setConnetion y luego usen el objeto
     * link de esta clase padre si fuere neceario.
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void SetConnection() throws ClassNotFoundException, SQLException {
        Class.forName(this.getCfname());
        this.link = DriverManager.getConnection(getDireccion(), getUser(), getPass());
    }

    
    /**
     * direccion para hacer conexion local a la base de datos de MYSQL
     * negocio_r
     * @return String
     */
    public String getDireccion() {
        return this.direccionGranero;
    }
    
    /**
     * String usuario para hacer conexion local a la base de datos de 
     * MYSQL negocio_r
     * @return 
     */
    public String getUser() {
        return this.user;
    }
    
    
    /**
     * Contraseña para hacer conexion a local a la base de datos de MYSQL
     * al schema negocio_r
     * @return 
     */
    public String getPass() {
        return this.pass;
    }
    
    
    
    /**
     * String necesario para establecer la conexion local  la base de datos
     * de MYSQL
     * @return 
     */
    public String getCfname() {
        return this.cfname;
    }
    
    
    /**
     * Este metodo se debe ejecutar por todas las clases hijas
     * al final de cualquier operacion de lectura en la
     * base de datos. se cierran los objetos:
     * Connection link, ResultSet rs, PreparedStatement pst.
     * tener cuidado de usar si se hace un Update porque rs seria nulo
     * @throws SQLException 
     */
    public void CerrarTodo() throws SQLException{
        rs.close();
        pst.close();
        link.close();
    }
    
    
    /**
     * Metodo que se debe ejecutar por las clases hijas despues de hacer una 
     * operacion de escritura sobre la base de datos
     * Se cierran los objetos Connection link y PreparedStatement pst.
     * @throws SQLException 
     */
    public void CerrarUpdate() throws SQLException{
        this.link.close();
        this.pst.close();
    }
    
    /**
     * en las busquedas por palabras claves, siempre es necesario varios
     * procedimientos que son comunes a todos los casos com busqueda de producto
     * cliente o vendedor entre otros. Este meto debe:
     * hacer un split con " " para la obtencion de estas palbaras claves.
     * agrega porcentajes (%keyword_i%) al comienzo y final para
     * que en el SQL select mejoren los match. finalmente se arma el prepared 
     * statement.
     * 
     * Este metodo recibe el nombre de la columna en la que se desea buscar,
     * la frase SQL de base y el string de palabras claves.
     * al final configura su atributo prepared statement por lo que no retorna
     * ningun valor directamete, sino de manera indirecta para que sea accedido 
     * por clases hijas.
     * @param frase_base
     * @param ColName
     * @param b
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void ArreglarFrase_y_PST(String frase_base, String ColName, String b) throws ClassNotFoundException, SQLException{
        
        Validador v = new Validador();
        
        //se desea que una busqueda del string "" arroje todos los productos
        if(v.EstaVacio(b)) b = "";// pero mientras split("") entrega un String[] de tamaño = 1
        // split(" ") entrega un String[] de tamaño = 0 por lo que se usa este if
        // para asumir cualquier cantidad de white spaces como un "".
        
        ArrayList<Producto> alp = new ArrayList<>();
        
        String key_words[] = b.split(" ");
        String frase = frase_base;
        
        for (int j = 0; j < key_words.length; j++) {
            if (j == (key_words.length - 1)) { // si es el ultimo item
                frase = frase + ColName + " like ? "; // se cierra sin and
            } else {
                frase = frase + ColName + " like ? and ";
            }
            key_words[j] = "%"+key_words[j]+"%";
        }
        
        this.SetConnection();
        this.pst = (PreparedStatement) this.link.prepareStatement(frase);
        
        for(int j = 0; j < key_words.length; j++){
            this.pst.setString(j+1, key_words[j]);
        }
        
    }
    
}
