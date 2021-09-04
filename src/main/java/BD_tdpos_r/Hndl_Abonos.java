package BD_tdpos_r;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Abono;

/**
 *Conector para la tabla de abonos.
 * @author esteban
 */
public class Hndl_Abonos extends BDHandler {
    
    
    /**
     * metodo que
     * @param a
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void IsertarAbono(Abono a) 
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Abonos.INSERT_ABONO);
        
        super.pst.setInt(1, 0); // se pone en 0 ya que  es autoicremental
        super.pst.setInt(2, a.valor);
        super.pst.setString(3, a.Cliente_id);
        super.pst.setInt(4, a.Factura_consecutivo);
        super.pst.setString(5, a.Vendedor_id);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
        
    }
    
    
    /**
     * metodo que trae de la tabla de abonos todos los records tales que
     * tengan el numero de factura asociado al int consecutivo.
     * @param consecutivo
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ArrayList<Abono> getRecordsByFacturaId(int consecutivo) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Abono> lab = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Abonos.SELECT_BY_FACTURA);
        super.pst.setInt(1, consecutivo); // se pone el id de la factura en el SQL query.
        
        super.rs = super.pst.executeQuery();
        
        while(super.rs.next()){
            lab.add(new Abono(
                    super.rs.getInt(SQL_Abonos.COL_ID),
                    super.rs.getInt(SQL_Abonos.COL_VALOR),
                    super.rs.getDate(SQL_Abonos.COL_FECHA).toLocalDate(),
                    super.rs.getTime(SQL_Abonos.COL_HORA).toLocalTime(),
                    super.rs.getString(SQL_Abonos.COL_CLIENTE_ID),
                    super.rs.getInt(SQL_Abonos.COL_FACTURA_CONSEC),
                    super.rs.getString(SQL_Abonos.COL_VENDEDOR_ID)));
        }
        super.CerrarTodo();
        return lab;
    }
    
    
    /**
     *  retorna una lista que solo puede ser dimension 1 o 0. 0 si la facutura especificada no tiene ningun
     * abono. 1 en caso de que hayan 1 o mas abonos. en este ultimo caso retornara el abono mas reciente.
     * @param Factura_id
     * @return 
     */
    public ArrayList<LocalDate> getLastAbonoFromFactura(int Factura_id)
            throws ClassNotFoundException, SQLException{
        ArrayList<LocalDate> ls_abo = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Abonos.SELECT_LAST_ABONO_FROM_FACTURA);
        
        super.pst.setInt(1, Factura_id);
        super.rs = super.pst.executeQuery();
        
        while(super.rs.next()){
            Date da = rs.getDate("maxi");
            if(!(da==null)) ls_abo.add(da.toLocalDate());
        }
        
        super.CerrarTodo();
        
        return ls_abo;
    }
    
    
    /**
     * trae de la tabla de abonos el record mas reciente que corresponda con el cliente
     * especificado en el aegumento de esta funcion.
     * 
     * @param Cliente
     * @return 
     */
    public String getLastAbonoFromCliente(String Cliente_id)
            throws ClassNotFoundException, SQLException{
        String r = "";
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Abonos.SELECT_LAST_ABONO_BY_CLIENT);
        // se pone el cc del cliente en el query.
        super.pst.setString(1, Cliente_id);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getString(SQL_Abonos.COL_AS_MAX);
        }
        super.CerrarTodo();
        return r;
    }
    
    
    
}
