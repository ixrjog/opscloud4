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

import com.baiyi.opscloud.sshserver.postprocess.PostProcessorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Pretty json post processor
 */
@Slf4j
@AllArgsConstructor
public class PrettyJsonPostProcessor
        implements PostProcessor<Object, String> {

    private final ObjectMapper mapper;

    @Override
    public String getName() {
        return "pretty";
    }

    @Override
    public String getDescription() {
        return "Pretty print thanks to json format";
    }

    @Override
    public String process(Object result, List<String> parameters) throws PostProcessorException {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            log.warn("Unable to prettify object: {}", result);
            throw new PostProcessorException("Unable to prettify object. " + e.getMessage(), e);
        }
    }

}