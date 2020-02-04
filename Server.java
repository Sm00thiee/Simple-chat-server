package com.ict9.oop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class Server {
   static Vector<ClientHandler> ar = new Vector<>();

   static int i = 0;

   public static void main(String []args) throws IOException{
       ServerSocket ss = new ServerSocket(2424);

       Socket s;

       while(true){
           s = ss.accept();

           System.out.println("New client is connecting : " + i);

           DataInputStream dis = new DataInputStream(s.getInputStream());
           DataOutputStream dos = new DataOutputStream(s.getOutputStream());

           System.out.println("Creating handler...");

           ClientHandler cucai = new ClientHandler(s, "Client", dis, dos);

           Thread t = new Thread(cucai);

           System.out.println("Throwing to active client list");

           ar.add(cucai);

           t.start();

           i++;
       }
   }
}

class ClientHandler implements Runnable{
    final DataInputStream dis;
    final DataOutputStream dos;
    private String name;
    Socket s;
    boolean online;
    Scanner bapcai = new Scanner(System.in);


    public ClientHandler(Socket s, String name, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.online = true;

    }

    @Override
    public void run() {
        String received;
        while(true){
            try{
                received = dis.readUTF();

                System.out.println(received);

                if(received.equals("quit")){
                    this.online = false;
                    this.s.close();
                    break;
                }

                StringTokenizer st = new StringTokenizer(name, "#");
                String msg = st.nextToken();
                String recipient = st.nextToken();

                for(ClientHandler carot : Server.ar){
                    if(carot.name.equals(recipient) && carot.online==true){
                        carot.dos.writeUTF(this.name + " : " +msg);
                        break;
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            this.dis.close();
            this.dos.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}