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
        this.keyVal.setKey(key);
        this.keyVal.setVal(val);
        this.rankTwin = new ListNode(key,val);
    }

    public Leaf(T metaData, int key, int val, String name){
        this(metaData,key,val);
        this.rankTwin.setName(name);
        this.name = name;
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
