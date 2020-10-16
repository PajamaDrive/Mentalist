package Mentalist.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class QueueList<T>{
    private ArrayList<T> items;
    private int subSize;

    public QueueList(int size){
        items = new ArrayList<>();
        subSize = size;
    }


    public boolean isEmpty(){ return items.isEmpty(); }

    public String toString(){ return getQueue().toString(); }

    public String toStringAll(){ return items.toString(); }

    public void enQueue(T element){ items.add(element); }

    public void enQueueAll(ArrayList<T> elements){ items.addAll(elements); }

    public T deQueue(){ return items.isEmpty() ? null : items.remove(0); }

    public void clear(){ items.clear(); }

    public T getElement(int index){ return items.get(index); }

    public int queueSize(){ return items.size() <= subSize ? items.size() : this.subSize; }

    public ArrayList<T> getQueue(){
        return items.size() <= subSize ? items : new ArrayList<T>(items.subList(items.size() - subSize, items.size()));
    }

    public ArrayList<T> getExceptZeroQueue(){
        return (items.size() - 1) <= subSize ? new ArrayList<T>(items.subList(1, items.size() + 1)) : new ArrayList<T>(items.subList(items.size() - subSize, items.size()));
    }

    public T getLastElement(){ return items.get(items.size() - 1); }

    public int sum(){
        int sum = 0;
        Iterator<T> iter = getQueue().iterator();
        while(iter.hasNext()){
            sum += (Integer)iter.next();
        }
        return sum;
    }

    public int getFullSize(){ return items.size(); }

    public ArrayList<T> getAllArray() { return items; }
}
