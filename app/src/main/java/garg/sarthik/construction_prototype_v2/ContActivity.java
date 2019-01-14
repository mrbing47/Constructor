package garg.sarthik.construction_prototype_v2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ContActivity extends AppCompatActivity {

    private static final String TAG = "getting doc";
    Button btnContProfile;
    RecyclerView rvContProjects;
    EditText etProId;
    EditText etUserId;

    ArrayList<Project> projectData;

    FirebaseFirestore db;
    DocumentReference docRef;
    CollectionReference colRef;
    int count = 0;
    String userId;
    String proId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont);

        projectData = new ArrayList<>();

        etProId = findViewById(R.id.etContProID);
        etUserId = findViewById(R.id.etContUserId);
        btnContProfile = findViewById(R.id.btnContProfile);

        Contractor contractor = getIntent().getParcelableExtra(Constants.CONT_PARCEL);

        db = FirebaseFirestore.getInstance();
        docRef = db.collection(Constants.COL_CONT).document(contractor.getContId());
        colRef = docRef.collection(Constants.COL_PRO);

        btnContProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userId = etUserId.getText().toString();
                proId = etProId.getText().toString();

                if (userId.isEmpty() || proId.isEmpty()) {
                    Toast.makeText(ContActivity.this, "Need Both to search the project", Toast.LENGTH_SHORT).show();
                    return;
                }
                final DocumentReference mDocRef = db.collection(Constants.COL_USER).document(userId);
                mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            mDocRef.collection(Constants.COL_PRO).document(proId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {

                                        Project project = documentSnapshot.toObject(Project.class);
                                        Log.e(TAG, "onSuccess: " + project.getProStatus());
                                        startActivity(new Intent(ContActivity.this,UserProjectActivity.class)
                                                .putExtra(Constants.PRO_PARCEL,documentSnapshot.toObject(Project.class))
                                                .putExtra(Constants.PARCEL_TYPE,Constants.COL_CONT));

                                    } else
                                        Toast.makeText(ContActivity.this, "No such Project exists", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                            Toast.makeText(ContActivity.this, "No such User exists", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        docRef.collection(Constants.COL_PRO).get()

                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Map<String, Object> projectMap = doc.getData();
                    count++;
                    String userId = (String) projectMap.get(Constants.PRO_USERID);
                    String proId = (String) projectMap.get(Constants.PRO_ID);
                    Log.e(TAG, "onSuccess: \nProject ID = " + proId + "\nUser ID = " + userId);
                    DocumentReference mDocRef = db.collection(Constants.COL_USER).document(userId).collection(Constants.COL_PRO).document(proId);
                    mDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Project project = documentSnapshot.toObject(Project.class);
                            projectData.add(project);
                            Log.e(TAG, "onSuccess: \nStatus = " + documentSnapshot.toObject(Project.class).getProStatus() + "\nProject Size = " + projectData.size());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure: ", e);
                            Toast.makeText(ContActivity.this, "Unable to connect to the server", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                rvContProjects = findViewById(R.id.rvContProjects);
                Log.e(TAG, "onSuccess: \nProject Size = " + projectData.size() + "\nCount = " + count);
                rvContProjects.setLayoutManager(new LinearLayoutManager(ContActivity.this));
                rvContProjects.setAdapter(new ProjectAdaptor(ContActivity.this, projectData, Constants.COL_CONT));
            }
        });

    }
}
