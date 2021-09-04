package tentactildesktoppos.FXMLinjection;

import BD_tdpos_r.Hndl_Abonos;
import BD_tdpos_r.Hndl_Clientes;
import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.Hndl_Cupon;
import BD_tdpos_r.Hndl_Factura;
import BD_tdpos_r.Hndl_ProductoRecord;
import BD_tdpos_r.Hndl_Productos;
import BD_tdpos_r.Hndl_Vendedor;
import BD_tdpos_r.SQL_Configuracion;
import Impresion.LocalPrinter;
import Validator_tdpos_r.Va_Cupon;
import Validator_tdpos_r.Validador;
import Validator_tdpos_r.ValidationResult;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.groupingBy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.print.PrintException;

import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.negocio_r_objs.Abono;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.Cupon;
import tentactildesktoppos.negocio_r_objs.Factura;
import tentactildesktoppos.negocio_r_objs.Producto;
import tentactildesktoppos.negocio_r_objs.ProductoRecord;
import tentactildesktoppos.negocio_r_objs.Vendedor;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptVendedor;


/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLVentasController extends ControllerFather{
    
    // Elementos de la pestaña de ventas
    
    /**
     * TextArea para mostrar la informacion del cliente.
     */
    @FXML
    public TextArea TA_infoCliente;
    
    
    /**
     * TF para hacer busqueda.
     */
    @FXML
    public TextField TF_buscar_cliente;
    
    /**
     * cliente seleccionado para hacer la venta.
     */
    public Cliente cliente;
    
    /**
     * mensaje para el TextArea de la pesaña de ventas cuando no se encuentra un cliente
     */
    public final String NO_RESULTS = "No hay ningun cliente con el cc o nit especificado";
    
    
    /**
     * para seleccionar si el pago sera a credito o contado
     */
    @FXML
    public ComboBox CB_tipoPago;
    
    /**
     * combo box de la pestaña de ventas para seleccionar al vendedor
     * que hara la venta.
     */
    @FXML
    public ComboBox CB_vendedor;
    
    /**
     * para registrar productos en la venta
     */
    @FXML
    public TextField TF_buscarProducto;
    
    
    /**
     * TableView en el que se muestran los elementtos que se agregan a la venta.
     * pueden ser productos, como pueden ser cupones.
     */
    @FXML
    public TableView<SptFacturaRecord> TV_ventas;
    
    
    /**
     * Columnas de la tabla para mostrar los Valores String de la venta
     */
    @FXML
    public TableColumn<SptFacturaRecord, String> TC_codigo, TC_descripcion;
    
    
    /**
     * TableColumns para mostrar los valores int de una venta
     */
    @FXML
    public TableColumn<SptFacturaRecord, Integer> TC_precio_venta, TC_cantidad, TC_subTotal;
    
    
    /**
     * label para escribir la cantidad de prendas que se han registrado hasta el momento
     */
    @FXML
    public Label Lb_NPrendas;
    
    
    /**
     * titulo de la ventana modal cuando no se encuentre el producto buscado
     */
    public final String PRODUCTO_NO_ENCONTRADO_TITLE = "No Encontrado";
    
    
    /**
     * subtitulo de la ventana modal cuando no se encuentre el producto buscado
     */
    public final String PRODUCTO_NO_ENCONTRADO_SUBTITLE = "Producto no codificado";
    
    /**
     * mensaje de la ventana modal cuando no se encuentra un producto.
     */
    public final String PRODUCTO_NO_ENCONTRADO_MESS = "El codigo de barras "
            + "ingresado no corresponde a ninguno de los productos codificados en la basee de datos";
    
    /**
     * mensaje que indica al usuario que el nuevo precio para el producto escogido
     * no es valido. no hay un final String para el titulo de la ventana modal
     * ya que se reutiliza un titolo mas generico de la clase padre para validacion.
     */
    public final String NO_VALI_NEW_PRICE_MESS = "El valor introducido no es valido. "
            + "recuerde quede introducir solo numeros enteros o un porcentaje y que estos "
            + "tienen un limite para no generar la venta";
    
    
    /**
     * titulo de ventana modal cuando no se puede hacer el registro de una venta
     */
    public final String NO_REGIS_TITLE = "Hay Un Error";
    
    /**
     * sub titulo de la ventana modal cuando no se puede hacer el registro de una venta
     */
    public final String NO_REGIS_SUBMESS = "";
    
    /**
     * mensaje para la ventana modal cuando no se puede hacer el registro de una venta
     */
    public final String NO_REGIS_MESS = "Revise la venta porfavor ya que hay un error\n"
            + " en los datos. Recuerde que debe haber por lo menos un producto registrado\n"
            + " en la venta. Ademas el valor de los cupones (en caso de haberlos) \n"
            + " no puede superar el valor de la compra.";
    
    
    
    /**
     * titulo de la ventana modal cunado se intenta registrar y no hay cliente 
     * seleccionado
     */
    public final String NO_CLIENTE_SEL_TITLE = "No hay ningun cliente seleccionado";
    
    
    /**
     * mensaje de la ventana modal cuando se intenta registrar ún producto y 
     * no hay cliene seleccionado
     */
    public final String NO_CLIENTE_SEL_MESS = "Para poder registrar productos debe "
            + "primero seleccionar un cliente. Para ello digite porfavor el CC "
            + "o NIT del cliente en el debido campo de texto y presione la tecla Enter";
    
    
     
    /**
     * titulo para la ventana modal que permitte fijar un nuevo precio de venta 
     * para un producto record
     */
    public final String NEW_PRICE_TITLE = "Establecer Precio de venta";
    
    
    
    /**
     * Submensaje que pemrite informar al usuario el valor de costo
     * y el maximo porcentaje de rebaja para el producto.
     * Se debe usar replace con ? y con x.
     */
    public final String NEW_PRICE_MESS = "Precio de costo: _  maximo porcentaje de rebaja: #%";
    
    
    /**
     * Mensaje que indica al usuario el tipo de dato que debe ingresar
     * y como hacerlo
     */
    public final String NEW_PRICE_SUBMESS = "Puede ingresar un valor arbitrario en pesos como precio de venta\n"
            + "siempre y cuando no sea inferior al precio de costo. Si desea puede ingresar un procentaje de \n"
            + "descuento. Ejemplo: si desea descontar el 10% debe ingresar en el campo de texto '10%'. \n"
            + "De igual forma este pporcentaje no puede ser mayor al especificado ya que en ese caso habria\n"
            + "una perdida.";
    
    
    /**
     * titulo para el input modal dialog de ingresar datoss de cupon
     */
    public final String CUPON_TITLE = "Ingrese los datos del cupon Porfavor";
    
    /**
     * sub mensaje para el input modal dialog de ingresar datos de cupon
     */
    public final String CUPON_SUBMESS = "Debe ingresar los datos de la siguiente manera: \n"
            + "id-pass \n"
            + "por ejemplo si en el cupon impreso identifica pass : 4370 y id = 101 entonces \n"
            + "debe ingresar en el campo de texto 101-4370";
    
    /**
     * mensaje para el input modal dialog de ingresar datos de cupon
     */
    public final String CUPON_MESS = "Ingrese el valor tal como se especifica arriva";
    
    /**
     * titulo de ventana modal para registro de venta
     */
    public final String SI_RES_TITLE = "Registro Exitoso";
    
    /**
     * sub tituloo de ventana modal de registro exitoso de venta
     */
    public final String SI_RES_SUB = "";
    
    /**
     * mensaje para ventana modal de registro exitoso de venta
     */
    public final String SI_RES_MESS = "La venta ha sido registrada exitosamente";
    
    
    /**
     * Label en el que se muestra Total de la compra.
     */
    @FXML
    public Label L_total;
    
    
    @FXML
    public void initialize() {
        TA_infoCliente.setEditable(false); // se evita que puedan editar el TextField
        
        this.ConfigTVventas(); // se configura el tableView.
        
        this.cliente = new Cliente();
        this.cliente.id=""; // para tener un equivalente a cliente nulo
        // pero sin usar null ya que trae numorosos problemas de robustez
        
        
        // se agrega un listener a cambios de item seleccionado al ComboBox de ipo de venta
        this.CB_tipoPago.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                // solo si el comboBox no esta vacio entonces se hace la aperacion de actualizar
                // los precios deacuerdo al tipo de venta.
                // recuerdese que cuando no hay cliente seleccionado el 
                // comboBox se deja sin items
                if(!CB_tipoPago.getItems().isEmpty()) UpdatePrices_TipoVenta();
            }
        });
        
        this.ConfigurarCB_vendedor(); // se muestran en el ComboBox los vendedores activos.
    }
    
    
    /**
     * metodo que hace busqueda de un cliente y lo instacia como variablee global de este archivo.
     * se activa cuando se hace enter en el TextField de buscar cliente en la pestaña
     * de ventas.
     * @param ae 
     */
    @FXML
    public void onAction_TF_buscar_cliente(ActionEvent ae){
        this.SearchAndSetCliente();
        
    }
    
    
    /**
     * cuando se presiona una tecla y el TF de busqueda de cliente esta
     * seleccionado.
     * @param ke 
     */
    @FXML
    public void onKeyRel_TF_buscar_cliente(KeyEvent ke){
        //this.SearchAndSetCliente();
    }
    
    
    /**
     * Metodo que hce busquueeda de un cliente y si existe lo establece
     * como variable global para este archivo de clase controller
     */
    public void SearchAndSetCliente(){    
        Hndl_Clientes hndc = new Hndl_Clientes();
        
        
        // primero se busca el cliente
        try{
            ArrayList<Cliente> lcli = hndc.buscarCliente_id(TF_buscar_cliente.getText());
            
            if(lcli.isEmpty()){ // si no existe un cliente con ese id en la BD
                TA_infoCliente.setText(this.NO_RESULTS);
                CB_tipoPago.getItems().clear(); // se quitan las opciones de tipo de pago
            }
            else{ // en caso de que el cliente sii exista->
                this.cliente = lcli.get(0); // se establece a que cliente se le realizara la venta.
                TA_infoCliente.setText(this.cliente.InfoClienteStr());
                CargarTiposdeVenta(cliente);
                this.TF_buscar_cliente.setEditable(false);
                this.TF_buscarProducto.clear();
                this.TF_buscarProducto.requestFocus();
            }
            
            
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de un error con dialgo modal
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
    
    /**
     * metodo que carga los tipos de venta que un cliente tiene permtido
     * de acuerdo a su tipo. Lo anterior se explica un poco mejor en la clase
     * de cliente
     * @param c
     */
    public void CargarTiposdeVenta(Cliente c){
        ArrayList<String> ltv = new ArrayList<>();
        
        // sin importar el tipo de cliente siempre podra hacer compras de contado
        ltv.add(Factura.TIPOVENTA_CONTADO); // y por esto se agrega sin condicion.
        
        if(c.tipo.equals(Cliente.TIPO_CREDITO) || c.tipo.equals(Cliente.TIPO_MAYOR)){
            ltv.add(Factura.TIPOVENTA_CREDITO); // solo si son de credito o contado se habilita la opcion de
            // compra a credito.
        }
        // finalmente se carga el combobox
        super.SetComboBox(CB_tipoPago, ltv);
        
        if(c.tipo.equals(Cliente.TIPO_CREDITO) || c.tipo.equals(Cliente.TIPO_MAYOR)){
            CB_tipoPago.getSelectionModel().select(Factura.TIPOVENTA_CREDITO);
        }
    }
    
    
    /**
     * when Enter key is pressed and Cancel button is focused
     * @param ae 
     */
    @FXML
    public void onAction_B_cancelar(ActionEvent ae){
        this.clearVenta();
    }
    
    
    /**
     * click en el boton de cancelar de la pestaña de ventas.
     * @param me 
     */
    @FXML
    public void onClick_B_cancelar(MouseEvent me){
        this.clearVenta();
    }
    
    /**
     * se borran todos los datos de la venta en curso
     */
    public void clearVenta(){
        this.TF_buscar_cliente.clear();
        this.TF_buscar_cliente.setEditable(true);
        this.TA_infoCliente.clear();
        
        this.cliente = new Cliente();
        this.cliente.id = "";
        this.TV_ventas.getItems().clear();
        this.CB_tipoPago.getItems().clear();
        this.L_total.setText("---");
        this.ContarPrendas(); // se recuenta el numero de prendas
    }
    
    
    /**
     * cuando se preciona enter en el TF de buscar producto se procede a hacer la
     * busqueda del producto y a pedir al usuario la cantidad a vender
     * @param ae 
     */
    @FXML
    public void onAction_TF_buscarProducto(ActionEvent ae){
        this.Registrar();
    }
    
    
    /**
     * para registra un producto se busca en la base de datos y se contruye
     * un ProductoRecord para agregar al TableView de la compra.
     */
    public void Registrar(){
        
        Validador val = new Validador();
        
        if(!(this.cliente.id.equals(""))){ // si el cliente no es nulo, es decir
            // en caso de haber un cliente seleccionado entonces se procede al registro
            
            ArrayList<Producto> lp = new ArrayList<>(); // para guardar el producto
            Hndl_Productos hndp = new Hndl_Productos(); // para buscar el producto
            
            try{
                // se trae el producto de la base de datos
                lp = hndp.buscarProducto_codigo(this.TF_buscarProducto.getText());
                
                // se notifica al usuario en caso de que el producto no se encuentre en la BD
                if(lp.isEmpty()){ 
                    super.ShowInfo(this.PRODUCTO_NO_ENCONTRADO_TITLE,
                            this.PRODUCTO_NO_ENCONTRADO_SUBTITLE, this.PRODUCTO_NO_ENCONTRADO_MESS);
                    
                } else { // si el producto si existe se anexa.
                    
                    /**
                     * hice un cambio de afan, inicialmente se pedia cantidad de prendas pero en la practica se ha 
                     * observado que es mejor asumir de entrada que cada lectura de un codigo de barras va con
                     * cantidad 1, pero para evitar errores sobre la marcha simplemente estableci el String = "1"
                     * en lugar de modificar la validacion de el string etc...
                     */
                    
                    //String str_cantidad = 
                    //        super.ShowInput(Validador.INPUT_QUANT_TITLE, "", Validador.INPUT_QUANT_MESS, "");
                    String str_cantidad = "1"; 
                    // entrada vacia se asume como uno por defecto por parte del programa de ventas.
                    //if(str_cantidad.equals("")) str_cantidad = "1";
                    
                    int cantidad; // cantidad de prendas que el usuario desea ingresar
                    
                    if(val.isPositiveNumGreZero(str_cantidad)){ // si el usuario introduce un numero valido
                        cantidad = Integer.parseInt(str_cantidad);
                        
                        // Se crea un producto record
                        ProductoRecord PR = new ProductoRecord(lp.get(0),
                                cantidad,
                                CB_tipoPago.getSelectionModel().getSelectedItem().toString(),
                                cliente);
                        
                        //  se agrega el productot record
                        this.TV_ventas.getItems().add(new SptFacturaRecord(PR));
                        this.UnificarCompra();
                        
                    }
                    else{ // cuando el usuario no introduce una cantidad de prendas validas
                        super.ShowWarning(Validador.NUM_NO_POS_TITLE, "", Validador.NUM_NO_POS_MESS);
                    }
                    
                }
                this.ContarPrendas();
            } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de una excepcion.
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
            
            
        } else{ // si no hay cliente seleccionado entonces no se registra y se notifica al usuario.
            super.ShowInfo(this.NO_CLIENTE_SEL_TITLE, "", this.NO_CLIENTE_SEL_MESS);
        }
        // se limpia el tf de busqueda y se pone nuevamente el cursor.
        this.TF_buscarProducto.clear();
        this.TF_buscarProducto.requestFocus();
        
        this.SetLabelCompraPrecio();
        this.TF_buscarProducto.requestFocus();
    }
    
    
    /**
     * metodo para configurar los table columns para el table view de ventas
     */
    public void ConfigTVventas(){
        // Strings
        this.TC_codigo.setCellValueFactory(new PropertyValueFactory<>("spt_codigo"));
        this.TC_descripcion.setCellValueFactory(new PropertyValueFactory<>("spt_descripcion"));
        
        // Integers
        this.TC_precio_venta.setCellValueFactory(new PropertyValueFactory<>("spt_precio_venta"));
        this.TC_cantidad.setCellValueFactory(new PropertyValueFactory<>("spt_cantidad"));
        this.TC_subTotal.setCellValueFactory(new PropertyValueFactory<>("spt_subtotal"));
        
        // se inicializa la lista obsevable del tableView con una lista Arreglo vacia
        ObservableList<SptFacturaRecord> ovl_lf = FXCollections.observableArrayList(new ArrayList<>());
        this.TV_ventas.setItems(ovl_lf);
    }
    
    
    /**
     * cuando se presiona una tecla cualquiera y el TableView de ventas esta
     * seleccionado
     * @param ke 
     */
    public void onKeyPress_TV_ventas(KeyEvent ke){
        // se lee la tecla precinada
        KeyCode key = ke.getCode();
        
        //si se preciona tecla +
        // se debe de agregar +1 al item seleccionado
        if(key.equals(KeyCode.PLUS)){
            this.TV_ventas.getSelectionModel().getSelectedItem().OnePlusProducto();
        }
        
        //si se preciona tecla suprimir
        //se debe remover el producto seleccionado
        if(key.equals(KeyCode.DELETE)){
            SptFacturaRecord spt_fr = this.TV_ventas.getSelectionModel().getSelectedItem();
            this.TV_ventas.getItems().remove(spt_fr);
        }
        
        
        //si se preciona tecla -
        // se debe restar -1 al porducto seleccionadopero si la cantidad
        // llega a 0 entonces se debe remover el item
        if(key.equals(KeyCode.MINUS)){
            SptFacturaRecord spt_fr = this.TV_ventas.getSelectionModel().getSelectedItem();
            if(spt_fr.spt_cantidad.intValue() == 1){ // si solo queda una prenda del item seleccionado
                // un menos lo pondra en 0, es decir que debera ser eliminado
                this.TV_ventas.getItems().remove(spt_fr);
            } else{
                this.TV_ventas.getSelectionModel().getSelectedItem().OneMinusProducto();
            }
        }
        
        // si se preciona tecla Enter
        // entonces se pide un precio arbitrario para el articulo.
        if(key.equals(KeyCode.ENTER)){
            try{
                // se toma el producto seleccionado de la tabla
                SptFacturaRecord sel_it= this.TV_ventas.getSelectionModel().getSelectedItem();
                
                // solo si se trata de un producto record se intenta cambiar el precio de lo
                //contrario no se hace nada, recuerdese que puede ser un record tipo cupon
                if(sel_it.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                    // se lee el costo del producto seleccionado para mostrarlo
                    int pvmayor = sel_it.producto_record.getProducto().pventa_mayor;
                    String str_costo = Integer.toString(pvmayor);
                    
                    ArrayList min_percent_arra = sel_it.MinPercent(); // int en slot 0 y string en slot 1 con el minimo porcentaje aceptable
                    
                    boolean ok_flag = false; // boolean que permite lograr en if's no encapsulados
                    // efecto programatico similar que si estuviesen nested.
                    // esto se hace ya que ensted if's pueden llegar a ser extrmadamente confusos
                    
                    // se toma del usuario el precio o porcentaje de descuento deseado (promt modal window)
                    String str_new_price = super.ShowInput(this.NEW_PRICE_TITLE,
                            this.NEW_PRICE_SUBMESS, this.GenMess(this.NEW_PRICE_MESS,
                                    str_costo, (String) min_percent_arra.get(1)),
                                    "");
                    Validador va = new Validador();
                    
                    if(va.isPositiveNum(str_new_price)){ // si el usuario ingreso un valor arbitrario
                        if(Integer.parseInt(str_new_price) >= pvmayor){ // y si el precio arbitrario es mayor a costo
                            // se estabelce en el nuevo valor si todo esta bien
                            TV_ventas.getSelectionModel().getSelectedItem().setSpt_precio_venta(Integer.parseInt(str_new_price));
                            ok_flag = true; // indica que entro a uno de los 2 if's
                            // y por tanto es str_new_price es un String valido
                        }
                    }
                    
                    
                    if(va.EsPorcentajeValido(str_new_price)){ // en caso de que usuario hay ingresado un porcentaje
                        str_new_price = str_new_price.replace("%", "");
                        int percent_redu = Integer.parseInt(str_new_price); // se pasa a int
                        if( percent_redu <= (int) min_percent_arra.get(0)){ // solo si el porcentaje de rebaja no supera
                            // el maximo de limite de perdida
                            this.TV_ventas.getSelectionModel().getSelectedItem().SetDescuento(percent_redu);
                            // ok solo sera igual a true si se hizo una de las dos operaciones de descuento
                            ok_flag = true; // el false indica que el valor introducido, aunque no genere excepcion
                            // no es valido.
                        }
                    }
                    
                    if(!ok_flag){// si no entro a ninguno de los 2 if's entonces
                        //el string igresado no es ni porcentaje ni precio arbitrario. y por tanto no es valido
                        // este esquema se usa para reducir los nested if's ya que son muy confusos y error prone.
                        super.ShowWarning(Validador.NUM_NO_POS_TITLE, "", this.NO_VALI_NEW_PRICE_MESS);
                    }
                }
                
                
            } catch(NumberFormatException | NullPointerException ex){
                System.out.println("excepcion:  " + ex.getMessage()); // solo para debugging
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(FXMLVentasController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(FXMLVentasController.class.getName()).log(Level.SEVERE, null, ex);
            }
            TF_buscarProducto.requestFocus();
            
        }
        
        
        
        // metodo para actualizar los cambios en la lista observable del
        // table view
        this.TV_ventas.refresh();
        this.SetLabelCompraPrecio();
        this.ContarPrendas(); // se recuenta el numero de prendas
    }
    
    /**
     * metodo que recibe un mensaje base y unos vaales para reemplazar
     * en el estring base, m.Este metodo debe generar el submensaje de
     * la ventana modal para establecer precio arbitrario a un item.
     * @param m
     * @param costo
     * @param min_prtge
     * @return
     */
    public String GenMess(String m, String costo, String min_prtge){
        String r = m.replace("_", costo);
        r = r.replace("#", min_prtge);
        return r;
    }
    
    
    /**
     * cuando se presiona la tecla enter y boton de registrar tiene focus
     * @param ae
     */
    @FXML
    public void onAction_B_registrar(ActionEvent ae){
        //this.RegistrarVenta();
    }
    
    
    /**
     * Cuando se hace click en el boton de registrar.
     * @param me 
     */
    @FXML
    public void onClick_B_registrar(MouseEvent me){
        this.RegistrarVenta();
    }
    
    
    /**
     * toma todos los datos en el table view y contruye un objeto tipo factura
     * para ingresarlo a la base de datos. tambien ingresa el debido
     * factura record
     */
    public void RegistrarVenta(){
        // primero se valida si el TableView no suma mas dinero en cupones que en producto records
        // si no hay ningun producto record entonces el total de la tabla sera 0
        // si hay mas dinero en cupones entonces la venta sera un numero negativo.
        // por este motivo se hace validacion del Valor en dinero del Label de total
        // de venta.
        try{
            
            if( Integer.parseInt(L_total.getText()) >= 0){ //  si el total de la venta es un numero positivo
                
                Hndl_Factura hndf = new Hndl_Factura(); // conector a tabla de facturas
                
                int consecutivo = hndf.getNextConsecutiv();
                
                // instancia de la factura que se guarda en la BD. se requiere para hacer su respectiva impresion.
                Factura theFac;
                
                boolean ok = false; // bandera para indicar que la factura se guardo correctamente.
                
                // cuando la venta es de contado
                if(this.CB_tipoPago.getSelectionModel().getSelectedItem().toString().equals(Factura.TIPOVENTA_CONTADO)){
                    
                    // se Inserta el record de la la factura en la BD
                    hndf.InsertarFactura(new Factura(consecutivo, 0, this.cliente.id, 
                            this.getSelectedVendedortFromCBox().id,
                            this.CB_tipoPago.getSelectionModel().getSelectedItem().toString(), 
                            0));
                    
                    // se insertan los producto records con el respectivo consecutivo de la factura
                    this.GuardarTablaVentasRecords(consecutivo);
                    
                    // se guarda el record de abono por el total del saldo. (la factura queda paga y sin saldo
                    Hndl_Abonos hndb = new Hndl_Abonos();
                    hndb.IsertarAbono(new Abono(0, Integer.parseInt(L_total.getText()), // a la larga la tabla de abono es una tabla de ingreso de efectivo.
                            this.cliente.id, consecutivo, this.getSelectedVendedortFromCBox().id));
                    
                    ok = true;
                    super.ShowInfo(this.SI_RES_TITLE, SI_RES_SUB, SI_RES_MESS);
                }
                
                // Cuando la venta es a credito
                if(this.CB_tipoPago.getSelectionModel().getSelectedItem().toString().equals(Factura.TIPOVENTA_CREDITO)){
                    
                    // se Inserta el record de la factura en la BD
                    hndf.InsertarFactura(new Factura(
                            consecutivo, 
                            Integer.parseInt(this.L_total.getText()),
                            this.cliente.id, 
                            this.getSelectedVendedortFromCBox().id,
                            this.CB_tipoPago.getSelectionModel().getSelectedItem().toString(), 
                            0));
                    // se ingresan los productos records
                    this.GuardarTablaVentasRecords(consecutivo);
                    // no se inserta record de abono por los que el saldo debe ser diferente de 0.
                    
                    ok = true; // se indica que la operacion en BD ha sido exitosa.
                    super.ShowInfo(this.SI_RES_TITLE, SI_RES_SUB, SI_RES_MESS);
                }
                
                //se revisa si hay impresora cconfigurada
                boolean ok_printer = true;
                Hndl_Configuracion hndconf = new Hndl_Configuracion();
                
                if(hndconf.getConfiguracion(SQL_Configuracion.PRT_FACT_MAX_LINES).isEmpty()) ok_printer = false;
                if(hndconf.getConfiguracion(SQL_Configuracion.PRT_IMPR_FACTURA).isEmpty()) ok_printer = false;
                
                // si se lleva a cabo el registro en la BD entonces es correcto realizar una impresion
                if(ok){
                    // cuando la venta se ha concretado se limpia la tabla de ventas y los demas datos de la venta
                    this.clearVenta();
                    if(ok_printer){
                        LocalPrinter lcprinter = new LocalPrinter();
                        theFac = hndf.getFacturaBy_ID(consecutivo).get(0);
                        lcprinter.ImprimirFactura(theFac);
                    }
                }
            }
            
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        } catch (NumberFormatException ex){ // ocurre cuando el usuario intenta (ver if con parseInt de label.getText())
            // registrar sin que haya ningun producto registrado o ningun cliente registrado
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        catch(NullPointerException ex){ // se lanza cuando se intenta registrar sin seleccionar primero
            // un cliente y como concecuencia el combo Box de tipo de pago esta vacio
            // generando un NullPointer en la ejecucion de este metodo.
            // en caso de ocurrir esta excepcion simplemente no se hace nada
            // pero se escribe en consola para troubleShooting.
            System.out.println("se intento registrar, sin seleccionar cliente primero");
        } catch (RuntimeException ex) { // estas 3 excepciones corresponden a la fuincion de impresion.
            Logger.getLogger(FXMLVentasController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrintException ex) {
            Logger.getLogger(FXMLVentasController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLVentasController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    /**
     * click en el boton de redimir bono, pestaña de ventas
     * @param me 
     */
    @FXML
    public void onClick_B_bono(MouseEvent me){
        this.ReadCupon();
    }
    
    
    /**
     * metodo que arroja una ventana modal para que el usuario
     * introduzca los numeros de identifiacion de un cupon para redimir
     */
    public void ReadCupon(){
        
        String input = super.ShowInput(this.CUPON_TITLE, this.CUPON_SUBMESS, this.CUPON_MESS, "");
        
        try{ // objetos requeridos para hacer la validacion del uso del cupon especificado.
            Va_Cupon vald = new Va_Cupon();
            ObservableList<SptFacturaRecord> lfr = TV_ventas.getItems();
            ValidationResult vr = vald.EsValidoIngresarCupon(input, lfr, cliente);
            
            if(vr.result){ // si es valido 
                // se hace cast al cupon, que se guardo en vr.obj_carry.
                this.TV_ventas.getItems().add(new SptFacturaRecord( (Cupon) vr.obj_carry ));
                this.TV_ventas.refresh();
                SetLabelCompraPrecio();
            }
            else{
                super.ShowWarning(super.no_operacion, "", vr.motivo);
            }
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de un error.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
   
   
   
   /**
    * metodo que actualiza los precios de venta de acuerdo al tipo de venta 
    * seleccionada en el Combo Box.
     */
   public void UpdatePrices_TipoVenta() {
       
       String tipoVenta = CB_tipoPago.getSelectionModel().getSelectedItem().toString();
       
       try{
           //se barre cada uno de los records del Table View para hacer el cambio de
           // precio
           for(int j=0; j<this.TV_ventas.getItems().size(); j++){ // a cada record se la hace cambio del precio
               // el metodo de la clase se encarga de hacerlo dependiendo del tipo de cliente y tipo de venta seleccionado
               this.TV_ventas.getItems().get(j).ChangeSellPrice(tipoVenta, this.cliente.tipo);
           }
       } catch(SQLException | ClassNotFoundException ex){
           
       }
       this.TV_ventas.refresh();
       this.SetLabelCompraPrecio();
   }
   
   
   /**
    * metodo que calcula el total de la compra y establece el valor en el
    * Label destinado para tal motivo.
    */
   public void SetLabelCompraPrecio(){
       int suma = 0; // se inicia el total en cero
       
       // se barren todos los elementos del TableView
       for(int k=0; k<this.TV_ventas.getItems().size(); k++){
           
           // si es un producto record se suma al total
           if(this.TV_ventas.getItems().get(k).behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
               suma += this.TV_ventas.getItems().get(k).spt_subtotal.get();
           }
           
           // si es un cupon entonces se reduce del total
           if(this.TV_ventas.getItems().get(k).behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
               suma -= this.TV_ventas.getItems().get(k).spt_precio_venta.get();
           }
       }
       // se escribe el total de la compra en el label.
       this.L_total.setText(Integer.toString(suma));
   }
   
   
   
   /**
    * metodo que guarda los productos records en la BD y anula los cupones redimidos
    * en caso de haberlos en la venta.
    * Ademas descarga de Stock las unidades vendidas.
    * @param consecutivo
    */
   public void GuardarTablaVentasRecords(int consecutivo){
       try{
           Hndl_ProductoRecord hndpr = new Hndl_ProductoRecord(); // conector para producto record
           Hndl_Productos hndp = new Hndl_Productos(); // conector para productos
           
           Hndl_Cupon hndcu = new Hndl_Cupon();// conector para cupones
           
           for(int y=0; y<this.TV_ventas.getItems().size(); y++){ // solo si es un producto record entonces ->
               if(this.TV_ventas.getItems().get(y).behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                   
                   // en cada iteracion se inserta el respectivo producto record
                   hndpr.InsertProductoRecord (new ProductoRecord(
                           0,
                           consecutivo,
                           this.TV_ventas.getItems().get(y).getSpt_cantidad(),
                           this.TV_ventas.getItems().get(y).producto_record.costo,
                           this.TV_ventas.getItems().get(y).producto_record.pventa,
                           this.TV_ventas.getItems().get(y).getSpt_codigo()));
                   // se descuenta de stock la cantidad vendida.
                   hndp.Sum2Stock(this.TV_ventas.getItems().get(y).getSpt_codigo(), this.TV_ventas.getItems().get(y).getSpt_cantidad()*-1);
                   
               }//si es tipo cupon entonces se inactiva el cupon
               if(this.TV_ventas.getItems().get(y).behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
                    hndcu.DesactivarCupon(this.TV_ventas.getItems().get(y).cupon, consecutivo);
               }
           }
           // una posible excepcion es registrar un producto en la 
       } catch(SQLException | ClassNotFoundException ex){
           super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
       }
   }
   
   /**
    * metodo  que carga los vendedores activos de la base de datos
    */
   public void ConfigurarCB_vendedor(){
       // representaciones String de los vendedores activos para el comboBox
       String list4cbBox[]; // tiene la estructura Nombre - Cedula
       
       try{
           Hndl_Vendedor hndv = new Hndl_Vendedor(); // conector a la tabla de vendedores.
           ArrayList<SptVendedor> lvend = new ArrayList<>(); // lista de los vendedores activos.
           lvend = hndv.getVendedoresActivos(); // sleect de la base de datos.
           ArrayList<String> lv_str = new ArrayList<>(); // lista de String para el comboBox
           for(int j=0; j<lvend.size() ; j++){ // para cada vendedor se arma su representacion String
               lv_str.add(lvend.get(j).nombre + "-" + lvend.get(j).id);
           }
           super.SetComboBox(CB_vendedor, lv_str); // se establece el valor del comboBox.
           
       } catch(ClassNotFoundException | SQLException ex ){
           super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
       }
   }
   
   
   
   /**
    * metodo que construye el objeto vendedor a partir de la repsresentacion String
    * en el ComboBox de vendedor. La estructura del esting del comboBox
    * se representa por: "[String nombre del vendedor]-[String cc del vendedor]".
    * por esto hay que hace un split("-") para separ la llave primaria.
    * @return 
    */
   public Vendedor getSelectedVendedortFromCBox(){
       Vendedor v = new Vendedor(); 
       
       try{
      String sel_str = this.CB_vendedor.getSelectionModel().getSelectedItem().toString();
      String sep[] = sel_str.split("-"); // se separa el nombre del cc
      Hndl_Vendedor hndv = new Hndl_Vendedor(); // conctor a tabla de vendedores
      v = hndv.getVendedorId(sep[1]).get(0);// construye el Obj vendedor partiendo del String
      // seleccionado en el comboBox de Vendedor
       } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de una excepcion
           super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
       }
      return v;
   }
   
   
   /**
    * metodo que retorna true cuando existe en lista de venta un producto
    * con el String codigo. se usa principalemten para hacer unificacion de
    * productos. 
    * @param codigo
    * @return 
    */
   public boolean ContieneProducto(String codigo){
       boolean r = false;
       for(int t=0; t<this.TV_ventas.getItems().size(); t++){
           if(this.TV_ventas.getItems().get(t).behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){ 
               // solo si es un producto se hace la revicion
               // solo si hay un match se pone r = true indicando que ya hay un producto con este
               // codigo en lla lista de productos codificados.
               if(this.TV_ventas.getItems().get(t).producto_record.Producto_codigo.equals(codigo)) r = true;
           }
       }
       return r;
   }
   
   
   
   /**
    * metodo que  al ser invocado unfica las cantidades de los
    * productos records repetidos y actualza el table view.
    */
   public void UnificarCompra(){
       // se hace una copia de los elementos de la lista de factura records.
       ObservableList<SptFacturaRecord> clfr = this.TV_ventas.getItems();
       
       ObservableList<SptFacturaRecord> prcopy = FXCollections.observableArrayList();
       ObservableList<SptFacturaRecord> cupcopy = FXCollections.observableArrayList();
       
       for(int j=0; j<clfr.size(); j++){ //se remueven los cupones de la lista copia
           if(clfr.get(j).behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
               cupcopy.add(clfr.get(j)); // se pasan los cupones a una lista aparte.
           }
           if(clfr.get(j).behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
               prcopy.add(clfr.get(j)); // se pasan los producto records a una lista aparte
           }
       }
       
       // es necesario un mapa para el uso de la API de collections
       Map<String, List<SptFacturaRecord>> mapa; // donde se guardaran las listas unificadas
       // se hace la recolecciond de los elementos
       mapa = prcopy.stream().collect(groupingBy(SptFacturaRecord::getSpt_codigo));
       
       Set<String> keyset = mapa.keySet(); // los codigos de cada producto
       // en cada key del mapa hay una lista con productos con el mismo codigo pero diferente cantidad.
       
       // donde se guardara la nueva lista unificada.
       ObservableList<SptFacturaRecord> neu_lfr = FXCollections.observableArrayList();
       
       for(String key: keyset){ // para cada keySet se hace un porceso de unificacion
           List<SptFacturaRecord> laux = mapa.get(key);
           int sum = 0; // auxiliar para guardar acumulacion de cantidades del record y hacer unificacion
           for(SptFacturaRecord fr : laux){
               sum += fr.getSpt_cantidad(); // se accumulan las cantidades de cada producto record
           }
           SptFacturaRecord auxfr = laux.get(0);
           auxfr.setSpt_cantidad(sum); // se construye un nuevo y unico record con la cantidad unificada
           neu_lfr.add(auxfr); // se agrega a la nueva lista.
       } // al final de neu_lfr tiene la lista de producto records unificada
       
       for(SptFacturaRecord fr : cupcopy){// se agregan los cupon records.
           neu_lfr.add(fr);
       }
       
       this.TV_ventas.setItems(neu_lfr);
       this.TV_ventas.refresh();
   }
   
   
   /**
    * ver javadoc en la declaracion del metodo abstracto en la superclaser.
    * @return 
    */
   @Override
    public Stage GetStage() {
        return (Stage) this.CB_tipoPago.getScene().getWindow();
    }
   
    
    
    /**
     * metodo que cuenta el total de predas registradas en la tabla de ventas
     */
    public void ContarPrendas(){
        ObservableList<SptFacturaRecord> lfrs = TV_ventas.getItems();
        
        int qnt = 0; // variable para acumular la cantidad de prendas.
        
        for(SptFacturaRecord fr : lfrs){ // se barren todos los items de la tabla
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                qnt += fr.getSpt_cantidad().intValue();
            }
        }
        
        if(lfrs.isEmpty()){
            qnt = 0;
        }
        
        // se establede la cantidad contada en el Label destinado para ello
        this.Lb_NPrendas.setText(Integer.toString(qnt));
        
    }
    
    
    
}
