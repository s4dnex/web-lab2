package sadnex.web.http;

public enum ContentType {
    JSON("application/json"),
    TEXT_PLAIN("text/plain");

    private final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return contentType;
    }
}
