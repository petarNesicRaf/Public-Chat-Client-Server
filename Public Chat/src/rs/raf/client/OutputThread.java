package rs.raf.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class OutputThread implements Runnable{

    private Socket clientSocket;
    private PrintWriter out;
    private Scanner sc;

    public OutputThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //slanje poruka serveru
    @Override
    public void run() {

        try {
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
            sc = new Scanner(System.in);

            String message;
            while(true)
            {
                //System.out.print(">");
                message = sc.nextLine().trim();
                out.println(message);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while connecting.");
            //e.printStackTrace();
        }finally {
            if(out != null){
                out.close();
            }
        }

    }
}
