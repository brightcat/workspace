package za.co.brightcat.tools.workspace.bilrost;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.brightcat.tools.workspace.Task;

public class JmsQueuesTask implements Task {

    public JmsQueuesTask() {
    }

    @Override
    public void run() {
        final String[] commands = {
            "D:/dev/wildfly-10.1.0.Final/bin/jboss-cli.bat",
            "--connect",
            "--commands=\"jms-queue add --queue-address=TesterClixx --entries=java:/jms/TesterClixx\""
        };
        ProcessBuilder pb = new ProcessBuilder(commands);
        try {
            Process proc = pb.start();
            
            new Thread(() -> {
                
//                readAll(proc.getInputStream());
                InputStream is = proc.getErrorStream();

                readAll(is);
            }, "jms-queue-thread").start();

        } catch (IOException ex) {
            Logger.getLogger(JmsQueuesTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void readAll(InputStream is) {
        try {
            byte[] buffer = new byte[4096];
            int read = is.read(buffer);
            while (read > -1) {
                System.out.println(Thread.currentThread().getName() + "\t" + new String(buffer, 0, read));
                read = is.read(buffer);
            }
        } catch (IOException ex) {
            Logger.getLogger(JmsQueuesTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
