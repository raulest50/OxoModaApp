
package tentactildesktoppos.FXMLPopUps;



import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;


/**
 * FXML Controller class.
 * 
 * controlador de ventana modal que muestra la sinstesis de una factura cunado se hace doble click
 * en un record de la seccionde busqueda de facturas. esta funcionalidad permite ver rapidamente los
 * records pendientes de una factura sin enviarla a la seccion de abonos  y deboluciones lo cual permite
 * mayor agilidad.
 *
 * @author Esteban.
 */
public class FXMLSeeFactuSintesisController extends ControllerFather{
    
    
    /**
     * Text area para mostrar la sintesis de la factura.
     */
    @FXML
    public TextArea TA_Factu_Records;

    
    /**
     * etiqueta para mostrar el numero de factura
     */
    @FXML
    public Label LB_docID;

    /**
     * textarea para mostrar la informacion del cliente correspondiente a la factura seleccioanda.
     */
    @FXML
    public TextArea TA_CLient_info;
    
    
    
    /**
     * instancia de la factura seleccionada de la seccion de busqeuda de facturas
     */
    public SptFactura sel_factu;
    
    
    /**
     * instacia del cliente correspondiente a la factura seleccioanda.
     */
    public Cliente sel_cli;

    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // no se permite editar los campos de texto.
        TA_CLient_info.setEditable(false);
        TA_Factu_Records.setEditable(false);
    }
    
    
    
    /**
     * Permite incializar el controlador sin usar el metodo initialize de FX y pasando argumentos.
     * Es decir que se inicializa la ventana antes de que aparezca.
     */
    public void complement_Init(SptFactura sel_factu, Cliente sel_cli){
        
        try{
            // se asignan los atributos.
            this.sel_cli = sel_cli;
            this.sel_factu = sel_factu;
            
            // se pone el consecutivo de la factura en la cabecera.
            LB_docID.setText(Integer.toString(sel_factu.factura.consecutivo));
            TA_Factu_Records.setText(sel_factu.factura.ObList_FR_2Str(sel_factu.factura.getFrecordsSIntetizados()));
            
            // se pone la informacion del cliente.
            TA_CLient_info.setText(sel_cli.InfoClienteStr());
            
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }

    
    /**
     * ver java doc en la definicion del metodo abstracto en la super clase controller father.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.TA_CLient_info.getScene().getWindow();
    }
    
    /**
     * cuando se suelta una tecla.
     * @param ke 
     */
    @FXML
    public void OnKeyRes(KeyEvent ke){
        /**
         * si se presiona la tecla de escape entonces se cierra la
         * ventana modal
         */
        if(ke.getCode().equals(KeyCode.ESCAPE)){ 
            this.CloseThisWindow();
        }
    }
    
    
    /**
     * se ejecuta cuando se hace click en el boton azul de ok.
     * @param me 
     */
    @FXML
    public void onClickOk(MouseEvent me){
        this.CloseThisWindow(); // se cierra esta ventana modal.
    }
    
    
    /**
     * metodo para cerra esta ventana modal.
     */
    public void CloseThisWindow(){
        /**
         * se obtiene una instancia del stage de fx
         * y se usa para cerrar esta ventana.
         */
        this.GetStage().close(); 
    }
    
}



