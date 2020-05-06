package com.baiyi.opscloud.xterm.model;

import lombok.Data;

import java.io.PrintStream;

@Data
public class JSchSession {

    private String sessionId;

    private String instanceId;

    private PrintStream commander;
}
