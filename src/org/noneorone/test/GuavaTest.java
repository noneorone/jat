package org.noneorone.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.net.InetAddresses;
import com.google.common.primitives.Ints;

public class GuavaTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void sortList() throws Exception {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		list.add(2);
		list.add(4);
		list.add(null);
		list.add(5);
		System.out.println(list);

		Ordering<Integer> ordering = Ordering.natural();
		Collections.sort(list, ordering.nullsFirst());
		System.out.println(list);
	}

	@Test
	public void rangeList() throws Exception {
		Range<Integer> range = Range.closed(0, 9);
		System.out.print("[");
		for (int grade : ContiguousSet.create(range, DiscreteDomain.integers())) {
			System.out.print(grade + " ");
		}
		System.out.print("]\n");

		System.out.println(range.contains(5));
		System.out.println(range.containsAll(Ints.asList(1, 4, 3)));
	}

	@Test
	public void StringTest() throws Exception {
		String join = Joiner.on("...").skipNulls().join(Arrays.asList(1, 2, 3, 4, null, 5));
		System.out.println(join);
	}

	@Test
	public void testNet() throws Exception {
		Assert.assertTrue(InetAddresses.isMappedIPv4Address("10.192.48.131"));;
	}

}
