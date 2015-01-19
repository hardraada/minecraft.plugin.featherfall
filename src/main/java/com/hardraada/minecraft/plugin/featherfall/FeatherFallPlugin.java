package com.hardraada.minecraft.plugin.featherfall;

import java.util.HashMap;
import java.util.Map;
import net.canarymod.Canary;
import net.canarymod.api.DamageType;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.database.Database;
import net.canarymod.database.exceptions.DatabaseReadException;
import net.canarymod.database.exceptions.DatabaseWriteException;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.plugin.PluginListener;
import com.hardraada.minecraft.commons.PluginBase;

public class FeatherFallPlugin extends PluginBase implements PluginListener {
	@Override
	public boolean enable( ) {
		Canary.hooks( ).registerListener( this, this );
		return super.enable( );
	}

	@Command(
		aliases = { "featherfall" }, 
		description = "Avoid taking damage on falls.", 
		permissions = { "featherfall.admin" }, 
		min = 3, 
		toolTip = "/featherfall [player] [on|off]"
	)
	public void enableCommand( MessageReceiver caller, String[ ] args ) {
		if( !( caller instanceof Player ) ) return;
		Player player = ( Player )caller;
		
		if( args == null || args.length < 3 ) { this.usage( player ); return; }
		String playerName = args[ 1 ];
		String action = args[ 2 ].toLowerCase( );

		Map<String, Object> search = new HashMap<String, Object>( );
		search.put( "player_name", playerName );
		
		FeatherFallPlayer data = new FeatherFallPlayer( );
		data.playerName = playerName;
		data.value = ( "on".equals( action ) );
		
		try {
			Database.get( ).update( data, search );
		} catch( DatabaseWriteException e ) {
			FeatherFallPlugin.logger.error( e );
			FeatherFallPlugin.logger.info( "An error occurred saving the record for " + playerName + ":  " + e.getMessage( ) );
		}
	}

	@HookHandler
	public void onPlayerDamage( DamageHook event ) {		
		Entity entity = event.getDefender( );
		if( !( entity instanceof Player ) ) return;
		
		Player player = ( Player )entity;
		FeatherFallPlayer data = new FeatherFallPlayer( );
		Map<String, Object> search = new HashMap<String, Object>( );
		search.put(  "player_name", player.getName( ) );
		
		try {
			Database.get( ).load( data, search );
		} catch( DatabaseReadException e ) {
			FeatherFallPlugin.logger.error( e );
			FeatherFallPlugin.logger.info( "An error occurred looking up the record for " + player.getName( ) + ":  " + e.getMessage( ) );
			return;
		}
		
		if( data == null || data.value == false ) return;
		
		if( event.getDamageSource( ).getDamagetype( ) == DamageType.FALL ) event.setCanceled( );
	}
	
	private void usage( Player player ) {
		player.chat( "FeatherFall Usage:" );
		player.chat( "--------------------" );
		player.chat( "/featherfall [player] [on|off]" );
	}
}
