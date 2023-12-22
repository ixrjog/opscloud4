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

import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Grep post processor
 */
@Slf4j
public class GrepPostProcessor
        implements PostProcessor<String, String> {

    @Override
    public String getName() {
        return "grep";
    }

    @Override
    public String getDescription() {
        return "Find pattern in result lines";
    }

    @Override
    public String process(String result, List<String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            log.debug("Cannot use [{}] post processor without any parameters", getName());
            return result;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String line : result.split("\n")) {
                if (contains(line, parameters)) {
                    sb.append(line).append("\n");
                }
            }
            return sb.toString().isEmpty() ? sb.toString() : sb.substring(0, sb.toString().length() - 1);
        }
    }

    private boolean contains(String line, List<String> parameters) {
        for (String parameter : parameters) {
            if (parameter == null || parameter.isEmpty() || line.contains(parameter)) {
                return true;
            }
        }
        return false;
    }

}