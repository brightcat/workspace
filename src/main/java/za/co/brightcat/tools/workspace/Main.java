package za.co.brightcat.tools.workspace;

import za.co.brightcat.tools.workspace.wildfly.JmsConnectionFactoryTask;
import za.co.brightcat.tools.workspace.wildfly.JmsQueuesTask;
import za.co.brightcat.tools.workspace.wildfly.InstallWebSphereTask;
import za.co.brightcat.tools.workspace.wildfly.JdbcDatasourceTask;
import za.co.brightcat.tools.workspace.wildfly.JmsQueue;
import za.co.brightcat.tools.workspace.wildfly.ServerHandle;
import za.co.brightcat.tools.workspace.wildfly.cli.CliServerHandle;

public class Main implements Runnable {
    public static void main(String[] args) {
        new Main().run();
    }

    @Override
    public void run() {
        SequencesTask sequencesTask = new SequencesTask(new InstallWebSphereTask());
        final ServerHandle serverHandle = serverHandle();
        JmsQueuesTask jmsSetupTask = new JmsQueuesTask(serverHandle, testJmsQueues());
        ParallelTask setupTasks = new ParallelTask(jmsSetupTask, new JdbcDatasourceTask(), new JmsConnectionFactoryTask());
        
        sequencesTask.add(setupTasks);
        
        sequencesTask.run();
    }
    
    private ServerHandle serverHandle() {
        return new CliServerHandle(jbossCliCommand());
    }
    
    private String jbossCliCommand() {
        return "c:\\dev\\wildfly-10.1.0.Final\\bin\\jboss-cli.bat";
    }
    
    private JmsQueue[] testJmsQueues() {
        final JmsQueue queue1 = new JmsQueue("Test1", "jms/test/Test1a", "jms/test/Test1b");
        final JmsQueue queue2 = new JmsQueue("Test2", "jms/test/Test2a", "jms/test/Test2b", "jms/test/Test2c");
        final JmsQueue queue3 = new JmsQueue("Test3", "jms/test/Test3");
        
        return new JmsQueue[] {
            queue1,
            queue2,
            queue3
        };
    }
}
