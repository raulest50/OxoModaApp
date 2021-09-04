package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Esteban
 * 
 * Para enviar mensajes broadcast UDP.
 * clase creada como alternativa para enviar la clave dinamica a una app de
 * android.
 */
public class UDP_Broadcaster extends Thread{
    
    
    private final byte[] msg;
    
    private final DatagramSocket socket; // para enviar y recivir informacion
    
    private final int UDP_PORT_BIND = 11752;
    private final int UDP_PORT_SENDTO = 11751;
    
    /**
     * El token o pase dinamico se transforma a bytes y se inicializa el
     * atributo token. Este es el que se envia al llamar SendUDP() dentro de run
     * @param token
     * @throws SocketException 
     */
    public UDP_Broadcaster(String token) throws SocketException {
        this.msg = ("OxoModaDynPass:${token}:").getBytes();
        this.socket = new DatagramSocket(UDP_PORT_BIND);
        this.socket.setBroadcast(true);
    }
    
    /**
     * se envia 
     */
    @Override
    public void run(){
        try{
            for(int i=0; i<=4; i++){
                SendUDP(this.msg);
                sleep(400);
            }
        } catch(InterruptedException ex){
            socket.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(UDP_Broadcaster.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(UDP_Broadcaster.class.getName()).log(Level.SEVERE, null, ex);
            socket.close();
        }
        socket.close();
    }
    
    private void SendUDP(byte[] data) throws UnknownHostException, IOException{
        DatagramPacket data_send = new DatagramPacket(data, data.length, GetBroadcast(), UDP_PORT_SENDTO);
        socket.send(data_send);
        socket.send(data_send);
        socket.send(data_send);
    }
    
    public InetAddress GetBroadcast() throws UnknownHostException{
        //InetAddress broadcast;    
        //broadcast = InetAddress.getByName("255.255.255.255");
        return InetAddress.getByName("255.255.255.255");
    }
}
