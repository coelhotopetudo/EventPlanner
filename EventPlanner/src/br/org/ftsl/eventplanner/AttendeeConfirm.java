package br.org.ftsl.eventplanner;

import java.util.List;

import br.org.ftsl.eventplanner.R;

import br.org.ftsl.eventplanner.db.Attendee;
import br.org.ftsl.eventplanner.db.EventHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AttendeeConfirm extends Activity {
	
	private static final int ACTIVITY_RESULT_QR_DRDROID = 0;
	
	TextView report;
	TextView rool;
	Button scan;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendee_confirm);
		
		report = (TextView) findViewById(R.id.textView14);
		scan = (Button) findViewById(R.id.btSend);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ateendee_confirm, menu);
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
			View rootView = inflater.inflate(
					R.layout.fragment_ateendee_confirm, container, false);
			return rootView;
		}
	}

	//START HERE
	

	public void viewAttendee(View view){
		EventHelper db = new EventHelper(this);
 
        List<Attendee> list = db.getAllAttendees();
        String nada = "";
        
        for (Attendee attendee : list) {
			nada += attendee.getMail()+"\n";	
		}

		report.setText(nada);
		
	}
	
	public void scan(View view) {
	    // Do something in response to button
	
		Intent i = new Intent("la.droid.qr.scan");
		
		try {
			
			startActivityForResult(i, ACTIVITY_RESULT_QR_DRDROID);
		} 
		catch (ActivityNotFoundException activity) {
			
			AttendeeConfirm.qrDroidRequired(AttendeeConfirm.this);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if( ACTIVITY_RESULT_QR_DRDROID == requestCode 
				&& data != null && data.getExtras() != null ) {
			
			String result = data.getExtras().getString("la.droid.qr.result");
			
			String[] qrResults = result.split(";");
			if(qrResults.length != 3){
				new AlertDialog.Builder(this)
			    .setTitle("QR Inválido")
			    .setMessage("Formato Inválido do QR code")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        }
			     })
			    .show();
				return;
			}
				
			
			EventHelper db = new EventHelper(this);
			Attendee a = db.getAttendeeByMail(qrResults[2]);
			if(a.getId() > 0)
				report.setText(a.getName());
				
			else{
				a.setName(qrResults[1]);
				report.setText("Não localizado:"+result.split("|")[2]);
			}
			testResult(a);
		}
	}
	
	
	private void testResult(Attendee attendee){
		if(attendee.getId() < 1){
			new AlertDialog.Builder(this)
		    .setTitle("Não Localizado")
		    .setMessage("Participante "+attendee.getName()+" não cadastrado")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        }
		     })
		    .show();
		}else{
			new AlertDialog.Builder(this)
		    .setTitle("Cadastrar Presença")
		    .setMessage("Confirma a presença do participante "+attendee.getName()+"?")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		        	
		        }
		     })
		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // do nothing
		        }
		     })
		    .show();
		}
		
	}
	
	protected static void qrDroidRequired(final AttendeeConfirm activity) {
		
		AlertDialog.Builder AlertBox = new AlertDialog.Builder(activity);
		
		AlertBox.setMessage("QRDroid Missing");
		
		AlertBox.create().show();
	}
	
	
	
	
}
