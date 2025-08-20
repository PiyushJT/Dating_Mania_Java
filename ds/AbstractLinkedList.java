package ds;


public abstract class AbstractLinkedList {

    // Abstract methods that must be implemented by subclasses

    public abstract int length();
    public abstract boolean isEmpty();
    public abstract void clear();
    public abstract Object get(int index);
    public abstract Object[] toArray();


}