package tentactildesktoppos.FXMLPopUps;


import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.SQL_Configuracion;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javax.mail.MessagingException;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLinjection.FXMLConfiguracionController;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLPopUpMailConfigController extends ControllerFather {
    
    /**
     * campo de texto para ingresar la cuenta de gmail para
     * enviar el correo con la clave dinamica.
     */
    @FXML
    public TextField TF_SenderMail;

    /**
     * para poner la contraseña de la cuenta de gmail para enviar 
     * la clave dinamica
     */
    @FXML
    public PasswordField TF_Pass;
    
    /**
     * email de destino,puede ser el mismo de la cuenta gmail 
     * que envia como tambien puede ser otro diferente.
     * se aconseja que sea diferente.
     */
    @FXML
    public TextField TF_DestinoMail;

    
    /**
     * para ingresar la clave dinamica de test.
     */
    @FXML
    public TextField TF_DynPassTest;
    
    
    /**
     * boton que se usa para guardar los cambios.
     * se invoca para poder habiliar o inhabilitar
     */
    @FXML
    public Button B_Guardar;
    
    
    /**
     * instancia del controlador de configuracion que es responsable
     * de lanzar este panel modal.
     * 
     * se guarda esta instancia para poder hacer refresh del campo de texto
     * que muestra el correo electronico actualmente  configurado.
     */
    public FXMLConfiguracionController confCont;
    
    
    /**
     * boton que se usa para generar la  clave dinamica de test.
     */
    @FXML
    public Button B_DynPass;
    
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // se pone como inhabilitado
        this.B_Guardar.setDisable(true);
        
    }    

    /**
     * ver documentacion en la declaracion de metdo abstrato en la
     * superclase.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.TF_Pass.getScene().getWindow();
    }
    
    /**
     * cuando se hace click en el boton de cancelar
     * @param event 
     */
    @FXML
    public void onClickCancel(MouseEvent event) {
        this.Close(); // se cierra la ventana y no se hace ninguna actualizacion
        // en el text field del tab de configuracion.
    }

    /**
     * cuando se hace click en el boton de Test de clave dinamica
     * @param event 
     */
    @FXML
    public void onClickDynPass(MouseEvent event) {
        try{
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            // se genera una clave dinamica de test.
            hndconf.generarClaveDinamica();
            
            // el metodo soporta multiples destinatarios por lo que
            // el email de destino se pone en forma de arreglo String
            
            // se envia un correo con los
            
            // se inabilitan estos elementos de laui para forzar al usuario
            //  un unnico wworkFlow
            B_DynPass.setDisable(true);
            TF_DestinoMail.setDisable(true);
            TF_SenderMail.setDisable(true);
            TF_Pass.setDisable(true);
            
            super.ShowInfo("Clave Dinamica Generada", "Se Ha enviado una clave dinamica al \n"
                    + TF_DestinoMail.getText(), 
                    "Por favor revise el correo e identifique la clave aleatoria. \n"
                            + "digitela en el unico campo de texto que queda activo ");
            
            // se pone cursor en el campo de texto donde se debe digitar la clave dinamca.
            TF_DynPassTest.requestFocus();
            
        } catch (ClassNotFoundException | SQLException ex) {
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }

    
    /**
     * cuando se hace  click en el boton de guardar.
     * @param event 
     */
    @FXML
    public void onClickGuardar(MouseEvent event) {
        try{
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            
            hndconf.updateConf( SQL_Configuracion.PRT_SENDER_MAIL, this.TF_SenderMail.getText());
            hndconf.updateConf( SQL_Configuracion.PRT_SENDER_PASS, this.TF_Pass.getText());
            hndconf.updateConf( SQL_Configuracion.PRT_DESTINATION_MAIL, this.TF_DestinoMail.getText());
            
            // se refresca el email.
            confCont.LB_ConfigEmail.setText(hndconf.getConfiguracion(SQL_Configuracion.PRT_DESTINATION_MAIL));
            this.Close();
            
        } catch(SQLException | ClassNotFoundException ex) {
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * cuando se digita la clave dinamica aleatoria, y se presiona
     * enter con este textfield focuseado, entoces se hace chekeo de la
     * clave aleatoria. si es correcta, se habilita el boton de 
     * guardar para que se haga efectivo el cambio, en caso conntrario
     * se inhabilita.
     * 
     * el correcto ingreso de la clave aleatoria implica que los datos
     * correspondentes a los correos no solo son consistentes y correctos
     * sino que tambien esta apropiadamente configurada la  cuenta de 
     * gmail para permitir aplcaciones menos seguras.
     * 
     * por esto no es necesario hacer ningun tipo de validacion de los
     * campos de texto.
     * 
     * este checkeo tambien ppreviene un enfrascamiento por digitacion erronea
     * de correo, ya que una vez se ingresa por primera vez esta configuracion
     * para la clave dinamica, es necesrio hacer la validacion de clave dinamica
     * para ingresar nuevamente a este panel de conffiguracion.
     * @param ke 
     */
    @FXML
    public void onKeyRes_TFTest(KeyEvent ke){
        KeyCode kc = ke.getCode();
        if(kc.equals(KeyCode.ENTER)){ // se presiono Enter:
            try{ 
                Hndl_Configuracion hndconf = new Hndl_Configuracion();
                String dnp = hndconf.getConfiguracion(SQL_Configuracion.PRT_DYN_PASS);
                // si la clave es correcta se habilita el bonton
                // cuando es primera vez que se configura el correo, la clave
                // aleatoria es "" en la tabla de configuracion.
                // para tener en cuenta  esto se agrega la condicion adicional
                // que la clave guardada en la tabla de cconfiguracion debe tener
                // exactamente 4 digitos.
                if(this.TF_DynPassTest.getText().equals(dnp) && dnp.length() == 4){
                    this.B_Guardar.setDisable(false);
                } else{
                    this.B_Guardar.setDisable(true);
                }
            } catch(ClassNotFoundException | SQLException ex){
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
        }
    }
    
    
    /**
     * metodo que cierra este panel de configuracion.
     */
    public void Close(){
        this.GetStage().close();
    }
    
    
    /**
     * setter para la instancia al controlador  de configuracion.
     * @param confCont 
     */
    public void setConfCont(FXMLConfiguracionController confCont){
        this.confCont = confCont;
    }
    
    
}
