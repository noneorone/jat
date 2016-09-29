package org.noneorone.io.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel to Json Sample.
 * @author Mars.Wong
 */
public class ExcelToJsonSample {

	public static void main(String[] args) {
		try {

			String srcDir = System.getProperty("user.dir").concat("\\src\\");
			String packagePath = ExcelToJsonSample.class.getPackage().getName().replace(".", "\\").concat("\\");
			String currentClassDir = srcDir.concat(packagePath);

			File excelFile = new File(currentClassDir.concat("address.xlsx"));
			
			Map<String, Map<String, List<Map<String, String>>>> rootMap = parseExcel(excelFile);
			
			System.out.println(mapToJson(rootMap));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse Excel
	 * @param excelFile
	 * @return
	 */
	private static Map<String, Map<String, List<Map<String, String>>>> parseExcel(File excelFile) {
		Map<String, Map<String, List<Map<String, String>>>> rootMap = new HashMap<String, Map<String, List<Map<String, String>>>>();
		
		try {
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFile);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(1);

			for (Row row : xssfSheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
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

				// System.out.println(provinceName + "-" + provinceCode + "-" + cityName + "-" + cityCode);

				if (!rootMap.containsKey(provinceCode)) {
					Map<String, List<Map<String, String>>> nodeMap = new HashMap<String, List<Map<String, String>>>();
					List<Map<String, String>> subNodesList = new ArrayList<Map<String, String>>();
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

			xssfWorkbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rootMap;
		
	}
	
	/**
	 * Convert Map to json
	 * 
	 * @param rootMap
	 * @return
	 */
	private static String mapToJson(Map<String, Map<String, List<Map<String, String>>>> rootMap) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		Set<String> keys = rootMap.keySet();
		for (String key : keys) {
			Map<String, List<Map<String, String>>> nodeMap = rootMap.get(key);
			String nodeMapKey = new ArrayList<String>(nodeMap.keySet()).get(0);
			Iterator<List<Map<String, String>>> subNodesIterator = nodeMap.values().iterator();
			builder.append("{\"code\": \"" + key + "\", \"text\": \"" + nodeMapKey + "\",");
			if (subNodesIterator.hasNext()) {
				builder.append("\"child\": [");
				List<Map<String, String>> subNodesList = subNodesIterator.next();
				for (Map<String, String> subNodeMap : subNodesList) {
					String subNodeKey = new ArrayList<String>(subNodeMap.keySet()).get(0);
					builder.append("{\"code\": \"" + subNodeKey + "\", \"text\": \"" + subNodeMap.get(subNodeKey) + "\"},");
				}
				builder.deleteCharAt(builder.lastIndexOf(","));
				builder.append("]},");
			}
		}
		builder.deleteCharAt(builder.lastIndexOf(","));
		builder.append("]");

		return builder.toString();
	}
}
