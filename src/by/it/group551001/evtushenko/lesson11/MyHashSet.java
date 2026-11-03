package by.it.group551001.evtushenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import by.it.group551001.evtushenko.lesson10.MyLinkedList;

public class MyHashSet<E> implements Set<E> {
	private MyLinkedList<E>[] a;
	int n, m;

	@SuppressWarnings("unchecked")
	public MyHashSet(){
		this.n = 0;
		this.m = 4;
		a = (MyLinkedList<E>[])new MyLinkedList<?>[m];
		for (int i = 0; i < m; i++) {
			a[i] = new MyLinkedList<>();
		}
	}

	@Override
	public int size() {
		return n;
	}

	@Override
	public boolean isEmpty() {
		return n == 0;
	}

	@Override
	public void clear() {
		n = 0;
		for(int i = 0; i < m; ++i)a[i].clear();
	}

	private int gi(E e, int mod){
		return (e.hashCode() & 0x7FFFFFFF) % mod;
	}

	@SuppressWarnings("unchecked")
	private void reb(){
		MyLinkedList<E>[] b = (MyLinkedList<E>[])new MyLinkedList<?>[m<<1];
		for (int i = 0; i < m << 1; i++) {
			b[i] = new MyLinkedList<>();
		}
		for(int i = 0; i < m; ++i){
			while(!a[i].isEmpty()){
				E fi = a[i].pollFirst();
				b[gi(fi, m << 1)].add(fi);
			}
		}
		a = b;
		m <<= 1;
	}

	@Override
	public boolean add(E e) {
		if(n >= m){
			reb();
		}
		int i = gi(e, m);
		MyLinkedList<E> ai = a[i];

		if(a[i] == null || a[i].contains(e))return false;
		ai.add(e);
		++n;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o){
		int i = gi((E)o, m);
		if(a[i] == null || !a[i].contains(o))return false;
		a[i].remove(o);
		--n;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		int i = gi((E)o, m);
		return a[i].contains(o);
	}

	public String toString() {
		if(this.isEmpty())return "[]";
		String S = new String();
		S += "[";
		for(int i = 0; i < m; ++i){
			if(a[i].isEmpty())continue;
			if(S.length() != 1)S += ", ";
			String s = a[i].toString();
			S += s.substring(1, s.length() - 1);
		}
		S += "]";
		return S;
	}

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("iterator method is not yet implemented.");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("toArray method is not yet implemented.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("toArray method is not yet implemented.");
    }

    

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll method is not yet implemented.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("addAll method is not yet implemented.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll method is not yet implemented.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll method is not yet implemented.");
    }
}
