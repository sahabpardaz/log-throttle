package ir.sahab.logthrottle;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.Marker;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

public class LogThrottleTest {
    public static final int MIN_DISTANCE_TO_REPORT_REPEATED_MILLIS = 10;
    private LogThrottle throttle;
    private SimulatedClock clock;
    private LoggerMock logger;

    @Before
    public void setup() {
        clock = new SimulatedClock();
        clock.advanceMillis(System.currentTimeMillis());
        logger = new LoggerMock();
        throttle = new LogThrottle(logger, MIN_DISTANCE_TO_REPORT_REPEATED_MILLIS, clock);
    }

    @Test
    public void testByStringTypes() {
        // The first message of a type should be reported.
        String msg = "page not found!";
        throttle.logger("HTTP 404").warn(msg);
        assertEquals(1, logger.warnMessages.size());
        assertEquals(msg, logger.warnMessages.get(0));

        // The next message of the same type should not be reported because the required time
        // distance is not yet reached.
        clock.advanceMillis(1);
        throttle.logger("HTTP 404").warn(msg);
        assertEquals(1, logger.warnMessages.size());

        // A message of another type should be reported.
        throttle.logger("HTTP 416").warn(msg);
        assertEquals(2, logger.warnMessages.size());
        assertEquals(msg, logger.warnMessages.get(1));

        // Time passes and we reach the minimum required distance. Then the next message should
        // be reported. And the forwarded message should contain the frequency report too.
        clock.advanceMillis(MIN_DISTANCE_TO_REPORT_REPEATED_MILLIS);
        throttle.logger("HTTP 404").warn(msg);
        assertEquals(3, logger.warnMessages.size());
        assertEquals(msg + String.format(LoggerWrapper.FREQUENCY_MESSAGE_FORMAT, 2, 11),
                logger.warnMessages.get(2));
    }

    @Test
    public void testByExceptionTypes() {
        // The first message of an exception type should be reported.
        String msg = "page not found!";
        Exception e1 = new RuntimeException();
        throttle.logger("HTTP 404").error(msg, e1);
        assertEquals(1, logger.errorMessages.size());
        assertEquals(msg, logger.errorMessages.get(0));

        // The next message for the same exception should not be reported because the required time
        // distance is not yet reached.
        clock.advanceMillis(1);
        throttle.logger("HTTP 404").error(msg, e1);
        assertEquals(1, logger.errorMessages.size());

        // A message with another call stack (although it has the same type) should be reported.
        Exception e2 = new RuntimeException();
        throttle.logger("HTTP 416").error(msg, e2);
        assertEquals(2, logger.errorMessages.size());
        assertEquals(msg, logger.errorMessages.get(1));

        // Time passes and we reach the minimum required distance. Then the next message for
        // the first call stack should be reported. And the forwarded message should contain the
        // frequency report too.
        clock.advanceMillis(MIN_DISTANCE_TO_REPORT_REPEATED_MILLIS);
        throttle.logger("HTTP 404").error(msg, e1);
        assertEquals(3, logger.errorMessages.size());
        assertEquals(msg + String.format(LoggerWrapper.FREQUENCY_MESSAGE_FORMAT, 2, 11),
                logger.errorMessages.get(2));
    }

    /**
     * A mock for logger which saves all forwarded messages.
     */
    public class LoggerMock implements Logger {
        private Vector<String> traceMessages = new Vector<>();
        private Vector<String> debugMessages = new Vector<>();
        private Vector<String> infoMessages = new Vector<>();
        private Vector<String> warnMessages = new Vector<>();
        private Vector<String> errorMessages = new Vector<>();

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isTraceEnabled() {
            return false;
        }

        @Override
        public void trace(String msg) {
            traceMessages.add(msg);
        }

        @Override
        public void trace(String format, Object arg) {
            traceMessages.add(format);
        }

        @Override
        public void trace(String format, Object arg1, Object arg2) {
            traceMessages.add(format);
        }

        @Override
        public void trace(String format, Object... arguments) {
            traceMessages.add(format);
        }

        @Override
        public void trace(String msg, Throwable t) {
            traceMessages.add(msg);
        }

        @Override
        public boolean isTraceEnabled(Marker marker) {
            return false;
        }

        @Override
        public void trace(Marker marker, String msg) {
            traceMessages.add(msg);
        }

        @Override
        public void trace(Marker marker, String format, Object arg) {
            traceMessages.add(format);
        }

        @Override
        public void trace(Marker marker, String format, Object arg1, Object arg2) {
            traceMessages.add(format);
        }

        @Override
        public void trace(Marker marker, String format, Object... argArray) {
            traceMessages.add(format);
        }

        @Override
        public void trace(Marker marker, String msg, Throwable t) {
            traceMessages.add(msg);
        }

        @Override
        public boolean isDebugEnabled() {
            return false;
        }

        @Override
        public void debug(String msg) {
            debugMessages.add(msg);
        }

        @Override
        public void debug(String format, Object arg) {
            debugMessages.add(format);
        }

        @Override
        public void debug(String format, Object arg1, Object arg2) {
            debugMessages.add(format);
        }

        @Override
        public void debug(String format, Object... arguments) {
            debugMessages.add(format);
        }

        @Override
        public void debug(String msg, Throwable t) {
            debugMessages.add(msg);
        }

        @Override
        public boolean isDebugEnabled(Marker marker) {
            return false;
        }

        @Override
        public void debug(Marker marker, String msg) {
            debugMessages.add(msg);
        }

        @Override
        public void debug(Marker marker, String format, Object arg) {
            debugMessages.add(format);
        }

        @Override
        public void debug(Marker marker, String format, Object arg1, Object arg2) {
            debugMessages.add(format);
        }

        @Override
        public void debug(Marker marker, String format, Object... arguments) {
            debugMessages.add(format);
        }

        @Override
        public void debug(Marker marker, String msg, Throwable t) {
            debugMessages.add(msg);
        }

        @Override
        public boolean isInfoEnabled() {
            return false;
        }

        @Override
        public void info(String msg) {
            infoMessages.add(msg);
        }

        @Override
        public void info(String format, Object arg) {
            infoMessages.add(format);
        }

        @Override
        public void info(String format, Object arg1, Object arg2) {
            infoMessages.add(format);
        }

        @Override
        public void info(String format, Object... arguments) {
            infoMessages.add(format);
        }

        @Override
        public void info(String msg, Throwable t) {
            infoMessages.add(msg);
        }

        @Override
        public boolean isInfoEnabled(Marker marker) {
            return false;
        }

        @Override
        public void info(Marker marker, String msg) {
            infoMessages.add(msg);
        }

        @Override
        public void info(Marker marker, String format, Object arg) {
            infoMessages.add(format);
        }

        @Override
        public void info(Marker marker, String format, Object arg1, Object arg2) {
            infoMessages.add(format);
        }

        @Override
        public void info(Marker marker, String format, Object... arguments) {
            infoMessages.add(format);
        }

        @Override
        public void info(Marker marker, String msg, Throwable t) {
            infoMessages.add(msg);
        }

        @Override
        public boolean isWarnEnabled() {
            return false;
        }

        @Override
        public void warn(String msg) {
            warnMessages.add(msg);
        }

        @Override
        public void warn(String format, Object arg) {
            warnMessages.add(format);
        }

        @Override
        public void warn(String format, Object... arguments) {
            warnMessages.add(format);
        }

        @Override
        public void warn(String format, Object arg1, Object arg2) {
            warnMessages.add(format);
        }

        @Override
        public void warn(String msg, Throwable t) {
            warnMessages.add(msg);
        }

        @Override
        public boolean isWarnEnabled(Marker marker) {
            return false;
        }

        @Override
        public void warn(Marker marker, String msg) {
            warnMessages.add(msg);
        }

        @Override
        public void warn(Marker marker, String format, Object arg) {
            warnMessages.add(format);
        }

        @Override
        public void warn(Marker marker, String format, Object arg1, Object arg2) {
            warnMessages.add(format);
        }

        @Override
        public void warn(Marker marker, String format, Object... arguments) {
            warnMessages.add(format);
        }

        @Override
        public void warn(Marker marker, String msg, Throwable t) {
            warnMessages.add(msg);
        }

        @Override
        public boolean isErrorEnabled() {
            return false;
        }

        @Override
        public void error(String msg) {
            errorMessages.add(msg);
        }

        @Override
        public void error(String format, Object arg) {
            errorMessages.add(format);
        }

        @Override
        public void error(String format, Object arg1, Object arg2) {
            errorMessages.add(format);
        }

        @Override
        public void error(String format, Object... arguments) {
            errorMessages.add(format);
        }

        @Override
        public void error(String msg, Throwable t) {
            errorMessages.add(msg);
        }

        @Override
        public boolean isErrorEnabled(Marker marker) {
            return false;
        }

        @Override
        public void error(Marker marker, String msg) {
            errorMessages.add(msg);
        }

        @Override
        public void error(Marker marker, String format, Object arg) {
            errorMessages.add(format);
        }

        @Override
        public void error(Marker marker, String format, Object arg1, Object arg2) {
            errorMessages.add(format);
        }

        @Override
        public void error(Marker marker, String format, Object... arguments) {
            errorMessages.add(format);
        }

        @Override
        public void error(Marker marker, String msg, Throwable t) {
            errorMessages.add(msg);
        }
    }

    /**
     * A simulated clock which its time can be mocked according to test requirements.
     */
    class SimulatedClock extends Clock {
        private AtomicLong millis = new AtomicLong(0);

        @Override
        public long millis() { return millis.get(); }

        public void advanceMinutes(long minutes) { millis.addAndGet(minutes * 60 * 1000L); }

        public void advanceMillis(long millis) { this.millis.addAndGet(millis); }

        @Override
        public ZoneId getZone() { return ZoneId.of("GMT"); }
        @Override
        public Clock withZone(ZoneId zone) { throw new UnsupportedOperationException(); }
        @Override
        public Instant instant() { return Instant.ofEpochMilli(millis.get()); }
    }

}