package org.noneorone.lang.object.serializable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@SuppressWarnings({ "unchecked", "unused" })
public class Commission {

	public static void main(String[] args) throws Exception {
		String path = "E:\\info.log";
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}

//		testObjToFile(file);
		testExternalObj(file);
		// testXmlObj();

	}

	private static void testObjToFile(File file) throws FileNotFoundException, IOException, ClassNotFoundException {
		InfoSerial info1 = new InfoSerial(1001, "duck1");
		info1.setAge(30);
		InfoSerial info2 = new InfoSerial(1002, "duck2");
		InfoSerial info3 = new InfoSerial(1003, "duck3");
		List<InfoSerial> list = new ArrayList<>();
		list.add(info1);
		list.add(info2);
		list.add(info3);

		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(list);
		oos.writeObject(info1);
		oos.writeObject(info2);
		oos.flush();
		oos.close();

		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		Object readObject = ois.readObject();
		System.out.println(((List<InfoSerial>) readObject).get(0).getAge());
		ois.close();

		// Cipher arg1 = new CipherInputStream(arg0, arg1);
		// SealedObject so = new SealedObject(info1, arg1);

		System.out.println("ok");
	}

	private static void testExternalObj(File file) throws IOException, ClassNotFoundException {
		InfoExternal ie = new InfoExternal(10001, "ie-001", 20);
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		ie.writeExternal(oos);
		oos.flush();
		oos.close();

		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		InfoExternal iec = new InfoExternal();
		iec.readExternal(ois);
		ois.close();

		System.out.println("iec>>> " + iec.toString());
	}

	private static void testXmlObj() throws JAXBException {
		InfoXml xi = new InfoXml("10001", "tom", 20);

		JAXBContext jc = JAXBContext.newInstance(InfoXml.class);
		Marshaller ms = jc.createMarshaller();
		StringWriter sw = new StringWriter();
		ms.marshal(xi, sw);
		String content = sw.toString();

		System.out.println("xml-content>>> " + content);

	}
}
