package org.technologybrewery.mash;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An Excel mediator that transforms .xlsx documents to lists of transfer
 * objects.
 * 
 * The mediator observes the following rules:
 * 
 * --Any .xlsx document which the mediator is passed must have a single header
 * row containing its fully-qualified (package and class) name. The following
 * row must contain the names for the class's fields, with values following
 * logically below.
 * 
 * --These field names must match exactly to the object's field names, as this
 * object is defined as an entity through Fermenter, and therefore in its
 * generated Transfer object as well.
 * 
 */
public class XlsxToTransferObjectMediator extends XlsxToJsonMediator {

    private static final int numberOfHeaderRows = 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(XlsxToTransferObjectMediator.class);

    @Override
    protected Object performMediation(Object input, Properties properties) {

        Workbook workbook = constructWorkbook((String) input);
        ArrayList<String> fieldNames = getFieldNames(workbook);
        String className = this.getTransferObjectClassName(workbook);
        ArrayList<Object> transferObjectList = new ArrayList<Object>();

        // go through workbook and translate rows to objects
        for (Sheet sheet : workbook) {
            for (int rowIndex = numberOfHeaderRows; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
                Object transferObject = translateRowToObject(fieldNames, sheet, rowIndex, className);
                transferObjectList.add(transferObject);
            }
        }

        return transferObjectList;
    }

    @SuppressWarnings("rawtypes")
    private Object translateRowToObject(ArrayList<String> fieldNames, Sheet sheet, int rowIndex, String className) {
        Class transferObjectClass = null;
        Object transferObject = null;
        Row row = sheet.getRow(rowIndex);
        try {
            transferObjectClass = Class.forName(className);
            transferObject = transferObjectClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            LOGGER.error("Failed to setup transfer object class", e);
        }

        // populate the object with each cell value in the row
        for (int cellIndex = 0; cellIndex < fieldNames.size(); cellIndex++) {
            translateCellToObjectField(fieldNames, transferObjectClass, transferObject, row, cellIndex);
        }
        return transferObject;
    }

    @SuppressWarnings("unchecked")
    private void translateCellToObjectField(ArrayList<String> fieldNames, Class transferObjectClass,
            Object transferObject, Row row, int cellIndex) {

        String cellValue = row.getCell(cellIndex).getStringCellValue();
        String methodName = getSetValueMethodName(fieldNames.get(cellIndex));
        try {
            Method method = transferObjectClass.getMethod(methodName, String.class);
            method.invoke(transferObject, cellValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            LOGGER.error("Failed to set the value for: " + methodName, e);
        }
    }

    public String getTransferObjectClassName(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        return sheet.getRow(0).getCell(0).getStringCellValue();
    }

    public String getSetValueMethodName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }
}
