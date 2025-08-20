package ds;

import model.Song;

public class SongLinkedList extends AbstractLinkedList {


    static class SongNode {
        Song data;
        SongNode next;

        SongNode(Song data) {
            this.data = data;
        }

    }


    SongNode head;

    public void insert(Song data) {

        SongNode n = head;

        if (n != null) {
            while (n.next != null)
                n = n.next;
            n.next = new SongNode(data);
        }
        else {
            head = new SongNode(data);
        }

    }

    @Override
    public void delete() {

        SongNode n = head;

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


        SongNode n = head;

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
        SongNode n = head;

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


    public Song get(int ind) {

        SongNode n = head;

        for (int i = 0; i < ind; i++)
            if (n.next != null)
                n = n.next;


        return n.data;

    }


    public Song[] toArray() {

        Song[] arr = new Song[this.length()];

        SongNode n = head;

        for (int i = 0; i < arr.length; i++) {
            arr[i] = n.data;
            n = n.next;
        }

        return arr;

    }

}