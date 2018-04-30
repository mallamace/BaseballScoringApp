package com.doggydish.baseballscoringapp;

import android.app.ActionBar;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int homeScore;
    int guestScore;
    int currInning; // 1-9 innings
    boolean currTeam = false; // false = guest team, and true = home team

    int batterStrike; // 0-3 strikes
    int pitcherBall; // 0-4 balls
    int currOuts; // 0-3 outs

    boolean baseTracker[] = new boolean[3]; //track base positions 1-3

    TextView inningText;
    TextView inningTextTwo;
    TextView homeScoreText;
    TextView guestScoreText;
    TextView batterStrikeText;
    TextView pitcherBallText;
    TextView outsText;
    ImageView baseImage;
    ImageView teamImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inningText = (TextView)findViewById(R.id.inningText);
        inningTextTwo = (TextView)findViewById(R.id.inningTextTwo);
        homeScoreText = (TextView) findViewById(R.id.homeScoreText);
        guestScoreText = (TextView) findViewById(R.id.guestScoreText);
        batterStrikeText = (TextView) findViewById(R.id.batterStrikeText);
        pitcherBallText = (TextView) findViewById(R.id.pitcherBallText);
        outsText = (TextView) findViewById(R.id.outsText);
        baseImage = (ImageView) findViewById(R.id.baseImage);
        teamImage = (ImageView) findViewById(R.id.teamImage);

        startGame(null);
    }

    public void startGame(View v) {

        currInning = 1; // set the first inning as
        currTeam = false; // set to false, we are in top inning to start
        homeScore = 0;
        guestScore = 0;
        batterStrike = 0; // reset strikes
        pitcherBall = 0; // reset balls
        currOuts = 0; // reset outs

        //activityLog.clear(); Phase 2 - empty the activity log
        inningText.setText(topBottom()+ " " + currInning+getSuffix(currInning)); //set Inning Text
        inningTextTwo.setText(topBottom()+ " " + currInning+getSuffix(currInning)); //set Inning Text
        teamImage.setImageResource(R.drawable.guestteam);
        baseImage.setImageResource(R.drawable.basesempty);
        guestScoreText.setText(String.valueOf(guestScore));
        homeScoreText.setText(String.valueOf(homeScore));
    }


    public void inningReset(){

        batterStrike = 0; // reset strikes
        pitcherBall = 0; // reset balls
        currOuts = 0; // reset outs

        batterStrikeText.setText(String.valueOf(batterStrike));
        pitcherBallText.setText(String.valueOf(pitcherBall));
        outsText.setText(String.valueOf(currOuts));
        baseImage.setImageResource(R.drawable.basesempty);

        clearBases();
    }

    /**
     *
     * @param v
     */
    public void advanceInning(View v) {

        inningReset();

        if (currTeam) {  // if it's the bottom inning, advance to next inning and update text

            currInning++;
        }

        if (currTeam) { // switch from guest to home
            currTeam = false;
            teamImage.setImageResource(R.drawable.guestteam);
        } else {
            currTeam = true;
            teamImage.setImageResource(R.drawable.teamlogoangels);
        }

        inningText.setText(topBottom()+ " " + currInning+getSuffix(currInning)); //set Inning Text in Top
        inningTextTwo.setText(topBottom()+ " " + currInning+getSuffix(currInning)); //set Inning Text in Bottom
    }


    public String topBottom (){ // guests play in top, home plays in bottom

        if (currTeam) {
            return "B"; } // home
        else {
            return "T"; } //guest
    }

    /**
     *
     * @param v
     */
    public void addStrike (View v){

        batterStrike++;

        batterStrikeText.setText(String.valueOf(batterStrike));

        if (batterStrike >= 3) {currOuts++; batterStrike = 0; pitcherBall=0; outsText.setText(String.valueOf(currOuts));} // if 3 strikes advance to next player, reset strikes and balls

       checkOuts(); //check if 3 outs, if so end inning

    }

    public void checkOuts(){

        if (currOuts >= 3){ advanceInning(null); } //check if 3 outs, if so end inning
    }

    /**
     *
     * @param v
     */
    public void addBall (View v){

        pitcherBall++;
        pitcherBallText.setText(String.valueOf(pitcherBall));

        if (pitcherBall >= 4) { // if 4 balls advance to next player, reset strikes and balls

            batterStrike = 0;
            pitcherBall=0;

            advancePlayerToFirst();
            pitcherBallText.setText(String.valueOf(pitcherBall));
        }
    }

    /**
     *
     * @param v
     */
    public void addOut(View v){

        currOuts++;
        outsText.setText(String.valueOf(currOuts));

        resetPlayer(); // reset strikes and balls for next player
        checkOuts(); //check if 3 outs, if so end inning
    }


    public void advancePlayerToFirst(){ //put player on base and move any other players up


        resetPlayer(); // reset strikes and balls for next player

        if(baseTracker[2] && baseTracker[1] && baseTracker[0]) { // if bases are loaded add score to current team

            // add score to current team
            addScoreCurr(null);

            return;
        }

        if(baseTracker[0] && baseTracker[1]) { // if player on first and second, load up bases

            baseImage.setImageResource(R.drawable.bases123);
            baseTracker[2] = true; // advance a player to 3rd base
            return;
        }

        if(baseTracker[0]) { // if player on 1st only

            baseImage.setImageResource(R.drawable.bases12);
            baseTracker[1] = true; // advance a player to 2nd
            return;
        }


        baseImage.setImageResource(R.drawable.bases1);
        baseTracker[0] = true; // add player to first base
    }


    public void resetPlayer(){  //reset player count strikes

        batterStrike = 0; // reset strikes
        pitcherBall = 0; // reset balls
        batterStrikeText.setText(String.valueOf(batterStrike));
        pitcherBallText.setText(String.valueOf(pitcherBall));

        // Phase 2 - update players name from roster

    }

    /**
     *
     * @param v
     */
    public void addScoreCurr(View v){ // Add a point to the current team

        batterStrike = 0;
        pitcherBall=0;

        if(currTeam) { homeScore++; } else { guestScore++; }

        refreshTeamScore();
    }

    /**
     *
     * @param v
     */
    public void addSingleRun(View v){ // Advance Runner to First Base

        advancePlayerToFirst();
    }

    /**
     *
     * @param v
     */
    public void addHomeRun(View v){

        batterStrike = 0; // reset strikes
        pitcherBall = 0; // reset balls

        batterStrikeText.setText(String.valueOf(batterStrike));
        pitcherBallText.setText(String.valueOf(pitcherBall));
        baseImage.setImageResource(R.drawable.basesempty);

        if(currTeam) { homeScore = homeScore + 1 + howMany(); } else { guestScore = guestScore + 1 + howMany(); }

        clearBases();
        refreshTeamScore();
    }

    public void clearBases(){ // Clear the bases of players, set boolean baseTracker array all to false

        Arrays.fill(baseTracker, false); //clear the bases
        baseImage.setImageResource(R.drawable.basesempty);
    }

    /**
     *
     * @return
     */
    public int howMany(){ // get a count of players on base

        int totalPlayersOnBase = 0;

        if(baseTracker[0]) { totalPlayersOnBase++; }
        if(baseTracker[1]) { totalPlayersOnBase++; }
        if(baseTracker[2]) { totalPlayersOnBase++; }

        return totalPlayersOnBase;
    }

    public void refreshTeamScore(){

        guestScoreText.setText(String.valueOf(guestScore));
        homeScoreText.setText(String.valueOf(homeScore));
    }

    /**
     *
     * @param n
     * @return
     */
    public String getSuffix(int n){

        if (n<1){ return getString(R.string.thText);}

        switch (n % 100){
            case 1: return getString(R.string.stText);
            case 2: return getString(R.string.ndText);
            case 3: return getString(R.string.rdText);
            default: return getString(R.string.thText);
        }
    }
}
