package ir.sahab.logthrottle;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.time.Clock;

/**
 * Wrapper for a SLF4J logger which adds the aggregation feature to a logger to avoid log burst.
 * It is not supposed to use directly but via the {@link LogThrottle}.
 */
class LoggerWrapper implements Logger {
    // visible for testing
    final static String FREQUENCY_MESSAGE_FORMAT =
            " -visited %d logs of same type in last %d millis";

    private Logger logger;
    private Clock clock;

    private int numUnreported;
    private long lastReportMillis;
    private int minRepeatingDistanceMillis;

    public LoggerWrapper(Logger logger, int minRepeatingDistance, Clock clock) {
        this.logger = logger;
        this.minRepeatingDistanceMillis = minRepeatingDistance;
        this.clock = clock;
    }

    /**
     * Counts a new call for this logger and tells the current state in return.
     * @return an status object which both tells whether we should report the log to the underlying
     * logger and a message which should ne appended to the log message, indicating the log
     * frequency.
     */
    private synchronized Status newVisit() {
        numUnreported++;

        long current = clock.millis();
        long timePeriod = current - lastReportMillis;
        if (timePeriod > minRepeatingDistanceMillis) {
            String frequencyMessage = numUnreported == 1 ? "" :
                    String.format(FREQUENCY_MESSAGE_FORMAT, numUnreported, timePeriod);
            numUnreported = 0;
            lastReportMillis = current;
            return Status.report(frequencyMessage);
        }

        return Status.doNotReport();
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void trace(String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(marker, msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(marker, format + status.getFrequencyMessage(), arg);
        }

    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(marker, format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(marker, format + status.getFrequencyMessage(), argArray);
        }

    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.trace(marker, msg + status.getFrequencyMessage(), t);
        }

    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void debug(String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(marker, msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(marker, format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(marker, format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(marker, format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.debug(marker, msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void info(String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void info(String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void info(String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(marker, msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(marker, format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(marker, format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(marker, format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.info(marker, msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void warn(String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void warn(String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void warn(String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(marker, msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(marker, format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(marker, format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(marker, format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.warn(marker, msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(msg, status.getFrequencyMessage());
        }
    }

    @Override
    public void error(String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void error(String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void error(String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(msg + status.getFrequencyMessage(), t);
        }
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(marker, msg + status.getFrequencyMessage());
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(marker, format + status.getFrequencyMessage(), arg);
        }
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(marker, format + status.getFrequencyMessage(), arg1, arg2);
        }
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(marker, format + status.getFrequencyMessage(), arguments);
        }
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        Status status = newVisit();
        if (status.shouldReport()) {
            logger.error(marker, msg + status.getFrequencyMessage(), t);
        }
    }

    /**
     * Status both tells whether we should report a log to the underlying
     * logger and a message which should ne appended to the log message, indicating the log
     * frequency.
     */
    private static class Status {
        private boolean shouldReport;
        private String frequencyMessage;

        private static Status DO_NOT_REPORT = new Status(false, null);

        private Status(boolean shouldReport, String frequencyMessage) {
            this.shouldReport = shouldReport;
            this.frequencyMessage = frequencyMessage;
        }

        static Status doNotReport() {
            return DO_NOT_REPORT;
        }

        static Status report(String reportAppendix) {
            return new Status(true, reportAppendix);
        }

        boolean shouldReport() {
            return shouldReport;
        }

        String getFrequencyMessage() {
            return frequencyMessage;
        }
    }
}
