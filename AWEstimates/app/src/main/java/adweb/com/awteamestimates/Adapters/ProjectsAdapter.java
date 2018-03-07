package adweb.com.awteamestimates.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import java.util.List;

import adweb.com.awteamestimates.Models.CurrentEstimatedIssue;
import adweb.com.awteamestimates.R;
import adweb.com.awteamestimates.Utilities.AppConstants;

/**
 * Created by PoojaGaonkar on 3/7/2018.
 */

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.MyViewHolder> implements  Filterable  {
    private  List<CurrentEstimatedIssue> projectList;
    private  List<CurrentEstimatedIssue> projectListFiltered;
    private Context context;

    private ProjectsAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.txtprojectName);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected CurrentEstimatedIssue in callback
                    listener.onCurrentEstimatedIssueSelected(projectListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ProjectsAdapter(Context context, ProjectsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.projectList = AppConstants.FullProjectList;
        this.projectListFiltered = AppConstants.FullProjectList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_recycler_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CurrentEstimatedIssue mProject = projectListFiltered.get(position);
        holder.name.setText(mProject.getProjectName());


        //TODO: Uncomment this after image url is set in api
//        Picasso.with(context)
//                .load(mProject.getAvatar())
//                .placeholder(R.drawable.ic_description_blue_grey_600_36dp)
//                .error(R.drawable.ic_description_blue_grey_600_36dp)
//                .centerCrop()
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return projectListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    projectListFiltered = projectList;
                } else {
                    List<CurrentEstimatedIssue> filteredList = new ArrayList<>();
                    for (CurrentEstimatedIssue row : projectList) {

                        // name match condition. this might differ depending on your requirement
                        if (row.getProjectName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    projectListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = projectListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                projectListFiltered = (ArrayList<CurrentEstimatedIssue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ProjectsAdapterListener {
        void onCurrentEstimatedIssueSelected(CurrentEstimatedIssue currentEstimatedIssue);
    }
}
