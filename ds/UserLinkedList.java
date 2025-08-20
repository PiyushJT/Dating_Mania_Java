package ds;

import model.User;

public class UserLinkedList extends AbstractLinkedList {


    static class UserNode {
        User data;
        UserNode next;

        UserNode(User data) {
            this.data = data;
        }

    }


    UserNode head;

    public void insert(User data) {

        UserNode n = head;

        if (n != null) {
            while (n.next != null)
                n = n.next;
            n.next = new UserNode(data);
        }
        else {
            head = new UserNode(data);
        }

    }

    @Override
    public void delete() {

        UserNode n = head;

        if (head == null)
            return;

        if (head.next == null) {
            head = null;
            return;
        }

        while (n.next.next != null)
            n = n.next;
        n.next = null;

    }

    @Override
    public void display() {


        UserNode n = head;

        System.out.println();
        while (n != null) {
            System.out.print(n.data + " ");
            n = n.next;
        }
        System.out.println();


    }

    @Override
    public int length() {

        int i = 0;
        UserNode n = head;

        while (n != null) {
            n = n.next;
            i++;
        }

        return i;

    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public void clear() {
        head = null;
    }


    public User get(int ind) {

        UserNode n = head;

        for (int i = 0; i < ind; i++)
            if (n.next != null)
                n = n.next;


        return n.data;

    }


    public User[] toArray() {

        User[] arr = new User[this.length()];

        UserNode n = head;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = n.data;
            n = n.next;
        }

        return arr;

    }

}