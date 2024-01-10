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

package com.baiyi.opscloud.sshserver.providers;

import com.baiyi.opscloud.sshserver.ExtendedCompletionProposal;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProvider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fixed file value provider (mostly for windows) and allow to not put space after proposal when directory
 */
@Component
public class ExtendedFileValueProvider implements ValueProvider {

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        String input = completionContext.currentWordUpToCursor();
        int lastSlash = input.lastIndexOf("/");
        File currentDir = lastSlash > -1 ? new File(input.substring(0, lastSlash + 1)) : new File("./");
        String prefix = input.substring(lastSlash + 1);

        File[] files = currentDir.listFiles((dir, name) -> name.startsWith(prefix));
        if (files == null || files.length == 0) {
            return Collections.emptyList();
        }
        return Arrays.stream(files)
                .map(f -> new ExtendedCompletionProposal(path(f), f.isFile()))
                .collect(Collectors.toList());
    }

    private static String path(File f) {
        String path = f.getPath().replaceAll("\\\\", "/");
        return f.isDirectory() ? path + "/" : path;
    }

}