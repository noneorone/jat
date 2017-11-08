package org.noneorone.io.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Excel to Json Sample.
 * 
 * @author Mars.Wong
 */
public class ExcelToJson {

	public static void main(String[] args) {
		try {

			String srcDir = System.getProperty("user.dir").concat("\\src\\main\\java\\");
			String packagePath = ExcelToJson.class.getPackage().getName().replace(".", "\\").concat("\\");
			String currentClassDir = srcDir.concat(packagePath);

//			doDataFilter(currentClassDir);

			Map<String, String> map = getDuplicatedMap(currentClassDir);
			exportExcel(map, currentClassDir.concat("duplicated3333.xlsx"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doDataFilter(String currentClassDir) {
		Map<String, String> map = importExcel(currentClassDir.concat("all_new.xlsx"), 1);

		for (Iterator<Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String> next = iterator.next();
			String value = next.getValue();
			if (value.contains(",")) {
				System.out.println(next.getKey() + "-->" + value);
			}
		}

		Gson gson = new Gson();
		System.out.println(gson.toJson(map));
		
//		exportExcel(map, currentClassDir.concat("弃用的.xlsx"));
	}

	/**
	 * 获取重复的Map对象
	 * 
	 * @param currentClassDir
	 * @return
	 */
	private static Map<String, String> getDuplicatedMap(String currentClassDir) {
		Map<String, String> map = new TreeMap<String, String>();
		Map<String, String> nowMap = getContentData(currentClassDir.concat("used_now.json"));
		Map<String, String> neverMap = getContentData(currentClassDir.concat("used_never.json"));
		for (Iterator<Entry<String, String>> nowItr = nowMap.entrySet().iterator(); nowItr.hasNext();) {
			Entry<String, String> now = nowItr.next();
			for (Iterator<Entry<String, String>> neverItr = neverMap.entrySet().iterator(); neverItr.hasNext();) {
				Entry<String, String> never = neverItr.next();
				if (now.getKey().equals(never.getKey())) {
					map.put(now.getValue(), now.getKey());
					System.out.println(now.getKey() + "<---->" + now.getValue() + "," + never.getValue());
				}
			}
		}
		return map;
	}

	/**
	 * JSON文件转成Map对象
	 * 
	 * @param path
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getContentData(String path) {
		BufferedReader br = null;
		try {
			File file = new File(path);
			br = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder content = new StringBuilder();
			while ((line = br.readLine()) != null) {
				content.append(line);
			}

			if (content.length() > 0) {
				return new Gson().fromJson(content.toString(), Map.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	/**
	 * 将文件导入到Map对象
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	private static Map<String, String> importExcel(String path, int sheetIndex) {
		Map<String, String> map = new TreeMap<String, String>();

		try {
			File file = new File(path);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file);
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(sheetIndex);

			for (Row row : xssfSheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				XSSFRow xssfRow = (XSSFRow) row;
//				XSSFCell cell1 = xssfRow.getCell(1);
//				XSSFCell cell2 = xssfRow.getCell(2);
//				String cv1 = String.valueOf(Double.valueOf(cell1.getNumericCellValue()).intValue());
//				String cv2 = cell2.getStringCellValue().replace(" ", "").trim();
				
				XSSFCell cell1 = xssfRow.getCell(0);
				XSSFCell cell2 = xssfRow.getCell(1);
				String cv1 = cell1.getStringCellValue();
				String cv2 = cell2.getStringCellValue().replace(" ", "").trim();
				
				if (cv2.length() == 0) {
					continue;
				}
				if (!String.valueOf(cv2.charAt(0)).equals("/")) {
					cv2 = "/" + cv2;
				}
				// System.out.println(cv0 + "-" + cv1 + "-" + cv2);

				if (!map.containsKey(cv2)) {
					map.put(cv2, cv1);
				} else {
					String value = map.get(cv2);
					if (!value.contains(cv1)) {
						map.put(cv2, value + ", " + cv1);
					}
				}
			}

			xssfWorkbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Map对象导出到Excel文件
	 * 
	 * @param map
	 */
	private static void exportExcel(Map<String, String> map, String path) {
		WritableWorkbook book = null;
		try {
			File file = new File(path);
			book = Workbook.createWorkbook(file);
			WritableSheet sheet = book.createSheet("sheet1", 0);
			Label lc = new Label(0, 0, "接口编号");
			Label lu = new Label(1, 0, "URL");
			sheet.addCell(lc);
			sheet.addCell(lu);
			int index = 1;
			for (Iterator<Entry<String, String>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, String> entry = iterator.next();
				Label l1 = new Label(0, index, entry.getKey());
				Label l2 = new Label(1, index, entry.getValue());
				sheet.addCell(l1);
				sheet.addCell(l2);
				index++;
			}

			book.write();
			System.out.println("exported in " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} finally {
			if (book != null) {
				try {
					book.close();
				} catch (WriteException | IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
