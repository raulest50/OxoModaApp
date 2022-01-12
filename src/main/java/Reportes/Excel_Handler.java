/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Reportes;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Informatica10
 */
public class Excel_Handler {
    protected Workbook wb = null;
    protected Sheet sh = null;
    protected Row row = null;
    protected int rowCounter;
    protected int cellCounter;
    
    public Excel_Handler(String SheetName){
        wb = new XSSFWorkbook ();
        //wb.setCompressTempFiles(true);
        sh = wb.createSheet(SheetName);
        rowCounter=0;
    }
    
    public void AddCell(String cellValue){
        Cell c = row.createCell(cellCounter++);
        if (cellValue != null && !cellValue.isBlank()){
            c.setCellValue(cellValue.toUpperCase());
        }
    }
    
    public void AddCell(int cellValue){
         Cell c = row.createCell(cellCounter++);
         c.setCellValue(cellValue);
    }
    
    public void AddCell(double cellValue){
         Cell c = row.createCell(cellCounter++);
         c.setCellValue(cellValue);
    }
    
    public void AddCell(LocalDate cellValue){
        Cell c = row.createCell(cellCounter++);
        if(cellValue != null){ 
            c.setCellValue(cellValue);
        }
    }
    
    public void AddRow(){
        row = sh.createRow(rowCounter++);
        System.out.println("Fila Creada");
        cellCounter = 0;
    }
    /*
    private static void removeCalcChain(SXSSFWorkbook  workbook) throws Exception {
        CalculationChain calcchain = workbook.getCalculationChain();
        Method removeRelation = POIXMLDocumentPart.class.getDeclaredMethod("removeRelation", POIXMLDocumentPart.class); 
        removeRelation.setAccessible(true); 
        removeRelation.invoke(workbook, calcchain);
    }
    */
    public void toFile(String fileName) throws IOException, Exception{
        
        //if (wb instanceof XSSFWorkbook ) removeCalcChain((XSSFWorkbook) wb);
        FileOutputStream out = new FileOutputStream(new File(fileName));
        wb.write(out);
        out.close();
        System.out.println("Cerrado");
    }
    
    public void toFile(File file) throws IOException, Exception{
        
        //if (wb instanceof XSSFWorkbook ) removeCalcChain((XSSFWorkbook) wb);
        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();
        System.out.println("Cerrado");
    }
}
