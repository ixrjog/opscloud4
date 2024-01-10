package com.baiyi.opscloud.leo.domain.model;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/9/4 15:14
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JenkinsInstanceTask {

    @Builder.Default
    private List<Task> instanceTasks = Lists.newArrayList();

    public void sort() {
        Collections.sort(this.instanceTasks);
    }

    public int getIndex() {
        if (CollectionUtils.isEmpty(instanceTasks)) {
            return 0;
        }
        return instanceTasks.getFirst().getIndex();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Task implements Comparable<Task> {

        @Schema(description = "Instance Name")
        private String name;

        private int task;

        private int index;

        @Override
        public int compareTo(Task o) {
            return (o.getTask() - this.getTask()) * -1;
        }
    }

}