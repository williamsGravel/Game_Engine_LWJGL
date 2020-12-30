package engineTester;

import entities.Entity;
import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateRandomTerrain {

    public GenerateRandomTerrain() {

    }

    public List<Entity> generateThings(Entity entity, int iteration){
        List<Entity> allCube = new ArrayList<Entity>();
        Random random = new Random();

        for(int i = 0; i < iteration; i++){
            float x = random.nextFloat() * 100 -50;
            float y = 0;
            float z = random.nextFloat() * -300;
            allCube.add(new Entity(entity.getModel(), new Vector3f(x,y,z),0f,0f,0f,1f));

        }

        return allCube;
    }
}
