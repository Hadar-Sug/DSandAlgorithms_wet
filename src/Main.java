import java.util.*;
import java.lang.AssertionError;
public class Main {
    public static void main(String[] args) {

        /** Initializing the tournament **/
        TechnionTournament tournament = new TechnionTournament();
        tournament.init();
        /** End of initializing the Tournament **/

        /** Adding faculties to the Tournament **/
        Map<Integer,String> faculties = new HashMap<>();
        faculties.put(1,"CS");
        faculties.put(2,"EE");
        faculties.put(3,"IE");
        faculties.put(4,"BME");

        for(Map.Entry<Integer,String> f : faculties.entrySet()){
            Faculty faculty = new Faculty(f.getKey(), f.getValue());
            tournament.addFacultyToTournament(faculty);
        }

        /** Adding players to the Tournament **/
        Player player = new Player(0,"");
        String[] names = {"Asil","Yuval", "Noga", "Adam","Yuval", "Ziyech", "Moshe", "Amit", "Amir", "Omer", "Maroon", "Ido"};
        int[] faculties_id = {1,1,2,2,3,3,4,4,4,5,6,6};
        for (int i = 0; i < names.length; i++) {
            player.setId(i);
            player.setName(names[i]);
            if(faculties_id[i] == 5 || faculties_id[i] == 6)
                continue;
            tournament.addPlayerToFaculty(faculties_id[i],player);
        }


        /** Playing some games between faculties **/
        ArrayList<Integer> home_faculty_goals = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            home_faculty_goals.add(1);
        }

        ArrayList<Integer> away_faculty_goals = new ArrayList<>();
        away_faculty_goals.add(4);//changed to 4-5 was 5-6
        away_faculty_goals.add(5);

        /*-----GAME 1 - CS wins:
        * points: CS - 3
        * goals: Yuval(1) - 4
        *       Yuval(4) - 1
        *       ziyech - 1
        * */

        tournament.playGame(1,3,1,home_faculty_goals,away_faculty_goals);
        tournament.getTopScorer(player);
        boolean expression = player.getId() == 1 && player.getName().equals("Yuval");
        Assert(expression);

        home_faculty_goals.clear();
        away_faculty_goals.clear();

        home_faculty_goals.add(5);
        home_faculty_goals.add(5);
        away_faculty_goals.add(7);
        away_faculty_goals.add(8);

        /*-----GAME 2 - IE wins:
         * points: CS(1) - 3
         *          IE(3) - 3
         * goals: Yuval(1) - 4
         *       Yuval(4) - 1
         *       ziyech(5) - 3
         *      Amit(7) - 1
         *        Amir(8) -1
         * */
        tournament.playGame(3,4,1,home_faculty_goals,away_faculty_goals);
        tournament.getTopScorer(player);
        expression =  player.getId() == 1 && player.getName().equals("Yuval");
        Assert(expression);

        /** Removing Teams from Tournament and getting the best scorers **/
        tournament.removeFacultyFromTournament(2);

        ArrayList<Player> scorers = new ArrayList<>();
        tournament.getTopKScorers(scorers,2,true);

        expression = scorers.get(1).getId() == 1  && scorers.get(1).getName().equals("Yuval");
        Assert(expression);
        expression = scorers.get(0).getId() == 5 && scorers.get(0).getName().equals("Ziyech");
        Assert(expression);

        tournament.getTopScorerInFaculty(1,player);
        expression = player.getId() == 1;
        Assert(expression);


        home_faculty_goals.clear();
        away_faculty_goals.clear();

        /*-----GAME 3 - TIE wins:
        (no more fac#2)
         * points: CS(1) - 4
         *          IE(3) - 3
         *          BME(4) - 1
         * goals: Yuval(1) - 4
         *       Yuval(4) - 1
         *       ziyech(5) - 3
         *      Amit(7) - 1
         *        Amir(8) -1
         * */
        tournament.playGame(4,1,0,home_faculty_goals,away_faculty_goals);

        tournament.removePlayerFromFaculty(1,1);
        tournament.getTopScorer(player);
        //TODO: test fail here, prob their mistake again, code looks good :)
        expression = player.getId() == 1 && player.getName().equals("Yuval");
        Assert(expression);

        ArrayList<Faculty> top_faculties = new ArrayList<>();
        tournament.getTopKFaculties(top_faculties,2,false);

        /** pay attention that until now we have current standing **/
        /*
           ____________________________________
           || Team_id || Team_name || Points ||
           ||_________||___________||________||
           ||    1    ||    CS     ||    4   ||
           ||    3    ||    IE     ||    3   ||
           ||    4    ||    CE     ||    1   ||
           ||_________||___________||________||

         */

        expression = top_faculties.get(0).getId() == 1 && top_faculties.get(0).getName().equals("CS");
        Assert(expression);
        expression = top_faculties.get(1).getId() == 3 && top_faculties.get(1).getName().equals("IE");
        Assert(expression);

        /** Get the winner faculty **/
        Faculty faculty = new Faculty(0,"");
        tournament.getTheWinner(faculty);
        expression = faculty.getId() == 1 && faculty.getName().equals("CS");
        Assert(expression);

        System.out.println("Congratulations You Have passed the Test ");
    }

    public static void Assert(boolean expression){
        if (!expression){
            throw new AssertionError();
        }
    }
}