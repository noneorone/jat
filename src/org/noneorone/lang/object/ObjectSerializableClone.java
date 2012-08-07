package org.noneorone.lang.object;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
* Title: JavaTech<br>
* Description: 序列化方式复制对象<br>
* Copyright: Copyright (c) 2012 <br>
* Create DateTime: Aug. 7, 2012 1:51:08 PM <br>
* @author wangmeng
*/
public class ObjectSerializableClone{

	private ObjectSerializableClone(){}
	
	/**
	 * 序列化的对象要实现Serializable接口才能实现序列化。序列化后，通过反序列化可以得到和当前对象一样的对象。它比克隆来得更准备。但也就不一定最好，如果这个对象在之前被修改，序列化后可能就会出问题了。<br>
	 * 序列化经常用于文件传递的读取。尤其是在缓存中用得比较多，通过序列化可以将对象缓存在硬盘中。这在登录系统缓存用户权限和角色等信息最常见。而用对克隆对象，也不失为一种很好的方法。<br>
	 * @param object 此对象必须实现Serializable接口.
	 * @return
	 */
    public static Object cloneObject(Object object){
    	if(object instanceof Serializable){
        	ByteArrayOutputStream bos = null;
        	ByteArrayInputStream bis = null;
        	ObjectOutputStream oos = null;
        	ObjectInputStream ois = null;
        	Object obj = null;
        	try {
        		//写对象、序列化
        		bos = new ByteArrayOutputStream();
    			oos = new ObjectOutputStream(bos);
    			oos.writeObject(object);
    			//读对象、反序列化
    			bis = new ByteArrayInputStream(bos.toByteArray());
    			ois = new ObjectInputStream(bis);
    			obj = ois.readObject();
    		} catch (IOException e) {
    			e.printStackTrace();
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    		}
        	return obj;
    	}else{
    		Class<? extends Object> cls = null;
        	try {
        		cls = object.getClass();
    			throw new Exception("object[" + cls + "] must be serialized!");
    		} catch (Exception e) {
    			System.err.println("object[" + cls + "] must implement the Serializable interface!");
    		} finally{
    			cls = null;
    		}
        	return cls;
    	}
    }
	
}
