package io.pivotal.geode.size.function;

import java.util.Properties;

import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.query.Index;
import org.apache.geode.cache.query.QueryService;

public class IndexSizeCalculator implements Function, Declarable {

	private final QueryService queryService;

	public IndexSizeCalculator() {
		this.queryService = CacheFactory.getAnyInstance().getQueryService();
	}

	public void execute(FunctionContext context) {
		// Calculate sizes of indexes
		RegionFunctionContext rfc = (RegionFunctionContext) context;
		IndexSizes indexSizes = new IndexSizes();
		for (Index index : this.queryService.getIndexes(rfc.getDataSet())) {
			long indexSize = ObjectSizer.calculateSize(index, false);
			indexSizes.addIndexSize(index.getName(), indexSize);
		}

		// Return result
		context.getResultSender().lastResult(indexSizes);
	}

	public String getId() {
		return getClass().getSimpleName();
	}

	public boolean optimizeForWrite() {
		return true;
	}

	public boolean hasResult() {
		return true;
	}

	public boolean isHA() {
		return true;
	}

	public void init(Properties properties) {
	}
}