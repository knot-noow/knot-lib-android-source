-- Create drink fountain Application data base
CREATE TABLE IF NOT EXISTS drink_fountain (
    _id          		    INTEGER         PRIMARY KEY AUTOINCREMENT,
    uuid         		    VARCHAR( 200 )  NOT NULL,
    token       		    VARCHAR( 200 )  NOT NULL,
    positionX	    		INTEGER DEFAULT 0,
    positionY          		INTEGER DEFAULT 0,
    description             VARCHAR( 200 ) NOT NULL
);

CREATE TABLE IF NOT EXISTS water_level_data(
    _id                     INTEGER PRIMARY KEY AUTOINCREMENT,
    water_fountain_uuid     VARCHAR( 200 ) NOT NULL,
    current_value           REAL DEFAULT 0.0,
    time_stamp              VARCHAR( 200 ) NOT NULL
);

CREATE TABLE IF NOT EXISTS white_lists_config (
    _id                     INTEGER PRIMARY KEY AUTOINCREMENT,
    water_fountain_uuid     VARCHAR( 200 ) NOT NULL,
    uuid_can_send           VARCHAR( 200 ) NOT NULL,
    uuid_can_receiver       VARCHAR( 200 ) NOT NULL,
    uuid_can_discover       VARCHAR( 200 ) NOT NULL
);

