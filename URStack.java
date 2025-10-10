
public class URStack<E> extends URLinkedList<E>{
	
	public URStack() {
		super();
	}
	
	public void push(E e) { 
		this.addFirst(e);
	}
	
	public E pop() {
		return this.pollFirst().element();
	}
	
	public E peek() {
		return this.peekFirst().element();
	}
}
