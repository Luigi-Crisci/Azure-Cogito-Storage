package exeption;

public class BlobNotFoundExeption extends Exception {

	private static final long serialVersionUID = 1L;
	
	public BlobNotFoundExeption(String message) {
		super(message);
	}

}
