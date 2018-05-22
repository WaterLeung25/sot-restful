package gameService.service.resources;

import gameService.gameWhatever.Maja;
import gameService.gameWhatever.Player;
import gameService.gameWhatever.Score;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("gameService")
@Singleton
public class GameResources {
    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Score> scores = new ArrayList<>();

    public GameResources(){
        players.add(new Player("Water", "water"));
        players.add(new Player("Joe", "joe"));
        players.add(new Player("Sam", "sam"));
        scores.add(new Score("Water", 50));
        scores.add(new Score("Joe", 60));
        scores.add(new Score("Sam", 0));
    }


    @GET
    @Path("hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello(){
        return "Hello! Your service works! You have " + players.size() + " users.";
    }

    private ArrayList<String> createRank(ArrayList<Score> scores){
        ArrayList<String> rank =  new ArrayList<>();
        Score temp;
        for (int i = 0; i < scores.size(); i++){
            for (int j = 0; j < (scores.size() - i - 1); j++){
                if ((scores.get(j).getScore()) < (scores.get(j + 1).getScore())){
                    temp = scores.get(j);
                    scores.set(j, scores.get(j+1));
                    scores.set(j+1, temp);
                }
            }
        }
        int num = 1;
        String s;
        for (Score score : scores){
            s = num + ". " + score.getUser() + ", score: " + score.getScore();
            rank.add(s);
            num++;
        }
        return rank;
    }

    private Score getScoreByName(String name){
        for (Score score : scores){
            if (score.getUser().equals(name)){
                return score;
            }
        }
        return null;
    }

    private Player getUserByName(String name){
        for (Player player : players){
            if (player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    private Player verifyPassword(String name, String password){
        for (Player player : players){
            if ((player.getName().equals(name)) && (player.getPassword().equals(password))){
                return player;
            }
        }
        return null;
    }

    @GET
    @Path("maja/{name}")
    @Produces({MediaType.APPLICATION_JSON/*, MediaType.APPLICATION_XML*/})
    public Response getMaja(@PathParam("name") String name){
        Maja m = new Maja(name);
        return Response.ok(m).build();
    }

//    @GET
//    @Produces ({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    public Response getPlayer(@QueryParam("name") String name){
//        Player player = this.getUserByName(name);
//        if (player != null){
//            return Response.ok(player).build();
//        } else {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Player not exist").build();
//        }
//    }

    @GET
    @Path("score/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getScore(@PathParam("name") String name){
        Score score = getScoreByName(name);
        if (score != null){
            int scr = score.getScore();
            return Response.ok(scr).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(-1).build();
        }
    }

    @GET
    @Path("rank")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getRank(){
        ArrayList<String> rank = this.createRank(scores);
        GenericEntity<ArrayList<String>> entity = new GenericEntity<ArrayList<String>>(rank){};
        return Response.ok(entity).build();
    }

    @DELETE
    @Path ("reset/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response resetScore(@PathParam("name") String name){
        Score score = this.getScoreByName(name);
        if (score != null){
            scores.remove(score);
            return Response.ok("Your score is reset and you have been remove from the rank").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("You have not sign in or Your score is already reset!").build();
        }
    }

//    //initial score when user first register
//    @POST
//    @Consumes({MediaType.APPLICATION_XML})
//    public Response addScore(Score score){
//        if (getScoreByName(score.getUser()) == null){
//            scores.add(score);
//            return Response.noContent().build();
//        } else {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Score of " + score.getUser() + " is already initialized").build();
//        }
//    }

    //win/lose game
    @PUT
    @Path("update/score/{name}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void updateScore(@PathParam("name") String name, @FormParam("update") String update){
        int newScore;
        int updateScore = Integer.parseInt(update);
        Score score = getScoreByName(name);
        if (score != null){
            newScore = score.getScore() + updateScore;
            score.setScore(newScore);
            //return score.getUser() + " with score " + score.getScore();
        } else {
            newScore = 0 + updateScore;
            scores.add(new Score(name, newScore));
            score = getScoreByName(name);
            //return score.getUser() + " with score " + score.getScore();
        }
    }

    //user already in the database
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response login(@QueryParam("name") String name, @QueryParam("password") String password){
        Player player = this.verifyPassword(name, password);
        if (player != null){
            return Response.ok(player).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username or Password is wrong, please try again!").build();
        }
    }

    //new user
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response register(Player player){
        if (getUserByName(player.getName()) == null){
            players.add(player);
            scores.add(new Score(player.getName(), 0));
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Player with name " + player.getName() + " already exist!").build();
        }
    }





}
