package sadnex.web.data;

public enum Parameter {
    X("x"),
    Y("y"),
    R("r"),
    RESULT("result"),
    EXECUTION_TIME("executionTime"),
    CURRENT_TIME("currentTime"),
    ERROR("error");

    private final String value;

    Parameter(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
