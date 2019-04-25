package com.example.re_task;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.re_task.base.BaseActivity;
import com.example.re_task.helper.PrefManager;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener{

    CircleImageView image;
    TextView nama, email, phone;
    Button logout;
    PrefManager prefManager;
    String namatext, emailtext, phonetext;

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        super.setUpActionBar("Profile");

        mAuth = FirebaseAuth.getInstance();
        configureSigIn();
        init();
    }

    private void configureSigIn()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(ProfileActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void init()
    {
        prefManager = new PrefManager(this);
        image = findViewById(R.id.circleImageUser);
        nama = findViewById(R.id.tvNameUser);
        email = findViewById(R.id.tvEmail);
        phone = findViewById(R.id.tvHp);
        logout = findViewById(R.id.btnLogout);

        namatext = prefManager.getGivenName();
        emailtext = prefManager.getUserEmail();
        phonetext = prefManager.getPhoneNumber();
        String uri = prefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        nama.setText(namatext);
        email.setText(emailtext);
        Picasso.get()
                .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
                .error(android.R.drawable.sym_def_app_icon)
                .into(image);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new iOSDialogBuilder(ProfileActivity.this)
                        .setTitle("Logout")
                        .setSubtitle("Yakin ingin keluar ?")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener("Ya", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                logout();
                            }
                        })
                        .setNegativeListener("Tidak", new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        }).build().show();
            }
        });

    }

    private void logout()
    {
        new PrefManager(getApplicationContext()).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Toast.makeText(getApplicationContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
