package com.example;

import org.productivity.java.syslog4j.SyslogConstants;
import org.productivity.java.syslog4j.server.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MySyslogServer {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 32376;

    public void receive() {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            while (true) {
                DatagramPacket packet = new DatagramPacket(new byte[512], 512);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println(packet.getAddress() + "/" + packet.getPort() + "." + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveSyslog() {
        SyslogServerIF server = SyslogServer.getInstance(SyslogConstants.UDP);
        SyslogServerConfigIF config = server.getConfig();
        config.setHost(HOST);
        config.setPort(PORT);
        config.addEventHandler((SyslogServerEventHandlerIF) (syslogServer, event) ->
                System.out.println("receive from:" + event.getHost() + "\tmessage:" + event.getMessage()));
//        SyslogServer.getThreadedInstance(SyslogConstants.UDP);
        new Thread(server).start();
        try {
            System.out.println("I'm waiting..");
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MySyslogServer().receiveSyslog();
    }
}
