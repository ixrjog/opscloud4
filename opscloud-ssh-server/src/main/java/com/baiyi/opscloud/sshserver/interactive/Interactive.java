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

package com.baiyi.opscloud.sshserver.interactive;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import org.jline.terminal.Size;

import java.util.List;

/**
 * Interactive bean
 */
@Builder
@Getter
public class Interactive {

    @NonNull
    private InteractiveInput input;

    @Builder.Default
    private long refreshDelay = 3000;

    @Builder.Default
    private boolean fullScreen = true;

    @Builder.Default
    private boolean exit = true;

    @Builder.Default
    private boolean increase = true;

    @Builder.Default
    private boolean decrease = true;

    @Singular
    private List<KeyBinding> bindings;

    private Size size;

}