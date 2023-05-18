package rs.raf.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;

public class InputThread implements Runnable{

    private Socket clientSocket;
    private BufferedReader in;

    public InputThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    //citanje poruka
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while(true){
                String message = in.readLine();
                System.out.println(message);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while connecting.");
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("An error occurred while disconnecting.");
                    //e.printStackTrace();
                }
            }
        }

    }
}
