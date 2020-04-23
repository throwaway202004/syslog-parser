package sshdProcessParser;

import java.util.regex.Pattern;

/**
 * This class will attempt to parse the sshd output as taken from the
 * application specific part of the syslog.
 *
 * The following mapping is made:
 *  disconnect -> stop
 *  closed     -> stop
 *  Accepted   -> allowed
 *  opened     -> start
 *
 *  The reasoning for the mappings is:
 *  A connection that is disconnected or closed will STOP the service (for that user)
 *  If a password is accepted the user is ALLOWED to use the service
 *  A session that is opened will START the service
 */
public class MatchGroup {

    private final String regex = "(disconnect)|(closed)|(opened)|(Accepted)";
    private java.util.regex.Matcher matcher;
    private final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

    /**
     * Constructor that sets up the regex matcher
     * @param stringToParse
     */
    MatchGroup(String stringToParse) {
        matcher = pattern.matcher(stringToParse);
    }

    /**
     * This will return the status mapping given which regex
     * group that was matched
     * @return SshdEntry.Status
     */
    public SshdEntry.Status getStatus() {
        if (!matcher.find()) return SshdEntry.Status.UNKNOWN;

        // The iteration starts from 1 instead of 0 to skip the first
        // match that is always the whole line.
        for (int i = 1; i <= matcher.groupCount(); i++) {
            var match = matcher.group(i);
            if (match != null) return SshdEntry.Status.fromInt(i);
        }
        return SshdEntry.Status.UNKNOWN;
    }
}
