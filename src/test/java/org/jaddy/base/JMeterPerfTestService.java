package org.jaddy.base;

import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.Test;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class JMeterPerfTestService {



        @Test
        public void testPerformance() throws IOException {
            TestPlanStats stats = testPlan(
                    threadGroup(2, 10,
                            httpSampler("http://localhost:8080/products")
                                    .post("{\"name\": \"Amazon Fire\", \"price\": 129.0}", ContentType.APPLICATION_JSON).header("Authorization",
                                            "Bearer ")
                    ),
                    //this is just to log details of each request stats
                    jtlWriter("test" + Instant.now().toString().replace(":", "-") + ".jtl")
            ).run();
            assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
        }

    }
