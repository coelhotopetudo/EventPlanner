package br.org.ftsl.eventplanner.db;

public class Room {
	 
    private int id;
    
	private String name;
 
    public Room(){}
 
    public Room(String name) {
        super();
        this.name = name;
    }
 
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
 
    @Override
    public String toString() {
        return "Room [id=" + id + ", Name=" + name + "]";
    }
}
