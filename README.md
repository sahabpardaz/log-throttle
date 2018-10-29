# Log Throttle
It is a throttle to avoid log bursts. You have your ordinary slf4j loggers but call them via throttle in potential burst scenarios.

In situations such as network failure or when a third party system is down, we may have a burst of logs produced by multiple worker threads. The failure can last for a while causing millions of log records which totally pollutes the log files. Sometimes we may implement Circuit Breaker pattern to settle it, but the initial burst for already triggered operations is still there.
Actually the bad effect is not limited to polluting log files. We have also encountered a bug in log4j which sometimes causes livelock in sharp bursts.

Log throttle targets this issue by aggregating logs of a same type if they are repeated during a small period. 

## Sample Usage

### Declaration
```
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
private static final LogThrottle logThrottle = new LogThrottle(logger, 1000);
```
Second argument to throttle constructor (i.e., 1000 in above sample) , defines the minimum time
distance of a repeated log from its predecessor to be reported by throttle.
Because logger and loggerThrottle are defined as static members, they are shared between
all instances of the class.

### Call
```
logThrottle.logger("NO RESPONSE").warn("No response from server, reqId: {}!", reqId);
```
The aggregation is based on the type of messages which are defined by user. For example
in the above example, the type of message is "NO RESPONSE".

You *should* have limited number of possible message types.
For example if you set the request ID in message type, its bad effect is not just preventing
aggregation, but also uncontrolled memory consumption due to track of so many message types in memory.

### Sample Output
> No response from remote server, reqId: 243567! -visited 2300 logs of same type in last 1000 millis

The second part of this message (number of visits) will be created by throttle itself.

### Hybrid Call

It is possible to call logger directly when you want to log all occurrences, e.g., with a lower level for debug purpose:

```
logger.debug("No response from server, reqId: {}!", reqId);
logThrottle.logger("NO RESPONSE").warn("No response from server, reqId: {}!", reqId);
```

### Aggregation by Exception Type
In situations when you want to log an exception, you may want to aggregate based on the exception:
```
try {
  something();
} catch (Exception e) {
  logThrottle.logger(e).warn("No response from server", e);
}
```
In aggregation by exception, we consider stack trace of the exception (not just its type) as the message type.

## Add it to your project
You can reference to this library by either of java build systems (Maven, Gradle, SBT or Leiningen) using snippets from this jitpack link:
[![](https://jitpack.io/v/sahabpardaz/log-throttle.svg)](https://jitpack.io/#sahabpardaz/log-throttle)
