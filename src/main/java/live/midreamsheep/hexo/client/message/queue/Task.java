package live.midreamsheep.hexo.client.message.queue;

import live.midreamsheep.hexo.client.hand.HandlerInter;

public class Task {
    private byte[] datas;

    private HandlerInter handler;

    public Task(byte[] datas, HandlerInter handler) {
        this.datas = datas;
        this.handler = handler;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    public HandlerInter getHandler() {
        return handler;
    }

    public void setHandler(HandlerInter handler) {
        this.handler = handler;
    }
}
