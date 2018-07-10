package org.bitbucket.cpointe.mash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An Excel mediator that reads .xlsx files with Apache Poi, tranforming them
 * into JSON.
 * 
 * The XlsxMediator observes the following rule:
 * 
 * Any .xlsx file which the mediator is passed must be formatted with a header
 * row containing the exact field names as these fields are present in the
 * database. This header row *must* be the second row in the document; the first
 * row is left as an optional title row. The XlsxMediator uses this header row
 * when it constructs its return JSON, in order to correctly pair fields to
 * their values.
 */
public class XlsxToJsonMediator extends Mediator {

    private static final int numberOfHeaderRows = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxToJsonMediator.class);

    @Override
    protected Object performMediation(Object input, Properties properties) {

        Workbook workbook = this.constructWorkbook((String) input);
        List<Map<String, String>> data = new ArrayList<>();

        ArrayList<String> fieldNames = this.getFieldNames(workbook);

        for (Sheet sheet : workbook) {
            for (int rowIndex = numberOfHeaderRows; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Map<String, String> rowObject = new HashMap<>();
                Row row = sheet.getRow(rowIndex);

                for (int cellIndex = 0; cellIndex < fieldNames.size(); cellIndex++) {
                    String fieldName = fieldNames.get(cellIndex);
                    String cellValue = row.getCell(cellIndex).getStringCellValue();
                    rowObject.put(fieldName, cellValue);
                }
                data.add(rowObject);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to convert excel to json", e);
            return null;
        }
    }

    public Workbook constructWorkbook(String filePath) {
        File file = new File(filePath);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }

        return workbook;
    }

    public ArrayList<String> getFieldNames(Workbook workbook) {

        ArrayList<String> fieldNameList = new ArrayList<String>();

        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(1);

        for (Cell headerCell : headerRow) {
            String stringCellValue = headerCell.getStringCellValue();
            
            // Parse out the leading '#' in the id field, if there is one
            if (stringCellValue.charAt(0) == '#') {
                stringCellValue = stringCellValue.substring(1);
            }
            
            fieldNameList.add(stringCellValue);
        }

        return fieldNameList;
    }
}
