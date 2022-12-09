

public class TwoThreeTree<node extends TreeNode> {
    node root;
    Leaf sentinel_l;
    Leaf sentinel_r;
    static int nodeCount = 0;
    public static int L = 0;
    public static int M = 1;
    public static int R = 2;

    public node getRoot() {
        return root;
    }

    public void setRoot(node root) {
        this.root = root;
    }

    public TwoThreeTree(node root) {
        root.setVal(nodeCount++);
        this.root = root;
        this.sentinel_r = new Leaf();
        this.sentinel_l = new Leaf();
        this.sentinel_r.setKeyVal(Constants.MAX_VALUE,nodeCount);
        this.sentinel_l.setKeyVal(Constants.MIN_VALUE,nodeCount);
        sentinel_l.setParent(this.root);
        sentinel_r.setParent(this.root);
        this.root.setKey(sentinel_r.getKey());
    }

    public TreeNode Search(TreeNode x, KeyVal key) {
        if (x instanceof Leaf){
            if (x.getKeyVal() == key) {
                return x;
            }
            else return null;
        }
        if (key.compareTo(x.getChild(L).getKeyVal()) <=0){
            return Search( x.getChild(L),key);
        }
        else if (key.compareTo(x.getChild(M).getKeyVal())<=0){
            return Search(x.getChild(M),key);
        }
        else
            return Search(x.getChild(R),key);
    }

    private void updateKey(TreeNode x) {
        x.keyVal = x.children[L].getKeyVal();
        if (x.children[M] != null)
            x.keyVal = x.children[M].getKeyVal();
        if (x.children[R] != null)
            x.keyVal = x.children[R].getKeyVal();
    }

    private void setchildren(TreeNode root, TreeNode left, TreeNode middle, TreeNode right) {
        TreeNode[] children = new TreeNode[]{left,middle,right};
        root.setChildren(children);
        for (TreeNode child : children) {
            if (child!=null)
                child.setParent(root);
        }
        updateKey(root);
//        for (int i = 0; i <2; i++) {
//            root.setChild(left,i);
//        }
//        if (middle != null)
//            middle.setParent(root);
//        if (right != null)
//            right.setParent(root);
    }

    private TreeNode insertAndSplit(TreeNode root,TreeNode insertMe){
        if (root.getChild(R) == null){//there's space, so lets find the right spot for insertMe
            if (insertMe.getKeyVal().compareTo(root.getChild(L).getKeyVal()) <0 ) // insertme,L,M
                setchildren(root,insertMe, root.getChild(L), root.getChild(M));
            else if (insertMe.getKeyVal().compareTo(root.getChild(M).getKeyVal()) <0)// L,insertme,M
                setchildren(root,root.getChild(L),insertMe, root.getChild(M));
            else  setchildren(root,root.getChild(L),root.getChild(M),insertMe);// L,M,insertme
            return null;
        }
        else { // no space so we gotta split as such:
            TreeNode newRoot = new TreeNode();
            if (insertMe.getKeyVal().compareTo(root.getChild(L).getKeyVal()) < 0) { // insertme,L,M,R
                setchildren(root, insertMe, root.getChild(L), null);
                setchildren(newRoot, root.getChild(M), root.getChild(R), null);
            } else if (insertMe.getKeyVal().compareTo(root.getChild(M).getKeyVal()) < 0) { // L,insertme,M,R
                setchildren(root, root.getChild(L), insertMe, null);
                setchildren(newRoot, root.getChild(M), root.getChild(R), null);
            } else if (insertMe.getKeyVal().compareTo(root.getChild(R).getKeyVal()) < 0) {// L,M,insertme,R
                setchildren(root, root.getChild(L), root.getChild(M), null);
                setchildren(newRoot, insertMe, root.getChild(R), null);
            } else {
                setchildren(root, root.getChild(L), root.getChild(M), null); //L,M,R,insertme
                setchildren(newRoot, root.getChild(R), insertMe, null);
            }
            return newRoot;
        }
    }

    public void insert(TwoThreeTree<node> T, TreeNode insertMe){
        TreeNode y = T.getRoot();
        while(!(y instanceof  Leaf)){ //risky converions with leaf and such
            if (insertMe.getKeyVal().compareTo(y.getChild(L).getKeyVal())<0)
                y = y.getChild(L);
            else if (insertMe.getKeyVal().compareTo(y.getChild(M).getKeyVal())<0)
                y = y.getChild(M);
            else y=y.getChild(R);
        }
        TreeNode x = y.getParent();
        insertMe = insertAndSplit(x,insertMe);
        while (x.getKeyVal().compareTo(T.getRoot().keyVal)!=0){
            x = x.getParent();
            if (insertMe != null)
                insertMe = insertAndSplit(x,insertMe);
            else updateKey(x);
        }
        if (insertMe!=null){
            TreeNode newRoot = new TreeNode();
            setchildren(newRoot,x,insertMe,null);
            T.setRoot((node)newRoot); // ugh bad
        }
    }

}
