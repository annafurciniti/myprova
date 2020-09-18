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
                makeListCorsi(root);
                break;
            case 2:
               textView.setText("O MERDA");
                break;
            case 3:
                textView.setText("medusa");
                break;
        }
        return root;
    }

    public void setlbl(){
        TextView button = (TextView) getView().findViewById(R.id.section_label);
        button.setText("cornuto");
    }

/*restituisce tutti i corsi disponibili*/
    private void makeListCorsi(View view1){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = view1;
        params.put("action", "INIT");
        params.put("case", "android");

        final PlaceholderFragment self = this;

        client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    final ArrayList<String> titolo= new ArrayList<>();
                    final JSONArray corsi = response.getJSONArray(0);
                    System.out.println(response.getJSONArray(0));

                    for (int i = 0; i < corsi.length(); i++) {
                        JSONObject e = corsi.getJSONObject(i);
                        titolo.add(e.getString("titolo"));
                    }
                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter adp = new MyAdapter(getContext(),titolo,  0);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getContext(), ((TextView)view.findViewById(R.id.txtMainTitle)).getText(), Toast.LENGTH_SHORT).show();
                            Connection.corso = (String)((TextView)view.findViewById(R.id.txtMainTitle)).getText();
                            Connection.docente = null;
                            Connection.ora =null;
                            Connection.giorno=null;
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

/*restituisce tutti i docenti*/
    private void makeListDocenti(String corso){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = root;
        System.out.println(corso);
        params.put("action", "DOC");
        params.put("corso", corso);
        params.put("case", "android");
        final PlaceholderFragment self = this;

        client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    final ArrayList<String> nome = new ArrayList<>();

                    final JSONArray docenti = response.getJSONArray(0);
                    System.out.println(response.getJSONArray(0));

                        for (int i = 0; i < docenti.length(); i++) {
                            JSONObject e = docenti.getJSONObject(i);
                            nome.add(e.getString("nome"));
                        }


                    if(docenti.length() == 0)
                        ((TextView)root.findViewById(R.id.section_label)).setText("Nessun docente per questo corso!");
                    else
                        ((TextView)root.findViewById(R.id.section_label)).setText("Scegli un docente disponibile:");

                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter adp = new MyAdapter(getContext(), nome, 1);
                    listView.setAdapter(adp);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getContext(), ((TextView)view.findViewById(R.id.txtMainTitle)).getText(), Toast.LENGTH_SHORT).show();
                            Connection.docente = ((String)((TextView)view.findViewById(R.id.txtMainTitle)).getText()).split(": ")[1];
                            Connection.ora=null;
                            Connection.giorno=null;
                            ((ListView)SectionsPagerAdapter.fragments[2].getListEntita()).clearChoices();

                            SectionsPagerAdapter.fragments[2].makeListRip(((String) ((TextView)view.findViewById(R.id.txtMainTitle)).getText()).split(": ")[1]);
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

/* restituisce tutti gli slot con giorni e orari*/
    private void makeListRip(String nome){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final View view2 = root;
        params.put("action", "RIP");
        params.put("doc", nome);
        params.put("corso",Connection.corso);
        params.put("case", "android");
        //params.put("utente",Connection.usernameApp);
        final PlaceholderFragment self = this;

        client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
            @SuppressLint("ShowToast")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                try {

                    final ArrayList<String> giorno = new ArrayList<>();
                      ArrayList<String> ora_i = new ArrayList<>();

                    final JSONArray orario = response.getJSONArray(0);
                    System.out.println(response.getJSONArray(0));
                    System.out.println(""+ orario +"");


                    if(orario.length() == 0)
                        ((TextView)root.findViewById(R.id.section_label)).setText("Il docente non ha slot disponibili!");
                    else
                        ((TextView)root.findViewById(R.id.section_label)).setText("Scegli giorno ed ora disponibili:");
                    System.out.println("rip");
                    for (int i = 0; i < orario.length(); i++) {
                        JSONObject e = orario.getJSONObject(i);

                        giorno.add(Integer.toString(e.getInt("giorno")));
                        ora_i.add(Integer.toString(e.getInt("ora_i")));

                    }
                    System.out.println(giorno);
                    System.out.println(ora_i);

                    ListView listView = view2.findViewById(R.id.listEntita);
                    MyAdapter2 adp2 = new MyAdapter2(getContext(), giorno, ora_i,2);
                    listView.setAdapter(adp2);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            String h,d;
                            switch ((String)((TextView)view.findViewById(R.id.txtMainTitle)).getText()){
                                case "Lunedì":
                                    d = "1";
                                    break;
                                case "Martedì":
                                    d = "2";
                                    break;
                                case "Mercoledì":
                                    d = "3";
                                    break;
                                case "Giovedì":
                                    d = "4";
                                    break;
                                default:
                                    d = "5";
                                    break;
                            }
                            System.out.println((String)((TextView)view.findViewById(R.id.txtSubTitle)).getText());
                            switch ((String)((TextView)view.findViewById(R.id.txtSubTitle)).getText()){
                                case "15:00/16:00":
                                    h = "15";
                                    break;
                                case "16:00/17:00":
                                    h = "16";
                                    break;
                                case "17:00/18:00":
                                    h = "17";
                                    break;
                                default:
                                    h = "18";
                                    break;
                            }

                            Connection.ora = h;
                            Connection.giorno = d;
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

        private int index= -1;
        public MyAdapter(Context context, ArrayList<String> mainTitles,  int index) {
            super(context, R.layout.rowprenota, R.id.txtMainTitle, mainTitles);
            this.mainTitles = mainTitles;

            this.index = index;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.rowprenota, parent, false);

            TextView main = row.findViewById(R.id.txtMainTitle);


            main.setText(mainTitles.get(position));
            if (this.index == 1)
                main.setText("ID: " + mainTitles.get(position));
            else
                main.setText(mainTitles.get(position));

            return row;
        }
    }
    class MyAdapter2 extends ArrayAdapter<String> {
        ArrayList<String> mainTitles;
         ArrayList<String> subTitles;
        private int index= -1;
        public MyAdapter2(Context context, ArrayList<String> mainTitles, ArrayList<String> subTitles,  int index) {
            super(context, R.layout.roworari, R.id.txtMainTitle, mainTitles);
            System.out.println(mainTitles);
            mainTitles=riccardo(mainTitles);
            System.out.println(mainTitles);
            this.mainTitles = mainTitles;
            System.out.println(subTitles);
            subTitles=teprego(subTitles);
            System.out.println(subTitles);
             this.subTitles = subTitles;
            this.index = index;
        }
        public ArrayList<String> riccardo(ArrayList<String> giorni) {
            for (int i = 0; i < giorni.size(); i++) {
                switch (giorni.get(i)) {
                    case "1":
                        giorni.set(i, "Lunedì");
                        break;
                    case "2":
                        giorni.set(i, "Martedì");
                        break;
                    case "3":
                        giorni.set(i, "Mercoledì");
                        break;
                    case "4":
                        giorni.set(i, "Giovedì");
                        break;
                    default:
                        giorni.set(i, "Venerdì");
                        break;
                }
            }
            return giorni;
        }


        public ArrayList<String> teprego(ArrayList<String> ore) {
            for (int i = 0; i < ore.size(); i++) {
                switch (ore.get(i)) {
                    case "15":
                        ore.set(i, "15:00/16:00");
                        break;
                    case "16":
                        ore.set(i, "16:00/17:00");
                        break;
                    case "17":
                        ore.set(i, "17:00/18:00");
                        break;
                    default:
                        ore.set(i, "18:00/19:00");
                        break;
                }
            }
            return ore;

        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater)getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.roworari, parent, false);
            TextView main = row.findViewById(R.id.txtMainTitle);
            TextView sub = row.findViewById(R.id.txtSubTitle);


            main.setText(mainTitles.get(position));
       if (this.index == 2)
                sub.setText( subTitles.get(position));
            else
                sub.setText(subTitles.get(position));

            return row;
        }
    }
}