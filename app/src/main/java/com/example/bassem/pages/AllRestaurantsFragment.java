package com.example.bassem.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bassem.data.FireBaseServices;
import com.example.bassem.R;
import com.example.bassem.data.Restaurant;
import com.example.bassem.adapters.RestaurantAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRestaurantsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRestaurantsFragment extends Fragment {
    private FireBaseServices fbs;
    private ArrayList<Restaurant> rests;
    private RecyclerView rvRests;
    private RestaurantAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllRestaurantsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRestaurantsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRestaurantsFragment newInstance(String param1, String param2) {
        AllRestaurantsFragment fragment = new AllRestaurantsFragment();
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
        return inflater.inflate(R.layout.fragment_all_restaurants, container, false);
    }
    @Override
    public void onStart() {
        super.onStart();

        fbs = FireBaseServices.getInstance();
        rests = new ArrayList<>();
        rvRests = getView().findViewById(R.id.rvRestaurantsRestFragment);
        adapter = new RestaurantAdapter(getActivity(), rests);
        rvRests.setAdapter(adapter);
        rvRests.setHasFixedSize(true);
        rvRests.setLayoutManager(new LinearLayoutManager(getActivity()));
        fbs.getFire().collection("restaurants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dataSnapshot: queryDocumentSnapshots.getDocuments()){
                    Restaurant rest = dataSnapshot.toObject(Restaurant.class);

                    rests.add(rest);
                }

                adapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                Log.e("AllRestaurantsFragment", e.getMessage());
            }
        });
    }
}