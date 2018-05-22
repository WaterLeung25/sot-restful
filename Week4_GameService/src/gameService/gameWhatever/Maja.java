package gameService.gameWhatever;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Maja {
    String name;

    public Maja (String name){
        setName(name);
    }

    public Maja(){}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
