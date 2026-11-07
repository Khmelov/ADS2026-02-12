package by.it.group551001.evtushenko.lesson10;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

public class MyPriorityQueue<E extends Comparable<? super E>> implements Queue<E> {


	static private class MaxHeap<T extends Comparable<? super T>> {
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! НАЧАЛО ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
			//тут запишите ваше решение.
			//Будет мало? Ну тогда можете его собрать как Generic и/или использовать в варианте B
			@SuppressWarnings("unchecked")
			T[] heap = (T[]) new Comparable[4];
			int n = 0, m = 4;
			private static final int MAXM = 1 << 30;

			private void swap(int i, int j){
				T temp = heap[i];
				heap[i] = heap[j];
				heap[j] = temp;
			}

			private void recap(int nm){
				@SuppressWarnings("unchecked")
				T[] b = (T[])new Comparable[nm];
				for(int i = 0; i < n; ++i)b[i] = heap[i];
				heap = b;
			}
	
			private boolean getnewCap(){
				while(n >= m){
					if(m << 1 >= MAXM)return false;
					m <<= 1;
				}
				return true;
			}
	
			private boolean add(T e) {
				++n;
				boolean b = getnewCap();
				if(b == false){--n;return b;}
				recap(m);
				heap[n - 1] = e;
				return true;
			}

			private int siftDown(int i) {
					int s = n;

					while(i < (s >> 1)){
							if((i << 1) + 2 == s){
									if(heap[i].compareTo(heap[((i << 1) + 1)]) > 0)swap(i, (i << 1) + 1);
									break;
							}

							T p = heap[i], ch1 = heap[(i << 1) + 1], ch2 = heap[((i << 1) + 2)];

							if(p.compareTo(ch1) <= 0 &&  p.compareTo(ch2) <= 0) break;

							if(ch1.compareTo(ch2) <= 0) {
									swap(i, (i << 1) + 1);

									i = (i << 1) + 1;
							} else {
									swap(i, (i << 1) + 2);

									i = (i << 1) + 2;
							}
					}
					return i;
			}

			private int siftUp(int i) { 
					for(;i > 0 && heap[i].compareTo(heap[(i - 1) >> 1]) < 0;i = (i - 1) >> 1)
							swap(i, (i - 1) >> 1);
					
					return i;
			}

			public void heapify(){
				for(int i = n / 2 - 1; i >= 0; --i)siftDown(i);
			}

			public void insert(T value) {
					add(value);
					siftUp(n - 1);
			}

			public T top(){
				return heap[0];
			}

			public T extractMax() { 
					swap(0, n - 1);
					T result = heap[n - 1];
					--n;
					siftDown(0);

					return result;
			}
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! КОНЕЦ ЗАДАЧИ !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
	}

	@Override public Iterator<E> iterator() { return null; }
	@Override public Object[] toArray() { return null; }
	@Override public <T> T[] toArray(T[] a) { return null; }

	MaxHeap<E> pq = new MaxHeap<E>();





	@Override
	public boolean add(E el){
		pq.insert(el);
		return true;
	}

	@Override
	public int size(){
		return pq.n;
	}

	@Override
	public void clear(){
		pq.n = 0;
	}

	@Override 
	public E remove(){
		if(isEmpty())throw new NoSuchElementException();
		return pq.extractMax();
	}

	@Override
	public boolean remove(Object o){
		Collection<Object> c = List.of(o);
		return removeAll(c);
	}

	@Override
	public boolean isEmpty(){
		return pq.n == 0;
	}

	@Override
	public boolean contains(Object o){
		for(int i = 0; i < pq.n; ++i)if(o.equals(pq.heap[i]))return true;
		return false;
	}

	public boolean contains(E o){
		for(int i = 0; i < pq.n; ++i)if(o.equals(pq.heap[i]))return true;
		return false;
	}
	
	@Override
	public boolean offer(E e){
		pq.insert(e);
		return true;
	}
	
	@Override
	public E poll(){
		if(isEmpty())return null;
		return pq.extractMax();
	}

	@Override
	public E peek(){
		if(isEmpty())return null;
		return pq.top();
	}

	@Override
	public E element(){
		if(isEmpty())throw new NoSuchElementException();
		return pq.top();
	}

	@Override
	public boolean containsAll(Collection<?> c){
		for(Object ci: c){
			if(!contains(ci))return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c){
		for(E ci: c){
			pq.insert(ci);
		}
		return !c.isEmpty();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int nn = pq.n;
		for(int i = 0, j = 0; i < pq.n; ++i){
			if(!c.contains(pq.heap[i]))pq.heap[j++] = pq.heap[i];
			else --nn;
		}
		int on = pq.n;
		pq.n = nn;
		pq.heapify();
		return pq.n != on;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int nn = pq.n;
		for(int i = 0, j = 0; i < pq.n; ++i){
			if(c.contains(pq.heap[i]))pq.heap[j++] = pq.heap[i];
			else --nn;
		}
		int on = pq.n;
		pq.n = nn;
		pq.heapify();
		return pq.n != on;
	}

	@Override
	public String toString() {
		if(this.isEmpty())return "[]";
		String s = new String();
		s += "[";
		s += pq.heap[0].toString();
		for(int i = 1; i < pq.n; ++i)s += ", " + pq.heap[i].toString();
		s += "]";
		return s;
	}

	

	

	
}
