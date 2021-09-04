package BD_tdpos_r;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import tentactildesktoppos.negocio_r_objs.Cupon;

/**
 *
 * @author esteban
 */
public class Hndl_Cupon extends BDHandler{
    
    
    /**
     * metodo que trae un cupon por su llave primaria
     * @param id
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public ArrayList<Cupon> getCupnBy_Id(int id) 
            throws ClassNotFoundException, SQLException{
        ArrayList<Cupon> ls_cupon = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareCall(SQL_Cupon.SELECT_ID);
        super.pst.setInt(1, id);
        
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            ls_cupon.add(new Cupon(
                    super.rs.getInt(SQL_Cupon.COL_ID),
                    super.rs.getString(SQL_Cupon.COL_CLIENTE),
                    super.rs.getInt(SQL_Cupon.COL_VALOR),
                    super.rs.getDate(SQL_Cupon.COL_FECHA).toLocalDate(),
                    super.rs.getInt(SQL_Cupon.COL_ESTADO),
                    super.rs.getInt(SQL_Cupon.COL_PASS),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_GEN),
                    super.rs.getString(SQL_Cupon.COL_VENDEDOR),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_SPEND)));
        }
        return ls_cupon;
    }
    
    
    /**
     * metodo que recibe los datos basicos para generar un cupon devolucion.
     * cuando se hace una devolucion de una prenda de cierta factura,
     * se lleva a cabo una reduccion del saldo a pagar. Sin embargo si se hace 
     * una devolucion relacionada con una factura que ya esta paga la forma de
     * hacerle la devolucion es mediante la generacion de un cupon.
     * En este caso habria un numero de factura asociado, que seria la factura generadora del cupon.
     * No confundir con la factura en la que se gasta el cupon.
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public Cupon InsertarCuponDevolucion(String Cliente_id, int valor, int Factura_consecutivo_gen, String Vendedor_id ) throws 
            ClassNotFoundException, SQLException{
        
        // debe recordarse que al consultar getNextId se hace una operacion de base de datos
        // por lo que link pst y rs se cierran al final del metodo.
        // por tanto este metodo no se puede invocar despues de super.SetConnection() porque cerraria
        // la conexion recien abierta.
        int next_id = getNextId();
        
        super.SetConnection();
        super.pst = (PreparedStatement) this.link.prepareStatement(SQL_Cupon.INSERT);
        super.pst.setInt(1, next_id); // se determina el id que se debe usar para el cupon.
        super.pst.setString(2, Cliente_id);
        super.pst.setInt(3, valor); // valor en pesos del cupon.
        super.pst.setInt(4, 1); // se inserta como activo por defecto
        int clave = this.generarClaveAleatoria();  // se genera clave 
        super.pst.setInt(5, clave);
        super.pst.setInt(6, Factura_consecutivo_gen);
        super.pst.setString(7, Vendedor_id);
        super.pst.setInt(8, 0); // el 0 es un numero reservado para la tabla de facturas. Corresponde al elemento nulo 
        // del conjunto de facturas sin usar propiamente 'null' pues su uso puede desembocar en errores
        // y codigo mas complejo y menos robusto.
        
        super.pst.executeUpdate(); // se inserta en la tabla de cupones.
        super.CerrarUpdate();
        // se retorna una instancia del cupon que se guardo en la base de datos para la impresion de comprobante de cupon.
        Cupon r = new Cupon(next_id, Cliente_id, valor, LocalDate.now(), 1, clave, Factura_consecutivo_gen, Vendedor_id, 0);
        return r;
    }
    
    
    /**
     * metodo que genera una clave aleatoria de 4 digitos.
     * es decir que el numero aleatorio generado estara desde 1000
     * hasta 9999.
     * @return 
     */
    public int generarClaveAleatoria(){
        boolean continuar=true;
        int r = 0;
        while(continuar){ // se genera un nuemro aleatorio entre 0 y 1 y se 
            //multiplica por 10000 para intentar obtener un numero de 4 digitos
            r = (int) Math.ceil(10000*Math.random());
            if(1000<=r && r<=9999 ) continuar = false; // como puede tener menos digitos
            // se verifica y s itera hasta que el numero este dentro del rango deseado
        }
        return r;
    }
    
    
    /**
     * Cuando la generacion del cupon no se hace por motivo de una devolucion
     * directamente. por ahora voy a probar sin generar este tipo de cupones
     * @param c 
     */
    public void InsertarCupon_noFactura(Cupon c){
        
    }
    
    
    /**
     * metodo que retorna el numero de identificacion que debe
     * usarse para la insercion de un nuevo cupon de forma que todos quenden
     * numerados de forma consecutiva
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public int getNextId() 
            throws ClassNotFoundException, SQLException{
        
        int next_id = 0;
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Cupon.SELECT_MAX_ID);
        super.rs = super.pst.executeQuery();
        
        while(rs.next()){
            next_id = super.rs.getInt(SQL_Cupon.COL_MAX_ID);
        }
        next_id++;
        super.CerrarTodo();
        
        // en caso de que este vacio no entrara al while y por tanto next_id
        // se mantendra en 0, que seria el id a usar cuando la tabla esta vacia.
        return next_id;
    }
    
    
    /**
     * metodo que se invoca cuando un cupon se ha usado para una compra,
     * entonces este se marca en su estado como inactivo.
     * @param id 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public void DesactivarCupon(Cupon cup, int consecutivo) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Cupon.DISABLE_BY_ID);
        super.pst.setInt(1, 0); // se pone estado en 0, disabled cupon.
        super.pst.setInt(2, consecutivo);
        super.pst.setInt(3, cup.id);
        super.pst.executeUpdate();
        super.CerrarUpdate();
        
        
    }
    
    
    
    /**
     * Metodo que trae todos los cupones gastados en la factura
     * identificada por el int consecutivo.
     * @param consecutivo
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Cupon> getCuponByFacturaIdSpend(int consecutivo) 
            throws ClassNotFoundException, SQLException{
        ArrayList<Cupon> r = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Cupon.SELECT_BY_FACTURA_SPEND);
        super.pst.setInt(1, consecutivo);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r.add(new Cupon(
                    super.rs.getInt(SQL_Cupon.COL_ID),
                    super.rs.getString(SQL_Cupon.COL_CLIENTE),
                    super.rs.getInt(SQL_Cupon.COL_VALOR),
                    super.rs.getDate(SQL_Cupon.COL_FECHA).toLocalDate(),
                    super.rs.getInt(SQL_Cupon.COL_ESTADO),
                    super.rs.getInt(SQL_Cupon.COL_PASS),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_GEN),
                    super.rs.getString(SQL_Cupon.COL_VENDEDOR),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_SPEND)));
        }
        super.CerrarTodo();
        return r;
    }
    
    
    
    
    /**
     * Metodo que trae todos los cupones generados en la factura
     * identificada por el int consecutivo.
     * @param consecutivo
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Cupon> getCuponByFacturaIdGen(int consecutivo) 
            throws ClassNotFoundException, SQLException{
        ArrayList<Cupon> r = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Cupon.SELECT_BY_FACTURA_GEN);
        super.pst.setInt(1, consecutivo);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){
            r.add(new Cupon(
                    super.rs.getInt(SQL_Cupon.COL_ID),
                    super.rs.getString(SQL_Cupon.COL_CLIENTE),
                    super.rs.getInt(SQL_Cupon.COL_VALOR),
                    super.rs.getDate(SQL_Cupon.COL_FECHA).toLocalDate(),
                    super.rs.getInt(SQL_Cupon.COL_ESTADO),
                    super.rs.getInt(SQL_Cupon.COL_PASS),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_GEN),
                    super.rs.getString(SQL_Cupon.COL_VENDEDOR),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_SPEND)));
        }
        super.CerrarTodo();
        return r;
    }
    
    
    public ArrayList<Cupon> getCuponesActivosDelCliente(String Cli_id) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Cupon> r = new ArrayList<>();
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Cupon.SELECT_BY_ACTIVE_CLIENTS);
        super.pst.setString(1, Cli_id);
        super.rs = super.pst.executeQuery();
        
        while(super.rs.next()){
            r.add(new Cupon(
                    super.rs.getInt(SQL_Cupon.COL_ID),
                    super.rs.getString(SQL_Cupon.COL_CLIENTE),
                    super.rs.getInt(SQL_Cupon.COL_VALOR),
                    super.rs.getDate(SQL_Cupon.COL_FECHA).toLocalDate(),
                    super.rs.getInt(SQL_Cupon.COL_ESTADO),
                    super.rs.getInt(SQL_Cupon.COL_PASS),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_GEN),
                    super.rs.getString(SQL_Cupon.COL_VENDEDOR),
                    super.rs.getInt(SQL_Cupon.COL_FACTURA_SPEND)));
        }
        
        return r;
    }
    
}
