import java.net.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.*;

public class SendVoice extends Thread{ //extend

    private InetAddress hostName;
    private DatagramSocket socket;
    private ByteArrayOutputStream byteArrayOutputStream = null;
    private byte tempBuffer[] = new byte[500];
    private static boolean stopCapture = false;

    public AudioFormat audioFormatsend;
    private TargetDataLine targetDataLine;

    public static AudioFormat getAudioFormat() { //getAudioFormat()
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    public synchronized void  captureAudio() {
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
            System.out.println("Available mixers:");
            Mixer mixer = null;
            for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
                System.out.println(cnt + " " + mixerInfo[cnt].getName());
                mixer = AudioSystem.getMixer(mixerInfo[cnt]);

                Line.Info[] lineInfos = mixer.getTargetLineInfo();
                if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                    System.out.println(cnt + " Mic is supported!");
                    break;
                }
            }

            audioFormatsend = getAudioFormat();     //get the audio format
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormatsend);

            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormatsend);
            targetDataLine.start();

        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        try {
            this.socket = new DatagramSocket(20000);
            this.captureAudio();
            this.byteArrayOutputStream = new ByteArrayOutputStream();
            stopCapture = false;
            try {
                int readCount;
                while (!stopCapture) {
                    readCount = targetDataLine.read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer

                    if (readCount > 0) {
                        this.byteArrayOutputStream.write(this.tempBuffer, 0, readCount);

                        // Construct the datagram packet
                        DatagramPacket packet = new DatagramPacket(this.tempBuffer, this.tempBuffer.length, this.hostName,25000);

                        // Send the packet
                        this.socket.send(packet);
                    }
                }
                this.byteArrayOutputStream.close();
            } catch (IOException e) {

                e.printStackTrace();
                //System.exit(0);
            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            this.socket.close();
        }
    }

    public SendVoice(InetAddress hostName) {
        this.hostName = hostName;
    }

    public static void main(String[] args) {

        // Check the whether the arguments are given
        if (args.length != 1) {
            System.out.println("Usage : java Capture <hostName>");
            return;
        }

        try {

            SendVoice t1 = new SendVoice(InetAddress.getByName(args[0]));
            t1.start();

            RecieveVoice t2 = new  RecieveVoice();
            t2.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     static class RecieveVoice extends Thread {

        private AudioFormat audioFormatRecieve;
        private SourceDataLine sourceDataLine;

        @Override
        public void run() {

            try {

                // Construct the socket
                DatagramSocket recieveSocket = new DatagramSocket(25000);
                // Create a packet
                DatagramPacket recievedPacket = new DatagramPacket(new byte[500], (500));

                try {
                    audioFormatRecieve = getAudioFormat();     //get the audio format

                    DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormatRecieve);
                    sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
                    sourceDataLine.open(audioFormatRecieve);
                    sourceDataLine.start();

//            //Setting the maximum volume
                    FloatControl control = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
                    control.setValue(control.getMaximum()/2);

                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                    System.exit(0);
                }

                while (!stopCapture) {

                    try {

                        // Receive a packet (blocking)
                        recieveSocket.receive(recievedPacket);

                        // Print the packet
                        this.sourceDataLine.write(recievedPacket.getData(), 0, 500); //playing the audio

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            } catch (Exception e) {

                e.printStackTrace();

            }

        }


    }
}