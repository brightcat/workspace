package za.co.brightcat.tools.workspace.bilrost.wildfly;

import java.util.Arrays;

public class JmsQueue {
    private final String queueAddress;
    private final String[] entries;

    public JmsQueue(String queueAddress, String... entries) {
        this.queueAddress = queueAddress;
        this.entries = entries;
    }

    public String getQueueAddress() {
        return queueAddress;
    }

    public String[] getEntries() {
        return Arrays.copyOf(entries, entries.length);
    }

    @Override
    public String toString() {
        return "JmsQueue{" + "queueAddress=" + queueAddress + ", entries=" + Arrays.toString(entries) + '}';
    }
}
