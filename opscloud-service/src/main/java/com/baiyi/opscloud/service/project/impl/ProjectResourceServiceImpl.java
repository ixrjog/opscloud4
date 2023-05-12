package com.baiyi.opscloud.service.project.impl;

import com.baiyi.opscloud.mapper.ProjectResourceMapper;
import com.baiyi.opscloud.service.project.ProjectResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 修远
 * @Date 2023/5/12 5:31 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class ProjectResourceServiceImpl implements ProjectResourceService {

    private final ProjectResourceMapper projectResourceMapper;
}
