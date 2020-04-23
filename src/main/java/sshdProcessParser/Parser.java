package sshdProcessParser;

import processParsers.IProcessParser;
/**
 * Front end for the sshd log parser.
 * This class is responsible for calling the actual parser
 * and then create and return a SshdEntry representing
 * the parsed log entry.
 */
public class Parser implements IProcessParser {
    private static final String PROCESS_NAME = "sshd" ;

    @Override
    public boolean isValidProcess(String processName) {
        return processName.equals(PROCESS_NAME);
    }

    public SshdEntry parse(String sshLogLine) {
        var matchGroup = new MatchGroup(sshLogLine);
        return new SshdEntry(matchGroup.getStatus());
    }
}
