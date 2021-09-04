package Impresion;


import BD_tdpos_r.Hndl_Configuracion;
import BD_tdpos_r.SQL_Configuracion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;


import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import org.apache.commons.lang3.StringUtils;

/**
 * de la libreria barcode4j. Hasta ahora ha funcionado mejor que barbecue.
 */
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code128.Code128Constants;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import tentactildesktoppos.negocio_r_objs.Cliente;
import tentactildesktoppos.negocio_r_objs.Cupon;
import tentactildesktoppos.negocio_r_objs.Factura;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFactura;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptFacturaRecord;


/**
 *
 * Encapsula todos los atributos y metodos necesarios para hacer impresion de adhesivos o de facturas.
 * @author esteban
 */
public class LocalPrinter {
    
    /**
     * titulo para ventana modal si ocurre una exepcion relacionada con la impresion.
     */
    public static final String PROBLEM_TITLE = "Ha Ocurrido Un Problema";
    
    /**
     * titulo para ventana modal si ocurre una exepcion relacionada con la impresion.
     */
    public static final String PROBLEM_SUB = "";
    
    /**
     * Mensaje de error si ocurre PrinteException. (error durante la impresion).
     */
    public static final String PROBLEM_PRINTER_MESS = "Ha ocurrido un problema durante la impresion. \n"
            + " Haga profavor una inspeccion fisica de la impresora. Ejemplo: Revisar que tenga \n"
            + " suficiente papel";
    
    
    /**
     * Mensaje de error si ocurre RunTime Exeption. (Cuando no se encuentra un Printer Service que concuerde
     * con el nombre de la impresora guardado en la tabla de configracion
     */
    public static final String PROBLEM_FIND_PRINT_SERVICE = "No se ha podido realizar la impresion, \n"
            + " rectifique porfavor el nombre de la impresora establecido en la seccion de configuracion.";
    
    /**
     * ruta del archivo png en el que se guarda temporalmente el codigo de barras
     */
    private final String FILE_PATH_STIKER = System.getProperty("user.home") + "/sticker_salida.png";
    
    
    /**
     * se inicializa en constructor.
     * Es el numero maximo de caracteres que vabe en una linea.
     */
    public final int MAX_CHARS_PER_LINE;
    
    /**
     * constante de espacio en balnco para reducir posibilidad de errores ya que se usa multiples veses
     * en la formacion de Strings
     */
    public final String W_SPACE = " ";
    
    /**
     * Caracteres de escape para hacer corte parcial.
     */
    public final byte[] CUT = {(byte) 0x1B, (byte)0x64, (byte) 5, (byte) 0x1B, (byte)0x69};
    
    /**
     * Caracteres de escape para hacer un reset del buffer de la impresora.
     */
    public final byte[] RESET = {(byte) 0x1B, (byte) 0x40};
    
    /**
     * caracteres de escape para hacer feed 5 lineas, corte, y reset del buffer.
     */
    public final byte ENDING_BYTES[] = {(byte) 0x1B, (byte)0x64, (byte) 5, (byte) 0x1B, (byte)0x69, (byte) 0x1B, (byte) 0x40};
    
    
    /**
     * constructor.
     * se inicializa el atributo que indica el numero maximo de caracteres por linea.
     */
    public LocalPrinter() 
            throws ClassNotFoundException, SQLException{
        Hndl_Configuracion hndconf = new Hndl_Configuracion();
        String max = hndconf.getConfiguracion(SQL_Configuracion.PRT_FACT_MAX_LINES);
        this.MAX_CHARS_PER_LINE = Integer.parseInt(max);
    }
    
    
    /**
     * metodo que realiza la impresion de caracteres con el fin de probar la configuracion de la 
     * impresora y tabien para poder indentificar el numero maximo de caracteres por linea..
     */
    public void PRT_Factura_Test()
            throws ClassNotFoundException, SQLException, RuntimeException, PrintException, IOException{
        String test_text = "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789\n"
                + "abcdefghijklmnñopqrstuvwxyz\n"
                + "ª!·$%&/()=?¿[]{};:_,.-/*+ \n"
                + "Impresion de prueva.\n"
                + "para soporte tecnico visitar:\n"
                + "www.tentactil.com\n ";  // se genera el String de prueba.
        
         SendBytes2ReceiptPrinter(appendBytes(test_text.getBytes(), CUT));
    }
    
    
    
    /**
     * 
     * Recibe un objeto tipo factura e imprime todos sus records
     * 
     * 
     * @param fac 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     * @throws javax.print.PrintException 
     */
    public void ImprimirFactura(Factura fac) 
            throws ClassNotFoundException, SQLException, RuntimeException, PrintException, IOException{
        
        String r=""; // String que se construira para que represente todos los datos de una factura.
        
        r+= ".\n.\n.\n.\n.\n.\n";
        
        
        r+= "   _ - _   _     _   _ - _\n" +
                "  _     _   _   _   _     _\n" +
                "  _     _    _ _    _     _\n" +
                "  _     _     _     _     _\n" +
                "  _     _    _ _    _     _ \n" +
                "   _   _    _   _    _   _\n" +
                "     -     _     _     -\n \n";
        
        
        // CABECERA datos negocio
        r += "OXOMODA MEDELLIN\n";
        r += "C. C. Metro Hueco. Cra 53 # 46-31\nLocal 218 \n Medellin Colombia.";
        r += "tel: 513 25 06\n";
        // datos factura
        r += "Doc ID: " + Integer.toString(fac.consecutivo) + "\n";
        r += "fecha gen. : " + fac.fecha.toString() + "-" + fac.hora.toString() + "\n";
        r += "pago : " + fac.forma_pago + "\n";
        // datos cliente
        Cliente cli =fac.getCliente();
        r += "Cliente: " + cli.nombre+"\n";
        r += "id: " + fac.Cliente_id + "\n";
        
        // RECORDS FACTURA
        r+= this.addStrCols("Unit.", "Cant.", "SubT.");
        
        int suma_pr = 0; // valor total de los producto records.
        
        // conector a la tabla de configuracion para
        // determinar el tipo de impresion que se debe usar (tiop historia o sisnteis).
        Hndl_Configuracion hndconf = new Hndl_Configuracion();
        String tipoPrint = hndconf.getConfiguracion(SQL_Configuracion.PRT_PRINT_TYPE);
        
        ArrayList<SptFacturaRecord> fr_list;
        
        if(tipoPrint.equals(SQL_Configuracion.PRINT_OPTION_HISTORIA)){
            fr_list = fac.getFacturaRecords();
        } else{ // se usa else ya que solo hay dos tipos de impresion, entonces es mas robusto
            //FXCollections.o
            
            // se hace un cast de observable list a arraylist. se trata de algo tecnicamente innecesario
            // pero que se realiza para no cambiar la estructura de los metodos.
            List<SptFacturaRecord> tmpls = fac.getFrecordsSIntetizados();
            fr_list = new ArrayList<>(tmpls);
        }
        
        
        for(SptFacturaRecord fr : fr_list){
            if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
                r += this.addFRstr(fr, fac); // solo si es un producto record se imprime en esta seccion.
                suma_pr += fr.producto_record.cantidad*fr.producto_record.pventa;
            }
        }
        // se concatena el total de la venta
        r += this.addStrCols("Total :", "--->", Integer.toString(suma_pr));
        r += "\n-\n";
        
        
        // se colocan la devoluciones si las hay
        for(SptFacturaRecord fr : fr_list){
            if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
                r += this.addFRstr(fr, fac); // solo si es un abono record se imprime en esta seccion.
            }
        }
        r += "\n-\n";
        
        
        // se colocan los cupones gastados y generados si los hay.
        for(SptFacturaRecord fr : fr_list){
            if(fr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
                r += this.addFRstr(fr, fac); // solo si es un abono record se imprime en esta seccion.
            }
        }
        
        r += "\n-\n";
        int suma_abo = 0; // variable para acumular el total de dinero abonado
        // se colocan los abonos si los hay
        for(SptFacturaRecord fr : fr_list){
            if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
                r += this.addFRstr(fr, fac); // solo si es un abono record se imprime en esta seccion.
                suma_abo += fr.abono.valor; // se suma el valor de cada abono.
            }
        }
        
        if(suma_abo > 0) {// se anexa el total abonado en caso que sea diferente de 0
            r+= this.addStrCols("Abonado", "--->", Integer.toString(suma_abo));
        }
        
        r+= " Saldo Factura --> " + Integer.toString(fac.ReconstruirSaldo()) + "\n\n";
        
        SendBytes2ReceiptPrinter(appendBytes(r.getBytes(), CUT));
    }
    
    
    /**
     * realiza N copias del codigo especificado.primero se usa la libreria barcode4j para generar un archivo png 
     * del codigo de barras.Luego se usa la API de java (javax.print y afines) para hacer la impresion
     * del archivo png.Cada que se invoque el metodo se sobreescribe lel fichero png por lo que no es necesario
     * eliminarlo al final de la operacion.
     * @param codigo
     * @param copias 
     * @throws java.io.IOException : al generar el png con el ean128 
     * @throws java.lang.ClassNotFoundException : Buscando el nombre de la impresora en la tabla de configuracion
     * @throws java.sql.SQLException : Buscando el nombre de la impresora en la tabla de configuracion
     * @throws javax.print.PrintException  : haciendo la impresion.
     * @throws RuntimeException : cuando no puede encontrar el print service.
     */
    public void ImprimirStickers(String codigo, int copias) 
            throws IOException, ClassNotFoundException, SQLException, PrintException, RuntimeException{
        this.GenerarEan128PNG(codigo, this.FILE_PATH_STIKER); // se genera el png
        Hndl_Configuracion hndconf = new Hndl_Configuracion();
        String PrinterName = hndconf.getConfiguracion(SQL_Configuracion.PRT_IMPR_STICKER);
        this.HacerPrintJobSticker(this.FILE_PATH_STIKER, copias, this.GetPrinterService(PrinterName));
    }
    
    /**
     * Metodo que recibe un String arbitrario y los imprime con la impresora de facturas.
     * @param fullContent
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws RuntimeException : cuando no se encuentra el PrintService identificado con el String
     * almacenado en la tabla de configuracion.
     * @throws javax.print.PrintException
     */
    public void SendBytes2ReceiptPrinter(byte[] fullContent)
            throws ClassNotFoundException, SQLException, RuntimeException, PrintException, IOException{
        
        // se obtiene una instancia en java de la impresora de facturas
        Hndl_Configuracion hndconf = new Hndl_Configuracion();
        PrintService printer = this.GetPrinterService(hndconf.getConfiguracion(SQL_Configuracion.PRT_IMPR_FACTURA));
        
        DocPrintJob dpj = printer.createPrintJob();
        
        // se crea un tipo de DocFlavor soportado por la impresora (esta relacionado con el tipo de documento)
        //DocFlavor docfav = DocFlavor.INPUT_STREAM.POSTSCRIPT;
        // cuando se paso a windows, postscript flavor no funciona, toco
        //cambiar a autosense.
        DocFlavor docfav = DocFlavor.INPUT_STREAM.AUTOSENSE;
        
        // se pasa a Bytes el texto que se desea imprimir.
        InputStream is = new ByteArrayInputStream(fullContent);
        
        // se construye una instancia del documento que se desea imprimir, con los bytes y el tipo de 
        // documento
        Doc docu = new SimpleDoc(is, docfav, null);    
        // finalmente se realiza la impresion. aqui se puede generar una PrintException.
        dpj.print(docu, null);
        
        is.close();
    }
    
    
    /**
     * realiza la impresion del sticker en png en la ruta path, con el print service ps
     * @param path
     * @param copias
     * @param ps
     * @throws PrintException
     * @throws IOException
     * @throws RuntimeException      
     */
    private void HacerPrintJobSticker(String path, int copias, PrintService ps) 
            throws PrintException, IOException{
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(copias));
        
                // Lookup custom paper by name
        MediaSizeName paper = null;
        for (Media m : (Media[])ps.getSupportedAttributeValues(Media.class, null, null)){
            if (m instanceof MediaSizeName){
                MediaSizeName media = ((MediaSizeName)m);
                System.out.println(m.toString());
                //if ("Tama�o definido por el usuario".equals(media.toString())) paper = media;
                if ("Etiqueta para lomo archivador 5cm".equals(media.toString())) paper = media;
                //if ("62mm x 100mm".equals(media.toString())) paper = media;
            }
        }
        System.out.println("****");
        System.out.println(paper.toString());
        //pras.add(paper);
        //pras.add(new MediaSize());
        
        System.out.println("Printing to " + ps);
        DocPrintJob job = ps.createPrintJob();
        FileInputStream fin = new FileInputStream(path);
        Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
        
        job.print(doc, pras);
        fin.close();
    }
        
    
    /**
     * genera un archivo png con el codigo especificado por 'codigo' en la ruta path
     * @param codigo
     * @param path 
     */
    private void GenerarEan128PNG(String codigo, String path) 
            throws FileNotFoundException, IOException{
        // se guarda la imagen en formato png como out.png -->
        
        Code128Bean barcode128Bean = new Code128Bean();
        barcode128Bean.setCodeset(Code128Constants.CODESET_B);
        final int dpi = 100;
        
        //Configure the barcode generator
        //adjust barcode width here
        barcode128Bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
        barcode128Bean.doQuietZone(false);
        
        //Open output file
        File outputFile = new File(path);
        OutputStream out = new FileOutputStream(outputFile);
        try{
            BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(
                    out, "image/x-png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);
            barcode128Bean.generateBarcode(canvasProvider, codigo);
            canvasProvider.finish();
        }
        finally{
            out.close();
        }
    }
    
    
    public String addFRstr(SptFacturaRecord fr, Factura fac) 
            throws ClassNotFoundException, SQLException{
        
        String r = "";
        
        // si es un producto vendido
        if(fr.behave.equals(SptFacturaRecord.TIPO_PRODUCTO_RECORD)){
            // se trunca la descripcion del producto para que se ajuste a una solca linea.
            r += "cod: " + fr.spt_codigo.get() + "\n";
            String descrip = fr.spt_descripcion.getValue();
            if(descrip.length()>this.MAX_CHARS_PER_LINE) r += descrip.substring(0, this.MAX_CHARS_PER_LINE)+"\n";
            else r += descrip + "\n";
            r += this.addFRnums(fr.getSpt_precio_venta().intValue(), fr.getSpt_cantidad().intValue());
        }
        
        // si es una devolcuion
        if(fr.behave.equals(SptFacturaRecord.TIPO_DEVOLUCION_RECORD)){
            // se trunca la descripcion del producto para que se ajuste a una solca linea.
            r += "cod: " + fr.spt_codigo.get() + "\n";
            String descrip = fr.spt_descripcion.getValue();
            if(descrip.length()>this.MAX_CHARS_PER_LINE) r += descrip.substring(0, this.MAX_CHARS_PER_LINE)+"\n";
            else r += descrip + "\n";
            r += this.addFRnums(fr.getSpt_precio_venta().intValue(), fr.getSpt_cantidad().intValue());
        }
        
        // si es un abono
        if(fr.behave.equals(SptFacturaRecord.TIPO_ABONO_RECORD)){
            // se trunca la descripcion del producto para que se ajuste a una solca linea.
            String descrip = fr.spt_descripcion.getValue();
            String time_stamp = "-" + fr.abono.fecha.toString() + " - " + fr.abono.hora.toString();
            if(descrip.length()+time_stamp.length()>this.MAX_CHARS_PER_LINE) r += descrip.substring(0, this.MAX_CHARS_PER_LINE)+"\n" 
                    + time_stamp + "\n";
            else r += descrip + time_stamp + "\n";
            r += this.addFRnums(fr.getSpt_precio_venta().intValue(), fr.getSpt_cantidad().intValue());
        }
        
        // si es un cupon.
        if(fr.behave.equals(SptFacturaRecord.TIPO_CUPON_RECORD)){
            if(fr.cupon.Factura_consecutivo_gen == fac.consecutivo){ // si es un cupon tipo gen.
                r+= "(GEN) " + fr.spt_descripcion.get() + "\n"; 
            } else { // si es u cuon tipo spend.
                r+= fr.spt_descripcion.get() + "\n";
            }
            r += this.addFRnums(fr.getSpt_precio_venta().intValue(), fr.getSpt_cantidad().intValue());
        }
        
        return r;
    }
    
    
    /**
     * recibe los atributos basicos de un factura record y
     * acomoda esos datos de manera ordenada en un String con la estructura
     * visual deseada para la factura.  Excepciones relacionadas con la base de datos por acceso a la tabla de
     * configuracion.
     * @param name
     * @param pventa
     * @param n
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public String addFRnums(int pventa, int n) 
            throws ClassNotFoundException, SQLException{
        String r; // donde se almacena la respuesta del metodo. 
        
        int subtotal = pventa*n; // se transforman a String
        String str_pventa = Integer.toString(pventa);
        String str_cantidad = Integer.toString(n);
        String str_subtotal = Integer.toString(subtotal);
        
        // se calcula la cantidad de espacios que s deben concatenar para lograr alineacion en columnas
        int pv_spaces = 10 - str_pventa.length();
        int cant_spaces = 10 - str_cantidad.length();
        int sub_spaces = 10 - str_subtotal.length();
        
        // se producen los white space para la alineacion en clumnas, usando una libreria externa: apache utils
        String str_pv_spaces = StringUtils.repeat(W_SPACE, pv_spaces);
        String str_cant_spaces = StringUtils.repeat(W_SPACE, cant_spaces);
        String str_sub_spaces = StringUtils.repeat(W_SPACE, sub_spaces);
        
        
        // se organizan el precio  unitario cantidad y subtotal en columnas.
        r = str_pv_spaces + str_pventa + str_cant_spaces + str_cantidad + str_sub_spaces + str_subtotal + "\n\n";
        
        return r;
    }
    
    
     /**
     * recibe 3 Strings arbitrarios y los
     * acomoda de manera apropiadamente espaciada para lograr que queden en columnas.
     * configuracion.
     * @param name
     * @param pventa
     * @param n
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public String addStrCols(String a, String b, String c) 
            throws ClassNotFoundException, SQLException{
        String r; // donde se almacena la respuesta del metodo. 
        
        
        // se calcula la cantidad de espacios que s deben concatenar para lograr alineacion en columnas
        int a_spaces = 10 - a.length();
        int b_spaces = 10 - b.length();
        int c_spaces = 10 - c.length();
        
        // se producen los white space para la alineacion en clumnas, usando una libreria externa: apache utils
        String str_a_spaces = StringUtils.repeat(W_SPACE, a_spaces);
        String str_b_spaces = StringUtils.repeat(W_SPACE, b_spaces);
        String str_c_spaces = StringUtils.repeat(W_SPACE, c_spaces);
        
        
        // se organizan el precio  unitario cantidad y subtotal en columnas.
        r = str_a_spaces + a + str_b_spaces + b + str_c_spaces + c + "\n\n";
        
        return r;
    }
    
    
    /**
     * retorna el servicio de impresion deseado, ya sea la impresora de facturas o la de adhesivos.como argumento se debe usar un String con el nombre de la impresora en el sistema operativo.
     * se puede ver abriende el asistente de impresoras en del respectivo sistema operativo.
     * @param printer_name
     * @param printer_conf_name
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws RuntimeException 
     */ 
    public PrintService GetPrinterService(String printer_name) 
            throws ClassNotFoundException, SQLException, RuntimeException{
        
        // se obtienen todos los servicios de impresion.
        PrintService ps[] = PrintServiceLookup.lookupPrintServices(null, null);
        
        PrintService printer = null;
        
        // se obtiene como PrintService la impresora tmu220pd epson
        for (PrintService p : ps){
            if(p.getName().equals(printer_name)) printer = p;
        }
        
        if(printer == null) throw new RuntimeException("No printer services available.");
        
        return printer;
    }
    
//    
//    /**
//     * metodo que concatena al final del texto que se desea imprimir los caracteres de escape para
//     * hacer corte, hacer feed +feedN+ veces y finalmente el comando ESC  @ que en este caso se 
//     * de la BIXOLON SRP350plusiii limpia el buffer y retorna los valores de los registros de la impresora 
//     * @param texto
//     * @param feedN
//     * @return 
//     */
//    public byte[] AppendEndingESCchars(String texto, int feedN){
//        
//        // start charset conf. ESCAPE sequence para seleccionar el charset latino america
//        final byte start_bytes[] = {(byte) 0x1B, (byte) 0x4C};
//        
//        // caracteres de escape:  *        0x1B ESC   orden     n lineas   *       ESC      cut           *          ESC  command => like reset *
//        //                                  |                   feed lines         n       | |            cut               |  |              ESC @          |
//        final byte endig_bytes[] = {(byte) 0x1B, (byte)0x64, (byte) feedN, (byte) 0x1B, (byte)0x69, (byte) 0x1B, (byte) 0x40};
//        
//        byte[] text_bytes = texto.getBytes(); // se pasa de String a bytes.
//        
//        // se concatenan ambos arreglos de bytes para obtener el contenido completo que se debe enviar a la 
//        // impresora.
//        byte[] fullContent = new byte[start_bytes.length + text_bytes.length + endig_bytes.length];
//        System.arraycopy(start_bytes, 0, fullContent, 0, start_bytes.length);
//        System.arraycopy(text_bytes, 0, fullContent, start_bytes.length, text_bytes.length);
//        System.arraycopy(endig_bytes, 0, fullContent, start_bytes.length + text_bytes.length, endig_bytes.length);
//        // en fullContent se tiene ya toda la informacion completa que se enviara a la impresora.
//        return fullContent;
//    }
    
    
    /**
     * metodo que concatena 2 bytes[] de la forma [ a b ].
     * @param a
     * @param b
     * @return 
     */
    public byte[] appendBytes(byte[] a, byte[] b){
        byte[] fullContent = new byte[a.length + b.length];
        System.arraycopy(a, 0, fullContent, 0, a.length);
        System.arraycopy(b, 0, fullContent, a.length, b.length);
        return fullContent;
    }

    
    /**
     * metodo que se usa para imprimir los datos mas importantes al momento de generar un nuevo cupon.
     * debe usarse este metodo obligatoriamente siempre que se genere un cupon.
     * @param cupo
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void ImprimirCupon(Cupon cupo) 
            throws ClassNotFoundException, SQLException, RuntimeException, PrintException, IOException {
        
        String r = "";
        
        // se imprime una cabecera larga en caso de que se pierdan
        // los primeros bytes del printjob. aun desconozco el motivo.
        // a veces pasa avese no.
        r+= ".\n.\n.\n.\n.\n.\n.\n.\n.\n";
        
        
        r+= "   _ - _   _     _   _ - _\n" +
                "  _     _   _   _   _     _\n" +
                "  _     _    _ _    _     _\n" +
                "  _     _     _     _     _\n" +
                "  _     _    _ _    _     _ \n" +
                "   _   _    _   _    _   _\n" +
                "     -     _     _     -\n \n";
        
        
        r += "Cupon ID :" + Integer.toString(cupo.id) + "\n";
        r += "Password : " + Integer.toString(cupo.pass) + "\n";
        r+= "Cliente ID : " + cupo.Cliente_id + "\n";
        r+= "Cliente Nombre : " + cupo.getCliente().nombre + "\n";
        r+= "fecha emision : " + cupo.fecha + "\n";
        r+= "Doc. (gen.) : " + Integer.toString(cupo.Factura_consecutivo_gen) + "\n";
        
        r+= "para redimir porfavor digitar: \n" + Integer.toString(cupo.id) + "-" + Integer.toString(cupo.pass) + "\n";
        
        SendBytes2ReceiptPrinter(appendBytes(r.getBytes(), CUT));
    }

    
    /**
     * con un cliente especifico cli y su correspondientes facturas con saldo diferente de zero, se imprime
     * el consolidado del cliente cli.
     * @param cli
     * @param lsfac 
     */
    public void PrintConsolidado(Cliente cli, ObservableList<SptFactura> lsfac) throws ClassNotFoundException, SQLException, RuntimeException, PrintException, IOException {
        String r = "";
        
        // se imprime una cabecera larga en caso de que se pierdan
        // los primeros bytes del printjob. aun desconozco el motivo.
        // a veces pasa avese no.
        r+= ".\n.\n.\n.\n.\n.\n.\n.\n.\n";
        
        
        r+= "   _ - _   _     _   _ - _\n" +
                "  _     _   _   _   _     _\n" +
                "  _     _    _ _    _     _\n" +
                "  _     _     _     _     _\n" +
                "  _     _    _ _    _     _ \n" +
                "   _   _    _   _    _   _\n" +
                "     -     _     _     -\n \n";
        
        
        // CABECERA datos negocio
        r += "OXOMODA MEDELLIN\n";
        r += "C. C. Metro Hueco. Cra 53 # 46-31\nLocal 218 \n Medellin Colombia.";
        r += "tel: 513 25 06\n";
        
        r += "Cliente: " + cli.nombre+"\n";
        r += "id: " + cli.id + "\n";
        
        
        r += ".\n.\n.\n.\n.";
        
        r += addStrCols("Doc id", "-", "Saldo");
        
        int total_saldo = 0; // para acumular el saldo total de todas las facturas.
        
        for(SptFactura factu : lsfac){ // se imprime cada id de cada factura y su respectivo saldo
            r += addStrCols(Integer.toString(factu.factura.consecutivo), "-", Integer.toString(factu.factura.saldo));
            total_saldo += factu.factura.saldo;
        }
        
        r += addStrCols("Total", "->", Integer.toString(total_saldo));
        
        // finalmente se imprime el consolidado del cliente.
        SendBytes2ReceiptPrinter(appendBytes(r.getBytes(), CUT));
    }
}
