package adweb.com.awteamestimates;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import adweb.com.awteamestimates.Fragments.IssueListDialogFragment;

public class IssueSummaryActivity extends AppCompatActivity implements  IssueListDialogFragment.Listener{

    private ArrayList<CharSequence> optionsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View view = getLayoutInflater().inflate(R.layout.fragment_issue_list_dialog, null);

        optionsList = new ArrayList<>();
        optionsList.add("Estimate Issue");
        optionsList.add("More Details");


        IssueListDialogFragment.newInstance(optionsList).show(this.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onIssueClicked(int position) {

    }

}
