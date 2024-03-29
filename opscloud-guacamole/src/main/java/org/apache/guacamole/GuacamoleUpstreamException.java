/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.guacamole;

import org.apache.guacamole.protocol.GuacamoleStatus;

import java.io.Serial;


/**
 * An exception which indicates than an upstream server (such as the remote
 * desktop) is returning an error or is otherwise unreachable.
 */
public class GuacamoleUpstreamException extends GuacamoleException {

    @Serial
    private static final long serialVersionUID = -6514432656216873706L;

    /**
     * Creates a new GuacamoleUpstreamException with the given message and
     * cause.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     * @param cause The cause of this exception.
     */
    public GuacamoleUpstreamException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new GuacamoleUpstreamException with the given message.
     *
     * @param message A human readable description of the exception that
     *                occurred.
     */
    public GuacamoleUpstreamException(String message) {
        super(message);
    }

    /**
     * Creates a new GuacamoleUpstreamException with the given cause.
     *
     * @param cause The cause of this exception.
     */
    public GuacamoleUpstreamException(Throwable cause) {
        super(cause);
    }

    @Override
    public GuacamoleStatus getStatus() {
        return GuacamoleStatus.UPSTREAM_ERROR;
    }

}