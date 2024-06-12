package org.example.ecoli;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.prometheus.client.exemplars.tracer.common.SpanContextSupplier;
import zipkin2.codec.BytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.Sender;
import zipkin2.reporter.metrics.micrometer.MicrometerReporterMetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcoliApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcoliApplication.class, args);
	}

	@Bean
	AsyncReporter<zipkin2.Span> spanReporter(Sender sender, BytesEncoder<zipkin2.Span> encoder, MeterRegistry meterRegistry) {
		return AsyncReporter.builder(sender)
			.metrics(MicrometerReporterMetrics.create(meterRegistry))
			.build(encoder);
	}

	@Bean // comment this line out and Boot's LazyTracingSpanContextSupplier takes over, "fixing" the issue
	TracerSpanContextSupplier spanContextSuppler(Tracer tracer) {
		return new TracerSpanContextSupplier(tracer);
	}

	static class TracerSpanContextSupplier implements SpanContextSupplier {
		private final Tracer tracer;

		TracerSpanContextSupplier(Tracer tracer) {
			this.tracer = tracer;
		}

		@Override
		public String getTraceId() {
			Span currentSpan = currentSpan();
			return (currentSpan != null) ? currentSpan.context().traceId() : null;
		}

		@Override
		public String getSpanId() {
			Span currentSpan = currentSpan();
			return (currentSpan != null) ? currentSpan.context().spanId() : null;
		}

		@Override
		public boolean isSampled() {
			Span currentSpan = currentSpan();
			if (currentSpan == null) {
				return false;
			}
			Boolean sampled = currentSpan.context().sampled();
			return sampled != null && sampled;
		}

		private Span currentSpan() {
			return this.tracer.currentSpan();
		}
	}
}
