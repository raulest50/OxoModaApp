package BD_tdpos_r;



import java.sql.Date;
import java.sql.PreparedStatement;




import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Cliente;


/**
 *
 * @author esteban
 */
public class Hndl_Clientes extends BDHandler{
    
    /**
     * constructor vacio
     */
    public Hndl_Clientes(){
        
    }
    
    
    
    /**
     * Se hace busqueda de clientes por nombre y se usa el wildcard %
     * para multiples coincidencia. ademas el uso de "" o " " trae todos los
     * clientes de la BD.
     * @param b
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public ArrayList<Cliente> buscarCliente_nombre(String b) throws SQLException, ClassNotFoundException{
        
        ArrayList<Cliente> lc = new ArrayList<>();
        
        // se arma el querie con los respectivos wildcards % se construye el pst como se requiere.
        super.ArreglarFrase_y_PST(SQL_Clientes.BASE_SEARCH_NOMBRE, SQL_Clientes.COL_NOMBRE, b);
        
        super.rs = super.pst.executeQuery();
        
        // result fetch
        while(rs.next()){ // se itera el result set para leer el/los resultados
            lc.add( new Cliente(
                    rs.getString(SQL_Clientes.COL_ID),
                    rs.getString(SQL_Clientes.COL_NOMBRE),
                    rs.getString(SQL_Clientes.COL_TEL1),
                    rs.getString(SQL_Clientes.COL_TEL2),
                    rs.getString(SQL_Clientes.COL_TEL3),
                    rs.getString(SQL_Clientes.COL_DIRECCION),
                    rs.getString(SQL_Clientes.COL_EMAIL),
                    rs.getString(SQL_Clientes.COL_DESCRIPCION),
                    rs.getDate(SQL_Clientes.COL_CUMPLE).toLocalDate(), // se pasa de slq.Date a local Date.
                    rs.getString(SQL_Clientes.COL_TIPO),
                    rs.getString(SQL_Clientes.COL_UBICACION)));
        }
        
        super.CerrarTodo();
        return lc;
    }
    
    
    /**
     * metodo que hace una busqueda de un cliente
     * que coinsida con una cedula o nit exacto.
     * @param id
     * @return 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Cliente> buscarCliente_id(String id) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Cliente> alc = new ArrayList<>();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Clientes.SELECT_ID);
        super.pst.setString(1, id);
        super.rs = super.pst.executeQuery();
        while(rs.next()){ // se itera el result set para leer el/los resultados
            alc.add( new Cliente(
                    rs.getString(SQL_Clientes.COL_ID),
                    rs.getString(SQL_Clientes.COL_NOMBRE),
                    rs.getString(SQL_Clientes.COL_TEL1),
                    rs.getString(SQL_Clientes.COL_TEL2),
                    rs.getString(SQL_Clientes.COL_TEL3),
                    rs.getString(SQL_Clientes.COL_DIRECCION),
                    rs.getString(SQL_Clientes.COL_EMAIL),
                    rs.getString(SQL_Clientes.COL_DESCRIPCION),
                    rs.getDate(SQL_Clientes.COL_CUMPLE).toLocalDate(), // se pasa de slq.Date a local Date.
                    rs.getString(SQL_Clientes.COL_TIPO),
                    rs.getString(SQL_Clientes.COL_UBICACION)));
        }
        super.CerrarTodo();
        return alc;
    }
    
    
    /**
     * metodo que hace registra un Cliente
     * @param c 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void codificarCliente(Cliente c) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Clientes.INSERT);
        super.pst.setString(1, c.id);
        super.pst.setString(2, c.nombre);
        super.pst.setString(3, c.tel1);
        super.pst.setString(4, c.tel2);
        super.pst.setString(5, c.tel2);
        super.pst.setString(6, c.direccion);
        super.pst.setString(7, c.email);
        super.pst.setString(8, c.descripcion);
        super.pst.setDate(9, Date.valueOf(c.cumple)); // se pasa de local  date a sql.Date
        super.pst.setString(10, c.tipo);
        super.pst.setString(11, c.ubicacion);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
    }
    
    
    /**
     * se elimina el de la tabla de clientes el cliente identificado por
     * id
     * @param id
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void EliminarCliente(String id) throws 
            ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = super.link.prepareStatement(SQL_Clientes.DELETE);
        super.pst.setString(1, id);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
    /**
     * modifica un cliente en la base de datos.
     * para ejecutar este metodo se asume que los datos ya fueron previamente 
     * validades y por tanto no ocurrira ninguna exepcion conocida de antemano.
     * @param nuevo_cli
     * @param old_id
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void modificarCliente(Cliente nuevo_cli, String old_id) 
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = super.link.prepareStatement(SQL_Clientes.UPDATE);
        super.pst.setString(1, nuevo_cli.id);
        super.pst.setString(2, nuevo_cli.nombre);
        super.pst.setString(3, nuevo_cli.tel1);
        super.pst.setString(4, nuevo_cli.tel2);
        super.pst.setString(5, nuevo_cli.tel3);
        super.pst.setString(6, nuevo_cli.direccion);
        super.pst.setString(7, nuevo_cli.email);
        super.pst.setString(8, nuevo_cli.descripcion);
        super.pst.setDate(9, Date.valueOf(nuevo_cli.cumple));
        super.pst.setString(10, nuevo_cli.tipo);
        super.pst.setString(11, nuevo_cli.ubicacion);
        super.pst.setString(12, old_id);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
    /**
     * retorna el numero total de records en la tabla de clientes.
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int CountAll() 
            throws SQLException, ClassNotFoundException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Clientes.SELECT_COUNT_ALL);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Clientes.COL_AS_COUNT);
        }
        return r;
    }
    
    
    /**
     * retorna el numero de clientes que encajan con el tipo especificado.
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int CountByTipo(String tipo) 
            throws SQLException, ClassNotFoundException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Clientes.SELECT_COUNT_TIPO);
        super.pst.setString(1, tipo);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Clientes.COL_AS_COUNT);
        }
        return r;
    }
    
    
    /**
     * retorna el numero de clientes que encajan con la descripcion seleccionada.
     * ver documentacion arriba de la definicion de la columna de descripcion en la clase 
     * SQLC_Clientes.
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int CountByDescri(String descri) 
            throws SQLException, ClassNotFoundException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Clientes.SELECT_COUNT_DESCRI);
        super.pst.setString(1, descri);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Clientes.COL_AS_COUNT);
        }
        return r;
    }
    
}
