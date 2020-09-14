package com.example.myprova.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myprova.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.makeListCorsi();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void makeListCorsi(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("action", "INIT");
        params.put("case", "android");

        final HomeFragment self = this;

        client.post(Connection.URL + "HomeServlet", params, new JsonHttpResponseHandler() {

            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response){
                super.onSuccess(statusCode, headers, response);
                try {
                    String result="";
                     final ArrayList<String> titolo = new ArrayList<>();
                    final ArrayList<String> descrizione = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject e = response.getJSONObject(i);
                        titolo.add(e.getString("titolo"));
                       descrizione.add(e.getString("descrizione"));
                    }
                    ListView listView = getView().findViewById(R.id.listCorsi);
                    MyAdapter adp = new MyAdapter(getContext(), titolo);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(true);
                            builder.setTitle(titolo.get(position));
                            builder.setMessage(descrizione.get(position));
                            builder.setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }

                            });
                            if(Connection.username != null){
                                builder.setPositiveButton("Vedi corso", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrenotazioniFragment()).commit();
                                        dialog.dismiss();
                                    }
                                });
                            }

                            builder.show();
                        }
                    });
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

    class MyAdapter extends ArrayAdapter<String> {
        ArrayList<String> mainTitles;


        public MyAdapter(Context context, ArrayList<String> mainTitles) {
            super(context, R.layout.row, R.id.txtMainTitle, mainTitles);
            this.mainTitles = mainTitles;

        }

        @SuppressLint("Range")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);

            TextView main = row.findViewById(R.id.txtMainTitle);
            //TextView sub = row.findViewById(R.id.txtSubTitle);

            main.setText(mainTitles.get(position));
           // sub.setText(subTitles.get(position));
            main.setTextColor(Color.parseColor("#6200EE"));

            return row;
        }
    }
}