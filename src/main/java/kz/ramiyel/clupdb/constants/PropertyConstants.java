package kz.ramiyel.clupdb.constants;

public class PropertyConstants {
    private static final String DATASOURCE_URL = "spring.datasource.url";
    private static final String DATASOURCE_USERNAME = "spring.datasource.username";
    private static final String DATASOURCE_PASSWORD = "spring.datasource.password";
    private static final String DATASOURCE_DRIVER_CLASS_NAME = "spring.datasource.driver-class-name";
    private static final String DATASOURCE_DATABASE_SCHEMA = "spring.datasource.database.schema";
    private static final String DATASOURCE_BATCH_SIZE = "spring.datasource.batch-size";
    private static final String DATASOURCE_GENERATE_ON_RUNNING = "spring.datasource.generate-on-running";
    private static final String DATASOURCE_SHOW_SQL_QUERY = "spring.datasource.show-sql-query";
    private static final String REFLECTIONS_PACKAGE = "spring.reflection.package";

    public static String getDatasourceUrl() {
        return System.getProperty(DATASOURCE_URL);
    }

    public static String getDatasourceUsername() {
        return System.getProperty(DATASOURCE_USERNAME);
    }

    public static String getDatasourcePassword() {
        return System.getProperty(DATASOURCE_PASSWORD);
    }

    public static String getDatasourceDriverClassName() {
        return System.getProperty(DATASOURCE_DRIVER_CLASS_NAME);
    }

    public static String getDatasourceDatabaseSchema() {
        return System.getProperty(DATASOURCE_DATABASE_SCHEMA);
    }

    public static int getDatasourceBatchSize() {
        return Integer.parseInt(System.getProperty(DATASOURCE_BATCH_SIZE, "100"));
    }

    public static boolean getDatasourceGenerateOnRunning() {
        return Boolean.parseBoolean(System.getProperty(DATASOURCE_GENERATE_ON_RUNNING, "false"));
    }

    public static boolean getDatasourceShowSqlQuery() {
        return Boolean.parseBoolean(System.getProperty(DATASOURCE_SHOW_SQL_QUERY, "false"));
    }

    public static String getReflectionsPackage() {
        return System.getProperty(REFLECTIONS_PACKAGE);
    }
}
