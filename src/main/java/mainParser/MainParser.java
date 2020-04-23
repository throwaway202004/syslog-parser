package mainParser;

import processParsers.IProcessEntry;
import processParsers.IProcessParser;
import syslogParser.Parser;
import syslogParser.SyslogParseException;
import java.util.List;
import java.util.Optional;

public class MainParser {
    private final Parser syslogParser;
    private List<IProcessParser> processParsers;

    public MainParser(List<IProcessParser> parsers) {
        this.processParsers = parsers;
        this.syslogParser = new syslogParser.Parser();
    }

    public JsonOutput parse(String value) throws SyslogParseException {
        var syslogEntry = syslogParser.parse(value);

        Optional<IProcessParser> processParser = processParsers.stream().filter(p -> p.isValidProcess(syslogEntry.getProcess())).findFirst();

        IProcessEntry processEntry = null;
        if (processParser.isPresent()) {
            processEntry = processParser.get().parse(syslogEntry.getMessage());
        }

        return new JsonOutput(syslogEntry, processEntry);
    }
}
