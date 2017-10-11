package za.co.brightcat.tools.workspace.wildfly.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.brightcat.tools.workspace.wildfly.JmsQueue;
import za.co.brightcat.tools.workspace.wildfly.JmsQueuesTask;
import za.co.brightcat.tools.workspace.wildfly.ServerHandle;

public class CliServerHandle implements ServerHandle {
    private static final Logger LOGGER = Logger.getLogger(JmsQueuesTask.class.getName());
    private final String jbossCliCommand;

    public CliServerHandle(String jbossCliCommand) {
        this.jbossCliCommand = jbossCliCommand;
    }
    
    @Override
    public void addJmsQueues(JmsQueue... queues) {
        final String[] command = {
            jbossCliCommand,
            "--connect",
            ""
        };
                
        final String[] commands = new String[queues.length];
        int i = 0;
        for (JmsQueue queue : queues) {
            final String s = "jms-queue add --queue-address=" + queue.getQueueAddress() +" --entries=[" + String.join(",", queue.getEntries()) + "]";
            commands[i++] = s;
        }
        
        command[2] = "--commands=" + String.join(",", commands);
        
        ProcessBuilder pb = new ProcessBuilder(command);
        log(pb.command().toString());
        Map<String, String> environment = pb.environment();
        environment.put("NOPAUSE","true");
        try {
            Process proc = pb.start();
            toThread("jms-queue-stderr", proc.getErrorStream());
            toThread("jms-queue-stdin", proc.getInputStream());
            
            proc.waitFor(10, TimeUnit.SECONDS);
            int exitValue = proc.exitValue();
            log("Process exit value: " + exitValue);
        } catch (IOException | InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    private void toThread(String name, InputStream is) {
            Thread t = new Thread(() -> {
                readAll(is);
            }, name);
            t.setDaemon(true);
            t.start();
    }

    private void readAll(InputStream is) {
        try {
            byte[] buffer = new byte[4096];
            int read = is.read(buffer);
            while (read > -1) {
                log(new String(buffer, 0, read));
                if (is.available() < 0) break;
                read = is.read(buffer);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    
    private void log(String m) {
        LOGGER.log(Level.INFO, "{0}\t{1}", new Object[]{Thread.currentThread().getName(), m});
    }
}
