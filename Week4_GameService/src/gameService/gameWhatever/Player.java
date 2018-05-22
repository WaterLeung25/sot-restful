package gameService.gameWhatever;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Player {
    private String name;
    private String password;

    public Player(String name, String password){
        setName(name);
        setPassword(password);
    }

    public Player(){}

    public String getName(){return name;}

    public void setName(String name){
        this.name = name;
    }

    public String getPassword() {return password;}

    public void setPassword(String password){
        this.password = password;
    }
}
