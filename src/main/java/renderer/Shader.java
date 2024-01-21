package renderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    // Constructor nhận đường dẫn đến file shader
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            // Đọc nội dung shader từ file
            String source = new String(Files.readAllBytes(Paths.get(filepath)));

            // Phân tách mã nguồn shader thành vertex và fragment shaders
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Lấy loại shader và mã nguồn tương ứng
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            // Xác định vertex và fragment shader dựa trên loại
            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("unexpected token" + firstPattern);
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("unexpected token" + secondPattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "error: could not open file for shader:" + filepath;
        }

        // In ra mã nguồn của vertex và fragment shader để kiểm tra
        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }

    // Phương thức biên dịch shader
    public void compile() {
        int vertexID, fragmentID;

        // Tạo và biên dịch vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Kiểm tra lỗi khi biên dịch vertex shader
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : " ";
        }

        // Tạo và biên dịch fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Kiểm tra lỗi khi biên dịch fragment shader
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : " ";
        }

        // Tạo và liên kết shader program
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Kiểm tra lỗi khi liên kết shader program
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: Linking of shaders failed.");
            System.out.println(glGetShaderInfoLog(shaderProgramID, len));
            assert false : " ";
        }
    }

    // Phương thức sử dụng shader program
    public void use() {
        glUseProgram(shaderProgramID);
    }

    // Phương thức huỷ kết nối shader program
    public void detach() {
        glUseProgram(0);
    }
}
