package com.ict9.oop;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    final static int ServerPort = 2424;

    public static void main(String[]args) throws UnknownHostException, IOException {
        Scanner cucai = new Scanner(System.in);

        InetAddress ip = InetAddress.getByName("localhost");

        Socket s = new Socket(ip, ServerPort);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        Thread send = new Thread(new Runnable() {
            @Override
            public void run() {

                    while(true){
                        String msg = cucai.nextLine();

                        try{
                            dos.writeUTF(msg);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

            }
        });
        Thread read = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
        send.start();
        read.start();
    }
}
