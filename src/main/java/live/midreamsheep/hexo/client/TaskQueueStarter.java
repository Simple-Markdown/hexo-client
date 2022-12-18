package live.midreamsheep.hexo.client;

import live.midreamsheep.hexo.netapi.message.queue.QueueApi;
import live.midreamsheep.hexo.netapi.message.queue.Task;

public class TaskQueueStarter {
    public static void start(){
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (!QueueApi.isEmpty()) {
                    Task task = QueueApi.getTask();
                    task.getHandler().handle(task.getDatas());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
