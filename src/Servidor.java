import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {


  public ServerSocket serverSocket;

  public Servidor(ServerSocket serverSocket){
      this.serverSocket = serverSocket;
  }

public void inciarServidor(){

      try {

          while (!serverSocket.isClosed()){
              Socket socket = serverSocket.accept();
              System.out.println("Um novo cliente se conectou !");
              OrganizarCliente organizarCliente = new OrganizarCliente(socket);
              Thread thread = new Thread(organizarCliente);
              thread.start();
          }

      }catch (IOException e){

          //TODO
      }

}

public void closeServerSocket(){
      try{
          if (serverSocket != null){
              serverSocket.close();
          }
      }catch (IOException e){
          e.printStackTrace();
      }
}



    public static void main(String[] args) throws IOException {

      InetAddress address = InetAddress.getByName("192.168.18.48");

      ServerSocket serverSocket = new ServerSocket(4899,50,address);
      Servidor servidor = new Servidor(serverSocket);
      servidor.inciarServidor();

    }


}