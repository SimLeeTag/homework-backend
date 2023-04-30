package com.simleetag.homework.api.domain.webhook.api;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PinpointResources {
    public record Payload(
            @Schema(name = "타겟 애플리케이션 ID")
            @NotNull
            String applicationId,

            @Schema(name = "alarm 설정 페이지의 checker 정보")
            @NotNull
            Checker checker,

            @Schema(name = "설정된 시간동안 체커가 감지한 값의 임계치")
            @NotNull
            Integer threshold
    ) {
        public record Checker(
                @Schema(name = "알람 설정 페이지의 checker 이름")
                @NotNull
                String name,

                @Schema(name = "Checker가 감지한 값")
                @NotNull
                List<LongValueAgentChecker> detectedValue
        ) {
            public record LongValueAgentChecker(
                    @Schema(name = "Checker가 감지한 에이전트 ID")
                    @NotNull
                    String agentId,

                    @Schema(name = "체커가 감지한 에이전트의 값")
                    @NotNull
                    Long agentValue
            ) {

            }
        }
    }
}
