package com.simleetag.homework.api.domain.webhook;

import java.util.List;

public class SlackResources {
    public record Payload(
            List<BlockKit.Block> blocks
    ) {
    }
}
