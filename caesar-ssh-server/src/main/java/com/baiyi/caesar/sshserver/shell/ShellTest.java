package com.baiyi.caesar.sshserver.shell;

/**
 * @Author baiyi
 * @Date 2021/6/7 11:22 上午
 * @Version 1.0
 */
public class ShellTest {

//    @Resource
//    private SshShellHelper helper;
//
//    @Resource
//    private ServerService serverService;
//
//    @Resource
//    private HostSystemHandler hostSystemHandler;
//
//    @Resource
//    private ServerGroupService serverGroupService;
//
//    //@ShellMethod("Show ServerGroup")
//    public String test() {
//        List<String> releases = Lists.newArrayList("a");
//        LinkedHashMap<String, Object> headers = new LinkedHashMap<>();
//        headers.put("name", "Name");
//        headers.put("version", "Version");
//        headers.put("info.lastDeployed", "Last updated");
//        headers.put("info.status.statusCode", "Status");
//        headers.put("pkg.metadata.name", "Package Name");
//        headers.put("pkg.metadata.version", "Package Version");
//        headers.put("platformName", "Platform Name");
//        headers.put("info.status.platformStatusPrettyPrint", "Platform Status");
//        TableModel model = new BeanListTableModel<>(releases, headers);
//        TableBuilder tableBuilder = new TableBuilder(model);
//
//        return tableBuilder.build().render(10);
//    }
//
//
//    //@ShellMethod("Show ServerGroup")
//    public String show(@ShellOption(help = "输入组名称") String  name) {
//        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
//                .column("ID")
//                .column("Group")
//                .column("Comment")
//                .useFullBorder(false);
//        ServerGroupParam.ServerGroupPageQuery pageQuery = ServerGroupParam.ServerGroupPageQuery.builder()
//                .name(name)
//                .build();
//        pageQuery.setLength(100);
//        pageQuery.setPage(1);
//        List<ServerGroup> groups = serverGroupService.queryServerGroupPage(pageQuery).getData();
//        groups.forEach(s ->
//                builder.line(Arrays.asList(s.getId(), s.getName(), s.getComment()))
//        );
//
//        builder.borderStyle(BorderStyle.air);
//
//
//        return helper.renderTable(builder.build());
//    }
}
