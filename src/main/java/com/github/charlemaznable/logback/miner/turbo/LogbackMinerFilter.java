package com.github.charlemaznable.logback.miner.turbo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.github.charlemaznable.logback.miner.level.EffectorContext;
import lombok.AllArgsConstructor;
import lombok.val;
import org.slf4j.Marker;

@AllArgsConstructor
public class LogbackMinerFilter extends TurboFilter {

    private EffectorContext effectorContext;

    @Override
    public FilterReply decide(Marker marker, Logger logger, Level level,
                              String format, Object[] params, Throwable t) {
        val effector = effectorContext.getEffector(logger.getName());
        if (effector.getConsoleEffectiveLevelInt() > level.levelInt) {
            return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;
    }
}