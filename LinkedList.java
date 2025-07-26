public class LinkedList {

    Node head = null;

    public class Node {
        UserMatch data;
        Node next;

        Node(UserMatch data) {
            this.data = data;
            this.next = null;
        }

    }

    public void insertFirst(UserMatch data) {

        Node n = new Node(data);
        n.next = head;

        head = n;

    }


    public void insertEnd(UserMatch data) {

        Node n = head;

        if (n != null) {
            while (n.next != null)
                n = n.next;
            n.next = new Node(data);
        } else {
            head = new Node(data);
        }

    }


    public void deleteVal(UserMatch val){

        Node n = head;

        if (head.data == val) {
            head = head.next;
            return;
        }


        while (n.next != null){

            if (n.next.data == val){
                n.next = n.next.next;
                return;
            }

            n = n.next;
        }

    }





    public void deleteEnd() {

        Node n = head;

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

    public void deleteFirst() {

        if (head != null)
            head = head.next;

    }


    public void display() {

        Node n = head;

        System.out.println();
        while (n != null) {
            System.out.print(n.data + " ");
            n = n.next;
        }
        System.out.println();

    }
    public int length(){

        int i = 0;
        Node n = head;

        while (n != null) {
            n = n.next;
            i++;
        }

        return i;

    }


}