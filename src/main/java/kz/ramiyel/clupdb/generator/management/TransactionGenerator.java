package kz.ramiyel.clupdb.generator.management;

import kz.ramiyel.clupdb.annotation.DBTransaction;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TransactionGenerator {

    public String generate(DBTransaction transaction) {
        String blockName = transaction.name();
        String statements = Arrays.stream(transaction.statements())
                .collect(Collectors.joining(";\n    ", "    ", ";"));
        return String.format(
                "-- Transaction Block: %s\nBEGIN\n%s\nCOMMIT;",
                blockName,
                statements
        );
    }

}
