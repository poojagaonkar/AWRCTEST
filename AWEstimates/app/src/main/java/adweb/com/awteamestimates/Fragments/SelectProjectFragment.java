package adweb.com.awteamestimates.Fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Collections2;

import java.io.Console;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import adweb.com.awteamestimates.Adapters.ProjectsAdapter;
import adweb.com.awteamestimates.HomeActivity;
import adweb.com.awteamestimates.IssueSummaryActivity;
import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.Models.GetRoles.RoleIdModel;
import adweb.com.awteamestimates.Models.GetRoles.RoleModel;
import adweb.com.awteamestimates.Models.GetRoles.Roles;
import adweb.com.awteamestimates.Models.ProjectModel;
import adweb.com.awteamestimates.Models.TeamEstimationsRolesDatum;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Service.JiraServices;
import adweb.com.awteamestimates.Utilities.AppConstants;
import adweb.com.awteamestimates.Utilities.ProjectDividerItemDecoration;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
//import adweb.com.awteamestimates.Utilities.DividerItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SelectProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SelectProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectProjectFragment extends Fragment implements ProjectsAdapter.ProjectsAdapterListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TextView txtTemp;
    private Spinner mProjectSpinner;
    private ImageButton btnNext;
    public SharedPreferences mPrefs ;
    public  SharedPreferences.Editor mEdit ;
    private  String mUserName ;
    private  String mSessionUserName;
    private  String mSessionUserValue;
    private  String mBaseUrl;
    private boolean isMenuVisible;
    private Button btnProjectNext;
    private Spinner mRoleSpinner;
    private LinearLayout layoutRole;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private ProjectsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public SelectProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectProjectFragment newInstance(String param1, String param2) {
        SelectProjectFragment fragment = new SelectProjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEdit = mPrefs.edit();
        setHasOptionsMenu(true);
        //setRetainInstance(false);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.select_project_layout, null, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        txtTemp = (TextView)view.findViewById(R.id.txtSelect);
//        mProjectSpinner = (Spinner)view.findViewById(R.id.spinProjectName);
//        mRoleSpinner = (Spinner)view.findViewById(R.id.spinRoleName);
//        layoutRole = (LinearLayout)view.findViewById(R.id.layoutRole);
        recyclerView = view.findViewById(R.id.recycler_view);
        searchView  = view.findViewById(R.id.searchFilterProjects);


        mBaseUrl = mPrefs.getString(getResources().getString(R.string.pref_baseUrl), null);
        mUserName = mPrefs.getString(getResources().getString(R.string.pref_userName), null);

        Toolbar mtoolbar = ((HomeActivity)getActivity()).toolbar;

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

       // btnProjectNext = (Button) mtoolbar.findViewById(R.id.btnProjectNext);

       // btnProjectNext.setVisibility(View.VISIBLE);



        //region Get Project Details
        try {

            if(AppConstants.ProjectTitles == null || AppConstants.ProjectTitles.size() ==0 || AppConstants.isRefreshed ) {
                JiraServices.GetProjectDetails getProjectDetails = new JiraServices.GetProjectDetails(getActivity(), mUserName, mBaseUrl);
                ProjectModel mModel = getProjectDetails.execute().get();

                AppConstants.isRefreshed = false;



                if (mModel != null) {


                    AppConstants.FullProjectList = mModel.getCurrentEstimatedIssue();
                    Collections.reverse(AppConstants.FullProjectList);
                    AppConstants.ProjectTitles = new ArrayList<>();

                    for (CurrentEstimatedIssue mIssue : AppConstants.FullProjectList) {

                        AppConstants.ProjectTitles.add(mIssue.getProjectName());
                        System.out.println(mIssue.getIssueKey() + "\n" + mIssue.getIssueTitle() + "\n" + mIssue.getProjectKey() + "\n" + mIssue.getProjectName());

                    }





                    SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                    searchView.setSearchableInfo(searchManager .getSearchableInfo(getActivity().getComponentName()));
                    searchView.setMaxWidth(Integer.MAX_VALUE);

                  // mAdapter.notifyDataSetChanged();




//                    if(mModel.getIsEstimationsInstalled() !=null && mModel.getIsEstimationsInstalled())
//                    {
//                        AppConstants.isRoleEnabled = mModel.getIsEstimationsInstalled();
//                        layoutRole.setVisibility(View.VISIBLE);
//
//
//
//                    }

                }
                else
                {
                    throw new RuntimeException("Could not fetch project details.");
                }
            }

            mAdapter = new ProjectsAdapter(getActivity(), this);
            recyclerView.setAdapter(mAdapter);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // filter recycler view when query submitted
                    mAdapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String query) {
                    // filter recycler view when text is changed
                    mAdapter.getFilter().filter(query);
                    return false;
                }
            });






        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //endregion


    }

    Object[] convertToObjectArray(Object array) {
        Class ofArray = array.getClass().getComponentType();
        if (ofArray.isPrimitive()) {
            List ar = new ArrayList();
            int length = Array.getLength(array);
            for (int i = 0; i < length; i++) {
                ar.add(Array.get(array, i));
            }
            return ar.toArray();
        }
        else {
            return (Object[]) array;
        }
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        MenuItem menuItem = menu.findItem(R.id.action_refresh);
        menuItem.setVisible(false);


    }

    private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        try {
            super.setMenuVisibility(menuVisible);
            if (menuVisible) {
                isMenuVisible  = menuVisible;
            }
        } catch (Exception e) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCurrentEstimatedIssueSelected(CurrentEstimatedIssue currentEstimatedIssue) {
        //Toast.makeText(getActivity(), "Selected: " + currentEstimatedIssue.getProjectName() + ", " + currentEstimatedIssue.getAvatar().toString(), Toast.LENGTH_LONG).show();

        AppConstants.CurrentSelectedProject = currentEstimatedIssue.getProjectName();
        //AppConstants.CurrentIssueDetails = Collections2.filter(AppConstants.FullProjectList, user -> user.getProjectName().equals(AppConstants.CurrentSelectedProject)).iterator();
        AppConstants.CurrentEstimatedIssue  = currentEstimatedIssue;

//        try {
//            JiraServices.GetRoleDetails getRoleDetails = new JiraServices.GetRoleDetails(getActivity(), mUserName, mBaseUrl, AppConstants.CurrentEstimatedIssue.getIssueKey());
//            RoleModel mModel = getRoleDetails.execute().get();
//
//            if(mModel !=null)
//            {
//
//                AppConstants.ProjectRoleTitles =new ArrayList<String>(mModel.getRoles().values());
//
//                AppConstants.ProjectRoleList = new ArrayList<RoleIdModel>();
//                Iterator it = mModel.getRoles().entrySet().iterator();
//                while (it.hasNext())
//                {
//                    Map.Entry pair = (Map.Entry)it.next();
//                    RoleIdModel pModel = new RoleIdModel();
//                    pModel.setRoleID(String.valueOf(pair.getKey()));
//                    pModel.setRoleName(String.valueOf(pair.getValue()));
//                    if(AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData() != null && AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData().size() >0)
//                    {
//                        for(TeamEstimationsRolesDatum bModel : AppConstants.CurrentEstimatedIssue.getTeamEstimationsRolesData())
//                        {
//                            if(bModel.getRoleid()!= null &&  bModel.getRoleid().equals(pair.getKey()))
//                            {
//                                pModel.setRoleEstimatee(bModel.getEstimateFormatted());
//                            }
//                        }
//
//                    }
//
//                    AppConstants.ProjectRoleList.add(pModel);
//                }
//
//                final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item, AppConstants.ProjectRoleTitles);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                mRoleSpinner.setAdapter(adapter);
//                layoutRole.setVisibility(View.VISIBLE);
//
//                mRoleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        AppConstants.CurrentSelectedRole = adapter.getItem(i);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
//
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        getActivity().startActivity(new Intent(getActivity(), IssueSummaryActivity.class));

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
