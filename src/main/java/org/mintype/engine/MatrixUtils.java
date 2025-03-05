package org.mintype.engine;
public class MatrixUtils {

    public static float[] createIdentityMatrix() {
        return new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };
    }

    public static float[] multiplyMatrices(float[] a, float[] b) {
        float[] result = new float[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i * 4 + j] = a[i * 4 + 0] * b[0 * 4 + j] +
                        a[i * 4 + 1] * b[1 * 4 + j] +
                        a[i * 4 + 2] * b[2 * 4 + j] +
                        a[i * 4 + 3] * b[3 * 4 + j];
            }
        }
        return result;
    }

    public static float[] createViewMatrix(float[] position, float[] rotation) {
        // Create view matrix based on position and rotation (assuming look-at logic for view matrix)
        // We'll keep it simple and assume no rotation here, only translation
        return new float[]{
                1.0f, 0.0f, 0.0f, -position[0],
                0.0f, 1.0f, 0.0f, -position[1],
                0.0f, 0.0f, 1.0f, -position[2],
                0.0f, 0.0f, 0.0f, 1.0f
        };
    }

    public static float[] createProjectionMatrix(float fov, float aspectRatio, float nearPlane, float farPlane) {
        float tanHalfFov = (float) Math.tan(Math.toRadians(fov) / 2.0);
        return new float[]{
                1.0f / (aspectRatio * tanHalfFov), 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f / tanHalfFov, 0.0f, 0.0f,
                0.0f, 0.0f, -(farPlane + nearPlane) / (farPlane - nearPlane), -1.0f,
                0.0f, 0.0f, -(2.0f * farPlane * nearPlane) / (farPlane - nearPlane), 0.0f
        };
    }

    public static float[] createRotationYMatrix(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);

        return new float[]{
                cos, 0.0f, sin, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -sin, 0.0f, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };
    }

}
