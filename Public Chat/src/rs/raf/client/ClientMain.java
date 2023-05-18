package rs.raf.client;

import java.io.IOException;
import java.net.Socket;

public class ClientMain {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 9999;

    //pokretanje threadova
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(IP,PORT);

            Thread input = new Thread(new InputThread(socket));
            Thread output = new Thread(new OutputThread(socket));
            input.start();
            output.start();

        } catch (IOException e) {
            System.out.println("An error occurred while connecting to the server.");
            //e.printStackTrace();
        }
    }
}
