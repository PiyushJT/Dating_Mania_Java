package ds;

import model.Match;

public class MatchLinkedList extends AbstractLinkedList {


    static class MatchNode {
        Match data;
        MatchNode next;

        MatchNode(Match data) {
            this.data = data;
        }

    }


    MatchNode head;

    public void insert(Match data) {

        MatchNode n = head;

        if (n != null) {
            while (n.next != null)
                n = n.next;
            n.next = new MatchNode(data);
        }
        else {
            head = new MatchNode(data);
        }

    }

    @Override
    public void delete() {

        MatchNode n = head;

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


        MatchNode n = head;

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
        MatchNode n = head;

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


    public Match get(int ind) {

        MatchNode n = head;

        for (int i = 0; i < ind; i++)
            if (n.next != null)
                n = n.next;


        return n.data;

    }


    public Match[] toArray() {

        Match[] arr = new Match[this.length()];

        MatchNode n = head;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = n.data;
            n = n.next;
        }

        return arr;

    }

}