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

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import org.springframework.shell.table.Aligner;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.List;

/**
 * Simple data builder, with header names, and list of lines, containing map with header names.
 * Optionally set aligner, and style
 */
@Data
@Builder
public class SimpleTable {

    @Singular
    private List<String> columns;

    @Builder.Default
    private boolean displayHeaders = true;

    @Singular
    private List<Aligner> headerAligners;

    @NonNull
    @Singular
    private List<List<Object>> lines;

    @Singular
    private List<Aligner> lineAligners;

    @Builder.Default
    private boolean useFullBorder = true;

    @Builder.Default
    private BorderStyle borderStyle = BorderStyle.fancy_light;

    private SimpleTableBuilderListener tableBuilderListener;

    /**
     * Listener to add some properties to table builder before it is rendered
     */
    @FunctionalInterface
    public interface SimpleTableBuilderListener {

        /**
         * Method called before render
         *
         * @param tableBuilder table builder
         */
        void onBuilt(TableBuilder tableBuilder);
    }

}