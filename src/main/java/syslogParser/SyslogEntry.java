package syslogParser;

import java.time.OffsetDateTime;

/**
 * This class holds information parsed from
 * a syslog entry.
 */
public class SyslogEntry {
    private OffsetDateTime parsedAt;
    private OffsetDateTime dateTime;
    private int priority;
    private String host;
    private String process;
    private int pid;
    private String message;

    /**
     * Get the date and time parsed from the Syslog entry
     * @return LocalDateTime
     */
    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Set the date of the Syslog entry
     */
    public void setDateTime(OffsetDateTime dateTime) { this.dateTime = dateTime; }

    /**
     * Get the date and time of when the syslog file was parsed
     * @return LocalDateTime
     */
    public OffsetDateTime getParsedAt() {
        return parsedAt;
    }

    /**
     * Set the date and time when the syslog file was parsed
     * @param parsedAt
     */
    public void setParsedAt(OffsetDateTime parsedAt) {
        this.parsedAt = parsedAt;
    }

    /**
     * Set the priority of the Syslog entry
     * @param priority
     */
    public void setPriority(int priority) { this.priority = priority; }

    /**
     * Get the priority of the syslog entry
     * @return int
     */
    public int getPriority() { return this.priority; }

    /**
     * Set the host name
     * @param host
     */
    public void setHost(String host) { this.host = host; }

    /**
     * Get the host name
     * @return
     */
    public String getHost() { return this.host; }

    /**
     * Set the process name
     * @param process
     */
    public void setProcess(String process) { this.process = process; }

    /**
     * Get the process name
     * @return String
     */
    public String getProcess() { return this.process; }

    /**
     * Set the process id
     * @param pid
     */
    public void setPid(int pid) { this.pid = pid; }

    /**
     * Get the process id
     * @return
     */
    public int getPid() { return this.pid; }

    /**
     * Set the message
     * @param message
     */
    public void setMessage(String message) { this.message = message; }

    /**
     * Get the message
     * @return
     */
    public String getMessage() { return this.message; }
}
