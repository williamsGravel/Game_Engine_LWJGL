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

import java.io.IOException;
import java.util.List;

public class MainGameLoop {

    public static void main (String[] args) throws IOException {

        DisplayManager.createDisplay();

        Loader loader = new Loader();



        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TextureModel staticModel = new TextureModel(model, new ModelTexture(loader.loadTexture("tree")));

        GenerateRandomTerrain generateRandomTerrain = new GenerateRandomTerrain();

        ModelTexture texture = staticModel.getTexture();
        texture.setReflectivity(0.5f);

        Entity entity = new Entity(staticModel, new Vector3f(0, 0,-25),0,0,0,1);
        Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));

        Terrain terrain = new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("grass")));
        Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();
        MasterRenderer masterRenderer = new MasterRenderer();

        List<Entity> threeMaster = generateRandomTerrain.generateThings(entity, 100);

        while (!Display.isCloseRequested()){
            entity.increaseRotation(0,0,0);
            camera.move();

            masterRenderer.processTerrain(terrain);
            masterRenderer.processTerrain(terrain2);
            for (Entity arbre:threeMaster)
            {
                masterRenderer.processEntity(arbre);
            }

            masterRenderer.render(light,camera);
            DisplayManager.updateDisplay();
        }

        masterRenderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}