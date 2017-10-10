package org.noneorone.io.file.gzip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP工具类
 * 
 * @author mars.wong
 * @since 2017-10-10 15:56 created.
 */
@SuppressWarnings("unused")
public class GZIPUtils {

	public static final int BUFFER_SIZE = 1024;
	public static final String GZIP_FILE_EXT = ".gzip";
	public static final String CHARSET_NAME = "utf-8";

	/**
	 * 数据压缩
	 *
	 * @param path
	 *            压缩到指定文件路径
	 * @param data
	 *            字符串内容
	 * @return 是否压缩成功
	 */
	public static boolean compress(String path, String data) {
		FileOutputStream fos = null;
		GZIPOutputStream gos = null;

		try {
			fos = new FileOutputStream(path);
			gos = new GZIPOutputStream(fos, BUFFER_SIZE);
			gos.write(data.getBytes(CHARSET_NAME));
			gos.finish();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (gos != null) {
				try {
					gos.close();
				} catch (Exception e) {
					// do noting
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					// do noting
				}
			}
		}

		return true;
	}

	/**
	 * 解压指定文件数据
	 *
	 * @param path
	 *            指定压缩文件的路径
	 * @param isCheckValid
	 *            是否检查有效性，若是则只对文件有效性校验，否则返回解压过后的内容串
	 * @return 根据传入变量isCheckValid返回不同的结果
	 */
	private static Object decompress(String path, boolean isCheckValid) {
		FileInputStream fis = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream baos = null;
		byte[] buf = null;
		int len = 0;
		String content = null;
		boolean isValid = false;
		try {
			fis = new FileInputStream(new File(path));
			gis = new GZIPInputStream(fis);
			baos = new ByteArrayOutputStream();
			buf = new byte[BUFFER_SIZE];
			if (isCheckValid) {
				isValid = gis.read(buf) != -1;
			} else {
				while ((len = gis.read(buf)) != -1) {
					baos.write(buf, 0, len);
				}
				content = new String(baos.toByteArray(), CHARSET_NAME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (Exception e) {
					// do noting
				}
			}
			if (gis != null) {
				try {
					gis.close();
				} catch (Exception e) {
					// do noting
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					// do noting
				}
			}
		}

		return isCheckValid ? isValid : content;
	}

	/**
	 * 解压文件并返回解压内容
	 *
	 * @param path
	 *            指定压缩文件的路径
	 * @return 返回解压内容
	 */
	public static String decompress(String path) {
		Object obj = decompress(path, false);
		if (obj != null && obj instanceof String) {
			return String.valueOf(obj);
		}

		return null;
	}

	/**
	 * 校验文件是否为有效的GZIP文件
	 *
	 * @param path
	 *            指定压缩文件的路径
	 * @return 是否有效
	 */
	public static boolean isValid(String path) {
		Object obj = decompress(path, true);
		if (obj != null && obj instanceof Boolean) {
			return (boolean) obj;
		}

		return false;
	}

	public static byte[] unGZip(byte[] data) {
		byte[] b = null;
		ByteArrayInputStream bis = null;
		GZIPInputStream gis = null;
		ByteArrayOutputStream bos = null;

		try {
			bis = new ByteArrayInputStream(data);
			gis = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			bos = new ByteArrayOutputStream();
			while ((num = gis.read(buf, 0, buf.length)) != -1) {
				bos.write(buf, 0, num);
			}
			b = bos.toByteArray();
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return b;
	}

}
