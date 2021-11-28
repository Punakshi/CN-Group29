package uf.cs.cn.utils;

import uf.cs.cn.message.ActualMessage;
import uf.cs.cn.message.MessageType;
import uf.cs.cn.peer.Peer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class MessageParser {
//    static PeerLogging logger = new PeerLogging();

    public static void parse(ActualMessage actualMessage, int client_peer_id) throws IOException {

        System.out.println("Received a message of type " + actualMessage.getMessage_type());

        switch (actualMessage.getMessage_type()) {
            case MessageType.UN_CHOKE:
                break;

            case MessageType.CHOKE:
                break;

            case MessageType.INTERESTED:
                break;

            case MessageType.NOT_INTERESTED:
                break;

            case MessageType.HAVE:
                break;

            case MessageType.BIT_FIELD:
                System.out.println(actualMessage.getMessage_type() + " " + Arrays.toString(actualMessage.getEncodedMessage()) + " " + client_peer_id);
                // update peer memory
                Peer.getInstance().updateNeighbourFileChunk(client_peer_id, BitFieldUtils.convertToBoolArray(actualMessage.getPayload()));
                // trigger sending the interested message event
                if (Peer.getInstance().checkIfInterested(client_peer_id)) Peer.sendInterested(client_peer_id);
                else Peer.sendNotInterested(client_peer_id);
                break;

            case MessageType.REQUEST:
                break;

            case MessageType.PIECE:
                Peer.getInstance().incrementDownloadCount(client_peer_id);
                if (Peer.getInstance().gotCompleteFile()) {
                    String running_dir = System.getProperty("user.dir"); // gets the base directory of the project
                    String peer_id = String.valueOf(Peer.getInstance().getSelf_peer_id());
                    FileMerger.mergeFile(
                            Paths.get(running_dir, peer_id, CommonConfigFileReader.file_name).toString(),
                            Paths.get(running_dir, peer_id).toString());

//                    logger.downloadingCompleteLog();
                }
                break;

            default:
//                logger.genericErrorLog("Wrong Message Type received - Cannot parse");
                System.out.println("Some other type of message is coming");
        }

    }
}
