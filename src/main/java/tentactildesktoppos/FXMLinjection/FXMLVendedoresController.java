package tentactildesktoppos.FXMLinjection;


import BD_tdpos_r.Hndl_Vendedor;
import Validator_tdpos_r.Va_Vendedor;
import Validator_tdpos_r.ValidationResult;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLPopUps.FXMLModClienteController;
import tentactildesktoppos.FXMLPopUps.FXMLModVendedorController;
import tentactildesktoppos.negocio_r_objs.Vendedor;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptVendedor;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLVendedoresController extends ControllerFather{
    
    /**
     * para poder saber desde este controlador si hay una venta en curso
     * y congelar las funcionalidades necesarias para asegurar la consistencia
     * de los datos mostrados en la interafz grafica.
     */
    public FXMLVentasController ventasController;
    
    
    //*********************
    // seccion para codificar un vendedor
    
    @FXML
    private DatePicker DTP_nacimiento;
    
    @FXML
    private TextField TF_email;
    
    @FXML
    private TextField TF_direccion;
    
    @FXML
    private TextField TF_tel2;
    
    @FXML
    private TextField TF_tel1;
    
    @FXML
    private TextField TF_nombre;
    
    @FXML
    private TextField TF_cedula;
    
    @FXML
    private TextArea TA_descripcion;
    
    @FXML
    private Button B_codificar;
    
    @FXML
    private Button B_borrar;
    
    
    //*************
    // seccion para buscar y modificar un vendedor
    
    /**
     * TF para hacer busqueda vendedores
     */
    @FXML
    public TextField TF_buscar;
    
    
    /**
     * combobox para seleciconar el tipo de busqueda
     */
    @FXML
    public ComboBox CB_tipoBusqueda;
    
    
    /**
     * tipo de busqueda de vendedor para el combo box
     */
    public final String TSEARH_CC = "Busqueda por CC";
    
    
    /**
     * tipo de busqueda en el combobox
     */
    public final String TSEARH_NOMBRE = "Busqueda por nombre";
    
    /**
     * mensaje para warning dialog cuando se hace click al boton de modificar
     * pero no se tiene ningun elemento del tableView seleccionado
     */
    public final String NO_SEL_VENDEDOR = "No hay  ningun vendedor seleccionado.\n"
            + " Porfavor seleccione un vendedor.";
    
    
    /**
     * numero de identificacion del vendedor generico.
     */
    public final String ID_VENDEDOR_GENERICO = "321";
    
    @FXML
    TableView<SptVendedor> TV_vendedores;
    
    
    @FXML
    TableColumn<SptVendedor, String> TC_cc, TC_nombre;
    
    
    /**
     * TA para mostrar los datos restantes del vendedor que no caben en el
     * TableView.
     */
    @FXML
    TextArea TA_demasDatos;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        this.CargarTipoBusqueda();        
        this.TA_demasDatos.setEditable(false);
        
        // si no se configuran los tableColumns, la tabla se llena pero con valores "invisibles".
        this.ConfigTV_vendedores(); 
    }
    

    @FXML
    void onAction_TF_cedula(ActionEvent event) {
        TF_nombre.requestFocus();
    }
    
    @FXML
    void onAction_TF_nombre(ActionEvent event) {
        TF_tel1.requestFocus();
    }
    
    
    @FXML
    void onAction_TF_tel1(ActionEvent event) {
        TF_tel2.requestFocus();
    }
    
    
    @FXML
    void onAction_TF_tel2(ActionEvent event) {
        TF_direccion.requestFocus();
    }
    

    @FXML
    void onAction_TF_direccion(ActionEvent event) {
        TF_email.requestFocus();
    }
    
    
    @FXML
    void onAction_TF_email(ActionEvent event) {
        DTP_nacimiento.requestFocus();
    }
    
    
    @FXML
    void onAction_DTP_nacimiento(ActionEvent event) {
        TA_descripcion.requestFocus();
    }

    @FXML
    void onClick_B_borrar(MouseEvent event) {
        this.clearFields();
    }
    
    @FXML
    void onAction_B_borrar(ActionEvent event) {
        this.clearFields();
    }
    
    
    
    /**
     * cuando se hace click en el boton de modificar
     * @param me 
     */
    @FXML
    public void onClick_b_Modificar(MouseEvent me){
        // si hay vendedor seleccionado en el tbleView ->
        if(!TV_vendedores.getSelectionModel().isEmpty()){ // se verifica tambien que no este seleccionado el vendedor generico
            if(!TV_vendedores.getSelectionModel().getSelectedItem().id.equals(this.ID_VENDEDOR_GENERICO)){
                Vendedor v = TV_vendedores.getSelectionModel().getSelectedItem();
                this.PopUpModificarCliente(v, me); // se abre la ventana modal para hacer modifcacion de un cliente.
            }
        } else{ // si no hay vendedor seleccionado en tableView ->
            // se notifica al usuario.
            super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, this.NO_SEL_VENDEDOR);
        }
    }
    
    /**
     * 
     * @param v
     * @param event 
     */
    public void PopUpModificarCliente(Vendedor v, Event event){
        try {
            // se carga el path del FXML
            FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                    getResource("tentactildesktoppos/FXMLPopUps/FXMLModVendedor.fxml"));
            Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
            Stage st = new Stage();// modificacion
            st.setScene(new Scene(root));
            st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
            st.initOwner( ((Node) event.getSource()).getScene().getWindow());
            FXMLModVendedorController c = cargador.<FXMLModVendedorController>getController(); // se obtiene el controlador
            // se inicializa la ventana modal con los datos del vendedor que se desean
            // modificar
            c.init(v, this);// se inicializan los valores de la ventana
            st.show(); // se muestra la ventana
            
        } catch (IOException ex) {
            Logger.getLogger(FXMLModClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**
     * metodo que hace reset a todos los campos de la UI
     */
    public void clearFields(){
        TF_cedula.clear();
        TF_nombre.clear();
        TF_tel1.clear();
        TF_tel2.clear();
        TF_direccion.clear();
        TF_email.clear();
        TA_descripcion.clear();
        DTP_nacimiento.getEditor().clear();
    }

    @FXML
    void onClick_B_codificar(MouseEvent event) {
        this.codificarVendedor();
    }

    @FXML
    void onAction_B_codificar(ActionEvent event) {
        this.codificarVendedor();
    }
    
    /**
     * metodo que toma todos los datos de la UI y los valida. En caso de
     * pasar la validacion entonces se codifica el vendedor y se limpian 
     * los strings de los textfields y demas.
     */
    public void codificarVendedor(){
        
        try {
            String id, nombre, tel1, tel2, direccion, email, descripcion;
            LocalDate nacimiento;
            id = this.TF_cedula.getText();
            nombre = this.TF_nombre.getText();
            tel1 = this.TF_tel1.getText();
            tel2 = this.TF_tel2.getText();
            direccion = this.TF_direccion.getText();
            email = this.TF_email.getText();
            descripcion = this.TA_descripcion.getText();
            // la forma segura de leer el date picker para evitar null values.
            nacimiento = super.GetDateFromDatePicker(DTP_nacimiento);
            // el valor de fingreso lo pone el algortimo con curdate() en SQL
            // el estado se establece en 1 (activo) por defecto.
            
            Va_Vendedor vav = new Va_Vendedor(); // obj para hacer validacion de los datos
            ValidationResult vr = new ValidationResult();
            vr = vav.VendedorEsValido(id, nombre, tel1, tel2, email, direccion, nacimiento);
            
            if(vr.result){ // se los datos son validos se hace la codifiicacion del vendedor.
                Hndl_Vendedor hndv = new Hndl_Vendedor();
                
               // se pone un valor sin sentido para fecha ingreso (LocalDate.MIN) 
               // ya que este se estblece por la BD con la funcion curdate()
               // 
               Vendedor v = new Vendedor(id, nombre, tel1, tel2, direccion, email, 
                        descripcion, nacimiento, LocalDate.MIN, 1); 
                
                hndv.InsertVendor(v); // se hace la codificacion del vendedor.
                super.ShowInfo(super.si_operacion, super.exito, "");// se notifica al usuarui que la operacion fue exitosa
                this.clearFields(); // finalmente se limpian los campos.
            }
            
            else{ // si los datos no son validos se notifican los motivos mediante un dialogo modal
                super.ShowInfo(super.no_operacion, super.recti_mess, vr.motivo);
            }
        } catch (ClassNotFoundException | SQLException ex) { // si ocurre una excepcion relacionada con la BD se notifica con un dialogo modal
            Logger.getLogger(FXMLVendedoresController.class.getName()).log(Level.SEVERE, null, ex);
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
    
    /**
     * click en el boton de busqueda.
     * pestaÃ±a de busqueda y modificacion de vendedor
     * @param me 
     */
    @FXML
    public void onClick_B_buscar(MouseEvent me){
        this.BuscarVendedor();
    }
    
    
    /**
     * enter en el boton de busqueda.
     * pestaÃ±a de busqueda y modificacion
     * @param ae 
     */
    @FXML
    public void onAction_B_buscar(ActionEvent ae){
        this.BuscarVendedor();
    }
    
    /**
     * enter en el TF de buscar vendedor
     * @param ae 
     */
    @FXML
    public void onAction_TF_buscar(ActionEvent ae){
        this.BuscarVendedor();
    }
    
    
    /**
     * metodo que busca un vendedor usando el criterio establecido en
     * CB_tipoBusqueda.
     */
    public void BuscarVendedor(){
        
        this.TA_demasDatos.clear(); //se limpia el textField para evitar que se muestren datos
        // que no corresponden a ninguno de  los elementos de la tabla.
        
        String b = TF_buscar.getText();
        String tipo_b = CB_tipoBusqueda.getSelectionModel().getSelectedItem().toString();
        
        ArrayList<Vendedor> lv = new ArrayList<>();
        
        try{
            Hndl_Vendedor hndv = new Hndl_Vendedor();
            
            if(tipo_b.equals(this.TSEARH_CC)){
                lv = hndv.getVendedorId(b);
            }
            
            if(tipo_b.equals(this.TSEARH_NOMBRE)){
                lv = hndv.buscarVendedor_xNombre(b);
            }
            this.SetTV_vendedores(lv);
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario en caso de un excepcion
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
    
    /**
     * metodo que carga todos los tipos de busqueda en el comboBox.
     * pestaÃ±a de busqueda y modificacion de vendedor
     */
    public void CargarTipoBusqueda(){
        ArrayList<String> ltb = new ArrayList<>();
        ltb.add(this.TSEARH_CC);
        ltb.add(this.TSEARH_NOMBRE);
        super.SetComboBox(CB_tipoBusqueda, ltb);
    }
    
    
    /**
     * enter en el boton de borrar.
     * pestaÃ±a de busqueda y modificacion de vendedor.
     * @param ae 
     */
    @FXML
    public void onAction_B_borrar_mod(ActionEvent ae){
        TF_buscar.clear();
    }
    
    /**
     * click en el boton de borrar.
     * pestaÃ±a de busqueda y modificacion de vendedor.
     * @param me 
     */
    @FXML
    public void onClick_B_borrar_mod(MouseEvent me){
        TF_buscar.clear();
    }
    
    
    /**
     * metodo que configura el TableView de la pestaÃ±a de busqueda y modificacion
     * de vendedores
     */
    public void ConfigTV_vendedores(){
        this.TC_cc.setCellValueFactory(new PropertyValueFactory<>("spt_id"));
        this.TC_nombre.setCellValueFactory(new PropertyValueFactory<>("spt_nombre"));
    }
    
    
    /**
     * metodo que permite poblar la tabla de resultados de busqueda de vendedores.
     * @param lv 
     */
    public void SetTV_vendedores(ArrayList<Vendedor> lv){
        ArrayList<SptVendedor> spt_lv = this.fromVendedor2Spt(lv);
        ObservableList<SptVendedor> ovl_lc = FXCollections.observableArrayList(spt_lv);
        this.TV_vendedores.setItems(ovl_lc);
    }
    
    
    /**
     * metodo que transforma una lista de vendedores a su forma extendida Spt.
     * @param l
     * @return 
     */
    public ArrayList<SptVendedor> fromVendedor2Spt(ArrayList<Vendedor> l){
        ArrayList<SptVendedor> slv = new ArrayList<>();
        for(int i=0; i<l.size() ; i++){
            slv.add(new SptVendedor(l.get(i)));
        }
        return slv;
    }
    
    
    /**
     * cubre el caso que se seleccionen vendedores de la tabla usando las flechas 
     * del teclado.
     * @param ke 
     */
    @FXML
    public void onKeyRes_TV_vendedores(KeyEvent ke){
        this.TA_demasDatos.setText(this.TV_vendedores.getSelectionModel().getSelectedItem().RetrieveRemainingInfo());
    }
    
    
    /**
     * cuando se hace click en la tabla de la pestaÃ±a de busqueda y modificacion
     * de vendedores. cubre el caso enn que se selecciones vendedores del table view
     * con un click.
     * @param me 
     */
    @FXML
    public void onClick_TV_vendedores(MouseEvent me){
        this.TA_demasDatos.setText(this.TV_vendedores.getSelectionModel().getSelectedItem().RetrieveRemainingInfo());
    }
    
    /**
     * ver javadoc en la defiinicion del metodo abstracto en la superclase ControllerFather para mayor informacion.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.B_codificar.getScene().getWindow();
    }
    
    
    
}