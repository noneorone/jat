package org.noneorone.io.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

public class ExcelToJsonSample {

	public static void main(String[] args) {
		try {

			String srcDir = System.getProperty("user.dir").concat("\\src\\");
			String packagePath = ExcelToJsonSample.class.getPackage().getName().replace(".", "\\").concat("\\");
			String currentClassDir = srcDir.concat(packagePath);

			File excelFile = new File(currentClassDir.concat("address.xlsx"));
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(1);
			
			Map<String, Map<String, List<Map<String, String>>>> rootMap = new HashMap<String, Map<String,List<Map<String,String>>>>();

			for (Row row : xssfSheet) {
				XSSFRow xssfRow = (XSSFRow) row;
				XSSFCell cell0 = xssfRow.getCell(0);
				XSSFCell cell1 = xssfRow.getCell(1);
				XSSFCell cell2 = xssfRow.getCell(2);
				XSSFCell cell3 = xssfRow.getCell(3);

				String provinceName = cell0.getStringCellValue();
				String provinceCode = cell1.getRawValue();
				String cityName = cell2.getStringCellValue();
				String cityCode = cell3.getRawValue();
				if (provinceName.length() == 0) {
					break;
				}

				System.out.println(provinceName + "-" + provinceCode + "-" + cityName + "-" + cityCode);
				
				if (!rootMap.containsKey(provinceCode)) {
					Map<String, List<Map<String, String>>> nodeMap = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> subNodesList = new ArrayList<Map<String,String>>();
					Map<String, String> subNodeMap = new HashMap<String, String>();
					subNodeMap.put(cityCode, cityName);
					subNodesList.add(subNodeMap);
					nodeMap.put(provinceName, subNodesList);
					rootMap.put(provinceCode, nodeMap);
				} else {
					Map<String, List<Map<String, String>>> nodeMap = rootMap.get(provinceCode);
					List<Map<String, String>> subNodesList = nodeMap.get(provinceName);
					Map<String, String> subNodeMap = new HashMap<String, String>();
					subNodeMap.put(cityCode, cityName);
					subNodesList.add(subNodeMap);
				}
			}
			
			System.out.println(new Gson().toJson(rootMap));
			
			xssfWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
