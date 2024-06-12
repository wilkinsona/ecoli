package org.example.ecoli;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EcoliRestController {
	private final Tracer tracer;

	public EcoliRestController(Tracer tracer) {
		this.tracer = tracer;
	}

	@GetMapping("/echo/{message}")
	public EchoResponse echo(@PathVariable String message) {
		return new EchoResponse(message);
	}

	@GetMapping("/span")
	public SpanResponse span() {
		return new SpanResponse(tracer.currentSpan());
	}

	public record EchoResponse(String message) {
	}

	public static class SpanResponse {
		private final String traceId;
		private final String spanId;
		private final String parentSpanId;

		SpanResponse(Span span) {
			if (span != null) {
				TraceContext traceContext = span.context();
				this.traceId = traceContext.traceId();
				this.spanId = traceContext.spanId();
				this.parentSpanId = span.context().parentId();
			}
			else {
				this.traceId = null;
				this.spanId = null;
				this.parentSpanId = null;
			}
		}

		@SuppressWarnings("unused")
		public String getTraceId() {
			return this.traceId;
		}

		@SuppressWarnings("unused")
		public String getSpanId() {
			return this.spanId;
		}

		@SuppressWarnings("unused")
		public String getParentSpanId() {
			return this.parentSpanId;
		}
	}
}
