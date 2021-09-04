package tentactildesktoppos.FXMLinjection;


import BD_tdpos_r.Hndl_Clientes;
import BD_tdpos_r.Hndl_Cupon;
import BD_tdpos_r.Hndl_Factura;
import Impresion.LocalPrinter;
import Validator_tdpos_r.Va_Cliente;
import Validator_tdpos_r.ValidationResult;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.animation.Animation.INDEFINITE;
import javafx.animation.FillTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.print.PrintException;


import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLPopUps.FXMLModClienteController;
import tentactildesktoppos.FXMLPopUps.FXMLSeeConsolidadoController;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.Cupon;

import tentactildesktoppos.negocio_r_objs.spt_objects.SptCliente;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;


/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLClientesController extends ControllerFather{
    
    
    /**
     * para poder saber desde este controlador si hay una venta en curso
     * y congelar las funcionalidades necesarias para asegurar la consistencia
     * de los datos mostrados en la interafz grafica.
     */
    public FXMLVentasController ventasController;
    
    
    //************
    // seccion de codificacion
    
    
    /**
     * hay 3 tipos de cliente:
     * cliente de pago de contado
     * cliente de pago a credito
     * y cliente mayorista.
     */
    ArrayList<String> tipoCliente;
    
    /**
     * hay dos formas de pago, se paga en el local o se manda a cobrar
     */
    ArrayList<String> formaPago;
    
    /**
     * Lista desplegable de java fx para seleccionar le tipo de cliente.
     */
    @FXML
    public ComboBox CB_tipoCliente;
    
    /**
     * TextField para ingresar el cc o el nit del cliente
     */
    @FXML
    public TextField TF_id;
    
    
    /**
     * textfield para el nombre del cliente
     */
    @FXML
    public TextField TF_nombre;
            
    /**
     * textfield para el telefono 1
     */
    @FXML
    public TextField TF_tel1;
    
    /**
     * textfield para el telefono 2
     */
    @FXML
    public TextField TF_tel2;
    
    /**
     * textfield para el telefono 3
     */
    @FXML
    public TextField TF_tel3;
    
    /**
     * textfield para la direccion.
     */
    @FXML
    public TextField TF_direccion;
    
    
    /**
     * textfield para el email.
     */
    @FXML
    public TextField TF_email;
    
    
    /**
     * textfield para la ubicacion.
     */
    @FXML
    public TextField TF_ubicacion;
    
    
    /**
     * elemento de FX para seleccionar la fecha de cumpleanos.
     * solo importa el mes y el dia, la hora no.
     */
    @FXML
    public DatePicker DTP_cumpleanos;
    
    
    /**
     * combo box para seleccion la forma de pago de cada cliente.
     */
    @FXML
    public ComboBox CB_forma_cobro;
    
    
    
    
    //***************
    // seccion de busqueda y modificacion
    
    
    /**
     * glypho con icono de buscar. se usa para poner un reloj de arena
     * cuando se hace trigger de la busquueda de cliente.
     * bajo ciertas condiciones puede ser muy time consuming entonces
     * se hace un hilo aparte para hacer esta operacion.
     */
    @FXML
    public FontAwesomeIcon GLY_Search;
    
    /**
     * String que asigna a un glifo para que muestre la grafica de una lupa.
     */
    public final String SET_GLY_SEARCH = "SEARCH";
    
    /**
     * nombre que se asigna a un glifo para que muestre la grafica de un spinner
     */
    public final String SET_GLY_BUSY = "SPINNER";
    
    /**
     * labels que indican diferentes counts sobre la tabla de clientes.
     */
    @FXML
    public Label LB_CountAll, LB_Count_mayor, LB_Count_credito, LB_Count_vienen, LB_Count_cobrar;
    
    
    /**
     * textfield para hacer busqueda de clientes
     */
    @FXML
    public TextField TF_buscar;
    
    
    /**
     * combo box para seleccionar el tipo de busqueda de clientes
     */
    @FXML
    public ComboBox CB_tipoBusqueda;
    
    
    
    /**
     * para hacer una busqueda por cc o nit exacto
     */
    public final String TSEARCH_CC = "Busqueda por CC o NIT";
    
    
    /**
     * busqueda por keywords en el nombre
     */
    public final String TSEARCH_NOMBRE = "Busqueda por Nombre y/o Apellido";
    
    
    /**
     * tableView busqueda clientes
     */
    @FXML
    public TableView<SptCliente> TV_clientes;
    
    
    /**
     * columnas table View busqueda de clientes.
     */
    @FXML
    public TableColumn<SptCliente, String> TC_id, TC_nombre, TC_UltAbono, TC_TpCobros;
    
    
    /**
     * columna con el total del consolidado del cliente.
     */
    @FXML
    public TableColumn<SptCliente, Integer> TC_Total;
    
    
    /***
     * TextArea para mmostrar los demas datos del cliente que no caben en el
     * TableView.
     */
    @FXML
    public TextArea TA_demasDatos;
    
    
    /**
     * menu desplegable cuando se hace click derecho en el table View de Clientes.
     */
    public ContextMenu rg_click_menu;
    
    
    /**
     * mensaje para el menu de click derecho de la tabla de Clientes.
     * Esta opcion selecciona el cliente resaltado en la tabla para la seccion de Ventas
     */
    public final String SEND_CLIENT = "Usar Cliente para Venta";
    
    
    /**
     * String constant para menu item que permite imprimir los cupones de un cliente seleccionado.
     */
    public final String PRINT_CUPONES_ACTIVOS = "Imprimir Cupones del Cliente";
    
    
    /**
     * String para el menu item de copiar el cco id del cliente al porta papeles.
     */
    public final  String COPY_CC = "Copiar CC o Nit";
    
    /**
     * Constant string pra menu click derecho. opcion que imprime la lista de facturas pendientes
     * de un cliente y ademas imprime el el saldo total, suma de todas las facturas.
     */
    public final String PRINT_CONSOLIDADO = "Imprimir consolidado";
    
    
    /**
     * constant string para el menu item (click derecho) que permite ir a la 
     * tablad e gestion de cobros, con las facturas del cliente seleccionado ya caragadas.
     */
    public final String SEE_FACTURAS = "Ir a seccion de Facturas";
    
    /**
     * glypho que se usa para decorar el combo box de tipo de busqueda de
     * cliente y ademas se le asigna una animacion de javafx 2.2 para
     * indicar el estado de busqueda de clientes cuando se dispara un 
     * hilo separado de la UI para tal proposito.
     */
    public MySearchGlyph Anim_Glyph;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // se configura el combo Box de tipo de cliente
        CargarTiposClientes(CB_tipoCliente);
        
        // se configura el combo Box de tipo de cobro
        CargarTipoPago(CB_forma_cobro);
        
        this.CargarTipoBusqueda(); // se cargan los tipos de busqueda.
        
        this.ConfigurarTVclientes();
        
        this.TA_demasDatos.setEditable(false); // el textArea ya no se puede editar.
        
        // se configura el menu item cuando se hace click derecho en la tabla de busqeuda de clientes.
        this.ConfigurarMenuItem_rgClick();
        
        /**
         * Se carga el conteo de los clientes en la BD.
         */
        this.setLabelsBDCli();
        
        this.Anim_Glyph = new MySearchGlyph(GLY_Search);
        
    }
    
    
    /**
     * cuando se hace una  busqueda por medio de un hilo separado al UI Thread de
     * fx se hace uso de un glyph icon para indicar que se sesta haciendo la
     * busqueda. ademas este glyph icon se anima.
     * esta clase encapsula las animaciones y el glypho para
     * facilitar el inicio y parada de la animacion.
     */
    public class MySearchGlyph{
        public FontAwesomeIcon icon;
        public RotateTransition rt;
        public FillTransition ft;
        
        /**
         * en el constructor se asignan las animaciones pero no se arrancan
         * @param icon 
         */
        public MySearchGlyph(FontAwesomeIcon icon){
            this.icon = icon;
            // se crea una animacion fx 2.2 de rotacion
            this.rt = new RotateTransition(Duration.millis(2000), this.icon);
            rt.setFromAngle(0);
            rt.setByAngle(357);
            rt.setCycleCount(INDEFINITE); // indefinida
            rt.setAutoReverse(false); // no se devuelve
            
            
            // se crea una animacion de cambio de color
            this.ft = new FillTransition(Duration.millis(3000), GLY_Search, Color.BLACK, Color.RED);
            ft.setCycleCount(INDEFINITE);
            ft.setAutoReverse(true);
        }
        // se debe usar cuando se inicia un hiilo de busqueda
        // se usa platform para que se pueda usar desde cualquier hilo
        public void Play(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    icon.setGlyphName(SET_GLY_BUSY);
                    rt.play();
                    ft.play();
                }
            });
        }
        // se debe usar cuando la busqueda ha terminado.
        // se usa la interfaz de run later par que se pueda usar desde otro hilo
        public void Stop(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    icon.setGlyphName(SET_GLY_SEARCH);
                    rt.stop();
                    ft.stop();
                    rt.jumpTo("start");
                    ft.jumpTo("start");
                }
            });
        }
    }
            
    
    
    /**
     * se separa en una funcion a parte para poder reutilizarlo en el
     * PopUp para modificar un cliente. 
     * @param cb 
     */
    public void CargarTiposClientes(ComboBox cb){
        // se configura el combo Box de tipo de cliente
        this.tipoCliente = new ArrayList<>();
        //this.tipoCliente.add(Cliente.TIPO_CONTADO);// se cargan los tipos de clientes.
        this.tipoCliente.add(Cliente.TIPO_CREDITO);
        this.tipoCliente.add(Cliente.TIPO_MAYOR);
        super.SetComboBox(cb, tipoCliente);
    }
    
    
    /**
     * se separa en una funcion a parte para poder reutilizarlo en el
     * PopUp para modificar un cliente. 
     * @param cb 
     */
    public void CargarTipoPago(ComboBox cb){
        // se configura el combo Box de tipo de cobro
        this.formaPago = new ArrayList<>();
        this.formaPago.add(Cliente.HACER_COBRO);
        this.formaPago.add(Cliente.VIENE_PAGAR);
        super.SetComboBox(cb, formaPago);
    }
    
    
    /**
     * cuando se presiona enter en el TextField de id (cc o nit del cliente)
     * @param ae 
     */
    @FXML
    public void onAction_TF_id(ActionEvent ae){
        TF_nombre.requestFocus();
    }
    
    @FXML
    public void onAction_TF_nombre(ActionEvent ae){
        TF_tel1.requestFocus();
    }
    
    
    @FXML
    public void onAction_TF_tel1(ActionEvent ae){
        TF_tel2.requestFocus();
    }
    
    
    @FXML
    public void onAction_TF_tel2(ActionEvent ae){
        TF_tel3.requestFocus();
    }
    
    
    @FXML
    public void onAction_TF_tel3(ActionEvent ae){
        TF_direccion.requestFocus();
    }
    
    
    @FXML
    public void onAction_TF_direccion(ActionEvent ae){
        TF_email.requestFocus();
    }
    
    @FXML
    public void onAction_TF_email(ActionEvent ae){
        TF_ubicacion.requestFocus();
    }
    
    
    @FXML
    public void onAction_TF_ubicacion(ActionEvent ae){
        DTP_cumpleanos.requestFocus();
    }
    
    
    @FXML
    public void onAction_DTP_cumpleanos(ActionEvent ae){
        this.CB_tipoCliente.requestFocus();
    }
    
    @FXML
    public void onAction_CB_tipoCliente(ActionEvent ae){
        this.CB_forma_cobro.requestFocus();
    }
    
    
    /**
     * Cuando se hace enter en el boton de codificar
     * @param ae 
     */
    @FXML
    public void onAction_B_codificar(ActionEvent ae){
        this.CodificarCliente();
    }
    
    /**
     * cuando se hace click en el boton de codifiar
     * @param me 
     */
    @FXML
    public void onClick_B_codificar(MouseEvent me){
        this.CodificarCliente();
    }
    
    /**
     * metodo para registrar un cliente en la base de datos
     */
    public void CodificarCliente(){
        
        String id, nombre, tel1, tel2, tel3, direccion, email, ubicacion,
                descripcion, tipo_cliente;
        
        LocalDate cumpleanos; // fecha  de cumpleaños del cliente, puede ser nulo
        
        try{
            // se toman los  datos de los campos de texto y demas elementos de entrda de la  gui.
            id = this.TF_id.getText();
            nombre = this.TF_nombre.getText();
            tel1 = this.TF_tel1.getText();
            tel2 = this.TF_tel2.getText();
            tel3 = this.TF_tel3.getText();
            direccion = this.TF_direccion.getText();
            email = this.TF_email.getText();
            descripcion = this.CB_forma_cobro.getSelectionModel().getSelectedItem().toString();  
            cumpleanos = super.GetDateFromDatePicker(DTP_cumpleanos);
            tipo_cliente = this.CB_tipoCliente.getSelectionModel().getSelectedItem().toString(); // se lee el combobox de tipo de cliente
            ubicacion = this.TF_ubicacion.getText();
            
            Va_Cliente vc = new Va_Cliente(); // objeeto para hacer la validacion de los datos
            
            ValidationResult vr = vc.clienteValido(id, nombre, tel1, tel2, tel3,
                    direccion, email, ubicacion, tipo_cliente); // se hace la  validacion de los datos
            
            if(vr.result){ //si los datos son validos se guarda el porducto en la base de datos
                Hndl_Clientes hndc = new Hndl_Clientes();
                
                // se contruye el objeto tipo cliente.
                Cliente c = new Cliente(id, nombre, tel1, tel2, tel3, direccion,
                        email, descripcion, cumpleanos, tipo_cliente, ubicacion);
                
                hndc.codificarCliente(c); // se ingresa el cliente en a base de datos
                super.ShowInfo(super.exito, super.si_operacion, ""); // se notifica al usuario que la operacion fue exitosa
                
            } else{ // en caso contrario se muestra un mensaje indicando la razon del error
                super.ShowInfo(super.no_operacion, super.recti_mess, vr.motivo);
            }
            
        }
        catch (SQLException | ClassNotFoundException ex) { // ventana modal de error en caso de una excepcion.
            Logger.getLogger(FXMLProductosController.class.getName()).log(Level.SEVERE, null, ex);
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        } catch(NullPointerException ex){
            Logger.getLogger(FXMLProductosController.class.getName()).log(Level.SEVERE, null, ex);
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    /**
     * cuando se presiona enter en el boton de cancelar
     * @param ae 
     */
    @FXML
    public void onAction_B_cancelar(ActionEvent ae){
        this.clearFields();
        this.TF_id.requestFocus();
    }
    
    
    /**
     * cuando se hace click en el boton borrar de la seccion de codificacion de clientes.
     * @param me 
     */
    @FXML
    public void onClick_B_cancelar(MouseEvent me){
        this.clearFields();
        this.TF_id.requestFocus();
    }
    
    public void clearFields(){
        this.TF_id.setText("");
        this.TF_nombre.setText("");
        this.TF_tel1.setText("");
        this.TF_tel2.setText("");
        this.TF_tel3.setText("");
        this.TF_direccion.setText("");
        this.TF_email.setText("");
        this.TF_ubicacion.setText("");
        this.DTP_cumpleanos.getEditor().clear();//se limpia el date picker.
    }
    
    
    /**
     * Enter al boton de buscar
     * pestaña busqueda y modificacion de clientes
     * @param ae 
     */
    @FXML
    public void onAction_B_buscar(ActionEvent ae){
        this.BuscarCliente();
    }
    
    /**
     * click en el boton de busqueda.
     * pestaña busqueda y modificacion de clientes
     * @param me 
     */
    @FXML
    public void onClick_B_buscar(MouseEvent me){
        this.BuscarCliente();
    }
    
    
    /**
     * enter en el TextField de buscar cliente
     * @param es 
     */
    @FXML
    public void onAction_TF_buscar(ActionEvent es){
        this.BuscarCliente();
    }
    
    
    /**
     * hace la busqueda de un cliente deacuerdo al criterio de busqueda 
     * seleccionado y pone el resultado en el tableView.
     * pestaña busqueda y modificacion de clientes
     */
    public void BuscarCliente(){
        
        TA_demasDatos.setText(""); // para que al hacer una busqueda no queden datos 
        //que ya no pertenecen a ninguno de los clientes que estan en la tabla.
        
        // se limpia la lista ya que puede demorar mucho tiempo la busqueda y esto evita
        // inconsistencias de datos en la tabla que ya no correspondan con
        // la informacion de la BD por motivo de alguna modificacion o eliminacion
        this.TV_clientes.getItems().clear();
        
        this.Anim_Glyph.Play();// se inicia la animacion de busqueda
        
        // se lee el tipo de busqueda.
        String tp_busqueda = CB_tipoBusqueda.getSelectionModel().getSelectedItem().toString();
        
        String b = TF_buscar.getText(); // se lee la busqueda
        
        Thread t = new Thread(){
            @Override
            public void run(){
                // donde se guardara el resultado de la busqueda
                ArrayList<Cliente> lc = new ArrayList<>();
                
                Hndl_Clientes hndc = new Hndl_Clientes();
                try{
                    // tipo de busqueda por cc
                    if(tp_busqueda.equals(TSEARCH_CC)){ 
                        lc = hndc.buscarCliente_id(b);
                    }
                    
                    if(tp_busqueda.equals(TSEARCH_NOMBRE)){ // tipo de busqueda por nombre
                        lc = hndc.buscarCliente_nombre(b);
                    }
                    
                    SetTV_clientes(lc);
                    Anim_Glyph.Stop();
                    
                } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de una ecepcion java.
                    ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
                }
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        setLabelsBDCli();
                    }
                });
            }
        };
        // se arranca el hilo.
        t.setDaemon(true);
        t.start();
    }
    
    /**
     * metodo que inicializa el combobox de tipo de busqueda.
     * pestaña busqueda y modificacion de clientes
     */
    public void CargarTipoBusqueda(){
        ArrayList<String> ltb = new ArrayList<>();
        ltb.add(this.TSEARCH_CC);
        ltb.add(this.TSEARCH_NOMBRE);
        super.SetComboBox(CB_tipoBusqueda, ltb);
    }
    
    
    
    
    
    /**
     * enter boton borrar
     * borrar textfield de busqueda.
     * pestaña de busqueda y codificacion
     * @param ae 
     */
    @FXML
    public void onAction_B_borrar(ActionEvent ae){
        this.TF_buscar.clear();
        this.TF_buscar.requestFocus();
    }
    
    /**
     * click boton borrar
     * pestaña de busqueda y codificacion
     * @param ae 
     */
    @FXML
    public void onClick_B_borrar(MouseEvent ae){
        this.TF_buscar.clear();
        this.TF_buscar.requestFocus();
    }
    
    
    /**
     * se activa
     * @param me 
     */
    @FXML
    public void OnClickedCBoxSearchProduct(MouseEvent me){
        
    }
    
    
    /**
     * enter en el boton de modificar
     * pestaña de busqueda y modificacion de clientes
     * @param ae 
     */
    @FXML
    public void onAction_B_Modificar(ActionEvent ae){
        // se inhabilito el enter y se dejo solo ,la accion de click
        // ya que en ubuntu ocurren los 2 simultaneamente.
        // en windows no ocurre pero es complentamente apropiado
        // realizar una operacion de boton solo con el click.
    }
    
    
    /**
     * click en el boton de modificar
     * pestaña de busqueda y modificacion de clientes
     * @param ae 
     */
    @FXML
    public void onClick_B_Modificar(MouseEvent ae){
        this.PopUpModificarCliente(ae);
    }
    
    
    /**
     * metodo que abre una ventana modal para hacer modificacion de un cliente
     * seleccionado en el tableView 
     * @param event
     */
    public void PopUpModificarCliente(Event event){
        if(TV_clientes.getSelectionModel().isEmpty()){
            // se notifica al usuario que no hay item seleccionado de la tabla
            this.ShowWarning(SuppMess.NO_SEL_ITEMTAB_TITLE, 
                    SuppMess.NO_SEL_ITEMTAB_SUB, SuppMess.NO_SEL_ITEMTAB_MESS);
        } else if(!TV_clientes.getSelectionModel().getSelectedItem().isGeneric()){ 
            // en caso de haber cliente seleccionado se procede a lanzar 
            // el pop up de modificacion si no se trata del cliente generico. id=123.
            SptCliente cli = TV_clientes.getSelectionModel().getSelectedItem();
            
            try {
                // se caraga el path del FXML
                FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                        getResource("tentactildesktoppos/FXMLPopUps/FXMLModCliente.fxml"));
                Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
                Stage st = new Stage();// modificacion
                st.setScene(new Scene(root));
                st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
                st.initOwner( ((Node) event.getSource()).getScene().getWindow());
                FXMLModClienteController c = cargador.<FXMLModClienteController>getController(); // se obtiene el controlador
                c.init(cli, this);// se inicializan los valores de la ventana
                st.show(); // se muestra la ventana
                
            } catch (IOException ex) {
                Logger.getLogger(FXMLModClienteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /**
     * populates the table with clients found in the search method.
     * @param lc
     */
    public void SetTV_clientes(ArrayList<Cliente> lc)
            throws SQLException, ClassNotFoundException{
        ArrayList<SptCliente> spt_lc = this.fromCliente2Spt(lc);
        ObservableList<SptCliente> ovl_lc = FXCollections.observableArrayList(spt_lc);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                TV_clientes.setItems(ovl_lc);
            }
        });
    }
    
    
    
    /**
     * transforma de Cliente a SPT.
     * necesario para popular la tabla de resultados de busqueda de cliente
     * @param lc
     * @return 
     */
    public ArrayList<SptCliente> fromCliente2Spt(ArrayList<Cliente> lc) 
            throws SQLException, ClassNotFoundException{
        ArrayList<SptCliente> lspt = new ArrayList<>();
        
        for(int i=0; i<lc.size(); i++){
            lspt.add(new SptCliente(lc.get(i)));
        }
        return lspt;
    }
    
    
    /**
     * configura las columnas del TaableView. es necesario.
     * pestaña de busqueda y modificacion de clientes
     */
    public void ConfigurarTVclientes(){
        this.TC_id.setCellValueFactory(new PropertyValueFactory<>("spt_id"));
        this.TC_nombre.setCellValueFactory(new PropertyValueFactory<>("spt_nombre"));
        this.TC_UltAbono.setCellValueFactory(new PropertyValueFactory<>("spt_last_abono"));
        this.TC_Total.setCellValueFactory(new PropertyValueFactory<>("spt_consolidado"));
        this.TC_TpCobros.setCellValueFactory(new PropertyValueFactory<>("spt_tipo_cobro"));
    }
    
    
    /**
     * cuando se presiona una tecla y esta se suelta, se invoca este metodo.
     * su funcion es abarcar los casos en los que se cambia el elemento selecionado
     * de lla tabla usando las flechas del teclado, en cuyo caso se debe mostrar
     * en el TextArea los datos restantes del cliente
     * @param ke 
     */
    @FXML
    public void onKeyRes_TV_clientes(KeyEvent ke){
        this.ShowRemainingData();
    }
    
    
    /**
     * cubre el caso de cambio de item en la tabla mediante click.
     * el metodo detecta cuando se hace click en la tabla y se verifica entonces
     * que objeto esta seleccionado para mostrar los datos restantes del cliente
     * seleccionado
     * @param me 
     */
    @FXML
    public void onClick_TV_clientes(MouseEvent me){
        this.ShowRemainingData();
        
        /**
         * si se hace doble click y si ahy unrecord seleccionado, entonces se muestra el
         * respectivo consolidado.
         */
        if(me.getClickCount()==2){ // si se hace dobleClick
            this.OpenConsolidadoWindow(me); // se abre una ventana modal
            // para mostrar los records de la factura seleeccionada.
        }
    }
    
    
    /**
     * abre una ventana modal que muestra el consolidado del cliente seleccionado
     */
    public void OpenConsolidadoWindow(Event ev){
        
        SptCliente sel_cli = TV_clientes.getSelectionModel().getSelectedItem();
        
        if(!(sel_cli==null)){ // en caso de que la factura seleccionada on sea nula
            try{
            FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                        getResource("tentactildesktoppos/FXMLPopUps/FXMLSeeConsolidado.fxml"));
                Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
                Stage st = new Stage();// modificacion
                st.setScene(new Scene(root));
                st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
                st.initOwner( ((Node) ev.getSource()).getScene().getWindow());
                FXMLSeeConsolidadoController c = cargador.<FXMLSeeConsolidadoController>getController(); // se obtiene el controlador
                
                c.CustomInit(sel_cli);// se inicializan los valores de la ventana
                st.show(); // se muestra la ventana
            } catch( IOException ex){
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
                
        } else{ // si no hay factura seleccioanada no se hace nada.
            
        }
    }
    
    
    
    /**
     * este metodo revisa que elemento de la TableView esta seleccionado para
     * mostrar los datos restantes del cliente seleccionado.
     * en caso de no tener ningun ellemento seleccionado entonces se pone
     * el textArea vacio.
     */
    public void ShowRemainingData(){
        SptCliente sptc = this.TV_clientes.getSelectionModel().getSelectedItem();
        
        try{
            this.TA_demasDatos.setText(sptc.retrieveRemainingData());
        } catch(NullPointerException ex){ // si no hay elemento seleccionado
            this.TA_demasDatos.setText("");
        }
    }
    
    
    /**
     * ver javadoc en la cabecera de la definicion del metodo abstracto en la superclase para mas informacion.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.TF_buscar.getScene().getWindow();
    }
    
    
    
    /**
     * metodo que configura click derecho en la tabla de clientes.
     * La unica opcion disponible por ahora es la de enviar el cc del cliente seleccionado
     * a la seccion de ventas. Se trata solo de comodidad y velocidad pero no es una funcion escencial.
     */
    public void ConfigurarMenuItem_rgClick(){
        rg_click_menu = new ContextMenu();
        
        MenuItem menu_Send_Client = new MenuItem(SEND_CLIENT);
        
        MenuItem menu_Print_Cupon = new MenuItem(PRINT_CUPONES_ACTIVOS);
        
        MenuItem menu_Copy_cc = new MenuItem(COPY_CC);
        
        MenuItem menu_See_Factu = new MenuItem(SEE_FACTURAS);
        
        MenuItem menu_Print_consolidado = new MenuItem(PRINT_CONSOLIDADO);
        
        /**
         * cuando se selecciona enviar un cliente a la seccion de ventas.
         */
        menu_Send_Client.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_SendClient();
            }
        });
        
        /**
         * cuando se selecciona imprimir los cupones activos del cliente seleccionado
         */
        menu_Print_Cupon.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_PrintCuponesSelCli();
            }
        });
        
        /**
         * cuando se selecciona imprimir los cupones activos del cliente seleccionado
         */
        menu_Copy_cc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_CopyCC();
            }
        });
        
        /**
         * cuando se selecciona imprimir los cupones activos del cliente seleccionado
         */
        menu_Print_consolidado.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_PrintConsolidado();
            }
        });
        
        
        menu_See_Factu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ActionMenu_IrFactu();
            }
        });
        
        // se agregan a menu las opciones configuradas.
        rg_click_menu.getItems().add(menu_Send_Client);
        rg_click_menu.getItems().add(menu_Print_Cupon);
        rg_click_menu.getItems().add(menu_Copy_cc);
        rg_click_menu.getItems().add(menu_See_Factu);
        rg_click_menu.getItems().add(menu_Print_consolidado);
        
        this.TV_clientes.setContextMenu(rg_click_menu);
    }
    
    
    /**
     * metodo que permite ver directamente las facturas del cliente seleccionado en la
     * table view de la seccion de gestion de cobros.
     */
    public void ActionMenu_IrFactu(){
        Cliente cli = TV_clientes.getSelectionModel().getSelectedItem();
        if(cli == null) { // si no hay cliente seleccionado
            
        } else{ // en caso d que si haya cliente seleccionado
            
            // se obtiene una instancia del controlador injectado de la seccion de abonos y deboluciones.
            FXMLAbon_DevController abdcont = this.MainDocController.abon_devTabController;
            
            // se pone la informacion del cliente en el tf de busqueda de facturas
            // y se ahce la busqueda por cliente id.
            abdcont.CB_tipo_busqueda.getSelectionModel().select(abdcont.BUSQUEDA_X_CLIENTE_ID);
            abdcont.TF_SEARCH_buscar_factura.setText(cli.id);
            abdcont.BuscarFacturas_Gcobros(); // se hace la busqueda de las facturas del cliente seleccionado
            
            // se selecciona el tab de abonos y devoluciones
            super.MainDocController.TabPaneFather.getSelectionModel().select(super.MainDocController.TabAbonDev);
            // se pone el titled pane de gestion de cobros.
            abdcont.AccordionAbonDev.setExpandedPane(abdcont.TLPane_GenCob);
        }
        
    }
    
    /**
     * Cuando se selecciona del menu item copiar la cedula del cliente selccionado.
     */
    public void Action_menu_CopyCC(){
        Cliente cli = TV_clientes.getSelectionModel().getSelectedItem();
        if(cli == null) { // si no hay cliente seleccionado
            
        } else{ // en caso d que si haya cliente seleccionado
            super.Copiar2ClipBoard(cli.id);
        }
    }
    
    
    /**
     * metodo que envia el cliente seleccionado en el table view a la seccion de ventas.
     */
    public void Action_menu_SendClient(){
        Cliente cli = TV_clientes.getSelectionModel().getSelectedItem();
        if(cli == null) { // si no hay cliente seleccionado
            
        } else { // e caso de haber cliente seleccioado ->
            this.ventasController.clearVenta(); // se limpia para evitar conflicto en caso de haber venta en curso.
            this.ventasController.TF_buscar_cliente.setText(cli.id);
            // se invoca el metodo para que se establezca como
            // cliente seleccionado en la seccion de ventas.
            this.ventasController.SearchAndSetCliente(); 
            
            super.MainDocController.TabPaneFather.getSelectionModel().select(super.MainDocController.TabVentas);
            super.MainDocController.ventasTabController.TF_buscarProducto.requestFocus();
        }
    }
    
    
    /**
     * Trae de la base de datos todas las facturas del cliente que tengan saldo
     * mayor a zero y las imprime junto con el neto.
     */
    public void Action_menu_PrintConsolidado(){
        Cliente cli = TV_clientes.getSelectionModel().getSelectedItem();
        if(cli == null) { // si no hay cliente seleccionado
            
        } else { // en caso de haber cliente seleccioado ->
            
            try{
                String local_cc = cli.id; // cc del cliente.
                
                // se instancia conector a la tabla de facturas.
                Hndl_Factura hndfac = new Hndl_Factura();
                
                // se traen de la bd todas las facturas del cliente local_cc con saldo diferente de zero.
                ObservableList<SptFactura> lsfac = hndfac.getFacturasByClienteID_saldo_no_zero(local_cc);
                
                if(lsfac.isEmpty()){ // se notifca al usuario si el clietne seleccionado no tiene ningun saldo pendiente
                    super.ShowInfo( "", cli.nombre, "No tiene saldo pendiente");
                } else { // se imprime el consolidado en caso contrario
                    LocalPrinter lp = new LocalPrinter();
                    lp.PrintConsolidado(cli, lsfac); // se imprime el consolidado del cliente.
                }
            } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de un error por SQL, se supone que no sebe ocurrir
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            } catch (RuntimeException ex) {
                Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrintException ex) {
                Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /**
     * imprime todos los cupones activos pertenecientes al cliente seleccionado.
     * este metodo se invoca cuando se da click en el correspondiente item menu
     * de la tabla de busqueda de clientes.
     */
    public void Action_menu_PrintCuponesSelCli(){
        SptCliente sel_cli = TV_clientes.getSelectionModel().getSelectedItem();
        try{
            if(!(sel_cli==null)){ //si el cliente seleccionado es no nulo
                Hndl_Cupon hndcup = new Hndl_Cupon();
                ArrayList<Cupon> ls_cup = hndcup.getCuponesActivosDelCliente(sel_cli.id);
                
                LocalPrinter lp = new LocalPrinter();
                if(!ls_cup.isEmpty()){ // se imprimen los cupones del cliente
                    for(Cupon cup : ls_cup){
                        lp.ImprimirCupon(cup);
                    }
                } else{ // se notifica al usuario en caso de que no hayan cupones activos.
                    super.ShowInfo("", sel_cli.nombre, "No tiene cupones activos");
                }
            }
        } catch(SQLException | ClassNotFoundException ex){
            
        } catch (RuntimeException ex) {
            Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrintException ex) {
            Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLClientesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * cuando se oprime enter y el combo box de tipo
     * de busqueda de cliente esta seleccionado.
     * @param ae 
     */
    @FXML
    public void onActionCBoxTPSearch(ActionEvent ae){
        this.TF_buscar.requestFocus();
    }
    
    /**
     * cuando se oprime enter y el combo box de filtro tiene focus.
     * @param ae 
     */
    @FXML
    public void onActionCBFIlter(ActionEvent ae){
        this.TF_buscar.requestFocus();
    }
    
    
    /**
     * metodo que carga la informacion correspondiente a los labels que estan arriba del text area
     * que muestra la informacio del cliente seleccinado.
     * 
     * estos labels muestran conteos de los clientes.
     * la cantidad total de los clientes en la base de datos.
     * cuantos clientes hay de pormayor y credito
     * cuantos clientes vienen a pagar y cuantos se madan a cobrar.
     */
    public void setLabelsBDCli(){
        try{
            Hndl_Clientes hndcli = new Hndl_Clientes();
            this.LB_CountAll.setText(Integer.toString(hndcli.CountAll()));
            
            this.LB_Count_mayor.setText(Integer.toString(hndcli.CountByTipo(Cliente.TIPO_MAYOR)));
            this.LB_Count_credito.setText(Integer.toString(hndcli.CountByTipo(Cliente.TIPO_CREDITO)));
            
            this.LB_Count_cobrar.setText(Integer.toString(hndcli.CountByDescri(Cliente.HACER_COBRO)));
            this.LB_Count_vienen.setText(Integer.toString(hndcli.CountByDescri(Cliente.VIENE_PAGAR)));
            
        } catch(SQLException | ClassNotFoundException ex){// se notifica al usuario en caso de una excepcion.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
}
