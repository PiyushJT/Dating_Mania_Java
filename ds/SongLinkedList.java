package ds;

import java.util.*;

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

    // New method to convert linked list to ArrayList
    private ArrayList<Song> toList() {
        ArrayList<Song> list = new ArrayList<>();
        SongNode n = head;

        while (n != null) {
            list.add(n.data);
            n = n.next;
        }

        return list;
    }


    public void shuffleSongs() {
        // Convert linked list to ArrayList
        ArrayList<Song> songList = new ArrayList<>(this.toList());

        // Shuffle using Collections.shuffle
        Collections.shuffle(songList);

        // Clear the current list
        this.clear();

        // Re-insert songs in shuffled order
        for (Song s : songList) {
            this.insert(s);
        }
    }



}