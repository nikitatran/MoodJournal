package com.codingwithmitch.journal;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import java.util.Date;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.util.VerticalSpacingItemDecorator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;


public class NotesListsFragment extends Fragment {

    public interface OnNoteListener{
        void onNoteClick(int position, ArrayList<Note> mNotes);
    }

    private OnNoteListener mOnNoteListener;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;

    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;
    private Snackbar snackbar;

    /*
    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    */

    public NotesListsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // Rename and change types and number of parameters
    /*
    public static NotesListsFragment newInstance(String param1, String param2) {
        NotesListsFragment fragment = new NotesListsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditActivity.class);
                startActivity(intent);
            }
        });

        //initialize RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(mNotes);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);


        mNoteRepository = new NoteRepository(getActivity());
        retrieveNotes();

        snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);

        return view;
    }

    private void retrieveNotes() {
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void deleteNote(Note note) {
        snackbar.setText("Deleted " + "\"" + note.getTitle() + "\"")
                .setAction("Undo", null); //TODO: create undo delete functionality
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNoteTask(note);

        snackbar.show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.d("OnAttach: ", (context instanceof OnNoteListener) + "");
        if (context instanceof OnNoteListener) {
            mOnNoteListener = (OnNoteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNoteListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnNoteListener = null;
    }

    //VIEWHOLDER
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timestamp, title, content;

        public ViewHolder(View itemView) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.note_timestamp);
            title = itemView.findViewById(R.id.note_title);
            content = itemView.findViewById(R.id.note_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("NotesRecyclerAdapter", "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getAdapterPosition(), mNotes);
        }
    }

    //ADAPTER
    public class NotesRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

        private static final String TAG = "NotesRecyclerAdapter";

        private ArrayList<Note> mNotes;

        public NotesRecyclerAdapter(ArrayList<Note> mNotes) {
            this.mNotes = mNotes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_item, parent, false);
            return new ViewHolder(view);
        }

        //This is where you can set the text shown in the list items
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            try {
                Long epoch = Long.parseLong(mNotes.get(position).getTimestamp());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY\nh:mm a");
                dateFormat.setTimeZone(TimeZone.getDefault());
                String timestamp = dateFormat.format(new Date(epoch));


                holder.timestamp.setText(timestamp);
                holder.title.setText(mNotes.get(position).getTitle());
                holder.content.setText(mNotes.get(position).getContent());
            } catch (NullPointerException e) {
                Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
