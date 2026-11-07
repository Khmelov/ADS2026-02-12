package by.it.group551001.evtushenko.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String>{
	public static class N{
		N l, r;
		Integer k, h;
		String v;
		N(Integer i, String s){
			k = i;
			h = 1;
			v = s;
			l = r = null;
		}
		public String toString(){
			return k + "=" + v;
		}
	}

	private int gh(N r){
		return r == null ? 0 : r.h;
	}

	private int gbf(N r){
		return r == null ? 0 : gh(r.l) - gh(r.r);
	}

	private N avlRR(N r){
		N l = r.l, lr = l.r;

		l.r = r;
		r.l = lr;

		int rh = gh(r.r), lh = gh(r.l);
		r.h = 1 + (rh > lh ? rh : lh);

		int lrh = gh(l.r), llh = gh(l.l);
		l.h = 1 + (lrh > llh ? lrh : llh);

		return l;
	}

	private N avlLL(N root){
		N r = root.r, rl = r.l;

		r.l = root;
		root.r = rl;

		int rootrHeight = gh(root.r), rootlHeight = gh(root.l);
		root.h = 1 + (rootrHeight > rootlHeight ? rootrHeight : rootlHeight);

		int rrHeight = gh(r.r), rlHeight = gh(r.l);
		r.h = 1 + (rrHeight > rlHeight ? rrHeight : rlHeight);

		return r;
	}

	private N avlLR(N root){
		root.l = avlLL(root.l);
		return avlRR(root);
	}
	
	private N avlRL(N root){
		root.r = avlRR(root.r);
		return avlLL(root);
	}

	private N avlRebalance(N root){
		int bf = gbf(root);
		if(bf > 1){
			int lbf = gbf(root.l);
			if(lbf >= 0) return avlRR(root);
			else return avlLR(root);
		}
	
		if(bf < -1){
			int rbf = gbf(root.r);
			if(rbf <= 0) return avlLL(root);
			else return avlRL(root);
		}
	
		return root;
	}

	private N avlInsert(N root, int n, String s, String[] pr){
		if(root == null){
			N newNode = new N(n, s);
			return newNode;
		}
	
		if(n < root.k) {
			root.l = avlInsert(root.l, n, s, pr);
		} else if(n > root.k) {
			root.r = avlInsert(root.r, n, s, pr);
		} else {
			pr[0] = root.v;
			root.v = s;
			return root;
		}
	
		int lheight = gh(root.l), rheight = gh(root.r);
		root.h = 1 + (lheight > rheight ? lheight : rheight);
	
		return avlRebalance(root);
	}

	private N avlErase(N root, int n, String[] pr){
		if(root == null){
			return root;
		}
	
		if(n < root.k){
			root.l = avlErase(root.l, n, pr);
		} else if(n > root.k){
			root.r = avlErase(root.r, n, pr);
		} else {
			pr[0] = root.v;
			if(root.l == null){
				return root.r;
			}
	
			if(root.r == null){
				return root.l;
			}
	
			N temp = root.r;
			while(temp.l != null)
				temp = temp.l;
	
			root.k = temp.k;
			root.v = temp.v;
			root.r = avlErase(root.r, temp.k, pr);
		}
	
		int lheight = gh(root.l), rheight = gh(root.r);
		root.h = 1 + (lheight > rheight ? lheight : rheight);
	
		return avlRebalance(root);
	}

	private N avlFind(N root, int n){
		if(root == null){
			return null;
		}
	
		if(n == root.k){
			return root;
		}
	
		if(n < root.k)
			return avlFind(root.l, n);
	
		return avlFind(root.r, n);
	}

	private void inor(N r, N[] a, int[] i){
		if(r == null)return;
		inor(r.l, a, i);
		a[i[0]++] = r;
		inor(r.r, a, i);
	}

	int n;
	N r;

	MyAvlMap(){
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
		r = avlInsert(r, k, v, pr);
		if(pr[0] == null)++n;
		return pr[0];
	}

	@Override
	public String remove(Object k){
		String[] pr = new String[1];
		pr[0] = null;
		r = avlErase(r, (Integer)k, pr);
		if(pr[0] != null)--n;
		return pr[0];
	}

	@Override
	public String get(Object k){
		N f = avlFind(r, (Integer)k);
		if(f == null)return null;
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
		r = null;
		n = 0;
	}

	@Override
	public boolean isEmpty(){
		return n == 0;
	}

	@Override
public boolean containsValue(Object value) {
    return false;
}

@Override
public void putAll(Map<? extends Integer, ? extends String> m) {
    // Intentional dummy implementation doing nothing
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

}
