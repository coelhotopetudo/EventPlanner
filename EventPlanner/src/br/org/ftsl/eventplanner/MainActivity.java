package br.org.ftsl.eventplanner;

import org.ftsl.eventplanner.R;

import br.org.ftsl.eventplanner.db.Attendee;
import br.org.ftsl.eventplanner.db.EventHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView report;
	TextView rool;
	Button scan;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		scan = (Button) findViewById(R.id.btSend);
        
	}
	
	public void save(View view){
		EventHelper db = new EventHelper(this);
		
        db.addAttendee(new Attendee("Fabiano Serpro", "fabiano.kuss@serpro.gov.br"));
		
	}
	
	public void confirmPresence(View view){
		Intent intent = new Intent(this, AttendeeConfirm.class);
		try{
			startActivity(intent);
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	
	public void config(View view) {
		Intent intent = new Intent(this, Configuration.class);
		try{
			startActivity(intent);
		}catch(Exception e){
			e.getMessage();
		}
		
	}	
}
