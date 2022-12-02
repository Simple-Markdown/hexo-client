import live.midreamsheep.hexo.client.message.queue.QueueApi;
import live.midreamsheep.hexo.client.message.queue.Task;

import java.util.concurrent.LinkedBlockingQueue;

public class test {
    public static void main(String[] args) throws InterruptedException {
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
        thread.start();
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        Thread.sleep(3000);
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
        QueueApi.addTask(new Task("helloword".getBytes(), a-> System.out.println(System.currentTimeMillis()+new String(a))));
    }
}
