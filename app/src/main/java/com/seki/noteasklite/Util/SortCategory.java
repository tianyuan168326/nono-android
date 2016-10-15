package com.seki.noteasklite.Util;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by 七升 on 2015/8/16.
 */
public class SortCategory implements Comparator<Object> {

	Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
	@Override
	public int compare(Object o1,  Object o2) {
		return cmp.compare(o1,o2);
		//return 0;
	}
}
