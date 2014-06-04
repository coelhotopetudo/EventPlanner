package br.org.ftsl.eventplanner.db;

public class Config {
	 
    private int _id;
	private String key;
	private String value;
	
	public Config(){}
	
    public Config(String key, String value){
    	this.key = key;
    	this.value = value;
    }
     @Override
    public String toString() {
        return "Config [id=" + _id + ", Key=" + key + ", Value="+value+"]";
    }
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}
	
}
