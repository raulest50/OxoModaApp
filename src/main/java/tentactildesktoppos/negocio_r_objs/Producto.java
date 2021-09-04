package tentactildesktoppos.negocio_r_objs;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * Objeto que representa el ente producto.
 * se asume que los articulos manipulados en el negocio
 * no son fraccionables y por ende no son suceptibles de stock
 * y/o precios diferentes a numeros enteros (no seran nunca fracciones)
 * 
 * el nombre de las variables es el mismo que el nombre de las columnas en
 * la tabla de productos.
 * 
 * @author esteban
 */
public class Producto {
    
    /**
     * llave primaria del producto
     */
    public String codigo;
    
    /**
     * descripcion arbitraria para el producto
     */
    public String descripcion;
    
    /**
     * costo del producto
     */
    public int costo;
    
    /**
     * precio de venta de contado. mas popular para  clientes genericos
     */
    public int pventa_contado;
    
    /**
     * precio de venta a credito
     */
    public int pventa_credito;
    
    /**
     * precio de venta  por mayor. para clientes que tienen negocio.
     */
    public int pventa_mayor;
    
    /**
     * fecha en la que el producto se codifico
     */
    public LocalDate fingreso;
    
    /**
     * el numero de unidades del producto codificado.
     */
    public int stock;
    
    
    /**
     * representacion en valor entero
     * del porcentaje de iva
     */
    public int iva;
    
    
    /**
     * constructor vacio
     */
    public Producto(){
        
    }
    
    
    /**
     * Constructor normal
     * @param codigo
     * @param descripcion
     * @param costo
     * @param pventa_contado
     * @param pventa_credito
     * @param pventa_mayor
     * @param fingreso
     * @param stock
     * @param iva 
     */
    public Producto(String codigo, String descripcion, int costo,
            int pventa_contado, int pventa_credito, int pventa_mayor, 
            LocalDate fingreso, int stock, int iva) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.costo = costo;
        this.pventa_contado = pventa_contado;
        this.pventa_credito = pventa_credito;
        this.pventa_mayor = pventa_mayor;
        this.fingreso = fingreso;
        this.stock = stock;
        this.iva = iva;
    }
    
    
    /**
     * Para construir unicamente a partir Strings.
     * en este metodo se hace la convercion a int de los atributos 
     * correspondiente.
     * @param codigo
     * @param descripcion
     * @param costo
     * @param pventa_contado
     * @param pventa_credito
     * @param pventa_mayor
     * @param fingreso
     * @param stock
     * @param iva 
     */
    public Producto(String codigo, String descripcion, String costo,
            String pventa_contado, String pventa_credito, String pventa_mayor, 
            String fingreso, String stock, String iva) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.costo = Integer.parseInt(costo);
        this.pventa_contado = Integer.parseInt(pventa_contado);
        this.pventa_credito = Integer.parseInt(pventa_credito);
        this.pventa_mayor = Integer.parseInt(pventa_mayor);
        
        // se ingresa en la BD con curdate() por lo que aqui se pone solo
        // 0001-01-01
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.fingreso = LocalDate.parse(fingreso, formatter);
        
        this.stock = Integer.parseInt(stock);
        this.iva = Integer.parseInt(iva);
    }
    
    /**
     * retorna una representacion String de este objeto, apropiada
     * para un Area de texto.
     * @return 
     */
    public String produto2Str(){
        String r = "";
        r+= "Codigo :  " + this.codigo + "\n";
        r+= "Descripcion :  " + this.descripcion + "\n";
        r+= "costo :  " + Integer.toString(this.costo) + "\n";
        r+= "Precio de Venta Contado :  " + Integer.toString(this.pventa_contado) + "\n";
        r+= "Precio de Venta Credito :  " + Integer.toString(this.pventa_credito) + "\n";
        r+= "Precio de Venta PorMayor:  " + Integer.toString(this.pventa_mayor) + "\n";
        r+= "Stock:  " + Integer.toString(this.stock) + "\n";
        r+= "iva:  " + Integer.toString(this.iva) + "\n";
        return r;
    }
    
    
}
