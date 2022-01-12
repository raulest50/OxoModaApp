/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import java.io.File;
import java.io.IOException;
import javafx.collections.ObservableList;
import tentactildesktoppos.negocio_r_objs.spt_objects.SptCliente;

/**
 *
 * @author Informatica10
 */
public class Clientes_Excel extends Excel_Handler {
    private final String fileName = "ConsolidadoClientes.xlsx";
    
    private ObservableList<SptCliente> clientes;
    
    public Clientes_Excel(){
        super("clientes");   
    }
    
    public void setClientes(ObservableList<SptCliente> _clientes){
        clientes = _clientes;
    }
    
    public void crear(){
        AddRow();
        AddCell("ID"); AddCell("Nombre"); AddCell("Tel_1");
        AddCell("Tel_2"); AddCell("Tel_3"); AddCell("Direccion");
        AddCell("Email"); AddCell("Forma Cobro"); AddCell("Cumple");
        AddCell("Tipo"); AddCell("Ubicacion"); AddCell("Ultimo Abono");
        AddCell("Total");
        
        for(SptCliente _item : clientes){
            AddRow();
            AddCell(_item.getSpt_id());
            AddCell(_item.getSpt_nombre());
            AddCell(_item.tel1);
            AddCell(_item.tel2);
            AddCell(_item.tel3);
            AddCell(_item.direccion);
            AddCell(_item.email);
            AddCell(_item.descripcion);
            AddCell(_item.cumple.toString());
            AddCell(_item.tipo);
            AddCell(_item.ubicacion);

            String lastAbono = _item.getSpt_last_abono();
            AddCell(lastAbono);
            
            int consolidado = _item.getSpt_consolidado(); 
            AddCell(consolidado);
            //System.out.println(_item.InfoClienteStr());
        }
        
    }
    public void generar(File file) throws IOException{
        try {
            toFile(file);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
    
