package mainParser;

import org.json.JSONObject;
import processParsers.IProcessEntry;
import syslogParser.SyslogEntry;
import java.time.format.DateTimeFormatter;

/**
 * A class implementing the specified JSON structure.
 */
public class JsonOutput {
    private static final String TIMESTAMP = "@timestamp";
    private static final String MESSAGE = "message";
    private static final String AGENT = "agent";
    private static final String ID = "id";
    private static final String HOST = "host";
    private static final String IP = "ip";
    private static final String EVENT = "event";
    private static final String CREATED = "created";
    private static final String TYPE = "type";
    private static final String PROCESS = "process";
    private static final String PID = "pid";
    private static final String NAME = "name";

    private final SyslogEntry syslogEntry;
    private final IProcessEntry processEntry;
    private final String ipAddress;

    public JsonOutput(SyslogEntry syslogEntry, IProcessEntry processEntry, String ipAddress) {
        this.syslogEntry = syslogEntry;
        this.processEntry = processEntry;
        this.ipAddress = ipAddress;
    }

    /**
     * Format the output according to the specification in spec.json
     * @return JSONObject
     */
    public JSONObject toJson() {
        var agent = new JSONObject();
        agent.put(ID, syslogEntry.getHost());

        var host = new JSONObject();
        host.put(IP, ipAddress);

        var event = new JSONObject();
        event.put(CREATED, syslogEntry.getDateTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        event.put(TYPE, processEntry.getData().toLowerCase());

        var process = new JSONObject();
        process.put(PID, syslogEntry.getPid());
        process.put(NAME, syslogEntry.getProcess());

        JSONObject json = new JSONObject();
        json.put(TIMESTAMP, syslogEntry.getParsedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        json.put(MESSAGE, syslogEntry.getMessage());
        json.put(AGENT, agent);
        json.put(HOST, host);
        json.put(EVENT, event);
        json.put(PROCESS, process);

        return json;
    }
}
