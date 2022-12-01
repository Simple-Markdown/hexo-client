package live.midreamsheep.hexo.client.hand;

import live.midreamsheep.hexo.client.hand.handlers.AddDir;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {
    public static final Map<Integer,HandlerInter> handlerMap = new HashMap<>();
    static {
        handlerMap.put(HandlerEnum.ADD_DIR.getId(),new AddDir());
/*        handlerMap.put(HandlerEnum.ADD_FILE.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");
        handlerMap.put(HandlerEnum.PULL.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");
        handlerMap.put(HandlerEnum.PUSH.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");
        handlerMap.put(HandlerEnum.DELETE_DIR.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");
        handlerMap.put(HandlerEnum.UPDATE_FILE.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");
        handlerMap.put(HandlerEnum.DELETE_FILE.getId(),"live.midreamsheep.hexo.client.hand.LoginHandler");*/
    }
}
