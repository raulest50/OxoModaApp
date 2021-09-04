package tentactildesktoppos.FXMLPopUps;


import BD_tdpos_r.Hndl_Productos;
import Validator_tdpos_r.Va_Producto;
import Validator_tdpos_r.ValidationResult;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLinjection.FXMLProductosController;
import tentactildesktoppos.negocio_r_objs.Producto;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptProducto;



/**
 * FXML Controller class
 *Controlador del dialogo modal que ermite hacer modificacion de productos.
 * @author esteban
 */
public class FXMLModProductoController extends ControllerFather{
    
    
    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_codigo_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_descri_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_costo_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_iva_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_stock_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_pvcontado_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_pvcredito_modpro;

    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextField tf_pvmayor_modpro;
    
    /**
     * campo de texto de modificacion de codigo de producto
     */
    @FXML
    public TextArea ta_datos_act_modpro;
    
    
    /**
     * producto seleccionado desde el acordeon de modificacion.
     */
    public Producto sel_pro;
    
    
    /**
     * instancia del document controller de tab de productos.
     */
    public FXMLProductosController c;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        //no se ejecuta hasta que se invoque el metodo .show()
    }    

    /**
     * retorna el de este documento de FX Stage. Mas informacion, ver el 
     * comentario de la funcion abstracta en la clase super (FatherController).
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.tf_codigo_modpro.getScene().getWindow();
    }

    /**
     * metodo que se ejecuta desde el controlador de productos para inicializar
     * Este controlador de modificacion de productos y evitar el lanzamiento
     * de excepcion del tipo de nullPointer.
     * @param pro
     * @param aThis 
     */
    public void init(SptProducto pro, FXMLProductosController aThis) {
        // campo de texto en el que se escriben los datos acctuales del
        // producto para comodidad de recordacion del cliente.
        this.ta_datos_act_modpro.setEditable(false);
        this.sel_pro = pro;
        this.c = aThis;
        this.SetData(); // se escriben en los campos de texto los atributos 
        // actuales del producto seleccionado.
        
    }
    
    
    /**
     * establece los datos de los elementos de la GUI deacuerdo al producto 
     * seleccionado.
     */
    public void SetData(){
        this.tf_codigo_modpro.setText(sel_pro.codigo);
        this.tf_descri_modpro.setText(sel_pro.descripcion);
        this.tf_costo_modpro.setText(Integer.toString(sel_pro.costo));
        this.tf_pvcontado_modpro.setText(Integer.toString(sel_pro.pventa_contado));
        this.tf_pvcredito_modpro.setText(Integer.toString(sel_pro.pventa_credito));
        this.tf_pvmayor_modpro.setText(Integer.toString(sel_pro.pventa_mayor));
        this.tf_iva_modpro.setText(Integer.toString(sel_pro.iva));
        this.tf_stock_modpro.setText(Integer.toString(sel_pro.stock));
        this.ta_datos_act_modpro.setText(this.sel_pro.produto2Str());
    }
    
    
    /**
     * metodo que se debe invocar para cerrar esta ventana.
     * aplica para el caso de cancelar una modificacion y tabien
     * cuando se hace una modificacion.
     */
    public void cerrar(){ // metodo para cerrar la ventana
        // se obtiene un handle de la ventana actual mediante un Child object de la  misma ventana
        //en este caso el text field de codigo.
        Stage stage = (Stage) tf_codigo_modpro.getScene().getWindow();
        // se cierra la ventana de modificacion
        stage.close();
        // se hace refresh de los datos del tableView para evita inconsistencias
        // mostrando los viejos valores del objeto modificado.
        this.c.BuscarProducto();
    }
    
    
    /**
     * se dispara cunado se hace enter en el textfield de modificar codigo
     * @param event 
     */
    @FXML
    void onAction_tf_codigo_modpro(ActionEvent event) {
        this.tf_descri_modpro.requestFocus();
    }

    /**
     * se dispara cuando se hace enter en el textfield de modificar costo
     * @param event 
     */
    @FXML
    void onAction_tf_costo_modpro(ActionEvent event) {
        this.tf_pvmayor_modpro.requestFocus();
    }

    
    /**
     * se dispara cuando se hace enter en el textfield de modificar descripcion
     * @param event 
     */
    @FXML
    void onAction_tf_descri_modpro(ActionEvent event) {
        this.tf_costo_modpro.requestFocus();
    }

    
    /**
     * se dispara cuando se hace enter en el textfield de modificar 
     * precio de vent de contado
     * @param event 
     */
    @FXML
    void onAction_tf_pvcontado_modpro(ActionEvent event) {
        this.tf_pvcredito_modpro.requestFocus();
    }

    
    /**
     * se dispara cuando se hace enter en el textfield de modificar 
     * precio de venta de credito
     * @param event 
     */
    @FXML
    void onAction_tf_pvcredito_modpro(ActionEvent event) {
        this.tf_pvmayor_modpro.requestFocus();
    }

    
    /**
     * se dispara cuando se hace enter en el textfield de modificar 
     * precio de venta pormayor
     * @param event 
     */
    @FXML
    void onAction_tf_pvmayor_modpro(ActionEvent event) {
        tf_pvcontado_modpro.requestFocus();
    }
    
    
    /**
     * se dispara cuando se hace enter en el textfield de modificar stock
     * @param event 
     */
    @FXML
    void onAction_tf_stock_modpro(ActionEvent event) {
        this.tf_pvcontado_modpro.requestFocus();
    }
    
    
    /**
     * se dispara cuando se hace enter en el textfield de modificar iva
     * @param event 
     */
    @FXML
    void onActionl_tf_iva_modpro(ActionEvent event) {
        this.tf_stock_modpro.requestFocus();
    }
    
    
    /**
     * se dispara cuando se hace clck en el boton de cancelar en la ventana modal
     * de modificacion de productos
     * @param event 
     */
    @FXML
    void onClick_b_cancel_modpro(MouseEvent event) {
        this.cerrar();
    }
    
    
    /**
     * se dispara cuando se hace clck en el boton de eliminar en la ventana modal
     * de modificacion de productos
     * @param event 
     */
    @FXML
    void onClick_b_eliminar_modpro(MouseEvent event) {
        Hndl_Productos hndps = new Hndl_Productos();
        try{ // se elimina el producto seleccionado de la basse de datos
            hndps.EliminarProducto(this.sel_pro.codigo);
            super.ShowInfo(super.exito, "", super.si_operacion);
            this.cerrar(); // se cierra la ventana y se refresca el table view correspondiente.
        } catch(SQLException | ClassNotFoundException ex){
            this.ShowWarning(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * se dispara cuando se hace clck en el boton de modificar en la ventana modal
     * de modificacion de productos
     * @param event 
     */
    @FXML
    void onClick_b_modifcar_modpro(MouseEvent event) {
        Hndl_Productos hndps = new Hndl_Productos();
        // se leen los datos del Text Field.
        String codigo = this.tf_codigo_modpro.getText();
        String descripcion = this.tf_descri_modpro.getText();
        String costo = this.tf_costo_modpro.getText();
        String pv_contado = this.tf_pvcontado_modpro.getText();
        String pv_credito = this.tf_pvcredito_modpro.getText();
        String pv_mayor = this.tf_pvmayor_modpro.getText();
        String iva = this.tf_iva_modpro.getText();
        String stock = this.tf_stock_modpro.getText();
        
        try{
            // se hace validacion de la modificacion del producto.
            Va_Producto valit = new Va_Producto();
            ValidationResult vr = valit.EsValidoModificar(codigo, descripcion, costo,
                    pv_contado, pv_credito, pv_mayor, stock, iva, this.sel_pro.codigo);
            
            if(vr.result){ // si es valido hace la modificacion ->    
                hndps.UpdateProducto(new Producto(
                        codigo, descripcion, costo, // se pone una fecha sin sentido
                        // ya que estaa es ingresada realmente por SQL mediante la funcion curdate()
                        pv_contado, pv_credito, pv_mayor, LocalDate.EPOCH.toString(), 
                        stock, iva), 
                        this.sel_pro.codigo);
                super.ShowInfo(super.exito, super.si_operacion, ""); // se notifica al usuario que la operacion fue exitosa
                this.cerrar(); // se cierra la ventana modal y se refresca
                // el table view.
                
            } // si no es valido hacer la modificacion se notifica al usuario.
            else{
                super.ShowInfo(super.no_operacion, super.recti_mess, vr.motivo);
            }
            
        } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de un error.
            this.ShowWarning(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    /**
     * se dispara cuando se hace clck en el boton de reset en la ventana modal
     * de modificacion de productos
     * @param event 
     */
    @FXML
    void onClick_b_reset_modpro(MouseEvent event) {
        this.SetData();
    }
    
}
