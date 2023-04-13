/**
 * Copyright (C) 2013 Loophole, LLC
 * <p>
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * As a special exception, the copyright holders give permission to link the
 * code of portions of this program with the OpenSSL library under certain
 * conditions as described in each individual source file and distribute
 * linked combinations including the program with the OpenSSL library. You
 * must comply with the GNU Affero General Public License in all respects for
 * all of the code used other than as permitted herein. If you modify file(s)
 * with this exception, you may extend this exception to your version of the
 * file(s), but you are not obligated to do so. If you do not wish to do so,
 * delete this exception statement from your version. If you delete this
 * exception statement from all source files in the program, then also delete
 * it in the license file.
 */
package com.baiyi.opscloud.sshcore.model;

import com.baiyi.opscloud.domain.model.SshCredential;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value object that contains host system information
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSystem {

    private Long id;
    private String displayNm;
    private String host;
    @Builder.Default
    private Integer port = 22;
    private String displayLabel;
    @Builder.Default
    private String authorizedKeys = "~/.ssh/authorized_keys";
    @Builder.Default
    private Boolean checked = false;
    @Builder.Default
    private String statusCd = INITIAL_STATUS;
    private String errorMsg;

    private String instanceId;

    private org.jline.terminal.Size terminalSize;

    /**
     * 登录凭据
     */
    private SshCredential sshCredential;

    private ServerMessage.BaseMessage loginMessage;

    public static final String INITIAL_STATUS = "INITIAL";
    public static final String AUTH_FAIL_STATUS = "AUTHFAIL";
    public static final String PUBLIC_KEY_FAIL_STATUS = "KEYAUTHFAIL";
    public static final String GENERIC_FAIL_STATUS = "GENERICFAIL";
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String HOST_FAIL_STATUS = "HOSTFAIL";

}