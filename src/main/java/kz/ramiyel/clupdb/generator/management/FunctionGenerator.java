package kz.ramiyel.clupdb.generator.management;

import kz.ramiyel.clupdb.annotation.DBFunction;

public class FunctionGenerator {


    public String generate(DBFunction function) {
        String params = String.join(", ", function.parameters());
        String header = "CREATE OR REPLACE FUNCTION";
        return String.format(
                "%s %s(%s)\nRETURNS %s AS $$\n%s\n$$ LANGUAGE %s;",
                header,
                function.name(),
                params,
                function.returnType(),
                function.body(),
                function.language()
        );
    }

}
