package io.pivotal.geode.size.function;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.geode.internal.cache.RegionEntry;

public class RegionEntrySizes implements Serializable {

	private final String regionName;

	private final List<RegionEntrySize> regionEntrySizes;

	private long totalRegionEntrySize;

	private int totalKeySize;

	private int totalValueSize;

	private static final DecimalFormat FORMAT = new DecimalFormat("#0.##");

	private static final boolean DUMP_HISTOGRAM = false;

	public RegionEntrySizes(String regionName) {
		this.regionName = regionName;
		this.regionEntrySizes = new ArrayList<RegionEntrySize>();
	}

	public int getNumberOfEntries() {
		return this.regionEntrySizes.size();
	}

	public long getTotalRegionEntrySize() {
		return this.totalRegionEntrySize;
	}

	public int getTotalKeySize() {
		return this.totalKeySize;
	}

	public int getTotalValueSize() {
		return this.totalValueSize;
	}

	public double getAverageRegionEntrySize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalRegionEntrySize * 1.0 / getNumberOfEntries();
	}

	public double getAverageKeySize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalKeySize * 1.0 / getNumberOfEntries();
	}

	public double getAverageValueSize() {
		return getNumberOfEntries() == 0 ? 0 : this.totalValueSize * 1.0 / getNumberOfEntries();
	}

	public void calculateEntrySize(RegionEntry regionEntry) {
		long regionEntrySize = ObjectSizer.calculateSize(regionEntry, DUMP_HISTOGRAM);
		this.totalRegionEntrySize += regionEntrySize;

		int keySize = ObjectSizer.calculateSize(regionEntry.getKey(), DUMP_HISTOGRAM);
		this.totalKeySize += keySize;

		int valueSize = ObjectSizer.calculateSize(regionEntry.getValueInVM(null), DUMP_HISTOGRAM);
		this.totalValueSize += valueSize;

		this.regionEntrySizes.add(new RegionEntrySize(regionEntrySize, keySize, valueSize));
	}

	public String toString() {
		return new StringBuilder().append(getClass().getName()).append("[").append("regionName=")
				.append(this.regionName).append("; numberOfEntries=")
				.append(NumberFormat.getInstance().format(getNumberOfEntries())).append("; totalRegionEntrySize=")
				.append(NumberFormat.getInstance().format(this.totalRegionEntrySize)).append("; totalKeySize=")
				.append(NumberFormat.getInstance().format(this.totalKeySize)).append("; totalValueSize=")
				.append(NumberFormat.getInstance().format(this.totalValueSize)).append("; averageRegionEntrySize=")
				.append(FORMAT.format(getAverageRegionEntrySize())).append("; averageKeySize=")
				.append(FORMAT.format(getAverageKeySize())).append("; averageValueSize=")
				.append(FORMAT.format(getAverageValueSize())).append("]").toString();
	}
}