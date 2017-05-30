package com.lol.test.suanfa.tree;

public class SearchTreeTest {
  
    /** 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param args     
     */  
    public static void main(String[] args) {  
        SearchTree tree = new SearchTree();  
        tree.addTreeNode(50);  
        tree.addTreeNode(80);  
        tree.addTreeNode(20);  
        tree.addTreeNode(60);     
        tree.addTreeNode(10);  
        tree.addTreeNode(30);  
        tree.addTreeNode(70);  
        tree.addTreeNode(90);     
        tree.addTreeNode(100);  
        tree.addTreeNode(40);  
        System.out.println("=============================="+"采用递归的前序遍历开始"+"==============================");  
        TreeOrder.preOrderMethodOne(tree.root);  
        System.out.println();  
        System.out.println("=============================="+"采用循环的前序遍历开始"+"==============================");  
        TreeOrder.preOrderMethodTwo(tree.root);  
        System.out.println();  
        System.out.println("=============================="+"采用递归的后序遍历开始"+"==============================");  
        TreeOrder.postOrderMethodOne(tree.root);  
        System.out.println();  
        System.out.println("=============================="+"采用循环的后序遍历开始"+"==============================");  
        TreeOrder.postOrderMethodTwo(tree.root);  
        System.out.println();  
        System.out.println("=============================="+"采用递归的中序遍历开始"+"==============================");  
        TreeOrder.medOrderMethodOne(tree.root);  
        System.out.println();  
        System.out.println("=============================="+"采用循环的中序遍历开始"+"==============================");  
        TreeOrder.medOrderMethodTwo(tree.root);

        TreeNode node = tree.findTreeNode(100);
        System.out.println(node);

    }  
  
}  