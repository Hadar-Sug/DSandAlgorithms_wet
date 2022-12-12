
public class ListNode<T>{
    private ListNode<T> next;
    private ListNode<T> prev;
    private KeyVal keyVal;
    private T metaData;


    // Constructor for the Node class
    public ListNode(T metaData) {
        this.next = null;
        this.prev = null;
        this.keyVal = null;
        this.metaData = metaData;
    }
    public ListNode(KeyVal keyVal){
        this.next = null;
        this.prev = null;
        this.keyVal = keyVal;
    }


    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> next) {
        this.next = next;
    }

    public ListNode<T> getPrev() {
        return prev;
    }

    public void setPrev(ListNode<T> prev) {
        this.prev = prev;
    }

    public KeyVal getKeyVal() {
        return keyVal;
    }

    public void setKeyVal(KeyVal keyVal) {
        this.keyVal = keyVal;
    }


}
