package live.midreamsheep.hexo.client.hand.handlers;

import live.midreamsheep.hexo.client.hand.HandlerInter;

public class UpdateAFile implements HandlerInter {
    @Override
    public void handle(byte[] data) {
        System.out.println(new String(data));
    }
}
