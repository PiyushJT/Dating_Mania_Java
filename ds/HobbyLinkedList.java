package ds;

import model.Hobby;

public class HobbyLinkedList extends AbstractLinkedList {


    static class HobbyNode {
        Hobby data;
        HobbyNode next;

        HobbyNode(Hobby data) {
            this.data = data;
        }

    }


    HobbyNode head;

    public void insert(Hobby data) {

        HobbyNode n = head;

        if (n != null) {
            while (n.next != null)
                n = n.next;
            n.next = new HobbyNode(data);
        }
        else {
            head = new HobbyNode(data);
        }

    }

    @Override
    public void delete() {

        HobbyNode n = head;

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


        HobbyNode n = head;

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
        HobbyNode n = head;

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


    public Hobby get(int ind) {

        HobbyNode n = head;

        for (int i = 0; i < ind; i++)
            if (n.next != null)
                n = n.next;


        return n.data;

    }


    public Hobby[] toArray() {

        Hobby[] arr = new Hobby[this.length()];

        HobbyNode n = head;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = n.data;
            n = n.next;
        }

        return arr;

    }

}