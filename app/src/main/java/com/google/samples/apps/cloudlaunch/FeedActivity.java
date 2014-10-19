package com.google.samples.apps.cloudlaunch;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.identitytoolkit.client.GitkitClient;
import com.google.identitytoolkit.model.IdToken;
import com.google.samples.apps.cloudlaunch.gitkit.LoginActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.encodeToString;


public class FeedActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Must set content view before calling parent class in order for navigation drawer to work
        setContentView(R.layout.base_activity);
        super.onCreate(savedInstanceState);

        //Initializes the application with the proper default settings
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //Replace the current fragment with the Feed/Camera fragment via transaction
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FeedFragment())
                    .commit();
        }

    }

    /**
     * Fragment that displays the main feed, as well as creates the photo capture capability
     */
    public static class FeedFragment extends Fragment {
        static final int REQUEST_IMAGE_CAPTURE = 1;
        static final int MEDIA_TYPE_IMAGE = 2;
        protected Uri mMediaUri;
        protected File mPhotoFile;
        private final String LOG_TAG = FeedFragment.class.getSimpleName();

        public FeedFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
            //required to display menu with the camera button
            setHasOptionsMenu(true);
            ListView listView = (ListView) rootView.findViewById(R.id.listview_timeline);
            //required initialization to request URLs
            new AbelanaThings(getActivity(), "");
            //set the adapter for the feed listview
            listView.setAdapter(new FeedAdapter(getActivity()));
            return rootView;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_camera) {
                takePicture();
            }
            return super.onOptionsItemSelected(item);

        }

        //Lifecycle method called after the photo is taken by the user
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                //add the photo to the Gallery
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(mMediaUri);
                getActivity().sendBroadcast(mediaScanIntent);

                //upload the photo to the cloud
                try {
                    InputStream photoStream = new FileInputStream(mPhotoFile);
                    new AbelanaUpload(photoStream, "test");
                    Toast.makeText(getActivity(),
                            getActivity().getString(R.string.photo_upload_success_message), Toast.LENGTH_SHORT)
                            .show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else if (resultCode != RESULT_CANCELED) {
                Toast.makeText(getActivity(), "error!", Toast.LENGTH_LONG).show();
            }
        }

        private void takePicture() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //file to save in the cloud
            mPhotoFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            //Uri to store the photo locally in the Android Gallery
            mMediaUri = Uri.fromFile(mPhotoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }

        /** Create a File for saving an image or video */
        private File getOutputMediaFile(int type){
            String appName = getActivity().getString(R.string.app_name);
            if (isExternalStorageAvailable()) {
                // To be safe, you should check that the SDCard is mounted
                // using Environment.getExternalStorageState() before doing this.

                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), appName);
                // This location works best if you want the created images to be shared
                // between applications and persist after your app has been uninstalled.

                // Create the storage directory if it does not exist
                if (! mediaStorageDir.exists()){
                    if (! mediaStorageDir.mkdirs()){
                        Log.d(appName, "failed to create directory");
                        return null;
                    }
                }

                /* Create a media file name*/
                //Encode localId from GitKit in base64
                GitkitClient client = LoginActivity.client;
                IdToken token = client.getSavedIdToken();
                String localId = token.getLocalId();
                byte[] localIdBytes = localId.getBytes();
                String localIdB64 = encodeToString(localIdBytes, DEFAULT);

                //Generate 8 digit random number and encode it in base64
                Random rand = new Random();
                StringBuilder rand8Dig = new StringBuilder(8);
                for (int i=0; i < 8; i++) {
                    rand8Dig.append((char)('0' + rand.nextInt(10)));
                }
                String randNum = rand8Dig.toString();
                byte[] randNumBytes = randNum.getBytes();
                String randNumB64 = encodeToString(randNumBytes, DEFAULT);

                String fileName = localIdB64 + '.' + randNumB64;

                //Determine if the photo is public or private
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Boolean accountVisibility = sharedPref.getBoolean(SettingsActivity.KEY_PREF_PROFILE_VISIBILITY, false);
                Log.v(LOG_TAG, "Account visibility is: " + accountVisibility);
                if (accountVisibility) {
                    fileName += 'F';
                } else {
                    fileName += 'P';
                }
                Log.v(LOG_TAG, "File name is " + fileName);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File mediaFile;
                if (type == MEDIA_TYPE_IMAGE){
                    mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                            fileName + ".jpg");
                    Log.d(LOG_TAG, "File: " + Uri.fromFile(mediaFile));
                    return mediaFile;
                } else {
                    return null;
                }
            } else {
                return null;
            }

        }

        private static boolean isExternalStorageAvailable() {
            String state = Environment.getExternalStorageState();

            if (state.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
