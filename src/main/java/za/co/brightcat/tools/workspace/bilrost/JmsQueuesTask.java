package za.co.brightcat.tools.workspace.bilrost;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.brightcat.tools.workspace.Task;

public class JmsQueuesTask implements Task {

    public JmsQueuesTask(String jbossCliCommand) {
        this.jbossCliCommand = jbossCliCommand;
    }
    private final String jbossCliCommand;
    
    
    @Override
    public void run() {
        final String[] commands = {
            jbossCliCommand,
            "--connect",
            "--commands=jms-queue add --queue-address=TesterClixx --entries=jms/TesterClixx"
        };
        ProcessBuilder pb = new ProcessBuilder(commands);
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
