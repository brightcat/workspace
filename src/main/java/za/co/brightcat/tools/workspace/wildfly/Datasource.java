package za.co.brightcat.tools.workspace.wildfly;

import java.util.Arrays;

public class Datasource {
    private final String name;
    private final String jndi;
    private final String url;
    private final String driver;

    public Datasource(String name, String jndi, String url, String driver) {
        this.name = name;
        this.jndi = jndi;
        this.url = url;
        this.driver = driver;
    }

    public String getName() {
        return name;
    }

    public String getJndi() {
        return jndi;
    }

    public String getUrl() {
        return url;
    }

    public String getDriver() {
        return driver;
    }

    @Override
    public String toString() {
        return "Datasource{" + "name=" + name + ", jndi=" + jndi + ", url=" + url + ", driver=" + driver + '}';
    }
}
