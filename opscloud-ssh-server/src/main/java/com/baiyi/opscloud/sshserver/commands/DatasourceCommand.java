/*
 * Copyright (c) 2020 François Onimus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baiyi.opscloud.sshserver.commands;

import com.baiyi.opscloud.sshserver.SimpleTable;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import com.baiyi.opscloud.sshserver.SshShellProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.shell.Availability;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Datasource command
 */
@Slf4j
@SshShellComponent
@ShellCommandGroup("Datasource Commands")
@ConditionalOnClass(DataSource.class)
@ConditionalOnProperty(
        name = SshShellProperties.SSH_SHELL_PREFIX + ".commands." + DatasourceCommand.GROUP + ".create",
        havingValue = "true", matchIfMissing = true
)
public class DatasourceCommand extends AbstractCommand {

    public static final String GROUP = "datasource";
    private static final String COMMAND_DATA_SOURCE_LIST = GROUP + "-list";
    private static final String COMMAND_DATA_SOURCE_PROPERTIES = GROUP + "-properties";
    private static final String COMMAND_DATA_SOURCE_QUERY = GROUP + "-query";
    public static final String COMMAND_DATA_SOURCE_UPDATE = GROUP + "-update";

    private final Map<Integer, DataSource> dataSourceByIndex = new HashMap<>();

    public DatasourceCommand(SshShellHelper helper, SshShellProperties properties,
                             @Autowired(required = false) List<DataSource> dataSourceList) {
        super(helper, properties, properties.getCommands().getDatasource());
        if (dataSourceList != null) {
            this.dataSourceByIndex
                    .putAll(IntStream.range(0, dataSourceList.size()).boxed().collect(Collectors.toMap(Function.identity(), dataSourceList::get)));
        }
    }

    /**
     * List datasources found in context
     *
     * @return datasource list
     */
    @ShellMethod(key = COMMAND_DATA_SOURCE_LIST, value = "List available datasources")
    @ShellMethodAvailability("datasourceListAvailability")
    public String datasourceList() {
        if (dataSourceByIndex.isEmpty()) {
            helper.printWarning("No datasource found in context.");
            return null;
        }

        SimpleTable.SimpleTableBuilder builder = SimpleTable.builder()
                .column("Id").column("Name").column("Url").column("Username").column("Product").column("Error");
        for (Map.Entry<Integer, DataSource> entry : dataSourceByIndex.entrySet()) {
            try (Connection connection = entry.getValue().getConnection()) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();
                builder.line(Arrays.asList(
                        entry.getKey(), entry.getValue().toString(),
                        databaseMetaData.getURL(),
                        databaseMetaData.getUserName(),
                        databaseMetaData.getDatabaseProductName() + " " + databaseMetaData.getDatabaseProductVersion(),
                        "-")
                );
            } catch (SQLException e) {
                log.warn("Unable to get datasource information for [{}] : {}-{}", entry.getValue().toString(),
                        e.getErrorCode(), e.getMessage());
                String url = find(entry.getValue(), "jdbcUrl", "url");
                String userName = find(entry.getValue(), "username", "user");
                builder.line(Arrays.asList(entry.getKey(), entry.getValue().toString(), url, userName, "-",
                        "Unable " + "to get" + " datasource information for [" + url + "] : " + e.getErrorCode() +
                                "-" + e.getMessage())
                );
            }
        }
        return helper.renderTable(builder.build());
    }

    private String find(Object object, String... fieldNames) {
        if (fieldNames != null) {
            for (String fieldName : fieldNames) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(fieldName, object.getClass());
                    Object result = pd.getReadMethod().invoke(object);
                    if (result != null) {
                        return result.toString();
                    }
                } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {
                    log.debug("Unable to access field [{}] on class [{}]", fieldName, object.getClass(), e);
                }
            }
        }
        return "-";
    }

    /**
     * Executes <b>show variables</b> on given datasource
     *
     * @param id     datasource identifier
     * @param filter filter properties according to pattern
     * @return server sql properties
     */
    @ShellMethod(key = COMMAND_DATA_SOURCE_PROPERTIES, value = "Datasource properties command. Executes 'show " +
            "variables'")
    @ShellMethodAvailability("datasourcePropertiesAvailability")
    public String datasourceProperties(
            @ShellOption(help = "Datasource identifier", valueProvider = DatasourceIndexValuesProvider.class) int id,
            @ShellOption(help = "Add like %<filter>% to sql query", defaultValue = ShellOption.NULL) String filter) {
        String query = "show variables";
        if (filter != null) {
            query += " LIKE '%" + filter + "%'";
        }
        return datasourceQuery(id, query);
    }

    /**
     * Executes <b>query</b> on given datasource
     *
     * @param id    datasource identifier
     * @param query sql query
     * @return query result in table
     */
    @ShellMethod(key = COMMAND_DATA_SOURCE_QUERY, value = "Datasource query command.")
    @ShellMethodAvailability("datasourceQueryAvailability")
    public String datasourceQuery(
            @ShellOption(help = "Datasource identifier", valueProvider = DatasourceIndexValuesProvider.class) int id,
            @ShellOption(help = "SQL query to execute") String query
    ) {
        StringBuilder sb = new StringBuilder();
        DataSource ds = getOrDie(id);
        try (Connection connection = ds.getConnection()) {
            sb.append("Query [").append(query).append("] for datasource : ").append(ds).append(" (").append(connection.getMetaData().getURL())
                    .append(")\n");
            try (Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(query)) {
                if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY
                        || rs.getType() == ResultSet.TYPE_SCROLL_INSENSITIVE
                        || rs.getType() == ResultSet.TYPE_SCROLL_SENSITIVE) {
                    SimpleTable.SimpleTableBuilder builder = SimpleTable.builder();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        builder.column(rs.getMetaData().getColumnName(i));
                    }
                    while (rs.next()) {
                        List<Object> list = new ArrayList<>();
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            list.add(rs.getString(i));
                        }
                        builder.line(list);
                    }
                    sb.append(helper.renderTable(builder.build()));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to execute SQL query : " + e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * Executes <b>update</b> on given datasource
     *
     * @param id     datasource identifier
     * @param update sql update
     */
    @ShellMethod(key = COMMAND_DATA_SOURCE_UPDATE, value = "Datasource update command.")
    @ShellMethodAvailability("datasourceUpdateAvailability")
    public void datasourceUpdate(
            @ShellOption(help = "Datasource identifier", valueProvider = DatasourceIndexValuesProvider.class) int id,
            @ShellOption(help = "SQL update to execute") String update
    ) {
        DataSource ds = getOrDie(id);
        try (Connection connection = ds.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                int result = statement.executeUpdate(update);
                helper.printSuccess("Query [" + update + "] for datasource : [" + ds +
                        " (" + connection.getMetaData().getURL() + ")] updated " + result + " row(s)");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Unable to execute SQL update : " + e.getMessage(), e);
        }
    }

    private DataSource getOrDie(int index) {
        DataSource ds = dataSourceByIndex.get(index);
        if (ds == null) {
            throw new IllegalArgumentException("Cannot find datasource with identifier [" + index + "]");
        }
        return ds;
    }

    private Availability datasourceListAvailability() {
        return availability(GROUP, COMMAND_DATA_SOURCE_LIST);
    }

    private Availability datasourcePropertiesAvailability() {
        return availability(GROUP, COMMAND_DATA_SOURCE_PROPERTIES);
    }

    private Availability datasourceQueryAvailability() {
        return availability(GROUP, COMMAND_DATA_SOURCE_QUERY);
    }

    private Availability datasourceUpdateAvailability() {
        return availability(GROUP, COMMAND_DATA_SOURCE_UPDATE);
    }
}

@Component
class DatasourceIndexValuesProvider implements ValueProvider {

    private final Map<Integer, DataSource> dataSourceByIndex = new HashMap<>();

    public DatasourceIndexValuesProvider(@Autowired(required = false) List<DataSource> dataSourceList) {
        if (dataSourceList != null) {
            this.dataSourceByIndex
                    .putAll(IntStream.range(0, dataSourceList.size()).boxed().collect(Collectors.toMap(Function.identity(), dataSourceList::get)));
        }
    }

    @Override
    public List<CompletionProposal> complete(CompletionContext completionContext) {
        return dataSourceByIndex.keySet().stream().map(i -> new CompletionProposal("" + i)).collect(Collectors.toList());
    }
}
