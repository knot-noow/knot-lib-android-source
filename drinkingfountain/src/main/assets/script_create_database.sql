-- Create drink fountain Application data base
CREATE TABLE IF NOT EXISTS tb_drink_fountain (
    _id          		    INTEGER         PRIMARY KEY AUTOINCREMENT,
    uuid         		    VARCHAR( 200 )  NOT NULL,
    token       		    VARCHAR( 200 ),
    position_x	    		INTEGER DEFAULT 0,
    position_y          	INTEGER DEFAULT 0,
    description             VARCHAR( 200 ),
    floor                 	INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS tb_water_level_data(
    _id                     INTEGER PRIMARY KEY AUTOINCREMENT,
    drink_fountain_uuid     VARCHAR( 200 ) NOT NULL,
    current_value           REAL DEFAULT 0.0,
    time_stamp              VARCHAR( 200 ) NOT NULL
);