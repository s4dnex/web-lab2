package sadnex.web.fcgi;

public enum ResponseBodyKey {
    X("x"),
    Y("y"),
    R("r"),
    RESULT("result"),
    EXECUTION_TIME("executionTime"),
    CURRENT_TIME("currentTime"),
    ERROR("error");

    private final String value;

    ResponseBodyKey(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
