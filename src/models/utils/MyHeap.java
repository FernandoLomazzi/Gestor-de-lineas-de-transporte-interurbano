package models.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyHeap<E extends Comparable<? super E>>{
	List<E> heap;
	Comparator<E> comp;
	public MyHeap(){
		this.heap = new ArrayList<E>();
		this.comp = ((E a,E b) -> a.compareTo(b));
	}
	public MyHeap(Comparator<E> comp){
		this.heap = new ArrayList<E>();
		this.comp = comp;
	}
	//Inserts the specified element into this queue
	public void push(E e) throws NullPointerException{
		if(e==null)
			throw new NullPointerException("Can't add null values to Heap");
		this.heap.add(e);
		Integer currentNodeIndex = this.size()-1;
		Integer parentNodeIndex = this.getParentIndex(currentNodeIndex);
		while(currentNodeIndex>0 && comp.compare(this.getElement(currentNodeIndex),this.getElement(parentNodeIndex)) < 0) {
			swap(currentNodeIndex,parentNodeIndex);
			currentNodeIndex = parentNodeIndex;
			parentNodeIndex = this.getParentIndex(currentNodeIndex);
		}
	}
	//Retrieves and removes the head of this queue,
	//or returns null if this queue is empty.
	public E pop() {
		swap(0,this.size()-1);
		this.removeLastElement();
		down_heapify(0);
		return this.top();
	}
	//Makes heap consistent after removing top element
	private void down_heapify(Integer index) {
		Integer leftChild = leftChildIndex(index);
		Integer rightChild = rightChildIndex(index);
		Integer minChild;
		if(leftChild == null)
			return;
		else if(rightChild == null)
			minChild = leftChild;
		else
			minChild = comp.compare(this.getElement(leftChild),this.getElement(rightChild))<0? leftChild: rightChild;
		if(comp.compare(this.getElement(index),this.getElement(minChild))>0) {
			swap(index,minChild);
			down_heapify(minChild);
		}
	}
	//Retrieves, but does not remove, the head of this queue,
	//or returns null if this queue is empty.
	public E top() {
		return this.size()>0 ? this.getElement(0) : null;
	}
	//Returns the number of elements in this collection
	public int size() {
		return heap.size();
	}
	
	private E getElement(Integer index) throws IndexOutOfBoundsException {
		return this.heap.get(index);
	}
	private Integer getParentIndex(Integer index) {
		return index==0 ? null : (index-1)/2;
	}
	private Integer leftChildIndex(Integer index) {
		return 2*index+1 >= this.size() ? null : 2*index+1;
	}
	private Integer rightChildIndex(Integer index) {
		return 2*index+2 >= this.size() ? null : 2*index+2;
	}
	private void swap(Integer a,Integer b) {
		Collections.swap(heap,a,b);
	}
	private void removeLastElement() {
		this.heap.remove(this.size()-1);
	}
	public Boolean empty() {
		return this.size()==0 ? true : false;
	}
}
