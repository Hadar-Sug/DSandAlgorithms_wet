
public class Leaf<T> extends TreeNode{

    ListNode rankTwin;
    Leaf<T> twin; // will point to the twin in either the goal/points matching tree
    T metaData; // the type, and actual object of the node (player/faculty/tree...)
    String name = null;


    //only for sentinels and twins
    public Leaf() {
        super();
        size = 1;
    }

    public Leaf(T metaData){
        this();
        this.twin = new Leaf<T>(); // maybe redundant
//        this.twin.setMetaData(metaData);
        this.metaData = metaData;
    }

    public Leaf(T metaData, int key, int val) {
        this(metaData);
        this.keyVal = new KeyVal(key,val);
        this.rankTwin = new ListNode(key,val);
    }

    public Leaf(T metaData, int key, int val, String name){
        this(metaData,key,val);
        this.rankTwin.setName(name);
        this.name = name;
    }

//    public Leaf(Leaf<T> other) {
//        // make a deep copy of the rankTwin field
//        this.rankTwin = new ListNode(other.rankTwin);
//        // make a deep copy of the twin field
//        this.twin = new Leaf<T>(other.twin);
//        // make a deep copy of the metaData field
//        this.metaData = deepCopy(other.metaData);
//        // make a deep copy of the name field
//        this.name = new String(other.name);
//
//        // make a deep copy of the parent field
//        this.parent = new TreeNode(other.parent);
//        // make a deep copy of the children array
//        this.children = deepCopy(other.children);
//        // make a deep copy of the size field
//        this.size = new Integer(other.size);
//        // make a deep copy of the keyVal field
//        this.keyVal = deepCopy(other.keyVal);
//    }



    public Leaf<T> copyLeaf() {
        Leaf<T> newLeaf = new Leaf<>();
        newLeaf.rankTwin = this.rankTwin;
        newLeaf.twin = this.twin;
        newLeaf.metaData = this.metaData;
        newLeaf.name = this.name;
        newLeaf.keyVal = this.keyVal;
        newLeaf.size = this.size;
        return newLeaf;
    }

    public Leaf(TreeNode node){
        super(node.getChildren(), 1, node.getKeyVal());
        getRankTwin().setKeyVal(node.getKeyVal()); //set the Keyval to be the same for the twin node in the list
    }

    public ListNode getRankTwin() {
        return rankTwin;
    }

    public void setRankTwin(ListNode rankTwin) {
        this.rankTwin = rankTwin;
    }

    public void setKeyVal(KeyVal keyVal) {
        this.keyVal = keyVal;
        this.rankTwin.setKeyVal(keyVal);
    }
    public Leaf<T> getTwin() {
        return twin;
    }

    public void setTwin(Leaf<T> twin) {
        this.twin = twin;
    }

    public T getMetaData() {
        return metaData;
    }

    public void setMetaData(T metaData) {
        this.metaData = metaData;
    }
}
