package live.midreamsheep.hexo.client;

import live.midreamsheep.command.control.CommandManager;
import live.midreamsheep.hexo.client.hand.HandlerEnum;
import live.midreamsheep.hexo.client.hand.HandlerMapper;

public class CommandStarter {
    public static void start(){
        CommandManager.addController("dpush",(a) ->{
            HandlerMapper.handlerMap.get(HandlerEnum.DPUSH.getId()).handle(new byte[0]);
        });
        CommandManager.addController("pull",(a) ->{
            HandlerMapper.handlerMap.get(HandlerEnum.PULL.getId()).handle(new byte[0]);
        });
        CommandManager.addController("push",(a)->{
            HandlerMapper.handlerMap.get(HandlerEnum.PUSH.getId()).handle(new byte[0]);
        });
        CommandManager.addController("server",(a)->{
            HandlerMapper.handlerMap.get(HandlerEnum.SERVER.getId()).handle(new byte[0]);
        });
        CommandManager.addController("stop",(a)->{
            HandlerMapper.handlerMap.get(HandlerEnum.SERVER_STOP.getId()).handle(new byte[0]);
        });
    }
}
