package za.co.brightcat.tools.workspace;

import java.util.ArrayList;
import java.util.List;

public class SequencesTask implements Task {
    private List<Task> taskList = new ArrayList<>();
    
    public SequencesTask(Task task) {
        add(task);
    }
    
    public void add(Task task) {
        taskList.add(task);
    }

    @Override
    public void run() {
        System.out.println(getClass().getName());
        taskList.forEach(t -> {
            System.out.print("\t");
            t.run();
        });
    }
}
