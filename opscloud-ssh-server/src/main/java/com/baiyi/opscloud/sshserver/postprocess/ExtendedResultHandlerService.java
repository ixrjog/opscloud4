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

package com.baiyi.opscloud.sshserver.postprocess;

import com.baiyi.opscloud.sshserver.SshContext;
import lombok.extern.slf4j.Slf4j;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.shell.ResultHandlerService;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;

/**
 * Result handler service which save stacktrace in context (for stacktrace command)
 * and optionally apply post processors if requested in command line
 */
@Slf4j
@Component
@Primary
public class ExtendedResultHandlerService implements ResultHandlerService {

    public static final ThreadLocal<Throwable> THREAD_CONTEXT = ThreadLocal.withInitial(() -> null);

    private final ResultHandlerService delegate;
    private final Map<String, PostProcessor<?, ?>> postProcessorMap = new HashMap<>();

    public ExtendedResultHandlerService(ResultHandlerService resultHandlerService, List<PostProcessor<?, ?>> postProcessorList) {
        this.delegate = resultHandlerService;
        if (postProcessorList != null) {
            for (PostProcessor<?, ?> postProcessor : postProcessorList) {
                if (this.postProcessorMap.containsKey(postProcessor.getName())) {
                    log.warn("Unable to register post processor for name [{}], it has already been registered",
                            postProcessor.getName());
                } else {
                    this.postProcessorMap.put(postProcessor.getName(), postProcessor);
                    log.debug("Post processor with name [{}] registered", postProcessor.getName());
                }
            }
        }
    }

    @Override
    public void handle(Object result) {
        handle(result, TypeDescriptor.forObject(result));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void handle(Object result, TypeDescriptor resultType) {
        if (result == null) {
            return;
        }
        if (result instanceof Throwable) {
            THREAD_CONTEXT.set((Throwable) result);
        }
        Object obj = result;
        SshContext ctx = SSH_THREAD_CONTEXT.get();
        if (ctx != null && ctx.getPostProcessorsList() != null) {
            for (PostProcessorObject postProcessorObject : ctx.getPostProcessorsList()) {
                String name = postProcessorObject.getName();
                PostProcessor postProcessor = postProcessorMap.get(name);
                if (postProcessor == null) {
                    printLogWarn("Unknown post processor [" + name + "]");
                    continue;
                }
                Class<?> cls =
                        ((Class<?>) ((ParameterizedType) (postProcessor.getClass().getGenericInterfaces())[0]).getActualTypeArguments()[0]);
                if (!cls.isAssignableFrom(obj.getClass())) {
                    printLogWarn("Post processor [" + name + "] can only apply to class [" + cls.getName() +
                            "] (current object class is " + obj.getClass().getName() + ")");
                } else {
                    log.debug("Applying post processor [{}] with parameters {}", name,
                            postProcessorObject.getParameters());
                    try {
                        obj = postProcessor.process(obj, postProcessorObject.getParameters());
                    } catch (Exception e) {
                        printError(e.getMessage());
                        return;
                    }
                }
            }
        }
        if (ctx == null || !ctx.isBackground()) {
            // do not display anything if is background script
            delegate.handle(obj);
        }
        if (ctx != null && ctx.isBackground()) {
            ctx.incrementBackgroundCount();
        }
    }

    private void printLogWarn(String warn) {
        delegate.handle(new AttributedString(warn, AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).toAnsi());
        log.warn(warn);
    }

    private void printError(String error) {
        delegate.handle(new AttributedString(error, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).toAnsi());
    }

}