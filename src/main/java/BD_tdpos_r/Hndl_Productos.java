package BD_tdpos_r;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.chart.XYChart;
import tentactildesktoppos.negocio_r_objs.Producto;

/**
 *
 * @author esteban
 */
public class Hndl_Productos extends BDHandler{
    
    
    /**
     * constructor vacio
     */
    public Hndl_Productos(){
        
    }
    
    
    /**
     * metodo que hace una busqueda de un producto
     * que coinsida con un codigo exacto.
     * @param codigo
     * @return 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public ArrayList<Producto> buscarProducto_codigo(String codigo) 
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Producto> alp = new ArrayList<>();
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.SELECT_CODIGO);
        super.pst.setString(1, codigo);
        super.rs = super.pst.executeQuery();
        while(rs.next()){
            alp.add( new Producto(rs.getString(SQL_Productos.COL_CODIGO),
                    rs.getString(SQL_Productos.COL_DESCRIPCION),
                    rs.getInt(SQL_Productos.COL_COSTO),
                    rs.getInt(SQL_Productos.COL_PVCONTADO),
                    rs.getInt(SQL_Productos.COL_PVCREDITO),
                    rs.getInt(SQL_Productos.COL_PVMAYOR),
                    rs.getDate(SQL_Productos.COL_FINGRESO).toLocalDate(),
                    rs.getInt(SQL_Productos.COL_STOCK),
                    rs.getInt(SQL_Productos.COL_IVA)));
        }
        
        super.CerrarTodo();
        return alp;
    }
    
    
    /**
     * metodo que hace una busqueda de un producto por palabras clave
     * 
     * @param b
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public ArrayList<Producto> buscarProducto_nombre(String b)
            throws ClassNotFoundException, SQLException{
        
        ArrayList<Producto> alp = new ArrayList<>();
        
        // se arma el querie con los respectivos wildcards % se construye el pst como se requiere.
        super.ArreglarFrase_y_PST(SQL_Productos.BASE_SEARCH_DESCRI, SQL_Productos.COL_DESCRIPCION, b);
        
        super.rs = super.pst.executeQuery();
        while(rs.next()){
            alp.add( new Producto(rs.getString(SQL_Productos.COL_CODIGO),
                    rs.getString(SQL_Productos.COL_DESCRIPCION),
                    rs.getInt(SQL_Productos.COL_COSTO),
                    rs.getInt(SQL_Productos.COL_PVCONTADO),
                    rs.getInt(SQL_Productos.COL_PVCREDITO),
                    rs.getInt(SQL_Productos.COL_PVMAYOR),
                    rs.getDate(SQL_Productos.COL_FINGRESO).toLocalDate(),
                    rs.getInt(SQL_Productos.COL_STOCK),
                    rs.getInt(SQL_Productos.COL_IVA)));
        }
        super.CerrarTodo();
        return alp;
    }
    
    
    
    
    
    
    /**
     * metodo que hace registra un producto
     * en la tabla de productos y ademas guarda en la tabla de configuracion el ultimo codigo
     * empleado.
     * @param p 
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void codificarProducto(Producto p) 
            throws ClassNotFoundException, SQLException{
        
        // se guarda el record del producto en la tabla de productos.
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.INSERT);
        super.pst.setString(1, p.codigo);
        super.pst.setString(2, p.descripcion);
        super.pst.setInt(3, p.costo);
        super.pst.setInt(4, p.pventa_contado);
        super.pst.setInt(5, p.pventa_credito);
        super.pst.setInt(6, p.pventa_mayor);
        super.pst.setInt(7, p.stock);
        super.pst.setInt(8, p.iva);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();// se cierra la conexion
        
        // se guarda en la tabla de configuracion el ultimo codigo empleado.
        super.SetConnection(); // se abre otra conexion ya que la anterior se cerro
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Configuracion.UPDATE);
        super.pst.setString(1, p.codigo);
        super.pst.setString(2, SQL_Configuracion.PRT_LAST_CODE_PROD);
        super.pst.executeUpdate(); // se actualiza en la tabla de configuracion el utlimo codigo empleado.
        super.CerrarUpdate(); // se cierra la segunda conexion
    }
    
    
    /**
     * aumenta qnt unidades del producto identificado por el String codigo.
     * si se desea reducir cantidad de stock entonces qnt se selecciona negativo
     * se usa principalmente en la devolucion de productos y para ventas.
     * @param codigo
     * @param qnt
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void Sum2Stock(String codigo, int qnt) 
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.SUMAR_A_STOCK);
        super.pst.setInt(1, qnt);
        super.pst.setString(2, codigo);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    
    /**
     * Elimina de la tabla de prodcutos el producto identificado por la llave
     * promaria String Codigo.
     * @param codigo 
     * @throws java.sql.SQLException 
     * @throws java.lang.ClassNotFoundException 
     */
    public void EliminarProducto(String codigo)
            throws SQLException, ClassNotFoundException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.DELETE);
        super.pst.setString(1, codigo);
        super.pst.executeUpdate();
        super.CerrarUpdate();
    }
    
    /**
     * modifica el producto identificado con la llave primria String old_id.
     * @param pro
     * @param old_id
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void UpdateProducto(Producto pro, String old_id) 
            throws ClassNotFoundException, SQLException{
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.UPDATE);
        super.pst.setString(1, pro.codigo); // el codigo estblecido para modificacion
        //old y new codigo podrian ser iguales.
        // se ponen los demas atributos de la modificacion en el query.
        super.pst.setString(2, pro.descripcion);
        super.pst.setInt(3, pro.costo);
        super.pst.setInt(4, pro.pventa_contado);
        super.pst.setInt(5, pro.pventa_credito);
        super.pst.setInt(6, pro.pventa_mayor);
        super.pst.setInt(7, pro.stock);
        super.pst.setInt(8, pro.iva);
        super.pst.setString(9, old_id); //  el codigo actual que tiene en BD
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
    }
    
    /**
     * metodo que cuenta el numero de records de la tabla de productos.
     * indica cuantos productos hay codificados en la bd.
     * @return 
     */
    public int CountTable() throws ClassNotFoundException, SQLException{
        int r = 0;
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.SELECT_COUNT_NUM_PRODUCTS);
        super.rs = super.pst.executeQuery();
        while(super.rs.next()){ // reading casted column, see comments up to the SQL_Products static property.
            r = super.rs.getInt(SQL_Productos.COL_ASQRY_COUNT);
        }
        return r;
    }
    
    

    
    
    /**
     * Cuando se deja el campo de texto para establecer el codigo de un poroducto en la 
     * secion de codificacion de productos se hace uso de este metodo para establecer 
     * de manera automatica el siguiente codigo.
     * consiste en buscar en la tabla de confiuguracion el ultimo codigo
     * empleado y usar un metodo que a partir de ese String determinad e manera automatica
     * y deacuerdo a una logic establecida cual es el siguiente codigo qu se debe emplear.
     * @return 
     */
    public String getNextProductCode(String cod){
        String r = "";
        
        
        // se pasa el String a arreglo char.
        //    x    x    x          x     ---> String
        // { [0], [1], [2], ... [n-1]} -__-> char array
        char[] cod_array = cod.toCharArray(); // se pasa a array
        
        ArrayList re = new ArrayList(); // para guardar la respuesta de cada ivocacion
        // del metodo que retorna el char+1 en index 0 y el carry como boolean en index 1.
        // se barre el arreglo char desde el ultimo caracter hastas el caracter 0.
        int len = cod_array.length-1;
        while(len >=0){
            re = NextChar(cod_array[len]); // por defecto siempre se va a calcular minimo
            // el siguiente del caracter menos significativo.
            cod_array[len] = (char) re.get(0); // se reemplaza por su char + 1 correspondiente.
            if( !((boolean) re.get(1)) )  len=-2; // si no hay carry entonces no se hace itera mas
            len--; // se resta 1 para continuar la iteracion del caracter mas significativo.
        }
        
        r = String.valueOf(cod_array);
        
        // ya habiendo iterado hasta que no hubiese mas carry entonces se revisa si se itero hasta
        // el caracter mas significativo y se se genero crry por prte de ese caracter.
        if(len==-1){ // si len es == -1 significa que se itero hasta el 0 que es el mas significativo.
            if( (boolean) re.get(1) ){ // si hay carry y se itero hasta el caracter mas significativo entonces
                // se debe agregar un caracter adicional aun mas significativo, que por defecto sera 'a'
                r = "a" + r;
            } 
        }
        // finalmente se retorna el forma String el codigo siguiente
        return r;
    }
    
    /**
     * Este metodo se emplea para la logica que determina el siguiente codigo de un producto.
     * Tiene en cuenta que el char se puede ser un numero 0-9 o una letra aA-zZ. y para cualquiera de
     * los dos casos determina la equivalencia de un +1. en el caso de los numeros es trivial
     * pero en el caso de las letras consiste en la siguiente. Ejemplo. a+1=A, A+1=b, b+1=B, B+1=c...
     * ademas cuando se llega al caracter maximo ( 9 para numeros Z para las letras) se reporta un carry,
     * que indica que debe sumarse +1 al siguiente digito de mayor magnitud. Ej:
     * a9 +1 = A0, ya que 9+1 = 0 y ocurre un carry para 'a'. siempre que haya carry se debera sumar 
     * sucesivamente. si hay un carry y ya no quedan mas digitos entonces se debe agregar un nuevo digito.
     * EJ: Z99 + 1 = a000. En resumen este metodo entrega en el indice 0 del arraylist el valor siguiente del
     * caracter 'e', argumento de esta funcion, y en el indice 1 un boolean = true si hay carry y
     * true en caso contrario. Esta funcionalidad es la que permite cumplir con la especificacion 
     * descrita al comienzo, lo cual se implementa junto con la funcion getNextProductCode()
     * @return 
     */
    public ArrayList NextChar(char e){
        ArrayList r = new ArrayList();
        
        
        char strr = 0; // para guardar el siguiente caracter
         
        boolean carry = false; // para guardar si hay o no carry.
         
        switch(e){
        
            // para el caso de las letras. -->
            case 'a':
                strr = 'b';
                break;
            case 'A':
                strr = 'B';
                break;
            case 'b':
                strr = 'c';
                break;
            case 'B':
                strr = 'C';
                break;
            case 'c':
                strr = 'd';
                break;
            case 'C':
                strr = 'D';
                break;
            case 'd':
                strr = 'e';
                break;
            case 'D':
                strr = 'E';
                break;
            case 'e':
                strr = 'f';
                break;
            case 'E':
                strr = 'F';
                break;
            case 'f':
                strr = 'g';
                break;
            case 'F':
                strr = 'G';
                break;
            case 'g':
                strr = 'h';
                break;
            case 'G':
                strr = 'H';
                break;
            case 'h':
                strr = 'i';
                break;
            case 'H':
                strr = 'I';
                break;
            case 'i':
                strr = 'j';
                break;
            case 'I':
                strr = 'J';
                break;
            case 'j':
                strr = 'k';
                break;
            case 'J':
                strr = 'K';
                break;
            case 'k':
                strr = 'l';
                break;
            case 'K':
                strr = 'L';
                break;
            case 'l':
                strr = 'm';
                break;
            case 'L':
                strr = 'M';
                break;
            case 'm':
                strr = 'n';
                break;
            case 'M':
                strr = 'N';
                break;
            case 'n':
                strr = 'o';
                break;
            case 'N':
                strr = 'O';
                break;
            case 'o':
                strr = 'p';
                break;
            case 'O':
                strr = 'P';
                break;
            case 'p':
                strr = 'q';
                break;
            case 'P':
                strr = 'Q';
                break;
            case 'q':
                strr = 'r';
                break;
            case 'Q':
                strr = 'R';
                break;
            case 'r':
                strr = 's';
                break;
            case 'R':
                strr = 'S';
                break;
            case 's':
                strr = 't';
                break;
            case 'S':
                strr = 'T';
                break;
            case 't':
                strr = 'u';
                break;
            case 'T':
                strr = 'U';
                break;
            case 'u':
                strr = 'v';
                break;
            case 'U':
                strr = 'V';
                break;
            case 'v':
                strr = 'w';
                break;
            case 'V':
                strr = 'W';
                break;
            case 'w':
                strr = 'x';
                break;
            case 'W':
                strr = 'X';
                break;
            case 'x':
                strr = 'y';
                break;
            case 'X':
                strr = 'Y';
                break;
            case 'y':
                strr = 'z';
                break;
            case 'Y':
                strr = 'Z';
                break;
            case 'z':
                strr = 'a';
                break;
            case 'Z': // ocurre carry por letra.
                strr = 'a';
                carry = true; // se indica que ha ocurrido carry
                break;
                
                // para el caso de numeros.
            case '0':
                strr = '1';
                break;
            case '1':
                strr = '2';
                break;
            case '2':
                strr = '3';
                break;
            case '3':
                strr = '4';
                break;
            case '4':
                strr = '5';
                break;
            case '5':
                strr = '6';
                break;
            case '6':
                strr = '7';
                break;
            case '7':
                strr = '8';
                break;
            case '8':
                strr = '9';
                break;
            case '9':
                strr = '0'; // hay carry por numero.
                carry = true; // se indica que ha ocurrido carry.
                break;       
        }
        
        // se ponen los valores en el arrayList de respuesta
        
        r.add(strr);
        r.add(carry);
        
        return r;
    }
    
    
    public XYChart.Series getLast7Records()
            throws ClassNotFoundException, SQLException{
        
        XYChart.Series series = new XYChart.Series();
        series.setName("nuevos records + modificaciones");
        
        super.SetConnection(); // se abre la conexion
        
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Productos.SELECT_COUNT_WITHIN7);
        super.rs = super.pst.executeQuery();
        
        int cuenta = 0;
        
        while(super.rs.next()){
            String fe = super.rs.getString(SQL_Productos.COL_FINGRESO);
            int qnt = super.rs.getInt(SQL_Productos.COL_ASQRY_COUNT);
            series.getData().add(new XYChart.Data(fe, qnt));
            cuenta++;
        }
        
        /**
         * si han pasado menos de 7 dias desde la instalacion del software entonces
         */
        if(!(cuenta==7)){ // se completan con elementos vacio lo que falte.
            for(int i=cuenta; i<=7; i++){
                series.getData().add(new XYChart.Data("", 0));
            }
        }
        
         return series;
    }
    
}
