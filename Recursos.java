package com.rusca8.primes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Recursos {

	public static Texture tex_foto;
	public static Sprite foto;
	
	public static void cargar(){ //seria recomendable hacer algo para que no lo cargue todo más de una vez ("Singleton")

		tex_foto = new Texture(Gdx.files.internal("fotos/memeserra.jpg"));
		tex_foto.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		foto = new Sprite(tex_foto);
		
	}
}
