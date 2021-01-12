package water;

import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;
import toolbox.Maths;
import entities.Camera;

import java.io.IOException;

public class WaterShader extends ShaderProgram {

    private final static String VERTEX_FILE = "src/main/java/water/waterVertex.txt";
    private final static String FRAGMENT_FILE = "src/main/java/water/waterFragment.txt";

    private int location_modelMatrix;
    private int location_viewMatrix;
    private int location_projectionMatrix;
    private int location_reflectionTexture;
    private int location_refractionTexture;
    private int location_dudvMap;
    private int location_moveFactor;
    private int location_cameraPosition;
    private int location_normalMap;
    private int location_lightColour;
    private int location_lightPosition;
    private int location_depthMap;

    public WaterShader() throws IOException {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    protected void getAllUniformLocation() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_reflectionTexture = super.getUniformLocation("reflectionTexture");
        location_refractionTexture = super.getUniformLocation("refractionTexture");
        location_dudvMap = super.getUniformLocation("dudvMap");
        location_moveFactor = super.getUniformLocation("moveFactor");
        location_cameraPosition = super.getUniformLocation("cameraPosition");
        location_normalMap = super.getUniformLocation("normalMap");
        location_lightColour = super.getUniformLocation("lightColour");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_depthMap = super.getUniformLocation("depthMap");
    }

    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void loadMoveFactor(float factor){
        super.loadFloat(location_moveFactor, factor);
    }

    public void connectTextureUnits(){
        super.loadInt(location_reflectionTexture, 0);
        super.loadInt(location_refractionTexture, 1);
        super.loadInt(location_dudvMap, 2);
        super.loadInt(location_normalMap, 3);
        super.loadInt(location_depthMap, 4);
    }

    public void loadLight(Light sun){
        super.load3DVector(location_lightPosition, sun.getPosition());
        super.load3DVector(location_lightColour, sun.getColour());
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        loadMatrix(location_viewMatrix, viewMatrix);
        super.load3DVector(location_cameraPosition, camera.getPosition());
    }

    public void loadModelMatrix(Matrix4f modelMatrix){
        loadMatrix(location_modelMatrix, modelMatrix);
    }

}