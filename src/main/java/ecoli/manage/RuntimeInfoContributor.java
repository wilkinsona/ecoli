package ecoli.manage;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.apache.commons.lang3.time.DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT;

/**
 * @author Jonatan Ivanov
 */
@Slf4j
@Component
public class RuntimeInfoContributor implements InfoContributor {
    private static final String DEFAULT_HOSTNAME = "unknown";
    private static final String DEFAULT_IP_ADDRESS = "unknown";
    private static final FastDateFormat DATE_FORMAT = ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT;

    @Autowired private Environment environment;
    private Date startDate = new Date();

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("environment",
                ImmutableMap.of("activeProfiles", environment.getActiveProfiles())
        );
        builder.withDetail("runtime",
                ImmutableMap.builder()
                        .put("host", getSafeHostName())
                        .put("ip", getSafeIpAddres())
                        .put("startDate", DATE_FORMAT.format(startDate))
                        .put("heartBeat", DATE_FORMAT.format(new Date()))
                        .put("uptime", getUptime())
                        .build()
        );
    }

    private String getUptime() {
        return Duration.between(startDate.toInstant(), Instant.now()).toString();
    }

    private String getSafeHostName() {
        String hostname = DEFAULT_HOSTNAME;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        }
        catch (Throwable e) {
            log.error("Unable to get the hostname", e);
        }

        return hostname;
    }

    private String getSafeIpAddres() {
        String ip = DEFAULT_IP_ADDRESS;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }
        catch (Throwable e) {
            log.error("Unable to get the IP address", e);
        }

        return ip;
    }
}
