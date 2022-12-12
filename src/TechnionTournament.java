import java.util.ArrayList;
import java.util.LinkedList;

public class TechnionTournament implements Tournament{


    //for all of these, the key attribute will be goals/points so the main comparisonis by key(=goals/points)
    // and secondary by val (=ID)
    TwoThreeTree<Player> PlayersByGoals; //tree of all players sorted by goals
    TwoThreeTree<TwoThreeTree<Player>> FacultiesByPoints; // tree of faculties sorted by points, each leaf is a tree for its respective team

    //for all of these, the key attribute will be 0 so the only comparison will be by the ID
    TwoThreeTree<Player> playersByID; // tree of all players sorted by ID
    TwoThreeTree<TwoThreeTree<Player>> FacultiesByID; //  tree of all faculties sorted by ID, each node leaf is a tree for its respective team
    TwoThreeTree<Player> FreeAgents;

    TechnionTournament(){};
    @Override
    public void init() {
        this.FacultiesByPoints = new TwoThreeTree<TwoThreeTree<Player>>();
        this.FacultiesByID = new TwoThreeTree<TwoThreeTree<Player>>();
        this.PlayersByGoals = new TwoThreeTree<Player>();
        this.playersByID = new TwoThreeTree<Player>();
        this.FreeAgents = new TwoThreeTree<Player>();
        // create a free agents team without a twin, so it doesn't affect the rankings
        Faculty freeAgentsFaculty = new Faculty(-1,"freeAgents");
        TwoThreeTree<Player> freeAgentsTree = new TwoThreeTree<>();
        Leaf<TwoThreeTree<Player>> freeAgentsLeaf = new Leaf<>(freeAgentsTree,0,-1); //decide what to do with val
        // TODO: double check this part
        freeAgentsLeaf.setTwin(null);
        FacultiesByID.insert(freeAgentsLeaf);
    }

    @Override
    public void addFacultyToTournament(Faculty faculty) {
        //check if it exists?
        int points = 0;
        int faculty_id = faculty.getId();
        TwoThreeTree<Player> newFacultyTreeID = new TwoThreeTree<>(); //maybe need to connect this
        TwoThreeTree<Player> newFacultyTreePoints = new TwoThreeTree<>(); // to this. maybe it happens because of the twin
        Leaf<TwoThreeTree<Player>> newFacultyID = new Leaf<>(newFacultyTreeID,0,faculty_id);
        Leaf<TwoThreeTree<Player>> newFacultyPoints = new Leaf<>(newFacultyTreePoints,points,faculty_id);
        newFacultyID.setTwin(newFacultyPoints);
        this.FacultiesByID.insert(newFacultyID);
        this.FacultiesByPoints.insert(newFacultyPoints);
    }


    @Override
    public void removeFacultyFromTournament(int faculty_id) {
        Leaf<TwoThreeTree<Player>> facultyLeafID = getLeaf(faculty_id);
        Leaf<TwoThreeTree<Player>> facultyLeafPoints = facultyLeafID.getTwin();
        TwoThreeTree<Player> facultyTreeID = facultyLeafID.getMetaData();

        for (int i = 1; i <= 11; i++) {
            Leaf<Player> currentPlayer = facultyTreeID.selectKthLeaf(facultyTreeID.getRoot(),i);
            if (currentPlayer != null)
                FreeAgents.insert(currentPlayer);
        }
        FacultiesByID.Delete(facultyLeafID);
        FacultiesByPoints.Delete(facultyLeafPoints);
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
        Leaf<TwoThreeTree<Player>> faculty1Leaf = getLeaf(faculty_id1);
        TwoThreeTree<Player> faculty1Players = faculty1Leaf.getMetaData();
        Leaf<TwoThreeTree<Player>> faculty2Leaf = getLeaf(faculty_id2);
        TwoThreeTree<Player> faculty2Players = faculty2Leaf.getMetaData();
        if (winner ==1){
            addPoints(faculty1Leaf,3);
        } else if (winner==2) {
            addPoints(faculty2Leaf,3);
        } else {
            addPoints(faculty1Leaf,1);
            addPoints(faculty2Leaf,1);
        }
        for (Integer player_id:faculty1_goals) {
            addGoal(player_id,faculty1Players);
            addGoal(player_id,playersByID);
        }
        for (Integer player_id:faculty2_goals) {
            addGoal(player_id,faculty2Players);
            addGoal(player_id,playersByID);
        }
    }



    @Override
    public void getTopScorer(Player player) {
        //go to the player trees rankings list and get the head
        Player topPlayer = PlayersByGoals.getRankings().getTail().getMetaData(); //make sure the best player is in the tail
        player.setId(topPlayer.getId());
        player.setName(topPlayer.getName());


    }

    @Override
    public void getTopScorerInFaculty(int faculty_id, Player player) {
        // go to the team tree, find the team tehn get the rankings list for that team and put it in player
        Leaf<TwoThreeTree<Player>> facultyLeaf = getLeaf(faculty_id);
        TwoThreeTree<Player> facultyPlayers = facultyLeaf.getMetaData();
        Player topPlayerInFaculty = facultyPlayers.getRankings().getTail().getMetaData();
        player.setId(topPlayerInFaculty.getId());
        player.setName(topPlayerInFaculty.getName());
    }

    @Override
    public void getTopKFaculties(ArrayList<Faculty> faculties, int k, boolean ascending) {
        //go to the faculty rankings list and insert to the array
        //depending on ascending insert to the list in the right order
        DoublyLinkedList<TwoThreeTree<Player>> facultyRankings = FacultiesByPoints.getRankings();
        if (ascending) {
            for (int i = k-1; i >=0; i++) {
                faculties.set(i, facultyRankings.getTail().getMetaData()); //figure out how to get the faculty
            }
        }
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

    /**
     * @param faculty_id id of faculty we're searching for
     * @return the respective leaf in the Faculty tree of tree's
     */
    private Leaf<TwoThreeTree<Player>> getLeaf(int faculty_id){
        KeyVal FacultyKeyVal = new KeyVal(0,faculty_id);
        return FacultiesByID.Search(FacultiesByID.getRoot(),FacultyKeyVal);
    }

    /**
     * removes a leaf from the points tree, adds points to the key, which represents the points
     * and then insert it back in
     * @param facultyLeaf leaf of the team we're updating points of
     * @param k points to be added
     */
    private void addPoints(Leaf<TwoThreeTree<Player>> facultyLeaf, int k){
        FacultiesByPoints.Delete(facultyLeaf);
        facultyLeaf.getTwin().setKey(facultyLeaf.getTwin().getKey()+k); //add k points to the key, which stands for the points
        FacultiesByPoints.insert(facultyLeaf);
    }

    /**
     * remove player from tree and then update his goals(key), then insert him back in
     * @param player_id player we're adding goals to
     * @param facultyPlayers team tree that he/she/they/them belongs to
     */
    private void addGoal(Integer player_id, TwoThreeTree<Player> facultyPlayers){
        KeyVal playerKeyVal = new KeyVal(0,player_id);
        Leaf<Player> currentPlayer = facultyPlayers.Search(facultyPlayers.getRoot(),playerKeyVal);
        facultyPlayers.Delete(currentPlayer);
        currentPlayer.getTwin().setKey(currentPlayer.getTwin().getKey()+1);
        facultyPlayers.insert(currentPlayer);
    }
}
