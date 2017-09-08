package org.noneorone.util.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * List工具类
 * 
 * @author wangmeng
 */
public class ListUtils {

	/**
	 * 按指定大小对集合分割
	 * 
	 * @param list
	 *            需要分割的{@link List}}}对象
	 * @param length
	 *            每个子集合大小
	 * @return
	 */
	public static List<List<?>> splitList(List<?> list, int length) {
		List<List<?>> result = new ArrayList<>();

		if (length != 0) {
			int size = list.size();
			int count = (size + length + 1) / length;
			for (int i = 0; i < count; i++) {
				int fromIndex = i * length;
				int toIndex = (i + 1) * length;
				if (toIndex > size) {
					toIndex = size;
				}
				if (fromIndex < toIndex) {
					List<?> subList = list.subList(fromIndex, toIndex);
					result.add(subList);
				}
			}
		}

		if (result.isEmpty()) {
			result.add(list);
		}

		return result;
	}
}
