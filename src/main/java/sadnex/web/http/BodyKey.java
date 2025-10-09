package sadnex.web.http;

public enum BodyKey {
    X("x"),
    Y("y"),
    R("r"),
    RESULT("result"),
    EXECUTION_TIME("executionTime"),
    CURRENT_TIME("currentTime"),
    ERROR("error");

    private final String value;

    BodyKey(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
