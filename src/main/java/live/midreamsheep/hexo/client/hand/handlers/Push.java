package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.net.NetToolApi;

import java.io.IOException;

public class Push implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        try {
            NetToolApi.sendMeta(HandlerEnum.PUSH.getId(),data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
