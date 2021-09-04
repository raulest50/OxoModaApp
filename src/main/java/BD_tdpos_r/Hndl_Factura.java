package BD_tdpos_r;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.Factura;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;

/**
 *Clase para hacer conexion a la tabla de 
 * @author esteban
 */
public class Hndl_Factura extends BDHandler{
    
    
    /**
     * metodo que trae la factura identificada con el int consecutivo, argumento
     * de entrada de esta funcion.
     * @param consecutivo
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ArrayList<Factura> getFacturaBy_ID(int consecutivo) 
            throws ClassNotFoundException, SQLException{
        ArrayList<Factura> fac = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_ID);
        
        super.pst.setInt(1, consecutivo);
        super.rs = super.pst.executeQuery();
        
        while(super.rs.next()){
            fac.add(new Factura(super.rs.getInt(SQL_Factura.COL_CONSEC), 
                    super.rs.getInt(SQL_Factura.COL_SALDO), 
                    super.rs.getString(SQL_Factura.COL_CLIENTE), 
                    super.rs.getString(SQL_Factura.COL_VENDEDOR),
                    super.rs.getDate(SQL_Factura.COL_FECHA).toLocalDate(), 
                    super.rs.getTime(SQL_Factura.COL_HORA).toLocalTime(), 
                    super.rs.getString(SQL_Factura.COL_FORMA_PAGO), 
                    super.rs.getInt(SQL_Factura.COL_INTERV_PAGO)));
        }
        
        super.CerrarTodo();
        return fac;
    }
    
    
    /**
     * metodo que registra un objeto tipo factura a la base de datos.
     * @param f
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void InsertarFactura(Factura f) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareCall(SQL_Factura.INSERT);
        
        super.pst.setInt(1, f.consecutivo);
        super.pst.setInt(2, f.saldo);
        super.pst.setString(3, f.Cliente_id);
        super.pst.setString(4, f.Vendedor_id);
        super.pst.setString(5, f.forma_pago);
        super.pst.setInt(6, f.intervalo_pago);
        
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
    /**
     * metodo que trae el numero mas alto de consecutivo en la tabla de
     * consecutivos.este valor es autoincremental pero ya que es necesario
     * guardar este valor en las facturas record y en otros elementos es necesario
     * conocer desde la capa  de logica el consecutivo a asignar.Por este motivo
     * usar la funcion autoincremento de la capa de base de datos no es una
     * opcion.
     * @return
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public int getNextConsecutiv()
            throws ClassNotFoundException, SQLException{
        int r = 0;
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.MAX_CONSEC);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Factura.COL_MAX) + 1; // el siguiente numero
        }
        super.CerrarTodo();
        return r;
    }

    /**
     * creado para permitir modificacion de un record de la tabla de facturas
     * pero aun no ha sido necesario implementarlo.
     * @param aThis 
     */
    public void Update(Factura aThis) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    /**
     * metodo que permite alterar el saldo de una factura.si se desea reducir el saldo entonces el argumento del metodo
     * debe ser un numero negativo
     * @param saldo_mod
     * @param consecutivo
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void Add2Saldo(int saldo_mod, int consecutivo) 
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.MOD_SALDO_FACTURA);
        super.pst.setInt(1, saldo_mod);
        super.pst.setInt(2, consecutivo);
        
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
    /**
     * para traer de la base de datos todas las faturas que correspondan al cliente identificado
     * por su respectiva llave primaria.
     * @param client_id
     * @return 
     */
    public ObservableList<SptFactura> getFacturasByClienteID(String client_id) 
            throws SQLException, ClassNotFoundException{
        ObservableList lsfac = FXCollections.observableArrayList();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_CLIENTE);
        
        super.pst.setString(1, client_id);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){ // fetching each results
            int id = rs.getInt(SQL_Factura.COL_CONSEC);
            int saldo = rs.getInt(SQL_Factura.COL_SALDO);
            String Cliente_id = rs.getString(SQL_Factura.COL_CLIENTE);
            LocalDate fecha = rs.getDate(SQL_Factura.COL_FECHA).toLocalDate();
            LocalTime hora = rs.getTime(SQL_Factura.COL_HORA).toLocalTime();
            String Vendedor_id = rs.getString(SQL_Factura.COL_VENDEDOR);
            String Pago = rs.getString(SQL_Factura.COL_FORMA_PAGO);
            int interv_pago = rs.getInt(SQL_Factura.COL_INTERV_PAGO);
            
            // se construye el objeto factura de este fectch
            Factura fa = new Factura(id, saldo, Cliente_id, Vendedor_id, fecha, hora, Pago, interv_pago);
            // se agrega a la lista de resultados.
            lsfac.add(new SptFactura(fa));
        }
        return lsfac;
    }
    
    
    /**
     * metodo que trae todas las facturas de un cliente dado pero con saldo mayor que cero.
     * @param client_id
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public ObservableList<SptFactura> getFacturasByClienteID_saldo_no_zero(String client_id) 
            throws SQLException, ClassNotFoundException{
        ObservableList lsfac = FXCollections.observableArrayList();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_CLIENTE_SALDO_NO_ZERO);
        
        super.pst.setString(1, client_id);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){ // fetching each results
            int id = rs.getInt(SQL_Factura.COL_CONSEC);
            int saldo = rs.getInt(SQL_Factura.COL_SALDO);
            String Cliente_id = rs.getString(SQL_Factura.COL_CLIENTE);
            LocalDate fecha = rs.getDate(SQL_Factura.COL_FECHA).toLocalDate();
            LocalTime hora = rs.getTime(SQL_Factura.COL_HORA).toLocalTime();
            String Vendedor_id = rs.getString(SQL_Factura.COL_VENDEDOR);
            String Pago = rs.getString(SQL_Factura.COL_FORMA_PAGO);
            int interv_pago = rs.getInt(SQL_Factura.COL_INTERV_PAGO);
            
            // se construye el objeto factura de este fectch
            Factura fa = new Factura(id, saldo, Cliente_id, Vendedor_id, fecha, hora, Pago, interv_pago);
            // se agrega a la lista de resultados.
            lsfac.add(new SptFactura(fa));
        }
        return lsfac;
    }
    
    
    
    /**
     * para traer de la bd todas las facturas cuyo nombre de cliente hagan match total o parcial 
     * de las palabras claves especificadas en el String de argumento.
     * @param client_id
     * @return 
     */
    public ObservableList<SptFactura> getFacturasByClienteName(String client_id){
        ObservableList lsfac = FXCollections.observableArrayList();
        
        return lsfac;
    }
    
    /**
     * Trae de la tabla de facturas todos los records cuyo saldo sea mayor al 
     * @param valor
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ObservableList<SptFactura> getFacturas_Saldo_MayorQ(int valor)
            throws ClassNotFoundException, SQLException{
        
        ObservableList<SptFactura> lsf = FXCollections.observableArrayList();
        
        super.SetConnection(); // Se establece una conexion
        
        /**
         * en caso que el usuario digite un valor sin sentido como un String o un entero negativo
         * entonces se establece valor en -1 par indicar a este metodo que debe retornar una lista
         * vacia.
         */
        if( !(valor == -1) ){  // si el valor indicado por el usuario tiene sentido entonces se procede a hacer el query.
            super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_SALDO_MAYOR);
            super.pst.setInt(1, valor);
            
            super.rs = super.pst.executeQuery();
            
            while(super.rs.next()){
                int id = rs.getInt(SQL_Factura.COL_CONSEC);
                int saldo = rs.getInt(SQL_Factura.COL_SALDO);
                String Cliente_id = rs.getString(SQL_Factura.COL_CLIENTE);
                LocalDate fecha = rs.getDate(SQL_Factura.COL_FECHA).toLocalDate();
                LocalTime hora = rs.getTime(SQL_Factura.COL_HORA).toLocalTime();
                String Vendedor_id = rs.getString(SQL_Factura.COL_VENDEDOR);
                String Pago = rs.getString(SQL_Factura.COL_FORMA_PAGO);
                int interv_pago = rs.getInt(SQL_Factura.COL_INTERV_PAGO);
                
                // se construye el objeto factura de este fectch
                Factura fa = new Factura(id, saldo, Cliente_id, Vendedor_id, fecha, hora, Pago, interv_pago);
                // se agrega a la lista de resultados.
                lsf.add(new SptFactura(fa));
            }
        }
        return lsf;
    }
    
    
    /**
     * Trae de la tabla de facturas todos los records cuyo saldo sea mayor al 
     * @param valor
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ObservableList<SptFactura> getFacturas_Saldo_MenorQ(int valor)
            throws ClassNotFoundException, SQLException{
        
        ObservableList<SptFactura> lsf = FXCollections.observableArrayList();
        
        super.SetConnection(); // Se establece una conexion
        
        /**
         * en caso que el usuario digite un valor sin sentido como un String o un entero negativo
         * entonces se establece valor en -1 par indicar a este metodo que debe retornar una lista
         * vacia.
         */
        if( !(valor == -1) ){  // si el valor indicado por el usuario tiene sentido entonces se procede a hacer el query.
            super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_SALDO_MENOR);
            super.pst.setInt(1, valor);
            
            super.rs = super.pst.executeQuery();
            
            while(super.rs.next()){
                int id = rs.getInt(SQL_Factura.COL_CONSEC);
                int saldo = rs.getInt(SQL_Factura.COL_SALDO);
                String Cliente_id = rs.getString(SQL_Factura.COL_CLIENTE);
                LocalDate fecha = rs.getDate(SQL_Factura.COL_FECHA).toLocalDate();
                LocalTime hora = rs.getTime(SQL_Factura.COL_HORA).toLocalTime();
                String Vendedor_id = rs.getString(SQL_Factura.COL_VENDEDOR);
                String Pago = rs.getString(SQL_Factura.COL_FORMA_PAGO);
                int interv_pago = rs.getInt(SQL_Factura.COL_INTERV_PAGO);
                
                // se construye el objeto factura de este fectch
                Factura fa = new Factura(id, saldo, Cliente_id, Vendedor_id, fecha, hora, Pago, interv_pago);
                // se agrega a la lista de resultados.
                lsf.add(new SptFactura(fa));
            }
        }
        return lsf;
    }
    
    
    /**
     * Metodo que retorna la suma de todos los saldos de las ffaturas pendientes
     * en la tabla de facturas. la suma es ejecutada por el motor de base de
     * datos sql.
     * @return 
     */
    public int GetCartera() 
            throws ClassNotFoundException, SQLException{
       int r = 0;
       super.SetConnection();
       super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_CARTERA);
       super.rs = super.pst.executeQuery();
       while(super.rs.next()){
           r = super.rs.getInt(SQL_Factura.COL_SUMA);
       }
       super.CerrarTodo();
       return r;
    }
    
    
    
    /**
     * metodo que calcula el capital equivalente de los productos
     * en stock.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int GetStock2Money()
            throws ClassNotFoundException, SQLException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_STOCK2MONEY);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Factura.COL_SUMA);
        }
        super.CerrarTodo();
        return r;
    }
    
    
    /**
     * entrega los pagos hechos entre las fecha f1 y f2,
     * se suman abonos y facturas pagadas de contado por igual.
     * @param f1
     * @param f2
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int GetPagoInDt(LocalDate f1, LocalDate f2)
        throws ClassNotFoundException, SQLException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Factura.SELECT_PAGOS_IN_DT);
        super.pst.setString(1, f1.toString());// se ponen las fechas en el query.
        super.pst.setString(2, f2.toString());
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r = super.rs.getInt(SQL_Factura.COL_SUMA);
        }
        super.CerrarTodo();
        return r;
    }
    
}
