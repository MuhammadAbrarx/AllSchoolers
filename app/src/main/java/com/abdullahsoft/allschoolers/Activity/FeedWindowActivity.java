package com.abdullahsoft.allschoolers.Activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.abdullahsoft.allschoolers.R;


public class FeedWindowActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //this is the total type of contents for eg: only image,image+text,only text ,text+text etc
    long numberOfCards;

    //Layouts
    ConstraintLayout layoutConstraint_mainContainer;
    //Views
    /***Scroll Views***/
    ScrollView scrollview_contentDrawer;

    LayoutInflater layoutInflater;
    ViewGroup parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_window);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ////INITIALIZING ALL STUFF////

        //Initializing Layouts
        layoutConstraint_mainContainer = (ConstraintLayout) findViewById(R.id.layoutConstraint_mainContainer_dynamicScrollableCardsHolder);
        parent = (ConstraintLayout) layoutConstraint_mainContainer;
        //Initializing Views
        /***Scroll Views***/
        scrollview_contentDrawer = (ScrollView) findViewById(R.id.scrollview_root_dynamicScrollableCardsHolder);

        //Initializing Buttons


        //MODIFIED//
        //Generating some views or layouts

        // layoutInflater = this.getLayoutInflater();
        GenerateLayoutsOrViews();
    }

    private void GenerateLayoutsOrViews() {

        //while creating views there are two options, one is to inflate a view or create one

        ///All formats necessary to inflate different cardviews
        //test start edit/remove when done
        int numberOfFields = 5;
        int numberOfTextFields = 3;
        //test end start edit/remove when done


        /**Starting Generating the card/cards**/
        // CardView card = new CardView(this);
        // card.setLayoutParams(parent.getLayoutParams());


        ConstraintSet constraintSet = new ConstraintSet();
        TextView texts[] = new TextView[numberOfFields];
        for (int i = 0; i < numberOfTextFields; i++) {
            TextView textV = new TextView(this);
            // textV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textV.setText("Hello I am a dynamic text  " + i);
            // textV.setTextSize(25);
            textV.setId(1000 + i);
            // textV.setLayoutParams(params);
            layoutConstraint_mainContainer.addView(textV);
            constraintSet.clone(layoutConstraint_mainContainer);
            if (i==0)
            {
                /***Should Only Run the first time when i=0***/
                constraintSet.connect(textV.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 20);
                constraintSet.connect(textV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 20);
                constraintSet.connect(textV.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 20);
                constraintSet.connect(textV.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 20);
            }
            else
            {

                // ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                //         ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                // textV.setLayoutParams(params);

                // constraintSet.constrainDefaultHeight(textV.getId(), 200);

                //constraintSet.connect (
                //                      viewTop/Bottom/Start/End Constraint id ,
                //                      constraintSet.op/Bottom/Start/End,
                //                      ctoViewOf Top/Bottom/Start/End Constraint id,
                //                      ConstraintSet.op/Bottom/Start/End, margin
                //                      );

                constraintSet.connect(textV.getId(), ConstraintSet.TOP, texts[i - 1].getId(), ConstraintSet.BOTTOM, 8);
                constraintSet.connect(textV.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 8);
                constraintSet.connect(textV.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 8);

                //baseline
                // constraintSet.connect(textV.getId(),ConstraintSet.BASELINE,textView1.id,ConstraintSet.BASELINE,0)

                //trying out animations
                // constraintSet.load(this,R.layout.dynamic_scrollable_cards_holder);
                // TransitionManager.beginDelayedTransition(layoutConstraint_mainContainer);
            }
            constraintSet.constrainWidth(textV.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.applyTo((ConstraintLayout) layoutConstraint_mainContainer);
            // card.addView(textV);
            texts[i] = textV;
        }

        /**Ending Generating the card/cards**/
        /*Adding card to View*/
        //an attempt to fill the parent with card
        // parent.addView(card);
        // card.ad
//        ConstraintLayout constraintLayout = new ConstraintLayout(this);
//        constraintLayout.
//        card.addView(constraintLayout);


        // CardView cardView = (CardView) layoutInflater.inflate(R.layout.card_text_holder, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_image, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageLeft_textRight, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageLeft_title_textRight, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageRight_textLeft, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageRight_title_textLeft, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageTop_textBottom, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageTop_title_textBottom, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageBottom_textTop, parent, false);
//        CardView cardView = layoutInflater.inflate(R.layout.card_imageBottom_title_textTop, parent, false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
