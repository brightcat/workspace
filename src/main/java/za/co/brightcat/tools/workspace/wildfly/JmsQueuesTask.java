package za.co.brightcat.tools.workspace.wildfly;

import za.co.brightcat.tools.workspace.Task;

public class JmsQueuesTask implements Task {
    private final JmsQueue[] queues;
    private final ServerHandle serverHandle;

    public JmsQueuesTask(ServerHandle serverHandle, JmsQueue... queues) {
        if (queues.length == 0) {
            throw new IllegalArgumentException("At least 1 JmsQueue required");
        }
        this.serverHandle = serverHandle;
        this.queues = queues;
    }
    
    @Override
    public void run() {
        serverHandle.addJmsQueues(queues);
    }
}
