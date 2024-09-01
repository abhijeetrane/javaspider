package org.eclipse.contribution.minidraw;

import java.util.Iterator;
import java.util.List;

class Reversed implements Iterator {
		protected List elements;
		int i;
		
		public Reversed(List elements) {
			this.elements= elements;
			i= elements.size();
		}

		public boolean hasNext() {
			return i > 0;
		}

		public Object next() {
			return elements.get(--i)		;
		}

		public void remove() {
		}
	}
