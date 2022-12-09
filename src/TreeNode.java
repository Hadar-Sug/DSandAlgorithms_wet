public class TreeNode {
    //---------------------members
    TreeNode[] children;
    TreeNode parent;
    int degree;
    protected KeyVal keyVal; //key =main data, value = based on order of node input
    public static int L = 0;
    public static int M = 1;
    public static int R = 2;
    //-------------------------

    //default constructor
    public TreeNode() {
        children = new TreeNode[3];
        for (int i = 0; i <2;i++)
            children[i] = null;
        parent = null;
        degree = 0;
    }
    public TreeNode[] getChildren() {
        return this.children;
    }
    public KeyVal getKeyVal() {
        return keyVal;
    }

    public void setVal(int val){
        this.keyVal.setVal(val);
    }

    public void setKey(int key){
        this.keyVal.setKey(key);
    }

    public int getKey(){
        return this.keyVal.getKey();
    }

    public int getVal(){
        return this.keyVal.getVal();
    }

    public void setKeyVal(KeyVal keyVal) {
        this.keyVal = keyVal;
    }

    TreeNode getChild(int i){
        if (i < 0 || i >= children.length){
            throw new IndexOutOfBoundsException();
        }
        return this.children[i];
    }
    void setChild(TreeNode child, int i){
        if (i < 0 || i >= children.length){
            throw new IndexOutOfBoundsException();
        }
        this.children[i] = child;
    }
    public void setChildren(TreeNode[] children) {
        this.children = children;
    }

    public TreeNode getParent() {
        return this.parent;
    }


    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public void setKeyVal(int key, int val) {
        this.keyVal.setKey(key);
        this.keyVal.setVal(val);
    }
}


