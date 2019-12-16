package myapp;

import com.azure.storage.blob.models.BlobItem;

public class BlobItemKeyStruct {

	private BlobItem item;
	private String key;
	private boolean dir;
	private String trueName;
	
	public BlobItemKeyStruct(BlobItem item, String key, boolean dir, String trueName) {
		super();
		this.item = item;
		this.key = key;
		this.dir = dir;
		this.trueName = trueName;
	}
	
	
	@Override
	public String toString() {
		return "BlobItemKeyStruct [item=" + item + ", key=" + key + ", dir=" + dir + ", trueName=" + trueName + "]";
	}


	public BlobItem getItem() {
		return item;
	}
	public void setItem(BlobItem item) {
		this.item = item;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public boolean isDir() {
		return dir;
	}
	public void setDir(boolean dir) {
		this.dir = dir;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	
	
	
}
