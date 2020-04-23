package sshdProcessParser;

import processParsers.IProcessEntry;

/**
 * This class holds information about the status
 * parsed from the sshd log entry.
 */
public class SshdEntry implements IProcessEntry {

    private Status status;

    public SshdEntry(Status status) {
        this.status = status;
    }

    public String getData() {
        return this.status.toString().toLowerCase();
    }

    /**
     * Enum representing the status for a given
     * sshd log entry.
     * See class MatchGroup for further discussion about
     * how the mapping is made.
     */
    public enum Status {
        UNKNOWN("unknown"),
        STOP("stop"),
        START("start"),
        ALLOWED("allowed");

        private final String status;

        Status(String status) { this.status = status; }

        /**
         * Map from int to Status
         * @param i
         * @return
         */
        public static Status fromInt(int i) {
            switch (i) {
                case 1:
                case 2:
                    return STOP;
                case 3:
                    return START;
                case 4:
                    return ALLOWED;

                default: return UNKNOWN;
            }
        }
    }
}
