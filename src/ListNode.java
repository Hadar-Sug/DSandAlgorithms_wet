
public class ListNode{
    private ListNode next;
    private ListNode prev;
    private KeyVal keyVal;

    // Constructor for the Node class
    public ListNode() {
        this.next = null;
        this.prev = null;
        this.keyVal = null;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public ListNode getPrev() {
        return prev;
    }

    public void setPrev(ListNode prev) {
        this.prev = prev;
    }

    public KeyVal getKeyVal() {
        return keyVal;
    }

    public void setKeyVal(KeyVal keyVal) {
        this.keyVal = keyVal;
    }

    public ListNode(KeyVal keyVal){
        this.next = null;
        this.prev = null;
        this.keyVal = keyVal;
    }
}
