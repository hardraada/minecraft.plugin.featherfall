package com.hardraada.minecraft.plugin.featherfall;

import net.canarymod.database.Column;
import net.canarymod.database.Column.ColumnType;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;

public class FeatherFallPlayer extends DataAccess {
	@Column( columnName = "player_name", columnType = ColumnType.PRIMARY, dataType = DataType.STRING )
	public String playerName;
	
	@Column( columnName = "value", columnType = ColumnType.NORMAL, dataType = DataType.BOOLEAN )
	public boolean value;
	
	public FeatherFallPlayer( ) {
		super( "feather_fall_player" );
	}
	
	@Override
	public DataAccess getInstance( ) {
		return new FeatherFallPlayer( );
	}
	
}
