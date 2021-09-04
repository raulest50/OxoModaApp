package tentactildesktoppos.negocio_r_objs;

import BD_tdpos_r.Hndl_Abonos;
import BD_tdpos_r.Hndl_Clientes;
import BD_tdpos_r.Hndl_Cupon;
import BD_tdpos_r.Hndl_Devoluciones;
import BD_tdpos_r.Hndl_Factura;
import BD_tdpos_r.Hndl_ProductoRecord;
import BD_tdpos_r.Hndl_Vendedor;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static java.util.stream.Collectors.groupingBy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;

/**
 *
 * @author esteban
 */
public class Factura {
    
    
    /**
     * llave primaria. entero autoincremental
     */
    public int consecutivo;
    
    /**
     * cantidad de dinero pendiente por cobrar de la factura.
     */
    public int saldo;
    
    
    /**
     * cliente al que se genera la factura
     */
    public String Cliente_id;
    
    
    /**
     * vendedor que realizo la emision de factura
     */
    public String Vendedor_id;
    
    
    
    /**
     * fecha en la que se genero la factura
     */
    public LocalDate fecha;
    
    
    /**
     * hora a la que se genero la factura
     */
    public LocalTime hora;
    
    
    /**
     * puede ser contado o credito
     */
    public String forma_pago;
    
    /**
     * intervalo de pago puede ser quincenal o mensual.
     */
    public int intervalo_pago;
    
    
    /**
     * tipo de venta de contado, se usa para el comboBox y para if's
     */
    public static final String TIPOVENTA_CONTADO = "Contado";
    
    /**
     * tipo de venta de credito, se usa para el comboBox y para if's
     */
    public static final String TIPOVENTA_CREDITO = "Credito";

    
    /**
     * constructor vacio.
     */
    public Factura(){
        
    }
    
    
    
    /**
     * constructor para select, se asume que  las fechas ya estan determinadas
     * @param consecutivo
     * @param saldo
     * @param Cliente_id
     * @param Vendedor_id
     * @param fecha
     * @param hora
     * @param forma_pago
     * @param intervalo_pago 
     */
    public Factura(int consecutivo, int saldo, String Cliente_id, String Vendedor_id, LocalDate fecha, LocalTime hora, String forma_pago, int intervalo_pago) {
        this.consecutivo = consecutivo;
        this.saldo = saldo;
        this.Cliente_id = Cliente_id;
        this.Vendedor_id = Vendedor_id;
        this.fecha = fecha;
        this.hora = hora;
        this.forma_pago = forma_pago;
        this.intervalo_pago = intervalo_pago;
    }
    
    
    /**
     * constructor para insert.ya que la fecha y la hora se inserta en nivel de base de datos,
     * ambos atributos solo son conocidos a partir del momento en que se
     * inserta el valor en la tabla.
     * a pesar de esto resulta util
     * encapsular los atributos restantes para que sea mas compacto pasar estos
     * datos al metodo handler que inserta la factura en la BD. Este  tipo de
     * constructor omite las variables de fecha y hora de ingreso por lo
     * que al usar este constructor no se debe hacer referencia a estas variables
     * ya que son nulas.
     * @param consecutivo
     * @param saldo
     * @param Cliente_id
     * @param Vendedor_id
     * @param forma_pago
     * @param intervalo_pago 
     */
    public Factura(int consecutivo, int saldo, String Cliente_id, String Vendedor_id ,String forma_pago, int intervalo_pago) {
        this.consecutivo = consecutivo;
        this.saldo = saldo;
        this.Cliente_id = Cliente_id;
        this.Vendedor_id = Vendedor_id;
        this.forma_pago = forma_pago;
        this.intervalo_pago = intervalo_pago;
    }
    
    
    /**
     * Metodo que retorna una representacion String de este obejto factura
     * pero en una manera apropiada para el TextArea de l seccionde abonos
     * y deviluciones.
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public String representacion_str() 
            throws ClassNotFoundException, SQLException{
        String r = ""; //respuesta que se construye de manera incremental.
        
        Hndl_Clientes hndc = new Hndl_Clientes(); // conector a la tabla de clietnes
        String cliente_nombre = hndc.buscarCliente_id(this.Cliente_id).get(0).nombre;
        
        Hndl_Vendedor hndv = new Hndl_Vendedor();
        String vendedor_nombre = hndv.getVendedorId(this.Vendedor_id).get(0).nombre;
        
        r = "Cliente Nombre: " + cliente_nombre + "\n";
        r += "Cliente cc o nit: " + this.Cliente_id + "\n";
        r += "Forma de pago: " + this.forma_pago + "\n";
        r += "Saldo: " + Integer.toString(this.saldo) + "\n";
        r += "Fecha: " + this.fecha.toString() + "\n";
        r += "Hora: " + this.hora.toString() + "\n";
        r += "Nombre Vendedor: " + vendedor_nombre + "\n";
        r += "cc. Vendedor: " + this.Vendedor_id + "\n";
        
        return r;
    }
    
    
    
    /**
     * metodo que entrega todos los records de esta factura.
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ArrayList<SptFacturaRecord> getFacturaRecords()
            throws ClassNotFoundException, SQLException{
        
        // lista auxiliar para guardar todos las factura record de la respectiva factura
        ArrayList<SptFacturaRecord> lact = new ArrayList<>();
        
        // se agrega la lista de producto records
        Hndl_ProductoRecord hndpr = new Hndl_ProductoRecord();
        ArrayList<ProductoRecord> lpr = hndpr.getRecordsByFacturaId(this.consecutivo);
        for (ProductoRecord pr : lpr){
            lact.add(new SptFacturaRecord(pr));
        }
        
        // se agregan las devoluciones
        Hndl_Devoluciones hndd = new Hndl_Devoluciones();
        ArrayList<Devolucion> ld = hndd.getRecordsByFacturaId(this.consecutivo);
        for(Devolucion devo: ld){
            lact.add(new SptFacturaRecord(devo));
        }
        
        // se agregan todos los abonos records
        Hndl_Abonos hndabo = new Hndl_Abonos();
        ArrayList<Abono> labo = hndabo.getRecordsByFacturaId(this.consecutivo);
        for(Abono abo : labo){
            lact.add(new SptFacturaRecord(abo));
        }
        
        // se agregan los cupones tipo spend
        Hndl_Cupon hndcu = new Hndl_Cupon();
        ArrayList<Cupon> lcup = hndcu.getCuponByFacturaIdSpend(this.consecutivo);
        for(Cupon cu : lcup){
            lact.add(new SptFacturaRecord(cu));
        }
        
        
        // se agregan los cupones tipo gen
        ArrayList<Cupon> lcup_gen = hndcu.getCuponByFacturaIdGen(this.consecutivo);
        for(Cupon cu : lcup_gen){
            lact.add(new SptFacturaRecord(cu));
        } // estos ultimos no se deben tener en cuenta en calculos relacionados con
        // el saldo de la factura.
        
        return lact;
    }
    
    
    /**
     * metodo que revisa si un producto pertenece a la factura
     * para hacer validacion de un inicio de devolucion de prenda.
     * @param codigo
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public boolean ContieneProducto(String codigo) 
            throws ClassNotFoundException, SQLException{
        boolean r = false;
        Hndl_ProductoRecord hndpr = new Hndl_ProductoRecord();
        ArrayList<ProductoRecord> lpr = hndpr.getRecordsByFacturaId(this.consecutivo);
        if(lpr.isEmpty()){
            r=false;
        } else{
            for(ProductoRecord pr : lpr){
                if(pr.Producto_codigo.equals(codigo)) r = true;
            }
        }
        return r;
    }
    
    
    /**
     * trae todos los records de la base de daos y reconstruye el saldo a partir 
     * de ellos.
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException 
     */
    public int ReconstruirSaldo() throws SQLException, ClassNotFoundException{
        int sl=0;
        ArrayList<SptFacturaRecord> lfr = this.getFacturaRecords();
        
        for(SptFacturaRecord fr : lfr){
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                sl += fr.getSpt_subtotal();
            }
            
            // si es una devolucion o un abono se resta
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD) || 
                    fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                sl += fr.getSpt_subtotal();
            }
            
            // si es un cupon, solo se resta si este cupon no es tipo gen.
            // es decir que esta instancia de factura no sea la genradora.
            if(fr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){ 
                if(!(fr.cupon.Factura_consecutivo_gen == this.consecutivo) ){
                    sl -= fr.getSpt_subtotal(); // abono, devolucion y cupon.
                }
            }
        }
        return saldo;
    }
    
    /**
     * se emplea para registrar records de la tabla de operaciones en la
     * la base de datos cuado .Se espera que a este metodo solo ingresen
 factura records del tipo Abono o Devolucion.
     * @param li 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public void InsertRecords(ArrayList<SptFacturaRecord> li) 
            throws SQLException, ClassNotFoundException{
        
        for(SptFacturaRecord fr : li){
            // se insertan ingresos de efectivo (abonos)
            if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                Hndl_Abonos hndb = new Hndl_Abonos();
                hndb.IsertarAbono(fr.abono);
            }
            // se insertan las devoluciones en la base de datos
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                Hndl_Devoluciones hndd = new Hndl_Devoluciones();
                hndd.InsertDevolucion(fr.devolucion);
            }
            
            // despues de insertados los records en la BD se hace recalculo del saldo
            this.saldo = ReconstruirSaldo();
            
        }
    }
    
    
    /**
     * cuando se hace devolucion de prendas de una factura ya cancelada
     * se genera un cupon. En el caso de una factura cancelada no se aceptan
     * abonos. Como consecuencia esta operacion no altera el saldo de la 
     * factura.
     * @param li
     * @param valor 
     */
    public void GenCupon(ArrayList<SptFacturaRecord> li, int valor){
        
    }
    
    
    
    /**
     * metodo que retorna el ingreso total de efectivo de esta instancia de factura.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public int getIngresoEfectivo()
            throws ClassNotFoundException, SQLException{
        int sum = 0; // si no hay ningun abono entonces sum se retorna en 0.
        ArrayList<SptFacturaRecord> lfr = new ArrayList<>();
        
        lfr = this.getFacturaRecords();
        for(SptFacturaRecord fr : lfr){
            if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                sum+= fr.abono.valor; // se suman los valores de todos los abonos 
            } // o ingresos de efectivo de la facutura
            if(fr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
                // solo se tienen en cuenta los cupones tipo gen, que descuentan el ingreso de efectivo
                if(fr.cupon.Factura_consecutivo_gen == this.consecutivo){ // de la factura.
                    sum -= fr.cupon.valor; // se resta del ingreso de efectivo.
                }
            }
        }
        return sum;
    }
    
    
    /**
     * metodo que hace un UPDATE query en la base de datos con los aatributos
     * de esta instancia de factura.Se usa principalmente para actualizar el saldo
     * de la factura.
     */
    public void UpdateItself(){
        Hndl_Factura hndf = new Hndl_Factura();
        hndf.Update(this);
    }
    
    
    /**
     * retorna una instancia del cliente relaciona a esta factura.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public Cliente getCliente() 
            throws ClassNotFoundException, SQLException{
        Hndl_Clientes hndcli = new Hndl_Clientes();
        return hndcli.buscarCliente_id(Cliente_id).get(0);
    }
    
    
    /**
     * metodo que identifica la fecha del ultimo abono de esta factura.
     * @return 
     */
    public LocalDate getLastAbonoFecha()
            throws ClassNotFoundException, SQLException{
        LocalDate lc = LocalDate.EPOCH; // se asigna este valor como equivalente a fecha nula.
        
        // si acontinuacion no se logra identificar una fecha de abono debido a que la factura no tiene ninguno
        // entonces se retorna LocalDate.EPOCH como equivalente a fecha nula.
        Hndl_Abonos hndabo = new Hndl_Abonos();
        
        ArrayList<LocalDate> ls_lc = hndabo.getLastAbonoFromFactura(this.consecutivo);
        if(!ls_lc.isEmpty()){ // se revisa que exista un ultimo abono, que podria ser el unico
            lc = ls_lc.get(0);
        }
        
        return lc;
    }
    
    
    /**
     * Trae todos los records de esta factura pero sitetizada.
     * la sintesis se hace de la misma forma como se implemento en la seccion de abonos y devoluciones.
     * 
     * se hizo copy paste y adaptacion para evitar errores. se dejo la copia del metodo en el controlador 
     * de abonos y devoluciones a pesar de que se hace redundante al crearse este metodo.
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ObservableList getFrecordsSIntetizados() 
            throws ClassNotFoundException, SQLException{
        // lista en la que se guardan los nuevos records ya unificados.
        // se trata de la lista de salida de esta funcion o lo que se busca producir en este metodo
        ObservableList<SptFacturaRecord> fre = FXCollections.observableArrayList(new ArrayList<>());
        
        ArrayList<SptFacturaRecord> AllfacturaRecords = this.getFacturaRecords();
        
        // array list para sacara una copia de los productos
        ArrayList<SptFacturaRecord> lprods = new ArrayList<>();
        
        int sum_abono = 0;
        SptFacturaRecord fare = new SptFacturaRecord();
        for(SptFacturaRecord sfr : AllfacturaRecords){ // se acumulan todos los valores de los abonos
            if(sfr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                sum_abono += sfr.abono.valor;
                fare = sfr; //  ineficiente pero permite obtener una copia que permita
                // construir facilmente un nuevo abono record que los represente todos
            }
            if(sfr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD) ||
                    sfr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                lprods.add(sfr); // se saca una copia aparte de los producto records y devoluciones.
            }
            
            if(sfr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
                // si es tipo cupon se agrega sin mas a la nueva lista de factura records
            }
        }
        // si no hay ningun abono record entonces la suma es igual a cero
        if(sum_abono>0){ // en caso de si haber abonos se crea un unico que represente todos en valor.
            Abono abo = fare.abono;
            abo.valor = sum_abono; // se hace de esta manera para no dejar cabos sueltos ya que hay redundancia
            // de los atributos de abono con el de SptFacturaRecord.
            fare = new SptFacturaRecord(abo); // se dejan todos los datos del abono y
            //se modifica solo el valor por la suma de todos los abonos.
            fre.add(fare); // se agrega un unico record tipo
        }
        
        // *************************************
        // se organinzan los producto records ->
        Map<String, List<SptFacturaRecord>> mapa; // se ordenan los ProductoRecords copiados en el ciclo anterior
        mapa = lprods.stream().collect(groupingBy(SptFacturaRecord::getSpt_codigo));
        Set<String> keyset = mapa.keySet(); // los codigos de cada producto
        
        // nueva lista para guardar los ProductoRecords ya uunificados
        ObservableList<SptFacturaRecord> neu_lprods = FXCollections.observableArrayList();
        
        for(String key: keyset){ // para cada keySet se hace un porceso de unificacion
            List<SptFacturaRecord> laux = mapa.get(key);
            int sum = 0; // auxiliar para guardar acumulacion de cantidades del record y hacer unificacion
            for(SptFacturaRecord fr : laux){
                // se suman las cantidades de cada producto record
                // y se restan las cantidades especificadas en cada devolucion.
                if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)) sum += fr.getSpt_cantidad();
                if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)) sum -= fr.getSpt_cantidad();
            }
            SptFacturaRecord auxfr = laux.get(0);
            auxfr.setSpt_cantidad(sum); // se construye un nuevo y unico record con la cantidad unificada
            neu_lprods.add(auxfr); // se agrega a la nueva lista.
        } // al final de neu_lprods tiene la lista de producto records unificada con las devoluciones
        fre.addAll(neu_lprods); // se pegan los elementos al final de la lista fre
        
        return fre;
    }
    
    /**
     * recive una lista todos los productos records de una factura y retorna 
     * una representacion String de la misma en un formato conveniente.
     * @return 
     */
    public String ObList_FR_2Str(ObservableList<SptFacturaRecord> lis){
        String r = "\n";
        
        /**
         * Separador.
         */
        final String SEP = "       _-_-_      ";
        
        r += "Codigo" + SEP + "Descripcion" + SEP + "Cantidad \n\n";
        
        /**
         * se escribe cada record.
         */
        for(SptFacturaRecord fr : lis){
            r+= fr.spt_codigo.get() + SEP + fr.spt_descripcion.get() + SEP + Integer.toString(fr.getSpt_cantidad().intValue()) + "\n\n";
        }
        
        return r;
    }
    
}
