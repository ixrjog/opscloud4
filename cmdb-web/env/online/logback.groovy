import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.TRACE

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
        fileNamePattern = "/data/www/logs/apps/8080-cmdb/app/core/core-%d{yyyy-MM-dd}.log.zip"
    }
}

appender("CMD-APPENDER", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        maxHistory = 60
        fileNamePattern = "/data/www/logs/apps/8080-cmdb/app/cmd/cmd-%d{yyyy-MM-dd}.log.zip"
    }
}

appender("INFO-APPENDER", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        maxHistory = 60
        fileNamePattern = "/data/www/logs/apps/8080-cmdb/app/info/info-%d{yyyy-MM-dd}.log.zip"
    }
}
appender("TASK-APPENDER", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        maxHistory = 60
        fileNamePattern = "/data/www/logs/apps/8080-cmdb/app/task/task-%d{yyyy-MM-dd}.log.zip"
    }
}

appender("EXPLAIN-APPENDER", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{yyyy-MM-dd HH:mm:ss.SSSZ}|%contextName|%thread|%-5level|%logger|%msg%n"
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        maxHistory = 60
        fileNamePattern = "/data/www/logs/apps/8080-cmdb/app/explain/explain-%d{yyyy-MM-dd}.log.zip"
    }
}

logger("coreLogger", INFO, ["CORE-APPENDER"], false)
logger("cmdLogger", WARN, ["CMD-APPENDER"], false)
logger("explainLogger", INFO, ["EXPLAIN-APPENDER"], false)

logger("com.sdg.cmdb.dao", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.service", INFO, ["INFO-APPENDER"], false)
logger("com.sdg.cmdb.service.impl", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.keybox", INFO, ["STDOUT-APPENDER", "INFO-APPENDER"], false)
logger("com.sdg.cmdb.util", INFO, ["STDOUT-APPENDER", "INFO-APPENDER"], false)
logger("com.sdg.cmdb.controller", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.filter", INFO, ["STDOUT-APPENDER"], false)
logger("com.sdg.cmdb.scheduler", INFO, ["TASK-APPENDER"], false)    //任务
logger("org.springframework.ldap.core", INFO, ["STDOUT-APPENDER"], false)
logger("com.alibaba",INFO, ["STDOUT-APPENDER"], false)
logger("org.springframework.web.socket.handler", DEBUG, ["INFO-APPENDER"], false)

root(INFO, ["STDOUT-APPENDER"])
