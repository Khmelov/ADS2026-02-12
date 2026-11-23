package by.it.group551001.evtushenko.lesson09;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ListA<E> implements List<E> {

    //Создайте аналог списка БЕЗ использования других классов СТАНДАРТНОЙ БИБЛИОТЕКИ

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
		
		@SuppressWarnings("unchecked")
		private E[] a = (E[]) new Object[4];
		private int n = 0, m = 4;
		private static final int MAXM = 1 << 30;

		@SuppressWarnings("unchecked")
		public void List(){
			a = (E[]) new Object[4]; m = 4; n = 0;
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
			E[] b = (E[])new Object[nm];
			for(int i = 0; i < n; ++i)b[i] = a[i];
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
			++n;
			boolean b = getnewCap();
			if(b == false){--n;return b;}
			recap(m);
			a[n - 1] = e;
			return true;
    }

    @Override
    public E remove(int index) {
			E res = a[index];
			for(int i = index + 1; i < n; ++i)a[i - 1] = a[i];
			--n;
			return res;
    }

    @Override
    public int size() {
        return n;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    //////               Опциональные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public void add(int index, E element) {
			++n;
			int om = m;
			@SuppressWarnings("unused")
			boolean __ = getnewCap();
			if(om != m)recap(m);
			for(int i = n - 1; i-- != index;)a[i + 1] = a[i];
			a[index] = element;
    }

    @Override
    public boolean remove(Object o) {
			for(int i = 0; i < n; ++i)if(a[i] == o){remove(i);return true;}
			return false;
    }

    @Override
    public E set(int index, E element) {
			E res = a[index];
			a[index] = element;
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

    @Override
    public int indexOf(Object o) {
			for(int i = 0; i < n; ++i)if(a[i].equals(o))return i;
			return -1;
    }

    @Override
    public E get(int index) {
        return a[index];
    }

    @Override
    public boolean contains(Object o) {
			for(int i = 0; i < n; ++i)if(a[i].equals(o))return true;
        return false;
    }

    @Override
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
				boolean b = this.add(ci);
				if(!b){
					n = on;
					return b;
				}
			}
			return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
			int cs = c.size();
			if(cs == 0)return false;
			n += cs;
			int om = m;
			@SuppressWarnings("unused")
			boolean __ = getnewCap();
			if(om != m)recap(m);
			for(int i = n; i - cs >= index; --i)a[i] = a[i - cs];
			int j = index;
			for(E ci: c){
				a[j++] = ci;
			}
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
			return n == on;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
			int nn = n;
			for(int i = 0, j = 0; i < n; ++i){
				if(!c.contains(a[i]))a[j++] = a[i];
				else --nn;
			}
			int on = n;
			n = nn;
			return n == on;
    }


    @Override
    public List<E> subList(int fromIndex, int toIndex) {
			return new SubList<>(this, fromIndex, toIndex);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ListAIterator<>(this, index);
    }

    @Override
    public ListIterator<E> listIterator() {
			return new ListAIterator<>(this);
    }

    @Override
		@SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
			
			T[] b = (T[])new Object[n];
			
			for(int i = 0; i < n; ++i){
				b[i] = (T)this.a[i];
			}

			return b;
    }

    @Override
    public Object[] toArray() {
			Object[] b = new Object[n];
			for(int i = 0; i < n; ++i)b[i] = this.a[i];

			return b;
    }

    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    ////////        Эти методы имплементировать необязательно    ////////////
    ////////        но они будут нужны для корректной отладки    ////////////
    /////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////
    @Override
    public Iterator<E> iterator() {
        return null;
    }

}
