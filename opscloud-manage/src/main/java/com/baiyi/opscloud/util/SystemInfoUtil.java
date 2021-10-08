package com.baiyi.opscloud.util;

import com.baiyi.opscloud.domain.generator.opscloud.Instance;
import com.baiyi.opscloud.domain.vo.sys.InstanceVO;
import com.baiyi.opscloud.domain.vo.sys.SystemVO;
import com.google.common.base.Joiner;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.Util;

/**
 * @Author baiyi
 * @Date 2021/9/6 2:26 下午
 * @Version 1.0
 */
public class SystemInfoUtil {

    private SystemInfoUtil() {
    }

    private static final int OSHI_WAIT_SECOND = 1000;

    public static String buildKey(InstanceVO.RegisteredInstance registeredInstance) {
        return toKey(registeredInstance.getHostIp());
    }

    public static String buildKey(Instance instance) {
        return toKey(instance.getHostIp());
    }

    public static String toKey(String instanceIp) {
        return Joiner.on("_").join("system_info_instance_ip", instanceIp);
    }

    public static SystemVO.Info buildInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        CentralProcessor processor = hal.getProcessor();

        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        SystemVO.Cpu cpu = SystemVO.Cpu.builder()
                .cpuNum(processor.getLogicalProcessorCount())
                .total(totalCpu)
                .sys(cSys)
                .used(user)
                .free(idle)
                .wait((double) iowait)
                .build();

        // Memory 信息
        GlobalMemory memory = hal.getMemory();
        SystemVO.Mem mem = SystemVO.Mem.builder()
                .total(memory.getTotal())
                .used(memory.getTotal() - memory.getAvailable())
                .free(memory.getAvailable())
                .build();

        return SystemVO.Info.builder()
                .cpu(cpu)
                .mem(mem)
                .build();
    }
}
