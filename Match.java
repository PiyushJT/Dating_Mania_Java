import java.sql.*;

public class Match {

    int senderUserId;
    int receiverUserId;
    boolean isAccepted;
    long acceptedAt;
    long sentAt;
    String matchedOn;
    boolean isDeleted;
    User sender;

    public Match(
            int senderUserId, int receiverUserId,
            boolean isAccepted, long acceptedAt,
            long sentAt, String matchedOn,
            boolean isDeleted
    ) {
        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.isAccepted = isAccepted;
        this.acceptedAt = acceptedAt;
        this.sentAt = sentAt;
        this.matchedOn = matchedOn;
        this.isDeleted = isDeleted;

        try {
            this.sender = DatabaseIO.getUserFromUid(senderUserId);
        }
        catch (SQLException e) {
            Log.E("Error getting sender user: " + e.getMessage());
        }

    }

    public Match(
            int senderUserId, int receiverUserId,
            long sentAt, String matchedOn
    ) {

        this.senderUserId = senderUserId;
        this.receiverUserId = receiverUserId;
        this.isAccepted = false;
        this.sentAt = sentAt;
        this.matchedOn = matchedOn;
        this.isDeleted = false;

        try {
            this.sender = DatabaseIO.getUserFromUid(senderUserId);
        }
        catch (SQLException e) {
            Log.E("Error getting sender user: " + e.getMessage());
        }

    }


    static Match fromDB(ResultSet rs) throws SQLException {

        int senderUserId = rs.getInt("sender_user_id");
        int receiverUserId = rs.getInt("receiver_user_id");
        boolean isAccepted = rs.getBoolean("is_accepted");
        long sentAt = Utility.getDateFromSQLDate(rs.getString("sent_at"));
        String matchedOn = rs.getString("matched_on");
        boolean isDeleted = rs.getBoolean("is_deleted");

        long acceptedAt = 0;


        return new Match(senderUserId, receiverUserId, isAccepted, acceptedAt, sentAt, matchedOn, isDeleted);

    }


    public String toString() {

        String sentAt = Utility.getDateString(this.sentAt);

        return String.format("%-2d %-25s %-10s %-22s", senderUserId, sender.getName(), matchedOn, sentAt);

    }

}