package com.abdullahsoft.allschoolers.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.abdullahsoft.allschoolers.R;
import com.facebook.FacebookSdk;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;


import static android.Manifest.permission.READ_CONTACTS;

/**
* A login screen that offers login via email/password.
*/
public class LoginCommonActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

   /**
    * Id to identity READ_CONTACTS permission request.
    */
   private static final int REQUEST_READ_CONTACTS = 0;

    private static final String TAG = "LoginCommonActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
   /*private static final String[] DUMMY_CREDENTIALS = new String[]{
           "foo@example.com:hello", "bar@example.com:world"
   };*/
   /**
    * Keep track of the login task to ensure we can cancel it if requested.
    */
   private UserLoginTask mAuthTask = null;

   // UI references.
   AutoCompleteTextView mEmailView;
   EditText mPasswordView;
   View mProgressView;
   View scrollView_form_signin;

   //for animation
   //  Button btnsub;
    Animation uptodown,downtoup;

    ////Views////
    ///Image Views///
    ImageView imageView_logo;



   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_login_common);
       //Facebook sdk has to be initialized before setContentView as Documentation states
       FacebookSdk.sdkInitialize(this);
       setContentView(R.layout.test_login);

       // Views
       mEmailView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_email);
       mPasswordView = (EditText) findViewById(R.id.edittext_password);
       mProgressView = findViewById(R.id.progressbar_login);
       scrollView_form_signin = findViewById(R.id.scrollview_signin_form);
       imageView_logo = findViewById(R.id.imageView_logo);

       //Buttons
       Button mEmailSignInButton = (Button) findViewById(R.id.button_sign_in);
       // btnsub = (Button)findViewById(R.id.buttonsub);

       ////Animations////
       //Animating the background
       ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout_root_login);
       AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
       animationDrawable.setEnterFadeDuration(2000);
       animationDrawable.setExitFadeDuration(4000);
       animationDrawable.start();

       uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
       downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
       imageView_logo.setAnimation(uptodown);
       scrollView_form_signin.setAnimation(downtoup);

       // [START config_signin]
       // Configure Google Sign In
       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
               .requestIdToken(getString(R.string.default_web_client_id))
               .requestEmail()
               .build();
       // [END config_signin]



       populateAutoComplete();

       // Listeners
       mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
               if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                   attemptLogin();
                   return true;
               }
               return false;
           }
       });

       mEmailSignInButton.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View view) {
               attemptLogin();
           }
       });


       //initialization
       // Initialize Facebook Login button
       // mCallbackManager = CallbackManager.Factory.create();
       // LoginButton loginButton = findViewById(R.id.button_facebook_login);
       // loginButton.setReadPermissions("email", "public_profile");
       // loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
       //     @Override
       //     public void onSuccess(LoginResult loginResult) {
       //         // Log.d(TAG, "facebook:onSuccess:" + loginResult);
       //         handleFacebookAccessToken(loginResult.getAccessToken());
       //     }
       //
       //     @Override
       //     public void onCancel() {
       //         // Log.d(TAG, "facebook:onCancel");
       //         // ...
       //     }
       //
       //     @Override
       //     public void onError(FacebookException error) {
       //         // Log.d(TAG, "facebook:onError", error);
       //         // ...
       //     }
       // });




   }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }
    // [END on_start_check_user]

   private void populateAutoComplete() {
       if (!mayRequestContacts()) {
           return;
       }

       getLoaderManager().initLoader(0, null, this);
   }

   private boolean mayRequestContacts() {
       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
           return true;
       }
       if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
           return true;
       }
       if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
           Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                   .setAction(android.R.string.ok, new View.OnClickListener() {
                       @Override
                       @TargetApi(Build.VERSION_CODES.M)
                       public void onClick(View v) {
                           requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                       }
                   });
       } else {
           requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
       }
       return false;
   }

   /**
    * Callback received when a permissions request has been completed.
    */
   @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
       if (requestCode == REQUEST_READ_CONTACTS) {
           if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               populateAutoComplete();
           }
       }
   }


   /**
    * Attempts to sign in or register the account specified by the login form.
    * If there are form errors (invalid email, missing fields, etc.), the
    * errors are presented and no actual login attempt is made.
    */
   private void attemptLogin() {
       if (mAuthTask != null) {
           return;
       }

       // Reset errors.
       mEmailView.setError(null);
       mPasswordView.setError(null);

       // Store values at the time of the login attempt.
       String email = mEmailView.getText().toString();
       String password = mPasswordView.getText().toString();

       boolean cancel = false;
       View focusView = null;

       // Check for a valid password, if the user entered one.
       if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
           mPasswordView.setError(getString(R.string.error_invalid_password));
           focusView = mPasswordView;
           cancel = true;
       }

       // Check for a valid email address.
       if (TextUtils.isEmpty(email)) {
           mEmailView.setError(getString(R.string.error_field_required));
           focusView = mEmailView;
           cancel = true;
       } else if (!isEmailValid(email)) {
           mEmailView.setError(getString(R.string.error_invalid_email));
           focusView = mEmailView;
           cancel = true;
       }

       if (cancel) {
           // There was an error; don't attempt login and focus the first
           // form field with an error.
           focusView.requestFocus();
       } else {
           // Show a progress spinner, and kick off a background task to
           // perform the user login attempt.
           showProgress(true);
           mAuthTask = new UserLoginTask(email, password);
           mAuthTask.execute((Void) null);
       }
   }

   private boolean isEmailValid(String email) {
       //TODO: Replace this with your own logic
       return email.contains("@");
   }

   private boolean isPasswordValid(String password) {
       //TODO: Replace this with your own logic
       return password.length() > 4;
   }

   /**
    * Shows the progress UI and hides the login form.
    */
   @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
   private void showProgress(final boolean show) {
       // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
       // for very easy animations. If available, use these APIs to fade-in
       // the progress spinner.
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
           int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

           scrollView_form_signin.setVisibility(show ? View.GONE : View.VISIBLE);
           scrollView_form_signin.animate().setDuration(shortAnimTime).alpha(
                   show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
               @Override
               public void onAnimationEnd(Animator animation) {
                   scrollView_form_signin.setVisibility(show ? View.GONE : View.VISIBLE);
               }
           });

           mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
           mProgressView.animate().setDuration(shortAnimTime).alpha(
                   show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
               @Override
               public void onAnimationEnd(Animator animation) {
                   mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
               }
           });
       } else {
           // The ViewPropertyAnimator APIs are not available, so simply show
           // and hide the relevant UI components.
           mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
           scrollView_form_signin.setVisibility(show ? View.GONE : View.VISIBLE);
       }
   }

   @Override
   public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
       return new CursorLoader(this,
               // Retrieve data rows for the device user's 'profile' contact.
               Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                       ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

               // Select only email addresses.
               ContactsContract.Contacts.Data.MIMETYPE +
                       " = ?", new String[]{ContactsContract.CommonDataKinds.Email
               .CONTENT_ITEM_TYPE},

               // Show primary email addresses first. Note that there won't be
               // a primary email address if the user hasn't specified one.
               ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
   }

   @Override
   public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
       List<String> emails = new ArrayList<>();
       cursor.moveToFirst();
       while (!cursor.isAfterLast()) {
           emails.add(cursor.getString(ProfileQuery.ADDRESS));
           cursor.moveToNext();
       }

       addEmailsToAutoComplete(emails);
   }

   @Override
   public void onLoaderReset(Loader<Cursor> cursorLoader) {

   }

   private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
       //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
       ArrayAdapter<String> adapter =
               new ArrayAdapter<>(LoginCommonActivity.this,
                       android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

       mEmailView.setAdapter(adapter);
   }


   private interface ProfileQuery {
       String[] PROJECTION = {
               ContactsContract.CommonDataKinds.Email.ADDRESS,
               ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
       };

       int ADDRESS = 0;
       int IS_PRIMARY = 1;
   }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showing progress dialogue here
        // showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginCommonActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hiding progress dialogue or progressbar
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        // hideProgressDialog();
        // if (user != null) {
        //     mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
        //     mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
        //
        //     findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        //     findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        // } else {
        //     mStatusTextView.setText(R.string.signed_out);
        //     mDetailTextView.setText(null);
        //
        //     findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        //     findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        // }
    }






                        /////*** Inner Classes are mentioned down below***/////

   /**
    * Represents an asynchronous login/registration task used to authenticate
    * the user.
    */
   public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

       private final String mEmail;
       private final String mPassword;

       UserLoginTask(String email, String password) {
           mEmail = email;
           mPassword = password;
       }

       @Override
       protected Boolean doInBackground(Void... params) {
           // TODO: attempt authentication against a network service.

           try {
               // Simulate network access.
               Thread.sleep(2000);
           } catch (InterruptedException e) {
               return false;
           }

           /*for (String credential : DUMMY_CREDENTIALS) {
               String[] pieces = credential.split(":");
               if (pieces[0].equals(mEmail)) {
                   // Account exists, return true if the password matches.
                   return pieces[1].equals(mPassword);
               }
           }*/

           // TODO: register the new account here.
           return true;
       }

       @Override
       protected void onPostExecute(final Boolean success) {
           mAuthTask = null;
           showProgress(false);

           if (success) {
               finish();
           } else {
               mPasswordView.setError(getString(R.string.error_incorrect_password));
               mPasswordView.requestFocus();
           }
       }

       @Override
       protected void onCancelled() {
           mAuthTask = null;
           showProgress(false);
       }
   }
}

