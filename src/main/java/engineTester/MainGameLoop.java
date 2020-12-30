package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    public static void main (String[] args) throws IOException {

        DisplayManager.createDisplay();
        Loader loader = new Loader();


        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy2"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFlowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));




        RawModel model = OBJLoader.loadObjModel("tree", loader);


        TextureModel staticModel = new TextureModel(model, new ModelTexture(loader.loadTexture("tree")));
        TextureModel grass = new TextureModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture")));
        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        TextureModel fern = new TextureModel(OBJLoader.loadObjModel("fern", loader),
                new ModelTexture(loader.loadTexture("fern")));
        fern.getTexture().setHasTransparency(true);



        ModelTexture texture = staticModel.getTexture();
        texture.setReflectivity(0.5f);

        Entity entity = new Entity(staticModel, new Vector3f(0, 0,-25),0,0,0,1);
        Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,-1,loader, texturePack, blendMap);
        Terrain terrain2 = new Terrain(-1,-1,loader,texturePack, blendMap);

        Camera camera = new Camera();
        MasterRenderer masterRenderer = new MasterRenderer();

        List<Entity> entities = new ArrayList<Entity>();
        Random random = new Random();
        for(int i = 0; i<500; i++){
            entities.add(new Entity(staticModel,new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),0,0,0,3));
            entities.add(new Entity(grass,new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),0,0,0,1));
            entities.add(new Entity(fern,new Vector3f(random.nextFloat() * 800 - 400, 0, random.nextFloat() * -600),0,0,0,0.6f));
        }

        while (!Display.isCloseRequested()){
            entity.increaseRotation(0,0,0);
            camera.move();

            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);

            for(Entity some:entities){
                masterRenderer.processEntity(some);
            }

            masterRenderer.render(light,camera);
            DisplayManager.updateDisplay();
        }

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