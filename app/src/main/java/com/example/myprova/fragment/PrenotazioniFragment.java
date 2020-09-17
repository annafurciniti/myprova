package com.example.myprova.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprova.R;

import com.example.myprova.fragment.DAO.Ripetizioni;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.example.myprova.fragment.Connection.corso;

public class PrenotazioniFragment extends Fragment {
    private ArrayAdapter<String> spinnerAdapter;
    ArrayList<Ripetizioni> prenSel = new ArrayList<>();
    boolean option = false;
    String stato1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fresh();
        return inflater.inflate(R.layout.fragment_prenotazioni, container, false);
    }

    public void fresh(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("action", "INIT");
        params.put("username", Connection.username);
       // params.put("role", Connection.isAdmin);
        params.put("caseMobile", "mobile");


        client.post(Connection.URL + "PrenotazioniServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    final ArrayList<Ripetizioni> pAtt = new ArrayList<>();
                    final ArrayList<Ripetizioni> pSvol= new ArrayList<>();
                    final ArrayList<Ripetizioni> pDisd = new ArrayList<>();

                    JSONArray goPrenApp= response.getJSONArray(0);
                    System.out.println(response.getJSONArray(0));

                    JSONArray goSvolApp = response.getJSONArray(1);
                    System.out.println(response.getJSONArray(1));

                    JSONArray goDisdApp = response.getJSONArray(2);
                    System.out.println(response.getJSONArray(2));


                    final ArrayList<String> titleListAtt = new ArrayList<>();
                    final ArrayList<String> titleListEff = new ArrayList<>();
                    final ArrayList<String> titleListDis = new ArrayList<>();


                    String days[]={"","Lunedì","Martedì","Mercoledì","giovedì", "Venerdì"};
                    String hours[]={"15:00","16:00","17:00","18:00"};

                  for(int i=0;i<goPrenApp.length();i++){
                        JSONObject json= goPrenApp.getJSONObject(i);
                        pAtt.add(new Ripetizioni((json.get("stato").toString()),(int)json.get("giorno"),(int)json.get("ora_i"),json.get("id_corso").toString(),json.get("id_docente").toString(),json.get("username").toString()));
                        titleListAtt.add(json.get("id_corso").toString() + ", " + days[(int)json.get("giorno")] +" alle ore " + hours[((int)json.get("ora_i"))-15] );
                    }
                   for(int i=0;i<goSvolApp.length();i++){
                        JSONObject json= goSvolApp.getJSONObject(i);
                       pSvol.add(new Ripetizioni((json.get("stato").toString()),(int)json.get("giorno"),(int)json.get("ora_i"),json.get("id_corso").toString(),json.get("id_docente").toString(),json.get("username").toString()));
                        titleListEff.add(json.get("id_corso").toString() + ", " + days[(int)json.get("giorno")] +" alle ore " + hours[((int)json.get("ora_i"))-15] );
                    }
                    for(int i=0;i<goDisdApp.length();i++){
                        JSONObject json= goDisdApp.getJSONObject(i);
                       pDisd.add(new Ripetizioni((json.get("stato").toString()),(int)json.get("giorno"),(int)json.get("ora_i"),json.get("id_corso").toString(),json.get("id_docente").toString(),json.get("username").toString()));
                        titleListDis.add(json.get("id_corso").toString() + ", " + days[(int)json.get("giorno")] +" alle ore " + hours[((int)json.get("ora_i"))-15]  );
                    }


                    //----------------------------------------//
                    spinnerAdapter=new ArrayAdapter<String>(getContext().getApplicationContext(),android.R.layout.simple_list_item_1);
                    spinnerAdapter.add("prenotato");
                    spinnerAdapter.add("svolto");
                    spinnerAdapter.add("disdetto");
                    final Spinner sp=(Spinner) getView().findViewById(R.id.spinner);
                    sp.setAdapter(spinnerAdapter);
                    final View line = getView().findViewById(R.id.line1);
                    GradientDrawable drawable = (GradientDrawable)line.getBackground();
                    drawable.setStroke(3, Color.GREEN); // set stroke width and stroke color
                    //sp.setBackgroundColor(Color.GREEN);
                    sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            ListView listaCorrente =  (ListView) getView().findViewById(R.id.activeListView);
                            ArrayAdapter<String> adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                                    android.R.layout.simple_list_item_1, titleListAtt);
                            TextView tv = (TextView)getView().findViewById(R.id.txt_empty);

                            if(pAtt.size() == 0){
                                //Lista vuota
                                tv.setText("Non ci sono ancora prenotazioni attive!");
                            }else{
                                tv.setText("");
                            }
                            GradientDrawable drawable = (GradientDrawable)line.getBackground();

                            switch (position){
                                case 0:
                                    listaCorrente = (ListView) getView().findViewById(R.id.activeListView);
                                    adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),
                                            android.R.layout.simple_list_item_1, titleListAtt);
                                    prenSel = pAtt;
                                    option = true;
                                    //idPren = position;
                                    // sp.setBackgroundColor(Color.GREEN);
                                    if(pAtt.size() == 0){
                                        //Lista vuota
                                        tv.setText("Non ci sono ancora prenotazioni attive!");
                                    }else{
                                        tv.setText("");
                                    }
                                    drawable.setStroke(3, Color.GREEN);
                                    break;
                                case 1:
                                    listaCorrente = (ListView) getView().findViewById(R.id.activeListView);
                                    adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),android.R.layout.simple_list_item_1, titleListEff);
                                    prenSel = pSvol;
                                    option = false;
                                    if(pSvol.size() == 0){
                                        //Lista vuota
                                        tv.setText("Non ci sono ancora prenotazioni effettuate!");
                                    }else{
                                        tv.setText("");
                                    }
                                    //sp.setBackgroundColor(Color.BLUE);
                                    drawable.setStroke(3, Color.BLUE);
                                    break;
                                case 2:
                                    listaCorrente = (ListView) getView().findViewById(R.id.activeListView);
                                    adapter  = new ArrayAdapter<String> (getContext().getApplicationContext(),android.R.layout.simple_list_item_1, titleListDis);
                                    prenSel = pDisd;
                                    option = false;
                                    if(pDisd.size() == 0){
                                        //Lista vuota
                                        tv.setText("Non ci sono ancora prenotazioni disdette!");
                                    }else{
                                        tv.setText("");
                                    }
                                    //sp.setBackgroundColor(Color.RED);
                                    drawable.setStroke(3, Color.RED);
                                    break;
                            }

                            listaCorrente.setAdapter(adapter);

                            listaCorrente.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setCancelable(true);
                                    builder.setTitle(prenSel.get(pos).getId_corso());
                                    builder.setMessage("Docente: " + prenSel.get(pos).getId_docente());
                                    if(option){
                                       stato1 = prenSel.get(pos).getStato();
                                        builder.setPositiveButton("Effettua", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                cambiaStato("svolto", stato1);
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setNegativeButton("Disdici", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                cambiaStato("disdetta", stato1);
                                                dialog.dismiss();
                                            }

                                        });}
                                    builder.show();
                                }
                            });
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

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
    public void cambiaStato(final String stato, String stato1){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("action", "STATO");
        params.put("docente", Connection.docente);
        params.put("username",Connection.username);
        params.put("ora", Connection.ora);
        params.put("giorno", Connection.giorno);
        params.put("stato", stato);
      //  params.put("id", idPrenotazione);
        params.put("caseMobile", "mobile");
        Log.d("Stato :" ,stato1);

        client.post(Connection.URL + "PrenotazioniServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);Toast.makeText(getContext().getApplicationContext(), "Prenotazione " + stato + "!", Toast.LENGTH_SHORT).show();
                fresh();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("failure", ""+ statusCode+""+ errorResponse);
            }
        });
    }
}
