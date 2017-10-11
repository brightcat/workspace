package za.co.brightcat.tools.workspace;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import za.co.brightcat.tools.workspace.wildfly.Datasource;
import za.co.brightcat.tools.workspace.wildfly.JmsConnectionFactoryTask;
import za.co.brightcat.tools.workspace.wildfly.JmsQueuesTask;
import za.co.brightcat.tools.workspace.wildfly.InstallWebSphereTask;
import za.co.brightcat.tools.workspace.wildfly.JdbcDatasourceTask;
import za.co.brightcat.tools.workspace.wildfly.JmsQueue;
import za.co.brightcat.tools.workspace.wildfly.ServerHandle;
import za.co.brightcat.tools.workspace.wildfly.cli.CliServerHandle;

public class Main implements Runnable {
    public static void main(String[] args) throws IOException {
        new Main().run();
    }
    private final Properties properties;

    public Main() throws IOException {
        this.properties = properties();
    }
    
    private Properties properties() throws IOException {
        final Properties props = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try (InputStream is = loader.getResourceAsStream("application.properties")) {
            props.load(is);
        }
        return props;
    }

    @Override
    public void run() {
        final SequencesTask sequencesTask = new SequencesTask(new InstallWebSphereTask());
        final ServerHandle serverHandle = serverHandle();
        final JmsQueuesTask jmsSetupTask = new JmsQueuesTask(serverHandle, testJmsQueues());
        final JdbcDatasourceTask datasourcesTask = new JdbcDatasourceTask(serverHandle, testDatasources());
        final ParallelTask setupTasks = new ParallelTask(jmsSetupTask, datasourcesTask, new JmsConnectionFactoryTask());
        
        sequencesTask.add(setupTasks);
        
        sequencesTask.run();
    }
    
    private ServerHandle serverHandle() {
        return new CliServerHandle(jbossCliCommand());
    }
    
    private String jbossCliCommand() {
        return properties.getProperty("app.wildfly.cli");
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

    private Datasource[] testDatasources() {
        return new Datasource[] {
            new Datasource("TestSource1", "java:/jdbc/test/source1", "jdbc:h2:mem:source1", "h2"),
            new Datasource("TestSource2", "java:/jdbc/test/source2", "jdbc:h2:mem:source1", "h2")
        };
    }
}
