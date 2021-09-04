package BD_tdpos_r;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.ProductoRecord;

/**
 *Clase para hacer conexion a la tabla de productos.
 * @author esteban
 */
public class Hndl_ProductoRecord extends BDHandler{
    
    
    /**
     * empty constructor
     */
    public Hndl_ProductoRecord(){
        
    }
    
    
    /**
     * metodo que trae todos los items de una factura usando la llave primaria
     * de la tabla de facturas, que a su vez es la llave foranea de la tabla  
     * de Productos records
     * @param Factura_consecutivo
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ArrayList<ProductoRecord> getRecordsByFacturaId(int Factura_consecutivo) 
            throws ClassNotFoundException, SQLException{
        ArrayList<ProductoRecord> lpr = new ArrayList<>();
        
        super.SetConnection();
        
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_ProductoRecord.SELECT_BY_FACTURA);
        super.pst.setInt(1, Factura_consecutivo);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            lpr.add(new ProductoRecord(
                    super.rs.getInt(SQL_ProductoRecord.COL_ID),
                    super.rs.getInt(SQL_ProductoRecord.COL_FAC_CONSEC),
                    super.rs.getInt(SQL_ProductoRecord.COL_CANTIDAD),
                    super.rs.getInt(SQL_ProductoRecord.COL_COSTO),
                    super.rs.getInt(SQL_ProductoRecord.COL_PVENTA),
                    super.rs.getString(SQL_ProductoRecord.COL_PRODUCTO_COD)
            ));
        }
        super.CerrarTodo();
        return lpr;
    }
    
    
    /**
     * metodo que permite ingresar una factura record de una factura
     * @param pr
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void InsertProductoRecord(ProductoRecord pr) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_ProductoRecord.INSERT);
        
        super.pst.setInt(1, pr.id);
        super.pst.setInt(2, pr.Factura_consecutivo);
        super.pst.setInt(3, pr.cantidad);
        super.pst.setInt(4, pr.costo);
        super.pst.setInt(5, pr.pventa);
        super.pst.setString(6, pr.Producto_codigo);
        
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
}
