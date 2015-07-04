package com.rusca8.primes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2D;

public class Primes extends Game {
	
	public Pantalla pantalla;
	
	@Override
	public void create () {
		Box2D.init();
		Recursos.cargar();
		
		pantalla = new Pantalla (this);
		setScreen(pantalla);
	}

}
