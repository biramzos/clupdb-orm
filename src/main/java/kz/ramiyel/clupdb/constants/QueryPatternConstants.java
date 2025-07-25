package kz.ramiyel.clupdb.constants;

import java.util.regex.Pattern;

public class QueryPatternConstants {

    public static final Pattern COUNT_OVER_PATTERN =
            Pattern.compile("(?i)count\\s*\\(\\s*\\*\\s*\\)\\s*over\\s*(\\([^)]*\\))?(\\s+as\\s+\\w+)?");
    public static final Pattern LIMIT_PATTERN =
            Pattern.compile("(?i)\\blimit\\b\\s*\\d+(\\s*,\\s*\\d+)?(\\s+offset\\s*\\d+)?");

}
