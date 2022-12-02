package live.midreamsheep.hexo.client.message.queue;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueApi {
    public static final LinkedBlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    public static void addTask(Task task){
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static Task getTask(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isEmpty(){
        return queue.isEmpty();
    }
}
