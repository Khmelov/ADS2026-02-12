package by.it.group551001.evtushenko.lesson12;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;

public class MySplayMap implements NavigableMap<Integer, String>{
	public static class N{
		N l, r;
		Integer k;
		String v;
		N(Integer i, String s){
			k = i;
			v = s;
			l = r = null;
		}
		public String toString(){
			return k + "=" + v;
		}
	}

	private N RR(N x) {
		N y = x.r;
		x.r = y.l;
		y.l = x;
		return y;
	}
	
	private N LL(N x) {
		N y = x.l;
		x.l = y.r;
		y.r = x;
		return y;
	}

	private N splay(N r, int k){
		if(r == null || r.k == k)return r;
		if(r.k > k){
			if(r.l == null)return r;
			if(r.l.k > k){
				r.l.l = splay(r.l.l, k);
				r = LL(r);
			} else if(r.l.k < k){
				r.l.r  = splay(r.l.r, k);
				if(r.l.r != null)r.l = RR(r.l);
			}
			return r.l == null ? r : LL(r);
		} else {
			if(r.r == null)return r;
			if(r.r.k > k){
				r.r.l = splay(r.r.l, k);
				if(r.r.l != null)r.r = LL(r.r);
			} else if(r.r.k < k){
				r.r.r = splay(r.r.r, k);
				r = RR(r);
			}
			return r.r == null ? r : RR(r);
		}
	}

	private N splayInsert(N r, int k, String s, String[] pr) {
		if(r == null)return new N(k, s);
		r = splay(r, k);

		if(r.k == k){
			pr[0] = r.v;
			r.v = s;
			return r;
		}

		N n = new N(k, s);
		if(r.k > k){
			n.r = r;
			n.l = r.l;
			r.l = null;
		} else {
			n.l = r;
			n.r = r.r;
			r.r = null;
		}
		return n;
	}

	
	private N splayErase(N r, int k, String[] pr) {
		if(r == null)return null;
		r = splay(r, k);
		if(r.k != k)return r;
		pr[0] = r.v;
		if(r.l == null)r = r.r;
		else {
			N t = r;
			r = splay(r.l, k);
			r.r = t.r;
		}
		return r;
	}

	private N minimum(N r){
		if(r == null)return null;
		while(r.l != null)r = r.l;
		return r;
	}
	private N maximum(N r){
		if(r == null)return null;
		while(r.r != null)r = r.r;
		return r;
	}

	

	private void inor(N r, N[] a, int[] i){
		if(r == null)return;
		inor(r.l, a, i);
		a[i[0]++] = r;
		inor(r.r, a, i);
	}

	int n;
	public N r;

	MySplayMap(){
		n = 0;
		r = null;
	}

	public String toString(){
		if(this.isEmpty())return "{}";
		String S = new String();
		S += "{";
		N[] a = new N[n];
		int[] j = new int[1];
		inor(r, a, j);
		S += a[0].toString();
		for(int i = 1; i < n; ++i){
			S += ", ";
			S += a[i].toString();
		}
		S += "}";
		return S;
	}
	
	@Override
	public String put(Integer k, String v){
		String[] pr = new String[1];
		pr[0] = null;
		r = splayInsert(r, k, v, pr);
		if(pr[0] == null)++n;
		return pr[0];
	}

	@Override
	public String remove(Object k){
		String[] pr = new String[1];
		pr[0] = null;
		r = splayErase(r, (Integer)k, pr);
		if(pr[0] != null)--n;
		return pr[0];
	}

	@Override
	public String get(Object k){
		r = splay(r, (Integer)k);
		if(r == null || !k.equals(r.k))return null;
		return r.v;
	}

	@Override
	public boolean containsKey(Object k){
		return get(k) != null;
	}

	@Override
	public int size(){
		return n;
	}

	@Override
	public void clear(){
		r = null;
		n = 0;
	}

	@Override
	public boolean isEmpty(){
		return n == 0;
	}

	@Override
	public boolean containsValue(Object value) {
		N[] a = new N[n];
		int[] j = new int[1];
		inor(r, a, j);
		for(int i = 0; i < n; ++i)if(value.equals(a[i].v))return true;
		return false;
	}

	@Override
	public Integer firstKey() {
		if(r == null)return null;
		return minimum(r).k;
	}

	@Override
	public Integer lastKey() {
		if(r == null)return null;
		return maximum(r).k;
	}

	@Override
	public Integer lowerKey(Integer key) {
		N n = r;
		Integer res = null;
		while(n != null){
			if(key <= n.k)n = n.l;
			else {
				res = n.k;
				n = n.r;
			}
		}
		return res;
	}

	@Override
	public Integer higherKey(Integer key) {
		N n = r;
		Integer res = null;
		while(n != null){
			if(key >= n.k)n = n.r;
			else {
				res = n.k;
				n = n.l;
			}
		}
		return res;
	}

	@Override
	public Integer floorKey(Integer key) {
		N n = r;
		Integer res = null;
		while(n != null){
			if(key < n.k)n = n.l;
			else {
				res = n.k;
				n = n.r;
			}
		}
		return res;
	}

	@Override
	public Integer ceilingKey(Integer key) {
		N n = r;
		Integer res = null;
		while(n != null){
			if(key > n.k)n = n.r;
			else {
				res = n.k;
				n = n.l;
			}
		}
		return res;
	}

	

@Override
public void putAll(Map<? extends Integer, ? extends String> m) {
}

@Override
public Set<Integer> keySet() {
    return java.util.Collections.emptySet();
}

@Override
public Collection<String> values() {
    return java.util.Collections.emptyList();
}

@Override
public Set<Map.Entry<Integer, String>> entrySet() {
    return java.util.Collections.emptySet();
}

	@Override
	public Comparator<? super Integer> comparator() {
		return null;
	}

	private int compare(Integer k1, Integer k2) {
		return k1.compareTo(k2);
	}






	@Override
	public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
		if (compare(fromKey, toKey) > 0) {
				throw new IllegalArgumentException("fromKey > toKey");
		}
		return new SubMapWrapper(fromKey, toKey);
	}

	@Override
	public SortedMap<Integer, String> headMap(Integer toKey) {
		return new SubMapWrapper(0, toKey);
	}

	@Override
	public SortedMap<Integer, String> tailMap(Integer fromKey) {
		return new SubMapWrapper(fromKey, maximum(r).k + 1);
	}











	private class SubMapWrapper implements SortedMap<Integer, String>{
		final int from, to;
		public SubMapWrapper(int from, int to){
			this.to = to;
			this.from = from;
		}
		private boolean inRange(Object key) {
			int k = (Integer) key;
			return k >= from && k < to;
		}

		@Override
		public Comparator<? super Integer> comparator() {
			return MySplayMap.this.comparator();
		}

		@Override
		public Integer firstKey() {
			N rr = r;
			if(rr == null)return null;
			Integer res = null;
			while(rr != null){
				if(rr.k.equals(from))return from;
				if(rr.k >= from){
					res = rr.k;
					rr = rr.l;
				} else rr = rr.r;
			}
			if(res == null || res >= to)return null;
			return res;
		}

		@Override
		public Integer lastKey() {
			N rr = r;
			if(rr == null)return null;
			Integer res = null;
			while(rr != null){
				if(rr.k.equals(to))return to;
				if(rr.k < to){
					res = rr.k;
					rr = rr.l;
				} else rr = rr.r;
			}
			if(res == null || res < from)return null;
			return res;
		}

		@Override
		public String put(Integer key, String value) {
			if (!inRange(key)) {
				throw new IllegalArgumentException();
			}
			return MySplayMap.this.put(key, value);
		}

		@Override
		public String get(Object key) {
			if (!inRange(key)) {
				throw new IllegalArgumentException();
			}
			return MySplayMap.this.get(key);
		}

		@Override
		public boolean containsKey(Object key) {
			return inRange(key) && MySplayMap.this.containsKey(key);
		}

		@Override
		public String remove(Object key) {
			if (!inRange(key)) return null;
			return MySplayMap.this.remove(key);
		}

		@Override
		public int size(){
			N[] a = new N[n];
			int[] j = new int[1];
			inor(r, a, j);
			int res = 0;
			for(int i = 0; i < n; ++i)if(a[i].k >= from && a[i].k < to)++res;
			return res;
		}

		@Override
		public Set<Entry<Integer, String>> entrySet() {
			return null;
		}

		@Override
		public SortedMap<Integer, String> subMap(Integer from, Integer to) {
			if (!inRange(from) || to > this.to){
				throw new IllegalArgumentException();
			}
			return new SubMapWrapper(from, to);
		}

		@Override
		public String toString() {
			if(this.isEmpty())return "{}";
			String S = new String();
			S += "{";
			N[] a = new N[n];
			int[] j = new int[1];
			inor(r, a, j);
			int i = 0;
			while(a[i].k < from)++i;
			S += a[i].toString();
			for(++i; i < n; ++i){
				if(a[i].k >= to)break;
				S += ", ";
				S += a[i].toString();
			}
			S += "}";
			return S;
		}

		@Override public void clear() { 
			N[] a = new N[n];
			int[] j = new int[1];
			inor(r, a, j);
			for(int i = 0; i < n; ++i)if(a[i].k >= from && a[i].k < to)splayErase(r, a[i].k, new String[1]);	
		}
		@Override public boolean isEmpty() { return firstKey() == null; }
		@Override public boolean containsValue(Object v) { return values().contains(v); }
		@Override public void putAll(Map<? extends Integer, ? extends String> m) { for(var e : m.entrySet()) put(e.getKey(), e.getValue()); }
		@Override public SortedMap<Integer, String> headMap(Integer toKey) { return subMap(this.from, toKey); }
		@Override public SortedMap<Integer, String> tailMap(Integer fromKey) { return subMap(fromKey, this.to); }
		@Override public Set<Integer> keySet() { return new AbstractSet<>() { @Override public Iterator<Integer> iterator() { return null; } @Override public int size() { return SubMapWrapper.this.size(); } }; }
		@Override public Collection<String> values() { return new AbstractCollection<>() { @Override public Iterator<String> iterator() { return null; } @Override public int size() { return SubMapWrapper.this.size(); } }; }
	}

	@Override
	public Entry<Integer, String> lowerEntry(Integer key) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'lowerEntry'");
	}

	@Override
	public Entry<Integer, String> floorEntry(Integer key) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'floorEntry'");
	}

	@Override
	public Entry<Integer, String> ceilingEntry(Integer key) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'ceilingEntry'");
	}

	@Override
	public Entry<Integer, String> higherEntry(Integer key) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'higherEntry'");
	}

	@Override
	public Entry<Integer, String> firstEntry() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'firstEntry'");
	}

	@Override
	public Entry<Integer, String> lastEntry() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'lastEntry'");
	}

	@Override
	public Entry<Integer, String> pollFirstEntry() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'pollFirstEntry'");
	}

	@Override
	public Entry<Integer, String> pollLastEntry() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'pollLastEntry'");
	}

	@Override
	public NavigableMap<Integer, String> descendingMap() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'descendingMap'");
	}

	@Override
	public NavigableSet<Integer> navigableKeySet() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'navigableKeySet'");
	}

	@Override
	public NavigableSet<Integer> descendingKeySet() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'descendingKeySet'");
	}

	@Override
	public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey,
			boolean toInclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'subMap'");
	}

	@Override
	public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'headMap'");
	}

	@Override
	public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'tailMap'");
	}

}
