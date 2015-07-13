package vandy.mooc.view;

import vandy.mooc.R;
import vandy.mooc.common.GenericActivity;
import vandy.mooc.presenter.VideoOps;
import vandy.mooc.utils.Constants;
import vandy.mooc.view.ui.VideoAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayActivity extends GenericActivity<VideoOps.View, VideoOps> 
						implements VideoOps.View{

	private RatingBar ratingBar;
	private TextView txtAvgRatingValue;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		setContentView(R.layout.video_play_activity);
		
		Button downloadButton = (Button) findViewById(R.id.download);
		
		final Bundle bundle = getIntent().getExtras();
		
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
		
		Button playButton = (Button) findViewById(R.id.play);
		
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
		
		ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
	 
				// TODO
				txtAvgRatingValue.setText(String.valueOf(rating));
			}
		});
		
		
		
		
		Button btnSubmit = (Button) findViewById(R.id.btnSubmit);
		 
		//if click on me, then display the current rating value.
		btnSubmit.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
	 
				Toast.makeText(VideoPlayActivity.this,
					String.valueOf(ratingBar.getRating()),
						Toast.LENGTH_SHORT).show();
	 
			}
	 
		});
		
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
}
