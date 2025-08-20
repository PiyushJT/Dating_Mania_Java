package ds;

import model.User;

public class BST {

    private static class Node {

        User user;
        int score;
        Node left;
        Node right;

        public Node(User user, int score) {
            this.user = user;
            this.score = score;
        }

        @Override
        public String toString() {
            return score + "";
        }

    }

    Node root;

    public void insert(User user, int score) {
        root = insert(root, user, score);
    }

    Node insert(Node root, User user, int score) {

        if (root == null)
            return new Node(user, score);

        if (score < root.score)
            root.left = insert(root.left, user, score);
        else
            root.right = insert(root.right, user, score);

        return root;

    }


    public User pollMax() {
        if (root == null) {
            return null;
        }

        Node parent = null;
        Node current = root;

        // Find rightmost node
        while (current.right != null) {
            parent = current;
            current = current.right;
        }

        // current is the max node
        User maxUser = current.user;

        // If the max node has a left child, reattach it
        if (parent == null) {
            // Max is the root
            root = current.left;
        } else {
            parent.right = current.left;
        }

        return maxUser;
    }




    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }


}