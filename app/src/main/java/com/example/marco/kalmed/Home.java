package com.example.marco.kalmed;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Home extends AppCompatActivity {


    @BindView(R.id.logoutBTN)
    Button logoutBTN;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    goToLogin();
                }
            }
        };

        Log.e("Cuenta", "conexion calendario");
        conexionCalendario();
        Log.e("instancia","instancias");
    }

    private void goToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null)
            mAuth.removeAuthStateListener(authListener);
    }

    public void logOut(View view) {
        mAuth.signOut();
        Toast.makeText(this, "El usuario salio de la sesion", Toast.LENGTH_LONG).show();
    }


    public void goToCrearConsulta(View view) {
        Intent intent = new Intent(this, createMeeting.class);
        intent.putExtra("calendario",8);
        startActivity(intent);
        finish();
    }

    public void conexionCalendario() {
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT
        };

        final String[] INSTANCE_PROJECTION = new String[] {
                CalendarContract.Instances.EVENT_ID,      // 0
                CalendarContract.Instances.BEGIN,         // 1
                CalendarContract.Instances.TITLE          // 2
        };

        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?))";/* AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";*/
        String[] selectionArgs = new String[]{};
        // Submit the query and get a Cursor object back.
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        //cur = cr.query(uri, INSTANCE_PROJECTION, CalendarContract.Instances.EVENT_ID +" ", null, null);

        Log.i("quepedo", DatabaseUtils.dumpCursorToString(cur));

        if(cur!=null){
            while (cur.moveToNext()) {
                long calID = 0;
                String displayName = null;
                String accountName = null;
                String ownerName = null;

                // Get the field values
                calID = cur.getLong(PROJECTION_ID_INDEX);
                displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
                accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
                ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

                // Do something with the values...

                Log.e("Cuenta","display name: "+displayName+"");
            }
            cur.moveToFirst();

        }else{
            Log.e("Cuenta", "cur" +
                    "cursor null");
        }

        Cursor cur2 = null;
        // String selection2 = CalendarContract.Instances.EVENT_ID + " = ?";
        //String[] selectionArgs2 = new String[] {Long.toString(calID)};

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, 1514786400000L);
        ContentUris.appendId(builder, 1546236000000L);

        cur2 =  cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                null);

//        while (cur2.moveToNext()) {
//            Log.i("pinchemadre",DatabaseUtils.dumpCursorToString(cur2));
//        }
        Log.i("pinchemadre",DatabaseUtils.dumpCursorToString(cur2));
    }


}
    