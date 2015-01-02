package com.hardraada.minecraft.plugin.featherfall;

import net.canarymod.Canary;
import net.canarymod.api.DamageType;
import net.canarymod.api.entity.Entity;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.hook.HookHandler;
import net.canarymod.hook.entity.DamageHook;
import net.canarymod.plugin.PluginListener;
import com.hardraada.minecraft.commons.PluginBase;

public class FeatherFallPlugin extends PluginBase implements PluginListener {
	private static boolean on = false;
	
	@Override
	public boolean enable( ) {
		Canary.hooks( ).registerListener( this, this );
		return super.enable( );
	}

	@Command(
		aliases = { "featherfall" }, 
		description = "Avoid taking damage on falls.", 
		permissions = { "" }, 
		min = 2, 
		toolTip = "/featherfall on|off"
	)
	public void enableCommand( MessageReceiver caller, String[ ] args ) {
		if( !( caller instanceof Player ) ) return;
		Player player = ( Player )caller;
		
		if( args == null || args.length < 2 ) { this.usage( player ); return; }
		String action = args[ 1 ].toLowerCase( );
		
		if( "on".equals( action ) ) {
			FeatherFallPlugin.on = true;
		} else if( "off".equals( action ) ) {
			FeatherFallPlugin.on = false;
		} else {
			this.usage( player );
		}
	}

	@HookHandler
	public void onPlayerDamage( DamageHook event ) {
		if( !FeatherFallPlugin.on ) return;
		
		Entity entity = event.getDefender( );
		if( !( entity instanceof Player ) ) return;
		
		if( event.getDamageSource( ).getDamagetype( ) == DamageType.FALL ) event.setCanceled( );
	}
	
	private void usage( Player player ) {
		player.chat( "FeatherFall Usage:" );
		player.chat( "--------------------" );
		player.chat( "/featherfall on|off" );
	}
}
