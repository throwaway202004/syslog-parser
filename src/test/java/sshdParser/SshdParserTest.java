package sshdParser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SshdParserTest {
    String disconnect = "Received disconnect from 192.168.41.24 port 61088:11: disconnected by user";
    String disconnected = "Disconnected from 192.168.41.24 port 61088";
    String sessionClosed = "pam_unix(sshd:session): session closed for user dba01";
    String accepted = "Accepted password for dba01 from 192.168.41.23 port 50760 ssh2";
    String sessionOpened = "pam_unix(sshd:session): session opened for user dba01 by (uid=0)";
    String someOtherString = "Some other output from sshd";

    @Test
    public void parser_disconnectAndSessionClosed_HasStatusStop() {
        var parser = new Parser();
        SshdEntry result = parser.parse(disconnect);
        assertEquals(SshdEntry.Status.STOP, result.getStatus());

        result = parser.parse(disconnected);
        assertEquals(SshdEntry.Status.STOP, result.getStatus());

        result = parser.parse(sessionClosed);
        assertEquals(SshdEntry.Status.STOP, result.getStatus());
    }

    @Test
    public void parser_accepted_HasStatusAllowed() {
        var parser = new Parser();
        SshdEntry result = parser.parse(accepted);
        assertEquals(SshdEntry.Status.ALLOWED, result.getStatus());
    }

    @Test
    public void parser_sessionOpened_HasStatusStart() {
        var parser = new Parser();
        SshdEntry result = parser.parse(sessionOpened);
        assertEquals(SshdEntry.Status.START, result.getStatus());
    }

    @Test
    public void parser_someOtherString_HasStatusUnknown() {
        var parser = new Parser();
        SshdEntry result = parser.parse(someOtherString);
        assertEquals(SshdEntry.Status.UNKNOWN, result.getStatus());
    }
}
