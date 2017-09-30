package org.noneorone.security.encrypt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.zip.CRC32;

/**
 * @author wangmeng
 *
 */
public class TestEncrypt {

//	private static String key = "MkvKhkgvMFqQeDB9";
	private static String key = "1CanNJryhLH3htPC";

	private static String logPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVV3HZSYFeVDHAYBUaH6RPbQE16dbPHLhZdAoRIrv/4SXYQeHT8dWfnurRZ8+lAFysyjZ76takGQ25EHPdpVQLpBwEZta42SWcAbP46gn9AHm/aZq+K8cn5lKpsbyxs5UcQp1LujNxwtpV3M5QcQJok78XYlbz7VjumFx54uBLPwIDAQAB";

	public static void main(String[] args) throws Exception {
		// String path =
		// ABC.class.getProtectionDomain().getCodeSource().getLocation().getFile();
		// File file = new File(path);
		//
		// System.out.println(path);
		// System.out.println(file);
		//
		// System.out.println(System.currentTimeMillis());

//		parseFile();
		
		parseFirstLine();
		
//		String a = "测试";
//		String b = new String(BackAES.encrypt(a, key, 0));
//		System.out.println(a);
//		System.out.println(b);
//		System.out.println(BackAES.decrypt(b, key, 0));

	}

	static void parseFile() {
		String path = "D:\\test\\1506417984378.log";
		File file = null;
		BufferedReader br = null;
		String line = null;
		StringBuilder content = null;
		try {
			file = new File(path);
			br = new BufferedReader(new FileReader(file));
			content = new StringBuilder();
			boolean flag = false;
			while ((line = br.readLine()) != null) {
//				if (!line.contains("\n")) {
//					flag = true;
//					System.out.println("ok");
//				}
				content.append(line);
			}
			File f = new File(file.getParent(), file.getName() + ".bak");
			if (!f.exists()) {
				f.createNewFile();
			}
			System.out.println("bak_file: " + f.getAbsolutePath());
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(content.toString());
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
	}

	public static String decryt(String data) {
		try {
			return BackAES.decrypt(data, key, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void parseFirstLine() throws Exception {
		String firstLine = "@1.0.0,b7c2a42a,iXJmVxMhCMUDEZZusaLyrsPEy/gggpjZGxdhKNzSrj9p1nT6f2KoteOEKD/Guee2DqDShWZwlf5rtpynztzHwL4LXWUe6s8qZwGlfLj0e+u9dRKmg5FmtxkTpRC19ZqdZfoTWpRlmVWuBmTd1LLn1Od+IHi7PD7KOyXkIHMmhuo=";
		String[] lines = firstLine.split(",");
		String crc32Hex = lines[1];
		String rsa = lines[2];
		
		System.out.println(crc32Hex);
		System.out.println(Long.parseLong(crc32Hex, 16));
        CRC32 crc32 = new CRC32();
        crc32.update(logPublicKey.getBytes());
        System.out.println(crc32.getValue());
		
		System.out.println("--------------------------------------------------------------");
        
		System.out.println(rsa);
		String rsaDec = Base64.decode(rsa);
		System.out.println(rsaDec);
		PrivateKey privateKey = RSAUtils.loadPrivateKey(logPublicKey);
		byte[] decryptData = RSAUtils.decryptData(rsaDec.getBytes(), privateKey);
		System.out.println(new String(decryptData));
		
	}
	

}
