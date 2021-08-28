package fr.flashback083.flashspec;


import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;


public class TimerApi {

    private static final List<Task> expiredTasks = new ArrayList<>();
    public static List<Task> tasks = new ArrayList<>();

    static void removeExpiredTasks(){
        tasks.removeAll(expiredTasks);
        expiredTasks.clear();
    }

    protected static void addTask(@Nonnull Task task){
        tasks.add(task);
    }
}
