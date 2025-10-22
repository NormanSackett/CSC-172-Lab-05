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

	@Override
	public void put(K key, V val) { //linear probing of hash table
		int iteration = 0, index = key.hashCode() % m;
		while (iteration < m) {
			if (values[index].equals(null)) {
				values[index] = val;
				keys[index] = key;
				n++;
				inserts++;
				break;
			}
			index++;
			iteration++;
			collisions++;
		}
	}

	@Override
	public V get(K key) {
		for (int i = 0; i < m; i++)
			if (keys[i].equals(key))
				return values[i];

		return null;
	}

	@Override
	public void delete(K key) {
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
			if (!values[i].equals(null))
				return false;
		
		return true;
	}

	@Override
	public boolean contains(K key) {
		for (int i = 0; i < keys.length; i++)
			if (keys[i].equals(key))
				return true;
	
		return false;
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
			if (probeNext().equals(null))
				return false;
			else
				return true;
		}
		
		private K probeNext() {
			int i = iteratorIndex;
			while (i < m) {
				if (!keys[i].equals(null))
					return keys[i++];
				else i++;
			}
			return null;
		}
		
		@Override
		public K next() {
			while (iteratorIndex < m) {
				if (!keys[iteratorIndex].equals(null))
					return keys[iteratorIndex++];
				else iteratorIndex++;
			}
			return null;
				
		}
	}
}
