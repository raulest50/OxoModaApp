package tentactildesktoppos;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import tentactildesktoppos.FXMLinjection.FXMLAbon_DevController;
import tentactildesktoppos.FXMLinjection.FXMLClientesController;
import tentactildesktoppos.FXMLinjection.FXMLConfiguracionController;
import tentactildesktoppos.FXMLinjection.FXMLProductosController;
import tentactildesktoppos.FXMLinjection.FXMLStatsController;
import tentactildesktoppos.FXMLinjection.FXMLVendedoresController;
import tentactildesktoppos.FXMLinjection.FXMLVentasController;



/**
 *
 * el controlador padre de toda la aplicacion de java fx.
 * gracias a fxml injection es posible que el controlador padre acceda
 * a controladores de documentos FX inyectados. solamente se debe
 * usar la anotacion @FXML y el nombre del controlador asi:
 * [fx:id]+Controller.
 * 
 * las clases hijas pueden acceder al controlador padre mediante
 * metodos de los nodod FX y una vez con la clase padre acceder a cualquier
 * otro controlador hijo en otros documentos.
 * @author esteban
 */
public class FXMLDocumentController {
    
    
    /**
     * Se requiere acceder al controlador para verificar cuando hay venta en curso
     * y congelar las funciones de modificacion de productos vendedores o 
     * clientes.
     */
    @FXML
    public FXMLVentasController ventasTabController;
    
    
    /**
     * controlador para acceder a los elementos de FX de la pestaña de abonos y devoluciones
     */
    @FXML
    public FXMLAbon_DevController abon_devTabController;
    
    
    /**
     * controlador de la pestaña de codificaicon de productos.
     * se obtiene por una injection fxml. es decir, al ahcer un include en el FXML
     * la el controlador del FXML padre puede acceder a controladores hijos 
     * invocando el fx:id del inlcude y adicionando la palabra Controller.
     */
    @FXML 
    public FXMLProductosController productosTabController;
    
    
    /**
     * controlador para acceder a los elementos de FX de la pestaña de clientes
     */
    @FXML
    public FXMLClientesController clientesTabController;
    
    
    /**
     * controlador para acceder a los elementos de FX de la pestaña de vendedores
     */
    @FXML
    public FXMLVendedoresController vendedoresTabController;
    
    
    /**
     * controlador para acceder a los elementos de FX de la pestaña de configuracion
     */
    @FXML
    public FXMLConfiguracionController configuracionTabController;
    
    
    /**
     * controlador del tab de Stats que se obtiene directamente
     * con el tag FXML y gracias  a que se incluyo en el documento principal
     * FXML por un include (FXML injection).
     */
    @FXML
    public FXMLStatsController statsTabController;
    
    
    /**
     * tab fx de la pestaña de productos. se instancia para poder hacer request focus de esta pestaña.
     */
    @FXML
    public Tab Tab_productos;
    
    
    /**
     * tab de abonos y devoluciones para poder hacer focus desde otros
     * cotroladores injectados.
     */
    @FXML
    public Tab TabAbonDev;
    
    
    /**
     * obj FX que representa el tab de ventas (solo la pestaña contenedora como objeto gui.)
     */
    @FXML
    public Tab TabVentas;
    
    
    /**
     * TabPane que contiene los diferentes tabs como el tab de productos, ventas clientes etc.
     */
    @FXML
    public TabPane TabPaneFather;
    
    
    /**
     * AnchorPane Parent de todos los documentos de FX inyectados.
     * se instancia para poder obtener un objeto tipo stage a partir de el di fuera necesario.
     */
    @FXML
    public AnchorPane AnchorPrincipal;
    
    
    /**
     * 
     */
    @FXML
    public void initialize() {
        //se entrega a cada controlador hijo una instancia del controlador
        //de la pestaña de ventas para que puedan saber cuando hay un a venta en curso
        this.abon_devTabController.ventasController = this.ventasTabController;
        this.clientesTabController.ventasController = this.ventasTabController;
        this.configuracionTabController.ventasController = this.ventasTabController;
        this.productosTabController.ventasController = this.ventasTabController;
        this.vendedoresTabController.ventasController = this.ventasTabController;
        
        
        /**
         * se pasa una instancia de este main doc a cada uno de los contrladores injectados,
         * de esta forma, esta clase prodra ser usada como puente entre los controladores 
         * injectados para poder realizar interacciones entre ellos. por ejemplo:
         * desde el controlador de abonos y devoluciones acceder al controlador de ventas para poder
         * manipular objetos de FX.
         */
        this.vendedoresTabController.setMainController(this);
        this.productosTabController.setMainController(this);
        this.ventasTabController.setMainController(this);
        this.abon_devTabController.setMainController(this);
        this.configuracionTabController.setMainController(this);
        this.clientesTabController.setMainController(this);
    }


    /**
     * para renovar los items del combo box cuando en la pestaña de configuracion
     * se agregue una nueva clasificacion.
     * Taambien se hace nuevamente la busqueda de productos para actualizar stock
     * en caso de hacer una venta y  mirar inmediatamente despues el stock del 
     * producto.
     * @param e 
     */
    @FXML
    public void onSelChan_Tab_productos(Event e){
        
        // Se hace una nueva busqueda de productos para hacer un refresh del table view
        productosTabController.BuscarProducto(); // y evitar inconsistencias
        
        // se refresca el numero de productos que hay en la BD.
        productosTabController.setROW_Count();
        
        // se actualiza el BarChart de la seccion de codificacion, ver documentacion del metodo en el controller.
        productosTabController.SetTB_ProHistory();
    }
    
    
    /**
     * si hay cliente establecido entonces cada que se haga click para poner la pestaña de ventas
     * entonces se hace focus automatico de el tf de busqueda de producto.
     * en caso contrario se hace focus del tf de busqueda de clientes.
     * @param e 
     */
    @FXML
    public void onSelTabChan_Ventas(Event e){
        if(!(ventasTabController.cliente == null)){
            ventasTabController.TF_buscarProducto.requestFocus();
        } else{
            ventasTabController.TF_buscar_cliente.requestFocus();
        }
    }
    
    /**
     * cuando se realiza una operacion de venta abono etc,
     * esto modifica los valores reales de lo que se dbe mostrar en 
     * el tab de stats. Cuando se cambia al taba de stats, se aprovecha  el
     * evento para hacer una actuaalizacion de los valores en los labels
     * en caso de que estos hayan cambiado.
     * @param e 
     */
    @FXML
    public void onSelTabChan_Stats(Event e){
        this.statsTabController.SetLabels();
    }
    
    
    /**
     * Hace seleccion del tab de productos, luego del TitedPane de Buscar.
     * selecciona la opcion de buscar por codigo, limpia el campo de texto de busqueda de
     * produtos y finalmente le hace un requestFocus.
     */
    public void ShortCutBuscarX_Codigo(){
        // se selecciona el Tab de productos.
        TabPaneFather.getSelectionModel().select(Tab_productos);
        productosTabController.AcordeonProductos.setExpandedPane(productosTabController.TlPane_Buscar);
        productosTabController.CB_tipoBusqueda.getSelectionModel().select(productosTabController.TSEARCH_COD);
        productosTabController.TF_buscar.clear();
        productosTabController.TF_buscar.requestFocus();
    }
    
    
}
