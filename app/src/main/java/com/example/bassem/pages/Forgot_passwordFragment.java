package com.example.bassem.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bassem.data.FireBaseServices;
import com.example.bassem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Forgot_passwordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Forgot_passwordFragment extends Fragment {
    private FireBaseServices fbs;
    private EditText etEmail;
    private Button btnreset;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Forgot_passwordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Forgot_passwordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Forgot_passwordFragment newInstance(String param1, String param2) {
        Forgot_passwordFragment fragment = new Forgot_passwordFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();
        fbs=FireBaseServices.getInstance();
        etEmail=getView().findViewById(R.id.etReset);
        btnreset=getView().findViewById(R.id.btnreset);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fbs.getAuth().sendPasswordResetEmail(etEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(getActivity(), "check your email", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getActivity(), "failed,check the email address you entered", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}