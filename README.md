# Dependency Cycle Demo

This demo reproduces a dependency cycle issue between Micrometer and Brave.

The cycle looks like this (simplified):
- `PrometheusMeterRegistry` needs a `SpanContextSupplier` (for exemplars)
- The `SpanContextSupplier` implementation needs a `Tracer`
- The `Tracer` needs an `AsyncReporter` that needs a `MeterRegistry` (to report metrics)

See configuration in `EcoliApplication`, also if you comment out the "eager" `TracerSpanContextSupplier` `@Bean`, Boot's [`LazyTracingSpanContextSupplier`](https://github.com/spring-projects/spring-boot/blob/84956ad56bb9e1d0eed8fa76921ba1db26853f44/spring-boot-project/spring-boot-actuator-autoconfigure/src/main/java/org/springframework/boot/actuate/autoconfigure/tracing/prometheus/PrometheusSimpleclientExemplarsAutoConfiguration.java#L61) takes over "fixing" the issue.

```
***************************
APPLICATION FAILED TO START
***************************

Description:

The dependencies of some of the beans in the application context form a cycle:

   webMvcObservationFilter defined in class path resource [org/springframework/boot/actuate/autoconfigure/observation/web/servlet/WebMvcObservationAutoConfiguration.class]
      ↓
   observationRegistry defined in class path resource [org/springframework/boot/actuate/autoconfigure/observation/ObservationAutoConfiguration.class]
      ↓
   defaultTracingObservationHandler defined in class path resource [org/springframework/boot/actuate/autoconfigure/tracing/MicrometerTracingAutoConfiguration.class]
┌─────┐
|  braveTracerBridge defined in class path resource [org/springframework/boot/actuate/autoconfigure/tracing/BraveAutoConfiguration.class]
↑     ↓
|  braveTracer defined in class path resource [org/springframework/boot/actuate/autoconfigure/tracing/BraveAutoConfiguration.class]
↑     ↓
|  braveTracing defined in class path resource [org/springframework/boot/actuate/autoconfigure/tracing/BraveAutoConfiguration.class]
↑     ↓
|  zipkinSpanHandler defined in class path resource [org/springframework/boot/actuate/autoconfigure/tracing/zipkin/ZipkinConfigurations$BraveConfiguration.class]
↑     ↓
|  spanReporter defined in org.example.ecoli.EcoliApplication
↑     ↓
|  prometheusMeterRegistry defined in class path resource [org/springframework/boot/actuate/autoconfigure/metrics/export/prometheus/PrometheusMetricsExportAutoConfiguration.class]
↑     ↓
|  exemplarSampler defined in class path resource [org/springframework/boot/actuate/autoconfigure/metrics/export/prometheus/PrometheusMetricsExportAutoConfiguration.class]
↑     ↓
|  spanContextSuppler defined in org.example.ecoli.EcoliApplication
└─────┘


Action:

Relying upon circular references is discouraged and they are prohibited by default. Update your application to remove the dependency cycle between beans. As a last resort, it may be possible to break the cycle automatically by setting spring.main.allow-circular-references to true.
```
