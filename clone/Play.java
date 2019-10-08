import java.net.*;

public class Play extends Voice {

    private final int packetsize = 1000;
    private final int port = 55001;
    private boolean stopPlay = false;
    private MulticastSocket socket = null;
    private InetAddress host = null;

    @Override
    public void run() {

        try {
            // Construct the socket
            this.socket = new MulticastSocket(3575);
            this.socket.setBroadcast(true);
            this.socket.joinGroup(this.host);
            System.out.println("The server is ready");
//
//            // Create a packet
            DatagramPacket packet = new DatagramPacket(new byte[this.packetsize], (this.packetsize));
//
//            byte[] bufReceive = new byte[1024];
//            while (true) {
//                System.out.print("");
//                if (!this.stopPlay) {
////                    try {
////                        Thread.sleep(2000);
////                    } catch (InterruptedException e1) {
////                        // TODO Auto-generated catch block
////                        e1.printStackTrace();
////                    }
//
////                System.out.println("Receiving...");
//                    DatagramPacket packetReceive = new DatagramPacket(bufReceive, bufReceive.length);
//                    try {
//                        socket.receive(packetReceive);
//                        System.out.println();
//                        System.out.println("msg..." + new String(bufReceive));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }

            this.stopPlay = true;
            this.playAudio();
            while (true) {
                System.out.print("");
                if (!this.stopPlay) {
                    try {

                        System.out.println("reciving");
                        // Receive a packet (blocking)
                        this.socket.receive(packet);

                        // Print the packet
                        this.getSourceDataLine().write(packet.getData(), 0, this.packetsize); //playing the audio

                        packet.setLength(this.packetsize);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }

    public Play(InetAddress host) {
        this.host = host;
    }

    public void stopPlay() {
        System.out.println("stop play equal to True");
        this.stopPlay = true;
    }

    public void startPlay() {
        System.out.println("stop play equal to False");
        this.stopPlay = false;
    }
}