package com.example.myprova.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myprova.R;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;


import static com.example.myprova.fragment.Connection.isAdmin;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toogle;
    private NavigationView navigationView;
    private boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NAVBAR
        this.logged = false;
        this.invalidateOptionsMenu();

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toogle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toogle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                break;
            case R.id.nav_prenotazioni:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrenotazioniFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_prenotazioni);
                break;
            case R.id.nav_prenota:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrenotaFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_prenota);
                break;
            case R.id.nav_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_login);
                break;
            case R.id.nav_signin:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SigninFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_signin);
                break;
            case R.id.nav_logout:
                ((TextView) findViewById(R.id.txt_username)).setText("");
                ((TextView)findViewById(R.id.txt_ruolo)).setText("");

                Connection.username = null;
                Connection.isAdmin = -1;

                this.logged = false;
                this.invalidateOptionsMenu();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
                Toast.makeText(this, "Logout eseguito", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu navMenuLogIn) {
        navMenuLogIn = navigationView.getMenu();

        navMenuLogIn.findItem(R.id.nav_prenotazioni).setVisible(logged);
        navMenuLogIn.findItem(R.id.nav_prenota).setVisible(logged);
        navMenuLogIn.findItem(R.id.nav_logout).setVisible(logged);

        navMenuLogIn.findItem(R.id.nav_login).setVisible(!logged);
        navMenuLogIn.findItem(R.id.nav_signin).setVisible(!logged);
        return super.onCreateOptionsMenu(navMenuLogIn);
    }

    public void Login(final View view) throws IOException {
        String username = ((EditText) findViewById(R.id.usernameField)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordField)).getText().toString();

        final TextView viewControl = (TextView) findViewById(R.id.txtWarning);
        if (username.equals("") || password.equals("")) {
            viewControl.setText("Attenzione, campo mancante");
            }else{
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("action", "login");
                params.put("username", username);
                params.put("password", password);
                params.put("role",isAdmin);

                client.post(Connection.URL + "LoginServlet", params, new JsonHttpResponseHandler() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            ArrayList<String> rets = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                rets.add(response.getString(i));
                                Log.d("ret" + i, rets.get(i));
                            }

                            if (!rets.get(0).equals("error")) {
                                TextView txtUsername = (TextView) findViewById(R.id.txt_username);
                                TextView txtRuolo =(TextView) findViewById(R.id.txt_ruolo);
                                txtUsername.setText(rets.get(0));
                                Connection.username = rets.get(0);

                                //0 studente, 1 admin
                                if(rets.get(1).equals("0")) {
                                    Connection.isAdmin = 0;
                                    txtRuolo.setText("Studente");

                                }else {
                                    Connection.isAdmin = 1;
                                    txtRuolo.setText("Amministratore");

                                }

                                //modifico la nav laterale
                                logged = true;
                                invalidateOptionsMenu();
                                //changeNav(true);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                                navigationView.setCheckedItem(R.id.nav_home);
                                Toast.makeText(getApplicationContext(), " Bentornato " + rets.get(0) + " !", Toast.LENGTH_SHORT).show();
                            } else {
                                viewControl.setText("Username o password errati");
                                Toast.makeText(getApplicationContext(), "Username o password errati", Toast.LENGTH_SHORT).show();
                            }
                            //rets.get(0): username
                            //rets.get(1): ruolo


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("failure",""+ statusCode+""+ errorResponse);
                    }
                });
            }
        }


    public void Signin(final View view) throws IOException {
        String username = ((EditText) findViewById(R.id.usernameFieldSignin)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordFieldSignin)).getText().toString();
        final TextView viewControl = (TextView) findViewById(R.id.txtWarningSignin);
        if (username.equals("") || password.equals("")) {
            viewControl.setText("Attenzione, campo mancante");
            }else{
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                params.put("action", "sing");
                params.put("username", username);
                params.put("password", password);
                params.put("role",isAdmin);

                client.post(Connection.URL + "LoginServlet", params, new JsonHttpResponseHandler() {
                    @SuppressLint({"ShowToast", "SetTextI18n"})
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            ArrayList<String> rets = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                rets.add(response.getString(i));
                                Log.d("ret" + i, rets.get(i));
                            }

                            if(rets.get(0).equals("esistente")){
                                viewControl.setText("Username già esistente");
                            }else if (rets.get(0).equals("error")){
                                viewControl.setText("Username o password errati");
                            }else{
                                TextView txtUsername = findViewById(R.id.txt_username);
                                TextView txtRuolo = findViewById(R.id.txt_ruolo);
                                txtUsername.setText(rets.get(0));
                                //0 studente, 1 admin
                            /*    if(rets.get(1).equals("0")) {
                                    txtRuolo.setText("User");
                                }else {
                                    txtRuolo.setText("Admin");
                                }*/

                                //modifico la nav laterale
                                logged = true;
                                invalidateOptionsMenu();
                                //changeNav(true);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                                navigationView.setCheckedItem(R.id.nav_home);
                                Toast.makeText(getApplicationContext(), "Benvenuto " + rets.get(0) + "! ", Toast.LENGTH_SHORT).show();
                            }
                            //rets.get(0): username
                            //rets.get(1): ruolo


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                       super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("failure",""+ statusCode+""+ errorResponse);
                    }
                });
            }
        }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(navigationView.getCheckedItem().toString().equals("Home")) {
                this.finish();
            }else{
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                navigationView.setCheckedItem(R.id.nav_home);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
