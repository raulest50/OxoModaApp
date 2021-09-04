/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tentactildesktoppos.FXMLPopUps;



import BD_tdpos_r.Hndl_Factura;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;

import tentactildesktoppos.negocio_r_objs.spt_objects.SptCliente;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;

/**
 * FXML Controller class
 *
 * @author valentina
 */
public class FXMLSeeConsolidadoController extends ControllerFather{
    
    
    @FXML
    private TextArea TA_Consolidado;

    @FXML
    private TextArea TA_Cli_info;

    
    /**
     * instancia del cliente seleccionado en la seccion de busqueda de clientes.
     */
    public SptCliente sel_cli;
    
    
    /**
     * constant string para hacer separacion de columnas en el String del Text Area de consolidado.
     */
    public final String SEP = "       _-_-_       ";
    
    
    @FXML
    public void initialize() {
        // si configuran los text area para que no sean editables.
        this.TA_Cli_info.setEditable(false);
        this.TA_Consolidado.setEditable(false);
    }    
    
    
    /**
     * inicializador personalizado para iniciar el controlador antes de la funcion init y poder pasar
     * variables de inicializacion a este doc controller.
     * @param sel_cli 
     */
    public void CustomInit(SptCliente sel_cli){
        this.sel_cli = sel_cli; 
        
        // se carga la informacion del cliente.
        this.TA_Cli_info.setText(sel_cli.retrieveRemainingData());
        
        Hndl_Factura hndfac = new Hndl_Factura();
        try{
            // se traen todas las facturas del cliente que aun estan pendientes.
            ObservableList lsf = hndfac.getFacturasByClienteID_saldo_no_zero(this.sel_cli.getSpt_id());
            this.PrintConsolidado(lsf); // se pone la informacion del consolidado en el text area de
            // correspondiente.
            
        } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de una excepcion.
            // esto nunca deberia ocurrir.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    /**
     * se pone una respresentacion
     * @param lsf 
     */
    public void PrintConsolidado(ObservableList<SptFactura> lsf){
        String r ="Factura ID" + SEP + "Valor" + SEP + "\n\n";
        int sum = 0;
        for(SptFactura fac : lsf){
            r += Integer.toString(fac.factura.consecutivo) + SEP + Integer.toString(fac.factura.saldo) + "\n\n";
            sum += fac.factura.saldo;
        }
        r+= "Total" + SEP + Integer.toString(sum);
        this.TA_Consolidado.setText(r); // se escribe el consolidado en el campo de texto
    }
    
    
    /**
     * cuando se hace click en el boton de ok.
     * 
     * se cierra el ventana modal.
     * @param event 
     */
    @FXML
    void onClickOK(MouseEvent event) {
        this.GetStage().close();
    }

    /**
     * cuando se presiona una tecla. se pone el listener para detectar el uso de la tecla
     * escape.
     * 
     * en este caso se cierra la ventana.
     * @param event 
     */
    @FXML
    void onKeyRes_Anchor(KeyEvent event) {
        if(event.getCode().equals(KeyCode.ESCAPE)){
            this.GetStage().close();
        }
    }

    /**
     * ver documentacion en la definicion del metodo abstracto en la super clase
     * ControllerFather.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.TA_Cli_info.getScene().getWindow();
    }
    
}
