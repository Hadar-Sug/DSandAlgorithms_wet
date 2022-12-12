import java.util.ArrayList;

public class TechnionTournament implements Tournament{


    //for all of these, the key attribute will be goals/points so the main comparisonis by key(=goals/points)
    // and secondary by val (=ID)
    TwoThreeTree<Player> PlayersByGoals; //tree of all players sorted by goals
    TwoThreeTree<Faculty> FacultiesByPoints; // tree of faculties sorted by points, each leaf is a tree for its respective team

    //for all of these, the key attribute will be 0 so the only comparison will be by the ID
    TwoThreeTree<Player> playersByID; // tree of all players sorted by ID
    TwoThreeTree<Faculty> FacultiesByID; //  tree of all faculties sorted by ID, each node leaf is a tree for its respective team

    TechnionTournament(){};
    @Override
    public void init() {
        this.FacultiesByPoints = new TwoThreeTree<Faculty>();
        this.PlayersByGoals = new TwoThreeTree<Player>();
        this.FacultiesByID = new TwoThreeTree<Faculty>();
        this.playersByID = new TwoThreeTree<Player>();
    }

    @Override
    public void addFacultyToTournament(Faculty faculty) {
        //check if it exists?
        int points = 0;
        int faculty_id = faculty.getId();
        Leaf<Faculty> newFacultyID = new Leaf<>(faculty,0,faculty_id);
        Leaf<Faculty> newFacultyPoints = new Leaf<>(faculty,points,faculty_id);
        newFacultyID.setTwin(newFacultyPoints);
        this.FacultiesByID.insert(newFacultyID);
        this.FacultiesByPoints.insert(newFacultyPoints);
    }

    @Override
    public void removeFacultyFromTournament(int faculty_id) {
        // find the faculty in the ID tree, get the pointer to the corresponding leaf in the points tree
        // get the pointer to the corresponding tram tree
        // change team of all players to free agent
        //delete both teams (Id and points) from the tree

    }

    @Override
    public void addPlayerToFaculty(int faculty_id,Player player) {
        //check the amount of players in the team isn't 11,
        // find the faculty in the faculty's tree
        // find the player in the playerID tree, so we get the goal count
        // insert player to teamID tree and teamGoal tree
        // change player team status

    }

    @Override
    public void removePlayerFromFaculty(int faculty_id, int player_id) {
        //check this doesn't drop the count below 2
        // find the faculty in the faculty's tree
        // find the player in the playerID tree, so we get the goal count
        // remove player from teamID tree and teamGoal tree
        //change player team status

    }

    @Override
    public void playGame(int faculty_id1, int faculty_id2, int winner,
                         ArrayList<Integer> faculty1_goals, ArrayList<Integer> faculty2_goals) {
        //find the team/s, add points based on winner parameter, update the teampoints tree(points were added)
        //then iterate over team player tree and add goals, updating the goals tree per team and the entire player tree
        // consequentially also each team goals tree

    }

    @Override
    public void getTopScorer(Player player) {
        //go to the player trees rankings list and get the head

    }

    @Override
    public void getTopScorerInFaculty(int faculty_id, Player player) {
        // go to the team tree, find the team tehn get the rankings list for that team and put it in player

    }

    @Override
    public void getTopKFaculties(ArrayList<Faculty> faculties, int k, boolean ascending) {
        //go to the faculty rankings list and insert to the array
        //depending on ascending insert to the list in the right order

    }

    @Override
    public void getTopKScorers(ArrayList<Player> players, int k, boolean ascending) {
        // go to all players goal rankings list and insert to the list

    }

    @Override
    public void getTheWinner(Faculty faculty) {
        //go to the team rankings and pput the head in faculty

    }

    ///TODO - add below your own variables and methods
}
