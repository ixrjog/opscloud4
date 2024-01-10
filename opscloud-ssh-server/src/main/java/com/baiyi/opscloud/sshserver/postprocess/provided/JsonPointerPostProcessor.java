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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Json pointer post processor
 */
@Slf4j
@AllArgsConstructor
public class JsonPointerPostProcessor
        implements PostProcessor<String, String> {

    private final ObjectMapper mapper;

    @Override
    public String getName() {
        return "json";
    }

    @Override
    public String getDescription() {
        return "Json path (use pretty postprocessor first to get json from object)";
    }

    @Override
    public String process(String result, List<String> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            log.debug("Cannot use [{}] post processor without any parameters", getName());
        } else {
            if (parameters.size() != 1) {
                log.debug("[{}] post processor only need one parameter, rest will be ignored", getName());
            }
            String path = parameters.getFirst();
            try {
                JsonNode node = mapper.readTree(result).at(path);
                if (node.isMissingNode()) {
                    return "No node found with json path expression: " + path;
                } else {
                    if (node.isTextual()) {
                        return node.asText();
                    } else {
                        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
                    }
                }
            } catch (IOException e) {
                log.warn("Unable to read tree", e);
            } catch (IllegalArgumentException e) {
                log.warn("Illegal argument: " + path, e);
                return e.getMessage();
            }
        }
        return result;
    }

}