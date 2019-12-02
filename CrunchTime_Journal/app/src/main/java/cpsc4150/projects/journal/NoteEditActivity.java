/*
    CrunchTime (Team 8)
    CPSC 4150 Main Project (Dec 2, 2019)
    Nikita Tran (nikitat@clemson.edu)
    Taylor Miller (tjm2@clemson.edu)

    References used:
        1. https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
        2. https://codingwithmitch.com/courses/sqlite-room-persistence-android/
           From "NoteActivity Initialization" lesson to "Closing an activity with Finish()" lesson
 */

package cpsc4150.projects.journal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.projects.journal.R;

import cpsc4150.projects.journal.database.async.AsyncResponse;
import cpsc4150.projects.journal.models.Note;
import cpsc4150.projects.journal.database.NoteRepository;
import cpsc4150.projects.journal.util.ParallelDotsApi;
import cpsc4150.projects.journal.util.Utility;

public class NoteEditActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher,
        AsyncResponse {

    private static final String TAG = "NoteEditActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // UI components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer, mEditContainer;
    private ImageButton mCheck, mBackArrow;
    private View mOverlay;

    // vars
    private boolean mIsNewNote;
    private NoteRepository mNoteRepository;
    private Note mNoteInitial; //note in it's initial state before edits applied
    private Note mNoteFinal; //note with edits applied
    private GestureDetector mGestureDetector;
    private int mMode;

    // api
    ParallelDotsApi api = new ParallelDotsApi();

    /**
     * pre: layout for this activity exists
     * post: Initializes view and note dependent on whether user is creating a new note or editing an existing one
     *
     * Referenced from reference 2.
     *
     * @param savedInstanceState bundle
     */
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
        mOverlay = findViewById(R.id.overlay);

        mEditContainer.setVisibility(View.GONE);

        mNoteRepository = new NoteRepository(this);

        setListeners();

        if (getIncomingIntent()) {
            setNewNoteProperties();
            enableEditMode();
        } else {
            setNoteProperties();
            disableContentInteraction();
        }
    }

    /**
     *  Helper function to save/update notes in database.
     *
     *  pre:
     *      - when creating a new note: note has body content
     *      - when editing existing note: edited content is different from initial content
     *  post:
     *      if new note was created, insert it into database
     *      else update existing note
     */
    private void saveChanges() {
        if (mIsNewNote) {
            //save new note
            mNoteRepository.insertNoteTask(mNoteFinal, this);
            //api call happens in processFinish()
        } else {
            //update existing note
            mNoteRepository.updateNoteTask(mNoteFinal);
            api.setNote(mNoteFinal, mNoteRepository);
            api.apiCall(mNoteFinal.getContent());
        }
    }

    /**
     *  Set Touch/Click listeners on UI elements
     *
     *  pre: UI elements exist
     *  post: UI elements will listen for a click/touch
     *
     *  Referenced from reference 2.
     */
    private void setListeners() {
        mGestureDetector = new GestureDetector(this, this);
        mLinedEditText.setOnTouchListener(this);
        mCheck.setOnClickListener(this);
        mViewTitle.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
        mOverlay.setOnClickListener(this);
    }

    /**
     * Checks to see if intent sent in from MainActivity has an extra
     *
     * pre: NoteEditActivity is started from another activity
     * post:
     *  if intent has extra, set pending note to data intent has,
     *  else do nothing
     *
     * Referenced from reference 2.
     *
     * @return true if no extra existed; false if it does exist
     */
    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_note")) {
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

    /**
     *  Helper function to disable user interaction with content EditText
     *
     *  pre: content EditText was interactable
     *  post: user cannot enter text into EditText
     *
     *  Referenced from reference 2.
     *
     *  note: does NOT automatically hide soft keyboard from screen
     */
    private void disableContentInteraction() {
        mLinedEditText.setKeyListener(null);
        mLinedEditText.setFocusable(false);
        mLinedEditText.setFocusableInTouchMode(false);
        mLinedEditText.setCursorVisible(false);
        mLinedEditText.clearFocus();
    }

    /**
     *  Helper function to enable user interaction with content EditText
     *
     *  pre: content EditText interaction was disabled
     *  post: user can enter text into content EditText
     *
     *  Referenced from reference 2.
     *
     *  note: does NOT automatically show soft keyboard in screen
     */
    private void enableContentInteraction() {
        mLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        mLinedEditText.setFocusable(true);
        mLinedEditText.setFocusableInTouchMode(true);
        mLinedEditText.setCursorVisible(true);
        mLinedEditText.requestFocus();
    }

    /**
     *  Helper function to show/hide soft keyboard.
     *
     *  pre: enableEditMode() was called and (EditText tapped OR check button in toolbar clicked)
     *  post: soft keyboard is shown/hidden from screen
     *
     * @param show true = show keyboard; false = hide
     */
    private void showSoftKeyboard(boolean show){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(show) imm.showSoftInput(view, 0);
            else imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     *  Changes view to be in edit mode.
     *
     *  pre: (MainActivity's floating action button was clicked OR edit button was clicked) AND EditText was tapped
     *  post:
     *   - toolbar shows check button in place of back button
     *   - title is EditText instead of TextView
     *   - user can interact with EditText
     *   - soft keyboard shown on screen
     */
    private void enableEditMode() {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
        showSoftKeyboard(true);
    }

    /**
     *  Changes view to be in view mode.
     *
     *  pre: enableEditMode() was called beforehand AND check button in toolbar clicked
     *  post:
     *   - toolbar shows back button in place of check button
     *   - title is TextView instead of EditText
     *   - user cannot interact with EditText
     *   - soft keyboard hidden from screen
     */
    private void disableEditMode() {
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        showSoftKeyboard(false);
        disableContentInteraction();
    }

    /**
     * Helper function to remove empty characters (new line and spaces) from string.
     *
     * pre:
     * post: empty characters are removed from toTrim
     *
     * @param toTrim input that will be trimmed
     * @return trimmed string
     */
    private String trimString(String toTrim) {
        String str = toTrim;
        str = str.replace("\n", "");
        str = str.replace(" ", "");
        return str;
    }

    /**
     *  Validates note content and saves/updates note into database
     *
     *  pre: user pressed back arrow in toolbar or back softkey
     *  post:
     *   - if note is new and content is not empty after trim,
     *   note created with user input from EditText fields is added into database
     *   - if note already exists and content differs original note,
     *   update note in database with user input from EditText fields
     */
    private void saveToDatabase() {
        String content = trimString(mLinedEditText.getText().toString());
        String title = trimString(mEditTitle.getText().toString());

        //only save note to database if there is content in the entry after trimming
        //title can be blank
        if (content.length() > 0) {
            //if title remains blank, set title to "Untitled note"
            if (title.length() == 0) {
                mNoteFinal.setTitle("Untitled note");
            } else mNoteFinal.setTitle(mEditTitle.getText().toString());

            mNoteFinal.setContent(mLinedEditText.getText().toString());

            String timestamp = Utility.getCurrentEpochMilli();
            mNoteFinal.setTimestamp(timestamp);

            //If the note was altered, save it.
            if (!mNoteFinal.getContent().equals(mNoteInitial.getContent())
                    || !mNoteFinal.getTitle().equals(mNoteInitial.getTitle())) {
                saveChanges();
            }
        }
    }

    /**
     *  Set view to default text and create new pending notes.
     *
     *  pre: mIsNewNote is true
     *  post: View shows default text
     *
     *  Referenced from reference 2.
     */
    private void setNewNoteProperties() {
        mViewTitle.setText("Untitled note");
        mEditTitle.setHint("Your title here");
        mLinedEditText.setHint("Tap to type");

        mNoteFinal = new Note();
        mNoteInitial = new Note();
    }

    /**
     *  Set view to default text and create new pending notes.
     *
     *  pre: mIsNewNote is false; intent from MainActivity contains note extra
     *  post: View displays data fields retrieved from intent from MainActivity
     *
     *  Referenced from reference 2.
     */
    private void setNoteProperties() {
        mViewTitle.setText(mNoteInitial.getTitle());
        mEditTitle.setText(mNoteInitial.getTitle());
        mLinedEditText.setText(mNoteInitial.getContent());
    }

    /* MAKE CONTENT EDITTEXT INTERACTABLE AFTER SINGLE/DOUBLE-TAPPING ON IT */
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
        enableEditMode();
        return false;
    }
    /* ------------------------------------------------------------------- */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back_arrow: {
                saveToDatabase();

                //send back edited title and content to DetailsActivity so
                //view seen after pressing back can reflect edits user name
                Intent returnIntent = new Intent();
                returnIntent.putExtra("title", mNoteFinal.getTitle());
                returnIntent.putExtra("content", mNoteFinal.getContent());
                setResult(RESULT_OK, returnIntent);

                finish();
                break;
            }
            case R.id.toolbar_check: {
                disableEditMode();

                //Reset listener for first click on content EditText
                mOverlay.setClickable(true);
                break;
            }
            case R.id.note_text_title: {
                enableEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }

            //Overlay is used to catch first click on EditText and set cursor to end
            //Cursor cannot be set to end in Click or Touch listeners because
            //every tap user makes on screen while in edit mode will set cursor to the end
            //which is undesireable if user taps in the middle of a word to fix a typo or something
            case R.id.overlay: {
                //On first click, set cursor to end of text and turn itself off until
                //edit mode disabled (check clicked)
                mOverlay.setClickable(false);
                mLinedEditText.setSelection(mLinedEditText.getText().length());
                enableEditMode();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
            //Turn off edit mode
            onClick(mCheck);
        } else {
            //Do behavior defined in onClick
            onClick(mBackArrow);
        }
    }

    /* KEEP EDIT MODE ACTIVE IF SCREEN ROTATES */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }
    /* ---- Referenced from reference 2. ---- */

    //Update TextView version of title (edit mode disabled version) when EditText title changes
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    /**
     * This function is part of an interface (AsyncResponse), used to listen for when
     * insertion into database is completed.
     *
     * After insertion, update mNoteFinal's ID field with int from output.
     * This is necessary because the note will be sent to the Api call,
     * and within the api call, the note is updated with emotion values.
     *
     * The note cannot be updated as is because it's ID is always 0, which is not the ID
     * it has in the database. Without setting the ID, emotion values will not be added
     * to the note until the user saves an edit to that note.
     *
     * pre: mIsNewNote is true and saveChanges() called
     * post: api sets emotion values of new note
     *
     * Reference 1 used here.
     *
     * @param output ID of the note that got inserted
     */
    @Override
    public void processFinish(long output) {
        mNoteFinal.setId((int) output);
        //need to update new note with ID from database so it can be updated with emotion values

        api.setNote(mNoteFinal, mNoteRepository);
        api.apiCall(mNoteFinal.getContent());
    }

    /* HERE LIES UNUSED FUNCTIONS THAT MUST BE DECLARED FOR IMPLEMENTED INTERFACES */

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) { return false; }

    @Override
    public boolean onDown(MotionEvent motionEvent) { return false; }

    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) { return false; }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }

    @Override
    public void onLongPress(MotionEvent motionEvent) {}

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }

    //////////////

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable) {}
}



















