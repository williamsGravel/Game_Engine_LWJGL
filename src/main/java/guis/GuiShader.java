package guis;

import org.lwjgl.util.vector.Matrix4f;
import shaders.ShaderProgram;

import java.io.IOException;

public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/java/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/main/java/guis/guiFragmentShader.txt";

    private int location_transformationMatrix;

    public GuiShader() throws IOException {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    protected void getAllUniformLocation() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    protected void bindAttributes() {
        super.bindAttribute(0,"position");
    }
}
