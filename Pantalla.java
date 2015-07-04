package com.rusca8.primes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Pantalla extends InputAdapter implements Screen {
	
	//cosas básicas
	Primes juego; 
	
	OrthographicCamera camera;
	Viewport viewport;
	SpriteBatch lote;
	ShapeRenderer dibujante;
	
	//box2D
	World world;
	Box2DDebugRenderer debugRenderer;
	float accumulator = 0; //esto lo uso en "render" para avanzar el mundo.
	
	BodyDef bodyDef; //definiciones y variables de los cuerpos.
	BodyDef sueloDef;
	Array<Body> bolas = new Array<Body>();
	Array<Boludo> boludos = new Array<Boludo>(); //objetos asociados a los cuerpos (información que quiero guardar en los cuerpos, vaya).
	Body suelo;

	FixtureDef fixtureDef;
	Array<Fixture> galletas = new Array<Fixture>();
	
	CircleShape circle;
	ChainShape cajaSuelo;
	
	Body clicado;
	Boludo boludo_clicado;
	
	//gestión de eventos
	Vector3 click;
	
	//variables de toda la vida
	int screenW = 4;
	int screenH = 6;
	float mg = 0.1f; //margen de la caja de pelotas
	
	Array<Integer> ies = new Array<Integer>();
	
	
	/***************** CONSTRUCTOR ******************/
	public Pantalla (Primes juego){ //constructor de la pantalla
		this.juego = juego;
		
		lote = new SpriteBatch();
		dibujante = new ShapeRenderer();
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(screenW,screenH,camera);
		viewport.apply(true);
		
		click = new Vector3(0,0,0);
		Gdx.input.setInputProcessor(this);
		
		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();

		bodyDef = new BodyDef(); //definición de base de las bolas
		bodyDef.type = BodyType.DynamicBody;
		fixtureDef = new FixtureDef();
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.4f; 
		fixtureDef.restitution = 0.6f;

		
		sueloDef = new BodyDef(); // SUELO LOCO NAMBER 2
		sueloDef.position.set(0,0);
		
		suelo = world.createBody(sueloDef);
		
		cajaSuelo = new ChainShape(); //una cadena chachi (también se pueden hacer cajas rellenas).
		cajaSuelo.createLoop(new float[] {mg,mg,screenW-mg,mg,screenW-mg,screenH*2,mg,screenH*2});
		
		suelo.createFixture(cajaSuelo,0.0f);
		
		cajaSuelo.dispose();
	}
	
	
	QueryCallback callback = new QueryCallback(){ //esto sirve para lo de la detección de clicks de debajo
		@Override
		public boolean reportFixture(Fixture fixture) {
			if(fixture.testPoint(click.x, click.y)){
				clicado = fixture.getBody();
				return false;
			}else{
				return true;
			}
		}
		
	};

	/*********** CLICK ***********/
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		click.x = screenX;
		click.y = screenY;
		viewport.unproject(click); //IMPORTANTE!!!! Si deshaces la proyección desde la cámara las barras negras se lo cargarán todo. Siempre desde viewport.
		if(button == 1){
			nuevaBola(click.x,click.y,3);
		}else{
			clicado = null; //detección de click sobre un cuerpo.
			world.QueryAABB(callback, click.x - 0.0001f, click.y-0.0001f, click.x+0.0001f, click.y+0.0001f);
			if(clicado == suelo){
				clicado = null;
			}
			if(clicado != null){
				System.out.println();
				System.out.println("ouch!");
				boludo_clicado = (Boludo) clicado.getUserData(); //parece que estas asignaciones de .getUserData no son sólo una copia del objeto, sino que agarran el original y te lo acercan para que lo cambies o lo mires.
				if(!boludo_clicado.porDestruir){
					boludo_clicado.porDestruir = true;
				}else{
					boludo_clicado.porDestruir = false;
				}
			}
		}
		return true;
	}
	
	/************ NUEVA BOLA ************/
	public void nuevaBola(float x, float y, int num){
		
		bodyDef.position.set(x,y); //dónde voy a ponerlo (pin de las coordenadas que va a tener).
		
		bolas.add(world.createBody(bodyDef));

		circle = new CircleShape();
		circle.setRadius(num/10f);
		fixtureDef.shape = circle; 
		
		galletas.add(bolas.get(bolas.size-1).createFixture(fixtureDef)); //Recortamos la galleta y la añadimos al cuerpo.
		
		circle.dispose();
		
		boludos.add(new Boludo(num,x,y));
		bolas.get(bolas.size-1).setUserData(boludos.get(boludos.size-1));
		System.out.println("Hay " + bolas.size + " bolas, " + galletas.size + " galletas y " + boludos.size + " boludos.");
	}
	/********************** RENDER method *********************/
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		camera.update();
		
		lote.setProjectionMatrix(camera.combined);
		dibujante.setProjectionMatrix(camera.combined);
		
		lote.begin();
			lote.draw(Recursos.foto, 0, 0, screenW,screenH);
		lote.end();
		
		dibujante.begin(ShapeType.Filled);
			for(Boludo bol : boludos){
				if(bol.porDestruir){
					dibujante.setColor(1f, 0.2f, 0.2f, 1f);
				}else{
					dibujante.setColor(0.2f, 0f, 0.8f, 1f);
				}
				dibujante.circle(bol.x, bol.y, bol.r, 100);
			}
		dibujante.end();
		
		/**- - - - - - - - - Box2D - - - - - - - -  **/
		doPhysicsStep(delta);
		debugRenderer.render(world, camera.combined);
		
		if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
			System.out.println("Destruyamos Juajajaja");
			int i = 0;
			for(Body b : bolas){ //mira si está marcado para destruir, y si lo está lo destruye y anula el cuerpo y sus datos
				Boludo bol = (Boludo) b.getUserData();
				if(bol != null && bol.porDestruir){
					System.out.println(bol.porDestruir + " at " + i);
					world.destroyBody(b);
					b.setUserData(null);
					b = null;
					ies.insert(0, i); //apunto qué cosas de la lista están anuladas. Empezando por la última.
					System.out.println(ies);
				}
				i++;
			}
			i = 0;

			for(Integer j : ies){ //borro de las listas las cosas anuladas. Como he empezado por la última no me cargo los índices de las siguientes. A que soy listo? :D
				bolas.removeIndex(j);
				galletas.removeIndex(j);
				boludos.removeIndex(j);
				System.out.println("Removed: "+j);
			}
			ies.clear();
		}
		/** - - - de la bola al boludo - - - **/
		for(Body b : bolas){
			Boludo bol = (Boludo) b.getUserData();
			bol.x = b.getPosition().x;
			bol.y = b.getPosition().y;
		}
	}
	/************* RESIZE *************/
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	private void doPhysicsStep(float delta){ //Esto lo he copiado de las recomendaciones. No lo entiendo muy bien, pero parece que hace que vaya igual en Desktop que en el móvil, así que guay.
		float frameTime = Math.min(delta, 0.25f);
		accumulator += frameTime;
		while (accumulator>=1/45f){
			world.step(1/45f, 6, 2);
			accumulator -= 1/45f;
		}
	}
	
	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}
	
	@Override
	public void show() {
		
	}


	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
	}

}
