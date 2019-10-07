import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Server3 {

    public static void main(String[] args) throws IOException {
        MulticastSocket multiSocket = new MulticastSocket(3575);
        InetAddress groupMulticast = InetAddress.getByName("224.0.0.1");
        multiSocket.setBroadcast(true);
        multiSocket.joinGroup(groupMulticast);

        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.out.println("Sending...");
            String msg = "Hai";
            byte[] bufSend = msg.getBytes();

            DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length, groupMulticast, 3575);
            try {
                multiSocket.send(packetSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class Client3 {
    public static void main(String[] args) throws IOException {
        MulticastSocket multiSocket = new MulticastSocket(3575);
        InetAddress groupMulticast = InetAddress.getByName("224.0.0.1");
        multiSocket.setBroadcast(true);
        multiSocket.joinGroup(groupMulticast);
        byte[] bufReceive = new byte[1024];
        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            System.out.println("Receiving...");
            DatagramPacket packetReceive = new DatagramPacket(bufReceive, bufReceive.length);
            try {
                multiSocket.receive(packetReceive);
                System.out.println("msg...");
                System.out.println(new String(bufReceive));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
