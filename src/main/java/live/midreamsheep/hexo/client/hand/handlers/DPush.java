package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerInter;
import live.midreamsheep.hexo.client.hand.HandlerMapper;
import live.midreamsheep.hexo.client.net.NetToolApi;

import java.io.IOException;

public class DPush implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        //对比本地文件数据
        HandlerMapper.handlerMap.get(HandlerEnum.PUSH.getId()).handle(data);
        try {
            NetToolApi.sendMeta(HandlerEnum.DPUSH.getId(),data.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
