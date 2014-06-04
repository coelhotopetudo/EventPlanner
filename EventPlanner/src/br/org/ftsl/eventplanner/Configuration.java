package br.org.ftsl.eventplanner;

import java.util.List;

import br.org.ftsl.eventplanner.R;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.org.ftsl.common.Utils;
import br.org.ftsl.eventplanner.db.Config;
import br.org.ftsl.eventplanner.db.EventHelper;
import br.org.ftsl.eventplanner.db.Lecture;
import br.org.ftsl.eventplanner.db.Room;
import br.org.ftsl.ws.WSFunctions;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class Configuration extends Activity {

	TextView urlServer;
	private Spinner spinner1;
	private Spinner spinner2;
	
	static final String SERVERNAME = "ServerName";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		urlServer = (TextView)findViewById(R.id.txtUrlServer);
		
		EventHelper ev = new EventHelper(getBaseContext());
		Config c = ev.getConfig(SERVERNAME);
		urlServer.setText(c.getValue());
		
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        Configuration.this.loadLectureData(null);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});
		
		loadEventData(ev);
		
		
	}
	
	void loadLectureData(EventHelper e){
		
		EventHelper ev = e;
		if(ev == null)
			ev = new EventHelper(getBaseContext());
		
		String lecture = spinner1.getSelectedItem().toString();
		
		List<String> list = ev.getLecturesByRoom(lecture);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,list);
		
		dataAdapter.setDropDownViewResource
        (android.R.layout.simple_spinner_dropdown_item);
         
		spinner2.setAdapter(dataAdapter);
		
	}

	
	
	void loadEventData(EventHelper e){
		
		EventHelper ev = e;
		if(ev == null)
			ev = new EventHelper(getBaseContext());
		List<String> list = ev.getAllRoomsNames();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,list);
		
		dataAdapter.setDropDownViewResource
        (android.R.layout.simple_spinner_dropdown_item);
         
		spinner1.setAdapter(dataAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
			View rootView = inflater.inflate(R.layout.fragment_config,
					container, false);
			return rootView;
		}
	}
	public void getData(View view) {
		
		EventHelper ev = new EventHelper(this);
		Config c = ev.getConfig(SERVERNAME);
		if(c.getValue() == ""){
			Utils.showMessage("Erro", "Cadastre o servidor de mensagens", this);
			return;
		}
		WSFunctions ws = new WSFunctions();
		if(ws.getDocFromUrl("http://"+urlServer.getText()+"/ftsl/dados.xml") != null){
			insertRooms(ws.doc);
			Utils.showMessage("Sucesso", "Dados atualizados com sucesso", this);
			loadEventData(null);
		}
		
	}
	
	public void setServer(View view){
		EventHelper ev = new EventHelper(this);
		ev.setConfig(new Config(SERVERNAME, ""+urlServer.getText()));
	}
	
	private void insertRooms(Document doc){
		EventHelper ev = new EventHelper(this);
		ev.deleteAllRoom();
		ev.deleteAllLecture();
		if(doc == null){
			Utils.showMessage("Erro", "Não foi possivel carregar o arquivo", this);
			return;
		}
		NodeList nodeList = doc.getElementsByTagName("room");
		for(int i =0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			String id = ((Element)node).getAttribute("id");
			String name = ((Element)node).getAttribute("name");
			ev.addRoom(new Room(name));
			insertLecture(node, ev, name);
		}
	}
	
	private void insertLecture(Node n, EventHelper ev, String room){
		NodeList nodeList = ((Element)n).getElementsByTagName("lecture");
		for(int i =0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			String id = ((Element)node).getAttribute("id");
			String title = ((Element)node).getAttribute("title");
			String date = ((Element)node).getAttribute("date");
			ev.addLecture(new Lecture(Integer.parseInt(id), title, date, room));
		}
	}

}
