package io.pivotal.geode.size.function;

import java.util.Properties;

import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;

public class RegionSizeCalculator implements Function, Declarable {

	public void execute(FunctionContext context) {
		// Get the region
		String regionName = (String) context.getArguments();
		Region region = CacheFactory.getAnyInstance().getRegion(regionName);

		// Calculate size
		RegionSizes regionSizes = new RegionSizes();
		regionSizes.addRegionSize(regionName, ObjectSizer.calculateSize(region, false));

		// Return result
		context.getResultSender().lastResult(regionSizes);
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