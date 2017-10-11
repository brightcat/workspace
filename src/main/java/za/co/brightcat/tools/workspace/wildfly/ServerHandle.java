package za.co.brightcat.tools.workspace.wildfly;

public interface ServerHandle {
    void addJmsQueues(JmsQueue... queues);
    void addDatasources(Datasource... datasources);
}
