public class Leaf extends TreeNode{

    ListNode twin;

    public Leaf() {
        super();
        size = 1;
        twin = new ListNode();
    }

    public Leaf(TreeNode node){
        super(node.getChildren(), 1, node.getKeyVal());
        getTwin().setKeyVal(node.getKeyVal()); //set the Keyval to be the same for the twin node in the list
    }

    public ListNode getTwin() {
        return twin;
    }

    public void setTwin(ListNode twin) {
        this.twin = twin;
    }

    public void setKeyVal(KeyVal keyVal) {
        this.keyVal = keyVal;
        this.twin.setKeyVal(keyVal);
    }

}
