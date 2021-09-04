package tentactildesktoppos.FXMLPopUps;



import BD_tdpos_r.Hndl_Clientes;
import Validator_tdpos_r.Va_Cliente;
import Validator_tdpos_r.ValidationResult;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLinjection.FXMLClientesController;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptCliente;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLModClienteController extends ControllerFather{
    
    
    /**
     * Cliente seleccionado para hacer la modificacion desde el acordeon 
     * de busqueda
     */
    public SptCliente cli;
    
    
    /**
     * campo de texto de nit o cc en la ventana modal de modificacion de cliente
     */
    @FXML
    TextField tf_ccnit_modcli;

    /**
     * campo de texto para modificar el nombre completo del cliente
     */
    @FXML
    private TextField tf_fname_modcli;

    /**
     * campo de texto para moodificar los telefonos
     */
    @FXML
    private TextField tf_tel1_modcli;

    @FXML
    private TextField tf_tel2_modcli;

    @FXML
    private TextField tf_tel3_modcli;

    /**
     * campo de texto para modifcar el email de un cliente
     */
    @FXML
    private TextField tf_email_modcli;

    /**
     * modificacion del tipo de cliente y del tipo de pago
     */
    @FXML
    private ComboBox cb_tipocli_modcli;

    @FXML
    private ComboBox cb_tipo_cobro_modcli;

    /**
     * para modificar el cumpleaños del cliente
     */
    @FXML
    private DatePicker dt_cumple_modcli;

    /**
     * area de texto donde se meustran los datos actuales del cliente en la BD
     */
    @FXML
    private TextArea ta_datos_actuales_modcli;

    /**
     * para modificar la direccion del cliente
     */
    @FXML
    private TextField tf_direccion_modcli;

    /**
     * para modificar la descripcion de la direccion del cliente
     */
    @FXML
    private TextField tf_ubicacion_modcli;
    
    @FXML
    public Button b_cancelar;
    
    @FXML
    public Button b_reset;
    
    @FXML
    public Button b_HacerModificacion;
    
    @FXML
    Button b_Eliminar;
    
    /**
     * mensaje que se usa por este controlador en las ventanas modales cuando
     * la operacion realizada por el usuario ha sido exitosa
     */
    public final String EXITO_TITLE = "Operacion Exitosa";
    
    /**
     * mensaje para ventana modal que se usa cuando se ha eliminado exitsamente
     * el cliente especificado.
     */
    public final String EXITO_DELETE_MESS = "El cliente especificado ha sido eliminado";
    
    public final String EXITO_MOD_MESS = "El cliente seleccionado ha sido modificado exitosamente";
    
    
    /**
     * instancia del controlador de clientes, responsable de lanzar este dialogo de
     * modificacion de clietnes. Esta insancia se usa en este controlador para 
     * refrescar el table view de busqueda de clientes en caso de una modificacion
     * ya que de no hacerlo podrian aparecer inconsistencias con valores 
     * previamente mostrados en la tabla que ya fueron modificados
     */
    public FXMLClientesController clicont;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        
    }
    
    
    public void init(SptCliente cli, FXMLClientesController clicont){
        this.cli = cli;
        this.clicont = clicont;
        
        // se cargan los valores correspondientes de los combo box
        this.clicont.CargarTipoPago(cb_tipo_cobro_modcli);
        this.clicont.CargarTiposClientes(cb_tipocli_modcli);
        
        SetData();
    }
    
    
    
    @FXML
    void onAction_cb_tipo_cobro_modcli(ActionEvent event) {
        this.dt_cumple_modcli.requestFocus();
    }

    @FXML
    void onAction_cb_tipocli_modcli(ActionEvent event) {
        this.cb_tipo_cobro_modcli.requestFocus();
    }

    @FXML
    void onAction_dt_cumple_modcli(ActionEvent event) {
        this.tf_direccion_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_ccnit_modcli(ActionEvent event) {
        this.tf_fname_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_direccion_modcli(ActionEvent event) {
        this.tf_ubicacion_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_email_modcli(ActionEvent event) {
        this.cb_tipocli_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_fname_modcli(ActionEvent event) {
        this.tf_tel1_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_tel1_modcli(ActionEvent event) {
        this.tf_tel2_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_tel2_modcli(ActionEvent event) {
        this.tf_tel3_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_tel3_modcli(ActionEvent event) {
        this.tf_email_modcli.requestFocus();
    }

    @FXML
    void onAction_tf_ubicacion_modcli(ActionEvent event) {
        this.ta_datos_actuales_modcli.requestFocus();
    }

    /**
     * se hace la modificacion del cliente
     * @param event 
     */
    @FXML
    void onClick_b_HacerModificacion(MouseEvent event) {
        try{
        // se construye el obj cliente
        String id = this.tf_ccnit_modcli.getText();
        String fname = this.tf_fname_modcli.getText();
        String tel1 = this.tf_tel1_modcli.getText();
        String tel2 = this.tf_tel2_modcli.getText();
        String tel3 = this.tf_tel3_modcli.getText();
        String direccion = this.tf_direccion_modcli.getText();
        String email = this.tf_email_modcli.getText();
        
        String descripcion = this.cb_tipo_cobro_modcli.
                getSelectionModel().getSelectedItem().toString();  
        
        LocalDate cumpleanos = super.GetDateFromDatePicker(dt_cumple_modcli);
        
        // se lee el combobox de tipo de cliente
        String tipo_cliente = this.cb_tipocli_modcli.
                getSelectionModel().getSelectedItem().toString();
        
        String ubicacion = this.tf_ubicacion_modcli.getText();
        
        // se valida el cliente
        Va_Cliente vacli = new Va_Cliente();
            ValidationResult vr = vacli.MODclienteValido
        (id, fname, tel1, tel2, tel3, direccion, email, ubicacion, tipo_cliente, this.cli.id);
        
        if(vr.result){ // si los datos son validos se hace la modificacion del cliente
            Hndl_Clientes hndc = new Hndl_Clientes();
            hndc.modificarCliente(new Cliente(id, fname, tel1, tel2, tel3, 
                    direccion, email, descripcion, cumpleanos, tipo_cliente, ubicacion), this.cli.id);
            this.ShowInfo(EXITO_TITLE, "", EXITO_MOD_MESS);
            this.cerrar();
            
        } else{ // si no se pasa la validacion se muestra una ventana modal para explicar el motivo.
            this.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, vr.motivo);
        }
        
        // se hace lla modificacion si es del caso o se notifca al usuario 
        // se hay errores
        } catch(ClassNotFoundException | SQLException ex){
            // se notifica al usuario en caso de una excecion de java.
            this.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    /**
     * cuando se hace click en el boton de eliminar
     * @param me 
     */
    @FXML
    public void onClick_b_Eliminar(MouseEvent me){
        EliminarCliente();
    }
    
    /**
     * se elimina el cliente seleccionado al lanzar el
     * dialogo modal para d
     */
    public void EliminarCliente(){
        Hndl_Clientes hndc = new Hndl_Clientes();
        try{
            hndc.EliminarCliente(this.cli.id); // SQL se elimina el cliente de la tabla
            this.ShowInfo(this.EXITO_TITLE, "", EXITO_DELETE_MESS);
            this.cerrar(); // se hace el cierre correspondiente de la ventana.
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de una exepcion
            this.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }

    
    /**
     * se cierra el dialogo modal sin hacer ningun  cambio a la tabla de clientes
     * @param event 
     */
    @FXML
    void onClick_b_cancelar(MouseEvent event) {
        cerrar();
    }
    
    
    /**
     * metodo que se debe invocar para cerrar esta ventana.
     * aplica para el caso de cancelar una modificacion y tabien
     * cuando se hace una modificacion.
     */
    public void cerrar(){ // metodo para cerrar la ventana
        // se obtiene un handle de la ventana actual mediante un boton de la  misma ventana
        //en este caso el mismo asociado a la operacion de cancelar
        Stage stage = (Stage) b_cancelar.getScene().getWindow();
        // se cierra la ventana de modificacion
        stage.close();
        // se hace refresh de los datos del tableView de clientes.
        this.clicont.BuscarCliente();
    }
    
    
    /**
     * se reestablecen nuevamentes los campos de texto con la informacion
     * actual del cliente.
     * @param event 
     */
    @FXML
    void onClick_b_reset(MouseEvent event) {
        SetData();
    }
    
    /**
     * metodo que caraga la informacion actual del cliente en los respectivos 
     * campos de texto.
     */
    public void SetData(){
        // se escriben en el campo de texto los valores actuales del cliente
        // en la base de datos.
        this.tf_ccnit_modcli.setText(this.cli.id);
        this.tf_fname_modcli.setText(this.cli.nombre);
        this.tf_tel1_modcli.setText(this.cli.tel1);
        this.tf_tel2_modcli.setText(this.cli.tel2);
        this.tf_tel3_modcli.setText(this.cli.tel3);
        this.tf_email_modcli.setText(this.cli.email);
        this.tf_direccion_modcli.setText(this.cli.direccion);
        this.tf_ubicacion_modcli.setText(this.cli.ubicacion);
        this.cb_tipo_cobro_modcli.getSelectionModel().select(cli.descripcion);
        this.cb_tipocli_modcli.getSelectionModel().select(cli.tipo);
        this.dt_cumple_modcli.setValue(cli.cumple);
        this.ta_datos_actuales_modcli.setText(cli.retrieveRemainingData());
    }
    
    /**
     * metodo que se obliga a las clases hijas a implementar para
     * que la clase padre pueda ofrecer una funcionalidad especial mediante heredacion.
     * esto se explica con mas detalle en la definicion del metodo abstracto en la
     * clase padre.
     * @return 
     */
    @Override // cual objeto de FX como botones o texfields sirvenn para obtener
    public Stage GetStage() { // el Stage.
        return (Stage) this.b_cancelar.getScene().getWindow();
    }
    
}
