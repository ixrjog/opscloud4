/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver;

import com.baiyi.opscloud.sshserver.listeners.SshShellListener;
import com.baiyi.opscloud.sshserver.listeners.SshShellListenerService;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import com.baiyi.opscloud.sshserver.postprocess.TypePostProcessorResultHandler;
import com.baiyi.opscloud.sshserver.postprocess.provided.*;
import com.baiyi.opscloud.sshserver.provider.ExtendedFileValueProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.server.SshServer;
import org.jline.reader.LineReader;
import org.jline.reader.Parser;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.shell.ResultHandler;
import org.springframework.shell.Shell;
import org.springframework.shell.SpringShellAutoConfiguration;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.shell.standard.ValueProvider;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;
import static com.baiyi.opscloud.sshserver.SshShellProperties.SSH_SHELL_ENABLE;

/**
 * <p>Ssh shell auto configuration</p>
 * <p>Can be disabled by property <b>ssh.shell.enable=false</b></p>
 */
@Slf4j
@Configuration
@ConditionalOnClass(SshServer.class)
@ConditionalOnProperty(name = SSH_SHELL_ENABLE, havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({SshShellProperties.class})
@AutoConfigureAfter(value = {
        SpringShellAutoConfiguration.class
}, name = {
        "org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.beans.BeansEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.condition.ConditionsReportEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.context.properties" +
                ".ConfigurationPropertiesReportEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.context.ShutdownEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.endpoint.EndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.env.EnvironmentEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.flyway.FlywayEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.info.InfoEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.jolokia.JolokiaEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.liquibase.LiquibaseEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.logging.LogFileWebEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.management.HeapDumpWebEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.management.ThreadDumpEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.metrics.MetricsEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.scheduling.ScheduledTasksEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.session.SessionsEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceEndpointAutoConfiguration",
        "org.springframework.boot.actuate.autoconfigure.web.mappings.MappingsEndpointAutoConfiguration"
})
@ComponentScan(basePackages = {"com.baiyi.opscloud.sshserver"})
public class SshShellAutoConfiguration {

    private static final String TERMINAL_DELEGATE = "terminalDelegate";

    public ApplicationContext context;

    private final ConfigurableEnvironment environment;

    private final SshShellProperties properties;

    public SshShellAutoConfiguration(ApplicationContext context, ConfigurableEnvironment environment,
                                     SshShellProperties properties) {
        this.context = context;
        this.environment = environment;
        this.properties = properties;
    }

    /**
     * Initialize ssh shell auto config
     */
    @PostConstruct
    public void init() {
        if (context.getEnvironment().getProperty("spring.main.lazy-initialization", Boolean.class, false)) {
            log.info("Lazy initialization enabled, calling configuration bean explicitly to start ssh server");
            context.getBean(SshShellConfiguration.SshServerLifecycle.class);
            // also need to get terminal to initialize thread context of main thread
            context.getBean(Terminal.class);
        }
        if (!properties.getPrompt().getLocal().isEnable()) {
            InteractiveShellApplicationRunner.disable(environment);
        }
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Primary
    public ExtendedShell sshShell(@Qualifier("main") ResultHandler<Object> resultHandler, List<PostProcessor> postProcessors) {
        return new ExtendedShell(new TypePostProcessorResultHandler(resultHandler, postProcessors), postProcessors);
    }

    @Bean
    @Primary
    public ExtendedCompleterAdapter sshCompleter(Shell shell) {
        return new ExtendedCompleterAdapter(shell);
    }

    // value providers

    @Bean
    public ValueProvider extendedFileValueProvider() {
        return new ExtendedFileValueProvider(properties.isExtendedFileProvider());
    }

    // post processors

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    public JsonPointerPostProcessor jsonPointerPostProcessor() {
        return new JsonPointerPostProcessor();
    }

    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    public PrettyJsonPostProcessor prettyJsonPostProcessor() {
        return new PrettyJsonPostProcessor();
    }

    @Bean
    public SavePostProcessor savePostProcessor() {
        return new SavePostProcessor();
    }

    @Bean
    public GrepPostProcessor grepPostProcessor() {
        return new GrepPostProcessor();
    }

    @Bean
    public HighlightPostProcessor highlightPostProcessor() {
        return new HighlightPostProcessor();
    }

    @Bean
    public SshShellHelper sshShellHelper() {
        return new SshShellHelper(properties.getConfirmationWords());
    }


    /**
     * Primary terminal which delegates with right session
     *
     * @param terminal   jline terminal
     * @param lineReader jline line reader
     * @return terminal
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean(TERMINAL_DELEGATE)
    @Primary
    public Terminal terminal(Terminal terminal, LineReader lineReader) {
        if (properties.getPrompt().getLocal().isEnable()) {
            // local prompt enable, add ssh context in main thread
            SSH_THREAD_CONTEXT.set(new SshContext(null, terminal, lineReader, null));
        }
        return new SshShellTerminalDelegate(terminal);
    }

    /**
     * Primary prompt provider
     *
     * @return prompt provider
     */
    @Bean
    @ConditionalOnMissingBean
    public PromptProvider sshPromptProvider() {
        return () -> new AttributedString(properties.getPrompt().getText(),
                AttributedStyle.DEFAULT.foreground(properties.getPrompt().getColor().toJlineAttributedStyle()));
    }

    /**
     * Creates ssh listener service
     *
     * @param listeners found listeners in context
     * @return listener service
     */
    @Bean
    public SshShellListenerService sshShellListenerService(@Autowired(required = false) List<SshShellListener> listeners) {
        return new SshShellListenerService(listeners);
    }

    /**
     * Primary shell application runner which answers true to {@link InteractiveShellApplicationRunner#isEnabled()}
     *
     * @param lineReader     line reader
     * @param promptProvider prompt provider
     * @param parser         parser
     * @param shell          spring shell
     * @param environment    spring environment
     * @return shell application runner
     */
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    @Primary
    public InteractiveShellApplicationRunner sshInteractiveShellApplicationRunner(LineReader lineReader,
                                                                                  PromptProvider promptProvider,
                                                                                  Parser parser, Shell shell,
                                                                                  Environment environment) {
        return new InteractiveShellApplicationRunner(lineReader, promptProvider, parser, shell, environment) {

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public void run(ApplicationArguments args) {
                // do nothing
            }
        };
    }
}

