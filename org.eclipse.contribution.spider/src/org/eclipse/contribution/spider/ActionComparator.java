package org.eclipse.contribution.spider;
import java.util.Comparator;

import org.eclipse.jface.action.IAction;
public final class ActionComparator implements Comparator {
	public int compare(Object o1, Object o2) {
		IAction a1= (IAction) o1;
		IAction a2= (IAction) o2;
		return a1.getText().compareTo(a2.getText());
	}
	public boolean equals(Object obj) {
		return false;
	}
}