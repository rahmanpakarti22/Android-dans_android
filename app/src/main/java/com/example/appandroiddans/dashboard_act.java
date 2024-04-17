package com.example.appandroiddans;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class dashboard_act extends AppCompatActivity {

    ImageView profil_pic;
    TextView name, email;
    Button logout;
    RecyclerView job_list_rv;
    RequestQueue requestQueue;
    List<Position> positionList;
    ProgressBar progressBar;
    PositionAdapter adapter;
    SearchView pencarian_job, pencarian_lokasi;
    CheckBox checkbox_fulltime;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        profil_pic = findViewById(R.id.profil_pic);
        name   = findViewById(R.id.name);
        email  = findViewById(R.id.email);
        logout = findViewById(R.id.logout);
        checkbox_fulltime = findViewById(R.id.checkbox_fulltime);

        loadusedataGoogle();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(dashboard_act.this, login_act.class);
                startActivity(intent);
                finish();
            }
        });

        checkbox_fulltime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fetchjobData(pencarian_job.getQuery().toString(), pencarian_lokasi.getQuery().toString(), isChecked);
            }
        });

        pencarian_lokasi = findViewById(R.id.pencarian_lokasi);
        pencarian_lokasi.clearFocus();
        pencarian_lokasi.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchjobData(query, pencarian_lokasi.getQuery().toString(), checkbox_fulltime.isChecked());;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchjobData(newText, pencarian_lokasi.getQuery().toString(), checkbox_fulltime.isChecked());
                return true;
            }
        });

        pencarian_job = findViewById(R.id.pencarian_job);
        pencarian_job.clearFocus();
        pencarian_job.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchjobData(pencarian_job.getQuery().toString(), query, checkbox_fulltime.isChecked());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchjobData(pencarian_job.getQuery().toString(), newText, checkbox_fulltime.isChecked());
                return true;
            }
        });

        job_list_rv = findViewById(R.id.job_list_rv);
        progressBar = findViewById(R.id.progress_bar);

        job_list_rv.setHasFixedSize(true);
        job_list_rv.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);
        positionList = new ArrayList<>();
        adapter = new PositionAdapter(this, positionList);
        job_list_rv.setAdapter(adapter);

        fetchjobData("","", false);
    }

    private void fetchjobData(String titleQuery, String locationQuery, boolean isFullTimeChecked) {
        String url = "https://dev6.dansmultipro.com/api/recruitment/positions.json";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                positionList.clear();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String Title = jsonObject.getString("title");
                        String Company = jsonObject.getString("company");
                        String Location = jsonObject.getString("location");
                        String logo_com = jsonObject.getString("company_logo");
                        String desc = jsonObject.getString("description");
                        String url = jsonObject.getString("url");
                        String type = jsonObject.getString("type");

                        Position position = new Position(Title, Company, Location, type, logo_com, desc, url);

                        boolean isTitleMatch = titleQuery.isEmpty() || position.getTitle().toLowerCase().contains(titleQuery.toLowerCase());
                        boolean isLocationMatch = locationQuery.isEmpty() || position.getLocation().toLowerCase().contains(locationQuery.toLowerCase());
                        boolean isTypeMatch = !isFullTimeChecked || position.getType().equalsIgnoreCase("Full Time");

                        if (isTitleMatch && isLocationMatch && isTypeMatch) {
                            positionList.add(position);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();

                if (positionList.isEmpty()) {
                    Toast.makeText(dashboard_act.this, "Tidak ada data", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(dashboard_act.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void loadusedataGoogle()
    {
        auth = FirebaseAuth.getInstance();
        Glide.with(dashboard_act.this).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(profil_pic);
        name.setText(auth.getCurrentUser().getDisplayName());
        email.setText(auth.getCurrentUser().getEmail());
    }
}
