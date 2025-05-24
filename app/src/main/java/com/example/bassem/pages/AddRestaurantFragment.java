package com.example.bassem.pages;

import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bassem.R;
import com.example.bassem.data.FireBaseServices;
import com.example.bassem.data.Restaurant;
import com.example.bassem.utilities.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

public class AddRestaurantFragment extends Fragment {
    private ImageView img;
    private FireBaseServices fbs;
    private EditText etName, etDescription, etAddress, etPhone;
    private Button btnAdd;
    private Utils utils;
    private ActivityResultLauncher<String> imagePickerLauncher;

    public AddRestaurantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register image picker launcher
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        img.setImageURI(uri);
                        Utils.uploadImage(getActivity(), uri);
                    }
                }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_restaurant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbs = FireBaseServices.getInstance();
        utils = Utils.getInstance();

        etName = view.findViewById(R.id.etNameAddRestaurantFragment);
        etDescription = view.findViewById(R.id.etDescAddRestaurantFragment);
        etAddress = view.findViewById(R.id.etAddressAddRestaurantFragment);
        etPhone = view.findViewById(R.id.etPhoneAddRestaurantFragment);
        btnAdd = view.findViewById(R.id.btnAddAddRestaurantFragment);
        img = view.findViewById(R.id.ivCarAddCarFragment); // ✅ المصحح

        img.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_LONG).show();
                return;
            }

            Restaurant rest = new Restaurant(name, description, address, phone);

            fbs.getFire().collection("restaurants").add(rest)
                    .addOnSuccessListener(documentReference ->
                            Toast.makeText(getActivity(), "Successfully added your restaurant!", Toast.LENGTH_SHORT).show()
                    )
                    .addOnFailureListener(e -> {
                        Log.e("AddRestaurant", "Error: " + e.getMessage());
                        Toast.makeText(getActivity(), "Failed to add restaurant: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
