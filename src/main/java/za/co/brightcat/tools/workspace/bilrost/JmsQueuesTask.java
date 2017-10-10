package za.co.brightcat.tools.workspace.bilrost;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.brightcat.tools.workspace.Task;
import za.co.brightcat.tools.workspace.bilrost.wildfly.JmsQueue;

public class JmsQueuesTask implements Task {

    private final String jbossCliCommand;
    private final JmsQueue[] queues;

    public JmsQueuesTask(String jbossCliCommand, JmsQueue... queues) {
        if (queues.length == 0) {
            throw new IllegalArgumentException("At least 1 JmsQueue required");
        }
        this.jbossCliCommand = jbossCliCommand;
        this.queues = queues;
    }
    
    @Override
    public void run() {
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
            Logger.getLogger(JmsQueuesTask.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(JmsQueuesTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void log(String m) {
        System.out.println(new Date() + "\t" + Thread.currentThread().getName() + "\t" + m);
    }
}
