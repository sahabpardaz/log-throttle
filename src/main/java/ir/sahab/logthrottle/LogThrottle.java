package ir.sahab.logthrottle;

import org.slf4j.Logger;

import java.time.Clock;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A throttle to avoid log bursts. In situations such as network failure or when a third party
 * system is down, we may have a burst of logs produced by multiple worker threads.
 * The failure can last for a while causing millions of log records which totally pollutes the
 * log files. Sometimes we may implement Circuit Breaker pattern to settle it, but the initial burst
 * for already triggered operations is still there.
 * Actually the bad effect is not limited to polluting log files. We have also encountered a
 * bug in log4j which sometimes causes livelock in sharp bursts.
 * <p>
 * Log throttle targets this issue by aggregating logs of a same type if they are repeated during a
 * small period. Here is a sample aggregated message:
 * <p>
 * "No response from remote server, reqId: 243567! -visited 2300 logs of same type in last 1000 millis"
 * <p>
 * The second part of this message (number of visits) will be created by throttle itself.
 * You just configure a throttle and call ordinary log methods:
 * <pre>
 * // Declaration:
 * private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
 * private static final LogThrottle logThrottle = new LogThrottle(logger, 1000);
 * ....
 * // Usage:
 * logThrottle.logger("NO RESPONSE").warn("No response from server, reqId: {}!", reqId);
 *
 * </pre>
 * <p>
 * Second argument to throttle constructor (i.e., 1000 in above sample) , defines the minimum time
 * distance of a repeated log from its predecessor to be reported by throttle.
 * Because logger and loggerThrottle are defined as static members, they are shared between
 * all instances of the class.
 * The aggregation is based on the type of messages which are defined by user. For example
 * in the above example, the type of message is "NO RESPONSE".
 * It is possible to call logger directly when you want to log all occurrences, e.g., with
 * a lower level for debug purpose:
 * <pre>
 * logger.debug("No response from server, reqId: {}!", reqId);
 * logThrottle.logger("NO RESPONSE").warn("No response from server, reqId: {}!", reqId);
 * </pre>
 * In situations when you want to log an exception, you may want to aggregate based on the
 * exception:
 * <pre>
 * logThrottle.logger(e).warn("No response from server", e);
 * </pre>
 * Note that in aggregation by exception, we consider stack trace of the exception
 * (not just its type) as the message type.
 * <p>
 * Note that you *should* have limited number of possible message types.
 * For example if you set the request ID in message type, its bad effect is not just preventing
 * aggregation, but also uncontrolled memory consumption due to track of so many message types
 * in memory.
 */
public class LogThrottle {
    private static int DEFAULT_MIN_DISTANCE_MILLIS = 1000;

    private Logger logger;
    private Clock clock;
    private int minDistanceMillis;
    private Map<String , LoggerWrapper> loggerWrappers = new ConcurrentHashMap<>();

    /**
     * @See {@link #LogThrottle(Logger, int, Clock)}
     */
    public LogThrottle(Logger logger) {
        this(logger, DEFAULT_MIN_DISTANCE_MILLIS);
    }

    /**
     * @See {@link #LogThrottle(Logger, int, Clock)}
     */
    public LogThrottle(Logger logger, int minDistance) {
        this(logger, minDistance, Clock.systemDefaultZone());
    }

    /**
     * @param logger Underlying sl4j logger. All logs will be forwarded to this logger.
     * @param minDistanceMillis Minimum time distance of a repeated log from its predecessor to be
     * reported by throttle.
     * @param clock The instance of clock used to measure distance between log messages.
     */
    LogThrottle(Logger logger, int minDistanceMillis, Clock clock) {
        this.logger = logger;
        this.minDistanceMillis = minDistanceMillis;
        this.clock = clock;
    }

    /**
     * @param type type of the message which the caller wants to log using the returned logger.
     * @return the logger which is responsible for logging this kind of messages.
     */
    public Logger logger(String type) {
        return loggerWrappers.computeIfAbsent(
                type, t -> new LoggerWrapper(logger, minDistanceMillis, clock));
    }

    /**
     * @param e the exception which the caller wants to log using the returned logger.
     * Its underlying stack trace indicates the message type.
     * @return the logger which is responsible for logging this kind of messages.
     */
    public Logger logger(Exception e) {
        return logger(Arrays.toString(e.getStackTrace()));
    }
}
