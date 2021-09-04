package tentactildesktoppos.FXMLinjection;


import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.Hndl_Productos;
import BD_tdpos_r.SQL_Configuracion;
import Impresion.LocalPrinter;
import Validator_tdpos_r.Va_Producto;
import Validator_tdpos_r.Validador;
import Validator_tdpos_r.ValidationResult;
import java.io.IOException;
import java.sql.SQLException;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.print.PrintException;
import org.apache.commons.lang3.StringUtils;
import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLPopUps.FXMLModClienteController;
import tentactildesktoppos.FXMLPopUps.FXMLModProductoController;
import tentactildesktoppos.negocio_r_objs.Producto;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptProducto;

/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLProductosController extends ControllerFather{
    
    /**
     * para poder saber desde este controlador si hay una venta en curso
     * y congelar las funcionalidades necesarias para asegurar la consistencia
     * de los datos mostrados en la interafz grafica.
     */
    public FXMLVentasController ventasController;
    
    
    /**
     * acordeon del tab de productos. Se instancia para poder hacer seleccion del tittled pane de 
     * busqueda de productos.
     */
    public Accordion AcordeonProductos;
    
    
    
    /**
     * tipo de busqueda por descripcion - palabras clave.
     * se usa para el comboBox y para el if (.equals)
     */
    public final String TSEARCH_DESCRI = "Busqueda por Descripcion";
    
    /**
     * tipo de busqueda por codigo exacto.
     * se usa para el comboBox de tipo de busqueda y para el if (.equals)
     */
    public final String TSEARCH_COD = "Busqueda por Codigo";
    
    //--        --      --      --
    // seccion de codificacion
    //--        --      --      --
    @FXML
    public TextField TF_codigo, TF_descripcion, TF_costo, TF_pvcontado, 
            TF_pvcredito, TF_pvmayor, TF_iva, TF_stock;
    
    /**
     * Labels para mostrar el porcentaje de ganancia ( los tres primeros). El ultimo label permite saber el
     * numero de productos codificados. LB_num_prod_BD = para mostrar el numero de rows en la tabla de productos
     */
    @FXML
    public Label LB_contado_frac, LB_credito_frac, LB_mayor_frac, LB_num_prod_BD;
    
    /**
     * para seleccionar el tipo de busqueda para los productos.
     */
    @FXML
    public ComboBox CB_tipoBusqueda;
    
    /**
     * checkBox para indicar si se desea imprimir adhesivos con el codigo del producto codificado.
     * chulo -> se imprimer stickers
     * no chulo -> no se imprimen
     */
     @FXML
     public CheckBox CHB_imprimirstk;
     
     
     /**
      * barChart que muestra el numero de productos codificados en los ultimos 7 dias.
      */
     @FXML
     public BarChart BChart_Productos;
     
     
    // --       --      --      --      --
    // seccion de busqueda y modificacion
    // --       --      --      --      --
     
     
     /**
      * check box para decidir si al hacer una busqueda por codigo se limpia o no el campo de texto.
      * esto permitiria hacer multples averiguariones por codigo exacto sin tocar el teclador, usando
      * unicamente el lector de codigo de barras.
      */
     @FXML
     public CheckBox CHBox_SearchN_Clear;
     
     
     /**
      * Se instancia para poder hacer focus de la seccion de busqueda de productos.
      */
     @FXML
     public TitledPane TlPane_Buscar;
    
    
    /**
     * textfield para buscar productos
     */
    @FXML
    public TextField TF_buscar;
    
    
    /**
     * para mostrar los resultados de busqueda en la seccion de seleccion
     * y modificacion
     */
    @FXML
    TableView<SptProducto> TV_productos;
    
    
    /**
     * columnas del  table view de tipo String
     */
    @FXML
    TableColumn<SptProducto, String> TC_codigo, TC_descripcion, TC_fecha;
    
    /**
     * columnas del  table view de tipo Integer
     */
    @FXML
    TableColumn<SptProducto, Integer> TC_costo, TC_contado, TC_credito, 
            TC_mayor, TC_stock;
    
    /**
     * campo de texto para mostrar el ultimo codigo usado para
     * la tabla de productos.
     */
    @FXML
    public TextField TF_Last_Cod;
    
    
    /**
     * atributo para la implementacion de un menu item con click derecho.
     */
    public ContextMenu rg_click_menu;
    
    
    public final String PRINT_STICKERS = "Imprimir Adhesivos";
    
    
    public final String  REQUEST_N_COPIAS_TITLE = "Ingrese un numero.";
    
    /**
     * subtitulo para ventana modal de insertar numero de copias
     */
    public final String REQUEST_N_COPIAS_SUB = "Ingrese Por Favor el Numero de Stickers que desea imprimir.";
    
    /**
     * mensaje para ventana modal de numero de copias para impresora de stickers
     */
    public final String REQUEST_N_COPIAS_MES = "Numero de Copias:";
    
    
    /**
     * Mensaje para menu item click derecho que copia el codigo al portapapeles
     */
    public final String COPIAR_CODIGO= "Copiar Codigo";
    
    
    
    @FXML
    public void initialize() {
        
            this.ConfigTableView();// se configura el table view.
            this.CargarTiposBusqueda(); // se cargan los tipos de busqueda en el
            
            //se configura un menu item para el tableView de busqueda de productos (rg click).
            this.ConfigurarMenuItem_rgClick();
            
            this.CargarIvaDefecto(); // se carga el valor de iva por defecto
            
            this.CargarLastUsedCode(); // se carga el ultimo codigo empleado para ingresar un producto
            
            this.setROW_Count(); // se pone el numero de productos codificados en el label de fx correspondiente
            
            this.SetTB_ProHistory(); // metodo para hacer pruebas con barchart de java fx.
            
            /**
             * se pone por defecto habilitada la opcion search & clear.
             * ver java doc en la definicion de este checkbox para mas informacion.
             */
            this.CHBox_SearchN_Clear.setSelected(true);
    }
    
    
    
    /**
     * Establece en el label correspondiente el numero de filas en la tabla de productos,
     * es decir el numero de prodcuctos codificado.
     */
    public void setROW_Count(){
        int count = 0;
        
        try{
            // conector a la tabla de productos
            Hndl_Productos hndpro = new Hndl_Productos();
            // se consulta el numero de filas
            count = hndpro.CountTable();
            // se establece en el label de fx correspondiente.
            this.LB_num_prod_BD.setText(Integer.toString(count));
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
    
    /**
     * metodo que carga en el campo de texto TF_Last_Cod, el ultimo
     * codigo empleado para inngresar un producto en la tabla de productos
     */
    public void CargarLastUsedCode(){
        try{
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            TF_Last_Cod.setText(hndconf.getConfiguracion(SQL_Configuracion.PRT_LAST_CODE_PROD));
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * metodo que carga en el campo de texto de iva valor establecido por defecto en la pestaña de configuracion
     */
    public void CargarIvaDefecto(){
        // ------ se carga el valor de iva por defecto -------- //
            Hndl_Configuracion hndconf = new Hndl_Configuracion();
            String iva_defecto;
         try {   
            iva_defecto = hndconf.getConfiguracion(SQL_Configuracion.PRT_IVA);
            Integer.parseInt(iva_defecto); // para intentar lanzar una excepcion porm number format.
            // si no ocurre la excepcion entonces se asigna al textfield
            this.TF_iva.setText(iva_defecto);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(FXMLProductosController.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NumberFormatException ex){ // en caso de guardarse en iva por defecto un valor no numerico
            Logger.getLogger(FXMLProductosController.class.getName()).log(Level.SEVERE, null, ex);
            // no se hace nada, por lo que el campo de iva por defecto queda vacio.
        }
    }
    
    
    /**
     * Cuando se unde enter en el textfield de codigo
     * @param ae 
     */
    @FXML
    public void onAction_TF_codigo(ActionEvent ae){
        TF_descripcion.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de descripcion
     * @param ae 
     */
    @FXML
    public void onAction_TF_descripcion(ActionEvent ae){
        TF_costo.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de costo
     * @param ae 
     */
    @FXML
    public void onAction_TF_costo(ActionEvent ae){
        TF_pvmayor.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de precio venta contado
     * @param ae 
     */
    @FXML
    public void onAction_TF_pvcontado(ActionEvent ae){
        TF_pvcredito.requestFocus();
    }
    
    
    
    /**
     * Cuando se unde enter en el textfield de precio venta credito
     * @param ae 
     */
    @FXML
    public void onAction_TF_credito(ActionEvent ae){
        TF_iva.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de precio venta por mayor
     * @param ae 
     */
    @FXML
    public void onAction_TF_mayor(ActionEvent ae){
        TF_pvcontado.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de iva
     * @param ae 
     */
    @FXML
    public void onAction_TF_iva(ActionEvent ae){
        TF_stock.requestFocus();
    }
    
    
    /**
     * Cuando se unde enter en el textfield de stock
     * @param ae 
     */
    @FXML
    public void onAction_TF_stock(ActionEvent ae){
        
    }
    
    
    /**
     * cuando se hace click en el boton borrar.
     * todos los campos se ponen en ceros.
     * pestaña de codificacion
     * @param me 
     */
    @FXML
    public void onCLick_B_borrar(MouseEvent me){
        this.clearFields();
    }
    
    
    /**
     * cuando se presiona la tecla enter y el boton de borrar
     * esta seleccinado.
     * pestaña de codificacion
     * @param ae 
     */
    @FXML
    public void onAction_B_borrar(ActionEvent ae){
        this.clearFields();
    }
    
    
    /**
     * metodo para limpiar los textfields.
     * seccion de codificacion
     */
    public void clearFields(){
        TF_codigo.setText("");
        TF_descripcion.setText("");
        TF_costo.setText("");
        TF_pvcontado.setText("");
        TF_pvcredito.setText("");
        TF_pvmayor.setText("");
        TF_iva.setText("");
        TF_stock.setText("");
        this.UpdateLabels(); // se actualizan las etiquetas de porcentaje
        this.CargarIvaDefecto();  // se carga el valor de iva por defecto.
        TF_codigo.requestFocus(); // se pone el cursor nuevamente en el tf de codigo.
    }
    
    
    /**
     * cuando se hace click en boton de borrar en la pestaña de busqueda
     * y modificacion.
     * @param ae 
     */
    @FXML
    public void onAction_B_borrar_mod(ActionEvent ae){
        TF_buscar.clear();
        TF_buscar.requestFocus();
    }
    
    
    /**
     * 
     * @param me 
     */
    @FXML
    public void onClick_B_borrar_mod(MouseEvent me){
        TF_buscar.clear();
        TF_buscar.requestFocus();
    }
    
    
    /**
     * cuando se hace click en el boton codificar.
     * todos los campos se ponen en ceros.
     * @param me 
     */
    @FXML
    public void onCLick_B_codificar(MouseEvent me){
        this.codificarProducto();
    }
    
    /**
     * cuando se hace click al boton de modificacion de productos, en el 
     * acordeon de busqueda y modificacion.
     * @param me 
     */
    @FXML
    public void onClick_B_Modificar(MouseEvent me){
        this.PopUpModificarProducto(me);
    }
    
    
    /**
     * cuando se presiona enter y el boton de codificar esta seleccionado
     * @param ae 
     */
    @FXML
    public void onAction_B_codificar(ActionEvent ae){
        //this.codificarProducto();
        // se inabilita porque en ubuntu, a diferencia de windows,
        // se activan onClick y onAction uno despues de otro
        // y el metodo se ejecuta 2 veces como consecuencia.
        // como nueva convencion se deja colo onClick para los botones.
    }
    
    
    
    /**
     * metodo que toma la informacion de la gui, la valida y
     * en caso de tenersen datos validos se hace la dcodificacion del
     * producto.
     */
    public void codificarProducto(){
        try{
            String codigo = this.TF_codigo.getText(); // se toman los datos de los TextField
            String descripcion = this.TF_descripcion.getText();
            String costo = this.TF_costo.getText();
            String pv_contado = this.TF_pvcontado.getText();
            String pv_credito = this.TF_pvcredito.getText();
            String pv_mayor = this.TF_pvmayor.getText();
            String stock = this.TF_stock.getText();
            String iva = this.TF_iva.getText();
            
            Hndl_Productos hndp = new Hndl_Productos();
            
            if(StringUtils.isAllBlank(codigo)){ //  si el codigo se deja vacio
                // por defecto se calcula el siguiente y se pone
                Hndl_Configuracion hndconf = new Hndl_Configuracion();
                // se toma el ultimo codigo usago que se guardo en el programa.
                String prev = hndconf.getConfiguracion(SQL_Configuracion.PRT_LAST_CODE_PROD);
                codigo = hndp.getNextProductCode(prev); // ver documentacion en la implementacion.
            }
            
            
            Va_Producto vp = new Va_Producto();
            ValidationResult vr = vp.ProductoEsValido(codigo, descripcion,
                    costo, pv_contado, pv_credito, pv_mayor, stock, iva);
            
            if(vr.result){ //si los datos son validos se guarda el porducto en la base de datos
                
                
                // se contruye el obejto tipo producto.
                // se usa Validador.emptyDate_str ya que en realidad se ingresa con curdate() de MySQL.
                Producto p = new Producto(codigo, descripcion, costo, pv_contado,
                        pv_credito, pv_mayor, Validador.EMPTY_DATE_STR, stock, iva);
                
                hndp.codificarProducto(p); // se guarda en MySQL.
                this.CargarLastUsedCode(); // se carga el ultimo codigo empleado para ingresar un producto
                if(this.CHB_imprimirstk.isSelected()) this.PrintSticker(p);
                super.ShowInfo(super.exito, super.si_operacion, ""); // se notifica al usuario que la operacion fue exitosa
                this.clearFields(); // se limpian los campos
                this.TF_codigo.requestFocus(); // se pone cursor en TF de codigo.
                this.setROW_Count(); // se establece el numero de productos codificados
                this.SetTB_ProHistory(); // se actualiza el BarChart
                
            } else{ // en caso contrario se muestra un mensaje indicando la razon del error
                super.ShowInfo(super.no_operacion, super.recti_mess, vr.motivo);
            }
        }
        catch (SQLException | ClassNotFoundException ex) { // ventana modal de error en caso de una excepcion.
            Logger.getLogger(FXMLProductosController.class.getName()).log(Level.SEVERE, null, ex);
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
       
        
    /**
     * cuando se oprime cualquier tecla y este textfield esta seleccioando
     * @param ke 
     */
    @FXML
    public void onKey_TF_costo(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * cuando se oprime cualquier tecla y este textfield esta seleccioando
     * @param ke 
     */
    @FXML
    public void onKey_TF_pvcontado(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * cuando se oprime cualquier tecla y este textfield esta seleccioando
     * @param ke 
     */
    @FXML
    public void onKey_TF_pvcredito(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * cuando se oprime cualquier tecla y este textfield esta seleccioando
     * @param ke 
     */
    @FXML
    public void onKey_TF_pvmayor(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * 
     * @param ke 
     */
    @FXML
    public void onKeyRel_TF_costo(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * 
     * @param ke 
     */
    @FXML
    public void onKeyRel_TF_pvcontado(KeyEvent ke){
        UpdateLabels();
    }
    
    
    /**
     * 
     * @param ke 
     */
    @FXML
    public void onKeyRel_TF_pvcredito(KeyEvent ke){
        UpdateLabels();
    }
    
    
    
    
    /**
     * 
     * @param ke 
     */
    @FXML
    public void onKeyRel_TF_pvmayor(KeyEvent ke){
        UpdateLabels();
    }
    
    /**
     * Este metodo es el encargado de actualizar los porcentajes de ganancia 
     * de cada uno de los precios de venta en los labels
     */
    public void UpdateLabels(){
        String frc_nulo = "--%";
        double costo;
        double pv_contado; // precio de venta contado.
        double pv_credito;
        double pv_mayor; // precio de venta por mayor.
        
        double prc_contado, prc_credito, prc_mayor;
        String str_contado, str_credito, str_mayor;
        
        // para number format exception
        try{
            costo = Double.parseDouble(TF_costo.getText());
            pv_contado = Double.parseDouble(TF_pvcontado.getText());
            pv_credito = Double.parseDouble(TF_pvcredito.getText());
            pv_mayor = Double.parseDouble(TF_pvmayor.getText());
            
            prc_contado = ((pv_contado-costo)/costo)*100;
            prc_credito = ((pv_credito-costo)/costo)*100;
            prc_mayor = ((pv_mayor-costo)/costo)*100;
            
            if(prc_contado < 0) str_contado = "xx";
            else str_contado = Double.toString(prc_contado);
            
            if(prc_credito < 0) str_credito = "xx";
            else str_credito = Double.toString(prc_credito);
            
            if(prc_mayor < 0) str_mayor = "xx";
            else str_mayor = Double.toString(prc_mayor);
            
            LB_contado_frac.setText(str_contado + "%");
            LB_credito_frac.setText(str_credito + "%");
            LB_mayor_frac.setText(str_mayor + "%");
            
        } catch(NumberFormatException ex){
            LB_contado_frac.setText(frc_nulo);
            LB_credito_frac.setText(frc_nulo);
            LB_mayor_frac.setText(frc_nulo);
        }    
    }
    
    
    
    /**
     * enter en boton de buscar pestaña de buscar y modificar
     * @param ae 
     */
    @FXML
    public void onAction_B_buscar(ActionEvent ae){
        this.BuscarProducto();
    }
    
    
    /**
     * click en boton de buscar. pestaña de busca y modificar
     * @param ae 
     */
    @FXML
    public void onClick_B_buscar(MouseEvent ae){
        this.BuscarProducto();
    }
    
    
    /**
     * enter en el textField de buscar producto.
     * @param ae 
     */
    @FXML
    public void onAction_TF_buscar(ActionEvent ae){
        this.BuscarProducto();
    }
    
    
    /**
     * metodo para buscar productos y popular el TableView de la pestaña
     * buscar y modificar productos
     */
    public void BuscarProducto(){
        
        // se lee el tipo de busqueda deseado
        String tipoBusqueda = CB_tipoBusqueda.getSelectionModel().getSelectedItem().toString();
        
        Hndl_Productos hndp = new Hndl_Productos();
        String busqueda = TF_buscar.getText();
        
        ArrayList<Producto> lp =  new ArrayList<>();
        
        try{
            
            if(tipoBusqueda.equals(this.TSEARCH_COD)){ // busqueda por codigo exacto
                lp = hndp.buscarProducto_codigo(busqueda);
            }
            if(tipoBusqueda.equals(this.TSEARCH_DESCRI)){ // busqueda por palabras clave
                lp = hndp.buscarProducto_nombre(busqueda);
            }
            
            // populating the TableView whit the search results.
            this.SetTV_Productos(this.fromProducto2Spt(lp));
            
            if(tipoBusqueda.equals(this.TSEARCH_COD)) this.TF_buscar.requestFocus(); // se pone el focus nuevamente para que se seleccione el String de busqueda
            // y poder usar laser nuevamente sin usar el boton borrar si es el caso.
            
            
            /**
             * si la opcion search and clear esta seleccionada entonces se limpia el campo de texto de busqueda de
             * productos justo despues de terminar la operacion de busqueda.
             */
            if(CHBox_SearchN_Clear.isSelected() && tipoBusqueda.equals(this.TSEARCH_COD)) this.TF_buscar.clear();
            
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuario con un dialogo modal en caso de excepcion
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
    }
    
    
    /**
     * se configuran las columnas del TableView deacuerdo a los datos que se van 
     * a mostrar en cada una. es una operacion requerida por la api de java fx.
     */
    public void ConfigTableView(){
        this.TC_codigo.setCellValueFactory(new PropertyValueFactory<>("spt_codigo"));
        this.TC_descripcion.setCellValueFactory(new PropertyValueFactory<>("spt_descripcion"));
        this.TC_costo.setCellValueFactory(new PropertyValueFactory<>("spt_costo"));
        this.TC_contado.setCellValueFactory(new PropertyValueFactory<>("spt_pv_contado"));
        this.TC_credito.setCellValueFactory(new PropertyValueFactory<>("spt_pv_credito"));
        this.TC_mayor.setCellValueFactory(new PropertyValueFactory<>("spt_pv_mayor"));
        this.TC_fecha.setCellValueFactory(new PropertyValueFactory<>("spt_fingreso"));
        this.TC_stock.setCellValueFactory(new PropertyValueFactory<>("spt_stock"));   
    }
    
    
    /**
     * inicializa el ComboBox para seleccionar el tipo de busqueda.
     * pestaña de busqueda y modificacion de productos
     */
    public void CargarTiposBusqueda(){
        ArrayList<String> ltpb = new ArrayList<>();
        
        ltpb.add(this.TSEARCH_COD);
        ltpb.add(this.TSEARCH_DESCRI);
        
        super.SetComboBox(CB_tipoBusqueda, ltpb);
    }
    
    
    /**
     * metodo que recive un arrayList y lo convierte a Observable list
     * para popular el table View.
     * pestaña de busqueda y modificacion de productos.
     * @param lp 
     */
    public void SetTV_Productos(ArrayList<SptProducto> lp){
        ObservableList<SptProducto> ovl_lp = FXCollections.observableArrayList(lp);
        this.TV_productos.setItems(ovl_lp);
    }
    
    
    /**
     * metodo que recive un ArrayList Producto y lo transforma a
     * ArrayList SptProducto para poder mostrar los resultados en el
     * table view.
     * @param lp
     * @return 
     */
    public ArrayList<SptProducto> fromProducto2Spt(ArrayList<Producto> lp){
        ArrayList<SptProducto> lspt = new ArrayList<>();
        
        for(int i=0; i<lp.size(); i++){
            lspt.add(new SptProducto(lp.get(i)));
        }
        return lspt;
    }
    
    
    /**
     * documentado en la definicion abstracta de la superclase. 
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.TV_productos.getScene().getWindow();
    }
    
    
    /**
     * se configura el click derecho
     */
    public void ConfigurarMenuItem_rgClick(){
        rg_click_menu = new ContextMenu();
        
        MenuItem menu_PrintSticker = new MenuItem(PRINT_STICKERS);
        MenuItem menu_copiarCodigo = new MenuItem(COPIAR_CODIGO);
        
        /**
         * cuando se selecciona impresion de stickers con el menu item (rg click)
         */
        menu_PrintSticker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_PrintSticker();
            }
        });
        
        /**
         * cuando se selecciona copiar codigo del producto seleccioando
         */
        menu_copiarCodigo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_CopyCode();
            }
        });
        
        rg_click_menu.getItems().add(menu_PrintSticker);
        rg_click_menu.getItems().add(menu_copiarCodigo);
        this.TV_productos.setContextMenu(rg_click_menu);
    }
    
    
    /**
     * para hacer la impresion de estickers desde la seccion de busqueda de productos
     */
    public void Action_menu_PrintSticker(){
        if(!this.TV_productos.getSelectionModel().isEmpty()){ // si hay item seleccionado- >
            SptProducto sel_pro = this.TV_productos.getSelectionModel().getSelectedItem();
            this.PrintSticker(sel_pro);
        } else{
            // si no hay producto seleccinado no se hace nada.
        }
    }
    
    /**
     * para copiar el codigo del producto seleecionado al protapapeles.
     */
    public void Action_menu_CopyCode(){
        if(!this.TV_productos.getSelectionModel().isEmpty()){ // si hay item seleccionado- >
            SptProducto sel_pro = this.TV_productos.getSelectionModel().getSelectedItem();
            super.Copiar2ClipBoard(sel_pro.codigo);
        } else{
            // si no hay producto seleccinado no se hace nada.
        }
    }
    
    
    
    /**
     * metodo que se ejecuta cuando se desaea hacer una impresion de stickers con el codigo de un producto
     */
    public void PrintSticker(Producto pro){
        String copias_str = super.ShowInput(this.REQUEST_N_COPIAS_TITLE, this.REQUEST_N_COPIAS_SUB, this.REQUEST_N_COPIAS_MES, "");
        int copias = 0; // numero de stickers a imprimir
        
        Validador valit = new Validador();
        
        boolean ok = false; // indica si es valido intentar impresion, y hacer mas claros los nested ifs
        
        if(valit.isPositiveNum(copias_str)){
            copias = Integer.parseInt(copias_str);
            if(copias>0) ok=true;
        }
        try{
            if(ok){
                LocalPrinter lp = new LocalPrinter();
                lp.ImprimirStickers(pro.codigo, copias);
            }
            // excepcion debida a un error buscando nombre de la impresora en la tabla de configuracion
        } catch (ClassNotFoundException | SQLException ex){
            super.ShowWarning(SuppMess.EXCEPTION, // se notifica al usuario en caso de una exepcion por acceso a BD
                    SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            ex.printStackTrace();
        } catch(RuntimeException rex){ // exepcion cuando no se encuentra el printer service que concuerde con
            // el nombre de la impresora guardado en la tabla de configuracion.
            super.ShowWarning(LocalPrinter.PROBLEM_TITLE, LocalPrinter.PROBLEM_SUB, LocalPrinter.PROBLEM_FIND_PRINT_SERVICE);
            rex.printStackTrace();
            
        } catch(IOException ioex){ // si hay un error generando el png del ean 128.
            super.ShowWarning(SuppMess.EXCEPTION, // se notifica al usuario en caso de una exepcion por Escritura de archivo png.
                    SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ioex.getMessage());
            ioex.printStackTrace();
        } catch(PrintException prex){ // si hay un error durante la ejecucion de la impresion.
            super.ShowWarning(LocalPrinter.PROBLEM_TITLE, LocalPrinter.PROBLEM_SUB, LocalPrinter.PROBLEM_PRINTER_MESS);
            prex.printStackTrace();
        }
    }
    
    
    /**
     * lanza una ventana modal que permite modificar un producto seleccionado
     * @param event 
     */
    public void PopUpModificarProducto(Event event){
        if(TV_productos.getSelectionModel().isEmpty()){
            // se notifica al usuario que no hay item seleccionado de la tabla
            this.ShowWarning(SuppMess.NO_SEL_ITEMTAB_TITLE,
                    SuppMess.NO_SEL_ITEMTAB_SUB, SuppMess.NO_SEL_ITEMTAB_MESS);
        } else {
            try{
                // se revisa si hay o no lock
                Hndl_Configuracion hndconf = new Hndl_Configuracion();
                String lock = hndconf.getConfiguracion(SQL_Configuracion.PRT_LOCKED);
                
                if(lock.equals(SQL_Configuracion.NOT_LOCKED)){
                    SptProducto pro = TV_productos.getSelectionModel().getSelectedItem();
                           
                    // se caraga el path del FXML
                    FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                            getResource("tentactildesktoppos/FXMLPopUps/FXMLModProducto.fxml"));
                    Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
                    Stage st = new Stage();// modificacion
                    st.setScene(new Scene(root));
                    st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
                    st.initOwner( ((Node) event.getSource()).getScene().getWindow());
                    FXMLModProductoController c = cargador.<FXMLModProductoController>getController(); // se obtiene el controlador
                    c.init(pro, this);// se inicializan los valores de la ventana
                    st.show(); // se muestra la ventana
                }
            } catch (IOException | ClassNotFoundException | SQLException ex) {
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
        }
    }
    
    @FXML
    public void onKeyPressCBoxSearchPro(KeyEvent me){
        TF_buscar.requestFocus();
    }
    
    
    /**
     * metodo que establece en uno de los labels de la seccion de codificacion de productos, el numero total
     * de producos codificados en la base de datos.
     */
    public void CargarNumeroProCodificados(){
        Hndl_Productos hndpro = new Hndl_Productos();
        
    }
    
    
    
    /**
     * carga un grafico de los records ingresados a la tabla de productos
     * en los ultimos 7 dias incluyendo "hoy".
     */
    public void SetTB_ProHistory(){
        
        this.BChart_Productos.getXAxis().setLabel("Fecha");
        this.BChart_Productos.getYAxis().setLabel("Nuevos Records + Mods.");
        
        Hndl_Productos hndpro = new Hndl_Productos();
        this.BChart_Productos.getData().clear(); // se limpia en cso de que ya halla un series agregado
        // ya que el metodo que se emplea no es tipo set sino tipo add.
        try{
            XYChart.Series series = hndpro.getLast7Records();
            this.BChart_Productos.getData().add(series);
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
    }
    
    
}
