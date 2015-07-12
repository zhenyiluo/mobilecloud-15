package vandy.mooc.view;

import vandy.mooc.R;
import vandy.mooc.utils.Constants;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VideoPlayActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.video_play_activity);
		
		Button playButton = (Button) findViewById(R.id.play);
		
		playButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dataUrl = getIntent().getStringExtra(Constants.DATA_URL);
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				String type = "video/mp4";
				intent.setDataAndType(Uri.parse(dataUrl), type);
				
				startActivity(intent);
			}
		});
		
		Button downloadButton = (Button) findViewById(R.id.download);
	}
}
