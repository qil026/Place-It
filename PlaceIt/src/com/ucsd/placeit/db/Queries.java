package com.ucsd.placeit.db;

public final class Queries {
	static final String CREATE_TABLE_PLACEIT = 
			"CREATE TABLE %s (" +
			"%s INTEGER PRIMARY KEY AUTOINCREMENT, " + //ID
			"%s TEXT, " + 				// Title
			"%s TEXT, " + 				// Desc
			"%s INTEGER, " +			// State
			"%s DOUBLE, " +				// Longitude
			"%s DOUBLE, " +				// Latitude
			"%s DATETIME, " +			// Date created
			"%s DATETIME, " +			// Date posted
			"%s DATETIME, " +			// Date Freq Start
			"%s INTEGER" +				// Frequency
			"%s INTEGER" +				// Type
			"%s TEXT" +					// Cat 1
			"%s TEXT" + 				// Cat 2
			"%s TEXT)";					// Cat 3
			
	static final String SELECT_PLACEIT =
			"SELECT * FROM %s " +
			"WHERE %s = %d";
	
	static final String SELECT_ALL_PLACEIT =
			"SELECT * FROM %s";
	
	static final String SELECT_STATE_CAT_PLACEIT =
			"SELECT * FROM %s" +
			"WHERE %s = '%d'" +
			"AND (" +
			"%s = '%s'" +
			"OR %s = '%s'" + 
			"OR %s = '%s'" +
			")";
			
			
	
	static final String UPDATE_PLACEIT = 
			"UPDATE %s " +
			"SET " +
			"%s = '%s', " +	// Title
			"%s = '%s', " +	// Desc
			"%s = '%s', " +	// State
			"%s = '%s', " +	// Longitude
			"%s = '%s', " +	// Latitude
			"%s = '%s', " +	// Date created
			"%s = '%s', " +	// Date posted
			"%s = '%s', " +	// Date freq start
			"%s = '%s', " +	// Frequency
			"%s = '%s', " +	// Cat 1
			"%s = '%s', " +	// Cat 2
			"%s = '%s' " +	// Cat 3
			"WHERE %s = '%d'"; // ID
	
	static final String CHANGE_STATE_PLACEIT =
			"UPDATE %s " +
			"SET " +
			"%s = '%s'" +
			"WHERE %s = '%d'";
	
	
	private Queries() {
		throw new AssertionError();
	}
}