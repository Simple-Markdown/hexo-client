package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.net.Connector;
import live.midreamsheep.hexo.client.net.NetToolApi;

import java.io.IOException;
import java.nio.ByteBuffer;

public class DeleteFile implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        try {
            NetToolApi.sendMeta(HandlerEnum.DELETE_FILE.getId(),data.length);
            Connector.socketChannel.write(ByteBuffer.wrap(data));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
