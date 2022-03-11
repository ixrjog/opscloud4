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

import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessorObject;
import com.baiyi.opscloud.sshserver.postprocess.provided.SavePostProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.baiyi.opscloud.sshserver.ExtendedInput.*;
import static com.baiyi.opscloud.sshserver.SshShellCommandFactory.SSH_THREAD_CONTEXT;


/**
 * Extended shell which takes in account special characters
 */
@Slf4j
public class ExtendedShell extends Shell {

    private final ResultHandler resultHandler;

    private final List<String> postProcessorNames = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param resultHandler  result handler
     * @param postProcessors post processors list
     */
    public ExtendedShell(ResultHandler resultHandler, List<PostProcessor> postProcessors) {
        super(resultHandler);
        this.resultHandler = resultHandler;
        if (postProcessors != null) {
            postProcessorNames.addAll(postProcessors.stream().map(PostProcessor::getName).collect(Collectors.toList()));
        }
    }


    @Override
    public void run(InputProvider inputProvider) {
        run(inputProvider, () -> false);
    }

    @SuppressWarnings("unchecked")
    public void run(InputProvider inputProvider, ShellNotifier shellNotifier) {
        Object result = null;
        // Handles ExitRequest thrown from Quit command
        while (!(result instanceof ExitRequest) && !shellNotifier.shouldStop()) {
            Input input;
            try {
                input = inputProvider.readInput();
            } catch (ExitRequest e) {
                // Handles ExitRequest thrown from hitting CTRL-C
                break;
            } catch (Exception e) {
                resultHandler.handleResult(e);
                continue;
            }
            if (input == null) {
                break;
            }

            result = evaluate(input);
            if (result != NO_INPUT && !(result instanceof ExitRequest)) {
                resultHandler.handleResult(result);
            }
        }
    }

    @Override
    public Object evaluate(Input input) {
        List<String> words = input.words();
        Object toReturn = super.evaluate(new ExtendedInput(input));
        SshContext ctx = SSH_THREAD_CONTEXT.get();
        if (ctx != null) {
            if (!ctx.isBackground()) {
                // clear potential post processors from previous commands
                ctx.getPostProcessorsList().clear();
            }
            if (isKeyCharInList(words)) {
                List<Integer> indexes =
                        IntStream.range(0, words.size()).filter(i -> KEY_CHARS.contains(words.get(i))).boxed().collect(Collectors.toList());
                for (Integer index : indexes) {
                    if (words.size() > index + 1) {
                        String keyChar = words.get(index);
                        if (keyChar.equals(PIPE)) {
                            String postProcessorName = words.get(index + 1);
                            int currentIndex = 2;
                            String word = words.size() > index + currentIndex ? words.get(index + currentIndex) : null;
                            List<String> params = new ArrayList<>();
                            while (word != null && !KEY_CHARS.contains(word)) {
                                params.add(word);
                                currentIndex++;
                                word = words.size() > index + currentIndex ? words.get(index + currentIndex) : null;
                            }
                            ctx.getPostProcessorsList().add(new PostProcessorObject(postProcessorName, params));
                        } else if (keyChar.equals(ARROW)) {
                            ctx.getPostProcessorsList().add(new PostProcessorObject(SavePostProcessor.SAVE,
                                    Collections.singletonList(words.get(index + 1))));
                        }
                    }
                }
                log.debug("Found {} post processors", ctx.getPostProcessorsList().size());
            }
        }
        return toReturn;
    }

    @Override
    public List<CompletionProposal> complete(CompletionContext context) {
        if (context.getWords().contains("|")) {
            return postProcessorNames.stream().map(CompletionProposal::new).collect(Collectors.toList());
        }
        return super.complete(context);
    }

    private static boolean isKeyCharInList(List<String> strList) {
        for (String key : KEY_CHARS) {
            if (strList.contains(key)) {
                return true;
            }
        }
        return false;
    }

    @FunctionalInterface
    public interface ShellNotifier {
        boolean shouldStop();
    }

}
