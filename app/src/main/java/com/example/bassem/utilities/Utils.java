package com.example.bassem.utilities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.bassem.data.FireBaseServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class Utils {private static Utils instance;

    private static FireBaseServices fbs;
    private static String imageStr;

    public Utils()
    {
        fbs = FireBaseServices.getInstance();
    }

    public static Utils getInstance()
    {
        if (instance == null)
            instance = new Utils();

        return instance;
    }
    public void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(message);
        //builder.setMessage(message);

        // Add a button to dismiss the dialog box
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // You can perform additional actions here if needed
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void uploadImage(Context context, Uri selectedImageUri) {
        if (selectedImageUri != null) {
            // اسم فريد للصورة داخل مجلد images/
            String imageName = "images/" + UUID.randomUUID() + ".jpg";
            StorageReference imageRef = fbs.getStorage().getReference().child(imageName);

            UploadTask uploadTask = imageRef.putFile(selectedImageUri);
            uploadTask
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            fbs.setSelectedImageURL(uri); // حفظ رابط الصورة
                        }).addOnFailureListener(e -> {
                            Log.e("Utils: getDownloadUrl", e.getMessage());
                            Toast.makeText(context, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show();
                        });

                        Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Utils: uploadImage", e.getMessage());
                        Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });

        } else {
            Toast.makeText(context, "Please choose an image first", Toast.LENGTH_SHORT).show();
        }
    }
}
