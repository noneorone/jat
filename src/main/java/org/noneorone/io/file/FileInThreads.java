package org.noneorone.io.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multiple Thread file operation.
 * @author wangmeng
 */
public class FileInThreads {

	private static String FILE_NAME = String.valueOf(System.currentTimeMillis()).concat(".log");

	private static ExecutorService executor = Executors.newSingleThreadExecutor();

	public static void main(String[] args) {
		final FileInThreads t7 = new FileInThreads();

		final List<String> content = new ArrayList<>();
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());
		content.add(content.size() + "-" + System.currentTimeMillis());

		for (final String s : content) {
			System.out.println(s);
			executor.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					t7.writeMultipleFile(s);
					if (content.indexOf(s) == content.size() - 1) {
						executor.shutdown();
					}
					return null;
				}
			});
		}
	}

	private void writeMultipleFile(String text) {
		FileReader fr = null;
		FileWriter fw = null;
		BufferedWriter bw = null;
		LineNumberReader lnr = null;
		try {
			File file = new File(new File("E:\\"), FILE_NAME);
			if (!file.exists()) {
				file.createNewFile();
			}
			if (file.exists()) {
				fr = new FileReader(file);
				lnr = new LineNumberReader(fr);
				lnr.skip(Long.MAX_VALUE);
				int lineNumber = lnr.getLineNumber() + 1;
				System.out.println(Thread.currentThread() + "-----lineNumber: " + lineNumber);
				if (lineNumber >= 5) {
					System.out.println(22222222);
					FILE_NAME = String.valueOf(System.currentTimeMillis()).concat(".log");
					writeMultipleFile(text);
				} else {
					fw = new FileWriter(file, true);
					bw = new BufferedWriter(fw);
					bw.write(new String(text.concat("\r\n").getBytes(), Charset.forName("UTF-8")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (lnr != null) {
					lnr.close();
				}
			} catch (Exception e) {
			} finally {
				lnr = null;
			}
			try {
				if (fr != null) {
					fr.close();
				}
			} catch (Exception e) {
			} finally {
				fr = null;
			}
			try {
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {
			} finally {
				bw = null;
			}
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (Exception e) {
			} finally {
				fw = null;
			}

		}
	}

}
