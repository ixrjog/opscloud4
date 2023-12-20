package com.baiyi.opscloud.sshcore.table.converter;

import com.baiyi.opscloud.sshcore.table.Converter;
import com.baiyi.opscloud.sshcore.table.PrettyTable;

public class HtmlConverter implements Converter {

    private boolean minified = false;

    public HtmlConverter minified(boolean minified) {
        this.minified = minified;
        return this;
    }

    @Override
    public String convert(PrettyTable pt) {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>");

        sb.append(minified ? "" : "\n  ");
        sb.append("<tr>");

        for (int i = 0; i < pt.fieldNames.size(); i++) {
            sb.append(minified ? "" : "\n    ");
            sb.append("<th>");
            sb.append(pt.fieldNames.get(i));
            sb.append("</th>");
        }

        sb.append(minified ? "" : "\n  ");
        sb.append("</tr>");

        pt.rows.forEach(r -> {

            sb.append(minified ? "" : "\n  ");
            sb.append("<tr>");

            for (Object o : r) {
                sb.append(minified ? "" : "\n    ");
                sb.append("<td>");
                sb.append(o.toString());
                sb.append("</td>");
            }

            sb.append(minified ? "" : "\n  ");
            sb.append("</tr>");
        });

        sb.append(minified ? "" : "\n");
        sb.append("</table>");

        return sb.toString();
    }

}