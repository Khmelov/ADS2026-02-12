package by.it.group551001.evtushenko.lesson09;


import java.util.List;

public class SubList<E> extends ListA<E>{
	private final List<E> L;
	private int s;
	public SubList(List<E> L, int i, int j){
		this.L = L;
		this.s = i;
	}
	@Override
	public E get(int i){
		return L.get(s + i);
	}
	@Override public E set(int i, E e){
		return L.set(s + i, e);
	}
	@Override
	public int size(){
		return s;
	}
}