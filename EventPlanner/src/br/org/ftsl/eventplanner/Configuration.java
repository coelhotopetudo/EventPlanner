package br.org.ftsl.eventplanner;

import java.util.List;

import br.org.ftsl.eventplanner.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.org.ftsl.common.Utils;
import br.org.ftsl.eventplanner.db.Attendee;
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
	static final String DEFAULT_LECTURE = "DefaultLecture";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configuration);
		urlServer = (TextView)findViewById(R.id.txtUrlServer);
		
		EventHelper ev = new EventHelper(getBaseContext());
		Config c = ev.getConfig(SERVERNAME);
		
		String value = c.getValue();
		if (value != null && value.startsWith("http") == false) {
			value = "https://raw.githubusercontent.com/coelhotopetudo/EventPlanner/master/ftsl/dados.xml";
		}
		urlServer.setText(value);
		
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
		
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	EventHelper ev = new EventHelper(Configuration.this);
		    	Lecture l = ev.getLectureByTitle(spinner2.getSelectedItem().toString());
				ev.setConfig(new Config(DEFAULT_LECTURE, ""+l.getId()));
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

	
	public void setServer(View view){
		EventHelper ev = new EventHelper(this);
		ev.setConfig(new Config(SERVERNAME, ""+urlServer.getText()));
	}
	
	public void getData(View view) {
		
		EventHelper ev = new EventHelper(this);
		Config c = ev.getConfig(SERVERNAME);
		if(c.getValue() == ""){
			Utils.showMessage("Erro", "Cadastre o servidor de mensagens", this);
			return;
		}
		WSFunctions ws = new WSFunctions();
		
		String url = null;
		String digitado = urlServer.getText().toString();
		if (digitado.startsWith("http") == false) {
			url = "http://"+digitado+"/ftsl/dados.xml";
		} else {
			if ("".equals(digitado)) {
				url = digitado;
			} else {
				url = "https://raw.githubusercontent.com/coelhotopetudo/EventPlanner/master/ftsl/dados.xml";
			}
		}
		
		Document docFromUrl = ws.getDocFromUrl(url);
		if(docFromUrl != null){
			insertAttendee(ws.doc);
			insertRooms(ws.doc);
			Utils.showMessage("Sucesso", "Dados atualizados com sucesso", this);
			loadEventData(null);
		}
		
	}
	
	private void insertAttendee(Document doc){
		EventHelper ev = new EventHelper(this);
		ev.deleteAllAttendee();
		if(doc == null){
			Utils.showMessage("Erro", "Não foi possivel carregar o arquivo", this);
			return;
		}
		NodeList nodeList = doc.getElementsByTagName("attendees");
		if(nodeList.getLength() < 1){
			Utils.showMessage("Erro", "Nenhum participante para o evento", this);
			return;
		}
		
		Node nodeAtt = nodeList.item(0);
		
		NodeList nodeListAtt = ((Element)nodeAtt).getElementsByTagName("mbr");
		
		for(int i =0; i < nodeListAtt.getLength(); i++){
			Node node = nodeListAtt.item(i);
			String id = ((Element)node).getAttribute("mbr_id");
			String name = ((Element)node).getAttribute("n");
			String mail = ((Element)node).getAttribute("e");
			ev.addAttendee(new Attendee(Integer.parseInt(id), name, mail));
		}
	}
	
	private void insertRooms(Document doc){
		EventHelper ev = new EventHelper(this);
		ev.deleteAllRoom();
		ev.deleteAllLecture();
		ev.deleteAllAttendeeLecture();
		if(doc == null){
			Utils.showMessage("Erro", "Não foi possivel carregar o arquivo", this);
			return;
		}
		NodeList nodeList = doc.getElementsByTagName("room");
		for(int i =0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			String id = ((Element)node).getAttribute("id");
			String name = ((Element)node).getAttribute("name");
			ev.addRoom(new Room(Integer.parseInt(id), name));
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
			insertAttendeeLecture(Integer.parseInt(id), ev, node);
			
		}
	}
	
	private void insertAttendeeLecture(int lecture, EventHelper ev, Node n){
		NodeList nodeList = ((Element)n).getElementsByTagName("m");
		for(int i =0; i < nodeList.getLength(); i++){
			Node node = nodeList.item(i);
			String id = ((Element)node).getAttribute("id");
			ev.addAttendeeLecture(lecture, Integer.parseInt(id)); 
		}		
	}

}
