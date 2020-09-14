package com.github.charlemaznable.logback.miner.configurator;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.github.charlemaznable.logback.miner.appender.VertxAppender;
import com.github.charlemaznable.logback.miner.appender.VertxManager;
import com.google.auto.service.AutoService;
import lombok.val;

import static com.github.charlemaznable.logback.miner.configurator.ConfiguratorUtil.fetchLoggerName;
import static com.github.charlemaznable.logback.miner.level.EffectorContextUtil.getEffectorContext;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.endsWithIgnoreCase;

@AutoService(Configurator.class)
public class VertxConfigurator extends AppenderConfigurator {

    private static final String VERTX_LEVEL_SUFFIX = "[vertx.level]";
    private static final String VERTX_NAME_SUFFIX = "[vertx.name]";
    private static final String VERTX_ADDRESS_SUFFIX = "[vertx.address]";

    @Override
    public void configurate(LoggerContext loggerContext, String key, String value) {
        if (endsWithIgnoreCase(key, VERTX_LEVEL_SUFFIX)) {
            val name = fetchLoggerName(key, VERTX_LEVEL_SUFFIX);
            val effectorContext = getEffectorContext(loggerContext);
            if (isNull(effectorContext)) return;
            effectorContext.getEffector(name).setVertxLevel(Level.toLevel(value));

        } else if (endsWithIgnoreCase(key, VERTX_NAME_SUFFIX)) {
            val name = fetchLoggerName(key, VERTX_NAME_SUFFIX);
            val vertxAppender = fetchVertxAppender(loggerContext.getLogger(name));
            vertxAppender.setVertxName(value);
            addAppenderIfAbsent(vertxAppender);
            VertxManager.configVertx(value);

        } else if (endsWithIgnoreCase(key, VERTX_ADDRESS_SUFFIX)) {
            val name = fetchLoggerName(key, VERTX_ADDRESS_SUFFIX);
            val vertxAppender = fetchVertxAppender(loggerContext.getLogger(name));
            vertxAppender.setVertxAddress(value);
            addAppenderIfAbsent(vertxAppender);

        }
    }

    private VertxAppender fetchVertxAppender(Logger logger) {
        val vertxAppenderName = "VertxAppender-" + logger.getName();
        Appender<ILoggingEvent> vertxAppender = logger.getAppender(vertxAppenderName);
        if (!(vertxAppender instanceof VertxAppender)) {
            logger.detachAppender(vertxAppender);
            vertxAppender = new VertxAppender();
            vertxAppender.setName(vertxAppenderName);
            vertxAppender.setContext(logger.getLoggerContext());
            // default vertx address is logger name
            ((VertxAppender) vertxAppender).setVertxAddress(logger.getName());
            logger.addAppender(vertxAppender);
        }
        return (VertxAppender) vertxAppender;
    }
}