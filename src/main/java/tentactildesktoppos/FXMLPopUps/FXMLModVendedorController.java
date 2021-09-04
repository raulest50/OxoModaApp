/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tentactildesktoppos.FXMLPopUps;


import BD_tdpos_r.Hndl_Vendedor;
import Validator_tdpos_r.Va_Vendedor;
import Validator_tdpos_r.ValidationResult;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLinjection.FXMLVendedoresController;
import tentactildesktoppos.negocio_r_objs.Vendedor;

/**
 * FXML Controller class
 *
 * @author erich
 */
public class FXMLModVendedorController extends ControllerFather {

    
    
    
    /**
     * vendedor que se esta modificando.
     */
    public Vendedor ven;
    
    /**
     * document controller que lanza la presente ventana modal.
     */
    public FXMLVendedoresController c;
    
    @FXML
    private TextField tf_cedula_vemod;

    @FXML
    private TextField tf_fname_vemod;

    @FXML
    private TextField tf_tel1_vemod;

    @FXML
    private TextField tf_tel2_vemod;

    @FXML
    private TextField tf_direccion_vemod;

    @FXML
    private TextField tf_email_vemod;

    @FXML
    private DatePicker dt_cumple_vemod;

    @FXML
    private TextArea ta_descripcion_vemod;

    @FXML
    private TextArea ta_act_datos_vemod;
    
    
    /**
     * checkbox para establecer si el vendedor aun sigue o no activo
     */
    @FXML
    public CheckBox chb_estado_vemod;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // no usar para evitar null pointer exception.
    }    

    /**
     * ver documentacion en la declaracion de la superclase.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.tf_cedula_vemod.getScene().getWindow();
    }

    
    /**
     * metodo de inicializacion. no se puede invocar el initialize de FX ya que ocurre null pointer exception.
     *  ya que no se  como pasarle el vendedor y el controlador al metodo initialize.
     * @param v
     * @param aThis 
     */
    public void init(Vendedor v, FXMLVendedoresController aThis) {
        this.c = aThis; // se establece el controlador.
        this.ven = v; // se establece el vendedor.
        this.SetData();
        // el campo de texto que sirve de recordacion para todos los datos 
        // se establece que no pueda ser editado.
        this.ta_act_datos_vemod.setEditable(false);
    }
    
    
    
    /**
     * metodo que se debe invocar para cerrar esta ventana.
     * aplica para el caso de cancelar una modificacion y tabien
     * cuando se hace una modificacion.
     */
    public void cerrar(){ // metodo para cerrar la ventana
        // se obtiene un handle de la ventana actual mediante un Child object de la  misma ventana
        //en este caso el text field de codigo.
        Stage stage = (Stage) tf_cedula_vemod.getScene().getWindow();
        // se cierra la ventana de modificacion
        stage.close();
        // se hace refresh de los datos del tableView para evita inconsistencias
        // mostrando los viejos valores del objeto modificado.
        this.c.BuscarVendedor();
    }
    
    
    
    /**
     * se cargan los view con la informacion correspondiente al vendedor
     * seleccionado.
     */
    public void SetData(){
        this.tf_cedula_vemod.setText(this.ven.id);
        this.tf_fname_vemod.setText(this.ven.nombre);
        this.tf_tel1_vemod.setText(this.ven.tel1);
        this.tf_tel2_vemod.setText(this.ven.tel2);
        this.tf_direccion_vemod.setText(this.ven.direccion);
        this.tf_email_vemod.setText(this.ven.email);
        this.tf_cedula_vemod.setText(this.ven.id);
        this.dt_cumple_vemod.setValue(this.ven.nacimiento);
        this.ta_descripcion_vemod.setText(this.ven.descripcion);
        
        if(this.ven.estado==1){ // se carga el valor actual de la variable estado
            this.chb_estado_vemod.setSelected(true);
        }
    }
    
    
    
    /**
     * cuando se hace click al boton de modificar
     * @param event 
     */
    @FXML
    void onClick_b_Modifcar_vemod(MouseEvent event) {
        
        // se leen los datos de la ventana.
        String id = this.tf_cedula_vemod.getText();
        String fname = this.tf_fname_vemod.getText();
        String tel1 = this.tf_tel1_vemod.getText();
        String tel2 = this.tf_tel2_vemod.getText();
        String direccion = this.tf_direccion_vemod.getText();
        String email = this.tf_email_vemod.getText();
        String descripcion = this.ta_descripcion_vemod.getText();
        LocalDate nacimiento = super.GetDateFromDatePicker(this.dt_cumple_vemod);
        
        // se lee el nuevo estado del cliente en el check box. 
        int estado;
        if(this.chb_estado_vemod.isSelected()) estado = 1;
        else estado = 0;
        try{
            Va_Vendedor valit = new Va_Vendedor();
            ValidationResult vr = valit.EsValidoModificarVendedor(id, fname, tel1, tel2, email, direccion, nacimiento, this.ven.id);
            if(vr.result){ // si es valido hacer la modificacion ->
                Hndl_Vendedor hndven = new Hndl_Vendedor(); // conector a la tabla de vendedores MYSQL
                // se contruye el vendedor con fecha de ingreso nacimiento ya que este atributo no es usado en la insercion o 
                // modificacion de vendedores. En el Query de MYSQL se hace uso de la funcion curdate().
                Vendedor vende = new Vendedor(id, fname, tel1, tel2, direccion, email, descripcion, nacimiento, nacimiento, estado);
                // se hace la modificacion en la tabla de vendedores.
                hndven.UpdateVendedor(vende, this.ven.id);
                this.ShowInfo(super.exito, "", super.si_operacion);
                this.cerrar();
            } else{ // si no es valido hacer la modificacion.
                this.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, vr.motivo);
            }
        } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de una exepcion relacionada con BD
            this.ShowWarning(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    
    /**
     * cundo se hace clik en el boton de cancelar
     * @param event 
     */
    @FXML
    void onClick_b_cancel_vemod(MouseEvent event) {
        this.cerrar();
    }

    
    /**
     * cuando se hace click en el boton de cancelar
     * @param event 
     */
    @FXML
    void onClick_b_eliminar_vemod(MouseEvent event) {
        try{
            Hndl_Vendedor hndven = new Hndl_Vendedor();
            hndven.EliminarVendedor(this.ven.id);
            super.ShowInfo(super.exito, "", super.si_operacion);
            cerrar(); // se cierra la ventana y se refresca el table View de Vendedores.
        } catch(SQLException | ClassNotFoundException ex) { // se notifica al usuario en caso de una excepcion
            this.ShowWarning(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * cuando se hace click en el boton de reset.
     * @param event 
     */
    @FXML
    void onClick_b_reset_vemod(MouseEvent event) {
        this.SetData();
    }
    
    
    /****************/
    // Acciones campos de texto
    
    /**
     * Cuando se hace enter en el campo de texto de la cedula.
     * @param event 
     */
    @FXML
    void onAction_tf_cedula_vemod(ActionEvent event) {
        this.tf_fname_vemod.requestFocus();
    }

    @FXML
    void onAction_tf_direccion_vemod(ActionEvent event) {
        this.tf_email_vemod.requestFocus();
    }

    @FXML
    void onAction_tf_email_vemod(ActionEvent event) {
        this.dt_cumple_vemod.requestFocus();
    }

    @FXML
    void onAction_tf_fname_vemod(ActionEvent event) {
        this.tf_tel1_vemod.requestFocus();
    }

    @FXML
    void onAction_tf_tel1_vemod(ActionEvent event) {
        this.tf_tel2_vemod.requestFocus();
    }

    @FXML
    void onAction_tf_tel2_vemod(ActionEvent event) {
        this.tf_direccion_vemod.requestFocus();
    }
    
}