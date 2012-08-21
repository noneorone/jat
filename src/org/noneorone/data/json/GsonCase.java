package org.noneorone.data.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson Use Case.
 * @author Mars.Wang
 * @created 2012-8-21
 */
public class GsonCase {

	class Emp{
		private Long id;
		private String name;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	
	public static void main(String[] args) {
		
		Gson gson = new Gson();
		
		List<Emp> list = new ArrayList<Emp>();
		Emp emp = null;
		for(int i=0; i<10; i++){
			emp = new GsonCase().new Emp();
			emp.setId((long)1000 + i);
			emp.setName("Rose_" + i);
			list.add(emp);
		}
		
		//list to JSON string.
		String listJson = gson.toJson(list);
		System.out.println(listJson);
		
		//one JSON node to one object. 
		String objJson = "{\"id\":10086,\"name\":\"ChinaMobile\"}";
		Emp empObj = gson.fromJson(objJson, Emp.class);
		System.out.println(empObj.getId() + " : " + empObj.getName());
		
		//many JSON node to object list.
		String nodesJson = "[{\"id\":10000,\"name\":\"ChinaUnion\"},{\"id\":10086,\"name\":\"ChinaMobile\"}]";
		List<Emp> empList = gson.fromJson(nodesJson, new TypeToken<List<Emp>>(){}.getType());
		for(Emp e : empList){
			System.out.println(e.getId() + " : " + e.getName());
		}
	}
	
}
