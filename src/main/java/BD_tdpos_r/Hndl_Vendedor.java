package BD_tdpos_r;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Vendedor;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptVendedor;

/**
 *
 * @author esteban
 */
public class Hndl_Vendedor extends BDHandler {
    
    
    /**
     * constructor vacio
     */
    public Hndl_Vendedor(){
        
    }
    
    
    
    /**
     * metodo que toma u String con palabras clave y hace una busqueda en la
     * BD de los vendedores que tienen un match con esas palabras clave.
     * el resultado lo retorna en un ArrayList<Vendedor>.
     * @param n
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Vendedor> buscarVendedor_xNombre(String n) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Vendedor> lv = new ArrayList<>();
        
        super.ArreglarFrase_y_PST(SQL_Vendedor.BASE_SELECT, SQL_Vendedor.COL_NOMBRE, n);
        
        super.rs = super.pst.executeQuery();
        while(rs.next()){
            lv.add(new Vendedor(rs.getString(SQL_Vendedor.COL_ID), 
                    super.rs.getString(SQL_Vendedor.COL_NOMBRE),
                    super.rs.getString(SQL_Vendedor.COL_TEL1),
                    super.rs.getString(SQL_Vendedor.COL_TEL2), 
                    super.rs.getString(SQL_Vendedor.COL_DIR),
                    super.rs.getString(SQL_Vendedor.COL_EMAIL), 
                    super.rs.getString(SQL_Vendedor.COL_DESCRI), 
                    super.rs.getDate(SQL_Vendedor.COL_NACIMIENTO).toLocalDate(), 
                    super.rs.getDate(SQL_Vendedor.COL_FREGIS).toLocalDate(),
                    super.rs.getInt(SQL_Vendedor.COL_ESTADO)));
        }
        
        super.CerrarTodo();
        return lv;
    }

    
    
    /**
     * metodo para buscar un vendedor en la base de datos 
     * usando la llave primaria
     * @param id
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ArrayList<Vendedor> getVendedorId(String id) 
            throws ClassNotFoundException, SQLException{
        ArrayList<Vendedor> lv = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Vendedor.SELECT_ID);
        super.pst.setString(1, id);
        
        super.rs = super.pst.executeQuery();
        while(rs.next()){
            lv.add(new Vendedor(id, 
                    super.rs.getString(SQL_Vendedor.COL_NOMBRE),
                    super.rs.getString(SQL_Vendedor.COL_TEL1),
                    super.rs.getString(SQL_Vendedor.COL_TEL2), 
                    super.rs.getString(SQL_Vendedor.COL_DIR),
                    super.rs.getString(SQL_Vendedor.COL_EMAIL), 
                    super.rs.getString(SQL_Vendedor.COL_DESCRI), 
                    super.rs.getDate(SQL_Vendedor.COL_NACIMIENTO).toLocalDate(), 
                    super.rs.getDate(SQL_Vendedor.COL_FREGIS).toLocalDate(),
                    super.rs.getInt(SQL_Vendedor.COL_ESTADO)));
        }
        
        super.CerrarTodo();
        return lv;
    }
    
    
    /**
     * Metodo que recibe un objeto del tipo vendedor y lo ingresa 
     * a la base de datos
     * @param v 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public void InsertVendor(Vendedor v) 
            throws SQLException, ClassNotFoundException{
        
        super.SetConnection(); // se abre la conexion
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Vendedor.INSERT);
        
        super.pst.setString(1, v.id);
        super.pst.setString(2, v.nombre);
        super.pst.setString(3, v.tel1);
        super.pst.setString(4, v.tel2);
        super.pst.setString(5, v.direccion);
        super.pst.setString(6, v.email);
        super.pst.setString(7, v.descripcion);
        super.pst.setDate(8, Date.valueOf(v.nacimiento));
        // curdate() de sql
        super.pst.setInt(9, v.estado);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
    }

    /**
     * metodo que rectorna una lista con todos los vendedores activos. 
     * se usa en la pestaña de ventas para el combo Box de vendedores
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException 
     */
    public ArrayList<SptVendedor> getVendedoresActivos() 
            throws SQLException, ClassNotFoundException {
        
        ArrayList<SptVendedor> sptlv = new ArrayList<>();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Vendedor.SELECT_ACTIVOS);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){ // Cada SptVendedor de construye con un Vendedor y se agrega a la lista
            sptlv.add(new SptVendedor(new Vendedor(super.rs.getString(SQL_Vendedor.COL_ID), 
                    super.rs.getString(SQL_Vendedor.COL_NOMBRE), 
                    super.rs.getString(SQL_Vendedor.COL_TEL1),
                    super.rs.getString(SQL_Vendedor.COL_TEL2),
                    super.rs.getString(SQL_Vendedor.COL_DIR),
                    super.rs.getString(SQL_Vendedor.COL_EMAIL),
                    super.rs.getString(SQL_Vendedor.COL_DESCRI),
                    super.rs.getDate(SQL_Vendedor.COL_NACIMIENTO).toLocalDate(),
                    super.rs.getDate(SQL_Vendedor.COL_FREGIS).toLocalDate(),
                    super.rs.getInt(SQL_Vendedor.COL_ESTADO))));
        }
        super.CerrarTodo();
        return sptlv;
    }
    
    
    /**
     * Elimina el vendedor identificado por la llave primaria id
     * @param id 
     */
    public void EliminarVendedor(String id)
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Vendedor.DELETE);
        super.pst.setString(1, id);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    /**
     * modifica el vendedor identificado con la llave primaria id y se reemplaza por los datos encapsulados en
     * el objeto vendedor.
     */
    public void UpdateVendedor(Vendedor vendedor, String old_id )
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Vendedor.UPDATE);
        super.pst.setString(1, vendedor.id);
        super.pst.setString(2, vendedor.nombre);
        super.pst.setString(3, vendedor.tel1);
        super.pst.setString(4, vendedor.tel2);
        super.pst.setString(5, vendedor.direccion);
        super.pst.setString(6, vendedor.email);
        super.pst.setString(7, vendedor.descripcion);
        super.pst.setDate(8, Date.valueOf(vendedor.nacimiento));
        super.pst.setInt(9, vendedor.estado);
        super.pst.setString(10, old_id);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
}
