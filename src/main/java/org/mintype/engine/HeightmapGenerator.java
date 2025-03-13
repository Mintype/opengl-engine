package org.mintype.engine;

import javax.swing.*;
import java.util.Random;

public class HeightmapGenerator {

    private static final int WIDTH = 16;
    private static final int HEIGHT = 16;
    private static final float SCALE = 0.01f;
    private static final int OCTAVES = 4;
    private static final float PERSISTENCE = 0.5f;

    public static void main(String[] args) {
        int[][] heightmap = generateHeightmap(WIDTH, HEIGHT, SCALE, OCTAVES, PERSISTENCE);
        printHeightmap(heightmap);
    }

    public static int[][] generateHeightmap(int width, int height, float scale, int octaves, float persistence) {
        int[][] heightmap = new int[width][height];
        PerlinNoise perlinNoise = new PerlinNoise();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float nx = x * scale;
                float ny = y * scale;
                float noiseValue = 0;
                float amplitude = 1;
                float frequency = 1;
                float maxValue = 0;

                for (int i = 0; i < octaves; i++) {
                    noiseValue += perlinNoise.noise(nx * frequency, ny * frequency) * amplitude;
                    maxValue += amplitude;
                    amplitude *= persistence;
                    frequency *= 2;
                }

                noiseValue /= maxValue;
                heightmap[x][y] = Math.round(noiseValue * 100.0f);
            }
        }

        return heightmap;
    }

    public static void printHeightmap(int[][] heightmap) {
        for (int y = 0; y < heightmap.length; y++) {
            for (int x = 0; x < heightmap[y].length; x++) {
                System.out.printf("%6d ", heightmap[x][y]);
            }
            System.out.println();
        }
    }

    static class PerlinNoise {
        private int[] permutation;
        private Random random;

        public PerlinNoise() {
            this(System.currentTimeMillis());
        }

        public PerlinNoise(long seed) {
            random = new Random(seed);
            permutation = new int[512];
            for (int i = 0; i < 256; i++) {
                permutation[i] = i;
            }
            for (int i = 0; i < 256; i++) {
                int j = random.nextInt(256);
                int temp = permutation[i];
                permutation[i] = permutation[j];
                permutation[j] = temp;
            }
            for (int i = 0; i < 256; i++) {
                permutation[256 + i] = permutation[i];
            }
        }

        public float noise(float x, float y) {
            int X = (int) Math.floor(x) & 255;
            int Y = (int) Math.floor(y) & 255;
            x -= Math.floor(x);
            y -= Math.floor(y);
            float u = fade(x);
            float v = fade(y);
            int A = permutation[X] + Y;
            int B = permutation[X + 1] + Y;
            return lerp(v, lerp(u, grad(permutation[A], x, y), grad(permutation[B], x - 1, y)),
                    lerp(u, grad(permutation[A + 1], x, y - 1), grad(permutation[B + 1], x - 1, y - 1)));
        }

        private float fade(float t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        private float lerp(float t, float a, float b) {
            return a + t * (b - a);
        }

        private float grad(int hash, float x, float y) {
            int h = hash & 7;
            float u = h < 4 ? x : y;
            float v = h < 4 ? y : x;
            return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }
    }
}