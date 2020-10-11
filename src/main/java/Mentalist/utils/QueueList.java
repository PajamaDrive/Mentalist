package Mentalist.utils;

import java.util.ArrayList;

public class QueueList<T>{
    private ArrayList<T> items;
    private int subSize;

    public QueueList(int size){
        items = new ArrayList<>();
        subSize = size;
    }


    public boolean isEmpty(){ return items.isEmpty(); }

    public String toString(){ return items.toString(); }

    public void enQueue(T element){ items.add(element); }

    public void enQueueAll(ArrayList<T> elements){ items.addAll(elements); }

    public T deQueue(){ return items.isEmpty() ? null : items.remove(0); }

    public void clear(){ items.clear(); }

    public int getQueueSize(){ return items.size() <= subSize ? items.size() : this.subSize; }

    public ArrayList<T> getQueue(){
        return items.size() <= subSize ? items : new ArrayList<T>(items.subList(items.size() - subSize, items.size()));
    }

    public int getFullSize(){ return items.size(); }

    public ArrayList<T> getAllArray() { return items; }
}
