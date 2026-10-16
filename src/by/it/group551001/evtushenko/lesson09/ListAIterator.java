package by.it.group551001.evtushenko.lesson09;


import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListAIterator<E> implements ListIterator<E>{
	private final List<E> L;


	int i = 0;

	public ListAIterator(List<E> L){this.L = L;}
	public ListAIterator(List<E> L, int i){this.L = L; this.i = i;}

	@Override
	public boolean hasNext(){
		return i < L.size();
	}

	@Override
	public E next(){
		if(!hasNext())throw new NoSuchElementException();
		return L.get(i++);
	}

	@Override
	public boolean hasPrevious(){
		return i != 0;
	}

	@Override
	public E previous(){
		if(!hasPrevious())throw new NoSuchElementException();
		return L.get(i--);
	}

	@Override
	public void set(E e){
		L.set(i, e);
	}

	@Override
	public void remove(){
		L.remove(i);
	}

	@Override
	public int nextIndex(){
		return i + 1;
	}

	@Override
	public int previousIndex(){
		return i - 1;
	}

	@Override
	public void add(E e){
		L.add(i, e);
	}
}