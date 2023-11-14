import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class OrganizarCliente implements Runnable {
    static ArrayList<OrganizarCliente> listaClientes = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clienteApelido;

    public OrganizarCliente(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
            this.clienteApelido = bufferedReader.readLine();
            listaClientes.add(this);
            broadcast("SERVIDOR:" + clienteApelido + "entrou no chat!");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }




    @Override
    public void run() {
        String mensagemCliente;
        while (socket.isConnected()){
            try {
                mensagemCliente = bufferedReader.readLine();
                broadcast(mensagemCliente);

            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }

    public void broadcast(String mensagem){
        for (OrganizarCliente organizarCliente: listaClientes){
            try{
                if (!organizarCliente.clienteApelido.equals(clienteApelido)){
                    organizarCliente.bufferedWriter.write(mensagem);
                    organizarCliente.bufferedWriter.newLine();
                    organizarCliente.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);

            }
        }
    }

    public void removerOrganizarCliente(){
        listaClientes.remove(this);
        broadcast("SERVER:" + clienteApelido + "saiu do chat!");

    }

    public void closeEverything(Socket socket,BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removerOrganizarCliente();
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
}
