public class TwoThreeTree<T> {
    //----------members
    DoublyLinkedList<T> rankings = new DoublyLinkedList<T>();
    TreeNode root;
    Leaf<T> sentinel_l;
    Leaf<T> sentinel_r;
    public static int L = 0;
    public static int M = 1;
    public static int R = 2;

    public static int MAX_VALUE = 2147483647;
    public static int MIN_VALUE = -2147483648;
    //--------------

    //-------------methods
    /**
     * default constructor
     */
    public TwoThreeTree() {
        // initialize sentinels
        this.sentinel_r = new Leaf<T>(null,MAX_VALUE,0);
        this.sentinel_l = new Leaf<T>(null,MIN_VALUE,0);
        this.sentinel_r.setSize(0);// leaf size is default 1
        this.sentinel_l.setSize(0);
        this.root = new TreeNode();
        sentinel_l.setParent(this.root);
        sentinel_r.setParent(this.root);
        TreeNode[] sentinels = new TreeNode[]{sentinel_l,sentinel_r,null};
        // initialize root
        this.root.setChildren(sentinels);
        this.root.setKeyVal(sentinel_r.getKeyVal());
//        //initialize the rankings list
//        this.rankings.setHead(sentinel_l.getTwin());
//        this.rankings.setTail(sentinel_r.getTwin());

    }

    /**
     * searches the tree starting at the given node x for the given key, and returns the TreeNode
     * containing the key if it is found, or null if the key is not found
     * @param x root of a tree were searching in
     * @param key keyval of who were searching for
     * @return TreeNode object we're looking for, or null if doesnt exist
     */
    public Leaf<T> Search(TreeNode x, KeyVal key) {
        if (x instanceof Leaf){
            if (x.getKeyVal().compareTo(key) == 0) {
                return (Leaf<T>) x;
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

    /**
     * updates the key and size of the given TreeNode x by setting the key to the
     * largest key in the subtree rooted at x, and setting the size to the sum of the sizes of the
     * children of x.
     * @param x node we're updating
     */
    private void updateKeyAndSize(TreeNode x) {
        x.keyVal = x.children[L].getKeyVal();
        int size = 0;
        if (x.children[M] != null) {
            x.keyVal = x.children[M].getKeyVal();
//            x.setDegree(2);
        }
        if (x.children[R] != null) {
            x.keyVal = x.children[R].getKeyVal();
//            x.setDegree(3);
        }
        for (TreeNode child : x.children) {
            if (child != null) {
                size += child.getSize();
            }
        }
        x.setSize(size);
    }

    /**
     * sets the children of the given TreeNode root to the given left, middle, and right nodes.
     * It also sets the parent of each of the children to root, and updates the key and size of root
     * @param root set this nodes children
     * @param left left child node to be set
     * @param middle middle child node to be set
     * @param right right child node to be set
     */
    private void setchildren(TreeNode root, TreeNode left, TreeNode middle, TreeNode right) {
        TreeNode[] children = new TreeNode[]{left,middle,right};
        root.setChildren(children);
        for (TreeNode child : children) {
            if (child!=null)
                child.setParent(root);
        }
        updateKeyAndSize(root);
//        for (int i = 0; i <2; i++) {
//            root.setChild(left,i);
//        }
//        if (middle != null)
//            middle.setParent(root);
//        if (right != null)
//            right.setParent(root);
    }

    /**
     * inserts the given TreeNode insertMe into the given TreeNode root. If root has space
     * for the new node, it is inserted into the appropriate spot in the children.
     * If root is full, it is split into two nodes
     * @param root node were inserting to
     * @param insertMe node were inserting
     * @return null if there was roon, new node if it was split
     */
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
                setchildren(newRoot, root.getChild(M), root.getChild(R), null);
                setchildren(root, insertMe, root.getChild(L), null);
            } else if (insertMe.getKeyVal().compareTo(root.getChild(M).getKeyVal()) < 0) { // L,insertme,M,R
                setchildren(newRoot, root.getChild(M), root.getChild(R), null);
                setchildren(root, root.getChild(L), insertMe, null);
            } else if (insertMe.getKeyVal().compareTo(root.getChild(R).getKeyVal()) < 0) {// L,M,insertme,R
                setchildren(newRoot, insertMe, root.getChild(R), null);
                setchildren(root, root.getChild(L), root.getChild(M), null);
            } else {
                setchildren(newRoot, root.getChild(R), insertMe, null);
                setchildren(root, root.getChild(L), root.getChild(M), null); //L,M,R,insertme
            }
            return newRoot;
        }
    }

    /**
     * // insert inserts the given TreeNode insertMe into the tree. It does this by navigating down the tree
     * to the correct leaf node where insertMe should be inserted, creating a new Leaf object to hold
     * insertMe, and then splitting the nodes in the tree as necessary to make room for the new Leaf.
     * Finally, it uses the placeInList method to insert the new Leaf into the doubly linked list of
     * Leaf objects in the tree.
     * @param insertMe node to be inserted
     */
    //TODO: maybe need to recieve a leaf in the first place
    public void insert(Leaf<T> insertMe){
        TreeNode y = this.getRoot();//T.getRoot();
        while(!(y instanceof  Leaf)){ //risky converions with leaf ?
            if (insertMe.getKeyVal().compareTo(y.getChild(L).getKeyVal())<0)
                y = y.getChild(L);
            else if (insertMe.getKeyVal().compareTo(y.getChild(M).getKeyVal())<0)
                y = y.getChild(M);
            else y=y.getChild(R);
        }
        TreeNode x = y.getParent(); //x is one level aboce the leaves, and thats where insertMe fits
//        Leaf newLeaf = new Leaf(insertMe); // our new node is going to be a leaf no matter what
        TreeNode z = insertAndSplit(x,insertMe);
        while (x!=this.getRoot()){ //CHANGED THIS TO COMPARE OBJECTS NOT VALS
            x = x.getParent();
            if (z != null) // needed to split, so we go up the tree till we find a spot
                z = insertAndSplit(x,z);
            else { //
                updateKeyAndSize(x);
            }
        }
        if (z!=null){
            TreeNode newRoot = new TreeNode();
            setchildren(newRoot,x,z,null);
            this.setRoot(newRoot);
        }
        placeInList(insertMe); // place our new leaf in the correct place
    }

    /**
     * placeInList inserts the given Leaf addMe into the doubly linked list of Leaf objects in the tree.
     * @param addMe leaf to be inserted
     */
    protected void placeInList(Leaf<T> addMe){
        if (addMe.getKeyVal().getKey() == MAX_VALUE || addMe.getKeyVal().getKey() == MIN_VALUE)
            return;
        int rank = Rank(addMe);
        Leaf<T> prev = selectKthLeaf(this.root,rank-1); //may be null, its ok
        if (prev != null)
            this.getRankings().addNode(addMe.getRankTwin(),prev.getRankTwin());
        else
            this.getRankings().addNode(addMe.getRankTwin());
    }

    /**
     * returns the rank of the given TreeNode x in the tree.
     * The rank is the number of Leaf objects
     * in the tree with keys less than the key of x.
     * @param x node were looking the rank of
     * @return returns the rank of the given TreeNode x in the tree
     */
    public int Rank(TreeNode x){
        int rank = 1;
        TreeNode y = x.getParent();
        while(y!=null){
            if(x.getKeyVal().compareTo(y.getChild(M).getKeyVal())==0)
                rank+=y.getChild(L).getSize();
            else {
                if ( y.getChild(R) != null){
                    if(x.getKeyVal().compareTo(y.getChild(R).getKeyVal())==0)
                        rank+=(y.getChild(L).getSize() + y.getChild(L).getSize());
                }
            }
            x=y;
            y=y.getParent();
        }
        return rank;
    }

    /**
     * returns the kth-ranked Leaf object in the tree
     * @param x root of the tree we're searching for x in
     * @param k the rank we're searching for
     * @return returns the kth-ranked Leaf object in the tree
     */
    //risky conversion?
    public Leaf<T> selectKthLeaf(TreeNode x, int k){
        if (x.getSize()<k)
            return null;
        if (x instanceof Leaf)
            return (Leaf<T>) x;
        int leftSize = x.getChild(L).getSize();
        int leftMiddleSize = x.getChild(L).getSize()+x.getChild(M).getSize();
        if (k<=leftSize)
            return selectKthLeaf(x.getChild(L),k);
        else if (k<=leftMiddleSize)
            return selectKthLeaf(x.getChild(M),k-leftSize);
        else return selectKthLeaf(x.getChild(R),k-leftMiddleSize);
    }

    /**
     * borrows a child from a sibling of y if possible, or merges y with one of its siblings
     * if necessary.
     * @param y node were borrowing or merging
     * @return the parent of y after the borrow or merge operation is complete
     */
    private TreeNode borrowOrMerge(TreeNode y){
        TreeNode z =y.getParent();
        if (y.getKeyVal().compareTo(z.getChild(L).getKeyVal())==0){
            TreeNode x = z.getChild(M);
            if(x.getChild(R)!=null){
                setchildren(y,y.getChild(L),x.getChild(L),null);
                setchildren(x,x.getChild(M),x.getChild(R),null);
            }
            else{
                setchildren(x,y.getChild(L),x.getChild(L),y.getChild(M));
                y=null;
                setchildren(z,x,z.getChild(R),null);
            }
            return z;
        }
        if (y.getKeyVal().compareTo(z.getChild(M).getKeyVal())==0){
            TreeNode x =z.getChild(L);
            if (x.getChild(R)!=null){
                setchildren(y,x.getChild(R),y.getChild(L),null);
                setchildren(x,x.getChild(L),x.getChild(M),null);
            }
            else{
                setchildren(x,x.getChild(L),x.getChild(M),y.getChild(L));
                y=null;
                setchildren(z,x,z.getChild(R),null);
            }
            return z;
        }
        TreeNode x=z.getChild(M);
        if(x.getChild(R)!=null){
            setchildren(y,x.getChild(R),y.getChild(L),null);
            setchildren(x,x.getChild(L),x.getChild(M),null);
        }
        else{
            setchildren(x,x.getChild(L),x.getChild(M),y.getChild(L));
            y=null;
            setchildren(z,z.getChild(L),x,null);
        }
        return z;
    }


    public void Delete(Leaf<T> deleteMe){
        getRankings().removeNode(deleteMe.getRankTwin());
        TreeNode y = deleteMe.getParent();
        if (deleteMe.getKeyVal().compareTo(y.getChild(L).getKeyVal())==0)
            setchildren(y,y.getChild(M),y.getChild(R),null);
        else if (deleteMe.getKeyVal().compareTo(y.getChild(M).getKeyVal())==0)
            setchildren(y,y.getChild(L),y.getChild(R),null);
        else setchildren(y,y.getChild(L),y.getChild(M),null);
        deleteMe = null;
        while (y!=null){
            if (y.getChild(M)==null){
                if (y!=root)
                    y=borrowOrMerge(y);
                else{
                    setRoot(y.getChild(L));
                    y.getChild(L).setParent(null);
                    y=null;
                    return;
                }
            }
            else{
                updateKeyAndSize(y);
                y=y.getParent();
            }
        }
    }

    public DoublyLinkedList<T> getRankings() {
        return rankings;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
}
