package de.uulm.par.notes;

/**
 * @author Fabian Schwab
 *
 */
public enum NoteType {
	LOCATION,DATETIME,PERSON,SIMPLE;
	
	/**
	 * @param x
	 * @return
	 */
	public static NoteType fromInteger(int x) {
        switch(x) {
        case 0:
            return LOCATION;
        case 1:
            return DATETIME;
        case 2: 
        	return PERSON;
        case 3:
        	return SIMPLE;
        }
        return SIMPLE;
    }
}
