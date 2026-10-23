package by.it.group551001.evtushenko.lesson10;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyLinkedList<E> implements Deque<E> {

	@Override public boolean offerFirst(E e) { return true; }
	@Override public boolean offerLast(E e) { return true; }
	@Override public E removeFirst() { return null; }
	@Override public E removeLast() { return null; }
	@Override public E peekFirst() { return null; }
	@Override public E peekLast() { return null; }
	@Override public boolean removeFirstOccurrence(Object o) { return true; }
	@Override public boolean removeLastOccurrence(Object o) { return true; }
	@Override public boolean offer(E e) { return true; }
	@Override public E remove() { return null; }
	@Override public E peek() { return null; }
	@Override public void push(E e) {}
	@Override public E pop() { return null; }
	@Override public boolean contains(Object o) { return true; }
	@Override public Iterator<E> iterator() { return null; }
	@Override public Iterator<E> descendingIterator() { return null; }
	@Override public Object[] toArray() { return null; }
	@Override public <T> T[] toArray(T[] a) { return null; }
	@Override public boolean containsAll(Collection<?> c) { return true; }
	@Override public boolean addAll(Collection<? extends E> c) { return true; }
	@Override public boolean removeAll(Collection<?> c) { return true; }
	@Override public boolean retainAll(Collection<?> c) { return true; }

	private static class L<T>{
		T v;
		L<T> n, p;
		L(T v){
			this.v = v;
			n = p = null;
		}
	}

	L<E> f = null, l = null;
	int n = 0;






	public boolean add(E el){
		addLast(el);
		return true;
	}

	public void addFirst(E el){
		L<E> nn = new L<E>(el);
		nn.n = f;
		f.p = nn;
		f = nn;
		++n;
	}

	public void addLast(E el){
		L<E> nn = new L<E>(el);
		if(n == 0){
			l = f = nn;
		}else {
			nn.p = l;
			l.n = nn;
			l = nn;
		}
		++n;
	}

	public E remove(int a){
		if(a >= n)throw new NoSuchElementException();
		if(a == 0){
			E res = f.v;
			if(n == 0){
				l = f = null;
				n = 0;
			} else {
				--n;
				f.n.p = null;
				f = f.n;
			}
			return res;
		}
		if(a == n - 1){
			E res = l.v;
			if(n == 1){
				l = f = null;
				n = 0;
			} else {
				--n;
				l.p.n = null;
				l = l.p;
			}
			return res;
		}
		--n;
		L<E> it = f;
		for(int i = 0; i < a - 1; ++i){
			it = it.n;
		}
		E res = it.n.v;
		it.n.n.p = it;
		it.n = it.n.n;
		return res;
	}
	public boolean remove(Object el){
		if(n == 0)return false;
		if(f.v.equals(el)){
			if(n == 1){
				l = f = null;
				n = 0;
				return true;
			} else {
				--n;
				f.n.p = null;
				f = f.n;
				return true;
			}
		}
		if(l.v.equals(el)){
			if(n == 1){
				l = f = null;
				n = 0;
				return true;
			} else {
				--n;
				l.p.n = null;
				l = l.p;
				return true;
			}
		}
		if(n <= 2)return false;
		for(L<E> it = f; it.n.n != null; it = it.n){
			if(it.n.v.equals(el)){
				--n;
				it.n.n.p = it;
				it.n = it.n.n;
				return true;
			}
		}
		return false;
	}


	public E element(){
		if(isEmpty())throw new NoSuchElementException();
		return f.v;
	}
	public E getFirst(){
		if(isEmpty())throw new NoSuchElementException();
		return f.v;
	}
	public E getLast(){
		if(isEmpty())throw new NoSuchElementException();
		return l.v;
	}

	public E poll(){
		return pollFirst();
	}
	public E pollFirst(){
		if(isEmpty())throw new NoSuchElementException();
		E res = f.v;
		remove(0);
		return res;
	}
	public E pollLast(){
		if(isEmpty())throw new NoSuchElementException();
		E res = l.v;
		remove(n - 1);
		return res;
	}

	public int size(){return n;}
	public void clear(){
		n = 0;
		l = f = null;
	}

	public String toString() {
		if(this.isEmpty())return "[]";
		String S = new String();
		S += "[";
		S += f.v.toString();
		for(L<E> it = f.n; it != null; it = it.n)S += ", " + it.v.toString();
		S += "]";
		return S;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	
}
