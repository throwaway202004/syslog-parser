package processParsers;

public interface IProcessParser {
    boolean isValidProcess(String processName);
    IProcessEntry parse(String data);
}
