import java.util.Iterator;

public class URHashTable<K, V> extends UR_HashTable<K, V> implements Iterable<K> {
	K[] keys;
	V[] values;
	int n, m, inserts, collisions;
	
	@SuppressWarnings("unchecked")
	public URHashTable(int cap) {
		this.keys = (K[]) new Object[cap];
		this.values = (V[]) new Object[cap];
		this.m = cap;
	}
	
	@SuppressWarnings("unchecked")
	public URHashTable() {
		keys = (K[]) new Object[INIT_CAPACITY];
		values = (V[]) new Object[INIT_CAPACITY];
		m = INIT_CAPACITY;
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i < m; i++) {
			str += "[" + keys[i] + ", " + values[i] + "]\n";
		}
		return str;
	}

	@Override
	public void put(K key, V val) { //linear probing of hash table
		if (key == null) return;
		
		int iteration = 0, index = key.hashCode() % m;
		if (n == m) resize(m);
		while (iteration < m) {
			if (values[index] == null) {
				values[index] = val;
				keys[index] = key;
				n++;
				inserts++;
				break;
			}
			index++;
			iteration++;
			collisions++;
			if (index == m) index = 0;
		}
	}

	@Override
	public V get(K key) {
		if (key == null) return null;
		
		for (int i = 0; i < m; i++)
			if (keys[i].equals(key))
				return values[i];

		return null;
	}

	@Override
	public void delete(K key) {
		if (key == null) return;
		
		for (int i = 0; i < m; i++)
			if (keys[i].equals(key)) {
				keys[i] = null;
				values[i] = null;
				n--;
			}
	}

	@Override
	public int size() {
		return m;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < m; i++)
			if (values[i] != null)
				return false;
		
		return true;
	}

	@Override
	public boolean contains(K key) {
		if (key == null) return false;
		
		for (int i = 0; i < keys.length; i++)
			if (keys[i] != null && keys[i].equals(key))
				return true;
	
		return false;
	}
	
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		if (capacity < 0) return;
		
		K[] tempKeys = keys.clone();
		V[] tempVals = values.clone();
		m = capacity * 2;
		keys = (K[]) new Object[m];
		values = (V[]) new Object[m];
		n = 0;
		inserts = 0;
		collisions = 0;
		
		for (int i = 0; i < capacity; i++) {
			if (tempKeys[i] != null) {
				put(tempKeys[i], tempVals[i]);
			}
		}
	}
	
	@Override
	public Iterable<K> keys() {
		return this;
	}

	@Override
	public Iterator<K> iterator() {
		return new URHashTableIterator();
	}
	
	private class URHashTableIterator implements Iterator<K> {
		int iteratorIndex = 0;

		@Override
		public boolean hasNext() {
			if (probeNext() == null)
				return false;
			else
				return true;
		}
		
		private K probeNext() {
			int i = iteratorIndex;
			while (i < m) {
				if (keys[i] != null)
					return keys[i++];
				else i++;
			}
			return null;
		}
		
		@Override
		public K next() {
			while (iteratorIndex < m) {
				if (keys[iteratorIndex] != null)
					return keys[iteratorIndex++];
				else iteratorIndex++;
			}
			return null;
				
		}
	}
}
