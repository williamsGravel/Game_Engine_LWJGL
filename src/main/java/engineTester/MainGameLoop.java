package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.TextureModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffer;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main (String[] args) throws IOException {

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy3"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));




        RawModel model = OBJLoader.loadObjModel("pine", loader);

        ModelTexture pineModel = new ModelTexture(loader.loadTexture("pine"));
        TextureModel staticModel = new TextureModel(model, pineModel);
        TextureModel grass = new TextureModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);

        ModelTexture fernTextureAtlas =new ModelTexture(loader.loadTexture("fernAtlas"));
        fernTextureAtlas.setNumberOfRows(2);
        TextureModel fern = new TextureModel(OBJLoader.loadObjModel("fern", loader), fernTextureAtlas);
        fern.getTexture().setHasTransparency(true);
        TextureModel lamp = new TextureModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));




        ModelTexture texture = staticModel.getTexture();
        texture.setReflectivity(0.5f);

        Entity entity = new Entity(staticModel, new Vector3f(0, 0,-25),0,0,0,1);


        Light sun = new Light(new Vector3f(0,100,7000),new Vector3f(0.4f,0.4f,0.4f));
        List<Light> lights = new ArrayList<Light>();
        lights.add(sun);
        lights.add(new Light(new Vector3f(185,10,-293),new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f( 370,17,-300), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f( 293,7,-305), new Vector3f(2,2,0), new Vector3f(1, 0.01f, 0.002f)));

        Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap, "heightmap");
        Terrain terrain1 = new Terrain(-1,-1,loader, texturePack, blendMap, "heightmap");


        MasterRenderer masterRenderer = new MasterRenderer(loader);

        List<Entity> entities = new ArrayList<Entity>();
        entities.add(new Entity(lamp, new Vector3f(185, -4.7f, -293),0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300),0,0,0,1));
        entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305),0,0,0,1));


        List<Entity> normalMapEntities = new ArrayList<Entity>();
        TextureModel barrelModel = new TextureModel(NormalMappedObjLoader.loadOBJ("barrel", loader),
                new ModelTexture(loader.loadTexture("barrel")));
        barrelModel.getTexture().setNormalMap(loader.loadTexture("barrelNormal"));
        barrelModel.getTexture().setShineDamper(10);
        barrelModel.getTexture().setReflectivity(0.5f);
        normalMapEntities .add(new Entity(barrelModel, new Vector3f(75,3, -75),0,0,0,1f));

        Random random = new Random();
        GenerateRandom generateRandom = new GenerateRandom(random);

        float x = 0,z =0;
        x = generateRandom.generateRandomX();
        z = generateRandom.generateRandomZ();
        Entity tree = new Entity(staticModel, new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0,0,0,1);

        for(int i = 0; i<500; i++){
            x = generateRandom.generateRandomX();
            z = generateRandom.generateRandomZ();
            if (x>=0) {
                entities.add(new Entity(staticModel, new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, 0.5f));
            }else {
                entities.add(new Entity(staticModel, new Vector3f(x, terrain1.getHeightOfTerrain(x, z), z), 0, 0, 0, 0.5f));
            }
            x = generateRandom.generateRandomX();
            z = generateRandom.generateRandomZ();
            if (x>=0) {
                entities.add(new Entity(fern, new Vector3f(x, terrain.getHeightOfTerrain(x, z), z), 0, 0, 0, 0.5f));
            }else {
                entities.add(new Entity(fern, new Vector3f(x, terrain1.getHeightOfTerrain(x, z), z), 0, 0, 0, 0.5f));
            }
        }
        RawModel bunnyModel = OBJLoader.loadObjModel("person",loader);
        TextureModel stanfordBunny = new TextureModel(bunnyModel, new ModelTexture(loader.loadTexture("playerTexture")));
        Player player = new Player(stanfordBunny,new Vector3f(70,terrain.getHeightOfTerrain(70,-70),-70), 0,0,0,0.5f);
        Camera camera = new Camera(player);

        List<GuiTexture> guis = new ArrayList<GuiTexture>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("health"),new Vector2f(-0.75f, -0.9f), new Vector2f(0.25f,0.35f));
        guis.add(gui);

        List<Terrain> terrains = new ArrayList<Terrain>();
        terrains.add(terrain);
        terrains.add(terrain1);

        GuiRenderer guiRenderer = new GuiRenderer(loader);

        MousePicker mousePicker = new MousePicker(camera,
                masterRenderer.getProjectionMatrix(), terrain);


        WaterFrameBuffer fbos = new WaterFrameBuffer();
        WaterShader waterShader = new WaterShader();
        WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader,
                masterRenderer.getProjectionMatrix(), fbos);
        WaterTile waterTile = new WaterTile(75, -75, 0);
        List<WaterTile> waterTiles = new ArrayList<WaterTile>();
        waterTiles.add(waterTile);



        while (!Display.isCloseRequested()){
            if(player.getPosition().x >= 0){
                player.move(terrains.get(0));
            }else {
                player.move(terrains.get(1));
            }
            camera.move();
            mousePicker.update();

            GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

            fbos.bindReflectionFrameBuffer();
            float distance = 2 * (camera.getPosition().y - waterTile.getHeight());
            camera.getPosition().y -= distance;
            camera.invertPitch();
            masterRenderer.renderScene(entities, terrains,lights, normalMapEntities,camera,
                    new Vector4f(0, 1, 0, -waterTile.getHeight() + 1f));
            camera.getPosition().y += distance;
            camera.invertPitch();

            fbos.bindRefractionFrameBuffer();
            masterRenderer.renderScene(entities, terrains,lights, normalMapEntities,camera,
                    new Vector4f(0, -1, 0, waterTile.getHeight()));

            masterRenderer.processEntity(player);

            fbos.unbindCurrentFrameBuffer();
            GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
            masterRenderer.renderScene(entities,terrains,lights, normalMapEntities,camera,
                    new Vector4f(0, -1, 0, 15));
            waterRenderer.render(waterTiles, camera, sun);
            guiRenderer.render(guis);

            DisplayManager.updateDisplay();
        }

        fbos.cleanUp();
        waterShader.cleanUp();
        guiRenderer.cleanUp();
        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}

/*      GenerateRandomTerrain generateRandomTerrain = new GenerateRandomTerrain();
        List<Entity> threeMaster = generateRandomTerrain.generateThings(entity, 100);
        List<Entity> fernMaster = generateRandomTerrain.generateThings(new Entity(new TextureModel
                (OBJLoader.loadObjModel("fern",loader),new ModelTexture(loader.loadTexture("fern"))),
                new Vector3f(0,0,0), 0,0,0,1),100);
        List<Entity> grassMaster = generateRandomTerrain.generateThings(new Entity(new TextureModel
                (OBJLoader.loadObjModel("grassModel",loader),new ModelTexture(loader.loadTexture("grassTexture"))),
                new Vector3f(0,0,0), 0,0,0,1),100);
            for (Entity arbre:threeMaster)
            {
                masterRenderer.processEntity(arbre);
            }
            for(Entity fern:fernMaster)
            {
                masterRenderer.processEntity(fern);
            }
            for(Entity grass:grassMaster)
            {
                masterRenderer.processEntity(grass);
            }*/