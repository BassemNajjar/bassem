package com.example.bassem.pages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bassem.R;
import com.example.bassem.data.FireBaseServices;
import com.example.bassem.data.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantDetailsFragment extends Fragment {
    private static final int PERMISSION_SEND_SMS = 1;
    private static final int REQUEST_CALL_PERMISSION = 2;
    private FireBaseServices fbs;
    private TextView tvResName,tvResDescription,tvphone,tvaddress;
    private ImageView ivRestaurantDetailsFragment;
    private Button sendSMSButton, btnWhatsapp, btnCall;
    private Restaurant myres;
    private boolean isEnlarged = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantDetailsFragment newInstance(String param1, String param2) {
        RestaurantDetailsFragment fragment = new RestaurantDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        Restaurant restaurant = getArguments().getParcelable("res");

        if (restaurant != null) {
            Log.d("RestaurantDetails", "Received: " + restaurant.getName());
            // set views here
        } else {
            Log.e("RestaurantDetails", "No Restaurant object received!");
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
        ImageView ivresPhoto = getView().findViewById(R.id.ivRestaurantDetailsFragment);

        ivresPhoto.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                ViewGroup.LayoutParams layoutParams = ivresPhoto.getLayoutParams();
                ivresPhoto.setLayoutParams(layoutParams);



            }
        });


    }



    public void init()
    {


        fbs=  FireBaseServices.getInstance();
        tvResName=getView().findViewById(R.id.tvResName);
        tvResDescription=getView().findViewById(R.id.tvResDescription);
        tvphone=getView().findViewById(R.id.tvphone);
        tvaddress=getView().findViewById(R.id.tvaddress);
        ivRestaurantDetailsFragment = getView().findViewById(R.id.ivRestaurantDetailsFragment);

        Bundle args = getArguments();
        if (args != null) {
            myres = args.getParcelable("res");
            if (myres != null) {
                //String data = myObject.getData();
                // Now you can use 'data' as needed in FragmentB
                tvResName.setText(myres.getName());
                tvResDescription.setText(myres.getDescription());
                tvphone.setText(myres.getPhone());
                tvaddress.setText(myres.getAddress());
                if (myres.getphoto() == null || myres.getphoto().isEmpty())
                {
                    Picasso.get().load(R.drawable.ic_launcher_background).into(ivRestaurantDetailsFragment);
                }
                else {
                    Picasso.get().load(myres.getphoto()).into(ivRestaurantDetailsFragment);
                }
            }
        }


    }



    private void sendSMS() {
        String phoneNumber = myres.getPhone();
        String message = "I am Interested in your  "+myres.getName()+"  fur: " + myres.getDescription();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "SMS sending failed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(requireContext(), "Permission denied. Cannot send SMS.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCall();
            }
        }
    }
    // TODO : check Phone number is not correct;
    public void sendWhatsAppMessage(View view) {

        String smsNumber = "+972"+myres.getPhone();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        //  Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, " I am Interested in your  " +myres.getName()+ "  car:  "  + myres.getDescription());
        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
        sendIntent.setPackage("com.whatsapp");

        startActivity(sendIntent);
    }
    //  888 whatsapp setting
    private boolean isAppInstalled(String s) {
        PackageManager packageManager= getActivity().getPackageManager();
        boolean is_installed;
        try{
            packageManager.getPackageInfo(s,PackageManager.GET_ACTIVITIES);
            is_installed=true;
        } catch (PackageManager.NameNotFoundException e) {
            is_installed=false;
            throw new RuntimeException(e);
        }
        return  is_installed;
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall();
        }

    }

    private void startCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + myres.getPhone()));

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }




    }

}