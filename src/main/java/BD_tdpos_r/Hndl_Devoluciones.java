package BD_tdpos_r;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Devolucion;

/**
 *Conector a la tabla de devoluciones.
 * @author esteban
 */
public class Hndl_Devoluciones extends BDHandler{
    
    
    
    /**
     * metodo que retorna todos las devoluciones asociadas con la factura 
     * identificada con el int consecutivo.
     * @param consecutivo
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Devolucion> getRecordsByFacturaId(int consecutivo) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Devolucion> ld = new ArrayList<>();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Devoluciones.SELECT_BY_FACTURA);
        
        super.pst.setInt(1, consecutivo);
        super.rs = super.pst.executeQuery();
        while(rs.next()){
            ld.add(new Devolucion(
                    super.rs.getInt(SQL_Devoluciones.COL_ID), 
                    super.rs.getInt(SQL_Devoluciones.COL_FACTU), 
                    super.rs.getInt(SQL_Devoluciones.COL_PVENTA), 
                    super.rs.getInt(SQL_Devoluciones.COL_CANTIDAD), 
                    super.rs.getDate(SQL_Devoluciones.COL_FECHA).toLocalDate(), 
                    super.rs.getTime(SQL_Devoluciones.COL_HORA).toLocalTime(),
                    super.rs.getString(SQL_Devoluciones.COL_VENDEDOR), 
                    super.rs.getString(SQL_Devoluciones.COL_PRODUCTO_COD)));
        }
        super.CerrarTodo();
        return ld;
    }
    
    
    /**
     * metodo que inserta una devolucion a la base de datos
     * @param dev 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public void InsertDevolucion(Devolucion dev) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Devoluciones.INSERT);
        
        super.pst.setInt(1, dev.consecutivo);
        super.pst.setInt(2, dev.Factura_consecutivo);
        super.pst.setInt(3, dev.pventa);
        super.pst.setInt(4, dev.cantidad);
        super.pst.setString(5, dev.Vendedor_id);
        super.pst.setString(6, dev.Producto_codigo);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
    }
    
    
    
}
