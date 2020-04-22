package syslogParser;

import java.time.LocalDateTime;

/**
 * Front end for the RFC3164 syslog parser.
 * This class is responsible for calling the actual parser
 * and then create and return a SyslogEntry representing
 * the parsed syslog entry.
 */
public class Parser {

    /**
     * Given a string with a syslog entry, parses it and returns
     * an object of type SyslogEntry.
     * @param syslogLine
     * @return SyslogEntry
     * @throws SyslogParseException
     */
    public SyslogEntry parse(String syslogLine) throws SyslogParseException {
        if (syslogLine == null || syslogLine.isEmpty()) throw new SyslogParseException("Empty syslog entry");

        var matchGroup = new MatchGroup(syslogLine);

        if (matchGroup.getMatchesCount() < 1) throw new SyslogParseException("Could not parse syslog entry");

        var entry = new SyslogEntry();

        entry.setParsedAt(LocalDateTime.now());
        entry.setDateTime(matchGroup.getDateTime());
        entry.setPriority(matchGroup.getPriority());
        entry.setHost(matchGroup.getHost());
        entry.setProcess(matchGroup.getProcess());
        entry.setPid(matchGroup.getPid());
        entry.setMessage(matchGroup.getMessage());
        return entry;
    }
}
