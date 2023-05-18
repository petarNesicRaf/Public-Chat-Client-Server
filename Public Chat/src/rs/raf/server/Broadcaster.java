package rs.raf.server;

import java.io.*;
import java.net.Socket;

public class Broadcaster implements Runnable{

    private PrintWriter out;
    //slanje najnovije poruke svim klijentima
    @Override
    public void run() {
        try {
            while (true) {
                String broadcastMessage = null;
                broadcastMessage = ServerMain.chatQue.take();
                broadcast(broadcastMessage);
            }
        } catch (InterruptedException e) {
            System.out.println("Broadcast interrupted. ");
            //e.printStackTrace();
        }finally {
            if(out != null){
                out.close();
            }
        }
    }

    //slanje poruke svim klijentima ukoliko su registrovani
    public void broadcast(String message)
    {
        for(Socket clientSocket : ServerMain.clients)
        {
            if(clientSocket != null && ServerMain.usernameMap.containsValue(clientSocket))
            {
                try {
                    out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
                    out.println(message);
                } catch (IOException e) {
                    System.out.println("Connection error occurred.");
                    //e.printStackTrace();
                }
            }
        }
    }
}
