package com.zony.common.filenet.util;

import com.filenet.api.collection.EngineCollection;
import org.apache.commons.collections4.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EngineCollectionUtils {

	public static <T> List<T> c(EngineCollection ec, Class<T> cls) {
		Iterator<T> it = ec.iterator();
		List<T> c = new ArrayList<T>();
		CollectionUtils.addAll(c, it);
		return c;
	}

}
