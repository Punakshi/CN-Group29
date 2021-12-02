package uf.cs.cn.peer;

import uf.cs.cn.utils.CommonConfigFileReader;
import uf.cs.cn.utils.FileSplitter;
import uf.cs.cn.utils.PeerInfoConfigFileReader;

import java.net.ServerSocket;
import java.nio.file.Paths;

public class PeerServer extends Thread {
    private ServerSocket serverSocket;
    private int self_port;
    private int self_peer_id;

    public PeerServer(int self_port, int self_peer_id) {
        this.self_port = self_port;
        this.self_peer_id = self_peer_id;
    }

    /**
     * We split the file into the pieces.
     */
    private void splitFileIntoChunks(){
        String running_dir = System.getProperty("user.dir"); // gets the base directory of the project
        String peer_id = String.valueOf(self_peer_id);
        FileSplitter.splitFile(
                Paths.get(running_dir, peer_id, CommonConfigFileReader.file_name).toString(),
                Paths.get(running_dir, peer_id).toString());
    }

    public void run() {
        serverSocket = null;
        try {
            /**
             * Starting the server at the {@link PeerServer#self_port}.
             * Server means a connection which is duplex and waiting for the other peer to get connected.
             */
            serverSocket = new ServerSocket(self_port);
            boolean is_server = false;
            for(PeerInfoConfigFileReader.PeerInfo peerInfo: PeerInfoConfigFileReader.getPeerInfoList()){
                /**
                 * This line will only iterate all elements until it reaches itself. Point of doing this is to connect
                 * only to the peers who are already in the network. Assuming we run the peers as per thier index numbers
                 * and the file is in sorted index number list, we are connecting to the ones who are already running.
                 */
                if(Peer.getPeerId() == peerInfo.getPeer_id() && peerInfo.isHas_file()){
                    is_server = true;
                }
            }
            if(is_server) {
                splitFileIntoChunks();
            }

            /**
             * We wait for a total of n-1 connections, as there are n peers in the networks, we wait for each peer other
             * then us to connect to us.
             */
            int counter = 0;
            while (counter != PeerInfoConfigFileReader.getPeerInfoList().size() - 1) {
                IncomingConnectionHandler connHandler = new IncomingConnectionHandler(serverSocket.accept(), this.self_peer_id);
                connHandler.start();
                counter++;
            }
        } catch (Exception e) {
            // TODO: handle exception here
            e.printStackTrace();
        }
    }
}