package sshdParser;

/**
 * Front end for the sshd log parser.
 * This class is responsible for calling the actual parser
 * and then create and return a SshdEntry representing
 * the parsed log entry.
 */
public class Parser {
    public SshdEntry parse(String sshLogLine) {
        var matchGroup = new MatchGroup(sshLogLine);

        var entry = new SshdEntry();
        entry.setStatus(matchGroup.getStatus());return entry;
    }
}
