package tentactildesktoppos.FXMLinjection;

import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.Hndl_Cupon;
import BD_tdpos_r.SQL_Configuracion;
import Impresion.LocalPrinter;
import Validator_tdpos_r.Va_Cupon;
import Validator_tdpos_r.ValidationResult;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import javax.print.PrintException;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;

import tentactildesktoppos.FXMLPopUps.FXMLPopUpMailConfigController;
import tentactildesktoppos.negocio_r_objs.Cupon;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLConfiguracionController extends ControllerFather{
    
    
    /**
     * para poder saber desde este controlador si hay una venta en curso
     * y congelar las funcionalidades necesarias para asegurar la consistencia
     * de los datos mostrados en la interafz grafica.
     */
    public FXMLVentasController ventasController;
    
    @FXML
    public TextField tf_printer_sticker_conf;

    // Icono para indicar modificado/guardado para el nombre de la impresora de adhesivos
    @FXML
    public FontAwesomeIcon icon_sticker;

    // campo de texto para el nombre de la impresora
    @FXML
    public TextField tf_printer_receipt_conf;
    
    // Icono para indicar modificado/guardado para el nombre de la impresora de facturas
    @FXML
    public FontAwesomeIcon icon_receipt;
    
    
    /**
     * combo box para seleccionar el tipo de impresion para la facturas.
     * impresion sintesis. (ahorra mas papel y puede ser mas clara.
     * impresion historia.
     */
    @FXML
    public ComboBox<String> ChcBox_TipoImpresion; 
    
    /**
     * textfield para establecer el maximo numero de caracteres por linea que permite 
     * la impresora e recibos y el ancho de papel seleccinoado.
     */
    @FXML
    private TextField tf_receipt_charxline_conf;

    
    // Icono para indicar modificado/guardado para el numero de caracteres por linea en la impresora de facturas.
    @FXML
    public FontAwesomeIcon icon_charxline;

    
    @FXML
    public TextField tf_iva_conf;

    
    // Icono para indicar modificado/guardado para el valor de iva por defecto.
    @FXML
    public FontAwesomeIcon icon_iva;
    
    /**
     * label para mostrar la direccion de correo actualmente configurada.
     */
    @FXML
    public Label LB_ConfigEmail;
    
    
    /**
     * toggle button que permite aplicar o remover un lock para la modificacion
     * de productos. cuando esta undido (locked) no sse puede hacer modificacion
     * de productos.
     * para desbloquearlo exige un DynPassCheck
     */
    @FXML
    public ToggleButton TGB_Lock;
    
    
    /**
     * boton que se usa para inhabilitar un cupon
     */
    @FXML
    public Button B_DisableCupon;
    
    /**
     * textField para ingresar los datos del cupon 
     * e inhabilitarlo
     */
    @FXML
    public TextField TF_DisableCupon;
    
    
    /**
     * Nombre de identificaion del glipho de signo de admiracion
     */
    public final String ICON_CHANGED = "EXCLAMATION";
    
    
    /**
     * nombre del glipho del signo de chulo. (ver documentacion de font awesome fx.)
     */
    public final String ICON_SAVED = "CHECK";
    
    /**
     * mensaje de error que indica al usuario que no existe en el OS ninguna impresora con el nombre 
     * especificado.
     */
    public final String WRONG_PRINTER_NAME = "Porfavor revice el asistente de impresoras de su sistema operativo.\n"
            + "es posible que el nombre configurado para su impresora configurado en esta aplicacion sea incorrecto.";
    
    /**
     * mensaje de error que indica al usuario que hay un problema con la impresora a pesar que el 
     * nombre de la misma si este bien configurado en el campo de texto.
     */
    public final String PRINTER_PROBLEM = "No se ha podido Hace la Impresion, Porfavor rectifique que la misma este encendida\n"
            + "y ademas tenga suficiente papel. Para descartar un problema fisico porfavor\n"
            + " haga una impresion de PRUEBA como se indica a continuacion:\n"
            + "1. apague la impresora.\n"
            + "2. con la impresora apagada presione el boton \'Feed\' (el que permite sacar papel sin imprimir)\n"
            + "3. si dejar de presionar el boton de Feed encienda nuevamente la impresora y no suelte el boton de Feed\n"
            + "hasta que esta vuelva a imprimir\n\n"
            + "Si no logra hacer una impresion de prueba siguiendo los pasos anteriores es posible de que la \n"
            + "impresora tenga un problema de hardware y debe sr llevada a mantemiento.";
    
    
    /**
     * Conset encapsula el campo de texto, el icon view, y la propiedad de la tabla de configuracion.
     * de esta forma se pueden relacionar estos 3 elementos cuando se dispare un OnChangeValue Listener.
     */
    public ConfSet PrinterSticker, PrinterRecipt, IvaX_Defecto, CharXline;
    
    /**
     * Lista de las opciones del combo Box de seleccion de tipo de impresion.
     * 
     */
    public ArrayList<String> tipoImpresion;

    @FXML
    public void initialize() {
        
        // se inicializan los ConfSet, ver comentario al inicio de la definicion de la clase, en este mismo archivo.
        this.PrinterRecipt = new ConfSet(icon_receipt, tf_printer_receipt_conf, SQL_Configuracion.PRT_IMPR_FACTURA);
        this.PrinterSticker = new ConfSet(icon_sticker, tf_printer_sticker_conf, SQL_Configuracion.PRT_IMPR_STICKER);
        this.IvaX_Defecto = new ConfSet(icon_iva, tf_iva_conf, SQL_Configuracion.PRT_IVA);
        this.CharXline = new ConfSet(icon_charxline, tf_receipt_charxline_conf, SQL_Configuracion.PRT_FACT_MAX_LINES);
        
        // se configura el combo box e tipo de impresion para los recibos.
        this.ConfigCBoxTipoImpresion();
        
        // se agrega un listener en caso de que se cambie el valor selecciondo para el combo box
        this.ChcBox_TipoImpresion.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValues) ->{
            OnPrintTypeChange();
        });
        
        
        /**
         * listener a cambios de del toggle button para hacer lock de las modificaciones.
         */
        this.TGB_Lock.selectedProperty().addListener((observable, oldValue, newValues) ->{
            LockLogic();
        });
        
        // se carga el email que esta configurado.
        // y el estado del toggle button que se guarddo en la bd
        try{
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            this.LB_ConfigEmail.setText(hndconf.getConfiguracion(SQL_Configuracion.PRT_DESTINATION_MAIL));
            if(hndconf.getConfiguracion(SQL_Configuracion.PRT_LOCKED).equals(SQL_Configuracion.LOCKED)){
                this.TGB_Lock.setSelected(true);
                this.TGB_Lock.setText("Bloqueado");
            }
            else{
                this.TGB_Lock.setSelected(false);
                this.TGB_Lock.setText("Desbloqueado");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * En el metodo de inicializacion del controlador se agrega un listener para disparar
     * este metodo cada que se cambie el item seleccionado en el combo box que establece el
     * tipo de impresion (sintesis o historia) de la impresora de recibos.
     * 
     * se toma el valor seleccionado y se actualiza en la tabla de configuracion con 
     * su respectiva propiedad en el objeto SQL de configuracion.
     */
    public void OnPrintTypeChange(){
        try { // se toma el valor seleccionado en el combo box de tipo de impresion y se 
            // se pone en la tabla de configuracion en la base de datos.
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            hndconf.updateConf(SQL_Configuracion.PRT_PRINT_TYPE, ChcBox_TipoImpresion.getSelectionModel().getSelectedItem());
            ShowInfo("", "", ChcBox_TipoImpresion.getSelectionModel().getSelectedItem());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FXMLConfiguracionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * metodo que actualiza los iconos de 
     * @param cf
     * @param kc 
     */
    public void UpdateFieldState(ConfSet cf, KeyCode kc){
        if(kc.equals(KeyCode.ENTER)){ // si la tecla presionada es enter
            // se actualiza el valor en la tabla de facturas
            try{
            cf.icon.setGlyphName(this.ICON_SAVED); // se pone el icono de chulo que indica valor guardado
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            hndconf.updateConf(cf.confProperty, cf.tf.getText()); // se hace update de la fila correspondiente en conf. table
            } catch(SQLException | ClassNotFoundException ex){
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
        } else{ // si la tecla presionada no es enter.
            cf.icon.setGlyphName(this.ICON_CHANGED);// se cambia a icono se signo de admiracion para indicar
            // que el valor del campo de texto difiere del valor en BD que debe presionarse enter para actualizarlo.
        }
    }

    /**
     * ver en la clase super, la definicion abstracta de este metodo. se explica en datalle en elos comentarios
     * de la declaracion abstracta.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.tf_iva_conf.getScene().getWindow();
    }

    
    /**
     * metodo para cargar los Strings del combo box de seleccion de tipo de impresion.
     * Se establece como elemento seleccionado el valor que este en la tabla de configuracion.
     */
    private void ConfigCBoxTipoImpresion() {
        // se configura el combo Box de tipo de cliente
        this.tipoImpresion = new ArrayList<>();
        this.tipoImpresion.add(SQL_Configuracion.PRINT_OPTION_SINTESIS);
        this.tipoImpresion.add(SQL_Configuracion.PRINT_OPTION_HISTORIA);
        super.SetComboBox(this.ChcBox_TipoImpresion, tipoImpresion);
        try{
            // conector a la tabla de configuracion.
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            // se lee el valor configurado por el usuario.
            ChcBox_TipoImpresion.getSelectionModel().select(hndconf.getConfiguracion(SQL_Configuracion.PRT_PRINT_TYPE));
            
        } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de excepcion por acceso a la base de datos.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }

    
    
    /**
     * metodo que se invoca cada que se cambia el valor del boton que permite hacer lock de las
     * modificaciones.
     */
    public void LockLogic() {
        boolean locked = this.TGB_Lock.isSelected();
        try{
            /**
             * el metodo se invoca cuando hay un cambio en el valor.
             * si locked es tru, es porque no hay lock antes de hacer click en el boton.
             * es decir que el usuario esta activando el lock y por tanto no se debe hacer nada.
             */
            if(locked) {
                // se pone en el texto del toggle button que se ha bloqueado
                this.TGB_Lock.setText("BLOQUEADO");
                // se guarda esta configuracion en la tabla de configuraciones
                Hndl_Configuracion hndconf = new Hndl_Configuracion();
                hndconf.updateConf(SQL_Configuracion.PRT_LOCKED, SQL_Configuracion.LOCKED);
            } else {
                
                // si el cambio se da a no locked entonces es porque el lock estaba activado.
                // portanto hay que hacer un DynPassCheck.
                if(DynPassCheck()){ // si la clave es correcta entonces se permite levantar el lock
                    TGB_Lock.setText("DESBLOQUEADO");
                    // se guarda en configuracion que las modificaciones de stock estan
                    // desbloqueadas.
                    Hndl_Configuracion hndconf = new Hndl_Configuracion();
                    hndconf.updateConf(SQL_Configuracion.PRT_LOCKED, SQL_Configuracion.NOT_LOCKED);
                } else { // si el checkeo es incorrecto entoces se debe poner nuevamente el lock.
                    TGB_Lock.setSelected(true);
                }
            }
        }catch (SQLException | SocketException | ClassNotFoundException | MessagingException ex) {
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            TGB_Lock.setSelected(true);
        }
    }
    
    
    
    
    /**
     * es un objeto que encapsula el campo de texto, su icono y su valor de configuracion.
     * cuando se invoca su constructor se establecen los valores correspondientes de sus atributos, se
     * agrega un listener al campo de texto y finalmente se carga el valor actual de para la respectiva configuracion
     * en el campo de texto.
     */
    public class ConfSet{
        /**
         * icono asociado al campo de texto tf.
         */
        public FontAwesomeIcon icon;
        /**
         * campo de texto asociado a la propiedad confProperty
         */
        public  TextField tf;
        /**
         * llave primaria de la configuracion correspondiente en la tabla de configuracion
         */
        public String confProperty;
        
        /**
         * constructor que no solo inicializa los atributos sino que tambien
         * agrega un Listener a cambio de contenido del textfield que se encapsule.
         * tambien carga el valor actual que se tiene en la tabla e configuracion para el parametro correspondiente
         * @param icon
         * @param tf
         * @param confProperty 
         */
        public ConfSet(FontAwesomeIcon icon, TextField tf, String confProperty){
            this.icon = icon;
            this.tf = tf;
            this.confProperty = confProperty;
            
            loadKey(this.confProperty);
            
            this.tf.textProperty().addListener((observable, oldValue, newValues) ->{
                notifyChange();
            });
        }
        
        /**
         * cuando se dispara el metodo siempre se trata de un cambio,
         * siempre se debe establecer el mismo iconView
         */
        public void notifyChange(){ // se pone glyfo de admiracion en el iconView
            this.icon.setGlyphName(ICON_CHANGED);
        }
        
        /**
         * se carga el valor guardado en la tabla de configuraciones
         * correspndiente.
         */
        public void loadKey(String confProperty){
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            try{
                this.tf.setText(hndconf.getConfiguracion(confProperty));
            } catch(SQLException | ClassNotFoundException ex){
                this.tf.setText(ex.getMessage());
            }
        }
    }
    
    
    /**
     * cuando se presiona cualquier tecla y el campo de iva por defecto tiene focus
     * @param event 
     */
    @FXML
    void onKeyPress_tf_iva_conf(KeyEvent event) {
        this.UpdateFieldState(IvaX_Defecto, event.getCode());
    }

    /**
     * cuando se presiona cualquier tecla y el campo del nombre de la impresora de facturas tiene focus
     * @param event 
     */
    @FXML
    void onKeyPress_tf_printer_receipt_conf(KeyEvent event) {
        this.UpdateFieldState(this.PrinterRecipt, event.getCode());
    }

    /**
     * cuando se presiona cualquier tecla y el campo del nombre de la impresora de stickers tiene focus
     * @param event 
     */
    @FXML
    void onKeyPress_tf_printer_sticker_conf(KeyEvent event) {
        this.UpdateFieldState(PrinterSticker, event.getCode());
    }
    
    
    /**
     * cuando se presiona cualquier tecla y el campo del numero maximo de caracter por linea tiene focus.
     * @param event 
     */
    @FXML
    void onKeyPress_tf_receipt_charXline_conf(KeyEvent event) {
        this.UpdateFieldState(CharXline, event.getCode());
    }
    
    /**
     * cuando se hace click en el boton de prueba de la impresora de recibos.
     * se hace una impresion de caracteres.
     * @param mv 
     */
    @FXML
    void onClickReceiptPrintTest(MouseEvent mv){
        
        try {
            LocalPrinter prt = new LocalPrinter();
            prt.PRT_Factura_Test();
        } catch ( RuntimeException ex) { // RunTime corresponderia a un nombre errado de la 
            // impresora (se debe poner el mismo que se seleccione en el OS. En el caso de un PrintException
            // se trataria de un problema de papel, daño de la impresora entre otros relacionados directamente
            // con el envio del printJob a la impresora (a pesar de que el printService o nombre de la impresora en 
            // el OS si este correcto).
            super.ShowInfo(super.no_operacion, "", this.WRONG_PRINTER_NAME);
        } catch (PrintException ex) {
            super.ShowInfo(super.no_operacion, "", this.PRINTER_PROBLEM);
        } catch(ClassNotFoundException | SQLException | IOException ex){ // si ocurre una exepcion por conexion
            // a BD o por el uso de InpusStream, se trataria de un bug. se notifica la exepcin en una ventana modal.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * cuando se hace click en el bonton de configuracion de email
     * @param me 
     */
    public void onClickConfigMail(MouseEvent me){
        this.PopUpConfigMailCheck(me);
    }
    
    
    /**
     * metodo que se encarga de checkear si se puede o on abrir el
     * panel de configuracion de email en cuyo caso se procede a 
     * abrir el panel de configuracion.
     */
    public void PopUpConfigMailCheck(Event me){
        try{
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            String SenderEmail = hndconf.getConfiguracion(SQL_Configuracion.PRT_SENDER_MAIL);
            
            // si no hay configurado ningun correo entonces
            // no se hace DynPass Check.
            if(SenderEmail.isEmpty()){ // se abre el panel de configuracion de email.
                this.OpenConfigMailPanel(me);
            } else{// si el email no esta vacio, ya fue configurado.
                // por tanto se debe hacer un DynPassCheck
                if(super.DynPassCheck()){ // solo si el checkeo dinamico funciona
                    // se abre el panel de configuracion
                    this.OpenConfigMailPanel(me);
                }
            }
            
        } catch(SQLException | ClassNotFoundException | MessagingException | IOException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * abre el la ventana modal para hacer configuracion del email
     * y cuenta de google para la generacion de clave dinamica.
     * @param event
     */
    public void OpenConfigMailPanel(Event event) throws IOException{
        FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                getResource("tentactildesktoppos/FXMLPopUps/FXMLPopUpMailConfig.fxml"));
        Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
        Stage st = new Stage();// modificacion
        st.setScene(new Scene(root));
        st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
        st.initOwner( ((Node) event.getSource()).getScene().getWindow());
        FXMLPopUpMailConfigController c = cargador.<FXMLPopUpMailConfigController>getController(); // se obtiene el controlador
        c.setConfCont(this);// se inicializan los valores de la ventana
        st.show(); // se muestra la ventana
    }
    
    
    /**
     * cuando se hace click en el boton de inhabilitar cupon.
     * se usa para inhabilitar el cupon seleccionado
     * @param me 
     */
    @FXML
    public void onClickDisableCupon(MouseEvent me){
        try {
            String cup_info = this.TF_DisableCupon.getText();
            // conector a la tbla de cupones.
            Hndl_Cupon hndcup = new Hndl_Cupon();
            
            Va_Cupon vacup = new Va_Cupon();
            ValidationResult vr = new ValidationResult();
            
            // se revisa que los datos ingresados sean validos para hacer la inhabilitacion.
            // a diferencia de la seccion de ventas aqui solo se valida id y clave aleatoria.
            vr = vacup.EsValidoInhabilitarCupon(cup_info);
            
            // en caso de ser valida la operacio se procede con nla misma
            if(vr.result){
                Cupon cupo = (Cupon) vr.obj_carry;
                /**
                 * se desactiva el cupon y se le asigna la factura 0, que es una factura vacia para evitar
                 * el valor null.
                 */
                hndcup.DesactivarCupon(cupo, 0);
                
                super.ShowInfo(exito, "", "El Cupon Especificado ha sido desactivado");
            } else{ // se notifica al usuario que los datos ingresados no son validos para hacer
                // una inhabilitacion.
                super.ShowInfo("", "", vr.motivo);
            }
            // se notifica la excepcion en caso de haberla
        } catch (ClassNotFoundException | SQLException ex) {
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
}