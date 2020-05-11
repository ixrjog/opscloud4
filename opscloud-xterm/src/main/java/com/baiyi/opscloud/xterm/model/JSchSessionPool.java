package com.baiyi.opscloud.xterm.model;

import org.apache.commons.collections4.map.HashedMap;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:43 下午
 * @Version 1.0
 */
public class JSchSessionPool {

    private static Map<String, Map<String, JSchSession>> jSchSessionMap = new HashedMap<>();
}
