package com.example.myprova.fragment.utilityPrenota;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myprova.R;
import com.example.myprova.fragment.Connection;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * A placeholder fragment containing a simple view.
 */
public class  PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private  View root = null;
    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @SuppressLint("FragmentLiveDataObserve")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_entita, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                System.out.println("corsi1");
                makeListCorsi(root);
                break;
            case 2:
                System.out.println("docenti1");
                textView.setText("cazzo");
                break;
            case 3:
                System.out.println("orari1");
                textView.setText("medusa");
                break;
        }
        return root;
    }

    public void setlbl(){
        TextView button = (TextView) getView().findViewById(R.id.section_label);
        button.setText("cornuto");
    }

    private void makeListCorsi(View view1){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = view1;
        params.put("action", "INIT");
        params.put("case", "android");

        final PlaceholderFragment self = this;

        client.post(Connection.URL + "HomeServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    final ArrayList<String> titolo= new ArrayList<>();
                    final ArrayList<String> descrizione= new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject e = response.getJSONObject(i);
                        titolo.add(e.getString("titolo"));
                    }
                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter adp = new MyAdapter(getContext(),titolo, descrizione, 0);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //FragmentTransaction ft = getFragmentManager().beginTransaction();
                            //ft.replace(R.id.fragment_container, new LoginFragment()).commit();
                            Toast.makeText(getContext(), ((TextView)view.findViewById(R.id.txtMainTitle)).getText(), Toast.LENGTH_SHORT).show();
                            Connection.corso = (String)((TextView)view.findViewById(R.id.txtMainTitle)).getText();
                            Connection.docente = null;
                            Connection.slot = null;
                            ((ListView)SectionsPagerAdapter.fragments[2].getListEntita()).setAdapter(null);
                            SectionsPagerAdapter.fragments[1].makeListDocenti((String) ((TextView)view.findViewById(R.id.txtMainTitle)).getText());
                            ((TabLayout) getParentFragment().getView().findViewById(R.id.tabs)).getTabAt(1).select();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.d("failure", String.valueOf(throwable));
            }
        });
    }

    private void makeListDocenti(String idCorso){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = root;
        System.out.println(idCorso);
        params.put("action", "DOC");
        params.put("idCorso", idCorso);
        params.put("case", "android");
        final PlaceholderFragment self = this;
        System.out.println("cia1");
        client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("cia2");
                try {
                    System.out.println("cia3");
                    final ArrayList<String> id_docente = new ArrayList<>();
                    ArrayList<String> nome = new ArrayList<>();
                    if(response.length() == 0)
                        ((TextView)root.findViewById(R.id.section_label)).setText("NESSUN DOCENTE ASSEGNATO AL CORSO!");
                    else
                        ((TextView)root.findViewById(R.id.section_label)).setText("SCEGLIERE UNO DEI DOCENTI DISPONIBILI:");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject e = response.getJSONObject(i);
                        id_docente.add(e.getString("idDocente"));
                        nome.add(e.getString("nome"));
                    }
                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter adp = new MyAdapter(getContext(), nome, id_docente, 1);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //FragmentTransaction ft = getFragmentManager().beginTransaction();
                            //ft.replace(R.id.fragment_container, new LoginFragment()).commit();
                            Toast.makeText(getContext(), "onClick()", Toast.LENGTH_SHORT).show();
                            Connection.nomeDoc = (String)((TextView)view.findViewById(R.id.txtMainTitle)).getText();
                          //  Connection.docente = ((String)((TextView)view.findViewById(R.id.txtSubTitle)).getText()).split(": ")[1];
                            ((ListView)SectionsPagerAdapter.fragments[2].getListEntita()).clearChoices();
                            Connection.slot = null;
                            //SectionsPagerAdapter.fragments[2].makeListOrari(((String) ((TextView)view.findViewById(R.id.txtSubTitle)).getText()).split(": ")[1]);
                            ((TabLayout) getParentFragment().getView().findViewById(R.id.tabs)).getTabAt(2).select();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }

    private void makeListOrari(String idDocente){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = root;
        params.put("action", "GETORARI");
        params.put("idDocente", idDocente);
        params.put("case", "android");
        params.put("utente",Connection.username);
        final PlaceholderFragment self = this;
        System.out.println("cia1");
        client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("cia2");
                try {
                    System.out.println("cia3");
                    final ArrayList<String> giorno = new ArrayList<>();
                    ArrayList<String> ora_i = new ArrayList<>();
                    if(response.length() == 0)
                        ((TextView)root.findViewById(R.id.section_label)).setText("IL DOCENTE NON HA ORARI DISPONIBILI!");
                    else
                        ((TextView)root.findViewById(R.id.section_label)).setText("SCEGLIERE UNO DEGLI SLOT DISPONIBILI:");
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject e = response.getJSONObject(i);
                        giorno.add(Connection.days[Integer.parseInt(e.getString("giorno"))]);
                        ora_i.add(Connection.hours[Integer.parseInt(e.getString("ora"))]);
                    }
                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter adp = new MyAdapter(getContext(), giorno, ora_i, 2);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //FragmentTransaction ft = getFragmentManager().beginTransaction();
                            //ft.replace(R.id.fragment_container, new LoginFragment()).commit();
                            String d;
                            String h;
                            switch ((String)((TextView)view.findViewById(R.id.txtMainTitle)).getText()){
                                case "Lunedì":
                                    d = "0";
                                    break;
                                case "Martedì":
                                    d = "1";
                                    break;
                                case "Mercoledì":
                                    d = "2";
                                    break;
                                case "Giovedì":
                                    d = "3";
                                    break;
                                default:
                                    d = "4";
                                    break;
                            }
                           /* switch ((String)((TextView)view.findViewById(R.id.txtSubTitle)).getText()){
                                case "14:00/15:00":
                                    h = "0";
                                    break;
                                case "15:00/16:00":
                                    h = "1";
                                    break;
                                case "16:00/17:00":
                                    h = "2";
                                    break;
                                default:
                                    h = "3";
                                    break;
                            }
                            Connection.slot = d + h;*/
                            Toast.makeText(getContext(), "onClick()", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }
        });
    }

    public View getListEntita(){
        return root.findViewById(R.id.listEntita);
    }

    class MyAdapter extends ArrayAdapter<String> {
        ArrayList<String> mainTitles;
       // ArrayList<String> subTitles;
        private int index= -1;
        public MyAdapter(Context context, ArrayList<String> mainTitles, ArrayList<String> subTitles, int index) {
            super(context, R.layout.row, R.id.txtMainTitle, mainTitles);
            this.mainTitles = mainTitles;
         // this.subTitles = subTitles;
            this.index = index;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            //ImageView images = row.findViewById(R.id.img);
            TextView main = row.findViewById(R.id.txtMainTitle);
           // TextView sub = row.findViewById(R.id.txtSubTitle);


            //images.setImage;
            main.setText(mainTitles.get(position));
        /*    if (this.index == 1)
                sub.setText("ID: " + subTitles.get(position));
            else
                sub.setText(subTitles.get(position));*/

            return row;
        }
    }
}