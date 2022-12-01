package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.net.Connector;

import java.io.IOException;
import java.nio.ByteBuffer;

public class AddDir implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        System.out.println(new String(data));
        byte[] bytes = new byte[5];
        bytes[0] = (byte) HandlerEnum.ADD_DIR.getId();
        int length = data.length;
        bytes[1] = (byte) (length >> 24);
        bytes[2] = (byte) (length >> 16);
        bytes[3] = (byte) (length >> 8);
        bytes[4] = (byte) (length);
        try {
            Connector.socketChannel.write(ByteBuffer.wrap(bytes));
            Connector.socketChannel.write(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
