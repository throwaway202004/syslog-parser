package mainParser;

import org.json.JSONObject;
import processParsers.IProcessEntry;
import syslogParser.SyslogEntry;


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

    public JsonOutput(SyslogEntry syslogEntry, IProcessEntry processEntry) {
        this.syslogEntry = syslogEntry;
        this.processEntry = processEntry;
    }

    /*
{
	"@timestamp": "tidstämpel då meddelandet konsumerades i format yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
	"message": "loggmeddelandet i sin helhet",
	"agent": {
		"id": "hostnamnet från loggposten"
	},
	"host": {
		"ip": "IP-nummer från konsumenten"
	},
	"event": {
		"created": "Tidsstämpel från loggmeddelandet i format yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX",
		"type": "Någon av access, allowed, denied, error, start, stop, unknown. Välj passande."
	},
	"process": {
		"pid": "PID från loggande process. Från loggmeddelandet",
		"name": "Loggande process namn. Från loggmeddelandet"
	}
}
 */
    public JSONObject toJson() {
        var agent = new JSONObject();
        agent.put(ID, syslogEntry.getHost());

        var host = new JSONObject();
        host.put(IP, "127.0.0.1");

        var event = new JSONObject();
        event.put(CREATED, syslogEntry.getDateTime());
        event.put(TYPE, processEntry.getData());

        var process = new JSONObject();
        process.put(PID, syslogEntry.getPid());
        process.put(NAME, syslogEntry.getProcess());

        JSONObject json = new JSONObject();
        json.put(TIMESTAMP, syslogEntry.getParsedAt());
        json.put(MESSAGE, syslogEntry.getMessage());
        json.put(AGENT, agent);
        json.put(HOST, host);
        json.put(EVENT, event);
        json.put(PROCESS, process);

        return json;
    }
}
