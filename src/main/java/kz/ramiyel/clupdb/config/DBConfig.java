package kz.ramiyel.clupdb.config;

import kz.ramiyel.clupdb.generator.DBGenerator;
import kz.ramiyel.clupdb.manager.DB;

import javax.sql.DataSource;
import java.util.Objects;

public class DBConfig {

    public DBConfig(DataSource dataSource, boolean generateOnRunning) {
        this(dataSource, null, 100, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, int batchSize, boolean generateOnRunning) {
        this(dataSource, null, batchSize, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, String schema, boolean generateOnRunning) {
        this(dataSource, schema, 100, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, String schema, int batchSize, boolean generateOnRunning) {
        DB.setDataSource(dataSource);
        if (generateOnRunning) {
            DBGenerator generator;
            if (Objects.isNull(schema)) {
                generator = new DBGenerator();
            } else {
                generator = new DBGenerator(schema, batchSize);
            }
            generator.execute();
        }
    }

}
