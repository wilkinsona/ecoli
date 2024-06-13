package org.example.ecoli;

import java.util.function.Supplier;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.util.function.SingletonSupplier;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.prometheus.client.exemplars.tracer.common.SpanContextSupplier;
import zipkin2.codec.BytesEncoder;
import zipkin2.reporter.AsyncReporter;
import zipkin2.reporter.ReporterMetrics;
import zipkin2.reporter.Sender;
import zipkin2.reporter.metrics.micrometer.MicrometerReporterMetrics;

@SpringBootApplication
public class EcoliApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcoliApplication.class, args);
	}

	@Bean
	AsyncReporter<zipkin2.Span> spanReporter(Sender sender, BytesEncoder<zipkin2.Span> encoder, ObjectProvider<MeterRegistry> meterRegistry) {
		return AsyncReporter.builder(sender)
			.metrics(new LazyReporterMetrics(SingletonSupplier.of(() -> MicrometerReporterMetrics.create(meterRegistry.getObject()))))
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
	
	static class LazyReporterMetrics implements ReporterMetrics {

		private final Supplier<ReporterMetrics> reporterMetrics;

		public LazyReporterMetrics(Supplier<ReporterMetrics> reporterMetrics) {
			this.reporterMetrics = reporterMetrics;
		}

		@Override
		public void incrementMessages() {
			this.reporterMetrics.get().incrementMessages();
		}

		@Override
		public void incrementMessagesDropped(Throwable cause) {
			this.reporterMetrics.get().incrementMessagesDropped(cause);
			
		}

		@Override
		public void incrementSpans(int quantity) {
			this.reporterMetrics.get().incrementSpans(quantity);
		}

		@Override
		public void incrementSpanBytes(int quantity) {
			this.reporterMetrics.get().incrementSpanBytes(quantity);
		}

		@Override
		public void incrementMessageBytes(int quantity) {
			this.reporterMetrics.get().incrementMessageBytes(quantity);
		}

		@Override
		public void incrementSpansDropped(int quantity) {
			this.reporterMetrics.get().incrementSpansDropped(quantity);
		}

		@Override
		public void updateQueuedSpans(int update) {
			this.reporterMetrics.get().updateQueuedSpans(update);
		}

		@Override
		public void updateQueuedBytes(int update) {
			this.reporterMetrics.get().updateQueuedBytes(update);
		}

	}

}
