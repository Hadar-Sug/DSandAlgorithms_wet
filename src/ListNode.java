
public class ListNode{
    private ListNode next;
    private ListNode prev;
    private KeyVal keyVal;
    private String name;


    // Constructor for the Node class

    public ListNode(int key, int val){
        this.next = null;
        this.prev = null;
        this.keyVal = new KeyVal(key,val);
    }

    public ListNode(int key, int val, String name){
        this(key,val);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
