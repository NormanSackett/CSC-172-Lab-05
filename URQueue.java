
public class URQueue<E> extends URLinkedList<E> {
	
	public URQueue() {
		super();
	}
	
	public void enqueue(E e) {
		this.addLast(e);
	}
	
	public E dequeue() {
		return this.pollFirst().element();
	}
	
	public E peek() {
		return this.peekFirst().element();
	}
}

