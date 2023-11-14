import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {

private Socket socket;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;
private String clienteApelido;

    public void setClienteApelido(String clienteApelido) {
        this.clienteApelido = clienteApelido;
    }

    public Cliente(Socket socket, String clienteApelido) throws UnknownHostException {
    try {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
        this.clienteApelido = clienteApelido;
    }catch (IOException e){
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
}

public void enviarMensagem(){
    try {
        bufferedWriter.write(clienteApelido);
        bufferedWriter.newLine();
        bufferedWriter.flush();

        Scanner sc = new Scanner(System.in);
        while (socket.isConnected()){
            String mensagem = sc.nextLine();
            if(mensagem.contains("$changenick")){
                String[] partes = mensagem.split(" ");
                String newname = partes[1];
                bufferedWriter.write("SERVIDOR: " + clienteApelido + "  o apelido para: " + newname);
                setClienteApelido(newname);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            else {
                bufferedWriter.write(clienteApelido + ":" + mensagem);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        }
    }catch (IOException e){
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
}

public void receberMensagem(){
    new Thread(new Runnable() {
        @Override
        public void run() {
            String mensagemRecebida;

            while (socket.isConnected()){
                try {
                    mensagemRecebida = bufferedReader.readLine();
                    System.out.println(mensagemRecebida);

                }catch (IOException e){
                    closeEverything(socket,bufferedReader,bufferedWriter);
                }

            }
        }
    }).start();
}

public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
    try {
        if(bufferedReader != null){
            bufferedReader.close();
        }
        if (bufferedWriter != null){
            bufferedWriter.close();
        }
        if (socket != null){
            socket.close();
        }
    }catch (IOException e){
        e.printStackTrace();
    }
}

    public static void main(String[] args) throws IOException {
        InetAddress adress = InetAddress.getByName("192.168.18.48");
        Scanner sc = new Scanner(System.in);
        System.out.println("Insira seu nome de usuario:");
        String apelidoUsuario = sc.nextLine();
        Socket socket = new Socket(adress,4899);
        Cliente cliente = new Cliente(socket,apelidoUsuario);
        cliente.receberMensagem();
        cliente.enviarMensagem();
    }

}
