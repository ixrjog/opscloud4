package com.sdg.cmdb.util

import com.sdg.cmdb.domain.auth.UserDO
import com.sdg.cmdb.domain.workflow.WorkflowTodoVO
import groovy.text.SimpleTemplateEngine
import groovy.text.markup.MarkupTemplateEngine
import groovy.text.markup.TemplateConfiguration


class TplWorkflowUtils {

    //public static String OC_URL = "https://oc.admin.gegejia.com/#/app/workflow"

    /**
     * 获取提交的工单邮件模板
     * @param todoDetailVO
     * @param notifyUserName
     * @return
     */
    public static String doWorkflowTodoNotify(WorkflowTodoVO workflowTodoVO, UserDO userDO, String externalUrl, String todoPhaseDesc) {
        def engine = new MarkupTemplateEngine(buildTplConfiguration());
        URL url = engine.resolveTemplate("emailTpl/workflow_todo_v2.tpl")

        Map<String, Object> binding = new HashMap<>();
        binding.put("displayName", userDO.getDisplayName())
        binding.put("content", workflowTodoVO.getContent())
        binding.put("todoAddress", externalUrl)
        binding.put("id", workflowTodoVO.getId())
        binding.put("applyUser", workflowTodoVO.getApplyDisplayName())
        // 工单状态
        binding.put("todoPhase", todoPhaseDesc)
        // binding.put("todoStatus", TodoDetailDO.TodoStatusEnum.getCodeDesc(todoDetailVO.getTodoStatus()))
        // 工单类型+名称
        binding.put("workflowTitle", workflowTodoVO.getWorkflowDO().getTitle())
        binding.put("applyTime", workflowTodoVO.applyViewTime)
        binding.put("todoContent", workflowTodoVO.getNotice())

        def simpleEngine = new SimpleTemplateEngine()
        def template = simpleEngine.createTemplate(url).make(binding)

        return template.toString()
    }

    private static TemplateConfiguration buildTplConfiguration() {
        TemplateConfiguration configuration = new TemplateConfiguration();
        configuration.setDeclarationEncoding("UTF-8");
        configuration.setExpandEmptyElements(true);
        configuration.setUseDoubleQuotes(true);
        configuration.setAutoNewLine(true);
        configuration.setAutoIndent(true);
        configuration.setAutoEscape(true);

        return configuration;
    }
}
