package kz.ramiyel.clupdb.config;

import kz.ramiyel.clupdb.base.GenericDataSource;
import kz.ramiyel.clupdb.constants.PropertyConstants;
import kz.ramiyel.clupdb.generator.DBGenerator;
import kz.ramiyel.clupdb.manager.DB;
import org.reflections.Reflections;
import javax.sql.DataSource;
import java.util.Objects;

public class DBConfig {

    public DBConfig() {
        this(getDatasource(), new Reflections(PropertyConstants.getReflectionsPackage()), PropertyConstants.getDatasourceDatabaseSchema(),
                PropertyConstants.getDatasourceBatchSize(), PropertyConstants.getDatasourceGenerateOnRunning());
    }

    public DBConfig(boolean generateOnRunning) {
        this(getDatasource(), new Reflections(PropertyConstants.getReflectionsPackage()), PropertyConstants.getDatasourceDatabaseSchema(),
                PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(String schema, boolean generateOnRunning) {
        this(getDatasource(), new Reflections(PropertyConstants.getReflectionsPackage()), schema,
                PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(String schema, int batchSize, boolean generateOnRunning) {
        this(getDatasource(), new Reflections(PropertyConstants.getReflectionsPackage()), schema,
                batchSize, generateOnRunning);
    }

    public DBConfig(Reflections reflections) {
        this(getDatasource(), reflections, PropertyConstants.getDatasourceDatabaseSchema(),
                PropertyConstants.getDatasourceBatchSize(), PropertyConstants.getDatasourceGenerateOnRunning());
    }

    public DBConfig(Reflections reflections, boolean generateOnRunning) {
        this(getDatasource(), reflections, PropertyConstants.getDatasourceDatabaseSchema(),
                PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(Reflections reflections, String schema, boolean generateOnRunning) {
        this(getDatasource(), reflections, schema,
                PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(Reflections reflections, String schema, int batchSize, boolean generateOnRunning) {
        this(getDatasource(), reflections, schema, batchSize, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, boolean generateOnRunning) {
        this(dataSource, reflections, PropertyConstants.getDatasourceDatabaseSchema(),
                PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, int batchSize, boolean generateOnRunning) {
        this(dataSource, reflections, PropertyConstants.getDatasourceDatabaseSchema(), batchSize, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, String schema, boolean generateOnRunning) {
        this(dataSource, reflections, schema, PropertyConstants.getDatasourceBatchSize(), generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, String schema, int batchSize, boolean generateOnRunning) {
        DB.setDataSource(dataSource);
        if (generateOnRunning) {
            DBGenerator generator;
            if (Objects.isNull(schema)) {
                generator = new DBGenerator(reflections);
            } else {
                generator = new DBGenerator(reflections, schema, batchSize);
            }
            generator.execute();
        }
    }

    private static DataSource getDatasource() {
        return new GenericDataSource(
                PropertyConstants.getDatasourceDriverClassName(),
                PropertyConstants.getDatasourceUrl(),
                PropertyConstants.getDatasourceUsername(),
                PropertyConstants.getDatasourcePassword()
        );
    }

}
