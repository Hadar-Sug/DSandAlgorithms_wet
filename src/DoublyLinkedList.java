public class DoublyLinkedList<T> {
    private ListNode head;
    private ListNode tail;

    // Constructor for the DoublyLinkedList class
    public DoublyLinkedList() {
        this.head = null;
        this.tail = null;
    }

    protected void removeNode(ListNode removeMe) {
        if (removeMe == head) {
            // Handle case where the node to remove is the head
            head = head.getNext();
            head.setPrev(null);
        } else if (removeMe == tail) {
            // Handle case where the node to remove is the tail
            tail = tail.getPrev();
            tail.setNext(null);
        } else {
            // Handle case where the node to remove is in the middle of the list
            ListNode prevNode = removeMe.getPrev();
            ListNode nextNode = removeMe.getNext();
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
    }

    public void addNode(ListNode insertMe, ListNode afterMe) {
        if (head == null) {
            // Handle case where the list is empty or the 'afterMe' node is null
            addNode(insertMe);
        }// Handle the case where afterMe is null and the list isn't empty
        else if(afterMe == null){
            insertMe.setNext(head);
            head.setPrev(insertMe);
            setHead(insertMe);
        }
        else {
            // Handle case where the list is not empty and the 'afterMe' node is not null
            ListNode nextNode = afterMe.getNext();
            afterMe.setNext(insertMe);
            insertMe.setPrev(afterMe);
            insertMe.setNext(nextNode);
            nextNode.setPrev(insertMe);
        }
    }

    protected void addNode(ListNode addMe) {
        if (head == null) {
            // Handle case where the list is empty
            setHead(addMe);
            setTail(addMe);
        } else {
            // Handle case where the list is not empty
            tail.setNext(addMe);
            addMe.setPrev(tail);
            setTail(addMe);
        }
    }

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    public void setTail(ListNode tail) {
        this.tail = tail;
    }
}


