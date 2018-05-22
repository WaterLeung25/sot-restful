package gameService.gameWhatever;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Score {
    private String user;
    private int score;

    public Score(String user, int score){
        setUser(user);
        setScore(score);
    }

    public Score(){};

    public String getUser(){return user;}

    public void setUser(String user){
        this.user = user;
    }

    public int getScore(){return score;}

    public void setScore(int score){
        this.score = score;
    }
}
