package com.baiyi.opscloud.facade.export.impl;

import cn.afterturn.easypoi.csv.CsvExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.baiyi.opscloud.builder.export.ExportTaskBuilder;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.decorator.export.ExportTaskDecorator;
import com.baiyi.opscloud.decorator.it.ItAssetDecorator;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcExportTask;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAsset;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetApply;
import com.baiyi.opscloud.domain.param.export.ExportParam;
import com.baiyi.opscloud.domain.vo.export.ExportVO;
import com.baiyi.opscloud.domain.vo.it.ItAssetVO;
import com.baiyi.opscloud.facade.export.ExportTaskFacade;
import com.baiyi.opscloud.service.export.OcExportTaskService;
import com.baiyi.opscloud.service.it.OcItAssetApplyService;
import com.baiyi.opscloud.service.it.OcItAssetService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.baiyi.opscloud.common.base.Global.ASYNC_POOL_TASK_EXECUTOR;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 2:00 下午
 * @Since 1.0
 */

@Slf4j
@Component
public class ExportTaskFacadeImpl implements ExportTaskFacade {

    private interface ExportTaskType {
        Integer asset = 1;
        Integer assetApply = 2;
    }

    private final static String EXCEL_FILE_SUFFIX = ".xlsx";

    private final static String EXCEL_FILE_PATH = "/res/static/export";

    private final static Integer EXPORT_INTERVAL = 3;

    @Resource
    private OcExportTaskService ocExportTaskService;

    @Resource
    private OpscloudConfig opscloudConfig;

    @Resource
    private OcItAssetService ocItAssetService;

    @Resource
    private ItAssetDecorator itAssetDecorator;

    @Resource
    private ExportTaskDecorator exportTaskDecorator;

    @Resource
    private OcItAssetApplyService ocItAssetApplyService;


    @Override
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void exportAssetAll(String username) {
        String fileName = Joiner.on("-").join("itAsset", new Date().getTime());
        OcExportTask ocExportTask = ExportTaskBuilder.build(username, fileName, ExportTaskType.asset);
        if (checkTask(ocExportTask)) return;
        ocExportTaskService.addOcExportTask(ocExportTask);
        List<OcItAsset> ocItAssetList = ocItAssetService.queryOcItAssetAll();
        List<ItAssetVO.Asset> assetList = itAssetDecorator.decoratorVOList(ocItAssetList);
        simpleExport(ocExportTask, ItAssetVO.Asset.class, assetList);
    }

    @Override
    @Async(value = ASYNC_POOL_TASK_EXECUTOR)
    public void exportAssetApplyAll(String username) {
        String fileName = Joiner.on("-").join("itAssetApply", new Date().getTime());
        OcExportTask ocExportTask = ExportTaskBuilder.build(username, fileName, ExportTaskType.assetApply);
        if (checkTask(ocExportTask)) return;
        ocExportTaskService.addOcExportTask(ocExportTask);
        List<OcItAssetApply> ocItAssetApplyList = ocItAssetApplyService.queryOcItAssetApplyAll();
        List<ItAssetVO.AssetApply> assetApplyList = itAssetDecorator.decoratorApplyVOList(ocItAssetApplyList);
        simpleExport(ocExportTask, ItAssetVO.AssetApply.class, assetApplyList);
    }

    private void simpleExport(OcExportTask ocExportTask, Class<?> clazz, Collection<?> dataSet) {
        String path = Joiner.on("").join(opscloudConfig.getDataPath(), EXCEL_FILE_PATH);
        File saveFile = new File(path);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        try {
            String file = Joiner.on("").join(path, File.separator, ocExportTask.getFileName(), EXCEL_FILE_SUFFIX);
            Workbook workbook = CsvExportUtil.exportBigExcel(new ExportParams(), clazz, dataSet);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            ocExportTask.setTaskStatus(2);
            ocExportTask.setEndTime(new Date());
            ocExportTaskService.updateOcExportTask(ocExportTask);
        } catch (IOException e) {
            log.error("导出失败,taskId:{}", ocExportTask.getId());
            ocExportTask.setTaskStatus(0);
            ocExportTask.setEndTime(new Date());
            ocExportTaskService.updateOcExportTask(ocExportTask);
        }
    }

    private boolean checkTask(OcExportTask ocExportTask) {
        List<OcExportTask> taskList = ocExportTaskService.queryOcExportTaskByType(ocExportTask.getUsername(), ocExportTask.getTaskType());
        return taskList.stream().anyMatch(
                x -> TimeUtils.calculateDateAgoMinute(x.getStartTime()) < EXPORT_INTERVAL
        );
    }

    @Override
    public DataTable<ExportVO.Task> queryOcExportTaskPage(ExportParam.PageQuery pageQuery) {
        DataTable<OcExportTask> table = ocExportTaskService.queryOcExportTaskPage(pageQuery);
        List<ExportVO.Task> page = exportTaskDecorator.decoratorVOList(table.getData());
        return new DataTable<>(page, table.getTotalNum());
    }
}
