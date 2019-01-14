package garg.sarthik.construction_prototype_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LoginProcedure";
    EditText etID;
    EditText etRegID;
    EditText etRegName;
    Button btnUser;
    Button btnCont;
    Button btnRegUser;
    Button btnRegCont;

    FirebaseFirestore firestore;
    boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();

        etID = findViewById(R.id.etID);
        etRegID = findViewById(R.id.etRegID);
        etRegName = findViewById(R.id.etRegName);

        btnUser = findViewById(R.id.btnUser);
        btnCont = findViewById(R.id.btnCont);
        btnRegUser = findViewById(R.id.btnRegUser);
        btnRegCont = findViewById(R.id.btnRegCont);

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = etID.getText().toString();

                if (userId.isEmpty())
                    return;

                firestore.collection(Constants.COL_USER).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            intent.putExtra(Constants.USER_PARCEL, documentSnapshot.toObject(User.class));
                            startActivity(intent);
                        } else
                            Toast.makeText(MainActivity.this, "No User exist under this ID", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Problem in Login In process ", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

            }
        });
        btnCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String contId = etID.getText().toString();

                if (contId.isEmpty())
                    return;

                firestore.collection(Constants.COL_CONT).document(contId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            Intent intent = new Intent(MainActivity.this, ContActivity.class);
                            intent.putExtra(Constants.CONT_PARCEL, documentSnapshot.toObject(Contractor.class));
                            startActivity(intent);
                        } else
                            Toast.makeText(MainActivity.this, "No Contractor exist under this ID", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Problem in Login In process ", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onFailure: ", e);
                    }
                });

            }
        });
        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String userName = etRegName.getText().toString();
                final String userId = etRegID.getText().toString();

                if (userId.isEmpty() || userName.isEmpty())
                    return;

                final User user = new User(userName, userId);

                if (!verifyUser(Constants.COL_USER, userId)) {
                    firestore.collection(Constants.COL_USER).document(userId).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(MainActivity.this, UserActivity.class);
                            intent.putExtra(Constants.USER_PARCEL, user);
                            startActivity(intent);
                        }
                    });
                }else
                    Toast.makeText(MainActivity.this, "User already exists", Toast.LENGTH_SHORT).show();


            }
        });
        btnRegCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String contName = etRegName.getText().toString();
                final String contId = etRegID.getText().toString();

                if (contName.isEmpty() || contId.isEmpty())
                    return;

                final Contractor contractor = new Contractor(contName, contId);
                if (!verifyUser(Constants.COL_CONT, contId)) {
                    firestore.collection(Constants.COL_CONT).document(contId).set(contractor).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(MainActivity.this, ContActivity.class);
                            intent.putExtra(Constants.CONT_PARCEL, contractor);
                            startActivity(intent);
                        }
                    });
                } else
                    Toast.makeText(MainActivity.this, "Contractor already exists", Toast.LENGTH_SHORT).show();

            }
        });
    }

    boolean verifyUser(String collection, final String doc) {

        firestore.collection(collection).document(doc).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                    exists = true;
                else
                    exists = false;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            }
        });

        return exists;
    }
}
