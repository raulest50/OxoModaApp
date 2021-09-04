package BD_tdpos_r;

import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.SQLException;


import networking.UDP_Broadcaster;

/**
 *clase conectora a la tabala de SQL de configuracion.
 * Esta clase implementa todas las funciones
 * @author esteban
 */
public class Hndl_Configuracion extends BDHandler{
    
    
    /**
     * metodo que retorna la configuracion identifiada con idconf.
     * @param idconf
     * @return 
     * @throws java.lang.ClassNotFoundException 
     * @throws java.sql.SQLException 
     */
    public String getConfiguracion(String idconf) 
            throws ClassNotFoundException, SQLException{
        String r =  "";
        
        super.SetConnection();
        super.pst = (PreparedStatement) this.link.prepareStatement(SQL_Configuracion.SELECT);
        
        super.pst.setString(1, idconf);
        super.rs = pst.executeQuery();
        while(rs.next()){
            r = super.rs.getString(SQL_Configuracion.COL_VALOR);
        }
        
        super.CerrarTodo();
        return r;
    }
    
    
    /**
     * metodo que se usa para establecer valores string para las configuraciones
     * en la BD negocio_r
     * @param idconf
     * @param valor
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void updateConf(String idconf, String valor) 
            throws ClassNotFoundException, SQLException{
        
        super.SetConnection();
        super.pst = (PreparedStatement) super.link.prepareStatement(SQL_Configuracion.UPDATE);
        
        super.pst.setString(1, valor);
        super.pst.setString(2, idconf);
        super.pst.executeUpdate();
        
        super.CerrarUpdate();
    }
    
    
    
    /**
     * 
     * genera una clave dinamica y la guarda en la tabla de configuracion.
     * de 1000 a 9999.
     * 
     * se debe invocar cada que se hace una operacion sensible para el sistema.
     * 
     */
    public void generarClaveDinamica()
            throws ClassNotFoundException, SQLException{
        Hndl_Cupon hndcu = new Hndl_Cupon();
        String claveDinamica = Integer.toString(hndcu.generarClaveAleatoria());
        
        // se guarda la clave dinamica en la BD
        this.updateConf(SQL_Configuracion.PRT_DYN_PASS, claveDinamica);
    }
    
    

        

    /**
     * metodo que envia un email tomando  los datos ya configurados de la
     * tabla de configuracion.
     * 
     * 27/01/2021:
     * Enviar la clave dinamica por correo funciono bien hasta cierto punto
     * sin embargo presentaba inconvenientes debido a unos cambios de seguridad
     * de google. este metodo de envio depende completamente de disponer un
     * correo activo y configurado. Esta dependencia no solo introduce incertidumbre
     * en el funcionamiento de la funcionalida de lock del programa, sino que 
     * tambien puede requerir que el usuario tenga unos conocimientos minimos,
     * lo cual tambien puede ser problematico. Ya en una ocasion se soluciono
     * un problema a tia doris para poder ver la clave dinamica en el correo
     * y el problema se presenta nuevamente. Debido a esto se decidio que era
     * mejor enviar la clave dinamica de otra manera, como usar UDP. Por lo 
     * anterior, este metodo queda anticuado, pero se deja a manera de referencia.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws MessagingException
     * @throws AddressException
     * @param subject
     * @param body 
     *
    @Deprecated
    public void SendConfiguredEmail(String subject, String body) 
            throws ClassNotFoundException, SQLException, MessagingException, AddressException{
        String gcuenta = this.getConfiguracion(SQL_Configuracion.PRT_SENDER_MAIL);
        String pass = this.getConfiguracion(SQL_Configuracion.PRT_SENDER_PASS);
        String[] destino = { this.getConfiguracion(SQL_Configuracion.PRT_DESTINATION_MAIL) };
        
        this.EnviarTextEmail(gcuenta, pass, destino, subject, body);
    }
    */
    
    
    /**
     * Metodo que envia clave dinamica mediante un UDP Broadcast.
     * se decidio hacer un UDP broadcast, y usar una app de android para 
     * recibir y visualizar el token. Por tanto solo funcionaria si la app
     * y OxoModa Desktop App estan en la misma intranet. 
     * 
     * En caso de necesitar visualizar la clave de manera remota depronto esto
     * puede ser util ver : https://keyvalue.xyz/
     * 
     * El metodo debe crear un periodic task que envie la clave un numero finito
     * de veces y termine.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws SocketException
     */
    public void Send_DynPass_UDP() 
            throws ClassNotFoundException, SQLException, SocketException{
        
        String pass = this.getConfiguracion(SQL_Configuracion.PRT_DYN_PASS);
        UDP_Broadcaster ub = new UDP_Broadcaster(pass);
        ub.start();
    }
    
}
