package com.baiyi.opscloud.event.process;

import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.Event;
import com.baiyi.opscloud.domain.param.datasource.DsInstanceParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.event.IEventProcess;
import com.baiyi.opscloud.event.factory.EventFactory;
import com.baiyi.opscloud.service.event.EventService;
import com.baiyi.opscloud.service.tag.BaseTagService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:14 下午
 * @Version 1.0
 */
public abstract class AbstractEventProcess<E> extends SimpleDsInstanceProvider implements IEventProcess, InitializingBean {

    protected static final int DsInstanceBusinessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

    protected static final String EVENT_TAG = "Event";

    @Resource
    private EventService eventService;

    @Resource
    protected DsConfigFactory dsFactory;

    @Resource
    private BaseTagService baseTagService;

    /**
     * 获取实例类型
     *
     * @return
     */
    abstract protected DsTypeEnum getDsInstanceType();

    /**
     * 监听事件
     *
     * @param dsInstance
     * @return
     */
    abstract protected List<E> listeningEvents(DatasourceInstance dsInstance);

    @Override
    public void listener() {
        List<DatasourceInstance> instances = listInstance();
        if (CollectionUtils.isEmpty(instances)) return;
        instances.forEach(i -> {
            List<E> events = listeningEvents(i);
            if (!CollectionUtils.isEmpty(instances)) {
                events.forEach(e -> recordEvent(i, e));
            }
        });
    }

    private void recordEvent(DatasourceInstance dsInstance, E e) {
        Event event = toEvent(dsInstance, e);
        eventService.add(event);
    }

    /**
     * 查询有效实例（包含标签）
     *
     * @return
     */
    protected List<DatasourceInstance> listInstance() {
        List<DatasourceInstance> instances = Lists.newArrayList();
        DsInstanceParam.DsInstanceQuery query = DsInstanceParam.DsInstanceQuery.builder()
                .instanceType(getDsInstanceType().getName())
                .build();
        // 过滤掉没有标签的实例
        instances.addAll(
                dsInstanceService.queryByParam(query).stream().filter(e ->
                        baseTagService.hasBusinessTag(EVENT_TAG, DsInstanceBusinessType, e.getId())
                ).collect(Collectors.toList())
        );
        return instances;
    }

    abstract Event toEvent(DatasourceInstance dsInstance, E e);

    @Override
    public void afterPropertiesSet() {
        EventFactory.register(this);
    }
}
