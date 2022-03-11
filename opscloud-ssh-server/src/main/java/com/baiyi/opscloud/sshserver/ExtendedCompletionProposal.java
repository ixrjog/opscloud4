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

import org.springframework.shell.CompletionProposal;

/**
 * Extended completion proposal to be able to set complete attribute of proposal
 */
public class ExtendedCompletionProposal extends CompletionProposal {

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /**
     * If should add space after proposed proposal
     */
    private boolean complete;

    /**
     * Default constructor
     *
     * @param value    string value
     * @param complete true if should add space after proposed proposal (true is default value when not using
     *                 extended completion proposal)
     */
    public ExtendedCompletionProposal(String value, boolean complete) {
        super(value);
        this.complete = complete;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ExtendedCompletionProposal)) return false;
        final ExtendedCompletionProposal other = (ExtendedCompletionProposal) o;
        if (!other.canEqual((Object) this)) return false;
        return this.isComplete() == other.isComplete();
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ExtendedCompletionProposal;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + (this.isComplete() ? 79 : 97);
        return result;
    }

}
