package ChippingDrills;

/*
 * This is the Java object that is going to represent our full Short Game Drill card.
 * It should contain 9 individual drills
 */
public class ShortGameCard {

    private int id;
    private int isComplete;
    private String lastUpdated;
    private int totalScore;
    private String Grade;
    private int handicap;

    public ShortGameCard(int isComplete, String lastUpdated, int totalScore, String grade, int handicap) {
        this.isComplete = isComplete;
        this.lastUpdated = lastUpdated;
        this.totalScore = totalScore;
        Grade = grade;
        this.handicap = handicap;
    }

    public ShortGameCard() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(int isComplete) {
        this.isComplete = isComplete;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = grade;
    }

    public int getHandicap() {
        return handicap;
    }

    public void setHandicap(int handicap) {
        this.handicap = handicap;
    }
}
