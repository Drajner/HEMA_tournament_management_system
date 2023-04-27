package drajner.hetman.services;

public class CompetitorGroupStatus {
    private Competitor competitor;
    private CompetitorStatus status;
    private float score;

    public CompetitorGroupStatus(Competitor competitor){
        this.competitor = competitor;
        this.status = CompetitorStatus.COMPETING;
        this.score = 0;
    }

    public CompetitorGroupStatus(Competitor competitor, CompetitorStatus status, float score){
        this.competitor = competitor;
        this.status = status;
        this.score = score;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public CompetitorStatus getStatus() {
        return status;
    }

    public float getScore() {
        return score;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public void setStatus(CompetitorStatus status) {
        this.status = status;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
