package io.pivotal.geode.size.function;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.Index;
import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.size.ObjectGraphSizer;
import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;
import org.apache.geode.internal.size.ReflectionObjectSizer;

public class ObjectSizer {

	private static final boolean LOG_SIZE = false;

	public static long calculateSize(Region region, boolean dumpHistogram) {
		long size = 0l;
		ObjectFilter filter = new RegionObjectFilter();
		try {
			size = ObjectGraphSizer.size(region, filter, false);
			if (dumpHistogram) {
				dumpHistogram(region, filter);
			}
			if (LOG_SIZE) {
				System.out.println("Size of " + region.getFullPath() + " (an instance of " + region.getClass().getName()
						+ "): " + size);
			}
		} catch (Exception e) {
			System.out.println("Caught the following exception attempting to dump the size of region "
					+ region.getFullPath() + ":" + e);
		}
		return size;
	}

	public static long calculateSize(RegionEntry regionEntry, boolean dumpHistogram) {
		// System.out.println("ObjectSizer calculating size of entry: " +
		// regionEntry);
		long size = 0l;
		ObjectFilter filter = new RegionEntryObjectFilter();
		try {
			size = ObjectGraphSizer.size(regionEntry, filter, false);
			if (dumpHistogram) {
				dumpHistogram(regionEntry, filter);
			}
			if (LOG_SIZE) {
				System.out.println("Size of " + regionEntry + " (an instance of " + regionEntry.getClass().getName()
						+ "): " + size);
			}
		} catch (Exception e) {
			System.out
					.println("Caught the following exception attempting to dump the size of " + regionEntry + ":" + e);
		}
		return size;
	}

	public static long calculateSize(Index index, boolean dumpHistogram) {
		// System.out.println("ObjectSizer calculating size of index: " +
		// index);
		ObjectFilter filter = new IndexObjectFilter();
		long size = 0l;
		try {
			size = ObjectGraphSizer.size(index, filter, false);
			if (dumpHistogram) {
				dumpHistogram(index, filter);
			}
			if (LOG_SIZE) {
				System.out
						.println("Size of " + index + " (an instance of " + index.getClass().getName() + "): " + size);
			}
		} catch (Exception e) {
			System.out.println("Caught the following exception attempting to dump the size of " + index + ":" + e);
		}
		return size;
	}

	public static int calculateSize(Object obj, boolean dumpHistogram) {
		// System.out.println("ObjectSizer calculating size of object: " + obj);
		int size = 0;
		try {
			size = ReflectionObjectSizer.getInstance().sizeof(obj);
			if (dumpHistogram) {
				dumpHistogram(obj);
			}
			if (LOG_SIZE) {
				System.out.println("Size " + obj + " (an instance of " + obj.getClass().getName() + "): " + size);
			}
		} catch (Exception e) {
			System.out.println("Caught the following exception attempting to dump the size of " + obj + ":" + e);
		}
		return size;
	}

	private static void dumpHistogram(Object obj) throws IllegalAccessException {
		System.out.println("Histogram for " + obj + " (an instance of " + obj.getClass().getName() + ")");
		System.out.println(ObjectGraphSizer.histogram(obj, false));
	}

	private static void dumpHistogram(Object obj, ObjectFilter filter) throws IllegalAccessException {
		System.out.println("Histogram for " + obj + " (an instance of " + obj.getClass().getName() + ")");
		System.out.println(ObjectGraphSizer.histogram(obj, filter, false));
	}

	private static final ObjectFilter LOGGING_FILTER = new ObjectFilter() {
		public boolean accept(Object parent, Object object) {
			String parentClassName = null;
			if (parent != null) {
				parentClassName = parent.getClass().getName();
			}
			System.out.println("Filtering parent=" + parentClassName + "; object=" + object.getClass().getName());
			return true;
		}
	};
}