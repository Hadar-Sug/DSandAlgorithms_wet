import java.util.ArrayList;

public class TechnionTournament implements Tournament{

    //------------members--------
    //for all of these, the key attribute will be goals/points so the main comparisonis by key(=goals/points)
    // and secondary by val (=ID)
    TwoThreeTree<Player> PlayersByGoals; //tree of all players sorted by goals
    TwoThreeTree<TwoThreeTree<Player>> FacultiesTreesGoals; // tree of faculties sorted by points, each leaf is a tree for its respective team

    //for all of these, the key attribute will be 0 so the only comparison will be by the ID
    TwoThreeTree<Player> playersByID; // tree of all players sorted by ID
    TwoThreeTree<TwoThreeTree<Player>> FacultiesTreesByID; //  tree of all faculties sorted by ID, each node leaf is a tree for its respective team
    TwoThreeTree<Player> FreeAgents;

    //----------------------------


    //--------------methods--------

    /**
     * intialize all our trees and also create the free agents tree
     * runtime: O(1), only initializing.
     * and inserting to an empty tree is O(1)
     */
    TechnionTournament(){};

    @Override
    public void init() {
        this.FacultiesTreesGoals = new TwoThreeTree<>();
        this.FacultiesTreesByID = new TwoThreeTree<>();
        this.PlayersByGoals = new TwoThreeTree<>();
        this.playersByID = new TwoThreeTree<>();
        this.FreeAgents = new TwoThreeTree<>();
        // create a free agents team without a twin, so it doesn't affect the rankings
        Faculty freeAgentsFaculty = new Faculty(-1,"freeAgents");
        TwoThreeTree<Player> freeAgentsTree = new TwoThreeTree<>();
        Leaf<TwoThreeTree<Player>> freeAgentsLeaf = new Leaf<>(freeAgentsTree,0,-1); //decide what to do with val
        // TODO: double check this part
        // shouldnt be a problem
        freeAgentsLeaf.setTwin(null);
        FacultiesTreesByID.insert(freeAgentsLeaf);
    }

    /**
     * create two trees, one for player ID, one for goals.
     * the tree that holds info about the goals also holds the info about the team ranking
     * runtime: O(logn)
     * initializing leaves and trees O(1)
     * inserting to each tree is O(logn)
     * @param faculty faculty to be added
     */
    @Override
    public void addFacultyToTournament(Faculty faculty) {

        int points = 0;
        int faculty_id = faculty.getId();
        String faculty_name = faculty.getName();
        TwoThreeTree<Player> newFacultyTreeID = new TwoThreeTree<>(); //player tree ordered by player ID
        TwoThreeTree<Player> newFacultyTreeGoals = new TwoThreeTree<>(); // player tree ordered by Goals
        Leaf<TwoThreeTree<Player>> newFacultyID = new Leaf<>(newFacultyTreeID,0,faculty_id,faculty_name);
        Leaf<TwoThreeTree<Player>> newFacultyPoints = new Leaf<>(newFacultyTreeGoals,points,faculty_id,faculty_name);
        newFacultyID.setTwin(newFacultyPoints);
        this.FacultiesTreesByID.insert(newFacultyID);
        this.FacultiesTreesGoals.insert(newFacultyPoints);
    }

    /**
     *first find the team Leaf(log(n))
     * then get the twin, so we can delete and update the team standings O(1)
     * iterate over players in the team and insert to free agents tree O(11*logm)
     * delete both leaves (O(logn))
     * runtime: O(logn+logm)
     * @param faculty_id faculty to be removed
     */
    @Override
    public void removeFacultyFromTournament(int faculty_id) {
        Leaf<TwoThreeTree<Player>> facultyLeafID = getTeamLeaf(faculty_id); //leaf of the team by ID
        Leaf<TwoThreeTree<Player>> facultyLeafPoints = facultyLeafID.getTwin(); // leaf of the team by goals
        TwoThreeTree<Player> facultyTreeID = facultyLeafID.getMetaData(); // get the team tree

        for (int i = 1; i <= 11; i++) {
            Leaf<Player> currentPlayer = facultyTreeID.selectKthLeaf(facultyTreeID.getRoot(),i);
            Leaf<Player> currentPlayerCopy = currentPlayer.copyLeaf(); //create a copy to be added to the freeAgents tree
            if (currentPlayerCopy != null)
                FreeAgents.insert(currentPlayerCopy);
        }
        FacultiesTreesByID.Delete(facultyLeafID);
        FacultiesTreesGoals.Delete(facultyLeafPoints);
    }

    /**
     * first find the team Leaf (log(n))
     * create 4 leaves for the 4 trees the player node exists in(teamID,TeamGoals,AllID,AllGoals) O(1)
     * insert to team trees O(1)
     * insert to players trees O(logm)
     * runtime: O(logn+logm)
     * @param faculty_id faculty we're adding the player to
     * @param player player to be added
     */
    @Override
    public void addPlayerToFaculty(int faculty_id,Player player) {
        int goals = 0;
        Leaf<TwoThreeTree<Player>> facultyTreeID = getTeamLeaf(faculty_id); //get the team leaf by ID
        Leaf<TwoThreeTree<Player>> facultyTreeGoals = facultyTreeID.getTwin(); // get the team leaf by goals
        Leaf<Player> newPlayerIDLeafTeam = new Leaf<>(player,0,player.getId(),player.getName()); // create a leaf for the new player
        Leaf<Player> newPlayerGoalsLeafTeam = new Leaf<>(player,goals,player.getId(),player.getName());
        Leaf<Player> newPlayerIDLeafAll = new Leaf<>(player,0,player.getId(),player.getName()); // create a leaf for the new player
        Leaf<Player> newPlayerGoalsLeafAll = new Leaf<>(player,goals,player.getId(),player.getName());// same but for goals
        newPlayerIDLeafTeam.setTwin(newPlayerGoalsLeafTeam);  // set the twin leaf
        newPlayerIDLeafAll.setTwin(newPlayerGoalsLeafAll);
        facultyTreeID.getMetaData().insert(newPlayerIDLeafTeam); // add player by ID to faculty
        facultyTreeGoals.getMetaData().insert(newPlayerGoalsLeafTeam);//add player by goals to faculty
        playersByID.insert(newPlayerIDLeafAll); // add player by ID to all players tree
        PlayersByGoals.insert(newPlayerGoalsLeafAll); //add player by goals to all players tree
    }

    /**
     * first get the team Leaf O(logn)
     * get the player leaf from both team trees O(1)
     * delete player from both team trees O(1)
     * insert to free agents tree O(logm)
     * runtime: O(logm+logn)
     * @param faculty_id faculty we're removing the player from
     * @param player_id player to be removed
     */
    @Override
    public void removePlayerFromFaculty(int faculty_id, int player_id) {
        Leaf<TwoThreeTree<Player>> facultyLeafID = getTeamLeaf(faculty_id); //get the team leaf by ID
        Leaf<TwoThreeTree<Player>> facultyLeafGoals = facultyLeafID.getTwin();// get the team goal leaf
        Leaf<Player> playerLeafIDTeam = getPlayerLeaf(player_id,facultyLeafID.getMetaData()); //teamId player leaf
        Leaf<Player> playerLeafGoalsTeam = playerLeafIDTeam.getTwin(); // teamGoals player leaf
        Leaf<Player> playerLeafIDTeamCopy = playerLeafIDTeam.copyLeaf(); // make a copy before removing so we can add to the free agents
        facultyLeafID.getMetaData().Delete(playerLeafIDTeam); // delete
        facultyLeafGoals.getMetaData().Delete(playerLeafGoalsTeam);
        FreeAgents.insert(playerLeafIDTeamCopy); // then insert as freeAgent
    }

    /**
     * first get the team Leaves O(logn)
     * depending on winner, remove the team from points tree O(logn)
     * (making sure we copy the team object cause the deletion wil delete the object itself)
     * then add points to the team O(1)
     * and insert the updated team back in O(logn)
     * for each team, iterate over the team goals,
     * remove the player from the goal treeS(!) (O(logm))
     * (again making sure we make a copy)
     * then insert back in O(logm)
     * runtime O(logn + klogm)
     * @param faculty_id1 team 1
     * @param faculty_id2 team 2
     * @param winner winner status, 0 if tied, 1 if team 1 wins, 2 if team 2 wins
     * @param faculty1_goals array of goals for team 1
     * @param faculty2_goals array of goals for team 2
     */
    @Override
    public void playGame(int faculty_id1, int faculty_id2, int winner,
                         ArrayList<Integer> faculty1_goals, ArrayList<Integer> faculty2_goals) {
        //find the team/s, add points based on winner parameter, update the teampoints tree(points were added)
        //then iterate over team player tree and add goals, updating the goals tree per team and the entire player tree
        // consequentially also each team goals tree
        Leaf<TwoThreeTree<Player>> faculty1Leaf = getTeamLeaf(faculty_id1); //ID leaf of faculty team
        Leaf<TwoThreeTree<Player>> faculty2Leaf = getTeamLeaf(faculty_id2); //same for team 2
        if (winner ==1){
            addPoints(faculty1Leaf.getTwin(),3); //adding to twin cause he holds the points
        } else if (winner==2) {
            addPoints(faculty2Leaf.getTwin(),3);
        } else {
            addPoints(faculty1Leaf.getTwin(),1);
            addPoints(faculty2Leaf.getTwin(),1);
        }
        for (Integer player_id:faculty1_goals) {
            addGoal(player_id,faculty1Leaf);
        }
        for (Integer player_id:faculty2_goals) {
            addGoal(player_id,faculty2Leaf);
        }
    }


    /**
     * get the rankings List from the player goals tree
     * then insert the player info to player
     * runtime: O(1)
     * @param player insert the top scorer into this ibject
     */
    //TODO: make sure tail is top scorer
    @Override
    public void getTopScorer(Player player) {
        int player_id = PlayersByGoals.getRankings().getTail().getKeyVal().getVal();
        String player_name = PlayersByGoals.getRankings().getTail().getName();//make sure the best player is in the tail
        player.setId(player_id);
        player.setName(player_name);
    }

    /**
     * first find the team Leaf (O(logn))
     * then get the rankings List and insert the top scorer info into player
     * runtime: O(logn)
     * @param faculty_id find the top scorer in this faculty
     * @param player update the top scorer info into this object
     */
    @Override
    public void getTopScorerInFaculty(int faculty_id, Player player) {
        // go to the team tree, find the team tehn get the rankings list for that team and put it in player
        Leaf<TwoThreeTree<Player>> facultyLeaf = getTeamLeaf(faculty_id);
        TwoThreeTree<Player> facultyPlayers = facultyLeaf.getTwin().getMetaData(); //twin holds the goal info
        int topPlayerInFaculty_ID = facultyPlayers.getRankings().getTail().getKeyVal().getVal();
        String topPlayerInFaculty_Name = facultyPlayers.getRankings().getTail().getName();
        player.setId(topPlayerInFaculty_ID);
        player.setName(topPlayerInFaculty_Name);
    }

    /**
     * get the rankings list O(1)
     * then iterate over the list and insert according the order requested
     * runtime O(k)
     * @param faculties list we'll insert the k top faculties
     * @param k k top faculties
     * @param ascending how to order the rankings in the array
     */
    //TODO: check null?
    @Override
    public void getTopKFaculties(ArrayList<Faculty> faculties, int k, boolean ascending) {
        ListNode current = FacultiesTreesGoals.getRankings().getTail();
        if (ascending) {
            for (int i = k-1; i >=0; i--) { // check null?
                Faculty faculty = new Faculty(current.getKeyVal().getVal(), current.getName());
                faculties.set(i,faculty);
                current = current.getPrev();
            }
        }
        else{
            for (int i = 0; i < k; i++) {
                Faculty faculty = new Faculty(current.getKeyVal().getVal(), current.getName());
                faculties.set(i,faculty);
                current = current.getPrev();
            }
        }
    }

    /**
     * same as getTopKFaculties
     * runtime: O(K)
     * @param players insert the top k players here
     * @param k k  top players
     * @param ascending order in which we insert the players into the array
     */
    @Override
    public void getTopKScorers(ArrayList<Player> players, int k, boolean ascending) {
        // go to all players goal rankings list and insert to the list
        ListNode current = PlayersByGoals.getRankings().getTail();
        if (ascending) {
            for (int i = k-1; i >=0; i--) { // check null?
                Player player = new Player(current.getKeyVal().getVal(), current.getName());
                players.set(i,player);
                current = current.getPrev();
            }
        }
        else{
            for (int i = 0; i < k; i++) {
                Player player = new Player(current.getKeyVal().getVal(), current.getName());
                players.set(i,player);
                current = current.getPrev();
            }
        }
    }

    /**
     * same as getTopScorrer
     * runtime: O(1)
     * @param faculty insert the info of the winning faculty here
     */
    @Override
    public void getTheWinner(Faculty faculty) {
        //go to the team rankings and pput the head in faculty
        int winnerID =FacultiesTreesGoals.getRankings().getTail().getKeyVal().getVal();
        String winnerName =FacultiesTreesGoals.getRankings().getTail().getName();
        faculty.setId(winnerID);
        faculty.setName(winnerName);
    }

    ///our own variables and methods

    /**
     * @param faculty_id id of faculty we're searching for
     * @return the respective leaf in the Faculty tree of tree's
     */
    private Leaf<TwoThreeTree<Player>> getTeamLeaf(int faculty_id){
        KeyVal FacultyKeyVal = new KeyVal(0,faculty_id);
        return FacultiesTreesByID.Search(FacultiesTreesByID.getRoot(), FacultyKeyVal);
    }

    //TODO: fix to get the correct leaf depending on the tree
    private Leaf<Player> getPlayerLeaf(int playerID, TwoThreeTree<Player> playerTree){
        KeyVal PlayerKeyVal = new KeyVal(0,playerID);
        return playersByID.Search(playerTree.getRoot(),PlayerKeyVal);
    }

    /**
     * removes a leaf from the points tree, adds points to the key, which represents the points
     * and then insert it back in
     * @param facultyLeafPoints leaf of the team we're updating points of
     * @param k points to be added
     */
    private void addPoints(Leaf<TwoThreeTree<Player>> facultyLeafPoints, int k){
        Leaf<TwoThreeTree<Player>> copy = facultyLeafPoints.copyLeaf();
        FacultiesTreesGoals.Delete(facultyLeafPoints);
        copy.setKeyVal(facultyLeafPoints.getKey()+k, facultyLeafPoints.getVal()); //add k points to the key, which stands for the points
        FacultiesTreesGoals.insert(copy);
    }

    //TODO: copying the object maybe fucks everything up, when deleting the second time, the oarent doesnt seem to be the correct one
    /**
     * remove player from team tree and all player tree and then update his goals(key), then insert him back in
     * @param player_id player we're adding goals to
     * @param facultyPlayersIDLeaf team tree that he/she/they/them belongs to
     */
    private void addGoal(Integer player_id, Leaf<TwoThreeTree<Player>> facultyPlayersIDLeaf){
        Leaf<Player> currentPlayerIDTeam = getPlayerLeaf(player_id,facultyPlayersIDLeaf.getMetaData()); //finds the player in the team playerID tree
        Leaf<Player> currentPlayerIDAll = getPlayerLeaf(player_id,playersByID);
        Leaf<Player> currentPlayerGoalsTeam = currentPlayerIDTeam.getTwin();//gets the playersGoal object
        Leaf<Player> currentPlayerGoalsAll = currentPlayerIDAll.getTwin();//gets the playersGoal object
        Leaf<Player> currentPlayerGoalsTeamCopy = currentPlayerGoalsTeam.copyLeaf();
        Leaf<Player> currentPlayerGoalsAllCopy = currentPlayerGoalsAll.copyLeaf();
        facultyPlayersIDLeaf.getTwin().getMetaData().Delete(currentPlayerGoalsTeam);// remove player from Goals tree
        PlayersByGoals.Delete(currentPlayerGoalsAll); // so delete from team tree and all player tree, by goals
        currentPlayerGoalsTeamCopy.setKeyVal(currentPlayerGoalsTeamCopy.getKey()+1,currentPlayerGoalsTeamCopy.getVal()); //update goals for the player
        currentPlayerGoalsAllCopy.setKeyVal(currentPlayerGoalsAllCopy.getKey()+1,currentPlayerGoalsAllCopy.getVal());
        facultyPlayersIDLeaf.getTwin().getMetaData().insert(currentPlayerGoalsTeamCopy);//insert back in
        PlayersByGoals.insert(currentPlayerGoalsAllCopy);
    }
}

