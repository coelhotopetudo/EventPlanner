package br.org.ftsl.eventplanner.db;

public class Room {
	 
    private int _id;
	private String name;
 
    public Room(){}
    public Room(int id, String name) {
        super();
        this._id = id;
        this.name = name;
    }
    
    public Room(String name) {
        super();
        this.name = name;
    }
 
    public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
    @Override
    public String toString() {
        return "Room [id=" + _id + ", Name=" + name + "]";
    }
}
