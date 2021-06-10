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

package com.baiyi.caesar.sshserver;

import org.apache.sshd.common.channel.PtyMode;
import org.jline.terminal.Attributes;

import java.util.Map;

/**
 * Utility tools
 */
public final class SshShellUtils {

    private SshShellUtils() {
        // private constructor
    }

    /**
     * Fill attributes with given modes
     *
     * @param attr     attributes
     * @param ptyModes pty modes
     */
    public static void fill(Attributes attr, Map<PtyMode, Integer> ptyModes) {
        for (Map.Entry<PtyMode, Integer> e : ptyModes.entrySet()) {
            switch (e.getKey()) {
                case VINTR:
                    attr.setControlChar(Attributes.ControlChar.VINTR, e.getValue());
                    break;
                case VQUIT:
                    attr.setControlChar(Attributes.ControlChar.VQUIT, e.getValue());
                    break;
                case VERASE:
                    attr.setControlChar(Attributes.ControlChar.VERASE, e.getValue());
                    break;
                case VKILL:
                    attr.setControlChar(Attributes.ControlChar.VKILL, e.getValue());
                    break;
                case VEOF:
                    attr.setControlChar(Attributes.ControlChar.VEOF, e.getValue());
                    break;
                case VEOL:
                    attr.setControlChar(Attributes.ControlChar.VEOL, e.getValue());
                    break;
                case VEOL2:
                    attr.setControlChar(Attributes.ControlChar.VEOL2, e.getValue());
                    break;
                case VSTART:
                    attr.setControlChar(Attributes.ControlChar.VSTART, e.getValue());
                    break;
                case VSTOP:
                    attr.setControlChar(Attributes.ControlChar.VSTOP, e.getValue());
                    break;
                case VSUSP:
                    attr.setControlChar(Attributes.ControlChar.VSUSP, e.getValue());
                    break;
                case VDSUSP:
                    attr.setControlChar(Attributes.ControlChar.VDSUSP, e.getValue());
                    break;
                case VREPRINT:
                    attr.setControlChar(Attributes.ControlChar.VREPRINT, e.getValue());
                    break;
                case VWERASE:
                    attr.setControlChar(Attributes.ControlChar.VWERASE, e.getValue());
                    break;
                case VLNEXT:
                    attr.setControlChar(Attributes.ControlChar.VLNEXT, e.getValue());
                    break;
			/*
			case VFLUSH:
					attr.setControlChar(Attributes.ControlChar.VMIN, e.getValue());
					break;
			case VSWTCH:
					attr.setControlChar(Attributes.ControlChar.VTIME, e.getValue());
					break;
			*/
                case VSTATUS:
                    attr.setControlChar(Attributes.ControlChar.VSTATUS, e.getValue());
                    break;
                case VDISCARD:
                    attr.setControlChar(Attributes.ControlChar.VDISCARD, e.getValue());
                    break;
                case ECHO:
                    attr.setLocalFlag(Attributes.LocalFlag.ECHO, e.getValue() != 0);
                    break;
                case ICANON:
                    attr.setLocalFlag(Attributes.LocalFlag.ICANON, e.getValue() != 0);
                    break;
                case ISIG:
                    attr.setLocalFlag(Attributes.LocalFlag.ISIG, e.getValue() != 0);
                    break;
                case ICRNL:
                    attr.setInputFlag(Attributes.InputFlag.ICRNL, e.getValue() != 0);
                    break;
                case INLCR:
                    attr.setInputFlag(Attributes.InputFlag.INLCR, e.getValue() != 0);
                    break;
                case IGNCR:
                    attr.setInputFlag(Attributes.InputFlag.IGNCR, e.getValue() != 0);
                    break;
                case OCRNL:
                    attr.setOutputFlag(Attributes.OutputFlag.OCRNL, e.getValue() != 0);
                    break;
                case ONLCR:
                    attr.setOutputFlag(Attributes.OutputFlag.ONLCR, e.getValue() != 0);
                    break;
                case ONLRET:
                    attr.setOutputFlag(Attributes.OutputFlag.ONLRET, e.getValue() != 0);
                    break;
                case OPOST:
                    attr.setOutputFlag(Attributes.OutputFlag.OPOST, e.getValue() != 0);
                    break;
                default:
                    // nothing to do
            }
        }
    }

}
