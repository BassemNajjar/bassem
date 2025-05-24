package com.example.bassem.pages;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bassem.R;
import com.example.bassem.data.FireBaseServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;

public class SignupFragment extends Fragment {

    private EditText etUsername, etpassword;
    private Button btnSignup;
    private FireBaseServices fbs;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbs = FireBaseServices.getInstance();

        etUsername = view.findViewById(R.id.etUserNameSignup);
        etpassword = view.findViewById(R.id.etpasswordSignup);
        btnSignup = view.findViewById(R.id.btnsignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etpassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                fbs.getAuth().createUserWithEmailAndPassword(username, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getActivity(), "Successfully signed up", Toast.LENGTH_SHORT).show();
                                goToHomeFragment();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Signup failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
    private void goToHomeFragment() {
        FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main, new HomeFragment());
        ft.commit();
    }
}