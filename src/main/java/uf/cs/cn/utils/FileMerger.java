package uf.cs.cn.utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileMerger {

    private static String getPieceName(int num) {
        return "piece_" + num;
    }

    public static void mergeFile(String input_file_path, String out_put_path) {
        FileInputStream fileInputStream = null;
        byte[] buffer = new byte[CommonConfigFileReader.piece_size];
        try(FileOutputStream fileOutputStream = new FileOutputStream(new File(out_put_path));
        ) {

            // Total number of piece files would be
            int num_of_pieces = (int)Math.ceil(1.0*CommonConfigFileReader.file_size / CommonConfigFileReader.piece_size);
            int read_bytes;
            int total_bytes_written = 0;
            for(int i =1;i<= num_of_pieces;i++) {
                // read the piece_<i> file and buffer it out in the output_file
                fileInputStream = new FileInputStream(Paths.get(input_file_path,getPieceName(i)).toString());
                read_bytes = fileInputStream.read(buffer);
                total_bytes_written += read_bytes;
                fileOutputStream.write(buffer,0,read_bytes);
                fileOutputStream.flush();
            }
            System.out.println("bytes written "+total_bytes_written);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream!=null)
                    fileInputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        String running_dir = System.getProperty("user.dir"); // gets the base directory of the project
        String peer_id = String.valueOf(PeerInfoConfigFileReader.getPeerInfoList().get(0).getPeer_id());
        FileMerger.mergeFile(
                Paths.get(running_dir, peer_id).toString(),
                Paths.get(running_dir, peer_id, "merged_file.png").toString());
    }
}
