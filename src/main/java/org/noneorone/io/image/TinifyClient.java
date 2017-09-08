package org.noneorone.io.image;

import com.tinify.Source;
import com.tinify.Tinify;

import java.io.*;
import java.util.Properties;

import org.noneorone.io.file.FetchDir;

/**
 * Created by sodaChen on 2017/5/21.
 */
public class TinifyClient {
	/** 单个key的最大压缩图片数量 **/
	private static final int MAX_IMG = 500;
	/** 属性配置 **/
	private static Properties properties = new Properties();
	/** key的索引值 **/
	private static int keyIndex;
	/** 图片数量 **/
	private static int imgCount;

	public static void main(String[] args) throws Exception {
		System.out.println(FetchDir.getCurrentPackPath(TinifyClient.class));
		System.out.println(System.getProperty("user.dir"));
		if (args.length == 0) {
			System.out.println("根据配置来压缩图片");
			// 读取配置
			readConfigHanle();
		} else {
			System.out.println("根据输入参数来压缩图片");
			properties.setProperty("key", args[0]);
			properties.setProperty("input", args[1]);
			if (args.length == 3)
				properties.setProperty("output", args[2]);
			else
				properties.setProperty("output", args[1]);
		}
		// 转换换用户自定义的字符串
		String str = properties.getProperty("input");
		str = str.replaceAll("/", "\\\\");
		properties.setProperty("input", str);
		str = properties.getProperty("output");
		str = str.replaceAll("/", "\\\\");
		properties.setProperty("output", str);
		// 设置用户key
		Tinify.setKey(properties.getProperty("key"));
		File root = new File(properties.getProperty("input"));
		File[] files = root.listFiles();
		// 遍历输入文件夹
		readDirectory(files);
		System.out.println("全部png和jpg压缩完成");
	}

	/**
	 * 读取配置文件来设置属性
	 */
	private static void readConfigHanle() {
		try {
			String confPath = FetchDir.getCurrentPackPath(TinifyClient.class) + "tinify.properties";
			InputStream in = new BufferedInputStream(new FileInputStream(confPath));
			// 加载属性列表
			properties.load(in);
			in.close();
		} catch (Exception e) {
			System.out.println("读取配置文件错误");
			e.printStackTrace();
		}
	}

	/**
	 * 读取文件夹，并处理里面的png和jpg图片
	 * 
	 * @param files
	 */
	private static void readDirectory(File[] files) {
		for (File file : files) {
			String fileName = file.getName();
			if (file.isDirectory()) {
				// 递归读文件夹
				readDirectory(file.listFiles());
			} else {
				int index = fileName.lastIndexOf(".");
				String prefix = fileName.substring(index + 1);
				if (prefix.equals("png") || prefix.equals("jpg"))
					// 进行压缩
					tinifyImg(file.getPath(), properties.getProperty("output"));
			}
		}
	}

	/**
	 * 连接tinify的API进行压缩图片和保存回本地
	 * 
	 * @param inPath
	 *            文件路径
	 * @param outPath
	 *            输出路径头
	 */
	private static void tinifyImg(String inPath, String outPath) {
		try {
			System.out.println("上传:" + inPath);
			Source source = Tinify.fromFile(inPath);
			// 确保输入和输出是一样的斜杠符号
			inPath = inPath.replaceAll("/", "\\\\");
			// 替换路径保存
			String savePath = inPath.replace(properties.getProperty("input"), outPath);
			System.out.println("保存:" + savePath);
			File file = new File(savePath);
			// 如果没有文件夹，则根据路径生成文件夹
			if (!file.getParentFile().exists())
				file.getParentFile().mkdirs();
			source.toFile(savePath);
			System.out.println("完成!!!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("上传压缩失败！");
		}
	}
}