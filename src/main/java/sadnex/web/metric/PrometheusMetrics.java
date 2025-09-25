package sadnex.web.metric;

import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Histogram;
import io.prometheus.metrics.model.registry.PrometheusRegistry;
import io.prometheus.metrics.model.snapshots.Unit;

public class PrometheusMetrics {
    private final Counter requests;
    private final Counter errors;
    private final Histogram requestDuration;

    public PrometheusMetrics() {
        this.requests = Counter.builder()
                .name("web_lab1_requests_total")
                .help("Total number of requests.")
                .register();

        this.errors = Counter.builder()
                .name("web_lab1_client_errors_total")
                .help("Total number of wrong client request (400-499).")
                .register();

        this.requestDuration = Histogram.builder()
                .name("web_lab1_request_duration_seconds")
                .help("Request duration in seconds.")
                .unit(Unit.SECONDS)
                .register();
    }

    public PrometheusRegistry getRegistry() {
        return PrometheusRegistry.defaultRegistry;
    }

    public Counter getRequests() {
        return requests;
    }

    public Counter getClientErrors() {
        return errors;
    }

    public Histogram getRequestDuration() {
        return requestDuration;
    }
}
