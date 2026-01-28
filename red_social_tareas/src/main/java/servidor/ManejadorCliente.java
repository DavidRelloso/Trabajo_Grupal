package servidor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorCliente extends Thread {

   	private Socket socket;
	
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            String mensaje;
            
            while ((mensaje = entrada.readLine()) != null) {
               // System.out.println("Cliente dice: " + mensaje);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeBoolean(true);
                //salida.println("Servidor responde: " + mensaje);
            }

        } catch (java.net.SocketException e) {
            System.out.println("El cliente cerró la conexión");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }

//  @Override
//  public void run() {
//  	
//      try {
//          BufferedReader entrada = new BufferedReader(
//                  new InputStreamReader(socket.getInputStream())
//          );
//
//          String mensaje = entrada.readLine();
//          System.out.println("Mensaje recibido: " + mensaje);
//
//          // Procesar el mensaje
//          String[] partes = mensaje.split(",");
//          String nombre = partes[0].replace("Nombre:", "").trim();
//          String email = partes[1].replace("Email:", "").trim();
//
//          
//          
//			BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
//			String mensaje;
//			while ((mensaje = entrada.readLine()) != null) {
//				System.out.println("Cliente dice: " + mensaje);
//				salida.println("Servidor responde: " + mensaje);
//			}
//			
//			socket.close();
//
//      } catch (Exception e) {
//          e.printStackTrace();
//      }
//  }
    
    private void comprobarPeticiones(String mensaje) {
	
    	switch (mensaje) {
    	
    	}
	}    
    
}
