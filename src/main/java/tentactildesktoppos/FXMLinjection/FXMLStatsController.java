package tentactildesktoppos.FXMLinjection;



import BD_tdpos_r.Hndl_Factura;
import Validator_tdpos_r.Validador;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;


/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLStatsController extends ControllerFather{
    
    
    /**
     * para establecer el punto inicial de Dt
     */
    @FXML
    public DatePicker DTP_Desde;
    
    
    /**
     * para establecer el punto incial de Dt
     */
    @FXML
    public DatePicker DTP_Hasta;
    
    
    /**
     * para poner el ingreso por pagos dentro del periodo
     * Dt especificado
     */
    @FXML
    public Label LB_Pagos;
    
    /**
     * para poner el equivalente en capital
     * de la mmercancia en stock.
     */
    @FXML
    public Label LB_Mercancia;
    
    /**
     * para poner a suma de todos los saldos de la facturas pendientes.
     */
    @FXML
    public Label LB_Cartera;
    
    

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // se carga una fecha por defecto en los datePickers.
        DTP_Desde.setValue(LocalDate.now());
        DTP_Hasta.setValue(LocalDate.now());
        
        SetLabels(); // se ponen los valores correspondientes
        // de cartera, ingreso de dinero por pagos y 
        // el capital en mercancia
        
        /**
         * se agregan listener a cambios de la fecha seleccionada en
         * ambos datepicker.
         * cada que hay un cambio en los valores de los date picker 
         * entonces se recalcula el ingreso de efectivo en dicha fecha.
         */
        DTP_Desde.valueProperty().addListener((observable, oldValue, newValues) ->{
            SetMoneyIncome();
        });
        
        DTP_Hasta.valueProperty().addListener((observable, oldValue, newValues) ->{
            SetMoneyIncome();
        });
    }    
    
    /**
     * metodo que calcula el valor en efectivo de la mercancia que 
     * hay en stock y establece ese valor equivalente de dinero 
     * en un label de este tab (Stats).
     * Sum(c_i * N_i) se suma para cada i-esimo producto, el prodcuto
     * de su costo con su cantidad.
     */
    public void SetStock2Money(){
        try{
            Hndl_Factura hndfac = new Hndl_Factura();
            this.LB_Mercancia.setText(Integer.toString(hndfac.GetStock2Money()));
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * metodo que se debe invocar cada que se selecciona un valor
     * en uno de los date picker de la seccion de stats. este metodo
     * calcula el total de ingreso en efectivo entre las fehas indicadas.
     * finalmente escribe este valor en el label correpondiente
     */
    public void SetMoneyIncome(){
        
        LocalDate f1, f2;
        
        f1 = super.GetDateFromDatePicker(DTP_Desde);
        f2 = super.GetDateFromDatePicker(DTP_Hasta);
        
        //si alguno de los datePickers no contienen valores validos
        // no se hace nada
        if(f1.toString().equals(Validador.EMPTY_DATE_STR) 
                || f2.toString().equals(Validador.EMPTY_DATE_STR)
                || f1.isAfter(f2)){
            this.LB_Pagos.setText("Hay un problema con las fechas seleccionadas");
        } else {// En caso contrario se hace el calculo y se pone su resultado en
            // en label correspondiente.
            try{
                Hndl_Factura hndfac = new Hndl_Factura();
                this.LB_Pagos.setText(Integer.toString(hndfac.GetPagoInDt(f1, f2)));
            } catch(SQLException | ClassNotFoundException ex){
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
        }
    }
    
    /**
     * metodo que suma todos los saldos de las facturas pendientes y
     * lo pone en el label correspondiente.
     */
    public void SetCarteraValue(){
        try{
            Hndl_Factura hndfac = new Hndl_Factura();
            this.LB_Cartera.setText(Integer.toString(hndfac.GetCartera()));
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * metodo que establece el valor instanteneo que debe ir en los labels.
     */
    public void SetLabels(){
        SetCarteraValue();
        SetMoneyIncome();
        SetStock2Money();
    }
    
    
    /**
     * para mas informmacion ver comentario en la definicion del metodo
     * abstracto en la super clase.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.DTP_Desde.getScene().getWindow();
    }
    
}
