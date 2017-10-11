package za.co.brightcat.tools.workspace.wildfly;

import za.co.brightcat.tools.workspace.Task;

public class JdbcDatasourceTask implements Task {
    private final ServerHandle handle;
    private final Datasource[] datasources;

    public JdbcDatasourceTask(ServerHandle handle, Datasource... datasources) {
        this.handle = handle;
        this.datasources = datasources;
    }

    @Override
    public void run() {
        handle.addDatasources(datasources);
    }
}
