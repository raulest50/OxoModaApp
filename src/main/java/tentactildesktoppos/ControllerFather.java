package tentactildesktoppos;

import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.SQL_Configuracion;
import Validator_tdpos_r.Validador;
import java.net.SocketException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import javax.mail.MessagingException;
import org.apache.commons.lang3.SystemUtils;
import support_tdpos.SuppMess;


/**
 * Todos los controladores FX que hagan uso de dialogos de confirmacion deben
 * heredar de esta clase, la cual imolementan los metodos para
 * mostrar dialogos modales usando la api de javaFX.
 * @author esteban
 */
public abstract class ControllerFather {
    
    
    /**
     * mensaje que indica al usuario que hay un error en los datos.
     */
    public String recti_mess = "Rectifique los datos por favor.";
    
    
    /**
     * mensaje que indica qal usuario que la operacion no se ha podidi realizar
     */
    public String no_operacion = "No se puede hacer operacion";
    
    
    /**
     * mensaje que indica al usuario que la operacion se ha realiazado con exito,
     */
    public String si_operacion = "La operacion ha sido realizada exotisamente";
    
    
    /**
     * String para operacion exitosa
     */
    public String exito = "Operacion exitosa";
    
    
    /**
     * Tamaño por defectio para los dialogos modales.
     * Se agrego porque en ubuntu no hace resize deacuerdo al contenido String del mensaje.
     */
    public final double DEF_HEIGHT = 100.0;
    
    /**
     * Ancho por defecto de las ventanas modales.
     */
    public final double DEF_WIDTH = 800.0;
    
    
    /**
     * instancia del controlador principal que permitira a todos los controladores injectados
     * interactuar con otros controladores, ya que este mainDoc ya tiene entre sus atributos una instancia
     * de cada uno de los controladores inyectados.
     */
    public FXMLDocumentController MainDocController;

    
    /**
     * ventana modal de informacion de la api de java fx.
     * @param titulo
     * @param subtitulo
     * @param mensaje 
     */
    public void ShowInfo(String titulo, String subtitulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        // no es necesario pero cuando esta en fullScreen mode
        // ayuda a evitar cammbio de focus molesto cuando se lanza esta ventana
        alert.initOwner(this.GetStage()); 
        
        alert.setTitle(titulo);
        alert.setHeaderText(subtitulo);
        alert.setContentText(mensaje);
        // En ubuntu no hace resize de acuerdo al contenido
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinHeight(this.DEF_HEIGHT);
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinWidth(this.DEF_WIDTH);
        alert.showAndWait();
        
        //DialogPane Dpane = alert.getDialogPane();
        //Dpane.getStylesheets().add(VirtualAdministrator.class.getClass().getResource("estilo.css").toExternalForm());
    }
    
    
    
    /**
     * ventana modal de advertencia
     * @param titulo
     * @param subtitulo
     * @param mensaje 
     */
    public void ShowWarning(String titulo, String subtitulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        
        // no es necesario pero cuando esta en fullScreen mode
        // ayuda a evitar cammbio de focus molesto cuando se lanza esta ventana
        alert.initOwner(this.GetStage());
        
        alert.setTitle(titulo);
        alert.setHeaderText(subtitulo);
        alert.setContentText(mensaje);
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinHeight(this.DEF_HEIGHT);
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinWidth(this.DEF_WIDTH);
        alert.showAndWait();
        
        //DialogPane Dpane = alert.getDialogPane();
        //Dpane.getStylesheets().add(VirtualAdministrator.class.getClass().getResource("estilo.css").toExternalForm());
    }
    
    
    
    /**
     * muestra un dialogo modal de error
     * @param titulo
     * @param subtitulo
     * @param mensaje 
     */
    public void ShowError(String titulo, String subtitulo, String mensaje){
        /*
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        // no es necesario pero cuando esta en fullScreen mode
        // ayuda a evitar cammbio de focus molesto cuando se lanza esta ventana
        alert.initOwner(this.GetStage());
        
        alert.setTitle(titulo);
        alert.setHeaderText(subtitulo);
        alert.setContentText(mensaje);
        // En ubuntu no hace resize de acuerdo al contenido
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinHeight(this.DEF_HEIGHT);
        if(SystemUtils.IS_OS_LINUX) alert.getDialogPane().setMinWidth(this.DEF_WIDTH);
        alert.showAndWait();
        //DialogPane Dpane = alert.getDialogPane();
        //Dpane.getStylesheets().add(VirtualAdministrator.class.getClass().getResource("estilo.css").toExternalForm());
        */
    }
    
    
    
    
    /**
     * metodo para pedir informacion al usuario en una ventana modal
     * @param titulo
     * @param subtitulo
     * @param mensaje
     * @param valordefecto
     * @return
     */
    public  String ShowInput(String titulo, String subtitulo, String mensaje, String valordefecto){
        String input = "";
        TextInputDialog dialog = new TextInputDialog(valordefecto);
        
        // no es necesario pero cuando esta en fullScreen mode
        // ayuda a evitar cambio de focus molesto cuando se lanza esta ventana
        dialog.initOwner(this.GetStage());
        
        dialog.setTitle(titulo);
        dialog.setHeaderText(subtitulo);
        dialog.setContentText(mensaje);
        // En ubuntu no hace resize de acuerdo al contenido
        if(SystemUtils.IS_OS_LINUX) dialog.getDialogPane().setMinHeight(this.DEF_HEIGHT);
        if(SystemUtils.IS_OS_LINUX) dialog.getDialogPane().setMinWidth(this.DEF_WIDTH);
        
        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        //DialogPane Dpane = dialog.getDialogPane();
        //Dpane.getStylesheets().add(VirtualAdministrator.class.getClass().getResource("estilo.css").toExternalForm());
        if (result.isPresent()){
            input = result.get();
        }
        return input;
    }
    
    
    /**
     * metodo que carga una lista de strings en un combo box
     * y ademas pone el primer elemento de la lista en la UI del combo box.
     * @param cb
     * @param ls 
     */
    public void SetComboBox(ComboBox cb, ArrayList<String> ls){
        String[] lista = new String[ls.size()];
        lista = ls.toArray(lista);
        //Se usa para el Observable List
        ObservableList<String> ofls = FXCollections.observableArrayList(lista); // se arma la lista observable
        cb.setItems(ofls);
        cb.setValue(ofls.get(0));//se pone el item 0
    }
    
    
    /**
     * metodo que lee la echa establecida en el date picker.
     * en caso de tener valor null se reemplaza por la 
     * fecha 0000-00-00 para evitar todos los problemas subyacentes
     * @param dtp
     * @return
     */
    public LocalDate GetDateFromDatePicker(DatePicker dtp){
        LocalDate ld = dtp.getValue();
        if(ld == null){ // si el Date picker entrega un valor nulo
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            ld = LocalDate.parse(Validador.EMPTY_DATE_STR, formatter);
        }
        return ld;
    }
    
    /**
     * funcion que genera una fecha vacia para usar en lugar de NULL en la
     * base de ddatos ya que null es bastante errorProne
     * @return 
     */
    public LocalDate getEmptyDay(){
        LocalDate ld;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ld = LocalDate.parse(Validador.EMPTY_DATE_STR, formatter);
        return ld;
    }
    
    
    
    /**
     * metodo abstracto necesario para inicializar el propietario de los
     * dialogosy asi evitar cambio molesto de focus cuando se esta en fullscren
     * mode y al mismo tiempo no se lanza una de las 4 ventanas definidas en esta
     * clase padre.
     * sin embargo la forma mas expedita de obtener el objeto Stage necesario
     * es usando un objeto de java FX, al cual cada DocController hijo
     * solo tiene acceso y a objetos diferentes de FX por lo que
     * se declara este emtodo abstracto para que sea implementado 
     * por las clases hijas y al mismo tiempo la clase 
     * padre pues establecer un propietario para la ventana modal.
     * @return 
     */
    public abstract Stage GetStage();
    
    
    /**
     * metodo que calcula el alto de una ventana modal de acuerdo a la cantidad de texto que 
     * se desea mostrar. En windows se calcula automaticamente pero en ubuntu hay un bug por lo que es 
     * necesario hacer un workAround.
     * @param mensaje
     * @return 
     */
    public double calcularHeight(String mensaje){
        int nca = mensaje.length();
        double h = this.DEF_HEIGHT*(Math.floor(nca/34));
        return h;
    }
    
    
    /**
     * metodo que copia texto al porta papeles.
     * se asume que con la libreria de java FX ya es cross plataform.
     * @param text 
     */
    public void Copiar2ClipBoard(String text){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
    
    
    /**
     * para poder realizar interaccion entre controladores de manera sencilla,
     * todos los controladores de los documentos injectatados deben guardar una instancia
     * del documento principal. ya que esto es comun a todos los controldores inyectados
     * entonces se define com atributo de esta super clase y se crea este metodo set
     * @param mainDoc 
     */
    public void setMainController(FXMLDocumentController mainDoc){
        this.MainDocController = mainDoc;
    }
    
    
    /**
     * metodo genera una clave dinamica y luego pregunta al
     * usuario por ella. si el usuario digita correctamente la clave entonces
     * se retorna true, en caso contrario se retorna false.
     * 
     * notese que cada que se desee hacer un DynPassCheck la calve dinamica es
     * diferente.
     * @return 
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws MessagingException
     * @throws SocketException
     */
    public boolean DynPassCheck()
            throws SQLException, ClassNotFoundException, MessagingException, SocketException{
        boolean ok; // indica si es o no exitoso el check
        
        Hndl_Configuracion hndconf = new Hndl_Configuracion();
        hndconf.generarClaveDinamica();
        String DynPass = hndconf.getConfiguracion(SQL_Configuracion.PRT_DYN_PASS);
        
        // 27/01/2021 : enviar token por correo queda deprecado, usar UDP broadcast desde ahora
        // se envia la clave dinamica al correo del administrador 
        //hndconf.SendConfiguredEmail("Tentactil DynPass", DynPass);
        
        // genera un periodic task que hace broadcast del token, se requiere
        // android app para visualizarlo
        hndconf.Send_DynPass_UDP(); 
        
        
        // se pregunta por la clave dinamica
        String input = this.ShowInput("Ingrese la Clave Dinamica Por favor",
                "Al correo configurado se ha enviado una clave dinamica \n"
                        + "ingresela porfavor:", "Clave Dinamica:", "");
        
        // verifica la veracidad de la clave dinamica.
        if(input.equals(DynPass)) ok=true;
        else ok=false;
        
        
        return ok;
    }
    
}
