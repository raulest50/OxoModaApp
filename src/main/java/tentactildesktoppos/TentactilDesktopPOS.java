package tentactildesktoppos;


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author esteban
 */
public class TentactilDesktopPOS extends Application {
    
    /**
     * combinacion de teclado para entrar en fullScreen.
     * En ubuntu si se usa ALT_ANY y/o CONTROL_ANY con solo presionar W se entra en kiosk mode por lo que se 
     * debe usar necesariamente combinaciones con ALT_DOWN y CONTROL_DOWN
     */
    final KeyCombination FULL_SCREEN = new KeyCodeCombination(KeyCode.W, KeyCombination.ALT_DOWN, KeyCombination.CONTROL_DOWN);
    
    
    /**
     * combinacion de tecla rapida para realizar (sin importar seccion del programa):
     * se pone en la pestaña de productos, acordeon de busqueda, se limpia el campo de texto, 
     * se pone el en el combo box la opcion de busqueda de por codigo exacto y finalmete
     * se hace request focus del campo de texto de busqueda.
     */
    public final KeyCombination BUSCAR_PRODUCTO = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
    
    
    /**
     * String contante, es el titulo para la venta
     */
    public final String     WINDOW_TITLE = "Tentactil Desktop POS";
    
    
    /**
     * instancia del Parent controller.
     */
    FXMLDocumentController mainDocController;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLMain.fxml"));
        
        Parent root = (Parent) loader.load();
        
        // se obtiene una instancia del controlador padre fx.
        this.mainDocController = (FXMLDocumentController) loader.getController();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        stage.setTitle(WINDOW_TITLE); // se pone el titculo de ventana modal
        
        stage.show(); // se muestra la ventana.
        
        //mi codigo adicional
        stage.setMaximized(true); // se inicia maximizado
        //stage.setFullScreen(true); // full screen --- KIOSK MODE
        
        // se establece el tamaño minimo de la ventana de la aplicacion
        stage.setMinHeight(700); // para que los elementos de la UI
        stage.setMinWidth(1300); // se deformen mas alla de lo tolerable.
        
        /**
         * se agrega un listener para una combinacion de teclas.
         * la funcion es poner pantalla completa KIOSK-MODE. no confundir
         * con ventana maximizada. (se sale con ESC).
         */
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (FULL_SCREEN.match( (KeyEvent) event)) {
                    stage.setFullScreen(true);
                }
            }
        });
        
        
        // configuracion shortcut para hacer busqueda por codigo
        
        /**
         * se adiciona un listener Ctlr+B para automatizar y agilizar la operacion
         * de busqueda de producto por codigo exacto.
         * 
         * ver javaDoc del metodo ShortCutBuscarX_Codigo.
         */
        scene.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler() {
            @Override
            public void handle(Event event) {
                if (BUSCAR_PRODUCTO.match( (KeyEvent) event)) {
                    // automaticamente se pone la interfaz para hacer una busqueda de producto 
                    // por codigo exacto.
                    mainDocController.ShortCutBuscarX_Codigo();
                }
            }
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
