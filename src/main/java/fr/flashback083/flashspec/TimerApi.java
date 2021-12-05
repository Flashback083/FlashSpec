package fr.flashback083.flashspec;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TimerApi {

    private static final List<Task> expiredTasks = new ArrayList<>();
    public static List<Task> tasks = new ArrayList<>();

    static void removeExpiredTasks(){
        Thread offthread = new Thread(() -> {
            tasks.removeAll(expiredTasks);
            expiredTasks.clear();
        });
        offthread.start();
        /*Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();

            task.tick();

            if (task.isExpired()) {
                iterator.remove();
            }
        }*/
    }

    protected static void addTask(@Nonnull Task task){
        tasks.add(task);
    }
}
