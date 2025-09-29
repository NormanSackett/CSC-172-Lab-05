
public class URStack<E> extends URLinkedList<E>{
	public void push(E e) { 
		this.addFirst(e);
	}
	
	public E pop() {
		return this.pollFirst().element();
	}
}
