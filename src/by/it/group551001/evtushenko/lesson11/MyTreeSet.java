package by.it.group551001.evtushenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class MyTreeSet<E extends Comparable<E>> implements Set<E> {
	@SuppressWarnings("unchecked")
	private E[] a = (E[]) new Comparable[4];
	private int n = 0, m = 4;
	private static final int MAXM = 1 << 30;

	@SuppressWarnings("unchecked")
	MyTreeSet(){
		a = (E[]) new Comparable[4]; m = 4; n = 0;
	}

	@SuppressWarnings("unchecked")
	private void mSort(E[] a, int l, int r){
		if(l == r) return;

		int m = (l + r) >> 1;

		mSort(a, l, m);
		mSort(a, m + 1, r);

		E[] merged = (E[])new Comparable[r - l + 1];

		int i1 = l, i2 = m + 1;

		while(i1 <= m && i2 <= r)
			merged[i1 + i2 - l - m - 1] = a[i1].compareTo(a[i2]) <= 0 ? a[i1++] : a[i2++];

		while(i1 <= m)
			merged[i1 + i2 - l - m - 1]
				= a[i1++];
		
		while(i2 <= r)
			merged[i1 + i2 - l - m - 1]
				= a[i2++];

		for(int i = l; i <= r; ++i)
			a[i] = merged[i - l];
	}

	@Override
	public String toString() {
		if(this.isEmpty())return "[]";
		String s = new String();
		s += "[";
		s += a[0].toString();
		for(int i = 1; i < n; ++i)s += ", " + a[i].toString();
		s += "]";
		return s;
	}

	private void recap(int nm){
		@SuppressWarnings("unchecked")
		E[] b = (E[])new Comparable[nm];
		for(int i = 0; i < n; ++i)b[i] = a[i];
		a = b;
	}
	private void recap(int nm, int oldn){
		@SuppressWarnings("unchecked")
		E[] b = (E[])new Comparable[nm];
		for(int i = 0; i < oldn; ++i)b[i] = a[i];
		a = b;
	}

	private boolean getnewCap(){
		while(n >= m){
			if(m << 1 >= MAXM)return false;
			m <<= 1;
		}
		return true;
	}

	@Override
	public boolean add(E e) {
		if(contains(e))return false;
		++n;
		boolean b = getnewCap();
		if(b == false){--n;return b;}
		recap(m);
		a[n - 1] = e;
		mSort(a, 0, n - 1);
		return true;
	}


	public E remove(int index) {
		E res = a[index];
		for(int i = index + 1; i < n; ++i)a[i - 1] = a[i];
		--n;
		mSort(a, 0, n - 1);
		return res;
	}

	@Override
	public int size() {
		return n;
	}

	public void add(int index, E element) {
		++n;
		int om = m;
		@SuppressWarnings("unused")
		boolean __ = getnewCap();
		if(om != m)recap(m);
		for(int i = n - 1; i-- != index;)a[i + 1] = a[i];
		a[index] = element;
		mSort(a, 0, n - 1);
	}

	@Override
	public boolean remove(Object o) {
		for(int i = 0; i < n; ++i)if(a[i].equals(o)){remove(i);return true;}
		return false;
	}

	public E set(int index, E element) {
		E res = a[index];
		a[index] = element;
		mSort(a, 0, n - 1);
		return res;
	}


	@Override
	public boolean isEmpty() {
		return n == 0;
	}


	@Override
	public void clear() {
		n = 0;
	}

	public int indexOf(Object o) {
		for(int i = 0; i < n; ++i)if(a[i].equals(o))return i;
		return -1;
	}

	public E get(int index) {
		return a[index];
	}

	@Override
	public boolean contains(Object o) {
		for(int i = 0; i < n; ++i)if(a[i].equals(o))return true;
		return false;
	}

	public int lastIndexOf(Object o) {
		for(int i = n; i-- != 0;)if(a[i].equals(o))return i;
		return -1;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object ci: c){
			boolean b = false;
			for(int j = 0; j < n; ++j)if(a[j].equals(ci)){b = true;break;}
			if(b == false)return b;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		int on = n;
		for(E ci: c){
			this.add(ci);
		}
		mSort(a, 0, n - 1);
		return on != n;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > n) {
			throw new IndexOutOfBoundsException();
		}
		int cs = c.size();
		if(cs == 0)return false;
		n += cs;
		boolean bb = getnewCap();
		if(!bb)throw new OutOfMemoryError();
		recap(m, n - cs);
		for(int i = n; i-- - cs > index;)a[i] = a[i - cs];
		int j = index;
		for(E ci: c){
			a[j++] = ci;
		}
		mSort(a, 0, n - 1);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int nn = n;
		for(int i = 0, j = 0; i < n; ++i){
			if(!c.contains(a[i]))a[j++] = a[i];
			else --nn;
		}
		int on = n;
		n = nn;
		mSort(a, 0, n - 1);
		return n != on;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		int nn = n;
		for(int i = 0, j = 0; i < n; ++i){
			if(c.contains(a[i]))a[j++] = a[i];
			else --nn;
		}
		int on = n;
		n = nn;
		return n != on;
	}


	
	@Override
	public <T> T[] toArray(T[] a) {


		return null;
	}

	@Override
	public Object[] toArray() {

		return null;
	}

	@Override
	public Iterator<E> iterator() {
			return null;
	}
}
