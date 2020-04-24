package mainParser;

import processParsers.IProcessEntry;
import processParsers.IProcessParser;
import syslogParser.Parser;
import syslogParser.SyslogParseException;
import java.util.List;
import java.util.Optional;

/**
 * This is the class responsible for parsing the value of a Kafka record.
 * The parsed result is returned in a JSON structure as described
 * in JsonOutput.java.
 * @see mainParser.JsonOutput
 */
public class MainParser {
    private final Parser syslogParser;
    private final String ipAddress;
    private List<IProcessParser> processParsers;

    /**
     * Constructs a MainParser by taking as argument a list of process parsers.
     * The process parsers are used in parsing the process specific output
     * of a syslog message.
     * The ip address parameter is used in the JSON structure returned by
     * the parse method.
     * @param parsers
     * @param ipAddress
     */
    public MainParser(List<IProcessParser> parsers, String ipAddress) {
        this.processParsers = parsers;
        this.syslogParser = new syslogParser.Parser();
        this.ipAddress = ipAddress;
    }

    /**
     * This method does the actual parsing. It start by handing of the parsing to
     * the syslog parser. The parsed result is the used to feed a suitable process
     * parser (if one is found). When both parsing steps are done a JSON structure
     * is created and returned to the caller.
     * @param value
     * @return
     * @throws SyslogParseException
     */
    public JsonOutput parse(String value) throws SyslogParseException {
        var syslogEntry = syslogParser.parse(value);

        Optional<IProcessParser> processParser = processParsers.stream().filter(p -> p.isValidProcess(syslogEntry.getProcess())).findFirst();

        IProcessEntry processEntry = null;
        if (processParser.isPresent()) {
            processEntry = processParser.get().parse(syslogEntry.getMessage());
        }

        return new JsonOutput(syslogEntry, processEntry, ipAddress);
    }
}
