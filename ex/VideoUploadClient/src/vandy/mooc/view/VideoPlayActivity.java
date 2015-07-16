package vandy.mooc.view;

import java.text.DecimalFormat;

import vandy.mooc.R;
import vandy.mooc.common.GenericActivity;
import vandy.mooc.model.mediator.webdata.Video;
import vandy.mooc.model.services.DownloadVideoService;
import vandy.mooc.model.services.UploadVideoService;
import vandy.mooc.presenter.VideoOps;
import vandy.mooc.utils.Constants;
import vandy.mooc.view.ui.VideoAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VideoPlayActivity extends GenericActivity<VideoOps.View, VideoOps> 
						implements VideoOps.View{

	private RatingBar ratingBar;
	private TextView txtAvgRatingValue;
	private Button downloadButton;
	private Button playButton;
	private DownloadResultReceiver mDownloadResultReceiver;
	private ImageView imageView;
	private Bitmap bMap;
	private String dataUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		
		
		setContentView(R.layout.video_play_activity);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		
		downloadButton = (Button) findViewById(R.id.download);
		
		final Bundle bundle = getIntent().getExtras();
		dataUrl = bundle.getString(Constants.DATA_URL);
		bMap = ThumbnailUtils.createVideoThumbnail(dataUrl, MediaStore.Video.Thumbnails.MINI_KIND);
		imageView.setImageBitmap(bMap);
		
		boolean fileExists = bundle.getBoolean(Constants.FILE_EXISTS);
		
		downloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Long id = bundle.getLong(Constants.VIDEO_ID);
				String dataUrl = bundle.getString(Constants.DATA_URL);
				Bundle data = new Bundle();
				data.putLong(Constants.VIDEO_ID, id);
				data.putString(Constants.DATA_URL, dataUrl);
				getOps().downloadVideo(data);
			}
		});
		
		playButton = (Button) findViewById(R.id.play);
		
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String dataUrl = bundle.getString(Constants.DATA_URL);
				
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String type = "video/*";
				intent.setDataAndType(Uri.parse(dataUrl), type);
				startActivity(intent);
			}
		});
		
		if(fileExists){
			downloadButton.setEnabled(false);
		}else{
			playButton.setEnabled(false);
		}
		
		txtAvgRatingValue = (TextView) findViewById(R.id.txtAvgRatingValue);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		double rating = bundle.getDouble(Constants.RATING);
		ratingBar.setRating((float) rating);
		txtAvgRatingValue.setText(new DecimalFormat("#0.00").format(rating));
		
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
	 
				String dataUrl = bundle.getString(Constants.DATA_URL);
				int position = bundle.getInt(Constants.POSITION);
				Bundle data = new Bundle();
				data.putString(Constants.DATA_URL, dataUrl);
				data.putInt(Constants.POSITION, position);
				data.putDouble(Constants.RATING, ratingBar.getRating());
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
				Video video = getOps().updateVideo(data);
				double starRating = video.getStarRating();
				txtAvgRatingValue.setText(new DecimalFormat("#0.00").format(starRating));
				ratingBar.setRating((float)starRating);
			}
		});
		
		// Receiver for the notification.
        mDownloadResultReceiver =
            new DownloadResultReceiver();
		
		// Invoke the special onCreate() method in GenericActivity,
        // passing in the VideoOps class to instantiate/manage and
        // "this" to provide VideoOps with the VideoOps.View instance.
        super.onCreate(savedInstanceState,
                       VideoOps.class,
                       this);
	}

	@Override
	public void setAdapter(VideoAdapter videoAdapter) {
		// TODO Auto-generated method stub
		// Do nothing.
	}
	
	 /**
     *  Hook method that is called when user resumes activity
     *  from paused state, onPause(). 
     */
    @Override
    protected void onResume() {
        // Call up to the superclass.
        super.onResume();
        // Register BroadcastReceiver that receives result from
        // DownloadVideoService when a video upload completes.
        registerReceiver();
    }
	
	/**
     * Register a BroadcastReceiver that receives a result from the
     * DownloadVideoService when a video upload completes.
     */
    private void registerReceiver() {
        
        // Create an Intent filter that handles Intents from the
        // DownloadVideoService.
        IntentFilter intentFilter =
            new IntentFilter(DownloadVideoService.ACTION_DOWNLOAD_SERVICE_RESPONSE);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

        // Register the BroadcastReceiver.
        LocalBroadcastManager.getInstance(this)
               .registerReceiver(mDownloadResultReceiver,
                                 intentFilter);
    }
    
	/*
	 * The Broadcast Receiver that registers itself to receive result
     * from DownloadVideoService.
     */
    private class DownloadResultReceiver 
            extends BroadcastReceiver {
        /**
         * Hook method that's dispatched when the DownloadService has
         * downloaded the Video.
         */
        @Override
        public void onReceive(Context context,
                              Intent intent) {
            // Starts an AsyncTask to get fresh Video list from the
            // Video Service.
//            getOps().getVideoList();
        	playButton.setEnabled(true);
        	downloadButton.setEnabled(false);
        	bMap = ThumbnailUtils.createVideoThumbnail(dataUrl, MediaStore.Video.Thumbnails.MINI_KIND);
    		imageView.setImageBitmap(bMap);
        }
    }
}
