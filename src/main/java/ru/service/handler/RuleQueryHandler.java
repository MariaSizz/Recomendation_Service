package ru.service.handler;

import java.util.List;

public interface RuleQueryHandler {
    boolean handle(String userId, List<String> arguments);
}
