package io.pivotal.geode.size.function;

import org.apache.geode.internal.cache.RegionEntry;
import org.apache.geode.internal.size.ObjectGraphSizer.ObjectFilter;

public class RegionEntryObjectFilter implements ObjectFilter {

	private boolean logAllClasses = false;

	private boolean logRejectedClasses = false;

	private boolean logAcceptedClasses = false;

	public boolean accept(Object parent, Object object) {
		boolean accept = true;
		String parentClassName = null;
		if (this.logAllClasses || this.logRejectedClasses || this.logAcceptedClasses) {
			if (parent != null) {
				parentClassName = parent.getClass().getName();
			}
		}
		// Reject the linked list of RegionEntries
		if (object instanceof RegionEntry && parent != null && parent instanceof RegionEntry) {
			if (this.logAllClasses || this.logRejectedClasses) {
				System.out.println("Rejecting parent=" + parentClassName + "; object=" + object.getClass().getName());
			}
			accept = false;
		} else {
			if (this.logAllClasses || this.logAcceptedClasses) {
				// System.out.println("Accepting parent=" + parent + "
				// parentIdentity=" + System.identityHashCode(parent) + " (an
				// instance of " + parentClassName + "); object=" + object + "
				// objectIdentity=" + System.identityHashCode(object) + " (an
				// instance of " + object.getClass().getName() + ")");
				System.out.println("Accepting object=" + object + " objectIdentity=" + System.identityHashCode(object)
						+ " (an instance of " + object.getClass().getName() + ")");
			}
		}
		return accept;
	}
}