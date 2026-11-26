package by.it.group551001.evtushenko.lesson10;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayDeque<E> implements Collection<E> {

	@Override
	public boolean retainAll(Collection<?> C){return true;}

	@Override
	public boolean contains(Object o){return true;}

	@Override
	public Iterator<E> iterator(){return null;}

	@Override
	public boolean removeAll(Collection<?> C){return true;}

	@Override
	public Object[] toArray(){return null;}

	@Override
	public <T> T[] toArray(T[] a){return null;}

	@Override
	public boolean containsAll(Collection<?> C){return true;}

	@Override
	public boolean addAll(Collection<? extends E> C){return true;}

	@Override
	public boolean remove(Object o){return true;}



	@SuppressWarnings("unchecked")
	private E[] a = (E[])new Object[4];
	int n = 0, m = 4;
	int s = 0, e = 0;

	int MAXM = 1 << 30;

	

	private boolean recap(){
		if(m > n + 3)return true;
		if(m >= MAXM)return false;
		
		int nm = m << 1;
		@SuppressWarnings("unchecked")
		E[] b = (E[])new Object[nm];
		if(isEmpty()){a = b;m = nm;clear();return true;}
		for(int i = s, j = 0; j < n; i = (i + 1) % m, ++j)b[j] = a[i];
		m = nm;
		a = b;
		s = 0; e = n;
		return true;
	}

	public boolean add(E el){
		addLast(el);
		return true;
	}

	public void addFirst(E el){
		if(!recap())throw new IllegalStateException();
		++n;
		a[s = (s + m - 1) % m] = el;
	}

	public void addLast(E el){
		if(!recap())throw new IllegalStateException();
		++n;
		a[e] = el;
		e = (e + 1) % m;
	}

	public boolean offer(E el){
		if(!recap())return false;
		++n;
		a[e++] = el;
		return true;
	}


	public E element(){
		if(isEmpty())throw new NoSuchElementException();
		return a[s];
	}
	public E getFirst(){
		if(isEmpty())throw new NoSuchElementException();
		return a[s];
	}
	public E getLast(){
		if(isEmpty())throw new NoSuchElementException();
		return a[(e + m - 1) % m];
	}

	public E poll(){
		return pollFirst();
	}
	public E pollFirst(){
		if(isEmpty())throw new NoSuchElementException();
		E res = a[s];
		s = (s + 1) % m;
		--n;
		return res;
	}
	public E pollLast(){
		if(isEmpty())throw new NoSuchElementException();
		--n;
		return a[e = (e + m - 1) % m];
	}

	public int size(){return n;}
	public void clear(){
		n = e = s = 0;
	}

	public String toString() {
		if(this.isEmpty())return "[]";
		String S = new String();
		S += "[";
		S += a[s].toString();
		for(int i = (s + 1) % m; i != e; i = (i + 1) % m)S += ", " + a[i].toString();
		S += "]";
		return S;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	
}
