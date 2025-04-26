package kz.ramiyel.clupdb.config;

import kz.ramiyel.clupdb.generator.DBGenerator;
import kz.ramiyel.clupdb.manager.DB;
import org.reflections.Reflections;

import javax.sql.DataSource;
import java.util.Objects;

public class DBConfig {

    public DBConfig(DataSource dataSource, Reflections reflections, boolean generateOnRunning) {
        this(dataSource, reflections, null, 100, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, int batchSize, boolean generateOnRunning) {
        this(dataSource, reflections, null, batchSize, generateOnRunning);
    }

    public DBConfig(DataSource dataSource, Reflections reflections, String schema, boolean generateOnRunning) {
        this(dataSource, reflections, schema, 100, generateOnRunning);
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

}
