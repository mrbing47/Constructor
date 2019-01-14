package garg.sarthik.construction_prototype_v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserActivity extends AppCompatActivity {


    Button btnUserProfile;
    Button btnAddProject;
    RecyclerView rvUserProjects;

    DocumentReference docRef;
    CollectionReference colRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final User user = getIntent().getParcelableExtra(Constants.USER_PARCEL);

        docRef = FirebaseFirestore.getInstance().collection(Constants.COL_USER).document(user.getUserId());
        colRef = docRef.collection(Constants.COL_PRO);
        btnAddProject = findViewById(R.id.btnAddProjects);
        btnUserProfile = findViewById(R.id.btnUserProfile);
        rvUserProjects = findViewById(R.id.rvUserProjects);

        btnAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UserActivity.this,UserProjectActivity.class);
                intent.putExtra(Constants.PRO_PARCEL,new Project(Constants.PRO_STATUS_INITIALISE,user,null));
                intent.putExtra(Constants.PARCEL_TYPE,"user");
                startActivity(intent);
            }
        });
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

            }
        });
    }
}
