package za.co.brightcat.tools.workspace;

import za.co.brightcat.tools.workspace.bilrost.JmsConnectionFactoryTask;
import za.co.brightcat.tools.workspace.bilrost.JmsQueuesTask;
import za.co.brightcat.tools.workspace.bilrost.InstallWebSphereTask;
import za.co.brightcat.tools.workspace.bilrost.JdbcDatasourceTask;

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
        JmsQueuesTask jmsSetupTask = new JmsQueuesTask("C:\\dev\\wildfly-10.1.0.Final\\bin\\jboss-cli.bat");
        ParallelTask setupTasks = new ParallelTask(jmsSetupTask, new JdbcDatasourceTask(), new JmsConnectionFactoryTask());
        
        sequencesTask.add(setupTasks);
        
        sequencesTask.run();
    }
}
