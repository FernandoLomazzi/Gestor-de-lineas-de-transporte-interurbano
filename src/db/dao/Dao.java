package db.dao;

import java.sql.SQLException;

import exceptions.AddFailException;
import exceptions.DBConnectionException;
import exceptions.DeleteFailException;
import exceptions.ModifyFailException;

public interface Dao<T> {
	public void addData(T t) throws DBConnectionException,AddFailException;
	public void modifyData(T t) throws DBConnectionException,ModifyFailException;
	public void deleteData(T t) throws DBConnectionException,DeleteFailException;
}
