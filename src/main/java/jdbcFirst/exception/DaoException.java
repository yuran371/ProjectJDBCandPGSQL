package jdbcFirst.exception;

/*
 * Data Access Object (DAO)
 * 003 DAO. Операции DELETE и INSERT
 */

public class DaoException extends RuntimeException{
	public DaoException (Throwable e) {
		super(e);
	}
}
