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

@SuppressWarnings("unused")
public class GZIPUtilsTest {

	public static void main(String[] args) throws Exception {

		// testTempFile();

		// testCompressOnError();

		// String path =
		// "D:\\test\\埋点log\\ublog2_20170927105756.3aeecfe8-3ab2-0e9f-ffff-ff.gzip";
		// String path =
		// "D:\\test\\埋点log\\ublog2_20171010092602.3aeecfe8-3ab2-0e9f-ffff-ffffc100a841.gzip";
		String path = "D:\\test\\埋点log\\ublog2_20171010164051.3aeecfe8-3ab2-0e9f-ffff-ffffc100a841.gzip";
		System.out.println(path);
		boolean isValid = GZIPUtils.isValid(path);
		System.out.println("isValid: " + isValid);
		String decompress = GZIPUtils.decompress(path);
		System.out.println("decompress: " + decompress);

		testUnGzip2(path);

	}

	/**
	 * 错误的测试搞法
	 * 
	 * @param path
	 */
	private static void testUnGzip(String path) {
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] buf = new byte[1024];
			fis.read(buf);
			fis.close();
			byte[] unGZip = GZIPUtils.unGZip(buf);
			if (unGZip != null) {
				System.out.println("unGZip: " + unGZip.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 会因为长度不一致引发异常：java.io.EOFException: Unexpected end of ZLIB input
			// stream
		}

	}

	/**
	 * 正确的测试搞法
	 * 
	 * @param path
	 */
	private static void testUnGzip2(String path) {
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int num = -1;
			while ((num = fis.read(buf, 0, buf.length)) != -1) {
				bos.write(buf, 0, num);
			}
			byte[] byteArray = bos.toByteArray();
			System.out.println("byteArray-length: " + byteArray.length);
			byte[] unGZip = GZIPUtils.unGZip(byteArray);
			System.out.println("unGZip-length: " + unGZip.length);
			bos.flush();
			bos.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		boolean compressed = GZIPUtils.compress(path, data.toString());
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

}
