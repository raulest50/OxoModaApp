package tentactildesktoppos.negocio_r_objs;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *Clase que encapsula todos los atributos y comportamientos del ente abono.
 * @author esteban
 */
public class Abono {
    
    /**
     * llave primaria autoincremento.
     * se pone para disponer de una llave primaria obligatoria pero no
     * planeo dar uso a esta columna.
     */
    public int id;
    
    
    /**
     * valor en pesos del abono
     */
    public int valor;
    
    
    /**
     * fecha en la que se hizo el abono
     */
    public LocalDate fecha;
    
    
    /**
     * hora en la que se hizo el abono
     */
    public LocalTime hora;
    
    
    /**
     * cliente que hizo el abono. es redundante ya que la misma factura
     * a la que se adjudica el abono ya contiene el codigo del cliente.
     * pero igual se usa esta redundancia para mejorar integridad y agilizar 
     * algunas operaciones
     */
    public String Cliente_id;
    
    /**
     * consecutivo de la factura a la que peretenece el abono
     */
    public int Factura_consecutivo;
    
    
    /**
     * cc del vendedor que registro el abono.
     */
    public String Vendedor_id;
    
    
    /**
     * constructor pensado para usar cuando se lee de la BD
     * @param id
     * @param valor
     * @param fecha
     * @param hora
     * @param Cliente_id
     * @param Factura_consecutivo
     * @param Vendedor_id 
     */
    public Abono(int id, int valor, LocalDate fecha, LocalTime hora,
            String Cliente_id, int Factura_consecutivo, String Vendedor_id){
        this.id = id;
        this.valor = valor;
        this.fecha = fecha;
        this.hora = hora;
        this.Cliente_id = Cliente_id;
        this.Factura_consecutivo = Factura_consecutivo;
        this.Vendedor_id = Vendedor_id;
    }
    
    
    /**
     * constructor pensado para usar cuando se va a insertar una fila a la 
     * tabla de abonos. no se inicializan atributos fecha ni hora ya que
     * estos son establecidos por MySQL con cursdate() y curtime()
     * @param id
     * @param valor
     * @param Cliente_id
     * @param Factura_consecutivo
     * @param Vendedor_id 
     */
    public Abono(int id, int valor, String Cliente_id, int Factura_consecutivo,
            String Vendedor_id){
        this.id = id;
        this.valor = valor;
        this.Cliente_id = Cliente_id;
        this.Factura_consecutivo = Factura_consecutivo;
        this.Vendedor_id = Vendedor_id;
    }
    
}
