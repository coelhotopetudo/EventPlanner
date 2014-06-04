package br.org.ftsl.eventplanner.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EventHelper extends SQLiteOpenHelper {

	// Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "EventDB";
 
	
	// Attendees table name
    private static final String TABLE_Attendee = "Attendees";
    private static final String TABLE_Room = "Rooms";
    private static final String TABLE_Lecture = "Lectures";
    private static final String TABLE_Config = "Config";
    private static final String TABLE_Attendee_Lecture = "Attendee_Lecture";
    

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DATE = "date";
    private static final String KEY_ROOM = "room_id";
    private static final String KEY_KEY = "key";
    private static final String KEY_VALUE = "value";
    private static final String KEY_ATTENDEE = "id_attendee";
    private static final String KEY_LECTURE = "id_lecture";

    private static final String[] COLUMNS_ATTENDEE = {KEY_ID,KEY_NAME,KEY_MAIL};
    private static final String[] COLUMNS_ROOM = {KEY_ID,KEY_NAME};
    private static final String[] COLUMNS_LECTURE = {KEY_ID, KEY_TITLE, KEY_DATE, KEY_ROOM};
    private static final String[] COLUMNS_CONFIG = {KEY_ID, KEY_KEY, KEY_VALUE};
    private static final String[] COLUMNS_ATTENDEE_LECTURE = {KEY_ID, KEY_KEY, KEY_VALUE};
	
    
    public EventHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create Attendee table
        String CREATE_Attendee_TABLE = "CREATE TABLE "+TABLE_Attendee+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, "+
                "mail TEXT )";
        
        String CREATE_Room_TABLE = "CREATE TABLE "+TABLE_Room+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT )";

        String CREATE_Room_LECTURE = "CREATE TABLE "+TABLE_Lecture+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT,"+
                "date TEXT, "+
                KEY_ROOM+" TEXT)";

        String CREATE_Config_TABLE = "CREATE TABLE "+TABLE_Config+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "key TEXT,"+
                "value TEXT)";
        
        String CREATE_Attendee_Lecture_TABLE = "CREATE TABLE "+TABLE_Attendee_Lecture+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_ATTENDEE+" INTEGER,"+
                KEY_LECTURE+" INTEGER)";
        
        // create Attendees table
        db.execSQL(CREATE_Attendee_TABLE);
        db.execSQL(CREATE_Room_TABLE);
        db.execSQL(CREATE_Room_LECTURE);
        db.execSQL(CREATE_Config_TABLE);
        db.execSQL(CREATE_Attendee_Lecture_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Attendee+"");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Room+"");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Lecture+"");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Config+"");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_Attendee_Lecture+"");
 
        this.onCreate(db);
    }
    
    public void addAttendee(Attendee attendee){
        

    	SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if(attendee.getId() > 0){
			values.put(KEY_ID, attendee.getId());
		}
		values.put(KEY_NAME, attendee.getName()); 
		values.put(KEY_MAIL, attendee.getMail()); // get author
		
		db.insert(TABLE_Attendee, // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		db.close();
    }
    
    public long setConfig(Config conf){
        
    	SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_KEY, conf.getKey());
		values.put(KEY_VALUE, conf.getValue());
		long ret;
		if(conf.getId() > 0){
			ret = db.update(TABLE_Config, 
	                values,
	                KEY_ID+" = ?", 
	                new String[] { String.valueOf(conf.getId()) }); //selection args		
		}else{
			ret = db.insert(TABLE_Config, 
			        null, 
			        values); 
		}

		db.close();
		
		return ret;
}
    
    public Config getConfig(String key){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = null;
            
        cursor =
                db.query(TABLE_Config, 
                COLUMNS_CONFIG, 
                " key = ?", 
                new String[] { String.valueOf(key) }, 
                null, 
                null, 
                null, 
                null);
   
        Config conf = new Config();
        
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return conf;

        if(cursor.getCount() > 0){
	        conf.setId(Integer.parseInt(cursor.getString(0)));
	        conf.setKey(cursor.getString(1));
	        conf.setValue(cursor.getString(2));
        }
        
        return conf;
    }
    
    public Attendee getAttendee(int id){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Attendee, // a. table
                COLUMNS_ATTENDEE, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
     
     
        if (cursor != null)
            cursor.moveToFirst();
     
        Attendee attendee = new Attendee();
        attendee.setId(Integer.parseInt(cursor.getString(0)));
        attendee.setName(cursor.getString(1));
        attendee.setMail(cursor.getString(2));
     
        return attendee;
    }
    
    
    public Attendee getAttendeeByMail(String mail){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Attendee, // a. table
                COLUMNS_ATTENDEE, 
                " mail = ?", 
                new String[] { mail }, 
                null, 
                null, 
                null, 
                null);
     
        if (cursor != null)
            cursor.moveToFirst();
        
        if(cursor.getCount() < 1)
        	return new Attendee();
        
        Attendee a = new Attendee();
        try{
	        a.setId(cursor.getInt(0));
	        a.setName(cursor.getString(1));
	        a.setMail(cursor.getString(2));
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
        return a;
    }
    
    public List<Attendee> getAllAttendees() {
        List<Attendee> attendees = new LinkedList<Attendee>();
  
        String query = "SELECT  * FROM " + TABLE_Attendee;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Attendee attendee = null;
        if (cursor.moveToFirst()) {
            do {
                attendee = new Attendee();
                attendee.setId(Integer.parseInt(cursor.getString(0)));
                attendee.setName(cursor.getString(1));
                attendee.setMail(cursor.getString(2));
                attendees.add(attendee);
            } while (cursor.moveToNext());
        }

  
        // return Attendees
        return attendees;
    }
    
    public int updateAttendee(Attendee attendee) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put("name", attendee.getName());
        values.put("mail", attendee.getMail()); 
     

        int i = db.update(TABLE_Attendee, 
                values,
                KEY_ID+" = ?", 
                new String[] { String.valueOf(attendee.getId()) }); //selection args
     
        // 4. close
        db.close();
     
        return i;
     
    }


    public void deleteAllAttendee() {
   	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Attendee, //table name
                null,  // selections
                null); //selections args
 
        db.close();
 
    }
    
    public void deleteAttendee(Attendee Attendee) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Attendee, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(Attendee.getId()) }); //selections args
 
        db.close();
 
    }
    
    /*ROOM*/
    public void addRoom(Room room){
        

    	SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, room.getName());
		
		db.insert(TABLE_Room, // table
		        null, //nullColumnHack
		        values); // key/value -> keys = column names/ values = column values
		
		db.close();
}
    
    public Room getRoom(int id){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Room, 
                COLUMNS_ROOM, 
                " id = ?", 
                new String[] { String.valueOf(id) }, // d. selections args
                null, 
                null, 
                null, 
                null);
     
     
        if (cursor != null)
            cursor.moveToFirst();
     
        Room room = new Room();
        room.setId(Integer.parseInt(cursor.getString(0)));
        room.setName(cursor.getString(1));
      
        return room;
    }
    
    
    public Room getRoomByName(String name){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Room, // a. table
                COLUMNS_ROOM, 
                " mail = ?", 
                new String[] {name}, 
                null, 
                null, 
                null, 
                null);
     
        if (cursor != null)
            cursor.moveToFirst();
        
        if(cursor.getCount() < 1)
        	return new Room();
        
        Room a = new Room();
        try{
	        a.setId(cursor.getInt(0));
	        a.setName(cursor.getString(1));
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
        return a;
    }
    
    public List<String> getAllRoomsNames() {
        List<String> rooms = new ArrayList<String>();
  
        String query = "SELECT  * FROM " + TABLE_Room;
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        if (cursor.moveToFirst()) {
            do {
                rooms.add(cursor.getString(1));
  
            } while (cursor.moveToNext());
        }
  
 
        return rooms;
    }
    
    public List<Room> getAllRooms() {
        List<Room> rooms = new LinkedList<Room>();
  
        String query = "SELECT  * FROM " + TABLE_Room;
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Room room = null;
        if (cursor.moveToFirst()) {
            do {
                room= new Room();
                room.setId(Integer.parseInt(cursor.getString(0)));
                room.setName(cursor.getString(1));
  
                rooms.add(room);
            } while (cursor.moveToNext());
        }
  
 
        return rooms;
    }
    
    public int updateRoom(Room room) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put("name", room.getName()); 
     

        int i = db.update(TABLE_Room, 
                values,
                KEY_ID+" = ?", 
                new String[] { String.valueOf(room.getId()) }); //selection args
     
        // 4. close
        db.close();
     
        return i;
     
    }

    public void deleteAllRoom() {
   	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Room, //table name
                null,null); //selections args
 
        db.close();
   	
    }
   
    public void deleteRoom(Room room) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Room, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(room.getId()) }); //selections args
 
        db.close();
 
    }
 

    /*LECTURE*/
    public void addLecture(Lecture lecture){
        

    	SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		if(lecture.getId() > 0)
			values.put(KEY_ID, lecture.getId());
		values.put(KEY_TITLE, lecture.getTitle());
		values.put(KEY_DATE, lecture.getDate());
		values.put(KEY_ROOM, lecture.getRoom());
		
		db.insert(TABLE_Lecture, 
		        null, 
		        values); 
		
		db.close();
    }
    
    public Lecture getLecture(int id){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Lecture, 
                COLUMNS_ROOM, 
                " id = ?", 
                new String[] { String.valueOf(id) }, // d. selections args
                null, 
                null, 
                null, 
                null);
     
     
        if (cursor != null)
            cursor.moveToFirst();
     
        Lecture l = new Lecture();
        l.setId(Integer.parseInt(cursor.getString(0)));
        l.setTitle(cursor.getString(1));
        l.setDate(cursor.getString(2));
        l.setRoom(cursor.getString(3));
      
        return l;
    }
    
    public void deleteAllLecture() {
      	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Lecture, //table name
                null,null); //selections args
 
        db.close();
   	
    }
    
    
    public Lecture getLectureByName(String name){
    	 
        
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor =
                db.query(TABLE_Lecture, // a. table
                COLUMNS_LECTURE, 
                " name = ?", 
                new String[] {name}, 
                null, 
                null, 
                null, 
                null);
     
        if (cursor != null)
            cursor.moveToFirst();
        
        if(cursor.getCount() < 1)
        	return new Lecture();
        
        Lecture l = new Lecture();
        try{
            l.setId(Integer.parseInt(cursor.getString(0)));
            l.setTitle(cursor.getString(1));
            l.setDate(cursor.getString(2));
            l.setRoom(cursor.getString(3));
        }catch(Exception e){
        	System.out.println(e.getMessage());
        }
        return l;
    }
    
    public List<String> getLecturesByRoom(String room) {
        List<String> ls = new LinkedList<String>();
  
        String query = "SELECT  * FROM " + TABLE_Lecture +" where "+KEY_ROOM+"= ?";
  
        SQLiteDatabase db = this.getWritableDatabase();
        try{
        Cursor cursor = db.rawQuery(query, new String[]{room});
  
        Lecture l = null;
        if (cursor.moveToFirst()) {
            do {
                l = new Lecture(cursor.getString(1), cursor.getString(2), cursor.getString(3));
                l.setId(Integer.parseInt(cursor.getString(0)));
  
                ls.add(l.getTitle());
            } while (cursor.moveToNext());
        }
        }catch(Exception e){
        	e.getMessage();
        }
  
 
        return ls;
    }
    
    
    
    public List<Lecture> getAllLectures() {
        List<Lecture> ls = new LinkedList<Lecture>();
  
        String query = "SELECT  * FROM " + TABLE_Lecture;
  
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
  
        Lecture l = null;
        if (cursor.moveToFirst()) {
            do {
                l = new Lecture(cursor.getString(0), cursor.getString(0), cursor.getString(0));
                l.setId(Integer.parseInt(cursor.getString(0)));
  
                ls.add(l);
            } while (cursor.moveToNext());
        }
  
 
        return ls;
    }
    
    public int updateLecture(Room room) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put("name", room.getName()); 
     

        int i = db.update(TABLE_Lecture, 
                values,
                KEY_ID+" = ?", 
                new String[] { String.valueOf(room.getId()) }); //selection args
     
        // 4. close
        db.close();
     
        return i;
     
    }
   
    public void deleteLecture(Room room) {
    	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Lecture, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(room.getId()) }); //selections args
 
        db.close();
 
    }

    /*ATTENDEE LECTURE*/
    public void addAttendeeLecture(int attendee_id, int lecture_id ){
        
    	SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();

		values.put(KEY_LECTURE, lecture_id);
		values.put(KEY_ATTENDEE, lecture_id);
		
		db.insert(TABLE_Attendee_Lecture, 
		        null, 
		        values); 
		
		db.close();
    }

    public void deleteAllAttendeeLecture() {
     	 
        SQLiteDatabase db = this.getWritableDatabase();
 
        db.delete(TABLE_Attendee_Lecture, //table name
                null,null); //selections args
 
        db.close();
   	
    }
    
}
