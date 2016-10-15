package com.seki.noteasklite.DataUtil;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by 七升 on 2015/8/16.
 */
public class SortCategory implements Comparator<InfoArray> {

	Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
	@Override
	public int compare(InfoArray o1, InfoArray o2) {
		if(cmp.compare(o1.getCategory()[0], o2.getCategory()[0])==0) {
			if (cmp.compare(o1.getCategory()[1], o2.getCategory()[1]) > 0) {
				return 1;
			} else if (cmp.compare(o1.getCategory()[1], o2.getCategory()[1]) < 0) {
				return -1;
			}
			return 0;
		}else if(cmp.compare(o1.getCategory()[0], o2.getCategory()[0])>0){
			return 1;
		}else if(cmp.compare(o1.getCategory()[0], o2.getCategory()[0])<0){
			return -1;
		}
		return 0;
	}
}
