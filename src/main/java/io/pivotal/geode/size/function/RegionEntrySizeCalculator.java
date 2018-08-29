package io.pivotal.geode.size.function;

import java.util.Iterator;
import java.util.Properties;

import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;
import org.apache.geode.internal.cache.BucketRegion;
import org.apache.geode.internal.cache.LocalRegion;
import org.apache.geode.internal.cache.PartitionedRegion;

public class RegionEntrySizeCalculator implements Function, Declarable {

	public void execute(FunctionContext context) {
		// Get the local data
		RegionFunctionContext rfc = (RegionFunctionContext) context;
		Region region = rfc.getDataSet();

		RegionEntrySizes regionEntrySizes = new RegionEntrySizes(region.getFullPath());

		if (PartitionRegionHelper.isPartitionedRegion(region)) {
			processPartitionedValues(region, regionEntrySizes);
		} else {
			processReplicatedValues(region, regionEntrySizes);
		}

		// Return result
		context.getResultSender().lastResult(regionEntrySizes);
	}

	private void processPartitionedValues(Region region, RegionEntrySizes regionEntrySizes) {
		PartitionedRegion pr = (PartitionedRegion) region;
		for (BucketRegion br : pr.getDataStore().getAllLocalBucketRegions()) {
			for (Iterator i = br.entrySet().iterator(); i.hasNext();) {
				processRegionEntry(region, regionEntrySizes, i.next());
			}
		}
	}

	private void processReplicatedValues(Region region, RegionEntrySizes regionEntrySizes) {
		for (Iterator i = region.entrySet().iterator(); i.hasNext();) {
			processRegionEntry(region, regionEntrySizes, i.next());
		}
	}

	private void processRegionEntry(Region region, RegionEntrySizes regionEntrySizes, Object obj) {
		LocalRegion.NonTXEntry entry = (LocalRegion.NonTXEntry) obj;
		try {
			regionEntrySizes.calculateEntrySize(entry.getRegionEntry());
		} catch (Exception e) {
			System.out.println("Caught the following exception attempting to calculate the size of "
					+ entry.getRegionEntry() + ":" + e);
			e.printStackTrace();
		}
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