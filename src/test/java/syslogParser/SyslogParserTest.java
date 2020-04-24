package syslogParser;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import static org.junit.jupiter.api.Assertions.*;

public class SyslogParserTest {

    String syslogLine = "<123>Apr  6 10:33:08 db01 sshd[18989]: Received disconnect from 192.168.41.24 port 61088:11: disconnected by user";
    String invalidSyslogLine = "This is not a valid syslog line";
    String syslogLineWithOptionalsRemoved = "Apr  6 10:33:08 db01 sshd: Received disconnect from 192.168.41.24 port 61088:11: disconnected by user";

    @Test
    public void parser_ValidSyslog_DoesNotThrow() {
        var parser = new syslogParser.Parser();
        assertDoesNotThrow(() -> parser.parse(syslogLine));
    }

    @Test
    public void parser_ValidSyslog_IsNotNull() throws Exception {
        var parser = new syslogParser.Parser();
        var result = parser.parse(syslogLine);
        assertNotNull(result);
    }

    @Test
    public void parser_ValidSyslog_ValidSyslogEntryReturned() throws Exception {
        var parser = new syslogParser.Parser();
        var result = parser.parse(syslogLine);
        assertEquals(OffsetDateTime.of(2020, 4, 6, 10, 33, 8, 0, ZoneOffset.UTC), result.getDateTime());
        assertEquals(123, result.getPriority());
        assertEquals("db01", result.getHost());
        assertEquals("sshd", result.getProcess());
        assertEquals(18989, result.getPid());
        assertEquals("Received disconnect from 192.168.41.24 port 61088:11: disconnected by user", result.getMessage());
    }

    @Test
    public void parser_InvalidSyslog_WillThrowException() {
        var parser = new syslogParser.Parser();
        assertThrows(SyslogParseException.class, () -> parser.parse(invalidSyslogLine));
    }

    @Test
    public void parser_ValidSyslogWithOptionalsRemoved_ValidSyslogEntryReturned() throws Exception {
        var parser = new syslogParser.Parser();
        var result = parser.parse(syslogLineWithOptionalsRemoved);
        assertEquals(OffsetDateTime.of(2020, 4, 6, 10, 33, 8, 0, ZoneOffset.UTC), result.getDateTime());
        assertEquals(-1, result.getPriority());
        assertEquals("db01", result.getHost());
        assertEquals("sshd", result.getProcess());
        assertEquals(-1, result.getPid());
        assertEquals("Received disconnect from 192.168.41.24 port 61088:11: disconnected by user", result.getMessage());
    }

    @Test
    public void parser_EmptyInput_WillThrowException() {
        var parser = new syslogParser.Parser();
        assertThrows(SyslogParseException.class, () -> parser.parse(""));
    }
}