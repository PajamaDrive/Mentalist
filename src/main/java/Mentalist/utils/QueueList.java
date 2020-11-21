package Mentalist.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class QueueList<T>{
    private ArrayList<T> items;
    private int subSize;
    private double preWeight;

    public QueueList(int size, double weight){
        items = new ArrayList<>();
        subSize = size;
        preWeight = weight;
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

    public ArrayList<T> getPreQueue(){
        return items.size() <= subSize ? new ArrayList<T>() : new ArrayList<T>(items.subList(0, items.size() - subSize));
    }

    public ArrayList<T> getExceptZeroQueue(){
        return items.size() <= subSize ? new ArrayList<T>(items.subList(1, items.size())) : new ArrayList<T>(items.subList(items.size() - subSize, items.size()));
    }

    public ArrayList<T> getExceptZeroPreQueue(){
        return items.size() <= subSize ? new ArrayList<T>() : new ArrayList<T>(items.subList(1, items.size() - subSize));
    }

    public T getLastElement(){ return items.get(items.size() - 1); }

    public double sum(){
        int sum = 0;
        Iterator<T> iter = getQueue().iterator();
        while(iter.hasNext()){
            sum += items.get(0) instanceof Integer ? (Integer)iter.next() : (Double)iter.next();
        }
        int preSum = 0;
        iter = getPreQueue().iterator();
        while(iter.hasNext()){
            preSum += items.get(0) instanceof Integer ? (Integer)iter.next() : (Double)iter.next();
        }

        return preSum * preWeight + sum;
    }

    public double mean(){
        int sum = 0;
        Iterator<T> iter = getQueue().iterator();
        while(iter.hasNext()){
            sum += items.get(0) instanceof Integer ? (Integer)iter.next() : (Double)iter.next();
        }
        sum /= queueSize();
        int preSum = 0;
        iter = getPreQueue().iterator();
        while(iter.hasNext()){
            preSum += items.get(0) instanceof Integer ? (Integer)iter.next() : (Double)iter.next();
        }

        preSum /= (getFullSize() - queueSize());

        return preSum * preWeight + sum;
    }

    public double getPreWeight(){ return preWeight; }

    public int getFullSize(){ return items.size(); }

    public ArrayList<T> getAllArray() { return items; }
}
