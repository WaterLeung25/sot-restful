package GameClient;



import gameService.gameWhatever.Maja;
import gameService.gameWhatever.Player;
import org.glassfish.jersey.client.ClientConfig;

import javax.swing.*;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.util.ArrayList;

public class GameClient {
    private JPanel panel1;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton signInButton;
    private JButton logInButton;
    private JButton winButton;
    private JButton loseButton;
    private JList list1;
    private JButton showRankButton;
    private JButton resetScoreButton;

    ClientConfig config = new ClientConfig();
    Client client = ClientBuilder.newClient(config);
    URI baseURI = UriBuilder.fromUri("http://localhost:8080/GameRestService/rest").build();
    WebTarget serviceTarget = client.target(baseURI);

    public GameClient() {
        winButton.setEnabled(false);
        loseButton.setEnabled(false);
        resetScoreButton.setEnabled(false);

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = textField1.getText();
                char[] input = passwordField1.getPassword();
                String password = "";
                for (Character c : input){
                    password += c.toString();
                }
                boolean success = register(new Player(name, password));
                if (success){
                    JOptionPane.showMessageDialog(null, "You register successfully");
                } else {
                    //JOptionPane.showMessageDialog(null, Y);
                }
            }
        });
        logInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        winButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        loseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        showRankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        resetScoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void getScore(String name){
        WebTarget methodTarget = serviceTarget.path("gameService").path("score").path(name);
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            int score = response.readEntity(int.class);
            System.out.println(score);
        } else {
            System.err.println("Something wrong");
        }
    }

    public void maja(String name){
        WebTarget methodTarget = serviceTarget.path("gameService").path("maja").path(name);
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            Maja m = response.readEntity(Maja.class);
            System.out.println(m);
        } else {
            System.err.println("Something wrong");
        }
    }

    public void hello(){
        WebTarget methodTarget = serviceTarget.path("gameService").path("hello");
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.TEXT_PLAIN);
        Response response = requestBuilder.get();

        String result = response.readEntity(String.class);
        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            System.out.println(result);
        } else {
            System.err.println(result);
        }
    }

    public boolean login(String name, String password){
        WebTarget methodTarget = serviceTarget.path("gameService").queryParam("name", name).queryParam("password", password);
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            Player player = response.readEntity(Player.class);
            System.out.println("Player " + player.getName() + " logged in!");
            return true;
        } else {
            System.err.println(response.readEntity(String.class));
            return false;
        }
    }

    public boolean register(Player player){
        Response response = serviceTarget.path("gameService").request()
                .accept(MediaType.TEXT_PLAIN)
                .post(Entity.entity(player, MediaType.APPLICATION_JSON));

        if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()){
            System.out.println("A play registered in the system");
            return true;
        } else {
            System.err.println(response.readEntity(String.class));
            return false;
        }
    }

    public void playGame(String name, String update){
        WebTarget methodTarget = serviceTarget.path("gameService").path("update").path("score").path(name);
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.APPLICATION_JSON);

        Form form = new Form();
        form.param("update", update);
        Entity<Form> entity = Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED);

        Response response = requestBuilder.put(entity);

        if (response.getStatus() != Response.Status.NO_CONTENT.getStatusCode()){
            System.err.println(response.readEntity(String.class));
        }
    }

    public void showRank(){
        Response response = serviceTarget.path("gameService").path("rank").request().accept(MediaType.APPLICATION_JSON).get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            GenericType<ArrayList<String>> genericType = new GenericType<ArrayList<String>>(){  };
            ArrayList<String> rank = response.readEntity(genericType);
            for (String s : rank){
                System.out.println(s);
            }
        } else {
            System.err.println("Ooops");
        }
    }

    public void resetScore(String name){
        WebTarget methodTarget = serviceTarget.path("gameService").path("reset").path(name);
        Invocation.Builder requestBuilder = methodTarget.request().accept(MediaType.APPLICATION_JSON);
        Response response = requestBuilder.delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()){
            System.out.println(response.readEntity(String.class));
        } else {
            System.err.println(response.readEntity(String.class));
        }
    }

    public static void main(String[] args){
        GameClient gameClient = new GameClient();
        gameClient.hello();
        //gameClient.login("Water", "water");
        //gameClient.showRank();
        //gameClient.register(new Player("A", "a"));
        //gameClient.resetScore("Water");
        gameClient.maja("maja");
        gameClient.getScore("Water");
    }

}
