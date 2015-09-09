package features;

import ij.process.ByteProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.util.Arrays;

/**
 * Class that implements diverse methods to find edges on images. 
 * 
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez<a/>
 * @version 0.0.1
 */
public class Edge {

    /** The <code>ImageProcessor</code> to apply the differents functions */
    private ImageProcessor ip;

    /** The standar deviation in the Gaussian function */
    private float sigma;

    /** Give the necesary image derivatives for calculus  */
    private ImageDerivative derivative;


    /**
     * Empty contructor
     */
    public Edge() {
        this.derivative = new ImageDerivative();
    }


    /**
     * This constructor sets the base image to obtain the gradient, also sets the
     * value of the standard deviation of the Gaussian funtion.
     *
     * @param ip a <code>FloatImageProcessor</code>
     * @param sigma The standar deviation in the Gaussian function
     */
    public Edge(ImageProcessor ip, float sigma) {
        this.ip = ip;
        this.sigma = sigma;
        this.derivative = new ImageDerivative(ip, sigma);
    }


    /**
     * Gives the corresponding magnitude of the gradient on the base image,
     * computed using discrete derivatives of a Gaussian function.
     * Gradient is given by: |G| = (Gx^2 + Gy^2)^(1/2)
     */
    public ImageProcessor gradientMagnitude() {
        float[] dX = derivative.getDX();
        float[] dY = derivative.getDY();
        float[] magnitude = new float[dX.length];

        for (int i = 0; i < magnitude.length; i++) {
            magnitude[i] = (float) Math.sqrt(dX[i] * dX[i] + dY[i] * dY[i]);
        }

        ImageProcessor output = new FloatProcessor(ip.getWidth(), ip.getHeight());
        output.setPixels(magnitude);
        return output;
    }


    /**
     * Gives a <code>float[]</code> corresponding to the direction of the gradient of,
     * the base image. The base image remains unchanged.
     *
     * @return a new <code>FloatProcessor</code> corresponding to  magnitude of the gradient
     * of image.
     */
    public float[] gradientDirection() {
        float[] dX = derivative.getDX();
        float[] dY = derivative.getDY();
        float[] direction = new float[ip.getWidth() * ip.getHeight()];

        for (int i = 0; i < direction.length; i++) {
            direction[i] = (float) Math.atan2(dY[i], dX[i]);
        }

        return direction;
    }


    /**
     * Calculate the inverse of scaled magnitude of the gradient of the image.
     * The inverse scaled magnitude is given by: 1 - (G / max{G})
     */
    public ImageProcessor inverseScaledGradientMagnitude() {
        float[] dX = derivative.getDX();
        float[] dY = derivative.getDY();
        float[] magnitude = new float[dX.length];

        float max = Float.MIN_VALUE;
        for (int i = 0; i < magnitude.length; i++) {
            magnitude[i] = (float) Math.sqrt(dX[i] * dX[i] + dY[i] * dY[i]);
            max = (magnitude[i] > max) ? magnitude[i] : max;
        }

        float[] inverse = new float[magnitude.length];

        for (int i = 0; i < inverse.length; i++) {
            inverse[i] = 1.0f - (magnitude[i] / max);
        }

        ImageProcessor output = new FloatProcessor(ip.getWidth(), ip.getHeight());
        output.setPixels(inverse);

        return output;
    }


    /**
     * Appling a laplacian filter, to an <code>ImageProcessor</code> for a given scale sigma.
     * The secon order image derivative is aproximated by convolving the imagen with
     * a kernel obtainded from the second derivative of the Gaussian function
     */
    public ImageProcessor laplacian() {
        float[] dXX = derivative.getDXX();
        float[] dYY = derivative.getDYY();
        float[] laplacian = new float[dXX.length];

        for (int i = 0; i < laplacian.length; i++) {
            laplacian[i] = dXX[i] + dYY[i];
        }

        ImageProcessor output = new FloatProcessor(ip.getWidth(), ip.getHeight());
        output.setPixels(laplacian);
        return output;
    }


    /**
     * Generate a new <code>ByteProcessor</code> that is the  zero-crossings in of a
     * <code>FloatProcessor</code> thats is the Laplacian of the original image.
     * Laplacian image produces very few, if any, actual zero valued pixels.
     * Rather, a zero-crossing is represented by two neighboring pixels with opposite sign. Of the
     * two pixels, the one that is closest to zero is chosen to represent the zero-crossing. Thus,
     * a pixel in the new image is 0 for Laplacian image pixels that are either zero or closer
     * to zero than any neighbor with an opposite sign; otherwise, the pixel is 255.
     *
     * @return A new <code>ByteProcessor</code> thas is the zero crossings of the
     * Laplacian of the origianl image.
     */
    public ImageProcessor zeroCrossings() {
        // compute laplacian
        ImageProcessor laplacian = this.laplacian();
        float[] pixels = (float[]) laplacian.getPixels();

        byte[] crossings = new byte[ip.getWidth() * ip.getHeight()];
        Arrays.fill(crossings, (byte) 255);
        int width = ip.getWidth();

        for (int i = 0; i < pixels.length; i++) {
            float current = pixels[i];

            // the neighbours
            int[] neighbours = {i - width, i + 1, i + width, i - 1};

            // check the four neighbours for zero crossings
            for (int j = 0; j < neighbours.length; j++) {
                int neighbour = neighbours[j];
                if (neighbour >= 0 && neighbour < pixels.length) {
                    float neighbourValue = pixels[neighbour];
                    float sign = current * neighbourValue;
                    if (sign < 0)
                        if (Math.abs(current) < Math.abs(neighbourValue)) {
                            crossings[i] = (byte) 0;
                            break;
                        }
                }
            }
        }

        ByteProcessor zc = new ByteProcessor(ip.getWidth(), ip.getHeight());
        zc.setPixels(crossings);

        return zc;
    }


    /* getter an setter methds */
    /**
     * @return the ip
     */
    public ImageProcessor getImageProcessor() {
        return ip;
    }


    /**
     * @param ip the ip to set
     */
    public void setImageProcessor(ImageProcessor ip) {
        this.ip = ip;
        this.derivative.setImageProcessor(ip);
    }


    /**
     * @return the sigma
     */
    public float getSigma() {
        return sigma;
    }


    /**
     * @param sigma the sigma to set
     */
    public void setSigma(float sigma) {
        this.sigma = sigma;
        this.derivative.setSigma(sigma);
    }


    /**
     * @return the derivative
     */
    public ImageDerivative getDerivative() {
        return derivative;
    }

}
