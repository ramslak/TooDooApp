package com.codepath.simpletodo;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by rams on 19/02/17.
 */

@Database(name = TODODatabase.NAME, version = TODODatabase.VERSION)

public class TODODatabase {

        public static final String NAME = "TODODatabase";

        public static final int VERSION = 1;

}
