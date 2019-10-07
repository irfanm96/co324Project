package multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSocketServer extends Thread {

    final static String INET_ADDR = "224.0.0.3";
    final static int PORT = 8888;

    @Override
    public void run() {
        // Get the address that we are going to connect to.
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(INET_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Open a new DatagramSocket, which will be used to send the data.
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            int i = 0;
            while (true) {
                String msg = "Sent message no " + i;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);

                System.out.println("Server sent packet with msg: " + msg + '\n');
                Thread.sleep(500);
                i = (i + 1) % 10;
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }
}
