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

package com.baiyi.opscloud.sshserver.command;

import com.baiyi.opscloud.sshserver.PromptColor;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import org.springframework.shell.table.Aligner;

/**
 * Add this aligner to color cell
 */
public class ColorAligner implements Aligner {

    private final PromptColor color;

    /**
     * Default constructor
     *
     * @param color the cell text color
     */
    public ColorAligner(PromptColor color) {
        this.color = color;
    }

    @Override
    public String[] align(String[] text, int cellWidth, int cellHeight) {
        String[] result = new String[text.length];
        for (int i = 0; i < text.length; i++) {
            result[i] = SshShellHelper.getColoredMessage(text[i], color);
        }
        return result;
    }

}