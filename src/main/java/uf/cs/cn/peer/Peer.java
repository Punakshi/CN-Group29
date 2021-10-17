package uf.cs.cn.peer;

import java.util.ArrayList;

/**
 * Peer represents a node in the P2P connection
 * A peer can act as a server, or as a client with other peers
 * It cannot act as a server and client with the same peer at the same time.
 */

public class Peer extends Thread{

    // TODO: read the port from the file instead of harcoding here
    private final int server_port = 3000;
    private ArrayList<Integer> neighbour_ids;
    // Handshake message will be common for client and server
    private ArrayList<PeerServer> peer_server = new ArrayList<>();
    public boolean is_server;

    public Peer(boolean is_server){
        this.is_server = is_server;
    }

    /**
     * Purpose of the function is to connect to all peers' servers
     * @param args
     */
    public void runClients(String[] args) {
        // TODO: Loop through all listening ports from config file and connect to peer's servers
        // TODO: read this peer's id from  a file
        int peer_id = 1000;
        // Store the object references when looping for future use
        PeerClient peerClient = new PeerClient("localhost", 3000, 1000);
        peerClient.start();
        //
    }

    /**
     * Purpose of the function is to listen to all incoming peer client requests
     * @param args
     */
    public void runServer(String[] args) {
        // read peer id from file
        int peer_id = 1000;
        try{
            peer_server.add(new PeerServer(server_port, peer_id));
            peer_server.get(peer_server.size()-1).start();
        } catch (Exception e) {
            // TODO: handle the exception
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if(is_server) {
            System.out.println("Starting server");
            this.runServer(new String[]{});
        } else {
            this.runClients(new String[]{});
            System.out.println("Starting client");
        }
    }

}
