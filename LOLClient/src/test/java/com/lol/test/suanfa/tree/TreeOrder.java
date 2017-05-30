package com.lol.test.suanfa.tree;

import java.util.Stack;

public class TreeOrder {
      
    /** 
     * 递归实现前序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void preOrderMethodOne(TreeNode treeNode) {  
        if (null != treeNode) {  
            System.out.print(treeNode.data + "  ");  
            if (null != treeNode.left) {  
                preOrderMethodOne(treeNode.left);  
            }  
            if (null != treeNode.right) {  
                preOrderMethodOne(treeNode.right);  
  
            }  
        }  
    }  
  
    /** 
     * 循环实现前序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void preOrderMethodTwo(TreeNode treeNode) {  
        if (null != treeNode) {  
            Stack<TreeNode> stack = new Stack<TreeNode>();
            stack.push(treeNode);  
            while (!stack.isEmpty()) {  
                TreeNode tempNode = stack.pop();  
                System.out.print(tempNode.data + "  ");  
                // 右子节点不为null,先把右子节点放进去  
                if (null != tempNode.right) {  
                    stack.push(tempNode.right);  
                }  
                // 放完右子节点放左子节点，下次先取  
                if (null != tempNode.left) {  
                    stack.push(tempNode.left);  
                }  
            }  
        }  
    }  
      
    /** 
     * 递归实现中序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void medOrderMethodOne(TreeNode treeNode){  
        if (null != treeNode) {           
            if (null != treeNode.left) {  
                medOrderMethodOne(treeNode.left);  
            }  
            System.out.print(treeNode.data + "  ");  
            if (null != treeNode.right) {  
                medOrderMethodOne(treeNode.right);  
            }  
        }  
          
    }  
      
    /** 
     * 循环实现中序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void medOrderMethodTwo(TreeNode treeNode){      
        Stack<TreeNode> stack = new Stack<TreeNode>();    
        TreeNode current = treeNode;    
        while (current != null || !stack.isEmpty()) {    
            while(current != null) {    
                stack.push(current);    
                current = current.left;    
            }    
            if (!stack.isEmpty()) {    
                current = stack.pop();    
                System.out.print(current.data+"  ");    
                current = current.right;    
            }    
        }         
    }  
      
    /** 
     * 递归实现后序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void postOrderMethodOne(TreeNode treeNode){         
        if (null != treeNode) {       
            if (null != treeNode.left) {  
                postOrderMethodOne(treeNode.left);  
            }  
            if (null != treeNode.right) {  
                postOrderMethodOne(treeNode.right);  
            }  
            System.out.print(treeNode.data + "  ");  
        }  
          
    }  
      
    /** 
     * 循环实现后序遍历 
     * @author linbingwen 
     * @since  2015年8月29日  
     * @param treeNode 
     */  
    public static void postOrderMethodTwo(TreeNode treeNode){  
        if (null != treeNode) {  
            Stack<TreeNode> stack = new Stack<TreeNode>();  
            TreeNode current = treeNode;  
            TreeNode rightNode = null;  
            while(current != null || !stack.isEmpty()) {    
                while(current != null) {    
                    stack.push(current);    
                    current = current.left;    
                }    
                current = stack.pop();    
                while (current != null && (current.right == null ||current.right == rightNode)) {    
                    System.out.print(current.data + "  ");    
                    rightNode = current;    
                    if (stack.isEmpty()){    
                        System.out.println();    
                        return;    
                    }    
                    current = stack.pop();    
                }    
                stack.push(current);    
                current = current.right;    
            }    
              
              
              
        }  
    }  
      
}  