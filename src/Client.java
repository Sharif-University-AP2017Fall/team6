import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner screenScanner = new Scanner(System.in);
        System.err.println("Try to connect to server");
        Socket socket;// = new Socket("localhost", 9999);
        System.err.println("Connected to the server");
        Formatter formatter;// = new Formatter(socket.getOutputStream());
        Scanner scanner;
        System.err.println("sending message");


        for (int i = 0; i < 10; i++) {
            socket = new Socket("localhost", 9999);
            formatter = new Formatter(socket.getOutputStream());
            formatter.format(screenScanner.nextLine() + "\n");
            formatter.flush();
            System.err.println("Message sent");
            System.err.println("Waiting for Answer");
            scanner = new Scanner(socket.getInputStream());
            System.out.println("Server: " + scanner.nextLine());
            scanner.close();
            formatter.close();
            socket.close();
        }

        //socket.close();
    }
}
