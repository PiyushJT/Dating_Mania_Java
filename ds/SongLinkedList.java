package ds;

import model.Song;

import java.util.Random;

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



    public void shuffleSongs() {
        // Convert linked list to array for easier shuffling
        Song[] array = this.toArray();

        // Shuffle the array using Collections.shuffle logic
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            Song temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        // Clear the current list
        this.clear();

        // Re-insert songs in shuffled order back into linked list
        for (Song s : array) {
            this.insert(s);
        }
    }


}