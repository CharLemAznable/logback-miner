package com.github.charlemaznable.logback.miner.appender;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;
import com.github.charlemaznable.logback.miner.level.Effector;
import lombok.val;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ConsoleAppender extends AsyncAppender {

    public static final String DEFAULT_CONSOLE_PATTERN
            = "%date [%20.20thread] %5level %50.50logger{50}\\(%4.4line\\): %message%n";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String DEFAULT_TARGET = ConsoleTarget.SYSTEM_OUT.getName();
    public static final boolean DEFAULT_IMMEDIATE_FLUSH = true;

    private PatternLayoutEncoder encoder;
    private InternalAppender appender;

    public ConsoleAppender() {
        this.encoder = new PatternLayoutEncoder();
        this.encoder.setCharset(DEFAULT_CHARSET);
        this.encoder.setPattern(DEFAULT_CONSOLE_PATTERN);

        this.appender = new InternalAppender();
        this.appender.setTarget(DEFAULT_TARGET);
        this.appender.setImmediateFlush(DEFAULT_IMMEDIATE_FLUSH);
        this.appender.setEncoder(this.encoder);
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        this.appender.setContext(context);
        this.encoder.setContext(context);
    }

    public ConsoleAppender setCharset(String charsetName) {
        try {
            this.encoder.setCharset(Charset.forName(charsetName));
        } catch (Exception e) {
            addStatus(new WarnStatus("Set Charset Error: " + charsetName, this));
            this.encoder.setCharset(DEFAULT_CHARSET);
        }
        return this;
    }

    public ConsoleAppender setPattern(String patternString) {
        this.encoder.setPattern(patternString);
        return this;
    }

    public ConsoleAppender setTarget(String targetName) {
        this.appender.setTarget(targetName);
        return this;
    }

    public ConsoleAppender setImmediateFlush(boolean immediateFlush) {
        this.appender.setImmediateFlush(immediateFlush);
        return this;
    }

    @Override
    public void start() {
        this.encoder.start();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        this.encoder.stop();
    }

    @Override
    protected UnsynchronizedAppenderBase<ILoggingEvent> internalAppend() {
        return this.appender;
    }

    @Override
    protected FilterReply decide(Effector effector, Level eventLevel) {
        // configured ConsoleAppender and event passed EffectorTurboFilter,
        // but appender level is greater then event level -> DENY
        if (effector.getConsoleEffectiveLevelInt() > eventLevel.levelInt) {
            return FilterReply.DENY;
        }
        return FilterReply.ACCEPT;
    }

    static class InternalAppender extends OutputStreamAppender<ILoggingEvent> {

        private ConsoleTarget target = ConsoleTarget.SYSTEM_OUT;

        @Override
        public void start() {
            setOutputStream(target.getStream());
            super.start();
        }

        public void setTarget(String value) {
            val t = ConsoleTarget.findByName(value.trim());
            if (t == null) {
                targetWarn(value);
            } else {
                target = t;
            }
        }

        private void targetWarn(String val) {
            val status = new WarnStatus("[" + val + "] should be one of " + Arrays.toString(ConsoleTarget.values()), this);
            status.add(new WarnStatus("Using previously set target, System.out by default.", this));
            addStatus(status);
        }
    }
}
