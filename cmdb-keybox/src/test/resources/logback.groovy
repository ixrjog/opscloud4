import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.INFO

scan("10 seconds")

appender("STDOUT-APPENDER", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
}
appender("CORE-APPENDER", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        maxHistory = 60
        fileNamePattern = "../core/core-%d{yyyy-MM-dd}.log.zip"
    }
}

logger("coreLogger", WARN, ["CORE-APPENDER"], false)

logger("com.sdg.cmdb.dao", DEBUG, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.service", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.service.impl", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.util", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.controller", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.filter", INFO, ["STDOUT-APPENDER"], false)
root(ERROR, ["STDOUT-APPENDER"])
