package com.baiyi.caesar.sshserver.table;

import com.github.fonimus.ssh.shell.SimpleTable;
import lombok.NonNull;
import org.springframework.shell.table.Aligner;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/7 1:27 下午
 * @Version 1.0
 */
public class ShellTable {

    private List<String> columns;
    private boolean displayHeaders;
    private List<Aligner> headerAligners;
    @NonNull
    private List<List<Object>> lines;
    private List<Aligner> lineAligners;
    private boolean useFullBorder;
    private BorderStyle borderStyle;
    private ShellTable.ShellTableBuilderListener tableBuilderListener;

    private static boolean $default$displayHeaders() {
        return true;
    }

    private static boolean $default$useFullBorder() {
        return true;
    }

    private static BorderStyle $default$borderStyle() {
        return BorderStyle.fancy_light;
    }

    ShellTable(List<String> columns, boolean displayHeaders, List<Aligner> headerAligners, @NonNull List<List<Object>> lines, List<Aligner> lineAligners, boolean useFullBorder, BorderStyle borderStyle, ShellTable.ShellTableBuilderListener tableBuilderListener) {
        if (lines == null) {
            throw new NullPointerException("lines is marked non-null but is null");
        } else {
            this.columns = columns;
            this.displayHeaders = displayHeaders;
            this.headerAligners = headerAligners;
            this.lines = lines;
            this.lineAligners = lineAligners;
            this.useFullBorder = useFullBorder;
            this.borderStyle = borderStyle;
            this.tableBuilderListener = tableBuilderListener;
        }
    }

    public static ShellTableBuilder builder() {
        return new ShellTableBuilder();
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public boolean isDisplayHeaders() {
        return this.displayHeaders;
    }

    public List<Aligner> getHeaderAligners() {
        return this.headerAligners;
    }

    @NonNull
    public List<List<Object>> getLines() {
        return this.lines;
    }

    public List<Aligner> getLineAligners() {
        return this.lineAligners;
    }

    public boolean isUseFullBorder() {
        return this.useFullBorder;
    }

    public BorderStyle getBorderStyle() {
        return this.borderStyle;
    }

    public ShellTable.ShellTableBuilderListener getTableBuilderListener() {
        return this.tableBuilderListener;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public void setDisplayHeaders(boolean displayHeaders) {
        this.displayHeaders = displayHeaders;
    }

    public void setHeaderAligners(List<Aligner> headerAligners) {
        this.headerAligners = headerAligners;
    }

    public void setLines(@NonNull List<List<Object>> lines) {
        if (lines == null) {
            throw new NullPointerException("lines is marked non-null but is null");
        } else {
            this.lines = lines;
        }
    }

    public void setLineAligners(List<Aligner> lineAligners) {
        this.lineAligners = lineAligners;
    }

    public void setUseFullBorder(boolean useFullBorder) {
        this.useFullBorder = useFullBorder;
    }

    public void setBorderStyle(BorderStyle borderStyle) {
        this.borderStyle = borderStyle;
    }

    public void setTableBuilderListener(ShellTable.ShellTableBuilderListener tableBuilderListener) {
        this.tableBuilderListener = tableBuilderListener;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ShellTable)) {
            return false;
        } else {
            ShellTable other = (ShellTable) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isDisplayHeaders() != other.isDisplayHeaders()) {
                return false;
            } else if (this.isUseFullBorder() != other.isUseFullBorder()) {
                return false;
            } else {
                label88:
                {
                    Object this$columns = this.getColumns();
                    Object other$columns = other.getColumns();
                    if (this$columns == null) {
                        if (other$columns == null) {
                            break label88;
                        }
                    } else if (this$columns.equals(other$columns)) {
                        break label88;
                    }

                    return false;
                }

                Object this$headerAligners = this.getHeaderAligners();
                Object other$headerAligners = other.getHeaderAligners();
                if (this$headerAligners == null) {
                    if (other$headerAligners != null) {
                        return false;
                    }
                } else if (!this$headerAligners.equals(other$headerAligners)) {
                    return false;
                }

                label74:
                {
                    Object this$lines = this.getLines();
                    Object other$lines = other.getLines();
                    if (this$lines == null) {
                        if (other$lines == null) {
                            break label74;
                        }
                    } else if (this$lines.equals(other$lines)) {
                        break label74;
                    }

                    return false;
                }

                label67:
                {
                    Object this$lineAligners = this.getLineAligners();
                    Object other$lineAligners = other.getLineAligners();
                    if (this$lineAligners == null) {
                        if (other$lineAligners == null) {
                            break label67;
                        }
                    } else if (this$lineAligners.equals(other$lineAligners)) {
                        break label67;
                    }

                    return false;
                }

                Object this$borderStyle = this.getBorderStyle();
                Object other$borderStyle = other.getBorderStyle();
                if (this$borderStyle == null) {
                    if (other$borderStyle != null) {
                        return false;
                    }
                } else if (!this$borderStyle.equals(other$borderStyle)) {
                    return false;
                }

                Object this$tableBuilderListener = this.getTableBuilderListener();
                Object other$tableBuilderListener = other.getTableBuilderListener();
                if (this$tableBuilderListener == null) {
                    if (other$tableBuilderListener != null) {
                        return false;
                    }
                } else if (!this$tableBuilderListener.equals(other$tableBuilderListener)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SimpleTable;
    }

    public int hashCode() {
        //  int PRIME = true;
        // int result = 1;
        int result = 1 * 59 + (this.isDisplayHeaders() ? 79 : 97);
        result = result * 59 + (this.isUseFullBorder() ? 79 : 97);
        Object $columns = this.getColumns();
        result = result * 59 + ($columns == null ? 43 : $columns.hashCode());
        Object $headerAligners = this.getHeaderAligners();
        result = result * 59 + ($headerAligners == null ? 43 : $headerAligners.hashCode());
        Object $lines = this.getLines();
        result = result * 59 + ($lines == null ? 43 : $lines.hashCode());
        Object $lineAligners = this.getLineAligners();
        result = result * 59 + ($lineAligners == null ? 43 : $lineAligners.hashCode());
        Object $borderStyle = this.getBorderStyle();
        result = result * 59 + ($borderStyle == null ? 43 : $borderStyle.hashCode());
        Object $tableBuilderListener = this.getTableBuilderListener();
        result = result * 59 + ($tableBuilderListener == null ? 43 : $tableBuilderListener.hashCode());
        return result;
    }

    public String toString() {
        return "SimpleTable(columns=" + this.getColumns() + ", displayHeaders=" + this.isDisplayHeaders() + ", headerAligners=" + this.getHeaderAligners() + ", lines=" + this.getLines() + ", lineAligners=" + this.getLineAligners() + ", useFullBorder=" + this.isUseFullBorder() + ", borderStyle=" + this.getBorderStyle() + ", tableBuilderListener=" + this.getTableBuilderListener() + ")";
    }

    public static class ShellTableBuilder {
        private ArrayList<String> columns;
        private boolean displayHeaders$set;
        private boolean displayHeaders$value;
        private ArrayList<Aligner> headerAligners;
        private ArrayList<List<Object>> lines;
        private ArrayList<Aligner> lineAligners;
        private boolean useFullBorder$set;
        private boolean useFullBorder$value;
        private boolean borderStyle$set;
        private BorderStyle borderStyle$value;
        private ShellTable.ShellTableBuilderListener tableBuilderListener;

        ShellTableBuilder() {
        }

        public ShellTable.ShellTableBuilder column(String column) {
            if (this.columns == null) {
                this.columns = new ArrayList();
            }

            this.columns.add(column);
            return this;
        }

        public ShellTable.ShellTableBuilder columns(Collection<? extends String> columns) {
            if (columns == null) {
                throw new NullPointerException("columns cannot be null");
            } else {
                if (this.columns == null) {
                    this.columns = new ArrayList();
                }

                this.columns.addAll(columns);
                return this;
            }
        }

        public ShellTable.ShellTableBuilder clearColumns() {
            if (this.columns != null) {
                this.columns.clear();
            }

            return this;
        }

        public ShellTable.ShellTableBuilder displayHeaders(boolean displayHeaders) {
            this.displayHeaders$value = displayHeaders;
            this.displayHeaders$set = true;
            return this;
        }

        public ShellTable.ShellTableBuilder headerAligner(Aligner headerAligner) {
            if (this.headerAligners == null) {
                this.headerAligners = new ArrayList();
            }

            this.headerAligners.add(headerAligner);
            return this;
        }

        public ShellTable.ShellTableBuilder headerAligners(Collection<? extends Aligner> headerAligners) {
            if (headerAligners == null) {
                throw new NullPointerException("headerAligners cannot be null");
            } else {
                if (this.headerAligners == null) {
                    this.headerAligners = new ArrayList();
                }

                this.headerAligners.addAll(headerAligners);
                return this;
            }
        }

        public ShellTable.ShellTableBuilder clearHeaderAligners() {
            if (this.headerAligners != null) {
                this.headerAligners.clear();
            }

            return this;
        }

        public ShellTable.ShellTableBuilder line(List<Object> line) {
            if (this.lines == null) {
                this.lines = new ArrayList();
            }

            this.lines.add(line);
            return this;
        }

        public ShellTable.ShellTableBuilder lines(Collection<? extends List<Object>> lines) {
            if (lines == null) {
                throw new NullPointerException("lines cannot be null");
            } else {
                if (this.lines == null) {
                    this.lines = new ArrayList();
                }

                this.lines.addAll(lines);
                return this;
            }
        }

        public ShellTable.ShellTableBuilder clearLines() {
            if (this.lines != null) {
                this.lines.clear();
            }

            return this;
        }

        public ShellTable.ShellTableBuilder lineAligner(Aligner lineAligner) {
            if (this.lineAligners == null) {
                this.lineAligners = new ArrayList();
            }

            this.lineAligners.add(lineAligner);
            return this;
        }

        public ShellTable.ShellTableBuilder lineAligners(Collection<? extends Aligner> lineAligners) {
            if (lineAligners == null) {
                throw new NullPointerException("lineAligners cannot be null");
            } else {
                if (this.lineAligners == null) {
                    this.lineAligners = new ArrayList();
                }

                this.lineAligners.addAll(lineAligners);
                return this;
            }
        }

        public ShellTable.ShellTableBuilder clearLineAligners() {
            if (this.lineAligners != null) {
                this.lineAligners.clear();
            }

            return this;
        }

        public ShellTable.ShellTableBuilder useFullBorder(boolean useFullBorder) {
            this.useFullBorder$value = useFullBorder;
            this.useFullBorder$set = true;
            return this;
        }

        public ShellTable.ShellTableBuilder borderStyle(BorderStyle borderStyle) {
            this.borderStyle$value = borderStyle;
            this.borderStyle$set = true;
            return this;
        }

        public ShellTable.ShellTableBuilder tableBuilderListener(ShellTable.ShellTableBuilderListener tableBuilderListener) {
            this.tableBuilderListener = tableBuilderListener;
            return this;
        }

        public ShellTable build() {
            List columns;
            switch (this.columns == null ? 0 : this.columns.size()) {
                case 0:
                    columns = Collections.emptyList();
                    break;
                case 1:
                    columns = Collections.singletonList((String) this.columns.get(0));
                    break;
                default:
                    columns = Collections.unmodifiableList(new ArrayList(this.columns));
            }

            List headerAligners;
            switch (this.headerAligners == null ? 0 : this.headerAligners.size()) {
                case 0:
                    headerAligners = Collections.emptyList();
                    break;
                case 1:
                    headerAligners = Collections.singletonList((Aligner) this.headerAligners.get(0));
                    break;
                default:
                    headerAligners = Collections.unmodifiableList(new ArrayList(this.headerAligners));
            }

            List lines;
            switch (this.lines == null ? 0 : this.lines.size()) {
                case 0:
                    lines = Collections.emptyList();
                    break;
                case 1:
                    lines = Collections.singletonList((List) this.lines.get(0));
                    break;
                default:
                    lines = Collections.unmodifiableList(new ArrayList(this.lines));
            }

            List lineAligners;
            switch (this.lineAligners == null ? 0 : this.lineAligners.size()) {
                case 0:
                    lineAligners = Collections.emptyList();
                    break;
                case 1:
                    lineAligners = Collections.singletonList((Aligner) this.lineAligners.get(0));
                    break;
                default:
                    lineAligners = Collections.unmodifiableList(new ArrayList(this.lineAligners));
            }

            boolean displayHeaders$value = this.displayHeaders$value;
            if (!this.displayHeaders$set) {
                displayHeaders$value = ShellTable.$default$displayHeaders();
            }

            boolean useFullBorder$value = this.useFullBorder$value;
            if (!this.useFullBorder$set) {
                useFullBorder$value = ShellTable.$default$useFullBorder();
            }

            BorderStyle borderStyle$value = this.borderStyle$value;
            if (!this.borderStyle$set) {
                borderStyle$value = ShellTable.$default$borderStyle();
            }

            return new ShellTable(columns, displayHeaders$value, headerAligners, lines, lineAligners, useFullBorder$value, borderStyle$value, this.tableBuilderListener);
        }

        public String toString() {
            return "SimpleTable.SimpleTableBuilder(columns=" + this.columns + ", displayHeaders$value=" + this.displayHeaders$value + ", headerAligners=" + this.headerAligners + ", lines=" + this.lines + ", lineAligners=" + this.lineAligners + ", useFullBorder$value=" + this.useFullBorder$value + ", borderStyle$value=" + this.borderStyle$value + ", tableBuilderListener=" + this.tableBuilderListener + ")";
        }
    }

    @FunctionalInterface
    public interface ShellTableBuilderListener {
        void onBuilt(TableBuilder var1);
    }
}
