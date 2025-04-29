package kz.ramiyel.clupdb.generator.management;

import kz.ramiyel.clupdb.annotation.DBProcedure;

public class ProcedureGenerator {

    public String generate(DBProcedure procedure) {
        String params = String.join(", ", procedure.parameters());
        String header = "CREATE OR REPLACE PROCEDURE";
        return String.format(
                "%s %s(%s)\nAS $$\n%s\n$$ LANGUAGE %s;",
                header,
                procedure.name(),
                params,
                procedure.body(),
                procedure.language()
        );
    }

}
