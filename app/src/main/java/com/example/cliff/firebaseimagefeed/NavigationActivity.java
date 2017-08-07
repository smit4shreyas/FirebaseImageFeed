package com.example.cliff.firebaseimagefeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";

    public FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    public FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setTitle("Users");

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    makeToast("Signed in 2");
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                    makeToast("Signed out 2");
                }
            }
        };

        // Load initial fragment into view
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, new UsersFragment()).commit();

        // Setup BottomNavigationView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Setup Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle(null);

        /*----------------- Testing --------------
        // Observe the structure of these objects in the database
        // Test 1: ArrayList<CustomObject>
        FirebaseDatabase testDatabase = FirebaseDatabase.getInstance();
        ArrayList<User> testArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testArrayList.add(new User("boga", "noga"));
        }
        DatabaseReference testDatabaseReference = testDatabase.getReference("test");
        testDatabaseReference.setValue(testArrayList);

        // Test 2: ArrayList<String>
        ArrayList<String> testTwoArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testTwoArrayList.add("boga");
        }
        DatabaseReference testTwoDatabaseReference = testDatabase.getReference("testTwo");
        testTwoDatabaseReference.setValue(testTwoArrayList);
        // ----------------------------------------*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.users:
                    fragment = new UsersFragment();
                    setTitle("Users");
                    break;
                case R.id.upload:
                    fragment = new UploadFragment();
                    setTitle("Upload");
                    break;
                case R.id.current_user:
                    fragment = new CurrentUserFragment();
                    setTitle("Current user");
                    break;
            }

            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content, fragment).commit();
                return true;
            }
            return false;
        }

    };

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
