package vandy.mooc.operations;

import java.util.Iterator;

import vandy.mooc.common.GenericAsyncTask;
import vandy.mooc.common.GenericAsyncTaskOps;
import vandy.mooc.common.Utils;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

/**
 * Query all the star'd contacts in a background task.
 */
public class QueryContactsCommand
       extends GenericAsyncTaskOps<Void, Void, Cursor> 
       implements ContactsCommand {
    /**
     * Store a reference to the ContactsOps object.
     */
    private ContactsOps mOps;

    /**
     * Store a reference to the Application context's ContentResolver.
     */
    private ContentResolver mContentResolver;

    /**
     * Observer that's dispatched by the ContentResolver when Contacts
     * change (e.g., are inserted or deleted).
     */
    private final ContentObserver contactsChangeContentObserver =
        new ContentObserver(null) {
            /**
             * Trigger a query and display.
             */
            @Override
            public void onChange (boolean selfChange) {
                execute(null);
            }
        };

    /**
     * Constructor initializes the fields.
     */
    public QueryContactsCommand(ContactsOps ops) {
        // Store the ContactOps and the ContentResolver from the
        // Application context.
        mOps = ops;
        mContentResolver =
            ops.getActivity().getApplicationContext().getContentResolver();

        // Register a ContentObserver that's notified when Contacts
        // change (e.g., are inserted or deleted).
        mContentResolver.registerContentObserver
            (ContactsContract.Contacts.CONTENT_URI,
             true,
             contactsChangeContentObserver);
    }

    /**
     * Run the command.
     */
    @Override
    public void execute(Iterator<String> ignore) {
        // Create a GenericAsyncTask to query the contacts off the UI
        // Thread.
        final GenericAsyncTask<Void,
                               Void,
                               Cursor,
                               QueryContactsCommand> mAsyncTask = 
            new GenericAsyncTask<>(this);

        // Execute the GenericAsyncTask.
        mAsyncTask.execute((Void) null);
    }

    /**
     * Run in a background Thread to avoid blocking the UI Thread.
     */
    @Override
    public Cursor doInBackground(Void... v) {
        // Query the Contacts ContentProvider for the contacts and
        // return them.
        return queryAllContacts(mContentResolver);
    }

    /**
     * The results of the query are displayed in the UI Thread.
     */
    @Override
    public void onPostExecute(Cursor cursor) {
        if (cursor == null
            || cursor.getCount() == 0)
            // Utils.showToast(mOps.getActivity(),
            // "Contacts not found")
            ;
        else {
            mOps.setCursor(cursor);
            mOps.getActivity().displayCursor(cursor);
        }
    }

    /**
     * Synchronously query for contacts in the Contacts
     * ContentProvider.
     */
    public Cursor queryAllContacts(ContentResolver cr) {
        // Columns to query.
        final String columnsToQuery[] =
            new String[] {
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.STARRED 
        };
	
        // Contacts to select.
        final String selection = 
            "((" 
            + Contacts.DISPLAY_NAME 
            + " NOTNULL) AND ("
            + Contacts.DISPLAY_NAME 
            + " != '' ) AND (" 
            + Contacts.STARRED
            + "== 1))";

        // Perform a synchronous (blocking) query on the
        // ContactsContentProvider.
        return cr.query(ContactsContract.RawContacts.CONTENT_URI, 
                        columnsToQuery, 
                        selection,
                        null, 
                        ContactsContract.Contacts._ID
                        + " ASC");
    }
}

