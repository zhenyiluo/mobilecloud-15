package vandy.mooc.view;

import vandy.mooc.R;
import vandy.mooc.common.GenericActivity;
import vandy.mooc.presenter.VideoOps;
import vandy.mooc.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoPlayActivity extends GenericActivity<VideoOps.View, VideoOps> {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.video_play_activity);
		
		Button downloadButton = (Button) findViewById(R.id.download);
		
		final Bundle bundle = getIntent().getExtras();
		
		boolean fileExists = bundle.getBoolean(Constants.FILE_EXISTS);
		
		downloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Long id = bundle.getLong(Constants.VIDEO_ID);
				
				getOps().downloadVideo(id);
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
	}
}
