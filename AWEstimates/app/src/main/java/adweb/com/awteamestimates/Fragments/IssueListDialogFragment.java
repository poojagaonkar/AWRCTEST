package adweb.com.awteamestimates.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adweb.com.awteamestimates.R;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     IssueListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link IssueListDialogFragment.Listener}.</p>
 */
public class IssueListDialogFragment extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_LIST= "item_list";
    private Listener mListener;
    private List<String> optionsList;

    // TODO: Customize parameters
    public static IssueListDialogFragment newInstance(ArrayList<CharSequence> mOptionsList) {
        final IssueListDialogFragment fragment = new IssueListDialogFragment();
        final Bundle args = new Bundle();
        args.putCharSequenceArrayList(ARG_ITEM_LIST, mOptionsList);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_issue_list_dialog, container, false);


        return  contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        final RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new IssueAdapter(getArguments().getCharSequenceArrayList(ARG_ITEM_LIST)));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public interface Listener {
        void onIssueClicked(int position);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            // TODO: Customize the item layout
            super(inflater.inflate(R.layout.fragment_issue_list_dialog_item, parent, false));
            text = (TextView) itemView.findViewById(R.id.textOption);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onIssueClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class IssueAdapter extends RecyclerView.Adapter<ViewHolder> {

        private final int mItemCount;
        private final ArrayList<CharSequence> mItemList;

        IssueAdapter(ArrayList<CharSequence> itemList) {
            mItemCount = itemList.size();
            this.mItemList = itemList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.text.setText(mItemList.get(position));
        }

        @Override
        public int getItemCount() {
            return mItemCount;
        }

    }

}
