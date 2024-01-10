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
import com.baiyi.opscloud.sshserver.postprocess.PostProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * Post processor used to save console result into file
 */
@Slf4j
public class SavePostProcessor
        implements PostProcessor<Object, String> {

    public static final String SAVE = "save";

    private static final String REPLACE_REGEX = "(\\x1b\\x5b|\\x9b)[\\x30-\\x3f]*[\\x20-\\x2f]*[\\x40-\\x7e]";

    @Override
    public String getName() {
        return SAVE;
    }

    @Override
    public String getDescription() {
        return "Post processor to save result to file (or use special character '>')";
    }

    @Override
    public String process(Object result, List<String> parameters) throws PostProcessorException {
        if (parameters == null || parameters.isEmpty()) {
            throw new PostProcessorException("Cannot save without file path !");
        } else {
            if (parameters.size() != 1) {
                log.debug("[{}] post processor only need one parameter, rest will be ignored", getName());
            }
            String path = parameters.getFirst();
            if (path == null || path.isEmpty()) {
                throw new PostProcessorException("Cannot save without file path !");
            }
            File file = new File(path);
            try {
                String toWrite = string(result).replaceAll(REPLACE_REGEX, "") + "\n";
                Files.writeString(file.toPath(), toWrite, CREATE, APPEND);
                return "Result saved to file: " + file.getAbsolutePath();
            } catch (IOException e) {
                log.debug("Unable to write to file: " + file.getAbsolutePath(), e);
                throw new PostProcessorException("Unable to write to file: " + file.getAbsolutePath() + ". " + e.getMessage(), e);
            }
        }
    }

    private String string(Object result) {
        if (result instanceof String) {
            return (String) result;
        } else if (result instanceof Throwable) {
            return ((Throwable) result).getClass().getName() + ": " + ((Throwable) result).getMessage();
        } else if (result != null) {
            return result.toString();
        } else {
            return "";
        }
    }

}