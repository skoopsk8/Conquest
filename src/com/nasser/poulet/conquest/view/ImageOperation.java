package com.nasser.poulet.conquest.view;

/**
 * Created by Thomas on 4/30/14.
 */
public class ImageOperation {
    static public float[][] multiply(float[][] img1, float[][] img2){
        // Just check the image size for no errors
        if(img1.length == img2.length && img1[0].length == img2[0].length){
            int width, height;
            width = img1.length;
            height = img1[0].length;

            float[][] returnImg = new float[width][height];

            for(int i=0; i<width; i++){
                for (int j=0; j<height; j++){
                    returnImg[i][j] = img1[i][j] * img2[i][j];
                }
            }

            return returnImg;
        }
        return null;
    }

    static public float[][] threshold (float[][] img1, float threshold){
        int width, height;
        width = img1.length;
        height = img1[0].length;

        float[][] returnImg = new float[width][height];

        for(int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                if(img1[i][j]>threshold)
                    returnImg[i][j] = 1;
                else
                    returnImg[i][j] = 0;
            }
        }

        return returnImg;
    }

    static public float[][] decimate (float[][] img1, int width, int height){
        return img1;
    }
}
