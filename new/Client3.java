public class Client3 {
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
