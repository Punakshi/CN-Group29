package uf.cs.cn.message;

import java.io.IOException;

public class InterestedMessage extends ActualMessage{

    InterestedMessage() throws Exception {
        super(4, (byte) 2);
    }

    public byte[] getInterestedMessageBytes(){
        try {
            return getEncodedMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
