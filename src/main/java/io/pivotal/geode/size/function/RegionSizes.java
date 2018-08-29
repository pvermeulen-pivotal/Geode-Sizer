package io.pivotal.geode.size.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegionSizes implements Serializable {

	private final Map<String, Long> regionSizes;

	public RegionSizes() {
		this.regionSizes = new HashMap<String, Long>();
	}

	public void addRegionSize(String regionName, Long regionSize) {
		this.regionSizes.put(regionName, regionSize);
	}

	public Map<String, Long> getRegionSizes() {
		return this.regionSizes;
	}

	public String toString() {
		return new StringBuilder().append(getClass().getName()).append("[").append("regionSizes=")
				.append(this.regionSizes).append("]").toString();
	}
}