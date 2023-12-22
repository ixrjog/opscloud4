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

import org.springframework.shell.Input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Extended input which takes in account special characters
 */
public class ExtendedInput implements Input {

    public static final String PIPE = "|";

    public static final String ARROW = ">";

    public static final List<String> KEY_CHARS = Arrays.asList(PIPE, ARROW);

    private final Input base;

    /**
     * Default constructor
     *
     * @param base input base
     */
    public ExtendedInput(Input base) {
        this.base = base;
    }

    private static boolean isKeyCharInLine(String str) {
        for (String key : KEY_CHARS) {
            if (str.contains(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String rawText() {
        String raw = base.rawText();
        return raw != null && isKeyCharInLine(raw) ? raw.substring(0, firstIndexOfKeyChar(raw)) : raw;
    }

    @Override
    public List<String> words() {
        List<String> newList = new ArrayList<>();
        for (String word : base.words()) {
            if (KEY_CHARS.contains(word)) {
                return newList;
            }
            newList.add(word);
        }
        return newList;
    }

    private int firstIndexOfKeyChar(String str) {
        int firstIndex = Integer.MAX_VALUE;
        for (String key : KEY_CHARS) {
            int keyIndex = str.indexOf(key);
            if (keyIndex > -1 && keyIndex < firstIndex) {
                firstIndex = keyIndex;
            }
        }
        return firstIndex;
    }

}