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

package com.baiyi.opscloud.sshserver.postprocess.provided;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Grep post processor
 */
@Slf4j
public class HighlightPostProcessor
        implements PostProcessor<String, String> {

    @Override
    public String getName() {
        return "highlight";
    }

    @Override
    public String getDescription() {
        return "Highlight some words in result";
    }

    @Override
    public String process(String result, List<String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            log.debug("Cannot use [{}] post processor without any parameters", getName());
            return result;
        } else {
            String finalResult = result;
            for (String toHighlight : parameters) {
                finalResult = finalResult.replaceAll(toHighlight,
                        SshShellHelper.getBackgroundColoredMessage(toHighlight,
                                PromptColor.YELLOW));
            }
            return finalResult;
        }
    }

}