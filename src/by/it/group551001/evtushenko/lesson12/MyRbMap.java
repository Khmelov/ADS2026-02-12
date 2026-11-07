package by.it.group551001.evtushenko.lesson12;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

public class MyRbMap implements SortedMap<Integer, String>{
	public static class N{
		N l, r, p;
		Integer k;
		boolean red;
		String v;
		N(Integer i, String s){
			k = i;
			red = false;
			v = s;
			l = r = p = nil;
		}
		public String toString(){
			return k + "=" + v;
		}
	}

	public static  final N nil = new N(0, "");
	static{
		nil.l = nil.r = nil.p = nil;
	}

	private N rbRR(N x) {
		N y = x.r;
		x.r = y.l;
		if (y.l != nil) y.l.p = x;
		y.p = x.p;
		if (x.p != nil) {
			if (x == x.p.l) x.p.l = y; else x.p.r = y;
		} else {
			this.r = y;
		}
		y.l = x; x.p = y;
		return y;
	}
	
	private N rbLL(N y) {
		N x = y.l;
		y.l = x.r;
		if (x.r != nil) x.r.p = y;
		x.p = y.p;
		if (y.p != nil) {
			if (y == y.p.l) y.p.l = x; else y.p.r = x;
		} else {
			this.r = x;
		}
		x.r = y; y.p = x;
		return x;
	}

	private void fixInsert(N z) {
		while (z.p != nil && z.p.red) {
			if (z.p == z.p.p.l) {
				N y = z.p.p.r;
				if (y != nil && y.red) {
					z.p.red = y.red = false;
					z.p.p.red = true;
					z = z.p.p;
				} else {
					if (z == z.p.r) {
						z = z.p;
						rbRR(z);
					}
					z.p.red = false;
					z.p.p.red = true;
					rbLL(z.p.p);
				}
			} else {
				N y = z.p.p.l;
				if (y != nil && y.red) {
					z.p.red = y.red = false;
					z.p.p.red = true;
					z = z.p.p;
				} else {
					if (z == z.p.l) {
						z = z.p;
						rbLL(z);
					}
					z.p.red = false;
					z.p.p.red = true;
					rbRR(z.p.p);
				}
			}
		}
		this.r.red = false;
	}
	
	private N rbInsert(N root, int n, String s, String[] pr) {
		N nn = new N(n, s);
		nn.red = true;
		N p = nil;
		N r = root;
		while (r != nil) {
			p = r;
			if (n < r.k) r = r.l;
			else if (n > r.k) r = r.r;
			else {
				pr[0] = r.v;
				r.v = s;
				return this.r;
			}
		}
		nn.p = p;
		if (p == nil) this.r = nn;
		else if (nn.k < p.k) p.l = nn; else p.r = nn;
		fixInsert(nn);
		return this.r;
	}

	private void fixDelete(N x) {
		while(x != r && !x.red) {
			if(x == x.p.l) {
				N w = x.p.r;
				if (w.red) {
					w.red = false;
					x.p.red = true;
					rbRR(x.p);
					w = x.p.r;
				}
				if(!w.r.red && !w.l.red) {
					w.red = true;
					x = x.p;
				} else {
					if(!w.r.red) {
						w.l.red = false;
						w.red = true;
						rbLL(w);
						w = x.p.r;
					}
					w.red = x.p.red;
					x.p.red = false;
					w.r.red = false;
					rbRR(x.p);
					x = r;
				}
			} else {
				N w = x.p.l;
				if(w.red){
					w.red = false;
					x.p.red = true;
					rbLL(x.p);
					w = x.p.l;
				}
				if(!w.r.red && !w.l.red){
					w.red = true;
					x = x.p;
				} else {
					if(!w.l.red) {
						w.r.red = false;
						w.red = true;
						rbRR(w);
						w = x.p.l;
					}
					w.red = x.p.red;
					x.p.red = false;
					w.l.red = false;
					rbLL(x.p);
					x = r;
				}
			}
		}
		x.red = false;
	}
	
	private N rbErase(N root, int n, String[] pr) {
		N z = rbFind(root, n);
		if (z == nil) return this.r;
		pr[0] = z.v;
		N y = z;
		boolean yred = y.red;
		N x;
		if (z.l == nil) {
			x = z.r;
			transplant(z, z.r);
		} else if (z.r == nil) {
			x = z.l;
			transplant(z, z.l);
		} else {
			y = minimum(z.r);
			yred = y.red;
			x = y.r;
			if (y.p == z) {
				if (x != nil) x.p = y;
			} else {
				transplant(y, y.r);
				y.r = z.r;
				if(y.r != nil)y.r.p = y;
			}
			transplant(z, y);
			y.l = z.l;
			y.l.p = y;
			y.red = z.red;
		}
		if (!yred) fixDelete(x);
		return this.r;
	}

	private N rbFind(N root, int n){
		if(root == nil){
			return nil;
		}
	
		if(n == root.k){
			return root;
		}
	
		if(n < root.k)
			return rbFind(root.l, n);
	
		return rbFind(root.r, n);
	}

	private N minimum(N r){
		if(r == nil)return nil;
		while(r.l != nil)r = r.l;
		return r;
	}
	private N maximum(N r){
		if(r == nil)return nil;
		while(r.r != nil)r = r.r;
		return r;
	}

	private void transplant(N u, N v){
		if(u.p == nil)this.r = v;
		else if(u == u.p.l)u.p.l = v;
		else u.p.r = v;
		if(v != nil)v.p = u.p;
	}

	private void inor(N r, N[] a, int[] i){
		if(r == nil)return;
		inor(r.l, a, i);
		a[i[0]++] = r;
		inor(r.r, a, i);
	}

	int n;
	public N r;

	MyRbMap(){
		n = 0;
		r = nil;
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
		r = rbInsert(r, k, v, pr);
		if(pr[0] == null)++n;
		return pr[0];
	}

	@Override
	public String remove(Object k){
		String[] pr = new String[1];
		pr[0] = null;
		r = rbErase(r, (Integer)k, pr);
		if(pr[0] != null)--n;
		return pr[0];
	}

	@Override
	public String get(Object k){
		N f = rbFind(r, (Integer)k);
		if(f == nil)return null;
		return f.v;
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
		r = nil;
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
		if(r == nil)return null;
		return maximum(r).k;
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
			return MyRbMap.this.comparator();
		}

		@Override
		public Integer firstKey() {
			N rr = r;
			if(rr == nil)return null;
			Integer res = null;
			while(rr != nil){
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
			if(rr == nil)return null;
			Integer res = null;
			while(rr != nil){
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
			return MyRbMap.this.put(key, value);
		}

		@Override
		public String get(Object key) {
			if (!inRange(key)) {
				throw new IllegalArgumentException();
			}
			return MyRbMap.this.get(key);
		}

		@Override
		public boolean containsKey(Object key) {
			return inRange(key) && MyRbMap.this.containsKey(key);
		}

		@Override
		public String remove(Object key) {
			if (!inRange(key)) return null;
			return MyRbMap.this.remove(key);
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
			for(int i = 0; i < n; ++i)if(a[i].k >= from && a[i].k < to)rbErase(r, a[i].k, new String[1]);	
		}
		@Override public boolean isEmpty() { return firstKey() == null; }
		@Override public boolean containsValue(Object v) { return values().contains(v); }
		@Override public void putAll(Map<? extends Integer, ? extends String> m) { for(var e : m.entrySet()) put(e.getKey(), e.getValue()); }
		@Override public SortedMap<Integer, String> headMap(Integer toKey) { return subMap(this.from, toKey); }
		@Override public SortedMap<Integer, String> tailMap(Integer fromKey) { return subMap(fromKey, this.to); }
		@Override public Set<Integer> keySet() { return new AbstractSet<>() { @Override public Iterator<Integer> iterator() { return null; } @Override public int size() { return SubMapWrapper.this.size(); } }; }
		@Override public Collection<String> values() { return new AbstractCollection<>() { @Override public Iterator<String> iterator() { return null; } @Override public int size() { return SubMapWrapper.this.size(); } }; }
	}

}
