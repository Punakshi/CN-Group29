package uf.cs.cn.message;

import uf.cs.cn.peer.Peer;
import uf.cs.cn.utils.ActualMessageUtils;
import uf.cs.cn.utils.CommonConfigFileReader;
import uf.cs.cn.utils.FileMerger;
import uf.cs.cn.utils.PeerInfoConfigFileReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class PieceMessage extends ActualMessage {

    private int piece_index;
    private byte[] piece_bytes;

    /**
     * Will create a piece message and load the payload from disk
     */
    public PieceMessage(int piece_id) throws Exception {
        this.piece_index = piece_id;
        setMessage_type(MessageType.PIECE);
        // read the bytes of that piece id
        byte[] file_chunk;
        byte[] piece_index;
        byte[] payload = new byte[0];
        try (
                FileInputStream fileInputStream = new FileInputStream(
                        Paths.get(System.getProperty("user.dir"), Peer.getPeerId() + "", "piece_1").toString())
        ) {
            file_chunk = fileInputStream.readAllBytes();
            System.out.println("File size " + file_chunk.length);
            piece_index = ActualMessageUtils.convertIntToByteArray(piece_id);
            payload = new byte[4 + file_chunk.length];
            System.arraycopy(piece_index, 0, payload, 0, 4);
            System.arraycopy(file_chunk, 0, payload, 4, file_chunk.length);
            this.setPayload(payload);

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMessageLength(1 + payload.length);
    }
    // TODO: Testing

    public PieceMessage(byte[] message_length, byte[] payload) {
        super(message_length, payload);
        this.piece_index = convertByteArrayToInt(Arrays.copyOfRange(payload, 0, 4));
    }

    public PieceMessage(byte[] actualMessage){
        this(Arrays.copyOfRange(actualMessage, 0, 4),Arrays.copyOfRange(actualMessage, 4, actualMessage.length));
        this.piece_bytes = Arrays.copyOfRange(actualMessage, 4, actualMessage.length);
        processPieceMessage();
    }

    public int getPieceIndex() {
        return this.piece_index;
    }

    public byte[] getFileChunk() {
        return Arrays.copyOfRange(getPayload(), 4, getPayload().length);
    }

    public void processPieceMessage(){

        String running_dir = System.getProperty("user.dir"); // gets the base directory of the project
        String peer_id = String.valueOf(Peer.getInstance().getSelf_peer_id());

        FileMerger.mergeFile(
                Paths.get(running_dir, peer_id).toString(),
                Paths.get(running_dir, peer_id, "merged_file." + CommonConfigFileReader.file_extension).toString());

        try (FileOutputStream fos = new FileOutputStream(Paths.get(running_dir, peer_id + "/piece_"+ this.piece_index ).toString())) {
            fos.write(this.piece_bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
