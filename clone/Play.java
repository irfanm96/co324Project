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
            this.socket = new MulticastSocket(8888);
            this.socket.setBroadcast(true);
            this.socket.joinGroup(this.host);
            System.out.println("The server is ready");

            // Create a packet
            DatagramPacket packet = new DatagramPacket(new byte[this.packetsize], (this.packetsize));
            this.playAudio();

            while (true) {
                if (!this.stopPlay) {
                    try {

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
        this.stopPlay = true;
    }
    
    public void startPlay() {
        this.stopPlay = false;
    }
}