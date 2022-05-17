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

        private static double generateRandomPrice() {
            double ranprice = (Math.random() * ((500.00 - 1.00) + 1)) + 1.00;
            return ranprice;
        }

        @Test
        public void testPerformance() throws IOException {
            TestPlanStats stats = testPlan(
                    threadGroup(2, 10,
                            httpSampler("http://localhost:8080/products")
                                    .post("{\"name\": \"Amazon Fire\", \"price\": " +  generateRandomPrice() + "\t}", ContentType.APPLICATION_JSON).header("Authorization",
                                            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxLHRlc3R1czA5QGdtYWlsLmNvbSIsImlzcyI6IkxhdmlzaExhc2hlcyIsImlhdCI6MTY1Mjc2NDYwNCwiZXhwIjoxNjUyODUxMDA0fQ.8OiW3X0Un_j_S_vRHmiTdr95W1BBCTx8xBruq_FXNCQ2ZX-_o1KokroDi8gYuUpwfEqpT2urUJfYwUOzC9CVbw")
                    ),
                    //this is just to log details of each request stats
                    jtlWriter("test" + Instant.now().toString().replace(":", "-") + ".jtl")
            ).run();
            assertThat(stats.overall().sampleTimePercentile99()).isLessThan(Duration.ofSeconds(5));
        }
    }
