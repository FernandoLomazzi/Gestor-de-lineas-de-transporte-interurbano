package db.dao;

import java.sql.SQLException;

public interface Dao<T> {
	public Boolean addData(T t) throws SQLException;
	public Boolean modifyData(T t);
	public Boolean deleteData(T t);
}
