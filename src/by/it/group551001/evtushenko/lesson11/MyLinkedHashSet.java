package by.it.group551001.evtushenko.lesson11;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;



public class MyLinkedHashSet<E> implements Set<E> {
	MyLinkedListt<MyLinkedListt.L<E>>[] a;
	MyLinkedListt<E> ll;
	int n, m;

	@SuppressWarnings("unchecked")
	public MyLinkedHashSet(){
		this.n = 0;
		this.m = 4;
		this.a = (MyLinkedListt<MyLinkedListt.L<E>>[]) new MyLinkedListt<?>[m];
		this.ll = new MyLinkedListt<>();
		for (int i = 0; i < m; i++) {
			a[i] = new MyLinkedListt<>();
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
		ll.clear();
	}

	private int gi(E e, int mod){
		return (e.hashCode() & 0x7FFFFFFF) % mod;
	}

	@SuppressWarnings("unchecked")
	public void reb(){
		MyLinkedListt<MyLinkedListt.L<E>>[] b = (MyLinkedListt<MyLinkedListt.L<E>>[]) new MyLinkedListt<?>[m << 1];
		for (int i = 0; i < m << 1; i++) {
			b[i] = new MyLinkedListt<>();
		}

		MyLinkedListt.L<E> r = ll.f;
		while(r != null){
			b[gi(r.v, m << 1)].add(r);
			r = r.n;
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
		MyLinkedListt<MyLinkedListt.L<E>> ai = a[i];

		MyLinkedListt.L<MyLinkedListt.L<E>> r = ai.f;
		while(r != null){
			if(e.equals(r.v.v))return false;
			r = r.n;
		}

		ll.add(e);

		ai.add(ll.l);
		++n;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean remove(Object o){
		int i = gi((E)o, m);
		MyLinkedListt<MyLinkedListt.L<E>> ai = a[i];
		MyLinkedListt.L<MyLinkedListt.L<E>> r = ai.f;
		while(r != null){
			if(r.v.v.equals(o)){
				if(r.v == ll.f) {
					ll.f = r.v.n;
				}
				if(r.v == ll.l) {
					ll.l = r.v.p;
				}
				if(r.v.n != null)r.v.n.p = r.v.p;
				if(r.v.p != null)r.v.p.n = r.v.n;
				--ll.n;
				ai.remove(r.v);
				--n;
				return true;
			}
			r = r.n;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean contains(Object o) {
		int i = gi((E)o, m);
		MyLinkedListt.L<MyLinkedListt.L<E>> r = a[i].f;
		while(r != null){
			if(r.v.v.equals(o))return true;
			r = r.n;
		}
		return false;
	}

	public String toString() {
		if(this.isEmpty())return "[]";
		String S = new String();
		S += "[";
		MyLinkedListt.L<E> r = ll.f;
		S += r.v.toString();
		r = r.n;
		while(r != null){
			S += ", ";
			S += r.v.toString();
			r = r.n;
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
	public boolean containsAll(Collection<?> c){
		for(Object ci: c)if(!contains(ci))return false;
		return true;
	}
	
	@Override@SuppressWarnings("unchecked")
	public boolean addAll(Collection<? extends E> c){
		boolean res = false;
		for(Object ci: c)res = add((E)ci) || res;
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> c){
		boolean res = false;
		for(Object ci: c)res = remove(ci) || res;
		return res;
	}

	@Override@SuppressWarnings("unchecked")
	public boolean retainAll(Collection<?> c){

		E[] ac = (E[])new Object[n];
		MyLinkedListt.L<E> r = ll.f;
		for(int i = 0; i < n; ++i){
			ac[i] = r.v;
			r = r.n;
		}

		int nc = n;
		clear();

		for(int i = 0; i < nc; ++i)if(c.contains(ac[i]))add(ac[i]);

		return n != nc;
	}
}
