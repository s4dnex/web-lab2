package sadnex.web.data;

public enum Status {
    OK("OK"),
    MISS("MISS"),
    ERROR("ERROR");

    private final String value;

    Status(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
