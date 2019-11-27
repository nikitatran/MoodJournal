package com.codingwithmitch.journal;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingwithmitch.journal.database.async.AsyncResponse;
import com.codingwithmitch.journal.models.Note;
import com.codingwithmitch.journal.database.NoteRepository;
import com.codingwithmitch.journal.util.ParallelDotsApi;
import com.codingwithmitch.journal.util.Utility;

public class NoteEditActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher,
        AsyncResponse
{

    private static final String TAG = "NoteEditActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // UI components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer, mEditContainer;
    private ImageButton mCheck, mBackArrow;


    // vars
    private boolean mIsNewNote;
    private Note mNoteInitial;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mNoteFinal;
    //Api Call
    ParallelDotsApi api = new ParallelDotsApi ();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);
        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mEditContainer = findViewById(R.id.edit_container);

        mEditContainer.setVisibility(View.GONE);

        mNoteRepository = new NoteRepository(this);

        setListeners();

        if(getIncomingIntent()){
            setNewNoteProperties();
            enableEditMode();
        }
        else{
            setNoteProperties();
            disableContentInteraction();
        }
    }

    //Save Changes either changes based if it's a new note or a note that's been updated
    private void saveChanges(){
        if(mIsNewNote){
            saveNewNote();
            Log.d("whatstheID", mNoteFinal.getId()+" (in savechanges)");
        }else{
            updateNote();
            api.setNote(mNoteFinal, mNoteRepository, false);
            api.apiCall(mNoteFinal.getContent());
        }
    }

    public void updateNote() {
        mNoteRepository.updateNoteTask(mNoteFinal);
    }

    public void saveNewNote() {
        mNoteRepository.insertNoteTask(mNoteFinal, this);
        /*
            issue that occurred:
            so the logic here is new note created -> api call -> api edits database row
            that way the note can appear instantaneously to the user while the analysis runs in the background
            otherwise, the new note would only be created once the api call is done
            so there would be a lag in when the note appears in the recyclerview

            when creating a new note, the id aka primary key is always at 0 for some reason
            which does not reflect the actual primary key when you look in the database
            so updating a newly created entry wouldn't work because DAO can't find primary key 0
            which is why i had the issue where newly created notes would not have emotion values,
            while edited notes did. edited notes had the correct id so the DAO could update the
            corresponding entry.

            the AsyncResponse interface is used to grab the primary id returned by InsertAsyncTask and
            pass it over to this class so it can be used
         */
    }

    private void setListeners(){
        mGestureDetector = new GestureDetector(this, this);
        mLinedEditText.setOnTouchListener(this);
        mCheck.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_note")){
            mNoteInitial = getIntent().getParcelableExtra("selected_note");

            mNoteFinal = new Note();
            mNoteFinal.setTitle(mNoteInitial.getTitle());
            mNoteFinal.setContent(mNoteInitial.getContent());
            mNoteFinal.setTimestamp(mNoteInitial.getTimestamp());
            mNoteFinal.setId(mNoteInitial.getId());

            mMode = EDIT_MODE_ENABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;
    }

    private void disableContentInteraction(){
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    private void enableContentInteraction(){
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();

        mLinedEditText.setSelection(mLinedEditText.getText().length()); //set cursor to end of entry text if there is any
    }

    private void enableEditMode(){
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();

        //show soft keyboard
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            imm.showSoftInput(view, 0);
        }

        //enableContentInteraction();
    }

    private void disableEditMode() {
        Log.d(TAG, "disableEditMode: called.");
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        //hide soft keyboard
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        disableContentInteraction();
    }
    //take out empty characters (new line and spaces) from string
    private String trimString(String toTrim){
        String str = toTrim;
        str = str.replace("\n", "");
        str = str.replace(" ", "");
        return str;
    }

    private void saveToDatabase(){
        // Check if they typed anything into the note. Don't want to save an empty note.
        String content = trimString(mLinedEditText.getText().toString());
        String title = trimString(mEditTitle.getText().toString());

        //only save note to database if there is content in the entry -- title can be blank
        if(content.length() > 0){
            //if title remains blank, set title to "Untitled note"
            if(title.length() == 0){
                //mViewTitle.setText("Untitled note");
                mNoteFinal.setTitle("Untitled note");
            }
            else mNoteFinal.setTitle(mEditTitle.getText().toString());
            mNoteFinal.setContent(mLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentEpochMilli();
            mNoteFinal.setTimestamp(timestamp);

            Log.d(TAG, "disableEditMode: initial: " + mNoteInitial.toString());
            Log.d(TAG, "disableEditMode: final: " + mNoteFinal.toString());

            // If the note was altered, save it.
            if(!mNoteFinal.getContent().equals(mNoteInitial.getContent())
                    || !mNoteFinal.getTitle().equals(mNoteInitial.getTitle())){
                Log.d(TAG, "disableEditMode: saving edited note");

                saveChanges();
                //^ ensures a row exists for this entry
                //then set the emotion column values
                //api.setNote(mNoteFinal, mNoteRepository,false); //pass in the note so it knows which row in database to update
                //api.apiCall(mNoteFinal.getContent()); //start api call
            }
        }
    }

    private void setNewNoteProperties(){
        //mViewTitle.setText("Note Title");
        mViewTitle.setText("Untitled note");
        //mEditTitle.setText("Note Title");
        mEditTitle.setHint("Your title here");
        mLinedEditText.setHint("Tap to type");

        mNoteFinal = new Note();
        mNoteInitial = new Note();
        //mNoteInitial.setTitle("Note Title");
    }

    private void setNoteProperties(){
        mViewTitle.setText(mNoteInitial.getTitle());
        mEditTitle.setText(mNoteInitial.getTitle());
        mLinedEditText.setText(mNoteInitial.getContent());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        Log.d(TAG, "onDoubleTap: double tapped.");
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.toolbar_back_arrow:{
                saveToDatabase();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("title",mNoteFinal.getTitle());
                returnIntent.putExtra("content",mNoteFinal.getContent());
                setResult(RESULT_OK,returnIntent);
                finish();
                break;
            }
            case R.id.toolbar_check:{
                disableEditMode();
                break;
            }
            case R.id.note_text_title:{
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED){
            onClick(mCheck);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void processFinish(long output) {
        mNoteFinal.setId((int)output);
        Log.d("whatstheID", mNoteFinal.getId()+" (in processFinish)");

        api.setNote(mNoteFinal, mNoteRepository, true);
        api.apiCall(mNoteFinal.getContent());
    }
}



















