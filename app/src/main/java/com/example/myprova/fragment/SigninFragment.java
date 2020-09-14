package com.example.myprova.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myprova.R;

public class SigninFragment extends Fragment {
    private EditText user;
    private String blockCharacterSet = "|!£$%&/()=?^€><,.;:-\\ç@°#§¶[]{}+*'"+'"';
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        user = view.findViewById(R.id.usernameFieldSignin);
        user.setFilters(new InputFilter[] { filter });

        return view;
    }
}
