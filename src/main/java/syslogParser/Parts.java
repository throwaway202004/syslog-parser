package syslogParser;

/**
 * This enum type describes the different parts
 * of the syslog format (according to RFC3164).
 */
enum Parts {
    PRIORITY(1),
    MONTH(2),
    DATE(3),
    HOURS(4),
    MINUTES(5),
    SECONDS(6),
    HOST(7),
    PROCESS(8),
    PID(9),
    MESSAGE(10);

    private final int matchGroup;

    Parts(int i) {
        this.matchGroup = i;
    }

    /**
     * This method takes a java.util.regex.Matcher and returns the
     * value of the match group with an index indicated by the enum value.
     * var value = Parts.PROCESS.match(matcher)
     * Value will contain the value of the 8th (PROCESS) match group, i.e the process name.
     * @param matcher
     * @return String
     */
    String match(java.util.regex.Matcher matcher) {
        return matcher.group(matchGroup);
    }
}
