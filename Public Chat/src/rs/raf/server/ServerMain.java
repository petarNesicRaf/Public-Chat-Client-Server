package rs.raf.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class ServerMain {

    public static final int PORT = 9999;
    public static Map<String, Socket> usernameMap = new ConcurrentHashMap<>();
    public static BlockingQueue<String> messageHistory = new LinkedBlockingDeque<>();
    public static List<Socket> clients = new CopyOnWriteArrayList<>();
    public static BlockingQueue<String> chatQue = new LinkedBlockingDeque<>();
    public static Set<String> profanitySet = new HashSet<>();
    private static final String[] competitorsArray = {"whatsapp", "viber", "discord","signal", "telegram"};

    //inicijalizacija server socket-a, broadcaster-a i set-a sa nedozvoljenim recima
    public static void main(String[] args)  {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            profanitySet.addAll(Arrays.asList(competitorsArray));

            Thread broadcaster = new Thread(new Broadcaster());
            broadcaster.start();
            //cekanje za konekciju klijenta, dodavanje klijenta u listu za brodkast i pokretanje serverskog thread-a
            while (true) {
                System.out.println("The rs.raf.server is waiting for connection.");
                Socket clientSocket = serverSocket.accept();
                System.out.println("A rs.raf.client has connected to the rs.raf.server.");
                clients.add(clientSocket);
                Thread serverThread = new Thread(new ServerThread(clientSocket));
                serverThread.start();
            }
        } catch (IOException e) {
            System.out.println("Connection error occurred.");
            //e.printStackTrace();
        }
    }
}
