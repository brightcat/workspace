package za.co.brightcat.tools.workspace;

public interface Task extends Runnable {
    @Override
    default void run() {
        System.out.println(this.getClass().getName());
    }
}
