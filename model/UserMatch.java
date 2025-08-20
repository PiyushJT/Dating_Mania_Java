package model;

public class UserMatch implements Comparable<UserMatch> {
    User user;
    int score;

    public User getUser() {
        return user;
    }
    public UserMatch(User user, int score) {
        this.user = user;
        this.score = score;
    }

    @Override
    public int compareTo(UserMatch other) {
        // Reverse order for max-heap (top scores first)
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return user.getName() + " — Shared hobbies: " + score;
    }
}
