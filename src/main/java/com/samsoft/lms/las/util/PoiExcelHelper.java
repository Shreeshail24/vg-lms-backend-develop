package com.samsoft.lms.las.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class PoiExcelHelper {

	public List<Map<String, Object>> readJExcel(String fileLocation) throws Exception {

		Map<Integer, List<String>> data = new HashMap<>();
		int i = 0;
		int count = 0;

		try {
			log.info("path=======>" + fileLocation);
			FileInputStream file = new FileInputStream(new File(fileLocation));

			Workbook workbook = new XSSFWorkbook(file);
			DataFormatter dataFormatter = new DataFormatter();
			Iterator<Sheet> sheets = workbook.sheetIterator();
			while (sheets.hasNext()) {
				Sheet sh = sheets.next();
				Iterator<Row> iterator = sh.iterator();
				while (iterator.hasNext()) {

					Row row = iterator.next();
//					 Iterator<Cell> cellIterator = row.iterator();
					data.put(i, new ArrayList<String>());

//					while (cellIterator.hasNext()) {
						String cellValue = "";
//						Cell cell = cellIterator.next();
					for (int j = 0; j < 5; j++) {
						Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						
						if (cell == null || cell.getCellType() == CellType.BLANK) {
							count++;
							cellValue = "";
						} else {
							cellValue = dataFormatter.formatCellValue(cell);
						}
						
						data.get(i).add(cellValue);

//						switch (cell.getCellType()) {
//						case STRING:
//							data.get(i).add(cell.getStringCellValue());
//							break;
//						case NUMERIC:
//							data.get(i).add(Double.toString(cell.getNumericCellValue()));
//							break;
//						case BOOLEAN:
//							data.get(i).add(Boolean.toString(cell.getBooleanCellValue()));
//							break;
//						default: data.get(i).add(" ");
//						}
					}
					i++;
				}
			}
			System.out.println("Count: " + count);
			workbook.close();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return this.prepareExcelDataMap(data);
	}

	//Prepare Excel Data Map
	protected List<Map<String, Object>> prepareExcelDataMap(Map<Integer, List<String>> data) throws Exception {
		log.info("in prepareExcelDataMap");
		try {
			List<Map<String, Object>> listOfValue = new ArrayList<>();
			Map<String, Object> valueMap = null;
			Map<Integer, String> headerMap = new HashMap<>();
			for (Map.Entry<Integer, List<String>> entry : data.entrySet()) {
				if (entry.getKey() == 0) {
					for (int i = 0; i < entry.getValue().size(); i++) {
						headerMap.put(i, entry.getValue().get(i));
					}
				} else {
					valueMap = new HashMap<>();
					for (int i = 0; i < entry.getValue().size(); i++) {

						for (int j = 0; j < i + 1; j++) {
							valueMap.put(headerMap.get(j), entry.getValue().get(j));
						}
					}
					listOfValue.add(valueMap);
				}
				log.info("Value Map: " + valueMap);
			}
			return listOfValue;
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Method Name: prepareExcelDataMap " + e);
			throw e;
		}
	}
}
