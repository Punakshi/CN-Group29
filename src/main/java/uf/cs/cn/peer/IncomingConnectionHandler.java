package uf.cs.cn.peer;

import uf.cs.cn.message.HandShakeMessage;
import uf.cs.cn.utils.HandShakeMessageUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IncomingConnectionHandler extends Thread {
    private static Socket connection;
    int peer_id;
    ObjectInputStream listening_stream = null;
    ObjectOutputStream speaking_stream = null;

    private HandShakeMessage handShakeMessage;
    public IncomingConnectionHandler(Socket connection, int peer_id){
        this.connection = connection;
        this.peer_id = peer_id;

        handShakeMessage = new HandShakeMessage(peer_id);
    }

    public void run() {

        // handshake message reading
        byte handshake_32_byte_buffer[] = new byte[32];

        try {
            listening_stream = new ObjectInputStream(connection.getInputStream());
            speaking_stream = new ObjectOutputStream(connection.getOutputStream());
            // First message exchange is handshake
            // Handle handshake message
            listening_stream.read(handshake_32_byte_buffer);
            System.out.println("Receiver from client " + new String(handshake_32_byte_buffer));
            HandShakeMessageUtils.parseHandshakeMessage(handshake_32_byte_buffer);
            // TODO: Check if its the actual peer_id
            HandShakeMessageUtils.checkPeerId(handshake_32_byte_buffer, new byte[]{3, 1, 2, 5});

            // Send handshake
            speaking_stream.write(handShakeMessage.getEncodedMessage());
            System.out.println("Writing " + handShakeMessage.getMessage() + " to client");
            speaking_stream.flush();

            // listen infinitely
            while (true) {
                System.out.print(listening_stream.read());
            }
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }



}
