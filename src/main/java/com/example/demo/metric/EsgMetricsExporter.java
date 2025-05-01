package com.example.demo.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EsgMetricsExporter {

    private final MeterRegistry meterRegistry;

    // 회사별 ESG 점수 데이터 저장
    private final Map<String, Double> environmentScores = new ConcurrentHashMap<>();
    private final Map<String, Double> socialScores = new ConcurrentHashMap<>();
    private final Map<String, Double> governanceScores = new ConcurrentHashMap<>();

    public EsgMetricsExporter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostConstruct
    public void init() {
        // 예시 데이터 (회사명 → 점수)
        environmentScores.put("CJ", 81.0);
        socialScores.put("CJ", 79.0);
        governanceScores.put("CJ", 85.0);

        environmentScores.put("LG", 76.0);
        socialScores.put("LG", 82.0);
        governanceScores.put("LG", 88.0);

        // 메트릭 등록
        environmentScores.forEach((company, value) ->
                Gauge.builder("esg_score_environment", environmentScores, map -> map.get(company))
                        .description("환경 점수")
                        .tag("company", company)
                        .register(meterRegistry)
        );

        socialScores.forEach((company, value) ->
                Gauge.builder("esg_score_social", socialScores, map -> map.get(company))
                        .description("사회 점수")
                        .tag("company", company)
                        .register(meterRegistry)
        );

        governanceScores.forEach((company, value) ->
                Gauge.builder("esg_score_governance", governanceScores, map -> map.get(company))
                        .description("지배구조 점수")
                        .tag("company", company)
                        .register(meterRegistry)
        );
    }
}
