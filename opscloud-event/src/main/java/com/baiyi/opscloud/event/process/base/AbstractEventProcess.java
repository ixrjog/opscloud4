package com.baiyi.opscloud.event.process.base;

import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.provider.base.common.SimpleDsInstanceProvider;
import com.baiyi.opscloud.domain.base.IRecover;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/10/9 3:14 下午
 * @Version 1.0
 */
public abstract class AbstractEventProcess<E extends IRecover> extends SimpleDsInstanceProvider implements IEventProcess, InitializingBean {

    protected static final int dsInstanceBusinessType = BusinessTypeEnum.DATASOURCE_INSTANCE.getType();

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
            recordEvents(i, events);
        });
    }

    /**
     * 记录事件
     * @param dsInstance
     * @param events
     */
    private void recordEvents(DatasourceInstance dsInstance, List<E> events) {
        // 新事件
        Map<String, Event> newEventMap = events.stream().map(e ->
                recordEvent(dsInstance, e)
        ).collect(Collectors.toMap(Event::getEventId, a -> a, (k1, k2) -> k1));
        // 活跃事件
        List<Event> activeEvents = eventService.queryEventByInstance(dsInstance.getUuid());
        retrospectiveEvents(newEventMap, activeEvents);
    }

    /**
     * 回顾事件
     * @param newEventMap
     * @param activeEvents
     */
    protected void retrospectiveEvents(Map<String, Event> newEventMap, List<Event> activeEvents) {
        if (CollectionUtils.isEmpty(activeEvents)) return; // 无活跃事件
        activeEvents.forEach(e -> {
            if (!newEventMap.containsKey(e.getEventId())) {
                E eventMessage = getByEventId(e.getInstanceUuid(), e.getEventId());
                if (eventMessage != null && eventMessage.isRecover()) {
                    e.setIsActive(false);
                    e.setExpiredTime(new Date());
                    eventService.update(e); // 恢复事件
                }
            }
        });
    }

    /**
     * 数据源查询事件
     * @param instanceUuid
     * @param eventId
     * @return
     */
    abstract protected E getByEventId(String instanceUuid, String eventId);

    /**
     * 记录事件
     * @param dsInstance
     * @param e
     * @return
     */
    private Event recordEvent(DatasourceInstance dsInstance, E e) {
        Event pre = toEvent(dsInstance, e);
        Event event = eventService.getByUniqueKey(dsInstance.getUuid(), pre.getEventId());
        if (event == null) {
            eventService.add(pre);
            return pre;
        }
        return event;
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
                        baseTagService.hasBusinessTag(EVENT_TAG, dsInstanceBusinessType, e.getId())
                ).collect(Collectors.toList())
        );
        return instances;
    }

    abstract protected Event toEvent(DatasourceInstance dsInstance, E e);

    @Override
    public void afterPropertiesSet() {
        EventFactory.register(this);
    }
}
