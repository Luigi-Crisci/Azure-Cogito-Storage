package exeption;

public class UserNotFoundException extends LoginException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5866503546780330268L;

	public UserNotFoundException(String e) {
		super(e);
	}

}
