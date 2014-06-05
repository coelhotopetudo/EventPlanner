package br.org.ftsl.eventplanner;

import java.util.ArrayList;
import java.util.Arrays;

import br.org.ftsl.eventplanner.db.Config;
import br.org.ftsl.eventplanner.db.EventHelper;
import br.org.ftsl.eventplanner.db.Lecture;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class LectureControl extends Activity {
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture_control);
		
		EventHelper ev = new EventHelper(getBaseContext());
		Config lecture_default = ev.getConfig(Configuration.DEFAULT_LECTURE);
		Lecture lecture = ev.getLecture(Integer.parseInt(lecture_default.getValue()));
		
		ListView listview =(ListView) findViewById(R.id.listView1);	
		TextView title =(TextView) findViewById(R.id.textView1);
		title.setText(lecture.getTitle());
		
			
		ArrayList<String> planetList = ev.getAttendeesByLecture(Integer.parseInt(lecture_default.getValue()));  
	    
	    listAdapter = new ArrayAdapter<String>(this, R.layout.confirm_lecture, planetList); 
	    
	    listview.setAdapter( listAdapter );   
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lecture_control, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			return null;
		}
	}

}
