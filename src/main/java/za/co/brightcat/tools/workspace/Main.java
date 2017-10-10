package za.co.brightcat.tools.workspace;

import za.co.brightcat.tools.workspace.bilrost.JmsConnectionFactoryTask;
import za.co.brightcat.tools.workspace.bilrost.JmsQueuesTask;
import za.co.brightcat.tools.workspace.bilrost.InstallWebSphereTask;
import za.co.brightcat.tools.workspace.bilrost.JdbcDatasourceTask;
import za.co.brightcat.tools.workspace.bilrost.wildfly.JmsQueue;

public class Main {
    public static void main(String[] args) {
        /**
         * Install web sphere
         * 
         * Setup JMS queues
         * Setup JMS queue connection factories
         * Setup JDBC datasources
         */
        SequencesTask sequencesTask = new SequencesTask(new InstallWebSphereTask());
        final JmsQueue queue1 = new JmsQueue("Test1", "jms/test/Test1a", "jms/test/Test1b");
        final JmsQueue queue2 = new JmsQueue("Test2", "jms/test/Test2a", "jms/test/Test2b");
        final JmsQueue queue3 = new JmsQueue("Test3", "jms/test/Test3");
        JmsQueuesTask jmsSetupTask = new JmsQueuesTask("d:\\dev\\wildfly-10.1.0.Final\\bin\\jboss-cli.bat", queue1, queue2, queue3);
        ParallelTask setupTasks = new ParallelTask(jmsSetupTask, new JdbcDatasourceTask(), new JmsConnectionFactoryTask());
        
        sequencesTask.add(setupTasks);
        
        sequencesTask.run();
    }
}
