/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{
  Boolean signUpModeActive = true;
  TextView loginTextView;
  EditText usernameEditText;
  EditText passwordEditText;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
      signUpClick(v);
    }
    return false;
  }

  public void onClick(View view){
    if (view.getId()==R.id.loginTextView){
//      Log.i("Switch","Login was tapped");
      Button signUpButton = (Button) findViewById(R.id.signUpBotton);
      if (signUpModeActive){
        signUpModeActive = false;
        signUpButton.setText("Login");
        loginTextView.setText("or, Signup");
      }else{
        signUpModeActive = true;
        signUpButton.setText("Sign Up");
        loginTextView.setText("or, Login");
      }
    }else if(view.getId()==R.id.logoImageView || view.getId() ==R.id.backgroundLayout){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }

  public void signUpClick(View view){
    if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
      Toast.makeText(this,"A username or a password are required",Toast.LENGTH_SHORT).show();
    }else{
      if (signUpModeActive) {
        ParseUser user = new ParseUser();
        user.setUsername(usernameEditText.getText().toString());
        user.setPassword(passwordEditText.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              Log.i("Sign up", "success");
              showUserList();
            } else {
              e.printStackTrace();
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }else{
//        login
        ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
          @Override
          public void done(ParseUser user, ParseException e) {
            if (user!=null){
              Log.i("Login","ok");
              showUserList();
            }else{
              Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
          }
        });
      }
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Instagram");

    loginTextView =(TextView) findViewById(R.id.loginTextView);
    loginTextView.setOnClickListener(this);

    usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    passwordEditText = (EditText) findViewById(R.id.passwordEditText);

    ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
    RelativeLayout backgroundLayout = (RelativeLayout) findViewById(R.id.backgroundLayout);

    logoImageView.setOnClickListener(this);
    backgroundLayout.setOnClickListener(this);

    passwordEditText.setOnKeyListener(this);
    if (ParseUser.getCurrentUser()!=null){
      showUserList();
    }
    /*
    ParseUser user = new ParseUser();
    user.setUsername("nick");
    user.setPassword("myPass");
    user.signUpInBackground(new SignUpCallback() {
      @Override
      public void done(ParseException e) {
        if (e==null){
          Log.i("Sign up is OK!","We did it");
        }else{
          e.printStackTrace();
        }
      }
    });

    ParseUser.logInInBackground("nick", "myPass", new LogInCallback() {
      @Override
      public void done(ParseUser user, ParseException e) {
        if (user != null){
          Log.i("Success","we logged in");
        }else{
          e.printStackTrace();
        }
      }
    });
    */
//    ParseUser.logOut();
//    if (ParseUser.getCurrentUser()!=null){
//      Log.i("Signed In",ParseUser.getCurrentUser().getUsername());
//    }else{
//      Log.i("not luck","Not signed in");
//    }
/*
    ParseObject score = new ParseObject("Score");
    score.put("username","sean");
    score.put("score",65);
    score.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
          if (e==null){
            Log.i("Success","We save the score");
          }else{
            e.printStackTrace();
          }
        }
    });
*/
//    ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//    query.getInBackground("1uKbJZWFIX", new GetCallback<ParseObject>() {
//      @Override
//      public void done(ParseObject object, ParseException e) {
//        if (e==null && object!=null){
//          object.put("score",85);
//          object.saveInBackground();
//          Log.i("useranme",object.getString("username"));
//          Log.i("score",Integer.toString(object.getInt("score")));
//        }
//      }
//    });




//  ParseQuery<ParseObject> query = ParseQuery.getQuery("Score");
//
//  query.whereEqualTo("username","sean");
//  query.setLimit(1);
//  query.findInBackground(new FindCallback<ParseObject>() {
//    @Override
//    public void done(List<ParseObject> objects, ParseException e) {
//      if (e==null){
//        if (objects.size()>0){
//          for (ParseObject object : objects){
//            Log.i("username",object.getString("username"));
//            Log.i("score",object.getString("score"));
//          }
//        }
//      }
//    }
//  });
  ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}