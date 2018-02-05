package adweb.com.awteamestimates;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;

public class ProjectEstimateActivity extends AppCompatActivity {

    private TableLayout tableFooterLayout;
    private Button btnExpandExtimates;
    private EditText etWeeks;
    private  ImageButton btnAddWeeks;
    private  ImageButton btnRemoveWeeks;
    private int weekCounter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_estimate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tableFooterLayout = (TableLayout)findViewById(R.id.tableSlidingFooter);
        btnExpandExtimates = (Button)findViewById(R.id.btnEstimates);
        btnAddWeeks = findViewById(R.id.btnCounterUpW);
        btnRemoveWeeks = findViewById(R.id.btnCounterDownW);
        etWeeks = findViewById(R.id.editDays);

        tableFooterLayout.setVisibility(View.INVISIBLE);
        btnExpandExtimates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableFooterLayout.getVisibility() == View.INVISIBLE) {
                    // Prepare the View for the animation
                    tableFooterLayout.setVisibility(View.VISIBLE);
                    tableFooterLayout.setAlpha(0.0f);

                    // Start the animation
                    tableFooterLayout.animate()
                            .translationY(20)
                            .alpha(1.0f)
                            .setListener(null);                }
                else {

                    tableFooterLayout.animate().translationY(0);
                    tableFooterLayout.setVisibility(View.INVISIBLE);

                }
            }
        });

        btnAddWeeks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    weekCounter++;
                    etWeeks.setText(weekCounter);
                }
                catch (Exception xe)
                {
                    xe.printStackTrace();
                }
            }
        });
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
