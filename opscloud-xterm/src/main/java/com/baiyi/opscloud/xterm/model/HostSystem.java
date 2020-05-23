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
package com.baiyi.opscloud.xterm.model;

import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.xterm.message.BaseMessage;
import lombok.Data;

/**
 * Value object that contains host system information
 */
@Data
public class HostSystem {

    Long id;
    String displayNm;
   // String user;
    String host;
    Integer port = 22;
    String displayLabel;
    String authorizedKeys = "~/.ssh/authorized_keys";
    Boolean checked = false;
    String statusCd = INITIAL_STATUS;
    String errorMsg;
    // List<String> publicKeyList;
    String instanceId;

    /**
     * 登录凭据
     */
    private SSHKeyCredential sshKeyCredential;
    private BaseMessage initialMessage;

    public static final String INITIAL_STATUS = "INITIAL";
    public static final String AUTH_FAIL_STATUS = "AUTHFAIL";
    public static final String PUBLIC_KEY_FAIL_STATUS = "KEYAUTHFAIL";
    public static final String GENERIC_FAIL_STATUS = "GENERICFAIL";
    public static final String SUCCESS_STATUS = "SUCCESS";
    public static final String HOST_FAIL_STATUS = "HOSTFAIL";
}
