package garg.sarthik.construction_prototype_v2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;

public class UserProjectActivity extends AppCompatActivity implements LocationListener {

    EditText etProName;
    EditText etProId;
    EditText etUserName;
    EditText etUserId;
    EditText etContName;
    EditText etContId;
    EditText etProLong;
    EditText etProLat;
    EditText etProDate;

    TextView tvProStatus;

    RecyclerView rvProTimeline;

    Button btnSubmit;
    Button btnReport;

    LocationManager locationManager;

    Project project;
    String loginType;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userproject);

        project = getIntent().getParcelableExtra(Constants.PRO_PARCEL);
        loginType = getIntent().getStringExtra(Constants.PARCEL_TYPE);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        firestore = FirebaseFirestore.getInstance();

        tvProStatus = findViewById(R.id.tvProStatus);

        etProName = findViewById(R.id.etProName);
        etProId = findViewById(R.id.etProId);
        etUserName = findViewById(R.id.etUserName);
        etUserId = findViewById(R.id.etUserId);
        etContName = findViewById(R.id.etContName);
        etContId = findViewById(R.id.etContId);
        etProLong = findViewById(R.id.etProLong);
        etProLat = findViewById(R.id.etProLat);
        etProDate = findViewById(R.id.etProDate);

        rvProTimeline = findViewById(R.id.rvProTimeline);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnReport = findViewById(R.id.btnReport);

        etProLat.setEnabled(false);
        etProLong.setEnabled(false);
        etProDate.setEnabled(false);
        etUserName.setEnabled(false);
        etUserId.setEnabled(false);
        btnReport.setVisibility(View.INVISIBLE);

        etUserName.setText(project.getUser().getUserName());
        etUserId.setText(project.getUser().getUserId());

        if (project.getProStatus().equals(Constants.PRO_STATUS_INITIALISE)) {

            tvProStatus.setText(project.getProStatus());
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String contId = etContId.getText().toString();
                    String contName = etContName.getText().toString();
                    String proName = etProName.getText().toString();
                    String proId = etProId.getText().toString();

                    if (proId.isEmpty() || proName.isEmpty() || contId.isEmpty() || contName.isEmpty()) {
                        Toast.makeText(UserProjectActivity.this, "Editable fields required to fill", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    project.setProId(proId);
                    project.setProName(proName);
                    project.setContractor(new Contractor(contName, contId));
                    project.setProStatus(Constants.PRO_STATUS_PENDING);

                    firestore.collection(Constants.COL_CONT)
                            .document(project.getContractor().getContId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists())
                            {
                                firestore.collection(Constants.COL_USER)
                                        .document(project.getUser().getUserId())
                                        .collection(Constants.COL_PRO)
                                        .document(project.getProId()).set(project);
                                firestore.collection(Constants.COL_CONT)
                                        .document(project.getContractor().getContId())
                                        .collection(Constants.COL_PRO)
                                        .document(project.getProId()).set(project);

                                Toast.makeText(UserProjectActivity.this, "Project submitted successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(UserProjectActivity.this, "No Contractor exist with this ID", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
        if (project.getProStatus().equals(Constants.PRO_STATUS_PENDING)) {

            btnSubmit.setText("Start Project");
            etContName.setEnabled(false);
            etContId.setEnabled(false);
            etProName.setEnabled(false);
            etProId.setEnabled(false);

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    requestLocation();
                }
            });
        }

        if (project.getProStatus().equals(Constants.PRO_STATUS_IN_PROGRESS)) {

            btnSubmit.setText("Finish Project");
            if (loginType.equals(Constants.COL_CONT))
                btnReport.setVisibility(View.VISIBLE);

            etContName.setEnabled(false);
            etContId.setEnabled(false);
            etProName.setEnabled(false);
            etProId.setEnabled(false);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    project.setProStatus(Constants.PRO_STATUS_FINISHED);
                    firestore.collection(Constants.COL_USER)
                            .document(project.getUser().getUserId())
                            .collection(Constants.COL_PRO)
                            .document(project.getProId()).set(project);
                }
            });

            btnReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserProjectActivity.this, ReportActivity.class).putExtra(Constants.PRO_PARCEL, project));
                }
            });

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            locationSuccess();
        } else
            Toast.makeText(this, "Need Location", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    private void locationSuccess() {

        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
    }

    public void requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            int requestCodeForPermission = 420;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, requestCodeForPermission);

        } else {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Location is not enabled", Toast.LENGTH_SHORT).show();
            } else {

                locationSuccess();
            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

        project.setProLatitude(location.getLatitude());
        project.setProLongitude(location.getLongitude());
        project.setProDate(DateFormat.getDateTimeInstance().format(new Date()));
        project.setProStatus(Constants.PRO_STATUS_IN_PROGRESS);

        firestore.collection(Constants.COL_USER)
                .document(project.getUser().getUserId())
                .collection(Constants.COL_PRO)
                .document(project.getProId()).set(project);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
