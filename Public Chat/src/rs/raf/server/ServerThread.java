package rs.raf.server;

import com.sun.security.ntlm.Server;

import java.io.*;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerThread implements Runnable{
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            String userName;
            //provera username-a, slanje dobrodoslice i istorije
            while(true) {
                out.println("Please enter your username: ");
                userName = in.readLine();
                if (!ServerMain.usernameMap.containsKey(userName)) {
                    ServerMain.usernameMap.put(userName, socket);

                    out.println(userName + " joined the chat room.");

                    for (String message : ServerMain.messageHistory) {
                        out.println(message);
                    }
                    break;
                } else {
                    out.println("Username must be unique, this one already exists.");
                }
            }
            //citanje klijentske poruke, formatiranje i azuriranje poruke
            while(true) {
                String message = in.readLine();
                String formatedMessage = formatMessage(message.trim(), userName);

                ServerMain.chatQue.add(formatedMessage);

                if (ServerMain.messageHistory.size() >= 100)
                    ServerMain.messageHistory.poll();

                ServerMain.messageHistory.offer(formatedMessage);
                //System.out.println(ServerMain.messageHistory.toString());
            }
        } catch (IOException e) {
            System.out.println("Connection error occurred.");
            //e.printStackTrace();
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("Disconnection error occurred");
                    //e.printStackTrace();
                }
            }
            if (out != null){
                out.close();
            }

            if(this.socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Disconnection error occurred");
                    //e.printStackTrace();
                }
            }
        }
    }
    //formatiranje poruke i cenzura reci
    private String formatMessage(String message, String username)
    {
        String clean = message;
        String[] split = message.split(" ");
        for(String temp : split)
        {
            if(ServerMain.profanitySet.contains(temp))
            {
                String replacement = "";
                replacement += temp.charAt(0);
                for(int i =0; i<temp.length()-2; i++)
                {
                    replacement+="*";
                }
                replacement+=temp.charAt(temp.length()-1);
                System.out.println("Replacement " + replacement);
                clean = clean.replace(temp, replacement);
            }
        }
        String pattern = "dd.mm.yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date currentDate = new Date();
        String date = simpleDateFormat.format(currentDate);
        String formatted = "[" +date +"]" + " - " + username + ": " + clean;
        return formatted;
    }
}
