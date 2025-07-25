package kz.ramiyel.clupdb.config;

import kz.ramiyel.clupdb.base.GenericDataSource;
import kz.ramiyel.clupdb.constants.PropertyConstants;
import kz.ramiyel.clupdb.generator.DBGenerator;
import kz.ramiyel.clupdb.manager.DB;
import kz.ramiyel.clupdb.util.StringUtil;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

public class DBConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DBConfig.class);

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
        try {
            if (StringUtil.isEmpty(PropertyConstants.getDatasourceDriverClassName())) {
                throw new IllegalArgumentException("Datasource driver class name is not set!");
            }
            if (StringUtil.isEmpty(PropertyConstants.getDatasourceUrl())) {
                throw new IllegalArgumentException("Datasource url is not set!");
            }
            if (StringUtil.isEmpty(PropertyConstants.getDatasourceUrl())) {
                throw new IllegalArgumentException("Datasource username is not set!");
            }
            if (StringUtil.isEmpty(PropertyConstants.getDatasourceUrl())) {
                throw new IllegalArgumentException("Datasource password is not set!");
            }
            return new GenericDataSource(
                    PropertyConstants.getDatasourceDriverClassName(),
                    PropertyConstants.getDatasourceUrl(),
                    PropertyConstants.getDatasourceUsername(),
                    PropertyConstants.getDatasourcePassword()
            );
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

}
