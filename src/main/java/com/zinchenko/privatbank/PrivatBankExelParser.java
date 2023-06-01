package com.zinchenko.privatbank;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PrivatBankExelParser {

    // PrivatBank excel file have unknown symbol in report
    private static final String UNKNOWN_SYMBOL = "�";

    public Map<Integer, List<String>> parseExelFile(MultipartFile file) {
        Map<Integer, List<String>> data = new HashMap<>();

        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(file.getInputStream());
            // PrivatBank excel file have only one sheet
            Sheet sheet = workbook.getSheet(0);
            for (int rows = 0; rows < sheet.getRows(); rows++) {
                data.put(rows, new ArrayList<>());
                for (int colums = 0; colums < sheet.getColumns(); colums++) {
                    Cell cell = sheet.getCell(colums, rows);
                    data.get(rows).add(cell.getContents().replace(UNKNOWN_SYMBOL, StringUtils.EMPTY));
                }
            }
        } catch (IOException | BiffException e) {
            throw new IllegalStateException(e);
        }
        return data;
    }
}
