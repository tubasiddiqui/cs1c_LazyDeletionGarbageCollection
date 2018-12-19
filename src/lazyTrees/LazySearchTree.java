/**
 * Tuba Siddiqui
 * CS1C Program 5
 * Lazy deletion on tree
 */

package lazyTrees;

import java.util.*;

/**
 * implement lazy deletion
 * add attributes
 * @param <E>
 */
//In the new file, refactor outer public class from FHsearch_tree to LazySearchTree
public class LazySearchTree<E extends Comparable<? super E>>
        implements Cloneable {

    //mSize is still there and will only reflect the number of undeleted nodes
    protected int mSize;
    protected LazySTNode mRoot;
    //Add a new int mSizeHard attribute to LazySearchTree class which tracks the number of hard
    //nodes in it, i.e., both deleted and undeleted
    protected int mSizeHard;

    /**
     * constructor to initialize LazySearchTree()
     */
    //constructor
    public LazySearchTree() {
        clear();
    }

    /**
     * check if tree is empty or not
     *
     * @return true if empty
     */
    public boolean empty() {
        return (mSize == 0);
    }

    /**
     * @return size of tree
     */
    public int size() {
        return mSize;
    }

    /**
     * @return height of tree
     */
    //not used here - ignore this
    public int showHeight() {
        return findHeight(mRoot, -1);
    }

    /**
     * accessor so client can test class
     * displays both soft size and hard size
     * @return hard size of class
     */
    //Give it an accessor method called sizeHard(), so the client can test the class
    //by displaying both the soft size (the number the client normally wants) and the hard size.
    public int sizeHard() {
        return mSizeHard;
    }

    /**
     * initialize attributes
     */
    public void clear() {
        mSize = 0;
        mRoot = null;
        mSizeHard = 0;
    }

    /**
     * find min of tree or throw exception if empty
     * @return minimum of tree
     */
    public E findMin() {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMin(mRoot).data;
    }

    /**
     * find max of tree or throw exception if empty
     * @return max of tree or throw exception if empty
     */
    public E findMax() {
        if (mRoot == null)
            throw new NoSuchElementException();
        return findMax(mRoot).data;
    }


    /**
     * find specific data in tree
     * @param x
     * @return data in tree if found or exception if null
     */
    // public version available to client
    E find(E x) {
        LazySTNode resultNode;
        resultNode = find(mRoot, x);
        if (resultNode == null)
            throw new NoSuchElementException();
        return resultNode.data;
    }


    /**
     * @param x
     * @return boolean to state if x is in the tree or not
     */
    boolean contains(E x) {
        return find(mRoot, x) != null;
    }


    /**
     * insert data
     * @param x
     * @return boolean true if inserted
     */
    //Adjust insert() and any other methods that might need revision to work with this new deletion technique
    public boolean insert(E x) {
        int oldSize = mSize;
        mRoot = insert(mRoot, x);
        return (mSize != oldSize);
    }

    /**
     * remove node
     * @param x
     * @return boolean true if removed node
     */
    //Revise remove() (both the public and protected/recursive version) to implement lazy deletion.
    public boolean remove(E x) {
        int oldSize = mSize;

        LazySTNode temp = find(mRoot, x);
        if(temp != null) {
            temp.deleted = true;
            mSize--;
            return true;
        }
        else {
            remove(mRoot, x);
        }
        return (mSize != oldSize);
    }

    /**
     * public facing method of traverseHard() receive a generic argument for the functor
     * to use while traversing the current instance of LazySearchTree
     * @param func
     * @param <F>
     */
    //Rename the public/protected pair traverse() to traverseHard(). We will use these to traverse all nodes in the LazySearchTree,
    //which means both "deleted" and non-deleted nodes.
    public <F extends Traverser<? super E>>
    void traverseHard(F func) {
        traverseHard(func, mRoot);
    }

    /**
     * @param func
     * @param <F>
     */
    //Add a public/protected pair of traverseSoft() methods such that it traverses nodes
    //that have not been marked as deleted in the LazySearchTree.
    public <F extends Traverser<? super E>>
    void traverseSoft(F func) {
        traverseSoft(func, mRoot);
    }

    /**
     * clone subtree
     * @return cloned object
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        LazySearchTree<E> newObject = (LazySearchTree<E>) super.clone();
        newObject.clear();  // can't point to other's data

        newObject.mRoot = cloneSubtree(mRoot);
        newObject.mSize = mSize;

        return newObject;
    }

    //****** private helper methods *********
    /**
     * search from root to return min node that is not deleted
     * find from left since left < right
     * @param root
     * @return
     */
    //Modify the protected methods findMin() and findMax() to implement lazy deletion. These should start searching from the root
    //and return the minimum and maximum node that has NOT been deleted.
    protected LazySTNode findMin(LazySTNode root) {

        if (root == null)
            return null;

        LazySTNode minNode = findMin(root.lftChild);
        if (minNode != null) {
            return minNode;
        }
        //implement lazy deletion on protected findMin()
        if (root.deleted==true) {
            return findMin(root.rtChild);
        }

        return root;
    }


    /**
     * implement lazy deletion
     * find max from right child since right has greatest value
     * @param root
     * @return max node that is not deleted
     */
    //Modify the protected methods findMin() and findMax() to implement lazy deletion.
    //These should start searching from the root and return the minimum and maximum node that has NOT been deleted.
    protected LazySTNode findMax(LazySTNode root) {
        if (root == null)
            return null;

        LazySTNode maxNode = findMax(root.rtChild);
        if (maxNode != null) {
            return maxNode;
        }

        //implement lazy deletion on protected findMax()
        if (root.deleted==true) {
            return findMax(root.lftChild);
        }

        return root;
    }


    /**
     * insert data
     * @param root
     * @param x
     * @return node object
     */
    //Adjust insert() and any other methods that might need revision to work with this new deletion technique
    protected LazySTNode insert(LazySTNode root, E x) {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null) {
            mSize++;
            mSizeHard++;
            return new LazySTNode(x, null, null);
        }

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            root.lftChild = insert(root.lftChild, x);
        else if (compareResult > 0)
            root.rtChild = insert(root.rtChild, x);
            //revise for deletion
        else if (root.deleted == true) {
            root.deleted = false;
            mSize++;
        }

        return root;
    }

    /**
     * implement lazy deletion
     * remove node
     * @param root
     * @param x
     */
    //Revise remove() (both the public and protected/recursive version) to implement lazy deletion.
    protected void remove(LazySTNode root, E x) {

        if (root == null)
            return;

        LazySTNode temp;
        temp = find(root, x);

        if (temp != null)//if found
        {
            //modify for lazy deletion
            temp.deleted = true;
            mSize--;
        }
    }

    /**
     * ignore nodes marked as deleted
     * implement lazy deletion
     * @param root
     * @param x
     * @return
     */
    // Modify the protected method find() to implement lazy deletion such
    // that it will ignore any nodes that are marked as "deleted".
    protected LazySTNode find(LazySTNode root, E x) {
        int compareResult;  // avoid multiple calls to compareTo()

        if (root == null)
            return null;

        compareResult = x.compareTo(root.data);
        if (compareResult < 0)
            return find(root.lftChild, x);
        if (compareResult > 0)
            return find(root.rtChild, x);
        //implement lazy deletion
        if (root.deleted==false)
            return root;

        return null;// found!
    }


    /**
     * clone tree
     * @param root
     * @return
     */
    protected LazySTNode cloneSubtree(LazySTNode root) {
        LazySTNode newNode;
        if (root == null)
            return null;

        newNode = new LazySTNode(
                root.data,
                cloneSubtree(root.rtChild),
                cloneSubtree(root.lftChild)
        );

        return newNode;

    }

    /**
     * traverse all nodes in tree
     * @param func
     * @param treeNode
     * @param <F>
     */
    //Rename the public/protected pair traverse() to traverseHard(). We will use these to traverse all nodes in the LazySearchTree,
    //which means both "deleted" and non-deleted nodes
    protected <F extends Traverser<? super E>> void traverseHard(F func, LazySTNode treeNode) {
        {
            if (treeNode == null)
                return;

            traverseHard(func, treeNode.lftChild);
            func.visit(treeNode.data);
            traverseHard(func, treeNode.rtChild);
        }

    }

    /**
     * traverse nodes that have not been marked as deleted in tree
     * @param func
     * @param treeNode
     * @param <F>
     */
    //Add a public/protected pair of traverseSoft() methods such that it traverses
    //nodes that have not been marked as deleted in the LazySearchTree.
    protected <F extends Traverser<? super E>> void traverseSoft(F func, LazySTNode treeNode) {
        if (treeNode == null)
            return;

        traverseSoft(func, treeNode.lftChild);
        //traverse nodes that have not been marked as deleted in LazySearchTree
        if(treeNode.deleted==false)
            func.visit(treeNode.data);
        traverseSoft(func, treeNode.rtChild);

    }

    /**
     * find height of tree
     * @param treeNode
     * @param height
     * @return
     */
    protected int findHeight(LazySTNode treeNode, int height) {
        int leftHeight, rightHeight;
        if (treeNode == null)
            return height;
        height++;
        leftHeight = findHeight(treeNode.lftChild, height);
        rightHeight = findHeight(treeNode.rtChild, height);
        return (leftHeight > rightHeight) ? leftHeight : rightHeight;
    }


    //********* inner class ************

    /**
     * inner class
     */
    //Remove the type parameterization of the inner LazySTNode class
    private class LazySTNode {
        // use protected access so the tree, in the same package,
        // or derived classes can access members
        protected LazySTNode lftChild, rtChild;
        protected E data;
        protected LazySTNode myRoot; // needed to test for certain error
        //Add a boolean deleted attribute to LazySTNode
        //Adjust this class to accommodate this member
        public boolean deleted;

        public LazySTNode(E d, LazySTNode lft, LazySTNode rt) {
            lftChild = lft;
            rtChild = rt;
            data = d;
            deleted = false;
        }

        public LazySTNode() {
            this(null, null, null);
        }
    }
}

/**
 * implement traverser
 * @param <E>
 */
interface Traverser<E> {
    public void visit(E x);
}


/**
 * print objects of class
 * @param <E>
 */
class PrintObject<E> implements Traverser<E> {
    public void visit(E x) {
        System.out.print(x + " ");
    }
};