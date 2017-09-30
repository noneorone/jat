package org.noneorone.io.file.gzip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@SuppressWarnings("unused")
public class GZIPUtils {

	public static final String GZIP_FILE_EXT = ".gzip";
	public static final String CHARSET_NAME = "utf-8";

	public static void main(String[] args) throws Exception {

		// testTempFile();

		// testCompressOnError();

		// String path =
		// "D:\\test\\埋点log\\ublog2_20170927105756.3aeecfe8-3ab2-0e9f-ffff-ff.gzip";
		String path = "D:\\test\\埋点log\\haha_1506665483225.gzip";
		System.out.println(path);
		boolean isValid = isValid(path);
		System.out.println("isValid: " + isValid);
		String decompress = decompress(path);
		System.out.println("decompress: " + decompress);

	}

	private static void testCompressOnError() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000L);
					System.out.println("程序即将结束。。。");
					System.exit(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

		String path = "D:\\test\\埋点log\\haha_" + System.currentTimeMillis() + ".gzip";
		System.out.println("path: " + path);
		String str = "我是直接压缩成gzip文件的";
		StringBuilder data = new StringBuilder(str);
		long start = System.currentTimeMillis();
		System.out.println("开始追加。。。");
		for (int i = 0; i < 10000000; i++) {
			data.append(str);
		}
		System.out.println("追加结束1111。。。");
		boolean compressed = compress(path, data.toString());
		long end = System.currentTimeMillis();
		System.out.println("追加结束。。。" + (end - start));
		System.out.println("compressed? " + compressed + " >>> " + path);
	}

	private static void testTempFile() throws IOException, InterruptedException {
		// String path = "D:\\test\\埋点log\\abc";
		String path = "D:\\test\\埋点log\\abc.txt";
		// String path = "D:\\test\\埋点log\\abc.gzip";
		File file = new File(path);
		System.out.println("file.isFile(): " + file.isFile());
		System.out.println("file.length(): " + file.length());
		System.out.println("file is[.gzip]: " + file.getName().endsWith(".gzip"));

		if (!file.isFile() || file.length() == 0 || !file.getName().endsWith(".gzip")) {
			System.out.println("delete file >>> " + file.getAbsolutePath());
			file.delete();
		}

		for (int i = 0; i < 10; i++) {
			// File tf = File.createTempFile("pre_", "_suf",
			// file.getParentFile());
			File tf = new File(file.getParentFile(), "ublog2_" + System.currentTimeMillis() + ".3aeecfe8-3ab2-0e9f-ffff-ff");
			System.out.println("createTempFile: " + tf.getAbsolutePath());
			System.out.println("临时文件是否已经存在? " + tf.exists());
			FileWriter fw = new FileWriter(tf);
			fw.write("这是个临时文件");
			fw.close();
			if (i > 4) {
				Thread.sleep(1000L);
				tf.deleteOnExit();
			}
			System.out.println("临时文件已写入? " + tf.exists());
		}
	}

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
			gos = new GZIPOutputStream(fos);
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
			buf = new byte[2048];
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

}
