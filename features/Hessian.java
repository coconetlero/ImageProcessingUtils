package features;

import ij.process.ImageProcessor;

/**
 *
 * @author <a ref ="zianfanti@gmail.com"> Zian Fanti Gutierrez<a/>
 * @version 0.0.1-1
 */
public class Hessian {

    private ImageProcessor ip;

    private float sigma;

    private float[] Ixx;

    private float[] Ixy;

    private float[] Iyy;


    public Hessian(ImageProcessor ip, float sigma) {
        this.ip = ip;
        this.sigma = sigma;
    }

    /**
     * 
     * @param ip
     * @param sigma
     * @param process 
     */
    public Hessian(ImageProcessor ip, float sigma, boolean process) {
        this(ip, sigma);
        if (process) {
            ImageDerivative derivative = new ImageDerivative(ip, sigma);
            this.Ixx = (float[]) derivative.dXX().getPixels();
            this.Ixy = (float[]) derivative.dXY().getPixels();
            this.Iyy = (float[]) derivative.dYY().getPixels();
        }
    }


    /**
     * Calculate Ixx, Iyy, Ixy elements of the Hessian Matrix
     */
    public void composeHessianMatrix() {
        ImageDerivative derivative = new ImageDerivative(ip, sigma);
        this.Ixx = (float[]) derivative.dXX().getPixels();
        this.Ixy = (float[]) derivative.dXY().getPixels();
        this.Iyy = (float[]) derivative.dYY().getPixels();
    }


    /**
     * Compute the smallest eigenvalue of the current 2 x 2 Hessian matrix
     *
     * @return the eigenvalue result of
     * <code>L2 = 1/ 2 * ((dxx + dyy) - SQRT((dxx - dyy)^2 + 4 dxy^2))</code>
     */
    public float[] smallEigenvalue() {
        double alpha = 0;
        double trace = 0;
        float[] smallEigenvalue = new float[ip.getWidth() * ip.getHeight()];

        for (int i = 0; i < smallEigenvalue.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            smallEigenvalue[i] = (float) ((trace - alpha) * 0.5);
        }
        return smallEigenvalue;
    }


    /**
     * Compute the largest  eigenvalue of the current 2 x 2 Hessian matrix
     *
     * @return the eigenvalue result of
     * <code>L1 = 1/2 ((dxx + dyy) + SQRT((dxx - dyy)^2 + 4 dxy^2))</code>
     */
    public float[] largeEigenvalue() {
        double alpha = 0;
        double trace = 0;

        float[] largeEigenvalue = new float[ip.getWidth() * ip.getHeight()];

        for (int i = 0; i < largeEigenvalue.length; i++) {
            trace = Ixx[i] + Iyy[i];
            alpha = Math.sqrt(Math.pow(Ixx[i] - Iyy[i], 2) + (4 * (Ixy[i] * Ixy[i])));

            largeEigenvalue[i] = (float) ((trace + alpha) * 0.5);
        }
        return largeEigenvalue;
    }


    /**
     * Set sigma value for gaussian derivative
     *
     * @param sigma
     */
    public void setSigma(float sigma) {
        this.sigma = sigma;
    }

}
