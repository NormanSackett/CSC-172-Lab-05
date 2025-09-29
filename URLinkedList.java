import java.util.Collection;
import java.util.Iterator;

public class URLinkedList<E> implements URList<E>, Iterator<E> {

	URNode<E> head = new URNode<E>(null, null, null);
	URNode<E> tail = new URNode<E>(null, null, null);
	URNode<E> iteratorNode = head;
	
	// initialize an empty linked list
	public URLinkedList() {
		head.setNext(tail);
		tail.setPrev(head);
	}
	
	// initialize a linked list with data from an object array
	public URLinkedList(E[] arr) {
		URNode<E> tempNode = head;
		for (int i = 0; i < arr.length; i++) {
			tempNode.setNext(new URNode<E>(arr[i], tempNode, null));
			tempNode = tempNode.next();
		}
		tempNode.setNext(tail);
		tail.setPrev(tempNode);
	}
	
	// Prints the contents of the linked list to the stream
	public void print() {
		System.out.print("[");
		URNode<E> curNode = head;
		while (curNode.next() != tail) {
			if (curNode != head) System.out.print(", ");
			curNode = curNode.next();
			System.out.print(curNode.element().toString());
		}
		System.out.println("]");
	}
	
	// Inserts the specified element at the beginning of this list.
	public void addFirst(E e) {
		URNode<E> n = head.next();
		head.setNext(new URNode<E>(e, head, n));
		n.setPrev(head.next());
	}
	
	// Appends the specified element to the end of this list.
	public void addLast(E e) {
		URNode<E> p = tail.prev();
		tail.setPrev(new URNode<E>(e, p, tail));
		p.setNext(tail.prev());
	}
	
	// Retrieves, but does not remove, the first element of this list, or returns null if this list is empty.
	public URNode<E> peekFirst() {
		if (head.next() == tail) return null;
		else return head.next();
	}
	
	// Retrieves, but does not remove, the last element of this list, or returns null if this list is empty.
	public URNode<E> peekLast() {
		if (head.next() == tail) return null;
		else return tail.prev();
	}
	
	// Retrieves and removes the first element of this list, or returns null if this list is empty.
	public URNode<E> pollFirst() {
		if (head.next() == tail) return null;
		else {
			URNode<E> n = head.next();
			remove(0);
			return n;
		}
	}
	// Retrieves and removes the last element of this list, or returns null if this list is empty.
	public URNode<E> pollLast() {
		if (head.next() == tail) return null;
		else {
			URNode<E> n = tail.prev();
			tail.setPrev(n.prev());
			tail.prev().setNext(tail);
			return n;
		}
	}
	
	
	//LIST METHODS

	@SuppressWarnings("unchecked") // e is already of type E within the URList interface, so it need not be checked
	@Override
	public boolean add(Object e) {
		URNode<E> p = tail.prev();
		tail.setPrev(new URNode<E>((E) e, p, tail));
		p.setNext(tail.prev());
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void add(int index, Object element) {
		URNode<E> e = new URNode<E>((E) element, null, null);
		URNode<E> curNode = head;
		for (int i = 0; i < index; i++) {
			if (curNode.next() == tail) return;
			curNode = curNode.next();
		}
		e.setPrev(curNode);
		e.setNext(curNode.next());
		curNode.setNext(e);
		e.next().setPrev(e);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean addAll(Collection c) {
		URNode<E> p;
		E[] arr = (E[]) c.toArray();
		for (int i = 0; i < arr.length; i++) {
			p = tail.prev();
			tail.setPrev(new URNode<E>(arr[i], p, tail));
			p.setNext(tail.prev());
		}
		return true;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public boolean addAll(int index, Collection c) {
		if (get(index) == null) return false;
		E[] arr = (E[]) c.toArray();
		for (int i = index; i < arr.length + index; i++) {
			add(i, arr[i - index]);
		}
		return true;
	}

	@Override
	public void clear() {
		head.setNext(tail);
		tail.setPrev(head);
	}

	@Override
	public boolean contains(Object o) {
		URNode<E> curNode = head.next();
		while(curNode.next() != null) {
			if (curNode.element() == o) return true;
			curNode = curNode.next();
		}
		return false;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public boolean containsAll(Collection c) {
		E[] arr = (E[]) c.toArray();
		for (int i = 0; i < arr.length; i++) {
			if (!contains(arr[i])) return false;
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) { // ??????
		if (size() != ((URLinkedList) o).size()) return false; // assume o is a comparable linked list
		for (int i = 0; i < ((URLinkedList) o).size(); i++) {
			if(get(i) != ((URLinkedList) o).get(i)) return false;
		}
		return true;
	}

	@Override
	public E get(int index) {
		URNode<E> n = head;
		for (int i = 0; i <= index; i++) {
			n = n.next();
			if (n == tail) return null;
		}
		return n.element();
	}

	@Override
	public int indexOf(Object o) {
		int index = 0;
		URNode<E> curNode = head;
		while (curNode.next() != tail) {
			curNode = curNode.next();
			if (curNode.element() == o) return index;
			index++;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		if (head.next() == tail) return true;
		else return false;
	}

	@Override
	public Iterator<E> iterator() {
		return this;
	}

	@Override
	public E remove(int index) {
		URNode<E> curNode = head;
		for (int i = 0; i <= index; i++) {
			if (curNode.next() == tail) return null;
			curNode = curNode.next();
		}
		curNode.next().setPrev(curNode.prev());
		curNode.prev().setNext(curNode.next());
		return curNode.element();
	}

	@Override
	public boolean remove(Object o) {
		URNode<E> curNode = head;
		while(curNode.next() != tail) {
			curNode = curNode.next();
			if (curNode.element() == o) {
				curNode.next().setPrev(curNode.prev());
				curNode.prev().setNext(curNode.next());
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean removeAll(Collection c) {
		URNode<E> curNode = head;
		while(curNode.next() != tail) {
			curNode = curNode.next();
			if (c.contains(curNode.element())) {
				curNode.next().setPrev(curNode.prev());
				curNode.prev().setNext(curNode.next());
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E set(int index, Object element) {
		URNode<E> curNode = head;
		for (int i = 0; i <= index; i++) {
			if (curNode.next() == tail) return null;
			curNode = curNode.next();
		}
		E prevElement = curNode.element();
		curNode.setElement((E) element);
		return prevElement;
	}

	@Override
	public int size() {
		URNode<E> curNode = head;
		int index = 0;
		while (curNode.next() != tail) {
			curNode = curNode.next();
			index++;
		}
		return index;
	}

	@SuppressWarnings("unchecked")
	@Override
	public URList<E> subList(int fromIndex, int toIndex) {
		if (toIndex >= size() || fromIndex < 0) return null;
		
		Object[] arr = new Object[size() - fromIndex - (size() - toIndex) + 1];
		for (int i = fromIndex; i < arr.length + fromIndex; i++) {
			arr[i - fromIndex] = get(i);
		}
		URList<E> sub = new URLinkedList<E>((E[]) arr);
		return sub;
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = get(i);
		}
		return arr;
	}
	
	
	//ITERATOR METHODS

	@Override
	public boolean hasNext() {
		if (iteratorNode.next() != tail) return true;
		else return false;
	}

	@Override
	public E next() {
		if (iteratorNode.next() != tail) {
			iteratorNode = iteratorNode.next();
			return iteratorNode.element();
		} else {
			return null;
		}
	}
}
