package exeption;

public class WrongPasswordException extends LoginException{

	public WrongPasswordException(String e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
