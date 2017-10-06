package sqlapp.controller.formatter;

import com.inamik.text.tables.Cell;
import com.inamik.text.tables.GridTable;
import com.inamik.text.tables.SimpleTable;
import com.inamik.text.tables.grid.Border;
import sqlapp.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

public class TableDataFormatter implements DataFormatter {
    private Object data;
    private int width;
    private int height;

    public TableDataFormatter(Object data, int width, int height) {
        this.data = data;
        this.width = width;
        this.height = height;
    }

    public TableDataFormatter(Object data) {
        this(data, 10, 1);
    }

    @Override
    public String format() {
        if (data == null) {

            Log.warn(String.format("Data format error: %s", data));

            return "";
        }

        if (data instanceof String) {
            return (String) data;
        }

        if (data instanceof List<?>) {
            return tableFormatted((List) data);
        }

        return data.toString();
    }

    private String tableFormatted(List<?> list) {
        Iterator<?> rowIt = list.iterator();

        SimpleTable table = SimpleTable.of();

        while (rowIt.hasNext()) {
            Object content = rowIt.next();
            table = table.nextRow();

            if (content instanceof List) {
                Iterator colIt = ((List) content).iterator();
                while (colIt.hasNext()) {
                    Object data = colIt.next();
                    addCell(table, data != null ? data.toString() : "undefined");
                }
            } else {
                addCell(table, (String) content);
            }
        }

        GridTable grid = table.toGrid();
        grid = Border.of(Border.Chars.of('+', '-', '|')).apply(grid);

        StringWriter out = new StringWriter();
        PrintWriter print = new PrintWriter(out);

        grid = grid
                .apply(Cell.Functions.TOP_ALIGN)
                .apply(Cell.Functions.LEFT_ALIGN);

        for (String line: grid.toCell()) {
            print.println(String.format("\t" + line));
        }

        return out.toString();
    }

    private void addCell(SimpleTable table, String line) {
        if (line.length() > width) {
            line = line.substring(0, width - 3) + "...";
        }

        table = table
                .nextCell()
                .addLine(line)
                .applyToCell(Cell.Functions.VERTICAL_CENTER.withHeight(height))
                .applyToCell(Cell.Functions.HORIZONTAL_CENTER.withWidth(width));
    }
}
