package uf.cs.cn.message;
import uf.cs.cn.peer.Peer;
import uf.cs.cn.utils.ActualMessageUtils;
import uf.cs.cn.utils.PeerLogging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * message_length: It will be of size 4 bytes, and contain the size of the packet without including message length bytes.
 * Note: To transmit in the network, it used BigInteger implementation to byte array and vice versa.
 *
 *
 */
public class ActualMessage {
    private int message_length;
    private byte message_type;
    private byte[] payload;
    private PeerLogging peerLogging;

    public ActualMessage(int message_length, byte message_type) throws Exception {
        this.message_length = message_length;
        this.setMessage_type(message_type);
        payload = new byte[message_length];
        peerLogging = new PeerLogging(String.valueOf(Peer.getInstance().getSelf_peer_id()));
    }

    /**
     * Converts back from message stream to normal stream
     * @param message
     */
    public ActualMessage(byte[] message) {
        byte[] message_length_bytes = new byte[4];
        for(int i=0;i<4;i++){
            message_length_bytes[i] = message[i];
        }
        this.message_length = this.convertByteArrayToInt(message_length_bytes);
        this.message_type = message[4];
        setPayload(Arrays.copyOfRange(message,5, message_length));
    }

    public  ActualMessage(byte[] message_length, byte[] payload){
        this.message_length = this.convertByteArrayToInt(message_length);
        this.message_type = payload[0];
        setPayload(Arrays.copyOfRange(payload,1, this.message_length));
    }

    public int convertByteArrayToInt(byte[] int_chunk){
        return new BigInteger(int_chunk).intValue();
    }

    public void setMessage_type(byte num) throws Exception {
        if(num< 0 || num >7) {
            peerLogging.genericErrorLog("Invalid Message Type");
        }
        this.message_type = num;
    }
    public int getMessage_length() {
        return this.message_length;
    }

    public int getMessage_type() {
        return this.message_type;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    public byte[] getEncodedMessage() throws IOException {
        // TODO: look for a better logic if any
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        this.payload = new byte[0];
        outputStream.write(ActualMessageUtils.convertIntToByteArray(this.message_length));
        outputStream.write(message_type);
        outputStream.write(payload);
        return  outputStream.toByteArray();
    }
    public static void main(String args[]) throws Exception {
    }
}
