package io.pivotal.geode.size.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class IndexSizes implements Serializable {

	private final Map<String, Long> indexSizes;

	public IndexSizes() {
		this.indexSizes = new HashMap<String, Long>();
	}

	public void addIndexSize(String indexName, Long indexSize) {
		this.indexSizes.put(indexName, indexSize);
	}

	public Map<String, Long> getIndexSizes() {
		return this.indexSizes;
	}

	public String toString() {
		return new StringBuilder().append(getClass().getName()).append("[").append("indexSizes=")
				.append(this.indexSizes).append("]").toString();
	}
}