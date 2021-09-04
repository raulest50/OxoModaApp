package tentactildesktoppos.FXMLinjection;

import BD_tdpos_r.Hndl_Abonos;
import BD_tdpos_r.Hndl_Clientes;
import BD_tdpos_r.Hndl_Cupon;
import BD_tdpos_r.Hndl_Devoluciones;
import BD_tdpos_r.Hndl_Factura;
import BD_tdpos_r.Hndl_Productos;
import Impresion.LocalPrinter;
import Validator_tdpos_r.Va_Abo_Dev;



import Validator_tdpos_r.ValidationResult;
import java.io.IOException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.Objects.isNull;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.util.stream.Collectors.groupingBy;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.print.PrintException;

import support_tdpos.SuppMess;
import tentactildesktoppos.ControllerFather;
import tentactildesktoppos.FXMLPopUps.FXMLSeeFactuSintesisController;
import tentactildesktoppos.negocio_r_objs.Abono;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.Cupon;
import tentactildesktoppos.negocio_r_objs.Devolucion;
import tentactildesktoppos.negocio_r_objs.Factura;
import tentactildesktoppos.negocio_r_objs.Producto;
import tentactildesktoppos.negocio_r_objs.ProductoRecord;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;



/**
 * FXML Controller class
 *
 * @author esteban
 */
public class FXMLAbon_DevController extends ControllerFather{

    
    /**
     * Title pane de la seccion para hacer abono d la factura seleccionada.
     */
    @FXML
    public TitledPane TLPane_AbonDev;
    
    
    /**
     * Instancia del title pane para hacer buqueda de las facturas de un cliente determinado
     */
    @FXML
    public TitledPane TLPane_GenCob;
    
    
    /**
     * acordeon de la seccion de abonos y devoluciones.
     */
    @FXML
    public Accordion AccordionAbonDev;
    
    
    
    /**
     * para poder saber desde este controlador si hay una venta en curso
     * y congelar las funcionalidades necesarias para asegurar la consistencia
     * de los datos mostrados en la interafz grafica.
     */
    public FXMLVentasController ventasController;
    
    
    //****************
    // seccion de abonos y devoluciones.

    
    /**
     * tableView en el que se muestran las operaciones de : devolucion y abono
     * de la factura seleccionada.
     */
    @FXML
    public TableView<SptFacturaRecord> TV_operaciones;
    
    
    /**
     * columnas de la tabla correspondiente a los datos en BD de la factura. tipo String.
     */
    @FXML 
    public TableColumn<SptFacturaRecord, String> TC_FAC_codigo, TC_FAC_descripcion, TC_FAC_fecha;
    
    /**
     * columnas de la tabla correspondiente a los datos en BD de la factura. tipo Integer.
     */
    @FXML
    public TableColumn<SptFacturaRecord, Integer>  TC_FAC_pventa, TC_FAC_cantidad, TC_FAC_subtotal;
    
    /**
     * tabla en la que se miuestran los records actuales correspondientes a la 
     * factura seleccionada
     */
    @FXML
    public TableView<SptFacturaRecord> TV_factura;
    
    
    /**
     * columnas del tableView de operaciones tipo String
     */
    @FXML
    public TableColumn<SptFacturaRecord, String> TC_OPE_codigo, TC_OPE_descripcion;
    
    /**
     * columnas del tableView de operaciones tipo Integer.
     */
    @FXML
    public TableColumn<SptFacturaRecord, Integer> TC_OPE_pventa, TC_OPE_cantidad, TC_OPE_subtotal;
    
    
    /**
     * campo de texto para hacer busqueda de una factura.
     */
    @FXML
    public TextField TF_buscar_factura;
    
    
    /**
     * text area para mostrar los datos de una factura.
     */
    @FXML
    public TextArea TA_info_factura;
    
    
    /**
     * campo de texto donde se ingresa el codigo de la prenda que se desea 
     * devolver
     */
    @FXML
    public TextField TF_devolucion;
    
    /**
     * toggle button de la seccion de abonos y devoluciones.
     * cuando esta presionado se hace sintesis en la tabla de records
     * actuales de la factura. cuando no esta activado se muestran
     * todos los records de pr y devoluciones sin unficar
     */
    @FXML
    public ToggleButton TOGB_s;
    
    
    /**
     * Cuando busca una factura para hacer una operacion de devolucion o abono
     * y esta no se encuntra en la BD, se usa este mensaje para notificar en el
     * TextArea.
     */
    public final String NOT_FOUND_FACT = "No existe en la BD una factura con el\n "
            + "consecutivo especificado, revise los datos porfavor";
    
    
    /**
     * titulo ventana modal ingreso de prendas
     */
    public final String INPUT_QNT_DEV_TITLE = "Ingrese una cantidad Por favor";
    
    /**
     * subtitulo ventana modal ingreso de prendas
     */
    public final String INPUT_QNT_DEV_SUB = "Ingrese porfavor el numero de prendas que desea \n"
            + " devolver";
    
    /**
     * mensaje ventana modal ingreso de prendas
     */
    public final String INPUT_QNT_DEV_MESS = "Unidades: ";
    
    /**
     * titulo para ventana modal de ingreso de abono
     */
    public final String ABONO_INPUT_TITLE = "Abono";
    
    
    /**
     * subtitulo para ventana modal de ingreso de abono
     */
    public final String ABONO_INPUT_SUB = "Ingrese porfavor el valor en pesos\n"
            + " que se desea abonar a la factura seleccionda";
    
    
    /**
     * mensaje para ventana modal de ingreso de abono
     */
    public final String ABONO_INPUT_MESS = "Abonar: ";
    
    /**
     * cuando se intenta ingresar los datos de una devolucion o abono
     * que exece el saldo de la factura
     */
    public final String NO_OPE_MESS = "El monto de los intems ingresados superan \n "
            + "el saldo de la factura, rectifique la informacion porfavor.";
    
    
    /**
     * mensaje para ventana modal cuando se agrea un abono a la tabla de operaciones y la factura seleccionada
     * ya esta cancelada (saldo cero).
     */
    public final String NO_OPE_CONT_ABONO_MES =    "Esta factura ya ha sido cancelada, no se pueden hacer \n"
            + "mas ingresos de efectivo a esta factura.";
    
    
    /**
     * mensaje para ventana modal que explica que las prendas a devolver en la factura superan el ingreso de 
     * efectivo de la misma.
     */
    public final String NO_CUPON_QNT_MESS = "El monto en efectivo de las prendas que desea devolver supera \n "
            + "la cantidad de efectivo que se pago en esta factura.";
    
    /**
     * factura seleccionada actualmente
     */
    public Factura factura;
    
    /**
     * checkbox en la sseccion de abonos y devoluciones para determinar si se 
     * imprime o no comprobante despues de hacer una devolucion o un abono.
     */
    public CheckBox CHB_Print_Comp;
    
    //*************
    // seccion de busqueda de faturas.
    
    
    /**
     * table view que muestra los resultados de la busqueda de facturas.
     */
    @FXML
    public TableView<SptFactura> TV_busqueda;
    
    
    /**
     * campo de texto de busqueda de faturas de la seccion de busqueda.
     * en la que hay una sola tabla y criterio de ordenamiento.
     */
    @FXML
    public TextField TF_SEARCH_buscar_factura;
    
    
    /**
     * combo Box para seleccionar el criterio de busqueda para las facturas. acordeon de busqueda de 
     * facturas.
     */
    @FXML
    public ComboBox CB_tipo_busqueda;
    
    
    /**
     * para seleccionar el criterio de ordenamiento en el acordeon de busqueda de facturas.
     */
    @FXML
    public ComboBox CB_orden;      
    
    
    /**
     * checkbox que permite seleccionar si se traen las facturas con 
     * saldo o sin saldo
     */
    @FXML
    public CheckBox ChkBox_con_saldo;
    
    
    /**
     * Columnas del table view de busqueda de facturas.
     * Atributos con forma String
     */
    @FXML
    public TableColumn<SptFactura, String> TC_cli_id_facsearch, TC_cli_name_facsearch, TC_femision_facsearch, TC_ult_abono_facsearch;
    
    /**
     * columnas del table view de busqueda de facturas.
     * Atributos con forma Integer
     */
    @FXML
    public TableColumn<SptFactura, Integer> TC_saldo_facsearch, TC_consec_facsearch;
    
    
    
    //***** Strings constantes para los combo Box de la seccion de busqueda.
    
    /**
     * Busqueda de facturas que coincidad con el id ingresado.
     */
    public final String BUSQUEDA_X_CLIENTE_ID = "Id Cliente";
    
    
    /**
     * Busqueda de facturas cuyo cliente asociado coincida con el nombre especificado.
     */
    public final String BUSQUEDA_X_CLIENTE_NAME = "Nombre Cliente";
    
    /**
     * Criterio de busqueda que trae todas las facturas con saldos menores a los especificados 
     * en el campo de texto
     */
    public final String BUSQUEDA_SALDO_MENOR_Q = "Saldo menor igual que";
    
    /**
     * Criterio de busqueda que trae todas las facturas con saldos mayores a los especificados 
     * en el campo de texto
     */
    public final String BUSQUEDA_SALDO_MAYOR_Q = "Saldo mayor igual que";
    
    /**
     * Criterio de busqueda que trae todas las facturas con mas o igual dias sin abonar especificados
     * en el campo de texto de busqueda.
     */
    public final String BUSQUEDA_LAST_ABONO_MAYOR_Q = "Dias sin abonar";
    
    
    // String constantes para el combo Box de criterio de ordenamiento
    
    
    /**
     * ordena de mayor a menor deacuerdo los dias desde el utimo abono
     */
    public final String ORDEN_MAS_DIAS_SIN_ABONAR = "Mayor a menor dias sin abonar";
    
    
    /**
     * ordena de menor a mayor deacuerdo los dias desde el utimo abono
     */
    public final String ORDEN_MENOS_DIAS_SIN_ABONAR = "Menor a mayor dias sin abonar";
    
    /**
     * se guarda una instancia global de los elementos del tableview para poder hacer filtracion
     * de uno de los atributos sin perder records con el metodo remove del tableView.
     * Cada que se hace una busqueda este atributo debe establecerse como una copia de los 
     * elementos del table view del acordeon de gestion cobros.
     */
    public ObservableList<SptFactura> lsfac_global = FXCollections.observableArrayList();
    
    
    /**
     * Para implementar menu desplegable al hacer click derecho.
     * para la tabla de busqueda de facturas. gestion de cobros y busqueda de facturas.
     */
    public ContextMenu rg_click_menu_TV_buscar_factu;
    
    
    /**
     * constant string para menu de click derecho en la tabla de busqueda, seccion de gestion de cobros
     */
    public final String IMPRIMIR_COPIA_FACTURA = "Imprimir Copia";
    
    
    /**
     * constant string para menu de click derecho en la tabla de busqueda, seccion de gestion de cobros
     */
    public final String SEND_FACTURA = "Eviar a seccion de abonos y devs.";
    
    
    /**
     * Constant string mensaje para menu item click derecho, copiar codigo de la devolucion seleccionada.
     */
    public final String COPIAR_CODIGO = "Copiar Codigo";
    
    /**
     * context menu para la tabla de los records actuales de la factura, seccion de abonos y devoluciones.
     */
    public ContextMenu rg_click_menu_TV_factu_abon_dev;
    
    /**
     * inicializacion del controlador
     */
    @FXML
    public void initialize() {
        // se configura el tipo de dato de todos los table View de esta seccion
        this.ConfigureAllTableViews(); 
        
        // se cargan los valores de los comboBox
        this.Config_All_Cbox();
        
        // se agrega un listener al cambio del check box para seleccionar si se desean mostrar las
        // facturas pendientes o incluyendo las que ya estan canceladas.
        this.ChkBox_con_saldo.selectedProperty().addListener((observable, oldValue, newValues) ->{
            SetTV_GCobro_B_saldo(lsfac_global);
        });
        
        // se configura un menu item en la tabla de busqueda de facturas, seccion de gestion de cobros.
        this.ConfigurarMenuItem_rgClick();
        
        this.TOGB_s.setDisable(true); // el boton de sintesis empieza inhabilitado por defecto
        
        // por defecto siempre se imprimen comprobantes de los abonos.
        this.CHB_Print_Comp.setSelected(true);
    }    
    
    
    
    /**
     * cuando se presiona enter en el textField para ingresar la devolucion
     * @param ae 
     */
    @FXML
    public void onAction_TF_devolucion(ActionEvent ae){
        
        this.AgregarDevolucionRecord2();
        // se toma string del campo de texto.
        
        // se busca producto en la lista de los records de la factura
        // si existe entonces se pide con dialogo modal la cantidad de prendas a devolver.
        // se valida la cantidad, que no supere el valor de prendas compradas.
        // si los datos son validos se agrega el record a la tabla de operaciones.
        
        //si no esta la prenda se notifica al usuario y no se hace mas
    }
    
    
    /**
     * en la seccion de ventas se observo que era mas eficiente asumir cada lectura del laser como 1
     * e ir acumulando e unificando los productos de un mismo codigo.
     * 
     * este mismo comportamiento se desea replicar en la seccion de abonos y devoluciones.
     * sin embargo el algoritmo para esta seccion de complica ya que hay que tener en cuenata 2
     * tablas una de facturas y otra tabla de operaciones.
     * 
     */
    public void AgregarDevolucionRecord2(){
        
        // se lee el String ingresado por el lector de codigo de barras
        String cod = this.TF_devolucion.getText();
        
        /**
         * variable auxiliar para evitar muchos nested ifs.
         */
        boolean ok = true;
        
        /**
         * se revisa si hay una factura seleccionada.
         */
        if(this.factura == null){
            ok = false; // si no hay factura se frena el proceso y se notifica al usuario
            super.ShowError("Error", "", "Debe seleccionar una factura antes de registrar una devolucion");
        }
        
        /**
         * en caso de nhaber factura seleccinoda se procede
         */
        if(ok){
            // Se verifica su pertenencia a la factura.
            ObservableList<SptFacturaRecord> r = this.getFRecord(TV_factura.getItems(), cod);
            if(r.isEmpty()){
                ok = false;
                super.ShowError("Error", "", "El producto indicado no pertenece a esta factura.");
            }
        }
        
        /**
         *si ok== true entonces si hay factura seleccionada y ademas el producto indicado pertenece
         * a la factura que esta seleccinada.
         */
        if(ok){
            try {
                int ne = this.getQntPr(cod);
                if(ne<1){
                    ok = false; // se para la operacion.
                    super.ShowError("Error", "", "No puede exeder el numero de prendas pendientes.");
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
                ok = false;
            }
        }
        
        /**
         * si entra entonces, hay factura seleccionada y el producto pertenece a la factura y hay
         * consistencia de las cantidades de ambas tablas.
         */
        if(ok){
            this.AddPR(cod);
        }
        
    }
    
    
    /***
     * metodo que agrega un record tipo devolucion en la tabla de operaciones
     * solo si es valido.
     */
    public void AgregarDevolucionRecord(){
        // se toma el codigo del producto que se desea devolver.
        String str_dev = this.TF_devolucion.getText();
        Va_Abo_Dev vadev = new Va_Abo_Dev();// objeto para hacer validaciones
        int qnt; //cantidad de unidades a devolver.
        String qn_str; // String entrada del usuario cantidad de unidades a devolver.
        try{
            boolean ok = false; // bandera, indica si se paso o no las 2 validaciones.
            ValidationResult vr = vadev.PreDevolucionValida(factura, this.TV_operaciones.getItems(), str_dev);
            // numero de prendas que se desea devolver
            if(vr.result){ //si pasa la primera validacion se lee el numero de prendas a devolver
                //qn_str = super.ShowInput(this.INPUT_QNT_DEV_TITLE,this.INPUT_QNT_DEV_SUB,this.INPUT_QNT_DEV_MESS, "");
                qn_str = "1";
                qnt = Integer.parseInt(qn_str);
                vr = vadev.DevQuantValido(factura, qnt, str_dev); // se valida la cantidad ingresada
                if(vr.result){
                    // entra si se pasan las dos validaciones
                    // entonces se agrega el record a la tabla de validaciones
                    ok = true; // se indica que paso ambas validaciones
                    // se busca el producto record, que sirve de insumo para construir  una Devolucion.
                    ProductoRecord pr = this.getRecordByCodigo(str_dev).producto_record;
                    // se agrega el record a la tabla de operaciones
                    this.TV_operaciones.getItems().add
                        (new SptFacturaRecord(new Devolucion(pr, qnt)));
                    
                    this.TV_operaciones.refresh(); // se fuerza la tabla a hacer una actualziacion de UI
                    this.TF_devolucion.clear(); // se limpia el campo de texto para hacer la devolucion
                    this.TF_devolucion.requestFocus(); // metodo para pedir focos del campo de texto de devolucion.
                }
            }
            
            if(!ok){
                super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, vr.getMotivo());
            }
        
        } catch(ClassNotFoundException | SQLException ex){ // se notifica al usuuario en caso de ua exepcion
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        } catch(NumberFormatException ex){  // en caso de que el texto ingresado no sea un numero valido.
            super.ShowInfo(exito, exito, SuppMess.DATA_INT_NO_VALID_MESS); // PENDIENTE POR CORRECTO ACABADO.
        }
    }
    
    /**
     * metodo que retorna el ProductoRecord dentro de un SptFacturaRecord.
     * correspondiente a el producto con el codigo especificado por el argumento 
     * de entrada cod.
     * 
     * en las secciones de codigo donde se usa este metodo se asegura que 
     * que el codigo de porducto pertenece a la factura y por  consiguiente 
     * debe existir en el arrayList, por tanto, nuca deberia retornar un
     *  objeto record nulo.
     * @param cod
     * @return 
     */
    public SptFacturaRecord getRecordByCodigo(String cod){
        SptFacturaRecord fr = new SptFacturaRecord();
        for(SptFacturaRecord fre : this.TV_factura.getItems()){
            if(fre.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                if(fre.spt_codigo.get().equals(cod)){
                    fr = fre;
                }
            }
        }
        return fr;
    }
    
    
    /**
     * se encaga de cargar los valores de los dos combo box para establecer criterio de
     * orden y de busqueda en la tabla de facturas.
     */
    public void Config_All_Cbox(){
        
        // configuracion del combo box de tipo de busqueda.
        ArrayList<String> cblist_tpSearch = new ArrayList<>();
        cblist_tpSearch.add(this.BUSQUEDA_X_CLIENTE_ID);
        cblist_tpSearch.add(this.BUSQUEDA_X_CLIENTE_NAME);
        cblist_tpSearch.add(this.BUSQUEDA_SALDO_MAYOR_Q);
        cblist_tpSearch.add(this.BUSQUEDA_SALDO_MENOR_Q);
        cblist_tpSearch.add(this.BUSQUEDA_LAST_ABONO_MAYOR_Q);
        super.SetComboBox(CB_tipo_busqueda, cblist_tpSearch);
        
        
        // configuracion del comboBox de criterio de orden.
        ArrayList<String> cblist_ordenamiento = new ArrayList<>();
        cblist_ordenamiento.add(this.ORDEN_MAS_DIAS_SIN_ABONAR);
        cblist_ordenamiento.add(this.ORDEN_MENOS_DIAS_SIN_ABONAR);
        super.SetComboBox(CB_orden, cblist_ordenamiento);
        
    }
    
    /**
     * cuando se hace click al boton de abonar.
     * @param me 
     */
    @FXML
    public void onClick_B_abonar(MouseEvent me){
        this.AgregarAbonoRecord();
        // se lanza al usuario una ventana modal para que ingrese el valor
        // en pesos a abonar.
            // si el valor ingresado es valido no excede el valor de la factura
            // se agrega en la tabla de operaciones.
            
            // si el valor ingresado no es valido se notifica al usuario y no se hace mas
    }
    
    
    /**
     * metodo que agrega un Abono record a la tabla de operaciones
     */
    public void AgregarAbonoRecord(){
        
        Va_Abo_Dev vade = new Va_Abo_Dev();
        ValidationResult vr;
        int valor_abono;
        
        try{
            boolean ok = false; // para idicar si paso todas las validaciones
            vr = vade.PreAbonoValido(factura, this.TV_operaciones.getItems());
            if(vr.result){ // si hay factura seleccionada y no hay abono en tabal de operaciones
                String str_abono = super.ShowInput(this.ABONO_INPUT_TITLE, this.ABONO_INPUT_SUB, this.ABONO_INPUT_MESS, "");
                
                valor_abono = Integer.parseInt(str_abono);
                vr = vade.NumAbonoCheck(factura, valor_abono);
                if(vr.result){ // si la cantidad de abono ingresada es valida
                    ok=true;
                    Abono abo = new Abono(0, valor_abono, LocalDate.EPOCH, LocalTime.MIDNIGHT,
                            this.factura.Cliente_id, this.factura.consecutivo, factura.Vendedor_id);
                    this.TV_operaciones.getItems().add(new SptFacturaRecord(abo));
                    this.TV_operaciones.refresh(); // se agrega el abono record a la tabla de operaciones.
                }
            }
            
            if(!ok){ // si no llega al tercer if, validacion final
                super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, vr.motivo);
            }
            
        } catch(SQLException | ClassNotFoundException ex){// se notifica al usurio en caso de excepcion.
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        } catch(NumberFormatException ex){ // para hacer mas robusto en caso de digitar not parsable String
            super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, SuppMess.DATA_FORMAT_PROBLEM_NONUM);
        }
        
    }
    
    
    
    /**
     * cuando se hace click en el boton de ingresar modificaciones a la factura
     * @param me
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    @FXML
    public void onClick_B_hacer_operacion(MouseEvent me) 
            throws SQLException, ClassNotFoundException{
        
        try{ // si la factura ya ha sido cancelada su saldo es 0
            if(this.factura.ReconstruirSaldo()== 0){// en este caso la unica forma de retribuir dinero al comprador es cupon
                this.ModFsinSaldo(); // hace validacion y operacion para el caso cupon.
            }
            else{// en este caso la factura aun no ha sido cancelada por lo que se puede reducir saldo
                this.ModFconSaldo();
            }
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        //para actuaalizar el saldo de la factura modificada si estubiera cargada en la tabla de 
        // busqeda de facturas.
        this.BuscarFacturas_Gcobros();
    }
    
    
    /**
     * cuando se hace click en el boton de hcer operacion y la factura seleccionada aun tiene
     * saldo diferente de cero.
     */
    public void ModFconSaldo(){
        
        try{
            // si el dinero en la tabla de operaciones no supera el saldo de la factura.
            if(this.ValorOpeTab() <= this.factura.ReconstruirSaldo()){
                Hndl_Devoluciones hndd = new Hndl_Devoluciones();
                Hndl_Abonos hndabo = new Hndl_Abonos();
                Hndl_Factura hndfac = new Hndl_Factura();
                Hndl_Productos hndpro = new Hndl_Productos();
                // se ingresa cada uno de los records a la base de datos
                for(SptFacturaRecord fr : TV_operaciones.getItems()){// insert DB devoluciones
                    if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                        hndd.InsertDevolucion(fr.devolucion);
                        // se suma el monto negativo de las prendas que se estan devolviendo.
                        hndfac.Add2Saldo(-fr.devolucion.cantidad*fr.devolucion.pventa, this.factura.consecutivo);
                        // se aumenta nuevamente en stock el numero de prendas devuelto
                        hndpro.Sum2Stock(fr.devolucion.Producto_codigo, fr.devolucion.cantidad);
                    }
                    if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){//insert DB abonos
                        hndabo.IsertarAbono(fr.abono);
                        // se suma negativo el valor del abono a la factura.
                        hndfac.Add2Saldo(-fr.abono.valor, this.factura.consecutivo);
                    }
                } // se limpia la tabla de operaciones 
                this.TV_operaciones.getItems().clear();
                this.TV_operaciones.refresh();
                // se actualiza la tabla records facturas para mostrar el cambio
                this.TV_factura.getItems().clear();
                this.LoadFactura();
                // se notifica al usuario que la operacion ha sido exitosa.
                super.ShowInfo(exito, exito, exito);
                
                // se imprime la vercion actualizada de la factura solo si esta chequeada
                // la opcion de imprimir comprobante.
                if(this.CHB_Print_Comp.isSelected()){
                    LocalPrinter lp = new LocalPrinter();
                    lp.ImprimirFactura(factura);
                }
                
            } else{// se notifica al usuario de la inconsitencia de los datos
                super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, this.NO_OPE_MESS);
            }
        } catch(SQLException | ClassNotFoundException ex){ // se notifica en caso de excepcion
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        } catch (RuntimeException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrintException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * calcula el dinero equivalente en la tabla de operaciones.
     * En la tabla de operaciones solo es posible ingresar objetos tipo abono
     * y devolucion.
     * @return 
     */
    public int ValorOpeTab(){
        int valor = 0;
        for(SptFacturaRecord fr : this.TV_operaciones.getItems()){
            valor += fr.spt_subtotal.get();
        }
        return valor;
    }
    
    /**
     * metodo usado por el controlador para hacer una devolucion de prendas sobre una factura que ya esta 
     * pagada, por tanto la unica forma de devolver el dinero es mediante un cupon.
     *
     */
    public void ModFsinSaldo(){
        // si no hay saldo no puede haber ningun abono en la tabla de operaciones.
        Va_Abo_Dev vade = new Va_Abo_Dev();
        boolean ok = true;
        String razon = ""; // para escribir el motivo de la falla de la operacion
        
        try{
            if(vade.ContieneAbono(this.TV_operaciones.getItems())){
                ok = false; // si hay un record abono en la tabla de operaciones se invalida la operacion.
                razon += this.NO_OPE_CONT_ABONO_MES; // String para ventana modal (explicativo del error)
            }
            
            // si paso la validacion anterior y el valor a devolver es inferior o igual a el ingreso total de  efectivo de esta factura
            if(ok && this.factura.getIngresoEfectivo()>=this.ValorOpeTab()){ 
                // se guarda el cupon en la base de datos.
                Hndl_Cupon hndc = new Hndl_Cupon();
                Cupon cupo = hndc.InsertarCuponDevolucion(this.factura.Cliente_id, this.ValorOpeTab(), 
                        this.factura.consecutivo, this.factura.Vendedor_id);
                
                // se limpia la tabla de operaciones 
                this.TV_operaciones.getItems().clear();
                this.TV_operaciones.refresh();
                
                // se actualiza la tabla records facturas para mostrar el cambio
                this.TV_factura.getItems().clear();
                this.LoadFactura();
                
                super.ShowInfo(exito, "", si_operacion); // ensaje que indica que la operacion se ha realizado bien.
                
                // se imprime el comporobante de devolucion.
                LocalPrinter lp = new LocalPrinter();
                lp.ImprimirCupon(cupo);
                
                /**
                 * se imprime la factura actualizada solo si esta estachequeado
                 * imprimir comprobante.
                 */
                if(this.CHB_Print_Comp.isSelected()){
                    lp.ImprimirFactura(factura);
                }
                
            } else{ // si las prendas a devolver superan el ingreso de efectivo de la factura
                ok = false; // se invalida la operacion
                razon += this.NO_CUPON_QNT_MESS;
            }
            
            // si no se pasa la validacion
            if(!ok){ // se muestra un dialogo modal explicando el motivo
                super.ShowWarning(SuppMess.DATA_PROBLEM_TITLE, SuppMess.DATA_PROBLEM_SUBTITLE, razon);
            }
        } catch(SQLException | ClassNotFoundException ex){ // se notifica al usuario en caso de una excepcion
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            // si todo es correcto se genera el record y se imprime el cupon
            // se limpian las tablas.
            
            // en caso contrario se explican los inconvenientes
        } catch (RuntimeException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PrintException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        /**
         * cuando se hace click al boton de sistesis.
         * se deben juntar los records con sus respectivas devoluciones
     * y mostrarlo cada uno como un record unificado mostrando
     * el numero de unidades restantes.
     * @param me 
     */
    @FXML
    public void onClick_B_sintesis(MouseEvent me){
        // se sintetiiza solo si hay una factura seleccionada
        if(!(factura == null)) Sintetizar();
    }
    
    
    /**
     * toma los producto records y le resta a la cantidad vendida en la factura sus respectivas devoluciones.
     * como resultado, la lista queda sin ningun record tipo devolucion y muestra solo 
     * la cantidad de prendas que quedan pendientes en la factura. En el caso de abonos, se suman y queda
     * un unico record tipo abono con la acumulacion de todos los abonos.
     */
    public void Sintetizar(){
        boolean sel = this.TOGB_s.selectedProperty().get();
        // si el boton de sintesis esta presionado:
        if(sel){ // se actualiza la tabla haciendo sintesis
            
            // lista en la que se guardan los nuevos records ya unificados.
            // se trata de la lista de salida de esta funcion o lo que se busca producir en este metodo
            ObservableList<SptFacturaRecord> fre = FXCollections.observableArrayList(new ArrayList<>());
            
            // array list para sacara una copia de los productos
            ArrayList<SptFacturaRecord> lprods = new ArrayList<>();
            
            int sum_abono = 0;
            SptFacturaRecord fare = new SptFacturaRecord();
            for(SptFacturaRecord sfr : this.TV_factura.getItems()){ // se acumulan todos los valores de los abonos
                if(sfr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                    sum_abono += sfr.abono.valor;
                    fare = sfr; //  ineficiente pero permite obtener una copia que permita
                    // construir facilmente un nuevo abono record que los represente todos                    
                }
                if(sfr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD) || 
                        sfr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                    lprods.add(sfr); // se saca una copia aparte de los producto records y devoluciones.
                    
                }
                
                if(sfr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){ 
                    // si es tipo cupon se agrega sin mas a la nueva lista de factura records
                    
                }
            }
            // si no hay ningun abono record entonces la suma es igual a cero
            if(sum_abono>0){ // en caso de si haber abonos se crea un unico que repsente todos en valor.
                Abono abo = fare.abono;
                abo.valor = sum_abono; // se hace de esta manera para no dejar cabos sueltos ya que hay redundancia
                // de los atributos de abono con el de SptFacturaRecord.
                fare = new SptFacturaRecord(abo); // se dejan todos los datos del abono y 
                //se modifica solo el valor por la suma de todos los abonos.
                fre.add(fare); // se agrega un unico record tipo 
            }
            
            // *************************************
            // se organinzan los producto records ->
            Map<String, List<SptFacturaRecord>> mapa; // se ordenan los ProductoRecords copiados en el ciclo anterior
            mapa = lprods.stream().collect(groupingBy(SptFacturaRecord::getSpt_codigo));
            Set<String> keyset = mapa.keySet(); // los codigos de cada producto
            
            // nueva lista para guardar los ProductoRecords ya uunificados
            ObservableList<SptFacturaRecord> neu_lprods = FXCollections.observableArrayList();
                        
            for(String key: keyset){ // para cada keySet se hace un porceso de unificacion
                List<SptFacturaRecord> laux = mapa.get(key);
                int sum = 0; // auxiliar para guardar acumulacion de cantidades del record y hacer unificacion
                for(SptFacturaRecord fr : laux){
                    // se suman las cantidades de cada producto record
                    // y se restan las cantidades especificadas en cada devolucion.
                    if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)) sum += fr.getSpt_cantidad();
                    if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)) sum -= fr.getSpt_cantidad();
                }
                SptFacturaRecord auxfr = laux.get(0);
                auxfr.setSpt_cantidad(sum); // se construye un nuevo y unico record con la cantidad unificada
                neu_lprods.add(auxfr); // se agrega a la nueva lista.
            } // al final de neu_lprods tiene la lista de producto records unificada con las devoluciones
            fre.addAll(neu_lprods); // se pegan los elementos al final de la lista fre
            TV_factura.setItems(fre); // finalmente se muestra la lista unifcada en el table view.
            TV_factura.refresh(); // refresh para forzar a una actualizacion de la GUI del table view.
        }
        else{ // se atualiza la tabla quitando sintesis.
            this.LoadFactura(); // se carga nuevamente la factura como originalmente se
            // obtiene de la base de datos sin hacer ningun tipo de sinstesis
        }
        // fin del metodo de sintesis.
        
        this.TF_devolucion.requestFocus();
    }
    
    /**
     * cuando se hace click al boton de remover record.
     * elimina records de la tabla de operaciones
     * @param me 
     */
    @FXML
    public void onClick_B_remover(MouseEvent me){
        // solo si hay objeto seleccionado
        SptFacturaRecord fr = this.TV_operaciones.getSelectionModel().getSelectedItem();
        if(!isNull(fr)){
           this.TV_operaciones.getItems().remove(fr);
           this.TV_operaciones.refresh();
        } // sin no hay record seleccionado no se hace nada
        this.TF_devolucion.requestFocus();
    }
    
    
    /**
     * cuando se hace click en el boton de busqueda en el acordeon de gestion de cobros.
     * @param me 
     */
    @FXML
    public void onClick_buscar_Gcobros(MouseEvent me ){
        this.BuscarFacturas_Gcobros();
    }
    
    
    /**
     * cuando se hace enter en el campo de texto de busqueda de facturas
     * ubicado en la seccion de busqueda de facturas
     * @param ae
     */
    @FXML
    public void onAction_TF_SEARCH_buscar_factura(ActionEvent ae){
        this.BuscarFacturas_Gcobros();
    }
    
    
    /**
     * toma el valor de busqueda del campo de texto y el criterio de busqeuda del combo box
     * y realiza la busqueda de facturas para popular el tableView de Facturas.
     */
    public void BuscarFacturas_Gcobros(){
        String tipo_busqueda = this.CB_tipo_busqueda.getSelectionModel().getSelectedItem().toString();
        String busqueda = this.TF_SEARCH_buscar_factura.getText();
        Hndl_Factura hnd_fac  = new Hndl_Factura();
        int valor;
        
        try{
            switch (tipo_busqueda){
                case BUSQUEDA_X_CLIENTE_ID: // busqueda por cc exacto de cliente
                    lsfac_global = hnd_fac.getFacturasByClienteID(busqueda);
                    this.SetTV_GCobro_B_saldo(lsfac_global);
                    break;
                case BUSQUEDA_X_CLIENTE_NAME:// busqueda por match de palabras clave para el nombre
                    
                    
                    break;
                case BUSQUEDA_SALDO_MENOR_Q:// los records con saldos menores al especificado e el textfield
                    try{ // si el usuario digita un String que no represente un numero entero entonces se
                        // establece valor en -1 para indicar al metodo que trae las facturas, que no debe hacer un query
                        // sino que debe retornar una lista vacia.
                        valor = Integer.parseInt(busqueda);
                    } catch(NumberFormatException ex){
                        valor = -1;
                    }
                    
                    if(valor<0) valor=-1; // para cualquier valor negativo se pone valor en -1
                    
                    lsfac_global = hnd_fac.getFacturas_Saldo_MenorQ(valor);
                    this.SetTV_GCobro_B_saldo(lsfac_global);
                    break;
                case BUSQUEDA_LAST_ABONO_MAYOR_Q:// los records con un tiempo de sin abonar mayor al
                    // al especificado en el campo de texto.
                    break;
                    
                    /**
                     * busqueda de facturas tales que su saldo sea mayor que el valor respresentado por el String 
                     * busqueda.
                     */
                case BUSQUEDA_SALDO_MAYOR_Q:// los records con los saldos mayores al especificado en el campo de texto.
                    try{ // si el usuario digita un String que no represente un numero entero entonces se
                        // establece valor en -1 para indicar al metodo que trae las facturas, que no debe hacer un query
                        // sino que debe retornar una lista vacia.
                        valor = Integer.parseInt(busqueda);
                    } catch(NumberFormatException ex){
                        valor = -1;
                    }
                    
                    if(valor<0) valor=-1; // para cualquier valor negativo se pone valor en -1
                    
                    lsfac_global = hnd_fac.getFacturas_Saldo_MayorQ(valor);
                    this.SetTV_GCobro_B_saldo(lsfac_global);
                    break;
            }
        } catch(SQLException | ClassNotFoundException ex){
            // notificar a l usuario en caso de un error por acceso a base de datos.
        }
    }
    
    
    
    /**
     * Click en el boton de sistesis. 
     * @param me 
     */
    @FXML
    public void onClick_sintesis(MouseEvent me){
        
    }
    
    
    /** (ACORDEON DEVOLUCIONES)
     * Cuando se presiona enter en el textField para buscar factura en la
     * seccion de devoluciones.
     * @param ae 
     */
    @FXML
    public void onAction_TF_buscar_factura(ActionEvent ae){
        this.LoadFactura();
    }
    
    
    /**
     * cunado se hace busqueda de la factura y se cargan sus datos en
     * el textArea. seccion de abonos y devoluciones.
     */
    public void LoadFactura(){
        try{
            Hndl_Factura hndf = new Hndl_Factura();
            // se toma el dato del campo de texto
            int idfac = Integer.parseInt(this.TF_buscar_factura.getText());
            ArrayList<Factura> lf = hndf.getFacturaBy_ID(idfac); // hace busqueda en la BD
            
            if(!lf.isEmpty()){ // en caso de que si exista dicha factura...
                this.TF_buscar_factura.setEditable(false); // se bloquea el cambio de factura
                
                this.TA_info_factura.setText(lf.get(0).representacion_str());
                this.factura = lf.get(0); // se guarda una instanncia de la factura seleccionada
                
                ArrayList<SptFacturaRecord> lfrs = new ArrayList<>();
                
                // se establecen los records actuales de la factura en 
                this.TV_factura.setItems(FXCollections.observableList(this.factura.getFacturaRecords()));
                
                this.TOGB_s.setDisable(false); // se establece el toggle button.
                this.TOGB_s.setSelected(false); // en un estaod conveniente.
                
            } else{ // se notifica al usuarion si no existe este conecutivo.
                this.TA_info_factura.setText(this.NOT_FOUND_FACT);
                this.factura = new Factura();
            }
            
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            ClearDev(); // se hace reset para tender a una mayor integridad de la informacion.
        } catch(NumberFormatException ex){ // ocurre cuando el usario ingresa
            // un String que no es numero en el TextField.
            // en este caso simplemente no s hace nada cuando ocurra la excepcion
            this.factura = new Factura();
        }
    }
    

    /**
     * metodo que debe implementar clase hija para permitir a 
     * clase padre implementar una mejor vercion del los dialogos 
     * modales mediante la funcion initOwner y evitar perdida de focus de la
     * aplicacion al lanzar el dialogo modal.
     * @return 
     */
    @Override
    public Stage GetStage() {
        return (Stage) this.CB_orden.getScene().getWindow();
    }
    
    
    /**
     * cuando se hace click al bonton de cancelar en la seccion de 
     * abonos y devoluciones
     * @param me 
     */
    public void onClick_B_Cancelar(MouseEvent me){
        this.ClearDev();
    }
    
    /**
     * metodo que limpia los campos de texto tabla etc para 
     * cancelar los datos introducidos en la devolucion
     */
    public void ClearDev(){
        this.factura = new Factura(); // se limpia la factura seleccionada
        this.TA_info_factura.clear();
        this.TF_buscar_factura.clear();
        this.TF_devolucion.clear();
        this.TV_factura.getItems().clear();
        this.TV_operaciones.getItems().clear();
        // re-habilita la seleccion de facturas.
        this.TF_buscar_factura.setEditable(true); 
        this.TOGB_s.setDisable(true);
        this.TOGB_s.setSelected(false);
        
        // se hace focus al tf de set factura seleccionada.
        this.TF_buscar_factura.requestFocus();
    }
    
    
    /**
     * metodo que confura los 3 table views de esta seccion de abonos y devoluciones.
     */
    public void ConfigureAllTableViews(){
        
        //***** se configura el table view para mostrar los records actuales de la factura
        // Strings
        this.TC_FAC_codigo.setCellValueFactory(new PropertyValueFactory<>("spt_codigo"));
        this.TC_FAC_descripcion.setCellValueFactory(new PropertyValueFactory<>("spt_descripcion"));
        this.TC_FAC_fecha.setCellValueFactory(new PropertyValueFactory<>("spt_fecha"));
        
        // Integers
        this.TC_FAC_pventa.setCellValueFactory(new PropertyValueFactory<>("spt_precio_venta"));
        this.TC_FAC_cantidad.setCellValueFactory(new PropertyValueFactory<>("spt_cantidad"));
        this.TC_FAC_subtotal.setCellValueFactory(new PropertyValueFactory<>("spt_subtotal"));
        
        this.TV_factura.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        
        //***** se configura el TableView de las devoluciones y abonos
        this.TC_OPE_codigo.setCellValueFactory(new PropertyValueFactory<>("spt_codigo"));
        this.TC_OPE_descripcion.setCellValueFactory(new PropertyValueFactory<>("spt_descripcion"));
        
        // Integers
        this.TC_OPE_pventa.setCellValueFactory(new PropertyValueFactory<>("spt_precio_venta"));
        this.TC_OPE_cantidad.setCellValueFactory(new PropertyValueFactory<>("spt_cantidad"));
        this.TC_OPE_subtotal.setCellValueFactory(new PropertyValueFactory<>("spt_subtotal"));
        
        this.TV_operaciones.setItems(FXCollections.observableArrayList(new ArrayList<>()));
        
        //*** se configura el table view de busqueda de facturas.
        this.TC_cli_id_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_Cliente_id")); // identificacion del cliente
        this.TC_cli_name_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_Cliente_nombre")); // nombre del cliente
        this.TC_femision_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_fecha_registro")); // fecha en la que se genero la factura
        this.TC_ult_abono_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_fecha_last_abono")); // fecha del ultimo pago.
        
        this.TC_consec_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_consecutivo")); // consecutivo de la factura
        this.TC_saldo_facsearch.setCellValueFactory(new PropertyValueFactory<>("spt_saldo")); // saldo de la factura.
        
    }
    
    
    /**
     * cuando se hace click en el tableView de busqueda de facturas.
     * Se implementa deteccion de doble click
     * @param me 
     */
    @FXML
    public void onClick_TV_busqueda(MouseEvent me){
        if(me.getClickCount()==2){ // si se hace dobleClick
            this.OpenFactuRecordsWindow(me); // se abre una ventana modal
            // para mostrar los records de la factura seleeccionada.
        }
    }
    
    
    /**
     * Si hay una factura seleccionada entonces se abre una ventana modal que muestra todos los records de la
     * factura seleccionada y la informacion del cliente asociado a dicha factura.
     */
    public void OpenFactuRecordsWindow(MouseEvent event){
        // se caraga el path del FXML
        
        SptFactura sel_factu = TV_busqueda.getSelectionModel().getSelectedItem();
        
        if(!(sel_factu==null)){ // en caso de que la factura seleccionada on sea nula
            try{
            FXMLLoader cargador = new FXMLLoader(getClass().getClassLoader().
                        getResource("tentactildesktoppos/FXMLPopUps/FXMLSeeFactuSintesis.fxml"));
                Parent root = cargador.load(); // se usa el fxml para cargar tanto la gui como el controlador del dialogo de
                Stage st = new Stage();// modificacion
                st.setScene(new Scene(root));
                st.initModality(Modality.WINDOW_MODAL); // se fuerza el focus a al dialogo de modificacion
                st.initOwner( ((Node) event.getSource()).getScene().getWindow());
                FXMLSeeFactuSintesisController c = cargador.<FXMLSeeFactuSintesisController>getController(); // se obtiene el controlador
                
                Hndl_Clientes hndcli = new Hndl_Clientes(); // conector a la tabla de clientes 
                
                // si la factura existe, por integridad relacional el cliente existe tambien.
                Cliente sel_cli = hndcli.buscarCliente_id(sel_factu.factura.Cliente_id).get(0);
                
                c.complement_Init(sel_factu, sel_cli);// se inicializan los valores de la ventana
                st.show(); // se muestra la ventana
            } catch(SQLException | ClassNotFoundException | IOException ex){
                super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
            }
                
        } else{ // si no hay factura seleccioanada no se hace nada.
            
        }
    }
    
    
    
    /**
     * cuando se hace click en el boton de borrar
     * @param me 
     */
    @FXML
    public void onClick_B_borrar_gencob(MouseEvent me){
        this.TF_SEARCH_buscar_factura.clear();
        this.TF_SEARCH_buscar_factura.requestFocus();
    }
    
    
    /**
     * Revisa si esta activada o no la opcion de mostrar facturas sin saldo. De acuerdo a esto establece los
     * records del table view de facturas excluyendo o no, segun sea el caso, aquellos records con saldo 0.
     * @param cp_ls_fac
     */
    public void SetTV_GCobro_B_saldo(ObservableList<SptFactura> cp_ls_fac){
        
        ObservableList<SptFactura> filtered = FXCollections.observableArrayList();
        
        if(!ChkBox_con_saldo.isSelected()){ // si el check Box no esta seleccionado se remueven todos los
            for(int i=0; i<cp_ls_fac.size(); i++){ // records con saldo 0.
                if(!(cp_ls_fac.get(i).spt_saldo.intValue()==0)){
                    filtered.add(cp_ls_fac.get(i));
                }
            }
            cp_ls_fac = filtered;
        }
        TV_busqueda.setItems(cp_ls_fac);
        TV_busqueda.refresh();
    }
    
    
     /**
     * metodo que configura click derecho en la tabla de facturas.
     * La unica opcion disponible por ahora es la imprimir una copia de la factura seleccionada.
     */
    public void ConfigurarMenuItem_rgClick(){
        
        /**
         * se configura el menu ite de la tabla de busqueda de facturas
         */
        rg_click_menu_TV_buscar_factu = new ContextMenu();
        
        MenuItem menu_Print_copia = new MenuItem(IMPRIMIR_COPIA_FACTURA);
        
        
        MenuItem menu_Send_Factura = new MenuItem(SEND_FACTURA);
        
        /**
         * cuando se selecciona impresion de stickers con el menu item (rg click)
         */
        menu_Print_copia.setOnAction(new EventHandler<ActionEvent>() {
            @Override // menu item que imprime ua copia de la factura seleccionada
            public void handle(ActionEvent event) {
                Action_menu_print_copia_factura();
            }
        });
        
        menu_Send_Factura.setOnAction(new EventHandler<ActionEvent>() {
            @Override // menu item que manda factura seleccionada a seccion de abonos y devoluciones
            public void handle(ActionEvent event) {
                Action_menu_send_factura();
            }
        });
        
        // se agregan los items al menu item.
        rg_click_menu_TV_buscar_factu.getItems().add(menu_Print_copia);
        rg_click_menu_TV_buscar_factu.getItems().add(menu_Send_Factura);
        
        this.TV_busqueda.setContextMenu(rg_click_menu_TV_buscar_factu);
        
        
        /**
         * se configura el menu item de la tabla e facturas de la seccio de abonos y devoluciones.
         * 
         * 
         */
        
        rg_click_menu_TV_factu_abon_dev = new ContextMenu();
        
        MenuItem menu_copiarCodigo = new MenuItem(COPIAR_CODIGO);
        
        /**
         * cuando se selecciona copiar codigo del producto seleccioando
         */
        menu_copiarCodigo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Action_menu_CopyCode();
            }
        });
        
        //rg_click_menu.getItems().add(menu_PrintSticker);
        rg_click_menu_TV_factu_abon_dev.getItems().add(menu_copiarCodigo);
        this.TV_factura.setContextMenu(rg_click_menu_TV_factu_abon_dev);
    }
    
    
    /**
     * imprime una copia de la factura seleccionada.
     */
    public void Action_menu_print_copia_factura(){
        SptFactura sel_fac = TV_busqueda.getSelectionModel().getSelectedItem();
        if(!isNull(sel_fac)){ // en caso de que lña factura no sea nula se realiza la impresion
            try{
            LocalPrinter lp = new LocalPrinter();
            lp.ImprimirFactura(sel_fac.factura);
            } catch(SQLException | ClassNotFoundException ex){
                
            } catch (RuntimeException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrintException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    /**
     * carga la factura seleccionada en la seccion de abonos y devoluciones.
     */
    public void Action_menu_send_factura(){
        SptFactura sel_fac = TV_busqueda.getSelectionModel().getSelectedItem();
        if(!isNull(sel_fac)){ // en caso de que lña factura no sea nula se realiza la operacion.
            this.TF_buscar_factura.setText(Integer.toString(sel_fac.spt_consecutivo.intValue()));
            this.LoadFactura();
            this.AccordionAbonDev.setExpandedPane(TLPane_AbonDev);
            this.TF_devolucion.requestFocus();
        }
    }
    
    
    /**
     * se invoca cuando se hace click en el menu item de copiar codigo.
     * copia el codigo del record tipo devolucion.
     */
    public void Action_menu_CopyCode(){
        SptFacturaRecord fr = TV_factura.getSelectionModel().getSelectedItem();
        if(fr == null){ // si no hay item seleccionado entonces no hace nada
            
        } else{ // en caso de haber item seleccionado entonces se procede
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){ // solo si el record es tipo devolucion o producto record
                super.Copiar2ClipBoard(fr.devolucion.Producto_codigo); // entonces se copia el 
                // codigo del record seleccionado al portapapeles
            }
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                super.Copiar2ClipBoard(fr.producto_record.Producto_codigo);
            }
            
        }
    }
    
    
    /** cuando se hace click en el titled pane (en el acordeon) de la seccion de abonos
     * y devoluciones.
     * 
     * ene ste metodo se mejora el focus
     * 
     * @param me 
     */
    @FXML
    public void onClickTLPane_AbonDev(MouseEvent me){
        if(this.factura == null){ // si no hay factura seleccionda entonces se hace focus en el campo de texto que
            // permite establecer la factura seleccionada
            this.TF_buscar_factura.requestFocus();
        } else{ // si ya hay factura seleccionada entonces se hace focus al tf de busqueda de producto para hacer 
            // la devolucion.
            this.TF_devolucion.requestFocus();
        }
    }
    
    
    /**
     * metodo que agrega al table view de borrador un record tipo producto
     * record
     * @return 
     */
    public ObservableList<SptFacturaRecord> AddProductoRecord(){
        ObservableList nList = FXCollections.observableArrayList();
        try{
            // se obtiene la lisa sintetizada.
            ObservableList SynthFac = this.factura.getFrecordsSIntetizados();
            
            ObservableList curlist = this.TV_operaciones.getItems();
            
            String cod = this.TF_devolucion.getText();
            Hndl_Productos hndp = new Hndl_Productos();
            ArrayList<Producto> lpr = hndp.buscarProducto_codigo(cod);
            boolean ok = true;
            if(lpr.isEmpty()){
                super.ShowWarning("Error", "", "El codigo ingresado no correponde a un producto"
                        + "\n de la base de datos");
                ok = false;
            }
            if(ok){ // en caso de que si exista el producto ingresado
                // se construye el objeto tipo devolucion.
                ProductoRecord pr = this.getRecordByCodigo(cod).producto_record;
                Devolucion dev = new Devolucion(pr, 1); // por defecto se asume que 
                //que cada lectura corresponde solo a un item.
                
            }
        } catch(SQLException | ClassNotFoundException ex){
            super.ShowError(SuppMess.EXCEPTION, SuppMess.CALL_SUPPORT, SuppMess.INFORM_THIS + ex.getMessage());
        }
        
        return nList;
    }
    
    
    
    /**
     * Recibe un String con el codigo del producto que se desea obtener.
     * este metodo solo retornara un Producto record o una devolucion
     * record si en la lista establecida en el argumento de esta funcion 
     * existe un elemento con dicho codigo. Si el codigo no coincide con
     * ninguno de los elementos de la list entonces se retorna  una lista 
     * vacia. Por esto es quuee se retorna una lista en lugar de
     * un SptFacturaRecord, para poder usar el metodo isEmpty para revisar
     * si existe el producto, en lugar de usar  un check con un objeto
     * nulo y d estaa forma minimizar la posibilida de ocurrencia de 
     * un NullPointerException.
     * 
     * Es importante tener en cuenta que este metodo solo se puede emplear
     * con listas unificadas.
     * @param lista
     * @return 
     */
    public ObservableList<SptFacturaRecord> getFRecord(ObservableList<SptFacturaRecord> lista, String cod){
        
        ObservableList<SptFacturaRecord> r = FXCollections.observableArrayList();
        
        // se barre la lista entregada
        for(SptFacturaRecord fr : lista){
            // si es tipo prodcuto record como en el caso de la tabla de 
            // factura
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                if(fr.spt_codigo.get().equals(cod)) r.add(fr);
            }
            // si es tipo devolucion como es el caso de la tabla de operaciones
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                if(fr.spt_codigo.get().equals(cod)) r.add(fr);
            }
        }
        return r;
    }
    
    
    /**
     * metodo que retorna el numero de items pendientes del producto indicado
     * en la factura menos el numero de items que se desea devolver.
     * si el numero resultante es mayor o igual a uno entonces se puede 
     * agregar o acumular la unidad segun sea el caso 
     * de no ser asi entonces no se puede acumular la unidad con el codigo 
     * especificado.
     * @param cod
     * @return 
     */
    public int getQntPr(String cod) 
            throws ClassNotFoundException, SQLException{
       int nf;
       int no;
       // se obtiene el numero de records del codigo seleccionado en la factura
       // pero sintetizado para estar seguro que cada codigo ocurrira solo una vez.
       ObservableList<SptFacturaRecord> rtf = this.getFRecord(factura.getFrecordsSIntetizados(), cod);
       if(rtf.isEmpty()) nf=0;
       else nf = rtf.get(0).spt_cantidad.intValue();
       
       // se obtiene el numero de items coincidente con el codigo indicado
       //  en la tabla de operaciones. se parte de la base que solo habra maximo
       // un unico record con el codigo indicado ya que para agregar
       // solo se hara acumulacion.
       ObservableList<SptFacturaRecord> rto = this.getFRecord(TV_operaciones.getItems(), cod);
       if(rto.isEmpty()) no=0;
       else no = rto.get(0).spt_cantidad.intValue();
       
       return nf-no;
    }
    
    
    /**
     * Este metodo solo se puede emplear si se han hecho las correspondientes validaciones.
     * este agrega una nueva fila si en la tabla de operaciones no existe un record con este codigo.
     * En caso de existir entonces se agrega una unidad (+1).
     * @param cod 
     */
    public void AddPR(String cod){
        
        // Se busca una coincidencia del codigo en la tabla de operaciones.
        ObservableList<SptFacturaRecord> r = this.getFRecord(this.TV_operaciones.getItems(), cod);
        
        if(r.isEmpty()){ // si no existe en la tabla de operaciones entonces se hace add a los items de la tabla
            SptFacturaRecord frpr = this.getRecordByCodigo(cod);
            
            try {
                TV_operaciones.getItems().add(new SptFacturaRecord(new Devolucion(frpr.producto_record, 1)));
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else{ // en caso de que si haya coincidencia entonces se busca el index y se suma +1 a la cantidad.
            for(int i=0; i<this.TV_operaciones.getItems().size(); i++){
                // En el indice que se encuentra la coincidencia se hace un +1
                if(TV_operaciones.getItems().get(i).getSptCodigo().get().equals(cod)){
                    // las unicas coincidencias de la tabla de operaciones son devolucion records
                    TV_operaciones.getItems().get(i).OnePlusDevolucion();
                }
            }
        }
        // se actualizan los cambios en GUI.
        TV_operaciones.refresh();
        TF_devolucion.clear();
    }
    
    
    /**
     * cuando se presiona una tecla y la tabla de operaciones tiene focus.
     * 
     * las operacines permitidas con + para sumar +1 solo a records tipo devolucion
     * 
     * - para restar solo a records tipo devolucion 
     * 
     * suprimir para remover cualquier tipo de record.
     * @param ke 
     */
    @FXML
    public void onKeyPressTVOperaciones(KeyEvent ke){
        // se lee la tecla precinada
        KeyCode key = ke.getCode();
        
        boolean selection = true;
        
        // si no hay objeto seleccionado en la tabla de operaciones entonces no se hace
        // nada sin importar la tecla oprimida.
        if(TV_operaciones.getSelectionModel().getSelectedItem() == null) selection = false;
        
        //si se preciona tecla +
        // se debe de agregar +1 a la devolucion seleccionada
        if(key.equals(KeyCode.PLUS) && selection &&
                this.TV_operaciones.getSelectionModel().getSelectedItem().behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
            try {
                int ne = this.getQntPr(TV_operaciones.getSelectionModel().getSelectedItem().spt_codigo.get());
                if(ne<1){ // si no hay consistencia de las cantidades entonces no se hace +1
                    super.ShowError("Error", "", "No puede exeder el numero de prendas pendientes en la factura.");
                } else{ // en caso de que si haya consistencia numerica entonces se garega 1 mas.
                    this.TV_operaciones.getSelectionModel().getSelectedItem().OnePlusDevolucion();
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(FXMLAbon_DevController.class.getName()).log(Level.SEVERE, null, ex);
                
            }
        }
        
        /**
         * si se preciona tecla suprimir
         * se debe remover el record seleccionado sin importar si es tipo abono o devolucion.
         */
        if(key.equals(KeyCode.DELETE) && selection){
            SptFacturaRecord spt_fr = this.TV_operaciones.getSelectionModel().getSelectedItem();
            this.TV_operaciones.getItems().remove(spt_fr);
        }
        
        
        //si se preciona tecla -
        // se debe restar -1 a la devolucion seleccionada
        // llega a 0 entonces se debe remover el item
        if(key.equals(KeyCode.MINUS) && selection &&
                this.TV_operaciones.getSelectionModel().getSelectedItem().behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
            SptFacturaRecord spt_fr = this.TV_operaciones.getSelectionModel().getSelectedItem();
            if(spt_fr.spt_cantidad.intValue() == 1){ // si solo queda una prenda del item seleccionado
                // un menos lo pondra en 0, es decir que debera ser eliminado
                this.TV_operaciones.getItems().remove(spt_fr);
            } else{
                this.TV_operaciones.getSelectionModel().getSelectedItem().OneMinusDevolucion();
            }
        }
        TV_operaciones.refresh();
    }
    
}
