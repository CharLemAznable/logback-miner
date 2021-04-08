package com.github.charlemaznable.logback.miner.configurator;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.COWArrayList;

public abstract class AppenderConfigurator implements Configurator {

    private COWArrayList<Appender> appenderList = new COWArrayList<>(new Appender[0]);

    @Override
    public void postConfigurate(LoggerContext loggerContext) {
        appenderList.forEach(LifeCycle::start);
        appenderList.clear();
    }

    protected void addAppenderIfAbsent(Appender<ILoggingEvent> appender) {
        appenderList.addIfAbsent(appender);
    }
}
