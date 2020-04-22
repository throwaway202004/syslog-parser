package syslogParser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class handles the actual parsing of the syslog entry.
 * To parse the syslog entry a regular expression is used.
 * The expected syslog format is as described in
 * RFC3164 (https://tools.ietf.org/html/rfc3164) and
 * if the entry cannot be parsed as such an exception will
 * be thrown.
 */
class MatchGroup {

    /**
     * This multiline string is only for documentation reasons
     * and to make it easier to see whats going on in the
     * regular expression.
     */
    final String regex = String.join(
            "",
            "^",                         // Beginning of line
            "(<\\d+>)?",                           // Priority (optional)
            "(\\w{3})\\s+",                        // Month
            "(\\d{1,2})\\s",                       // Date
            "(\\d{2}):(\\d{2}):(\\d{2})\\s+",      // HH:mm:ss
            "([\\w.-]+)?\\s+",                     // Host
            "([\\w\\-().\\d]+)",                   // Process
            "(?:\\[([a-z0-9-.]+)\\])?:\\s+",       // Process Id (optional)
            "(.*)",                                // Message
            "$"                                    // End of line
    );

    // TODO: Add unit test for this conversion

    /**
     * Map from Syslog month format (three letter abbreviation of month)
     * to a string parsable by Javas Month type.
     */
    final Map<String, String> months = Map.ofEntries(
            Map.entry("Jan", "JANUARY" ),
            Map.entry("Feb", "FEBRUARY" ),
            Map.entry("Mar", "MARS" ),
            Map.entry("Apr", "APRIL" ),
            Map.entry("May", "may" ),
            Map.entry("Jun", "JUNE" ),
            Map.entry("Jul", "JULY" ),
            Map.entry("Aug", "AUGUST" ),
            Map.entry("Sep", "SEPTEMBER" ),
            Map.entry("Oct", "OCTOBER" ),
            Map.entry("Nov", "NOVEMBER" ),
            Map.entry("Dec", "DECEMBER" )
    );

    private java.util.regex.Matcher matcher;
    private final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

    /**
     * Constructor that sets up the regex matcher and
     * initiates the regex engine to find matches.
     * @param stringToParse
     */
    MatchGroup(String stringToParse) {
        matcher = pattern.matcher(stringToParse);
        matcher.find();
    }

    /**
     * The number of match groups found by the matcher.
     * @return int
     */
    int getMatchesCount() {
        return matcher.groupCount();
    }

    /**
     * Parse the timestamp part of the Syslog entry. The timestamp is not
     * optional and must be parsable or an exception will be thrown.
     * @return LocalDateTime
     * @throws SyslogParseException
     */
    LocalDateTime getDateTime() throws SyslogParseException {
        try {
            Month month = Month.valueOf(months.get(Parts.MONTH.match(matcher)));
            int day = Integer.valueOf(Parts.DATE.match(matcher));
            int hours = Integer.valueOf(Parts.HOURS.match(matcher));
            int minutes = Integer.valueOf(Parts.MINUTES.match(matcher));
            int seconds = Integer.valueOf(Parts.SECONDS.match(matcher));

            return LocalDateTime.of(LocalDate.now().getYear(), month, day, hours, minutes, seconds);
        } catch (Exception ex) {
            throw new SyslogParseException("Could not parse timestamp" );
        }
    }

    /**
     * The priority of the syslog entry. This is optional and in case
     * of a missing priority -1 will be returned.
     * @return int
     */
    int getPriority() {
        var priority = Parts.PRIORITY.match((matcher));
        if (priority == null) return -1;
        priority = priority.replaceAll("[<|>]", "");
        return Integer.valueOf(priority);
    }

    /**
     * The hostname of IP address from the syslog entry. This is not optional
     * and if it cannot be parsed an exception will be thrown.
     * @return String
     * @throws SyslogParseException
     */
    String getHost() throws SyslogParseException {
        try {
            return Parts.HOST.match(matcher);
        } catch (Exception ex) {
            throw new SyslogParseException("Could not parse host name");
        }
    }

    /**
     * The process name from the syslog entry. This is not a optional
     * and if it cannot be parsed an exception will be thrown.
     * @return String
     * @throws SyslogParseException
     */
    String getProcess() throws SyslogParseException {
        try {
            return Parts.PROCESS.match(matcher);
        } catch (Exception ex) {
            throw new SyslogParseException("Could not parse process name");
        }
    }

    /**
     * The process id from the syslog entry. This is an optional value
     * and if it cannot be parsed -1 will be returned.
     * @return int
     */
    int getPid() {
        var pid = Parts.PID.match(matcher);
        if (pid == null) return -1;
        return Integer.valueOf(pid);
    }

    /**
     * Arbitrary message from the process creating the log entry.
     * @return String
     */
    String getMessage() {
        return Parts.MESSAGE.match(matcher);
    }
}
