package com.example.myprova.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.myprova.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrenotaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrenotaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrenotaFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_prenota.
     */
    // TODO: Rename and change types and number of parameters
    public static PrenotaFragment newInstance(String param1, String param2) {
        PrenotaFragment fragment = new PrenotaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.fragment_prenota, container, false);
        //final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(view1.getContext(), getChildFragmentManager());
        ViewPager viewPager = view1.findViewById(R.id.view_pager);
        //viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        TabLayout tabs = view1.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = view1.findViewById(R.id.fabNext);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connection.corso != null && Connection.docente != null && Connection.slot != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setCancelable(true);
                    builder.setTitle("Confermare la seguente ripetizione?");
                    builder.setMessage( "Corso: " + Connection.corso + "\nDocente: " + Connection.nomeDoc +
                            "\nGiorno: " + Connection.days[Integer.parseInt(Connection.slot)/10] + "\nOra: " + Connection.hours[Integer.parseInt(Connection.slot)%10]);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            prenota();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("ANNULLA", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    });
                    builder.show();
                }else {
                    Snackbar.make(view, "Per salvare la prenotazione selezionare un corso, un docente ed uno slot temporale!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view1;
    }

    public void mioinit(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
    }

    public void prenota(){
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("action", "PRENOTA");
          //params.put("username", Connection.username);
            params.put("id_docente", Connection.docente);
            params.put("corso", Connection.corso);
           // params.put("case", "android");
            params.put("giorno", Connection.days);
            params.put("ora",Connection.hours) ;

            client.post(Connection.URL + "PrenotaServlet", params, new JsonHttpResponseHandler() {
                @SuppressLint("ShowToast")
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        boolean res = false;
                        res = response.getBoolean(0);
                        if(res){
                            Toast.makeText(getContext(), "Prenotazione registrata con successo!", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PrenotazioniFragment()).commit();
                            ((NavigationView)getActivity().findViewById(R.id.nav_view)).setCheckedItem(R.id.nav_prenotazioni);
                        }else{
                            Toast.makeText(getContext(), "Errore durante la registrazione della prenotazione!", Toast.LENGTH_SHORT).show();
                        }
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
