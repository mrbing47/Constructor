package garg.sarthik.construction_prototype_v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ContActivity extends AppCompatActivity {

    Button btnContProfile;
    RecyclerView rvContProjects;

    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cont);

        Contractor contractor = getIntent().getParcelableExtra(Constants.CONT_PARCEL);

        docRef = FirebaseFirestore.getInstance().collection(Constants.COL_CONT).document(contractor.getContId());

    }
}
