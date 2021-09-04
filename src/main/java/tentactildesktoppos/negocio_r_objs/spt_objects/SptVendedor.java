package tentactildesktoppos.negocio_r_objs.spt_objects;

import javafx.beans.property.SimpleStringProperty;
import tentactildesktoppos.negocio_r_objs.Vendedor;

/**
 *
 * @author esteban
 */
public class SptVendedor extends Vendedor{
    
    public SimpleStringProperty spt_id; // ´para el TableView
    
    public SimpleStringProperty spt_nombre; // para el TablleVIew
    
    
    public SptVendedor(Vendedor v){
        super(v.id, v.nombre, v.tel1, v.tel2, v.direccion, v.email, 
                v.descripcion, v.nacimiento, v.fregistro, v.estado);
        
        this.spt_id = new SimpleStringProperty(v.id);
        this.spt_nombre = new SimpleStringProperty(v.nombre);
    }
    
    
    public String getSpt_id() {
        return spt_id.get();
    }
    
    public String getSpt_nombre() {
        return spt_nombre.get();
    }
    
    /**
     * este metodo organiza en un solo string todos los demas datos
     * @return 
     */
    public String RetrieveRemainingInfo(){
        String r = "";
        r = "Telefono 1: " + super.tel1 + "\n";
        r += "Telefono 2: " + super.tel2 + "\n";
        r += "Direccion: " + super.direccion + "\n";
        r += "Email: " + super.email + "\n";
        r += "Fecha de Nacimiento: " + super.nacimiento.toString() + "\n";
        r += "Fecha de Ingreso a la Base de datos: " + super.fregistro.toString() + "\n";
        
        if(super.estado==1) r += "Estado: " + "Activo" + "\n";
        else r += "Estado: " + "Inactivo" + "\n";
        
        r += "Descripcion: " + super.descripcion + "\n";
        return r;
    }
    
}
