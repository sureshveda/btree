package com.wizard;

import com.wizard.exception.NoParentFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        int gNodes = 4;
        List<Integer> fromKeyList = new ArrayList<Integer>( Arrays. asList( 0 , 0 , 1 ) );
        List<Integer> toKeyList = new ArrayList<Integer>( Arrays. asList( 1 , 2 , 3 ) );
        List<Integer> valList = new ArrayList<Integer>( Arrays. asList( 5 , 5 , 7, 5 ) );

        // Form initial tree
        BinaryTree parentTree = new BinaryTree(fromKeyList.get(0) , valList.get(0));

        for (int index = 0; index < gNodes - 1; index++) {
            int from = fromKeyList.get(index);
            int to = toKeyList.get(index);
            int val = valList.get(index + 1);
            try {
                parentTree.add(from, to, val);
            } catch (NoParentFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Printing Parent tree in pre order");
        parentTree.traversePreOrder(parentTree.root);


        //Now start removing specific edges from the tree by marking a specific edge index as unusable
        gNodes = 3;
        int edgeToRemoveIndex = 0;
        int andValMatchCount = 0;
        while (edgeToRemoveIndex < fromKeyList.size()) {
            System.out.println();
            System.out.println("Now removing edge: " + (fromKeyList.get(edgeToRemoveIndex)) + ", " + (toKeyList.get(edgeToRemoveIndex)));
            BinaryTree subTree1 = null;
            BinaryTree subTree2 = null;
            // Create substrees
            for (int index = 0; index < gNodes ; index++) {
                // Consider one of the edges to be deleted and process only the rest
                if (index == edgeToRemoveIndex)
                    continue;
                if (subTree1==null) {
                    subTree1 = new BinaryTree(fromKeyList.get(index), valList.get(index));
                }
                int from = fromKeyList.get(index);
                int to = toKeyList.get(index);
                int val = valList.get(index + 1);
                try {
                    subTree1.add(from, to, val);
                } catch (NoParentFoundException e) {
                    // This means we need to create a second tree with this "from" as root and continue from there

                    if (subTree2==null) {
                        // Find the value from the parent tree and use it to create new root node
                        subTree2 = new BinaryTree(from, parentTree.findNode(from).value);
                    }
                    try {
                        subTree2.add(from, to, val);
                    } catch (NoParentFoundException noParentFoundException) {
                        noParentFoundException.printStackTrace();
                    }
                }
            }
            System.out.println("Printing Sub tree1 in pre order");
            subTree1.traversePreOrder(subTree1.root);
            int subtree1AndVal = subTree1.computeAndValue();
            System.out.println("AND value for subtree 1= " + subtree1AndVal);

            if (subTree2!=null) {
                System.out.println("Printing Sub tree2 in pre order");
                subTree2.traversePreOrder(subTree2.root);
                int subtree2AndVal = subTree2.computeAndValue();
                System.out.println("AND value for subtree 2= " + subtree2AndVal);
                if (subtree1AndVal==subtree2AndVal) {
                    andValMatchCount++;
                }

            } else {
                List<BinaryTree.Node> parentNodeList = parentTree.getNodeList();
                List<BinaryTree.Node> subTree1NodeList = subTree1.getNodeList();
                parentNodeList.removeAll(subTree1NodeList);
                System.out.println("Printing Sub tree2:");
                System.out.println(parentNodeList.get(0));
                int subtree2AndVal = parentNodeList.get(0).value;
                System.out.println("AND value for subtree 2= " + subtree2AndVal);
                if (subtree1AndVal==subtree2AndVal) {
                    andValMatchCount++;
                }
            }

            edgeToRemoveIndex++;
        }

        System.out.println("___________________________________________________________________");
        System.out.println("___________________________________________________________________");
        System.out.println("There are " + andValMatchCount + " ways to split the tree into trees with equal bitwise AND");
    }
}
