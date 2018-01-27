import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        System.err.println("Listening to port 9999");
        Scanner screenScanner = new Scanner(System.in);
        ServerSocket serverSocket = new ServerSocket(9999);
        System.err.println("Waiting for client to connect");
        Socket socket;// = serverSocket.accept();
        System.err.println("Connected to the client");
        Formatter formatter;
        Scanner scanner;


        for (int i = 0; i < 10; i++) {
            socket = serverSocket.accept();
            scanner = new Scanner(socket.getInputStream());
            String arg = scanner.nextLine();
            System.out.println("Client: " + arg);
            formatter = new Formatter(socket.getOutputStream());
            formatter.format(screenScanner.nextLine() + "\n");
            formatter.flush();
            formatter.close();
            socket.close();
        }

        //socket.close();
    }
}
